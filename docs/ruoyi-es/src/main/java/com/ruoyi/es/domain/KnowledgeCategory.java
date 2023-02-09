package com.ruoyi.es.domain;

import java.io.Serializable;

/**
 * 参数列别对象 knowledge_category
 * @author dayong_sun
 * @date 2021-03-12
 */
public class KnowledgeCategory implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 类别id */
    private String categoryId;

    /** 创建人 */
    private String createBy;

    /** 创建时间 */
    private String createDate;

    /** 修改人 */
    private String updateBy;

    /** 修改时间 */
    private String updateDate;

    /** 类别名称 */
    private String categoryName;

    /** 父类别id */
    private String parentId;

    /** 父类别m名称 */
    private String parentName;

    /** 备注 */
    private String remark;

    /** 状态 */
    private String status;

    /** 事件类型 */
    private String eventType;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
