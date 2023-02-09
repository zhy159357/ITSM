package com.ruoyi.activiti.task;

import com.ruoyi.activiti.service.IOutSystemSmsCodeService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.OutSystemSmsCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 定时任务清除对外提供短信功能out_system_sms_code表数据
 *
 * @author zhangchao
 */
@Component
/*@EnableScheduling*/
public class OutSystemSmsCodeCleanTask {

    @Autowired
    private IOutSystemSmsCodeService outSystemSmsCodeService;

    private static final Logger log = LoggerFactory.getLogger(OutSystemSmsCodeCleanTask.class);

    /**
     *每个月1号7点执行一次
     */
    /*@Scheduled(cron = "0 0 7 1 * ?")*/
    public void clean() {
        // 默认根据创建时间查询六个月之前的数据然后删除
        OutSystemSmsCode outSystemSmsCode = new OutSystemSmsCode();
        Date date = DateUtils.add(new Date(), Calendar.MONTH, -6);
        outSystemSmsCode.setCreateTime(date);
        List<OutSystemSmsCode> outSystemSmsCodes = outSystemSmsCodeService.selectOutSystemSmsCodeList(outSystemSmsCode);
        if (CollectionUtils.isEmpty(outSystemSmsCodes)) {
            return;
        } else {
            List<String> ids = new ArrayList<>();
            for (OutSystemSmsCode out : outSystemSmsCodes) {
                ids.add(out.getOutId());
            }
            int rows = outSystemSmsCodeService.deleteOutSystemSmsCodeByIds((String[]) ids.toArray());
            if (rows > 0) {
                log.debug("------定时任务删除对外短信功能表成功！");
            }
        }
    }
}
