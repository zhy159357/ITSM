package com.ruoyi.activiti.domain;

import com.ruoyi.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Map;

public class TelBizTwo {

    private static final long serialVersionUID = 1L;

    /** 来电人所属机构 */
    @Excel(name = "来电人所属机构")
    private String orgName;

    private String contactOrg;

    /** 报表的意见字段 */

    @Excel(name = "所属系统")
    private String caption;

    @Excel(name = "业务人员培训不到位")
    private String t1;// 业务人员培训不到位

    @Excel(name = "业务人员操作有误")
    private String t2;// 业务人员操作有误

    @Excel(name = "系统功能不完善或不满足")
    private String t3;// 系统功能不完善或不满足

    @Excel(name = "省内系统原因")
    private String t4;// 省内系统原因

    @Excel(name = "行内关联系统原因")
    private String t5;// 行内关联系统原因

    @Excel(name = "数据一致性问题")
    private String t6;// 数据一致性问题,

    @Excel(name = "信息系统原因")
    private String t7;// 信息系统原因

    @Excel(name = "系统程序BUG")
    private String t8;//  系统程序BUG

    @Excel(name = "版本或变更引起")
    private String t9;//  版本或变更引起

    @Excel(name = "客户操作问题")
    private String t10;// 客户操作问题

    @Excel(name = "第三方或他行系统原因")
    private String t11;// 第三方或他行系统原因

    @Excel(name = "数据分析相关咨询")
    private String t12;// 数据分析相关咨询

    @Excel(name = "其他或疑难")
    private String t13;// 其他或疑难

    @Excel(name = "基础问题咨询")
    private String t14;// 基础问题咨询

    @Excel(name = "转事件单")
    private String t15;// 转事件单

    @Excel(name = "小计")
    private String t16;// 小计

    @Excel(name = "占比")
    private String holdContrast;

    public String getHoldContrast() {
        return holdContrast;
    }

    public void setHoldContrast(String holdContrast) {
        this.holdContrast = holdContrast;
    }

    private Map<String,Object> params;

    public String getContactOrg() {
        return contactOrg;
    }

    public void setContactOrg(String contactOrg) {
        this.contactOrg = contactOrg;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public String getT2() {
        return t2;
    }

    public void setT2(String t2) {
        this.t2 = t2;
    }

    public String getT3() {
        return t3;
    }

    public void setT3(String t3) {
        this.t3 = t3;
    }

    public String getT4() {
        return t4;
    }

    public void setT4(String t4) {
        this.t4 = t4;
    }

    public String getT5() {
        return t5;
    }

    public void setT5(String t5) {
        this.t5 = t5;
    }

    public String getT6() {
        return t6;
    }

    public void setT6(String t6) {
        this.t6 = t6;
    }

    public String getT7() {
        return t7;
    }

    public void setT7(String t7) {
        this.t7 = t7;
    }

    public String getT8() {
        return t8;
    }

    public void setT8(String t8) {
        this.t8 = t8;
    }

    public String getT9() {
        return t9;
    }

    public void setT9(String t9) {
        this.t9 = t9;
    }

    public String getT10() {
        return t10;
    }

    public void setT10(String t10) {
        this.t10 = t10;
    }

    public String getT11() {
        return t11;
    }

    public void setT11(String t11) {
        this.t11 = t11;
    }

    public String getT12() {
        return t12;
    }

    public void setT12(String t12) {
        this.t12 = t12;
    }

    public String getT13() {
        return t13;
    }

    public void setT13(String t13) {
        this.t13 = t13;
    }

    public String getT14() {
        return t14;
    }

    public void setT14(String t14) {
        this.t14 = t14;
    }

    public String getT15() {
        return t15;
    }

    public void setT15(String t15) {
        this.t15 = t15;
    }

    public String getT16() {
        return t16;
    }

    public void setT16(String t16) {
        this.t16 = t16;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
