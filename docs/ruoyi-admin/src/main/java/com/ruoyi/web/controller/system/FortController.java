package com.ruoyi.web.controller.system;

import com.ruoyi.activiti.domain.CmBizSingleSJVo;
import com.ruoyi.activiti.domain.EventBean;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.service.*;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.data.JSONArray;
import com.ruoyi.common.data.JSONObject;
import com.ruoyi.common.sign.aop.ApiSignValid;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.es.domain.KnowledgeTitle;
import com.ruoyi.es.service.KnowledgeTitleService;
import com.ruoyi.system.service.*;
import com.ruoyi.web.controller.api.exception.ApiException;
import com.ruoyi.web.controller.api.util.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 运维管理平台与堡垒机接口
 *
 * @author 14735
 */
@Controller
@RequestMapping("/cps-server/apigateway")
public class FortController extends BaseController {

    @Autowired
    private IVmBizInfoService vmBizInfoService;

    @Autowired
    private CmBizSingleSJService cmBizSingleSJService;

    @Autowired
    private ICmBizSingleService cmBizSingleService;

    @Autowired
    private IPubParaValueService pubParaValueService;

    @Autowired
    private IFmBizService fmBizService;

    @Autowired
    private IIdGeneratorService idGeneratorService;

    @Autowired
    private ActivitiCommService activitiCommService;

    @Autowired
    private ISysApplicationManagerService sysApplicationManagerService;

    @Autowired
    ISysDeptService sysDeptService;

    @Autowired
    private ISysWorkService sysWorkService;

    @Value("${Antifraud.url}")
    private String AntifraudUrl;

    @Autowired(required=false)
    private KnowledgeTitleService knowledgeTitleService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    private IFmBizScriptService fmBizScriptService;

    @Autowired
    private IForwardFileService forwardFileService;

    @Autowired
    private IPubBizSmsService pubBizSmsService;

    @Autowired
    private IPubBizPresmsService pubBizPresmsService;

    @Autowired
    private IFmbizAndIssueService iFmbizAndIssueService;

    // 默认电话银行创建人ID
    @Value("${dhyh.createId}")
    private String CREATE_ID;

    // 默认信用卡创建人ID
    @Value("${xyk.createId}")
    private String XYK_CREATE_ID;

    // 默认电话银行发生机构ID
    @Value("${dhyh.createOrgId}")
    private String OG_ORG_ID;

    // 默认信用卡发生机构ID
    @Value("${xyk.createOrgId}")
    private String XYK_OG_ORG_ID;

    @RequestMapping("/authVerifcation.api")
    @ResponseBody
    public void authVerifcation(HttpServletRequest request, HttpServletResponse response) {
        ApiResData resData;
        String checkNo = "";
        try {
            checkNo = request.getReader().readLine();
            checkNo = checkNo.replace("\"", "");
            if (StringUtils.isEmpty(checkNo)) {
                resData = new ApiResData("-1", "参数单号不可为空!!!!");
            }
            JSONArray array = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            // 版本发布
            if (checkNo.contains("BB")) {
                VmBizInfo vmBizInfo = vmBizInfoService.selectVmBizInfoByNo(checkNo);
                if (vmBizInfo != null) {
                    // 单号
                    jsonObject.put("changeNo", vmBizInfo.getVersionInfoNo());
                    // 标题
                    jsonObject.put("tltleName", vmBizInfo.getVersionInfoName());
                    // 计划开始时间
                    jsonObject.put("udateStataTime", handleTimeYYYYMMDDHHMMSS(vmBizInfo.getStartUpgradeTime()));
                    // 申请人
                    jsonObject.put("appName", vmBizInfo.getVersionProducerName());
                    // 所属系统
                    jsonObject.put("sysName", vmBizInfo.getOgSys().getCaption());
                    // 实施方案
                    jsonObject.put("impPlan", vmBizInfo.getBusinessRequirementText());
                    // 实施内容
                    jsonObject.put("impText", vmBizInfo.getVersionDescription());

                    array.put(jsonObject);
                    if ("12".equals(vmBizInfo.getVersionStatus())) {
                        resData = new ApiResData("0", "成功");
                    } else {
                        resData = new ApiResData("-1", "输入的单号审批未通过请检查后重新输入单号。");
                    }
                    resData.setResDataJson(array.toString());
                } else {
                    resData = new ApiResData("-1", "输入的单号不正确。");
                }
            }
            // 数据变更
            else if (checkNo.contains("SJBG")) {
                CmBizSingleSJVo cmBizSingleSJVo = cmBizSingleSJService.selectSjbgByChangeCode(checkNo);
                if (cmBizSingleSJVo != null) {
                    String changeNo = cmBizSingleSJVo.getChangeCode();//单号
                    String tltleName = cmBizSingleSJVo.getChangeSingleName();//标题
                    String udateStataTime = handleTimeYYYYMMDDHHMMSS(cmBizSingleSJVo.getExpectStartTime());//计划开始时间
                    String appName = cmBizSingleSJVo.getChangeApplicantName();//申请人
                    String sysName = cmBizSingleSJVo.getSysName();//所属系统
                    String impPlan = cmBizSingleSJVo.getChangeProgram();//实施方案
                    String impText = cmBizSingleSJVo.getChangeDetails();//实施内容
                    jsonObject.put("changeNo", changeNo);
                    jsonObject.put("tltleName", tltleName);
                    jsonObject.put("udateStataTime", udateStataTime);
                    jsonObject.put("appName", appName);
                    jsonObject.put("sysName", sysName);
                    jsonObject.put("impPlan", impPlan);
                    jsonObject.put("impText", impText);
                    array.put(jsonObject);
                    if ("03".equals(cmBizSingleSJVo.getChangeSingleStatus()) || "05".equals(cmBizSingleSJVo.getChangeSingleStatus())) {
                        resData = new ApiResData("0", "成功");
                    } else {
                        resData = new ApiResData("-1", "输入的单号审批未通过请检查后重新输入单号。");
                    }
                    resData.setResDataJson(array.toString());
                } else {
                    resData = new ApiResData("-1", "输入的单号不正确。");
                }
            }
            // 资源变更
            else if (checkNo.contains("BG")) {
                CmBizSingle cmBizSingle = cmBizSingleService.getCmBizBycChangeCode(checkNo);
                if (cmBizSingle != null) {
                    String changeNo = cmBizSingle.getChangeCode();//单号
                    String tltleName = cmBizSingle.getChangeSingleName();//标题
                    String udateStataTime = handleTimeYYYYMMDDHHMMSS(cmBizSingle.getExpectStartTime());//计划开始时间
                    String appName = cmBizSingle.getApplicantName();//申请人
                    String sysName = cmBizSingle.getSysname();//所属系统
                    jsonObject.put("changeNo", changeNo);
                    jsonObject.put("tltleName", tltleName);
                    jsonObject.put("udateStataTime", udateStataTime);
                    jsonObject.put("appName", appName);
                    jsonObject.put("sysName", sysName);
                    jsonObject.put("impPlan", "略");
                    jsonObject.put("impText", "略");
                    array.put(jsonObject);
                    if ("0500".equals(cmBizSingle.getChangeSingleStauts())) {
                        resData = new ApiResData("0", "成功");
                    } else {
                        resData = new ApiResData("-1", "输入的单号审批未通过请检查后重新输入单号。");
                    }
                    resData.setResDataJson(array.toString());
                } else {
                    resData = new ApiResData("-1", "输入的单号不正确。");
                }
            } else {
                resData = new ApiResData("-1", "输入的单号不正确。");
            }
        } catch (Exception e) {
            resData = new ApiResData("-1", "参数单号不可为空!!!!");
        }
        ApiUtil.resJSONObjUTF8(response, resData);
    }

