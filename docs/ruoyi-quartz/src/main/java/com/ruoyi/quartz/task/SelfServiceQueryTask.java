package com.ruoyi.quartz.task;

import com.ruoyi.activiti.service.ISelfServiceQueryService;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.core.domain.entity.SelfServiceQuery;
import com.ruoyi.common.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("selfServiceQueryTask")
public class SelfServiceQueryTask {

    @Autowired
    private TaskLockManager taskLockManager;
    @Autowired
    private ISelfServiceQueryService iSelfServiceQueryService;

    private static final Logger log = LoggerFactory.getLogger(SelfServiceQueryTask.class);

    /**
     * 自动关闭查询申请单
     */
    public void closeTask() {
        if (taskLockManager.lock("selfServiceQueryTask")) {
            try {
                log.info("自动关闭查询申请单任务开始");
                List<SelfServiceQuery> list = iSelfServiceQueryService.selectSelfServiceQueryCloseList(DateUtils.dateTimeNow());//拿到超过规定时间的单子进行关闭
                for (SelfServiceQuery selfServiceQuery : list) {
                    iSelfServiceQueryService.completeSelf(selfServiceQuery);
                }
                log.info("自动关闭查询申请单任务结束");
            } finally {
                taskLockManager.unlock("selfServiceQueryTask");
            }
        } else {
            log.debug("selfServiceQueryTask - 任务已有其他服务执行...");
        }
    }
}
