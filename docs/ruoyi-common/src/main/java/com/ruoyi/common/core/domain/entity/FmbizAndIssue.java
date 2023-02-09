package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 事件单关联问题单关联关系对象 fmbiz_and_issue
 *
 * @author ruoyi
 * @date 2021-10-15
 */
public class FmbizAndIssue extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 运维事件单ID
     */
    @Excel(name = "运维事件单ID")
    private String fmId;

    /**
     * 状态 0关联 1待关联
     */
    @Excel(name = "状态 0关联 1待关联")
    private String state;

    /**
     * 问题单ID
     */
    @Excel(name = "问题单ID")
    private String issuefxId;

    /**
     * 操作组ID
     */
    @Excel(name = "操作组ID")
    private String groupId;

    /**
     * 操作人ID
     */
    @Excel(name = "操作人ID")
    private String operationId;

    /**
     * 操作时间
     */
    @Excel(name = "操作时间")
    private String operationTime;


    private String ccount;


    public String getCcount() {
        return ccount;
    }

    public void setCcount(String ccount) {
        this.ccount = ccount;
    }

    /**
     * ID
     */
    private String id;

    public void setFmId(String fmId) {
        this.fmId = fmId;
    }

    public String getFmId() {
        return fmId;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setIssuefxId(String issuefxId) {
        this.issuefxId = issuefxId;
    }

    public String getIssuefxId() {
        return issuefxId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("fmId", getFmId())
                .append("state", getState())
                .append("issuefxId", getIssuefxId())
                .append("groupId", getGroupId())
                .append("operationId", getOperationId())
                .append("operationTime", getOperationTime())
                .append("id", getId())
                .toString();
    }
}
