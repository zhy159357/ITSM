package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MailUtils;
import com.ruoyi.form.domain.EmailMessage;
import com.ruoyi.form.mapper.EmailMessageMapper;
import com.ruoyi.form.service.ForeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class EmailMessageServiceImpl extends ServiceImpl<EmailMessageMapper, EmailMessage> implements EmailMessageService {

    @Autowired
    private ForeignService foreignService;

    @Autowired
    private EmailMessageMapper emailMessageMapper;

    @Override
    public void insertEmailMessageBatch(List<EmailMessage> emailMessageList) {
        this.saveBatch(emailMessageList);
    }

    @Override
    public void insertEmailMessage(EmailMessage emailMessage) {
        emailMessageMapper.insert(emailMessage);
    }

    public void updateEmailMessageBatch(List<EmailMessage> emailMessageList) {
        this.updateBatchById(emailMessageList);
    }

    @Override
    public List<EmailMessage> selectEmailMessageList(String sendStatus) {
        return emailMessageMapper.selectList(new QueryWrapper<EmailMessage>().eq("send_status", sendStatus));
    }

    @Override
    public void updateEmailMessageStatusById(Long id, String status) {
        EmailMessage emailMessage = EmailMessage.builder().id(id).sendStatus(status).build();
        emailMessageMapper.updateById(emailMessage);
    }

    @Override
    public void deleteEmailMessageBySendStatusBatch(String status) {
        QueryWrapper<EmailMessage> queryWrapper = new QueryWrapper<EmailMessage>().eq("send_status", status);
        Date date = DateUtils.addDays(DateUtils.getNowDate(), -3);
        queryWrapper.lt("send_time", date);
        emailMessageMapper.delete(queryWrapper);
    }

    @Override
    public void sendEmailMessageTimeTask() {
        List<EmailMessage> messageList = this.selectEmailMessageList(EmailMessage.EMAIL_MESSAGE_STATUS_NO);
        if(!CollectionUtils.isEmpty(messageList)) {
            messageList.forEach(m -> {
                String result = foreignService.sendComplexMail(m.getBizNo(), m.getEmail(), null, m.getTitle(), m.getEmailMessage(), null, null, null);
                if(MailUtils.MESSAGE_SEND_SUCCESS.equals(result)) {
                    m.setSendStatus(EmailMessage.EMAIL_MESSAGE_STATUS_YES);
                } else {
                    m.setSendStatus(EmailMessage.EMAIL_MESSAGE_STATUS_NO);
                }
            });
        }
        this.updateEmailMessageBatch(messageList);
        // 执行删除逻辑  删除三天之前并且状态是正常发送的数据
        this.deleteEmailMessageBySendStatusBatch(EmailMessage.EMAIL_MESSAGE_STATUS_YES);
    }

}
