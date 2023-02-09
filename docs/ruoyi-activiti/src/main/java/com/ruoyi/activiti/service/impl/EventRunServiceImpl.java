package com.ruoyi.activiti.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.activiti.constants.ImBizIssueConstants;
import com.ruoyi.activiti.constants.WorkStatusConstants;
import com.ruoyi.activiti.constants.eventRunConstants;
import com.ruoyi.activiti.controller.itsm.eventRun.EventRunApiController;
import com.ruoyi.activiti.domain.EventRun;
import com.ruoyi.activiti.domain.ItsmWorkStatus;
import com.ruoyi.activiti.domain.OgPersonDuty;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.mapper.EventRunMapper;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.EventRunService;
import com.ruoyi.activiti.service.IOgPersonDutyService;
import com.ruoyi.activiti.service.ItsmWorkStatusService;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgSys;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.SysNotify;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import com.ruoyi.system.service.ISysNotifyService;
import com.ruoyi.system.service.ISysWorkService;
import com.ruoyi.system.service.impl.OgPersonServiceImpl;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 运行事件单Service业务层处理
 *
 * @author zx
 * @date 2021-03-04
 */
@Service
public class EventRunServiceImpl implements EventRunService
{
    @Autowired
    private EventRunMapper eventRunMapper;
    @Autowired
    private IPubParaValueService iPubParaValueService;
    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private ISysWorkService iSysWorkService;
    @Autowired
    private IOgPersonDutyService iOgPersonDutyService;
    @Autowired
    private ItsmWorkStatusService itsmWorkStatusService;
    @Autowired
    private ISysApplicationManagerService iSysApplicationManagerService;
    @Autowired
    private PubBizPresmsServiceImpl pubBizPresmsService;
    @Autowired
    private PubBizSmsServiceImpl PubBizSmsService;
    @Autowired
    private OgPersonServiceImpl ogPersonService;
    @Autowired
    private ISysNotifyService sysNotifyService;
    private String msgToAll="websocket.EventRun";
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    private static final Log logger = LogFactory.getLog(EventRunServiceImpl.class);
    /**
     * 查询运行事件单
     *
     * @param eventId 运行事件单ID
     * @return 运行事件单
     */
    @Override
    public EventRun selectEventRunById(String eventId)
    {
        if("mysql".equals(dbType)){
            EventRun eventRun=eventRunMapper.selectEventRunByIdMysql(eventId);
            return eventRun;
        }else{
            EventRun eventRun=eventRunMapper.selectEventRunById(eventId);
            return eventRun;
        }
    }

    /**
     * 查询运行事件单
     *
     * @param eventNo 运行事件单ID
     * @return 运行事件单
     */
    @Override
    public EventRun selectEventRunByNo(String eventNo)
    {
        if("mysql".equals(dbType)){
            return eventRunMapper.selectEventRunByNoMysql(eventNo);
        }else{
            return eventRunMapper.selectEventRunByNo(eventNo);
        }
    }


    /**
     * 查询运行事件单列表
     *
     * @param eventRun 运行事件单
     * @return 运行事件单
     */
    @Override
    public List<EventRun> selectEventRunList(EventRun eventRun)
    {

        if("mysql".equals(dbType)){
            return eventRunMapper.selectEventRunListMysql(eventRun);
        }else{
            return eventRunMapper.selectEventRunList(eventRun);
        }
    }

    /**
     * 新增运行事件单
     *
     * @param eventRun 运行事件单
     * @return 结果
     */
    @Override
    public int insertEventRun(EventRun eventRun)
    {
        eventRun.setCreateTime(DateUtils.dateTimeNow());
        return eventRunMapper.insertEventRun(eventRun);
    }

    /**
     * 修改运行事件单
     *
     * @param eventRun 运行事件单
     * @return 结果
     */
    @Override
    public int updateEventRun(EventRun eventRun)
    {
        if("mysql".equals(dbType)){
            return eventRunMapper.updateEventRunMysql(eventRun);
        }else{
            return eventRunMapper.updateEventRun(eventRun);
        }
    }

    /**
     * 删除运行事件单对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteEventRunByIds(String ids)
    {
        return eventRunMapper.deleteEventRunByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除运行事件单信息
     *
     * @param eventId 运行事件单ID
     * @return 结果
     */
    @Override
    public int deleteEventRunById(String eventId)
    {
        return eventRunMapper.deleteEventRunById(eventId);
    }

