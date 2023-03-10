package com.ruoyi.activiti.controller.itsm.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ruoyi.activiti.domain.PubRelation;
import com.ruoyi.activiti.domain.TelBiz;
import com.ruoyi.activiti.domain.TelFlowLog;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.activiti.service.CustInfoService;
import com.ruoyi.activiti.service.IFmBizService;
import com.ruoyi.activiti.service.IOgPhoneService;
import com.ruoyi.activiti.service.IPubRelationService;
import com.ruoyi.activiti.service.ITelFlowLogService;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.CustInfo;
import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.IdGenerator;
import com.ruoyi.common.core.domain.entity.OgGroup;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.api.KnowledgeApi;
import com.ruoyi.es.domain.KnowledgeContent;
import com.ruoyi.es.service.KnowledgeBusinessContentService;
import com.ruoyi.system.service.IIdGeneratorService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysWorkService;

/**
 * ???????????????????????????
 */
@Controller
@RequestMapping("/eventListApi")
public class EventListApi extends BaseController {

    @Autowired
    private IFmBizService fmBizService;
    @Autowired
    private IIdGeneratorService idGeneratorService;
    @Autowired
    ISysApplicationManagerService iSysApplicationManagerService;
    @Autowired
    IOgPersonService iOgPersonService;
    @Autowired
    ISysDeptService iSysDeptService;
    @Autowired
    private ActivitiCommService activitiCommService;
    @Autowired
    private ISysWorkService ISysWorkService;
    @Autowired
    private IPubParaValueService iPubParaValueService;
    @Autowired
    private IPubRelationService iPubRelationService;
    @Autowired(required=false)
    private KnowledgeApi knowledgeApi;
    @Autowired
    private IOgPhoneService iOgPhoneService;
    @Autowired
    private ITelFlowLogService iTelFlowLogService;
    @Autowired
    private CustInfoService custInfoService;
    @Autowired(required=false)
    KnowledgeBusinessContentService businessContentService;

    /**
     * ???????????????
     */
    @PostMapping("/getEventList")
    @ResponseBody
    public FmBiz getEventList(String faultNo) {
        FmBiz fm = fmBizService.selectFmBizByFaultNo(faultNo);
        return fm;
    }

