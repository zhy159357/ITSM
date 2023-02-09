package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * automate_middle
 *
 * @author ruoyi
 * @date 2021-03-27
 */
public class AutomateMiddle extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /*校验成功*/
    public static final String AUTO_STATUS_OK = "1";
    /*校验失败*/
    public static final String AUTO_STATUS_FAIL = "9";
    /*待校验*/
    public static final String AUTO_STATUS_LOADING = "0";

    /**
     * 主键ID
     */
    private String autoId;

    /**
     * 业务表主键ID
     */
    private String businessId;

    /**
     * 业务表单号
     */
    private String businessNo;

    /**
     * 自动化校验标识，0-代表未校验 9-校验失败，1-代表校验通过
     */
    private String status;

    /**
     * 校验次数，最大校验三次，三次都失败则定时任务不再校验
     */
    private Integer checkCount;

    public void setAutoId(String autoId) {
        this.autoId = autoId;
    }

    public String getAutoId() {
        return autoId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Integer getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(Integer checkCount) {
        this.checkCount = checkCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("autoId", getAutoId())
                .append("businessId", getBusinessId())
                .append("businessNo", getBusinessNo())
                .append("status", getStatus())
                .append("checkCount", getCheckCount())
                .toString();
    }

    public void addCheckCount() {
        if (this.checkCount == 0) {
            this.checkCount = 0;
        }
        this.checkCount++;
        if (this.checkCount > 2) {
            this.status = AUTO_STATUS_FAIL;
        } else {
            this.status = AUTO_STATUS_LOADING;
        }
    }
}
