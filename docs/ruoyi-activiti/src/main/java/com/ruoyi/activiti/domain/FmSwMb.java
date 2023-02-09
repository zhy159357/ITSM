package com.ruoyi.activiti.domain;

import java.io.Serializable;
import java.util.Map;

import com.ruoyi.common.annotation.Excel;

public class FmSwMb implements Serializable {

    private static final long serialVersionUID = 1L;
    private String swmdId;
    private String createId;

    private String fileStoreId;
    @Excel(name = "模板标题")
    private String swmbTitle;

    @Excel(name = "请求事项")
    private String faultKind;

    @Excel(name = "模板说明")
    private String memo;

    private String createOrgId;

    private String dealOrgId;
    private String content;
    private String dealPId;

    @Excel(name = "创建人")
    private String pname;

    @Excel(name = "创建机构")
    private String createorg;

    @Excel(name = "受理处室")
    private String dealorg;
    private String startCreateTime;
    private String endCreateTime;
    private String createTime;

    @Excel(name = "创建时间")
    private String createTimeText;

    private String levelCode;

    //审批流状态 1为三步流程 2为五步流程
    private String processStatus;

    /** 请求参数 */
    private Map<String, Object> params;
    public String getSwmdId() {
        return swmdId;
    }

    public void setSwmdId(String swmdId) {
        this.swmdId = swmdId;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFileStoreId() {
        return fileStoreId;
    }

    public void setFileStoreId(String fileStoreId) {
        this.fileStoreId = fileStoreId;
    }

    public String getSwmbTitle() {
        return swmbTitle;
    }

    public void setSwmbTitle(String swmbTitle) {
        this.swmbTitle = swmbTitle;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCreateOrgId() {
        return createOrgId;
    }

    public void setCreateOrgId(String createOrgId) {
        this.createOrgId = createOrgId;
    }

    public String getFaultKind() {
        return faultKind;
    }

    public void setFaultKind(String faultKind) {
        this.faultKind = faultKind;
    }

    public String getDealOrgId() {
        return dealOrgId;
    }

    public void setDealOrgId(String dealOrgId) {
        this.dealOrgId = dealOrgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDealPId() {
        return dealPId;
    }

    public void setDealPId(String dealPId) {
        this.dealPId = dealPId;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getCreateorg() {
        return createorg;
    }

    public void setCreateorg(String createorg) {
        this.createorg = createorg;
    }

    public String getDealorg() {
        return dealorg;
    }

    public void setDealorg(String dealorg) {
        this.dealorg = dealorg;
    }

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

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getCreateTimeText() {
        return createTimeText;
    }

    public void setCreateTimeText(String createTimeText) {
        this.createTimeText = createTimeText;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }
}
