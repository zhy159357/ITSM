package com.ruoyi.activiti.controller.itsm.check;

import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.activiti.service.*;
import com.ruoyi.common.constant.DictConstants;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.service.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.activiti.domain.CheckSheet;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 隐患排查单Controller
 * 
 * @author zx
 * @date 2021-01-19
 */
@Controller
@RequestMapping("activiti/sheet")
public class CheckSheetController extends BaseController
{
    private String prefix = "sheet";

    @Autowired
    private ICheckSheetService checkSheetService;
    @Autowired
    private IIdGeneratorService idGeneratorService;
    @Autowired
    private IOgPersonService iOgPersonService;
    @Autowired
    private ISysApplicationManagerService applicationManagerService;
    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private IImBizIssuefxService issueService;
    @Autowired
    private ICommonService commonService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private IPubFlowLogService pubFlowLogService;
    @Autowired
    private IPubParaValueService iPubParaValueService;
    @GetMapping("/show/{type}")
    public String sheet(@PathVariable("type") String type,ModelMap mp)
    {
        mp.addAttribute("type",type);
        if("all".equals(type)){
            return prefix + "/sheet";
        }else {
            return prefix+"/chuliSheet";
        }
    }

    /**
     * 查询我的隐患排查单列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CheckSheet checkSheet)
    {
        List<CheckSheet> all=new ArrayList<>();
        if(StringUtils.isNotNull(checkSheet.getStatus())){
            String[] state = checkSheet.getStatus().split(",");
            List<String> statecollection = Arrays.stream(state).collect(Collectors.toList());
            checkSheet.getParams().put("status", statecollection);
        }

        if(StringUtils.isNotNull(checkSheet.getParams().get("myFlag"))){
            if("1".equals(checkSheet.getParams().get("myFlag").toString())){
                //我创建的
                checkSheet.setCreateId(ShiroUtils.getUserId());
                List<CheckSheet> myCreat=checkSheetService.selectCheckSheetList(checkSheet);
                all.addAll(myCreat);
            }
            //我处理的
            if("2".equals(checkSheet.getParams().get("myFlag").toString())){
                //我处理的
                List<CheckSheet> myDeal=new ArrayList<>();
                PubFlowLog pub=new PubFlowLog();
                pub.setPerformerId(ShiroUtils.getOgUser().getUserId());
                pub.setLogType("PMYH");
                List<PubFlowLog> pgList=pubFlowLogService.selectPubFlowLogList(pub);
                Set<String> idList=new HashSet<>();
                for(PubFlowLog pb:pgList){
                    if(!idList.contains(pb.getBizId())){
                        CheckSheet ct=checkSheet;
                        ct.setCsId(pb.getBizId());
                        List<CheckSheet> sct= checkSheetService.selectCheckSheetList(ct);
                        myDeal.addAll(sct);
                        idList.add(pb.getBizId());
                    }
                }
               all.addAll(myDeal);
            }
        }else {
             checkSheet.setCreateId(ShiroUtils.getUserId());
             all=checkSheetService.selectCheckSheetList(checkSheet);
        }

        List<CheckSheet> reList=new ArrayList<>();
        for(CheckSheet ct:all){
            String sysName="";
            if(StringUtils.isNotEmpty(ct.getSysid())){
             sysName=applicationManagerService.selectOgSysBySysId(ct.getSysid()).getCaption();
            }
            OgPerson ogPerson=iOgPersonService.selectOgPersonById(ct.getCreateId());
            ct.setSysid(sysName);
            ct.setCreateId(ogPerson.getpName());
            ct.setCreateOrg(ogPerson.getOrgname());
            reList.add(ct);
        }

        return getDataTable_ideal(reList);
    }
    /**
     * 查询隐患排查单列表
     */
    @PostMapping("/listAll")
    @ResponseBody
    public TableDataInfo listAll(CheckSheet checkSheet)
    {
        if(StringUtils.isNotEmpty(checkSheet.getStatus())){
            String[] state=checkSheet.getStatus().split(",");
            List<String> statecollection= Arrays.stream(state).collect(Collectors.toList());
            checkSheet.getParams().put("status",statecollection);
        }
        startPage();
         List<CheckSheet> list = checkSheetService.selectCheckSheetList(checkSheet);
        List<CheckSheet> reList=new ArrayList<>();
        for(CheckSheet ct:list){
            OgPerson ogPerson=iOgPersonService.selectOgPersonById(ct.getCreateId());
            if(ogPerson!=null){
                ct.setCreateId(ogPerson.getpName());
                ct.setCreateOrg(ogPerson.getOrgname());
            }
            if(StringUtils.isNotEmpty(ct.getSysid())){
               OgSys sys=applicationManagerService.selectOgSysBySysId(ct.getSysid());
               if(sys!=null){
                   ct.setSysName(sys.getCaption());
               }
            }
            reList.add(ct);
        }
        return getDataTable(reList,list);
    }
    public Map<String,String>  switchList(String paraName){
        List<PubParaValue> list=iPubParaValueService.selectPubParaValueByParaName(paraName);
        Map<String,String> mp=new HashMap<>();
        for(PubParaValue pubParaValue:list){
            mp.put(pubParaValue.getValue(),pubParaValue.getValueDetail());
        }
        return mp;
    }
    /**
     * 导出隐患排查单
     */
    @Log(title = "隐患排查单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CheckSheet checkSheet)
    {   //隐患分类 hiddenSort
        Map<String,String> csType=switchList("CS_TYPE");
        Map<String,String> status=switchList("CS_STATUS");
        Map<String,String> csLevel=switchList("CS_LEVEL");
        String isCurrentPage = (String) checkSheet.getParams().get("currentPage");
        if(StringUtils.isNotEmpty(checkSheet.getStatus())){
            String[] state=checkSheet.getStatus().split(",");
            List<String> statecollection= Arrays.stream(state).collect(Collectors.toList());
            checkSheet.getParams().put("status",statecollection);
        }
        if ("currentPage".equals(isCurrentPage)){
            startPage();
        }
        List<CheckSheet> list = checkSheetService.selectCheckSheetList(checkSheet);
        List<CheckSheet> reList=new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(CheckSheet ct:list){
                if(StringUtils.isNotNull(ct)){
                    //责任人
                    OgPerson ogPerson=iOgPersonService.selectOgPersonById(ct.getCreateId());
                    ct.setCreateName(ogPerson.getpName());
                    ct.setCreateOrg(ogPerson.getOrgname());
                    ct.setHiddenSort(csType.get(ct.getHiddenSort()));
                    //状态
                    ct.setStatus(status.get(ct.getStatus()));
                    //级别
                    ct.setImportLevel(csLevel.get(ct.getImportLevel()));
                    reList.add(ct);
                }

            }
        }
        ExcelUtil<CheckSheet> util = new ExcelUtil<CheckSheet>(CheckSheet.class);
        return util.exportExcel(reList, "隐患排查单");
    }
    /**
     * 查询待办隐患排查单列表
     */
    @PostMapping("/listMy")
    @ResponseBody
    public TableDataInfo listMy(CheckSheet checkSheet)
    {
        startPage();
        List<CheckSheet> list = new ArrayList<>();
        if(StringUtils.isNotEmpty(checkSheet.getStatus())){
            String[] state=checkSheet.getStatus().split(",");
            List<String> statecollection= Arrays.stream(state).collect(Collectors.toList());
            checkSheet.getParams().put("status",statecollection);
        }
        List<Map<String,Object>> userList=activitiCommService.userTask("PMYH","");
        for(Map<String,Object> mp:userList){
            String csId=mp.get("businesskey").toString();
            checkSheet.setCsId(csId);
            CheckSheet ct=checkSheetService.selectCheckSheet(checkSheet);
            if(ct!=null){
                Map<String,Object> mmp=new HashMap<>();
                mmp.put("description",mp.get("description")==null?"":mp.get("description").toString());
                ct.setParams(mmp);
                list.add(ct);
            }
        }
        List<CheckSheet> reList=new ArrayList<>();
        for(CheckSheet ct:list){
            OgPerson ogPerson=iOgPersonService.selectOgPersonById(ct.getCreateId());
            ct.setCreateId(ogPerson.getpName());
            ct.setCreateOrg(ogPerson.getOrgname());
            reList.add(ct);
        }
        if (list.size() > 1) {
            list = list.stream().sorted(Comparator.comparing(CheckSheet::getCreateTime).reversed()).collect(Collectors.toList());
        }
        return getDataTable(reList);
    }
    /**
     * 新增隐患排查单
     */
    @GetMapping("/add")
    public String add(ModelMap mp)
    {
        String bizType = "YHPC";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        //单号
        mp.addAttribute("csNo",bizType + nowDateStr + numStr);
        //id
        mp.addAttribute("csId", IdUtils.fastSimpleUUID());
        OgPerson ogPerson=iOgPersonService.selectOgPersonById(ShiroUtils.getUserId());
        //创建人
        mp.addAttribute("createId", ShiroUtils.getUserId());
        mp.addAttribute("createName",ogPerson.getpName());
        //机构
        mp.addAttribute("createOrg",ogPerson.getOrgId());
        mp.addAttribute("createOrgName",ogPerson.getOrgname());
        return prefix + "/add";
    }

