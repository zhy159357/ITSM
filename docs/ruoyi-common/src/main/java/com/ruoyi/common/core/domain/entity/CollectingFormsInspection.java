package com.ruoyi.common.core.domain.entity;


import lombok.Data;



/**
 * 报表管理对象 ITSM_HC_COLLECTING
 *
 * @author ruoyi
 * @date 2022-3-8
 */
@Data
public class CollectingFormsInspection {
    /**
     * 主键
     */
    private String id;

    /**
     * 应用系统名称版本
     */
    private String sysName;

    /**
     * 应用系统运维单位
     */
    private String sysCompany;

    /**
     * 巡检人员
     */
    private String inspectors;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 系统负责人
     */
    private String sysManager;

    /**
     * 巡检项最后更新日期
     */
    private String lastDate;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 爬虫进程是否正常(0：异常；1：正常)
     */
    private String crawlerStates;

    /**
     * 爬虫进程是否正常说明
     */
    private String crawlerAnnotation;

    /**
     * 采集调度是否正常(0：异常；1：正常)
     */
    private String dispatchingStates;

    /**
     * 采集调度是否正常说明
     */
    private String dispatchingAnnotation;

    /**
     * 采集进程是否正常(0：异常；1：正常)
     */
    private String processStates;

    /**
     * 采集进程是否正常说明
     */
    private String processAnnotation;

    /**
     * 异常预警运行是否正常(0：异常；1：正常)
     */
    private String warningStates;

    /**
     * 异常预警运行是否正常说明
     */
    private String warningAnnotation;

    /**
     * 爬虫更新进程运行是否正常(0：异常；1：正常)
     */
    private String updateStates;

    /**
     * 爬虫更新进程运行是否正常说明
     */
    private String updateAnnotation;

    /**
     * 数据质量核验是否正常(0：异常；1：正常)
     */
    private String qualityStates;

    /**
     * 数据质量核验是否正常说明
     */
    private String qualityAnnotation;



    /**
     * 备注
     */
    private String remark;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysCompany() {
        return sysCompany;
    }

    public void setSysCompany(String sysCompany) {
        this.sysCompany = sysCompany;
    }

    public String getInspectors() {
        return inspectors;
    }

    public void setInspectors(String inspectors) {
        this.inspectors = inspectors;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSysManager() {
        return sysManager;
    }

    public void setSysManager(String sysManager) {
        this.sysManager = sysManager;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCrawlerStates() {
        return crawlerStates;
    }

    public void setCrawlerStates(String crawlerStates) {
        this.crawlerStates = crawlerStates;
    }

    public String getCrawlerAnnotation() {
        return crawlerAnnotation;
    }

    public void setCrawlerAnnotation(String crawlerAnnotation) {
        this.crawlerAnnotation = crawlerAnnotation;
    }

    public String getDispatchingStates() {
        return dispatchingStates;
    }

    public void setDispatchingStates(String dispatchingStates) {
        this.dispatchingStates = dispatchingStates;
    }

    public String getDispatchingAnnotation() {
        return dispatchingAnnotation;
    }

    public void setDispatchingAnnotation(String dispatchingAnnotation) {
        this.dispatchingAnnotation = dispatchingAnnotation;
    }

    public String getProcessStates() {
        return processStates;
    }

    public void setProcessStates(String processStates) {
        this.processStates = processStates;
    }

    public String getProcessAnnotation() {
        return processAnnotation;
    }

    public void setProcessAnnotation(String processAnnotation) {
        this.processAnnotation = processAnnotation;
    }

    public String getWarningStates() {
        return warningStates;
    }

    public void setWarningStates(String warningStates) {
        this.warningStates = warningStates;
    }

    public String getWarningAnnotation() {
        return warningAnnotation;
    }

    public void setWarningAnnotation(String warningAnnotation) {
        this.warningAnnotation = warningAnnotation;
    }

    public String getUpdateStates() {
        return updateStates;
    }

    public void setUpdateStates(String updateStates) {
        this.updateStates = updateStates;
    }

    public String getUpdateAnnotation() {
        return updateAnnotation;
    }

    public void setUpdateAnnotation(String updateAnnotation) {
        this.updateAnnotation = updateAnnotation;
    }

    public String getQualityStates() {
        return qualityStates;
    }

    public void setQualityStates(String qualityStates) {
        this.qualityStates = qualityStates;
    }

    public String getQualityAnnotation() {
        return qualityAnnotation;
    }

    public void setQualityAnnotation(String qualityAnnotation) {
        this.qualityAnnotation = qualityAnnotation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "CollectingFormsInspection{" +
                "id='" + id + '\'' +
                ", sysName='" + sysName + '\'' +
                ", sysCompany='" + sysCompany + '\'' +
                ", inspectors='" + inspectors + '\'' +
                ", contact='" + contact + '\'' +
                ", sysManager='" + sysManager + '\'' +
                ", lastDate='" + lastDate + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", crawlerStates='" + crawlerStates + '\'' +
                ", crawlerAnnotation='" + crawlerAnnotation + '\'' +
                ", dispatchingStates='" + dispatchingStates + '\'' +
                ", dispatchingAnnotation='" + dispatchingAnnotation + '\'' +
                ", processStates='" + processStates + '\'' +
                ", processAnnotation='" + processAnnotation + '\'' +
                ", warningStates='" + warningStates + '\'' +
                ", warningAnnotation='" + warningAnnotation + '\'' +
                ", updateStates='" + updateStates + '\'' +
                ", updateAnnotation='" + updateAnnotation + '\'' +
                ", qualityStates='" + qualityStates + '\'' +
                ", qualityAnnotation='" + qualityAnnotation + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
