package com.ruoyi.form.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 元数据对象 it_source_data
 * 
 * @author ruoyi
 * @date 2022-04-06
 */
public class ItSourceData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 元数据id */
    private String sourceDataId;

    /** 业务表id */
    @Excel(name = "业务表id")
    private String businessId;

    /** label标签中文名称 */
    private String labelName;

    /** 元素名称 */
    @Excel(name = "元素名称")
    private String dataKey;

    /** 元素值 */
    @Excel(name = "元素值")
    private String dataValue;

    /** 元素字典key（下拉选特有） */
    @Excel(name = "元素字典key")
    private String dictKey;

    /** 是否必输 */
    private String isRequired;
    /** 是否可以修改 */
    private String isChange;
    /** html类型 */
    private String htmlType;

    /** 保存时间 */
    @Excel(name = "保存时间")
    private String insertTime;

    /**表单编号唯一标识*/
    private String formKey;

    public void setSourceDataId(String sourceDataId) 
    {
        this.sourceDataId = sourceDataId;
    }

    public String getSourceDataId() 
    {
        return sourceDataId;
    }
    public void setBusinessId(String businessId) 
    {
        this.businessId = businessId;
    }

    public String getBusinessId() 
    {
        return businessId;
    }
    public void setDataKey(String dataKey) 
    {
        this.dataKey = dataKey;
    }

    public String getDataKey() 
    {
        return dataKey;
    }
    public void setDataValue(String dataValue) 
    {
        this.dataValue = dataValue;
    }

    public String getDataValue() 
    {
        return dataValue;
    }
    public void setDictKey(String dictKey) 
    {
        this.dictKey = dictKey;
    }

    public String getDictKey() 
    {
        return dictKey;
    }
    public void setInsertTime(String insertTime) 
    {
        this.insertTime = insertTime;
    }

    public String getInsertTime() 
    {
        return insertTime;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(String isRequired) {
        this.isRequired = isRequired;
    }

    public String getIsChange() {
        return isChange;
    }

    public void setIsChange(String isChange) {
        this.isChange = isChange;
    }

    public String getHtmlType() {
        return htmlType;
    }

    public void setHtmlType(String htmlType) {
        this.htmlType = htmlType;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("sourceDataId", getSourceDataId())
            .append("businessId", getBusinessId())
            .append("dataKey", getDataKey())
            .append("dataValue", getDataValue())
            .append("dictKey", getDictKey())
            .append("insertTime", getInsertTime())
            .append("remark", getRemark())
            .toString();
    }
}
