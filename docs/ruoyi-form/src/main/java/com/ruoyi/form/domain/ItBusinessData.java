package com.ruoyi.form.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 业务-通用对象 it_business_data
 * 
 * @author ruoyi
 * @date 2022-04-06
 */
public class ItBusinessData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 业务表id */
    private String businessId;

    /** 业务单号 */
    @Excel(name = "业务单号")
    private String businessNo;

    /** 流程id(未启动流程时为空) */
    @Excel(name = "流程id(未启动流程时为空)")
    private String processId;

    /** 表单id */
    @Excel(name = "表单id")
    private String formId;

    /** 业务类型 */
    @Excel(name = "业务类型")
    private String businessType;

    /** 创建人 */
    @TableField(exist = false)
    private String createName;

    @TableField(exist = false)
    private List<ItSourceData> sourceDataList;

    public void setBusinessId(String businessId) 
    {
        this.businessId = businessId;
    }

    public String getBusinessId() 
    {
        return businessId;
    }
    public void setBusinessNo(String businessNo) 
    {
        this.businessNo = businessNo;
    }

    public String getBusinessNo() 
    {
        return businessNo;
    }
    public void setProcessId(String processId) 
    {
        this.processId = processId;
    }

    public String getProcessId() 
    {
        return processId;
    }
    public void setFormId(String formId) 
    {
        this.formId = formId;
    }

    public String getFormId() 
    {
        return formId;
    }
    public void setBusinessType(String businessType) 
    {
        this.businessType = businessType;
    }

    public String getBusinessType() 
    {
        return businessType;
    }

    public List<ItSourceData> getSourceDataList() {
        return sourceDataList;
    }

    public void setSourceDataList(List<ItSourceData> sourceDataList) {
        this.sourceDataList = sourceDataList;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("businessId", getBusinessId())
            .append("businessNo", getBusinessNo())
            .append("processId", getProcessId())
            .append("formId", getFormId())
            .append("businessType", getBusinessType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
