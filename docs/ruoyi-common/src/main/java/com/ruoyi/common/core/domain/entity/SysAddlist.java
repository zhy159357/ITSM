package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Objects;

/**
 * 通讯录 system_addlist
 */
public class SysAddlist extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String address_list_id;
    private String address_list_type;
    private String create_pid;
    private String orgId;
    private String create_time;
    private String update_time;
    private String update_user_id;
    @Excel(name = "姓名")
    private String pname;
    @Excel(name = "手机")
    private String phone;
    @Excel(name = "座机")
    private String tel;
    @Excel(name = "邮箱")
    private String email;
    @Excel(name = "使用状态")
    private String invalidationmark;
    private String server_name;
    private String server_order;
    private String iscanread;
    private String create_orgid;
    @Excel(name = "职务")
    private String duty;
    @Excel(name = "职责")
    private String responsibility;
    private String memo;
    public SysAddlist() {

    }

    public static long getSerialVersionUID() {

        return serialVersionUID;
    }

    public String getAddress_list_id() {
        return address_list_id;
    }

    public void setAddress_list_id(String address_list_id) {
        this.address_list_id = address_list_id;
    }

    public String getAddress_list_type() {
        return address_list_type;
    }

    public void setAddress_list_type(String address_list_type) {
        this.address_list_type = address_list_type;
    }

    public String getCreate_pid() {
        return create_pid;
    }

    public void setCreate_pid(String create_pid) {
        this.create_pid = create_pid;
    }

    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getUpdate_user_id() {
        return update_user_id;
    }

    public void setUpdate_user_id(String update_user_id) {
        this.update_user_id = update_user_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInvalidationmark() {
        return invalidationmark;
    }

    public void setInvalidationmark(String invalidationmark) {
        this.invalidationmark = invalidationmark;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getServer_name() {
        return server_name;
    }

    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getServer_order() {
        return server_order;
    }

    public void setServer_order(String server_order) {
        this.server_order = server_order;
    }

    public String getIscanread() {
        return iscanread;
    }

    public void setIscanread(String iscanread) {
        this.iscanread = iscanread;
    }

    public String getCreate_orgid() {
        return create_orgid;
    }

    public void setCreate_orgid(String create_orgid) {
        this.create_orgid = create_orgid;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
