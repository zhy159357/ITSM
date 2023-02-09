package com.ruoyi.activiti.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class BgJSEfficiency extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Excel(name="所属处室",sort = 1)
    private String orgname;
    @Excel(name="变更单总数",sort = 2)
    private String cmBizBgNum;
    @Excel(name="及时变更单总数",sort = 3)
    private String isJSNum;
    @Excel(name="不及时变更单总数",sort = 4)
    private String isNotJSNum;
    @Excel(name="及时变更百分比",sort = 5)
    private String countEfficiency;

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getCmBizBgNum() {
        return cmBizBgNum;
    }

    public void setCmBizBgNum(String cmBizBgNum) {
        this.cmBizBgNum = cmBizBgNum;
    }

    public String getIsJSNum() {
        return isJSNum;
    }

    public void setIsJSNum(String isJSNum) {
        this.isJSNum = isJSNum;
    }

    public String getIsNotJSNum() {
        return isNotJSNum;
    }

    public void setIsNotJSNum(String isNotJSNum) {
        this.isNotJSNum = isNotJSNum;
    }

    public String getCountEfficiency() {
        return countEfficiency;
    }

    public void setCountEfficiency(String countEfficiency) {
        this.countEfficiency = countEfficiency;
    }
}
