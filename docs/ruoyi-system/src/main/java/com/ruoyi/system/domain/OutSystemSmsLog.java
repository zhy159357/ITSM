package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 对外系统短信验证码日志对象 out_system_sms_log
 * 
 * @author ruoyi
 * @date 2021-04-26
 */
public class OutSystemSmsLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 系统名称 */
    @Excel(name = "系统名称")
    private String sysName;

    /** 系统编码 */
    @Excel(name = "系统编码")
    private String sysCode;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phoneNumber;

    /** 发送状态 */
    @Excel(name = "发送状态")
    private String sendStatus;

    /** 短信信息 */
    @Excel(name = "短信信息")
    private String msg;

    /** 主键ID */
    private String logId;

    public void setSysName(String sysName) 
    {
        this.sysName = sysName;
    }

    public String getSysName() 
    {
        return sysName;
    }
    public void setSysCode(String sysCode) 
    {
        this.sysCode = sysCode;
    }

    public String getSysCode() 
    {
        return sysCode;
    }
    public void setPhoneNumber(String phoneNumber) 
    {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() 
    {
        return phoneNumber;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("sysName", getSysName())
            .append("sysCode", getSysCode())
            .append("phoneNumber", getPhoneNumber())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("sendStatus", getSendStatus())
            .append("msg", getMsg())
            .append("outId", getLogId())
            .toString();
    }
}