    @Override
    public int updateFmYxByInvalidationMark(String id) {
        return eventRunMapper.updateFmYxByInvalidationMark(id);
    }
    /**
     * 任务耗时查询
     * @param eventRun
     * @return
     */
    @Override
   public List<EventRun> selectByTiming(EventRun eventRun){
        if("mysql".equals(dbType)){
            return eventRunMapper.selectByTimingMysql(eventRun);
        }else{
            return eventRunMapper.selectByTiming(eventRun);
        }
   }
    /**
     * 匹配值班人ID
     * @param eventRun
     * @return
     */
    @Override
    public String selectDutyUserId(EventRun eventRun,String loginUserId){
        String sysCode=eventRun.getAppSystemCode();
        String sysName=eventRun.getAppSystemName();
        String handle=eventRun.getHandleRoles();
        List<OgPersonDuty> listRun=new ArrayList<>();
        if("6".equals(handle)||"8".equals(handle)){
            //应用 查询工作组
            String grouptype="6".equals(handle)?"5":"2";
            OgGroup ogGroup=new OgGroup();
            OgSys ogSys=null;
            try {
                if (eventRun.getEventSource().equals("01")) {
                    //本地发起 appsystemCode 存的是sysId
                    if (StringUtils.isNotEmpty(sysCode)) {
                        ogSys = iSysApplicationManagerService.selectOgSysBySysId(sysCode);
                    }
                } else {
                    ogSys = iSysApplicationManagerService.selectOgSysBySysCode(sysCode);
                }
                if (ogSys == null&&StringUtils.isNotEmpty(sysName)) {
                    ogSys = iSysApplicationManagerService.selectOgSysBySysName(sysName);
                }
            }catch (Exception e){
                logger.debug("========监控事件单匹配：根据系统编码查询系统报错！"+e.getMessage());
                e.printStackTrace();
            }
            ogGroup.setGroupType(grouptype);
            List<OgGroup> groupList=new ArrayList<>();
            if(ogSys!=null){
                ogGroup.setSysId(ogSys.getSysId());
                groupList=iSysWorkService.selectOgGroupList(ogGroup);
            }
            //通过工作组查值班人
            listRun=serchOgPersonDuty(groupList,handle);
            if(CollectionUtils.isEmpty(listRun)&&!CollectionUtils.isEmpty(groupList)){
                //工作组中没有匹配到 状态合适的值班人
                listRun=serchOgPersonDuty(null,handle);
                //检查值班人状态，匹配合适的值班人
                listRun=checkPersonStatus(listRun);
            }
        }else {
              //非应用条线
              List<OgGroup> groupList=searchGroup(handle);
              //通过工作组查值班人
              listRun=serchOgPersonDuty(groupList,handle);
            if(CollectionUtils.isEmpty(listRun)&&!CollectionUtils.isEmpty(groupList)){
                //工作组中没有匹配到 状态合适的值班人
                listRun=serchOgPersonDuty(null,handle);
                //检查值班人状态，匹配合适的值班人
                listRun=checkPersonStatus(listRun);
            }
        }
        //计算任务量最少的值班人 返回Id
        String id=dtuyUserId(listRun,loginUserId);
        return id;
    }

