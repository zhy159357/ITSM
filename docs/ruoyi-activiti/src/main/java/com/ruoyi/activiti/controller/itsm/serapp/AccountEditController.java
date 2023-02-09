package com.ruoyi.activiti.controller.itsm.serapp;

import java.text.SimpleDateFormat;
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
 * 服务请求-账号修改Controller
 * @author jintong
 *
 */
@Controller
@RequestMapping("/accountEdit")
public class AccountEditController extends BaseController{
	private String prefix = "accountEdit";
	
	@Autowired
    private IAccountReqService accountReqService;
	@Autowired
	private IOgPersonService ogPersonService;
	@Autowired
    private IOgUserService ogUserService;
	@Autowired
	private ISysDeptService sysDeptService;
	
	@GetMapping()
	public String accountEdit(ModelMap mmap) {
	    return prefix + "/accountEdit";
	}
	/**
	 * 账号修改查询页面 表格数据
	 * @param accountReq
	 * @return
	 */
	@PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AccountReq accountReq) {     	
        startPage();
        accountReq.setReqType(2);
        List<AccountReq> list = accountReqService.selectAccountReqList(accountReq);
        return getDataTable(list);
    }
	/**
	 * 账号修改发起页面 表格数据
	 * @param accountReq
	 * @return
	 */
	@PostMapping("/listReq")
    @ResponseBody
    public TableDataInfo listReq(AccountReq accountReq) {     	
        startPage();
        accountReq.setReqType(2);
        accountReq.setStatus(1);
        accountReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        List<AccountReq> list = accountReqService.selectAccountReqList(accountReq);
        return getDataTable(list);
    }
	/**
	 * 账号修改审核页面 表格数据
	 * @param accountReq
	 * @return
	 */
    @PostMapping("/listAudit")
    @ResponseBody
    public TableDataInfo listAudit(AccountReq accountReq) {     	
        startPage();
        accountReq.setReqType(2);
        accountReq.setStatus(2);
        accountReq.setAuditor(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        List<AccountReq> list = accountReqService.selectAccountReqList(accountReq);
        return getDataTable(list);
    }
    /**
     * 账号修改处理页面 表格数据
     * @param accountReq
     * @return
     */
    @PostMapping("/listDeal")
    @ResponseBody
    public TableDataInfo listDeal(AccountReq accountReq) {     	
        startPage();
        accountReq.setReqType(2);
        accountReq.setStatus(3);
        accountReq.setDealer(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        List<AccountReq> list = accountReqService.selectAccountReqList(accountReq);
        return getDataTable(list);
    }
    /**
     * 弹出账号选择页面
     * @return
     */
	@GetMapping("/person")
    public String person() {
        return prefix + "/person";
    }
	/**
	 * 添加账号修改申请
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
        String porder=ogPerson.getpOrder()==null?"0":ogPerson.getpOrder();
    	accountReq.setOrder(Integer.parseInt(porder));
        String pflag=ogPerson.getPflag()==null?"0":ogPerson.getPflag();
    	accountReq.setMark(Integer.parseInt(pflag));
    	accountReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
    	accountReq.setCreateTime(new Date());
    	
        mmap.put("accountReq", accountReq);
        return prefix + "/add";
    }
    /**
     * 添加账号修改申请
     * @param accountReq
     * @return
     */
    @Log(title = "账号修改", businessType = BusinessType.UPDATE)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AccountReq accountReq) {
    	accountReq.setCreateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
    	accountReq.setStatus(1);
		accountReq.setId( UUID.getUUIDStr());
        accountReq.setReqType(2);
        return toAjax(accountReqService.insertAccountReq(accountReq));
    }
    /**
     * 修改账号修改申请
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
     * 暂存账号修改申请
     * @param accountReq
     * @return
     */
    @Log(title = "账号修改", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AccountReq accountReq) {
    	accountReq.setStatus(1);
    	accountReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        return toAjax(accountReqService.updateAccountReq(accountReq));
    }
    /**
     * 提交账号修改申请
     * @param accountReq
     * @return
     */
    @Log(title = "账号修改", businessType = BusinessType.UPDATE)
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
    		accountReq.setReqType(2);
    		return toAjax(accountReqService.insertAccountReq(accountReq));
    	}
    }
    
    /**
     * 删除
     */
    @Log(title = "账号修改", businessType = BusinessType.DELETE)
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
    	accountReq.setReqType(2);
        List<AccountReq> list = accountReqService.selectAccountReqList(accountReq);
        ExcelUtil<AccountReq> util = new ExcelUtil<>(AccountReq.class);
        return util.exportExcel(list, "accountReq");
    }
    /**
     * 跳转至账号修改审核页面
     * @return
     */
    @GetMapping("/toAudit")
    public String toAudit() {
        return prefix + "/accountEditAudit";
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
     * 账号修改审核
     * @param accountReq
     * @return
     */
    @Log(title = "账号修改", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    @ResponseBody
    public AjaxResult auditSave(AccountReq accountReq) {
    	accountReq.setStatus(3);
    	accountReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
        return toAjax(accountReqService.updateAccountReq(accountReq));
    }

    /**
     * 跳转至账号修改处理页面
     * @return
     */
    @GetMapping("/toDeal")
    public String toDeal() {
        return prefix + "/accountEditDeal";
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
     * 账号修改处理
     * @param accountReq
     * @return
     */
    @Log(title = "账号修改", businessType = BusinessType.UPDATE)
    @PostMapping("/deal")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult dealSave(AccountReq accountReq) {
    	accountReq.setStatus(4);
    	accountReq.setUpdateBy(ogUserService.selectOgUserByUserId(ShiroUtils.getOgUser().getUserId()).getpId());
    	AccountReq ar= accountReqService.selectAccountReqById(accountReq.getId());
    	OgPerson ogPerson = new OgPerson();
		ogPerson.setpId(ar.getPid());
		ogPerson.setOrgId(ar.getDeptid()==null?" ":ar.getDeptid());     
		ogPerson.setOrgname(ar.getDept()==null?" ":ar.getDept()); 
		ogPerson.setpName(ar.getName()==null?" ":ar.getName());
		ogPerson.setSex(String.valueOf(ar.getGender()));
		ogPerson.setEdu(String.valueOf(ar.getEducation()));
		ogPerson.setBirthday(ar.getBirthday()==null?" ":ar.getBirthday());
		ogPerson.setBirthPlace(ar.getBirthPlace()==null?" ":ar.getBirthPlace());	
		ogPerson.setPosition(ar.getJob()==null?" ":ar.getJob());
		ogPerson.setPhone(ar.getOfficePhone()==null?" ":ar.getOfficePhone());
		ogPerson.setMobilPhone(ar.getMobPhone()==null?" ":ar.getMobPhone());
		ogPerson.setEmail(ar.getEmail()==null?" ":ar.getEmail());
		ogPerson.setAddress(ar.getAddress()==null?" ":ar.getAddress());
		ogPerson.setAdder(ar.getCreateBy());
		ogPerson.setAddtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ar.getCreateTime()));
		ogPerson.setModer(ar.getUpdateBy());
		ogPerson.setMemo(ar.getNote());
		ogPerson.setUpdatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ar.getUpdateTime()));
		ogPerson.setpOrder(String.valueOf(ar.getOrder()));
		ogPerson.setPflag(String.valueOf(ar.getMark()));
    	
        return toAjax(ogPersonService.updateOgPerson(ogPerson)+accountReqService.updateAccountReq(accountReq));
    }
    /**
     * 跳转至账号查询页面
     * @return
     */
    @GetMapping("/toSelect")
    public String toSelect() {
        return prefix + "/accountEditSelect";
    }
    /**
     * 检查账号唯一性
     * @param pid
     * @return
     */
    @Log(title = "账号修改", businessType = BusinessType.UPDATE)
    @PostMapping("/checkUnique")
    @ResponseBody
    public AjaxResult checkUnique(String pid) {
        return accountReqService.checkUnique(pid);
    }
}
