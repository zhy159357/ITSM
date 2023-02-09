package com.ruoyi.activiti.controller.itsm.issueList;

import com.ruoyi.activiti.constants.ImBizIssueConstants;
import com.ruoyi.activiti.domain.ImBizIssuefx;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.ICommonService;
import com.ruoyi.activiti.service.IImBizIssuefxService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.DictConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.json.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysDeptService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 问题单流程操作
 * @author zx
 */
@Controller
@RequestMapping("issueList/activiti")
public class AssessmentController extends BaseController
{
    private String prefix = "issueList/build";
    private String prefixinside="issueList/inside";
    private String prefixStatement="issueList/table";

    @Autowired
    private IImBizIssuefxService issueService;
    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private ICommonService commonService;
    @Autowired
    private IPubParaValueService iPubParaValueService;
    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IOgPersonService ogPersonService;
    /**
     * 评估问题单列表
     * @param mmap
     * @return
     * zx
     */
    @GetMapping()
    public String operlog(ModelMap mmap) {
        List<OgOrg> issueOrgs = issueService.selectOgOrgList();
        mmap.addAttribute("issueOrgs", issueOrgs);
        mmap.addAttribute("type","pinggu");
        return prefix + "/pingguissue";
    }
    /**
     * 转技术处理
     * @param mmap
     * @return
     * zx
     */
    @GetMapping("/goJscl/{issuefxId}/{type}")
    public String goJscl(@PathVariable("issuefxId") String issuefxId,@PathVariable("type")String type,ModelMap mmap) {
        mmap.addAttribute("issuefxId",issuefxId);
        mmap.addAttribute("type",type);
        return prefix + "/zfjscl";
    }
    /**
     * 预解决
     * @param mmap
     * @return
     * zx
     */
    @GetMapping("/goYjj/{issuefxId}/{type}")
    public String goYjj(@PathVariable("issuefxId") String issuefxId,@PathVariable("type")String type,ModelMap mmap) {
        mmap.addAttribute("issuefxId",issuefxId);
        mmap.addAttribute("type",type);
        return prefix + "/yjj";
    }
    /**
     * 受理
     * @param mmap
     * @return
     * zx
     */
    @GetMapping("/goShouli/{issuefxId}")
    public String goShouli(@PathVariable("issuefxId") String issuefxId,ModelMap mmap) {
        mmap.addAttribute("issuefxId",issuefxId);
        return prefix + "/shouli";
    }
    /**
     * 转业务处理
     * @param mmap
     * @return
     * zx
     */
    @Transactional
    @GetMapping("/goYwcl/{issuefxId}/{type}")
    public String goYwcl(@PathVariable("issuefxId") String issuefxId,@PathVariable("type") String type,ModelMap mmap) {
        //获取字典项
        List<PubParaValue> businessOrgs = issueService.selectIssuesourceList(DictConstants.VM_DEPT);//业务部门
        ImBizIssuefx imBizIssuefx = issueService.selectImBizIssuefxById(issuefxId);
        String orgId = imBizIssuefx.getBusinessOrg();
        String orgName = "";
        for (PubParaValue pubParaValue : businessOrgs) {
            if (pubParaValue.getParaValueId().equals(orgId)) {
                orgName = pubParaValue.getValueDetail();
                break;
            }
        }
        List<OgPerson> buslist = new ArrayList<>();
        if (!CollectionUtils.isEmpty(businessOrgs)) {
            OgPerson ogPerson = new OgPerson();
            ogPerson.setOrgname(orgName);
            ogPerson.setrId(ImBizIssueConstants.ROLE_YWFUHE);
            buslist = commonService.selectPersonByOrgAndRole(ogPerson);
        }
        mmap.addAttribute("buslist", buslist);
        mmap.addAttribute("businessOrgs", businessOrgs);
        mmap.addAttribute("issuefxId",issuefxId);
        mmap.addAttribute("type",type);
        mmap.addAttribute("issuefx",imBizIssuefx);
        return prefix + "/zfywcl";
    }
    /**
     * 转问题经理
     * @param mmap
     * @return
     * zx
     */
    @GetMapping("/goWtjl/{issuefxId}/{type}")
    public String goWtjl(@PathVariable("issuefxId") String issuefxId,@PathVariable("type") String type,ModelMap mmap) {
        OgPerson ogPerson = new OgPerson();
        //问题经理
        ogPerson.setrId(ImBizIssueConstants.ROLE_WTJINGLI);
        List<OgPerson> list = commonService.selectPersonByOrgAndRole(ogPerson);
        mmap.addAttribute("issuefxId",issuefxId);
        mmap.addAttribute("type",type);
        mmap.addAttribute("users",list);
        return prefix + "/wtjl";
    }

    /**
     * 问题单列表
     * @param mmap
     * @return
     * zx
     */
    @GetMapping("/go/{type}")
    public String goYwfh(@PathVariable("type") String type,ModelMap mmap) {
        List<OgOrg> issueOrgs = issueService.selectOgOrgList();
        mmap.addAttribute("issueOrgs", issueOrgs);
        mmap.addAttribute("type",type);
        return prefix + "/pingguissue";
    }
    /**
     * 版本
     * @param mmap
     * @return
     * zx
     */
    @GetMapping("/goVersion")
    public String goVersion(ModelMap mmap) {
        List<OgOrg> issueOrgs = issueService.selectOgOrgList();
        return prefix + "/versionlist";
    }


