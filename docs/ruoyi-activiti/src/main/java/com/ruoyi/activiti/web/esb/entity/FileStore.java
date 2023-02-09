package com.ruoyi.activiti.web.esb.entity;


import com.ruoyi.common.core.Domain;

public class FileStore
        implements Domain
{
    private static final long serialVersionUID = -8275829299008837305L;

    private String fileStoreId;

    private String filePath;

    private String uploadFileName;

    private String status;

    private String timestamp;

    private Integer fileSize;

    private String userId;

    private String fileType;

    private String fileGroup;

    public FileStore()
    {
    }

    public FileStore(String filePath)
    {
        this.filePath = filePath;
    }

    public String getFileStoreId() {
        return this.fileStoreId;
    }

    public void setFileStoreId(String fileStoreId) {
        this.fileStoreId = fileStoreId;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUploadFileName() {
        return this.uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileGroup() {
        return this.fileGroup;
    }

    public void setFileGroup(String fileGroup) {
        this.fileGroup = fileGroup;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("uploadFileName:" + this.uploadFileName);
        builder.append(",filePath:" + this.filePath);
        builder.append(",fileGroup:" + this.fileGroup);
        builder.append(",userId:" + this.userId);
        builder.append("}");
        return builder.toString();
    }
}