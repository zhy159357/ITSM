package com.ruoyi.quartz.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.form.service.impl.ShSysSyncServiceImpl;

/**
 * 
 *
 * @author 14735
 */
@Component
public class SysSyncAdpmShTask {

    @Value("${foreign.adpm.syncDate}")
    private String valueDate;
    /**admp*/
    @Value("${foreign.adpm.sysUrl}")
    private String adpmSysUrl;
    
    @Autowired
    private TaskLockManager taskLockManager;

    @Autowired
    private ShSysSyncServiceImpl webServerForADPM;

    private static final Logger log = LoggerFactory.getLogger(SysSyncAdpmShTask.class);

    private final String taskName = "sysSyncAdpmTask";

    public void sysSyncAdpmTask() {
        if (taskLockManager.lock(taskName)) {
            long start = System.currentTimeMillis();
            try {
                log.debug("------------sysSyncSh定时任务执行开始-----------------");
                webServerForADPM.sysSyncSh(adpmSysUrl, valueDate);
                log.debug("------------sysSyncSh定时任务执行结束-----------------");
            } finally {
                taskLockManager.unlock(taskName);
            }
            long end = System.currentTimeMillis();
            log.debug("--------定时任务【sysSyncSh】执行总时长【"+(end-start)+"】毫秒");
        } else {
            log.debug(taskName + " - 任务已有其他服务执行...");
        }
    }
}