    /**
     * 新增保存隐患排查单
     */
    @Log(title = "新增排查单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(CheckSheet checkSheet)
    {
        checkSheet.setStatus("0");
        String unitSchedule=checkSheet.getUnitSchedule();
        checkSheet.setUnitSchedule("整改进度："+unitSchedule+"-时间："+DateUtils.getTime());
        return toAjax(checkSheetService.insertCheckSheet(checkSheet));
    }
    /**
     * 审核
     */
    @Log(title = "审核隐患排查单", businessType = BusinessType.INSERT)
    @PostMapping("/shenhe")
    @ResponseBody
    public AjaxResult shenhe(CheckSheet checkSheet) {
        try{
            String reCode=checkSheet.getParams().get("reCode").toString();
            if("0".equals(reCode)){
                checkSheet.setStatus("2");
            }else {
                checkSheet.setStatus("3");
            }
            checkSheetService.updateCheckSheet(checkSheet);
            Map<String,Object> mp=new HashMap<>();
            mp.put("businessKey",checkSheet.getCsId());
            mp.put("processDefinitionKey","PMYH");
            mp.put("description","shenhe");
            mp.put("reCode",checkSheet.getParams().get("reCode"));
            mp.put("comment",checkSheet.getParams().get("comment"));
            Task tk = activitiCommService.getTask(checkSheet.getCsId(), "shenhe");
            mp.put("taskId", tk.getId());
            activitiCommService.usersComplete(mp);
            return toAjax(1);
        }catch (Exception e){
             e.printStackTrace();
            return error("审核失败，请刷新页面，确认任务状态！");
        }
    }
    /**
     * 修改再提交
     */
    @Log(title = "修改再提交", businessType = BusinessType.INSERT)
    @PostMapping("/submit")
    @ResponseBody
    @Transactional
    public AjaxResult submit(CheckSheet checkSheet) {
        try {
            Map<String,Object> mp=new HashMap<>();
            mp.put("businessKey",checkSheet.getCsId());
            String reCode=checkSheet.getParams().get("reCode").toString();
            if("1".equals(reCode)){
                checkSheet.setStatus("1");
                reCode="0";
                mp.put("comment","退回修改");
            }
            if("2".equals(reCode)){
                checkSheet.setStatus("5");
                reCode="1";
                mp.put("comment","作废");
            }
            mp.put("reCode",reCode);
            mp.put("processDefinitionKey","PMYH");
            Task tk = activitiCommService.getTask(checkSheet.getCsId(), "edit");
            mp.put("taskId", tk.getId());
            activitiCommService.usersComplete(mp);
            checkSheetService.updateCheckSheet(checkSheet);
            return toAjax(1);
        }catch (Exception e){
             e.printStackTrace();
            return error("再提交失败，请刷新页面，请确认任务状态！");
        }
    }
    /**
     * 处理
     */
    @Log(title = "处理排查单", businessType = BusinessType.INSERT)
    @PostMapping("/chuli")
    @ResponseBody
    @Transactional
    public AjaxResult chuli(CheckSheet checkSheet) {
        try {
            String reCode = checkSheet.getParams().get("reCode").toString();
            if ("0".equals(reCode)) {
                Map<String, Object> mp = new HashMap<>();
                mp.put("businessKey", checkSheet.getCsId());
                mp.put("description", "chuli");
                mp.put("processDefinitionKey","PMYH");
                mp.put("reCode", reCode);
                mp.put("comment", checkSheet.getParams().get("unitSchedule"));
                Task tk = activitiCommService.getTask(checkSheet.getCsId(), "chuli");
                mp.put("taskId", tk.getId());
                activitiCommService.usersComplete(mp);
                checkSheet.setStatus("4");
            }
            String unitSchedule = "整改进度：" + checkSheet.getParams().get("unitSchedule").toString() +
                    "-时间：" + DateUtils.getTime() + checkSheet.getUnitSchedule();
            checkSheet.setUnitSchedule(unitSchedule);
            checkSheetService.updateCheckSheet(checkSheet);
            return toAjax(1);
        }catch (Exception e){
             e.printStackTrace();
            return error("处理失败，请刷新页面，确认任务状态！");
        }
    }
    /**
     * 转问题单
     */
    @GetMapping("/zhuanWenti/{csId}")
    @Transactional
    public String zhuanWenti(@PathVariable("csId") String csId, ModelMap mmap)
    {
        CheckSheet checkSheet = checkSheetService.selectCheckSheetById(csId);
        //问题单基础信息
        //获取字典项
        List<PubParaValue> businessOrgs = issueService.selectIssuesourceList(DictConstants.VM_DEPT);//业务部门
        List<OgOrg> issueOrgs = issueService.selectOgOrgList();
        List<OgPerson> userlist = new ArrayList<>();
        List<OgPerson> buslist = new ArrayList<>();
        if (null != businessOrgs && businessOrgs.size() > 0) {
            OgPerson ogPerson = new OgPerson();
            ogPerson.setOrgname(businessOrgs.get(0).getValueDetail());
            ogPerson.setrId("2302");
            buslist = commonService.selectPersonByOrgAndRole(ogPerson);
        }
        if (null != issueOrgs && issueOrgs.size() > 0) {
            OgPerson ogPerson = new OgPerson();
            ogPerson.setOrgId(issueOrgs.get(0).getOrgId());
            ogPerson.setrId("9983");
            userlist = commonService.selectPersonByOrgAndRole(ogPerson);
        }
        OgPerson userPerson = iOgPersonService.selectOgPersonById(ShiroUtils.getUserId());
        String createrName = userPerson.getpName();
        String createrPhone=userPerson.getMobilPhone();
        String bizType = "WT";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        mmap.addAttribute("userId", ShiroUtils.getUserId());
        mmap.addAttribute("userlist", userlist);
        mmap.addAttribute("buslist", buslist);
        mmap.addAttribute("issuefxNo", bizType + nowDateStr + numStr);
        mmap.addAttribute("createrName", createrName);
        mmap.addAttribute("createrPhone", createrPhone);
        mmap.addAttribute("businessOrgs", businessOrgs);
        mmap.addAttribute("issueOrgs", issueOrgs);
        mmap.addAttribute("issuefxId", IdUtils.fastSimpleUUID());
        //问题描述
        mmap.addAttribute("issuefxText", checkSheet.getHiddenText());
        //影响描述
        mmap.addAttribute("issuefxImpact", checkSheet.getAffectBusiness());
        //隐患排查单号
        mmap.addAttribute("csNo",checkSheet.getCsNo());
        return prefix + "/wentiAdd";
    }
    /**
     * 发起隐患排查单流程
     */
    @Log(title = "发起隐患排查单", businessType = BusinessType.INSERT)
    @PostMapping("/startProcess")
    @ResponseBody
    @Transactional
    public AjaxResult startProcess(CheckSheet checkSheet) {
        try {
            CheckSheet ct = checkSheetService.selectCheckSheetById(checkSheet.getCsId());
            checkSheet.setStatus("1");
            if (ct == null) {
                checkSheetService.insertCheckSheet(checkSheet);
                ct = checkSheet;
            }
            checkSheetService.updateCheckSheet(checkSheet);

            Map<String, Object> mp = new HashMap<>();
            mp.put("checkId", ct.getCheckId());
            mp.put("technologyauditId", ct.getTechnologyauditId());
            mp.put("createId", ct.getCreateId());
            List<ProcessInstance> pro=activitiCommService.processInfo(ct.getCsId());
            if(pro.size()>0){
                return error("该业务单号已有流程启动");
            }
            activitiCommService.startProcess(ct.getCsId(), "PMYH", mp);
            return toAjax(1);
         }catch (Exception e){
             e.printStackTrace();
            return error("流程发起失败，请刷新页面重新发起！");
        }
    }

    /**
     * 修改隐患排查单
     */
    @GetMapping("/edit/{csId}")
    @Transactional
    public String edit(@PathVariable("csId") String csId, ModelMap mmap)
    {
        CheckSheet checkSheet = checkSheetService.selectCheckSheetById(csId);
        String sysName="";
        if(StringUtils.isNotEmpty(checkSheet.getSysid())){
            sysName=applicationManagerService.selectOgSysBySysId(checkSheet.getSysid()).getCaption();
        }
        OgPerson ogPerson=iOgPersonService.selectOgPersonById(checkSheet.getCreateId());
        mmap.addAttribute("sysName",sysName);
        mmap.addAttribute("createName",ogPerson.getpName());
        mmap.addAttribute("createOrgName",ogPerson.getOrgname());
        mmap.addAttribute("checkSheet", checkSheet);
        return prefix + "/edit";
    }
    /**
     * 处理隐患排查单
     */
    @GetMapping("/deal")
    public String deal(String csId,String description, ModelMap mmap)
    {
        CheckSheet checkSheet = checkSheetService.selectCheckSheetById(csId);
        String sysName="";
        if(StringUtils.isNotEmpty(checkSheet.getSysid())){
           sysName=applicationManagerService.selectOgSysBySysId(checkSheet.getSysid()).getCaption();
        }
        OgPerson ogPerson=iOgPersonService.selectOgPersonById(checkSheet.getCreateId());
        mmap.addAttribute("sysName",sysName);
        mmap.addAttribute("createName",ogPerson.getpName());
        mmap.addAttribute("createOrgName",ogPerson.getOrgname());
        mmap.addAttribute("checkSheet", checkSheet);
        mmap.addAttribute("description", description);
        if("shenhe".equals(description)){
            return prefix + "/shenheView";
        }
        if("chuli".equals(description)){
            return prefix+"/chuliView";
        }
        return prefix+"/view";
    }
    /**
     * 修改隐患排查单
     */
    @GetMapping("/view/{csId}")
    public String view(@PathVariable("csId") String csId, ModelMap mmap)
    {
        CheckSheet checkSheet = checkSheetService.selectCheckSheetById(csId);
        String sysName="";
        if(StringUtils.isNotEmpty(checkSheet.getSysid())){
            sysName=applicationManagerService.selectOgSysBySysId(checkSheet.getSysid()).getCaption();
        }
        OgPerson ogPerson=iOgPersonService.selectOgPersonById(checkSheet.getCreateId());
        mmap.addAttribute("sysName",sysName);
        mmap.addAttribute("createName",ogPerson.getpName());
        mmap.addAttribute("createOrgName",ogPerson.getOrgname());
        mmap.addAttribute("checkSheet", checkSheet);
        return prefix + "/view";
    }
    /**
     * 修改隐患排查单
     */
    @GetMapping("/selectPerson/{rId}")
    public String selectPerson(@PathVariable("rId") String rId, ModelMap mmap)
    {
        mmap.put("rId", rId);
        return prefix + "/selectBusinessAudit";
    }
    /**
     * 修改隐患排查单
     */
    @GetMapping("/selectOneApplication")
    public String selectOneApplication( ModelMap mmap)
    {
        return prefix + "/selectOneApplication";
    }
    /**
     * 修改保存隐患排查单
     */
    @Log(title = "隐患排查单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(CheckSheet checkSheet)
    {
        return toAjax(checkSheetService.updateCheckSheet(checkSheet));
    }

    /**
     * 删除隐患排查单
     */
    @Log(title = "隐患排查单", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        CheckSheet checkSheet=checkSheetService.selectCheckSheetById(ids);
        if(checkSheet!=null&&checkSheet.getStatus()!=null&&"0".equals(checkSheet.getStatus())){
            return toAjax(checkSheetService.deleteCheckSheetByIds(ids));
        }else {
            return error("只有待提交状态的隐患排查单可以删除！");
        }
    }

}
