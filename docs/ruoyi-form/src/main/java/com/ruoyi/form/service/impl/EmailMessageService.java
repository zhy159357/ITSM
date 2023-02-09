package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.form.domain.EmailMessage;

import java.util.List;

public interface EmailMessageService extends IService<EmailMessage> {

    void insertEmailMessageBatch(List<EmailMessage> emailMessageList);

    void insertEmailMessage(EmailMessage emailMessage);

    List<EmailMessage> selectEmailMessageList(String sendStatus);

    void updateEmailMessageStatusById(Long id, String status);

    void deleteEmailMessageBySendStatusBatch(String status);

    void sendEmailMessageTimeTask();
}
