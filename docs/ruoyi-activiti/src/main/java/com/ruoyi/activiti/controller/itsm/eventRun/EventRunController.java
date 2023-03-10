package com.ruoyi.activiti.controller.itsm.eventRun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.activiti.constants.eventRunConstants;
import com.ruoyi.activiti.domain.EventRun;
import com.ruoyi.activiti.domain.PubFlowLog;
import com.ruoyi.activiti.mapper.DutyAccountMapper;
import com.ruoyi.activiti.service.*;
import com.ruoyi.activiti.utils.RestTemplateUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.ZtreeStr;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.es.api.KnowledgeApi;
import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.service.*;
import io.swagger.annotations.ApiOperation;
import net.sf.json.util.JSONUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ???????????????Controller
 *
 * @author zx
 * @date 2021-03-04
 */
@Controller
@RequestMapping("activiti/run")
public class EventRunController extends BaseController
{
    private String prefix = "run";

    @Autowired
    private EventRunService eventRunService;
    @Autowired
    private IIdGeneratorService idGeneratorService;
    @Autowired
    private IDutyTypeinfoService dutyTypeinfoService;
    @Autowired
    private IDutySchedulingService iDutySchedulingService;
    @Autowired
    private IOgPersonService ogPersonService;
    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private ICommonService commonService;
    @Autowired
    private OgPostService ogPostService;
    @Autowired
    private IOgUserService ogUserService;
    @Autowired
    private IPubParaValueService iPubParaValueService;
    @Autowired
    private IDutyAccountService iDutyAccountService;
    @Autowired
    private IPubFlowLogService pubFlowLogService;
    @Autowired
    private DutyAccountMapper dutyAccountMapper;
    @Autowired
    private IOgPersonService iOgPersonService;
    @Autowired(required=false)
    private KnowledgeApi KnowledgeApi;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private IOgUserPostService iOgUserPostService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ISysApplicationManagerService iSysApplicationManagerService;
    //????????????
    @Value("${FmYx.url}")
    private String eventUrl;
    @Value("${FmYx.monitorUrl}")
    private String monitorUrl;
    @Value("${FmYx.detailUrl}")
    private String detailUrl;
    //apikey
    @Value("${FmYx.apiKey}")
    private String apiKey;
    public Map<String,String>  switchList(String paraName){
        List<PubParaValue> list=iPubParaValueService.selectPubParaValueByParaName(paraName);
        Map<String,String> mp=new HashMap<>();
        for(PubParaValue pubParaValue:list){
            mp.put(pubParaValue.getValue(),pubParaValue.getValueDetail());
        }
        return mp;
    }
    /**
     * ????????????
     * @return
     */
    @GetMapping("/go/{type}")
    public String run(@PathVariable("type")String type,ModelMap mp)
    {
        mp.put("type",type);
        OgUserPost ogUserPost = new OgUserPost();
        ogUserPost.setUserId(ShiroUtils.getUserId());
        List<OgUserPost> ogUserPosts = iOgUserPostService.selectPostByUserId(ogUserPost);
        //??????
        for(OgUserPost post : ogUserPosts){
            //???????????????????????????????????????????????????
            if("0001".equals(post.getPostId())){
                mp.put("admin",true);
            }
        }
        if("myList".equals(type)||"chuli".equals(type)||"close".equals(type)){
            return prefix + "/mylist";
        }
        if("allList".equals(type)){
            return prefix + "/allList";
        }
        if("myDealList".equals(type)){
            return prefix+"/myDealList";
        }
        return prefix+"/allList";
    }

