package com.ruoyi.form.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.activiti.service.ActivitiCommService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.form.constants.CustomerFlowConstants;
import com.ruoyi.form.domain.*;
import com.ruoyi.form.enums.ChmPJEnum;
import com.ruoyi.form.enums.ChmStatusEnum;
import com.ruoyi.form.enums.RedundancyFieldEnum;
import com.ruoyi.form.enums.WorkOrderInformation;
import com.ruoyi.form.mapper.CustomerFormMapper;
import com.ruoyi.form.mapper.DesignBizChmMapper;
import com.ruoyi.form.mapper.EventWhiteListMapper;
import com.ruoyi.form.service.*;
import com.ruoyi.form.util.Base64ToMultipartFile;
import com.ruoyi.form.util.RSAUtil;
import com.ruoyi.form.util.VueDataJsonUtil;
import com.ruoyi.framework.config.MybatisPlusConfig;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import com.ruoyi.form.domain.DesignBizChm;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysBizFileService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChmApiServiceImpl extends ServiceImpl<EventWhiteListMapper, EventWhiteList> implements ChmApiService {
    @Value("${openApiPath.url}")
    private String openApiUrl;
    @Value("${openApiPath.token}")
    private String token;
    @Autowired
    private CustomerFormService customerFormService;
    @Autowired
    private  CustomerFormMapper customerFormMapper;
    @Autowired
    private  OperationDetailsService operationDetailsService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private FormVersionService formVersionService;
    @Resource
    IOgUserService ogUserService;
    @Autowired
    DesignBizChmMapper designBizChmMapper;
    @Autowired
    IChmParavalueService iChmParavalueService;
    @Autowired
    IChmBasedataService iChmBasedataService;
    @Autowired
    ForeignService foreignService;
    @Autowired
    ActivitiCommService activitiCommService;
    @Autowired
    DesignBizJsonDataService designBizJsonDataService;
    @Autowired
    ISysBizFileService iSysBizFileService;
    protected final Logger logger = LoggerFactory.getLogger(ChmApiServiceImpl.class);
    /**
     * 移动端开单接口
     * @param request
     * @return
     */
    @Override
    public JSONObject createEvent(JSONObject request){
        logger.info("================================移动端开单入参："+request);
        JSONObject rejson=JSONObject.parseObject("{\"code\": \"0\",\"imCode\": \"\",\"msg\": \"操作成功\" }");
        CustomerFormContent customerFormContent=new CustomerFormContent();
        customerFormContent.setCode(WorkOrderInformation.chm_task.getCode());
        String title=request.getString("title");
        String branchName=request.getString("branchName");
        String contact=request.getString("contact");
        String contactDetails=request.getString("contactDetails");
        String equipmentType=request.getString("equipmentType");
        String equipmentName=request.getString("equipmentName");
        String equipmentModel=request.getString("equipmentModel");
        String expectTime=request.getString("expectTime");
        String faultDescription=request.getString("faultDescription");
        String source=request.getString("source");
        String requestId=request.getString("requestId");
        String createBy=request.getString("createBy");
        OgUser ogUser = ogUserService.selectTimeByUsername(createBy);
        if(ogUser==null){
            ogUser=ogUserService.selectTimeByUsername(CustomerFlowConstants.CHM_ADMIN);
        }
        CustomerBizInterceptor.currentUserThread.set(ogUser);
        //设备标识号
        String equipmentId=request.getString("equipmentId");
        //机具号
        String equipmentNo=request.getString("equipmentNo");
        Map<String,Object> mapFiles=new HashMap<>();
        Map<String,Object> newMap=new HashMap<>();
        mapFiles.put("title",title);
        mapFiles.put("chm_type",1);
        Map<String,Object> typeMap=new HashMap<>();
        typeMap.put("硬件",1);
        newMap.put("chm_type",typeMap);
        ChmBasedata chmBasedata=iChmBasedataService.selectChmBasedataByName(branchName);
        mapFiles.put("report_department",chmBasedata.getId().toString());
        Map<String,Object> reMap=new HashMap<>();
        reMap.put(chmBasedata.getOrgName(),chmBasedata.getId());
        newMap.put("report_department",reMap);
        mapFiles.put("contact",contact);
        mapFiles.put("contact_details",contactDetails);
        ChmParavalue chmParavalue=iChmParavalueService.selectChmParavalueByName(equipmentType);
        if(chmBasedata!=null){
            mapFiles.put("equipment_type",chmParavalue.getId().toString());
        }

        ChmParavalue chmParavalue1=new ChmParavalue();
        chmParavalue1.setName(equipmentName);
        chmParavalue1.setLevels("2");
        List<ChmParavalue> list1=iChmParavalueService.selectChmParavalueList(chmParavalue1);
        if(CollectionUtil.isNotEmpty(list1)){
            mapFiles.put("equipment_name",list1.get(0).getId().toString());
        }

        ChmParavalue chmParavalue2=new ChmParavalue();
        chmParavalue2.setName(equipmentModel);
        chmParavalue2.setLevels("3");
        List<ChmParavalue> list2=iChmParavalueService.selectChmParavalueList(chmParavalue2);
        if(CollectionUtil.isNotEmpty(list2)){
            mapFiles.put("equipment_model",list2.get(0).getId().toString());
        }

        mapFiles.put("expect_time",expectTime);
        mapFiles.put("fault_Description",faultDescription);
        mapFiles.put("source",source);
        mapFiles.put("request_Id",requestId);
        mapFiles.put("implement_Id",equipmentId);//设备标识号
        mapFiles.put("implement_no",equipmentNo);//机具号
        mapFiles.put("created_by",createBy);
        DesignFormVersion formVersion = formVersionService.getOne(new QueryWrapper<DesignFormVersion>().eq("code", "design_biz_chm").eq("is_current", 1));
        String dataJson = VueDataJsonUtil.analysisDataJson(formVersion.getJson(), mapFiles);
        dataJson=VueDataJsonUtil.analysisDataJsonSelect(dataJson,newMap);
        Map<String,Object> tMap=new HashMap<>();
        tMap.put(ogUser.getPname(),ogUser.getpId());
        tMap.put("default",ogUser.getpId());
        Map<String,Object> mp=new HashMap<>();
        mp.put("created_by",tMap);
        dataJson=VueDataJsonUtil.analysisDataJsonSelect(dataJson, mp);
        customerFormContent.setDataJson(dataJson);
        customerFormContent.setFields(mapFiles);
        Integer bizId= customerFormService.dataOperation(WorkOrderInformation.chm_task.getCode(),customerFormContent);
        if(StringUtils.isEmpty(bizId)){
            rejson.put("code","500");
            rejson.put("msg","数据储存失败！");
            logger.info("================================移动端开单数据存储失败====");
            return rejson;
        }
        String version = String.valueOf(customerFormMapper.getCurrentTableInfo(String.format("%s_%s", "design_biz", WorkOrderInformation.chm_task.getCode()), null).get("version"));
        Map<String,Object> variables=new HashMap<>();
        variables.put("userNo",ogUser.getpId());
        AjaxResult ajaxResult=customerFormService.processSubmit(WorkOrderInformation.chm_task.getCode(),bizId.toString(),version,variables);
        dynamicTableName(WorkOrderInformation.chm_task.getCode());
        DesignBizChm designBizChm =  designBizChmMapper.selectOne(Wrappers.<DesignBizChm>query().select(RedundancyFieldEnum.extra1.name).eq("id", bizId));
        rejson.put("imCode",designBizChm.getExtra1());
        OperationDetails details = OperationDetails.builder().operationType("硬件报障").bizNo(designBizChm.getExtra1()).createdName(ogUser.getPname()).createdBy(ogUser.getpId()).description("移动端发起硬件报障").build();
        operationDetailsService.save(details);
        logger.info("================================移动端开单数据存储成功====");
        return rejson;
    }

    /**
     * 移动端人员故障单查询接口
     * @param request
     * @return
     */
    @Override
    public JSONObject getUserListDetail(JSONObject request){
        logger.info("===============================移动端人员故障单查询接口入参："+request);
        dynamicTableName(WorkOrderInformation.chm_task.getCode());
        JSONObject reJson=JSONObject.parseObject("{\"code\": \"0\",\"imCode\": \"\",\"msg\": \"操作成功\" }");
        String contact=request.getString("createBy");//工号
//        if(StringUtils.isEmpty(userNo)){
//            reJson.put("code","500");
//            reJson.put("msg","用户工号为空！");
//            return reJson;
//        }
        String imCode=request.getString("imCode");//编码
        String status=request.getString("status");//状态
        String pageNum=request.getString("pageNum");//页码
        String pageSize=request.getString("pageSize");//分页条数
        QueryWrapper<CustomerFormContent> wrapper=Wrappers.<CustomerFormContent>query()//
                .select("title",RedundancyFieldEnum.extra1.name+" as imCode","report_department as branchName","contact","contact_details as contactDetails","equipment_type as equipmentType",
                        "equipment_name as equipmentName","equipment_model as equipmentModel","status","expect_time as expectTime","fault_Description as faultDescription","implement_Id as equipmentId","implement_no as equipmentNo");
        if(StringUtils.isNotEmpty(imCode)){
            wrapper.eq(RedundancyFieldEnum.extra1.name,imCode);
        }
        if(StringUtils.isNotEmpty(contact)){
            OgUser ogUser = ogUserService.selectTimeByUsername(contact);
            if(ogUser!=null){
                wrapper.eq("created_by", ogUser.getUserId());
            }
        }
        if(StringUtils.isNotEmpty(status)){
            status= ChmStatusEnum.getInfo(status);
            wrapper.eq("status",status);
            if("已完成".equals(status)){
                if(StringUtils.isNotEmpty(request.getString("startDate"))&&StringUtils.isNotEmpty(request.getString("endDate"))){
                    wrapper.ge("updated_time",request.getString("startDate"));
                    wrapper.le("updated_time",request.getString("endDate"));
                }
            }
        }
        Page page=buildPageObject();
        if(StringUtils.isNotEmpty(pageNum)){
            page.setCurrent(Long.valueOf(pageNum));
        }
        if(StringUtils.isNotEmpty(pageSize)){
            page.setSize(Long.valueOf(pageSize));
        }
        Page pg = customerFormMapper.selectMapsPage(page,wrapper);
        List<Map<String,Object>> list=pg.getRecords();
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        list.forEach(event->{
            String st=event.get("status").toString();
            event.put("status",ChmStatusEnum.getCode(st));
            String custNo=event.get("imCode").toString();
            String equipmentType=event.get("equipmentType").toString();
            ChmParavalue chmParavalue=iChmParavalueService.selectChmParavalueById(Long.valueOf(equipmentType));
            event.put("equipmentType",chmParavalue.getName());

            String equipmentName=event.get("equipmentName").toString();
            chmParavalue=iChmParavalueService.selectChmParavalueById(Long.valueOf(equipmentName));
            event.put("equipmentName",chmParavalue.getName());

            String equipmentModel=event.get("equipmentModel").toString();
            chmParavalue=iChmParavalueService.selectChmParavalueById(Long.valueOf(equipmentModel));
            event.put("equipmentModel",chmParavalue.getName());
            String branchName=event.get("branchName").toString();
            ChmBasedata chmBasedata=iChmBasedataService.selectChmBasedataById(Long.valueOf(branchName));
            event.put("branchName",chmBasedata.getOrgName());
            List<Map<String,Object>> relist=new ArrayList<>();
            List<OperationDetails> ls=operationDetailsService.list(Wrappers.<OperationDetails>query().eq(OperationDetails.COL_BIZ_NO, custNo).orderByDesc(OperationDetails.COL_ID));
            ls.forEach(detail->{
                Map<String,Object> map=new HashMap<>();
                map.put("dealTime",sf.format(detail.getCreatedTime()));
                map.put("dealUser",detail.getCreatedName());
                map.put("operation",detail.getDescription());
                relist.add(map);
            });
            event.put("detail",JSON.toJSONString(relist));
            if(StringUtils.isEmpty(event.get("equipmentNo"))){
                event.put("equipmentNo","-");
            }
            if(StringUtils.isEmpty(event.get("equipmentId"))){
                event.put("equipmentId","-");
            }
        });
        ///
        JSONArray jsonArray= (JSONArray) JSONArray.parse(JSON.toJSONString(list));
        reJson.put("total",page.getTotal());
        reJson.put("data",jsonArray);
        logger.info("===============================移动端人员故障单查询"+reJson.toJSONString());
        return  reJson;
    }

    /**
     * 合胜开单接口
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public JSONObject createHeshengOpenApi(String Id,OgUser ogUser,String businessKey) {
        logger.info("==================================合胜开单接口ID："+Id);
        dynamicTableName(WorkOrderInformation.chm_task.getCode());
        QueryWrapper<DesignBizChm> wrapper=Wrappers.<DesignBizChm>query()//
                .select("*")
                .eq("id",Id);
        DesignBizChm designBizChm=  designBizChmMapper.selectOne(wrapper);
        JSONObject chm=new JSONObject();
        JSONObject reRsult=new JSONObject();
        try {
            for(Field field:designBizChm.getClass().getDeclaredFields()){
                field.setAccessible(true);
                if(field.get(designBizChm)!=null){
                    chm.put(field.getName(),field.get(designBizChm));
                }
            }
            ChmParavalue chmParavalue=iChmParavalueService.selectChmParavalueById(Long.valueOf(chm.getString("equipmentType")));
            chm.put("equipmentType",chmParavalue.getName());
            ChmParavalue chmParavalue2=iChmParavalueService.selectChmParavalueById(Long.valueOf(chm.getString("equipmentName")));
            chm.put("equipmentName",chmParavalue2.getName());
            ChmParavalue chmParavalue3=iChmParavalueService.selectChmParavalueById(Long.valueOf(chm.getString("equipmentModel")));
            chm.put("equipmentModel",chmParavalue3.getName());
            //设备标识号
            chm.put("deviceId",designBizChm.getImplementId());
            //机具号
            chm.put("toolNo",designBizChm.getImplementNo());
            chm.put("serviceFormNo",designBizChm.getExtra1());
            //预约时间 创建时间
            chm.put("appointment",designBizChm.getExpectTime());
            ChmBasedata chmBasedata=iChmBasedataService.selectChmBasedataById(Long.valueOf(designBizChm.getReportDepartment()));
            chm.put("branchName",chmBasedata.getOrgName());
            //是否加急
            chm.put("urgent",designBizChm.getUrgent().toString());
            //影响范围
            chm.put("incidence",designBizChm.getScopeInfluence());
            chm.remove("priority");
            String params=chm.toJSONString();
            logger.info("==================================合胜开单接口入参："+params);
            //调用合胜接口
            String reJson= HttpUtils.sendPostBody(openApiUrl+"/bosc/api/nonstandard/v1.0/technology/other/omsmitsm/hardwareReportFailureBilling",params);
            logger.info("==================================合胜开单接口返回结果："+reJson);
            //验证返回结果
            if(StringUtils.isNotEmpty(reJson)){
                reRsult=JSON.parseObject(reJson);
                if("0000".equals(reRsult.getString("code"))){
                    String flowNo=reRsult.getJSONObject("data").getString("flowNo");
                    designBizChm.setFlowNo(flowNo);
                    designBizChm.setStatus("进行中");
                    designBizChmMapper.update(designBizChm,new UpdateWrapper<DesignBizChm>().eq("id", designBizChm.getId()));
                    CustomerFormContent customerFormContent=new CustomerFormContent();
                    customerFormContent.setCode(WorkOrderInformation.chm_task.getCode());
                    Map<String,Object> mapFiles=new HashMap<>();
                    mapFiles.put("status","进行中");
                    mapFiles.put("id",designBizChm.getId());
                    DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                            .eq(DesignBizJsonData.COL_BIZ_ID, designBizChm.getId())
                            .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", "design_biz", WorkOrderInformation.chm_task.getCode())));
                    String dataJson = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), mapFiles);
                    customerFormContent.setDataJson(dataJson);
                    customerFormContent.setFields(mapFiles);
                    Integer bizId= customerFormService.dataOperation(WorkOrderInformation.chm_task.getCode(),customerFormContent);
                    OperationDetails details = OperationDetails.builder().operationType("硬件报障").bizNo(designBizChm.getExtra1()).description("订单推送厂商成功").createdName(ogUser.getPname()).createdBy(ogUser.getpId()).build();
                    operationDetailsService.save(details);
                }else {
                    OperationDetails details = OperationDetails.builder().operationType("硬件报障").bizNo(designBizChm.getExtra1()).description("订单推送厂商失败返回参数异常").createdName(ogUser.getPname()).createdBy(ogUser.getpId()).highWarn("1").build();
                    operationDetailsService.save(details);

                }
            }else {
                OperationDetails details = OperationDetails.builder().operationType("硬件报障").bizNo(designBizChm.getExtra1()).description("订单推送厂商失败网络不通").createdName(ogUser.getPname()).createdBy(ogUser.getpId()).highWarn("1").build();
                operationDetailsService.save(details);
            }
        }catch (Exception e){
            OperationDetails details = OperationDetails.builder().operationType("硬件报障").bizNo(designBizChm.getExtra1()).description("订单推送厂商失败网络不通").createdName(ogUser.getPname()).createdBy(ogUser.getpId()).highWarn("1").build();
            operationDetailsService.save(details);
            logger.error("厂商接口调用失败"+e.getMessage());
        }
        return reRsult;
    }

    /**
     * 合胜白名单同步接口
     * @return
     */
    public void supplierDTDEngineer() throws Exception {
        String body="{token:"+token+"}";
        //调用合胜接口
        String reJson= HttpUtils.sendPostBody(openApiUrl+"/itsmApp/api/flow/supplierDTDEngineer",RSAUtil.decrypt(body,"UTF-8"));
        //解密
        String result=RSAUtil.decrypt(reJson,"UTF-8");
        JSONObject resultJson=JSONObject.parseObject(result);
        //接口调用成功
        if("0000".equals(resultJson.getString("code"))){
            JSONArray jsonArray=resultJson.getJSONArray("data");
            if(!jsonArray.isEmpty()){
                for(Object jt:jsonArray){
                    JSONObject js=(JSONObject)jt;
                    String supplier=js.getString("supplier");//厂商
                    //人员
                    JSONArray jsa=js.getJSONArray("DTDEngineers");
                    jsa.forEach(p->{
                        JSONObject person=(JSONObject)p;
                        QueryWrapper<EventWhiteList> queryWrapper = new QueryWrapper<EventWhiteList>().eq("certificates_number",person.getString("idCard"));
                        EventWhiteList eventWhiteList = baseMapper.selectOne(queryWrapper);
                        if(eventWhiteList != null) {
                            eventWhiteList.setName(person.getString("userName"));
                            eventWhiteList.setPhoneNumber(person.getString("mobile"));
                            eventWhiteList.setVendor(supplier);
                            baseMapper.update(eventWhiteList, new UpdateWrapper<EventWhiteList>().eq("certificates_number", person.getString("idCard")));
                        } else {
                            eventWhiteList.setName(person.getString("userName"));
                            eventWhiteList.setPhoneNumber(person.getString("mobile"));
                            eventWhiteList.setVendor(supplier);
                            eventWhiteList.setCertificatesNumber(person.getString("idCard"));
                            baseMapper.insert(eventWhiteList);
                        }

                    });
                }
            }
        }
    }
    /**
     * 查询合胜进度
     * @return
     */
    public JSONObject getprocessingProgress(String imCode) {
        QueryWrapper<DesignBizChm> wrapper=Wrappers.<DesignBizChm>query()//
                .select("*")
                .eq(RedundancyFieldEnum.extra1.name,imCode);
        DesignBizChm designBizChm=  designBizChmMapper.selectOne(wrapper);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("flowNo",designBizChm.getFlowNo());
        OgUser ogUser = ogUserService.selectTimeByUsername("admin");
        CustomerBizInterceptor.currentUserThread.set(ogUser);
        String reJson=null;
        //调用合胜接口
        try{
            reJson= HttpUtils.sendPostBody(openApiUrl+"/bosc/api/nonstandard/v1.0/technology/other/omsmitsm/handleProgressQuery",jsonObject.toJSONString());

        }catch (Exception e){
            logger.error("============厂商接口调用失败："+e.getMessage());
        }
        if(StringUtils.isNotEmpty(reJson)) {
            logger.info("===================厂商进度查询："+reJson);
            JSONObject result = JSONObject.parseObject(reJson);
            Map<String, Object> mapFiles = new HashMap<>();
            if (result != null && "0000".equals(result.getString("code"))) {
                JSONArray jsonArray = result.getJSONArray("data");
                jsonArray.forEach(ja -> {
                    JSONObject j = JSONObject.parseObject(ja.toString());
                    String operationContent = j.getString("operationContent");
                    //合胜开单时间
                    if ("系统开单".equals(operationContent)) {
                        String dealTime = j.getString("dealTime");
                        mapFiles.put("order_time", dealTime);
                        designBizChm.setOrderTime(dealTime);
                    }
                    if ("组长派单".equals(operationContent)) {
                        String dealTime = j.getString("dealTime");
                        mapFiles.put("facturer_accept_time", dealTime);
                        designBizChm.setFacturerAcceptTime(dealTime);
                    }
                    if ("工程师接单".equals(operationContent)) {
                        String dealTime = j.getString("dealTime");
                        mapFiles.put("engineer_accept_time", dealTime);
                        designBizChm.setRealDealUser(j.getString("dealUser"));
                        designBizChm.setEngineerAcceptTime(dealTime);
                    }
                    if ("工程师到场确认".equals(operationContent)) {
                        String dealTime = j.getString("dealTime");
                        mapFiles.put("engineer_arrive_time", dealTime);
                        designBizChm.setEngineerArriveTime(dealTime);
                    }
                    if ("工程师工单完成".equals(operationContent)) {
                        String dealTime = j.getString("dealTime");
                        mapFiles.put("complete_time", dealTime);
                        designBizChm.setCompleteTime(dealTime);
                    }
                    if("退回中台".equals(j.getString("operation"))){
                        result.put("return",j.getString("operationContent"));
                    }
                });
                mapFiles.put("id", designBizChm.getId());
                CustomerFormContent customerFormContent = new CustomerFormContent();
                customerFormContent.setCode(WorkOrderInformation.chm_task.getCode());
                DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                        .eq(DesignBizJsonData.COL_BIZ_ID, designBizChm.getId())
                        .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", "design_biz", WorkOrderInformation.chm_task.getCode())));
                String dataJson = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), mapFiles);
                customerFormContent.setDataJson(dataJson);
                customerFormContent.setFields(mapFiles);
                Integer bizId = customerFormService.dataOperation(WorkOrderInformation.chm_task.getCode(), customerFormContent);
                designBizChmMapper.update(designBizChm, new UpdateWrapper<DesignBizChm>().eq("id", designBizChm.getId()));
            }
            return result;
        }else {
            return  null;
        }
    }
    public AjaxResult getprocessingProgressList(String imCode){
        JSONObject jsonObject=getprocessingProgress(imCode);
      //    jsonObject=JSONObject.parseObject("{\"code\":\"0000\",\"data\":[{\"dealUser\":\"虞莉\",\"dealTime\":\" 2021-08-17 14:09:37\",\"operation\":\"创建工单\"},{\"dealUser\":\"虞莉\",\"dealTime\":\" 2021-08-19 14:09:37\",\"operation\":\"点击发起\"}],\"serviceTime\":1660893933300}");
        Map<String, Object> resultMap = new HashMap<>();
        if(StringUtils.isNotEmpty(jsonObject)&&"0000".equals(jsonObject.getString("code"))) {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            resultMap.put("total", jsonArray.size());
            List<Map<String, Object>> colLists = new ArrayList<>();
            Map<String, Object> colMap1 = new HashMap<>();
            Map<String, Object> colMap2 = new HashMap<>();
            Map<String, Object> colMap3 = new HashMap<>();
            Map<String, Object> colMap4 = new HashMap<>();
            Map<String, Object> colMap5 = new HashMap<>();
            colMap1.put("label", "操作人");
            colMap1.put("val", "dealUser");
            colMap2.put("label", "处理时间");
            colMap2.put("val", "dealTime");
            colMap3.put("label", "操作");
            colMap3.put("val", "operation");
            colMap4.put("label", "描述");
            colMap4.put("val", "operationContent");
            colLists.add(colMap1);
            colLists.add(colMap2);
            colLists.add(colMap3);
            colLists.add(colMap4);
            colLists.add(colMap5);
            resultMap.put("col", colLists);
            resultMap.put("list", jsonArray);
        }
        return AjaxResult.success(resultMap);

    }
    /**
     * 合胜白名单校验接口
     * @return
     */
    @Override
    public JSONObject whitelistVerification(JSONObject request) {
        logger.info("=============================合胜白名单校验接口入参："+request);
        JSONObject reJson=JSONObject.parseObject("{\"code\": \"0\",\"imCode\": \"\",\"msg\": \"验证成功\" }");
        String imCode=request.getString("imCode");
        String userName=request.getString("name");
        String mobile=request.getString("phoneNumber");
        String idCard=request.getString("certificatesNumber");
        String supplier=request.getString("supplier");
        String estimateGoTime=request.getString("estimateGoTime");
        OgUser ogUser = ogUserService.selectTimeByUsername(CustomerFlowConstants.CHM_ADMIN);
        CustomerBizInterceptor.currentUserThread.set(ogUser);
        QueryWrapper<DesignBizChm> wrapper=Wrappers.<DesignBizChm>query()//
                .select("*")
                .eq(RedundancyFieldEnum.extra1.name,imCode);
        DesignBizChm designBizChm=  designBizChmMapper.selectOne(wrapper);
        if(designBizChm==null){
            reJson.put("code",500);
            reJson.put("msg","事件单不存在!单号："+imCode);
            logger.info("=============================合胜白名单校验接口：事件单不存在!单号："+imCode);
            return reJson;
        }else {
            reJson.put("imCode",imCode);
            MybatisPlusConfig.customerTableName.set("event_white_list");
            QueryWrapper<EventWhiteList> queryWrapper = new QueryWrapper<EventWhiteList>().eq("certificates_number",idCard);
            EventWhiteList eventWhiteList = baseMapper.selectOne(queryWrapper);
            if(eventWhiteList==null){
                reJson.put("code",500);
                reJson.put("msg","服务人员不存在!报文："+request);
            }else {
                if(!userName.equals(eventWhiteList.getName())){
                    reJson.put("code",500);
                    reJson.put("msg","服务人员名字不符!详情："+userName+"->"+eventWhiteList.getName());
                }
                if(!mobile.equals(eventWhiteList.getPhoneNumber())){
                    reJson.put("code",500);
                    reJson.put("msg","服务人员电话号不符!详情："+mobile+"->"+eventWhiteList.getPhoneNumber());
                }
                if(!supplier.equals(eventWhiteList.getVendor())){
                    reJson.put("code",500);
                    reJson.put("msg","服务人员关联厂商不符!详情："+supplier+"->"+eventWhiteList.getVendor());
                }

            }
            if("0".equals(reJson.getString("code"))){
                designBizChm.setFacturer(supplier);
                designBizChm.setRealDealUser(userName);
                designBizChm.setCurrentHandlerId(userName);
                designBizChmMapper.update(designBizChm,new UpdateWrapper<DesignBizChm>().eq("id", designBizChm.getId()));
                CustomerFormContent customerFormContent=new CustomerFormContent();
                customerFormContent.setCode(WorkOrderInformation.chm_task.getCode());
                Map<String,Object> mapFiles=new HashMap<>();
                mapFiles.put("facturer",supplier);
                mapFiles.put("id",designBizChm.getId());
                DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                        .eq(DesignBizJsonData.COL_BIZ_ID, designBizChm.getId())
                        .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", "design_biz", WorkOrderInformation.chm_task.getCode())));
                String dataJson = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), mapFiles);
                customerFormContent.setDataJson(dataJson);
                customerFormContent.setFields(mapFiles);
                Integer bizId= customerFormService.dataOperation(WorkOrderInformation.chm_task.getCode(),customerFormContent);
                getprocessingProgress(imCode);
                sendSmsMessgae(designBizChm.getExtra1(),designBizChm.getFaultDescription(),userName,idCard,designBizChm.getContactDetails(),mobile,estimateGoTime);
            }
        }
        logger.info("=============================合胜白名单校验接口验证结果："+reJson.toJSONString());
        return reJson;
    }
    private void sendSmsMessgae( String extra1, String faultDescription, String userName, String idCard, String mobile,String phone,String estimateGoTime) {
       String msg="事件号:" +extra1+","+
               "故障描述:" +faultDescription+
               "系统已分派厂商工程师"+userName+"处理，" +
               "工程师身份证号:"+idCard +//.replaceAll("(\\d{6})\\d+(\\w{4})", "$1*****$2")+"，"
               "工程师联系方式:"+phone+"，" +
               "预计到达时间:" +estimateGoTime+"。";
        foreignService.sendSmsMessageBySocket("硬件报障",extra1,mobile,msg);
    }

    /**
     * 合胜返回处理结果
     * @return
     */
    @Override
    public JSONObject receiveResult(JSONObject request){
        logger.info("======================================合胜返回处理结果入参："+request);
        JSONObject reJson=JSONObject.parseObject("{\"code\": \"0\",\"imCode\": \"\",\"msg\": \"操作成功\" }");
        Map<String,Object> variables=new HashMap<>();
        String imCode=request.getString("imCode");
        OgUser ogUser = ogUserService.selectTimeByUsername(CustomerFlowConstants.CHM_ADMIN);
        CustomerBizInterceptor.currentUserThread.set(ogUser);
        QueryWrapper<DesignBizChm> wrapper=Wrappers.<DesignBizChm>query()//
                .select("*")
                .eq(RedundancyFieldEnum.extra1.name,imCode);
        DesignBizChm designBizChm=  designBizChmMapper.selectOne(wrapper);
        if(designBizChm==null){
            reJson.put("code",500);
            reJson.put("msg","事件单不存在，单号："+imCode);
            logger.info("======================================合胜返回处理结果接口：事件单不存在，单号："+imCode);
            return reJson;
        }else {
            String receiveFlag=request.getString("receiveFlag");
            String valuationDesc=request.getString("valuationDesc");//操作记录
            String instanceId=designBizChm.getInstanceId();
            Task task = processEngine.getTaskService().createTaskQuery().processInstanceId(instanceId).singleResult();
            if(!"和胜".equals(task.getName() )){
                return reJson;
            }
            variables.put("reCode","Y".equals(receiveFlag)?"0":"1");
            variables.put("dealGroup", CustomerFlowConstants.CHM_IT_GROUP);
            variables.put("loginUser",ogUser.getpId());
            taskService.complete(task.getId(),variables);
            designBizChm.setDealNotes(valuationDesc);
            String faultType=request.getString("chmType");//故障类型
            String solution=request.getString("solution");//解决方式
            if(StringUtils.isNotEmpty(solution)){
                if("1".equals(solution)){
                    //厂商受理时间
                    designBizChm.setFacturerAcceptTime(request.getString("supplierAcceptTime"));
                    //工程师受理时间
                    designBizChm.setEngineerAcceptTime(request.getString("engineerAcceptTime"));
                    //工程师完成时间
                    designBizChm.setEngineerArriveTime(request.getString("engineerArriveTime"));
                }
            }
            String supplier=request.getString("supplier");
            String remarks=request.getString("remarks");//备注
            String dealNotes=request.getString("dealNotes");//处理过程记录
            String equipmentType=request.getString("equipmentType");//设备一级
            String equipmentName=request.getString("equipmentName");//设备二级
            String equipmentModel=request.getString("equipmentModel");//设备三级

            ChmParavalue chmParavalue=iChmParavalueService.selectChmParavalueByName(equipmentType);
            if(chmParavalue!=null){
                designBizChm.setEquipmentTypeB(chmParavalue.getId().toString());
            }

            ChmParavalue chmParavalue2=new ChmParavalue();
            chmParavalue2.setName(equipmentName);
            chmParavalue2.setLevels("2");
            List<ChmParavalue> list2=iChmParavalueService.selectChmParavalueList(chmParavalue2);
            if(CollectionUtil.isNotEmpty(list2)){
                designBizChm.setEquipmentNameB(list2.get(0).getId().toString());
            }

            ChmParavalue chmParavalue3=new ChmParavalue();
            chmParavalue3.setName(equipmentModel);
            chmParavalue3.setLevels("3");
            List<ChmParavalue> list3=iChmParavalueService.selectChmParavalueList(chmParavalue3);
            if(CollectionUtil.isNotEmpty(list3)){
                designBizChm.setEquipmentModelB(list3.get(0).getId().toString());
            }
            try {
                String base64=request.getString("fileImg");
                if(StringUtils.isNotEmpty(base64)){
                    base64.replace("\\r\\n", "");
                    MultipartFile multipartFile = new Base64ToMultipartFile(base64,"jpg","image/jpg" );
                    iSysBizFileService.upload(multipartFile,"chm"+designBizChm.getId(),ogUser.getUserId());
                }else {
                    logger.error("================================厂商返回附件为空================================");
                }

            }catch (Exception e){
                OperationDetails details = OperationDetails.builder().operationType("硬件报障").bizNo(designBizChm.getExtra1()).description("厂商返回处理结果附件保存失败！").build();
                operationDetailsService.save(details);
                logger.error("================================厂商返回附件上传失败================================");
                e.printStackTrace();
            }
            designBizChm.setServiceEvaluation(ChmPJEnum.getCode(valuationDesc));
            designBizChm.setFaultType(faultType);
            designBizChm.setSolution(solution);
            designBizChm.setRemarks(remarks);
            designBizChm.setDealNotes(dealNotes);
            designBizChm.setFacturer(supplier);
            designBizChm.setStatus("待复核");
            if("N".equals(receiveFlag)){
                JSONObject jsonObject=getprocessingProgress(imCode);
                designBizChm.setDealNotes(jsonObject.getString("retrun"));
            }
            designBizChmMapper.update(designBizChm,new UpdateWrapper<DesignBizChm>().eq("id", designBizChm.getId()));
            CustomerFormContent customerFormContent=new CustomerFormContent();
            customerFormContent.setCode(WorkOrderInformation.chm_task.getCode());
            Map<String,Object> mapFiles=new HashMap<>();
            if(StringUtils.isNotEmpty(solution)){
                if("1".equals(solution)) {
                    //厂商受理时间
                    mapFiles.put("facturer_accept_time",request.getString("supplierAcceptTime"));
                    //工程师受理时间
                    mapFiles.put("engineer_accept_time",request.getString("engineerAcceptTime"));
                    //工程师到场时间
                    mapFiles.put("engineer_arrive_time",request.getString("engineerArriveTime"));
                }
                }
            mapFiles.put("fault_type",faultType);
            mapFiles.put("solution",solution);
            mapFiles.put("remarks",remarks);
            mapFiles.put("deal_notes",dealNotes);
            mapFiles.put("equipment_type_b",designBizChm.getEquipmentTypeB());
            mapFiles.put("equipment_name_b",designBizChm.getEquipmentNameB());
            mapFiles.put("equipment_model_b",designBizChm.getEquipmentModelB());
            DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                    .eq(DesignBizJsonData.COL_BIZ_ID, designBizChm.getId())
                    .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", "design_biz", WorkOrderInformation.chm_task.getCode())));
            String dataJson = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), mapFiles);
            Map<String,Object> map=new HashMap<>();
            Map<String,Object> typeMap=new HashMap<>();
            typeMap.put("default",faultType);
            map.put("fault_type",typeMap);
            Map<String,Object> slMap=new HashMap<>();
            slMap.put("default",solution);
            map.put("solution",slMap);
            Map<String, Object> service_evaluation=new HashMap<>();
            service_evaluation.put("default",ChmPJEnum.getCode(valuationDesc));
            map.put("service_evaluation",service_evaluation);
            dataJson = VueDataJsonUtil.anElDataJsonSelect(dataJson, map);
            customerFormContent.setDataJson(dataJson);
            mapFiles.put("id",designBizChm.getId());
            mapFiles.put("status","待复核");
            customerFormContent.setFields(mapFiles);
            Integer bizId= customerFormService.dataOperation(WorkOrderInformation.chm_task.getCode(),customerFormContent);
            OperationDetails details = OperationDetails.builder().operationType("硬件报障").bizNo(designBizChm.getExtra1()).description("厂商返回处理结果："+valuationDesc).build();
            operationDetailsService.save(details);
        }
        getprocessingProgress(imCode);
        reJson.put("imCode",imCode);
        logger.info("======================================合胜返回处理结果接口："+reJson.toJSONString());
        return reJson;

    }
    public Page buildPageObject(){
        Integer pageNum = 0;
        Integer pageSize = 10;
        Page page=new Page<>(pageNum,pageSize);
        return page;
    }
    /**
     * 构造动态表名
     *
     * @param code 表名
     */
    private void dynamicTableName(String code) {
        MybatisPlusConfig.customerTableName.set(String.format("%s_%s", "design_biz", code));
    }
    /**
     *
     * @return
     */
//    public JSONObject createWorkOrder(){
//
//
//
//    }
}
