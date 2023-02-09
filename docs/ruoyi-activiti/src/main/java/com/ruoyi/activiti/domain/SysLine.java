package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【硬件故障】对象 og_line_fault
 *
 * @author ruoyi
 * @date 2021-07-20
 */
public class SysLine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private String hardwareId;

    @Excel(name = "中心",
            readConverterExp = "1=丰台,2=亦庄,3=合肥,4=廊坊")
    private String hardwareCentre;

    @Excel(name = "环境",
            readConverterExp = "1=生产网,2=测试网,3=运维网")
    private String hardwareEnvironment;

    @Excel(name = "处室",
            readConverterExp = "1=系统管理一处,2=系统管理二处,3=资源管理处")
    private String hardwareMistress;

    @Excel(name = "设备名称")
    private String hardwareDevicename;

    @Excel(name = "品牌",
            readConverterExp = "1=思科,2=华为,3=华三,4=山石,5=Juniper,6=迈普,7=中兴,8=F5,9=A10,10=Array,11=绿盟,12=信安世纪,13=启明星辰,14=锐捷,15=深信服,16=Radware,16=宝利通")
    private String hardwareBrand;

    @Excel(name = "设备型号")
    private String hardwareTypeOne;

    @Excel(name = "故障设备序列号")
    private String hardwareSerialNumber;

    @Excel(name = "更换后设备序列号")
    private String hardwareSerialNumberChange;

    @Excel(name = "监控是否正常报警",
            readConverterExp = "1=是,2=否")
    private String hardwareIsnoPolice;

    private String hardwareTypeTwo;

    @Excel(name = "故障点",
            readConverterExp = "1=板卡故障,2=机箱故障,3=引擎故障,4=电源故障,5=风扇故障")
    private String hardwareDot;

    @Excel(name = "处置设施")
    private String registerSystem;

    @Excel(name = "确认故障时间")
    private String hardwareTime;

    @Excel(name = "备件到场时间")
    private String partPresentTime;

    @Excel(name = "人员到场时间")
    private String personPresentTime;

    @Excel(name = "处置完成时间")
    private String disposeFinishTime;

    @Excel(name = "备件耗时")
    private String partElapsedTime;

    @Excel(name = "故障处理耗时")
    private String hardwareDisposeTime;

    @Excel(name = "人员响应耗时")
    private String personAnswerTime;

    @Excel(name = "通报情况",
            readConverterExp = "1=是,2=否")
    private String informationCase;

    @Excel(name = "是否影响业务连续性",
            readConverterExp = "1=是,2=否")
    private String continuityIsno;

    @Excel(name = "值班人员")
    private String watchkeeperPerson;

    @Excel(name = "处理结果")
    private String dealWith;

    @Excel(name = "变更单号")
    private String alterationNumbers;

    @Excel(name = "维修超时原因")
    private String overtimeCause;

    @Excel(name = "备注")
    private String hardwareRemark;

    //状态
    private String lineStart;

    //故障处理人
    @Excel(name = "处理人")
    private String hardwarePerson;

    //发生时间
    @Excel(name = "发生时间")
    private String createTimeTwo;

    @Excel(name = "设备类型")
    private String hardwareTypeThere;

    private String isNo;
    private String noIs;

    public String getIsNo() {
        return isNo;
    }

    public void setIsNo(String isNo) {
        this.isNo = isNo;
    }

    public String getNoIs() {
        return noIs;
    }

    public void setNoIs(String noIs) {
        this.noIs = noIs;
    }

    public String getHardwareTypeThere() {
        return hardwareTypeThere;
    }

    public void setHardwareTypeThere(String hardwareTypeThere) {
        this.hardwareTypeThere = hardwareTypeThere;
    }

    public String getCreateTimeTwo() {
        return createTimeTwo;
    }

    public void setCreateTimeTwo(String createTimeTwo) {
        this.createTimeTwo = createTimeTwo;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getHardwareCentre() {
        return hardwareCentre;
    }

    public void setHardwareCentre(String hardwareCentre) {
        this.hardwareCentre = hardwareCentre;
    }

    public String getHardwareEnvironment() {
        return hardwareEnvironment;
    }

    public void setHardwareEnvironment(String hardwareEnvironment) {
        this.hardwareEnvironment = hardwareEnvironment;
    }

    public String getHardwareMistress() {
        return hardwareMistress;
    }

    public void setHardwareMistress(String hardwareMistress) {
        this.hardwareMistress = hardwareMistress;
    }

    public String getHardwareDevicename() {
        return hardwareDevicename;
    }

    public void setHardwareDevicename(String hardwareDevicename) {
        this.hardwareDevicename = hardwareDevicename;
    }

    public String getHardwareBrand() {
        return hardwareBrand;
    }

    public void setHardwareBrand(String hardwareBrand) {
        this.hardwareBrand = hardwareBrand;
    }

    public String getHardwareTypeOne() {
        return hardwareTypeOne;
    }

    public void setHardwareTypeOne(String hardwareTypeOne) {
        this.hardwareTypeOne = hardwareTypeOne;
    }

    public String getHardwareSerialNumber() {
        return hardwareSerialNumber;
    }

    public void setHardwareSerialNumber(String hardwareSerialNumber) {
        this.hardwareSerialNumber = hardwareSerialNumber;
    }

    public String getHardwareSerialNumberChange() {
        return hardwareSerialNumberChange;
    }

    public void setHardwareSerialNumberChange(String hardwareSerialNumberChange) {
        this.hardwareSerialNumberChange = hardwareSerialNumberChange;
    }

    public String getHardwareIsnoPolice() {
        return hardwareIsnoPolice;
    }

    public void setHardwareIsnoPolice(String hardwareIsnoPolice) {
        this.hardwareIsnoPolice = hardwareIsnoPolice;
    }

    public String getHardwareTypeTwo() {
        return hardwareTypeTwo;
    }

    public void setHardwareTypeTwo(String hardwareTypeTwo) {
        this.hardwareTypeTwo = hardwareTypeTwo;
    }

    public String getHardwareDot() {
        return hardwareDot;
    }

    public void setHardwareDot(String hardwareDot) {
        this.hardwareDot = hardwareDot;
    }

    public String getRegisterSystem() {
        return registerSystem;
    }

    public void setRegisterSystem(String registerSystem) {
        this.registerSystem = registerSystem;
    }

    public String getHardwareTime() {
        return hardwareTime;
    }

    public void setHardwareTime(String hardwareTime) {
        this.hardwareTime = hardwareTime;
    }

    public String getPartPresentTime() {
        return partPresentTime;
    }

    public void setPartPresentTime(String partPresentTime) {
        this.partPresentTime = partPresentTime;
    }

    public String getPersonPresentTime() {
        return personPresentTime;
    }

    public void setPersonPresentTime(String personPresentTime) {
        this.personPresentTime = personPresentTime;
    }

    public String getDisposeFinishTime() {
        return disposeFinishTime;
    }

    public void setDisposeFinishTime(String disposeFinishTime) {
        this.disposeFinishTime = disposeFinishTime;
    }

    public String getPartElapsedTime() {
        return partElapsedTime;
    }

    public void setPartElapsedTime(String partElapsedTime) {
        this.partElapsedTime = partElapsedTime;
    }

    public String getHardwareDisposeTime() {
        return hardwareDisposeTime;
    }

    public void setHardwareDisposeTime(String hardwareDisposeTime) {
        this.hardwareDisposeTime = hardwareDisposeTime;
    }

    public String getPersonAnswerTime() {
        return personAnswerTime;
    }

    public void setPersonAnswerTime(String personAnswerTime) {
        this.personAnswerTime = personAnswerTime;
    }

    public String getInformationCase() {
        return informationCase;
    }

    public void setInformationCase(String informationCase) {
        this.informationCase = informationCase;
    }

    public String getContinuityIsno() {
        return continuityIsno;
    }

    public void setContinuityIsno(String continuityIsno) {
        this.continuityIsno = continuityIsno;
    }

    public String getWatchkeeperPerson() {
        return watchkeeperPerson;
    }

    public void setWatchkeeperPerson(String watchkeeperPerson) {
        this.watchkeeperPerson = watchkeeperPerson;
    }

    public String getDealWith() {
        return dealWith;
    }

    public void setDealWith(String dealWith) {
        this.dealWith = dealWith;
    }

    public String getAlterationNumbers() {
        return alterationNumbers;
    }

    public void setAlterationNumbers(String alterationNumbers) {
        this.alterationNumbers = alterationNumbers;
    }

    public String getOvertimeCause() {
        return overtimeCause;
    }

    public void setOvertimeCause(String overtimeCause) {
        this.overtimeCause = overtimeCause;
    }

    public String getHardwareRemark() {
        return hardwareRemark;
    }

    public void setHardwareRemark(String hardwareRemark) {
        this.hardwareRemark = hardwareRemark;
    }

    public String getLineStart() {
        return lineStart;
    }

    public void setLineStart(String lineStart) {
        this.lineStart = lineStart;
    }

    public String getHardwarePerson() {
        return hardwarePerson;
    }

    public void setHardwarePerson(String hardwarePerson) {
        this.hardwarePerson = hardwarePerson;
    }
}
