package com.ruoyi.common.core.domain.entity;

import java.io.Serializable;


/**
 * 短信處理數據对象
 *
 * @author ruoyi
 * @date 2020-12-08
 */
public class SmsPreObj implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String smstype;//短信类型
    private String telephone;//手机号
    private String name;//名称
    private String smstext;//短信内容
    private String inspecttime;//检查时间
    private String obj;//检查对象
    private String objid;//检查对象ID
    private String inspecttext;//检查内容
    private OgPerson createrid;//创建人
    private String updatetime;//更新时间
    private String state;//状态
    private String status;//状态
    private String vfCode;//校验码
    private String taskId;//流程ID
    private String dealStatus;//任务处理状态
    private String mark;//有效标志

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSmstype() {
        return smstype;
    }

    public void setSmstype(String smstype) {
        this.smstype = smstype;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmstext() {
        return smstext;
    }

    public void setSmstext(String smstext) {
        this.smstext = smstext;
    }

    public String getInspecttime() {
        return inspecttime;
    }

    public void setInspecttime(String inspecttime) {
        this.inspecttime = inspecttime;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getObjid() {
        return objid;
    }

    public void setObjid(String objid) {
        this.objid = objid;
    }

    public String getInspecttext() {
        return inspecttext;
    }

    public void setInspecttext(String inspecttext) {
        this.inspecttext = inspecttext;
    }

    public OgPerson getCreaterid() {
        return createrid;
    }

    public void setCreaterid(OgPerson createrid) {
        this.createrid = createrid;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getVfCode() {
        return vfCode;
    }

    public void setVfCode(String vfCode) {
        this.vfCode = vfCode;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
