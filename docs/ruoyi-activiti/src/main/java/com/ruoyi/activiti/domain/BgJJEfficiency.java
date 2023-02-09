package com.ruoyi.activiti.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class BgJJEfficiency extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Excel(name="所属处室",sort = 1)
    private String orgname;
    @Excel(name="变更单总数",sort = 2)
    private String cmBizBgNum;
    @Excel(name="紧急变更单总数",sort = 3)
    private String isJJNum;
    @Excel(name="有效紧急变更单总数",sort = 4)
    private String isValidJJNum;
    @Excel(name="紧急变更百分比",sort = 5)
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

    public String getIsJJNum() {
        return isJJNum;
    }

    public void setIsJJNum(String isJJNum) {
        this.isJJNum = isJJNum;
    }

    public String getIsValidJJNum() {
        return isValidJJNum;
    }

    public void setIsValidJJNum(String isValidJJNum) {
        this.isValidJJNum = isValidJJNum;
    }

    public String getCountEfficiency() {
        return countEfficiency;
    }

    public void setCountEfficiency(String countEfficiency) {
        this.countEfficiency = countEfficiency;
    }

}
