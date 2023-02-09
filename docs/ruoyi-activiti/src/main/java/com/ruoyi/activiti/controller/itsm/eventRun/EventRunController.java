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
 * 监控事件单Controller
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
    //监控地址
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
     * 菜单跳转
     * @return
     */
    @GetMapping("/go/{type}")
    public String run(@PathVariable("type")String type,ModelMap mp)
    {
        mp.put("type",type);
        OgUserPost ogUserPost = new OgUserPost();
        ogUserPost.setUserId(ShiroUtils.getUserId());
        List<OgUserPost> ogUserPosts = iOgUserPostService.selectPostByUserId(ogUserPost);
        //循环
        for(OgUserPost post : ogUserPosts){
            //判断查询出的岗位信息是否为数据中心
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
     * 查询我的监控事件单列表
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
     * 查询我处理过的监控事件单列表
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
     * 查询所有监控事件单列表
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
        //任务耗时查询
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
     * 导出监控事件单列表
     */
    @Log(title = "监控事件单", businessType = BusinessType.EXPORT)
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
            //事件来源
            er.setEventSource(eventSouce.get(er.getEventSource()));
            //事件等级
            er.setEventLevel(eventLevel.get(er.getEventLevel()));
            //事件类型
            er.setEventType(eventType.get(er.getEventType()));
            //事件状态
            er.setStatus(status.get(er.getStatus()));
        }
        ExcelUtil<EventRun> util = new ExcelUtil<EventRun>(EventRun.class);
        return util.exportExcel(list, "监控事件");
    }

    /**
     * 新增监控事件单
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
     * 新增保存监控事件单
     */
    @Log(title = "监控事件单", businessType = BusinessType.INSERT)
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
     * 修改监控事件单
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
     * 监控事件单详情
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
            String knowledgeTittle="未匹配到相关知识";
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
     * 跳转指派
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
     * 跳转转派
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
     * 跳转协同处理
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
     * 监控事件单详情没有关闭按钮
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
     * 修改保存监控事件单
     */
    @Log(title = "监控事件单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(EventRun eventRun)
    {
        eventRun.setHandleRoles(eventRun.getEventType());
        return toAjax(eventRunService.updateEventRun(eventRun));
    }

    /**
     * 删除监控事件单
     */
    @Log(title = "监控事件单", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(eventRunService.deleteEventRunByIds(ids));
    }
    /**
     * 监控数据清理  删除任务 及工单
     */
    @Log(title = "监控数据清理", businessType = BusinessType.DELETE)
    @PostMapping( "/removeAll")
    @ResponseBody
    @Transactional
    public AjaxResult removeAll(String ids)
    {
        String[] allId=ids.split(",");
        for(String id:allId){
            Task task = processEngine.getTaskService().createTaskQuery().processInstanceBusinessKey(id).singleResult();
            if(task!=null){
                runtimeService.deleteProcessInstance(task.getProcessInstanceId(), "数据清理");
            }
        }
        return toAjax(eventRunService.deleteEventRunByIds(ids));
    }
    /**
     * 系统选择页面
     *
     * @return
     */
    @GetMapping("/selectOneApplication/{flag}")
    public String selectOneApplication(@PathVariable("flag")String flag,ModelMap map) {
        map.put("flag",flag);
        return prefix + "/selectOneApplication";
    }

    /**选择处理人页面
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
     * 加载值班参数设置列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<ZtreeStr> treeData(String eventType,String affiliatedCenter) {
        List<ZtreeStr> ztrees = dutyTypeinfoService.selectTypeinfoTree(new DutyTypeinfo());
        return ztrees;
    }
    /**
     * 加载值班部门列表树（排除下级）
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
     * 查询值班参数列别列表
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
     * 查询数据中心人员
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
     * 发起监控事件单
     */
    @Log(title = "发起监控事件单", businessType = BusinessType.INSERT)
    @PostMapping("/submit")
    @ResponseBody
    @Transactional
    public AjaxResult submit(EventRun eventRun)
    {
        try {
            List<ProcessInstance> pro = activitiCommService.processInfo(eventRun.getEventId());
            if (StringUtils.isNotEmpty(pro)) {
                return error("该业务单号已有流程启动");
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
               // return error("发起任务失败:未找到可分配值班人");
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
            String smMsg="单号为："+eventRun.getEventNo()+"监控事件单已经分派给您，请及时处理！";
            eventRunService.sendMsgNoUp(dealId,smMsg);
            eventRun.setStatus("1");
            eventRunService.updateEventRun(eventRun);
            activitiCommService.startProcess(eventRun.getEventId(), "FmYx", reMap);
        }catch (Exception e){
            e.printStackTrace();
            return error("发起任务失败："+e.toString());
        }
        return success();
    }

    /**
     * 待办任务 分页查询
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
     * 监控事件单详情
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
        String knowledgeTittle="未匹配到相关知识";
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
     *跳转处理
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
     *跳转处理
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
     *分词查询
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
     * 知识库查询
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
     * 处理监控事件单
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
                return error("该事件单已处理过！");
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
                //指派
                reMap.put("dealId", eventRun.getHandlePerson());
                handlePerson=eventRun.getHandlePerson();
                smMsg="单号为："+ev.getEventNo()+"监控事件单已经指派给您，请及时处理！";
                eventRun.setStatus("1");
            }
            if ("0".equals(reCode)) {
                //已处理
                eventRun.setStatus("8");
            }
            if ("1".equals(reCode)) {
                //退回
                eventRun.setStatus("6");
            }
            if("3".equals(reCode)){
                //转派
                String handRoles=eventRun.getEventType();
                String handDesc=eventRun.getHandleDescr();
                ev.setHandleRoles(handRoles);
                ev.setHandleDescr(handDesc);
                String dealId=eventRunService.selectDutyUserId(ev,ShiroUtils.getUserId());
                if(StringUtils.isEmpty(dealId)){
                    return error("操作失败未匹配到值班人，请使用'指派'功能！");
                }
                reMap.put("dealId",dealId );
                handlePerson=dealId;
                smMsg="单号为："+ev.getEventNo()+"的监控事件单已经转派给您，请及时处理！";
                eventRun.setStatus("1");
                eventRun.setEventType("");
            }
            //协同处理
            if("4".equals(reCode)){
                //指派
                reMap.put("xtdealId", eventRun.getHandlePerson());
                handlePerson=eventRun.getHandlePerson();
                smMsg="单号为："+ev.getEventNo()+"监控事件单需要您协同处理，请及时处理！";
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
     * 协同处理
     * @param eventRun
     * @return
     */
    @RequestMapping("/cooperateDeal")
    @ResponseBody
    @Transactional
    public AjaxResult cooperateDeal(EventRun eventRun){
        EventRun ev=eventRunService.selectEventRunById(eventRun.getEventId());
        if(!"1,2,4,9".contains(ev.getStatus())){
            return error("该事件单已处理过！");
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
            eventRunService.sendMsgNoUp(map.get("dealId").toString(),"单号为："+eventRun.getEventNo()+"的监控事件单协同处理已完成！");
            // eventRun.setReportTime(DateUtils.dateTimeNow());
            //通知
            eventRunService.sysNotify(eventRun.getEventNo(),map.get("dealId").toString());
        }
        eventRun.setDutyAccount("");
        eventRun.setStatus("1");
        eventRunService.updateEventRun(eventRun);
        return success();
    }

    /**
     * 更新处理情况
     * @param eventRun
     * @return
     */
    @PostMapping("/temporarySave")
    @ResponseBody
    @Transactional
    public AjaxResult closeCase(EventRun eventRun){
        List<OgPost> postUser = ogPostService.selectPostUserById(eventRunConstants.DATA_CENTER);
        //耗时
        Date date = DateUtils.parseDate(eventRun.getHandleTime(), "yyyy-MM-dd HH:mm:ss");
        long createTime=Long.valueOf(date==null?eventRun.getHandleTime():String.valueOf(date.getTime()));
        long now=(new Date()).getTime();
        long timing=0;
        if((now-createTime)!=0){
             timing=(now-createTime)/1000;
        }
        //接手 更新任务状态 添加流程日志
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
        pubFlowLog.setTaskName("处理");
        pubFlowLog.setPerformName("更新处理情况");
        pubFlowLog.setPerformDesc(eventRun.getHandleDescr());
        pubFlowLog.setNextPerformerDesc(ShiroUtils.getUserId());
        OgPerson ogUser=iOgPersonService.selectOgPersonById(ShiroUtils.getUserId());
        pubFlowLog.setPerformerName(ogUser.getpName());
        pubFlowLog.setPerformerId(ShiroUtils.getUserId());
        //操作时间
        pubFlowLog.setPerformerTime(DateUtils.dateTimeNow());
        //处理用时间
        pubFlowLog.setSysResidenceTime(String.valueOf(timing));
        //值班账号
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
     * 查询告警关闭时间
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
            logger.debug("=========查询告警关闭时间：url:"+eventUrl+"================");
            logger.debug("=========查询告警关闭时间：apiKey:"+apiKey+"===============");
            String result= HttpUtils.httpURLConectionGET(eventUrl+"?"+params);
            logger.debug("=========查询告警关闭时间：result:"+result+"===============");
         //监控返回数据格式不是json 用字符串截取
            int index=result.indexOf("closeTime");
            if(index<0){
                params+="&status=255";
                logger.debug("=========查询告警关闭时间：url:"+eventUrl+"================");
                logger.debug("=========查询告警关闭时间：apiKey:"+apiKey+"===============");
                 result= HttpUtils.httpURLConectionGET(eventUrl+"?"+params);
                logger.debug("=========查询告警关闭时间：result:"+result+"===============");
            }
            index=result.indexOf("closeTime");
            if(index<0){
                return error("未查询到告警关闭时间！");
            }
            String closeTime=result.substring(index+11,index+24);
            logger.debug("=========查询告警关闭时间：closeTime:"+closeTime+"===============");
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
     * 对账
     */
    @ApiOperation("监控对账接口")
    @PostMapping("/searchMonitor")
    @Transactional
    @RepeatSubmit
    @ResponseBody
    public void searchMonitor(){
        logger.debug("===========================监控对账");
        String url=monitorUrl+"?apikey="+apiKey;
        String result= HttpUtils.httpURLConectionGET(url);
        logger.debug("===========================监控对账result:"+result);
        if(StringUtils.isNotEmpty(result)&&!result.contains("errCode")){
            JSONArray jsonArray= JSON.parseArray(result);
            for(int i=0;i<jsonArray.size();i++){
                if(StringUtils.isNotEmpty(jsonArray.getJSONObject(i))){
                    //判断是否存在
                    JSONObject js=jsonArray.getJSONObject(i);
                    String id=js.getString("alertId");
                    String handleRoles=js.getString("handleRoles");
                    logger.debug(id);
                    EventRun eventRun=eventRunService.selectEventRunById(id);
                    if(eventRun==null){
                        String urlEvent=detailUrl+"?apikey="+apiKey;
                        String body="select id,name,entityName,alias,status,firstOccurTime,closeTime,owner,source,description,severity,`ciProperties`,`properties` from `最近一月未关闭告警集市` " +
                                "where id='"+id+"'";
                        String resultEvent= HttpUtils.sendPostBody(urlEvent,body);
                        logger.debug("==================监控对账 resultEvent:"+resultEvent);
                        JSONObject jsonObject=JSONObject.parseObject(resultEvent);
                        String records=jsonObject.get("data")==null?"":jsonObject.get("data").toString();
                        if(StringUtils.isNotEmpty(records)){
                            JSONArray recordArray=JSONArray.parseArray(records);
                            JSONObject record=recordArray.getJSONObject(0);
                            String evenId=record.get("id").toString();
                            //生成单号
                            String bizType = "YXSJ";
                            IdGenerator generator = new IdGenerator();
                            String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
                            generator.setCurrentDate(nowDateStr);
                            generator.setBizType(bizType);
                            String eventNo = idGeneratorService.selectIdGeneratorByType(generator);
                            String eventSource="02";//监控来源
                            String eventTitle=record.get("name")==null?"":record.get("name").toString();//标题
                            if(StringUtils.isEmpty(eventTitle)){
                                eventTitle=record.get("alias")==null?"":record.getString("alias");
                            }
                            String reportTime=record.get("firstOccurTime")==null?"":record.get("firstOccurTime").toString();//发生时间
                            String reportource=record.get("source")==null?"":record.get("source").toString();//告警来源
                            String eventDescr=record.get("description")==null?"":record.get("description").toString();//告警描述
                            String closeTime=record.get("closeTime")==null?"":record.get("closeTime").toString();//关闭时间
                            String eventLevel=record.get("severity")==null?"":record.get("severity").toString();//等级 编码
                            String affiliatedCenter="";//所属中心 编码
                            String appSystemName="";//系统名称
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
                            String eventType=handleRoles;//handleRoles 角色条线
                            String appSystemCode="";//系统编码
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
                            //启动流程
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
                                String smMsg="单号为："+eventRunNew.getEventNo()+"监控事件单已经分派给您，请及时处理！";
                                eventRunService.sysNotify(eventRunNew.getEventNo(),eventRunNew.getHandlePerson());
                                eventRunService.sendMsgNoUp(dealId,smMsg);
                                activitiCommService.startProcess(eventRunNew.getEventId(), "FmYx", reMap);
                            }
                        }
                    }
                }
            }
        }
        logger.debug("==============================监控对账结束===============================");
    }
    /**
     *接手
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
            return error("该监控事件单已接手，请处理！");
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
        //耗时
        long createTime=tk.getCreateTime().getTime();
        long now=(new Date()).getTime();
        long timing=(now-createTime)/1000;
        //接手 更新任务状态 添加流程日志
        eventRun.setHandleTime(String.valueOf((new Date()).getTime()));
        eventRun.setStatus("9");
        eventRunService.updateEventRun(eventRun);
        PubFlowLog pubFlowLog=new PubFlowLog();
        pubFlowLog.setBizId(eventId);
        List<PubFlowLog> logList=pubFlowLogService.selectPubFlowLogList(pubFlowLog);
        pubFlowLog=logList.get(logList.size()-1);
        pubFlowLog.setSerialNum(String.valueOf(logList.size()+1));
        pubFlowLog.setLogId(IdUtils.fastSimpleUUID());
        pubFlowLog.setTaskName("接手");
        pubFlowLog.setPerformName("接手任务");
        pubFlowLog.setPerformDesc("接手监控任务");
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
        //操作时间
        pubFlowLog.setPerformerTime(DateUtils.dateTimeNow());
        //处理用时间
        pubFlowLog.setSysResidenceTime(String.valueOf(timing));
        //值班账号
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
        return success("接手成功");
    }
    /**
     *接手
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
                    return error("该监控事件单已接手，请处理！");
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
                //耗时
                long createTime = tk.getCreateTime().getTime();
                long now = (new Date()).getTime();
                long timing = (now - createTime) / 1000;
                //接手 更新任务状态 添加流程日志
                eventRun.setHandleTime(String.valueOf((new Date()).getTime()));
                eventRun.setStatus("9");
                eventRunService.updateEventRun(eventRun);
                PubFlowLog pubFlowLog = new PubFlowLog();
                pubFlowLog.setBizId(eventId);
                List<PubFlowLog> logList = pubFlowLogService.selectPubFlowLogList(pubFlowLog);
                pubFlowLog = logList.get(logList.size() - 1);
                pubFlowLog.setSerialNum(String.valueOf(logList.size() + 1));
                pubFlowLog.setLogId(IdUtils.fastSimpleUUID());
                pubFlowLog.setTaskName("接手");
                pubFlowLog.setPerformName("接手任务");
                pubFlowLog.setPerformDesc("接手监控任务");
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
                //操作时间
                pubFlowLog.setPerformerTime(DateUtils.dateTimeNow());
                //处理用时间
                pubFlowLog.setSysResidenceTime(String.valueOf(timing));
                //值班账号
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
            return error("接手失败，请刷新页面确认监控事件单状态！");
        }
        return success("接手成功");
    }
    /**
     * 修改提交监控事件单
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
                //作废
                eventRun.setStatus("8");
            }
//            if("0".equals(eventRun.getStatus())){
//                //重新提交
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
