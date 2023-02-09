package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 短信发送对象 pub_biz_sms
 * 
 * @author ruoyi
 * @date 2021-02-09
 */
public class PubBizSms extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String pubBizSmsId;

    /** 存储表ID */
    @Excel(name = "存储表ID")
    private String pubBizPresmsId;

    /** 序号 */
    @Excel(name = "序号")
    private Long serialNumber;

    /** 电话 */
    @Excel(name = "电话")
    private String telephone;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 短信内容 */
    @Excel(name = "短信内容")
    private String smsText;

    /** 发送时间 */
    @Excel(name = "发送时间")
    private String smsTime;

    /** 状态 */
    @Excel(name = "状态")
    private String smsState;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createrId;

    /** 是否有效 */
    @Excel(name = "是否有效")
    private String invalidationMark;

    public void setPubBizSmsId(String pubBizSmsId) 
    {
        this.pubBizSmsId = pubBizSmsId;
    }

    public String getPubBizSmsId() 
    {
        return pubBizSmsId;
    }
    public void setPubBizPresmsId(String pubBizPresmsId) 
    {
        this.pubBizPresmsId = pubBizPresmsId;
    }

    public String getPubBizPresmsId() 
    {
        return pubBizPresmsId;
    }
    public void setSerialNumber(Long serialNumber) 
    {
        this.serialNumber = serialNumber;
    }

    public Long getSerialNumber() 
    {
        return serialNumber;
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
    public void setSmsTime(String smsTime) 
    {
        this.smsTime = smsTime;
    }

    public String getSmsTime() 
    {
        return smsTime;
    }
    public void setSmsState(String smsState) 
    {
        this.smsState = smsState;
    }

    public String getSmsState() 
    {
        return smsState;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("pubBizSmsId", getPubBizSmsId())
            .append("pubBizPresmsId", getPubBizPresmsId())
            .append("serialNumber", getSerialNumber())
            .append("telephone", getTelephone())
            .append("name", getName())
            .append("smsText", getSmsText())
            .append("smsTime", getSmsTime())
            .append("smsState", getSmsState())
            .append("createrId", getCreaterId())
            .append("updateTime", getUpdateTime())
            .append("invalidationMark", getInvalidationMark())
            .toString();
    }
}
