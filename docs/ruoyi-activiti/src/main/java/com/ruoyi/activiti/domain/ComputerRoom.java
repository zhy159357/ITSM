package com.ruoyi.activiti.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 机房出入登记 COMPUTER_ROOM_APPLY
 */
public class ComputerRoom extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /** 主键 */
    private String id;

    //机房申请编码
    @Excel(name = "机房出入编号" ,sort = 0)
    private String computerApplyNo;

    /** 申请人ID */
    private String applyUserId;

    /** 申请人名称 */
    @Excel(name = "申请人名称" ,sort = 1)
    private String applyUserName;

    /** 申请人处室ID */
    private String applyOrgId;

    /** 申请人处室名称 */
    @Excel(name = "申请人处室名称" ,sort = 2)
    private String applyOrgName;

    //机房所属中心
    private String computerCenter;

    //机房所属中心名称
    @Excel(name = "机房所属中心" ,sort = 3 )
    private String computerCenterName;


    /** 进入机房模块*/
    private String computerRoomModule;

    /** 进入机房模块名称*/
    @Excel(name = "机房模块",sort = 4)
    private String computerRoomModuleName;

    /** 陪同人员ID*/
    private String accompanyUserId;

    /** 陪同人员名称*/
    @Excel(name = "陪同人员1" ,sort = 5)
    private String accompanyUserName;

    /** 陪同人员2 ID*/
    private String accompanyUserIdTwo;

    /** 陪同人员2 名称*/
    @Excel(name = "陪同人员2" ,sort = 6)
    private String accompanyUserNameTwo;


    /**预计进入时间*/
    @Excel(name = "预计进入时间",sort = 7)
    private String predictInTime;

    /**预计离开时间*/
    @Excel(name = "预计离开时间",sort = 8)
    private String predictOutTime;

    /**工作内容id */
    private String workContentIds;

    /**工作内容 */
    @Excel(name = "工作内容",sort = 9)
    private String workContent;

    /**具体工作描述 */
    @Excel(name = "具体工作描述",sort = 10)
    private String workDescription;


    /**审批状态  0:已提交，1:审批通过，2:退回， 3:已登记 */
    @Excel(name = "状态", readConverterExp="-2=已作废,-1=暂存,0=已申请,1=审批通过,2=退回,3=已登记",sort = 11)
    private String applyState;

    /** 登记陪同人员ID*/
    private String  registerAccompanyUserId;

    /** 实际陪同人员名称*/
    @Excel(name = "实际陪同人员" ,sort = 17)
    private String  registerAccompanyUserName;

    /**登记时间*/
    @Excel(name = "登记时间",sort = 27)
    private String registerTime;

    //审理理由
    private String reason;

    //审核人ID
    private String auditorId;

    //审核人名称
    @Excel(name = "审核人名称",sort = 25)
    private String auditorName;

    private String status;

    @Excel(name = "创建时间",sort = 24)
    private String createTimes;

    private String updateTimes;

    //是否为临时人员（0 否，1 是）
    @Excel(name = "是否为临时人员", readConverterExp="0=否,1=是",sort = 12)
    private String isTemporary;

    //进入人员姓名
    @Excel(name = "进入人员名称",sort = 13)
    private String intoName;

    //进入人员身份证号
    private String intoIdNumber;

    //进入人员电话
    private String intoPhone;

    //进入人员单位
    private String intoUnit;

    //是否携带物品（0 否，1 是）
    @Excel(name = "是否携带物品", readConverterExp="0=否,1=是",sort = 14)
    private String isBelongings;

    //携带物品
    @Excel(name = "携带物品",sort = 15)
    private String belongings;

    //携带物品数量
    @Excel(name = "携带物品数量",sort = 15)
    private String belongingsNum;

    //进出情况
    @Excel(name = "携带物品数量进出情况",sort = 15)
    private String inOutType;

    //安装位置
    private String installSite;


    //是否紧急情况（0 否，1 是）
    private String isUrgency;


    //陪同人1员备注
    private String accompanyUserOne;

    //实际进入时间
    @Excel(name = "实际进入时间",sort = 18)
    private String realityIntoTime;

    //实际离开时间
    @Excel(name = "实际离开时间",sort = 19)
    private String realityOuttoTime;

    //实际进入人员
    @Excel(name = "实际进入人员",sort = 20)
    private String realityIntoPersonnel;

    //实际携带物品
    @Excel(name = "实际携带物品",sort = 22)
    private String realityBelongings;

    //实际携带物品数量
    @Excel(name = "实际携带物品数量",sort = 22)
    private String realityBelongingsNum;

    //备注
    @Excel(name = "备注信息",sort = 23)
    private String remark;

    //实际进入机房模块
    @Excel(name = "实际进入机房模块",sort = 21)
    private String realityModule;

    //实际登记人员
    @Excel(name = "实际登记人员",sort = 26)
    private String registerName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }


    public String getComputerRoomModule() {
        return computerRoomModule;
    }

    public void setComputerRoomModule(String computerRoomModule) {
        this.computerRoomModule = computerRoomModule;
    }

    public String getAccompanyUserId() {
        return accompanyUserId;
    }

    public void setAccompanyUserId(String accompanyUserId) {
        this.accompanyUserId = accompanyUserId;
    }

    public String getAccompanyUserName() {
        return accompanyUserName;
    }

    public void setAccompanyUserName(String accompanyUserName) {
        this.accompanyUserName = accompanyUserName;
    }

    public String getPredictInTime() {
        return predictInTime;
    }

    public void setPredictInTime(String predictInTime) {
        this.predictInTime = predictInTime;
    }

    public String getPredictOutTime() {
        return predictOutTime;
    }

    public void setPredictOutTime(String predictOutTime) {
        this.predictOutTime = predictOutTime;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }


    public String getApplyState() {
        return applyState;
    }

    public void setApplyState(String applyState) {
        this.applyState = applyState;
    }

    public String getRegisterAccompanyUserId() {
        return registerAccompanyUserId;
    }

    public void setRegisterAccompanyUserId(String registerAccompanyUserId) {
        this.registerAccompanyUserId = registerAccompanyUserId;
    }

    public String getRegisterAccompanyUserName() {
        return registerAccompanyUserName;
    }

    public void setRegisterAccompanyUserName(String registerAccompanyUserName) {
        this.registerAccompanyUserName = registerAccompanyUserName;
    }

    public String getApplyOrgId() {
        return applyOrgId;
    }

    public void setApplyOrgId(String applyOrgId) {
        this.applyOrgId = applyOrgId;
    }

    public String getApplyOrgName() {
        return applyOrgName;
    }

    public void setApplyOrgName(String applyOrgName) {
        this.applyOrgName = applyOrgName;
    }

    public String getWorkContentIds() {
        return workContentIds;
    }

    public void setWorkContentIds(String workContentIds) {
        this.workContentIds = workContentIds;
    }


    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getCreateTimes() {
        return createTimes;
    }

    public void setCreateTimes(String createTimes) {
        this.createTimes = createTimes;
    }

    public String getUpdateTimes() {
        return updateTimes;
    }

    public void setUpdateTimes(String updateTimes) {
        this.updateTimes = updateTimes;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsTemporary() {
        return isTemporary;
    }

    public void setIsTemporary(String isTemporary) {
        this.isTemporary = isTemporary;
    }

    public String getIntoName() {
        return intoName;
    }

    public void setIntoName(String intoName) {
        this.intoName = intoName;
    }

    public String getIntoIdNumber() {
        return intoIdNumber;
    }

    public void setIntoIdNumber(String intoIdNumber) {
        this.intoIdNumber = intoIdNumber;
    }

    public String getIntoPhone() {
        return intoPhone;
    }

    public void setIntoPhone(String intoPhone) {
        this.intoPhone = intoPhone;
    }

    public String getIntoUnit() {
        return intoUnit;
    }

    public void setIntoUnit(String intoUnit) {
        this.intoUnit = intoUnit;
    }

    public String getIsBelongings() {
        return isBelongings;
    }

    public void setIsBelongings(String isBelongings) {
        this.isBelongings = isBelongings;
    }

    public String getBelongings() {
        return belongings;
    }

    public void setBelongings(String belongings) {
        this.belongings = belongings;
    }

    public String getIsUrgency() {
        return isUrgency;
    }

    public void setIsUrgency(String isUrgency) {
        this.isUrgency = isUrgency;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getComputerCenter() {
        return computerCenter;
    }

    public void setComputerCenter(String computerCenter) {
        this.computerCenter = computerCenter;
    }

    public String getAccompanyUserOne() {
        return accompanyUserOne;
    }

    public void setAccompanyUserOne(String accompanyUserOne) {
        this.accompanyUserOne = accompanyUserOne;
    }

    public String getRealityIntoTime() {
        return realityIntoTime;
    }

    public void setRealityIntoTime(String realityIntoTime) {
        this.realityIntoTime = realityIntoTime;
    }

    public String getRealityOuttoTime() {
        return realityOuttoTime;
    }

    public void setRealityOuttoTime(String realityOuttoTime) {
        this.realityOuttoTime = realityOuttoTime;
    }

    public String getRealityIntoPersonnel() {
        return realityIntoPersonnel;
    }

    public void setRealityIntoPersonnel(String realityIntoPersonnel) {
        this.realityIntoPersonnel = realityIntoPersonnel;
    }

    public String getRealityBelongings() {
        return realityBelongings;
    }

    public void setRealityBelongings(String realityBelongings) {
        this.realityBelongings = realityBelongings;
    }

    public String getRealityModule() {
        return realityModule;
    }

    public void setRealityModule(String realityModule) {
        this.realityModule = realityModule;
    }

    public String getComputerRoomModuleName() {
        return computerRoomModuleName;
    }

    public void setComputerRoomModuleName(String computerRoomModuleName) {
        this.computerRoomModuleName = computerRoomModuleName;
    }

    public String getAccompanyUserIdTwo() {
        return accompanyUserIdTwo;
    }

    public void setAccompanyUserIdTwo(String accompanyUserIdTwo) {
        this.accompanyUserIdTwo = accompanyUserIdTwo;
    }

    public String getAccompanyUserNameTwo() {
        return accompanyUserNameTwo;
    }

    public void setAccompanyUserNameTwo(String accompanyUserNameTwo) {
        this.accompanyUserNameTwo = accompanyUserNameTwo;
    }

    public String getBelongingsNum() {
        return belongingsNum;
    }

    public void setBelongingsNum(String belongingsNum) {
        this.belongingsNum = belongingsNum;
    }

    public String getInOutType() {
        return inOutType;
    }

    public void setInOutType(String inOutType) {
        this.inOutType = inOutType;
    }

    public String getInstallSite() {
        return installSite;
    }

    public void setInstallSite(String installSite) {
        this.installSite = installSite;
    }

    public String getComputerCenterName() {
        return computerCenterName;
    }

    public void setComputerCenterName(String computerCenterName) {
        this.computerCenterName = computerCenterName;
    }

    public String getRealityBelongingsNum() {
        return realityBelongingsNum;
    }

    public void setRealityBelongingsNum(String realityBelongingsNum) {
        this.realityBelongingsNum = realityBelongingsNum;
    }

    public String getComputerApplyNo() {
        return computerApplyNo;
    }

    public void setComputerApplyNo(String computerApplyNo) {
        this.computerApplyNo = computerApplyNo;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }
}
