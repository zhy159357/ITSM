package com.ruoyi.form.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 单模版对象 it_form_data
 * 
 * @author ruoyi
 * @date 2022-04-06
 */
public class ItFormData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 模版表id */
    private String formId;

    /** 模版key */
    @Excel(name = "模版key")
    private String formKey;

    /** Form模版名称 */
    @Excel(name = "Form模版名称")
    private String formName;

    /** 模版json */
    @Excel(name = "模版json")
    private String formData;

    public void setFormId(String formId) 
    {
        this.formId = formId;
    }

    public String getFormId() 
    {
        return formId;
    }
    public void setFormKey(String formKey) 
    {
        this.formKey = formKey;
    }

    public String getFormKey() 
    {
        return formKey;
    }
    public void setFormName(String formName) 
    {
        this.formName = formName;
    }

    public String getFormName() 
    {
        return formName;
    }
    public void setFormData(String formData) 
    {
        this.formData = formData;
    }

    public String getFormData() 
    {
        return formData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("formId", getFormId())
            .append("formKey", getFormKey())
            .append("formName", getFormName())
            .append("formData", getFormData())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
