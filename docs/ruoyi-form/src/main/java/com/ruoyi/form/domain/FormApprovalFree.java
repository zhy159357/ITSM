package com.ruoyi.form.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 form_approval_free
 * 
 * @author ruoyi
 * @date 2022-10-31
 */
public class FormApprovalFree extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 是否免审批，0=否，1=是 */
    @Excel(name = "是否免审批，0=否，1=是")
    private Long flag;

    /** 审批意见 */
    @Excel(name = "审批意见")
    private String comment;

    /** 单子类型 */
    @Excel(name = "单子类型")
    private String type;

    /** 流程节点的key */
    @Excel(name = "流程节点的key")
    private String defineKey;

    /** 审批人名字 */
    @Excel(name = "审批人名字")
    private String approvalName;

    /** 审批人id */
    @Excel(name = "审批人id")
    private String approval;

    /** 单子主键 */
    @Excel(name = "单子主键")
    private String formId;

    /** 单子单号 */
    @Excel(name = "单子单号")
    private String formNo;

    /** 主键 */
    private Long id;

    public void setFlag(Long flag) 
    {
        this.flag = flag;
    }

    public Long getFlag() 
    {
        return flag;
    }
    public void setComment(String comment) 
    {
        this.comment = comment;
    }

    public String getComment() 
    {
        return comment;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setDefineKey(String defineKey) 
    {
        this.defineKey = defineKey;
    }

    public String getDefineKey() 
    {
        return defineKey;
    }
    public void setApprovalName(String approvalName) 
    {
        this.approvalName = approvalName;
    }

    public String getApprovalName() 
    {
        return approvalName;
    }
    public void setApproval(String approval) 
    {
        this.approval = approval;
    }

    public String getApproval() 
    {
        return approval;
    }
    public void setFormId(String formId) 
    {
        this.formId = formId;
    }

    public String getFormId() 
    {
        return formId;
    }
    public void setFormNo(String formNo) 
    {
        this.formNo = formNo;
    }

    public String getFormNo() 
    {
        return formNo;
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
            .append("flag", getFlag())
            .append("comment", getComment())
            .append("type", getType())
            .append("defineKey", getDefineKey())
            .append("approvalName", getApprovalName())
            .append("approval", getApproval())
            .append("formId", getFormId())
            .append("formNo", getFormNo())
            .append("id", getId())
            .toString();
    }
}
