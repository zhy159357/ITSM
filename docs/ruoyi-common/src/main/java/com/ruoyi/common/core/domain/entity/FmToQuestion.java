package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【转化问题单表】对象 fm_to_question
 *
 * @author ruoyi
 * @date 2021-08-17
 */
public class FmToQuestion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private String id;

    /** 系统id */
    @Excel(name = "系统id")
    private String sysId;

    /** 应提交而未提交的问题单次数 */
    @Excel(name = "应提交而未提交的问题单次数")
    private String notCommitCount;

    /** 当月未提交事件单分析报告次数 */
    @Excel(name = "当月未提交事件单分析报告次数")
    private String notCommitReport;

    /** 问题单总数 */
    @Excel(name = "问题单总数")
    private String commitQuestionCount;

    /** 关闭总数 */
    @Excel(name = "关闭总数")
    private String closeStatusCount;

    /** 汇总时间 */
    @Excel(name = "汇总时间")
    private String dateTime;

    /** 根据分类汇总未提交的 */
    @Excel(name = "根据分类汇总未提交的")
    private String notCountType;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setSysId(String sysId)
    {
        this.sysId = sysId;
    }

    public String getSysId()
    {
        return sysId;
    }
    public void setNotCommitCount(String notCommitCount)
    {
        this.notCommitCount = notCommitCount;
    }

    public String getNotCommitCount()
    {
        return notCommitCount;
    }
    public void setNotCommitReport(String notCommitReport)
    {
        this.notCommitReport = notCommitReport;
    }

    public String getNotCommitReport()
    {
        return notCommitReport;
    }
    public void setCommitQuestionCount(String commitQuestionCount)
    {
        this.commitQuestionCount = commitQuestionCount;
    }

    public String getCommitQuestionCount()
    {
        return commitQuestionCount;
    }
    public void setCloseStatusCount(String closeStatusCount)
    {
        this.closeStatusCount = closeStatusCount;
    }

    public String getCloseStatusCount()
    {
        return closeStatusCount;
    }
    public void setDateTime(String dateTime)
    {
        this.dateTime = dateTime;
    }

    public String getDateTime()
    {
        return dateTime;
    }
    public void setNotCountType(String notCountType)
    {
        this.notCountType = notCountType;
    }

    public String getNotCountType()
    {
        return notCountType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("sysId", getSysId())
                .append("notCommitCount", getNotCommitCount())
                .append("notCommitReport", getNotCommitReport())
                .append("commitQuestionCount", getCommitQuestionCount())
                .append("closeStatusCount", getCloseStatusCount())
                .append("dateTime", getDateTime())
                .append("notCountType", getNotCountType())
                .toString();
    }
}
