package com.ruoyi.form.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtil;
import com.ruoyi.form.entity.AutomationPlatFormsParams;
import com.ruoyi.form.entity.DbUserAndTableMessageVO;
import com.ruoyi.form.entity.automation.*;
import com.ruoyi.form.service.AutomationPlatformsService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class AutomationPlatformsServiceImpl implements AutomationPlatformsService {



    @Value("${automate.automateUrl}")
    private  String prefix ;

    //private static final String prefix = "http://10.240.140.238:8888";
    //获取tokenurl
    private   String GET_TOKEN_URL = prefix + "/aoms/dmoperation/V1/token.do";
    //模板名称列表URL
    private   String GET_TEMPLATE_NAME_LIST_URL = prefix + "/aoms/dmoperation/stardard/standardOperSysName.do";
    //获取任务申请列表URL
    private   String GET_TASK_APPLY_LIST_URL = prefix + "/aoms/dmoperation/stardard/getStandardTaskInfo.do";
    //提交任务URL
    private   String COOMMIT_TASK_URL = prefix + "/aoms/dmoperation/stardard/applicationStandardTask.do";
    //获取设备信息
    private   String GET_EQUIPMENT_INFO_URL = prefix + "/aoms/dmoperation/stardard/getEquiInfoList.do";
    //获取双人复核列表信息（获取工单id） iid
    private   String GET_DOUBLE_AUDITOR_URL = prefix + "/aoms/dmoperation/stardard/getDoubleCheckWorkItemList.do";
    //标准运维审核通过操作
    private   String AUDIT_PASS_URL = prefix + "/aoms/dmoperation/stardard/auditStandardTask.do";
    //执行任务
    private   String EXECUTE_TASK_URL = prefix + "/aoms/dmoperation/stardard/submitStrategyTaskForIC.do";
    //查看任务状态
    private   String CHECK_STATUS = prefix + "/aoms/dmoperation/stardard/queryStandardTaskHistoryList.do";

    @Override
    public List<CheckStatus> commitTask(AutomationPlatFormsParams automationPlatFormsParams) {
        String taskName = automationPlatFormsParams.getTaskName();
        //第一步 登陆获取token
        //String tokenA = getToken("ideal", "ideal");
        String tokenA = getToken("huchenglin", "huchenglin");
        String tokenB = getToken("sa", "admin");
        //获取标准运维模板名称列表
        List<TaskEntity> templateNameList = getTemplateNameList(tokenA);
        if (CollectionUtils.isEmpty(templateNameList)) {
            throw new BusinessException("不存在该任务模板");
        }
        List<TaskEntity> collect = templateNameList.stream().filter(taskEntity -> taskName.equals(taskEntity.getSysName())).collect(Collectors.toList());
        if (collect.size() < 1) {
            throw new BusinessException("不存在该任务模板");
        }
        //获取任务申请列表信息
        List<TaskApply> taskApplyList = getTaskApplyList(tokenA, taskName);
        if (CollectionUtils.isEmpty(taskApplyList)) {
            throw new BusinessException("该任务的申请信息不存在");
        }
        //提交任务 String tokenUserNameJson, Long systemId, String sysName, String instName,String equiIds
        TaskEntity taskEntity = collect.get(0);
        String submitResult = submitTask(tokenA, taskEntity.getIsystemId(), taskEntity.getSysName(), taskName, automationPlatFormsParams.getEquipmentIds(),automationPlatFormsParams.getWorkOrderId(),automationPlatFormsParams.getJsonParamArrList());
        if (!submitResult.contains("成功")) {
            throw new BusinessException(submitResult);
        }

        /**
         * 审核任务
         */
        //获取workID
        String workItemId = getWorkItemId(tokenB, automationPlatFormsParams.getWorkOrderId());
        if (StringUtils.isEmpty(workItemId)) {
            throw new BusinessException("未能获取到工单ID");
        }
        //审核任务
        String auditResult = auditPass(tokenB, workItemId);
        if (!auditResult.contains("成功")) {
            throw new BusinessException(auditResult);
        }
        /**
         * 执行任务
         */
        //执行任务 超时0
        String executeTaskResult = executeTask(tokenA, workItemId);
        if (!executeTaskResult.contains("成功")) {
            throw new BusinessException(executeTaskResult);
        }
        //查看执行状态
        List<CheckStatus> checkStatus = checkStatus(tokenA, automationPlatFormsParams.getWorkOrderId());
        return checkStatus;
    }

    /**
     * 获取token
     *
     * @return
     */
    @Override
    public String getToken(String userName, String userPwd) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json;charset=UTF-8");
        //Accept: application/json
        map.put("Accept", "application/json");
        //用户名密码json格式
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("userName", userName);
        userMap.put("userPwd", userPwd);
        String signIn = JSON.toJSONString(userMap);
        //获取到token
        String getTokenResult = HttpUtil.send(GET_TOKEN_URL, signIn, map, HttpMethod.POST);
        TokenResult tokenResult = JSON.parseObject(getTokenResult, TokenResult.class);
        HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", tokenResult.getToken());
        tokenMap.put("userName", userName);
        return JSON.toJSONString(tokenMap);
    }

    /**
     * 获取任务列表
     *
     * @param tokenUserNameJson
     * @return
     */
    private List<TaskEntity> getTemplateNameList(String tokenUserNameJson) {
        HashMap<String, String> map = new HashMap<>();
        map.put("paramsHeader", tokenUserNameJson);
        /**
         * {
         *   "isOk": true,
         *   "resultList": [{
         *     "isystemId": 10010,
         *     "sysName": "标准运维模板"
         *   }]
         * }
         */
        String templateList = HttpUtil.send(GET_TEMPLATE_NAME_LIST_URL, null, map, HttpMethod.GET);
        Map templateMap = JSON.parseObject(templateList, Map.class);
        String resultList = templateMap.get("resultList").toString();
        return JSON.parseArray(resultList, TaskEntity.class);
    }

    /**
     * 获取任务申请列表信息
     *
     * @param tokenUserNameJson
     * @param taskName
     * @return
     */
    private List<TaskApply> getTaskApplyList(String tokenUserNameJson, String taskName) {
        HashMap<String, String> map = new HashMap<>();
        map.put("paramsHeader", tokenUserNameJson);
        HashMap<String, String> params = new HashMap<>();
        params.put("start", "0");
        params.put("limit", "5000");
        params.put("iProjectName", taskName);
        params.put("createUser", "");
        params.put("isystemTypeUuid", "");
        String json = JSON.toJSONString(params);
        String taskApplyListJson = HttpUtil.send(GET_TASK_APPLY_LIST_URL, json, map, HttpMethod.GET);
        Map taskApplyMap = JSON.parseObject(taskApplyListJson, Map.class);
        String resultList = taskApplyMap.get("resultList").toString();
        return JSON.parseArray(resultList, TaskApply.class);
    }

    /**
     * 提交任务申请
     *
     * @param tokenUserNameJson token
     * @param systemId          模板id
     * @param sysName           模板名称
     * @param instName          任务名称
     * @param equiIds           设备id（多个用,隔开）
     * @return
     */
    private String submitTask(String tokenUserNameJson, Long systemId, String sysName, String instName, String equiIds,String workItemId,List<JsonParamArr> jsonParamArrList) {
        HashMap<String, String> map = new HashMap<>();
        map.put("paramsHeader", tokenUserNameJson);
        /**
         * {
         * 	"systemId":10454,
         * 	"sysName":"testut",
         * 	"instName":"testut201901171704",
         * 	"startUser":"ideal",
         * 	"auditUser":"sa",
         * 	"perFormUser":"sa",
         * 	"execStrategy":"2",
         * "execTime":"2019-01-19 08:00:00",
         * 	"cornValue":"00 00 08 19 01 ? 2019",
         * 	"oddNumbersType":"1",
         * 	"butterflyVerion":"2342342342334",
         * 	"uploadFlag":true,
         * 	"rootPath":"/testut/20190117170709628/e1.config",
         * 	"equiIds":"10037",
         * 	"jsonParamArr":[
         *                {
         * 			"iparamName":"ftp_localfile",
         * 			"iparamType":"String",
         * 			"iparamValue":"d:\\",
         * 			"iparamDes":""
         *        }
         * 	]
         * }
         */
        HashMap<String, Object> params = new HashMap<>();
        params.put("systemId", systemId);
        params.put("sysName", sysName);
        params.put("instName", instName);//taskName
        params.put("startUser", "huchenglin");//=======================================>写死 A
        params.put("auditUser", "sa");//=======================================>写死 2.8判断人是否在内 B 密码admin
        params.put("perFormUser", "huchenglin");//=======================================>写死 A
        params.put("execStrategy", "1");
        params.put("execTime", "");
        //params.put("cornValue", "");
        params.put("oddNumbersType", "1");
        params.put("butterflyVerion",workItemId);//===================> 工单id
        params.put("uploadFlag", false);
        params.put("rootPath", "");
        //获取设备id
        params.put("equiIds", equiIds);
//        ArrayList<JsonParamArr> jsonParamArrs = new ArrayList<>();
//        JsonParamArr jsonParamArr = new JsonParamArr();
//        jsonParamArr.setIparamName("canshu1");//========================>表单
//        jsonParamArr.setIparamType("String");//========================>表单
//        jsonParamArr.setIparamValue("test001");//========================>表单
//        jsonParamArr.setIparamDes("这是一条测试数据");//========================>表单
//        jsonParamArrs.add(jsonParamArr);
        params.put("jsonParamArr", jsonParamArrList);
        String json = JSON.toJSONString(params);
        String equipmentInfoJson = HttpUtil.send(COOMMIT_TASK_URL, json, map, HttpMethod.POST);
        /**
         * {
         *     "isOk": true,
         *     "message": "用户 [Server Admin] 发起 [testut] 任务。成功"
         * }
         */
        Map equipmentInfoMap = JSON.parseObject(equipmentInfoJson, Map.class);
        return equipmentInfoMap.get("message").toString();
    }

    /**
     * 查询设备列表
     *
     * @param
     * @return
     */
    @Override
    public List<EquipmentInfo> getEquipmentInfoUrl() {
        String tokenA = getToken("ideal", "ideal");
        HashMap<String, String> map = new HashMap<>();
        map.put("paramsHeader", tokenA);
        HashMap<String, Object> params = new HashMap<>();
        //  "start": "0",
        //    "limit": "30",
        params.put("start", "0");
        params.put("limit", "3000");
        String json = JSON.toJSONString(params);
        String equipmentInfoJson = HttpUtil.send(GET_EQUIPMENT_INFO_URL, json, map, HttpMethod.GET);
        Map equipmentInfoMap = JSON.parseObject(equipmentInfoJson, Map.class);
        String resultList = equipmentInfoMap.get("resultList").toString();
        return JSON.parseArray(resultList, EquipmentInfo.class);
    }

    /**
     * 获取工单id
     *
     * @param butterflyVersion 相当于流程的唯一id
     * @return
     */
    public String getWorkItemId(String tokenUserNameJson, String butterflyVersion) {
        HashMap<String, String> map = new HashMap<>();
        map.put("paramsHeader", tokenUserNameJson);
        HashMap<String, String> params = new HashMap<>();
        params.put("start", "0");
        params.put("limit", "5000");
        String json = JSON.toJSONString(params);
        String taskApplyListJson = HttpUtil.send(GET_DOUBLE_AUDITOR_URL, json, map, HttpMethod.GET);
        Map taskApplyMap = JSON.parseObject(taskApplyListJson, Map.class);
        String resultList = taskApplyMap.get("resultList").toString();
        List<DoubleAuditor> doubleAuditors = JSON.parseArray(resultList, DoubleAuditor.class);
        if (!CollectionUtils.isEmpty(doubleAuditors)) {
            List<DoubleAuditor> collect = doubleAuditors.stream().filter(doubleAuditor -> doubleAuditor.getButterflyversion().equals(butterflyVersion)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(collect)) {
                return collect.get(0).getIid();
            }
        }
        return null;
    }

    /**
     * 提交审核
     *
     * @param tokenUserNameJson
     * @param workItemId        工单id
     * @return
     */
    public String auditPass(String tokenUserNameJson, String workItemId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("paramsHeader", tokenUserNameJson);
        HashMap<String, String> params = new HashMap<>();
        params.put("iworkItemid", workItemId);
        String json = JSON.toJSONString(params);
        String taskApplyListJson = HttpUtil.send(AUDIT_PASS_URL, json, map, HttpMethod.POST);
        Map taskApplyMap = JSON.parseObject(taskApplyListJson, Map.class);
        return  taskApplyMap.get("message").toString();
    }

    /**
     * 执行任务
     *
     * @param tokenUserNameJson
     * @param workItemId
     * @return
     */
    public String executeTask(String tokenUserNameJson, String workItemId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("paramsHeader", tokenUserNameJson);
        HashMap<String, String> params = new HashMap<>();
        params.put("iworkItemid", workItemId);
        String json = JSON.toJSONString(params);
        String taskApplyListJson = HttpUtil.send(EXECUTE_TASK_URL, json, map, HttpMethod.POST);
        Map taskApplyMap = JSON.parseObject(taskApplyListJson, Map.class);
        return  taskApplyMap.get("message").toString();
    }

    /**
     * 查看任务执行状态
     *
     * @param tokenUserNameJson
     * @param butterflyVersion
     * @return
     */
    @Override
    public List<CheckStatus> checkStatus(String tokenUserNameJson, String butterflyVersion) {
        HashMap<String, String> map = new HashMap<>();
        map.put("paramsHeader", tokenUserNameJson);
        HashMap<String, String> params = new HashMap<>();
        params.put("start", "0");
        params.put("limit", "5000");
        String json = JSON.toJSONString(params);
        String checkStatusResultJson = HttpUtil.send(CHECK_STATUS, json, map, HttpMethod.POST);
        Map checkStatusMap = JSON.parseObject(checkStatusResultJson, Map.class);
        String resultList =checkStatusMap.get("resultList").toString();
        List<CheckStatus> doubleAuditors = JSON.parseArray(resultList, CheckStatus.class);
        if (!CollectionUtils.isEmpty(doubleAuditors)) {
            List<CheckStatus> collect = doubleAuditors.stream().filter(checkStatus -> checkStatus.getButterflyVersion().contains(butterflyVersion)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(collect)) {
                return collect;
            }else {
                return null;
            }
        }
        throw new BusinessException(checkStatusMap.get("message").toString());
    }
}
