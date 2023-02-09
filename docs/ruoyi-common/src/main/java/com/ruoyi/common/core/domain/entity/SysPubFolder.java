package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 信息管理制度 system_info
 */
public class SysPubFolder extends BaseEntity {
    private static final long serialVersionUID = 1L;
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Excel(name = "ID")
    private String id_;
    @Excel(name = "名称")
    private String name_;
    @Excel(name = "路径")
    private String path_;
    @Excel(name = "父节点")
    private String parent_;
    @Excel(name = "描述")
    private String description_;
    @Excel(name = "创建人")
    private String orgs_;
    @Excel(name = "创建人")
    private String create_user_;
    @Excel(name = "修改人")
    private String manager_user_;
    @Excel(name = "创建时间")
    private String create_time_;
    @Excel(name = "修改人")
    private String modify_user_;
    @Excel(name = "修改时间")
    private String modify_time_;


    public String getId_() {
        return id_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }

    public String getName_() {
        return name_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public String getPath_() {
        return path_;
    }

    public void setPath_(String path_) {
        this.path_ = path_;
    }

    public String getParent_() {
        return parent_;
    }

    public void setParent_(String parent_) {
        this.parent_ = parent_;
    }

    public String getDescription_() {
        return description_;
    }

    public void setDescription_(String description_) {
        this.description_ = description_;
    }

    public String getOrgs_() {
        return orgs_;
    }

    public void setOrgs_(String orgs_) {
        this.orgs_ = orgs_;
    }

    public String getCreate_user_() {
        return create_user_;
    }

    public void setCreate_user_(String create_user_) {
        this.create_user_ = create_user_;
    }

    public String getManager_user_() {
        return manager_user_;
    }

    public void setManager_user_(String manager_user_) {
        this.manager_user_ = manager_user_;
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

    public String getUpdate_time_() {
        return update_time_;
    }

    public void setUpdate_time_(String update_time_) {
        this.update_time_ = update_time_;
    }

    public String getLeaf_() {
        return leaf_;
    }

    public void setLeaf_(String leaf_) {
        this.leaf_ = leaf_;
    }

    @Excel(name = "更新时间")
    private String update_time_;
    @Excel(name = "叶子节点")
    private String leaf_;


}
