package com.ruoyi.activiti.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 采购计划｜实施审批记录表 PurchaseApproveLog
 *
 * @author ruoyi
 * @date 2021-11-22
 */
public class PurchaseApproveLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String approveLogId;

    private String changeId;

    private String purchaseId;

    /** 备注 */
    private String memo;

    /** 创建人 */
    private String createName;

    public String getApproveLogId() {
        return approveLogId;
    }

    public void setApproveLogId(String approveLogId) {
        this.approveLogId = approveLogId;
    }

    public String getChangeId() {
        return changeId;
    }

    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }
}
