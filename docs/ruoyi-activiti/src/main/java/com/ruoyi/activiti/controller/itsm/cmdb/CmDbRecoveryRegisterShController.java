package com.ruoyi.activiti.controller.itsm.cmdb;

import com.ruoyi.activiti.domain.CmDbRecoveryRegister;
import com.ruoyi.activiti.domain.CmOsRecoveryRegister;
import com.ruoyi.activiti.service.ICmDbRecoveryRegisterService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 【请填写功能名称】Controller
 * 
 * @author ruoyi
 * @date 2021-08-02
 */
@Controller
@RequestMapping("cmdb/sh")
public class CmDbRecoveryRegisterShController extends BaseController
{
    private String prefix = "cmdb/sh";

    @Autowired
    private ICmDbRecoveryRegisterService cmDbRecoveryRegisterService;

    @Autowired
    ISysDeptService iSysDeptService;

    @Autowired
    private IOgPersonService iOgPersonService;

    //机构
    @Autowired
    private ISysDeptService deptService;

    @GetMapping()
    public String register()
    {
        return prefix + "/cmdb";
    }

    /**
     * 查询【数据库恢复登记表】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CmDbRecoveryRegister cmDbRecoveryRegister)
    {
        startPage();
        //获取待审核状态
        cmDbRecoveryRegister.setLineStart("3");
        //获取当前登录人id
        String pId = ShiroUtils.getOgUser().getpId();
        //审核页面查询
        cmDbRecoveryRegister.setpId(pId);
        List<CmDbRecoveryRegister> list = cmDbRecoveryRegisterService.selectCmDbRecoveryRegisterList(cmDbRecoveryRegister);
        return getDataTable(list);
    }

    //校验请求的路径 判断对应按钮才能显示修改（状态为：待提交）
    @PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectById(String recoveryRegisterId)
    {
        AjaxResult ajaxResult = new AjaxResult();
        CmDbRecoveryRegister cmDbRecoveryRegister = cmDbRecoveryRegisterService.selectCmDbRecoveryRegisterById(recoveryRegisterId);
        ajaxResult.put("data",cmDbRecoveryRegister);
        return  ajaxResult;
    }

    /**
     * 修改【数据库恢复登记表】
     */
    @GetMapping("/edit/{recoveryRegisterId}")
    public String edit(@PathVariable("recoveryRegisterId") String recoveryRegisterId, ModelMap mmap)
    {
        CmDbRecoveryRegister cmDbRecoveryRegister = cmDbRecoveryRegisterService.selectCmDbRecoveryRegisterById(recoveryRegisterId);
        if (StringUtils.isNotEmpty(cmDbRecoveryRegister.getOrgId())) {
            String orgId = cmDbRecoveryRegister.getOrgId();
            //String org = orgId.substring(0,orgId.length() - 1);
            OgOrg ogOrg = iSysDeptService.selectDeptById(orgId);
            if(ogOrg!=null){
                List<OgPerson> list = iOgPersonService.selectListByOrgIdAndRoleId(ogOrg.getOrgId(), "666668");
                if(list!=null){
                    mmap.put("pidList", list);
                }
            }
        }
        mmap.put("cmDbRecoveryRegister", cmDbRecoveryRegister);
        return prefix + "/edit";
    }

    /**
     * 修改保存【数据库恢复登记表】
     */
    @Log(title = "【数据库恢复登记】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(CmDbRecoveryRegister cmDbRecoveryRegister)
    {
        if(StringUtils.isNotEmpty(cmDbRecoveryRegister.getBackupDate())){
            String parseStart = DateUtils.handleTimeYYYYMMDD(cmDbRecoveryRegister.getBackupDate());
            cmDbRecoveryRegister.setBackupDate(parseStart);
        }
        if(StringUtils.isNotEmpty(cmDbRecoveryRegister.getRecoveryStartTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(cmDbRecoveryRegister.getRecoveryStartTime());
            cmDbRecoveryRegister.setRecoveryStartTime(parseStart);
        }
        if(StringUtils.isNotEmpty(cmDbRecoveryRegister.getRecoveryEndTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(cmDbRecoveryRegister.getRecoveryEndTime());
            cmDbRecoveryRegister.setRecoveryEndTime(parseStart);
        }
        return toAjax(cmDbRecoveryRegisterService.updateCmDbRecoveryRegister(cmDbRecoveryRegister));
    }

    /**
     * 选择审批部门树
     *
     * @param deptId    部门ID
     * @param excludeId 排除ID
     */
    @GetMapping(value = {"/selectCheckDeptTree/{deptId}", "/selectCheckDeptTree/{deptId}/{excludeId}"})
    public String selectCheckDeptTree(@PathVariable("deptId") String deptId,
                                      @PathVariable(value = "excludeId", required = false) String excludeId, ModelMap mmap) {
        mmap.put("dept", deptService.selectDeptById(deptId));
        mmap.put("excludeId", excludeId);
        return prefix + "/checkOrgTree";
    }

    /**
     * 加载审批部门列表树
     */
    @GetMapping("/treeDataCheck")
    @ResponseBody
    public List<Ztree> treeDataCheck() {

        //当前用户的人员ID
        String pId = ShiroUtils.getOgUser().getpId();
        //获取人员信息
        OgPerson ogPerson = iOgPersonService.selectOgPersonById(pId);
        //获取机构ID
        String orgId = ogPerson.getOrgId();
        //当前登陆人的机构信息
        OgOrg ogOrg = deptService.selectDeptById(orgId);
        //如果当前用户的机构为一级机构
        OgOrg showOrg = getOneLvOrTwoLv(ogOrg);
        OgOrg org = new OgOrg();
        org.setLevelCode(showOrg.getLevelCode());
        List<OgOrg> list = new ArrayList<>();
        list.addAll(deptService.selectDeptList(org));
        List<Ztree> ztrees = initZtree(list);
        return ztrees;
    }

    /**
     * 获取当前登陆人的二级或者是一级机构
     *
     * @param ogOrg
     * @return
     */
    private OgOrg getOneLvOrTwoLv(OgOrg ogOrg) {
        //1.当前登陆用户的机构就是一级或者是二级机构
        if ("1".equals(ogOrg.getOrgLv()) || "2".equals(ogOrg.getOrgLv())) {
            return ogOrg;
        } else {
            String levelCode = ogOrg.getLevelCode();
            String[] split = levelCode.split("/");
            String twoLevelCode = split[2];
            return deptService.selectDeptByCode(twoLevelCode);
        }

    }

    /**
     * 对象转部门树
     *
     * @param deptList 部门列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<OgOrg> deptList) {
        return initZtree(deptList, null);
    }

    private final String SUCCESS = "1";// 启用状态

    /**
     * 对象转部门树
     *
     * @param deptList     部门列表
     * @param roleDeptList 角色已存在菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<OgOrg> deptList, List<String> roleDeptList) {

        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(roleDeptList);
        for (OgOrg dept : deptList) {
            if (SUCCESS.equals(dept.getInvalidationMark())) {
                Ztree ztree = new Ztree();
                ztree.setId(dept.getOrgId());
                ztree.setpId(dept.getParentId());
                ztree.setName(dept.getOrgName());
                ztree.setTitle(dept.getOrgName());
                if (isCheck) {
                    ztree.setChecked(roleDeptList.contains(dept.getOrgId() + dept.getOrgName()));
                }
                ztrees.add(ztree);
            }
        }
        return ztrees;
    }
}
