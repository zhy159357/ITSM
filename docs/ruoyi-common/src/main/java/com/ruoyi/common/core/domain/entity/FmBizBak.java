package com.ruoyi.common.core.domain.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 业务事件单对象 fm_biz
 *
 * @author ruoyi
 * @date 2020-12-23
 */
public class FmBizBak extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private String auditorId;

    /**
     * $column.columnComment
     */
    private String occurrenceOrgId;

    /**
     * $column.columnComment
     */
    private String evaluaterId;
    @Excel(name = "评价人", sort = 41)
    private String evaluateName;
    /**
     * 事件单编号
     */
    @Excel(name = "事件单编号", sort = 1)
    private String faultNo;

    /**
     * 事件来源
     */
    @Excel(name = "事件来源", sort = 19, readConverterExp = "01=手工事件单,02=监控事件单,03=电话银行,04=信用卡,05=电话事件单转")
    private String faultSource;

    /**
     * 事件等级
     */
    @Excel(name = "事件等级", sort = 9, readConverterExp = "1=低,2=中,3=高")
    private String seriousLev;

    /**
     * 发生地址
     */
    @Excel(name = "交易机构", sort = 29)
    private String occurrenceAddress;

    /**
     * 发生时间
     */
    @Excel(name = "发生时间", sort = 20)
    private String occurTime;

    /**
     * 报告人
     */
    @Excel(name = "当事人", sort = 21)
    private String faultReportName;

    /**
     * 报告时间
     */
    @Excel(name = "报告时间", sort = 23)
    private String reportTime;

    /**
     * 报告人电话
     */
    @Excel(name = "当事人电话", sort = 22)
    private String reportPhone;

    /**
     * 联系人
     */
    @Excel(name = "填报人", sort = 24)
    private String faultContactName;

    /**
     * 联系人电话
     */
    @Excel(name = "填报人电话", sort = 25)
    private String contactPhone;

    /**
     * 联系人地址
     */
    @Excel(name = "填报人地址", sort = 26)
    private String contactAddress;

    /**
     * 流水号
     */
    @Excel(name = "流水号", sort = 31)
    private String serialNumber;

    /**
     * 交易名称
     */
    @Excel(name = "交易名称", sort = 30)
    private String tradingName;

    /**
     * 错误信息
     */
    @Excel(name = "错误信息", sort = 37)
    private String errorInformation;

    /**
     * 事件标题
     */
    @Excel(name = "事件标题", sort = 2)
    private String faultDecriptSummary;

    /**
     * 事件描述
     */
    @Excel(name = "事件描述", sort = 38)
    private String faultDecriptDetail;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间", sort = 12)
    private String creatTime;

    /**
     * 处理时间
     */
    @Excel(name = "处理时间", sort = 17)
    private String dealTime;

    /**
     * 处理描述
     */
    @Excel(name = "处理描述", sort = 39)
    private String dealDescription;

    /**
     * 评价
     */
    @Excel(name = "评价", sort = 40)
    private String evaluate;

    /**
     * 评价时间，用于运营事件单评价
     */
    @Excel(name = "评价时间", sort = 42)
    private String evaluateTime;

    /**
     * 关闭时间
     */
    private String endTime;

    /**
     * 当前状态
     */
    @Excel(name = "当前状态", sort = 10, readConverterExp = "01=待提交,02=待分派,03=待处理,04=处理中,05=退回,06=待评价,07=省中心退回,08=待领取(省),09=关闭,10=作废,11=处理中(省)")
    private String currentState;

    /**
     * auditStatus
     */
    private String auditStatus;

    /**
     * auditStatus
     */
    private String auditTime;

    private String n3;

    private String n2;

    private String n4;

    private String n1;

    private String n5;

    private String dealUseTime;

    /**
     * 涉事金额
     */
    @Excel(name = "涉事金额", sort = 28, readConverterExp = "0=0元,1=10万元以内,2=10万至50万元,3=50万元以上")
    private String involveAmount;

    /**
     * 涉事笔数
     */
    @Excel(name = "涉事笔数", sort = 27)
    private String involveScount;

    /**
     * 客户姓名
     */
    @Excel(name = "客户姓名", sort = 32)
    private String customerName;

    /**
     * 客户身份证号
     */
    @Excel(name = "客户身份证号", sort = 33)
    private String customerIdcard;

    /**
     * 交易账号
     */
    @Excel(name = "交易账号", sort = 34)
    private String transactionAccount;

    /**
     * 交易金额
     */
    @Excel(name = "交易金额", sort = 35)
    private String transactionAmount;

    /**
     * 订单号
     */
    @Excel(name = "订单号", sort = 36)
    private String orderNumber;

    /**
     * 评价结果：满意、不满意
     */
    private String evaluateResult;

    /**
     * 事件起因
     */
    @Excel(name = "事件起因", sort = 8, readConverterExp = "01=业务人员培训不到位,02=版本或变更引起,03=系统功能不完善或不满足,04=信息系统原因,05=系统程序BUG,06=数据一致性问题,07=行内关联系统原因,08=客户操作问题,09=第三方或他行系统原因,10=省内系统原因,11=数据分析相关咨询,12=其他或疑难,13=业务人员操作有误")
    private String fmCause;

    /**
     * 监控标记
     */
    private String fmJkmark;

    /**
     * 参与处理人员ID,用于过滤我处理过的事件单
     */
    private String participatorIds;

    /**
     * 处理方式
     */
    @Excel(name = "处理方式", sort = 16, readConverterExp = "01=正常处理,02=疑难事件处理,03=多方配合处理,04=数据变更处理,05=差错处理")
    private String dealMode;

    /**
     * 参与工作组ID,用于过滤工作组参与过的事件单
     */
    private String participateGroupids;

    /**
     * 转全国中心时间
     */
    @Excel(name = "转全国中心时间", sort = 13)
    private String toQgzxTime;

    /**
     * 退回省中心时间
     */
    private String backSzxTime;

    /**
     * 转发原因：1多方配合处理，2非本系统，用于统计省转全国中心选择的工作组是否正确
     */
    private String repeatMark;

    /**
     * 领取人ID,用于统计人员领取的事件单数量
     */
    private String receiverIds;

    /**
     * 无效标志，0：无效，1：有效
     */
    private String invalidationMark;

    /**
     * 是否紧急
     */
    @Excel(name = "是否紧急", sort = 8, readConverterExp = "1=是,2=否")
    private String ifJj;

    /**
     * 是否通报
     */
    private String ifBulletion;

    /**
     * 通报原因
     */
    private String bullCause;

    /**
     * 通报时间
     */
    private String bullTime;

    /**
     * 是否是反欺诈相关 1-是  0-否
     */
    private String isAntiFraud;

    /**
     * 是否发送成功  1-是 0-否
     */
    private String isSend;

    /**
     * 事件单ID
     */
    private String fmId;

    /**
     * 创建人
     */
    private String createId;
    @Excel(name = "创建人", sort = 11)
    private String createName;
    /**
     * 创建机构
     */
    private String createOrgId;

    /**
     * 工作组
     */
    private String groupId;
    @Excel(name = "所属工作组", sort = 4)
    private String createGroupName;
    /**
     * 处理人
     */
    private String dealerId;
    @Excel(name = "处理人", sort = 15)
    private String dealName;
    /**
     * 处理组
     */
    private String dealGroupId;
    @Excel(name = "处理组", sort = 14)
    private String dealGroupName;
    /**
     * 类别
     */
    private String typeinfoId;

    /**
     * 系统
     */
    private String sysid;

    /**
     * $column.columnComment
     */
    private String fmTypeId;

    /**
     * 应用系统作为关联用
     */
    private OgSys ogSys;
    @Excel(name = "所属系统", sort = 3)
    private String systemName;
    /**
     * 应用系统分类作为关联用
     */
    private FmKind fmKind;

    @Excel(name = "分类", sort = 5)
    private String fmKindName;
    /**
     * 应用系统工作组作为关联用
     */
    private OgOrg ogOrg;
    @Excel(name = "发生机构", sort = 18)
    private String orgName;
    /**
     *
     */
    private String groupCountdown;
    /**
     *
     */
    private String fmbizCountdown;
    /**
     *
     */
    private String ifdz;
    /**
     * 紧急事件单统计展示使用
     */
    private String couNum;//事件单数量
    private String jjCouNum;//紧急数量
    private String countEfficiency;//占比
    /**
     * 2021/6/8需求修改新增对接知识
     */
    private String oneTypeName;//导出当前一级分类
    private String oneType;//当前一级分类
    private String iniSys;//初始系统
    private String iniSysName;//初始系统
    private String twoTypeName;//导出当前二级分类
    private String twoType;//当前二级分类
    private String threeTypeName;//导出当前三级分类
    private String threeType;//当前三级分类
    private String keywords;//当前关键字
    private String iniOneType;//初始一级分类
    private String iniOneTypeName;//初始一级分类名称
    private String iniTwoType;//初始二级分类
    private String iniTwoTypeName;//初始二级分类名称
    private String iniThreeType;//初始三级分类
    private String iniThreeTypeName;//初始三级分类名称
    private String iniKeywords;//初始关键字
    private String knowledgeId;//对应知识

    public String getIniSysName() {
        return iniSysName;
    }

    public void setIniSysName(String iniSysName) {
        this.iniSysName = iniSysName;
    }

    public String getIniOneTypeName() {
        return iniOneTypeName;
    }

    public void setIniOneTypeName(String iniOneTypeName) {
        this.iniOneTypeName = iniOneTypeName;
    }

    public String getIniTwoTypeName() {
        return iniTwoTypeName;
    }

    public void setIniTwoTypeName(String iniTwoTypeName) {
        this.iniTwoTypeName = iniTwoTypeName;
    }

    public String getIniThreeTypeName() {
        return iniThreeTypeName;
    }

    public void setIniThreeTypeName(String iniThreeTypeName) {
        this.iniThreeTypeName = iniThreeTypeName;
    }

    public String getOneTypeName() {
        return oneTypeName;
    }

    public void setOneTypeName(String oneTypeName) {
        this.oneTypeName = oneTypeName;
    }

    public String getTwoTypeName() {
        return twoTypeName;
    }

    public void setTwoTypeName(String twoTypeName) {
        this.twoTypeName = twoTypeName;
    }

    public String getThreeTypeName() {
        return threeTypeName;
    }

    public void setThreeTypeName(String threeTypeName) {
        this.threeTypeName = threeTypeName;
    }

    public String getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(String knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public String getOneType() {
        return oneType;
    }

    public void setOneType(String oneType) {
        this.oneType = oneType;
    }

    public String getIniSys() {
        return iniSys;
    }

    public void setIniSys(String iniSys) {
        this.iniSys = iniSys;
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

    public String getIniOneType() {
        return iniOneType;
    }

    public void setIniOneType(String iniOneType) {
        this.iniOneType = iniOneType;
    }

    public String getIniTwoType() {
        return iniTwoType;
    }

    public void setIniTwoType(String iniTwoType) {
        this.iniTwoType = iniTwoType;
    }

    public String getIniThreeType() {
        return iniThreeType;
    }

    public void setIniThreeType(String iniThreeType) {
        this.iniThreeType = iniThreeType;
    }

    public String getIniKeywords() {
        return iniKeywords;
    }

    public void setIniKeywords(String iniKeywords) {
        this.iniKeywords = iniKeywords;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setOccurrenceOrgId(String occurrenceOrgId) {
        this.occurrenceOrgId = occurrenceOrgId;
    }

    public String getOccurrenceOrgId() {
        return occurrenceOrgId;
    }

    public void setEvaluaterId(String evaluaterId) {
        this.evaluaterId = evaluaterId;
    }

    public String getEvaluaterId() {
        return evaluaterId;
    }

    public void setFaultNo(String faultNo) {
        this.faultNo = faultNo;
    }

    public String getFaultNo() {
        return faultNo;
    }

    public void setFaultSource(String faultSource) {
        this.faultSource = faultSource;
    }

    public String getFaultSource() {
        return faultSource;
    }

    public void setSeriousLev(String seriousLev) {
        this.seriousLev = seriousLev;
    }

    public String getSeriousLev() {
        return seriousLev;
    }

    public void setOccurrenceAddress(String occurrenceAddress) {
        this.occurrenceAddress = occurrenceAddress;
    }

    public String getOccurrenceAddress() {
        return occurrenceAddress;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
    }

    public String getOccurTime() {
        return occurTime;
    }

    public void setFaultReportName(String faultReportName) {
        this.faultReportName = faultReportName;
    }

    public String getFaultReportName() {
        return faultReportName;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportPhone(String reportPhone) {
        this.reportPhone = reportPhone;
    }

    public String getReportPhone() {
        return reportPhone;
    }

    public void setFaultContactName(String faultContactName) {
        this.faultContactName = faultContactName;
    }

    public String getFaultContactName() {
        return faultContactName;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public String getTradingName() {
        return tradingName;
    }

    public void setErrorInformation(String errorInformation) {
        this.errorInformation = errorInformation;
    }

    public String getErrorInformation() {
        return errorInformation;
    }

    public void setFaultDecriptSummary(String faultDecriptSummary) {
        this.faultDecriptSummary = faultDecriptSummary;
    }

    public String getFaultDecriptSummary() {
        return faultDecriptSummary;
    }

    public void setFaultDecriptDetail(String faultDecriptDetail) {
        this.faultDecriptDetail = faultDecriptDetail;
    }

    public String getFaultDecriptDetail() {
        return faultDecriptDetail;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getDealTime() {
        return dealTime;
    }

    public void setDealDescription(String dealDescription) {
        this.dealDescription = dealDescription;
    }

    public String getDealDescription() {
        return dealDescription;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluateTime(String evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public String getEvaluateTime() {
        return evaluateTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setN3(String n3) {
        this.n3 = n3;
    }

    public String getN3() {
        return n3;
    }

    public void setN2(String n2) {
        this.n2 = n2;
    }

    public String getN2() {
        return n2;
    }

    public void setN4(String n4) {
        this.n4 = n4;
    }

    public String getN4() {
        return n4;
    }

    public void setN1(String n1) {
        this.n1 = n1;
    }

    public String getN1() {
        return n1;
    }

    public void setN5(String n5) {
        this.n5 = n5;
    }

    public String getN5() {
        return n5;
    }

    public void setDealUseTime(String dealUseTime) {
        this.dealUseTime = dealUseTime;
    }

    public String getDealUseTime() {
        return dealUseTime;
    }

    public void setInvolveAmount(String involveAmount) {
        this.involveAmount = involveAmount;
    }

    public String getInvolveAmount() {
        return involveAmount;
    }

    public void setInvolveScount(String involveScount) {
        this.involveScount = involveScount;
    }

    public String getInvolveScount() {
        return involveScount;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerIdcard(String customerIdcard) {
        this.customerIdcard = customerIdcard;
    }

    public String getCustomerIdcard() {
        return customerIdcard;
    }

    public void setTransactionAccount(String transactionAccount) {
        this.transactionAccount = transactionAccount;
    }

    public String getTransactionAccount() {
        return transactionAccount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setEvaluateResult(String evaluateResult) {
        this.evaluateResult = evaluateResult;
    }

    public String getEvaluateResult() {
        return evaluateResult;
    }

    public void setFmCause(String fmCause) {
        this.fmCause = fmCause;
    }

    public String getFmCause() {
        return fmCause;
    }

    public void setFmJkmark(String fmJkmark) {
        this.fmJkmark = fmJkmark;
    }

    public String getFmJkmark() {
        return fmJkmark;
    }

    public void setParticipatorIds(String participatorIds) {
        this.participatorIds = participatorIds;
    }

    public String getParticipatorIds() {
        return participatorIds;
    }

    public void setDealMode(String dealMode) {
        this.dealMode = dealMode;
    }

    public String getDealMode() {
        return dealMode;
    }

    public void setParticipateGroupids(String participateGroupids) {
        this.participateGroupids = participateGroupids;
    }

    public String getParticipateGroupids() {
        return participateGroupids;
    }

    public void setToQgzxTime(String toQgzxTime) {
        this.toQgzxTime = toQgzxTime;
    }

    public String getToQgzxTime() {
        return toQgzxTime;
    }

    public void setBackSzxTime(String backSzxTime) {
        this.backSzxTime = backSzxTime;
    }

    public String getBackSzxTime() {
        return backSzxTime;
    }

    public void setRepeatMark(String repeatMark) {
        this.repeatMark = repeatMark;
    }

    public String getRepeatMark() {
        return repeatMark;
    }

    public void setReceiverIds(String receiverIds) {
        this.receiverIds = receiverIds;
    }

    public String getReceiverIds() {
        return receiverIds;
    }

    public void setInvalidationMark(String invalidationMark) {
        this.invalidationMark = invalidationMark;
    }

    public String getInvalidationMark() {
        return invalidationMark;
    }

    public void setIfJj(String ifJj) {
        this.ifJj = ifJj;
    }

    public String getIfJj() {
        return ifJj;
    }

    public void setIfBulletion(String ifBulletion) {
        this.ifBulletion = ifBulletion;
    }

    public String getIfBulletion() {
        return ifBulletion;
    }

    public void setBullCause(String bullCause) {
        this.bullCause = bullCause;
    }

    public String getBullCause() {
        return bullCause;
    }

    public void setBullTime(String bullTime) {
        this.bullTime = bullTime;
    }

    public String getBullTime() {
        return bullTime;
    }

    public void setIsAntiFraud(String isAntiFraud) {
        this.isAntiFraud = isAntiFraud;
    }

    public String getIsAntiFraud() {
        return isAntiFraud;
    }

    public void setIsSend(String isSend) {
        this.isSend = isSend;
    }

    public String getIsSend() {
        return isSend;
    }

    public void setFmId(String fmId) {
        this.fmId = fmId;
    }

    public String getFmId() {
        return fmId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateOrgId(String createOrgId) {
        this.createOrgId = createOrgId;
    }

    public String getCreateOrgId() {
        return createOrgId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealGroupId(String dealGroupId) {
        this.dealGroupId = dealGroupId;
    }

    public String getDealGroupId() {
        return dealGroupId;
    }

    public void setTypeinfoId(String typeinfoId) {
        this.typeinfoId = typeinfoId;
    }

    public String getTypeinfoId() {
        return typeinfoId;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public String getSysid() {
        return sysid;
    }

    public void setFmTypeId(String fmTypeId) {
        this.fmTypeId = fmTypeId;
    }

    public String getFmTypeId() {
        return fmTypeId;
    }

    public OgSys getOgSys() {
        return ogSys;
    }

    public void setOgSys(OgSys ogSys) {
        if (ogSys != null) {
            this.systemName = ogSys.getCaption();
        } else {
            ogSys = new OgSys();
        }
        this.ogSys = ogSys;
    }

    public FmKind getFmKind() {
        return fmKind;
    }

    public void setFmKind(FmKind fmKind) {

        if (fmKind != null) {
            this.fmKindName = fmKind.getName();
        } else {
            fmKind = new FmKind();
        }
        this.fmKind = fmKind;
    }

    public OgOrg getOgOrg() {
        return ogOrg;
    }

    public void setOgOrg(OgOrg ogOrg) {

        if (ogOrg != null) {
            this.orgName = ogOrg.getOrgName();
        } else {
            ogOrg = new OgOrg();
        }
        this.ogOrg = ogOrg;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateGroupName() {
        return createGroupName;
    }

    public void setCreateGroupName(String createGroupName) {
        this.createGroupName = createGroupName;
    }

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    public String getDealGroupName() {
        return dealGroupName;
    }

    public void setDealGroupName(String dealGroupName) {
        this.dealGroupName = dealGroupName;
    }

    public String getEvaluateName() {
        return evaluateName;
    }

    public void setEvaluateName(String evaluateName) {
        this.evaluateName = evaluateName;
    }

    public String getGroupCountdown() {
        return groupCountdown;
    }

    public void setGroupCountdown(String groupCountdown) {
        this.groupCountdown = groupCountdown;
    }

    public String getFmbizCountdown() {
        return fmbizCountdown;
    }

    public void setFmbizCountdown(String fmbizCountdown) {
        this.fmbizCountdown = fmbizCountdown;
    }

    public String getIfdz() {
        return ifdz;
    }

    public void setIfdz(String ifdz) {
        this.ifdz = ifdz;
    }

    public String getCouNum() {
        return couNum;
    }

    public void setCouNum(String couNum) {
        this.couNum = couNum;
    }

    public String getJjCouNum() {
        return jjCouNum;
    }

    public void setJjCouNum(String jjCouNum) {
        this.jjCouNum = jjCouNum;
    }

    public String getCountEfficiency() {
        return countEfficiency;
    }

    public void setCountEfficiency(String countEfficiency) {
        this.countEfficiency = countEfficiency;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("auditorId", getAuditorId())
                .append("occurrenceOrgId", getOccurrenceOrgId())
                .append("evaluaterId", getEvaluaterId())
                .append("faultNo", getFaultNo())
                .append("faultSource", getFaultSource())
                .append("seriousLev", getSeriousLev())
                .append("occurrenceAddress", getOccurrenceAddress())
                .append("occurTime", getOccurTime())
                .append("faultReportName", getFaultReportName())
                .append("reportTime", getReportTime())
                .append("reportPhone", getReportPhone())
                .append("faultContactName", getFaultContactName())
                .append("contactPhone", getContactPhone())
                .append("contactAddress", getContactAddress())
                .append("serialNumber", getSerialNumber())
                .append("tradingName", getTradingName())
                .append("errorInformation", getErrorInformation())
                .append("faultDecriptSummary", getFaultDecriptSummary())
                .append("faultDecriptDetail", getFaultDecriptDetail())
                .append("creatTime", getCreatTime())
                .append("dealTime", getDealTime())
                .append("dealDescription", getDealDescription())
                .append("evaluate", getEvaluate())
                .append("evaluateTime", getEvaluateTime())
                .append("endTime", getEndTime())
                .append("currentState", getCurrentState())
                .append("auditStatus", getAuditStatus())
                .append("auditTime", getAuditTime())
                .append("n3", getN3())
                .append("n2", getN2())
                .append("n4", getN4())
                .append("n1", getN1())
                .append("n5", getN5())
                .append("dealUseTime", getDealUseTime())
                .append("involveAmount", getInvolveAmount())
                .append("involveScount", getInvolveScount())
                .append("customerName", getCustomerName())
                .append("customerIdcard", getCustomerIdcard())
                .append("transactionAccount", getTransactionAccount())
                .append("transactionAmount", getTransactionAmount())
                .append("orderNumber", getOrderNumber())
                .append("evaluateResult", getEvaluateResult())
                .append("fmCause", getFmCause())
                .append("fmJkmark", getFmJkmark())
                .append("participatorIds", getParticipatorIds())
                .append("dealMode", getDealMode())
                .append("participateGroupids", getParticipateGroupids())
                .append("toQgzxTime", getToQgzxTime())
                .append("backSzxTime", getBackSzxTime())
                .append("repeatMark", getRepeatMark())
                .append("receiverIds", getReceiverIds())
                .append("invalidationMark", getInvalidationMark())
                .append("ifJj", getIfJj())
                .append("ifBulletion", getIfBulletion())
                .append("bullCause", getBullCause())
                .append("bullTime", getBullTime())
                .append("isAntiFraud", getIsAntiFraud())
                .append("isSend", getIsSend())
                .append("fmId", getFmId())
                .append("createId", getCreateId())
                .append("createOrgId", getCreateOrgId())
                .append("groupId", getGroupId())
                .append("dealerId", getDealerId())
                .append("dealGroupId", getDealGroupId())
                .append("typeinfoId", getTypeinfoId())
                .append("sysid", getSysid())
                .append("fmTypeId", getFmTypeId())
                .toString();
    }
}
