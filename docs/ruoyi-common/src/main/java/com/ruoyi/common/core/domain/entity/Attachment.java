package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 *
 * 附件
 * @author 14735
 */
public class Attachment extends BaseEntity {

    /**
     * @Fields 文件获取成功
     */
    public final static String FILE_STATUS_OK = "0";
    /**
     * @Fields 文件未获取
     */
    public final static String FILE_STATUS_UNLOAD = "1";
    /**
     * @Fields 文件获取中
     */
    public final static String FILE_STATUS_LOADING = "2";
    /**
     * @Fields 文件获取失败
     */
    public final static String FILE_STATUS_FAILD = "9";

    /**ID*/
    private String atId;
    /**创建人*/
    private String createId;
    /**所属*/
    private String ownerId;
    /**备注*/
    private String memo;
    /**文件路径*/
    private String filePath;
    /**文件名*/
    private String fileName;
    /**上传时间*/
    private String fileTime;
    private String flag;
    /**文件大小*/
    private String size;
    /**文件状态*/
    private String fileStatus;
    /**密文描述*/
    private String fileCiphertext;
    /**重试次数*/
    private int fileRetryCount;
    /**附件类型*/
    private String fileType;
    /**文件上传fast服务器分配的组名称*/
    private String groupName;

    private String fileGroup;

    /**文件md5码*/
    private String md5;

    /**自动化接口校验变更原因说明*/
    private String changeReason;
    /**发动自动化状态*/
    private String automateStatus;
    /**自动化返回结果信息*/
    private String automateResultMsg;
    /**自动化返回系统名称*/
    private String sysName;

    /**附件关联到人*/
    private OgPerson person;

    public String getAtId() {
        return atId;
    }

    public void setAtId(String atId) {
        this.atId = atId;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getFileCiphertext() {
        return fileCiphertext;
    }

    public void setFileCiphertext(String fileCiphertext) {
        this.fileCiphertext = fileCiphertext;
    }

    public int getFileRetryCount() {
        return fileRetryCount;
    }

    public void setFileRetryCount(int fileRetryCount) {
        this.fileRetryCount = fileRetryCount;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public OgPerson getPerson() {
        return person;
    }

    public void setPerson(OgPerson person) {
        this.person = person;
    }

    public String getFileGroup() {
        return fileGroup;
    }

    public void setFileGroup(String fileGroup) {
        this.fileGroup = fileGroup;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public String getAutomateStatus() {
        return automateStatus;
    }

    public void setAutomateStatus(String automateStatus) {
        this.automateStatus = automateStatus;
    }

    public String getAutomateResultMsg() {
        return automateResultMsg;
    }

    public void setAutomateResultMsg(String automateResultMsg) {
        this.automateResultMsg = automateResultMsg;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    /**
     * 增加失败次数
     */
    public void addFaildRetryCount() {
        if (this.fileRetryCount == 0) {
            this.fileRetryCount = 0;
        }
        this.fileRetryCount++;
        if (this.fileRetryCount > 2) {
            this.fileStatus = FILE_STATUS_FAILD;
        }else {
            this.fileStatus = FILE_STATUS_UNLOAD;
        }
    }

}
