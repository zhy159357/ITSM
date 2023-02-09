package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 公告通知对象 数据中心专用 am_biz_notice
 *
 * @author dongdongLiu
 * @date 2021-10-19
 */
public class AmBizNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 公告ID */
    private String amBizId;

    /** 公告编码 */
    @Excel(name = "公告编码")
    private String amCode;

    /** 审核人id）*/
    private String checkerId;

    /** 创建人 */
    private String createId;

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

    /** 发送范围 ....*/
    private String sendRange; //1 数据中心 2 厂商+数据中心（72号院） 3 所有用户

    /** 公告标题 */
    @Excel(name = "公告标题")
    private String amTitle;

    /** 状态 01新建、02待审核、03=退回,04=待阅,05=已过期*/
    @Excel(name = "状态", readConverterExp = "01=新建,02=待审核,03=退回,04=待阅,05=已过期")
    private String currentStatus;

    /** 发布人*/
    @Excel(name = "发布人")
    private String pName;

    /** 发布机构*/
    @Excel(name = "发布机构")
    private String orgName;

    //接收范围
    private String[] sendRanges;

    //创建时间
    private String addTime;

    //审核人名称
    private String checkerName;

    //是否置顶  0 否 1 是
    private String isTop;

    //置顶时间flag 回显用 1 一天 /2 三天 /3 一周 /4 一月 /5 长期
    private String topTimeFlag;

    //置顶时间
    private String topTime;

    //公告下线类型 (1、一天 2、一周 3、一个月 4、三个月 5、六个月 6、一年)
    private String amOffline;

    //公告下线时间
    private String amOfflineTime;

    //工作台标记<下线时间>
    private String tag;

    public void setAmBizId(String amBizId)
    {
        this.amBizId = amBizId;
    }

    public String getAmBizId()
    {
        return amBizId;
    }

    public String getAmCode() {
        return amCode;
    }

    public void setAmCode(String amCode) {
        this.amCode = amCode;
    }

    public void setCheckerId(String checkerId)
    {
        this.checkerId = checkerId;
    }

    public String getCheckerId()
    {
        return checkerId;
    }

    public void setCreateId(String createId)
    {
        this.createId = createId;
    }

    public String getCreateId()
    {
        return createId;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setReleaseDate(String releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }
    public void setReleaseOrg(String releaseOrg)
    {
        this.releaseOrg = releaseOrg;
    }

    public String getReleaseOrg()
    {
        return releaseOrg;
    }
    public void setCheckerFlag(String checkerFlag)
    {
        this.checkerFlag = checkerFlag;
    }

    public String getCheckerFlag()
    {
        return checkerFlag;
    }
    public void setCheckerAdvice(String checkerAdvice)
    {
        this.checkerAdvice = checkerAdvice;
    }

    public String getCheckerAdvice()
    {
        return checkerAdvice;
    }
    public void setInvalidationMark(String invalidationMark)
    {
        this.invalidationMark = invalidationMark;
    }

    public String getInvalidationMark()
    {
        return invalidationMark;
    }
    public void setCheckerTime(String checkerTime)
    {
        this.checkerTime = checkerTime;
    }

    public String getCheckerTime()
    {
        return checkerTime;
    }

    public void setSendRange(String sendRange)
    {
        this.sendRange = sendRange;
    }

    public String getSendRange()
    {
        return sendRange;
    }
    public void setAmTitle(String amTitle)
    {
        this.amTitle = amTitle;
    }

    public String getAmTitle()
    {
        return amTitle;
    }
    public void setCurrentStatus(String currentStatus)
    {
        this.currentStatus = currentStatus;
    }

    public String getCurrentStatus()
    {
        return currentStatus;
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

    public String[] getSendRanges() {
        return sendRanges;
    }

    public void setSendRanges(String[] sendRanges) {
        this.sendRanges = sendRanges;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getCheckerName() {
        return checkerName;
    }

    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getTopTimeFlag() {
        return topTimeFlag;
    }

    public void setTopTimeFlag(String topTimeFlag) {
        this.topTimeFlag = topTimeFlag;
    }

    public String getTopTime() {
        return topTime;
    }

    public void setTopTime(String topTime) {
        this.topTime = topTime;
    }

    public String getAmOffline() {
        return amOffline;
    }

    public void setAmOffline(String amOffline) {
        this.amOffline = amOffline;
    }

    public String getAmOfflineTime() {
        return amOfflineTime;
    }

    public void setAmOfflineTime(String amOfflineTime) {
        this.amOfflineTime = amOfflineTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
