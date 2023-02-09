package com.ruoyi.activiti.controller.itsm.eventRun;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.activiti.constants.eventRunConstants;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.EventRunService;
import com.ruoyi.activiti.domain.EventRun;
import com.ruoyi.activiti.service.impl.PubBizPresmsServiceImpl;
import com.ruoyi.activiti.service.impl.PubBizSmsServiceImpl;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.domain.entity.IdGenerator;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.esb.invoke.ServiceInvoker;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.impl.OgPersonServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.activiti.editor.language.json.converter.util.CollectionUtils;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 监控接口
 */
@RestController
@RequestMapping("/eventRun")
public class EventRunApiController {
    @Autowired
    private EventRunService eventRunService;
    @Autowired
    private IIdGeneratorService idGeneratorService;
    @Autowired
    private ActivitiCommService activitiCommService;
    private static final Log logger = LogFactory.getLog(EventRunApiController.class);
    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(3, 5, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @ApiOperation("新增告警/监控事件单")
    @PostMapping("/report")
    @Transactional
    @RepeatSubmit
    public JSONObject save(String report) throws Exception {
        logger.debug("=====================监控事件单新增接口,监控调用===============");
        logger.debug("=======监控入参：" + report);
        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                JSONObject reJson = new JSONObject();
                try {
                    JSONObject jsonObject = JSONObject.parseObject(report);
                    String into = "id,eventTitle,eventDescr,reportSource,eventSource,eventLevel,eventType,reportTime,handleRoles";
                    String[] intos = into.split(",");
                    String error = "";
                    for (String is : intos) {
                        if (StringUtils.isEmpty(jsonObject.getString(is))) {
                            error = "参数：" + is + "为空";
                            break;
                        }
                    }
                    if (StringUtils.isNotEmpty(error)) {
                        reJson.put("reCode", "1");
                        reJson.put("reMessage", error);
                        logger.debug("=====================监控事件单新增接口,监控调用结束入参为空===============");
                        logger.debug(reJson);
                        return reJson;
                    }
                    EventRun eventRun = JSONObject.parseObject(report, EventRun.class);
                    if (StringUtils.isEmpty(eventRun.getAffiliatedCenter())) {
                        eventRun.setAffiliatedCenter("5");
                    }
                    eventRun.setEventId(jsonObject.get("id").toString());
                    EventRun er = eventRunService.selectEventRunById(eventRun.getEventId());
                    if (er != null) {
                        reJson.put("reCode", "1");
                        reJson.put("reMessage", "该告警已提交过！");
                        logger.debug("=====================监控事件单新增接口,监控调用结束重复提交===============");
                        logger.debug(reJson);
                        return reJson;
                    }
                    //待定
                    /*if("6".equals(eventRun.getHandleRoles())||"8".equals(eventRun.getHandleRoles())){
                        if(StringUtils.isEmpty(eventRun.getAppSystemName())||StringUtils.isEmpty(eventRun.getAppSystemCode())){
                            reJson.put("reCode", "1");
                            reJson.put("reMessage", "应用条线告警，系统名称及系统编码为必输项！");
                            logger.debug("=====================监控事件单新增接口,监控调用结束系统名称及系统编码为空===============");
                            logger.debug(reJson);
                            return reJson;
                        }
                    }*/
                    //接口生成默认创建人为 管理员
                    eventRun.setCreateId(eventRunConstants.USERID);
                    //创建事件为告警时间
                    eventRun.setCreateTime(DateUtils.dateTimeNow());
                    //报文落地
                    eventRun.setRequestClob(report);
                    //来源
                    eventRun.setEventSource("02");
                    //生成单号
                    String bizType = "YXSJ";
                    IdGenerator generator = new IdGenerator();
                    String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
                    generator.setCurrentDate(nowDateStr);
                    generator.setBizType(bizType);
//                    String numStr = idGeneratorService.selectIdGeneratorByType(generator);
//                    eventRun.setEventNo(bizType + nowDateStr + numStr);
                    eventRun.setStatus("1");
                    eventRun.setEventType(eventRun.getHandleRoles());
                    //启动流程
                    Map<String, Object> reMap = new HashMap<>();
                    String dealId = eventRunService.selectDutyUserId(eventRun, eventRunConstants.USERID);
                    if (StringUtils.isEmpty(dealId)) {
                        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
                        eventRun.setEventNo(bizType + nowDateStr + numStr);
                        eventRun.setWebAttendant("");
                        eventRun.setSysAttendant("");
                        eventRun.setStatus("0");
                        eventRunService.insertEventRun(eventRun);
                        reJson.put("reCode", "1");
                        reJson.put("reMessage", "未找到匹配的值班人员！");
                        logger.debug("=====================监控事件单新增接口,监控调用失败值班人员未找到===============");
                        logger.debug(reJson);
                        return reJson;
                    }
                    reMap.put("dealId", dealId);
                    reMap.put("createId", eventRun.getCreateId());
                    reMap.put("dutyAccountId", eventRun.getCreateId());
                    reMap.put("userId", eventRunConstants.USERID);
                    List<ProcessInstance> pro = activitiCommService.processInfo(eventRun.getEventId());
                    if (!CollectionUtils.isEmpty(pro)) {
                        reJson.put("reCode", "1");
                        reJson.put("reMessage", "流程已启动！");
                        logger.debug("=====================监控事件单新增接口,监控调用失败值班人员未找到===============");
                        logger.debug(reJson);
                        return reJson;
                    }
                    eventRun.setDutyAccount(dealId);
                    eventRun.setHandlePerson(dealId);
                    eventRun.setWebAttendant("");
                    eventRun.setSysAttendant("");
                    String numStr = idGeneratorService.selectIdGeneratorByType(generator);
                    eventRun.setEventNo(bizType + nowDateStr + numStr);
                    eventRunService.insertEventRun(eventRun);
                    String smMsg = "单号为：" + eventRun.getEventNo() + "监控事件单已经分派给您，请及时处理！";
                    eventRunService.sysNotify(eventRun.getEventNo(), eventRun.getHandlePerson());
                    eventRunService.sendMsgNoUp(dealId, smMsg);
                    activitiCommService.startProcess(eventRun.getEventId(), "FmYx", reMap);
                    reJson.put("reCode", "0");
                    reJson.put("reMessage", "success");
                } catch (Exception e) {
                    e.printStackTrace();
                    reJson.put("reCode", "1");
                    reJson.put("reMessage", e.getMessage());
                }
                return reJson;
            }
        };
        Future future = poolExecutor.submit(callable);
        logger.debug("=====================监控事件单新增接口,监控调用结束===============");
        logger.debug(future.get());
        return (JSONObject) future.get();
    }

    @ApiOperation("告警关闭事件推送")
    @PostMapping("/closeReport")
    @Transactional
    @RepeatSubmit
    public JSONObject update(String upString) throws Exception {
        logger.debug("=====================监控事件单告警关闭推送接口,监控调用===============");
        logger.debug(upString);
        Callable callable = new Callable() {
            @Override
            public JSONObject call() throws Exception {
                JSONObject reJson = new JSONObject();
                try {
                    JSONObject upJson = JSONObject.parseObject(upString);
                    StringBuffer sbf = new StringBuffer();
                    if (StringUtils.isNull(upJson.get("id"))) {
                        sbf.append("id为空");
                    }
                    if (StringUtils.isNull(upJson.get("closeTime"))) {
                        sbf.append("closeTime为空");
                    }
                    if (StringUtils.isNotEmpty(sbf)) {
                        reJson.put("reCode", "1");
                        reJson.put("reMessage", sbf);
                        logger.debug("=====================监控事件单告警关闭推送接口,监控调用结束入参为空===============");
                        logger.debug(reJson);
                        return reJson;
                    }
                    EventRun eventRun = new EventRun();
                    eventRun.setEventId(upJson.get("id").toString());
                    EventRun er = eventRunService.selectEventRunById(upJson.get("id").toString());
                    if (er == null) {
                        reJson.put("reCode", "1");
                        reJson.put("reMessage", "id不存在");
                        logger.debug("=====================监控事件单告警关闭推送接口,监控调用结束告警不存在===============");
                        logger.debug(reJson);
                        return reJson;
                    } else {
                        eventRun.setCloseTime(upJson.getString("closeTime"));
                        String reportTime = eventRun.getReportTime();
                        eventRun.setReportTime(DateUtils.handleTimeYYYYMMDDHHMMSS(reportTime));
                        eventRun.setRequestClob(er.getRequestClob() + upString);
                        if ("4".equals(eventRun.getStatus())) {
                            String text = "单号为：" + eventRun.getEventNo() + "。的监控事件单告警已关闭，请登录平台处理。";
                            eventRunService.sendMsgNoUp(eventRun.getHandlePerson(), text);
                        }
                        eventRunService.updateEventRun(eventRun);
                        reJson.put("reCode", "0");
                        reJson.put("reMessage", "success");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    reJson.put("reCode", "1");
                    reJson.put("reMessage", e.getMessage());
                    return reJson;
                }
                logger.debug("=====================监控事件单关闭接口,监控调用结束===============");
                logger.debug(reJson);
                return reJson;
            }
        };
        Future future = poolExecutor.submit(callable);
        return (JSONObject) future.get();
    }
}
