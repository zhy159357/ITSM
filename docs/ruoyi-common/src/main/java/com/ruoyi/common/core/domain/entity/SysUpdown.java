package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
/**
 * 上传下载 system_updown
 */
public class SysUpdown extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    private String id_;
    @Excel(name = "机构")
    private String folder_;
    @Excel(name = "机构")
    private String folder;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    @Excel(name = "文件名")
    private String file_name_;
    @Excel(name = "大小")
    private String size_;
    @Excel(name = "描述 ")
    private String description_;
    @Excel(name = "创建人")
    private String create_user_;
    @Excel(name = "上传时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private String create_time_;
    @Excel(name = "修改人")
    private String modify_user_;
    @Excel(name = "修改时间")
    private String modify_time_;
    @Excel(name = "路径")
    private String file_path_;
    @Excel(name = "标记")
    private String invalidation_mark;

    private String folderTest;

    private String createName;

    private OgPerson person;// 附件关联人
    /**文件上传fast服务器分配的组名称*/
    private String groupName;

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getId_() {
        return id_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }

    public String getFolder_() {
        return folder_;
    }

    public void setFolder_(String folder_) {
        this.folder_ = folder_;
    }

    public String getFile_name_() {
        return file_name_;
    }

    public void setFile_name_(String file_name_) {
        this.file_name_ = file_name_;
    }

    public String getSize_() {
        return size_;
    }

    public void setSize_(String size_) {
        this.size_ = size_;
    }

    public String getDescription_() {
        return description_;
    }

    public void setDescription_(String description_) {
        this.description_ = description_;
    }

    public String getCreate_user_() {
        return create_user_;
    }

    public void setCreate_user_(String create_user_) {
        this.create_user_ = create_user_;
    }

    public String getCreate_time_() {
        return create_time_;
    }

    public void setCreate_time_(String create_time_) {
        this.create_time_ = create_time_;
    }

    public String getModify_user_() {
        return modify_user_;
    }

    public void setModify_user_(String modify_user_) {
        this.modify_user_ = modify_user_;
    }

    public String getModify_time_() {
        return modify_time_;
    }

    public void setModify_time_(String modify_time_) {
        this.modify_time_ = modify_time_;
    }

    public String getFile_path_() {
        return file_path_;
    }

    public void setFile_path_(String file_path_) {
        this.file_path_ = file_path_;
    }

    public String getInvalidation_mark() {
        return invalidation_mark;
    }

    public void setInvalidation_mark(String invalidation_mark) {
        this.invalidation_mark = invalidation_mark;
    }

    public OgPerson getPerson() {
        return person;
    }

    public void setPerson(OgPerson person) {
        this.person = person;
    }

    public String getFolderTest() {
        return folderTest;
    }

    public void setFolderTest(String folderTest) {
        this.folderTest = folderTest;
    }
}


