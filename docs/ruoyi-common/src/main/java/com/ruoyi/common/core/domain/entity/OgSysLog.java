package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.Domain;

//应用系统日志表
public class OgSysLog implements Domain {

    private String logId;


    private String isexamination;

    private String dept;


    private String pid;


    private String businessid;


    private String isKeySys;


    private String sysType;


    private String upgradeTime;


    private String systemStage;


    private String updateTime;


    private String frontlineDate;


    private String secondlineDate;


    private String isOutChannel;


    private String sysid;


    private String orgid;


    private String code;


    private String caption;


    private String memo;


    private String adder;


    private String addtime;


    private String moder;


    private String invalidationMark;

    public void setIsexamination(String isexamination)
    {
        this.isexamination = isexamination;
    }

    public String getIsexamination()
    {
        return isexamination;
    }
    public void setDept(String dept)
    {
        this.dept = dept;
    }

    public String getDept()
    {
        return dept;
    }
    public void setPid(String pid)
    {
        this.pid = pid;
    }

    public String getPid()
    {
        return pid;
    }
    public void setBusinessid(String businessid)
    {
        this.businessid = businessid;
    }

    public String getBusinessid()
    {
        return businessid;
    }
    public void setIsKeySys(String isKeySys)
    {
        this.isKeySys = isKeySys;
    }

    public String getIsKeySys()
    {
        return isKeySys;
    }
    public void setSysType(String sysType)
    {
        this.sysType = sysType;
    }

    public String getSysType()
    {
        return sysType;
    }
    public void setUpgradeTime(String upgradeTime)
    {
        this.upgradeTime = upgradeTime;
    }

    public String getUpgradeTime()
    {
        return upgradeTime;
    }
    public void setSystemStage(String systemStage)
    {
        this.systemStage = systemStage;
    }

    public String getSystemStage()
    {
        return systemStage;
    }
    public void setFrontlineDate(String frontlineDate)
    {
        this.frontlineDate = frontlineDate;
    }

    public String getFrontlineDate()
    {
        return frontlineDate;
    }
    public void setSecondlineDate(String secondlineDate)
    {
        this.secondlineDate = secondlineDate;
    }

    public String getSecondlineDate()
    {
        return secondlineDate;
    }
    public void setIsOutChannel(String isOutChannel)
    {
        this.isOutChannel = isOutChannel;
    }

    public String getIsOutChannel()
    {
        return isOutChannel;
    }
    public void setLogId(String logId)
    {
        this.logId = logId;
    }

    public String getLogId()
    {
        return logId;
    }
    public void setSysid(String sysid)
    {
        this.sysid = sysid;
    }

    public String getSysid()
    {
        return sysid;
    }
    public void setOrgid(String orgid)
    {
        this.orgid = orgid;
    }

    public String getOrgid()
    {
        return orgid;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
    public void setCaption(String caption)
    {
        this.caption = caption;
    }

    public String getCaption()
    {
        return caption;
    }
    public void setMemo(String memo)
    {
        this.memo = memo;
    }

    public String getMemo()
    {
        return memo;
    }
    public void setAdder(String adder)
    {
        this.adder = adder;
    }

    public String getAdder()
    {
        return adder;
    }
    public void setAddtime(String addtime)
    {
        this.addtime = addtime;
    }

    public String getAddtime()
    {
        return addtime;
    }
    public void setModer(String moder)
    {
        this.moder = moder;
    }

    public String getModer()
    {
        return moder;
    }
    public void setInvalidationMark(String invalidationMark)
    {
        this.invalidationMark = invalidationMark;
    }

    public String getInvalidationMark()
    {
        return invalidationMark;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
