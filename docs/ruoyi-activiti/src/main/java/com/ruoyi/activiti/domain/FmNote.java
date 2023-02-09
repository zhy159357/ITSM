package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 【通知工单】对象 fm_note
 * 
 * @author ruoyi
 * @date 2021-11-29
 */
public class FmNote extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String noteId;

    /** 工单号 */
    @Excel(name = "工单号")
    private String noteNo;

    /** 通知工单名称 */
    @Excel(name = "通知工单名称")
    private String noteName;

    /** 通知类别 */
    @Excel(name = "通知类别")
    private String noteType;

    /** 通知时间 */
    @Excel(name = "通知时间")
    private String noteTime;

    /** 是否反馈 */
    @Excel(name = "是否反馈")
    private String ifFeedback;

    /** 反馈截止时间 */
    @Excel(name = "反馈截止时间")
    private String feedbackAbortTime;

    /** 正文 */
    @Excel(name = "正文")
    private String square;

    /** 附件（默认不需要字段） */
    @Excel(name = "附件", readConverterExp = "默=认不需要字段")
    private String filePath;

    /** 处置超时时间  默认未超时，在反馈截止时间未反馈的  记录超时时间。 */
    @Excel(name = "处置超时时间  默认未超时，在反馈截止时间未反馈的  记录超时时间。")
    private String disposeTime;

    /** 反馈时间 */
    @Excel(name = "反馈时间")
    private String feedbackTime;

    /** 处置单位（当前处置人所属单位名称） */
    @Excel(name = "处置单位", readConverterExp = "当=前处置人所属单位名称")
    private String orgid;

    /** 接收状态（处置人点击接收后可查看工单内容，不点击不可以看工单内容，同事我们展示上显示相关分行的状态里面显示已接收） */
    @Excel(name = "接收状态", readConverterExp = "处=置人点击接收后可查看工单内容，不点击不可以看工单内容，同事我们展示上显示相关分行的状态里面显示已接收")
    private String acceptingState;

    /** 处置状态：处置中/处置完成 */
    @Excel(name = "处置状态：处置中/处置完成")
    private String disposeState;

    /** 处置人（当前处置人） */
    @Excel(name = "处置人", readConverterExp = "当=前处置人")
    private String disposeId;

    /** 是否涉及：是/否 */
    @Excel(name = "是否涉及：是/否")
    private String ifInvolve;

    /** 反馈详情 */
    @Excel(name = "反馈详情")
    private String feedbackDetail;

    /** $column.columnComment */
    private String n1;

    /** $column.columnComment */
    private String n2;

    /** $column.columnComment */
    private String n3;

    /** $column.columnComment */
    private String n4;

    /** $column.columnComment */
    private String n5;

    /** 创建时间 */
    private String startTime;

    /** 工单状态 */
    private String noteState;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getNoteState() {
        return noteState;
    }

    public void setNoteState(String noteState) {
        this.noteState = noteState;
    }

    public void setNoteId(String noteId)
    {
        this.noteId = noteId;
    }

    public String getNoteId() 
    {
        return noteId;
    }
    public void setNoteNo(String noteNo) 
    {
        this.noteNo = noteNo;
    }

    public String getNoteNo() 
    {
        return noteNo;
    }
    public void setNoteName(String noteName) 
    {
        this.noteName = noteName;
    }

    public String getNoteName() 
    {
        return noteName;
    }
    public void setNoteType(String noteType) 
    {
        this.noteType = noteType;
    }

    public String getNoteType() 
    {
        return noteType;
    }
    public void setNoteTime(String noteTime) 
    {
        this.noteTime = noteTime;
    }

    public String getNoteTime() 
    {
        return noteTime;
    }
    public void setIfFeedback(String ifFeedback) 
    {
        this.ifFeedback = ifFeedback;
    }

    public String getIfFeedback() 
    {
        return ifFeedback;
    }
    public void setFeedbackAbortTime(String feedbackAbortTime) 
    {
        this.feedbackAbortTime = feedbackAbortTime;
    }

    public String getFeedbackAbortTime() 
    {
        return feedbackAbortTime;
    }
    public void setSquare(String square) 
    {
        this.square = square;
    }

    public String getSquare() 
    {
        return square;
    }
    public void setFilePath(String filePath) 
    {
        this.filePath = filePath;
    }

    public String getFilePath() 
    {
        return filePath;
    }
    public void setDisposeTime(String disposeTime) 
    {
        this.disposeTime = disposeTime;
    }

    public String getDisposeTime() 
    {
        return disposeTime;
    }
    public void setFeedbackTime(String feedbackTime) 
    {
        this.feedbackTime = feedbackTime;
    }

    public String getFeedbackTime() 
    {
        return feedbackTime;
    }
    public void setOrgid(String orgid) 
    {
        this.orgid = orgid;
    }

    public String getOrgid() 
    {
        return orgid;
    }
    public void setAcceptingState(String acceptingState) 
    {
        this.acceptingState = acceptingState;
    }

    public String getAcceptingState() 
    {
        return acceptingState;
    }
    public void setDisposeState(String disposeState) 
    {
        this.disposeState = disposeState;
    }

    public String getDisposeState() 
    {
        return disposeState;
    }
    public void setDisposeId(String disposeId) 
    {
        this.disposeId = disposeId;
    }

    public String getDisposeId() 
    {
        return disposeId;
    }
    public void setIfInvolve(String ifInvolve) 
    {
        this.ifInvolve = ifInvolve;
    }

    public String getIfInvolve() 
    {
        return ifInvolve;
    }
    public void setFeedbackDetail(String feedbackDetail) 
    {
        this.feedbackDetail = feedbackDetail;
    }

    public String getFeedbackDetail() 
    {
        return feedbackDetail;
    }
    public void setN1(String n1) 
    {
        this.n1 = n1;
    }

    public String getN1() 
    {
        return n1;
    }
    public void setN2(String n2) 
    {
        this.n2 = n2;
    }

    public String getN2() 
    {
        return n2;
    }
    public void setN3(String n3) 
    {
        this.n3 = n3;
    }

    public String getN3() 
    {
        return n3;
    }
    public void setN4(String n4) 
    {
        this.n4 = n4;
    }

    public String getN4() 
    {
        return n4;
    }
    public void setN5(String n5) 
    {
        this.n5 = n5;
    }

    public String getN5() 
    {
        return n5;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("noteId", getNoteId())
            .append("noteNo", getNoteNo())
            .append("noteName", getNoteName())
            .append("noteType", getNoteType())
            .append("noteTime", getNoteTime())
            .append("ifFeedback", getIfFeedback())
            .append("feedbackAbortTime", getFeedbackAbortTime())
            .append("square", getSquare())
            .append("filePath", getFilePath())
            .append("disposeTime", getDisposeTime())
            .append("feedbackTime", getFeedbackTime())
            .append("orgid", getOrgid())
            .append("acceptingState", getAcceptingState())
            .append("disposeState", getDisposeState())
            .append("disposeId", getDisposeId())
            .append("ifInvolve", getIfInvolve())
            .append("feedbackDetail", getFeedbackDetail())
            .append("n1", getN1())
            .append("n2", getN2())
            .append("n3", getN3())
            .append("n4", getN4())
            .append("n5", getN5())
            .toString();
    }
}
