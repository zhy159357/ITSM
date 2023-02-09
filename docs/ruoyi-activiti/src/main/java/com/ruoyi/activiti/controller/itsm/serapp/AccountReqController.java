package com.ruoyi.activiti.controller.itsm.serapp;

import com.ruoyi.activiti.domain.AccountReq;
import com.ruoyi.activiti.service.IAccountReqService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.SynchronizeUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.framework.shiro.service.SysPasswordService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserPostService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISynchronizeUserService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.server.SynchronizeUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 服务请求-账号申请Controller
 * @author jintong
 *
 */
@Controller
@RequestMapping("/accountReq")
public class AccountReqController extends BaseController{
	private String prefix = "accountReq";
	
	@Autowired
    private IAccountReqService accountReqService;
	@Autowired
	private IOgPersonService ogPersonService;
	@Autowired
	private ISysDeptService sysDeptService;
	@Autowired
    private IOgUserService ogUserService;
    @Autowired
    private SysPasswordService sysPasswordService;
    @Autowired
    private ISynchronizeUserService synchronizeUserService;
    @Autowired
    private IOgUserPostService ogUserPostService;
     
    @GetMapping()
    public String accountReq(ModelMap mmap) {
        return prefix + "/accountReq";
    }
    /**
     * 账号申请查询页面 表格数据
     * @param accountReq
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AccountReq accountReq) {     	
        startPage();
        accountReq.setReqType(1);
        List<AccountReq> list = accountReqService.selectAccountReqList(accountReq);
        return getDataTable(list);
    }
    /**
     * 账号申请发起页面 表格数据
     * @param accountReq
     * @return
     */
    @PostMapping("/listReq")
    @ResponseBody
    public TableDataInfo listReq(AccountReq accountReq) {
        startPage();
        accountReq.setReqType(1);
        accountReq.setStatus(1);
        accountReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        List<AccountReq> list = accountReqService.selectAccountReqList(accountReq);
        return getDataTable(list);
    }
    /**
     * 账号申请审核页面 表格数据
     * @param accountReq
     * @return
     */
    @PostMapping("/listAudit")
    @ResponseBody
    public TableDataInfo listAudit(AccountReq accountReq) {     	
        startPage();
        accountReq.setReqType(1);
        accountReq.setStatus(2);
        accountReq.setAuditor(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        List<AccountReq> list = accountReqService.selectAccountReqList(accountReq);
        return getDataTable(list);
    }
    /**
     * 账号申请处理页面 表格数据
     * @param accountReq
     * @return
     */
    @PostMapping("/listDeal")
    @ResponseBody
    public TableDataInfo listDeal(AccountReq accountReq) {     	
        startPage();
        accountReq.setReqType(1);
        accountReq.setStatus(3);
        accountReq.setDealer(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        List<AccountReq> list = accountReqService.selectAccountReqList(accountReq);
        return getDataTable(list);
    }
    /**
     * 添加账号申请
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }
    /**
     * 添加账号申请
     * @param accountReq
     * @return
     */
    @Log(title = "账号申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AccountReq accountReq) {
    	accountReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
    	accountReq.setStatus(1);
    	String iid = UUID.getUUIDStr();
		accountReq.setId(iid);
		String pid = accountReq.getPid();
		if(pid==null || "".equals(pid) || "null".equals(pid)){
			accountReq.setPid(iid);
		}
        accountReq.setReqType(1);
    	return toAjax(accountReqService.insertAccountReq(accountReq));
    }
    /**
     * 修改账号申请
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap mmap) {
    	AccountReq accountReq = accountReqService.selectAccountReqById(id);
        mmap.put("accountReq", accountReq);
        return prefix + "/edit";
    }
    /**
     * 暂存账号申请
     * @param accountReq
     * @return
     */
    @Log(title = "账号申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AccountReq accountReq) {
    	accountReq.setStatus(1);
    	accountReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        return toAjax(accountReqService.updateAccountReq(accountReq));
    }
    /**
     * 提交账号申请
     * @param accountReq
     * @return
     */
    @Log(title = "账号申请", businessType = BusinessType.UPDATE)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxResult editSubmit(AccountReq accountReq) {    
        accountReq.setStatus(2);
    	String id = accountReq.getId();
        if (id!=null && !"".equals(id) && !"null".equals(id)) {
        	accountReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
    		return toAjax(accountReqService.updateAccountReq(accountReq));
    	}else{
    		accountReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
    		String iid = UUID.getUUIDStr();
    		accountReq.setId(iid);
    		String pid = accountReq.getPid();
    		if(pid==null || "".equals(pid) || "null".equals(pid)){
    			accountReq.setPid(iid);
    		}
    		accountReq.setReqType(1);
    		return toAjax(accountReqService.insertAccountReq(accountReq));
    	}
    }
    
    /**
     * 删除
     */
    @Log(title = "账号申请", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(accountReqService.deleteAccountReqByIds(ids));
    }
    /**
     * 查看详情
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") String id, ModelMap mmap) {
    	AccountReq accountReq = accountReqService.selectAccountReqById(id);
        mmap.put("accountReq", accountReq);
        return prefix + "/detail";
    }
    /**
     * 导出
     * @param accountReq
     * @return
     */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(AccountReq accountReq) {
    	accountReq.setReqType(1);
        List<AccountReq> list = accountReqService.selectAccountReqList(accountReq);
        ExcelUtil<AccountReq> util = new ExcelUtil<>(AccountReq.class);
        return util.exportExcel(list, "accountReq");
    }
    /**
     * 跳转至账号申请审核页面
     * @return
     */
    @GetMapping("/toAudit")
    public String toAudit() {
        return prefix + "/accountReqAudit";
    }
    /**
     * 查看要审核的申请
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/audit/{id}")
    public String audit(@PathVariable("id") String id, ModelMap mmap) {
    	AccountReq accountReq = accountReqService.selectAccountReqById(id);
        mmap.put("accountReq", accountReq);
        return prefix + "/audit";
    }
    /**
     * 账号申请审核
     * @param accountReq
     * @return
     */
    @Log(title = "账号申请", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    @ResponseBody
    public AjaxResult auditSave(AccountReq accountReq) {
    	accountReq.setStatus(3);
    	accountReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        return toAjax(accountReqService.updateAccountReq(accountReq));
    }

    /**
     * 跳转至账号申请处理页面
     * @return
     */
    @GetMapping("/toDeal")
    public String toDeal() {
        return prefix + "/accountReqDeal";
    }
    /**
     * 查看要处理的申请
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/deal/{id}")
    public String deal(@PathVariable("id") String id, ModelMap mmap) {
    	AccountReq accountReq = accountReqService.selectAccountReqById(id);
        mmap.put("accountReq", accountReq);
        return prefix + "/deal";
    }
    /**
     * 前往岗位分配页面
     * @param id
     * @param mmap
     * @return
     */
    @GetMapping("/selectPost/{id}")
    public String toSelectPost(@PathVariable("id") String id, ModelMap mmap) {
    	mmap.put("pid", id);
        return prefix + "/selectPost";
    }
    /**
     * 展示岗位数据
     * @param pid
     * @return
     */
    @PostMapping("/selectListPostByUserId")
    @ResponseBody
    public AjaxResult selectListPostByUserId(String pid)
    {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.put("data",ogUserPostService.selectPostByUserIdTwo(pid));
        return ajaxResult;
    }
    /**
     * 账号申请处理
     * @param accountReq
     * @return
     */
    @Log(title = "账号申请", businessType = BusinessType.UPDATE)
    @PostMapping("/deal")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult dealSave(AccountReq accountReq) {
    	accountReq.setStatus(4);
    	accountReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
    	AccountReq ar= accountReqService.selectAccountReqById(accountReq.getId());
    	
    	OgPerson ogPerson = new OgPerson();
		ogPerson.setpId(ar.getPid());
		ogPerson.setOrgId(ar.getDeptid());     
		ogPerson.setOrgname(ar.getDept()); 
		ogPerson.setpName(ar.getName());
		ogPerson.setSex(String.valueOf(ar.getGender()));
		ogPerson.setEdu(String.valueOf(ar.getEducation()));
		ogPerson.setBirthday(ar.getBirthday());
		ogPerson.setBirthPlace(ar.getBirthPlace());
		ogPerson.setPosition(ar.getJob());
		ogPerson.setPhone(ar.getOfficePhone());
		ogPerson.setMobilPhone(ar.getMobPhone());
		ogPerson.setEmail(ar.getEmail());
		ogPerson.setAddress(ar.getAddress());
		ogPerson.setAdder(ShiroUtils.getLoginName());
        ogPerson.setAddtime(DateUtils.getTime());
		ogPerson.setMemo(ar.getNote());
		ogPerson.setpOrder(String.valueOf(ar.getOrder()));
		ogPerson.setPflag(String.valueOf(ar.getMark()));
		if (ogPersonService.selectOgPersonById(ar.getPid()) != null) {
            return error("新增人员'" + ogPerson.getpName() + "'失败，人员已存在");
        }
    	
		OgUser ogUser = new OgUser();
		ogUser.setUsername(ar.getMobPhone());
		ogUser.setpId(ar.getPid());
		ogUser.setPname(ar.getName());
		if (UserConstants.ACCOUNT_NAME_NOT_UNIQUE.equals(ogUserService.checkAccountNameUnique(ogUser))) {
            return error("新增账号'" + ogUser.getUsername() + "'失败，账号名称已存在");
        }
        ogUser.setUserId(ogUser.getpId());
        ogUser.setInvalidationMark("1");
        ogUser.setPassword("Bosc@1234");
        ogUser.setPassword(sysPasswordService.encryptPassword(ogUser.getUsername(), ogUser.getPassword(), ogUser.getUserId()));
        int v = ogUserService.insertOgUser(ogUser);
        List<SynchronizeUser> users = synchronizeUserService.selectList();
        SynchronizeUserService synchronizeUserThread = new SynchronizeUserService();
        synchronizeUserThread.save(0, ogUser, users, "");
        
        return toAjax(v+ogPersonService.insertOgPerson(ogPerson)+accountReqService.updateAccountReq(accountReq));
    }
    /**
     * 跳转至账号查询页面
     * @return
     */
    @GetMapping("/toSelect")
    public String toSelect() {
        return prefix + "/accountReqSelect";
    }
    
	/**
	 * 检查手机号唯一性
	 * @param mobPhone
	 * @param id
	 * @param pid
	 * @return
	 */
    @Log(title = "账号申请", businessType = BusinessType.UPDATE)
    @PostMapping("/checkPhone")
    @ResponseBody
    public boolean checkPhone(String mobPhone, String id, String pid) {
        return accountReqService.checkPhone(mobPhone,id,pid);
    }
    /**
     * 选择审核/处理部门
     * @param deptid
     * @return
     */
    @Log(title = "账号申请", businessType = BusinessType.UPDATE)
    @PostMapping("/selectAuditDept")
    @ResponseBody
    public Map<String,Object> selectAuditDept(String deptid) {
        return accountReqService.selectAuditDept(deptid);
    }
    /**
     * 选择审核/处理人
     * @param deptid
     * @param orgid
     * @return
     */
    @Log(title = "账号申请", businessType = BusinessType.UPDATE)
    @PostMapping("/selectAuditor")
    @ResponseBody
    public Map<String,Object> selectAuditor(String deptid,String orgid) {
        return accountReqService.selectAuditor(deptid,orgid);
    }
    /**
     * 默认选中当前部门
     * @return
     */
    @Log(title = "账号申请", businessType = BusinessType.UPDATE)
    @PostMapping("/selectCurrentDept")
    @ResponseBody
    public Map<String,Object> selectCurrentDept() {
    	Map<String,Object> map = new HashMap<>();
    	OgPerson ogPerson = ogPersonService.selectOgPersonById(ShiroUtils.getOgUser().getpId());
        OgOrg ogOrg = sysDeptService.selectDeptById(ogPerson.getOrgId());
        map.put("deptid", ogOrg.getOrgId());
        map.put("dept", ogOrg.getOrgName());
    	return map;
    }
}
