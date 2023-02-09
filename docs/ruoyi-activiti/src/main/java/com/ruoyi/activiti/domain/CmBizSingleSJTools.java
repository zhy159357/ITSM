package com.ruoyi.activiti.domain;

import com.ruoyi.common.annotation.Excel;

import java.io.Serializable;
import java.util.Map;


public class CmBizSingleSJTools implements Serializable {

    private String startCreateTime;//开始创建时间

    private String endCreateTime;// 结束创建时间

    @Excel(name = "所属系统")
    private String caption;

    @Excel(name = "所属公司")
    private String orgName;//工具统计

    @Excel(name = "数据变更单总数")
    private String cmBizSjNum;

    @Excel(name = "使用工具的数量")
    private String isToolsNum;

    @Excel(name = "工具使用比例")
    private String countEfficiency;

    private String cmBizSjCountType;//统计类型
    /** 请求参数 */
    private Map<String, Object> params;


    public String getStartCreateTime() {
        return startCreateTime;
    }

    public void setStartCreateTime(String startCreateTime) {
        this.startCreateTime = startCreateTime;
    }

    public String getEndCreateTime() {
        return endCreateTime;
    }

    public void setEndCreateTime(String endCreateTime) {
        this.endCreateTime = endCreateTime;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCmBizSjNum() {
        return cmBizSjNum;
    }

    public void setCmBizSjNum(String cmBizSjNum) {
        this.cmBizSjNum = cmBizSjNum;
    }

    public String getIsToolsNum() {
        return isToolsNum;
    }

    public void setIsToolsNum(String isToolsNum) {
        this.isToolsNum = isToolsNum;
    }

    public String getCountEfficiency() {
        return countEfficiency;
    }

    public void setCountEfficiency(String countEfficiency) {
        this.countEfficiency = countEfficiency;
    }

    public String getCmBizSjCountType() {
        return cmBizSjCountType;
    }

    public void setCmBizSjCountType(String cmBizSjCountType) {
        this.cmBizSjCountType = cmBizSjCountType;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
