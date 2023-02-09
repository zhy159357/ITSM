package com.ruoyi.quartz.task;

import com.ruoyi.activiti.domain.ActHiActinst;
import com.ruoyi.activiti.service.IActHiActInstService;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.form.domain.EventSheet;
import com.ruoyi.form.enums.EventStatusEnum;
import com.ruoyi.form.service.IEventSheetService;
import com.ruoyi.system.service.IPubParaValueService;
import org.activiti.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component("eventAutoCloseTask")
public class EventAutoCloseTask {

    private static final Logger log = LoggerFactory.getLogger(EventAutoCloseTask.class);

    @Autowired
    private IPubParaValueService pubParaValueService;

    @Autowired
    private IEventSheetService eventService;

    @Autowired
    private IEventSheetService eventSheetService;

    @Autowired
    private IActHiActInstService actHiActinstService;

    @Resource
    TaskService taskService;

    public void eventAutoClose() {
        log.info("---------------事件单自动关闭开始...");
        try {
            List<PubParaValue> vs = pubParaValueService.selectPubParaValueByParaName("event_auto_day");
            PubParaValue pv = vs.get(0);
            int dayNum = Integer.parseInt(pv.getValue());//从配置参数中拿到天数
            // 查询待关闭的事件单数据
            List<EventSheet> eventSheets = eventService.selectEventAutoCloseList();
            if (CollectionUtils.isEmpty(eventSheets)) {
                log.info("---------------没有要关闭的数据...");
                return;
            }

            // 获取到流程实例集合
            List<String> instanceIds = eventSheets.stream().map(EventSheet::getInstanceId).collect(Collectors.toList());
            // 查询历史流程实例
            List<ActHiActinst> actList = actHiActinstService.listActHiActinst(instanceIds);
            if (CollectionUtils.isEmpty(actList)) {
                log.info("---------------未查询到待关闭的流程数据,流程实例ids:{}",instanceIds);
            }

            // 获取几天前的时间
            String day = DateUtils.getday5(DateUtils.dateTimeNow(), dayNum);
            // 过滤只保留流程超过指定时间的历史实例集合
            List<ActHiActinst> actHiActinstList = actList.stream().filter(actHiActinst ->  actHiActinst.getStartTime().before(DateUtils.dateTime("yyyy-MM-dd HH:mm:ss", day))).collect(Collectors.toList());
            for (ActHiActinst actHiActinst : actHiActinstList) {
                // 关闭流程
                taskService.complete(actHiActinst.getTaskId(), null);
            }

            List<String> procInstIdList = actHiActinstList.stream().map(ActHiActinst::getProcInstId).collect(Collectors.toList());
            for (EventSheet eventSheet : eventSheets) {
                if (!procInstIdList.contains(eventSheet.getInstanceId())) {
                    break;
                }
                EventSheet event = new EventSheet();
                event.setId(eventSheet.getId());
                event.setEventStatus(EventStatusEnum.CLOSED.getCode());// 已关闭
                event.setUpdateBy("定时任务自动关闭");
                event.setUpdateTime(DateUtils.getNowDate());
                eventSheetService.updateEvent(event);
            }
        } catch(Exception e) {
            log.info("---------------事件单自动关闭异常!");
        }
        log.info("---------------事件单自动关闭结束");
    }
}