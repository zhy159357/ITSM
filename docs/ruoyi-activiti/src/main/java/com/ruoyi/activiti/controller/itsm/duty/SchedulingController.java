package com.ruoyi.activiti.controller.itsm.duty;

import com.ruoyi.activiti.service.IDutySchedulingService;
import com.ruoyi.activiti.service.IDutyTypeinfoService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.ZtreeStr;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.ruoyi.activiti.constants.VersionStatusConstants.QGZX_ORGID;
import static com.ruoyi.activiti.constants.VersionStatusConstants.ZBJS_ROLE;

/**
 * 排班信息Controller
 * @author dayong_sun
 * @date 2020-12-06
 */
@Controller
@RequestMapping("duty/scheduling")
public class SchedulingController extends BaseController {
    private String prefix = "duty/scheduling";

    @Autowired
    private IDutySchedulingService dutySchedulingService;
    @Autowired
    private IDutyTypeinfoService dutyTypeinfoService;
    @Autowired
    private IOgPersonService ogPersonService;
    @Autowired
    private ISysDeptService deptService;
    @Value("${cntxtag.enabled}")
    private String cntxtag;

    @GetMapping()
    public String scheduling() {
        return prefix + "/scheduling";
    }

    /**
     * 修改值班日志内容
     */
    @PostMapping("/editCheckLog")
    @ResponseBody
    public int editCheckLog(String id, String content, String updateTime) {
        return dutySchedulingService.editCheckLog(id,content,updateTime);
    }

    @Log(title = "值班日志", businessType = BusinessType.DELETE)
    @PostMapping("/removeLog")
    @ResponseBody
    public AjaxResult removeLog(String ids) {
        return dutySchedulingService.deleteDutyLogByIds(ids);
    }

    /**
     * 根据人员id查询值班日志
     */
    @PostMapping("/searchLogForUserId/{userId}")
    @ResponseBody
    public TableDataInfo searchLogForUserId(@PathVariable("userId") String userId,DutyLog dutyLog){
        startPage();
        dutyLog.setUserId(userId);
        List<DutyLog> result = dutySchedulingService.searchLogForUserId(dutyLog);
        return getDataTable(result);
    }

    /**
     * 新增值班日志
     */
    @PostMapping("/addLog")
    @ResponseBody
    public int add(DutyLog dutyLog){
        int result = dutySchedulingService.addLog(dutyLog);
        return result;
    }

    /**
     * 查询值班人员
     */
    @PostMapping("/duty/person")
    @ResponseBody
    public int dutyPerson(String mobilePhone, String endTime) throws ParseException {
        int length = dutySchedulingService.selectDutyPersonList(mobilePhone, endTime);
        return length;
    }

    /**
     * 查询参数列别列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(DutyScheduling dutyScheduling) {
        startPage();
        List<DutyScheduling> list = dutySchedulingService.selectDutySchedulingList(dutyScheduling);
        return getDataTable(list);
    }

    /**
     * 新增排班信息
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        List<DutyTypeinfo> typelist = dutyTypeinfoService.selectTypeNo(new DutyTypeinfo());
        List<DutyPerson> userlist = dutySchedulingService.selectOgPersonList(QGZX_ORGID);

        mmap.put("typelist", typelist);
        mmap.put("userlist", userlist);
        mmap.put("cntxtag", cntxtag);

        return prefix + "/add";
    }

    @PostMapping("/addlist")
    @ResponseBody
    public TableDataInfo addlist(OgPerson ogPerson)
    {
        startPage();
        ogPerson.setrId(ZBJS_ROLE);
        List<OgPerson> list =  dutySchedulingService.selectDutyUserList(ogPerson);
        return getDataTable(list);
    }

    /**
     * 选择类别树
     */
    @GetMapping("/selectTypeinfoTree/{typeinfoId}")
    public String selectTypeinfoTree(@PathVariable("typeinfoId") String typeinfoId, ModelMap mmap)
    {
        mmap.put("typeinfoId", typeinfoId);
        return prefix + "/tree";
    }

    /**
     * 加载参数设置列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<ZtreeStr> treeData() {
        List<ZtreeStr> ztrees = dutyTypeinfoService.selectTypeinfoTree(new DutyTypeinfo());
        return ztrees;
    }

    /**
     * 根据用户id查询手机
     */
    @Log(title = "参数列别", businessType = BusinessType.DELETE)
    @PostMapping("/selectPhoneByPid")
    @ResponseBody
    public String selectPhoneByPid(String pids) {
        return dutySchedulingService.selectPhoneByPid(pids);
    }

