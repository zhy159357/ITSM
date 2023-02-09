package com.ruoyi.activiti.controller.itsm.serapp;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysWorkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.activiti.domain.GroupReq;
import com.ruoyi.activiti.service.IGroupReqService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
/**
 * 服务请求-工作组注销Controller
 * @author jintong
 *
 */
@Controller
@RequestMapping("/groupDelete")
public class GroupDeleteController extends BaseController {
    private String prefix = "groupDelete";

    @Autowired
    private IGroupReqService groupReqService;
    @Autowired
    private ISysWorkService sysWorkService;
    @Autowired
	private ISysDeptService sysDeptService;
    @Autowired
	private ISysApplicationManagerService sysApplicationManagerService;
    @Autowired
    private IOgUserService ogUserService;
    
    @GetMapping()
    public String groupDelete(ModelMap mmap) {
        return prefix + "/groupDelete";
    }
    /**
	 * 工作组注销查询页面 表格数据
	 * @param groupReq
	 * @return
	 */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(GroupReq groupReq) {
        startPage();
        groupReq.setReqType(3);
        List<GroupReq> list = groupReqService.selectGroupReqList(groupReq);
        return getDataTable(list);
    }
    /**
	 * 工作组注销发起页面 表格数据
	 * @param groupReq
	 * @return
	 */
    @PostMapping("/listReq")
    @ResponseBody
    public TableDataInfo listReq(GroupReq groupReq) {     	
        startPage();
        groupReq.setReqType(3);
        groupReq.setStatus(1);
        groupReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        List<GroupReq> list = groupReqService.selectGroupReqList(groupReq);
        return getDataTable(list);
    }
    /**
	 * 工作组注销审核页面 表格数据
	 * @param groupReq
	 * @return
	 */
    @PostMapping("/listAudit")
    @ResponseBody
    public TableDataInfo listAudit(GroupReq groupReq) {     	
        startPage();
        groupReq.setReqType(3);
        groupReq.setStatus(2);
        groupReq.setAuditor(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        List<GroupReq> list = groupReqService.selectGroupReqList(groupReq);
        return getDataTable(list);
    }
    /**
	 * 工作组注销处理页面 表格数据
	 * @param groupReq
	 * @return
	 */
    @PostMapping("/listDeal")
    @ResponseBody
    public TableDataInfo listDeal(GroupReq groupReq) {     	
        startPage();
        groupReq.setReqType(3);
        groupReq.setStatus(3);
        groupReq.setDealer(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        List<GroupReq> list = groupReqService.selectGroupReqList(groupReq);
        return getDataTable(list);
    }
    /**
     * 弹出工作组选择页面
     * @return
     */
    @GetMapping("/group")
    public String person() {
        return prefix + "/group";
    }
    /**
	 * 添加工作组注销申请
	 * @param id
	 * @param mmap
	 * @return
	 */
    @GetMapping("/add/{id}")
    public String add(@PathVariable("id") String id, ModelMap mmap) {
    	OgGroup ogGroup = sysWorkService.selectOgGroupById(id);
    	GroupReq groupReq = new GroupReq();
    	groupReq.setGroupid(ogGroup.getGroupId());
    	groupReq.setName(ogGroup.getGrpName());
    	groupReq.setNote(ogGroup.getMemo());
    	groupReq.setDeptid(ogGroup.getOrgId());
    	OgOrg ogOrg = sysDeptService.selectDeptById(ogGroup.getOrgId());
    	groupReq.setDept(ogOrg.getOrgName()); 
    	groupReq.setPhone(ogGroup.getTel());
    	String sysid = ogGroup.getSysId();
    	groupReq.setSysid(sysid);
    	if(sysid!=null && !sysid.trim().equals("")){
	    	OgSys ogSys = sysApplicationManagerService.selectOgSysBySysId(sysid);
	    	groupReq.setSys(ogSys.getCaption());
    	}
    	groupReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
    	groupReq.setCreateTime(new Date());
		groupReq.setMark(ogGroup.getInvalidationMark().intValue());
    	
		mmap.put("groupReq", groupReq);
        return prefix + "/add";
    }
    /**
     * 添加工作组注销申请
     * @param groupReq
     * @return
     */
    @Log(title = "工作组申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(GroupReq groupReq) {
        groupReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        groupReq.setStatus(1);
        groupReq.setId(UUID.getUUIDStr());
		groupReq.setReqType(3);
        return toAjax(groupReqService.insertGroupReq(groupReq));
    }
    /**
     * 修改工作组注销申请
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
        GroupReq groupReq = groupReqService.selectGroupReqById(id);
        mmap.put("groupReq", groupReq);
        return prefix + "/edit";
    }
    /**
     * 暂存工作组注销申请
     * @param groupReq
     * @return
     */
    @Log(title = "工作组申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(GroupReq groupReq) {
        groupReq.setStatus(1);
        groupReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        return toAjax(groupReqService.updateGroupReq(groupReq));
    }
    /**
     * 提交工作组注销申请
     * @param groupReq
     * @return
     */
    @Log(title = "工作组申请", businessType = BusinessType.UPDATE)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxResult editSubmit(GroupReq groupReq) {
        groupReq.setStatus(2);
        String id =  groupReq.getId();
        if (id!=null && !"".equals(id) && !"null".equals(id)) {
        	groupReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
    		return toAjax(groupReqService.updateGroupReq(groupReq));
    	}else{
    		groupReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
    		groupReq.setId(UUID.getUUIDStr());
    		groupReq.setReqType(3);
    		return toAjax(groupReqService.insertGroupReq(groupReq));
    	}
    }
    /**
     * 删除
     */
    @Log(title = "工作组申请", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(groupReqService.deleteGroupReqByIds(ids));
    }
    /**
     * 查看详情
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
        GroupReq groupReq = groupReqService.selectGroupReqById(id);
        mmap.put("groupReq", groupReq);
        return "groupReq/detail";
    }
    /*
     * 导出
     */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(GroupReq groupReq) {
    	groupReq.setReqType(3);
        List<GroupReq> list = groupReqService.selectGroupReqList(groupReq);
        ExcelUtil<GroupReq> util = new ExcelUtil<>(GroupReq.class);
        return util.exportExcel(list, "groupReq");
    }
    /**
     * 跳转至工作组注销审核页面
     * @return
     */
    @GetMapping("/toAudit")
    public String toAudit() {
        return prefix + "/groupDeleteAudit";
    }
    /**
     * 查看要审核的申请
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/audit/{id}")
    public String audit(@PathVariable("id") String id, ModelMap mmap) {
        GroupReq groupReq = groupReqService.selectGroupReqById(id);
        mmap.put("groupReq", groupReq);
        return prefix + "/audit";
    }
    /**
     * 工作组注销审核
     * @param groupReq
     * @return
     */
    @Log(title = "工作组申请", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    @ResponseBody
    public AjaxResult auditSave(GroupReq groupReq) {
        groupReq.setStatus(3);
        groupReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        return toAjax(groupReqService.updateGroupReq(groupReq));
    }

    /**
     * 跳转至工作组注销处理页面
     * @return
     */
    @GetMapping("/toDeal")
    public String toDeal() {
        return prefix + "/groupDeleteDeal";
    }
    /**
     * 查看要处理的申请
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/deal/{id}")
    public String deal(@PathVariable("id") String id, ModelMap mmap) {
        GroupReq groupReq = groupReqService.selectGroupReqById(id);
        mmap.put("groupReq", groupReq);
        return prefix + "/deal";
    }
    /**
     * 工作组注销处理
     * @param groupReq
     * @return
     */
    @Log(title = "工作组申请", businessType = BusinessType.UPDATE)
    @PostMapping("/deal")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult dealSave(GroupReq groupReq) {
    	groupReq.setStatus(4);
        groupReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        GroupReq gr = groupReqService.selectGroupReqById(groupReq.getId());
        return toAjax(sysWorkService.deleteOgGroupByIds(gr.getGroupid())+groupReqService.updateGroupReq(groupReq));
    }
    /**
     * 跳转至账号注销查询页面
     * @return
     */
    @GetMapping("/toSelect")
    public String toSelect() {
        return prefix + "/groupDeleteSelect";
    }
}
