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
     * ?????????????????????
     * @param request
     * @return
     */
    @Override
    public JSONObject createEvent(JSONObject request){
        logger.info("================================????????????????????????"+request);
        JSONObject rejson=JSONObject.parseObject("{\"code\": \"0\",\"imCode\": \"\",\"msg\": \"????????????\" }");
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
        //???????????????
        String equipmentId=request.getString("equipmentId");
        //?????????
        String equipmentNo=request.getString("equipmentNo");
        Map<String,Object> mapFiles=new HashMap<>();
        Map<String,Object> newMap=new HashMap<>();
        mapFiles.put("title",title);
        mapFiles.put("chm_type",1);
        Map<String,Object> typeMap=new HashMap<>();
        typeMap.put("??????",1);
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
        mapFiles.put("implement_Id",equipmentId);//???????????????
        mapFiles.put("implement_no",equipmentNo);//?????????
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
            rejson.put("msg","?????????????????????");
            logger.info("================================?????????????????????????????????====");
            return rejson;
        }
        String version = String.valueOf(customerFormMapper.getCurrentTableInfo(String.format("%s_%s", "design_biz", WorkOrderInformation.chm_task.getCode()), null).get("version"));
        Map<String,Object> variables=new HashMap<>();
        variables.put("userNo",ogUser.getpId());
        AjaxResult ajaxResult=customerFormService.processSubmit(WorkOrderInformation.chm_task.getCode(),bizId.toString(),version,variables);
        dynamicTableName(WorkOrderInformation.chm_task.getCode());
        DesignBizChm designBizChm =  designBizChmMapper.selectOne(Wrappers.<DesignBizChm>query().select(RedundancyFieldEnum.extra1.name).eq("id", bizId));
        rejson.put("imCode",designBizChm.getExtra1());
        OperationDetails details = OperationDetails.builder().operationType("????????????").bizNo(designBizChm.getExtra1()).createdName(ogUser.getPname()).createdBy(ogUser.getpId()).description("???????????????????????????").build();
        operationDetailsService.save(details);
        logger.info("================================?????????????????????????????????====");
        return rejson;
    }

    /**
     * ????????????????????????????????????
     * @param request
     * @return
     */
    @Override
    public JSONObject getUserListDetail(JSONObject request){
        logger.info("===============================?????????????????????????????????????????????"+request);
        dynamicTableName(WorkOrderInformation.chm_task.getCode());
        JSONObject reJson=JSONObject.parseObject("{\"code\": \"0\",\"imCode\": \"\",\"msg\": \"????????????\" }");
        String contact=request.getString("createBy");//??????
//        if(StringUtils.isEmpty(userNo)){
//            reJson.put("code","500");
//            reJson.put("msg","?????????????????????");
//            return reJson;
//        }
        String imCode=request.getString("imCode");//??????
        String status=request.getString("status");//??????
        String pageNum=request.getString("pageNum");//??????
        String pageSize=request.getString("pageSize");//????????????
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
            if("?????????".equals(status)){
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
        logger.info("===============================??????????????????????????????"+reJson.toJSONString());
        return  reJson;
    }

    /**
     * ??????????????????
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public JSONObject createHeshengOpenApi(String Id,OgUser ogUser,String businessKey) {
        logger.info("==================================??????????????????ID???"+Id);
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
            //???????????????
            chm.put("deviceId",designBizChm.getImplementId());
            //?????????
            chm.put("toolNo",designBizChm.getImplementNo());
            chm.put("serviceFormNo",designBizChm.getExtra1());
            //???????????? ????????????
            chm.put("appointment",designBizChm.getExpectTime());
            ChmBasedata chmBasedata=iChmBasedataService.selectChmBasedataById(Long.valueOf(designBizChm.getReportDepartment()));
            chm.put("branchName",chmBasedata.getOrgName());
            //????????????
            chm.put("urgent",designBizChm.getUrgent().toString());
            //????????????
            chm.put("incidence",designBizChm.getScopeInfluence());
            chm.remove("priority");
            String params=chm.toJSONString();
            logger.info("==================================???????????????????????????"+params);
            //??????????????????
            String reJson= HttpUtils.sendPostBody(openApiUrl+"/bosc/api/nonstandard/v1.0/technology/other/omsmitsm/hardwareReportFailureBilling",params);
            logger.info("==================================?????????????????????????????????"+reJson);
            //??????????????????
            if(StringUtils.isNotEmpty(reJson)){
                reRsult=JSON.parseObject(reJson);
                if("0000".equals(reRsult.getString("code"))){
                    String flowNo=reRsult.getJSONObject("data").getString("flowNo");
                    designBizChm.setFlowNo(flowNo);
                    designBizChm.setStatus("?????????");
                    designBizChmMapper.update(designBizChm,new UpdateWrapper<DesignBizChm>().eq("id", designBizChm.getId()));
                    CustomerFormContent customerFormContent=new CustomerFormContent();
                    customerFormContent.setCode(WorkOrderInformation.chm_task.getCode());
                    Map<String,Object> mapFiles=new HashMap<>();
                    mapFiles.put("status","?????????");
                    mapFiles.put("id",designBizChm.getId());
                    DesignBizJsonData currentNodeFormInfo = designBizJsonDataService.getOne(Wrappers.<DesignBizJsonData>query()
                            .eq(DesignBizJsonData.COL_BIZ_ID, designBizChm.getId())
                            .eq(DesignBizJsonData.COL_BIZ_TABLE, String.format("%s_%s", "design_biz", WorkOrderInformation.chm_task.getCode())));
                    String dataJson = VueDataJsonUtil.analysisDataJson(currentNodeFormInfo.getJsonData(), mapFiles);
                    customerFormContent.setDataJson(dataJson);
                    customerFormContent.setFields(mapFiles);
                    Integer bizId= customerFormService.dataOperation(WorkOrderInformation.chm_task.getCode(),customerFormContent);
                    OperationDetails details = OperationDetails.builder().operationType("????????????").bizNo(designBizChm.getExtra1()).description("????????????????????????").createdName(ogUser.getPname()).createdBy(ogUser.getpId()).build();
                    operationDetailsService.save(details);
                }else {
                    OperationDetails details = OperationDetails.builder().operationType("????????????").bizNo(designBizChm.getExtra1()).description("??????????????????????????????????????????").createdName(ogUser.getPname()).createdBy(ogUser.getpId()).highWarn("1").build();
                    operationDetailsService.save(details);

                }
            }else {
                OperationDetails details = OperationDetails.builder().operationType("????????????").bizNo(designBizChm.getExtra1()).description("????????????????????????????????????").createdName(ogUser.getPname()).createdBy(ogUser.getpId()).highWarn("1").build();
                operationDetailsService.save(details);
            }
        }catch (Exception e){
            OperationDetails details = OperationDetails.builder().operationType("????????????").bizNo(designBizChm.getExtra1()).description("????????????????????????????????????").createdName(ogUser.getPname()).createdBy(ogUser.getpId()).highWarn("1").build();
            operationDetailsService.save(details);
            logger.error("????????????????????????"+e.getMessage());
        }
        return reRsult;
    }

    /**
     * ???????????????????????????
     * @return
     */
    public void supplierDTDEngineer() throws Exception {
        String body="{token:"+token+"}";
        //??????????????????
        String reJson= HttpUtils.sendPostBody(openApiUrl+"/itsmApp/api/flow/supplierDTDEngineer",RSAUtil.decrypt(body,"UTF-8"));
        //??????
        String result=RSAUtil.decrypt(reJson,"UTF-8");
        JSONObject resultJson=JSONObject.parseObject(result);
        //??????????????????
        if("0000".equals(resultJson.getString("code"))){
            JSONArray jsonArray=resultJson.getJSONArray("data");
            if(!jsonArray.isEmpty()){
                for(Object jt:jsonArray){
                    JSONObject js=(JSONObject)jt;
                    String supplier=js.getString("supplier");//??????
                    //??????
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
     * ??????????????????
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
        //??????????????????
        try{
            reJson= HttpUtils.sendPostBody(openApiUrl+"/bosc/api/nonstandard/v1.0/technology/other/omsmitsm/handleProgressQuery",jsonObject.toJSONString());

        }catch (Exception e){
            logger.error("============???????????????????????????"+e.getMessage());
        }
        if(StringUtils.isNotEmpty(reJson)) {
            logger.info("===================?????????????????????"+reJson);
            JSONObject result = JSONObject.parseObject(reJson);
            Map<String, Object> mapFiles = new HashMap<>();
            if (result != null && "0000".equals(result.getString("code"))) {
                JSONArray jsonArray = result.getJSONArray("data");
                jsonArray.forEach(ja -> {
                    JSONObject j = JSONObject.parseObject(ja.toString());
                    String operationContent = j.getString("operationContent");
                    //??????????????????
                    if ("????????????".equals(operationContent)) {
                        String dealTime = j.getString("dealTime");
                        mapFiles.put("order_time", dealTime);
                        designBizChm.setOrderTime(dealTime);
                    }
                    if ("????????????".equals(operationContent)) {
                        String dealTime = j.getString("dealTime");
                        mapFiles.put("facturer_accept_time", dealTime);
                        designBizChm.setFacturerAcceptTime(dealTime);
                    }
                    if ("???????????????".equals(operationContent)) {
                        String dealTime = j.getString("dealTime");
                        mapFiles.put("engineer_accept_time", dealTime);
                        designBizChm.setRealDealUser(j.getString("dealUser"));
                        designBizChm.setEngineerAcceptTime(dealTime);
                    }
                    if ("?????????????????????".equals(operationContent)) {
                        String dealTime = j.getString("dealTime");
                        mapFiles.put("engineer_arrive_time", dealTime);
                        designBizChm.setEngineerArriveTime(dealTime);
                    }
                    if ("?????????????????????".equals(operationContent)) {
                        String dealTime = j.getString("dealTime");
                        mapFiles.put("complete_time", dealTime);
                        designBizChm.setCompleteTime(dealTime);
                    }
                    if("????????????".equals(j.getString("operation"))){
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
      //    jsonObject=JSONObject.parseObject("{\"code\":\"0000\",\"data\":[{\"dealUser\":\"??????\",\"dealTime\":\" 2021-08-17 14:09:37\",\"operation\":\"????????????\"},{\"dealUser\":\"??????\",\"dealTime\":\" 2021-08-19 14:09:37\",\"operation\":\"????????????\"}],\"serviceTime\":1660893933300}");
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
            colMap1.put("label", "?????????");
            colMap1.put("val", "dealUser");
            colMap2.put("label", "????????????");
            colMap2.put("val", "dealTime");
            colMap3.put("label", "??????");
            colMap3.put("val", "operation");
            colMap4.put("label", "??????");
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
     * ???????????????????????????
     * @return
     */
    @Override
    public JSONObject whitelistVerification(JSONObject request) {
        logger.info("=============================????????????????????????????????????"+request);
        JSONObject reJson=JSONObject.parseObject("{\"code\": \"0\",\"imCode\": \"\",\"msg\": \"????????????\" }");
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
            reJson.put("msg","??????????????????!?????????"+imCode);
            logger.info("=============================????????????????????????????????????????????????!?????????"+imCode);
            return reJson;
        }else {
            reJson.put("imCode",imCode);
            MybatisPlusConfig.customerTableName.set("event_white_list");
            QueryWrapper<EventWhiteList> queryWrapper = new QueryWrapper<EventWhiteList>().eq("certificates_number",idCard);
            EventWhiteList eventWhiteList = baseMapper.selectOne(queryWrapper);
            if(eventWhiteList==null){
                reJson.put("code",500);
                reJson.put("msg","?????????????????????!?????????"+request);
            }else {
                if(!userName.equals(eventWhiteList.getName())){
                    reJson.put("code",500);
                    reJson.put("msg","????????????????????????!?????????"+userName+"->"+eventWhiteList.getName());
                }
                if(!mobile.equals(eventWhiteList.getPhoneNumber())){
                    reJson.put("code",500);
                    reJson.put("msg","???????????????????????????!?????????"+mobile+"->"+eventWhiteList.getPhoneNumber());
                }
                if(!supplier.equals(eventWhiteList.getVendor())){
                    reJson.put("code",500);
                    reJson.put("msg","??????????????????????????????!?????????"+supplier+"->"+eventWhiteList.getVendor());
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
        logger.info("=============================??????????????????????????????????????????"+reJson.toJSONString());
        return reJson;
    }
    private void sendSmsMessgae( String extra1, String faultDescription, String userName, String idCard, String mobile,String phone,String estimateGoTime) {
       String msg="?????????:" +extra1+","+
               "????????????:" +faultDescription+
               "??????????????????????????????"+userName+"?????????" +
               "?????????????????????:"+idCard +//.replaceAll("(\\d{6})\\d+(\\w{4})", "$1*****$2")+"???"
               "?????????????????????:"+phone+"???" +
               "??????????????????:" +estimateGoTime+"???";
        foreignService.sendSmsMessageBySocket("????????????",extra1,mobile,msg);
    }

    /**
     * ????????????????????????
     * @return
     */
    @Override
    public JSONObject receiveResult(JSONObject request){
        logger.info("======================================?????????????????????????????????"+request);
        JSONObject reJson=JSONObject.parseObject("{\"code\": \"0\",\"imCode\": \"\",\"msg\": \"????????????\" }");
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
            reJson.put("msg","??????????????????????????????"+imCode);
            logger.info("======================================???????????????????????????????????????????????????????????????"+imCode);
            return reJson;
        }else {
            String receiveFlag=request.getString("receiveFlag");
            String valuationDesc=request.getString("valuationDesc");//????????????
            String instanceId=designBizChm.getInstanceId();
            Task task = processEngine.getTaskService().createTaskQuery().processInstanceId(instanceId).singleResult();
            if(!"??????".equals(task.getName() )){
                return reJson;
            }
            variables.put("reCode","Y".equals(receiveFlag)?"0":"1");
            variables.put("dealGroup", CustomerFlowConstants.CHM_IT_GROUP);
            variables.put("loginUser",ogUser.getpId());
            taskService.complete(task.getId(),variables);
            designBizChm.setDealNotes(valuationDesc);
            String faultType=request.getString("chmType");//????????????
            String solution=request.getString("solution");//????????????
            if(StringUtils.isNotEmpty(solution)){
                if("1".equals(solution)){
                    //??????????????????
                    designBizChm.setFacturerAcceptTime(request.getString("supplierAcceptTime"));
                    //?????????????????????
                    designBizChm.setEngineerAcceptTime(request.getString("engineerAcceptTime"));
                    //?????????????????????
                    designBizChm.setEngineerArriveTime(request.getString("engineerArriveTime"));
                }
            }
            String supplier=request.getString("supplier");
            String remarks=request.getString("remarks");//??????
            String dealNotes=request.getString("dealNotes");//??????????????????
            String equipmentType=request.getString("equipmentType");//????????????
            String equipmentName=request.getString("equipmentName");//????????????
            String equipmentModel=request.getString("equipmentModel");//????????????

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
                    logger.error("================================????????????????????????================================");
                }

            }catch (Exception e){
                OperationDetails details = OperationDetails.builder().operationType("????????????").bizNo(designBizChm.getExtra1()).description("?????????????????????????????????????????????").build();
                operationDetailsService.save(details);
                logger.error("================================??????????????????????????????================================");
                e.printStackTrace();
            }
            designBizChm.setServiceEvaluation(ChmPJEnum.getCode(valuationDesc));
            designBizChm.setFaultType(faultType);
            designBizChm.setSolution(solution);
            designBizChm.setRemarks(remarks);
            designBizChm.setDealNotes(dealNotes);
            designBizChm.setFacturer(supplier);
            designBizChm.setStatus("?????????");
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
                    //??????????????????
                    mapFiles.put("facturer_accept_time",request.getString("supplierAcceptTime"));
                    //?????????????????????
                    mapFiles.put("engineer_accept_time",request.getString("engineerAcceptTime"));
                    //?????????????????????
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
            mapFiles.put("status","?????????");
            customerFormContent.setFields(mapFiles);
            Integer bizId= customerFormService.dataOperation(WorkOrderInformation.chm_task.getCode(),customerFormContent);
            OperationDetails details = OperationDetails.builder().operationType("????????????").bizNo(designBizChm.getExtra1()).description("???????????????????????????"+valuationDesc).build();
            operationDetailsService.save(details);
        }
        getprocessingProgress(imCode);
        reJson.put("imCode",imCode);
        logger.info("======================================?????????????????????????????????"+reJson.toJSONString());
        return reJson;

    }
    public Page buildPageObject(){
        Integer pageNum = 0;
        Integer pageSize = 10;
        Page page=new Page<>(pageNum,pageSize);
        return page;
    }
    /**
     * ??????????????????
     *
     * @param code ??????
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
