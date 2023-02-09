package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Objects;

/**
 * 信息管理制度 system_info
 */
public class SysInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    private String regime_info_id;

    @Excel(name = "制度标题")
    private String regime_title;

    @Excel(name = "制度摘要")
    private String regime_digest;

    @Excel(name = "提交人")
    private String commit_id;

    @Excel(name = "提交时间")
    private String commit_time;

    @Excel(name = "组织ID")
    private String orgid;

    @Excel(name = "制度信息状态")
    private String regime_info_type;

    @Excel(name = "上一步操作人")
    private String step_up_operation_id;

    @Excel(name = "上一步操作时间")
    private String step_up_operation_time;

    @Excel(name = "印发日期")
    private String print_time;

    @Excel(name = "提交部门")
    private String commit_dept;

    @Excel(name = "印发文号")
    private String num;

    @Excel(name = "备注")
    private String mark;

    @Excel(name = "类别一")
    private String type_one;

    @Excel(name = "类别二")
    private String type_two;

    @Excel(name = "四级制度")
    private String sytem_four;

    @Excel(name = "分类域")
    private String classify;

    @Excel(name = "废止原因")    //1.修订  2.废止
    private String delete_reason;

    @Excel(name = "关键字")
    private String key_word;

    @Excel(name = "逻辑删除标记")
    private String is_delete;

    @Excel(name = "状态")
    //1.正式,2.试行,3.新增,4.废止,5.修订,6.退回待审核,7.待删除,8.待废止,9.待审核,
    private String current_state;

    @Excel(name = "审核人")
    private String checker;

    @Excel(name = "文件夹")
    private String folder_;

    @Excel(name = "废止日期")
    private String delete_time;

    private String createName;

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    /**
     * 标签
     */
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRegime_info_id() {
        return regime_info_id;
    }

    public void setRegime_info_id(String regime_info_id) {
        this.regime_info_id = regime_info_id;
    }

    public String getRegime_title() {
        return regime_title;
    }

    public void setRegime_title(String regime_title) {
        this.regime_title = regime_title;
    }

    public String getRegime_digest() {
        return regime_digest;
    }

    public void setRegime_digest(String regime_digest) {
        this.regime_digest = regime_digest;
    }

    public String getCommit_id() {
        return commit_id;
    }

    public void setCommit_id(String commit_id) {
        this.commit_id = commit_id;
    }

    public String getCommit_time() {
        return commit_time;
    }

    public void setCommit_time(String commit_time) {
        this.commit_time = commit_time;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getRegime_info_type() {
        return regime_info_type;
    }

    public void setRegime_info_type(String regime_info_type) {
        this.regime_info_type = regime_info_type;
    }

    public String getStep_up_operation_id() {
        return step_up_operation_id;
    }

    public void setStep_up_operation_id(String step_up_operation_id) {
        this.step_up_operation_id = step_up_operation_id;
    }

    public String getStep_up_operation_time() {
        return step_up_operation_time;
    }

    public void setStep_up_operation_time(String step_up_operation_time) {
        this.step_up_operation_time = step_up_operation_time;
    }

    public String getPrint_time() {
        return print_time;
    }

    public void setPrint_time(String print_time) {
        this.print_time = print_time;
    }

    public String getCommit_dept() {
        return commit_dept;
    }

    public void setCommit_dept(String commit_dept) {
        this.commit_dept = commit_dept;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getType_one() {
        return type_one;
    }

    public void setType_one(String type_one) {
        this.type_one = type_one;
    }

    public String getType_two() {
        return type_two;
    }

    public void setType_two(String type_two) {
        this.type_two = type_two;
    }

    public String getSytem_four() {
        return sytem_four;
    }

    public void setSytem_four(String sytem_four) {
        this.sytem_four = sytem_four;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getDelete_reason() {
        return delete_reason;
    }

    public void setDelete_reason(String delete_reason) {
        this.delete_reason = delete_reason;
    }

    public String getKey_word() {
        return key_word;
    }

    public void setKey_word(String key_word) {
        this.key_word = key_word;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getCurrent_state() {
        return current_state;
    }

    public void setCurrent_state(String current_state) {
        this.current_state = current_state;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getFolder_() {
        return folder_;
    }

    public void setFolder_(String folder_) {
        this.folder_ = folder_;
    }

    public String getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(String delete_time) {
        this.delete_time = delete_time;
    }

    public SysInfo() {

    }

    @Override
    public String toString() {
        return "SysInfo{" +
                "regime_info_id='" + regime_info_id + '\'' +
                ", regime_title='" + regime_title + '\'' +
                ", regime_digest='" + regime_digest + '\'' +
                ", commit_id='" + commit_id + '\'' +
                ", commit_time='" + commit_time + '\'' +
                ", orgid='" + orgid + '\'' +
                ", regime_info_type='" + regime_info_type + '\'' +
                ", step_up_operation_id='" + step_up_operation_id + '\'' +
                ", step_up_operation_time='" + step_up_operation_time + '\'' +
                ", print_time='" + print_time + '\'' +
                ", commit_dept='" + commit_dept + '\'' +
                ", num='" + num + '\'' +
                ", mark='" + mark + '\'' +
                ", type_one='" + type_one + '\'' +
                ", type_two='" + type_two + '\'' +
                ", sytem_four='" + sytem_four + '\'' +
                ", classify='" + classify + '\'' +
                ", delete_reason='" + delete_reason + '\'' +
                ", key_word='" + key_word + '\'' +
                ", is_delete='" + is_delete + '\'' +
                ", current_state='" + current_state + '\'' +
                ", checker='" + checker + '\'' +
                ", folder_='" + folder_ + '\'' +
                ", delete_time='" + delete_time + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        SysInfo sysInfo = (SysInfo) o;
        return Objects.equals(regime_info_id, sysInfo.regime_info_id) &&
                Objects.equals(regime_title, sysInfo.regime_title) &&
                Objects.equals(regime_digest, sysInfo.regime_digest) &&
                Objects.equals(commit_id, sysInfo.commit_id) &&
                Objects.equals(commit_time, sysInfo.commit_time) &&
                Objects.equals(orgid, sysInfo.orgid) &&
                Objects.equals(regime_info_type, sysInfo.regime_info_type) &&
                Objects.equals(step_up_operation_id, sysInfo.step_up_operation_id) &&
                Objects.equals(step_up_operation_time, sysInfo.step_up_operation_time) &&
                Objects.equals(print_time, sysInfo.print_time) &&
                Objects.equals(commit_dept, sysInfo.commit_dept) &&
                Objects.equals(num, sysInfo.num) &&
                Objects.equals(mark, sysInfo.mark) &&
                Objects.equals(type_one, sysInfo.type_one) &&
                Objects.equals(type_two, sysInfo.type_two) &&
                Objects.equals(sytem_four, sysInfo.sytem_four) &&
                Objects.equals(classify, sysInfo.classify) &&
                Objects.equals(delete_reason, sysInfo.delete_reason) &&
                Objects.equals(key_word, sysInfo.key_word) &&
                Objects.equals(is_delete, sysInfo.is_delete) &&
                Objects.equals(current_state, sysInfo.current_state) &&
                Objects.equals(checker, sysInfo.checker) &&
                Objects.equals(folder_, sysInfo.folder_) &&
                Objects.equals(delete_time, sysInfo.delete_time);
    }

    @Override
    public int hashCode() {

        return Objects.hash(regime_info_id, regime_title, regime_digest, commit_id, commit_time, orgid, regime_info_type, step_up_operation_id, step_up_operation_time, print_time, commit_dept, num, mark, type_one, type_two, sytem_four, classify, delete_reason, key_word, is_delete, current_state, checker, folder_, delete_time);
    }
}
