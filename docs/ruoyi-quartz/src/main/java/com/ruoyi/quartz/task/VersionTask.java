package com.ruoyi.quartz.task;

import com.ruoyi.activiti.service.IVmBizInfoService;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 版本发布定时任务将符合状态的数据变为作废
 *
 * @author 14735
 */
@Component("versionTask")
public class VersionTask {

    @Autowired
    private IVmBizInfoService vmBizInfoService;

    @Autowired
    private TaskLockManager taskLockManager;

    private static final Logger log = LoggerFactory.getLogger(VersionTask.class);

    public void updateVersionStatus() {

        if (taskLockManager.lock("vmBizInfoTask")) {
            long start = System.currentTimeMillis();
            try {
                log.debug("############版本发布单定时任务执行开始#############");
                vmBizInfoService.cancelVmBizInfoByVersionStatus();
                log.debug("############版本发布单定时任务执行结束#############");
            } finally {
                taskLockManager.unlock("vmBizInfoTask");
            }
            long end = System.currentTimeMillis();
            log.debug("--------定时任务【vmBizInfoTask】执行总时长【"+(end-start)+"】毫秒");
        } else {
            log.debug("vmBizInfoTask - 任务已有其他服务执行...");
        }
    }
}
