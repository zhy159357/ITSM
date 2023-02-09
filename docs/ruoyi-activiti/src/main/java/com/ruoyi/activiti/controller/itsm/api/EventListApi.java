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
 * 监控系统事件单接口
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
     * 查询事件单
     */
    @PostMapping("/getEventList")
    @ResponseBody
    public FmBiz getEventList(String faultNo) {
        FmBiz fm = fmBizService.selectFmBizByFaultNo(faultNo);
        return fm;
    }

    /**
     * 增加事件单
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
            fmBiz.setFmId(UUID.getUUIDStr());//创建ID
        } else {
            return AjaxResult.error("事件单编号已存在",map);
        }
        Map<String, Object> reMap = new HashMap<>();
        OgUser u = ShiroUtils.getOgUser();//获取当前登陆人
        String isCenter = iSysDeptService.getIsCenter();//判断创建人是否为全国中心机构
        if ("1".equals(isCenter)) {//是
            reMap.put("reCode", "0");//流程走向[非省中心创建直接到全国中心]
            fmBiz.setCurrentState("03");//待领取状态
            FmBiz fb = getGroupIdByKnowledgek(fmBiz);//根据知识加载规则查询该分给哪个工作组
            reMap.put("groupId", fb.getGroupId());//全国中心提交分派的工作组
            fmBiz.setKnowledgeId(fb.getKnowledgeId());//添加知识
            fmBiz.setGroupId(fb.getGroupId());//添加初始组信息
            fmBiz.setDealGroupId(fb.getGroupId());//添加处理组信息
            fmBiz.setToQgzxTime(DateUtils.dateTimeNow());//添加转全国中心时间
        } else {//否
            reMap.put("reCode", "1");//流程走向[省中心创建到分派环节]
            String pCode = iSysDeptService.getpCode(u.getpId());//当前登陆人对应三级机构
            reMap.put("pCode", "0301" + "_" + pCode);//分派角色+机构code
            fmBiz.setCurrentState("02");//待分派状态
        }
        fmBiz.setOccurTime(handleTimeYYYYMMDDHHMMSS(fmBiz.getOccurTime()));//重新转换下保存的时间格式
        fmBiz.setReportTime(handleTimeYYYYMMDDHHMMSS(fmBiz.getReportTime()));//重新转换下保存的时间格式
        fmBiz.setInvalidationMark("1");//有效标志1
        fmBiz.setDealMode("01");//处理方式默认正常处理
        fmBiz.setIfYn("0");
        //事件单等级划分
        List<PubParaValue> list1 = iPubParaValueService.selectPubParaValueByParaName("fm_level_keywords");//高级关键字
        List<PubParaValue> list2 = iPubParaValueService.selectPubParaValueByParaName("fm_level_intermediate");//中级关键字
        fmBiz.setSeriousLev("1");//默认等级为1[低]
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
        reMap.put("createId", u.getpId());//流程发起人
        reMap.put("userId", u.getpId());//流程发起人

        String cId = checkAddOrUpdate(fmBiz); //客户id
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
            throw new BusinessException("业务事件单流程创建失败");
        }
        return AjaxResult.success("业务事件单提交成功",map);
    }

    /**
     * 判断给一、二线工作组
     */
    public FmBiz getGroupIdByKnowledgek(FmBiz fmBiz) {
        String s = "2";
        List<OgGroup> list = new ArrayList<>();
        //调用知识库接口查询是否存在知识且为一个知识
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
     *  添加or修改客户信息
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
            //事件单是否关联了客户
            if(!"".equals(fmBiz.getcId())){
                //1.有手机号
                if(!"".equals(fmBiz.getcPhone()) && fmBiz.getcPhone()!=null){
                    CustInfo custInfoObj = new CustInfo();
                    custInfoObj.setcPhone(fmBiz.getcPhone());
                    CustInfo obj = custInfoService.getByCustInfo(custInfoObj);
                    //1.1数据库存在手机号
                    if(obj!=null){
                        cId = obj.getcId();
                        custInfo.setcId(obj.getcId());
                    }
                }
                custInfoService.updateById(custInfo);

            }else{
                //第一次保存 根据手机号查看客户信息是否存在
                int size = custInfoService.getByCount(custInfo.getcPhone());
                if(size>0){
                    //电话号存在，修改
                    custInfoService.updateById(custInfo);
                }else{
                    //第一次保存
                    custInfo.setcId(UUID.getUUIDStr());
                    custInfoService.insertCustInfo(custInfo);
                    cId = custInfo.getcId();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException("客户信息异常");
        }

        return cId;
    }

    /**
     * 判断是否为电话事件单转事件单如果是保存关联关系
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
                if (list.isEmpty()) {//判断是否存在绑定关系(暂存)
                    PubRelation pr = new PubRelation();
                    pr.setRelationId(UUID.getUUIDStr());
                    pr.setObj1Id(fmBiz.getFmId());//obj1放入事件单ID
                    pr.setObj2Id(telId);//obj2放入电话单ID
                    pr.setType("88");//放入关联关系类型
                    iPubRelationService.insertPubRelation(pr);//保存关联关系
                    TelBiz tb = iOgPhoneService.selectPhoneById(telId);
                    tb.setFaultNo(fmBiz.getFaultNo());
                    tb.setState("4");//放入状态
                    iOgPhoneService.updatePhone(tb);//保存事件单号到电话事件单表
                    //保存电话事件单流程记录
                    TelFlowLog telBizFlowLog = new TelFlowLog();
                    telBizFlowLog.setLogId(UUID.getUUIDStr());
                    telBizFlowLog.setCreatTime(fmBiz.getCreatTime());
                    telBizFlowLog.setTelId(telId);
                    telBizFlowLog.setSerialNum("3");
                    telBizFlowLog.setOperationName("转业务事件单");
                    OgPerson person = iOgPersonService.selectOgPersonById(fmBiz.getCreateId());
                    telBizFlowLog.setOperator(person.getpName());
                    telBizFlowLog.setOperatorTel(person.getMobilPhone());
                    telBizFlowLog.setState("处理中");
                    iTelFlowLogService.insertTelFlowLog(telBizFlowLog);
                }
            }
        }
    }

}
