package com.ruoyi.es.domain;

import com.ruoyi.common.annotation.Excel;
import java.io.Serializable;
import java.util.Map;

/**
 * @version 1.0
 */
public class KnowledgeContent implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;
    @Excel(name = "知识标题")
    private String title;
    //系统名称id
    private String sysId;
    //系统名称
    private String systemName;
    @Excel(name = "类别名称")
    private String categoryId;
    @Excel(name = "一级标题")
    private String content;
    @Excel(name = "二级标题")
    private String sectitle;
    private String threetitle;
    @Excel(name = "事件类型", readConverterExp="0=监控事件单,1=业务事件单")
    private String eventType;
    @Excel(name = "标签")
    private String name;
    @Excel(name = "状态", readConverterExp="0=草稿,1=待审核,2=已发布,3=退回修改,"
    		+ "4=初审通过,5=修改待初审,6=修改初审通过,7=作废待初审,8=作废初审通过,9=已作废" )
    private String status;
    private String createId;
    //审理理由
    private String reason;

    private String atId;
    private String categoryName;
    private String describes;
    private String updateId;
    private String updateName;
    private String updateTime;
    private String powerId;
    private String filename;
    @Excel(name = "创建人")
    private String createName;
    @Excel(name = "创建时间")
    private String createTime;

    //一线审核人
    private String oneAuditorId;
    //一线审核人
    private String oneAuditorName;
    //二线审核人
    private String twoAuditorId;
    //二线审核人
    private String twoAuditorName;

    private String contentId;

    private String sectitleId;

    /** 请求参数 */
    private Map<String, Object> params;

    //问题单单号
    private String issuefxNo;

    //问题单ID
    private String issuefxId;

    public String getPowerId() {
        return powerId;
    }

    public void setPowerId(String powerId) {
        this.powerId = powerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getAtId() {
        return atId;
    }

    public void setAtId(String atId) {
        this.atId = atId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSectitle() {
        return sectitle;
    }

    public void setSectitle(String sectitle) {
        this.sectitle = sectitle;
    }

    public String getThreetitle() {
        return threetitle;
    }

    public void setThreetitle(String threetitle) {
        this.threetitle = threetitle;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOneAuditorId() {
        return oneAuditorId;
    }

    public void setOneAuditorId(String oneAuditorId) {
        this.oneAuditorId = oneAuditorId;
    }

    public String getOneAuditorName() {
        return oneAuditorName;
    }

    public void setOneAuditorName(String oneAuditorName) {
        this.oneAuditorName = oneAuditorName;
    }

    public String getTwoAuditorId() {
        return twoAuditorId;
    }

    public void setTwoAuditorId(String twoAuditorId) {
        this.twoAuditorId = twoAuditorId;
    }

    public String getTwoAuditorName() {
        return twoAuditorName;
    }

    public void setTwoAuditorName(String twoAuditorName) {
        this.twoAuditorName = twoAuditorName;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getSectitleId() {
        return sectitleId;
    }

    public void setSectitleId(String sectitleId) {
        this.sectitleId = sectitleId;
    }

    public String getIssuefxNo() {
        return issuefxNo;
    }

    public void setIssuefxNo(String issuefxNo) {
        this.issuefxNo = issuefxNo;
    }

    public String getIssuefxId() {
        return issuefxId;
    }

    public void setIssuefxId(String issuefxId) {
        this.issuefxId = issuefxId;
    }
}