    /**
     * 根据用户id查询手机
     */
    @Log(title = "参数列别", businessType = BusinessType.DELETE)
    @PostMapping("/addCheckSave")
    @ResponseBody
    public String addCheckSave(DutyScheduling dutyScheduling) {
        return dutySchedulingService.addCheckSave(dutyScheduling);
    }

    /**
     * 新增保存参数列别
     */
    @Log(title = "参数列别", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(DutyScheduling dutyScheduling) {
        return toAjax(dutySchedulingService.insertDutyScheduling(dutyScheduling));
    }

    /**
     * 修改校验时间
     */
    @Log(title = "参数列别", businessType = BusinessType.UPDATE)
    @PostMapping("/editCheck")
    @ResponseBody
    public int editCheck(String schedulingId) {
        return dutySchedulingService.editSchedulingCheck(schedulingId);
    }

    /**
     * 给当前账号指定人员信息
     * @return
     */
    @GetMapping("/selectUser")
    public String selectUser()
    {
        return prefix + "/selectUser";
    }

    @PostMapping("/userlist")
    @ResponseBody
    public TableDataInfo userlist(OgPerson ogPerson)
    {
        startPage();
        ogPerson.setrId(ZBJS_ROLE);
        List<OgPerson> list =  dutySchedulingService.selectDutyUserList(ogPerson);
        return getDataTable(list);
    }
    /**
     * 修改参数列别
     */
    @GetMapping("/edit/{schedulingId}")
    public String edit(@PathVariable("schedulingId") String schedulingId, ModelMap mmap) {
        DutyScheduling dutyScheduling = dutySchedulingService.selectDutySchedulingById(schedulingId);
        List<DutyTypeinfo> typelist = dutyTypeinfoService.selectTypeNo(new DutyTypeinfo());
        List<DutyPerson> userlist = dutySchedulingService.selectOgPersonList(QGZX_ORGID);
        mmap.put("scheduling", dutyScheduling);
        mmap.put("userlist", userlist);
        mmap.put("typelist", typelist);
        mmap.put("cntxtag", cntxtag);
        return prefix + "/edit";
    }

    /**
     * 修改保存参数列别
     */
    @Log(title = "参数列别", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(DutyScheduling dutyScheduling) {
        return toAjax(dutySchedulingService.updateDutyScheduling(dutyScheduling));
    }

    /**
     * 删除参数列别
     */
    @Log(title = "参数列别", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return dutySchedulingService.deleteDutySchedulingByIds(ids);
    }

    /**
     * 校验角色权限
     */
    @PostMapping("/checkTypeNoUnique")
    @ResponseBody
    public String checkTypeNoUnique(DutyTypeinfo dutyTypeinfo)
    {
        return dutySchedulingService.checkTypeNoUnique(dutyTypeinfo);
    }

    /**
     * 验证月份
     */
    @PostMapping("/checkDutyDateUnique")
    @ResponseBody
    public String checkDutyDateUnique(DutyScheduling dutyScheduling)
    {
        return dutySchedulingService.checkDutyDateUnique(dutyScheduling);
    }

    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate()
    {
        ExcelUtil<DutyScheduling> util = new ExcelUtil<DutyScheduling>(DutyScheduling.class);
        return util.importTemplateExcel("值班模板");
    }

    @PostMapping("/exportExcel")
    @ResponseBody
    public AjaxResult exportExcel()
    {
        //orgid为全国中心的机构id（QGZX_ORGID）
        List<DutyPerson> list = dutySchedulingService.exportOgPersonList();
        ExcelUtil<DutyPerson> util = new ExcelUtil<DutyPerson>(DutyPerson.class);
        return util.exportExcel(list,"人员信息");
    }

    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file) throws Exception
    {
        ExcelUtil<DutyScheduling> util = new ExcelUtil<DutyScheduling>(DutyScheduling.class);
        List<DutyScheduling> userList = null;
        try{
            userList = util.importExcel(file.getInputStream());
        }catch (Exception e){
            return AjaxResult.warn("导入数据有误！");
        }
        String operName = ShiroUtils.getSysUser().getLoginName();
        String message = dutySchedulingService.importScheduling(userList, operName);
        return AjaxResult.success(message);
    }
}
