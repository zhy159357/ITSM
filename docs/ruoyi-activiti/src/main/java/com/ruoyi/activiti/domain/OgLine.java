package com.ruoyi.activiti.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.OgSys;

import java.util.Map;

public class OgLine extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String lineId;
    @Excel(name = "中心",
            readConverterExp = "1=丰台,2=亦庄,3=合肥,4=廊坊")
    private String lineCore;
    @Excel(name = "环境",
            readConverterExp = "1=生产,2=测试")
    private String lineAmbient;
    @Excel(name = "处室",
            readConverterExp = "1=系统管理一处,2=系统管理二处,3=资源管理处")
    private String LineOffice;
    //@Excel(name = "系统id",)
    private String sysId;
    @Excel(name = "用途")
    private String purpose;
    @Excel(name = "设备位置")
    private String equipment;

    @Excel(name = "故障分类",
            readConverterExp = "1=服务器故障,2=存储故障,3=光纤交换机故障,4=IB交换机故障,5=操作系统,6=操作系统自动重启,7=其他")
    private String classIfication;
    @Excel(name = "故障点",
            readConverterExp = "01=电源,02=硬盘,03=主板,04=背板,05=电池,06=风扇,07=网卡,08=光纤卡,09=IB卡,10=CPU,11=内存,12=控制器,13=光纤模块,14=其他")
    private String classDot;
    @Excel(name = "处置设施",
            readConverterExp = "1=更换,2=升级,3=重启,4=其他")
    private String treatment;

    private String repairsTime;
    @Excel(name = "报修时间")
    private String startTime;
    @Excel(name = "确认故障时间")
    private String affirmRepairsTime;
    @Excel(name = "备件到场时间")
    private String presentTime;
    @Excel(name = "人员到场时间")
    private String personPresentTime;
    @Excel(name = "结束时间")
    private String endTime;
    @Excel(name = "备件耗时")
    private String spareTime;
    @Excel(name = "故障处理耗时")
    private String elapsedTime;
    @Excel(name = "人员响应耗时")
    private String personAnswerTime;

    @Excel(name = "通报情况",
            readConverterExp = "1=是,2=否")
    private String briefing;
    @Excel(name = "是否影响业务连接性",
            readConverterExp = "1=是,2=否")
    private String continuity;
    @Excel(name = "值班人员")
    private String pId;
    @Excel(name = "设备型号")
    private String unitType;
    @Excel(name = "序列号")
    private String seriesNumber;
    private String companies;
    @Excel(name = "监控是否正常报警",
            readConverterExp = "1=是,2=否")
    private String monitoring;
    @Excel(name = "CaseID")
    private String caseId;
    @Excel(name = "故障处理人")
    private String handler;
    @Excel(name = "处理结果",
            readConverterExp = "1=成功,2=失败")
    private String handlerStructure;
    @Excel(name = "是否三方维保",
            readConverterExp = "1=是,2=否")
    private String threeParties;
    private String manufacturer;
    @Excel(name = "变更单号")
    private String bgId;
    @Excel(name = "是否消磁",
            readConverterExp = "1=是,2=否")
    private String degaussing;
    @Excel(name = "维修超时原因")
    private String maintenance;
    @Excel(name = "备注")
    private String remark;
    @Excel(name = "设备类型",
            readConverterExp = "1=维保设备,2=在保设备,3=测试设备")
    private String lineTypeOne;
    @Excel(name = "故障是否明确",
            readConverterExp = "1=是,2=否")
    private String lineIsnoOne;

    private String orgIdNameThree;

    @Excel(name = "系统名称")
    private String pIdNameOne;

    public String getRepairsTime() {
        return repairsTime;
    }

    public void setRepairsTime(String repairsTime) {
        this.repairsTime = repairsTime;
    }

    public String getAffirmRepairsTime() {
        return affirmRepairsTime;
    }

    public void setAffirmRepairsTime(String affirmRepairsTime) {
        this.affirmRepairsTime = affirmRepairsTime;
    }

    public String getPersonPresentTime() {
        return personPresentTime;
    }

    public void setPersonPresentTime(String personPresentTime) {
        this.personPresentTime = personPresentTime;
    }

    public String getPersonAnswerTime() {
        return personAnswerTime;
    }

    public void setPersonAnswerTime(String personAnswerTime) {
        this.personAnswerTime = personAnswerTime;
    }

    public String getLineTypeOne() {
        return lineTypeOne;
    }

    public void setLineTypeOne(String lineTypeOne) {
        this.lineTypeOne = lineTypeOne;
    }

    public String getLineIsnoOne() {
        return lineIsnoOne;
    }

    public void setLineIsnoOne(String lineIsnoOne) {
        this.lineIsnoOne = lineIsnoOne;
    }

    //故障状态
    private String lineStart;

    public String getLineStart() {
        return lineStart;
    }

    public void setLineStart(String lineStart) {
        this.lineStart = lineStart;
    }

    /*系统名称*/
    private String caption;

    /*人员名称*/
    private String pName;

    /*导出*/
    private Map<String,Object> params;

    /*机构名称*/
    @Excel(name = "所属公司")
    private String orgNameOne;
    @Excel(name = "三方维保厂商")
    private String orgNameTwo;

    /*审核机构和审核人*/
    private String pIdTwo;
    private String orgId;

    public String getpIdTwo() {
        return pIdTwo;
    }

    public void setpIdTwo(String pIdTwo) {
        this.pIdTwo = pIdTwo;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgIdNameThree() {
        return orgIdNameThree;
    }

    public void setOrgIdNameThree(String orgIdNameThree) {
        this.orgIdNameThree = orgIdNameThree;
    }

    public String getpIdNameOne() {
        return pIdNameOne;
    }

    public void setpIdNameOne(String pIdNameOne) {
        this.pIdNameOne = pIdNameOne;
    }

    /*系统表*/
    private OgSys ogSys;

    public OgSys getOgSys() {
        return ogSys;
    }

    public void setOgSys(OgSys ogSys) {
        this.ogSys = ogSys;
    }

    public String getOrgNameOne() {
        return orgNameOne;
    }

    public void setOrgNameOne(String orgNameOne) {
        this.orgNameOne = orgNameOne;
    }

    public String getOrgNameTwo() {
        return orgNameTwo;
    }

    public void setOrgNameTwo(String orgNameTwo) {
        this.orgNameTwo = orgNameTwo;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getSpareTime() {
        return spareTime;
    }

    public void setSpareTime(String spareTime) {
        this.spareTime = spareTime;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getLineCore() {
        return lineCore;
    }

    public void setLineCore(String lineCore) {
        this.lineCore = lineCore;
    }

    public String getLineAmbient() {
        return lineAmbient;
    }

    public void setLineAmbient(String lineAmbient) {
        this.lineAmbient = lineAmbient;
    }

    public String getLineOffice() {
        return LineOffice;
    }

    public void setLineOffice(String lineOffice) {
        LineOffice = lineOffice;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPresentTime() {
        return presentTime;
    }

    public void setPresentTime(String presentTime) {
        this.presentTime = presentTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getClassIfication() {
        return classIfication;
    }

    public void setClassIfication(String classIfication) {
        this.classIfication = classIfication;
    }

    public String getClassDot() {
        return classDot;
    }

    public void setClassDot(String classDot) {
        this.classDot = classDot;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getBriefing() {
        return briefing;
    }

    public void setBriefing(String briefing) {
        this.briefing = briefing;
    }

    public String getContinuity() {
        return continuity;
    }

    public void setContinuity(String continuity) {
        this.continuity = continuity;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public String getCompanies() {
        return companies;
    }

    public void setCompanies(String companies) {
        this.companies = companies;
    }

    public String getMonitoring() {
        return monitoring;
    }

    public void setMonitoring(String monitoring) {
        this.monitoring = monitoring;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getHandlerStructure() {
        return handlerStructure;
    }

    public void setHandlerStructure(String handlerStructure) {
        this.handlerStructure = handlerStructure;
    }

    public String getThreeParties() {
        return threeParties;
    }

    public void setThreeParties(String threeParties) {
        this.threeParties = threeParties;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getBgId() {
        return bgId;
    }

    public void setBgId(String bgId) {
        this.bgId = bgId;
    }

    public String getDegaussing() {
        return degaussing;
    }

    public void setDegaussing(String degaussing) {
        this.degaussing = degaussing;
    }

    public String getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(String maintenance) {
        this.maintenance = maintenance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
