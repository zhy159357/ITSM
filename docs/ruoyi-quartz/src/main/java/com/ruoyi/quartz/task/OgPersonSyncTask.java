package com.ruoyi.quartz.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.system.service.IOgPersonService;

@Component("ogPersonSyncTask")
public class OgPersonSyncTask {

    private static final Logger logger = LoggerFactory.getLogger(OgPersonSyncTask.class);

    @Autowired
    private TaskLockManager taskLockManager;

    private final String taskName = "ogPersonSyncTask";

    @Autowired
    private IOgPersonService ogPersonService;

    //用户每天0点取消过期代办人
    public void personCleanAgent() {
        if (taskLockManager.lock(taskName)) {
            long start = System.currentTimeMillis();
            try {
            	
            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                taskLockManager.unlock(taskName);
            }
            long end = System.currentTimeMillis();
            logger.debug("--------定时任务【personCleanAgent】执行总时长【"+(end-start)+"】毫秒");
        } else {
            logger.debug("setNoticeIsTop - 任务已有其他服务执行...");
        }
    }
}
