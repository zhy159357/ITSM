package com.ruoyi.activiti.controller.itsm.serapp;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.activiti.domain.AccountReq;
import com.ruoyi.activiti.service.IAccountReqService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysDeptService;
/**
 * 服务请求-账号注销Controller
 * @author jintong
 *
 */
@Controller
@RequestMapping("/accountDelete")
public class AccountDeleteController extends BaseController{
	private String prefix = "accountDelete";
	
	@Autowired
    private IAccountReqService accountReqService;
	@Autowired
	private IOgPersonService ogPersonService;
	@Autowired
	private IOgUserService ogUserService;
	@Autowired
	private ISysDeptService sysDeptService;
	
	@GetMapping()
	public String accountDelete(ModelMap mmap) {
	    return prefix + "/accountDelete";
	}
	/**
	 * 账号注销查询页面 表格数据
	 * @param accountReq
	 * @return
	 */
	@PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AccountReq accountReq) {     	
        startPage();
        accountReq.setReqType(3);
        List<AccountReq> list = accountReqService.selectAccountReqList(accountReq);
        return getDataTable(list);
    }
	/**
	 * 账号注销发起页面 表格数据
	 * @param accountReq
	 * @return
	 */
	@PostMapping("/listReq")
    @ResponseBody
    public TableDataInfo listReq(AccountReq accountReq) {     	
        startPage();
        accountReq.setReqType(3);
        accountReq.setStatus(1);
        accountReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        List<AccountReq> list = accountReqService.selectAccountReqList(accountReq);
        return getDataTable(list);
    }
	/**
	 * 账号注销审核页面 表格数据
	 * @param accountReq
	 * @return
	 */
	@PostMapping("/listAudit")
	@ResponseBody
	public TableDataInfo listAudit(AccountReq accountReq) {     	
	    startPage();
	    accountReq.setReqType(3);
	    accountReq.setStatus(2);
	    accountReq.setAuditor(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
	    List<AccountReq> list = accountReqService.selectAccountReqList(accountReq);
	    return getDataTable(list);
	}
	/**
	 * 账号注销处理页面 表格数据
	 * @param accountReq
	 * @return
	 */
	@PostMapping("/listDeal")
	@ResponseBody
	public TableDataInfo listDeal(AccountReq accountReq) {     	
	    startPage();
	    accountReq.setReqType(3);
	    accountReq.setStatus(3);
	    accountReq.setDealer(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
	    List<AccountReq> list = accountReqService.selectAccountReqList(accountReq);
	    return getDataTable(list);
	}
	/**
	 * 添加账号注销申请
	 * @param id
	 * @param mmap
	 * @return
	 */
	@GetMapping("/add/{id}")
    public String add(@PathVariable("id") String id, ModelMap mmap) {
    	OgPerson ogPerson = ogPersonService.selectOgPersonById(id);
    	AccountReq accountReq = new AccountReq();
    	
    	accountReq.setPid(ogPerson.getpId());
    	accountReq.setDeptid(ogPerson.getOrgId());  
    	OgOrg ogOrg = sysDeptService.selectDeptById(ogPerson.getOrgId());
    	accountReq.setDept(ogOrg.getOrgName()); 
    	accountReq.setName(ogPerson.getpName());
    	accountReq.setGender(Integer.parseInt(ogPerson.getSex()));
    	String edu=ogPerson.getEdu()==null?"0":ogPerson.getEdu();
    	accountReq.setEducation(Integer.parseInt(edu));
    	accountReq.setJob(ogPerson.getPosition());
    	accountReq.setBirthday(ogPerson.getBirthday());
    	accountReq.setBirthPlace(ogPerson.getBirthPlace());
    	accountReq.setMobPhone(ogPerson.getMobilPhone());
    	accountReq.setOfficePhone(ogPerson.getPhone());
    	accountReq.setEmail(ogPerson.getEmail());
    	accountReq.setAddress(ogPerson.getAddress());
    	accountReq.setNote(ogPerson.getMemo());
    	accountReq.setOrder(Integer.parseInt(ogPerson.getpOrder()));
    	accountReq.setMark(Integer.parseInt(ogPerson.getPflag()));
    	accountReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
    	accountReq.setCreateTime(new Date());
    	
        mmap.put("accountReq", accountReq);
        return prefix + "/add";
    }
    /**
     * 添加账号注销申请
     * @param accountReq
     * @return
     */
    @Log(title = "账号注销", businessType = BusinessType.UPDATE)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AccountReq accountReq) {
    	accountReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
    	accountReq.setStatus(1);
		accountReq.setId( UUID.getUUIDStr());
        accountReq.setReqType(3);
        return toAjax(accountReqService.insertAccountReq(accountReq));
    }
    /**
     * 修改账号注销申请
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
     * 暂存账号注销申请
     * @param accountReq
     * @return
     */
    @Log(title = "账号注销", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AccountReq accountReq) {
    	accountReq.setStatus(1);
    	accountReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        return toAjax(accountReqService.updateAccountReq(accountReq));
    }
    /**
     * 提交账号注销申请
     * @param accountReq
     * @return
     */
    @Log(title = "账号注销", businessType = BusinessType.UPDATE)
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
    		accountReq.setId(UUID.getUUIDStr());
    		accountReq.setReqType(3);
    		return toAjax(accountReqService.insertAccountReq(accountReq));
    	}
    }
    /**
     * 删除
     */
    @Log(title = "账号注销", businessType = BusinessType.DELETE)
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
        return  "accountReq/detail";
    }
    /**
     * 导出
     */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(AccountReq accountReq) {
    	accountReq.setReqType(3);
        List<AccountReq> list = accountReqService.selectAccountReqList(accountReq);
        ExcelUtil<AccountReq> util = new ExcelUtil<>(AccountReq.class);
        return util.exportExcel(list, "accountReq");
    }
    /**
     * 跳转至账号注销审核页面
     * @return
     */
    @GetMapping("/toAudit")
    public String toAudit() {
        return prefix + "/accountDeleteAudit";
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
     * 账号注销审核
     * @param accountReq
     * @return
     */
    @Log(title = "账号注销", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    @ResponseBody
    public AjaxResult auditSave(AccountReq accountReq) {
    	accountReq.setStatus(3);
    	accountReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        return toAjax(accountReqService.updateAccountReq(accountReq));
    }

    /**
     * 跳转至账号注销处理页面
     * @return
     */
    @GetMapping("/toDeal")
    public String toDeal() {
        return prefix + "/accountDeleteDeal";
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
     * 账号注销处理
     * @param accountReq
     * @return
     */
    @Log(title = "账号注销", businessType = BusinessType.UPDATE)
    @PostMapping("/deal")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult dealSave(AccountReq accountReq) {
    	accountReq.setStatus(4);
    	accountReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
    	String pid = accountReqService.selectAccountReqById(accountReq.getId()).getPid();
        return toAjax(ogUserService.deleteOgUserByPIds(pid)+ogPersonService.deleteOgPersonByPIds(pid)+accountReqService.updateAccountReq(accountReq));
    }
    /**
     * 跳转至账号注销查询页面
     * @return
     */
    @GetMapping("/toSelect")
    public String toSelect() {
        return prefix + "/accountDeleteSelect";
    }
}
