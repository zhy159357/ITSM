package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 对外系统短信验证码对象 out_system_sms_code
 * 
 * @author ruoyi
 * @date 2021-04-25
 */
public class OutSystemSmsCode extends BaseEntity
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

    /** 登录id */
    @Excel(name = "登录id")
    private String loginId;

    /** 短信验证码 */
    @Excel(name = "短信验证码")
    private String smsCode;

    /** 尝试次数 */
    @Excel(name = "尝试次数")
    private Long tryNum;

    /** 短信信息 */
    @Excel(name = "短信信息")
    private String msg;

    /** 移动运维调用短信，验证码的超时时间 */
    private String mobileAppEffectiveTime;

    /** 主键ID */
    private String outId;

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
    public void setLoginId(String loginId) 
    {
        this.loginId = loginId;
    }

    public String getLoginId() 
    {
        return loginId;
    }
    public void setSmsCode(String smsCode) 
    {
        this.smsCode = smsCode;
    }

    public String getSmsCode() 
    {
        return smsCode;
    }
    public void setTryNum(Long tryNum) 
    {
        this.tryNum = tryNum;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTryNum()
    {
        return tryNum;
    }
    public void setOutId(String outId) 
    {
        this.outId = outId;
    }

    public String getOutId() 
    {
        return outId;
    }

    public String getMobileAppEffectiveTime() {
        return mobileAppEffectiveTime;
    }

    public void setMobileAppEffectiveTime(String mobileAppEffectiveTime) {
        this.mobileAppEffectiveTime = mobileAppEffectiveTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("sysName", getSysName())
            .append("sysCode", getSysCode())
            .append("phoneNumber", getPhoneNumber())
            .append("loginId", getLoginId())
            .append("smsCode", getSmsCode())
            .append("tryNum", getTryNum())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("msg", getMsg())
            .append("outId", getOutId())
            .append("mobileAppEffectiveTime", getMobileAppEffectiveTime())
            .toString();
    }
}
