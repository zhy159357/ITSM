package com.ruoyi.activiti.controller.itsm.serapp;

import java.text.SimpleDateFormat;
import java.util.List;

import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgUserService;
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
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
/**
 * 服务请求-工作组申请Controller
 * @author jintong
 *
 */
@Controller
@RequestMapping("/groupReq")
public class GroupReqController extends BaseController {
    private String prefix = "groupReq";

    @Autowired
    private IGroupReqService groupReqService;
    @Autowired
    private ISysWorkService sysWorkService;
    @Autowired
    private IOgUserService ogUserService;
    @GetMapping()
    public String groupReq(ModelMap mmap) {
        return prefix + "/groupReq";
    }
    /**
     * 工作组申请查询页面 表格数据
     * @param groupReq
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(GroupReq groupReq) {
        startPage();
        groupReq.setReqType(1);
        List<GroupReq> list = groupReqService.selectGroupReqList(groupReq);
        return getDataTable(list);
    }
    /**
     * 工作组申请发起页面 表格数据
     * @param groupReq
     * @return
     */
    @PostMapping("/listReq")
    @ResponseBody
    public TableDataInfo listReq(GroupReq groupReq) {     	
        startPage();
        groupReq.setReqType(1);
        groupReq.setStatus(1);
        groupReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        List<GroupReq> list = groupReqService.selectGroupReqList(groupReq);
        return getDataTable(list);
    }
    /**
     * 工作组申请审核页面 表格数据
     * @param groupReq
     * @return
     */
    @PostMapping("/listAudit")
    @ResponseBody
    public TableDataInfo listAudit(GroupReq groupReq) {     	
        startPage();
        groupReq.setReqType(1);
        groupReq.setStatus(2);
        groupReq.setAuditor(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        List<GroupReq> list = groupReqService.selectGroupReqList(groupReq);
        return getDataTable(list);
    }
    /**
     * 工作组申请处理页面 表格数据
     * @param groupReq
     * @return
     */
    @PostMapping("/listDeal")
    @ResponseBody
    public TableDataInfo listDeal(GroupReq groupReq) {     	
        startPage();
        groupReq.setReqType(1);
        groupReq.setStatus(3);
        groupReq.setDealer(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        List<GroupReq> list = groupReqService.selectGroupReqList(groupReq);
        return getDataTable(list);
    }
    /**
     * 添加工作组申请
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }
    /**
     * 添加工作组申请
     * @param groupReq
     * @return
     */
    @Log(title = "工作组申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(GroupReq groupReq) {
        groupReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        groupReq.setStatus(1);
        String iid = UUID.getUUIDStr();
        groupReq.setId(iid);
		String groupid = groupReq.getGroupid();
		if(groupid==null || "".equals(groupid) || "null".equals(groupid)){
			groupReq.setGroupid(iid);
		}
		groupReq.setReqType(1);
        return toAjax(groupReqService.insertGroupReq(groupReq));
    }
    /**
     * 修改工作组申请
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
     * 暂存工作组申请
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
     * 提交工作组申请
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
    		String iid = UUID.getUUIDStr();
    		groupReq.setId(iid);
    		String groupid = groupReq.getGroupid();
    		if(groupid==null || "".equals(groupid) || "null".equals(groupid)){
    			groupReq.setGroupid(iid);
    		}
    		groupReq.setReqType(1);
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
        return prefix + "/detail";
    }
    /**
     * 导出
     * @param groupReq
     * @return
     */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(GroupReq groupReq) {
    	groupReq.setReqType(1);
        List<GroupReq> list = groupReqService.selectGroupReqList(groupReq);
        ExcelUtil<GroupReq> util = new ExcelUtil<>(GroupReq.class);
        return util.exportExcel(list, "groupReq");
    }
    /**
     * 跳转至工作组申请审核页面
     * @return
     */
    @GetMapping("/toAudit")
    public String toAudit() {
        return prefix + "/groupReqAudit";
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
     * 工作组申请审核
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
     * 跳转至工作组申请处理页面
     * @return
     */
    @GetMapping("/toDeal")
    public String toDeal() {
        return prefix + "/groupReqDeal";
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
     * 工作组申请处理
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
        OgGroup ogGroup = new OgGroup();
        ogGroup.setGroupId(gr.getGroupid());//
        ogGroup.setGrpName(gr.getName());
        ogGroup.setMemo(gr.getNote());
        ogGroup.setOrgId(gr.getDeptid());
        ogGroup.setOrgName(gr.getDept());
        ogGroup.setTel(gr.getPhone());
        ogGroup.setSysId(gr.getSys());
        ogGroup.setAdder(gr.getCreateBy());
		ogGroup.setAddTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(gr.getCreateTime()));
		ogGroup.setModer(gr.getUpdateBy());
		ogGroup.setModTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(gr.getUpdateTime()));
        ogGroup.setInvalidationMark((long)gr.getMark());
		
        return toAjax(sysWorkService.insertOgGroup(ogGroup)+groupReqService.updateGroupReq(groupReq));
    }
    /**
     * 跳转至工作组查询页面
     * @return
     */
    @GetMapping("/toSelect")
    public String toSelect() {
        return prefix + "/groupReqSelect";
    }
}
