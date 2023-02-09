package com.ruoyi.activiti.web.esb.impl;

import com.ruoyi.activiti.web.esb.utils.ShortUrlGenerator;
import com.ruoyi.common.utils.DateUtils;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.FileCleaningTracker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class FileUploadConfig implements ApplicationContextAware {
    protected final Log logger;
    private static final String DEFAULT_UPLOAD_LOCATION = "file:file/upload";
    private ApplicationContext applicationContext;

    @Autowired
    private FileCleaningTracker fileCleaningTracker;

    @Value("${upload.location}")
    private String uploadLocation;

    public FileUploadConfig() {
        this.logger = LogFactory.getLog(FileUploadConfig.class);

        this.uploadLocation = "file:file/upload";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public FileItemFactory createFileItemFactory(String subPath) {
        Resource resource = this.applicationContext.getResource(this.uploadLocation);
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();

        fileItemFactory.setFileCleaningTracker(this.fileCleaningTracker);
        try {
            File dir = new File(resource.getFile().getAbsolutePath() + subPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            fileItemFactory.setRepository(dir);
        } catch (IOException e) {
            this.logger.warn("文件上传路径设置不成功，使用默认的路径！");
        }
        return fileItemFactory;
    }

    public String getUploadLocation() {
        Resource resource = this.applicationContext.getResource(this.uploadLocation);
        try {
            return resource.getFile().getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.uploadLocation;
    }

    public File getDateTmpPath(String name) {
        String root = getUploadLocation();
        String subpath = name + File.separator + DateUtils.getToday("yyyyMMdd") + File.separator;
        File path = new File(root + File.separator + subpath);
        if (!path.exists()) {
            path.mkdirs();
        }
        return path;
    }

    public synchronized File getRandomTmpPath(String name) {
        return getRandomTmpPath(getDateTmpPath(name));
    }

    private File getRandomTmpPath(File path) {
        String[] shortUrls = ShortUrlGenerator.shortUrl(path.getPath() + DateUtils.getToday("HHmmss"));
        for (String shortUrl : shortUrls) {
            File tmpsubPath = new File(path, shortUrl);
            if (!tmpsubPath.exists()) {
                tmpsubPath.mkdirs();
                return tmpsubPath;
            }
        }
        try {
            super.wait(100L);
        } catch (InterruptedException e) {
        }
        return getRandomTmpPath(path);
    }

    public void setUploadLocation(String uploadLocation) {
        this.uploadLocation = uploadLocation;
    }
}