    /**
     * 发起应用问题单
     * @param issue
     * @return
     * zx
     */
    @Log(title = "发起应用问题单", businessType = BusinessType.INSERT)
    @PostMapping("/startProcess")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult StartProcess(@Validated ImBizIssuefx issue){
       logger.debug("=======问题单流程启动："+issue.toString());
        try{
            ImBizIssuefx is=   issueService.selectImBizIssuefxById(issue.getIssuefxId());
            if(is==null){
                //直接提交
                issue.setCreaterId(ShiroUtils.getUserId());
                if (null != issue.getReporttime()) {
                    String businessOrg= StringUtils.removeDuplicationString(issue.getBusinessOrg());
                    String businessid=StringUtils.removeDuplicationString(issue.getBusinessId());
                    //去重复
                    issue.setBusinessOrg(businessOrg);
                    issue.setBusinessId(businessid);

                    issue.setReporttime(DateUtils.dateTimeNow());
                }
                issueService.insertIssue(issue);
                is=issue;
            }else{
                if(StringUtils.isNotEmpty(issue.getIssuefxNo())){
                    is=issue;
                }
            }
            Map<String,Object> reMap=new HashMap<>();
            //审核人
            reMap.put("reviewerId",is.getReviewerId());
            //评估人
            if(StringUtils.isNotEmpty(is.getMultiusers())){
                reMap.put("reCode",0);
                reMap.put("multiusers",is.getMultiusers());
            }else {
                reMap.put("reCode",1);
            }
            //技术处理人
            reMap.put("auditId",is.getAuditId());
            //创建人
            reMap.put("creatId", ShiroUtils.getUserId());
            //业务复核人
            reMap.put("businessId",is.getBusinessId());
            reMap.put("userId",ShiroUtils.getUserId());
            reMap.put("issueNo",is.getIssuefxNo());
            reMap.put("issueTitle",is.getIssuefxName());
            reMap.put("inside_deal",ImBizIssueConstants.ROLE_INSIDE);
            List<ProcessInstance> pro=activitiCommService.processInfo(is.getIssuefxId());
            if(pro.size()>0){
               logger.debug("=======问题单启动失败：businesskey 重复");
                return error("该业务单号已有流程启动");
            }
            issueService.updateIssue(is);
            activitiCommService.startProcess(is.getIssuefxId(),"PMYY",reMap);
            return toAjax(1);
        }catch (Exception e){
           logger.debug("=======问题单启动失败:"+issue.toString());
            e.printStackTrace();
            return error("问题单发起失败，请刷新页面重新发起");
        }
    }

