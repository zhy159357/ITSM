package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【数据库恢复登记表】对象 cm_db_recovery_register
 * 
 * @author ruoyi
 * @date 2021-08-02
 */
public class CmDbRecoveryRegister extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private String recoveryRegisterId;

    @Excel(name = "所属中心名称",
            readConverterExp = "1=丰台,2=亦庄,3=合肥,4=廊坊")
    private String centreName;

    private String sysId;

    @Excel(name = "业务系统名称")
    private String sysName;

    @Excel(name = "主机名")
    private String hostName;

    @Excel(name = "数据库备份日期")
    private String backupDate;

    @Excel(name = "恢复开始时间")
    private String recoveryStartTime;

    @Excel(name = "恢复结束时间")
    private String recoveryEndTime;

    @Excel(name = "恢复耗时（分钟）")
    private String recoveryTime;

    @Excel(name = "备份有效性验证结果",
            readConverterExp = "01=成功,02=失败")
    private String verificationResult;

    @Excel(name = "恢复完数据大小（MB）")
    private String dataSize;

    @Excel(name = "数据库类型",
            readConverterExp = "1=ORACLE,2=Postgresql,3=OpenGauss")
    private String type;

    @Excel(name = "状态",
            readConverterExp = "1=待提交,2=已提交,3=待审核,4=已审核")
    private String lineStart;

    private String pId;
    private String orgId;
    private String orgName;

    private String somment;

    public String getSomment() {
        return somment;
    }

    public void setSomment(String somment) {
        this.somment = somment;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getLineStart() {
        return lineStart;
    }

    public void setLineStart(String lineStart) {
        this.lineStart = lineStart;
    }

    public void setRecoveryRegisterId(String recoveryRegisterId)
    {
        this.recoveryRegisterId = recoveryRegisterId;
    }

    public String getRecoveryRegisterId() 
    {
        return recoveryRegisterId;
    }
    public void setCentreName(String centreName) 
    {
        this.centreName = centreName;
    }

    public String getCentreName() 
    {
        return centreName;
    }
    public void setSysId(String sysId) 
    {
        this.sysId = sysId;
    }

    public String getSysId() 
    {
        return sysId;
    }
    public void setSysName(String sysName) 
    {
        this.sysName = sysName;
    }

    public String getSysName() 
    {
        return sysName;
    }
    public void setHostName(String hostName) 
    {
        this.hostName = hostName;
    }

    public String getHostName() 
    {
        return hostName;
    }
    public void setBackupDate(String backupDate) 
    {
        this.backupDate = backupDate;
    }

    public String getBackupDate() 
    {
        return backupDate;
    }
    public void setRecoveryStartTime(String recoveryStartTime) 
    {
        this.recoveryStartTime = recoveryStartTime;
    }

    public String getRecoveryStartTime() 
    {
        return recoveryStartTime;
    }
    public void setRecoveryEndTime(String recoveryEndTime) 
    {
        this.recoveryEndTime = recoveryEndTime;
    }

    public String getRecoveryEndTime() 
    {
        return recoveryEndTime;
    }
    public void setRecoveryTime(String recoveryTime) 
    {
        this.recoveryTime = recoveryTime;
    }

    public String getRecoveryTime() 
    {
        return recoveryTime;
    }
    public void setVerificationResult(String verificationResult) 
    {
        this.verificationResult = verificationResult;
    }

    public String getVerificationResult() 
    {
        return verificationResult;
    }
    public void setDataSize(String dataSize) 
    {
        this.dataSize = dataSize;
    }

    public String getDataSize() 
    {
        return dataSize;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("recoveryRegisterId", getRecoveryRegisterId())
            .append("centreName", getCentreName())
            .append("sysId", getSysId())
            .append("sysName", getSysName())
            .append("hostName", getHostName())
            .append("backupDate", getBackupDate())
            .append("recoveryStartTime", getRecoveryStartTime())
            .append("recoveryEndTime", getRecoveryEndTime())
            .append("recoveryTime", getRecoveryTime())
            .append("verificationResult", getVerificationResult())
            .append("remark", getRemark())
            .append("dataSize", getDataSize())
            .append("type", getType())
            .toString();
    }
}
