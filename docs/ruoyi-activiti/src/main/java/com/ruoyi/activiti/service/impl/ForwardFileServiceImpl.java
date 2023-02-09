package com.ruoyi.activiti.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.activiti.service.IForwardFileService;
import com.ruoyi.activiti.service.IPubAttachmentService;
import com.ruoyi.common.core.domain.entity.ApiResData;
import com.ruoyi.common.core.domain.entity.Attachment;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.fastdfs.FastDFSHelper;
import com.ruoyi.common.netty.utils.DESUtils;
import com.ruoyi.common.netty.vo.FileUploadResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import org.apache.http.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.*;

/**
 * 跳板机文件下载类
 *
 * @author 14735
 */
@Service
public class ForwardFileServiceImpl implements IForwardFileService {

    @Autowired
    private IPubAttachmentService attachmentService;

    @Value("${forward.encryKey}")
    private String encryKey;

    private static final Logger logger = LoggerFactory.getLogger(ForwardFileServiceImpl.class);

    @Override
    public Attachment saveForwardFile(String fileCiphertext, OgPerson create, String ownerId, String fileGroup) {

        Attachment pubAttachment = new Attachment();
        pubAttachment.setOwnerId(ownerId);
        pubAttachment.setCreateId(create.getpId());
        pubAttachment.setFileCiphertext(fileCiphertext);
        pubAttachment.setFileGroup(fileGroup);

        FileUploadResult fileUploadResult = getFileUploadResult(fileCiphertext);

        pubAttachment.setFileTime(DateUtils.dateTimeNow());
        pubAttachment.setFileRetryCount(0);
        pubAttachment.setFileStatus(Attachment.FILE_STATUS_UNLOAD);
        pubAttachment.setFileName(fileUploadResult.getFileName());
        // 新增附件类型
        pubAttachment.setFileType(fileUploadResult.getFileType());
        pubAttachment.setAutomateStatus("1");
        pubAttachment.setAtId(UUID.getUUIDStr());
        attachmentService.insertAttachment(pubAttachment);
        return pubAttachment;
    }

    @Override
    public FileUploadResult getFileUploadResult(String fileCiphertext) {
        FileUploadResult fileUploadResult = null;
        try {
            String fileJson = DESUtils.decrypt(fileCiphertext, encryKey);
            fileUploadResult = JSON.parseObject(fileJson, FileUploadResult.class);
        } catch (Exception e) {
            logger.error("文件信息解密失败", e);
            throw new BusinessException("文件信息解密失败");
        }

        if (StringUtils.isEmpty(fileUploadResult.getHostname()) || StringUtils.isEmpty(fileUploadResult.getFileName())
                || StringUtils.isEmpty(fileUploadResult.getPath())) {

            logger.error("文件信息异常:" + JSON.toJSONString(fileUploadResult));
            throw new BusinessException("文件信息异常");
        }
        return fileUploadResult;
    }

    /**
     * 电话银行运维事件单接口专用
     *
     * @param ownerId  工单ID
     * @param userId  创建人ID
     * @param filePath 文件URL
     * @param resData  返回值
     * @return
     */
    public ApiResData saveFortFile(String ownerId, String userId, String filePath, ApiResData resData) {
        String[] str = filePath.split(",");
        for (int i = 0; i < str.length; i++) {
            String url = str[i];
            url.replaceAll("%20", " ");
            logger.debug("--------95580url--------------" + url);
            logger.debug("--------95580url--------------" + url);
            logger.debug("--------95580url--------------" + url);
            CloseableHttpClient client = HttpClients.createDefault();
            // 目标文件url
            HttpPost httpPost = new HttpPost(url);
            try {
                HttpResponse respone = client.execute(httpPost);
                Header contentHeader = respone.getFirstHeader("Content-Disposition");
                if (contentHeader != null) {
                    HeaderElement[] values = contentHeader.getElements();
                    if (values.length > 0) {
                        for (HeaderElement he : values) {
                            String filename = null;
                            NameValuePair param = he.getParameterByName("filename");
                            if (param != null) {
                                filename = new String(param.getValue().getBytes("iso-8859-1"), "GBK");
                                int a = filename.lastIndexOf(".");
                                System.out.println("文件名是：" + filename);
                            }
                            HttpEntity entity = respone.getEntity();
                            byte[] byteData = EntityUtils.toByteArray(respone.getEntity());//拿到返回结果字节流
                            long length = entity.getContentLength();
                            if (length <= 0) {
                                return resData = new ApiResData("-1","目标附件不存在！");
                            }
                            if (entity != null) {
//                                logger.debug("--------95580------------------附件下载----------");
//                                logger.debug("--------95580------------------附件下载----------");
//                                logger.debug("--------95580------------------附件下载----------");
//                                String path = this.getClass().getResource("/").getPath();
//                                logger.debug("path=" + path);
//                                String filePathsss = path.substring(0, path.indexOf("web")) + "95580/";
//                                logger.debug("filePathsss=" + filePathsss);
//                                logger.debug("--------95580------------------附件下载----------");
//                                logger.debug("--------95580------------------附件下载----------");
//                                logger.debug("--------95580------------------附件下载----------");
//                                FileOutputStream out = new FileOutputStream(filePathsss + filename);
//                                out.write(byteData);
//                                out.close();
                                InputStream is = new ByteArrayInputStream(byteData);
                                long size = is.available();
                                String[] arr = FastDFSHelper.getInstance().uploadInputStream(is, filename, size);
                                if (ObjectUtils.isEmpty(arr)) {
                                    return resData = new ApiResData("-1",
                                            "附件上传失败，请重试！");
                                }
                                Attachment attachment = new Attachment();
                                attachment.setAtId(UUID.getUUIDStr());
                                attachment.setCreateId(userId);
                                attachment.setOwnerId(ownerId);
                                attachment.setSize(Convert.convertSize(size));
                                attachment.setFileTime(DateUtils.dateTimeNow());
                                attachment.setFileName(StringUtils.replaceSpace(filename));
                                attachment.setGroupName(arr[0]);
                                attachment.setFilePath(arr[1]);
                                attachment.setFileType("1");
                                attachment.setAutomateStatus("1");
                                attachmentService.insertAttachment(attachment);
                                resData = new ApiResData("0000", "附件添加成功");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resData;
    }
}
