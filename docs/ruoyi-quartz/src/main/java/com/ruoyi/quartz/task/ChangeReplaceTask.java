package com.ruoyi.quartz.task;

import com.ruoyi.activiti.service.IFmBizOvertimeService;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.domain.ChangeDeptEntity;
import com.ruoyi.form.mapper.ChangeDeptMapper;
import com.ruoyi.form.service.IChangeDeptService;
import com.ruoyi.system.service.IPubParaValueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @program: ruoyi
 * @description: 变更重置日期定时任务
 * @author: ma zy
 * @date: 2022-09-23 14:40
 **/
@Component("changeReplaceTask")
@EnableScheduling
public class ChangeReplaceTask {
    private static final Logger logger = LoggerFactory.getLogger(ChangeReplaceTask.class);
    @Autowired
    private IPubParaValueService iPubParaValueService;
    @Autowired
    private IChangeDeptService iChangeDeptService;

    @Autowired
    private TaskLockManager taskLockManager;
    //@Scheduled(cron = "0/3 * * * * ?")
    public void changeDateTask( ) {
        if (taskLockManager.lock("changeDateTask")) {
            try {
                logger.debug("############重置变更额度日期定时任务执行开始#############");
                List<PubParaValue> pubParaValueList =iPubParaValueService.selectPubParaValueByParaName("change_replace");
                List<ChangeDeptEntity> changeDeptEntityList = iChangeDeptService.list();
                changeDeptEntityList.stream().forEach(p->{
                    Date date = new Date();// 获取当前时间
                    String day = String.format("%te", date);
                    if(day.equals(p.getReplaceTime())){
                        p.setOverSize(pubParaValueList.get(0).getValue());
                    }
                });
                iChangeDeptService.saveOrUpdateBatch(changeDeptEntityList);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                taskLockManager.unlock("changeDateTask");
            }
        }else {
            logger.debug("changeDateTask - 任务已有其他服务执行...");
        }

    }
}
