package com.ruoyi.activiti.domain;

import com.ruoyi.common.annotation.Excel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 舆情监测系统巡检单 patrol_inspection
 */
public class PatrolInspection implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 巡检单Id
     */
    @Excel(name = "巡检单编号")
    private String patrolId;

    /**
     * 应用系统名称版本
     */
    @Excel(name = "应用系统名称版本")
    private String patrolName;

    private String sysId;

    /**
     * 应用系统运维单位
     */
    private String patrolDepartname;

    /**
     * 巡查人员
     */
    @Excel(name = "巡查人员")
    private String createName;

    /**
     * 联系方式
     */
    private String telePhone;

    /**
     * 系统负责人
     */
    @Excel(name = "系统负责人")
    private String leaderName;

    /**
     * 巡检项最后更新日期
     */
    @Excel(name = "巡检项最后更新日期")
    private String endUpTime;

    /**
     * 开始时间
     */
    @Excel(name = "开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @Excel(name = "结束时间")
    private String endTime;

    /**
     * 网闸数据同步巡查结果
     */
    private String syncData;

    /**
     * 系统服务器状态
     */
    private String sysServerData;

    /**
     * 首页展示
     */
    private String pageData;

    /**
     * 门户热搜词展示
     */
    private String hotSearchData;

    /**
     * 突发预警展示
     */
    private String emergencyData;

    /**
     * 热点话题展示
     */
    private String hotTopicData;

    /**
     * 热点文章展示
     */
    private String hotEssayData;

    /**
     * 内容监测展示
     */
    private String contentData;

    /**
     * 专题分析展示
     */
    private String specialData;

    /**
     * 信息检索展示
     */
    private String informationData;

    /**
     * 备注
     */
    private String remark;

    private String createId;

    private String createTime;
    /**
     * 事务事件单id集合
     */
    private List<String> ids;
    /**
     * 请求参数
     */
    private Map<String, Object> params;

    public String getPatrolId() {
        return patrolId;
    }

    public void setPatrolId(String patrolId) {
        this.patrolId = patrolId;
    }

    public String getPatrolName() {
        return patrolName;
    }

    public void setPatrolName(String patrolName) {
        this.patrolName = patrolName;
    }

    public String getPatrolDepartname() {
        return patrolDepartname;
    }

    public void setPatrolDepartname(String patrolDepartname) {
        this.patrolDepartname = patrolDepartname;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getEndUpTime() {
        return endUpTime;
    }

    public void setEndUpTime(String endUpTime) {
        this.endUpTime = endUpTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSyncData() {
        return syncData;
    }

    public void setSyncData(String syncData) {
        this.syncData = syncData;
    }

    public String getSysServerData() {
        return sysServerData;
    }

    public void setSysServerData(String sysServerData) {
        this.sysServerData = sysServerData;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHotSearchData() {
        return hotSearchData;
    }

    public void setHotSearchData(String hotSearchData) {
        this.hotSearchData = hotSearchData;
    }

    public String getEmergencyData() {
        return emergencyData;
    }

    public void setEmergencyData(String emergencyData) {
        this.emergencyData = emergencyData;
    }


    public String getHotTopicData() {
        return hotTopicData;
    }

    public void setHotTopicData(String hotTopicData) {
        this.hotTopicData = hotTopicData;
    }

    public String getHotEssayData() {
        return hotEssayData;
    }

    public void setHotEssayData(String hotEssayData) {
        this.hotEssayData = hotEssayData;
    }

    public String getContentData() {
        return contentData;
    }

    public void setContentData(String contentData) {
        this.contentData = contentData;
    }

    public String getSpecialData() {
        return specialData;
    }

    public void setSpecialData(String specialData) {
        this.specialData = specialData;
    }

    public String getInformationData() {
        return informationData;
    }

    public void setInformationData(String informationData) {
        this.informationData = informationData;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreateId() {
        return createId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getPageData() {
        return pageData;
    }

    public void setPageData(String pageData) {
        this.pageData = pageData;
    }
}
