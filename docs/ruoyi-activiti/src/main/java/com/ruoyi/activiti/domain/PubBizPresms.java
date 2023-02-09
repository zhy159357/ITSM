package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 短信存储对象 pub_biz_presms
 * 
 * @author ruoyi
 * @date 2021-02-09
 */
public class PubBizPresms extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String pubBizPresmsId;

    /** 短信类型 */
    @Excel(name = "短信类型")
    private String smsType;

    /** 电话 */
    @Excel(name = "电话")
    private String telephone;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 短信内容 */
    @Excel(name = "短信内容")
    private String smsText;

    /** 检查时间 */
    @Excel(name = "检查时间")
    private String inspectTime;

    /** 检查对象 */
    @Excel(name = "检查对象")
    private String inspectObject;

    /** 检查对象ID */
    @Excel(name = "检查对象ID")
    private String inspectObjectId;

    /** 检查内容 */
    @Excel(name = "检查内容")
    private String inspectText;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createrId;

    /** 是否有效 */
    @Excel(name = "是否有效")
    private String invalidationMark;

    /** 0 - 未发送 1 - 已发送 */
    @Excel(name = "0 - 未发送 1 - 已发送")
    private String smsState;

    /** 任务ID(上行短信使用) */
    @Excel(name = "任务ID(上行短信使用)")
    private String taskId;

    /** 验证码 */
    @Excel(name = "验证码")
    private String verificationcode;

    /** 0 - 未处理 1 - 短信处理 2 - 页面处理 */
    @Excel(name = "0 - 未处理 1 - 短信处理 2 - 页面处理")
    private String dealStatus;

    public void setPubBizPresmsId(String pubBizPresmsId) 
    {
        this.pubBizPresmsId = pubBizPresmsId;
    }

    public String getPubBizPresmsId() 
    {
        return pubBizPresmsId;
    }
    public void setSmsType(String smsType) 
    {
        this.smsType = smsType;
    }

    public String getSmsType() 
    {
        return smsType;
    }
    public void setTelephone(String telephone) 
    {
        this.telephone = telephone;
    }

    public String getTelephone() 
    {
        return telephone;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setSmsText(String smsText) 
    {
        this.smsText = smsText;
    }

    public String getSmsText() 
    {
        return smsText;
    }
    public void setInspectTime(String inspectTime) 
    {
        this.inspectTime = inspectTime;
    }

    public String getInspectTime() 
    {
        return inspectTime;
    }
    public void setInspectObject(String inspectObject) 
    {
        this.inspectObject = inspectObject;
    }

    public String getInspectObject() 
    {
        return inspectObject;
    }
    public void setInspectObjectId(String inspectObjectId) 
    {
        this.inspectObjectId = inspectObjectId;
    }

    public String getInspectObjectId() 
    {
        return inspectObjectId;
    }
    public void setInspectText(String inspectText) 
    {
        this.inspectText = inspectText;
    }

    public String getInspectText() 
    {
        return inspectText;
    }
    public void setCreaterId(String createrId) 
    {
        this.createrId = createrId;
    }

    public String getCreaterId() 
    {
        return createrId;
    }
    public void setInvalidationMark(String invalidationMark) 
    {
        this.invalidationMark = invalidationMark;
    }

    public String getInvalidationMark() 
    {
        return invalidationMark;
    }
    public void setSmsState(String smsState) 
    {
        this.smsState = smsState;
    }

    public String getSmsState() 
    {
        return smsState;
    }
    public void setTaskId(String taskId) 
    {
        this.taskId = taskId;
    }

    public String getTaskId() 
    {
        return taskId;
    }
    public void setVerificationcode(String verificationcode) 
    {
        this.verificationcode = verificationcode;
    }

    public String getVerificationcode() 
    {
        return verificationcode;
    }
    public void setDealStatus(String dealStatus) 
    {
        this.dealStatus = dealStatus;
    }

    public String getDealStatus() 
    {
        return dealStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("pubBizPresmsId", getPubBizPresmsId())
            .append("smsType", getSmsType())
            .append("telephone", getTelephone())
            .append("name", getName())
            .append("smsText", getSmsText())
            .append("inspectTime", getInspectTime())
            .append("inspectObject", getInspectObject())
            .append("inspectObjectId", getInspectObjectId())
            .append("inspectText", getInspectText())
            .append("createrId", getCreaterId())
            .append("updateTime", getUpdateTime())
            .append("invalidationMark", getInvalidationMark())
            .append("smsState", getSmsState())
            .append("taskId", getTaskId())
            .append("verificationcode", getVerificationcode())
            .append("dealStatus", getDealStatus())
            .toString();
    }
}
