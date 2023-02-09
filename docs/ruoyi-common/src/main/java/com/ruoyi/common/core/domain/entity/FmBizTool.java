package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 工具使用情况对象 fm_biz_tool
 * 
 * @author ruoyi
 * @date 2021-01-21
 */
public class FmBizTool extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String fbtId;

    /** 事件单ID */
    private String fmId;

    /** 是否使用工具 */
    private String ifTool;

    /** 工具名称 */
    private String toolName;

    /** 处理人 */
    private String dealName;

    /** $column.columnComment */
    private String n1;

    /** $column.columnComment */
    private String n2;

    /** 系统ID */
    private String sysId;

    /** 处理耗时 */
    private String residenceTime;

    /** 处理时间 */
    private String dealTime;

    /** 应用系统作为关联用*/
    private OgSys ogSys;
    @Excel(name="所属系统",sort = 1)
    private String sysName;
    /** 所属机构*/
    @Excel(name="所属公司",sort = 2)
    private String orgName;
    /**使用工具情况统计*/
    @Excel(name="处理步骤总数",sort = 3)
    private String dealCouNum;//处理步骤总数
    @Excel(name="使用工具数量",sort = 4)
    private String isToolsNum;//使用工具数量
    @Excel(name="工具使用比例",sort = 5)
    private String countTool;//工具使用比例

    public void setFbtId(String fbtId) 
    {
        this.fbtId = fbtId;
    }

    public String getFbtId() 
    {
        return fbtId;
    }
    public void setFmId(String fmId) 
    {
        this.fmId = fmId;
    }

    public String getFmId() 
    {
        return fmId;
    }
    public void setIfTool(String ifTool) 
    {
        this.ifTool = ifTool;
    }

    public String getIfTool() 
    {
        return ifTool;
    }
    public void setToolName(String toolName) 
    {
        this.toolName = toolName;
    }

    public String getToolName() 
    {
        return toolName;
    }
    public void setDealName(String dealName) 
    {
        this.dealName = dealName;
    }

    public String getDealName() 
    {
        return dealName;
    }
    public void setN1(String n1) 
    {
        this.n1 = n1;
    }

    public String getN1() 
    {
        return n1;
    }
    public void setN2(String n2) 
    {
        this.n2 = n2;
    }

    public String getN2() 
    {
        return n2;
    }
    public void setSysId(String sysId) 
    {
        this.sysId = sysId;
    }

    public String getSysId() 
    {
        return sysId;
    }
    public void setResidenceTime(String residenceTime) 
    {
        this.residenceTime = residenceTime;
    }

    public String getResidenceTime() 
    {
        return residenceTime;
    }
    public void setDealTime(String dealTime) 
    {
        this.dealTime = dealTime;
    }

    public String getDealTime() 
    {
        return dealTime;
    }

    public String getDealCouNum() {
        return dealCouNum;
    }

    public void setDealCouNum(String dealCouNum) {
        this.dealCouNum = dealCouNum;
    }

    public String getIsToolsNum() {
        return isToolsNum;
    }

    public void setIsToolsNum(String isToolsNum) {
        this.isToolsNum = isToolsNum;
    }

    public String getCountTool() {
        return countTool;
    }

    public void setCountTool(String countTool) {
        this.countTool = countTool;
    }

    public OgSys getOgSys() {
        return ogSys;
    }

    public void setOgSys(OgSys ogSys) {

        if(ogSys != null){
            this.sysName = ogSys.getCaption();
        }else{
            ogSys = new OgSys();
        }
        this.ogSys = ogSys;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("fbtId", getFbtId())
            .append("fmId", getFmId())
            .append("ifTool", getIfTool())
            .append("toolName", getToolName())
            .append("dealName", getDealName())
            .append("n1", getN1())
            .append("n2", getN2())
            .append("sysId", getSysId())
            .append("residenceTime", getResidenceTime())
            .append("dealTime", getDealTime())
            .toString();
    }
}
