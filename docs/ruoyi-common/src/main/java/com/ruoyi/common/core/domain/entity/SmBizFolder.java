package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.io.Serializable;

/**
 * 【请填写功能名称】对象 sm_biz_folder
 * 
 * @author ruoyi
 * @date 2021-01-12
 */
public class SmBizFolder implements Serializable
{
    private static final long serialVersionUID = 1L;


    private String id_;//ID_


    private String name_;//名称


    private String path_;//路径


    private String parent_;//上级


    private String description_;//描述


    private String createUser_;//创建人

    private String createTime_;//创建时间


    private String modifyUser_;//修改人


    private String modifyTime_;//修改时间

    private String updateTime_;//文件最后更新时间


    private String leaf_;//是否叶子节点


    private String type;//2资源计划 1重大计划

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

    public String getCreateUser_() {
        return createUser_;
    }

    public void setCreateUser_(String createUser_) {
        this.createUser_ = createUser_;
    }

    public String getCreateTime_() {
        return createTime_;
    }

    public void setCreateTime_(String createTime_) {
        this.createTime_ = createTime_;
    }

    public String getModifyUser_() {
        return modifyUser_;
    }

    public void setModifyUser_(String modifyUser_) {
        this.modifyUser_ = modifyUser_;
    }

    public String getModifyTime_() {
        return modifyTime_;
    }

    public void setModifyTime_(String modifyTime_) {
        this.modifyTime_ = modifyTime_;
    }

    public String getUpdateTime_() {
        return updateTime_;
    }

    public void setUpdateTime_(String updateTime_) {
        this.updateTime_ = updateTime_;
    }

    public String getLeaf_() {
        return leaf_;
    }

    public void setLeaf_(String leaf_) {
        this.leaf_ = leaf_;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SmBizFolder{" +
                "id_='" + id_ + '\'' +
                ", name_='" + name_ + '\'' +
                ", path_='" + path_ + '\'' +
                ", parent_='" + parent_ + '\'' +
                ", description_='" + description_ + '\'' +
                ", createUser_='" + createUser_ + '\'' +
                ", createTime_='" + createTime_ + '\'' +
                ", modifyUser_='" + modifyUser_ + '\'' +
                ", modifyTime_='" + modifyTime_ + '\'' +
                ", updateTime_='" + updateTime_ + '\'' +
                ", leaf_='" + leaf_ + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