    /**
     * 问题单待办外部列表
     * @param issue
     * @return
     * zx
     */
    @Log(title = "问题单列表", businessType = BusinessType.INSERT)
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ImBizIssuefx issue,ModelMap map) {
        String type=issue.getParams().get("type").toString();
        List<Map<String,Object>> userList=new ArrayList<>();
        List<Map<String, Object>> groupList=new ArrayList<>();
        if("chuli".equals(type)){
            userList=activitiCommService.userTask("PMYY","chuli");
            userList.addAll(activitiCommService.userTask("PMYY","yujiejue"));
            userList.addAll(activitiCommService.userTask("PMYY","jiejue"));
        }else if("close".equals(type)){
            userList=activitiCommService.userTask("PMYY",type);
            userList.addAll(activitiCommService.userTask("PMYY","inside_close"));
        }
        else {
            userList=activitiCommService.userTask("PMYY",type);
            groupList=activitiCommService.groupTasks("PMYY",type);
        }

        userList.addAll(groupList);
        List<ImBizIssuefx> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(issue.getCurrentState())) {
            String[] state = issue.getCurrentState().split(",");
            List<String> statecollection = Arrays.stream(state).collect(Collectors.toList());
            issue.getParams().put("states", statecollection);
        }
        if(!CollectionUtils.isEmpty(userList)){
            for(Map<String,Object> mp:userList){
                issue.setIssuefxId(mp.get("businesskey").toString());
                List<ImBizIssuefx> im=  issueService.selectIssueList(issue);
                if(im.size()>0){
                    for(ImBizIssuefx ib:im){
                        ib.getParams().put("type",mp.get("description"));
                        list.add(ib);

                    }
                }
            }
        }
        if (list.size() > 1) {
            list = list.stream().sorted(Comparator.comparing(ImBizIssuefx::getCreatTime).reversed()).collect(Collectors.toList());
        }
        return getDataTable_ideal(list);
    }

    /**
     * 数据中心任务待办
     * @param issue
     * @param map
     * @return
     */
    @Log(title = "问题单列表", businessType = BusinessType.INSERT)
    @PostMapping("/dataList")
    @ResponseBody
    public TableDataInfo datalist(ImBizIssuefx issue,ModelMap map) {

        //数据中心处理
        List<Map<String,Object>> userList1=activitiCommService.userTask("PMYY","inside_deal");
        //预解决
        List<Map<String,Object>> userList2=activitiCommService.userTask("PMYY","inside_yujiejue");
        //解决
        List<Map<String,Object>> userList3=activitiCommService.userTask("PMYY","inside_jiejue");
        userList1.addAll(userList2);
        userList1.addAll(userList3);
        List<ImBizIssuefx> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(issue.getCurrentState())) {
            String[] state = issue.getCurrentState().split(",");
            List<String> statecollection = Arrays.stream(state).collect(Collectors.toList());
            issue.getParams().put("states", statecollection);
        }
        if(!CollectionUtils.isEmpty(userList1)){
            for(Map<String,Object> mp:userList1){
                issue.setIssuefxId(mp.get("businesskey").toString());
                List<ImBizIssuefx> im=  issueService.selectIssueList(issue);
                if(im.size()>0){
                    for(ImBizIssuefx ib:im){
                        ib.getParams().put("type",mp.get("description"));
                        list.add(ib);

                    }
                }
            }
        }
        if (list.size() > 1) {
            list = list.stream().sorted(Comparator.comparing(ImBizIssuefx::getCreatTime).reversed()).collect(Collectors.toList());
        }
        return getDataTable_ideal(list);
    }
    /**
     *问题单评估
     * @param issue
     * @return
     * zx
     */
    @Log(title = "问题单评估", businessType = BusinessType.INSERT)
    @PostMapping("/pinggu")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult pinggu(ImBizIssuefx issue){
       logger.debug("=======问题单评估："+issue.toString());
        if(!issue.getCurrentState().equals("1")){
            return error("该事件单已评估！请刷新页面，确认问题单状态！");
        }
       
        Map<String, Object> reMap=new HashMap<>();
        String reCode = issue.getParams().get("reCode").toString();
        reMap.put("businessKey",issue.getIssuefxId());
        reMap.put("comment",issue.getParams().get("comment"));
        reMap.put("description","pinggu");
        reMap.put("processDefinitionKey","PMYY");
        reMap.put("reCode",reCode);
        String insideUserId=issue.getParams().get("dataCenterUserId")==null?"":issue.getParams().get("dataCenterUserId").toString();
        reMap.put("insideUserId",insideUserId);
        issue.setDealId(insideUserId);
        try{

            Task tk=activitiCommService.getTask(issue.getIssuefxId(),"pinggu");
            reMap.put("taskId",tk.getId());
            reMap.put("userId",ShiroUtils.getUserId());
            issueService.updateIssue(issue);
            if("0".equals(reCode)){
                activitiCommService.usersComplete(reMap);
            }else {
                activitiCommService.complete(reMap);
            }
           logger.debug("=======问题单评估结束："+issue.toString());
            return toAjax(1);
        }catch (Exception e){
           logger.debug("=======问题单评估出错："+issue.toString());
             e.printStackTrace();
            return error("评估出错！请刷新页面，确认任务状态!");
        }
    }
    /**
     *问题单业务复核
     * @param issue
     * @return
     * zx
     */
    @Log(title = "问题单复核", businessType = BusinessType.INSERT)
    @PostMapping("/fuhe")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult fuhe(ImBizIssuefx issue){
        if(!issue.getHanguptask().equals("401")){
            return error("该事件单已复核！请刷新页面，确认问题单状态！");
        }
       logger.debug("=======问题单业务复核："+issue.toString());
        Map<String, Object> reMap=new HashMap<>();
        reMap.put("businessKey",issue.getIssuefxId());
        reMap.put("comment",issue.getBufuheDesc());
        reMap.put("processDefinitionKey","PMYY");
        reMap.put("description","fuhe");
        try {
            Task tk=activitiCommService.getTask(issue.getIssuefxId(),"fuhe");
            reMap.put("taskId",tk.getId());
            reMap.put("userId",ShiroUtils.getUserId());
            issueService.updateIssue(issue);
            activitiCommService.usersComplete(reMap);
           logger.debug("=======问题单业务复核完成");
            return toAjax(1);
        }catch (Exception e){
           logger.debug("=======问题单业务复核失败："+issue.toString());
            e.printStackTrace();
            return error("复核出错，请刷新也面，确认任务状态！");
        }

    }
    /**
     *问题单业务处理
     * @param issue
     * @return
     * zx
     */
    @Log(title = "问题单业务处理", businessType = BusinessType.INSERT)
    @PostMapping("/busDeal")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult busDeal(ImBizIssuefx issue){
        if(!issue.getCurrentState().equals("7")){
            return error("该事件单已被业务处理！请刷新页面，确认问题单状态！");
        }
       logger.debug("=======问题单业务处理："+issue.toString());
        Map<String, Object> reMap=new HashMap<>();
        reMap.put("businessKey",issue.getIssuefxId());
        reMap.put("comment",issue.getBuDealDesc());
        reMap.put("processDefinitionKey","PMYY");
        reMap.put("description","ywshouli");
        try {
            Task tk=activitiCommService.getTask(issue.getIssuefxId(),"ywshouli");
            reMap.put("taskId",tk.getId());
            reMap.put("userId",ShiroUtils.getUserId());
            issueService.updateIssue(issue);
            activitiCommService.usersComplete(reMap);
           logger.debug("=======问题单业务处理完成");
            return toAjax(1);
        }catch (Exception e){
           logger.debug("=======问题单业务处理失败："+issue.toString());
            e.printStackTrace();
            return error("业务处理出错，请刷新也面，确认任务状态！");
        }

    }
    /**
     *问题单编辑重新提交
     * @param issue
     * @return
     * zx
     */
    @Log(title = "问题单编辑重新提交", businessType = BusinessType.INSERT)
    @PostMapping("/submitEdit")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult submitEdit(ImBizIssuefx issue){
        if(!issue.getCurrentState().equals("10")){
            return error("该事件单已被处理！请刷新页面，确认问题单状态！");
        }
       logger.debug("=======问题单退回重新提交"+issue.toString());
        Map<String, Object> reMap=new HashMap<>();
        reMap.put("businessKey",issue.getIssuefxId());
        reMap.put("comment","问题单编辑重新操作");
        reMap.put("reCode",issue.getParams().get("reCode"));
        reMap.put("description","edit");
        try {
            Task tk=activitiCommService.getTask(issue.getIssuefxId(),"edit");
            reMap.put("taskId",tk.getId());
            reMap.put("userId",ShiroUtils.getUserId());
            issueService.updateIssue(issue);
            activitiCommService.complete(reMap);
           logger.debug("=======问题单退回重新提交完成");
            return toAjax(1);
        }catch (Exception e){
           logger.debug("=======问题单退回重新提交失败:"+issue.toString());
            e.printStackTrace();
            return error("重新提交出错，请刷新页面，确认问题状态！");
        }

    }
    /**
     *问题单业务处理
     * @param issue
     * @return
     * zx
     */
    @Log(title = "问题单业务处理", businessType = BusinessType.INSERT)
    @PostMapping("/deal")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult deal(ImBizIssuefx issue){
       logger.debug("=======问题单业务处理/deal:"+issue.toString());
        Map<String, Object> reMap=new HashMap<>();
        reMap.put("businessKey",issue.getIssuefxId());
        reMap.put("comment",issue.getBuDealDesc());
        reMap.put("description","ywshouli");
        reMap.put("processDefinitionKey","PMYY");
        try {
            Task tk=activitiCommService.getTask(issue.getIssuefxId(),"ywshouli");
            reMap.put("taskId",tk.getId());
            reMap.put("userId",ShiroUtils.getUserId());
            issueService.updateIssue(issue);
            activitiCommService.usersComplete(reMap);
           logger.debug("=======问题单业务处理/deal完成");
            return toAjax(1);
        }catch (Exception e){
           logger.debug("=======问题单业务处理失败/deal:"+issue.toString());
             e.printStackTrace();
            return error("问题受理出错，请刷新页面，确认任务状态！");
        }

    }
    /**
     *解决问题
     * @param issue
     * @return
     * zx
     */
    @Log(title = "解决问题", businessType = BusinessType.INSERT)
    @PostMapping("/jiejue")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult jiejue(ImBizIssuefx issue){
       logger.debug("=======问题单解决："+issue.toString());
        if(!issue.getCurrentState().equals("8")){
            return error("该问题单已被处理！请刷新页面，确认问题单状态！");
        }
        Map<String, Object> reMap=new HashMap<>();
        reMap.put("businessKey",issue.getIssuefxId());
        reMap.put("comment",issue.getDealDetail());
        reMap.put("processDefinitionKey","PMYY");
        reMap.put("description","jiejue");
        try{
            Task tk=activitiCommService.getTask(issue.getIssuefxId(),"jiejue");
            reMap.put("taskId",tk.getId());
            reMap.put("userId",ShiroUtils.getUserId());
            issue.setRealityTime(DateUtils.dateTimeNow());
            issueService.updateIssue(issue);
            activitiCommService.usersComplete(reMap);
           logger.debug("=======问题单解决完成");
            return toAjax(1);
        }catch (Exception e){
           logger.debug("=======问题单解决失败："+issue.toString());
            e.printStackTrace();
            return error("问题解决失败，请刷新页面，确认任务状态！");
        }

    }
    /**
     *关闭问题
     * @param issue
     * @return
     * zx
     */
    @Log(title = "关闭问题", businessType = BusinessType.INSERT)
    @PostMapping("/close")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult close(ImBizIssuefx issue){
       logger.debug("=======问题单关闭："+issue.toString());
        if(!issue.getCurrentState().equals("9")){
            return error("该问题单已被处理！请刷新页面，确认问题单状态！");
        }
        Map<String, Object> reMap=new HashMap<>();
        reMap.put("businessKey",issue.getIssuefxId());
        reMap.put("comment",issue.getParams().get("comment"));
        reMap.put("reCode",issue.getParams().get("reCode"));
        reMap.put("processDefinitionKey","PMYY");
        reMap.put("description","close");
        try {
            Task tk=activitiCommService.getTask(issue.getIssuefxId(),"close");
            reMap.put("taskId",tk.getId());
            reMap.put("userId",ShiroUtils.getUserId());
            issueService.updateIssue(issue);
            activitiCommService.usersComplete(reMap);
           logger.debug("=======问题单关闭完成");
            return toAjax(1);
        }catch (Exception e){
           logger.debug("=======问题单关闭失败："+issue.toString());
             e.printStackTrace();
            return error("问题关闭出错，请刷新页面，确认任务状态！");
        }

    }
    /**
     *问题处理，受理，退回修改，转发技术，转发业务，转问题经理
     * @param issue
     * @return
     * zx
     */
    @Log(title = "问题处理，受理，退回修改，转发技术，转发业务，转问题经理", businessType = BusinessType.INSERT)
    @PostMapping("/wentichuli")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult wentichuli(ImBizIssuefx issue){
       logger.debug("=======问题单处理"+issue.getParams().get("type").toString()+":"+issue.toString());
       ImBizIssuefx issuefx=issueService.selectImBizIssuefxById(issue.getIssuefxId());
        Map<String, Object> reMap=new HashMap<>();
        String type=issue.getParams().get("type").toString();
        if(!issuefx.getCurrentState().equals("5")&&"chuli".equals(type)){
            return error("该问题单已被处理！请刷新页面，确认问题单状态！");
        }
        if(!issuefx.getCurrentState().equals("6")&&"yujiejue".equals(type)){
            return error("该问题单已被处理！请刷新页面，确认问题单状态！");
        }
        String shouli=issue.getParams().get("shouli")==null?"":issue.getParams().get("shouli").toString();
        String reCode=issue.getParams().get("reCode").toString();
        reMap.put("businessKey",issue.getIssuefxId());
        reMap.put("processDefinitionKey","PMYY");
        reMap.put("comment",issue.getParams().get("comment").toString());
        reMap.put("description",type);
        ImBizIssuefx imBizIssuefx= issueService.selectImBizIssuefxById(issue.getIssuefxId());
        List<PubParaValue> pvList= iPubParaValueService.selectPubParaValueByParaName("WT_MULTICOUNT");
        if(pvList.size()<1){
           logger.debug("=======问题单处理"+type+"失败，转发次数上限没有设置:"+issue.toString());
            return AjaxResult.error("问题发现单转发次数上限未设定");
        }
        //已转发次数
        long num=imBizIssuefx.getMulticount();
        //最大转发次数
        long maxNum=Long.valueOf(pvList.get(0).getValue());
        //转技术处理,问题经理
        if(ImBizIssueConstants.DEAL_ZJISHU.equals(reCode)||ImBizIssueConstants.DEAL_WTJINGLI.equals(reCode)){
            if("1".equals(reCode)){
                if(num>=maxNum){
                    return AjaxResult.error("转发次数已达"+num+"次！无法转发！");
                }
                num+=1;
            }
            reCode="1";
            reMap.put("auditId",issue.getAuditId());
        }
        //转业务
        if(ImBizIssueConstants.DEAL_ZYEWU.equals(reCode)){
            if(num>=maxNum){
                return AjaxResult.error("转发次数已达"+num+"次！无法转发！");
            }
            num+=1;
            reMap.put("busId",issue.getBusinessId());
        }
        reMap.put("reCode",reCode);
        try {
            Task tk=activitiCommService.getTask(issue.getIssuefxId(),type);
            reMap.put("taskId",tk.getId());
            reMap.put("userId",ShiroUtils.getUserId());
            if(StringUtils.isNotEmpty(shouli)){
                issue.setAcceptTime(DateUtils.dateTimeNow());
            }
            if("yujiejue".equals(type)){
                issue.setPreSolutionTime(DateUtils.dateTimeNow());
            }
            issue.setMulticount(num);
            issueService.updateIssue(issue);
            activitiCommService.usersComplete(reMap);
           logger.debug("=======问题单处理"+type+"完成");
            return toAjax(1);
        }catch (Exception e){
           logger.debug("=======问题单处理"+type+"失败："+issue.toString());
             e.printStackTrace();
            return AjaxResult.warn("问题处理出错，请刷新页面，确认任务状态！");
        }
    }
    /**
     *问题审核
     * @param issue
     * @return
     * zx
     */
    @Log(title = "问题单审核", businessType = BusinessType.INSERT)
    @PostMapping("/shenhe")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult shenhe(ImBizIssuefx issue){
       logger.debug("=======问题单审核："+issue.toString());
        if(!issue.getCurrentState().equals("2")){
            return error("该问题单已被处理！请刷新页面，确认问题单状态！");
        }
        Map<String, Object> reMap=new HashMap<>();
        reMap.put("businessKey",issue.getIssuefxId());
        reMap.put("comment",issue.getParams().get("comment"));
        String reCode=issue.getParams().get("reCode").toString();
        reMap.put("processDefinitionKey","PMYY");
        reMap.put("groupId",ImBizIssueConstants.ROLE_WTFENFA);
        issue =issueService.selectImBizIssuefxById(issue.getIssuefxId());
        String inside=issue.getIsInside();
        try {
            if(!"1".equals(reCode)){
                //转外部 修改问题归属
                if("0".equals(inside)){
                    reCode="0";
                }
                if("1".equals(inside)){
                    reCode="2";
                }
                issueService.updateIssue(issue);
            }
            reMap.put("reCode",reCode);
            Task tk=activitiCommService.getTask(issue.getIssuefxId(),"shenhe");
            reMap.put("taskId",tk.getId());
            reMap.put("userId",ShiroUtils.getUserId());
            activitiCommService.complete(reMap);
           logger.debug("=======问题单审核完成=====");
            return toAjax(1);
        }catch (Exception e){
           logger.debug("=======问题单审核失败："+issue.toString());
            e.printStackTrace();
            return error("审核出错,请刷新页面，确认任务状态！");
        }

    }
    /**
     *问题分发
     * @param issue
     * @return
     * zx
     */
    @Log(title = "问题单分发", businessType = BusinessType.INSERT)
    @PostMapping("/fenfa")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult fenfa(ImBizIssuefx issue){
       logger.debug("=======问题单分发："+issue.toString());

        Map<String, Object> reMap=new HashMap<>();
        reMap.put("businessKey",issue.getIssuefxId());
        reMap.put("processDefinitionKey","PMYY");
        reMap.put("comment",issue.getParams().get("comment").toString());

        try{
            Task tk=activitiCommService.getTask(issue.getIssuefxId(),"fenfa");
            reMap.put("taskId",tk.getId());
            reMap.put("userId",ShiroUtils.getUserId());
            issue.setDistributeTime(DateUtils.dateTimeNow());
            issueService.updateIssue(issue);
            activitiCommService.usersComplete(reMap);
           logger.debug("=======问题单分发完成！");
            return toAjax(1);
        }catch (Exception e){
           logger.debug("=======问题单分发失败："+issue.toString());
            e.printStackTrace();
            return error("问题分发出错！请刷新页面，确认任务状态！");
        }

    }
    /**
     * 查看问题单
     */
    @GetMapping("/view")
    public String view(String issuefxId,String type, ModelMap mmap) {
        mmap=issueService.view(issuefxId,mmap);
        mmap.addAttribute("type",type);
        mmap.addAttribute("issuefxId",issuefxId);
        if("fuhe".equals(type)){
            return prefix+"/fuheview";
        }
        if("jiejue".equals(type)){
            return prefix+"/jiejueview";
        }
        if("close".equals(type)){
            return prefix+"/closeview";
        }
        //内部条线关闭页面
        if("inside_close".equals(type)){
            return prefixinside+"/dataCloseView";
        }
        if("edit".equals(type)){
            return prefix+"/backedit";
        }
        if("ywshouli".equals(type)){
            return prefix+"/budealview";
        }
        if("createdit".equals(type)){
            return prefix+"/edit";
        }
        if("flowview".equals(type)){
            return prefix+"/flowview";
        }
        if("pinggu".equals(type)){
            return prefixinside+"/pingguview";
        }
        return prefix + "/view";
    }

    /**
     * 数据中心处理 内部条线页面跳转
     * @param issuefxId
     * @param type
     * @param mmap
     * @return
     */
    @GetMapping("/goDataDeal/{issuefxId}/{type}")
    public String goDataDeal(@PathVariable("issuefxId") String issuefxId,@PathVariable("type")String type,
                             ModelMap mmap){
        mmap=issueService.view(issuefxId,mmap);
        mmap.addAttribute("type",type);
        mmap.addAttribute("issuefxId",issuefxId);
        if("inside_deal".equals(type)){
            return  prefixinside+"/dealView";
        }
        if("inside_yujiejue".equals(type)){
            return  prefixinside+"/yujiejueView";
        }
        if("inside_jiejue".equals(type)){
            return  prefixinside+"/jiejueView";
        }
        return prefix + "/view";
    }

    /**
     * 内部条线数据中心处理
     * @param issue
     * @return
     */
    @PostMapping("/dealData")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult dealData(ImBizIssuefx issue){
       logger.debug("======问题单内部条线数据中心处理："+issue.toString());
        if(!issue.getCurrentState().equals("12")){
            return error("该问题单已被处理！请刷新页面，确认问题单状态！");
        }
        Map<String, Object> reMap=new HashMap<>();
        String reCode=issue.getParams().get("reCode").toString();
        reMap.put("businessKey",issue.getIssuefxId());
        reMap.put("processDefinitionKey","PMYY");
        reMap.put("comment",issue.getParams().get("comment").toString());
        reMap.put("reCode",reCode);
        if("2".equals(reCode)){
            reMap.put("insideUserId",issue.getParams().get("dataCenterUserId").toString());
        }
        //转外部 数据不全
        if("3".equals(reCode)){
            //技术处理人
            reMap.put("auditId",issue.getAuditId());
            //业务复核人
            reMap.put("businessId",issue.getBusinessId());
            reMap.put("reCode",0);
            issue.setIsInside("0");
            issueService.updateIssue(issue);
        }
        try{
            Task tk=activitiCommService.getTask(issue.getIssuefxId(),"inside_deal");
            reMap.put("taskId",tk.getId());
            reMap.put("userId",ShiroUtils.getUserId());
            activitiCommService.usersComplete(reMap);
           logger.debug("======问题单内部条线数据中心处理完成！=====");
            return toAjax(1);
        }catch (Exception e){
           logger.debug("======问题单内部条线数据中心处理失败："+issue.toString());
            e.printStackTrace();
            return error("问题处理出错！请刷新页面，确认任务状态！");
        }
    }

    /**
     * 内部条线预解决
     * @param issue
     * @return
     */
    @PostMapping("/yuJieJue")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult yuJJ(ImBizIssuefx issue){
       logger.debug("========问题单内部条线预解决："+issue.toString());
        if(!issue.getCurrentState().equals("13")){
            return error("该问题单已被处理！请刷新页面，确认问题单状态！");
        }
        Map<String, Object> reMap=new HashMap<>();
        reMap.put("businessKey",issue.getIssuefxId());
        reMap.put("processDefinitionKey","PMYY");
        reMap.put("comment",issue.getUnitSchedule());
        try{
            Task tk=activitiCommService.getTask(issue.getIssuefxId(),"inside_yujiejue");
            reMap.put("taskId",tk.getId());
            reMap.put("userId",ShiroUtils.getUserId());
            issue.setDistributeTime(DateUtils.dateTimeNow());
            issueService.updateIssue(issue);
            activitiCommService.complete(reMap);
           logger.debug("========问题单内部条线预解决完成！=========");
            return toAjax(1);
        }catch (Exception e){
           logger.debug("========问题单内部条线预解决失败："+issue.toString());
            e.printStackTrace();
            return error("问题预解决出错！请刷新页面，确认任务状态！");
        }
    }

    /**
     * 内部条线解决
     * @param issue
     * @return
     */
    @PostMapping("/jieJueInside")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult jieJue(ImBizIssuefx issue){
       logger.debug("========问题单内部条线解决："+issue.toString());
        if(!issue.getCurrentState().equals("14")){
            return error("该问题单已被处理！请刷新页面，确认问题单状态！");
        }
        Map<String, Object> reMap=new HashMap<>();
        String reCode=issue.getParams().get("reCode").toString();
        reMap.put("businessKey",issue.getIssuefxId());
        reMap.put("processDefinitionKey","PMYY");
        reMap.put("comment",issue.getUnitSchedule());
        reMap.put("reCode",reCode);
        try{
            Task tk=activitiCommService.getTask(issue.getIssuefxId(),"inside_jiejue");
            reMap.put("taskId",tk.getId());
            reMap.put("userId",ShiroUtils.getUserId());
            issue.setDistributeTime(DateUtils.dateTimeNow());
            issueService.updateIssue(issue);
            activitiCommService.complete(reMap);
           logger.debug("========问题单内部条线解决完成！========");
            return toAjax(1);
        }catch (Exception e){
           logger.debug("========问题单内部条线解决失败："+issue.toString());
            e.printStackTrace();
            return error("问题解决出错！请刷新页面，确认任务状态！");
        }
    }

    /**
     * 数据中心关闭
     * @param issue
     * @return
     */
    @PostMapping("/dataClose")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult dataClose(ImBizIssuefx issue){
        if(!issue.getCurrentState().equals("9")){
            return error("该问题单已处理！请刷新页面，确认问题单状态！");
        }
        Map<String, Object> reMap=new HashMap<>();
        reMap.put("businessKey",issue.getIssuefxId());
        reMap.put("processDefinitionKey","PMYY");
        reMap.put("comment",issue.getParams().get("comment"));
        try{
            Task tk=activitiCommService.getTask(issue.getIssuefxId(),"inside_close");
            reMap.put("taskId",tk.getId());
            reMap.put("userId",ShiroUtils.getUserId());
            issue.setDistributeTime(DateUtils.dateTimeNow());
            issueService.updateIssue(issue);
            activitiCommService.complete(reMap);
            return toAjax(1);
        }catch (Exception e){
            e.printStackTrace();
            return error("问题解决出错！请刷新页面，确认任务状态！");
        }
    }
    /**
     * 统计报表跳转
     * @param type
     * @param mmap
     * @return
     */
    @GetMapping("/goStatement/{type}")
    public String goDataDeal(@PathVariable("type") String type, ModelMap mmap){
        if("statementDataCenter".equals(type)){
            //丰台中心
            List<OgOrg> listOrg=deptService.selectAllChildOrg(ImBizIssueConstants.ORG_DATACENTER_FENGTAI);
            //合肥中心
            listOrg.addAll(listOrg.size(),deptService.selectAllChildOrg(ImBizIssueConstants.ORG_DATACENTER_HEFEI));
            //亦庄中心
            listOrg.addAll(listOrg.size(),deptService.selectAllChildOrg(ImBizIssueConstants.ORG_DATACENTER_YIZHUNAG));
            listOrg = listOrg.stream().sorted(Comparator.comparing(OgOrg::getOrgName).reversed()).collect(Collectors.toList());
            Set<String> tittle=new HashSet<>();
            for(OgOrg og:listOrg){
                tittle.add(og.getOrgName());
            }
            mmap.put("tittle",tittle);
            return  prefixStatement+"/statementDataCenter";
        }
        if("statementBulid".equals(type)){
            return  prefixStatement+"/statementBulid";
        }
        return prefix + "/statementBulid";
    }

    /**
     * 查询建设单位报表
     * @param
     * @param  ModelMap
     * @return
     */
    @Log(title = "建设单位报表", businessType = BusinessType.INSERT)
    @PostMapping("/statementBulidList")
    @ResponseBody
    public TableDataInfo statementBulidList(ModelMap mp){
        List<Map<Object,Object>> list=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        map.put("xxkjb",ImBizIssueConstants.ORG_ZHXXKJB_ID);
        map.put("sjzx",ImBizIssueConstants.ORG_DATACENTER);
        map.put("jrcxb",ImBizIssueConstants.FINANCE_SAT_ID);
        map.put("xykzx",ImBizIssueConstants.CARD_CENTER_ID);
        map.put("glxxb",ImBizIssueConstants.MANAGE_INFO_ID);
        map.put("rjyfzx",ImBizIssueConstants.SOFTWARE_CENTER_ID);
        List<PubParaValue> plist=iPubParaValueService.selectPubParaValueByParaName("issue_status");
        for(PubParaValue pa:plist){
            if(!pa.getValueDetail().contains("数据中心")){
                map.put("status",pa.getValue());
                map.put("statusDetail",pa.getValueDetail());
                list.addAll(issueService.selectBulidList(map));
            }
        }
        return getDataTable(list);
    }

    /**
     * 查询数据中心报表
     * @param
     * @param
     * @return
     */
    @Log(title = "数据中心报表", businessType = BusinessType.INSERT)
    @PostMapping("/statementDataList")
    @ResponseBody
    public TableDataInfo statementDataList(ModelMap mp){
        //丰台中心
        List<OgOrg> listOrg=deptService.selectAllChildOrg(ImBizIssueConstants.ORG_DATACENTER_FENGTAI);
        //合肥中心
        listOrg.addAll(listOrg.size(),deptService.selectAllChildOrg(ImBizIssueConstants.ORG_DATACENTER_HEFEI));
        //亦庄中心
        listOrg.addAll(listOrg.size(),deptService.selectAllChildOrg(ImBizIssueConstants.ORG_DATACENTER_YIZHUNAG));
        Set<String> ids=new HashSet<>();
        Set<String> tittle=new HashSet<>();
        for(OgOrg og:listOrg){
            ids.add("'"+og.getOrgId()+"' as "+og.getOrgName());
            tittle.add("NVL(t."+og.getOrgName()+",0) as "+og.getOrgName());
        }
        Map<String,Object> map=new HashMap<>();
        map.put("orgIds",ids);
        map.put("tittle",tittle);
        List<Map<Object,Object>> list=new ArrayList<>();
        List<PubParaValue> plist=iPubParaValueService.selectPubParaValueByParaName("issue_status");
        for(PubParaValue pa:plist){
            map.put("status",pa.getValue());
            map.put("statusDetail",pa.getValueDetail());
            list.addAll(issueService.statementDataList(map));
        }
        return getDataTable(list);
    }
    @Log(title = "批量分发", businessType = BusinessType.INSERT)
    @PostMapping("/fenfaAll")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult fenfaAll(ImBizIssuefx issue){
       logger.debug("=======问题单批量分发："+issue.toString());
        String comment=issue.getParams().get("comment").toString();
        String id=issue.getIssuefxId();
        String[] ids=id.split(",");
        for(String issueId:ids){
            Map<String, Object> reMap=new HashMap<>();
            reMap.put("businessKey",issueId);
            reMap.put("processDefinitionKey","PMYY");
            reMap.put("comment",comment);
            ImBizIssuefx issuefx=issueService.selectImBizIssuefxById(issueId);
            if(!issuefx.getCurrentState().equals("3")){
                return error("问题单号为："+issuefx.getIssuefxNo()+"的问题单已处理！请刷新页面，确认问题单状态！");
            }
            try{
                Task tk=activitiCommService.getTask(issueId,"fenfa");
                ImBizIssuefx ie=issueService.selectImBizIssuefxById(issueId);
                reMap.put("taskId",tk.getId());
                reMap.put("userId",ShiroUtils.getUserId());
                ie.setDistributeTime(DateUtils.dateTimeNow());
                issueService.updateIssue(ie);
                activitiCommService.usersComplete(reMap);
               logger.debug("=======问题单分发完成！");
            }catch (Exception e){
               logger.debug("=======问题单分发失败："+issue.toString());
                e.printStackTrace();
                return error("问题分发出错！请刷新页面，确认任务状态！");
            }
        }
        return success();
    }

    /**
     * 转外部选择业务复核人，技术经理
     * @param eventId
     * @param map
     * @return
     */
    @GetMapping("/goRelay/{eventId}")
    public String relay(@PathVariable("eventId")String eventId,ModelMap map){
        map=issueService.view(eventId,map);
        return prefixinside+"/relay";
    }
}
