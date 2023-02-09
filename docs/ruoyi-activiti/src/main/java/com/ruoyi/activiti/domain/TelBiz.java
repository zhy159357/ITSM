package com.ruoyi.activiti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * 电话事件单对象 tel_biz
 * 
 * @author ruoyi
 * @date 2021-01-19
 */
public class TelBiz
{
    private static final long serialVersionUID = 1L;

    /** 电话单id */

    private String telid;

    /** 电话单号 */
    @Excel(name = "电话单号")
    private String telno;

    /** 是否为转接(1是0否) */
    //@Excel(name = "是否为转接(1是0否)")
    private String istransfer;

    /** 来电人所属机构 */
    @Excel(name = "来电人所属机构",
            readConverterExp = "1=总行,2=北京,3=上海,4=天津,5=重庆,6=河北,7=山西,8=内蒙古,9=辽宁,10=吉林," +
                    "11=黑龙江,12=江苏,13=浙江,14=安徽,15=福建,16=江西,17=山东,18=河南,19=湖南,20=湖北," +
                    "21=广东,22=广西,23=海南,24=四川,25=贵州,26=云南,27=西藏,28=陕西,29=甘肃,30=青海," +
                    "31=宁夏,32=新疆,33=大连,34=厦门,35=深圳,36=宁波,37=青岛",prompt="报表导出和正常")
    private String contactOrg;

    /** 问题所属系统 */
    private String sysId;

    /** 问题类型 */
    @Excel(name = "事件类型",
            readConverterExp = "01=业务人员培训不到位,02=业务人员操作有误,03=系统功能不完善或不满足,04=省内系统原因,05=行内关联系统原因," +
                    "06=数据一致性问题,07=信息系统原因,08=系统程序BUG,09=版本或变更引起,10=客户操作问题," +
                    "11=第三方或他行系统原因,12=数据分析相关咨询,13=其他或疑难,14=基础问题咨询,15=转提交事件单")
    private String telType;

    /** 来电内容 */
    @Excel(name = "来电内容")
    private String content;

    /** 创建人 */
    private String createId;

    /** 创建机构 */
    //@Excel(name = "创建机构")
    private String createOrg;

    /** 来电人 */
    @Excel(name = "来电人")
    private String contactName;

    /** 来电人电话 */
    @Excel(name = "来电人电话")
    //@NotEmpty(message = "手机号不能为空")
    private String contactPhone;

    /** 有效标志 */
    //@Excel(name = "有效标志")
    private String invalidationMark;

    /** 业务事件单号（转业务事件单的才有） */
    @Excel(name = "业务事件单号")
    private String faultNo;

    /** 状态 */
    @Excel(name = "状态",
            readConverterExp = "1=待提交,2=未处理,3=已处理,4=待处理")
    private String state;

    /** 关闭时间 */
    @Excel(name = "关闭时间")
    private String closeTime;

    /** 事件类型 */
    @Excel(name = "工单类型",
            readConverterExp = "1=正常类型,2=溢出类型")
    private String eventType;

    /** 是否解决 */
    @Excel(name = "是否解决",
            readConverterExp = "1=是,2=否")
    private String isSolve;

    /** 组号 */
    @Excel(name = "组号")
    private String groupNo;

    /** 接入时间 */
    @Excel(name = "创建时间")
    private String createTime;

    /** 溢出组 */
    @Excel(name = "溢出组")
    private String overFlowGroup;

    /** 溢出时间 */
    @Excel(name = "溢出时间")
    private String overFlowTime;

    /** 关闭意见 */
    @Excel(name = "补充意见")
    private String closeOption;

    @Excel(name = "创建人")
    private String startTime;

    //创建人名称
    private String createName;

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    /** 报表的意见字段 */

    private String t1;// 业务人员培训不到位

    private String t2;// 业务人员操作有误

    private String t3;// 系统功能不完善或不满足

    private String t4;// 省内系统原因

    private String t5;// 行内关联系统原因

    private String t6;// 数据一致性问题,

    private String t7;// 信息系统原因

    private String t8;//  系统程序BUG

    private String t9;//  版本或变更引起

    private String t10;// 客户操作问题

    private String t11;// 第三方或他行系统原因

    private String t12;// 数据分析相关咨询

    private String t13;// 其他或疑难

    private String t14;// 基础问题咨询

    private String t15;// 转事件单

    private String t16;// 小计

    //这个是机构里面的一个参数
    private String paraName;
    //标识
    private String phoneState="0";/*1.机构、2.系统、3.机构和系统、0.不展示*/
    private String orgName;
    @Excel(name = "所属系统")
    private String caption;
    private String holdContrast;
    private String pName;

