package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.form.domain.CustomerVo;
import com.ruoyi.form.domain.IncidentBatchTemp;
import com.ruoyi.form.service.EventForeignService;
import com.ruoyi.form.service.IncidentBatchService;
import com.ruoyi.form.service.impl.EventForeignServiceImpl;
import com.ruoyi.system.service.IOgUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component("eventBatchTask")
public class EventBatchTask {
    @Autowired
    private IncidentBatchService incidentBatchService;

    @Autowired
    private EventForeignServiceImpl eventForeignService;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private TaskLockManager taskLockManager;

    private static final Logger log = LoggerFactory.getLogger(EventBatchTask.class);

    public void execute() {

        if (taskLockManager.lock("eventBatchTask")) {
            long start = System.currentTimeMillis();
            try {
                log.info("--------事件单批量定时任务执行开始-------------");
                this.executeBatch();

                // 删除状态已成功的数据
                incidentBatchService.deleteIncidentBatchTempBatch(IncidentBatchTemp.INCIDENT_BATCH_SUCCESS);
                log.info("--------事件单批量定时任务执行结束-------------");
            } finally {
                taskLockManager.unlock("eventBatchTask");
            }
            long end = System.currentTimeMillis();
            log.debug("--------定时任务【eventBatchTask】执行总时长【"+(end-start)+"】毫秒");
        } else {
            log.debug("eventBatchTask - 任务已有其他服务执行...");
        }
    }

    private void executeBatch() {
        IncidentBatchTemp batchTemp = new IncidentBatchTemp();
        batchTemp.setExecuteStatus(IncidentBatchTemp.INCIDENT_BATCH_UNEXECUTED);
        List<IncidentBatchTemp> tempList = incidentBatchService.selectIncidentBatchTempList(batchTemp);
        if(!CollectionUtils.isEmpty(tempList)) {
            IncidentBatchTemp t0 = tempList.get(0);
            List<String> collect = tempList.stream().map(temp -> {
                return temp.getEventNo();
            }).collect(Collectors.toList());
            String instanceId = t0.getInstanceId();
            String customerJson = t0.getCustomerJson();
            CustomerVo customerVo = JSON.parseObject(customerJson, new TypeReference<CustomerVo>() {});
            String code = customerVo.getCustomerFormContent().getCode();
            if(StringUtils.isEmpty(code)) {
                code = "incident";
            }
            OgUser ogUser = ogUserService.selectOgUserByUserId(t0.getUserId());
            eventForeignService.batchCloseEventAsync(code, instanceId, t0.getStatusStr(), customerVo, ogUser, collect);
        }
    }
}

