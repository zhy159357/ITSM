package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * 隐患排查单对象 check_sheet
 * 
 * @author zx
 * @date 2021-01-19
 */
public class CheckSheet {
    private static final long serialVersionUID = 1L;

    /** ID */
    private String csId;

    /** 单号 */
    @Excel(name = "单号")
    private String csNo;

    /** 系统 */

    private String sysid;
    /**系统**/
    @Excel(name="系统")
    private String sysName;
    /** 重要级别 */
    @Excel(name = "重要级别")
    private String importLevel;

    /** 责任处室 */
    @Excel(name = "责任处室")
    private String createOrg;

    /** 责任人 */
    private String createId;

    /**责任人**/
    /** 责任人 */
    @Excel(name = "责任人")
    private String createName;
    /** 隐患描述 */
    private String hiddenText;

    /** 影响业务 */
    private String affectBusiness;

    /** 隐患分类（老旧设备、应用性能、业务流程、功能不全、灾备系统、监控系统、信息安全、其他隐患） */
    @Excel(name = "隐患分类")
    private String hiddenSort;

    /** 最近一次关联故障时间 */
    private String lastTime;

    /** 关联问题单号 */
    private String issuefxNo;

    /** 是否提交运维建议函 */
    private String maintLetter;

    /** 整改建议 */
    private String rectProp;

    /** 整改单位 */
    private String putUnit;

    /** 整改进度 */
    private String unitSchedule;

    /** 审核人(处长) */
    private String checkName;

    /** 状态（待提交 0/待审核 1/待处理 2/退回待修改 3/已处理 4/作废 5） */
    @Excel(name = "状态")
    private String status;

    /** 分管领导 */
    @Excel(name = "分管领导")
    private String leadName;

    /** 无效标志 */
    private String invalidationMark;

    /** 整改实施状态(待分析 1/待实施 2/已处理 3) */
    private String iStatus;

    /** 预计解决时间 */
    private String jjTime;

    /** 审核人ID */
    private String checkId;


    /** 处理人姓名 */
    @Excel(name="处理人")
    private String technologyauditName;

    /** 处理人ID */
    private String technologyauditId;
    /**
     * 创建时间
     */
    @Excel(name="创建时间")
    private String createTime;
    /**更新时间**/
    private String updateTime;
    /**分管领导Id**/
    private String leadId;

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    /** 请求参数 */
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {

        this.params = params;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setCsId(String csId)
    {
        this.csId = csId;
    }

    public String getCsId() 
    {
        return csId;
    }
    public void setCsNo(String csNo) 
    {
        this.csNo = csNo;
    }

    public String getCsNo() 
    {
        return csNo;
    }
    public void setSysid(String sysid) 
    {
        this.sysid = sysid;
    }

    public String getSysid() 
    {
        return sysid;
    }
    public void setImportLevel(String importLevel) 
    {
        this.importLevel = importLevel;
    }

    public String getImportLevel() 
    {
        return importLevel;
    }
    public void setCreateOrg(String createOrg) 
    {
        this.createOrg = createOrg;
    }

    public String getCreateOrg() 
    {
        return createOrg;
    }
    public void setCreateId(String createId) 
    {
        this.createId = createId;
    }

    public String getCreateId() 
    {
        return createId;
    }
    public void setHiddenText(String hiddenText) 
    {
        this.hiddenText = hiddenText;
    }

    public String getHiddenText() 
    {
        return hiddenText;
    }
    public void setAffectBusiness(String affectBusiness) 
    {
        this.affectBusiness = affectBusiness;
    }

    public String getAffectBusiness() 
    {
        return affectBusiness;
    }
    public void setHiddenSort(String hiddenSort) 
    {
        this.hiddenSort = hiddenSort;
    }

    public String getHiddenSort() 
    {
        return hiddenSort;
    }
    public void setLastTime(String lastTime) 
    {
        this.lastTime = lastTime;
    }

    public String getLastTime() 
    {
        return lastTime;
    }
    public void setIssuefxNo(String issuefxNo) 
    {
        this.issuefxNo = issuefxNo;
    }

    public String getIssuefxNo() 
    {
        return issuefxNo;
    }
    public void setMaintLetter(String maintLetter) 
    {
        this.maintLetter = maintLetter;
    }

    public String getMaintLetter() 
    {
        return maintLetter;
    }
    public void setRectProp(String rectProp) 
    {
        this.rectProp = rectProp;
    }

    public String getRectProp() 
    {
        return rectProp;
    }
    public void setPutUnit(String putUnit) 
    {
        this.putUnit = putUnit;
    }

    public String getPutUnit() 
    {
        return putUnit;
    }
    public void setUnitSchedule(String unitSchedule) 
    {
        this.unitSchedule = unitSchedule;
    }

    public String getUnitSchedule() 
    {
        return unitSchedule;
    }
    public void setCheckName(String checkName) 
    {
        this.checkName = checkName;
    }

    public String getCheckName() 
    {
        return checkName;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setLeadName(String leadName) 
    {
        this.leadName = leadName;
    }

    public String getLeadName() 
    {
        return leadName;
    }
    public void setInvalidationMark(String invalidationMark) 
    {
        this.invalidationMark = invalidationMark;
    }

    public String getInvalidationMark() 
    {
        return invalidationMark;
    }
    public void setiStatus(String iStatus) 
    {
        this.iStatus = iStatus;
    }

    public String getiStatus() 
    {
        return iStatus;
    }
    public void setJjTime(String jjTime) 
    {
        this.jjTime = jjTime;
    }

    public String getJjTime() 
    {
        return jjTime;
    }
    public void setCheckId(String checkId) 
    {
        this.checkId = checkId;
    }

    public String getCheckId() 
    {
        return checkId;
    }
    public void setTechnologyauditName(String technologyauditName) 
    {
        this.technologyauditName = technologyauditName;
    }

    public String getTechnologyauditName() 
    {
        return technologyauditName;
    }
    public void setTechnologyauditId(String technologyauditId) 
    {
        this.technologyauditId = technologyauditId;
    }

    public String getTechnologyauditId() 
    {
        return technologyauditId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("csId", getCsId())
            .append("csNo", getCsNo())
            .append("sysid", getSysid())
            .append("importLevel", getImportLevel())
            .append("createOrg", getCreateOrg())
            .append("createId", getCreateId())
            .append("hiddenText", getHiddenText())
            .append("affectBusiness", getAffectBusiness())
            .append("hiddenSort", getHiddenSort())
            .append("lastTime", getLastTime())
            .append("issuefxNo", getIssuefxNo())
            .append("maintLetter", getMaintLetter())
            .append("rectProp", getRectProp())
            .append("putUnit", getPutUnit())
            .append("unitSchedule", getUnitSchedule())
            .append("checkName", getCheckName())
            .append("status", getStatus())
            .append("leadName", getLeadName())
            .append("invalidationMark", getInvalidationMark())
            .append("createTime", getCreateTime())
            .append("iStatus", getiStatus())
            .append("jjTime", getJjTime())
            .append("updateTime", getUpdateTime())
            .append("checkId", getCheckId())
            .append("technologyauditName", getTechnologyauditName())
            .append("technologyauditId", getTechnologyauditId())
            .toString();
    }
}