    /**
     * ???????????????
     */
    @PostMapping("/addEventList")
    @ResponseBody
    @Transactional
    @RepeatSubmit
    public AjaxResult addEventList(FmBiz fmBiz) {
        String pId = ShiroUtils.getOgUser().getpId();
        OgPerson person = iOgPersonService.selectOgPersonEvoById(pId);
        OgOrg org = iSysDeptService.selectDeptById(person.getOrgId());
        String bizType = "SJ";
        Map<String,String> bizTypeMap = iOgPersonService.getBizTypeMap();
        for(Map.Entry<String,String> entry : bizTypeMap.entrySet()){
            if(entry.getKey().equals(org.getOrgName())){
                bizType = entry.getValue();
                break;
            }
        }
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        fmBiz.setFaultNo(bizType + nowDateStr + numStr);
        fmBiz.setFaultSource("02");
        fmBiz.setReportTime(DateUtils.dateTimeNow());
        String fmId = fmBiz.getFmId();
        FmBiz fm = fmBizService.selectFmBizByFaultNo(fmBiz.getFaultNo());
        HashMap<String,Object> map =  new HashMap<String,Object>();
        map.put("faultNo",fmBiz.getFaultNo());
        if (ObjectUtils.isEmpty(fm) && StringUtils.isEmpty(fmId)) {
            fmBiz.setFmId(UUID.getUUIDStr());//??????ID
        } else {
            return AjaxResult.error("????????????????????????",map);
        }
        Map<String, Object> reMap = new HashMap<>();
        OgUser u = ShiroUtils.getOgUser();//?????????????????????
        String isCenter = iSysDeptService.getIsCenter();//??????????????????????????????????????????
        if ("1".equals(isCenter)) {//???
            reMap.put("reCode", "0");//????????????[???????????????????????????????????????]
            fmBiz.setCurrentState("03");//???????????????
            FmBiz fb = getGroupIdByKnowledgek(fmBiz);//??????????????????????????????????????????????????????
            reMap.put("groupId", fb.getGroupId());//????????????????????????????????????
            fmBiz.setKnowledgeId(fb.getKnowledgeId());//????????????
            fmBiz.setGroupId(fb.getGroupId());//?????????????????????
            fmBiz.setDealGroupId(fb.getGroupId());//?????????????????????
            fmBiz.setToQgzxTime(DateUtils.dateTimeNow());//???????????????????????????
        } else {//???
            reMap.put("reCode", "1");//????????????[??????????????????????????????]
            String pCode = iSysDeptService.getpCode(u.getpId());//?????????????????????????????????
            reMap.put("pCode", "0301" + "_" + pCode);//????????????+??????code
            fmBiz.setCurrentState("02");//???????????????
        }
        fmBiz.setOccurTime(handleTimeYYYYMMDDHHMMSS(fmBiz.getOccurTime()));//????????????????????????????????????
        fmBiz.setReportTime(handleTimeYYYYMMDDHHMMSS(fmBiz.getReportTime()));//????????????????????????????????????
        fmBiz.setInvalidationMark("1");//????????????1
        fmBiz.setDealMode("01");//??????????????????????????????
        fmBiz.setIfYn("0");
        //?????????????????????
        List<PubParaValue> list1 = iPubParaValueService.selectPubParaValueByParaName("fm_level_keywords");//???????????????
        List<PubParaValue> list2 = iPubParaValueService.selectPubParaValueByParaName("fm_level_intermediate");//???????????????
        fmBiz.setSeriousLev("1");//???????????????1[???]
        for (PubParaValue pp : list2) {
            if (fmBiz.getFaultDecriptDetail().contains(pp.getValueDetail())) {
                fmBiz.setSeriousLev("2");
                break;
            }
        }
        int isFmlevelHigh = 0;
        for (int y = 0; y < list1.size(); y++) {
            if (fmBiz.getFaultDecriptDetail().contains(list1.get(y).getValueDetail())) {
                isFmlevelHigh++;
            }
        }
        if (list1.size() == isFmlevelHigh) {
            fmBiz.setSeriousLev("3");
            fmBiz.setIfJj("1");

            if (isCenter != null) {
                if (isCenter.equals("1")) {
                    fmBizService.ifJJSendMessage(fmBiz.getGroupId(), fmBiz.getSysid(), fmBiz.getFaultNo());
                }
            }
        } else {
            fmBiz.setIfJj("2");
        }
        reMap.put("createId", u.getpId());//???????????????
        reMap.put("userId", u.getpId());//???????????????

        String cId = checkAddOrUpdate(fmBiz); //??????id
        fmBiz.setcId(cId);
        try {
            fmBiz.setIniSys(fmBiz.getSysid());
            fmBiz.setIniOneType(fmBiz.getOneType());
            fmBiz.setIniTwoType(fmBiz.getTwoType());
            fmBiz.setIniThreeType(fmBiz.getThreeType());
            fmBiz.setIniKeywords(fmBiz.getKeywords());
            fmBizService.insertFmBiz(fmBiz);
            activitiCommService.startProcess(fmBiz.getFmId(), "FmBiz", reMap);
            saveTelBiz(fmBiz);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("?????????????????????????????????");
        }
        return AjaxResult.success("???????????????????????????",map);
    }

    /**
     * ??????????????????????????????
     */
    public FmBiz getGroupIdByKnowledgek(FmBiz fmBiz) {
        String s = "2";
        List<OgGroup> list = new ArrayList<>();
        //???????????????????????????????????????????????????????????????
        List<String> nameList = new ArrayList<>();
        String str = fmBiz.getKeywords();
        if (StringUtils.isNotEmpty(str)) {
            String[] sName = str.split(",");
            if (sName.length > 0) {
                for (String ss : sName) {
                    nameList.add(ss);
                }
            }
        }
        List<KnowledgeContent> knowledgeList = knowledgeApi.searchKnowledge(fmBiz.getSysid(), "", fmBiz.getOneType(), fmBiz.getTwoType(), fmBiz.getThreeType(), nameList);
        if (!knowledgeList.isEmpty() && knowledgeList.size() == 1) {
            fmBiz.setKnowledgeId(knowledgeList.get(0).getId());
            s = "1";
        }
        OgGroup group = new OgGroup();
        group.setSysId(fmBiz.getSysid());
        group.setGroupType(s);
        list = ISysWorkService.selectOgGroupList(group);
        if (list.isEmpty()) {
            OgGroup group2 = new OgGroup();
            group2.setSysId(fmBiz.getSysid());
            group2.setGroupType("2");
            list = ISysWorkService.selectOgGroupList(group2);
        }
        fmBiz.setGroupId(list.get(0).getGroupId());
        return fmBiz;
    }

