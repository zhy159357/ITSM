package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 接收短信对象 pub_biz_sms_receive
 * 
 * @author ruoyi
 * @date 2021-02-09
 */
public class PubBizSmsReceive extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** 发送手机号 */
    @Excel(name = "发送手机号")
    private String telephone;

    /** 发送短信内容 */
    @Excel(name = "发送短信内容")
    private String smsText;

    /** 发送时间 */
    @Excel(name = "发送时间")
    private String sendTime;

    /** 验证码 */
    @Excel(name = "验证码")
    private String verificationcode;

    /** 处理结果 */
    @Excel(name = "处理结果")
    private String smsResult;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setTelephone(String telephone) 
    {
        this.telephone = telephone;
    }

    public String getTelephone() 
    {
        return telephone;
    }
    public void setSmsText(String smsText) 
    {
        this.smsText = smsText;
    }

    public String getSmsText() 
    {
        return smsText;
    }
    public void setSendTime(String sendTime) 
    {
        this.sendTime = sendTime;
    }

    public String getSendTime() 
    {
        return sendTime;
    }
    public void setVerificationcode(String verificationcode) 
    {
        this.verificationcode = verificationcode;
    }

    public String getVerificationcode() 
    {
        return verificationcode;
    }
    public void setSmsResult(String smsResult) 
    {
        this.smsResult = smsResult;
    }

    public String getSmsResult() 
    {
        return smsResult;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("telephone", getTelephone())
            .append("smsText", getSmsText())
            .append("sendTime", getSendTime())
            .append("verificationcode", getVerificationcode())
            .append("smsResult", getSmsResult())
            .toString();
    }
}
