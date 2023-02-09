package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 电话终端绑定 system_bind
 */
public class SysBind extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @Excel(name = "ID")
    private String id;
    /**
     * ip地址
     */
    @Excel(name = "IP地址")
    private String ip;
    /**
     * 电话银行工号
     */
    @Excel(name = "电话银行工号")
    private String telbankid;
    /**
     * 分机号
     */
    @Excel(name = "分机号")
    private String extensnum;
    /**
     * 工号
     */
    @Excel(name = "工号")
    private String jobnum;
    /**
     * 创建者
     */
    @Excel(name = "创建人")
    private String creater;
    /**
     * DATETIME
     */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date datetime;
    /**
     * 创建者
     */
    @Excel(name = "状态", cellType = ColumnType.NUMERIC)
    private Long status;
    /**
     * 创建者
     */
    @Excel(name = "删除标记", cellType = ColumnType.NUMERIC)
    private Long del_flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTelbankid() {
        return telbankid;
    }

    public void setTelbankid(String telbankid) {
        this.telbankid = telbankid;
    }

    public String getExtensnum() {
        return extensnum;
    }

    public void setExtensnum(String extensnum) {
        this.extensnum = extensnum;
    }

    public String getJobnum() {
        return jobnum;
    }

    public void setJobnum(String jobnum) {
        this.jobnum = jobnum;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(Long del_flag) {
        this.del_flag = del_flag;
    }

    public SysBind() {

    }

    public SysBind(String id, String ip, String telbankid, String extensnum, String jobnum, String creater, Date datetime, Long status, Long del_flag) {
        this.id = id;
        this.ip = ip;
        this.telbankid = telbankid;
        this.extensnum = extensnum;
        this.jobnum = jobnum;
        this.creater = creater;
        this.datetime = datetime;
        this.status = status;
        this.del_flag = del_flag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("ip", getIp())
                .append("telbankid", getTelbankid())
                .append("extensnum", getExtensnum())
                .append("jobnum", getJobnum())
                .append("creater", getCreater())
                .append("datetime", getDatetime())
                .append("status", getStatus())
                .append("del_flag", getDel_flag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .toString();
    }
}
