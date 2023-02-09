package com.ruoyi.activiti.controller.itsm.cmos;

import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.activiti.domain.CmOsRecoveryRegister;
import com.ruoyi.activiti.service.ICmOsRecoveryRegisterService;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.servlet.ModelAndView;

/**
 * 【操作系统恢复登记】Controller
 * 
 * @author ruoyi
 * @date 2021-08-02
 */
@Controller
@RequestMapping("cmos/dj")
public class CmOsRecoveryRegisterController extends BaseController
{

    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CmOsRecoveryRegisterController.class);

    private String prefix = "cmos/dj";

    @Autowired
    private ICmOsRecoveryRegisterService cmOsRecoveryRegisterService;

    //机构
    @Autowired
    private ISysDeptService deptService;

    @Autowired
    ISysDeptService iSysDeptService;

    @GetMapping()
    public String register()
    {
        return prefix + "/cmos";
    }

    /**
     * 查询机构里的公司
     */
    @PostMapping("/selectOrgList")
    @ResponseBody
    public TableDataInfo selectOrgList(OgOrg org)
    {
        startPage();
        List<OgOrg> list = deptService.selectDeptList(org);
        return getDataTable(list);
    }

    /**
     * 查询【操作系统恢复登记】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CmOsRecoveryRegister cmOsRecoveryRegister)
    {
        startPage();
        List<CmOsRecoveryRegister> list = cmOsRecoveryRegisterService.selectCmOsRecoveryRegisterList(cmOsRecoveryRegister);
        return getDataTable(list);
    }

    /**
     * 导出【操作系统恢复登记】列表
     */
    @Log(title = "【操作系统恢复登记】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CmOsRecoveryRegister cmOsRecoveryRegister)
    {
        List<CmOsRecoveryRegister> list = cmOsRecoveryRegisterService.selectCmOsRecoveryRegisterList(cmOsRecoveryRegister);
        ExcelUtil<CmOsRecoveryRegister> util = new ExcelUtil<CmOsRecoveryRegister>(CmOsRecoveryRegister.class);
        return util.exportExcel(list, "操作系统恢复登记");
    }

    @GetMapping("/selectOneApplication")
    public String selectOneApplication() {
        return prefix + "/selectOneApplication";
    }

    /**
     * 新增【操作系统恢复登记】
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        mmap.put("recoveryRegisterId", UUID.getUUIDStr());
        return prefix + "/add";
    }

    /**
     * 新增保存【操作系统恢复登记】
     */
    @Log(title = "【操作系统恢复登记】", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(CmOsRecoveryRegister cmOsRecoveryRegister)
    {
        //UUID
        //cmOsRecoveryRegister.setRecoveryRegisterId(UUID.getUUIDStr());
        if(StringUtils.isNotEmpty(cmOsRecoveryRegister.getBackupDate())){
            String parseStart = DateUtils.handleTimeYYYYMMDD(cmOsRecoveryRegister.getBackupDate());
            cmOsRecoveryRegister.setBackupDate(parseStart);
        }
        if(StringUtils.isNotEmpty(cmOsRecoveryRegister.getRecoveryStartTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(cmOsRecoveryRegister.getRecoveryStartTime());
            cmOsRecoveryRegister.setRecoveryStartTime(parseStart);
        }
        if(StringUtils.isNotEmpty(cmOsRecoveryRegister.getRecoveryEndTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(cmOsRecoveryRegister.getRecoveryEndTime());
            cmOsRecoveryRegister.setRecoveryEndTime(parseStart);
        }
        return toAjax(cmOsRecoveryRegisterService.insertCmOsRecoveryRegister(cmOsRecoveryRegister));
    }

    /**
     * 详情【数据库恢复登记】列表
     */
    @GetMapping("/detail/{recoveryRegisterId}")
    public String detail(@PathVariable("recoveryRegisterId") String recoveryRegisterId, ModelMap mmap)
    {
        CmOsRecoveryRegister cmOsRecoveryRegister = cmOsRecoveryRegisterService.selectCmOsRecoveryRegisterById(recoveryRegisterId);
        if (StringUtils.isNotEmpty(cmOsRecoveryRegister.getOrgId())) {
            String orgId = cmOsRecoveryRegister.getOrgId();
            String org = orgId.substring(0,orgId.length() - 1);
            OgOrg ogOrg = iSysDeptService.selectDeptById(org);
            if(ogOrg!=null){
                List<OgPerson> list = iOgPersonService.selectListByOrgIdAndRoleId(ogOrg.getOrgId(), "666667");
                if(list!=null){
                    mmap.put("pidList", list);
                }
            }
        }
        mmap.put("cmOsRecoveryRegister",cmOsRecoveryRegister);
        return prefix + "/detail";
    }

    /**
     * 选择审批人
     * @return
     */
    @GetMapping("/selectBusinessAudit")
    public ModelAndView selectBusinessAudit(String orgId, String pflag, String rId, ModelMap mmap) {
        ModelAndView model = new ModelAndView(prefix+"/selectPerson");
        model.addObject("orgId",orgId);
        return model;
    }

    /**
     * 修改【操作系统恢复登记】
     */
    @GetMapping("/edit/{recoveryRegisterId}")
    public String edit(@PathVariable("recoveryRegisterId") String recoveryRegisterId, ModelMap mmap)
    {
        CmOsRecoveryRegister cmOsRecoveryRegister = cmOsRecoveryRegisterService.selectCmOsRecoveryRegisterById(recoveryRegisterId);
        if (StringUtils.isNotEmpty(cmOsRecoveryRegister.getOrgId())) {
            String orgId = cmOsRecoveryRegister.getOrgId();
            String org = orgId.substring(0,orgId.length() - 1);
            OgOrg ogOrg = iSysDeptService.selectDeptById(org);
            if(ogOrg!=null){
                List<OgPerson> list = iOgPersonService.selectListByOrgIdAndRoleId(ogOrg.getOrgId(), "666667");
                if(list!=null){
                    mmap.put("pidList", list);
                }
            }
        }
        mmap.put("recoveryRegisterId",recoveryRegisterId);
        mmap.put("cmOsRecoveryRegister", cmOsRecoveryRegister);
        return prefix + "/edit";
    }

    /**
     * 修改保存【操作系统恢复登记】
     */
    @Log(title = "【操作系统恢复登记】", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(CmOsRecoveryRegister cmOsRecoveryRegister)
    {
        if(StringUtils.isNotEmpty(cmOsRecoveryRegister.getBackupDate())){
            String parseStart = DateUtils.handleTimeYYYYMMDD(cmOsRecoveryRegister.getBackupDate());
            cmOsRecoveryRegister.setBackupDate(parseStart);
        }
        if(StringUtils.isNotEmpty(cmOsRecoveryRegister.getRecoveryStartTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(cmOsRecoveryRegister.getRecoveryStartTime());
            cmOsRecoveryRegister.setRecoveryStartTime(parseStart);
        }
        if(StringUtils.isNotEmpty(cmOsRecoveryRegister.getRecoveryEndTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDDHHMMSS(cmOsRecoveryRegister.getRecoveryEndTime());
            cmOsRecoveryRegister.setRecoveryEndTime(parseStart);
        }

        return toAjax(cmOsRecoveryRegisterService.updateCmOsRecoveryRegister(cmOsRecoveryRegister));
    }

    //校验请求的路径 判断对应按钮才能显示修改（状态为：待提交）
    @PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectById(String recoveryRegisterId)
    {
        AjaxResult ajaxResult = new AjaxResult();
        CmOsRecoveryRegister cmOsRecoveryRegister = cmOsRecoveryRegisterService.selectCmOsRecoveryRegisterById(recoveryRegisterId);
        ajaxResult.put("data",cmOsRecoveryRegister);
        return  ajaxResult;
    }

    /**
     * 删除【操作系统恢复登记】
     */
    @Log(title = "【操作系统恢复登记】", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(cmOsRecoveryRegisterService.deleteCmOsRecoveryRegisterByIds(ids));
    }

    @Autowired
    private IOgUserService ogUserService;

    @GetMapping("/selectOgUserByUserId")
    @ResponseBody
    public AjaxResult selectOgUserByUserId(){
        AjaxResult ajaxResult = new AjaxResult();
        String userId = ShiroUtils.getOgUser().getUserId();
        ajaxResult.put("data", ogUserService.selectOgUserByUserId(userId));
        return ajaxResult;
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

    @Autowired
    private IOgPersonService iOgPersonService;

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
