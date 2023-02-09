package com.ruoyi.quartz.task;

import com.ruoyi.activiti.service.IPubAttachmentService;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.common.core.domain.entity.Attachment;
import com.ruoyi.common.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 自动清除附件类型为版本包的附件，每月执行一次
 *
 * @author 14735
 */
@Component("attachmentCleanTask")
public class AttachmentCleanTask {

    @Autowired
    private TaskLockManager taskLockManager;

    @Autowired
    private IPubAttachmentService attachmentService;

    private static final Logger log = LoggerFactory.getLogger(AttachmentCleanTask.class);

    private final String taskName = "attachmentCleanTask";

    public void cleanAttachment() {
        if (taskLockManager.lock(taskName)) {
            long start = System.currentTimeMillis();
            try {
                log.debug("------------自动清除附件类型为版本包的附件定时任务执行开始-----------------");
                this.deleteAttachmentAfterOneMonth();
                this.deleteAttachmentByVersionCancel();
                log.debug("------------自动清除附件类型为版本包的附件定时任务执行结束-----------------");
            } finally {
                taskLockManager.unlock(taskName);
            }
            long end = System.currentTimeMillis();
            log.debug("--------定时任务【attachmentCleanTask】执行总时长【"+(end-start)+"】毫秒");
        } else {
            log.debug(taskName + " - 任务已有其他服务执行...");
        }
    }

    /**
     * 根据时间删除六个月之前附件类型为版本包的附件
     */
    public void deleteAttachmentAfterOneMonth() {
        Attachment attachment = new Attachment();
        Date date = DateUtils.addMonths(DateUtils.getNowDate(), -6);
        attachment.setFileTime(DateUtils.formatDate(date, DateUtils.YYYYMMDDHHMMSS));
        attachment.setFileType("2");
        List<Attachment> attachments = attachmentService.selectAttachmentList(attachment);
        attachmentService.deleteAttachment(attachments, true);
    }

    /**
     * 删除已经作废版本单关联的附件
     */
    public void deleteAttachmentByVersionCancel() {
        List<Attachment> attachments = attachmentService.selectAttachmentByVersionCancel();
        attachmentService.deleteAttachment(attachments, true);
    }
}