    private String startTimeBiz;

    public String getStartTimeBiz() {
        return startTimeBiz;
    }

    public void setStartTimeBiz(String startTimeBiz) {
        this.startTimeBiz = startTimeBiz;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    private Map<String,Object> params;

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getHoldContrast() {
        return holdContrast;
    }

    public void setHoldContrast(String holdContrast) {
        this.holdContrast = holdContrast;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPhoneState() {
        return phoneState;
    }

    public void setPhoneState(String phoneState) {
        this.phoneState = phoneState;
    }

    public String getParaName() {
        return paraName;
    }

    public void setParaName(String paraName) {
        this.paraName = paraName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
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

    public void setTelid(String telid)
    {
        this.telid = telid;
    }

    public String getTelid() 
    {
        return telid;
    }
    public void setTelno(String telno) 
    {
        this.telno = telno;
    }

    public String getTelno() 
    {
        return telno;
    }
    public void setIstransfer(String istransfer) 
    {
        this.istransfer = istransfer;
    }

    public String getIstransfer() 
    {
        return istransfer;
    }
    public void setContactOrg(String contactOrg) 
    {
        this.contactOrg = contactOrg;
    }

    public String getContactOrg() 
    {
        return contactOrg;
    }
    public void setSysId(String sysId) 
    {
        this.sysId = sysId;
    }

    public String getSysId() 
    {
        return sysId;
    }
    public void setTelType(String telType) 
    {
        this.telType = telType;
    }

    public String getTelType() 
    {
        return telType;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setCreateId(String createId) 
    {
        this.createId = createId;
    }

    public String getCreateId() 
    {
        return createId;
    }
    public void setCreateOrg(String createOrg) 
    {
        this.createOrg = createOrg;
    }

    public String getCreateOrg() 
    {
        return createOrg;
    }
    public void setContactName(String contactName) 
    {
        this.contactName = contactName;
    }

    public String getContactName() 
    {
        return contactName;
    }
    public void setContactPhone(String contactPhone) 
    {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() 
    {
        return contactPhone;
    }
    public void setInvalidationMark(String invalidationMark) 
    {
        this.invalidationMark = invalidationMark;
    }

    public String getInvalidationMark() 
    {
        return invalidationMark;
    }
    public void setFaultNo(String faultNo) 
    {
        this.faultNo = faultNo;
    }

    public String getFaultNo() 
    {
        return faultNo;
    }
    public void setState(String state) 
    {
        this.state = state;
    }

    public String getState() 
    {
        return state;
    }
    public void setCloseTime(String closeTime) 
    {
        this.closeTime = closeTime;
    }

    public String getCloseTime() 
    {
        return closeTime;
    }
    public void setEventType(String eventType) 
    {
        this.eventType = eventType;
    }

    public String getEventType() 
    {
        return eventType;
    }
    public void setIsSolve(String isSolve) 
    {
        this.isSolve = isSolve;
    }

    public String getIsSolve() 
    {
        return isSolve;
    }
    public void setGroupNo(String groupNo) 
    {
        this.groupNo = groupNo;
    }

    public String getGroupNo() 
    {
        return groupNo;
    }
    public void setOverFlowGroup(String overFlowGroup) 
    {
        this.overFlowGroup = overFlowGroup;
    }

    public String getOverFlowGroup() 
    {
        return overFlowGroup;
    }
    public void setOverFlowTime(String overFlowTime) 
    {
        this.overFlowTime = overFlowTime;
    }

    public String getOverFlowTime() 
    {
        return overFlowTime;
    }
    public void setCloseOption(String closeOption) 
    {
        this.closeOption = closeOption;
    }

    public String getCloseOption() 
    {
        return closeOption;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("telid", getTelid())
            .append("telno", getTelno())
            .append("istransfer", getIstransfer())
            .append("contactOrg", getContactOrg())
            .append("sysId", getSysId())
            .append("telType", getTelType())
            .append("content", getContent())
            .append("createId", getCreateId())
            .append("createTime", getCreateTime())
            .append("createOrg", getCreateOrg())
            .append("contactName", getContactName())
            .append("contactPhone", getContactPhone())
            .append("invalidationMark", getInvalidationMark())
            .append("faultNo", getFaultNo())
            .append("state", getState())
            .append("closeTime", getCloseTime())
            .append("eventType", getEventType())
            .append("isSolve", getIsSolve())
            .append("groupNo", getGroupNo())
            .append("overFlowGroup", getOverFlowGroup())
            .append("overFlowTime", getOverFlowTime())
            .append("closeOption", getCloseOption())
            .toString();
    }
}
