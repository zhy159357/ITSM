package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * auto_itsm_resultmsg
 *
 * @author ruoyi
 * @date 2021-03-22
 */
public class AutoItsmResultmsg extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String businessId;

    private String businessNo;

    private String changeReason;

    private String startTime;

    private String endTime;

    private String status;

    private String resultMsg;

    private String resultId;

    /**关联到附件对象*/
    private Attachment attachment;

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public String getResultId() {
        return resultId;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("businessId", getBusinessId())
                .append("businessNo", getBusinessNo())
                .append("changeReason", getChangeReason())
                .append("startTime", getStartTime())
                .append("endTime", getEndTime())
                .append("status", getStatus())
                .append("resultMsg", getResultMsg())
                .append("resultId", getResultId())
                .toString();
    }
}
