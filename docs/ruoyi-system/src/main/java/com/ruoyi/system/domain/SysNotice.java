package com.ruoyi.system.domain;

import javax.validation.constraints.*;

import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Arrays;

/**
 * 通知公告表 AM_BIZ_ANNOUNCEMENT
 *
 * @author ruoyi
 */
public class SysNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 公告编号 */
    private String amBizId;

    /** 公告编码 */
    @Excel(name = "公告编码")
    private String amCode;

    /** 公告类型（0执行件 1阅件） */
    @Excel(name = "公告类型", readConverterExp = "0=执行件,1=阅件")
    private String amType;

    /** 创建时间 */
    @Excel(name = "创建时间")
    private String addTime;

    /** 审核人id）*/
    private String checkerId;

    /** 审核人名称）*/
    private String checkerName;

    /** 处理类型------- */
    private String amDealType;

    /** 联系电话 */
    private String contactPhone;

    /** 其他联系电话 */
    private String otherPhone;

    /** 创建人 */
    private String createId;

    /** 等级 */
    private String urgentLev;

    /** 密级 */
    private String secretLev;

    /** 摘要 */
    private String summry;

    /** 描述 */
    private String description;

    /** 发布时间 */
    @Excel(name = "发布时间")
    private String releaseDate;

    /** 发布机构 */
    private String releaseOrg;

    /** 审核标志------ */
    private String checkerFlag;

    /** 审核意见 */
    private String checkerAdvice;

    /** 有效标志------- */
    private String invalidationMark;

    /** 审核时间 */
    private String checkerTime;

    /** 是否发送短信------ */
    private String sms;

    /** 发送范围 ....*/
    private String sendRange;

    /** 接收部门id */
    private String receiveDeptId;

    /** 接收角色id */
    private String receiveRoleId;

    /** 接收部门名 */
    private String receiveDeptName;

    /** 接收角色名称 */
    private String receiveRoleName;

    /** 接收工作组id */
    private String receiveGroupId;

    /** 接收工作组名称 */
    private String receiveGroupName;

    /** 公告标题 */
    @Excel(name = "公告标题")
    private String amTitle;

    /** 状态 01新建、02待审核、03退回、04待处理、05处理中、06处理完毕、07作废*/
    @Excel(name = "状态", readConverterExp = "01=新建,02=待审核,03=退回,04=待处理,05=处理中,06=处理完毕,07=作废")
    private String currentStatus;

    /** 结束时间 */
    private String endTime;

    /** 处理进度 */
    @Excel(name = "处理进度")
    private String dealSchdule;

    /** 我的标识 0我接收的 1我创建的  默认0 */
    private String myIdentity;

    /** 接收机构/工作组 */
    private String receiveDeptGroupId;

    /** 接收机构/工作组 */
    private String receiveDeptGroupName;

    /** 接收岗位IDs */
    private String[] postId;

    /** 接收机构ID */
    private String orgId;

    /** 接收工作组Is */
    private String groupId;

    /** 我接收的公告IDs */
    private String[] amBizIds;

    /** 发布人*/
    @Excel(name = "发布人")
    private String pName;

    /** 发布机构*/
    @Excel(name = "发布机构")
    private String orgName;

    /** 查询公告列表式用到的范围状态noticeStatus*/
    private String noticeStatus;

    /** 接收岗位下拉名称 */
    private String postName;

    /** rid角色id */
    private String rId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAmBizId() {
        return amBizId;
    }

    public void setAmBizId(String amBizId) {
        this.amBizId = amBizId;
    }

    public String getAmCode() {
        return amCode;
    }

    public void setAmCode(String amCode) {
        this.amCode = amCode;
    }

    public String getAmType() {
        return amType;
    }

    public void setAmType(String amType) {
        this.amType = amType;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getCheckerId() {
        return checkerId;
    }

    public void setCheckerId(String checkerId) {
        this.checkerId = checkerId;
    }

    public String getCheckerName() {
        return checkerName;
    }

    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }

    public String getAmDealType() {
        return amDealType;
    }

    public void setAmDealType(String amDealType) {
        this.amDealType = amDealType;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getOtherPhone() {
        return otherPhone;
    }

    public void setOtherPhone(String otherPhone) {
        this.otherPhone = otherPhone;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getUrgentLev() {
        return urgentLev;
    }

    public void setUrgentLev(String urgentLev) {
        this.urgentLev = urgentLev;
    }

    public String getSecretLev() {
        return secretLev;
    }

    public void setSecretLev(String secretLev) {
        this.secretLev = secretLev;
    }

    public String getSummry() {
        return summry;
    }

    public void setSummry(String summry) {
        this.summry = summry;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseOrg() {
        return releaseOrg;
    }

    public void setReleaseOrg(String releaseOrg) {
        this.releaseOrg = releaseOrg;
    }

    public String getCheckerFlag() {
        return checkerFlag;
    }

    public void setCheckerFlag(String checkerFlag) {
        this.checkerFlag = checkerFlag;
    }

    public String getCheckerAdvice() {
        return checkerAdvice;
    }

    public void setCheckerAdvice(String checkerAdvice) {
        this.checkerAdvice = checkerAdvice;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getCheckerTime() {
        return checkerTime;
    }

    public void setCheckerTime(String checkerTime) {
        this.checkerTime = checkerTime;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getSendRange() {
        return sendRange;
    }

    public void setSendRange(String sendRange) {
        this.sendRange = sendRange;
    }

    public String getReceiveDeptId() {
        return receiveDeptId;
    }

    public void setReceiveDeptId(String receiveDeptId) {
        this.receiveDeptId = receiveDeptId;
    }

    public String getReceiveRoleId() {
        return receiveRoleId;
    }

    public void setReceiveRoleId(String receiveRoleId) {
        this.receiveRoleId = receiveRoleId;
    }

    public String getReceiveDeptName() {
        return receiveDeptName;
    }

    public void setReceiveDeptName(String receiveDeptName) {
        this.receiveDeptName = receiveDeptName;
    }

    public String getReceiveRoleName() {
        return receiveRoleName;
    }

    public void setReceiveRoleName(String receiveRoleName) {
        this.receiveRoleName = receiveRoleName;
    }

    public String getReceiveGroupId() {
        return receiveGroupId;
    }

    public void setReceiveGroupId(String receiveGroupId) {
        this.receiveGroupId = receiveGroupId;
    }

    public String getReceiveGroupName() {
        return receiveGroupName;
    }

    public void setReceiveGroupName(String receiveGroupName) {
        this.receiveGroupName = receiveGroupName;
    }

    public String getAmTitle() {
        return amTitle;
    }

    public void setAmTitle(String amTitle) {
        this.amTitle = amTitle;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDealSchdule() {
        return dealSchdule;
    }

    public void setDealSchdule(String dealSchdule) {
        this.dealSchdule = dealSchdule;
    }

    public String getMyIdentity() {
        return myIdentity;
    }

    public void setMyIdentity(String myIdentity) {
        this.myIdentity = myIdentity;
    }

    public String getReceiveDeptGroupId() {
        return receiveDeptGroupId;
    }

    public void setReceiveDeptGroupId(String receiveDeptGroupId) {
        this.receiveDeptGroupId = receiveDeptGroupId;
    }

    public String getReceiveDeptGroupName() {
        return receiveDeptGroupName;
    }

    public void setReceiveDeptGroupName(String receiveDeptGroupName) {
        this.receiveDeptGroupName = receiveDeptGroupName;
    }

    public String[] getPostId() {
        return postId;
    }

    public void setPostId(String[] postId) {
        this.postId = postId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String[] getAmBizIds() {
        return amBizIds;
    }

    public void setAmBizIds(String[] amBizIds) {
        this.amBizIds = amBizIds;
    }


    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getNoticeStatus() {
        return noticeStatus;
    }

    public void setNoticeStatus(String noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    @Override
    public String toString() {
        return "SysNotice{" +
                "amBizId='" + amBizId + '\'' +
                ", amCode='" + amCode + '\'' +
                ", amType='" + amType + '\'' +
                ", addTime='" + addTime + '\'' +
                ", checkerId='" + checkerId + '\'' +
                ", checkerName='" + checkerName + '\'' +
                ", amDealType='" + amDealType + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", otherPhone='" + otherPhone + '\'' +
                ", createId='" + createId + '\'' +
                ", urgentLev='" + urgentLev + '\'' +
                ", secretLev='" + secretLev + '\'' +
                ", summry='" + summry + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", releaseOrg='" + releaseOrg + '\'' +
                ", checkerFlag='" + checkerFlag + '\'' +
                ", checkerAdvice='" + checkerAdvice + '\'' +
                ", invalidationMark='" + invalidationMark + '\'' +
                ", checkerTime='" + checkerTime + '\'' +
                ", sms='" + sms + '\'' +
                ", sendRange='" + sendRange + '\'' +
                ", receiveDeptId='" + receiveDeptId + '\'' +
                ", receiveRoleId='" + receiveRoleId + '\'' +
                ", receiveDeptName='" + receiveDeptName + '\'' +
                ", receiveRoleName='" + receiveRoleName + '\'' +
                ", receiveGroupId='" + receiveGroupId + '\'' +
                ", receiveGroupName='" + receiveGroupName + '\'' +
                ", amTitle='" + amTitle + '\'' +
                ", currentStatus='" + currentStatus + '\'' +
                ", endTime='" + endTime + '\'' +
                ", dealSchdule='" + dealSchdule + '\'' +
                ", myIdentity='" + myIdentity + '\'' +
                ", receiveDeptGroupId='" + receiveDeptGroupId + '\'' +
                ", receiveDeptGroupName='" + receiveDeptGroupName + '\'' +
                ", postId=" + Arrays.toString(postId) +
                ", orgId='" + orgId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", amBizIds=" + Arrays.toString(amBizIds) +
                '}';
    }
}
