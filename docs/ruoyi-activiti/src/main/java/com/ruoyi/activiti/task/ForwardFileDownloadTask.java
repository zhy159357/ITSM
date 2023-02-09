package com.ruoyi.activiti.task;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.activiti.service.IForwardFileService;
import com.ruoyi.activiti.service.IPubAttachmentService;
import com.ruoyi.activiti.task.thread.ForwardFileDownloadThread;
import com.ruoyi.activiti.web.esb.EsbHttpFileService;
import com.ruoyi.activiti.web.esb.TaskLockManager;
import com.ruoyi.activiti.web.esb.impl.FileUploadConfig;
import com.ruoyi.common.core.domain.entity.Attachment;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.netty.vo.FileUploadResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 跳板机文件下载任务
 *
 * @author zc
 * @date 2021年2月14日
 */
@Component(value="ForwardFileDownloadTask")
@EnableScheduling
public class ForwardFileDownloadTask implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(ForwardFileDownloadTask.class);

    /**
     * @Fields sftp下载跳板机文件线程池
     */
    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 5, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Autowired
    private TaskLockManager taskLockManager;
    @Autowired
    private IPubAttachmentService pubAttachmentService;
    @Autowired
    private IForwardFileService forwardFileService;

    @Value("${forward.host2ip}")
    private String host2ip;

    @Value("${forward.ssh.port:22}")
    private int port;
    @Value("${forward.ssh.username}")
    private String username;
    @Value("${forward.ssh.pemfilepath}")
    private String pemfilepath;
    @Value("${forward.ssh.password}")
    private String password;

    @Value("${shiro.user.isTest}")
    private boolean isTest;

    private File pemfile;

    @Autowired
    private EsbHttpFileService esbHttpFileService;

    private Map<String, String> host2ipMap;

    @Autowired
    private FileUploadConfig fileUploadConfig;

    /**
     * 每5分钟执行下载跳板机文件
     */
    @Scheduled(cron = "0 1/5 * * * ?")
    public void excute() {

        if (taskLockManager.lock("ForwardFileDownloadTask")) {
            try {
                logger.debug("ForwardFileDownloadTask - start");

                downloadFiles();

                logger.debug("ForwardFileDownloadTask - end");
            } finally {
                taskLockManager.unlock("ForwardFileDownloadTask");
            }
        } else {
            logger.debug("ForwardFileDownloadTask - 任务已有其他服务执行...");
        }
    }

    /**
     * 下载文件
     */
    @SuppressWarnings("unchecked")
    private void downloadFiles() {
        try {
            String root = fileUploadConfig.getUploadLocation();
            File downtmp = new File((root + File.separator + "files"), DateUtils.getToday("yyyyMMdd"));
            if (!downtmp.exists()) {
                downtmp.mkdirs();
            }
            if (poolExecutor.getQueue().size() < 50) {
                Page<Attachment> attachmentPage = PageHelper.startPage(0, 50, true);
                Attachment attachment = new Attachment();
                attachment.setFileStatus(Attachment.FILE_STATUS_UNLOAD);
                List<Attachment> attachments = pubAttachmentService.selectAttachmentList(attachment);
                for (Attachment att : attachments) {
                    try {
                        if (StringUtils.isEmpty(att.getFileCiphertext())) {
                            continue;
                        }
                        // 转换时间格式
                        if(StringUtils.isNotEmpty(att.getFileTime())){
                            att.setFileTime(DateUtils.dateTimeNow());
                        }
                        FileUploadResult fileUploadResult = forwardFileService.getFileUploadResult(att.getFileCiphertext());
                        att.setFileStatus(Attachment.FILE_STATUS_LOADING);
                        pubAttachmentService.updateAttachment(att);

                        ForwardFileDownloadThread downloadThread = new ForwardFileDownloadThread();
                        downloadThread.setAttachment(att);
                        downloadThread.setFileUploadResult(fileUploadResult);
                        downloadThread.setHost2ipMap(host2ipMap);
                        downloadThread.setPort(port);
                        downloadThread.setDowntmp(downtmp);
                        downloadThread.setUsername(username);
                        downloadThread.setPassword(password);
                        downloadThread.setTest(isTest);
                        downloadThread.setPemFile(pemfile);
                        downloadThread.setForwardFileDownloadTask(this);
                        poolExecutor.submit(downloadThread);
                    } catch (Exception e) {
                        att.addFaildRetryCount();
                        pubAttachmentService.updateAttachment(att);
                        logger.debug("ForwardFileDownloadTask - 提交任务失败:" + att.getAtId(), e);
                    }
                }

                logger.debug("ForwardFileDownloadTask - 未处理:" + attachmentPage.getTotal() + ",添加至队列:" + attachments.size());
            } else {
                logger.warn("ForwardFileDownloadTask - 队列已满！  -  " + poolExecutor.getQueue().size());
            }
        } catch (Exception e) {
            logger.error("ForwardFileDownloadTask - faild", e);
        }
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isNotEmpty(host2ip)) {
            String[] strArr = Convert.toStrArray(host2ip);
            for (String str : strArr) {
                List<String> strList = Arrays.asList(Convert.toStrArray(":", str));
                map.put(strList.get(0), strList.get(1));
            }
            this.host2ipMap = map;
        }
        pemfile = new File(pemfilepath);
        logger.debug("--------加载forward跳板机主机对应关系:" + host2ipMap);
    }

    public IPubAttachmentService getPubAttachmentService() {
        return pubAttachmentService;
    }

    public EsbHttpFileService getEsbHttpFileService() {
        return esbHttpFileService;
    }

}
