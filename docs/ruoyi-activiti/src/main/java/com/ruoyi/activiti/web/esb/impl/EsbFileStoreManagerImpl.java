package com.ruoyi.activiti.web.esb.impl;

import com.ruoyi.activiti.web.esb.EsbHttpFileService;
import com.ruoyi.activiti.web.esb.entity.FileStore;
import com.ruoyi.common.core.BusException;
import com.ruoyi.common.core.Condition;
import com.ruoyi.common.core.Order;
import com.ruoyi.common.netty.utils.Pager;
import com.ruoyi.common.netty.utils.PagerRecords;
import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.tree.TreeNode;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

@Service
public class
EsbFileStoreManagerImpl implements FileStoreManager {
    private Log logger;

    @Autowired
    private EsbHttpFileService esbHttpFileService;

    @Autowired
    private FileUploadConfig fileUploadConfig;

    @Value("${esb.file.upload.maxMB}")
    private int uploadMaxMb;

    public EsbFileStoreManagerImpl() {
        this.logger = LogFactory.getLog(EsbFileStoreManagerImpl.class);
    }

    @Override
    public FileStore storeFile(String fileName, InputStream in, String userId, String fileGroup, String fileType) {

        if (this.uploadMaxMb > 0)
            try {

                int maxLength = this.uploadMaxMb * 1024 * 1024;

                int fileLength = in.available();

                if (fileLength > maxLength)
                    throw new BusException("文件大小超出限制，最大允许上传文件大小:" + this.uploadMaxMb + "MB!");
            } catch (Exception e) {
            }
        String filePath = null;
        try {

            File file = new File(this.fileUploadConfig.getRandomTmpPath("webuptmp"), fileName);
            try {

                FileUtils.copyInputStreamToFile(in, file);
            } catch (IOException e) {

                throw new BusException("文件写入异常");
            }


            filePath = this.esbHttpFileService.uploadFile(fileName, file, userId, fileGroup, fileType);
        } catch (IOException e) {

            this.logger.error("ESB上传文件失败..." + e.getMessage());

            e.printStackTrace();

            throw new BusException("ESB上传文件失败..." + e.getMessage());
        }

        if (StringUtils.isEmpty(filePath)) {

            throw new BusException("ESB上传文件失败...");
        }

        return new FileStore(filePath);
    }

    @Override
    public FileStore storeFile(String fileName, InputStream in, String userId, String fileType) {

        return storeFile(fileName, in, userId, "file", fileType);
    }

    @Override
    public File getFile(String refPath) {
        try {

            return this.esbHttpFileService.downloadFile(refPath, "0");
        } catch (Exception e) {

            e.printStackTrace();

            throw new BusException("ESB下载文件失败..." + e.getMessage());
        }
    }

    @Override
    public File getAbsoluteFile(String filePath) {
        try {

            return this.esbHttpFileService.downloadFile(filePath, "1");
        } catch (Exception e) {

            e.printStackTrace();

            throw new BusException("ESB下载文件失败..." + e.getMessage());
        }
    }

    @Override
    public void delFile(String refPath) {

        this.esbHttpFileService.delFile(refPath);
    }

    @Override
    public FileStore getFileStoreByPath(String refPath) {

        return this.esbHttpFileService.getFileStoreByPath(refPath);
    }

    @Override
    public void updateFileStore(FileStore fileStore) {

        this.esbHttpFileService.updateFileStore(fileStore);
    }

    @Override
    public void markedUsed(String refPath) {

        throw new BusException("非Web实现类，调用失败");
    }

    @Override
    public PagerRecords getPagerFileStoresByUser(Pager pager, Collection<Condition> conditions, Collection<Order> orders)
    {
        throw new BusException("非Web实现类，调用失败");
    }

    @Override
    public List<TreeNode> getGroupTreesByUser(String userId) {

        throw new BusException("非Web实现类，调用失败");
    }
}
