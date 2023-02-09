package com.ruoyi.activiti.domain;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 采购当前进度表 purchase_current_progress
 *
 * @author ruoyi
 * @date 2021-11-22
 */
public class PurchaseCurrentProgress extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String currentProgressId;

    private String purchaseId;

    /** 当前进度 */
    private String currentProgress;

    /** 风险提示 */
    private String riskWarning;

    /** 备注 */
    private String memo;

    public String getCurrentProgressId() {
        return currentProgressId;
    }

    public void setCurrentProgressId(String currentProgressId) {
        this.currentProgressId = currentProgressId;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(String currentProgress) {
        this.currentProgress = currentProgress;
    }

    public String getRiskWarning() {
        return riskWarning;
    }

    public void setRiskWarning(String riskWarning) {
        this.riskWarning = riskWarning;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
