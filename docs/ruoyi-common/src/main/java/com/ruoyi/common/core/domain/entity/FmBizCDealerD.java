package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class FmBizCDealerD extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Excel(name = "统计机构", sort = 1)
    private String orgName;
    @Excel(name = "统计工作组", sort = 2)
    private String groupName;
    @Excel(name = "统计人", sort = 3)
    private String pName;
    @Excel(name = "处理数量", sort = 4)
    private String dealCount;
    @Excel(name = "解决数量", sort = 5)
    private String receiveCount;

    private String notDealCount;
    private String bySjCount;

    public String getBySjCount() {
        return bySjCount;
    }

    public void setBySjCount(String bySjCount) {
        this.bySjCount = bySjCount;
    }

    public String getNotDealCount() {
        return notDealCount;
    }

    public void setNotDealCount(String notDealCount) {
        this.notDealCount = notDealCount;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getDealCount() {
        return dealCount;
    }

    public void setDealCount(String dealCount) {
        this.dealCount = dealCount;
    }

    public String getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(String receiveCount) {
        this.receiveCount = receiveCount;
    }
}
