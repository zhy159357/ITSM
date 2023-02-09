package com.ruoyi.activiti.service.forward.impl;

import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.activiti.service.forward.IfmBizFaultinfoService;
import com.ruoyi.activiti.web.EsbServiceMapping;
import com.ruoyi.common.core.BusException;
import com.ruoyi.common.core.Record;
import com.ruoyi.common.core.ServiceParam;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("fmBizFaultinfoManager")
public class FmBizFaultinfoServiceImpl implements IfmBizFaultinfoService {
    private static final Logger logger = LoggerFactory.getLogger(FmBizFaultinfoServiceImpl.class);
    @Autowired
    private IFmBizService iFmBizService;
    @Autowired
    private IOgPersonService ogPersonService;
    @Autowired
    ISysDeptService iSysDeptService;
    /**
     * 810007-查询业务事件单详情信息
     * @param id
     * @return
     * @throws BusException
     */
    @Override
    @EsbServiceMapping
    public Record getFmBizFaultinfo(@ServiceParam(name = "fmId") String id) throws BusException {
        logger.info("================810007-查询业务事件单详情信息开始===================");
        FmBiz fmBizFaultinfo = iFmBizService.selectFmBizById(id);
        Record record = new Record();
        record.put("fmId", fmBizFaultinfo.getFmId());//业务事件单id
        record.put("faultNo", fmBizFaultinfo.getFaultNo());//事件单编号
        record.put("faultSource", fmBizFaultinfo.getFaultSource());//事件来源
        if (fmBizFaultinfo.getCreateOrgId() !=null) {
            OgOrg ogOrg=iSysDeptService.selectDeptById(fmBizFaultinfo.getCreateOrgId());
            record.put("oocAgencyOrgname", ogOrg.getOrgName());//创建机构
        }
        record.put("reportTime", fmBizFaultinfo.getReportTime());//报告时间
        record.put("contactName", fmBizFaultinfo.getFaultContactName());//填报人
        record.put("contactPhone", fmBizFaultinfo.getContactPhone());//填报人电话
        record.put("contactAddress", fmBizFaultinfo.getContactAddress());//发生地址
        if (fmBizFaultinfo.getOgSys() !=null) {
            record.put("sysCaption", fmBizFaultinfo.getOgSys());//所属应用系统
        }
        if (fmBizFaultinfo.getCreateGroupName() !=null) {
            record.put("groupGrpname", fmBizFaultinfo.getCreateGroupName());//所属工作组
        }
        if (fmBizFaultinfo.getFmKind() !=null) {
            record.put("kindName", fmBizFaultinfo.getFmKind());//事件分类
        }
        record.put("cause", fmBizFaultinfo.getFmCause());//事件起因
        record.put("involveSCount", fmBizFaultinfo.getInvolveScount());//涉事笔数
        record.put("involveAmount", fmBizFaultinfo.getInvolveAmount());//涉事金额
        record.put("fmLevel", fmBizFaultinfo.getSeriousLev());//事件等级
        record.put("tAddress", fmBizFaultinfo.getOccurrenceAddress());//交易机构
        record.put("tName", fmBizFaultinfo.getTradingName());//交易名称/代码
        record.put("serialNumber", fmBizFaultinfo.getSerialNumber());//流水号
        record.put("customerName", fmBizFaultinfo.getCustomerName());//客户姓名
        record.put("customerIdCard", fmBizFaultinfo.getCustomerIdcard());//客户身份证号
        record.put("transactionAccount", fmBizFaultinfo.getTransactionAccount());//交易账号
        record.put("transactionAmount", fmBizFaultinfo.getTransactionAmount());//交易金额
        record.put("orderNumber", fmBizFaultinfo.getOrderNumber());//订单号
        record.put("errorInformation", fmBizFaultinfo.getErrorInformation());//错误信息
        record.put("occTime", fmBizFaultinfo.getOccurTime());//发生时间
        record.put("reportName", fmBizFaultinfo.getFaultReportName());//当事人
        record.put("reportPhone", fmBizFaultinfo.getReportPhone());//当事人电话
        record.put("title", fmBizFaultinfo.getFaultDecriptSummary());//事件标题
        record.put("fmDesc", fmBizFaultinfo.getFaultDecriptDetail());//事件描述
        if (fmBizFaultinfo.getCreateId() !=null) {
            OgPerson createperson=ogPersonService.selectOgPersonById(fmBizFaultinfo.getCreateId());
            record.put("createrPname", createperson.getpName());//创建人员
        }
        record.put("createTime", fmBizFaultinfo.getCreateTime());//创建时间
        record.put("dealDesc", fmBizFaultinfo.getFaultDecriptDetail());//处理描述
        if (fmBizFaultinfo.getParticipatorIds() !=null) {
            OgPerson participator=ogPersonService.selectOgPersonById(fmBizFaultinfo.getCreateId());
            record.put("dealerPname", participator.getpName());//处理人
        }
        if (fmBizFaultinfo.getDealGroupName() !=null) {
            record.put("dealGroupGrpname", fmBizFaultinfo.getDealGroupName());//处理组
        }
        record.put("dealTime", fmBizFaultinfo.getDealTime());//处理时间
        record.put("dealMode", fmBizFaultinfo.getDealMode());//处理方式
        record.put("evaluate", fmBizFaultinfo.getEvaluate());//评价
        if (fmBizFaultinfo.getEvaluateName() !=null) {
            record.put("evaluaterPname", fmBizFaultinfo.getEvaluateName());//评价人
        }
        record.put("evaluateTime", fmBizFaultinfo.getEvaluateTime());//评价时间
        logger.info(record.toString());
        logger.info("================810007-查询业务事件单详情信息结束===================");
        return record;
    }
}
