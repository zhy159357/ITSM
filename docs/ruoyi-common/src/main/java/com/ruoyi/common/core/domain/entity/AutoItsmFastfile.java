package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.Date;

/**
 * 自动化定时任务下载附件实体类
 * 
 * @author ruoyi
 * @date 2021-03-21
 */
public class AutoItsmFastfile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private String uuid;

    private String iitsmno;

    private String isystem;

    private String isystemabb;

    private String fastno;

    private String groupname;

    private String filepath;

    private String filename;

    private Long istatus;

    private Long istatusCentral;

    private Date iuploadtime;

    private Date idownloadtime;

    private String yzCenterStatus;// 亦庄下载状态
    private String hfCenterStatus;// 合肥下载状态

    private String sendStatus;// 发送状态

    public void setUuid(String uuid) 
    {
        this.uuid = uuid;
    }

    public String getUuid() 
    {
        return uuid;
    }
    public void setIitsmno(String iitsmno) 
    {
        this.iitsmno = iitsmno;
    }

    public String getIitsmno() 
    {
        return iitsmno;
    }
    public void setIsystem(String isystem) 
    {
        this.isystem = isystem;
    }

    public String getIsystem() 
    {
        return isystem;
    }
    public void setIsystemabb(String isystemabb) 
    {
        this.isystemabb = isystemabb;
    }

    public String getIsystemabb() 
    {
        return isystemabb;
    }
    public void setFastno(String fastno) 
    {
        this.fastno = fastno;
    }

    public String getFastno() 
    {
        return fastno;
    }
    public void setGroupname(String groupname) 
    {
        this.groupname = groupname;
    }

    public String getGroupname() 
    {
        return groupname;
    }
    public void setFilepath(String filepath) 
    {
        this.filepath = filepath;
    }

    public String getFilepath() 
    {
        return filepath;
    }
    public void setFilename(String filename) 
    {
        this.filename = filename;
    }

    public String getFilename() 
    {
        return filename;
    }
    public void setIstatus(Long istatus) 
    {
        this.istatus = istatus;
    }

    public Long getIstatus() 
    {
        return istatus;
    }

    public Long getIstatusCentral() {
        return istatusCentral;
    }

    public void setIstatusCentral(Long istatusCentral) {
        this.istatusCentral = istatusCentral;
    }

    public void setIuploadtime(Date iuploadtime)
    {
        this.iuploadtime = iuploadtime;
    }

    public Date getIuploadtime() 
    {
        return iuploadtime;
    }
    public void setIdownloadtime(Date idownloadtime) 
    {
        this.idownloadtime = idownloadtime;
    }

    public Date getIdownloadtime() 
    {
        return idownloadtime;
    }

    public String getYzCenterStatus() {
        return yzCenterStatus;
    }

    public void setYzCenterStatus(String yzCenterStatus) {
        this.yzCenterStatus = yzCenterStatus;
    }

    public String getHfCenterStatus() {
        return hfCenterStatus;
    }

    public void setHfCenterStatus(String hfCenterStatus) {
        this.hfCenterStatus = hfCenterStatus;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("uuid", getUuid())
            .append("iitsmno", getIitsmno())
            .append("isystem", getIsystem())
            .append("isystemabb", getIsystemabb())
            .append("fastno", getFastno())
            .append("groupname", getGroupname())
            .append("filepath", getFilepath())
            .append("filename", getFilename())
            .append("istatus", getIstatus())
            .append("istatusCentral", getIstatusCentral())
            .append("iuploadtime", getIuploadtime())
            .append("idownloadtime", getIdownloadtime())
            .toString();
    }
}
