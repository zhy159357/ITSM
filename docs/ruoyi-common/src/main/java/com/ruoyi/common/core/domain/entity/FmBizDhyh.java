package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 转发电话银行消息对象 fm_biz_dhyh
 * 
 * @author ruoyi
 * @date 2021-02-18
 */
public class FmBizDhyh extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @Excel(name = "主键id")
    private String fdId;

    /** 电话银行ID */
    @Excel(name = "电话银行ID")
    private String telId;

    /** 处理人 */
    @Excel(name = "处理人")
    private String dealId;

    /** 处理人联系方式 */
    @Excel(name = "处理人联系方式")
    private String dealTel;

    /** 事件单号 */
    @Excel(name = "事件单号")
    private String fmId;

    /** 处理人所在系统 */
    @Excel(name = "处理人所在系统")
    private String dealDept;

    /** 处理内容 */
    @Excel(name = "处理内容")
    private String dealContent;

    /** 处理方式0退回修改，1处理 */
    @Excel(name = "处理方式0退回修改，1处理")
    private String dealResult;

    /** 发送状态0成功1失败 */
    @Excel(name = "发送状态0成功1失败")
    private String fdStatus;

    /** 发送次数 */
    @Excel(name = "发送次数")
    private String sendNum;

    /** 03电话银行04信用卡 */
    @Excel(name = "03电话银行04信用卡")
    private String fdType;

    /** 发送时间 */
    @Excel(name = "发送时间")
    private String sendTime;

    public void setFdId(String fdId) 
    {
        this.fdId = fdId;
    }

    public String getFdId() 
    {
        return fdId;
    }
    public void setTelId(String telId) 
    {
        this.telId = telId;
    }

    public String getTelId() 
    {
        return telId;
    }
    public void setDealId(String dealId) 
    {
        this.dealId = dealId;
    }

    public String getDealId() 
    {
        return dealId;
    }
    public void setDealTel(String dealTel) 
    {
        this.dealTel = dealTel;
    }

    public String getDealTel() 
    {
        return dealTel;
    }
    public void setFmId(String fmId) 
    {
        this.fmId = fmId;
    }

    public String getFmId() 
    {
        return fmId;
    }
    public void setDealDept(String dealDept) 
    {
        this.dealDept = dealDept;
    }

    public String getDealDept() 
    {
        return dealDept;
    }
    public void setDealContent(String dealContent) 
    {
        this.dealContent = dealContent;
    }

    public String getDealContent() 
    {
        return dealContent;
    }
    public void setDealResult(String dealResult) 
    {
        this.dealResult = dealResult;
    }

    public String getDealResult() 
    {
        return dealResult;
    }
    public void setFdStatus(String fdStatus) 
    {
        this.fdStatus = fdStatus;
    }

    public String getFdStatus() 
    {
        return fdStatus;
    }
    public void setSendNum(String sendNum) 
    {
        this.sendNum = sendNum;
    }

    public String getSendNum() 
    {
        return sendNum;
    }
    public void setFdType(String fdType) 
    {
        this.fdType = fdType;
    }

    public String getFdType() 
    {
        return fdType;
    }
    public void setSendTime(String sendTime) 
    {
        this.sendTime = sendTime;
    }

    public String getSendTime() 
    {
        return sendTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("fdId", getFdId())
            .append("telId", getTelId())
            .append("dealId", getDealId())
            .append("dealTel", getDealTel())
            .append("fmId", getFmId())
            .append("dealDept", getDealDept())
            .append("dealContent", getDealContent())
            .append("dealResult", getDealResult())
            .append("fdStatus", getFdStatus())
            .append("sendNum", getSendNum())
            .append("fdType", getFdType())
            .append("sendTime", getSendTime())
            .toString();
    }
}