    /**
     * 通过工作组查询值班人
     * @return
     */
    public  List<OgPersonDuty> serchOgPersonDuty(List<OgGroup> groupList,String handle){
        List<OgPersonDuty> listRun=new ArrayList<>();
        if(CollectionUtils.isEmpty(groupList)){
            //工作组为空 按照监控事件类型匹配
            PubParaValue pubPar=iPubParaValueService.selectPubParaValue("duty_type",handle);
            String paraValueId=pubPar.getParaValueId();
            OgPersonDuty ogPersonDuty=new OgPersonDuty();
            ogPersonDuty.setMonitorTypeAccountId(paraValueId);
            listRun=iOgPersonDutyService.selectOgPersonDutyList(ogPersonDuty);
            listRun=checkPersonStatus(listRun);
        } else {
            for(OgGroup op:groupList){
                OgPersonDuty ogPersonGroup=new OgPersonDuty();
                ogPersonGroup.setGroupId(op.getGroupId());
                //ogPersonGroup.setDutyType(op.getGroupType());
                listRun.addAll(iOgPersonDutyService.selectOgPersonDutyList(ogPersonGroup));
                listRun=checkPersonStatus(listRun);
            }
            if(CollectionUtils.isEmpty(listRun)){
                //工作组内值班人为空 按照监控事件类型匹配
                PubParaValue pubPar=iPubParaValueService.selectPubParaValue("duty_type",handle);
                String paraValueId=pubPar.getParaValueId();
                OgPersonDuty ogPersonDuty=new OgPersonDuty();
                ogPersonDuty.setMonitorTypeAccountId(paraValueId);
                listRun=iOgPersonDutyService.selectOgPersonDutyList(ogPersonDuty);
                listRun=checkPersonStatus(listRun);
            }
        }
        //应用一线匹配不到值班人，转应用二线值班人
        if(CollectionUtils.isEmpty(listRun)&&"6".equals(handle)){
            //工作组为空 按照监控事件类型匹配
            PubParaValue pubPar=iPubParaValueService.selectPubParaValue("duty_type","8");
            String paraValueId=pubPar.getParaValueId();
            OgPersonDuty ogPersonDuty=new OgPersonDuty();
            ogPersonDuty.setMonitorTypeAccountId(paraValueId);
            listRun=iOgPersonDutyService.selectOgPersonDutyList(ogPersonDuty);
            listRun=checkPersonStatus(listRun);
        }
        if(CollectionUtils.isEmpty(listRun)){
            //类型查询为空 查询 运行值班人
            OgPersonDuty ogPersonDutyRun=new OgPersonDuty();
            ogPersonDutyRun.setDutyType("6");
            listRun=iOgPersonDutyService.selectOgPersonDutyList(ogPersonDutyRun);
            listRun=checkPersonStatus(listRun);
        }
        return listRun;
    }