    /**
     * ?????????????????????????????????
     */
    @PostMapping("/myList")
    @ResponseBody
    public TableDataInfo myList(EventRun eventRun)
    {
        startPage();
        if(StringUtils.isNotEmpty(eventRun.getStatus())){
            String[] state=eventRun.getStatus().split(",");
            List<String> statecollection= Arrays.stream(state).collect(Collectors.toList());
            eventRun.getParams().put("status",statecollection);
        }
        if(StringUtils.isNotEmpty(eventRun.getAppSystemName())){
            String[] appSystemName=eventRun.getAppSystemName().split(",");
            List<String> appSystemNameCodecollection= Arrays.stream(appSystemName).collect(Collectors.toList());
            eventRun.getParams().put("appSystemName",appSystemNameCodecollection);
        }
        eventRun.setCreateId(ShiroUtils.getUserId());
        List<EventRun> list = eventRunService.selectEventRunList(eventRun);
        return getDataTable(list);
    }
    /**
     * ??????????????????????????????????????????
     */
    @PostMapping("/myDealList")
    @ResponseBody
    public TableDataInfo myDealList(EventRun eventRun)
    {
        if(StringUtils.isNotEmpty(eventRun.getStatus())){
            String[] state=eventRun.getStatus().split(",");
            List<String> statecollection= Arrays.stream(state).collect(Collectors.toList());
            eventRun.getParams().put("status",statecollection);
        }
        if(StringUtils.isNotEmpty(eventRun.getAppSystemName())){
            String[] appSystemName=eventRun.getAppSystemName().split(",");
            List<String> appSystemNameCodecollection= Arrays.stream(appSystemName).collect(Collectors.toList());
            eventRun.getParams().put("appSystemName",appSystemNameCodecollection);
        }
        eventRun.getParams().put("dealId",ShiroUtils.getUserId());
        startPage();
        List<EventRun> list = eventRunService.selectMydealEventRunList(eventRun);
        return getDataTable(list);
    }
    /**
     * ?????????????????????????????????
     */
    @PostMapping("/allList")
    @ResponseBody
    public TableDataInfo allList(EventRun eventRun)
    {
        if(StringUtils.isNotEmpty(eventRun.getStatus())){
            String[] state=eventRun.getStatus().split(",");
            List<String> statecollection= Arrays.stream(state).collect(Collectors.toList());
            eventRun.getParams().put("status",statecollection);
        }
        if(StringUtils.isNotEmpty(eventRun.getAppSystemName())){
            String[] appSystemName=eventRun.getAppSystemName().split(",");
            List<String> appSystemNameCodecollection= Arrays.stream(appSystemName).collect(Collectors.toList());
            eventRun.getParams().put("appSystemName",appSystemNameCodecollection);
        }
        //??????????????????
        if(eventRun.getParams().get("userTask")!=null&&!"".equals(eventRun.getParams().get("userTask"))){
            String userTask=eventRun.getParams().get("userTask").toString();
            userTask=iPubParaValueService.selectPubParaValueByNameValue("YXSJ_TASKNAME",userTask);
            String case1String=eventRun.getParams().get("case1")==null?"":eventRun.getParams().get("case1").toString();
            float case1=Float.valueOf(case1String.equals("")?"0":case1String);
            String case2String=eventRun.getParams().get("case2")==null?"":eventRun.getParams().get("case2").toString();
            float case2=Float.valueOf(case2String.equals("")?"0":case2String);
            String unit=eventRun.getParams().get("unit")==null?"":eventRun.getParams().get("unit").toString();
            switch (unit){
                case "1":
                    case1=case1*60;
                    case2=case2*60;
                    break;
                case "2":
                    case1=case1*60*60;
                    case2=case2*60*60;
                    break;
                case "3":
                    case1=case1*24*60*60;
                    case2=case2*24*60*60;
                    break;
            }
            if(case1>0){
                eventRun.getParams().put("case1",case1);
            }
            if(case2>0){
                eventRun.getParams().put("case2",case2);
            }
            eventRun.getParams().put("userTask",userTask);
            startPage();
            List<EventRun> list = eventRunService.selectByTiming(eventRun);
            return getDataTable(list);
        }
        startPage();
        List<EventRun> list = eventRunService.selectEventRunList(eventRun);
        return getDataTable(list);
    }
    /**
     * ???????????????????????????
     */
    @Log(title = "???????????????", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(EventRun eventRun)
    {
        final  Map<String,String> eventSouce=switchList("YXSJ_SOURCE");
        final  Map<String,String> eventLevel=switchList("dhsj_sjdj");
        final  Map<String,String> eventType=switchList("duty_type");
        final  Map<String,String> status=switchList("Event_status");
        if(StringUtils.isNotEmpty(eventRun.getStatus())){
            String[] state=eventRun.getStatus().split(",");
            List<String> statecollection= Arrays.stream(state).collect(Collectors.toList());
            eventRun.getParams().put("status",statecollection);
        }
        if(StringUtils.isNotEmpty(eventRun.getAppSystemName())){
            String[] appSystemName=eventRun.getAppSystemName().split(",");
            List<String> appSystemNameCodecollection= Arrays.stream(appSystemName).collect(Collectors.toList());
            eventRun.getParams().put("appSystemName",appSystemNameCodecollection);
        }
        String isCurrentPage = (String) eventRun.getParams().get("currentPage");
        List<EventRun> list =new ArrayList<>();
        if("currentPage".equals(isCurrentPage)){
            startPage();
            list=eventRunService.selectEventRunList(eventRun);
        } else if(eventRun.getParams().get("userTask")!=null&&!"".equals(eventRun.getParams().get("userTask"))){
            String userTask=eventRun.getParams().get("userTask").toString();
            userTask=iPubParaValueService.selectPubParaValueByNameValue("YXSJ_TASKNAME",userTask);
            String case1String=eventRun.getParams().get("case1")==null?"":eventRun.getParams().get("case1").toString();
            float case1=Float.valueOf(case1String.equals("")?"0":case1String);
            String case2String=eventRun.getParams().get("case2")==null?"":eventRun.getParams().get("case2").toString();
            float case2=Float.valueOf(case2String.equals("")?"0":case2String);
            String unit=eventRun.getParams().get("unit")==null?"":eventRun.getParams().get("unit").toString();
            switch (unit){
                case "1":
                    case1=case1*60;
                    case2=case2*60;
                    break;
                case "2":
                    case1=case1*60*60;
                    case2=case2*60*60;
                    break;
                case "3":
                    case1=case1*24*60*60;
                    case2=case2*24*60*60;
                    break;
            }
            if(case1>0){
                eventRun.getParams().put("case1",case1);
            }
            if(case2>0){
                eventRun.getParams().put("case2",case2);
            }
            eventRun.getParams().put("userTask",userTask);
            list = eventRunService.selectByTiming(eventRun);
        }else {
            list=eventRunService.selectEventRunList(eventRun);
        }
          for(EventRun er:list){
            //????????????
            er.setEventSource(eventSouce.get(er.getEventSource()));
            //????????????
            er.setEventLevel(eventLevel.get(er.getEventLevel()));
            //????????????
            er.setEventType(eventType.get(er.getEventType()));
            //????????????
            er.setStatus(status.get(er.getStatus()));
        }
        ExcelUtil<EventRun> util = new ExcelUtil<EventRun>(EventRun.class);
        return util.exportExcel(list, "????????????");
    }

    /**
     * ?????????????????????
     */
    @GetMapping("/add")
    public String add(ModelMap mmap)
    {
        OgPerson ogPerson = new OgPerson();
        ogPerson.setrId(eventRunConstants.DUTY_ROLE);
        List<OgPerson> userlist = commonService.selectPersonByOrgAndRole(ogPerson);
        String bizType = "YXSJ";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        mmap.put("num", bizType + nowDateStr + numStr);
        mmap.put("eventId", IdUtils.fastSimpleUUID());
        mmap.put("userlist", userlist);
        return prefix + "/add";
    }

    /**
     * ???????????????????????????
     */
    @Log(title = "???????????????", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(EventRun eventRun)
    {
        String reportTime=eventRun.getReportTime();
        eventRun.setReportTime(DateUtils.handleTimeYYYYMMDDHHMMSS(reportTime));
        eventRun.setCreateTime(DateUtils.dateTimeNow());
        eventRun.setCreateId(ShiroUtils.getUserId());
        eventRun.setHandleRoles(eventRun.getEventType());
        eventRun.setStatus("0");
        return toAjax(eventRunService.insertEventRun(eventRun));
    }
    /**
     * ?????????????????????
     */
    @GetMapping("/edit/{eventId}")
    public String edit(@PathVariable("eventId") String eventId, ModelMap mmap)
    {
        EventRun eventRun = eventRunService.selectEventRunById(eventId);
        OgPerson ogPerson = new OgPerson();
        ogPerson.setrId(eventRunConstants.DUTY_ROLE);
        List<OgPerson> userlist = commonService.selectPersonByOrgAndRole(ogPerson);
        OgUser handlePerson=ogUserService.selectOgUserByUserId(eventRun.getHandlePerson());
        String handlePersonName="";
        if(handlePerson!=null){
            handlePersonName=handlePerson.getPname();
        }
        eventRun.getParams().put("handlePersonName",handlePersonName);
        mmap.put("eventRun", eventRun);
        mmap.put("userlist", userlist);
        if("6".equals(eventRun.getStatus())){
            return prefix+"/backEdit";
        }
        return prefix + "/edit";
    }
    /**
     * ?????????????????????
     */
    @GetMapping("/view/{eventId}")
    public String view(@PathVariable("eventId") String eventId, ModelMap mmap)
    {
        EventRun eventRun = eventRunService.selectEventRunById(eventId);
        String handlePerson=eventRun.getHandlePerson();
        OgUser charge=ogUserService.selectOgUserByUserId(handlePerson);
        if(charge!=null){
            eventRun.setHandlePerson(charge.getPname());
        }
        if(StringUtils.isNotEmpty(eventRun.getHandleTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            eventRun.setHandleTime(sdf.format(new Date(Long.valueOf(eventRun.getHandleTime()))));
        }
        if(StringUtils.isEmpty(eventRun.getWebAttendant())){
             String sys=eventRun.getAppSystemCode();
            if("02".equals(eventRun.getEventSource())){
                OgSys ogSys=iSysApplicationManagerService.selectOgSysBySysCode(sys);
                if(ogSys!=null){
                    sys=ogSys.getSysId();
                }else {
                    sys="-1";
                }
            }
            final String tittle=eventRun.getReportSource();
            final String detail=eventRun.getEventDescr();
            String knowledgeTittle="????????????????????????";
            String knowledgeId="";
            if(StringUtils.isNotEmpty(sys)&&StringUtils.isNotEmpty(tittle)){
                List<KnowledgeContent> list= KnowledgeApi.searchKnowledge(sys,tittle,detail);
                if(!CollectionUtils.isEmpty(list)){
                    knowledgeTittle=list.get(0).getTitle();
                    knowledgeId=list.get(0).getId();
                }
            }
            eventRun.setWebAttendant(knowledgeTittle);
            eventRun.setSysAttendant(knowledgeId);
        }
        mmap.put("eventRun", eventRun);
        return prefix + "/view";
    }

    /**
     * ????????????
     * @param eventId
     * @param mmap
     * @return
     */
    @GetMapping("/goTransferred/{eventId}/{handleDescr}")
    public String goTransferred(@PathVariable("eventId")String eventId,@PathVariable("handleDescr")String handleDescr,ModelMap mmap) throws UnsupportedEncodingException {
        mmap.put("eventId",eventId);
        handleDescr=URLDecoder.decode(handleDescr, "UTF-8");
        mmap.put("handleDescr",handleDescr);
        if("0".equals(handleDescr)){
            mmap.put("handleDescr","");
        }
        return prefix+"/transferred";
    }
    /**
     * ????????????
     * @param eventId
     * @param mmap
     * @return
     */
    @GetMapping("/goReassign/{eventId}/{handleDescr}")
    public String goReassign(@PathVariable("eventId")String eventId,@PathVariable("handleDescr")String handleDescr,ModelMap mmap) throws UnsupportedEncodingException {
        mmap.put("eventId",eventId);
        handleDescr=URLDecoder.decode(handleDescr, "UTF-8");
        mmap.put("handleDescr",handleDescr);
        if("0".equals(handleDescr)){
            mmap.put("handleDescr","");
        }
        return prefix+"/reassign";
    }
    /**
     * ??????????????????
     * @param eventId
     * @param mmap
     * @return
     */
    @GetMapping("/goCooperate/{eventId}/{handleDescr}")
    public String goCooperate(@PathVariable("eventId")String eventId,@PathVariable("handleDescr")String handleDescr,ModelMap mmap) throws UnsupportedEncodingException {
        mmap.put("eventId",eventId);
        handleDescr=URLDecoder.decode(handleDescr, "UTF-8");
        mmap.put("handleDescr",handleDescr);
        if("0".equals(handleDescr)){
            mmap.put("handleDescr","");
        }
        return prefix+"/cooperate";
    }

    /**
     * ???????????????????????????????????????
     */
    @GetMapping("/viewNoClose/{eventId}")
    public String viewNoClose(@PathVariable("eventId") String eventId, ModelMap mmap)
    {
        EventRun eventRun = eventRunService.selectEventRunById(eventId);
        String handlePerson=eventRun.getHandlePerson();
        String[] person=handlePerson.split(",");
        String multiuser="";
        for(String mu:person){
            OgUser multiusers=ogUserService.selectOgUserByUserId(mu);
            multiuser+=multiusers.getPname()+",";
        }
        if(StringUtils.isNotEmpty(multiuser)){
            multiuser=multiuser.substring(0,multiuser.length()-1);
            eventRun.setHandlePerson(multiuser);
        }
        mmap.put("eventRun", eventRun);
        return prefix + "/viewNoClose";
    }
    /**
     * ???????????????????????????
     */
    @Log(title = "???????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(EventRun eventRun)
    {
        eventRun.setHandleRoles(eventRun.getEventType());
        return toAjax(eventRunService.updateEventRun(eventRun));
    }

    /**
     * ?????????????????????
     */
    @Log(title = "???????????????", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(eventRunService.deleteEventRunByIds(ids));
    }
    /**
     * ??????????????????  ???????????? ?????????
     */
    @Log(title = "??????????????????", businessType = BusinessType.DELETE)
    @PostMapping( "/removeAll")
    @ResponseBody
    @Transactional
    public AjaxResult removeAll(String ids)
    {
        String[] allId=ids.split(",");
        for(String id:allId){
            Task task = processEngine.getTaskService().createTaskQuery().processInstanceBusinessKey(id).singleResult();
            if(task!=null){
                runtimeService.deleteProcessInstance(task.getProcessInstanceId(), "????????????");
            }
        }
        return toAjax(eventRunService.deleteEventRunByIds(ids));
    }
    /**
     * ??????????????????
     *
     * @return
     */
    @GetMapping("/selectOneApplication/{flag}")
    public String selectOneApplication(@PathVariable("flag")String flag,ModelMap map) {
        map.put("flag",flag);
        return prefix + "/selectOneApplication";
    }

    /**?????????????????????
     *
     * @param eventType
     * @param affiliatedCenter
     * @param mp
     * @return
     */
    @GetMapping("/chooseHandle")
    public String typeinfo(String eventType,String affiliatedCenter,ModelMap mp) {
        mp.put("eventType",eventType);
        mp.put("affiliatedCenter",affiliatedCenter);
        return prefix + "/typeinfo";
    }
    /**
     * ?????????????????????????????????
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<ZtreeStr> treeData(String eventType,String affiliatedCenter) {
        List<ZtreeStr> ztrees = dutyTypeinfoService.selectTypeinfoTree(new DutyTypeinfo());
        return ztrees;
    }
    /**
     * ?????????????????????????????????????????????
     */
    @GetMapping("/treeData/{typeinfoId}")
    @ResponseBody
    public List<Ztree> treeDataExcludeChild(@PathVariable(value = "typeinfoId", required = false) String typeinfoId) {
        DutyTypeinfo dutyTypeinfo = new DutyTypeinfo();
        dutyTypeinfo.setTypeinfoId(typeinfoId);
        List<Ztree> ztrees = dutyTypeinfoService.selectTypeTreeExcludeChild(dutyTypeinfo);
        return ztrees;
    }
    /**
     * ??????????????????????????????
     */
    @PostMapping("/listData")
    @ResponseBody
    public TableDataInfo list(DutyTypeinfo dutyTypeinfo) {
        startPage();
        List<DutyTypeinfo> list = dutyTypeinfoService.selectDutyTypeinfoList(dutyTypeinfo);
        List<DutyScheduling> reList=new ArrayList<>();
        for(DutyTypeinfo df:list){
            String typeNo =df.getTypeNo();
            DutyScheduling dutyScheduling=new DutyScheduling();
            dutyScheduling.setTypeNo(typeNo);
            //dutyScheduling.setPname(dutyTypeinfo.getPname());
            List<DutyScheduling> dtfList=iDutySchedulingService.selectDutySchedulingByTypeNo(dutyScheduling);
            reList.addAll(dtfList);
        }
        return getDataTable(reList);
    }
    @GetMapping("/goListUser/{flag}")
    public String goListUser(@PathVariable("flag")String flag,ModelMap mp) {
        mp.put("flag",flag);
        return prefix + "/auditUser";
    }
    /**
     * ????????????????????????
     */
    @PostMapping("/listUser")
    @ResponseBody
    public TableDataInfo listUser(OgPerson ogPerson) {
        List<OgPost> postUser = ogPostService.selectPostUserById(eventRunConstants.DATA_CENTER);
        postUser.addAll(ogPostService.selectPostUserById(eventRunConstants.MANUFACTURER));
        List<OgPerson> reList=new ArrayList<>();
        for(OgPost opt:postUser){
            if(!ShiroUtils.getUserId().equals(opt.getUserId())){
                ogPerson.setpId(opt.getUserId());
                List<OgPerson> ogPer=ogPersonService.selectOgPersonList(ogPerson);
                reList.addAll(ogPer);
            }
        }
        return getDataTable_ideal(reList);
    }
    /**
     * ?????????????????????
     */
    @Log(title = "?????????????????????", businessType = BusinessType.INSERT)
    @PostMapping("/submit")
    @ResponseBody
    @Transactional
    public AjaxResult submit(EventRun eventRun)
    {
        try {
            List<ProcessInstance> pro = activitiCommService.processInfo(eventRun.getEventId());
            if (StringUtils.isNotEmpty(pro)) {
                return error("?????????????????????????????????");
            }
            EventRun ern = eventRunService.selectEventRunById(eventRun.getEventId());
            if (ern == null) {
                String reportTime = eventRun.getReportTime();
                eventRun.setReportTime(DateUtils.handleTimeYYYYMMDDHHMMSS(reportTime));
                eventRun.setCreateTime(DateUtils.dateTimeNow());
                eventRun.setCreateId(ShiroUtils.getUserId());
                eventRun.setStatus("0");
                eventRunService.insertEventRun(eventRun);
            }
            if(StringUtils.isEmpty(eventRun.getEventNo())){
                eventRun=ern;
            }
            Map<String, Object> reMap = new HashMap<>();
            eventRun.setHandleRoles(eventRun.getEventType());
            String dealId=eventRunService.selectDutyUserId(eventRun,ShiroUtils.getUserId());
            if(StringUtils.isEmpty(dealId)){
                dealId=ShiroUtils.getUserId();
               // return error("??????????????????:???????????????????????????");
            }
            eventRun.setHandlePerson(dealId);
            reMap.put("dealId",dealId);
            reMap.put("createId", ShiroUtils.getUserId());
            reMap.put("userId", ShiroUtils.getUserId());
            DutyAccount dutyAccount= iDutyAccountService.selectByAccountId(ShiroUtils.getUserId());
            if(dutyAccount!=null){
                reMap.put("dutyAccountId",dutyAccount.getPid());
            }
            eventRunService.sysNotify(eventRun.getEventNo(),dealId);
            String smMsg="????????????"+eventRun.getEventNo()+"??????????????????????????????????????????????????????";
            eventRunService.sendMsgNoUp(dealId,smMsg);
            eventRun.setStatus("1");
            eventRunService.updateEventRun(eventRun);
            activitiCommService.startProcess(eventRun.getEventId(), "FmYx", reMap);
        }catch (Exception e){
            e.printStackTrace();
            return error("?????????????????????"+e.toString());
        }
        return success();
    }

    /**
     * ???????????? ????????????
     * @param eventRun
     * @return
     */
    @PostMapping("/dealList")
    @ResponseBody

    public TableDataInfo dealList(EventRun eventRun)
    {
        String  status=eventRun.getStatus();
        if(StringUtils.isEmpty(status)){
            status="1,2,3,4,9";
        }
        String[] state=status.split(",");
        List<String> statecollection= Arrays.stream(state).collect(Collectors.toList());
        eventRun.getParams().put("status",statecollection);
        eventRun.getParams().put("userId",ShiroUtils.getUserId());
        if(StringUtils.isNotEmpty(eventRun.getAppSystemName())){
            String[] appSystemName=eventRun.getAppSystemName().split(",");
            List<String> appSystemNameCodecollection= Arrays.stream(appSystemName).collect(Collectors.toList());
            eventRun.getParams().put("appSystemName",appSystemNameCodecollection);
        }
         startPage();
         List<EventRun> listev = eventRunService.selectActEventRunList(eventRun);
        return getDataTable(listev);
    }
    /**
     * ?????????????????????
     */
    @GetMapping("/dealView/{eventId}/{type}")
    public String dealView(@PathVariable("eventId") String eventId,@PathVariable("type") String type, ModelMap mmap)
    {
        EventRun eventRun = eventRunService.selectEventRunById(eventId);
        OgUser handlePerson=ogUserService.selectOgUserByUserId(eventRun.getHandlePerson());
        if(handlePerson!=null){
            eventRun.getParams().put("handlePersonName",handlePerson.getPname());
        }else {
            eventRun.getParams().put("handlePersonName","");
        }
        OgUser dutyAccount=ogUserService.selectOgUserByUserId(eventRun.getDutyAccount());
        if(dutyAccount!=null){
            eventRun.getParams().put("dutyAccountName",dutyAccount.getPname());
        }
        long now=(new Date()).getTime();
        if(StringUtils.isNotEmpty(eventRun.getHandleTime())){
            long createTime=Long.valueOf(eventRun.getHandleTime());
            long timing=(now-createTime)/1000;
            mmap.put("timing",timing);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            eventRun.setHandleTime(sdf.format(new Date(Long.valueOf(eventRun.getHandleTime()))));
        }else {
            Task tk = activitiCommService.getTask(eventId,type);
            long createTime=tk.getCreateTime().getTime();
            long timing=(now-createTime)/1000;
            mmap.put("timing",timing);
        }
        mmap.put("eventRun", eventRun);
        mmap.put("type",type);
        if("edit".equals(type)){
            OgPerson ogPerson = new OgPerson();
            ogPerson.setrId(eventRunConstants.DUTY_ROLE);
            List<OgPerson> userlist = commonService.selectPersonByOrgAndRole(ogPerson);
            mmap.put("userlist", userlist);
            return prefix + "/backEdit";
        }
         String sys=eventRun.getAppSystemCode();
        final String tittle=eventRun.getReportSource();
        final String detail=eventRun.getEventDescr();
        if("02".equals(eventRun.getEventSource())){
            OgSys ogSys=iSysApplicationManagerService.selectOgSysBySysCode(sys);
            if(ogSys!=null){
                sys=ogSys.getSysId();
            }else {
                sys="-1";
            }
        }
        String knowledgeTittle="????????????????????????";
        String knowledgeId="";
        if(StringUtils.isNotEmpty(sys)&&StringUtils.isNotEmpty(tittle)){
            List<KnowledgeContent> list= KnowledgeApi.searchKnowledge(sys,tittle,detail);
            if(!CollectionUtils.isEmpty(list)){
                knowledgeTittle=list.get(0).getTitle();
                knowledgeId=list.get(0).getId();
            }
        }
        mmap.put("knowledgeTittle",knowledgeTittle);
        mmap.put("knowledgeId",knowledgeId);
        if("chuli".equals(type)){
            return prefix+"/dealView";
        }
        if("xtchuli".equals(type)){
            return prefix+"/cooperateDeal";
        }
        return prefix+"/view";
    }
    /**
     *????????????
     * @param eventId
     * @param mmap
     * @return
     */
    @GetMapping("/goDeal/{eventId}")
    public String goDeal(@PathVariable("eventId")String eventId,ModelMap mmap){
        List<OgPost> postUser = ogPostService.selectPostUserById(eventRunConstants.DATA_CENTER);
        List<OgPerson> reList=new ArrayList<>();
        for(OgPost opt:postUser){
            OgPerson ogPer=ogPersonService.selectOgPersonById(opt.getUserId());
            if(ogPer!=null){
                reList.add(ogPer);
            }
        }
        EventRun eventRun=eventRunService.selectEventRunById(eventId);
        long now=(new Date()).getTime();
        if(StringUtils.isNotEmpty(eventRun.getHandleTime())){
            long createTime=Long.valueOf(eventRun.getHandleTime());
            long timing=(now-createTime)/1000;
            mmap.put("timing",timing);
        }else {
            Task tk = activitiCommService.getTask(eventId,"chuli");
            long createTime=tk.getCreateTime().getTime();
            long timing=(now-createTime)/1000;
            mmap.put("timing",timing);
        }
        mmap.put("userList",reList);
        mmap.put("eventId",eventId);
        mmap.put("closeTime",eventRun.getCloseTime());
        mmap.put("eventSource",eventRun.getEventSource());
        return prefix+"/deal";
    }
    /**
     *????????????
     * @param eventId
     * @param mmap
     * @return
     */
    @GetMapping("/goBack/{eventId}")
    public String goBack(@PathVariable("eventId")String eventId,ModelMap mmap){
        EventRun eventRun=eventRunService.selectEventRunById(eventId);
        long now=(new Date()).getTime();
        if(StringUtils.isNotEmpty(eventRun.getHandleTime())){
            long createTime=Long.valueOf(eventRun.getHandleTime());
            long timing=(now-createTime)/1000;
            mmap.put("timing",timing);
        }else {
            Task tk = activitiCommService.getTask(eventId,"chuli");
            long createTime=tk.getCreateTime().getTime();
            long timing=(now-createTime)/1000;
            mmap.put("timing",timing);
        }
        mmap.put("eventId",eventId);
        mmap.put("eventSource",eventRun.getEventSource());
        return prefix+"/back";
    }
    /**
     *
     * @param eventId
     * @param mmap
     * @return
     */
    @GetMapping("/goKnowl/{eventId}")
    public String goKnowl(@PathVariable("eventId")String eventId,ModelMap mmap){
        mmap.put("eventId",eventId);
        return prefix+"/knowl";
    }
    /**
     *????????????
     * @param accountId
     * @return
     */
    @PostMapping("/getHandlePerson")
    @ResponseBody
    public OgPerson getHandlePerson(String accountId){
        DutyAccount dutyAccount= iDutyAccountService.selectByAccountId(accountId);
        if(dutyAccount!=null){
            return ogPersonService.selectOgPersonById(dutyAccount.getPid());
        }else {
            return null;
        }
    }

    /**
     * ???????????????
     * @param eventId
     * @return
     */
    @PostMapping("/listKnow/{eventId}")
    @ResponseBody
    public TableDataInfo listKnow(@PathVariable("eventId")String eventId){
        EventRun eventRun=eventRunService.selectEventRunById(eventId);
         String sys=eventRun.getAppSystemCode();
        if("02".equals(eventRun.getEventSource())){
            OgSys ogSys=iSysApplicationManagerService.selectOgSysBySysCode(sys);
            if(ogSys!=null){
                sys=ogSys.getSysId();
            }else {
                sys="-1";
            }
        }
        final String tittle=eventRun.getReportSource();
        final String detail=eventRun.getEventDescr();
        List<KnowledgeContent> list=new ArrayList<KnowledgeContent>();
        if(StringUtils.isEmpty(sys)||StringUtils.isEmpty(tittle)){
            return getDataTable_ideal(list);
        }
        list= KnowledgeApi.searchKnowledge(sys,tittle,detail);
       return getDataTable_ideal(list);
    }
    /**
     * ?????????????????????
     * @param eventRun
     * @return
     */
    @PostMapping("/deal")
    @ResponseBody
    @Transactional
    public AjaxResult deal(EventRun eventRun){
        try {
            EventRun ev=eventRunService.selectEventRunById(eventRun.getEventId());
            if(!"1,2,4,9".contains(ev.getStatus())){
                return error("???????????????????????????");
            }
            String reCode = eventRun.getParams().get("reCode").toString();
            String userId=ShiroUtils.getUserId();
            String handleTime=String.valueOf((new Date()).getTime());
            eventRun.setChargePerson(userId);
            eventRun.setHandleTime(handleTime);
            Map<String, Object> reMap = new HashMap<>();
            reMap.put("comment", eventRun.getHandleDescr());
            String smMsg="";
            String handlePerson="";
            if ("2".equals(reCode)) {
                //??????
                reMap.put("dealId", eventRun.getHandlePerson());
                handlePerson=eventRun.getHandlePerson();
                smMsg="????????????"+ev.getEventNo()+"??????????????????????????????????????????????????????";
                eventRun.setStatus("1");
            }
            if ("0".equals(reCode)) {
                //?????????
                eventRun.setStatus("8");
            }
            if ("1".equals(reCode)) {
                //??????
                eventRun.setStatus("6");
            }
            if("3".equals(reCode)){
                //??????
                String handRoles=eventRun.getEventType();
                String handDesc=eventRun.getHandleDescr();
                ev.setHandleRoles(handRoles);
                ev.setHandleDescr(handDesc);
                String dealId=eventRunService.selectDutyUserId(ev,ShiroUtils.getUserId());
                if(StringUtils.isEmpty(dealId)){
                    return error("?????????????????????????????????????????????'??????'?????????");
                }
                reMap.put("dealId",dealId );
                handlePerson=dealId;
                smMsg="????????????"+ev.getEventNo()+"?????????????????????????????????????????????????????????";
                eventRun.setStatus("1");
                eventRun.setEventType("");
            }
            //????????????
            if("4".equals(reCode)){
                //??????
                reMap.put("xtdealId", eventRun.getHandlePerson());
                handlePerson=eventRun.getHandlePerson();
                smMsg="????????????"+ev.getEventNo()+"?????????????????????????????????????????????????????????";
                eventRun.setStatus("1");
            }
            reMap.put("reCode", reCode);
            Task tk = activitiCommService.getTask(eventRun.getEventId(), "chuli");
            reMap.put("taskId", tk.getId());
            reMap.put("userId", ShiroUtils.getUserId());
            reMap.put("businessKey", eventRun.getEventId());
            reMap.put("processDefinitionKey", "FmYx");
            DutyAccount dutyAccount= iDutyAccountService.selectByAccountId(ShiroUtils.getUserId());
            if(dutyAccount!=null){
                reMap.put("dutyAccountId",dutyAccount.getPid());
            }
            activitiCommService.complete(reMap);

            if("2".equals(reCode)||"3".equals(reCode)||"4".equals(reCode)){
                if("4".equals(reCode)){
                    String[] ids=handlePerson.split(",");
                    for(String id:ids){
                        eventRunService.sysNotify(ev.getEventNo(),id);
                        eventRunService.sendMsgNoUp(id,smMsg);
                        eventRun.setHandlePerson("");
                    }
                }else {
                    eventRunService.sysNotify(ev.getEventNo(),handlePerson);
                    eventRunService.sendMsgNoUp(handlePerson,smMsg);
                }
            }
          /*  if("0".equals(reCode)||"1".equals(reCode)){
                eventRunService.sysNotify(ev.getCreateId(),eventRun.getHandlePerson());
            }*/
           // eventRun.setReportTime(DateUtils.dateTimeNow());
            eventRun.setDutyAccount("");
            eventRunService.updateEventRun(eventRun);
            return success();
        }catch (Exception e){
            e.printStackTrace();
            return error();
        }
    }

    /**
     * ????????????
     * @param eventRun
     * @return
     */
    @RequestMapping("/cooperateDeal")
    @ResponseBody
    @Transactional
    public AjaxResult cooperateDeal(EventRun eventRun){
        EventRun ev=eventRunService.selectEventRunById(eventRun.getEventId());
        if(!"1,2,4,9".contains(ev.getStatus())){
            return error("???????????????????????????");
        }
        String userId=ShiroUtils.getUserId();
        String handleTime=String.valueOf((new Date()).getTime());
        eventRun.setChargePerson(userId);
        eventRun.setHandleTime(handleTime);
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("comment", eventRun.getHandleDescr());
        Task tk = activitiCommService.getTask(eventRun.getEventId(), "xtchuli");
        reMap.put("taskId", tk.getId());
        reMap.put("userId", ShiroUtils.getUserId());
        reMap.put("businessKey", eventRun.getEventId());
        reMap.put("processDefinitionKey", "FmYx");
        DutyAccount dutyAccount= iDutyAccountService.selectByAccountId(ShiroUtils.getUserId());
        if(dutyAccount!=null){
            reMap.put("dutyAccountId",dutyAccount.getPid());
        }
        activitiCommService.usersComplete(reMap);
        Task task=activitiCommService.getTask(eventRun.getEventId(),"chuli");
        if(task!=null){
            Map<String,Object> map=runtimeService.getVariables(task.getExecutionId());
            eventRunService.sendMsgNoUp(map.get("dealId").toString(),"????????????"+eventRun.getEventNo()+"??????????????????????????????????????????");
            // eventRun.setReportTime(DateUtils.dateTimeNow());
            //??????
            eventRunService.sysNotify(eventRun.getEventNo(),map.get("dealId").toString());
        }
        eventRun.setDutyAccount("");
        eventRun.setStatus("1");
        eventRunService.updateEventRun(eventRun);
        return success();
    }

    /**
     * ??????????????????
     * @param eventRun
     * @return
     */
    @PostMapping("/temporarySave")
    @ResponseBody
    @Transactional
    public AjaxResult closeCase(EventRun eventRun){
        List<OgPost> postUser = ogPostService.selectPostUserById(eventRunConstants.DATA_CENTER);
        //??????
        Date date = DateUtils.parseDate(eventRun.getHandleTime(), "yyyy-MM-dd HH:mm:ss");
        long createTime=Long.valueOf(date==null?eventRun.getHandleTime():String.valueOf(date.getTime()));
        long now=(new Date()).getTime();
        long timing=0;
        if((now-createTime)!=0){
             timing=(now-createTime)/1000;
        }
        //?????? ?????????????????? ??????????????????
        eventRun.setHandleTime(String.valueOf((new Date()).getTime()));
        eventRun.setHandlePerson(ShiroUtils.getUserId());
        eventRun.setStatus("2");
        eventRunService.updateEventRun(eventRun);
        PubFlowLog pubFlowLog=new PubFlowLog();
        pubFlowLog.setBizId(eventRun.getEventId());
        List<PubFlowLog> logList=pubFlowLogService.selectPubFlowLogList(pubFlowLog);
        pubFlowLog=logList.get(logList.size()-1);
        pubFlowLog.setSerialNum(String.valueOf(logList.size()+1));
        pubFlowLog.setLogId(IdUtils.fastSimpleUUID());
        pubFlowLog.setTaskName("??????");
        pubFlowLog.setPerformName("??????????????????");
        pubFlowLog.setPerformDesc(eventRun.getHandleDescr());
        pubFlowLog.setNextPerformerDesc(ShiroUtils.getUserId());
        OgPerson ogUser=iOgPersonService.selectOgPersonById(ShiroUtils.getUserId());
        pubFlowLog.setPerformerName(ogUser.getpName());
        pubFlowLog.setPerformerId(ShiroUtils.getUserId());
        //????????????
        pubFlowLog.setPerformerTime(DateUtils.dateTimeNow());
        //???????????????
        pubFlowLog.setSysResidenceTime(String.valueOf(timing));
        //????????????
        DutyAccount dutyAccount= dutyAccountMapper.selectDutyAccountByPid(ShiroUtils.getUserId());
        if(dutyAccount!=null){
            pubFlowLog.setDutyId(dutyAccount.getAccountPid());
            OgPerson ogPerson= iOgPersonService.selectOgPersonById(dutyAccount.getAccountPid());
            if(ogPerson!=null){
                pubFlowLog.setDutyAccount(ogPerson.getpName());
            }
        }
        String tel=ogUser.getPhone();
        if(StringUtils.isEmpty(tel)){
            tel=ogUser.getMobilPhone();
        }
        pubFlowLog.setPerformerTel(tel);
        pubFlowLogService.insertPubFlowLog(pubFlowLog);
        return toAjax(1);
    }
    /**
     * ????????????????????????
     * @param eventId
     * @return
     */
    @PostMapping("/searchEvent")
    @ResponseBody
    @Transactional
    public AjaxResult searchEvent(String eventId){
        try{
            EventRun eventRun=eventRunService.selectEventRunById(eventId);
            String params="id="+eventId+"&apikey="+apiKey;
            logger.debug("=========???????????????????????????url:"+eventUrl+"================");
            logger.debug("=========???????????????????????????apiKey:"+apiKey+"===============");
            String result= HttpUtils.httpURLConectionGET(eventUrl+"?"+params);
            logger.debug("=========???????????????????????????result:"+result+"===============");
         //??????????????????????????????json ??????????????????
            int index=result.indexOf("closeTime");
            if(index<0){
                params+="&status=255";
                logger.debug("=========???????????????????????????url:"+eventUrl+"================");
                logger.debug("=========???????????????????????????apiKey:"+apiKey+"===============");
                 result= HttpUtils.httpURLConectionGET(eventUrl+"?"+params);
                logger.debug("=========???????????????????????????result:"+result+"===============");
            }
            index=result.indexOf("closeTime");
            if(index<0){
                return error("?????????????????????????????????");
            }
            String closeTime=result.substring(index+11,index+24);
            logger.debug("=========???????????????????????????closeTime:"+closeTime+"===============");
            Long tiem=Long.valueOf(closeTime);
            if(StringUtils.isNotEmpty(closeTime)){
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time=fmt.format(new Date(tiem));
                return success(time);
            }else {
                return error();
            }
        }catch (Exception e){
            e.printStackTrace();
            return error();
        }
    }
    /**
     * ??????
     */
    @ApiOperation("??????????????????")
    @PostMapping("/searchMonitor")
    @Transactional
    @RepeatSubmit
    @ResponseBody
    public void searchMonitor(){
        logger.debug("===========================????????????");
        String url=monitorUrl+"?apikey="+apiKey;
        String result= HttpUtils.httpURLConectionGET(url);
        logger.debug("===========================????????????result:"+result);
        if(StringUtils.isNotEmpty(result)&&!result.contains("errCode")){
            JSONArray jsonArray= JSON.parseArray(result);
            for(int i=0;i<jsonArray.size();i++){
                if(StringUtils.isNotEmpty(jsonArray.getJSONObject(i))){
                    //??????????????????
                    JSONObject js=jsonArray.getJSONObject(i);
                    String id=js.getString("alertId");
                    String handleRoles=js.getString("handleRoles");
                    logger.debug(id);
                    EventRun eventRun=eventRunService.selectEventRunById(id);
                    if(eventRun==null){
                        String urlEvent=detailUrl+"?apikey="+apiKey;
                        String body="select id,name,entityName,alias,status,firstOccurTime,closeTime,owner,source,description,severity,`ciProperties`,`properties` from `?????????????????????????????????` " +
                                "where id='"+id+"'";
                        String resultEvent= HttpUtils.sendPostBody(urlEvent,body);
                        logger.debug("==================???????????? resultEvent:"+resultEvent);
                        JSONObject jsonObject=JSONObject.parseObject(resultEvent);
                        String records=jsonObject.get("data")==null?"":jsonObject.get("data").toString();
                        if(StringUtils.isNotEmpty(records)){
                            JSONArray recordArray=JSONArray.parseArray(records);
                            JSONObject record=recordArray.getJSONObject(0);
                            String evenId=record.get("id").toString();
                            //????????????
                            String bizType = "YXSJ";
                            IdGenerator generator = new IdGenerator();
                            String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
                            generator.setCurrentDate(nowDateStr);
                            generator.setBizType(bizType);
                            String eventNo = idGeneratorService.selectIdGeneratorByType(generator);
                            String eventSource="02";//????????????
                            String eventTitle=record.get("name")==null?"":record.get("name").toString();//??????
                            if(StringUtils.isEmpty(eventTitle)){
                                eventTitle=record.get("alias")==null?"":record.getString("alias");
                            }
                            String reportTime=record.get("firstOccurTime")==null?"":record.get("firstOccurTime").toString();//????????????
                            String reportource=record.get("source")==null?"":record.get("source").toString();//????????????
                            String eventDescr=record.get("description")==null?"":record.get("description").toString();//????????????
                            String closeTime=record.get("closeTime")==null?"":record.get("closeTime").toString();//????????????
                            String eventLevel=record.get("severity")==null?"":record.get("severity").toString();//?????? ??????
                            String affiliatedCenter="";//???????????? ??????
                            String appSystemName="";//????????????
                            JSONArray ciProperties=JSONArray.parseArray(record.getString("ciProperties"));
                            if(!JSONUtils.isNull(ciProperties)) {
                                for (Object jsb : ciProperties) {
                                    JSONObject cip = (JSONObject) jsb;
                                    if ("belong_center".equals(cip.getString("code"))) {
                                        String centerdetail = cip.getString("val");
                                        List<PubParaValue> pubParaValueList = iPubParaValueService.selectPubParaValueByParaName("YXSJ_CENTER");
                                        for (PubParaValue pb : pubParaValueList) {
                                            if (StringUtils.isNotEmpty(centerdetail) && centerdetail.contains(pb.getValueDetail())) {
                                                affiliatedCenter = pb.getValue();
                                            }
                                        }
                                    }
                                    if ("belongBizSys".equals(cip.getString("code"))) {
                                        String sys = cip.getString("val");
                                        JSONObject sysJson = JSONObject.parseObject(sys);
                                        if (!JSONUtils.isNull(sysJson)) {
                                            appSystemName = sysJson.getString("name");
                                        }
                                    }
                                }
                            }
                            String eventType=handleRoles;//handleRoles ????????????
                            String appSystemCode="";//????????????
                            JSONArray properties=JSONArray.parseArray(record.getString("properties"));
                            if(!JSONUtils.isNull(properties)) {
                                for (Object jsb : properties) {
                                    JSONObject cip = (JSONObject) jsb;
                                    if ("platform_number".equals(cip.getString("code"))) {
                                        appSystemCode = cip.getString("val");
                                    }
                                }
                            }
                            EventRun eventRunNew=new EventRun();
                            eventRunNew.setEventId(evenId);
                            eventRunNew.setEventNo(bizType + nowDateStr + eventNo);
                            eventRunNew.setEventSource(eventSource);
                            eventRunNew.setEventTitle(eventTitle);
                            eventRunNew.setReportTime(DateUtils.handleTimeYYYYMMDDHHMMSS(reportTime));
                            eventRunNew.setCreateTime(DateUtils.dateTimeNow());
                            eventRunNew.setReportSource(reportource);
                            eventRunNew.setEventDescr(eventDescr);
                            eventRunNew.setCloseTime(DateUtils.handleTimeYYYYMMDDHHMMSS(closeTime));
                            eventRunNew.setEventLevel(eventLevel);
                            eventRunNew.setAffiliatedCenter(StringUtils.isEmpty(affiliatedCenter)?"5":affiliatedCenter);
                            eventRunNew.setEventType(eventType);
                            eventRunNew.setAppSystemCode(appSystemCode);
                            eventRunNew.setAppSystemName(appSystemName);
                            eventRunNew.setCreateId(eventRunConstants.USERID);
                            eventRunNew.setHandleRoles(handleRoles);
                            //????????????
                            Map<String, Object> reMap = new HashMap<>();
                            String dealId=eventRunService.selectDutyUserId(eventRunNew, eventRunConstants.USERID);
                            if(StringUtils.isEmpty(dealId)){
                                eventRunNew.setStatus("0");
                                eventRunService.insertEventRun(eventRunNew);
                            }
                            reMap.put("dealId",dealId);
                            reMap.put("createId", eventRunConstants.USERID);
                            reMap.put("dutyAccountId", eventRunConstants.USERID);
                            reMap.put("userId", eventRunConstants.USERID);
                            List<ProcessInstance> pro=activitiCommService.processInfo(eventRunNew.getEventId());
                            if(CollectionUtils.isEmpty(pro)){
                                eventRunNew.setStatus("1");
                                eventRunNew.setDutyAccount(dealId);
                                eventRunNew.setHandlePerson(dealId);
                                eventRunService.insertEventRun(eventRunNew);
                                String smMsg="????????????"+eventRunNew.getEventNo()+"??????????????????????????????????????????????????????";
                                eventRunService.sysNotify(eventRunNew.getEventNo(),eventRunNew.getHandlePerson());
                                eventRunService.sendMsgNoUp(dealId,smMsg);
                                activitiCommService.startProcess(eventRunNew.getEventId(), "FmYx", reMap);
                            }
                        }
                    }
                }
            }
        }
        logger.debug("==============================??????????????????===============================");
    }
    /**
     *??????
     * @param eventId
     * @param
     * @return
     */
    @PostMapping("/claimEventOne")
    @ResponseBody
    public AjaxResult claimEventOne(String eventId,String style){
        Task tk = activitiCommService.getTask(eventId,style);
        EventRun eventRun=eventRunService.selectEventRunById(eventId);
        if(!"1".equals(eventRun.getStatus())){
            return error("??????????????????????????????????????????");
        }
        OgUser handlePerson=ogUserService.selectOgUserByUserId(eventRun.getHandlePerson());
        if(handlePerson!=null){
            eventRun.getParams().put("handlePersonName",handlePerson.getPname());
        }else {
            eventRun.getParams().put("handlePersonName","");
        }
        OgUser dutyAccountUser=ogUserService.selectOgUserByUserId(eventRun.getDutyAccount());
        if(dutyAccountUser!=null){
            eventRun.getParams().put("dutyAccountName",dutyAccountUser.getPname());
        }
        //??????
        long createTime=tk.getCreateTime().getTime();
        long now=(new Date()).getTime();
        long timing=(now-createTime)/1000;
        //?????? ?????????????????? ??????????????????
        eventRun.setHandleTime(String.valueOf((new Date()).getTime()));
        eventRun.setStatus("9");
        eventRunService.updateEventRun(eventRun);
        PubFlowLog pubFlowLog=new PubFlowLog();
        pubFlowLog.setBizId(eventId);
        List<PubFlowLog> logList=pubFlowLogService.selectPubFlowLogList(pubFlowLog);
        pubFlowLog=logList.get(logList.size()-1);
        pubFlowLog.setSerialNum(String.valueOf(logList.size()+1));
        pubFlowLog.setLogId(IdUtils.fastSimpleUUID());
        pubFlowLog.setTaskName("??????");
        pubFlowLog.setPerformName("????????????");
        pubFlowLog.setPerformDesc("??????????????????");
        //
        String nextPerformerDesc="";
        List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(tk.getId());
        for(IdentityLink idk:identityLinkList){
            nextPerformerDesc+=idk.getUserId();
        }
        pubFlowLog.setNextPerformerDesc(nextPerformerDesc);
        pubFlowLog.setNextTaskName(tk.getName());
        pubFlowLog.setNextTaskId(tk.getId());
        OgPerson ogUser=iOgPersonService.selectOgPersonEvoById(ShiroUtils.getUserId());
        pubFlowLog.setPerformerName(ogUser.getpName());
        pubFlowLog.setPerformerId(ShiroUtils.getUserId());
        //????????????
        pubFlowLog.setPerformerTime(DateUtils.dateTimeNow());
        //???????????????
        pubFlowLog.setSysResidenceTime(String.valueOf(timing));
        //????????????
        DutyAccount dutyAccount= dutyAccountMapper.selectDutyAccountByPid(ShiroUtils.getUserId());
        if(dutyAccount!=null){
            pubFlowLog.setDutyId(dutyAccount.getAccountPid());
            OgPerson ogPerson= iOgPersonService.selectOgPersonById(dutyAccount.getAccountPid());
            pubFlowLog.setDutyAccount(ogPerson.getpName());
        }else {
            pubFlowLog.setDutyId(ShiroUtils.getUserId());
            OgPerson ogPerson= iOgPersonService.selectOgPersonById(ShiroUtils.getUserId());
            pubFlowLog.setDutyAccount(ogPerson.getpName());
        }
        String tel=ogUser.getPhone();
        if(StringUtils.isEmpty(tel)){
            tel=ogUser.getMobilPhone();
        }
        pubFlowLog.setPerformerTel(tel);
        pubFlowLogService.insertPubFlowLog(pubFlowLog);
        return success("????????????");
    }
    /**
     *??????
     * @param rows
     * @param
     * @return
     */
    @PostMapping("/claimEvent")
    @ResponseBody
    public AjaxResult claimEvent(String rows){
        try {
            JSONArray recordArray = JSONArray.parseArray(rows);
            for (Object jst : recordArray) {
                JSONObject record = (JSONObject) jst;
                String eventId = record.getString("eventId");
                String description = record.getString("description");
                Task tk = activitiCommService.getTask(eventId, description);
                EventRun eventRun = eventRunService.selectEventRunById(eventId);
                if (!"1".equals(eventRun.getStatus())) {
                    return error("??????????????????????????????????????????");
                }
                OgUser handlePerson = ogUserService.selectOgUserByUserId(eventRun.getHandlePerson());
                if (handlePerson != null) {
                    eventRun.getParams().put("handlePersonName", handlePerson.getPname());
                } else {
                    eventRun.getParams().put("handlePersonName", "");
                }
                OgUser dutyAccountUser = ogUserService.selectOgUserByUserId(eventRun.getDutyAccount());
                if (dutyAccountUser != null) {
                    eventRun.getParams().put("dutyAccountName", dutyAccountUser.getPname());
                }
                //??????
                long createTime = tk.getCreateTime().getTime();
                long now = (new Date()).getTime();
                long timing = (now - createTime) / 1000;
                //?????? ?????????????????? ??????????????????
                eventRun.setHandleTime(String.valueOf((new Date()).getTime()));
                eventRun.setStatus("9");
                eventRunService.updateEventRun(eventRun);
                PubFlowLog pubFlowLog = new PubFlowLog();
                pubFlowLog.setBizId(eventId);
                List<PubFlowLog> logList = pubFlowLogService.selectPubFlowLogList(pubFlowLog);
                pubFlowLog = logList.get(logList.size() - 1);
                pubFlowLog.setSerialNum(String.valueOf(logList.size() + 1));
                pubFlowLog.setLogId(IdUtils.fastSimpleUUID());
                pubFlowLog.setTaskName("??????");
                pubFlowLog.setPerformName("????????????");
                pubFlowLog.setPerformDesc("??????????????????");
                //
                String nextPerformerDesc="";
                List<IdentityLink> identityLinkList = taskService.getIdentityLinksForTask(tk.getId());
                for(IdentityLink idk:identityLinkList){
                    nextPerformerDesc+=idk.getUserId()+',';
                }
                pubFlowLog.setNextPerformerDesc(nextPerformerDesc);
                pubFlowLog.setNextTaskName(tk.getName());
                pubFlowLog.setNextTaskId(tk.getId());
                OgPerson ogUser = iOgPersonService.selectOgPersonEvoById(ShiroUtils.getUserId());
                pubFlowLog.setPerformerName(ogUser.getpName());
                pubFlowLog.setPerformerId(ShiroUtils.getUserId());
                //????????????
                pubFlowLog.setPerformerTime(DateUtils.dateTimeNow());
                //???????????????
                pubFlowLog.setSysResidenceTime(String.valueOf(timing));
                //????????????
                DutyAccount dutyAccount = dutyAccountMapper.selectDutyAccountByPid(ShiroUtils.getUserId());
                if (dutyAccount != null) {
                    pubFlowLog.setDutyId(dutyAccount.getAccountPid());
                    OgPerson ogPerson = iOgPersonService.selectOgPersonById(dutyAccount.getAccountPid());
                    if (ogPerson != null) {
                        pubFlowLog.setDutyAccount(ogPerson.getpName());
                    }
                }
                String tel = ogUser.getPhone();
                if (StringUtils.isEmpty(tel)) {
                    tel = ogUser.getMobilPhone();
                }
                pubFlowLog.setPerformerTel(tel);
                pubFlowLogService.insertPubFlowLog(pubFlowLog);
            }
        }catch (Exception e){
            e.printStackTrace();
            return error("????????????????????????????????????????????????????????????");
        }
        return success("????????????");
    }
    /**
     * ???????????????????????????
     * @param eventRun
     * @return
     */
    @PostMapping("/reSubmit")
    @ResponseBody
    @Transactional
    public AjaxResult reSumbit(EventRun eventRun){
        try{
            Map<String, Object> reMap = new HashMap<>();
            Task tk = activitiCommService.getTask(eventRun.getEventId(), "edit");
            String reCode=eventRun.getParams().get("reCode").toString();
            reMap.put("taskId", tk.getId());
            reMap.put("userId", ShiroUtils.getUserId());
            reMap.put("businessKey", eventRun.getEventId());
            reMap.put("reCode",reCode);
            reMap.put("processDefinitionKey", "FmYx");
            eventRun.setStatus("1");
            if("1".equals(reCode)){
                //??????
                eventRun.setStatus("8");
            }
//            if("0".equals(eventRun.getStatus())){
//                //????????????
//                eventRun.setStatus("1");
//            }
            activitiCommService.complete(reMap);
            eventRunService.updateEventRun(eventRun);
            return success();
        }catch (Exception e){
            e.printStackTrace();
            return error();
        }

    }
}
