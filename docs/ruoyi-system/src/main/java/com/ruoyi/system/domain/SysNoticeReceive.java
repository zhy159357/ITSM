package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 公告接收表 AM_BIZ_RECEIVE
 *
 * @author ruoyi
 */
public class SysNoticeReceive  extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 公告接收id */
    private String amReceiveId;

    /** 公告编号 */
    private String amBizId;

    /** 接收机构 */
    private String receiveDept;

    /** 接收工作组 */
    private String receiveGroup;

    /** 接收时间 */
    private String receiveTime;

    /** 是否接收 */
    private String ifReceive;

    /** 接收响应 */
    private String receiveReply;

    /** 无效标记 */
    private String invalidationMark;

    /** 更新时间 */
    private String editTime;

    /** 接收人员列表 */
    private String receivePersonList;

    /** 处理人 */
    private String dealer;

    /** 处理人名称 */
    private String dealerName;

    /*处理人电话*/
    private String dealerPhone;

    /*处理人机构*/
    private String dealerDept;

    /** 接收公告Id*/
    private String[] amBizIds;

    /** 公告编号 */
    @Excel(name = "公告编号")
    private String amCode;

    /** 公告标题 */
    @Excel(name = "公告标题")
    private String amTitle;

    /** 状态 01新建、02待审核、03退回、04待处理、05处理中、06处理完毕、07作废*/
    private String currentStatus;

    /** 发布时间 */
    @Excel(name = "发布时间")
    private String releaseDate;

    /** 发布机构 */
    @Excel(name = "发布机构")
    private String orgName;

    /** 创建人 */
    @Excel(name = "创建人")
    private String pName;

    /** 接收机构名称 */
    @Excel(name = "接收机构")
    private String receiveDeptName;

    /** 接受工作组名称 */
    @Excel(name = "接受工作组")
    private String receiveGroupName;

    /** 等级 */
    private String urgentLev;

    /** 密级 */
    private String secretLev;

    /** 摘要 */
    private String summry;

    /** 描述 */
    private String description;

    /** 公告类型（0执行件 1阅件） */
    private String amType;

    /** 联系电话 */
    private String contactPhone;

    /** 其他联系电话 */
    private String otherPhone;

    /** 是否发送短信------ */
    private String sms;

    /** 发送范围 ....*/
    private String sendRange;

    /** 公告通知表中接收机构list ....*/
    private String noticeDeptName;

    /** 公告通知表中接收工作组list ....*/
    private String noticeGroupName;

    /** 接收机构/工作组名称 */
    private String receiveDeptGroupName;

    /** 创建时间 */
    private String addTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAmReceiveId() {
        return amReceiveId;
    }

    public void setAmReceiveId(String amReceiveId) {
        this.amReceiveId = amReceiveId;
    }

    public String getAmBizId() {
        return amBizId;
    }

    public void setAmBizId(String amBizId) {
        this.amBizId = amBizId;
    }

    public String getReceiveDept() {
        return receiveDept;
    }

    public void setReceiveDept(String receiveDept) {
        this.receiveDept = receiveDept;
    }

    public String getReceiveGroup() {
        return receiveGroup;
    }

    public void setReceiveGroup(String receiveGroup) {
        this.receiveGroup = receiveGroup;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getIfReceive() {
        return ifReceive;
    }

    public void setIfReceive(String ifReceive) {
        this.ifReceive = ifReceive;
    }

    public String getReceiveReply() {
        return receiveReply;
    }

    public void setReceiveReply(String receiveReply) {
        this.receiveReply = receiveReply;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getReceivePersonList() {
        return receivePersonList;
    }

    public void setReceivePersonList(String receivePersonList) {
        this.receivePersonList = receivePersonList;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String[] getAmBizIds() {
        return amBizIds;
    }

    public void setAmBizIds(String[] amBizIds) {
        this.amBizIds = amBizIds;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getAmCode() {
        return amCode;
    }

    public void setAmCode(String amCode) {
        this.amCode = amCode;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getReceiveDeptName() {
        return receiveDeptName;
    }

    public void setReceiveDeptName(String receiveDeptName) {
        this.receiveDeptName = receiveDeptName;
    }

    public String getReceiveGroupName() {
        return receiveGroupName;
    }

    public void setReceiveGroupName(String receiveGroupName) {
        this.receiveGroupName = receiveGroupName;
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

    public String getAmType() {
        return amType;
    }

    public void setAmType(String amType) {
        this.amType = amType;
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

    public String getNoticeDeptName() {
        return noticeDeptName;
    }

    public void setNoticeDeptName(String noticeDeptName) {
        this.noticeDeptName = noticeDeptName;
    }

    public String getNoticeGroupName() {
        return noticeGroupName;
    }

    public void setNoticeGroupName(String noticeGroupName) {
        this.noticeGroupName = noticeGroupName;
    }

    public String getReceiveDeptGroupName() {
        return receiveDeptGroupName;
    }

    public void setReceiveDeptGroupName(String receiveDeptGroupName) {
        this.receiveDeptGroupName = receiveDeptGroupName;
    }

    public String getDealerPhone() {
        return dealerPhone;
    }

    public void setDealerPhone(String dealerPhone) {
        this.dealerPhone = dealerPhone;
    }

    public String getDealerDept() {
        return dealerDept;
    }

    public void setDealerDept(String dealerDept) {
        this.dealerDept = dealerDept;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "SysNoticeReceive{" +
                "amReceiveId='" + amReceiveId + '\'' +
                ", amBizId='" + amBizId + '\'' +
                ", receiveDept='" + receiveDept + '\'' +
                ", receiveGroup='" + receiveGroup + '\'' +
                ", receiveTime='" + receiveTime + '\'' +
                ", ifReceive='" + ifReceive + '\'' +
                ", receiveReply='" + receiveReply + '\'' +
                ", invalidationMark='" + invalidationMark + '\'' +
                ", editTime='" + editTime + '\'' +
                ", receivePersonList='" + receivePersonList + '\'' +
                ", dealer='" + dealer + '\'' +
                '}';
    }
}
