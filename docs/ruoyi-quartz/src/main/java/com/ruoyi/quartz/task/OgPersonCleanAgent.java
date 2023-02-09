package com.ruoyi.quartz.task;

import com.ruoyi.activiti.domain.AmBizNotice;
import com.ruoyi.activiti.service.IAmBizNoticeService;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IOgPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component("ogPersonCleanAgent")
public class OgPersonCleanAgent {

    private static final Logger logger = LoggerFactory.getLogger(AmBizNoticeTask.class);

    @Autowired
    private TaskLockManager taskLockManager;

    @Autowired
    private IAmBizNoticeService amBizNoticeService;

    private final String taskName = "ogPersonCleanAgent";

    @Autowired
    private IOgPersonService ogPersonService;

    //用户每天0点取消过期代办人
    public void personCleanAgent() {
        if (taskLockManager.lock(taskName)) {
            long start = System.currentTimeMillis();
            try {

                OgPerson person = new OgPerson();
                person.setAgencySwitch("1");
                Date date = new Date("yyyy-MM-dd HH:mm:ss");
                List<OgPerson> personList = ogPersonService.selectOgPersonList(person);
                if (!CollectionUtils.isEmpty(personList)) {
                    for (OgPerson ogPerson : personList) {
                        if (StringUtils.isNotEmpty(ogPerson.getAgencyEndTime())) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date endTime = sdf.parse(ogPerson.getAgencyEndTime());
                            if (date.after(endTime)) {
                                OgPerson per = new OgPerson();
                                per.setpId(ogPerson.getpId());
                                per.setUpdatetime(DateUtils.dateTimeNow());
                                per.setAgencySwitch("0");
                                ogPersonService.updateAgencyIsNull(ogPerson);
                            }
                        }
                    }
                }

                logger.debug("--------定时任务【personCleanAgent】----------" + CacheUtils.get("setNoticeIsTop"));
            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                taskLockManager.unlock("personCleanAgent");
            }
            long end = System.currentTimeMillis();
            logger.debug("--------定时任务【personCleanAgent】执行总时长【"+(end-start)+"】毫秒");
        } else {
            logger.debug("setNoticeIsTop - 任务已有其他服务执行...");
        }
    }
}
