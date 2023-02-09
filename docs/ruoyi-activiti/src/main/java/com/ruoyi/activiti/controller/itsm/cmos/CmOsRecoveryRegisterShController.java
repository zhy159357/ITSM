package com.ruoyi.activiti.controller.itsm.cmos;

import com.ruoyi.activiti.domain.CmOsRecoveryRegister;
import com.ruoyi.activiti.service.ICmOsRecoveryRegisterService;
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
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 【操作系统恢复登记】Controller
 * 
 * @author ruoyi
 * @date 2021-08-02
 */
@Controller
@RequestMapping("cmos/sh")
public class CmOsRecoveryRegisterShController extends BaseController
{

    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CmOsRecoveryRegisterShController.class);

    private String prefix = "cmos/sh";

    @Autowired
    private ICmOsRecoveryRegisterService cmOsRecoveryRegisterService;

    //机构
    @Autowired
    private ISysDeptService deptService;

    @Autowired
    IOgPersonService iOgPersonService;

    @Autowired
    ISysDeptService iSysDeptService;

    @GetMapping()
    public String register()
    {
        return prefix + "/cmos";
    }

    /**
     * 查询【操作系统恢复登记】列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CmOsRecoveryRegister cmOsRecoveryRegister)
    {
        startPage();
        //获取待审核状态
        cmOsRecoveryRegister.setLineStart("3");
        //获取当前登录人id
        String pId = ShiroUtils.getOgUser().getpId();
        //审核页面查询
        cmOsRecoveryRegister.setpId(pId);
        List<CmOsRecoveryRegister> list = cmOsRecoveryRegisterService.selectCmOsRecoveryRegisterList(cmOsRecoveryRegister);
        return getDataTable(list);
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
     * 审核【操作系统恢复登记】
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
        mmap.put("cmOsRecoveryRegister", cmOsRecoveryRegister);
        mmap.put("recoveryRegisterId",recoveryRegisterId);
        return prefix + "/edit";
    }

    /**
     * 审核保存【操作系统恢复登记】
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

    /**
     * 公司部门和是否第三方维保厂商选择部门树
     *
     * @param deptId    部门ID
     * @param excludeId 排除ID
     */
    @GetMapping(value = {"/selectDeptTree/{deptId}", "/selectDeptTree/{deptId}/{excludeId}"})
    public String selectDeptTree(@PathVariable("deptId") String deptId,
                                 @PathVariable(value = "excludeId", required = false) String excludeId, ModelMap mmap) {
        mmap.put("dept", deptService.selectDeptById(deptId));
        mmap.put("excludeId", excludeId);
        return prefix + "/tree";
    }

    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData() {
        List<Ztree> ztrees = deptService.selectDeptListByDJ();
        return ztrees;
    }
}
