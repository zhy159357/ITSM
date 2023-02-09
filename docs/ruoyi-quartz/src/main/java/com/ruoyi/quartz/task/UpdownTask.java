package com.ruoyi.quartz.task;

import com.ruoyi.activiti.service.IVmBizInfoService;
import com.ruoyi.system.service.ISysUpdownService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("UpdownTask")
public class UpdownTask {

    @Autowired
    private ISysUpdownService updownService;

    private static final Logger log = LoggerFactory.getLogger(UpdownTask.class);

    public void deleteUpdownFile(){
        log.info("=================================删除附件相关内容=================================");
        updownService.selectUpdownByTime();
        log.info("=================================删除附件相关内容=================================");
    }
}
