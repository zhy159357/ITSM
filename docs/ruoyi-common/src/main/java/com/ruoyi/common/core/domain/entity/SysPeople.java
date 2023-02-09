package com.ruoyi.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class SysPeople  extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public SysPeople() {

    }

    /**
     * 人员编号
     */
    private String pId;

    /**
     * 人员机构
     */
    private String orgId;
    /**
     * 人员名字
     */
    private String pName;
    /**
     * 人员性别
     */
    private String sex;
    /**
     * 人员学历
     */
    private String edu;
    /**
     * 出生日期
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;


    /**
     * 籍贯
     */
    private String  birthPlace;
    /**
     * 移动电话
     */
    private String phone;
    /**
     * 办公电话
     */
    private String moblePhone;
    /**
     * 邮箱
     */
    private String emall;
    /**
     * 地址
     */
    private String address;
    /**
     * 添加人
     */
    private String adder;
    /**
     * 添加时间
     */
    private String addtime;
    /**
     * 修改人
     */
    private String moder;
    /**
     * 人员备注
     */
    private String mome;
    /**
     * 更新时间
     */
    private String updatetime;
    /**
     *无效标识
     */
    private String invalidationMark;
    /**
     * 人员职位
     */
    private String position;
    /**
     * 领导者
     */
    private String leader;
    /**
     * 顺序
     */
    private String pOrder;
    /**
     * 标识
     */
    private String pflag;

    /**
     * 部门信息
     */
    private SysDept dept;



    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEdu() {
        return edu;
    }

    public void setEdu(String edu) {
        this.edu = edu;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMoblePhone() {
        return moblePhone;
    }

    public void setMoblePhone(String moblePhone) {
        this.moblePhone = moblePhone;
    }

    public String getEmall() {
        return emall;
    }

    public void setEmall(String emall) {
        this.emall = emall;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdder() {
        return adder;
    }

    public void setAdder(String adder) {
        this.adder = adder;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getModer() {
        return moder;
    }

    public void setModer(String moder) {
        this.moder = moder;
    }

    public String getMome() {
        return mome;
    }

    public void setMome(String mome) {
        this.mome = mome;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getpOrder() {
        return pOrder;
    }

    public void setpOrder(String pOrder) {
        this.pOrder = pOrder;
    }

    public String getPflag() {
        return pflag;
    }

    public void setPflag(String pflag) {
        this.pflag = pflag;
    }

    public SysDept getDept() {
        return dept;
    }

    public void setDept(SysDept dept) {
        this.dept = dept;
    }

    @Override
    public String toString() {
        return "SysPeople{" +
                "pId='" + pId + '\'' +
                ", orgId='" + orgId + '\'' +
                ", pName='" + pName + '\'' +
                ", sex='" + sex + '\'' +
                ", edu='" + edu + '\'' +
                ", birthday='" + birthday + '\'' +
                ", birthPlace='" + birthPlace + '\'' +
                ", phone='" + phone + '\'' +
                ", moblePhone='" + moblePhone + '\'' +
                ", emall='" + emall + '\'' +
                ", address='" + address + '\'' +
                ", adder='" + adder + '\'' +
                ", addtime='" + addtime + '\'' +
                ", moder='" + moder + '\'' +
                ", mome='" + mome + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", invalidationMark='" + invalidationMark + '\'' +
                ", position='" + position + '\'' +
                ", leader='" + leader + '\'' +
                ", pOrder='" + pOrder + '\'' +
                ", pflag='" + pflag + '\'' +
                '}';
    }
}
