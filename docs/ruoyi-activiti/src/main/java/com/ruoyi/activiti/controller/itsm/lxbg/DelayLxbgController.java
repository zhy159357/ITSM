package com.ruoyi.activiti.controller.itsm.lxbg;

import com.ruoyi.activiti.service.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 *例行变更计划延期申请
 */
@Controller
@RequestMapping("/lxbg/delaylxbg")
public class DelayLxbgController extends BaseController {

    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DelayLxbgController.class);

    @Autowired
    private AddlxbgService addlxbgService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private IAmBizParaService amBizParaService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private DelayLxbgService delayLxbgService;

    @Autowired
    private ISmBizTaskinfoService smBizTaskinfoService;

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private ISysWorkService sysWorkService;


    //例行计划延期申请
    private String prefix = "lxbg/delaylxbg";

    //例行计划延期申请处长审核
    private String cz_prefix = "lxbg/auditcz";

    //例行计划延期申请计划人审核
    private String jh_prefix = "lxbg/auditjh";

    //例行计划延期申请计划人审核
    private String se_prefix = "lxbg/selectlxbgyq";

    /**
     * 转到新增例行变更页面
     * @param map
     * @return
     */
    @GetMapping()
    public String delayxbg(ModelMap map)
    {

        return prefix + "/delaylxbg";
    }




    /**
     * 列表展示
     * @param scheduling
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SmBizScheduling scheduling)
    {

        //查询计划状态为：未发布
        scheduling.setPlanStatus("04");
        startPage();
        List<SmBizScheduling> list = addlxbgService.selectScheduling(scheduling);
        return getDataTable(list);

    }


    /**
     * 转到申请页面
     */
    @GetMapping("/lxbgapply/{id}")
    public String lxbgapply(@PathVariable("id") String  id, ModelMap map)
    {
        SmBizTask smBizTask = taskService.selectSchedulingId(id);
        //获取当前页面的信息
        SmBizScheduling scheduling = addlxbgService.selectSchedulingById(id);

        //获取审核人   岗位为：数据中心处长与科技部处长  0011	, 0016
        Set<String> idList=new HashSet<>();

        OgPerson person = new OgPerson();

        List<OgPerson> userPostsList = ogPersonService.selectPostByuser(person);

        List<OgPerson> personList = new ArrayList<>();

        if(StringUtils.isNotEmpty(userPostsList)){
            for (OgPerson ogPerson : userPostsList) {
                //去重
                if (!idList.contains(ogPerson.getpId())) {
                    idList.add(ogPerson.getpId());
                    if(StringUtils.isNotEmpty(ogPerson.getpId())){
                        person.setpId(ogPerson.getpId());
                    }
                    personList.addAll(ogPersonService.selectOgPersonList(person));
                }
            }
            map.put("userPosts", personList);
        }



        //计划类别
        scheduling.setTaskTypeId(smBizTask.getTaskTypeId());
        //是否短信通知
        scheduling.setMsgDoor(smBizTask.getMsgDoor());
        //接收范围
        scheduling.setSendRange(smBizTask.getSendRange());
        map.put("scheduling",scheduling);
        map.put("smBizTask",smBizTask);


        //执行日期进行日期回显
        String performDate = smBizTask.getStartTime();
        if(StringUtils.isNotEmpty(performDate)){
            scheduling.setPerformDate(DateUtils.formatDateStr(performDate,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            map.addAttribute("performDate",scheduling.getPerformDate());
        }

        //获取当前节点对象的信息
        OgOrg ogOrg = deptService.selectDeptById(scheduling.getCreatorDeptId());
        if(ogOrg !=null){
            String levelCode = ogOrg.getLevelCode();
            List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode,"8200");
            map.put("checkList", checkList);

        }
        //获取路径
        SmBizFolder folder = folderService.selectFolderTreeById(scheduling.getFolder());
        map.put("sf",folder.getName_());

        List<AmBizPara> list = amBizParaService.selectAmBizParaList(new AmBizPara());
        List<Map<String,String>> reList=new ArrayList<>();
        for(AmBizPara aa:list){
            Map<String,String> mp=new HashMap<>();
            mp.put("id",aa.getAmParaId());
            mp.put("value",aa.getReceiveRange());
            reList.add(mp);
        }
        map.put("sendRangeList" ,reList);

        //创建时间进行日期回显
        String createTime = scheduling.getCreateTime();
        if(StringUtils.isNotEmpty(createTime)){
            scheduling.setCreateTime(DateUtils.formatDateStr(createTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        //执行日期进行日期回显
        String startTime = smBizTask.getStartTime();
        if(StringUtils.isNotEmpty(startTime)){
            smBizTask.setStartTime(DateUtils.formatDateStr(startTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        String checkTime = DateUtils.getTime();
        map.put("checkTime",checkTime);


        return prefix + "/lxbgapply";
    }


    /**
     * 审核是否通过
     *
     * @param
     * @return
     */
    @PostMapping("/appPass")
    @ResponseBody
    public AjaxResult appPass(SmBizScheduling scheduling) {

        Map<String, Object> reMap=new HashMap<>();

        //延期表
        SmBizLxbgApply smBizLxbgApply = new SmBizLxbgApply();
        //计划单ID
        smBizLxbgApply.setSchedulingId(scheduling.getSchedulingId());
        //申请原因
        smBizLxbgApply.setReason(scheduling.getReason());
        //延期时间
        smBizLxbgApply.setReleaseTime(scheduling.getReleaseTime());
        //处长审核ID
        smBizLxbgApply.setCheckId(scheduling.getCheckId());
        //创建人ID
        smBizLxbgApply.setCreateId(ShiroUtils.getUserId());
        //创建时间
        smBizLxbgApply.setCreateTime(DateUtils.dateTimeNow());
        //状态 1:待处长审核  2：计划审核人审核  3:关闭   4:处长审核不通过   5.计划人审核不通过
        smBizLxbgApply.setAppType("1");
        //流程开始
        String businessKey=scheduling.getSchedulingId();
        String processDefinitionKey="LXBGdelay";
        String comment = scheduling.getReason();//申请原因
        if(StringUtils.isNotEmpty(smBizLxbgApply.getCheckId())) {//待审核
            reMap.put("reCode","0");
            reMap.put("createId",smBizLxbgApply.getCreateId());
            reMap.put("checkId",smBizLxbgApply.getCheckId());
            reMap.put("comment",comment);
        }
        try{
            //根据计划单ID查询延期表信息
            SmBizLxbgApply apply = delayLxbgService.selectByScId(scheduling.getSchedulingId());
            if(StringUtils.isEmpty(apply)){
                //延期表ID生成
                smBizLxbgApply.setId(UUID.getUUIDStr());
                delayLxbgService.insertDelayLxbg(smBizLxbgApply);
            }else {
                smBizLxbgApply.setId(apply.getId());
                delayLxbgService.updateSmBizLxbgapply(smBizLxbgApply);
            }
            activitiCommService.startProcessLxbg(businessKey,processDefinitionKey,reMap);
        } catch (Exception e){
            e.printStackTrace();
            logger.error("例行变更计划延期申请失败 "+e.getMessage());
            throw  new BusinessException("例行变更计划延期申请失败,请刷新页面进行重试");
        }
        return AjaxResult.success("操作成功");
    }



    /**
     * 查询id
     * @param id
     * @return
     */
    @PostMapping( "/selectById")
    @ResponseBody
    public AjaxResult selectById(String id)
    {
        AjaxResult ajaxResult = new AjaxResult();
        SmBizLxbgApply apply = delayLxbgService.selectByScId(id);
        if(apply != null){
            ajaxResult.put("data",apply);
        }else {
            ajaxResult.put("data",null);
        }
        return  ajaxResult;
    }




    /**
     * 查看详情
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/detail/{id}")
    public String edit(@PathVariable("id") String  id, ModelMap map)
    {
        SmBizTask smBizTask = taskService.selectSchedulingId(id);
        //获取当前页面的信息
        SmBizScheduling scheduling = addlxbgService.selectSchedulingById(id);

        //获取审核人
        String pid = ShiroUtils.getOgUser().getpId();

        OgPerson ogPersonName = ogPersonService.selectOgPersonById(pid);

        String pname = ogPersonName.getpName();

        //计划类别
        scheduling.setTaskTypeId(smBizTask.getTaskTypeId());
        //是否短信通知
        scheduling.setMsgDoor(smBizTask.getMsgDoor());
        //接收范围
        scheduling.setSendRange(smBizTask.getSendRange());
        map.put("scheduling",scheduling);
        map.put("smBizTask",smBizTask);
        map.put("pname",pname);

        //执行日期进行日期回显
        String performDate = smBizTask.getStartTime();
        if(StringUtils.isNotEmpty(performDate)){
            scheduling.setPerformDate(DateUtils.formatDateStr(performDate,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            map.addAttribute("performDate",scheduling.getPerformDate());
        }

        //获取当前节点对象的信息
        OgOrg ogOrg = deptService.selectDeptById(scheduling.getCreatorDeptId());
        if(ogOrg !=null){
            String levelCode = ogOrg.getLevelCode();
            List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode,"8200");
            map.put("checkList", checkList);

        }
        //获取路径
        SmBizFolder folder = folderService.selectFolderTreeById(scheduling.getFolder());
        map.put("sf",folder.getName_());

        List<AmBizPara> list = amBizParaService.selectAmBizParaList(new AmBizPara());
        List<Map<String,String>> reList=new ArrayList<>();
        for(AmBizPara aa:list){
            Map<String,String> mp=new HashMap<>();
            mp.put("id",aa.getAmParaId());
            mp.put("value",aa.getReceiveRange());
            reList.add(mp);
        }
        map.put("sendRangeList" ,reList);

        //创建时间进行日期回显
        String createTime = scheduling.getCreateTime();
        if(StringUtils.isNotEmpty(createTime)){
            scheduling.setCreateTime(DateUtils.formatDateStr(createTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        //执行日期进行日期回显
        String startTime = smBizTask.getStartTime();
        if(StringUtils.isNotEmpty(startTime)){
            smBizTask.setStartTime(DateUtils.formatDateStr(startTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        String checkTime = DateUtils.getTime();
        map.put("checkTime",checkTime);

        // 根据计划单ID查询延期信息
        SmBizLxbgApply apply = delayLxbgService.selectByScId(id);
        if(!StringUtils.isEmpty(apply)){
            //获取处长审核人
            OgPerson person = ogPersonService.selectOgPersonById(apply.getCheckId());
            if(!StringUtils.isEmpty(person.getpName())){
                map.put("psername",ogPersonName.getpName());
            }
            //延期日期
            String releaseTime = apply.getReleaseTime();
            if(StringUtils.isNotEmpty(releaseTime)){
                apply.setReleaseTime(DateUtils.formatDateStr(releaseTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
                map.addAttribute("releaseTime",apply.getReleaseTime());
            }
            //原因
            map.put("reason",apply.getReason());
        }

        return prefix + "/delaylxbgdetail";
    }



    /**
     * 转到计划延期处长审核页面
     * @param map
     * @return
     */
    @GetMapping("/auditczym")
    public String auditcz(ModelMap map)
    {

        return cz_prefix + "/auditcz";
    }

    /**
     * 列表展示
     * @param scheduling
     * @return
     */
    @PostMapping("/directorshlist")
    @ResponseBody
    public TableDataInfo ctor_list(SmBizScheduling scheduling)
    {
        //展示待处长审核的信息
        startPage();
        List<Map<String,Object>> reList =activitiCommService.userTask("LXBGdelay",null);
        List<OgGroup> userGroupList= sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());
        List<Map<String,Object>> regList=new ArrayList<>();
        if(userGroupList.size()>0){
            regList=activitiCommService.groupTasks("LXBGdelay","czcheck");
            reList.addAll(regList);
        }
        List<String> schedulingIds = new ArrayList<>();
        for(Map<String,Object> map:reList){
            if("czcheck".equals(map.get("description"))){
                String business_key=map.get("businesskey").toString();
                if(business_key!=null){
                    schedulingIds.add(business_key);
                }
            }

        }
        List<SmBizScheduling> resultlist = null;
        if(schedulingIds!=null && schedulingIds.size()>0){
            scheduling.setIds(schedulingIds);
            resultlist =  addlxbgService.selectSchedulingList(scheduling);
            return getDataTable(resultlist);
        }

        return getDataTable(new ArrayList<>());

    }


    /**
     * 转到待处长审核页面
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/auditczlxbg/{id}")
    public String auditczlxbg(@PathVariable("id") String  id, ModelMap map)
    {
        SmBizTask smBizTask = taskService.selectSchedulingId(id);
        //获取当前页面的信息
        SmBizScheduling scheduling = addlxbgService.selectSchedulingById(id);

        //获取审核人
        String pid = ShiroUtils.getOgUser().getpId();

        OgPerson ogPersonName = ogPersonService.selectOgPersonById(pid);

        String pname = ogPersonName.getpName();

        //计划类别
        scheduling.setTaskTypeId(smBizTask.getTaskTypeId());
        //是否短信通知
        scheduling.setMsgDoor(smBizTask.getMsgDoor());
        //接收范围
        scheduling.setSendRange(smBizTask.getSendRange());
        map.put("scheduling",scheduling);
        map.put("smBizTask",smBizTask);
        map.put("pname",pname);

        //执行日期进行日期回显
        String performDate = smBizTask.getStartTime();
        if(StringUtils.isNotEmpty(performDate)){
            scheduling.setPerformDate(DateUtils.formatDateStr(performDate,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            map.addAttribute("performDate",scheduling.getPerformDate());
        }

        //获取当前节点对象的信息
        OgOrg ogOrg = deptService.selectDeptById(scheduling.getCreatorDeptId());
        if(ogOrg !=null){
            String levelCode = ogOrg.getLevelCode();
            List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode,"8200");
            map.put("checkList", checkList);

        }
        //获取路径
        SmBizFolder folder = folderService.selectFolderTreeById(scheduling.getFolder());
        map.put("sf",folder.getName_());

        List<AmBizPara> list = amBizParaService.selectAmBizParaList(new AmBizPara());
        List<Map<String,String>> reList=new ArrayList<>();
        for(AmBizPara aa:list){
            Map<String,String> mp=new HashMap<>();
            mp.put("id",aa.getAmParaId());
            mp.put("value",aa.getReceiveRange());
            reList.add(mp);
        }
        map.put("sendRangeList" ,reList);

        //创建时间进行日期回显
        String createTime = scheduling.getCreateTime();
        if(StringUtils.isNotEmpty(createTime)){
            scheduling.setCreateTime(DateUtils.formatDateStr(createTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        //执行日期进行日期回显
        String startTime = smBizTask.getStartTime();
        if(StringUtils.isNotEmpty(startTime)){
            smBizTask.setStartTime(DateUtils.formatDateStr(startTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        String checkTime = DateUtils.getTime();
        map.put("checkTime",checkTime);

        // 根据计划单ID查询延期信息
        SmBizLxbgApply apply = delayLxbgService.selectByScId(id);
        if(!StringUtils.isEmpty(apply)){
            //获取处长审核人
            OgPerson person = ogPersonService.selectOgPersonById(apply.getCheckId());
            if(!StringUtils.isEmpty(person.getpName())){
                map.put("psername",ogPersonName.getpName());
            }
            //延期日期
            String releaseTime = apply.getReleaseTime();
            if(StringUtils.isNotEmpty(releaseTime)){
                apply.setReleaseTime(DateUtils.formatDateStr(releaseTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
                map.addAttribute("releaseTime",apply.getReleaseTime());
            }
            //原因
            map.put("reason",apply.getReason());
        }
        return cz_prefix + "/authorczlxbg";
    }



    /**
     * 处长审核是否通过
     *
     * @param
     * @return
     */
    @PostMapping("/auditczPass")
    @ResponseBody
    public AjaxResult auditPass(SmBizScheduling scheduling) {

        Map<String, Object> map = new HashMap<>();
        SmBizScheduling smBizScheduling = addlxbgService.selectSchedulingById(scheduling.getSchedulingId());
        //根据计划单ID查询延期表信息
        SmBizLxbgApply apply = delayLxbgService.selectByScId(scheduling.getSchedulingId());
        String comment = scheduling.getCtorChecktext();//审核意见
        map.put("userId", ShiroUtils.getUserId());
        String businessKey = scheduling.getSchedulingId();
        map.put("businessKey", businessKey);
        map.put("comment", comment);
        String processDefinitionKey = "LXBGdelay";
        map.put("processDefinitionKey", processDefinitionKey);
        apply.setCtorChecktext(scheduling.getCtorChecktext());
        if ("0".equals(scheduling.getFlag())) {
            //通过
            map.put("reCode","0");
            map.put("shcheckId",smBizScheduling.getCheckPersonId());
            //状态 1:待处长审核  2：计划审核人审核  3:关闭   4:处长审核不通过   5.计划人审核不通过
            apply.setAppType("2");
            try{
                delayLxbgService.updateSmBizLxbgapply(apply);
                activitiCommService.complete(map);
            }catch (Exception e){
                log.error("延期申请例行计划单审核失败: "+e.getMessage());
                throw  new BusinessException("延期申请例行计划单审核失败,请刷新页面进行重试");
            }
            return AjaxResult.success("操作成功");
        }else {
            //不通过
            map.put("reCode","1");
            apply.setAppType("4");
            try{
                delayLxbgService.updateSmBizLxbgapply(apply);
                activitiCommService.complete(map);
            }catch (Exception e){
                log.error("延期申请例行计划单审核否决失败: "+e.getMessage());
                throw  new BusinessException("延期申请例行计划单审核否决失败,请刷新页面进行重试");
            }
            return AjaxResult.success("操作成功");
        }
    }



    /**
     * 转到延期申请计划人审核页面
     * @param map
     * @return
     */
    @GetMapping("/auditjhym")
    public String auditjh(ModelMap map)
    {

        return jh_prefix + "/auditjh";
    }

    /**
     * 列表展示
     * @param scheduling
     * @return
     */
    @PostMapping("/planlist")
    @ResponseBody
    public TableDataInfo planlist(SmBizScheduling scheduling)
    {
        //展示待计划人审核的信息
        startPage();
        List<Map<String,Object>> reList =activitiCommService.userTask("LXBGdelay",null);
        List<OgGroup> userGroupList= sysWorkService.selectGroupByUserId(ShiroUtils.getUserId());
        List<Map<String,Object>> regList=new ArrayList<>();
        if(userGroupList.size()>0){
            regList=activitiCommService.groupTasks("LXBGdelay","jhshenhe");
            reList.addAll(regList);
        }
        List<String> schedulingIds = new ArrayList<>();
        for(Map<String,Object> map:reList){
            if("jhshenhe".equals(map.get("description"))){
                String business_key=map.get("businesskey").toString();
                if(business_key!=null){
                    schedulingIds.add(business_key);
                }
            }

        }
        List<SmBizScheduling> resultlist = null;
        if(schedulingIds!=null && schedulingIds.size()>0){
            scheduling.setIds(schedulingIds);
            resultlist =  addlxbgService.selectSchedulingList(scheduling);
            return getDataTable(resultlist);
        }

        return getDataTable(new ArrayList<>());

    }

    /**
     * 转到待计划审核人审核页面
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/auditjhlxbg/{id}")
    public String auditjhlxbg(@PathVariable("id") String  id, ModelMap map)
    {
        SmBizTask smBizTask = taskService.selectSchedulingId(id);
        //获取当前页面的信息
        SmBizScheduling scheduling = addlxbgService.selectSchedulingById(id);

        //获取审核人
        String pid = ShiroUtils.getOgUser().getpId();

        OgPerson ogPersonName = ogPersonService.selectOgPersonById(pid);

        String pname = ogPersonName.getpName();

        //计划类别
        scheduling.setTaskTypeId(smBizTask.getTaskTypeId());
        //是否短信通知
        scheduling.setMsgDoor(smBizTask.getMsgDoor());
        //接收范围
        scheduling.setSendRange(smBizTask.getSendRange());
        map.put("scheduling",scheduling);
        map.put("smBizTask",smBizTask);
        map.put("pname",pname);

        //执行日期进行日期回显
        String performDate = smBizTask.getStartTime();
        if(StringUtils.isNotEmpty(performDate)){
            scheduling.setPerformDate(DateUtils.formatDateStr(performDate,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            map.addAttribute("performDate",scheduling.getPerformDate());
        }

        //获取当前节点对象的信息
        OgOrg ogOrg = deptService.selectDeptById(scheduling.getCreatorDeptId());
        if(ogOrg !=null){
            String levelCode = ogOrg.getLevelCode();
            List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode,"8200");
            map.put("checkList", checkList);

        }
        //获取路径
        SmBizFolder folder = folderService.selectFolderTreeById(scheduling.getFolder());
        map.put("sf",folder.getName_());

        List<AmBizPara> list = amBizParaService.selectAmBizParaList(new AmBizPara());
        List<Map<String,String>> reList=new ArrayList<>();
        for(AmBizPara aa:list){
            Map<String,String> mp=new HashMap<>();
            mp.put("id",aa.getAmParaId());
            mp.put("value",aa.getReceiveRange());
            reList.add(mp);
        }
        map.put("sendRangeList" ,reList);

        //创建时间进行日期回显
        String createTime = scheduling.getCreateTime();
        if(StringUtils.isNotEmpty(createTime)){
            scheduling.setCreateTime(DateUtils.formatDateStr(createTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        //执行日期进行日期回显
        String startTime = smBizTask.getStartTime();
        if(StringUtils.isNotEmpty(startTime)){
            smBizTask.setStartTime(DateUtils.formatDateStr(startTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        String checkTime = DateUtils.getTime();
        map.put("checkTime",checkTime);

        // 根据计划单ID查询延期信息
        SmBizLxbgApply apply = delayLxbgService.selectByScId(id);
        if(!StringUtils.isEmpty(apply)){
            //获取处长审核人
            OgPerson person = ogPersonService.selectOgPersonById(apply.getCheckId());
            if(!StringUtils.isEmpty(person.getpName())){
                map.put("psername",ogPersonName.getpName());
            }
            //延期日期
            String releaseTime = apply.getReleaseTime();
            if(StringUtils.isNotEmpty(releaseTime)){
                apply.setReleaseTime(DateUtils.formatDateStr(releaseTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
                map.addAttribute("releaseTime",apply.getReleaseTime());
            }
            //原因
            map.put("reason",apply.getReason());
        }
        return jh_prefix + "/authorjhlxbg";
    }



    /**
     * 计划审核人审核是否通过
     *
     * @param
     * @return
     */
    @PostMapping("/auditjhPass")
    @ResponseBody
    public AjaxResult auditjhPass(SmBizScheduling scheduling) {

        Map<String, Object> map = new HashMap<>();
        SmBizScheduling smBizScheduling = addlxbgService.selectSchedulingById(scheduling.getSchedulingId());
        //根据计划单ID查询延期表信息
        SmBizLxbgApply apply = delayLxbgService.selectByScId(scheduling.getSchedulingId());
        String comment = scheduling.getJhChecktext();//审核意见
        map.put("userId", ShiroUtils.getUserId());
        String businessKey = scheduling.getSchedulingId();
        map.put("businessKey", businessKey);
        map.put("comment", comment);
        String processDefinitionKey = "LXBGdelay";
        map.put("processDefinitionKey", processDefinitionKey);
        apply.setJhChecktext(scheduling.getJhChecktext());
        if ("0".equals(scheduling.getFlag())) {
            //通过
            map.put("reCode","0");
            //状态 1:待处长审核  2：计划审核人审核  3:关闭   4:处长审核不通过   5.计划人审核不通过
            apply.setAppType("3");
            SmBizTask task = taskService.selectSchedulingId(scheduling.getSchedulingId());
            if(!StringUtils.isEmpty(task)){
                //修改发布时间    作业表 发布时间修改
                task.setTaskId(task.getTaskId());
                task.setStartTime(apply.getReleaseTime());
                taskService.updateTaskByDelay(task);
                //修改发布时间    任务表  发布时间修改
                SmBizTaskinfo smBizTaskinfo = new SmBizTaskinfo();
                smBizTaskinfo.setTaskId(task.getTaskId());
                smBizTaskinfo.setPerformDate(apply.getReleaseTime());
                smBizTaskinfoService.updateSmBizTaskinfoByTaskId(smBizTaskinfo);
            }
            try{
                delayLxbgService.updateSmBizLxbgapply(apply);
                activitiCommService.complete(map);
            }catch (Exception e){
                log.error("延期申请例行计划单审核失败: "+e.getMessage());
                throw  new BusinessException("延期申请例行计划单审核失败,请刷新页面进行重试");
            }
            return AjaxResult.success("操作成功");
        }else {
            //不通过
            map.put("reCode","1");
            apply.setAppType("5");
            try{
                delayLxbgService.updateSmBizLxbgapply(apply);
                activitiCommService.complete(map);
            }catch (Exception e){
                log.error("延期申请例行计划单审核否决失败: "+e.getMessage());
                throw  new BusinessException("延期申请例行计划单审核否决失败,请刷新页面进行重试");
            }
            return AjaxResult.success("操作成功");
        }
    }


    /**
     * 转到查询延期例行计划页面
     * @param map
     * @return
     */
    @GetMapping("/selectlxbgyq")
    public String selectlxbgyq(ModelMap map)
    {

        return se_prefix + "/selectlxbgyq";
    }

    /**
     * 列表展示
     * @param scheduling
     * @return
     */
    @PostMapping("/yqlxbglist")
    @ResponseBody
    public TableDataInfo yqlxbglist(SmBizScheduling scheduling)
    {
        //展示所有延期计划例行变更计划单
        if(StringUtils.isNotEmpty(scheduling.getDelayCreateTime())){
            String parseStart = DateUtils.handleTimeYYYYMMDD(scheduling.getDelayCreateTime());
            scheduling.setDelayCreateTime(parseStart+"000000");
        }
        if(StringUtils.isNotEmpty(scheduling.getDelayEndTime())){
            String parseEnd = DateUtils.handleTimeYYYYMMDD(scheduling.getDelayEndTime());
            scheduling.setDelayEndTime(parseEnd+"240000");
        }
        startPage();
        SmBizLxbgApply smBizLxbgApply = new SmBizLxbgApply();
        List<SmBizLxbgApply> applies = delayLxbgService.selectSmBizLxbgapplyList(smBizLxbgApply);
        List<SmBizScheduling> list = addlxbgService.selectScheduling(scheduling);
        List<String>  schedulingIds= new ArrayList<>();
        for(SmBizLxbgApply apply:applies){
            schedulingIds.add(apply.getSchedulingId());
        }

        //过滤条件
        List<SmBizScheduling> list1 = list.stream().filter((item) -> {
            return schedulingIds.contains(item.getSchedulingId());
        }).collect(Collectors.toList());



        return getDataTable(list1);

    }


    /**
     * 转到查看详情页面
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/yqlxbgdetail/{id}")
    public String yqlxbgdetail(@PathVariable("id") String  id, ModelMap map)
    {
        SmBizTask smBizTask = taskService.selectSchedulingId(id);
        //获取当前页面的信息
        SmBizScheduling scheduling = addlxbgService.selectSchedulingById(id);

        //获取审核人
        String pid = ShiroUtils.getOgUser().getpId();

        OgPerson ogPersonName = ogPersonService.selectOgPersonById(pid);

        String pname = ogPersonName.getpName();

        //计划类别
        scheduling.setTaskTypeId(smBizTask.getTaskTypeId());
        //是否短信通知
        scheduling.setMsgDoor(smBizTask.getMsgDoor());
        //接收范围
        scheduling.setSendRange(smBizTask.getSendRange());
        map.put("scheduling",scheduling);
        map.put("smBizTask",smBizTask);
        map.put("pname",pname);

        //执行日期进行日期回显
        String performDate = smBizTask.getStartTime();
        if(StringUtils.isNotEmpty(performDate)){
            scheduling.setPerformDate(DateUtils.formatDateStr(performDate,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
            map.addAttribute("performDate",scheduling.getPerformDate());
        }

        //获取当前节点对象的信息
        OgOrg ogOrg = deptService.selectDeptById(scheduling.getCreatorDeptId());
        if(ogOrg !=null){
            String levelCode = ogOrg.getLevelCode();
            List<OgPerson> checkList = ogPersonService.selectListByLevelCode(levelCode,"8200");
            map.put("checkList", checkList);

        }
        //获取路径
        SmBizFolder folder = folderService.selectFolderTreeById(scheduling.getFolder());
        map.put("sf",folder.getName_());

        List<AmBizPara> list = amBizParaService.selectAmBizParaList(new AmBizPara());
        List<Map<String,String>> reList=new ArrayList<>();
        for(AmBizPara aa:list){
            Map<String,String> mp=new HashMap<>();
            mp.put("id",aa.getAmParaId());
            mp.put("value",aa.getReceiveRange());
            reList.add(mp);
        }
        map.put("sendRangeList" ,reList);

        //创建时间进行日期回显
        String createTime = scheduling.getCreateTime();
        if(StringUtils.isNotEmpty(createTime)){
            scheduling.setCreateTime(DateUtils.formatDateStr(createTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        //执行日期进行日期回显
        String startTime = smBizTask.getStartTime();
        if(StringUtils.isNotEmpty(startTime)){
            smBizTask.setStartTime(DateUtils.formatDateStr(startTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
        }

        String checkTime = DateUtils.getTime();
        map.put("checkTime",checkTime);

        // 根据计划单ID查询延期信息
        SmBizLxbgApply apply = delayLxbgService.selectByScId(id);
        if(!StringUtils.isEmpty(apply)){
            //获取处长审核人
            OgPerson person = ogPersonService.selectOgPersonById(apply.getCheckId());
            if(!StringUtils.isEmpty(person.getpName())){
                map.put("psername",ogPersonName.getpName());
            }
            //延期日期
            String releaseTime = apply.getReleaseTime();
            if(StringUtils.isNotEmpty(releaseTime)){
                apply.setReleaseTime(DateUtils.formatDateStr(releaseTime,DateUtils.YYYYMMDDHHMMSS,DateUtils.YYYY_MM_DD_HH_MM_SS));
                map.addAttribute("releaseTime",apply.getReleaseTime());
            }
            //原因
            map.put("reason",apply.getReason());
        }
        return se_prefix + "/yqlxbgdetail";
    }




}
