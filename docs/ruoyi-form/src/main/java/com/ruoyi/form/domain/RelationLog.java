package com.ruoyi.form.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 【请填写功能名称】对象 relation_log
 * 
 * @author ruoyi
 * @date 2022-09-30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelationLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 结束日期 */
    @Excel(name = "结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private String endDate;

    /** 开始日期 */
    @Excel(name = "开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private String startDate;

    /** 当前处理人 */
    @Excel(name = "当前处理人")
    private String currentHandlerId;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 请求摘要 */
    @Excel(name = "请求摘要")
    private String requestSummary;

    /** 请求类型 */
    @Excel(name = "请求类型")
    private String requestType;

    /** 关联编号 */
    @Excel(name = "关联编号")
    private String relationNo;

    /** 请求编号 */
    @Excel(name = "请求编号")
    private String requestNo;

    /** 关系类型 */
    @Excel(name = "关系类型")
    private String relationType;

    /** 主键 */
    private Long id;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setCurrentHandlerId(String currentHandlerId)
    {
        this.currentHandlerId = currentHandlerId;
    }

    public String getCurrentHandlerId() 
    {
        return currentHandlerId;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setRequestSummary(String requestSummary) 
    {
        this.requestSummary = requestSummary;
    }

    public String getRequestSummary() 
    {
        return requestSummary;
    }
    public void setRequestType(String requestType) 
    {
        this.requestType = requestType;
    }

    public String getRequestType() 
    {
        return requestType;
    }
    public void setRelationNo(String relationNo) 
    {
        this.relationNo = relationNo;
    }

    public String getRelationNo() 
    {
        return relationNo;
    }
    public void setRequestNo(String requestNo) 
    {
        this.requestNo = requestNo;
    }

    public String getRequestNo() 
    {
        return requestNo;
    }
    public void setRelationType(String relationType) 
    {
        this.relationType = relationType;
    }

    public String getRelationType() 
    {
        return relationType;
    }
    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("endDate", getEndDate())
            .append("startDate", getStartDate())
            .append("currentHandlerId", getCurrentHandlerId())
            .append("status", getStatus())
            .append("requestSummary", getRequestSummary())
            .append("requestType", getRequestType())
            .append("relationNo", getRelationNo())
            .append("requestNo", getRequestNo())
            .append("relationType", getRelationType())
            .append("id", getId())
            .toString();
    }
}