    /**
     * 验证值班人状态，返回合适的值班人
     * @param listRun
     * @return
     */
    public List<OgPersonDuty> checkPersonStatus(List<OgPersonDuty> listRun){
        //验证登录状态
        if(!CollectionUtils.isEmpty(listRun)){
            List<OgPersonDuty> reList=new ArrayList<>();
            for(OgPersonDuty Ogdt:listRun){
                //验证登录状态
                ItsmWorkStatus is = itsmWorkStatusService.selectItsmWorkStatusByPid(Ogdt.getUserId());
                if(is!=null){
                    if (is.getUserId().equals(Ogdt.getUserId()) && !WorkStatusConstants.qtzt.equals(is.getWorkStatus())) {
                        reList.add(Ogdt);
                    }
                }
            }
            listRun=reList;
        }else {
            return null;
        }
        return listRun;
    }
    /**
     *  非应用条线  查工作组
     * @param handle
     * @return
     */
    public List<OgGroup> searchGroup(String handle){
        //暂定，待时间充裕优化  工作组类型 系统3 网络 4
        //0-网络告警派单（合肥）
        //1-网络告警派单（亦庄）
        //2-网络告警派单（丰台）
        //3-系统告警派单（合肥）
        //4-系统告警派单（亦庄）
        //5-系统告警派单（丰台）
        //6-应用告警派单-> 应用一线
        //7-告警派单（无所属中心）->丰台监控值班，合肥监控值班
        //系统 非空 查询工作组
        OgGroup ogGroup=new OgGroup();
        switch (handle){
            //合肥网络
            case "0":
                ogGroup.setOrgId(ImBizIssueConstants.ORG_DATACENTER_HEFEI);
                ogGroup.setGroupType(eventRunConstants.GROUP_TYPE_WEB);
                break;
            //亦庄网络
            case "1":
                ogGroup.setOrgId(ImBizIssueConstants.ORG_DATACENTER_YIZHUNAG);
                ogGroup.setGroupType(eventRunConstants.GROUP_TYPE_WEB);
                break;
            //丰台网络
            case "2":
                ogGroup.setOrgId(ImBizIssueConstants.ORG_DATACENTER_FENGTAI);
                ogGroup.setGroupType(eventRunConstants.GROUP_TYPE_WEB);
                break;
            //合肥系统
            case "3":
                ogGroup.setOrgId(ImBizIssueConstants.ORG_DATACENTER_HEFEI);
                ogGroup.setGroupType(eventRunConstants.GROUP_TYPE_SYS);
                break;
            //亦庄系统
            case "4":
                ogGroup.setOrgId(ImBizIssueConstants.ORG_DATACENTER_YIZHUNAG);
                ogGroup.setGroupType(eventRunConstants.GROUP_TYPE_SYS);
                break;
            //丰台系统
            case "5":
                ogGroup.setOrgId(ImBizIssueConstants.ORG_DATACENTER_FENGTAI);
                ogGroup.setGroupType(eventRunConstants.GROUP_TYPE_SYS);
                break;
            //告警派单（无所属中心）
            default:
                ogGroup.setGroupType("6");
                break;
        }
        return iSysWorkService.selectOgGroupAllList(ogGroup);
    }
    /**
     * 计算值班人任务量 取最少任务的人员 id
     * @return
     */
    public String dtuyUserId( List<OgPersonDuty> list,String loginUserId){
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        LinkedHashMap<String, Integer> usersInfo = new LinkedHashMap<>();
        for(OgPersonDuty Ogdt:list){
            List<Task> dtTask=activitiCommService.allTasks(Ogdt.getUserId(),"FmYx");
            usersInfo.put(Ogdt.getUserId(), dtTask.size());
        }
        //按任务量排序
        List<Map.Entry<String,Integer>> infos = new ArrayList<>(usersInfo.entrySet());
        infos.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> t0,
                               Map.Entry<String, Integer> t1) {
                return t1.getValue() - t0.getValue();
            }
        });
        Map.Entry<String,Integer> user=infos.get(infos.size()-1);
        String userId=user.getKey();
        //如果匹配到当前登陆人
       if(StringUtils.isNotEmpty(userId)&&userId.equals(loginUserId)){
            infos.remove(user);
            if(infos.isEmpty()){
                return null;
            }else {
                return infos.get(infos.size()-1).getKey();
            }
        }
        return userId;
    }
    /**
     * 监控事件单转派
     * @param eventId
     * @param userId
     */
    public void reassign(String eventId,String userId){
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("reCode", "2");
        reMap.put("dealId", userId);
        Task tk = activitiCommService.getTask(eventId, "chuli");
        reMap.put("taskId", tk.getId());
        reMap.put("businessKey", eventId);
        reMap.put("processDefinitionKey", "FmYx");
        activitiCommService.complete(reMap);
    }
    /**
     * 待办查询
     * @param eventRun
     * @return
     */
    public  List<EventRun> selectActEventRunList(EventRun eventRun){
        if("mysql".equals(dbType)){
            return eventRunMapper.selectActEventRunListMysql(eventRun);
        }else{
            return eventRunMapper.selectActEventRunList(eventRun);
        }
    }
    /**
     * 发送短信方法(不包含上行)
     *
     * @param
     * @param msg
     */
    @Override
    public void sendMsgNoUp(String userId, String msg) {
        OgPerson person = ogPersonService.selectOgPersonById(userId);
        if(person!=null){
            PubBizPresms pubBizPresms = new PubBizPresms();
            pubBizPresms.setPubBizPresmsId(UUID.getUUIDStr());
            pubBizPresms.setTelephone(person.getMobilPhone());
            pubBizPresms.setName(person.getpName());
            pubBizPresms.setSmsText(msg);
            pubBizPresms.setCreaterId(person.getpId());
            pubBizPresms.setInvalidationMark("1");
            pubBizPresms.setSmsState("0");
            pubBizPresms.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
            pubBizPresms.setInspectObject("050100");// 检查对象
            pubBizPresms.setInspectTime(DateUtils.dateTimeNow());// 检查时间
            //pubBizPresms.setDealStatus("0");
            pubBizPresmsService.insertPubBizPresms(pubBizPresms);
            PubBizSmsService.findPreSmsAndSend(pubBizPresms);
        }
    }
    /**
     * 查询我处理过的任务
     * @param eventRun
     * @return
     */
    @Override
    public List<EventRun> selectMydealEventRunList(EventRun eventRun){
        if("mysql".equals(dbType)){
            return eventRunMapper.selectMydealEventRunListMysql(eventRun);
        }else{
            return eventRunMapper.selectMydealEventRunList(eventRun);
        }
    }
    /**
     * 系统通知
     * @param eventNo
     * @param userId
     */
    @Override
    public void sysNotify(String eventNo,String userId){
       JSONObject jsonObject=new JSONObject();
        jsonObject.put("message","新增监控事件单任务："+eventNo);
        jsonObject.put("userId",userId);
        redisTemplate.convertAndSend(msgToAll, jsonObject.toString());
    }
    @Override
    public void sysNotifyTime(String chnell, JSONObject jsonObject) {
        redisTemplate.convertAndSend(chnell, jsonObject.toString());
    }
}
