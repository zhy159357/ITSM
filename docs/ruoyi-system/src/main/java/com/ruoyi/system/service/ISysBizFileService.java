package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysBizFile;
import org.apache.commons.fileupload.FileUploadBase;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ISysBizFileService {
    List<SysBizFile> list(SysBizFile file);
    SysBizFile get(Long id);
    Integer deleteByBizId(String bizId);
    Integer create(SysBizFile file);
    Integer update(SysBizFile file);
    Integer delete(Long[] ids);

    SysBizFile upload(MultipartFile file, String ticketId, String currentUser) throws IOException, FileUploadBase.FileSizeLimitExceededException;
}
