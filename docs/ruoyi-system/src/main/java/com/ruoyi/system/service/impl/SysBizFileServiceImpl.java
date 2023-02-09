package com.ruoyi.system.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.common.config.BizFileConfig;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.SysBizFile;
import com.ruoyi.system.mapper.SysBizFileMapper;
import com.ruoyi.system.service.ISysBizFileService;

import cn.hutool.core.text.StrFormatter;


@Service
public class SysBizFileServiceImpl implements ISysBizFileService {
	@Value("${itsm.nginx}")
	private String nginxUrl;
    @Autowired
    private SysBizFileMapper sysBizFileMapper;
    @Autowired
    private BizFileConfig fileConfig;

    @Override
    public List<SysBizFile> list(SysBizFile file) {
        return this.sysBizFileMapper.list(file);
    }

    @Override
    public SysBizFile get(Long id) {
        return this.sysBizFileMapper.get(id);
    }

    @Override
    public Integer deleteByBizId(String bizId) {
        return sysBizFileMapper.deleteByBizId(bizId);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Integer create(SysBizFile file) {
        file.setCreateTime(new Date());
        file.setStatus(1);
        return this.sysBizFileMapper.insert(file);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Integer update(SysBizFile file) {
        file.setUpdateTime(new Date());
        file.setUpdateBy(ShiroUtils.getUserId());
        return this.sysBizFileMapper.update(file);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Integer delete(Long[] ids) {
        return this.sysBizFileMapper.delete(ids);
    }

    @Override
    public SysBizFile upload(MultipartFile file, String ticketId, String currentUser) throws IOException, FileUploadBase.FileSizeLimitExceededException {
        int fileNameLength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNameLength > 255) {
            throw new RuntimeException(StrFormatter.format("文件名长度过长: {}", 255));
        }
        this.checkFile(file);
        String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        String fileName = StrFormatter.format("{}/{}", ticketId, newFileName);  // 以ticketId作为目录
        String absPath = getAbsoluteFile(this.fileConfig.getPath(), fileName).getAbsolutePath();
        String host=this.fileConfig.getHost();
//        if(!"".equals(nginxUrl)) {
//        	host=nginxUrl;
//        }
        String url = host + this.fileConfig.getPrefix() + "/" + fileName;
        file.transferTo(Paths.get(absPath));
        SysBizFile f = new SysBizFile();
        f.setTicketId(ticketId);
        f.setFileName(newFileName);
        f.setOriginFileName(file.getOriginalFilename());
        f.setLocation(absPath);
        f.setUrl(url);
        f.setContentType(file.getContentType());
        f.setLength(file.getSize());
        f.setCreateTime(new Date());
        f.setCreateBy(currentUser);
        this.create(f);
        return f;
    }

    private void checkFile(MultipartFile file) throws FileUploadBase.FileSizeLimitExceededException {
        long size = file.getSize();
        if (size > this.fileConfig.getMaxFileSize()) {
            throw new FileUploadBase.FileSizeLimitExceededException("超出最大文件大小", size / 1024 / 1024, this.fileConfig.getMaxFileSize() / 1024 / 1024);
        }
        /*String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        boolean allowedExtension = false;
        for (String ext : this.fileConfig.getFileTypes()) {
            if (ext.equalsIgnoreCase(extension)) {
                allowedExtension = true;
                break;
            }
        }
        if (!allowedExtension) {
            throw new RuntimeException(StrFormatter.format("文件类型不支持: {}", extension));
        }*/
    }

    private File getAbsoluteFile(String uploadDir, String fileName) throws IOException
    {
        File desc = new File(uploadDir + File.separator + fileName);
        if (!desc.exists())
        {
            if (!desc.getParentFile().exists())
            {
                desc.getParentFile().mkdirs();
            }
        }
        return desc;
    }
}
