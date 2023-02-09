package com.ruoyi.activiti.web.esb.impl;

import com.gosft.tools.core.io.FileUtil;
import com.ruoyi.activiti.web.esb.ICleanTask;
import com.ruoyi.common.utils.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Service

public class FileCleanTask implements ICleanTask

{

    private static final Log logger = LogFactory.getLog(FileCleanTask.class);


    @Autowired

    private FileUploadConfig fileUploadConfig;


    public String getName()

    {

        return "FileUpload缓存文件清理";

    }


    public void run()

    {

        List excludeDates = new ArrayList();

        String today = DateUtils.getToday("yyyyMMdd");

        excludeDates.add(today);

        excludeDates.add(DateUtils.add(today, "yyyyMMdd", 6, -1));

        String root = this.fileUploadConfig.getUploadLocation();

        cleanFile(new File(root + File.separator + "webdowntmp"), excludeDates);

        cleanFile(new File(root + File.separator + "webuptmp"), excludeDates);

    }


    private void cleanFile(File file, List<String> excludeDates)

    {

        if (file.exists()) {

            File[] files = file.listFiles();

            for (File f : files) {

                try {

                    if ((!f.isDirectory()) || (!excludeDates.contains(f.getName()))) {

                        logger.warn("清理缓存文件..." + f.getPath());

                        f.delete();

                    }

                } catch (Exception e) {

                    logger.warn("清理缓存文件失败..." + f.getPath());

                }
            }
        }

    }

}