    /**
     *  ??????or??????????????????
     * @param fmBiz
     * @return
     */
    private String checkAddOrUpdate(FmBiz fmBiz) {
        String cId = "";
        if("".equals(fmBiz.getCustomerName()) && "".equals(fmBiz.getcPhone())){
            return cId;
        }
        CustInfo custInfo = new CustInfo();
        custInfo.setcName(fmBiz.getCustomerName());
        custInfo.setcPhone(fmBiz.getcPhone());
        custInfo.setcDept(fmBiz.getcDept());
        custInfo.setcPost(fmBiz.getcPost());
        custInfo.setcAddress(fmBiz.getcAddress());
        custInfo.setCreateTime(new Date());
        custInfo.setcId(fmBiz.getcId());
        cId = fmBiz.getcId();
        try{
            //??????????????????????????????
            if(!"".equals(fmBiz.getcId())){
                //1.????????????
                if(!"".equals(fmBiz.getcPhone()) && fmBiz.getcPhone()!=null){
                    CustInfo custInfoObj = new CustInfo();
                    custInfoObj.setcPhone(fmBiz.getcPhone());
                    CustInfo obj = custInfoService.getByCustInfo(custInfoObj);
                    //1.1????????????????????????
                    if(obj!=null){
                        cId = obj.getcId();
                        custInfo.setcId(obj.getcId());
                    }
                }
                custInfoService.updateById(custInfo);

            }else{
                //??????????????? ?????????????????????????????????????????????
                int size = custInfoService.getByCount(custInfo.getcPhone());
                if(size>0){
                    //????????????????????????
                    custInfoService.updateById(custInfo);
                }else{
                    //???????????????
                    custInfo.setcId(UUID.getUUIDStr());
                    custInfoService.insertCustInfo(custInfo);
                    cId = custInfo.getcId();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException("??????????????????");
        }

        return cId;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @param fmBiz
     */
    public void saveTelBiz(FmBiz fmBiz) {
        if (fmBiz.getParams().containsKey("telId")) {
            if (StringUtils.isNotEmpty(fmBiz.getParams().get("telId").toString())) {
                String telId = fmBiz.getParams().get("telId").toString();
                PubRelation prt = new PubRelation();
                prt.setObj1Id(fmBiz.getFmId());
                prt.setObj2Id(telId);
                prt.setType("88");
                List<PubRelation> list = iPubRelationService.selectPubRelationList(prt);
                if (list.isEmpty()) {//??????????????????????????????(??????)
                    PubRelation pr = new PubRelation();
                    pr.setRelationId(UUID.getUUIDStr());
                    pr.setObj1Id(fmBiz.getFmId());//obj1???????????????ID
                    pr.setObj2Id(telId);//obj2???????????????ID
                    pr.setType("88");//????????????????????????
                    iPubRelationService.insertPubRelation(pr);//??????????????????
                    TelBiz tb = iOgPhoneService.selectPhoneById(telId);
                    tb.setFaultNo(fmBiz.getFaultNo());
                    tb.setState("4");//????????????
                    iOgPhoneService.updatePhone(tb);//???????????????????????????????????????
                    //?????????????????????????????????
                    TelFlowLog telBizFlowLog = new TelFlowLog();
                    telBizFlowLog.setLogId(UUID.getUUIDStr());
                    telBizFlowLog.setCreatTime(fmBiz.getCreatTime());
                    telBizFlowLog.setTelId(telId);
                    telBizFlowLog.setSerialNum("3");
                    telBizFlowLog.setOperationName("??????????????????");
                    OgPerson person = iOgPersonService.selectOgPersonById(fmBiz.getCreateId());
                    telBizFlowLog.setOperator(person.getpName());
                    telBizFlowLog.setOperatorTel(person.getMobilPhone());
                    telBizFlowLog.setState("?????????");
                    iTelFlowLogService.insertTelFlowLog(telBizFlowLog);
                }
            }
        }
    }

}
