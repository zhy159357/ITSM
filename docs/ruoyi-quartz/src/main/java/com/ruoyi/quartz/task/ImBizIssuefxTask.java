package com.ruoyi.quartz.task;

import com.ruoyi.activiti.domain.ImBizIssuefx;
import com.ruoyi.activiti.service.IImBizIssuefxService;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.utils.CacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 问题单关联的事件单个数 刷新 每小时一次
 *
 * @author liudongdong
 */
@Component("imBizIssuefxTask")
public class ImBizIssuefxTask {
    private static final Logger logger = LoggerFactory.getLogger(AmBizNoticeTask.class);

    @Autowired
    private TaskLockManager taskLockManager;

    @Autowired
    private IImBizIssuefxService imBizIssuefxService;

    public void imBizIssuefxRelationCount() {
        if (taskLockManager.lock("imBizIssuefxRelationCount")) {
            long start = System.currentTimeMillis();
            try{
                ImBizIssuefx imBizIssuefx = new ImBizIssuefx();
                List<ImBizIssuefx> list = imBizIssuefxService.selectRelationFmbizList(imBizIssuefx);
                if (list != null && list.size() > 0) {
                    for (ImBizIssuefx imBizIssuefx1 : list) {
                        int count = imBizIssuefxService.selectRelationFmbizCount(imBizIssuefx1.getIssuefxId());
                        imBizIssuefx.setIssuefxId(imBizIssuefx1.getIssuefxId());
                        imBizIssuefx.setRelationFmbizCount(String.valueOf(count));
                        imBizIssuefxService.updateIssue(imBizIssuefx);
                    }
                }
                logger.debug("--------定时任务【imBizIssuefxRelationCount】----------" + CacheUtils.get("imBizIssuefxRelationCount"));
            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                taskLockManager.unlock("imBizIssuefxRelationCount");
            }
            long end = System.currentTimeMillis();
            logger.debug("--------定时任务【imBizIssuefxRelationCount】执行总时长【"+(end-start)+"】毫秒");
        } else {
            logger.debug("imBizIssuefxRelationCount - 任务已有其他服务执行...");
        }
    }
}