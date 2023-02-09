package com.ruoyi.activiti.controller.itsm.serapp;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.uuid.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.activiti.domain.SysReq;
import com.ruoyi.activiti.service.ISysReqService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysApplicationManagerService;
/**
 * 服务请求-应用系统申请Controller
 * （暂不投入使用）
 * @author jintong
 *
 */
@Controller
@RequestMapping("/sysReq")
public class SysReqController extends BaseController {
    private String prefix = "sysReq";

    @Autowired
    private ISysReqService sysReqService;
    @Autowired
    private IOgUserService ogUserService;
    @Autowired
    private ISysApplicationManagerService sysApplicationManagerService;

    @GetMapping()
    public String sysReq(ModelMap mmap) {
        return prefix + "/sysReq";
    }
    /**
     * 应用系统申请查询页面 表格数据
     * @param sysReq
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysReq sysReq) {
        startPage();
        List<SysReq> list = sysReqService.selectSysReqList(sysReq);
        return getDataTable(list);
    }
    /**
     * 应用系统申请发起页面 表格数据
     * @param sysReq
     * @return
     */
    @PostMapping("/listReq")
    @ResponseBody
    public TableDataInfo listReq(SysReq sysReq) {     	
        startPage();
        sysReq.setStatus(1);
        sysReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        List<SysReq> list = sysReqService.selectSysReqList(sysReq);
        return getDataTable(list);
    }
    /**
     * 应用系统申请审核页面 表格数据
     * @param sysReq
     * @return
     */
    @PostMapping("/listAudit")
    @ResponseBody
    public TableDataInfo listAudit(SysReq sysReq) {     	
        startPage();
        sysReq.setStatus(2);
        sysReq.setAuditor(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        List<SysReq> list = sysReqService.selectSysReqList(sysReq);
        return getDataTable(list);
    }
    /**
     * 应用系统申请处理页面 表格数据
     * @param sysReq
     * @return
     */
    @PostMapping("/listDeal")
    @ResponseBody
    public TableDataInfo listDeal(SysReq sysReq) {     	
        startPage();
        sysReq.setStatus(3);
        sysReq.setDealer(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        List<SysReq> list = sysReqService.selectSysReqList(sysReq);
        return getDataTable(list);
    }
    /**
     * 添加应用系统申请
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }
    /**
     * 添加应用系统申请
     * @param sysReq
     * @return
     */
    @Log(title = "应用系统申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysReq sysReq) {
        sysReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        sysReq.setStatus(1);
        sysReq.setId(UUID.getUUIDStr());
        return toAjax(sysReqService.insertSysReq(sysReq));
    }
    /**
     * 修改应用系统申请
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        SysReq sysReq = sysReqService.selectSysReqById(id);
        mmap.put("sysReq", sysReq);
        return prefix + "/edit";
    }
    /**
     * 暂存应用系统申请
     * @param sysReq
     * @return
     */
    @Log(title = "应用系统申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysReq sysReq) {
        sysReq.setStatus(1);
        sysReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        return toAjax(sysReqService.updateSysReq(sysReq));
    }
    /**
     * 提交应用系统申请
     * @param sysReq
     * @return
     */
    @Log(title = "应用系统申请", businessType = BusinessType.UPDATE)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxResult editSubmit(SysReq sysReq) {
        sysReq.setStatus(2);
        String id = sysReq.getId();
        if (id!=null && !"".equals(id) && !"null".equals(id)) {
        	sysReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
    		return toAjax(sysReqService.updateSysReq(sysReq));
    	}else{
    		sysReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
    		sysReq.setId(UUID.getUUIDStr());
    		return toAjax(sysReqService.insertSysReq(sysReq));
    	}
    }

    /**
     * 删除
     */
    @Log(title = "应用系统申请", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sysReqService.deleteSysReqByIds(ids));
    }
    /**
     * 查看详情
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        SysReq sysReq = sysReqService.selectSysReqById(id);
        mmap.put("sysReq", sysReq);
        return prefix + "/detail";
    }
    /**
     * 导出
     * @param sysReq
     * @return
     */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysReq sysReq) {
        List<SysReq> list = sysReqService.selectSysReqList(sysReq);
        ExcelUtil<SysReq> util = new ExcelUtil<>(SysReq.class);
        return util.exportExcel(list, "sysReq");
    }
    /**
     * 跳转至应用系统申请审核页面
     * @return
     */
    @GetMapping("/toAudit")
    public String toAudit() {
        return prefix + "/sysReqAudit";
    }
    /**
     * 查看要审核的申请
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/audit/{id}")
    public String audit(@PathVariable("id") String id, ModelMap mmap) {
        SysReq sysReq = sysReqService.selectSysReqById(id);
        mmap.put("sysReq", sysReq);
        return prefix + "/audit";
    }
    /**
     * 应用系统申请审核
     * @param sysReq
     * @return
     */
    @Log(title = "应用系统申请", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    @ResponseBody
    public AjaxResult auditSave(SysReq sysReq) {
        sysReq.setStatus(3);
        sysReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        return toAjax(sysReqService.updateSysReq(sysReq));
    }

    /**
     * 跳转至应用系统申请处理页面
     * @return
     */
    @GetMapping("/toDeal")
    public String toDeal() {
        return prefix + "/sysReqDeal";
    }
    /**
     * 查看要处理的申请
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/deal/{id}")
    public String deal(@PathVariable("id") String id, ModelMap mmap) {
        SysReq sysReq = sysReqService.selectSysReqById(id);
        mmap.put("sysReq", sysReq);
        return prefix + "/deal";
    }
    /**
     * 应用系统申请处理
     * @param sysReq
     * @return
     */
    @Log(title = "应用系统申请", businessType = BusinessType.UPDATE)
    @PostMapping("/deal")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult dealSave(SysReq sysReq) {
        sysReq.setStatus(4);
        sysReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        
        SysReq sr = sysReqService.selectSysReqById(sysReq.getId());
        OgSys ogSys = new OgSys();
        ogSys.setCode(sr.getCode());
        ogSys.setOrgId(sr.getOrganid());
        ogSys.setCaption(sr.getName());
        ogSys.setMemo(sr.getRemark());
        ogSys.setInvalidationMark(String.valueOf(sr.getMark()));
        ogSys.setIsExamination(String.valueOf(sr.getCheck()));
        ogSys.setDept(sr.getOfficeid());
        ogSys.setPid(sr.getChargeid());
        ogSys.setBusinessId(sr.getDeptid());
        ogSys.setIsKeySys(String.valueOf(sr.getImportant()));
        ogSys.setSysType(sr.getSysclassid());
        
        return toAjax(sysApplicationManagerService.insertOgSys(ogSys)+sysReqService.updateSysReq(sysReq));
    }
    /**
     * 选择所属负责人
     * @param orgid
     * @return
     */
    @Log(title = "应用系统申请", businessType = BusinessType.UPDATE)
    @PostMapping("/selectCharge")
    @ResponseBody
    public Map<String,Object> selectCharge(String orgid) {
        return sysReqService.selectCharge(orgid);
    }
    /**
     * 跳转至应用系统查询页面
     * @return
     */
    @GetMapping("/toSelect")
    public String toSelect() {
        return prefix + "/sysReqSelect";
    }
}