    /*
    查询所属系统@ApiSignValid
     */
    @RequestMapping(value = "/getAll.api")
    @ResponseBody
    public void test(HttpServletRequest request, HttpServletResponse response) {
        ApiResData resData = null;
        try {
            resData = getAll();

        } catch (ApiException e) {
            resData = new ApiResData(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resData = new ApiResData("999999", "系统错误");
        }
        ApiUtil.resJSONObjUTF8(response, resData);
    }

    public ApiResData getAll() {
        List<OgSys> list = sysApplicationManagerService.selectOgSysListByCondition(new OgSys());// 拿到所有系统
        JSONArray array = new JSONArray();
        try {
            if (!list.isEmpty()) {
                for (OgSys ogSys : list) {
                    JSONObject jsonObject = new JSONObject();
                    String sysid = ogSys.getSysId();
                    String name = ogSys.getCaption();
                    jsonObject.put("sysId", sysid);
                    //传回系统id 在分类表里的
                    KnowledgeTitle know = new KnowledgeTitle();
                    know.setSysId(sysid);
                    know.setCategory("0");
                    know.setStatus("0");
                    List<KnowledgeTitle> listSys = knowledgeTitleService.selectKnowledgeTitleList(know);
                    String id = "00";
                    if (listSys != null && listSys.size() > 0) {
                        id = listSys.get(0).getId();
                    }
                    jsonObject.put("sysIdForType", id);
                    jsonObject.put("sysName", name);
                    array.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiResData resData = new ApiResData("0");
        resData.setResDataJson(array.toString());
        return resData;
    }

    /**
     * 查询所属系统分类信息 关键字@ApiSignValid
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getGroupType.api")
    @ResponseBody
    public void getlist(HttpServletRequest request, HttpServletResponse response) {
        ApiResData resData = null;
        try {
            String br = request.getReader().readLine();
            String sysid = br.replace("\"", "");
            resData = getGroupType(sysid);

        } catch (ApiException e) {
            resData = new ApiResData(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resData = new ApiResData("999999", "系统错误");
        }
        ApiUtil.resJSONObjUTF8(response, resData);
    }

    public ApiResData getGroupType(String sysid) {
        KnowledgeTitle know = new KnowledgeTitle();
        know.setParentId(sysid);
        know.setStatus("0");
        List<KnowledgeTitle> listf = knowledgeTitleService.selectKnowledgeTitleList(know);
        JSONArray array = new JSONArray();
        try {
            if (listf.size() > 0) {
                for (KnowledgeTitle knowledgeTitle : listf) {
                    JSONObject jsonObject = new JSONObject();
                    String fmTypeId = knowledgeTitle.getId();
                    String fmTypeName = knowledgeTitle.getName();
                    jsonObject.put("fmTypeId", fmTypeId);
                    jsonObject.put("fmTypeName", fmTypeName);
                    array.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ApiResData resData = new ApiResData("0");
        resData.setResDataJson(array.toString());
        return resData;
    }

    /**
     * 电话银行/信用卡客服系统调用创建业务事件单
     */
    @RequestMapping(value = "/createFm.api")
    @ResponseBody
    public void createFmBiz(HttpServletRequest request,
                            HttpServletResponse response) {
        ApiResData resData = null;
        try {
            List<PubParaValue> list = pubParaValueService.selectPubParaValueByParaName("if_dhyh");

            if (!list.isEmpty()) {
                String str = list.get(0).getValue();
                if ("1".equals(str)) {
                    resData = new ApiResData("999999", "系统错误");
                } else {
                    EventBean parameters = ApiUtil.verifyAndGetReqData(request,
                            EventBean.class);
                    resData = create(parameters);
                }
            }
        } catch (ApiException e) {
            resData = new ApiResData(e.getCode(), e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            resData = new ApiResData("999999", "系统错误");
        }
        ApiUtil.resJSONObjUTF8(response, resData);
    }

    /**
     * 电话银行/信用卡创建事件单
     *
     * @param parameters
     * @return
     */
    public ApiResData create(EventBean parameters) {
        ApiResData resData = new ApiResData("0", "事件单创建成功！");
        FmBiz fm = new FmBiz();

        String sysid = parameters.getSysid();// 所属应用系统 NOT NULL
        if (StringUtils.isEmpty(sysid)) {
            return resData = new ApiResData("-1", "系统ID不能为空");
        } else {
            fm.setSysid(sysid);
            fm.setIniSys(sysid);
        }

        String oneType = parameters.getOneType();// 事件分类 NOT NULL 一级分类
        if (StringUtils.isEmpty(oneType)) {
            return resData = new ApiResData("-1", "事件一级分类ID不能为空");
        } else {
            fm.setOneType(oneType);
            fm.setIniOneType(oneType);
        }

        String twoType = parameters.getTwoType();// 事件分类 NOT NULL 二级分类
        if (StringUtils.isEmpty(twoType)) {
            return resData = new ApiResData("-1", "事件二级分类ID不能为空！");
        } else {
            fm.setTwoType(twoType);
            fm.setIniTwoType(twoType);
        }

        String threeType = parameters.getThreeType();// 事件分类 NOT NULL 三级分类
        if (StringUtils.isEmpty(threeType)) {
            return resData = new ApiResData("-1", "事件三级分类ID不能为空");
        } else {
            fm.setThreeType(threeType);
            fm.setIniThreeType(threeType);
        }

        String keywords = parameters.getKeywords();// 事件分类  关键字
        fm.setKeywords(keywords);
        fm.setIniKeywords(keywords);

        String telid = parameters.getFault_No();// 电话银行id NOT NULL
        String faultSource = parameters.getFault_Source();// 事件来源 NOT NULL
        if (StringUtils.isEmpty(telid) && StringUtils.isEmpty(faultSource)) {
            return resData = new ApiResData("-1", "事件来源不能为空");
        } else {
            fm.setFaultSource(faultSource);
            // -----------------------------------存放业务事件单与电话银行的关系----------------------------------------------
            if ("03".equals(faultSource)) {// 电话银行
                fm.setCreateId(CREATE_ID);
                fm.setCreateOrgId(OG_ORG_ID);
                fm.setOccurrenceOrgId(OG_ORG_ID);
                fm.setN1(telid);
            } else if ("04".equals(faultSource)) {// 信用卡
                fm.setCreateId(XYK_CREATE_ID);
                fm.setCreateOrgId(XYK_OG_ORG_ID);
                fm.setOccurrenceOrgId(XYK_OG_ORG_ID);
                fm.setN2(telid);
            }
        }
        String occurrenceAddress = parameters.getOccurrence_Address();// 发生地址//交易机构
        if (StringUtils.isNotEmpty(occurrenceAddress)) {
            if (occurrenceAddress.length() > 42) {
                return resData = new ApiResData("-1", "发生地址长度过长");
            }
            fm.setOccurrenceAddress(occurrenceAddress);
        }

        String occurTime = parameters.getOccur_Time();// 发生时间NOT NULL
        if (StringUtils.isEmpty(occurTime)) {
            return resData = new ApiResData("-1", "发生时间不能为空");
        } else {
            fm.setOccurTime(occurTime);
        }

        String faultReportName = parameters.getFault_Report_Name();// 当事人NOT NULL
        if (StringUtils.isEmpty(faultReportName)) {
            return resData = new ApiResData("-1", "当事人不能为空");
        } else {
            if (faultReportName.length() > 18) {
                return resData = new ApiResData("-1", "当事人长度过长");
            }
            fm.setFaultReportName(faultReportName);
        }

        String reportTime = parameters.getReport_Time();// 报告时间
        if (reportTime != null) {
            fm.setReportTime(reportTime);
        } else {
            fm.setReportTime(DateUtils.dateTimeNow());
        }

        String reportPhone = parameters.getReport_Phone();// 报告人电话
        if (StringUtils.isNotEmpty(reportPhone)) {
            if (reportPhone.length() > 32) {
                return resData = new ApiResData("-1", "报告人电话长度过长");
            }
            fm.setReportPhone(reportPhone);
        }

        String faultContactName = parameters.getFault_Contact_Name();// 填报人NOT
        // NULL
        if (StringUtils.isEmpty(faultContactName)) {
            return resData = new ApiResData("-1", "填报人不能为空");
        } else {
            if (faultContactName.length() > 18) {
                return resData = new ApiResData("-1", "填报人长度过长");
            }
            fm.setFaultContactName(faultContactName);
        }

        String contactPhone = parameters.getContact_Phone();// 填报人电话NOT NULL
        if (StringUtils.isEmpty(contactPhone)) {
            return resData = new ApiResData("-1", "填报人电话不能为空");
        } else {
            if (contactPhone.length() > 12) {
                return resData = new ApiResData("-1", "填报人电话长度过长");
            }
            fm.setContactPhone(contactPhone);
        }

//        String contactAddress = parameters.getContact_Address();// 填报人地址
//        if (StringUtils.isNotEmpty(contactAddress)) {
//            if (contactAddress.length() > 42) {
//                return resData = new ApiResData("-1", "填报人地址长度过长");
//            }
//            fm.setContactAddress(contactAddress);
//        } else {
//            return resData = new ApiResData("-1", "填报人地址不能为空");
//        }
        //填报人地址默认为  电话银行 95580调用该接口不传该参数 写成固定值
        fm.setContactAddress("电话银行");

        String serialNumber = parameters.getSerial_Number();// 流水号
        if (StringUtils.isNotEmpty(serialNumber)) {
            if (serialNumber.length() > 32) {
                return resData = new ApiResData("-1", "流水号长度过长");
            }
            fm.setSerialNumber(serialNumber);
        }

        String tradingName = parameters.getTrading_Name();// 交易名称
        if (StringUtils.isNotEmpty(tradingName)) {
            if (tradingName.length() > 32) {
                return resData = new ApiResData("-1", "交易名称长度过长");
            }
            fm.setTradingName(tradingName);
        }

        String errorInformation = parameters.getError_Information();// 错误信息
        if (StringUtils.isNotEmpty(errorInformation)) {
            if (errorInformation.length() > 64) {
                return resData = new ApiResData("-1", "错误信息长度过长");
            }
            fm.setErrorInformation(errorInformation);
        }

        String faultDecriptSummary = parameters.getFault_Decript_Summary();// 事件标题NOT
        if (StringUtils.isEmpty(faultDecriptSummary)) {
            return resData = new ApiResData("-1", "事件标题不能为空");
        } else {
            if (faultDecriptSummary.length() > 42) {
                return resData = new ApiResData("-1", "事件标题长度过长");
            }
            fm.setFaultDecriptSummary(faultDecriptSummary);
        }

        String faultDecriptDetail = parameters.getFault_Decript_Detail();// 事件描述NOT
        if (StringUtils.isEmpty(faultDecriptDetail)) {
            return resData = new ApiResData("-1", "事件描述不能为空");
        } else {
            if (faultDecriptDetail.length() > 1300) {
                return resData = new ApiResData("-1", "事件描述长度过长");
            }
            fm.setFaultDecriptDetail(faultDecriptDetail);
        }

        String creatTime = parameters.getCreat_Time();// 创建时间
        if (StringUtils.isNotEmpty(creatTime)) {
            fm.setCreatTime(creatTime);
        } else {
            fm.setCreatTime(DateUtils.dateTimeNow());
        }

        String currentState = parameters.getCurrent_State();// 当前状态
        if (StringUtils.isNotEmpty(currentState)) {
            fm.setCurrentState(currentState);
        } else {
            fm.setCurrentState("03");// 提交--全国中心03 省中心02
        }

        String involveAmount = parameters.getInvolve_Amount();// 涉事金额NOT NULL
        if (StringUtils.isEmpty(involveAmount)) {
            fm.setInvolveAmount("0");
        } else {
            fm.setInvolveAmount(involveAmount);
        }

        String involveScount = parameters.getInvolve_Scount();// 涉事笔数
        if (StringUtils.isNotEmpty(involveScount)) {
            if (involveScount.length() > 20) {
                return resData = new ApiResData("-1", "涉事笔数长度过长");
            }
            fm.setInvolveScount(involveScount);
        }

        String customerName = parameters.getCustomer_Name();// 客户姓名
        if (StringUtils.isNotEmpty(customerName)) {
            if (customerName.length() > 16) {
                return resData = new ApiResData("-1", "客户姓名长度过长");
            }
            fm.setCustomerName(customerName);
        }

        String customerIDcard = parameters.getCustomer_IDcard();// 客户身份证号
        if (StringUtils.isNotEmpty(customerIDcard)) {
            if (customerIDcard.length() > 18) {
                return resData = new ApiResData("-1", "客户身份证号长度过长");
            }
            fm.setCustomerIdcard(customerIDcard);
        }

        String transactionAccount = parameters.getTransaction_Account();// 交易账号
        if (StringUtils.isNotEmpty(transactionAccount)) {
            if (transactionAccount.length() > 64) {
                return resData = new ApiResData("-1", "交易账号长度过长");
            }
            fm.setTransactionAccount(transactionAccount);
        }

        String transactionAmount = parameters.getTransaction_Amount();// 交易金额
        if (StringUtils.isNotEmpty(transactionAmount)) {
            if (transactionAmount.length() > 36) {
                return resData = new ApiResData("-1", "交易金额长度过长");
            }
            fm.setTransactionAmount(transactionAmount);
        }

        String orderNumber = parameters.getOrder_Number();// 订单号
        if (StringUtils.isNotEmpty(orderNumber)) {
            if (orderNumber.length() > 42) {
                return resData = new ApiResData("-1", "订单号长度过长");
            }
            fm.setOrderNumber(orderNumber);
        }


        String invalidationMark = parameters.getInvalidation_Mark();// 无效标志
        if (StringUtils.isNotEmpty(invalidationMark)) {
            fm.setInvalidationMark(invalidationMark);
        } else {
            fm.setInvalidationMark("1");
        }
        //生成单号
        String bizType = "SJ";
        IdGenerator generator = new IdGenerator();
        String nowDateStr = DateUtils.dateTimeNow("yyyyMMdd");
        generator.setCurrentDate(nowDateStr);
        generator.setBizType(bizType);
        String numStr = idGeneratorService.selectIdGeneratorByType(generator);
        fm.setFaultNo(bizType + nowDateStr + numStr);//单号
        fm.setDealMode("01");//处理方式
        fm.setFmId(UUID.getUUIDStr());//ID
        // 业务事件单附件
        String filePath = parameters.getFile_Path();// 附件完整路径
        System.out.println("filePath值是：" + filePath);
        if (StringUtils.isNotEmpty(filePath)) {
            OgPerson person = ogPersonService.selectOgPersonById(CREATE_ID);
            if (person == null) {
                return new ApiResData("-1", "回退修改失败，查询创建人错误");
            }
            String[] str = filePath.split(",");
            ApiResData resDatas = new ApiResData("-1", "附件添加失败");
            for (int i = 0; i < str.length; i++) {
                resDatas = forwardFileService.saveFortFile(fm.getFmId(), CREATE_ID, str[i], resDatas);
                if (!"0000".equals(resDatas.resCode)) {
                    return new ApiResData("-1", "附件添加失败");
                }
            }
        }
        FmBiz fmBiz1 = knowledgeTitleService.getGroupIdByKnowledgek(fm);
        String groupId = fmBiz1.getGroupId();// 流程工作组
        //事件单等级划分
        List<PubParaValue> list1 = pubParaValueService.selectPubParaValueByParaName("fm_level_keywords");//高级关键字
        List<PubParaValue> list2 = pubParaValueService.selectPubParaValueByParaName("fm_level_intermediate");//中级关键字
        fm.setSeriousLev("1");//默认等级为1[低]
        for (PubParaValue pp : list2) {
            if (fm.getFaultDecriptDetail().contains(pp.getValueDetail())) {
                fm.setSeriousLev("2");
                break;
            }
        }
        int isFmlevelHigh = 0;
        for (int y = 0; y < list1.size(); y++) {
            if (fm.getFaultDecriptDetail().contains(list1.get(y).getValueDetail())) {
                isFmlevelHigh++;
            }
        }
        if (list1.size() == isFmlevelHigh) {
            fm.setSeriousLev("3");
            fm.setIfJj("1");
            ifJJSendMessage(fmBiz1.getGroupId(), fm.getFaultNo());
        } else {
            fm.setIfJj("2");
        }
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("createId", fm.getCreateId());//流程发起人
        reMap.put("groupId", groupId);//工作组领取环节候选组
        reMap.put("userId", fm.getCreateId());
        reMap.put("reCode", "0");//流程走向[非省中心创建直接到全国中心]
        fm.setToQgzxTime(DateUtils.dateTimeNow());//转全国中心时间
        fmBizService.insertFmBiz(fm);//保存事件单内容
        activitiCommService.startProcess(fm.getFmId(), "FmBiz", reMap);//发起事件单创建流程
        return resData;
    }

    /**
     * 未解决退回接口
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/unsolvedFm.api")
    @ResponseBody
    public void unsolvedFmBiz(HttpServletRequest request,
                              HttpServletResponse response) {
        ApiResData resData = null;
        try {
            EventBean parameters = ApiUtil.verifyAndGetReqData(request,
                    EventBean.class);
            resData = unsolvedFmBiz(parameters);
        } catch (ApiException e) {
            resData = new ApiResData(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resData = new ApiResData("999999", "系统错误");
        }
        ApiUtil.resJSONObjUTF8(response, resData);
    }

    public ApiResData unsolvedFmBiz(EventBean parameters) {
        ApiResData resData = null;
        String no = parameters.getFm_ID();// 拿到事件单号
        FmBiz fmBiz = fmBizService.selectFmBizByFaultNo(no);//根据事件单号查询
        if (fmBiz == null) {
            resData = new ApiResData("-1", "单号不存在");
            return resData;
        }
        String currentState = fmBiz.getCurrentState();
        if (!"06".equals(currentState)) {
            resData = new ApiResData("-1", "只有事件单状态为待评价时才可回退");
            return resData;
        }
        // 处理意见
        String result = parameters.getS_LOG_performDesc();
        // 处理意见为空
        if (StringUtils.isEmpty(result)) {
            resData = new ApiResData("-1", "处理意见未填写");
            return resData;
        }
        if (result.length() > 170) {
            resData = new ApiResData("-1", "处理意见长度过长");
            return resData;
        }
        OgPerson ogPerson = ogPersonService.selectOgPersonById(CREATE_ID);
        if (ogPerson == null) {
            resData = new ApiResData("-1", "回退失败查询创建人错误");
            return resData;
        }
        // 业务事件单附件
        String filePath = parameters.getFile_Path();// 附件完整路径
        System.out.println("filePath值是：" + filePath);
        try {
            if (StringUtils.isNotEmpty(filePath)) {
                OgPerson person = ogPersonService.selectOgPersonById(CREATE_ID);
                if (person == null) {
                    return new ApiResData("-1", "回退修改失败，查询创建人错误");
                }
                String[] str = filePath.split(",");
                ApiResData resDatas = new ApiResData("-1", "附件添加失败");
                for (int i = 0; i < str.length; i++) {
                    resDatas = forwardFileService.saveFortFile(fmBiz.getFmId(), CREATE_ID, str[i], resDatas);
                    if (!"0000".equals(resDatas.resCode)) {
                        return new ApiResData("-1", "附件添加失败");
                    }
                }
            }
            OgOrg org = sysDeptService.selectDeptById(ogPersonService.selectOgPersonById(fmBiz.getDealerId()).getOrgId());
            if ("000000000".equals(org.getOrgCode()) || org.getLevelCode().contains("/000000000/010000888/")) {
                fmBiz.setCurrentState("04");//处理中状态
                fmBiz.setToQgzxTime(DateUtils.dateTimeNow());//添加转全国中心时间
            } else {
                fmBiz.setCurrentState("11");
            }
            Map<String, Object> reMap = new HashMap<>();
            reMap.put("businessKey", fmBiz.getFmId());
            reMap.put("comment", parameters.getS_LOG_performDesc());
            reMap.put("processDefinitionKey", "FmBiz");
            reMap.put("reCode", "1");
            reMap.put("userId", fmBiz.getCreateId());
            fmBizService.updateFmBiz(fmBiz);
            activitiCommService.complete(reMap);
            iFmbizAndIssueService.deleteFmbizAndIssueByFmId(fmBiz.getFmId());//清除关联问题单绑定关系
        } catch (Exception e) {
            resData = new ApiResData("-1", "回退失败系统连接已断开");
            return resData;
        }
        resData = new ApiResData("0", "事件单回退成功");
        return resData;
    }

    /**
     * 已解决退回接口
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/evaluate.api")
    @ResponseBody
    public void evaluateFmBiz(HttpServletRequest request,
                              HttpServletResponse response) {
        ApiResData resData = null;
        try {
            EventBean parameters = ApiUtil.verifyAndGetReqData(request,
                    EventBean.class);
            resData = evaluateFmBiz(parameters);
        } catch (ApiException e) {
            resData = new ApiResData(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resData = new ApiResData("999999", "系统错误");
        }
        ApiUtil.resJSONObjUTF8(response, resData);
    }

    public ApiResData evaluateFmBiz(EventBean parameters) {
        ApiResData resData = null;
        String no = parameters.getFm_ID();// 拿到事件单号
        FmBiz fmBiz = fmBizService.selectFmBizByFaultNo(no);//根据事件单号查询
        if (fmBiz == null) {
            resData = new ApiResData("-1", "单号不存在");
            return resData;
        }
        String currentState = fmBiz.getCurrentState();
        if (!"06".equals(currentState)) {
            resData = new ApiResData("-1", "只有事件单状态为待评价时才可关闭");
            return resData;
        }
        // 处理意见
        String result = parameters.getS_LOG_performDesc();
        // 处理意见为空
        if (StringUtils.isEmpty(result)) {
            resData = new ApiResData("-1", "处理意见未填写");
            return resData;
        } else if (result.length() > 170) {
            resData = new ApiResData("-1", "处理意见长度过长");
            return resData;
        } else {
            fmBiz.setEvaluate(result);//评价
        }
        OgPerson ogPerson = ogPersonService.selectOgPersonById(CREATE_ID);
        if (ogPerson == null) {
            resData = new ApiResData("-1", "评价失败查询创建人错误");
            return resData;
        }
        fmBiz.setEvaluateResult(parameters.getEvaluate_Result());//评价结果 满意度
        fmBiz.setEvaluaterId(CREATE_ID);//评价人
        fmBiz.setEvaluateTime(DateUtils.dateTimeNow("yyyyMMddHHmmss"));//关闭时间/评价时间
        fmBiz.setCurrentState("09");//更新为关闭状态
        //计算赋值处理总耗时
        if (StringUtils.isNotEmpty(fmBiz.getToQgzxTime())) {
            SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
            try {
                long data1 = sd.parse(fmBiz.getDealTime()).getTime();
                long data2 = sd.parse(fmBiz.getToQgzxTime()).getTime();
                long l = data1 - data2;
                fmBiz.setDealUseTime(l / 1000 + "");
            } catch (Exception e) {
                System.out.println("日期转换失败");
            }
        }
        // 业务事件单附件
        String filePath = parameters.getFile_Path();// 附件完整路径
        System.out.println("filePath值是：" + filePath);
        try {
            if (StringUtils.isNotEmpty(filePath)) {
                OgPerson person = ogPersonService.selectOgPersonById(CREATE_ID);
                if (person == null) {
                    return new ApiResData("-1", "回退修改失败，查询创建人错误");
                }
                String[] str = filePath.split(",");
                ApiResData resDatas = new ApiResData("-1", "附件添加失败");
                for (int i = 0; i < str.length; i++) {
                    resDatas = forwardFileService.saveFortFile(fmBiz.getFmId(), CREATE_ID, str[i], resDatas);
                    if (!"0000".equals(resDatas.resCode)) {
                        return new ApiResData("-1", "附件添加失败");
                    }
                }
            }
            Map<String, Object> reMap = new HashMap<>();
            reMap.put("businessKey", fmBiz.getFmId());
            reMap.put("processDefinitionKey", "FmBiz");
            reMap.put("reCode", "0");
            reMap.put("userId", fmBiz.getCreateId());
            fmBizService.updateFmBiz(fmBiz);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            resData = new ApiResData("-1", "事件单关闭失败，系统连接已断开");
            return resData;
        }
        //如果是反欺诈相关调用反欺诈系统http接口发送事件单信息给对方
        String isAntiFraud = fmBiz.getIsAntiFraud();
        if ("1".equals(isAntiFraud)) {
            fmBizService.sendAntifraud(fmBiz, AntifraudUrl);
        }
        fmBizService.calculationDealTime(fmBiz);
        resData = new ApiResData("0", "事件单关闭成功");
        return resData;
    }

    /**
     * 退回修改重新提交
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/revampFm.api")
    @ResponseBody
    public void revampFmBiz(HttpServletRequest request,
                            HttpServletResponse response) {
        ApiResData resData = null;
        try {
            EventBean parameters = ApiUtil.verifyAndGetReqData(request,
                    EventBean.class);
            resData = revampFmBi(parameters);
        } catch (ApiException e) {
            resData = new ApiResData(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resData = new ApiResData("999999", "系统错误");
        }
        ApiUtil.resJSONObjUTF8(response, resData);
    }

    public ApiResData revampFmBi(EventBean parameters) {
        ApiResData resData = new ApiResData("0", "事件单创建成功");
        String no = parameters.getFm_ID();// 拿到事件单号
        FmBiz fmBiz = fmBizService.selectFmBizByFaultNo(no);//根据事件单号查询
        // 事件单为空
        if (fmBiz == null) {
            System.out.println("单号不存在");
            return new ApiResData("-1", "单号不存在");
        }
        // 只有05状态的事件单才允许修改
        if (!"05".equals(fmBiz.getCurrentState())) {
            System.out.println("事件单状态不为退回修改状态，不允许修改");
            return new ApiResData("-1", "事件单状态不为退回修改状态，不允许修改");
        }

        String faultContactName = parameters.getFault_Contact_Name();// 填报人NOT
        if (StringUtils.isEmpty(faultContactName)) {
            return resData = new ApiResData("-1", "填报人不能为空");
        } else {
            if (faultContactName.length() > 18) {
                return resData = new ApiResData("-1", "填报人长度过长");
            }
            fmBiz.setFaultContactName(faultContactName);
        }

        String contactPhone = parameters.getContact_Phone();// 填报人电话NOT NULL
        if (StringUtils.isEmpty(contactPhone)) {
            return resData = new ApiResData("-1", "填报人电话不能为空");
        } else {
            if (contactPhone.length() > 12) {
                return resData = new ApiResData("-1", "填报人电话长度过长");
            }
            fmBiz.setContactPhone(contactPhone);
        }

        String occurTime = parameters.getOccur_Time();// 发生时间NOT NULL
        if (StringUtils.isEmpty(occurTime)) {
            return resData = new ApiResData("-1", "发生时间不能为空");
        } else {
            fmBiz.setOccurTime(occurTime);
        }

        String faultReportName = parameters.getFault_Report_Name();// 当事人NOT NULL
        if (StringUtils.isEmpty(faultReportName)) {
            return resData = new ApiResData("-1", "当事人不能为空");
        } else {
            if (faultReportName.length() > 18) {
                return resData = new ApiResData("-1", "当事人长度过长");
            }
            fmBiz.setFaultReportName(faultReportName);
        }

        String faultDecriptSummary = parameters.getFault_Decript_Summary();// 事件标题NOT
        if (StringUtils.isEmpty(faultDecriptSummary)) {
            return resData = new ApiResData("-1", "事件标题不能为空");
        } else {
            if (faultDecriptSummary.length() > 42) {
                return resData = new ApiResData("-1", "事件标题长度过长");
            }
            fmBiz.setFaultDecriptSummary(faultDecriptSummary);
        }

        String faultDecriptDetail = parameters.getFault_Decript_Detail();// 事件描述NOT
        if (StringUtils.isEmpty(faultDecriptDetail)) {
            return resData = new ApiResData("-1", "事件描述不能为空");
        } else {
            if (faultDecriptDetail.length() > 1300) {
                return resData = new ApiResData("-1", "事件描述长度过长");
            }
            fmBiz.setFaultDecriptDetail(faultDecriptDetail);
        }

        // 当事人电话
        if (StringUtils.isNotEmpty(parameters.getReport_Phone())) {
            fmBiz.setReportPhone(parameters.getReport_Phone());
        }

        String telid = parameters.getFault_No();// 电话银行id NOT NULL
        String faultSource = parameters.getFault_Source();// 事件来源 NOT NULL
        if (StringUtils.isNotEmpty(telid) && StringUtils.isNotEmpty(faultSource)) {
            // -----------------------------------存放业务事件单与电话银行的关系----------------------------------------------
            if ("03".equals(faultSource)) {// 电话银行
                fmBiz.setN1(telid);
            } else if ("04".equals(faultSource)) {// 信用卡
                fmBiz.setN2(telid);
            }
        }

        String involveAmount = parameters.getInvolve_Amount();// 涉事金额NOT NULL
        if (StringUtils.isEmpty(involveAmount)) {
            fmBiz.setInvolveAmount("0");
        } else {
            fmBiz.setInvolveAmount(involveAmount);
        }

        //事件单等级划分
        List<PubParaValue> list1 = pubParaValueService.selectPubParaValueByParaName("fm_level_keywords");//高级关键字
        List<PubParaValue> list2 = pubParaValueService.selectPubParaValueByParaName("fm_level_intermediate");//中级关键字
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
            ifJJSendMessage(fmBiz.getGroupId(), fmBiz.getFaultNo());
        } else {
            fmBiz.setIfJj("2");
        }

//        // 填报人地址  填报人地址默认为  电话银行 95580调用该接口不传该参数 写成固定值
//        String contactAddress = parameters.getContact_Address();
//        if (StringUtils.isNotEmpty(contactAddress)) {
//            if (contactAddress.length() > 42) {
//                return resData = new ApiResData("-1", "填报人地址长度过长");
//            }
//            fmBiz.setContactAddress(contactAddress);
//        } else {
//            return resData = new ApiResData("-1", "填报人地址不能为空");
//        }
        // 涉事笔数
        String involveScount = parameters.getInvolve_Scount();
        if (StringUtils.isNotEmpty(involveScount)) {
            if (involveScount.length() > 20) {
                return resData = new ApiResData("-1", "涉事笔数长度过长");
            }
            fmBiz.setInvolveScount(involveScount);
        }

        // 交易机构
        String occurrenceAddress = parameters.getOccurrence_Address();
        if (StringUtils.isNotEmpty(occurrenceAddress)) {
            if (occurrenceAddress.length() > 42) {
                return resData = new ApiResData("-1", "交易机构长度过长");
            }
            fmBiz.setOccurrenceAddress(occurrenceAddress);
        }
        // 交易名称
        String tradingName = parameters.getTrading_Name();
        if (StringUtils.isNotEmpty(tradingName)) {
            if (tradingName.length() > 32) {
                return resData = new ApiResData("-1", "交易名称长度过长");
            }
            fmBiz.setTradingName(tradingName);
        }
        // 流水号
        String serialNumber = parameters.getSerial_Number();
        if (StringUtils.isNotEmpty(serialNumber)) {
            if (serialNumber.length() > 32) {
                return resData = new ApiResData("-1", "流水号长度过长");
            }
            fmBiz.setSerialNumber(serialNumber);
        }
        // 交易账号
        String transactionAccount = parameters.getTransaction_Account();
        if (StringUtils.isNotEmpty(transactionAccount)) {
            if (transactionAccount.length() > 64) {
                return resData = new ApiResData("-1", "交易账号长度过长");
            }
            fmBiz.setTransactionAccount(transactionAccount);
        }
        // 交易金额
        String transactionAmount = parameters.getTransaction_Amount();
        if (StringUtils.isNotEmpty(transactionAmount)) {
            if (transactionAmount.length() > 36) {
                return resData = new ApiResData("-1", "交易金额长度过长");
            }
            fmBiz.setTransactionAmount(transactionAmount);
        }
        // 客户姓名
        String customerName = parameters.getCustomer_Name();
        if (StringUtils.isNotEmpty(customerName)) {
            if (customerName.length() > 16) {
                return resData = new ApiResData("-1", "客户姓名长度过长");
            }
            fmBiz.setCustomerName(customerName);
        }
        // 客户身份证号
        String customerIDcard = parameters.getCustomer_IDcard();
        if (StringUtils.isNotEmpty(customerIDcard)) {
            if (customerIDcard.length() > 18) {
                return resData = new ApiResData("-1", "客户身份证号长度过长");
            }
            fmBiz.setCustomerIdcard(customerIDcard);
        }
        // 订单号
        String orderNumber = parameters.getOrder_Number();
        if (StringUtils.isNotEmpty(orderNumber)) {
            if (orderNumber.length() > 42) {
                return resData = new ApiResData("-1", "订单号长度过长");
            }
            fmBiz.setOrderNumber(orderNumber);
        }
        // 错误信息
        String errorInformation = parameters.getError_Information();
        if (StringUtils.isNotEmpty(errorInformation)) {
            if (errorInformation.length() > 64) {
                return resData = new ApiResData("-1", "错误信息长度过长");
            }
            fmBiz.setErrorInformation(errorInformation);
        }
        // 报告时间
        String reportTime = parameters.getReport_Time();
        if (StringUtils.isNotEmpty(reportTime)) {
            fmBiz.setReportTime(reportTime);
        }
        // 业务事件单附件
        String filePath = parameters.getFile_Path();// 附件完整路径
        System.out.println("filePath值是：" + filePath);
        try {
            if (StringUtils.isNotEmpty(filePath)) {
                OgPerson person = ogPersonService.selectOgPersonById(CREATE_ID);
                if (person == null) {
                    return new ApiResData("-1", "回退修改失败，查询创建人错误");
                }
                String[] str = filePath.split(",");
                ApiResData resDatas = new ApiResData("-1", "附件添加失败");
                for (int i = 0; i < str.length; i++) {
                    resDatas = forwardFileService.saveFortFile(fmBiz.getFmId(), CREATE_ID, str[i], resDatas);
                    if (!"0000".equals(resDatas.resCode)) {
                        return new ApiResData("-1", "附件添加失败");
                    }
                }
            }
            //判断是省中心还是全国中心赋值对应的状态
            OgOrg org = sysDeptService.selectDeptById(ogPersonService.selectOgPersonById(fmBiz.getDealerId()).getOrgId());
            if ("000000000".equals(org.getOrgCode()) || org.getLevelCode().contains("/000000000/010000888/")) {
                fmBiz.setCurrentState("04");//处理中状态
                fmBiz.setToQgzxTime(DateUtils.dateTimeNow());//添加转全国中心时间
            } else {
                fmBiz.setCurrentState("11");
            }
            /*
        页面重新修改后的内容赋值回对象中
         */
            Map<String, Object> reMap = new HashMap<>();
            reMap.put("reCode", "0");
            reMap.put("businessKey", fmBiz.getFmId());
            reMap.put("userId", fmBiz.getCreateId());
            reMap.put("processDefinitionKey", "FmBiz");
            fmBizService.updateFmBiz(fmBiz);
            activitiCommService.complete(reMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResData("-1", "回退修改失败，系统连接已断开");
        }
        return new ApiResData("0", "修改成功");
    }

    /**
     * 退回修改作废操作
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/deleteFm.api")
    @ResponseBody
    public void deleteFmBiz(HttpServletRequest request,
                            HttpServletResponse response) {
        ApiResData resData = null;
        try {
            EventBean parameters = ApiUtil.verifyAndGetReqData(request,
                    EventBean.class);
            resData = deleteFmBi(parameters);
        } catch (ApiException e) {
            resData = new ApiResData(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resData = new ApiResData("999999", "系统错误");
        }
        ApiUtil.resJSONObjUTF8(response, resData);
    }

    public ApiResData deleteFmBi(EventBean parameters) {
        String no = parameters.getFm_ID();// 拿到事件单号
        FmBiz fmBiz = fmBizService.selectFmBizByFaultNo(no);//根据事件单号查询
        if (fmBiz == null) {
            System.out.println("单号不存在");
            return new ApiResData("-1", "单号不存在");
        }
        System.out.println("单号：" + no);
        if (!"05".equals(fmBiz.getCurrentState())) {
            return new ApiResData("-1", "事件单状态不为退回修改状态，不允许修改");
        }
        fmBiz.setEndTime(DateUtils.dateTimeNow());
        fmBiz.setCurrentState("10");
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("reCode", "1");
        reMap.put("businessKey", fmBiz.getFmId());
        reMap.put("processDefinitionKey", "FmBiz");
        reMap.put("userId", fmBiz.getCreateId());
        activitiCommService.complete(reMap);
        fmBizService.updateFmBiz(fmBiz);

        return new ApiResData("0", "作废成功");
    }

    /**
     * 如果是紧急事件单发送短信
     *
     * @param groupId 工作组ID
     * @param faultNo 事件单号
     */
    private void ifJJSendMessage(String groupId, String faultNo) {
        List<OgGroupPerson> gps = sysWorkService.selectGroupIdByPersonList(groupId);
        OgGroup ogGroup = sysWorkService.selectOgGroupById(groupId);
        String sysname = "";
        if (ogGroup != null) {
            if (ogGroup.getSysId() != null) {
                sysname = sysApplicationManagerService.selectOgSysBySysId(ogGroup.getSysId()).getCaption();
            }
        }
        List<OgPerson> ops = new ArrayList<OgPerson>();
        if (!gps.isEmpty()) {
            for (OgGroupPerson gp : gps) {
                String gposition = gp.getGpOsition();//1组长 2数据中心负责人 3组员
                if ("组长".equals(gposition) || "1".equals(gposition)) {
                    ops.add(gp.getPerson());
                }
            }
            for (OgPerson op : ops) {
                //发送短信
                OgPerson person = ogPersonService.selectOgPersonEvoById(op.getpId());// 获取短信接收人
                String setSmstext = "{" + sysname + "}收到新的紧急事件单，单号是：" + faultNo + "，请及时处理。";// 短信内容
                sendSms(setSmstext, person);
            }
            sendMessageToApplication(faultNo, sysname);
        }
    }

    /**
     * 发送短信
     *
     * @param setSmsText 短信内容
     * @param person     短信接收人
     */
    public void sendSms(String setSmsText, OgPerson person) {
        PubBizPresms p = new PubBizPresms();
        p.setTelephone(person.getMobilPhone());// 手机号
        p.setName(person.getpName());// 姓名
        p.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
        p.setSmsText(setSmsText);// 短信内容
        p.setInspectTime(DateUtils.dateTimeNow());// 检查时间
        p.setInspectObject("050100");// 检查对象
        p.setCreaterId(ShiroUtils.getOgUser().getpId());// 创建人
        p.setInvalidationMark("1");
        p.setSmsState("0");
        p.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p);
        pubBizSmsService.findPreSmsAndSend(p);//发送短信
    }


    /**
     * @param faultNo
     * @param sysname 应用系统名
     */
    private void sendMessageToApplication(String faultNo, String sysname) {
        PubBizPresms p1 = new PubBizPresms();
        p1.setTelephone("13621150163");// 手机号
        p1.setName("应用二线值班");// 姓名
        p1.setSmsType("4");// 短信类型，3、4即时发送,2定时发送
        p1.setSmsText("{" + sysname + "}收到新的紧急事件单，单号是：" + faultNo + "，请及时处理。");// 短信内容
        p1.setInspectTime(DateUtils.dateTimeNow());// 检查时间
        p1.setInspectObject("050100");// 检查对象
        p1.setCreaterId(ShiroUtils.getOgUser().getpId());// 创建人
        p1.setInvalidationMark("1");
        p1.setSmsState("0");
        p1.setPubBizPresmsId(UUID.getUUIDStr());
        pubBizPresmsService.insertPubBizPresms(p1);
        pubBizSmsService.findPreSmsAndSend(p1);//发送短信
    }


    @RequestMapping(value = "/automaticCallItsm.api")
    @ResponseBody
    public void automaticCallItsm(HttpServletRequest request,
                                  HttpServletResponse response) {
        ApiResData resData = null;
        EventBean parameters = ApiUtil.verifyAndGetReqData(request,
                EventBean.class);

        resData = saveAuto(parameters);

        ApiUtil.resJSONObj(response, resData);
    }

    /**
     * 根据传入参数处理网络自动化变更单实施填报
     *
     * @param parameters
     * @return
     */
    @Transactional
    public ApiResData saveAuto(EventBean parameters) {
        String changeCode = parameters.getChangeCode();//拿到单号
        String changeStatus = parameters.getChangeStatus();//拿到状态（0-成功，1-失败，2-中断）
        String changeStartTime = parameters.getChangeStartTime();//拿到变更开始时间
        String changeEndTime = parameters.getChangeEndTime();//拿到变更结束时间
        if (StringUtils.isNotEmpty(changeCode) && StringUtils.isNotEmpty(changeStatus) && StringUtils.isNotEmpty(changeStartTime) && StringUtils.isNotEmpty(changeEndTime)) {
            CmBizSingle cmBizSingle = cmBizSingleService.getCmBizBycChangeCode(changeCode);
            cmBizSingle.setCreatetime(handleTimeYYYYMMDDHHMMSS(cmBizSingle.getCreatetime()));
            cmBizSingle.setApplicationSubmitTime(handleTimeYYYYMMDDHHMMSS(cmBizSingle.getApplicationSubmitTime()));
            cmBizSingle.setExpectStartTime(handleTimeYYYYMMDDHHMMSS(cmBizSingle.getExpectStartTime()));
            cmBizSingle.setExpectEndTime(handleTimeYYYYMMDDHHMMSS(cmBizSingle.getExpectEndTime()));
            if (cmBizSingle != null) {
                if ("0".equals(changeStatus)) {//根据自动化平台返回执行结果进行状态赋值 成功
                    cmBizSingle.setChangeSingleStauts("0801");
                    cmBizSingle.setPerformResult("0");
                    cmBizSingle.setNotesText("实施成功。");
                } else {//失败
                    cmBizSingle.setChangeSingleStauts("0802");
                    cmBizSingle.setPerformResult("1");
                    cmBizSingle.setNotesText("实施失败，请登录自动化平台查询具失败原因。");
                }
                cmBizSingle.setPracticleStart(handleTimeYYYYMMDDHHMMSS(changeStartTime));
                cmBizSingle.setPracticleEnd(handleTimeYYYYMMDDHHMMSS(changeEndTime));


                Map<String, Object> reMap = new HashMap<>();
                reMap.put("businessKey", cmBizSingle.getChangeId());
                reMap.put("processDefinitionKey", "CmZy");
                reMap.put("userId", cmBizSingle.getImplementorId());
                //判断是否及时变更
                Date nowDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                try {
                    Date endDate = sdf.parse(cmBizSingle.getPracticleEnd());
                    if (nowDate.getTime() - endDate.getTime() <= (60 * 60 * 24 * 1000)) {
                        cmBizSingle.setIsJs("1");
                    } else {
                        cmBizSingle.setIsJs("0");
                    }
                } catch (ParseException e) {
                    logger.debug("资源变更单计算是否及时变更日期转换失败，实施结束时间为：" + cmBizSingle.getPracticleEnd());
                }
                try {
                    cmBizSingleService.updateCmBizSingle(cmBizSingle);
                    activitiCommService.complete(reMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("资源变更单实施失败，请刷新列表后重新操作。");
                    return new ApiResData("-1", "资源变更单实施失败！");
                }
            }
        } else {
            return new ApiResData("-1", "传入参数有空值！");
        }
        return new ApiResData("0", "成功！");
    }

    @RequestMapping(value = "/returnExecutorResult.api")
    @ResponseBody
    public void returnExecutorResult(HttpServletRequest request,
                                     HttpServletResponse response) {
        ApiResData resData = null;
        EventBean parameters = ApiUtil.verifyAndGetReqData(request,
                EventBean.class);
        resData = saveScript(parameters);

        ApiUtil.resJSONObj(response, resData);
    }

    @RequestMapping(value = "/getCmbizByNo.api")
    @ResponseBody
    public void getCmbizByNo(HttpServletRequest request,
                             HttpServletResponse response) {
        ApiResData resData = null;
        try {
            String br = request.getReader().readLine();
            String changeNo = br.replace("\"", "");
            resData = getCmbizByChangeNo(changeNo);

        } catch (ApiException e) {
            resData = new ApiResData(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resData = new ApiResData("999999", "系统错误");
        }
        ApiUtil.resJSONObj(response, resData);
    }

    public ApiResData getCmbizByChangeNo(String changeNo) {
        CmBizSingle cbs = cmBizSingleService.getCmBizBycChangeCode(changeNo);
        ApiResData resData = new ApiResData("0", "成功");
        if (cbs != null) {
            try {
                JSONArray array = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("status", cbs.getChangeSingleStauts());
                array.put(jsonObject);
                resData.setResDataJson(array.toString());
            } catch (Exception e) {
                e.printStackTrace();
                return new ApiResData("-1", "JSON数据封装异常。");
            }
        } else {
            return new ApiResData("-1", "根据单号未查询到变更记录。");
        }
        return resData;
    }

    /**
     * 根据传入参数更新业务事件单脚本执行表数据
     *
     * @param parameters
     * @return
     */
    public ApiResData saveScript(EventBean parameters) {
        String fbsId = parameters.getFbsId();//拿到脚本记录ID
        String executorStatus = parameters.getExecutorStatus();//拿到状态（成功，失败，异常，执行中）
        String executorResult = parameters.getExecutorResult();//拿到执行结果
        String executorEndTime = parameters.getExecutorEndTime();//拿到执行结束时间

        if (StringUtils.isNotEmpty(fbsId)) {
            FmBizScript fbs = fmBizScriptService.selectFmBizScriptById(fbsId);
            if (fbs != null) {
                if (StringUtils.isNotEmpty(executorStatus))
                    fbs.setResultStatus(executorStatus);

                if (StringUtils.isNotEmpty(executorResult))
                    fbs.setExecutorResult(executorResult);

                if (StringUtils.isNotEmpty(executorEndTime))
                    fbs.setExecutorEndTime(executorEndTime);

                fmBizScriptService.updateFmBizScript(fbs);
            } else {
                return new ApiResData("-1", "脚本id为" + fbsId + "的记录不存在！");
            }
        }
        return new ApiResData("0", "成功！");
    }

    /*
     * 测试电话银行接口
     */
    private static void testCreateFm() {
        EventBean parameters = new EventBean();
        parameters.setFm_ID("SJ20211206000001");
        parameters.setS_LOG_performDesc("未解决退回");
//        parameters.setSysid("8b8080f457e76e810157f5f4e7750164");
//        parameters.setOneType("27");
//        parameters.setTwoType("29");
//        parameters.setThreeType("37");
//        parameters.setOccurrence_Address("测试部门");
//        parameters.setFault_No("SJ20211206000001");// 工单号
//        parameters.setFault_Source("03");
//        parameters.setInvolve_Amount("0");
//        parameters.setOccur_Time("20180706145800");// 注意日期格式
//        parameters.setFault_Report_Name("张三"); // 当事人 客户姓名
//        parameters.setFault_Contact_Name("电话银行");// 填报人
//        parameters.setContact_Phone("13240153333");// 填报人电话
//        parameters.setFault_Decript_Summary("电话银行测试");// 事件标题 必填项
//        parameters.setFault_Decript_Detail("电话银行测试发送20180706");// 事件描述
        // parameters.setCurrent_State("01");
//		parameters.setFile_Path("http://20.200.24.87:9001/agent/workFlow/downloadAttach.do?attach_id=w0000000063");
        // parameters.setFile_Path("http://20.13.3.29:8001/agent/workFlow/downloadAttach.do?attach_id=w0000000312,http://20.13.3.29:8001/agent/workFlow/downloadAttach.do?attach_id=w0000000314,http://20.13.3.29:8001/agent/workFlow/downloadAttach.do?attach_id=w0000000315");
        ApiResData res;
        try {
            res = ApiUtil.postJson("http://127.0.0.1:9999/cps-server/apigateway/unsolvedFm.api", parameters, 60);
            System.out.println("调用完方法给我返回的值是：" + res.getResCode() + "错误信息是："
                    + res.getResMsg());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*
     * 事件单回退修改测试
     */
    private static void testRevampFmBiz() {
        EventBean parameters = new EventBean();
        // 流程提交数据variables
        parameters.setFm_ID("SJ20210215000002");            // 事件单号
        parameters.setContact_Phone("15912305635");            // 填报人电话
        parameters.setFault_Contact_Name("MMMM");                // 填报人
        parameters.setOccur_Time("20210210163300");                // 发生时间
        parameters.setFault_Report_Name("MMMM");                // 当事人
        parameters.setFault_Decript_Summary("MMMM");        // 事件标题
        parameters.setFault_Decript_Detail("MMMMMMMM");        // 事件描述
        parameters.setReport_Phone("15912305635");            // 报告人电话
        parameters.setFault_No("SJ2018081600000005");        // 电话银行
        parameters.setFault_Source("04");                    // 信用卡
        parameters.setContact_Address("MMMMaddress");                // 填报人地址
        parameters.setInvolve_Scount("1");                    // 涉事笔数
        parameters.setInvolve_Amount("0");                    // 涉事金额
        parameters.setOccurrence_Address("MMMMocc");                // 交易机构
        parameters.setTrading_Name("MMMM4444");                    // 交易名称
        parameters.setSerial_Number("1111112");                    // 流水号
        parameters.setTransaction_Account("111111");                // 交易账号
        parameters.setTransaction_Amount("11111");            // 交易金额
        parameters.setCustomer_Name("MMMM");                    // 客户姓名
        parameters.setCustomer_IDcard("1111111");                // 客户身份证号
        parameters.setOrder_Number("111111");                        // 订单号
        parameters.setError_Information("250");                // 错误信息
        parameters.setSerious_Lev("3");                        // 事件等级
        parameters.setReport_Time("20180101");                        // 报告时间

        // 附件完整路径
//		parameters.setFile_Path("http://20.200.24.94:7001/download/FileServlet?p=workflow/w0000000329");
        // parameters.setFile_Path("http://20.13.3.29:8001/agent/workFlow/downloadAttach.do?attach_id=w0000000312,http://20.13.3.29:8001/agent/workFlow/downloadAttach.do?attach_id=w0000000314,http://20.13.3.29:8001/agent/workFlow/downloadAttach.do?attach_id=w0000000315");
        ApiResData res;
        try {
            res = ApiUtil.postJson("http://127.0.0.1:9999/apigateway/revampFm.api", parameters, 60);
            System.out.println("调用完方法给我返回的值是：" + res.getResCode() + "错误信息是：" + res.getResMsg());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试查询所属系统
     */
    private static void testGetAll() {
        ApiResData res;
        try {
            res = ApiUtil.postJson("http://127.0.0.1:9999/apigateway/getAll.api", "", 60);
            System.out.println("调用完方法给我返回的值是：" + res.getResCode() + "错误信息是：" + res.getResMsg());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试查询所属系统对应工作组和分类
     */
    private static void testGetGroupType() {
        ApiResData res;
        try {
            res = ApiUtil.postJson("http://127.0.0.1:9999/apigateway/getGroupType.api", "402888e455a57fcf0155a58037130001", 60);
            System.out.println("调用完方法给我返回的值是：" + res.getResCode() + "错误信息是：" + res.getResMsg());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试评价退回
     */
    private static void testUnsolvedFm() {
        EventBean parameters = new EventBean();
        parameters.setFm_ID("SJ20210215000002");  //事件单号
        parameters.setS_LOG_performDesc("回家吃饭"); //处理意见
        ApiResData res;
        try {
            res = ApiUtil.postJson("http://127.0.0.1:9999/apigateway/unsolvedFm.api", parameters, 60);
            System.out.println("调用完方法给我返回的值是：" + res.getResCode() + "错误信息是：" + res.getResMsg());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试评价通过
     */
    private static void testEvaluateFmBi() {
        EventBean parameters = new EventBean();
        parameters.setFm_ID("SJ20210215000002");  //事件单号
        parameters.setEvaluate_Result("1");  //评价结果
        parameters.setS_LOG_performDesc("下班了"); //处理意见
        ApiResData res;
        try {
            res = ApiUtil.postJson("http://127.0.0.1:9999/apigateway/Evaluate.api", parameters, 60);
            System.out.println("调用完方法给我返回的值是：" + res.getResCode() + "错误信息是：" + res.getResMsg());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testGetCheckNo() {
        String s = "BB20210201000001";
        ApiResData res;
        try {
            res = ApiUtil.postJson("http://127.0.0.1:9999/cps-server/apigateway/authVerifcation.api", s, 60);
            System.out.println("调用完方法给我返回的值是：" + res.getResCode() + "-----返回信息是：" + res.getResMsg());
            System.out.println(res.getResDataJson());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //测试接口
    public static void main(String[] args) {
        testCreateFm();
        //testGetCheckNo();
    }

 /*   @RequestMapping("/test")
    @ResponseBody
    public AjaxResult test(String telePhone, String vfCode){
        TestCMSLongConnectionAPI.api.receiveFlow(vfCode,telePhone,"测试通过");
        return success();
    }*/
}
