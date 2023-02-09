package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 公告接收日志表 AM_PRO_ACTLOG
 *
 * @author ruoyi
 */
public class SysNoticeReceiveLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** ID */
    private String amActlogId;

    /** 公告接收ID */
    private String amReceiveId;

    /** 执行人id */
    private String performerId;

    /** 描述 */
    private String description;

    /** 接收人*/
    @Excel(name = "接收人")
    private String pName;

    /** 接受机构*/
    @Excel(name = "接受机构")
    private String orgName;

    /** 接收工作组*/
    @Excel(name = "接收工作组")
    private String groupName;

    /** 联系电话*/
    @Excel(name = "联系电话")
    private String phone;

    /** 更新时间 */
    @Excel(name = "更新时间")
    private String editTime;

    /** 接收状态 */
    @Excel(name = "接收状态")
    private String currentState;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAmActlogId() {
        return amActlogId;
    }

    public void setAmActlogId(String amActlogId) {
        this.amActlogId = amActlogId;
    }

    public String getAmReceiveId() {
        return amReceiveId;
    }

    public void setAmReceiveId(String amReceiveId) {
        this.amReceiveId = amReceiveId;
    }


    public String getPerformerId() {
        return performerId;
    }

    public void setPerformerId(String performerId) {
        this.performerId = performerId;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
