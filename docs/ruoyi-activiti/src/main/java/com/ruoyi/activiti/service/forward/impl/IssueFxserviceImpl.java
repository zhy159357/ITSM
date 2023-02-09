package com.ruoyi.activiti.service.forward.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.activiti.domain.ImBizIssuefx;
import com.ruoyi.activiti.domain.PubRelation;
import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.activiti.service.IImBizIssuefxService;
import com.ruoyi.activiti.service.IPubRelationService;
import com.ruoyi.activiti.service.IVmBizInfoService;
import com.ruoyi.activiti.service.forward.IssueFxService;
import com.ruoyi.common.core.BusException;
import com.ruoyi.common.core.Record;
import com.ruoyi.common.core.ServiceParam;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.VmBizInfo;
import com.ruoyi.common.esb.data.EsbServiceMapping;
import com.ruoyi.common.netty.utils.Pager;
import com.ruoyi.common.netty.utils.PagerRecords;
import com.ruoyi.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("issueFxManager")
@Transactional(rollbackFor = Exception.class)
public class IssueFxserviceImpl implements IssueFxService {
    private static final Logger logger = LoggerFactory.getLogger(IssueFxserviceImpl.class);
    @Autowired
    private IPubRelationService pubRelationService;
    @Autowired
    private IFmBizService iFmBizService;
    @Autowired
    private IVmBizInfoService iVmBizInfoService;
    @Autowired
    private IImBizIssuefxService issueService;
    /**
     *  810004-#查询关联业务事件单
     * @param pager
     * @param params
     * @return
     * @throws BusException
     */
    @Override
    @EsbServiceMapping
    public PagerRecords getfmFmBizOrFmJk(Pager pager, Map<String,Object> params) throws BusException {
        logger.info("=========软研调用查询问题单关联业务事件单接口810004开始===========");

        if(params==null){
            throw new BusException("100004", "入参不全");
        }
        if(params.get("obj2Id")==null||params.get("type")==null){
            throw new BusException("100004", "入参不全");
        }
        String obj2Id=params.get("obj2Id").toString();
        String type=params.get("type").toString();
        PubRelation pubRelation=new PubRelation();
        pubRelation.setObj2Id(obj2Id);
        pubRelation.setType(type);
        List<PubRelation> pubRelationList=pubRelationService.selectPubRelationList(pubRelation);
        List<Record> recordList = new ArrayList<Record>();
        for(PubRelation pn:pubRelationList){
            String fmbizId=pn.getObj1Id();
            Record record = new Record();
            FmBiz fmBiz=iFmBizService.selectFmBizById(fmbizId);
            record.put("fmId", fmBiz.getFmId());// 业务事件单id
            record.put("faultNo", fmBiz.getFaultNo());// 事件单号
            record.put("title", fmBiz.getFaultDecriptSummary());// 标题
            if (fmBiz.getFmKind() !=null) {
                record.put("kindName", fmBiz.getFmKind());// 事件分类
            }
            if (fmBiz.getOgSys() !=null) {
                record.put("sysCaption", fmBiz.getOgSys());// 所属应用系统
            }
            record.put("currentState", fmBiz.getCurrentState());// 当前状态
            record.put("createTime", fmBiz.getCreateTime());// 创建时间
            recordList.add(record);
        }
        Page<FmBiz> FmBizPage = PageHelper.startPage(pager.getPageIndex(), pager.getPageSize(), true);
        PagerRecords pagerRecords = new PagerRecords(recordList, (int) FmBizPage.getTotal());
        pagerRecords.setPager(pager);
        logger.info(pagerRecords.toString());
        logger.info("=========软研调用查询问题单关联业务事件单接口810004结束===========");
        return pagerRecords;
    }
    /**
     *  810006-查询关联版本单
     * @param issueFxId
     * @return
     * @throws BusException
     */
    @Override
    @EsbServiceMapping
    public List<Record> getVmBizByFoNo(@ServiceParam(name = "issueFxId") String issueFxId) throws BusException {
        logger.info("=========810006软研调用查询问题单关联版本单810006接口开始===========");
        ImBizIssuefx imBizIssuefx=issueService.selectImBizIssuefxById(issueFxId);
        String fono=imBizIssuefx.getFuNo();
        if(StringUtils.isEmpty(fono)){
            throw new BusException("100004", "未查询到相关版本单号！");
        }
        List<Record> vmBizList= new ArrayList<Record>();
        String[] fonoList=fono.split(",");
        for(String id:fonoList){
            VmBizInfo vmBizByFoNo = iVmBizInfoService.selectVmBizInfoById(id);
                Record record = new Record();
                record.put("versionInfoId", vmBizByFoNo.getVersionInfoId());// 版本单id
                record.put("versionInfoNo", vmBizByFoNo.getVersionInfoNo());// 版本编号
                record.put("versionInfoName", vmBizByFoNo.getVersionName());//版本简称
                if (vmBizByFoNo.getVersionProducerName() !=null) {
                    record.put("versionProducerPname", vmBizByFoNo.getVersionProducerName());// 版本发起人
                }
                record.put("startUpgraedTime", vmBizByFoNo.getStartUpgradeTime());// 升级时间
                if (vmBizByFoNo.getSystemName() !=null) {
                    record.put("sysCaption", vmBizByFoNo.getSystemName());// 所属应用系统
                }
                vmBizList.add(record);
        }
        logger.info(vmBizList.toString());
        logger.info("=========软研调用查询问题单关联版本单接口810006结束===========");
        return vmBizList;
    }

    /**
     * 810018-- 查询转发次数
     * @param bizId
     * @return
     * @throws BusException
     */
    @Override
    @EsbServiceMapping
    public String ifMultiCount(@ServiceParam(name="bizId") String bizId) throws BusException{
        logger.info("==============810018-- 查询转发次数 开始==================");
        ImBizIssuefx imBizIssuefx=issueService.selectImBizIssuefxById(bizId);
        logger.info(imBizIssuefx.toString());
        logger.info("==============810018-- 查询转发次数 开始==================");
        return String.valueOf(imBizIssuefx.getMulticount());
    }
}
