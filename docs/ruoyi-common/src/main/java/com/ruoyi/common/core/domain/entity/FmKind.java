package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;

/**
 * 事件单系统分类对象 fm_kind
 *
 * @author ruoyi
 * @date 2020-12-28
 */
public class FmKind {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private String fmTypeId;

    /**
     * 分类名称
     */
    @Excel(name = "分类名称")
    private String name;

    /**
     * 所属应用系统
     */
    @Excel(name = "所属应用系统")
    private String sysId;

    /**
     * 有效标志
     */
    @Excel(name = "有效标志")
    private String invalidationMark;

    /**
     * 知识库链接地址
     */
    @Excel(name = "知识库链接地址")
    private String knowledgeUrl;

    /**
     * 添加人
     */
    @Excel(name = "添加人")
    private String adderId;

    /**
     * 修改人
     */
    @Excel(name = "修改人")
    private String updaterId;

    /**
     * 添加时间
     */
    @Excel(name = "添加时间")
    private String addTime;

    private String updateTime;
    /**
     * 类别：事件分类
     */
    @Excel(name = "类别：事件分类")
    private String type;
    /**
     * 序号排序使用
     */
    @Excel(name = "序号")
    private String serialNum;
    //父分类ID
    private String parentId;

    /**父分类名称*/
    private String parentName;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFmTypeId() {
        return fmTypeId;
    }

    public void setFmTypeId(String fmTypeId) {
        this.fmTypeId = fmTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getKnowledgeUrl() {
        return knowledgeUrl;
    }

    public void setKnowledgeUrl(String knowledgeUrl) {
        this.knowledgeUrl = knowledgeUrl;
    }

    public String getAdderId() {
        return adderId;
    }

    public void setAdderId(String adderId) {
        this.adderId = adderId;
    }

    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    @Override
    public String toString() {
        return "FmKind{" +
                "fmTypeId='" + fmTypeId + '\'' +
                ", name='" + name + '\'' +
                ", sysId='" + sysId + '\'' +
                ", invalidationMark='" + invalidationMark + '\'' +
                ", knowledgeUrl='" + knowledgeUrl + '\'' +
                ", adderId='" + adderId + '\'' +
                ", updaterId='" + updaterId + '\'' +
                ", addTime='" + addTime + '\'' +
                ", type='" + type + '\'' +
                ", serialNum='" + serialNum + '\'' +
                '}';
    }
}
