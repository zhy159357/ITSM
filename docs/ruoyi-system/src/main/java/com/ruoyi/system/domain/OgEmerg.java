package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgSys;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;
import java.util.Map;

public class OgEmerg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //事件id
    private String id;

    //单号
    @Excel(name = "单号")
    private String emergId;
    //关联系统id 系统名称
    private String xtId;
    //描述
    @Excel(name = "事件描述")
    private String emergMemo;
    //时间
    //Date
    private String emergTime;
    //来源
    @Excel(name = "事件来源",
            readConverterExp = "1=运行事件,2=手动创建")
    private String emergLy;
    //状态
    private String emergMark;
    //对应的状态
    @Excel(name = "状态")
    private String markName;

    //关联的系统名称
    private String caption;

    //复选的系统id
    private String xtsId;

    //这块是新建的按钮里的字段
    //事件等级
    private String emergGrade;
    //事件类型
    private String emergType;
    //发生时间
    //Date
    private String occurTime;
    //所属系统
    @Excel(name = "应用系统")
    private String emergSystem;
    //所属工作组
    private String emergWork;
    //处理人
    private String emergHandler;
    //联系电话
    private String emergPhone;
    //事件类别
    private String emergSort;
    //应急标题
    private String emergTitle;
    //系统级别
    private String systemRank;
    //应急启动时间
    //Date
    @Excel(name = "创建时间")
    private String startTime;
    //应急结束时间
    //Date
    private String endTime;
    //故障时长(自动计算)
    private String emergFault;
    //启动应急系统
    private String startSystem;
    //现象和影响业务的描述
    private String operationMemo;

    //第二次新增的字段
    //关联人员的表   现在是   创建人
    private String personemergId;
    //关联运行事件单的表
    private String runId;
    //是否有告警
    private String emergCaution;
    //是否彻底解决
    private String emergSolve;
    //是否有客户投诉
    private String emergComplaint;
    //是否有资金风险
    private String emergRisk;
    //故障报告提交人
    private String emergSubmitter;

    /** 应用系统作为关联用*/
    private OgSys ogSys;

    private OgPerson ogPerson;// 人员

    private Map<String,Object> params;

    @Override
    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public OgPerson getOgPerson() {
        return ogPerson;
    }

    public void setOgPerson(OgPerson ogPerson) {
        this.ogPerson = ogPerson;
    }

    public String getXtsId() {
        return xtsId;
    }

    public void setXtsId(String xtsId) {
        this.xtsId = xtsId;
    }

    public String getPersonemergId() {
        return personemergId;
    }

    public void setPersonemergId(String personemergId) {
        this.personemergId = personemergId;
    }

    public OgSys getOgSys() {
        return ogSys;
    }

    public void setOgSys(OgSys ogSys) {
        this.ogSys = ogSys;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getEmergCaution() {
        return emergCaution;
    }

    public void setEmergCaution(String emergCaution) {
        this.emergCaution = emergCaution;
    }

    public String getEmergSolve() {
        return emergSolve;
    }

    public void setEmergSolve(String emergSolve) {
        this.emergSolve = emergSolve;
    }

    public String getEmergComplaint() {
        return emergComplaint;
    }

    public void setEmergComplaint(String emergComplaint) {
        this.emergComplaint = emergComplaint;
    }

    public String getEmergRisk() {
        return emergRisk;
    }

    public void setEmergRisk(String emergRisk) {
        this.emergRisk = emergRisk;
    }

    public String getEmergSubmitter() {
        return emergSubmitter;
    }

    public void setEmergSubmitter(String emergSubmitter) {
        this.emergSubmitter = emergSubmitter;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmergGrade() {
        return emergGrade;
    }

    public void setEmergGrade(String emergGrade) {
        this.emergGrade = emergGrade;
    }

    public String getEmergType() {
        return emergType;
    }

    public void setEmergType(String emergType) {
        this.emergType = emergType;
    }

    public String getEmergSystem() {
        return emergSystem;
    }

    public void setEmergSystem(String emergSystem) {
        this.emergSystem = emergSystem;
    }

    public String getEmergWork() {
        return emergWork;
    }

    public void setEmergWork(String emergWork) {
        this.emergWork = emergWork;
    }

    public String getEmergHandler() {
        return emergHandler;
    }

    public void setEmergHandler(String emergHandler) {
        this.emergHandler = emergHandler;
    }

    public String getEmergPhone() {
        return emergPhone;
    }

    public void setEmergPhone(String emergPhone) {
        this.emergPhone = emergPhone;
    }

    public String getEmergSort() {
        return emergSort;
    }

    public void setEmergSort(String emergSort) {
        this.emergSort = emergSort;
    }

    public String getEmergTitle() {
        return emergTitle;
    }

    public void setEmergTitle(String emergTitle) {
        this.emergTitle = emergTitle;
    }

    public String getSystemRank() {
        return systemRank;
    }

    public void setSystemRank(String systemRank) {
        this.systemRank = systemRank;
    }

    public String getEmergFault() {
        return emergFault;
    }

    public void setEmergFault(String emergFault) {
        this.emergFault = emergFault;
    }

    public String getStartSystem() {
        return startSystem;
    }

    public void setStartSystem(String startSystem) {
        this.startSystem = startSystem;
    }

    public String getOperationMemo() {
        return operationMemo;
    }

    public void setOperationMemo(String operationMemo) {
        this.operationMemo = operationMemo;
    }

    public String getMarkName() {
        return markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName;
    }

    public String getEmergId() {
        return emergId;
    }

    public void setEmergId(String emergId) {
        this.emergId = emergId;
    }

    public String getXtId() {
        return xtId;
    }

    public void setXtId(String xtId) {
        this.xtId = xtId;
    }

    public String getEmergMemo() {
        return emergMemo;
    }

    public void setEmergMemo(String emergMemo) {
        this.emergMemo = emergMemo;
    }

    public String getEmergLy() {
        return emergLy;
    }

    public void setEmergLy(String emergLy) {
        this.emergLy = emergLy;
    }

    public String getEmergMark() {
        return emergMark;
    }

    public void setEmergMark(String emergMark) {
        this.emergMark = emergMark;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getEmergTime() {
        return emergTime;
    }

    public void setEmergTime(String emergTime) {
        this.emergTime = emergTime;
    }

    public String getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
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
}
