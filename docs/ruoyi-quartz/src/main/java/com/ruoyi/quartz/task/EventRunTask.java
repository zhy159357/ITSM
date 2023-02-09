package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.activiti.constants.eventRunConstants;
import com.ruoyi.activiti.domain.EventRun;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.EventRunService;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.domain.entity.IdGenerator;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IPubParaValueService;
import io.swagger.annotations.ApiOperation;
import net.sf.json.util.JSONUtils;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component("EventRunTask")
public class EventRunTask {
    @Autowired
    private EventRunService eventRunService;
    @Autowired
    private IIdGeneratorService idGeneratorService;
    @Autowired
    private IPubParaValueService iPubParaValueService;
    @Autowired
    private ActivitiCommService activitiCommService;
    //监控地址
    @Value("${FmYx.url}")
    private String eventUrl;
    @Value("${FmYx.monitorUrl}")
    private String monitorUrl;
    @Value("${FmYx.detailUrl}")
    private String detailUrl;
    @Value("${FmYx.apiKey}")
    private String apiKey;
    protected final Logger logger = LoggerFactory.getLogger(EventRunTask.class);
    /**
     * 对账
     */
    @ApiOperation("监控对账定时任务")
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
}
