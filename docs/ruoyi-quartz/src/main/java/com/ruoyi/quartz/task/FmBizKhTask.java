package com.ruoyi.quartz.task;

import com.ruoyi.activiti.service.IFmBizOvertimeService;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component("fmBizKhTask")
public class FmBizKhTask {
    private static final Logger logger = LoggerFactory.getLogger(FmBizKhTask.class);
    @Autowired
    private IFmBizOvertimeService fmBizOvertimeService;
    @Autowired
    private TaskLockManager taskLockManager;

    //业务事件单考核数据统计定时任务
    public void getFmBizOvertime(String monthStr) {
        if (taskLockManager.lock("fmBizKhTask")) {
            long start = System.currentTimeMillis();
            try {
                logger.debug("############业务事件单考核定时任务执行开始#############");
                if(StringUtils.isEmpty(monthStr)){
                    //获取上一个月的年月
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.MONTH, -1);
                    monthStr = DateUtils.getDateStr(c.getTime(), "yyyyMM");
                }
                String dataExist = fmBizOvertimeService.existData(monthStr);
                if ("1".equals(dataExist)) {
                    //存在上月考核数据
                    logger.debug("上月考核数据已经存在！");
                } else {
                    fmBizOvertimeService.createData(monthStr);//生成考核数据
                    //fmBizOvertimeService.getFmBizOvertimeDates(monthStr);//生成考核数据
                    //fmBizOvertimeService.problemCount(monthStr);//生成问题单汇总表
                    logger.debug("############业务事件单考核定时任务执行結束#############");
                }
            } catch (Exception e) {
                logger.debug("FmBizOvertimeJob任务生成上月考核数据失败");
            } finally {
                taskLockManager.unlock("fmBizKhTask");
            }
            long end = System.currentTimeMillis();
            logger.debug("--------定时任务【fmBizKhTask】执行总时长【"+(end-start)+"】毫秒");
        } else {
            logger.debug("fmBizKhTask - 任务已有其他服务执行...");
        }
    }
}
