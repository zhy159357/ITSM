package com.ruoyi.activiti.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class FmBizJJAppr extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Excel(name = "所属机构", sort = 1)
    private String orgName;
    @Excel(name = "事件单总数", sort = 2)
    private String couNum;
    @Excel(name = "紧急数量", sort = 3)
    private String jjCouNum;
    @Excel(name = "紧急占比", sort = 4)
    private String countEfficiency;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCouNum() {
        return couNum;
    }

    public void setCouNum(String couNum) {
        this.couNum = couNum;
    }

    public String getJjCouNum() {
        return jjCouNum;
    }

    public void setJjCouNum(String jjCouNum) {
        this.jjCouNum = jjCouNum;
    }

    public String getCountEfficiency() {
        return countEfficiency;
    }

    public void setCountEfficiency(String countEfficiency) {
        this.countEfficiency = countEfficiency;
    }
}
