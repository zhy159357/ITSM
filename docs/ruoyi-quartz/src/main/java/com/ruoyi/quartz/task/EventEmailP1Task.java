package com.ruoyi.quartz.task;

import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.IpUtils;
import com.ruoyi.form.service.impl.EmailMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component("eventEmailTask")
public class EventEmailP1Task {

    @Autowired
    private EmailMessageService emailMessageService;

    private static final Logger log = LoggerFactory.getLogger(EventEmailP1Task.class);

    private final String EMAIL_SERVICE_IP_KEY = "email_service_ip";

    public void sendEmailMessage() {
        List<PubParaValue> list = DictUtils.getParaValueCache(EMAIL_SERVICE_IP_KEY);
        if (!CollectionUtils.isEmpty(list)) {
            String emailServiceIp = list.get(0).getValue();
            if (IpUtils.getHostIp().equals(emailServiceIp)) {
                long start = System.currentTimeMillis();
                try {
                    log.info("------------eventEmailTask定时任务执行开始-----------------");
                    emailMessageService.sendEmailMessageTimeTask();
                    log.debug("------------eventEmailTask定时任务执行结束-----------------");
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("------------eventEmailTask定时任务执行错误-----------------");
                }
                long end = System.currentTimeMillis();
                log.info("--------定时任务【eventEmailTask】执行总时长【" + (end - start) + "】毫秒");
            } else {
                log.info("------读取本机IP和邮件设置服务器IP不符合，不执行邮件发送，邮件服务器配置IP:{}，本机IP:{}----", emailServiceIp, IpUtils.getHostIp());
            }
        } else {
            log.info("------读取邮件服务器配置IP信息为空，不执行定时任务----");
        }
    }
}
