package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
@Builder
@AllArgsConstructor
public class OgPerson extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public OgPerson() {

    }
    private String addAndUpdate;
    /**
     * 人员编号
     */
    private String pId;
    
    private long pid_crc;

    /**
     * 人员机构
     */
    private String orgId;
    /**
     * 人员名字
     */
    @Excel(name = "人员姓名", sort = 1)
    private String pName;
    /**
     * 人员性别
     */
    @Excel(name = "性别", sort = 2, readConverterExp = "1=男,2=女")
    private String sex;
    /**
     * 人员学历
     */
    private String edu;
    /**
     * 出生日期
     */

    private String birthday;


    /**
     * 籍贯
     */
    private String birthPlace;
    /**
     * 移动电话
     */
    @Excel(name = "办公电话", sort = 4)
    private String phone;
    /**
     * 办公电话
     */
    @Excel(name = "移动电话", sort = 3)
    private String mobilPhone;

    private String newMobilPhone;
    /**
     * 邮箱
     */
    @Excel(name = "邮箱", sort = 5)
    private String email;
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
    private String memo;
    /**
     * 更新时间
     */
    private String updatetime;
    /**
     * 无效标识
     */
    @Excel(name = "状态", sort = 9, readConverterExp = "1=启用,0=禁用")
    private String invalidationMark;
    /**
     * 人员职位
     */
    @Excel(name = "职务", sort = 7)
    private String position;
    /**
     * 领导者
     */
    @Excel(name = "负责人", sort = 8, readConverterExp = "1=是,0=否")
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
    @Excel(name = "所属机构", sort = 6)
    private String orgname;

    /**等级编号*/
    private String levelCode;

    /**
     * 工作组id参数传递用  add by zhang_chao
     */
    private String groupId;

    /**
     * 角色id参数传递用  add by zhang_chao
     */
    private String rId;

    // 机构信息
    private OgOrg org;

    public String getAddAndUpdate() {
        return addAndUpdate;
    }

    public void setAddAndUpdate(String addAndUpdate) {
        this.addAndUpdate = addAndUpdate;
    }

    //是否选中标识
    private boolean flag = false;

    //og_user的id
    private String userid;

    private Integer num;

    private String username;

    private String agencyPid;

    private String agencyPname;

    private String agencySwitch;

    private String agencyStrTime;

    private String agencyEndTime;

    //参与机构ID
    private String partakeOrgId;

    private String partakeOrgName;

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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
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

    public String getMobilPhone() {
        return mobilPhone;
    }

    public void setMobilPhone(String mobilPhone) {
        this.mobilPhone = mobilPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public OgOrg getOrg() {
        return org;
    }

    public void setOrg(OgOrg org) {
        this.org = org;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getNewMobilPhone() {
        return newMobilPhone;
    }

    public void setNewMobilPhone(String newMobilPhone) {
        this.newMobilPhone = newMobilPhone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAgencyPid() {
        return agencyPid;
    }

    public void setAgencyPid(String agencyPid) {
        this.agencyPid = agencyPid;
    }

    public String getAgencyPname() {
        return agencyPname;
    }

    public void setAgencyPname(String agencyPname) {
        this.agencyPname = agencyPname;
    }

    public String getAgencySwitch() {
        return agencySwitch;
    }

    public void setAgencySwitch(String agencySwitch) {
        this.agencySwitch = agencySwitch;
    }

    public String getAgencyStrTime() {
        return agencyStrTime;
    }

    public void setAgencyStrTime(String agencyStrTime) {
        this.agencyStrTime = agencyStrTime;
    }

    public String getAgencyEndTime() {
        return agencyEndTime;
    }

    public void setAgencyEndTime(String agencyEndTime) {
        this.agencyEndTime = agencyEndTime;
    }

    public String getPartakeOrgId() {
        return partakeOrgId;
    }

    public void setPartakeOrgId(String partakeOrgId) {
        this.partakeOrgId = partakeOrgId;
    }

    public String getPartakeOrgName() {
        return partakeOrgName;
    }

    public void setPartakeOrgName(String partakeOrgName) {
        this.partakeOrgName = partakeOrgName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("pId", pId)
                .append("orgId", orgId)
                .append("pName", pName)
                .append("sex", sex)
                .append("edu", edu)
                .append("birthday", birthday)
                .append("birthPlace", birthPlace)
                .append("phone", phone)
                .append("mobilPhone", mobilPhone)
                .append("mobilPhone", mobilPhone)
                .append("address", address)
                .append("adder", adder)
                .append("addtime", addtime)
                .append("moder", moder)
                .append("memo", memo)
                .append("updatetime", updatetime)
                .append("invalidationMark", invalidationMark)
                .append("position", position)
                .append("leader", leader)
                .append("pOrder", pOrder)
                .append("pflag", pflag)
                .append("newMobilPhone",newMobilPhone)
                .append("username",username)
                .toString();
    }
}
