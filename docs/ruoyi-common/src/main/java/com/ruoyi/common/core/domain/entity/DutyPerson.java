package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;

import java.io.Serializable;

/**
 * 值班人员对象 DutyPerson
 * @author dayong_sun
 * @date 2020-01-19
 */
public class DutyPerson implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 姓名id */
    private String pid;

    /** 机构-姓名-电话 */
    private String opmname;

    /** 姓名 */
    @Excel(name = "姓名")
    private String pname;

    /** 值班手机 */
    @Excel(name = "联系电话")
    private String mobilephone;

    /** 所在机构 */
    @Excel(name = "所在机构")
    private String orgname;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getOpmname() {
        return opmname;
    }

    public void setOpmname(String opmname) {
        this.opmname = opmname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }
}
