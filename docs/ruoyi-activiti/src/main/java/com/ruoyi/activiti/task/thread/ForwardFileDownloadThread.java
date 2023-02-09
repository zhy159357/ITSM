package com.ruoyi.activiti.task.thread;

import com.gsoft.cps.forward.api.util.MD5Utils;
import com.ruoyi.activiti.task.ForwardFileDownloadTask;
import com.ruoyi.common.core.domain.entity.Attachment;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.fastdfs.FastDFSHelper;
import com.ruoyi.common.netty.ssh.SftpUtils;
import com.ruoyi.common.netty.vo.FileUploadResult;
import com.ruoyi.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

/**
 * 跳板机文件下载线程
 *
 * @author zc
 * @date 2021年2月14日
 */
public class ForwardFileDownloadThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ForwardFileDownloadThread.class);

    private ForwardFileDownloadTask forwardFileDownloadTask;

    private Attachment attachment;
    private FileUploadResult fileUploadResult;
    private Map<String, String> host2ipMap;
    private int port;
    private String username;
    private String password;
    private File pemFile;
    private File downtmp;

    private boolean isTest;

    /**
     * @see Thread#run()
     */
    @SuppressWarnings("resource")
    @Override
    public void run() {
        try {

            String hostname = fileUploadResult.getHostname();
            String ip = host2ipMap.get(hostname);

            if (StringUtils.isEmpty(ip)) {
                throw new BusinessException(attachment.getAtId() + " - 跳板机获取ip失败:" + hostname);
            }

            logger.debug(attachment.getAtId() + " - 开始sftp下载文件:" + fileUploadResult.getPath() + " - 应用服务器存放文件目录为:" + downtmp.getPath());
            File file = null;
            try {
                if(isTest){
                    // 测试环境使用密码连接sftp
                    file = SftpUtils.getFile(ip, port, username, password, fileUploadResult.getPath(), downtmp.getPath());
                } else {
                    // 生产环境使用互信文件连接sftp
                    file = SftpUtils.getFile(ip, port, username, pemFile, fileUploadResult.getPath(), downtmp.getPath());
                    logger.debug("---------ip="+ip+",端口:"+port+",用户名:"+username+",互信文件路径:"+pemFile.getPath()+"开始连接跳板机......");
                }
            } catch (Exception e) {
                throw new BusinessException(attachment.getAtId() + " - sftp下载失败:" + fileUploadResult.getPath(), e);
            }

            logger.debug(attachment.getAtId() + " - 开始校验md5:" + fileUploadResult.getPath());
            String md5 = null;
            try {
                md5 = MD5Utils.md5(file);
                if (!md5.equals(fileUploadResult.getMd5())) {
                    throw new BusinessException(" - md5校验失败:" + md5 + " - " + fileUploadResult.getMd5());
                }
                // 将md5码设置到附件表
                attachment.setMd5(md5);
            } catch (Exception e) {
                throw new BusinessException(attachment.getAtId() + " - sftp MD5校验失败:" + e.getMessage(), e);
            }

            logger.debug(attachment.getAtId() + " - 开始上传fastdfs文件服务器:" + file.getPath());
            String[] arr = null;
            try {
                arr = FastDFSHelper.getInstance().uploadFile(file.getPath(), file.getName(), md5);
                if(StringUtils.isEmpty(arr))
                    throw new BusinessException(attachment.getAtId() + " - 上传fastdfs文件服务器失败:" + file.getPath());
            } catch (Exception e) {
                throw new BusinessException(attachment.getAtId() + " - 上传fastdfs文件服务器失败:" + file.getPath(), e);
            }

            attachment.setGroupName(arr[0]);
            attachment.setFilePath(arr[1]);
            attachment.setFileStatus(Attachment.FILE_STATUS_OK);

            String size = "";
            try {
                long fileS = new FileInputStream(file).available();
                size = Convert.convertSize(fileS);
            } catch (Exception e) {
                logger.error(attachment.getAtId() + " - 文件大小获取失败", e);
            }

            attachment.setSize(size);
            forwardFileDownloadTask.getPubAttachmentService().updateAttachment(attachment);
            logger.debug(attachment.getAtId() + " - 处理完成:" + fileUploadResult.getPath());
        } catch (Exception e) {
            logger.error(attachment.getAtId() + " - 跳板机下载文件失败", e);
            attachment.addFaildRetryCount();
            forwardFileDownloadTask.getPubAttachmentService().updateAttachment(attachment);
        }
    }

    /**
     * @return the attachment
     */
    public Attachment getAttachment() {
        return attachment;
    }

    /**
     * @param attachment the attachment to set
     */
    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    /**
     * @return the fileUploadResult
     */
    public FileUploadResult getFileUploadResult() {
        return fileUploadResult;
    }

    /**
     * @param fileUploadResult the fileUploadResult to set
     */
    public void setFileUploadResult(FileUploadResult fileUploadResult) {
        this.fileUploadResult = fileUploadResult;
    }

    /**
     * @return the host2ipMap
     */
    public Map<String, String> getHost2ipMap() {
        return host2ipMap;
    }

    /**
     * @param host2ipMap the host2ipMap to set
     */
    public void setHost2ipMap(Map<String, String> host2ipMap) {
        this.host2ipMap = host2ipMap;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the downtmp
     */
    public File getDowntmp() {
        return downtmp;
    }

    /**
     * @param downtmp the downtmp to set
     */
    public void setDowntmp(File downtmp) {
        this.downtmp = downtmp;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }


    public File getPemFile() {
        return pemFile;
    }

    public void setPemFile(File pemFile) {
        this.pemFile = pemFile;
    }

    /**
     * @return the forwardFileDownloadTask
     */
    public ForwardFileDownloadTask getForwardFileDownloadTask() {
        return forwardFileDownloadTask;
    }

    /**
     * @param forwardFileDownloadTask the forwardFileDownloadTask to set
     */
    public void setForwardFileDownloadTask(ForwardFileDownloadTask forwardFileDownloadTask) {
        this.forwardFileDownloadTask = forwardFileDownloadTask;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }
}
