package com.ruoyi.quartz.task;

import com.ruoyi.activiti.domain.AmBizNotice;
import com.ruoyi.activiti.service.IAmBizNoticeService;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component("amBizNoticeTask")
public class AmBizNoticeTask {
    private static final Logger logger = LoggerFactory.getLogger(AmBizNoticeTask.class);

    @Autowired
    private TaskLockManager taskLockManager;

    @Autowired
    private IAmBizNoticeService amBizNoticeService;

    //公告通知每天0点取消置顶
    public void setNoticeIsTop() {
        if (taskLockManager.lock("setNoticeIsTop")) {
            long start = System.currentTimeMillis();
            try{
                AmBizNotice amBizNotice = new AmBizNotice();
                amBizNotice.setIsTop("1");
                List<AmBizNotice> list = amBizNoticeService.selectAllNoticeList(amBizNotice);
                if (list != null && list.size() > 0) {
                    AmBizNotice notice = new AmBizNotice();
                    long nowTime = Long.parseLong(handleTimeYYYYMMDDHHMMSS(DateUtils.getTime()));
                    for (AmBizNotice amBizNotice1 : list) {
                        long topTime = Long.parseLong(amBizNotice1.getTopTime());
                        if (topTime < nowTime) {
                            notice.setAmBizId(amBizNotice1.getAmBizId());
                            notice.setIsTop("0");// 不置顶
                            amBizNoticeService.updateAmBizNotice(notice);
                        }
                    }
                }
                logger.debug("--------定时任务【setNoticeIsTop】----------" + CacheUtils.get("setNoticeIsTop"));
            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                taskLockManager.unlock("setNoticeIsTop");
            }
            long end = System.currentTimeMillis();
            logger.debug("--------定时任务【setNoticeIsTop】执行总时长【"+(end-start)+"】毫秒");
        } else {
            logger.debug("setNoticeIsTop - 任务已有其他服务执行...");
        }
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss类型的时间格式化为yyyyMMddHHmmss
     *
     * @param dateStr
     * @return
     */
    public String handleTimeYYYYMMDDHHMMSS(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return dateStr;
        }
        Date date = DateUtils.parseDate(dateStr);
        String formatDateStr = DateUtils.parseDateToStr(DateUtils.YYYYMMDDHHMMSS, date);
        return formatDateStr;
    }
}
