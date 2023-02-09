package com.ruoyi.activiti.task;

import com.ruoyi.activiti.service.IAutomateMiddleService;
import com.ruoyi.activiti.service.IPubAttachmentService;
import com.ruoyi.activiti.service.IVmBizInfoService;
import com.ruoyi.activiti.task.thread.ForwardFileCheckAndStart;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.core.domain.entity.AutomateMiddle;
import com.ruoyi.system.service.IOgPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 定时任务校验软研接口发送过来的自动化附件
 *
 * @author zhangchao
 */
@Component
@EnableScheduling
public class ForwardFileCheckTask {

    private static final Logger logger = LoggerFactory.getLogger(ForwardFileCheckTask.class);

    @Autowired
    private TaskLockManager taskLockManager;

    @Autowired
    private IAutomateMiddleService automateMiddleService;

    @Autowired
    private IVmBizInfoService vmBizInfoService;

    @Autowired
    private IPubAttachmentService attachmentService;

    @Autowired
    private IOgPersonService ogPersonService;

    /**
     * 每5分钟执行校验
     */
    @Scheduled(cron = "0 1/5 * * * ?")
    public void excute() {

        if (taskLockManager.lock("automateFileCheckTask")) {
            try {
                logger.debug("automateFileCheckTask - start");

                //automateCheck();

                logger.debug("automateFileCheckTask - end");
            } finally {
                taskLockManager.unlock("automateFileCheckTask");
            }
        } else {
            logger.debug("automateFileCheckTask - 任务已有其他服务执行...");
        }
    }

    /**
     * 定时任务自动化校验，校验通过启动流程
     */
    public void automateCheck() {
        AutomateMiddle middle = new AutomateMiddle();
        middle.setStatus("0");
        List<AutomateMiddle> automateMiddles = automateMiddleService.selectAutomateMiddleList(middle);
        if(!CollectionUtils.isEmpty(automateMiddles)){
            for (AutomateMiddle mm : automateMiddles) {
                try {
                    ForwardFileCheckAndStart checkAndStart = new ForwardFileCheckAndStart();
                    checkAndStart.setAutomateMiddle(mm);
                    checkAndStart.setForwardFileCheckTask(this);
                    checkAndStart.checkAndStart();
                } catch (Exception e){
                    e.printStackTrace();
                    mm.setStatus(AutomateMiddle.AUTO_STATUS_FAIL);
                    automateMiddleService.updateAutomateMiddle(mm);
                    logger.debug("----------定时任务发送自动化校验步骤或启动流程失败，失败原因:" + e.getMessage());
                }
            }
        } else {
            logger.debug("-----------未查询到需要执行自动化校验的 版本单数据！");
        }
    }

    public IAutomateMiddleService getAutomateMiddleService() {
        return automateMiddleService;
    }

    public void setAutomateMiddleService(IAutomateMiddleService automateMiddleService) {
        this.automateMiddleService = automateMiddleService;
    }

    public IVmBizInfoService getVmBizInfoService() {
        return vmBizInfoService;
    }

    public void setVmBizInfoService(IVmBizInfoService vmBizInfoService) {
        this.vmBizInfoService = vmBizInfoService;
    }

    public IPubAttachmentService getAttachmentService() {
        return attachmentService;
    }

    public void setAttachmentService(IPubAttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    public IOgPersonService getOgPersonService() {
        return ogPersonService;
    }

    public void setOgPersonService(IOgPersonService ogPersonService) {
        this.ogPersonService = ogPersonService;
    }
}
