package com.ruoyi.form.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 risk_access_record
 * 
 * @author ruoyi
 * @date 2022-10-02
 */
public class RiskAccessRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 变更单单号 */
    @Excel(name = "变更单单号")
    private String changeNo;

    /** 操作时间 */
    @Excel(name = "操作时间")
    private String operateTime;

    /** 操作人 */
    @Excel(name = "操作人")
    private String operator;

    /** 原始风险级别 */
    @Excel(name = "原始风险级别")
    private String initLevel;

    /** 当前风险级别 */
    @Excel(name = "当前风险级别")
    private String currentLevel;

    /** 变更风险级别的原因 */
    @Excel(name = "变更风险级别的原因")
    private String reason;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setChangeNo(String changeNo) 
    {
        this.changeNo = changeNo;
    }

    public String getChangeNo() 
    {
        return changeNo;
    }
    public void setOperateTime(String operateTime) 
    {
        this.operateTime = operateTime;
    }

    public String getOperateTime() 
    {
        return operateTime;
    }
    public void setOperator(String operator) 
    {
        this.operator = operator;
    }

    public String getOperator() 
    {
        return operator;
    }
    public void setInitLevel(String initLevel) 
    {
        this.initLevel = initLevel;
    }

    public String getInitLevel() 
    {
        return initLevel;
    }
    public void setCurrentLevel(String currentLevel) 
    {
        this.currentLevel = currentLevel;
    }

    public String getCurrentLevel() 
    {
        return currentLevel;
    }
    public void setReason(String reason) 
    {
        this.reason = reason;
    }

    public String getReason() 
    {
        return reason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("changeNo", getChangeNo())
            .append("operateTime", getOperateTime())
            .append("operator", getOperator())
            .append("initLevel", getInitLevel())
            .append("currentLevel", getCurrentLevel())
            .append("reason", getReason())
            .toString();
    }
}
