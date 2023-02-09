package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【态势感知工单】对象 fm_sawo
 *
 * @author ruoyi
 * @date 2021-10-12
 */
public class FmSawo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 工单id */
    private String ordId;

    /** 工单单号 */
    @Excel(name = "工单单号")
    private String ordNo;

    /** 告警/漏洞名称 */
    @Excel(name = "告警/漏洞/问题名称")
    private String gaaName;

    /** 告警/漏洞级别 */
    @Excel(name = "告警/漏洞/问题级别", readConverterExp = "1=低,2=中,3=高")
    private String gaaLev;

    /** 工单类型 */
    @Excel(name = "工单类型", readConverterExp = "1=漏洞工单,2=告警工单,3=问题工单")
    private String ordType;

    /** 来源IP */
    @Excel(name = "来源IP")
    private String sourceIp;

    /** 目的IP */
    @Excel(name = "目的IP")
    private String objIp;

    /** 告警/漏洞类型 */
    @Excel(name = "告警/漏洞/问题类型", readConverterExp = "1=系统,2=应用,3=网络,4=其他")
    private String gaaType;

    /** 告警/漏洞类别value */
    @Excel(name = "告警/漏洞/问题类别")
    private String gaaCategoryValue;

    /** 告警/漏洞类别 */
    private String gaaCategory;

    /** 目的IP所属系统 */
    @Excel(name = "目的IP所属系统")
    private String sysId;

    /** 所属部门/分行 */
    @Excel(name = "所属部门/分行", readConverterExp = "1=丰台,2=亦庄,3=合肥,4=XX分行")
    private String belDept;

    /** 责任处室/分行 */
    @Excel(name = "责任处室/分行")
    private String resDept;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String createrTime;

    /** 系统管理员 */
    @Excel(name = "系统管理员")
    private String sysAdmin;

    /** 应用管理员 */
    @Excel(name = "应用管理员")
    private String appAdmin;

    /** 告警/漏洞描述 */
    @Excel(name = "告警/漏洞/问题描述")
    private String gaaDesc;

    /** 处置人 */
    private String dealId;

    /** 处置人所属机构 */
    private String dealDept;

    /** 处置完成时间 */
    private String dealTime;

    /** 备注信息 */
    @Excel(name = "备注信息")
    private String rem;

    /** 状态 */
    @Excel(name = "状态", readConverterExp = "1=待分发,2=待处置,3=待验证,4=已关闭,")
    private String ordState;

    /** $column.columnComment */
    private String n1;

    /** 创建人id */
    private String createId;

    /** 处置结果 */
    private String disposeResult;

    private String n3;

    /** 传过来的  工单编号 */
    private String orderId;

    /** 告警/漏洞类型value */
    private String gaaTypeValue;

    /** 附件 */
    private String filePast;

    public String getFilePast() {
        return filePast;
    }

    public void setFilePast(String filePast) {
        this.filePast = filePast;
    }

    public String getGaaCategoryValue() {
        return gaaCategoryValue;
    }

    public void setGaaCategoryValue(String gaaCategoryValue) {
        this.gaaCategoryValue = gaaCategoryValue;
    }

    public String getGaaTypeValue() {
        return gaaTypeValue;
    }

    public void setGaaTypeValue(String gaaTypeValue) {
        this.gaaTypeValue = gaaTypeValue;
    }

    public void setOrdId(String ordId)
    {
        this.ordId = ordId;
    }

    public String getOrdId()
    {
        return ordId;
    }
    public void setOrdNo(String ordNo)
    {
        this.ordNo = ordNo;
    }

    public String getOrdNo()
    {
        return ordNo;
    }
    public void setGaaName(String gaaName)
    {
        this.gaaName = gaaName;
    }

    public String getGaaName()
    {
        return gaaName;
    }
    public void setGaaLev(String gaaLev)
    {
        this.gaaLev = gaaLev;
    }

    public String getGaaLev()
    {
        return gaaLev;
    }
    public void setOrdType(String ordType)
    {
        this.ordType = ordType;
    }

    public String getOrdType()
    {
        return ordType;
    }
    public void setSourceIp(String sourceIp)
    {
        this.sourceIp = sourceIp;
    }

    public String getSourceIp()
    {
        return sourceIp;
    }
    public void setObjIp(String objIp)
    {
        this.objIp = objIp;
    }

    public String getObjIp()
    {
        return objIp;
    }
    public void setGaaType(String gaaType)
    {
        this.gaaType = gaaType;
    }

    public String getGaaType()
    {
        return gaaType;
    }
    public void setGaaCategory(String gaaCategory)
    {
        this.gaaCategory = gaaCategory;
    }

    public String getGaaCategory()
    {
        return gaaCategory;
    }
    public void setSysId(String sysId)
    {
        this.sysId = sysId;
    }

    public String getSysId()
    {
        return sysId;
    }
    public void setBelDept(String belDept)
    {
        this.belDept = belDept;
    }

    public String getBelDept()
    {
        return belDept;
    }
    public void setResDept(String resDept)
    {
        this.resDept = resDept;
    }

    public String getResDept()
    {
        return resDept;
    }
    public void setCreaterTime(String createrTime)
    {
        this.createrTime = createrTime;
    }

    public String getCreaterTime()
    {
        return createrTime;
    }
    public void setSysAdmin(String sysAdmin)
    {
        this.sysAdmin = sysAdmin;
    }

    public String getSysAdmin()
    {
        return sysAdmin;
    }
    public void setAppAdmin(String appAdmin)
    {
        this.appAdmin = appAdmin;
    }

    public String getAppAdmin()
    {
        return appAdmin;
    }
    public void setGaaDesc(String gaaDesc)
    {
        this.gaaDesc = gaaDesc;
    }

    public String getGaaDesc()
    {
        return gaaDesc;
    }
    public void setDealId(String dealId)
    {
        this.dealId = dealId;
    }

    public String getDealId()
    {
        return dealId;
    }
    public void setDealDept(String dealDept)
    {
        this.dealDept = dealDept;
    }

    public String getDealDept()
    {
        return dealDept;
    }
    public void setDealTime(String dealTime)
    {
        this.dealTime = dealTime;
    }

    public String getDealTime()
    {
        return dealTime;
    }
    public void setRem(String rem)
    {
        this.rem = rem;
    }

    public String getRem()
    {
        return rem;
    }
    public void setOrdState(String ordState)
    {
        this.ordState = ordState;
    }

    public String getOrdState()
    {
        return ordState;
    }
    public void setN1(String n1)
    {
        this.n1 = n1;
    }

    public String getN1()
    {
        return n1;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getDisposeResult() {
        return disposeResult;
    }

    public void setDisposeResult(String disposeResult) {
        this.disposeResult = disposeResult;
    }

    public String getN3() {
        return n3;
    }

    public void setN3(String n3) {
        this.n3 = n3;
    }
}
