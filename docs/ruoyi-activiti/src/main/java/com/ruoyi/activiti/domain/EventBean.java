package com.ruoyi.activiti.domain;
/**
 * <p>@功能描述:webservice接口</p>
 */

import java.io.Serializable;


public class EventBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2656518207047468458L;
    //--------------展华监控--------------------
    String event_System_Name;
    String host_Name;
    String event_Desc;
    String event_Level;
    String event_Level_Name;
    String event_Happend_Time;
    String host_IP;
    String event_TypeF;
    String event_TypeF_Name;
    //-------------展华监控---------------------
    //--------------电话银行--------------------
    String OgSys;
    String OgGroup;
    String Type;
    String create_ID;//创建人id
    String create_Org_ID; //创建机构id
    String group_ID;//所属工作组 NOT NULL
    String sysid;//所属应用系统 NOT NULL
    String fm_Type_ID;//事件分类 NOT NULL
    String occurrence_Org_ID;//发生机构ID
    String fault_No;//电话银行id NOT NULL
    String fault_Source;//事件来源 NOT NULL
    String Serious_Lev;//事件等级
    String occurrence_Address;//发生地址 交易机构
    String occur_Time;//发生时间NOT NULL
    String fault_Report_Name;//当事人NOT NULL
    String report_Time;//报告时间
    String report_Phone;//报告人电话
    String fault_Contact_Name;//填报人NOT NULL
    String contact_Phone;//填报人电话NOT NULL
    String contact_Address;//填报人地址  发生部门
    String serial_Number;//流水号
    String trading_Name;//交易名称
    String error_Information;//错误信息
    String fault_Decript_Summary;//事件标题NOT NULL
    String fault_Decript_Detail;//事件描述NOT NULL
    String creat_Time;//创建时间
    String current_State;//当前状态
    String involve_Amount;//涉事金额NOT NULL
    String involve_Scount;//涉事笔数
    String customer_Name;//客户姓名
    String customer_IDcard;//客户身份证号
    String transaction_Account;//交易账号
    String transaction_Amount;//交易金额
    String order_Number;//订单号
    String fm_Cause;//事件起因
    String receiver_IDs;//领取人id
    String invalidation_Mark;//无效标志
    String file_Path;//附件路径

    String fm_ID;//我们单号
    String evaluater_ID;
    String fault_NO;//电话银行ID
    String evaluate;
    String evaluate_Result;//满意度
    String s_LOG_performDesc;//评价意见

    // --------------网络自动化--------------------
    String changeCode;//变更单号
    String changeStatus;//变更状态
    String changeResult;
    String changeStartTime;
    String changeEndTime;
    //------脚本服务化------
    String fbsId;
    String executorStatus;
    String executorResult;
    String executorEndTime;

    //------运维事件单分类&关键字------
    String oneType;
    String twoType;
    String threeType;
    String keywords;

    public String getFbsId() {
        return fbsId;
    }

    public void setFbsId(String fbsId) {
        this.fbsId = fbsId;
    }

    public String getExecutorStatus() {
        return executorStatus;
    }

    public void setExecutorStatus(String executorStatus) {
        this.executorStatus = executorStatus;
    }

    public String getExecutorResult() {
        return executorResult;
    }

    public void setExecutorResult(String executorResult) {
        this.executorResult = executorResult;
    }

    public String getExecutorEndTime() {
        return executorEndTime;
    }

    public void setExecutorEndTime(String executorEndTime) {
        this.executorEndTime = executorEndTime;
    }

    public String getChangeCode() {
        return changeCode;
    }

    public void setChangeCode(String changeCode) {
        this.changeCode = changeCode;
    }

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    public String getChangeResult() {
        return changeResult;
    }

    public void setChangeResult(String changeResult) {
        this.changeResult = changeResult;
    }

    public String getChangeStartTime() {
        return changeStartTime;
    }

    public void setChangeStartTime(String changeStartTime) {
        this.changeStartTime = changeStartTime;
    }

    public String getChangeEndTime() {
        return changeEndTime;
    }

    public void setChangeEndTime(String changeEndTime) {
        this.changeEndTime = changeEndTime;
    }

    public String getEvent_System_Name() {
        return event_System_Name;
    }

    public String getFm_ID() {
        return fm_ID;
    }

    public void setFm_ID(String fm_ID) {
        this.fm_ID = fm_ID;
    }

    public String getEvaluater_ID() {
        return evaluater_ID;
    }

    public void setEvaluater_ID(String evaluater_ID) {
        this.evaluater_ID = evaluater_ID;
    }

    public String getFault_NO() {
        return fault_NO;
    }

    public void setFault_NO(String fault_NO) {
        this.fault_NO = fault_NO;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getEvaluate_Result() {
        return evaluate_Result;
    }

    public void setEvaluate_Result(String evaluate_Result) {
        this.evaluate_Result = evaluate_Result;
    }

    public String getOgSys() {
        return OgSys;
    }

    public void setOgSys(String ogSys) {
        OgSys = ogSys;
    }

    public String getOgGroup() {
        return OgGroup;
    }

    public void setOgGroup(String ogGroup) {
        OgGroup = ogGroup;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setEvent_System_Name(String event_System_Name) {
        this.event_System_Name = event_System_Name;
    }

    public String getHost_Name() {
        return host_Name;
    }

    public void setHost_Name(String host_Name) {
        this.host_Name = host_Name;
    }

    public String getEvent_Desc() {
        return event_Desc;
    }

    public void setEvent_Desc(String event_Desc) {
        this.event_Desc = event_Desc;
    }

    public String getEvent_Level() {
        return event_Level;
    }

    public void setEvent_Level(String event_Level) {
        this.event_Level = event_Level;
    }

    public String getEvent_Level_Name() {
        return event_Level_Name;
    }

    public void setEvent_Level_Name(String event_Level_Name) {
        this.event_Level_Name = event_Level_Name;
    }

    public String getEvent_Happend_Time() {
        return event_Happend_Time;
    }

    public void setEvent_Happend_Time(String event_Happend_Time) {
        this.event_Happend_Time = event_Happend_Time;
    }

    public String getHost_IP() {
        return host_IP;
    }

    public void setHost_IP(String host_IP) {
        this.host_IP = host_IP;
    }

    public String getEvent_TypeF() {
        return event_TypeF;
    }

    public void setEvent_TypeF(String event_TypeF) {
        this.event_TypeF = event_TypeF;
    }

    public String getEvent_TypeF_Name() {
        return event_TypeF_Name;
    }

    public void setEvent_TypeF_Name(String event_TypeF_Name) {
        this.event_TypeF_Name = event_TypeF_Name;
    }

    public String getCreate_ID() {
        return create_ID;
    }

    public void setCreate_ID(String create_ID) {
        this.create_ID = create_ID;
    }

    public String getCreate_Org_ID() {
        return create_Org_ID;
    }

    public void setCreate_Org_ID(String create_Org_ID) {
        this.create_Org_ID = create_Org_ID;
    }

    public String getGroup_ID() {
        return group_ID;
    }

    public void setGroup_ID(String group_ID) {
        this.group_ID = group_ID;
    }

    public String getSysid() {
        return sysid;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public String getFm_Type_ID() {
        return fm_Type_ID;
    }

    public void setFm_Type_ID(String fm_Type_ID) {
        this.fm_Type_ID = fm_Type_ID;
    }

    public String getOccurrence_Org_ID() {
        return occurrence_Org_ID;
    }

    public void setOccurrence_Org_ID(String occurrence_Org_ID) {
        this.occurrence_Org_ID = occurrence_Org_ID;
    }

    public String getFault_No() {
        return fault_No;
    }

    public void setFault_No(String fault_No) {
        this.fault_No = fault_No;
    }

    public String getFault_Source() {
        return fault_Source;
    }

    public void setFault_Source(String fault_Source) {
        this.fault_Source = fault_Source;
    }

    public String getSerious_Lev() {
        return Serious_Lev;
    }

    public void setSerious_Lev(String serious_Lev) {
        Serious_Lev = serious_Lev;
    }

    public String getOccurrence_Address() {
        return occurrence_Address;
    }

    public void setOccurrence_Address(String occurrence_Address) {
        this.occurrence_Address = occurrence_Address;
    }

    public String getOccur_Time() {
        return occur_Time;
    }

    public void setOccur_Time(String occur_Time) {
        this.occur_Time = occur_Time;
    }

    public String getFault_Report_Name() {
        return fault_Report_Name;
    }

    public void setFault_Report_Name(String fault_Report_Name) {
        this.fault_Report_Name = fault_Report_Name;
    }

    public String getReport_Time() {
        return report_Time;
    }

    public void setReport_Time(String report_Time) {
        this.report_Time = report_Time;
    }

    public String getReport_Phone() {
        return report_Phone;
    }

    public void setReport_Phone(String report_Phone) {
        this.report_Phone = report_Phone;
    }

    public String getFault_Contact_Name() {
        return fault_Contact_Name;
    }

    public void setFault_Contact_Name(String fault_Contact_Name) {
        this.fault_Contact_Name = fault_Contact_Name;
    }

    public String getContact_Phone() {
        return contact_Phone;
    }

    public void setContact_Phone(String contact_Phone) {
        this.contact_Phone = contact_Phone;
    }

    public String getContact_Address() {
        return contact_Address;
    }

    public void setContact_Address(String contact_Address) {
        this.contact_Address = contact_Address;
    }

    public String getSerial_Number() {
        return serial_Number;
    }

    public void setSerial_Number(String serial_Number) {
        this.serial_Number = serial_Number;
    }

    public String getTrading_Name() {
        return trading_Name;
    }

    public void setTrading_Name(String trading_Name) {
        this.trading_Name = trading_Name;
    }

    public String getError_Information() {
        return error_Information;
    }

    public void setError_Information(String error_Information) {
        this.error_Information = error_Information;
    }

    public String getFault_Decript_Summary() {
        return fault_Decript_Summary;
    }

    public void setFault_Decript_Summary(String fault_Decript_Summary) {
        this.fault_Decript_Summary = fault_Decript_Summary;
    }

    public String getFault_Decript_Detail() {
        return fault_Decript_Detail;
    }

    public void setFault_Decript_Detail(String fault_Decript_Detail) {
        this.fault_Decript_Detail = fault_Decript_Detail;
    }

    public String getCreat_Time() {
        return creat_Time;
    }

    public void setCreat_Time(String creat_Time) {
        this.creat_Time = creat_Time;
    }

    public String getCurrent_State() {
        return current_State;
    }

    public void setCurrent_State(String current_State) {
        this.current_State = current_State;
    }

    public String getInvolve_Amount() {
        return involve_Amount;
    }

    public void setInvolve_Amount(String involve_Amount) {
        this.involve_Amount = involve_Amount;
    }

    public String getInvolve_Scount() {
        return involve_Scount;
    }

    public void setInvolve_Scount(String involve_Scount) {
        this.involve_Scount = involve_Scount;
    }

    public String getCustomer_Name() {
        return customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        this.customer_Name = customer_Name;
    }

    public String getCustomer_IDcard() {
        return customer_IDcard;
    }

    public void setCustomer_IDcard(String customer_IDcard) {
        this.customer_IDcard = customer_IDcard;
    }

    public String getTransaction_Account() {
        return transaction_Account;
    }

    public void setTransaction_Account(String transaction_Account) {
        this.transaction_Account = transaction_Account;
    }

    public String getTransaction_Amount() {
        return transaction_Amount;
    }

    public void setTransaction_Amount(String transaction_Amount) {
        this.transaction_Amount = transaction_Amount;
    }

    public String getOrder_Number() {
        return order_Number;
    }

    public void setOrder_Number(String order_Number) {
        this.order_Number = order_Number;
    }

    public String getFm_Cause() {
        return fm_Cause;
    }

    public void setFm_Cause(String fm_Cause) {
        this.fm_Cause = fm_Cause;
    }

    public String getReceiver_IDs() {
        return receiver_IDs;
    }

    public void setReceiver_IDs(String receiver_IDs) {
        this.receiver_IDs = receiver_IDs;
    }

    public String getInvalidation_Mark() {
        return invalidation_Mark;
    }

    public void setInvalidation_Mark(String invalidation_Mark) {
        this.invalidation_Mark = invalidation_Mark;
    }

    public String getFile_Path() {
        return file_Path;
    }

    public void setFile_Path(String file_Path) {
        this.file_Path = file_Path;
    }

    public String getS_LOG_performDesc() {
        return s_LOG_performDesc;
    }

    public void setS_LOG_performDesc(String s_LOG_performDesc) {
        this.s_LOG_performDesc = s_LOG_performDesc;
    }

    public String getOneType() {
        return oneType;
    }

    public void setOneType(String oneType) {
        this.oneType = oneType;
    }

    public String getTwoType() {
        return twoType;
    }

    public void setTwoType(String twoType) {
        this.twoType = twoType;
    }

    public String getThreeType() {
        return threeType;
    }

    public void setThreeType(String threeType) {
        this.threeType = threeType;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
