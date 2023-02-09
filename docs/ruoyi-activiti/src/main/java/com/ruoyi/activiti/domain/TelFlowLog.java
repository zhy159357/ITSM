package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 tel_flow_log
 * 
 * @author ruoyi
 * @date 2021-06-18
 */
public class TelFlowLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Excel(name = "ID")
    private String logId;

    /** 电话单id */
    @Excel(name = "电话单id")
    private String telId;

    /** 操作名称 */
    @Excel(name = "操作名称")
    private String operationName;

    /** 操作人 */
    @Excel(name = "操作人")
    private String operator;

    /** 联系方式 */
    @Excel(name = "联系方式")
    private String operatorTel;

    /** 状态 */
    @Excel(name = "状态")
    private String state;

    /** 序号 */
    @Excel(name = "序号")
    private String serialNum;

    private String creatTime;

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public void setLogId(String logId)
    {
        this.logId = logId;
    }

    public String getLogId() 
    {
        return logId;
    }
    public void setTelId(String telId) 
    {
        this.telId = telId;
    }

    public String getTelId() 
    {
        return telId;
    }
    public void setOperationName(String operationName) 
    {
        this.operationName = operationName;
    }

    public String getOperationName() 
    {
        return operationName;
    }
    public void setOperator(String operator) 
    {
        this.operator = operator;
    }

    public String getOperator() 
    {
        return operator;
    }
    public void setOperatorTel(String operatorTel) 
    {
        this.operatorTel = operatorTel;
    }

    public String getOperatorTel() 
    {
        return operatorTel;
    }
    public void setState(String state) 
    {
        this.state = state;
    }

    public String getState() 
    {
        return state;
    }
    public void setSerialNum(String serialNum) 
    {
        this.serialNum = serialNum;
    }

    public String getSerialNum() 
    {
        return serialNum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("telId", getTelId())
            .append("operationName", getOperationName())
            .append("createTime", getCreateTime())
            .append("operator", getOperator())
            .append("operatorTel", getOperatorTel())
            .append("state", getState())
            .append("serialNum", getSerialNum())
            .toString();
    }
}
