package com.ruoyi.system.service.server;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.common.core.domain.entity.PubParaValue;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.http.entegorserver.entity.InstanceStartup;
import com.ruoyi.system.http.entegorserver.entity.UploadMsg;
import com.ruoyi.system.service.IEntegorServer;
import com.ruoyi.system.service.IPubParaValueService;
import com.ruoyi.system.service.ISysApplicationManagerService;
import com.ruoyi.system.service.ISysRoleService;

/**
 * 自动化对接接口
 *
 * @author 14735
 */
@Service
public class EntegorServerImpl implements IEntegorServer {

    private static final Logger log = LoggerFactory.getLogger(EntegorServerImpl.class);
    private final String STATUS = "status";
    private final String MESSAGE = "message";

    private final String SUCCESS_CODE = "200";
    private final String ERROR_CODE = "500";

    private final String ENTEGOR_URL = "http://20.0.129.200:8888";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ISysApplicationManagerService iSysApplicationManagerService;
    @Autowired
    private IPubParaValueService iPubParaValueService;
    @Autowired
    private ISysRoleService iSysRoleService;

    @Value("${foreign.entegorPyService.url}")
    private String entegorPyUrl;

    @Override
    public Map<String, Object> sendMessageUpload(UploadMsg msg) {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> uriVariables = new HashMap();
        String suffix_path = "/aoms/SUSOpenAPI?action=upload&modultype=3&access_token={token}&username=ideal&replaceno={replaceno}" +
                "&excelname={excelname}&realexcelname={realexcelname}&groupName={groupName}&ftpType=FastDFS";
        Object mobilePhone = msg.getParams().get("mobilePhone");
        String token = getToken(StringUtils.isNull(mobilePhone) ? "ideal" : mobilePhone.toString());
        if (StringUtils.isEmpty(token)) {
            returnMap.put(STATUS, ERROR_CODE);
            returnMap.put(MESSAGE, "调用自动化启动接口失败,失败原因:发起人手机号[" + mobilePhone + "]在自动化平台不存在！");
            return returnMap;
        }
        uriVariables.put("token", token);
        uriVariables.put("realexcelname", msg.getParams().get("realexcelname"));
        uriVariables.put("excelname", msg.getParams().get("excelname"));
        uriVariables.put("replaceno", msg.getParams().get("replaceno"));
        uriVariables.put("groupName", msg.getParams().get("groupName"));
        String json = JSON.toJSONString(msg);
        log.debug("-----------上传自动化步奏校验接口请求体json参数为:" + json);
        String message = "";
        String status = "";
        String changedesc = "";
        String sysname = "";
        ResponseEntity<String> result = null;
        try {
            json = URLEncoder.encode(json, "GBK");
            result = this.getResponseEntity(suffix_path, json, uriVariables);
        } catch (Exception e) {
            e.printStackTrace();
            returnMap.put(STATUS, ERROR_CODE);
            returnMap.put(MESSAGE, "上传自动化步奏校验接口调用失败！");
            log.error("-----------自动化校验接口调用失败", e);
            return returnMap;
        }
        if (StringUtils.isNotEmpty(result.getBody())) {
            Map jsonObject = JSONObject.parseObject(result.getBody());
            message = (String) jsonObject.get("mes");
            status = (String) jsonObject.get("state");
            if ("0".equals(status)) {
                status = SUCCESS_CODE;
                changedesc = (String) jsonObject.get("changedesc");
                sysname = (String) jsonObject.get("sysname");
                log.debug("-----------上传自动化步奏校验接口成功,变更原因说明:changedesc=" + changedesc + "||系统名称:sysname=" + sysname);
            } else {
                status = ERROR_CODE;
                log.error("-----------上传自动化步奏校验接口失败,失败原因为:" + message);
            }
        } else {
            status = ERROR_CODE;
        }
        returnMap.put(MESSAGE, message);
        returnMap.put(STATUS, status);
        returnMap.put("changedesc", changedesc);
        returnMap.put("sysname", sysname);
        return returnMap;
    }

    @Override
    public Map<String, Object> sendMessageInstanceStartup(List<InstanceStartup> instances, String mobilPhone) {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> uriVariables = new HashMap<>();
        String suffix_path = "/aoms/SUSOpenAPI?action=ExcelStart&access_token={token}";
        String token = this.getToken(StringUtils.isEmpty(mobilPhone) ? "ideal" : mobilPhone);
        if (StringUtils.isEmpty(token)) {
            returnMap.put(STATUS, ERROR_CODE);
            returnMap.put(MESSAGE, "调用自动化启动接口失败,失败原因:发起人手机号[" + mobilPhone + "]在自动化平台不存在！");
            return returnMap;
        }
        uriVariables.put("token", token);
        String json = JSON.toJSONString(instances);
        log.debug("-----------自动化启动接口发送json参数为:" + json);
        ResponseEntity<String> result = null;
        String message = "";
        String status = "";
        try {
            json = URLEncoder.encode(json, "GBK");
            result = this.getResponseEntity(suffix_path, json, uriVariables);
        } catch (Exception e) {
            e.printStackTrace();
            returnMap.put(STATUS, ERROR_CODE);
            returnMap.put(MESSAGE, "自动化启动接口调用失败！");
            log.error("-----------自动化启动接口调用失败", e);
            return returnMap;
        }
        if (StringUtils.isNotEmpty(result.getBody())) {
            List list = (JSONArray) JSON.parse(result.getBody());
            Map map = (Map) list.get(0);
            message = (String) map.get("message");
            Boolean success = (Boolean) map.get("success");
            if (success && message.length() < 10) {
                status = SUCCESS_CODE;
                log.debug("-----------自动化启动接口成功.");
            } else {
                status = ERROR_CODE;
                log.error("-----------自动化启动接口失败,失败原因为:" + message);
            }
        } else {
            status = ERROR_CODE;
        }
        returnMap.put(MESSAGE, message);
        returnMap.put(STATUS, status);
        return returnMap;
    }

    @Override
    public Map<String, Object> selectResultMsg(Map uriVariables) {
        Map<String, Object> returnMap = new HashMap<>();
        String suffix_path = "aoms/SUSOpenAPI?action=queryTimeAndState&modultype=3&userName=ideal&sysName={sysName}" +
                "&version={version}&insName={insName}&access_token={token}";
        Object mobilePhone = uriVariables.get("mobilePhone");
        String token = this.getToken(StringUtils.isNull(mobilePhone) ? "ideal" : mobilePhone.toString());
        if (StringUtils.isEmpty(token)) {
            returnMap.put(STATUS, ERROR_CODE);
            returnMap.put(MESSAGE, "调用自动化启动接口失败,失败原因:发起人手机号[" + mobilePhone + "]在自动化平台不存在！");
            return returnMap;
        }
        uriVariables.put("token", token);
        String json = "";
        log.debug("-----------自动化查询变更历史接口参数为:" + JSON.toJSONString(uriVariables));
        ResponseEntity<String> result = null;
        String message = "";
        String status = "";
        try {
            json = URLEncoder.encode(json, "GBK");
            result = this.getResponseEntity(suffix_path, json, uriVariables);
        } catch (Exception e) {
            e.printStackTrace();
            returnMap.put(STATUS, ERROR_CODE);
            returnMap.put(MESSAGE, "自动化查询变更历史接口调用失败！");
            return returnMap;
        }
        if (StringUtils.isNotEmpty(result.getBody())) {
            Map jsonObject = JSONObject.parseObject(result.getBody());
            status = (String) jsonObject.get("state");
            message = (String) jsonObject.get("mes");
            if ("0".equals(status)) {
                status = SUCCESS_CODE;
                String resultListStr = (String) jsonObject.get("result");
                if (StringUtils.isNotEmpty(resultListStr)) {
                    List<Map> resultList = (List<Map>) JSON.parse(resultListStr);
                    returnMap.put("result", resultList);
                    log.debug("-----------自动化查询变更历史接口调用成功,返回参数为:" + resultListStr);
                }
            } else {
                status = ERROR_CODE;
                log.error("-----------自动化查询变更历史接口调用失败,失败原因为:" + message);
            }
        } else {
            status = ERROR_CODE;
        }
        returnMap.put(MESSAGE, message);
        returnMap.put(STATUS, status);
        return returnMap;
    }

    /**
     * 通知自动化禁用版本任务
     */
    @Override
    public void forbiddenVersion(Map uriVariables) {
        String suffix_path = "/aoms/SUSOpenAPI?action=forbiddenVersion&modultype=3&user=ideal&version={versionInfoNo}&access_token={token}";
        Object mobilePhone = uriVariables.get("mobilePhone");
        String token = this.getToken(StringUtils.isNull(mobilePhone) ? "ideal" : mobilePhone.toString());
        if (StringUtils.isEmpty(token)) {
            log.debug("版本单号:" + uriVariables.get("versionInfoNo") + "的版本单调用自动化接口失败，失败原因为:发起人手机号在自动化平台不存在!");
            return;
        }
        uriVariables.put("token", token);
        ResponseEntity<String> entity = null;
        try {
            entity = restTemplate.getForEntity(getEntegorUrl() + suffix_path, String.class, uriVariables);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (entity != null && entity.getStatusCodeValue() == Integer.valueOf(SUCCESS_CODE)) {
            log.debug("-----------版本单号:" + uriVariables.get("versionInfoNo") + "的版本单调用自动化接口结果信息为:" + entity.getBody());
        } else {
            log.debug("-----------版本单号:" + uriVariables.get("versionInfoNo") + "的版本单调用自动化接口失败，失败原因为:" + entity.getBody());
        }

    }

    /**
     * 获取ResponseEntity返回信息
     *
     * @param suffix_path
     * @param json
     * @param uriVariables
     * @return
     */
    public ResponseEntity<String> getResponseEntity(String suffix_path, String json, Map uriVariables) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.APPLICATION_JSON_UTF8;
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> request = new HttpEntity<String>(json, headers);
        return restTemplate.postForEntity(getEntegorUrl() + suffix_path, request, String.class, uriVariables);
    }

    /**
     * 获取token使用手机号作为用户，对于定时任务访问接口的情况使用默认用户名ideal
     *
     * @param mobilePhone
     * @return
     */
    public String getToken(String mobilePhone) {
        String token = "";
        String suffix_path = "/aoms/SUSOpenAPI?action=login&modultype=3&type=testcenter&user={mobilePhone}";
        Map<String, Object> params = new HashMap<>();
        if (!"ideal".equals(mobilePhone)) {
            suffix_path += "&loginType=tel";
        }
        params.put("mobilePhone", mobilePhone);
        ResponseEntity<String> entity = null;
        try {
            entity = restTemplate.getForEntity(getEntegorUrl() + suffix_path, String.class, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (entity != null && entity.getStatusCodeValue() == Integer.valueOf(SUCCESS_CODE)) {
            String body = entity.getBody();
            Map tokenMap = JSONObject.parseObject(body);
            token = (String) tokenMap.get("access_token");
        }
        return token;
    }

    /**
     * 获取自动化对接接口的路径
     */
    public String getEntegorUrl() {
        return StringUtils.isEmpty(RuoYiConfig.getEntegorUrl()) ? ENTEGOR_URL : RuoYiConfig.getEntegorUrl();
    }

    //以下为业务事件单对接脚本服务化20210329刘良新增

    /**
     * 获取脚本服务化token
     **/
    public String getScriptToken() {
        String token = "";
        String suffix_path = "/aoms/getScriptToken.do?loginType=tel&userKeyName={mobilePhone}&userKeyWord=ideal123@";
        Map<String, Object> params = new HashMap<>();
        params.put("mobilePhone", ShiroUtils.getOgUser().getMobilPhone());
        ResponseEntity<String> entity = null;
        try {
            entity = restTemplate.getForEntity(getEntegorUrl() + suffix_path, String.class, params);
        } catch (Exception e) {
            log.info("获取脚本服务化token失败！");
        }
        if (entity != null && entity.getStatusCodeValue() == Integer.valueOf(SUCCESS_CODE)) {
            String body = entity.getBody();
            Map tokenMap = JSONObject.parseObject(body);
            token = (String) tokenMap.get("token");
        }
        return token;
    }

    /**
     * 获取脚本服务化token
     **/
    public String getScriptTokenToFm() {
        String token = "";
        String suffix_path = "/aoms/getScriptToken.do?loginType=tel&userKeyName=18501260701&userKeyWord=ideal123@";
        Map<String, Object> params = new HashMap<>();
        ResponseEntity<String> entity = null;
        try {
            entity = restTemplate.getForEntity(getEntegorUrl() + suffix_path, String.class, params);
        } catch (Exception e) {
            log.info("获取脚本服务化token失败！");
        }
        if (entity != null && entity.getStatusCodeValue() == Integer.valueOf(SUCCESS_CODE)) {
            String body = entity.getBody();
            Map tokenMap = JSONObject.parseObject(body);
            token = (String) tokenMap.get("token");
        }
        return token;
    }

    @Override
    public List<Map<String, String>> getCommonTaskList(String sysCode) {
        List<Map<String, String>> list2 = new ArrayList<>();
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        String suffix_path = "/aoms/scriptplatform.do?action=getCommonTaskList&user={code}&token={token}";
        String code = iSysApplicationManagerService.selectOgSysBySysName(sysCode).getCode();
        String token = getScriptTokenToFm();
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException("获取脚本列表失败，连接脚本服务化平台获取token失败。");
        }
        ResponseEntity<String> result = null;
        returnMap.put("code", code);//放入token到消息头
        returnMap.put("token", token);//放入token到消息头
        map.put("taskName", "");
        map.put("serviceName", "");
        map.put("bsTypeName", sysCode);//放入系统编码
        map.put("start", 0);//放入分页起始值
        map.put("limit", 300);//放入每页条数

        String json = JSON.toJSONString(map);
        try {
            json = URLEncoder.encode(json, "GBK");
            result = getResponseEntity(suffix_path, json, returnMap);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("获取脚本列表失败！");
        }
        if (StringUtils.isNotEmpty(result.getBody())) {
            try {
                Map topViewMap = JSONObject.parseObject(result.getBody());
                Map content = (Map) topViewMap.get("content");
                List<Map<String, String>> list = (List<Map<String, String>>) content.get("dataList");
                for (int i = 0; i < list.size(); i++) {
                    Map<String, String> maps = list.get(i);
                    String s = maps.get("threeTypeName");
                    if ("事件单".equals(s)) {
                        list2.add(list.get(i));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException("自动化平台返回结果格式不正确！");
            }
        }
        return list2;
    }

    @Override
    public List<Map<String, Object>> getCommonTaskInfo(String commonTaskId) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        String suffix_path = "/aoms/scriptplatform.do?action=getCommonTaskInfoWithDetail&user=ideal&token={token}";
        String token = getScriptTokenToFm();
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException("连接脚本服务化平台获取token失败。");
        }
        ResponseEntity<String> result = null;
        returnMap.put("token", token);
        map.put("commonTaskId", Integer.parseInt(commonTaskId));
        String json = JSON.toJSONString(map);
        try {
            json = URLEncoder.encode(json, "GBK");
            result = getResponseEntity(suffix_path, json, returnMap);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("获取脚本列表失败！");
        }
        if (StringUtils.isNotEmpty(result.getBody())) {

            Map topViewMap = JSONObject.parseObject(result.getBody());
            Map content = ((JSONObject) topViewMap).getJSONObject("content");

            Object obj = content.get("globalParams");
            if (!ObjectUtils.isEmpty(obj)) {
                list = (List<Map<String, Object>>) content.get("globalParams");
                list.get(0).put("startTaskJsonKey", content.get("startTaskJsonKey"));
            }
        }
        return list;
    }

    @Override
    public Map<String, Object> startCommonTaskByConfig(String type) {
        Map<String, Object> returnMap = new HashMap<>();
        String suffix_path = "/aoms/scriptplatform.do?action=startCommonTaskByConfig&user=ideal&token={token}";
        String token = getScriptTokenToFm();
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException("连接脚本服务化平台获取token失败。");
        }
        ResponseEntity<String> result = null;
        returnMap.put("token", token);//放入token到消息头

        try {
            String json = URLEncoder.encode(type, "GBK");
            result = getResponseEntity(suffix_path, json, returnMap);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("脚本执行失败！");
        }
        Map<String, Object> topViewMap = JSONObject.parseObject(result.getBody());

        return topViewMap;
    }

    @Override
    public Map<String, Object> monitorActivityForFlowIdLog(String stepId) {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        String suffix_path = "/aoms/scriptplatform.do?action=monitorActivityForFlowIdLog&user=ideal&token={token}";
        String token = getScriptTokenToFm();
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException("连接脚本服务化平台获取token失败。");
        }
        ResponseEntity<String> result = null;
        returnMap.put("token", token);
        map.put("flowId", Integer.parseInt(stepId));
        String json = JSON.toJSONString(map);
        try {
            json = URLEncoder.encode(json, "GBK");
            result = getResponseEntity(suffix_path, json, returnMap);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("调用自动化查询结果失败！");
        }
        Map<String, Object> topViewMap = JSONObject.parseObject(result.getBody());

        return topViewMap;
    }


    //---------------2021/4/26数据变更自动化-------------

    /**
     * 获取数据变更单自动化token
     *
     * @param mobilePhone
     * @return
     */
    public String getDataChangeToken(String mobilePhone) {
        String token = "";
        String suffix_path = "/aoms/pubAomsRecordJson.do?action=login&loginType=tel&user={mobilePhone}";
        Map<String, Object> params = new HashMap<>();
        params.put("mobilePhone", mobilePhone);
        ResponseEntity<String> entity = null;
        try {
            entity = restTemplate.getForEntity(getEntegorUrl() + suffix_path, String.class, params);
        } catch (Exception e) {
            log.error("数据变更单获取token失败: " + e.getMessage());
            e.printStackTrace();
        }
        if (entity != null && entity.getStatusCodeValue() == Integer.valueOf(SUCCESS_CODE)) {
            String body = entity.getBody();
            Map tokenMap = JSONObject.parseObject(body);
            token = (String) tokenMap.get("access_token");
        }
        return token;
    }


    @Override
    public List<Map<String, String>> getDataCommonTaskList(String sysFlag, String sysName) {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, Object> urlMap = new HashMap<>();
        List<Map<String, Object>> paramMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        String suffix_path = "/aoms/pubAomsRecordJson.do?modultype=50&action=getService&access_token={token}";
        String currentLoginUser = ShiroUtils.getOgUser().getMobilPhone();
        String token = getDataChangeToken(currentLoginUser);
        /*if (StringUtils.isEmpty(token)) {
            throw new BusinessException("数据变更单场景服务列表,连接脚本服务化平台获取token失败。");
        }*/
        ResponseEntity<String> result;
        urlMap.put("token", token);//放入token到消息头
        map.put("sysFlag", sysFlag);
        map.put("sysName", sysName);
        paramMap.add(map);

        String json = JSON.toJSONString(paramMap);
        /*try {
            json = URLEncoder.encode(json, "GBK");
            result = getResponseEntity(suffix_path, json, urlMap);;
        } catch (Exception e) {
            log.error("获取数据变更单脚本列表失败: "+e.getMessage());
            throw new BusinessException("获取数据变更单脚本列表失败 ");
        }
        if (StringUtils.isNotEmpty(result.getBody())) {
            try {
                Map<String,Object> returnMap = JSONObject.parseObject(result.getBody());
                list = (List<Map<String, String>>) returnMap.get("dataList");
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException("自动化平台获取数据变更服务数据返回结果格式不正确！");
            }
        }*/

       /*
       * [{
		“scriptUuid”: aaa-ddd--f--w--d,
		“serviceName”: “重启服务”,
		“bussName”: “通用”,
		“bussTypeName”: “通用”,
		“threeTypeName”: “三级类别,
		“scriptVersion”: “1.3”
}]
       *
       *
       * */

        Map<String, String> t1 = new HashMap<>();
        t1.put("scriptUuid", "aaa-ddd--f--w--d");
        t1.put("serviceName", "重启服务");
        t1.put("bussName", "通用");
        Map<String, String> t2 = new HashMap<>();
        t2.put("scriptUuid", "aaa-ddd--f--w--d-2");
        t2.put("serviceName", "重启服务-2");
        t2.put("bussName", "通用-2");

        list.add(t1);
        list.add(t2);
        return list;
    }

    @Override
    public List<Map<String, String>> getServiceParamList(List<Map<String, Object>> paramMap) {
        List<Map<String, String>> list = new ArrayList<>();
        String suffix_path = "/aoms/pubAomsRecordJson.do?modultype=50&action=getParams&access_token={token}";
        String mobilPhone = ShiroUtils.getOgUser().getMobilPhone();
        //获取token
        String token = getDataChangeToken(mobilPhone);
        if (StringUtils.isNotEmpty(token)) {
            throw new BusinessException("数据变更获取服务参数Token验证失败");
        }

        //1.构建路径参数
        Map<String, String> urlParam = new HashMap<>();
        urlParam.put("token", token);

        return list;

    }

    @Override
    public List<Map<String, String>> getCommonTaskListByQuery(String taskName) {
        List<Map<String, String>> list2 = new ArrayList<>();
        PubParaValue ppv = new PubParaValue();
        String bsTypeName = "";
        //根据当前登陆人拥有角色判定查询哪个列表
        List<OgRole> roleIds = iSysRoleService.selectRolesByUserId(ShiroUtils.getUserId());//拿到登陆人角色
        if (roleIds.isEmpty()) {
            return list2;
        } else {
            for (OgRole role : roleIds) {
                if ("100006".equals(role.getRid())) {
                    ppv = iPubParaValueService.selectPubParaValue("sys_code", "sw");
                    bsTypeName = "95580通用查询工具";
                    break;
                }
                if ("100005".equals(role.getRid())) {
                    ppv = iPubParaValueService.selectPubParaValue("sys_code", "sn");
                    bsTypeName = "省内通用查询工具";
                    break;
                }
            }
            if (StringUtils.isEmpty(bsTypeName)) {
                return list2;
            }
        }
        String code = ppv.getValueDetail();
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        String suffix_path = "/aoms/scriptplatform.do?action=getCommonTaskList&user={code}&token={token}";
        String token = getScriptTokenToFm();
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException("获取脚本列表失败，连接脚本服务化平台获取token失败。");
        }
        ResponseEntity<String> result = null;
        returnMap.put("code", code);//放入token到消息头
        returnMap.put("token", token);//放入token到消息头
        map.put("taskName", taskName);
        map.put("serviceName", "");
        map.put("bsTypeName", bsTypeName);//放入系统编码
        map.put("start", 0);//放入分页起始值
        map.put("limit", 300);//放入每页条数

        String json = JSON.toJSONString(map);
        try {
            json = URLEncoder.encode(json, "GBK");
            result = getResponseEntity(suffix_path, json, returnMap);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("获取脚本列表失败！");
        }
        if (StringUtils.isNotEmpty(result.getBody())) {
            try {
                Map topViewMap = JSONObject.parseObject(result.getBody());
                Map content = (Map) topViewMap.get("content");
                List<Map<String, String>> list = (List<Map<String, String>>) content.get("dataList");
                for (int i = 0; i < list.size(); i++) {
                    Map<String, String> maps = list.get(i);
                    String s = maps.get("threeTypeName");
                    if ("事件单".equals(s)) {
                        list2.add(list.get(i));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException("自动化平台返回结果格式不正确！");
            }
        }
        return list2;
    }

    public String getAutoToken(String userName) {
        String token = "";
        String suffix_path = "/aoms/token/getToken.do";
        Map<String, Object> params = new HashMap<>();
        params.put("userName", userName);
        params.put("userPwd", "");
        params.put("validFlag", 0);
        ResponseEntity<String> entity = null;
        try {
            entity = restTemplate.getForEntity(getEntegorUrl() + suffix_path, String.class, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (entity != null && entity.getStatusCodeValue() == Integer.valueOf(SUCCESS_CODE)) {
            String body = entity.getBody();
            Map tokenMap = JSONObject.parseObject(body);
            token = (String) tokenMap.get("token");
        }
        return token;
    }
    /**
     * 调用自动化获取到中台创建工单
     * @param json
     * @param userName
     * @return
     */

    public Map<String, Object> startAutoEntegorMiddleGround(String from,String json,String userName,String itsmNo,String selfToken) {
        Map<String, Object> returnMap = new HashMap<>();
        String token =selfToken;
        if(null==selfToken || selfToken.isEmpty()) {
            token = getAutoToken(userName);
            if (StringUtils.isEmpty(token)) {
                throw new BusinessException("连接中台获取token失败。");
            }
        }
        String suffix_path = "/aoms/order/createOrder.do";
        ResponseEntity<String> result = null;
        returnMap.put("token", token);//放入token到消息头
        returnMap.put("userName", userName);//放入token到消息头
        try {
            String headJson = JSON.toJSONString(returnMap);
            headJson = URLEncoder.encode(headJson, "GBK");
            returnMap.put("paramsHeader", headJson);//放入token到消息头
            //处理json
            Map dataMap = JSONObject.parseObject(json);
            Map m = new HashMap();
            Map morders = new HashMap();
            m.put("itilNo", from+"_"+itsmNo);
            m.put("createUser", userName);
            m.put("platsource", "itsm");
            morders.put("orderName", itsmNo);
            morders.put("templateName",  dataMap.get("templateName"));
            Map<String, Object> data =this.middleTemplateInfo(userName,token,String.valueOf(dataMap.get("templateName")));
            morders.put("templateType", data.get("templateType"));
            morders.put("orderSort", "1");
            morders.put("params", dataMap.get("params"));
            Map<String, Object> cmps = JSONObject.parseObject(String.valueOf(dataMap.get("computers")));
            String ips = String.valueOf(cmps.get("ip"));
            List<Map> lc=new ArrayList<Map>();
            Map mocomputers = null;
            if(null!=ips && ips.length()>0) {
                String[] ip=ips.split(",");
                for (String string : ip) {
                    mocomputers = new HashMap();
                    mocomputers.put("ip", string);
                    mocomputers.put("port", "16005");
                    lc.add(mocomputers);
                }
                lc.add(mocomputers);
            }
            mocomputers.put("computers", lc);
            m.put("orders", morders);
            String jsons = JSON.toJSONString(m);
            result = getAutoEntegorMiddleGroundResponseEntity(suffix_path, jsons, returnMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("中台调用失败！");
        }
        Map<String, Object> topViewMap = JSONObject.parseObject(result.getBody());

        return topViewMap;
    }
    public ResponseEntity<String> getAutoEntegorMiddleGroundResponseEntity(String suffix_path, String json, Map uriVariables) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.APPLICATION_JSON_UTF8;
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("paramsHeader", String.valueOf(uriVariables.get("paramsHeader")));
        headers.add("token", String.valueOf(uriVariables.get("token")));
        headers.add("userName", String.valueOf(uriVariables.get("userName")));
        HttpEntity<String> request = new HttpEntity<String>(json, headers);
        return restTemplate.postForEntity(getEntegorUrl() + suffix_path, request, String.class, uriVariables);
    }
    public Map<String, Object> middleTables(String userName,String templateName,String selfToken) {
        Map<String, Object> returnMap = new HashMap<>();
        String token =selfToken;
        if(null==selfToken || selfToken.isEmpty()) {
            token = getAutoToken(userName);
            if (StringUtils.isEmpty(token)) {
                throw new BusinessException("连接中台获取token失败。");
            }
        }
        String suffix_path = "/aoms/getModelListInterface.do?user="+userName+"&token="+token+"&templateName="+templateName;
        ResponseEntity<String> result = null;
        returnMap.put("token", token);//放入token到消息头
        returnMap.put("userName", userName);//放入token到消息头
        try {
            String headJson = JSON.toJSONString(returnMap);
            headJson = URLEncoder.encode(headJson, "GBK");
            result = getAutoEntegorMiddleGroundResponseEntity(suffix_path, "", returnMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("中台调用失败！");
        }
        Map<String, Object> topViewMap = JSONObject.parseObject(result.getBody());
        return topViewMap;
    }
    public Map<String, Object> middleTaskState(String userName,String itsmNo,String wType,String selfToken) {
        Map<String, Object> returnMap = new HashMap<>();
        String token =selfToken;
        if(null==selfToken || selfToken.isEmpty()) {
            token = getAutoToken(userName);
            if (StringUtils.isEmpty(token)) {
                throw new BusinessException("连接中台获取token失败。");
            }
        }
        String suffix_path = "/aoms/order/getOrderStatus.do";
        ResponseEntity<String> result = null;
        returnMap.put("token", token);//放入token到消息头
        returnMap.put("userName", userName);//放入token到消息头
        try {
            String headJson = JSON.toJSONString(returnMap);
            headJson = URLEncoder.encode(headJson, "GBK");
            returnMap.put("paramsHeader", headJson);//放入token到消息头
            //处理json
            Map m = new HashMap();
            m.put("itilNo", wType+"_"+itsmNo);
            String jsons = JSON.toJSONString(m);
            result = getAutoEntegorMiddleGroundResponseEntity(suffix_path, jsons, returnMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("中台调用失败！");
        }
        Map<String, Object> topViewMap = JSONObject.parseObject(result.getBody());

        return topViewMap;
    }
    public Map<String, Object> middleTemplateInfo(String userName,String selfToken,String templateName) {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("templateName", templateName);
        retMap.put("templateId", "-1");
        retMap.put("templateType", "8");
        String token =selfToken;
        if(null==selfToken || selfToken.isEmpty()) {
            token = getAutoToken(userName);
            if (StringUtils.isEmpty(token)) {
                throw new BusinessException("连接中台获取token失败。");
            }
        }
        String suffix_path = "/aoms/getModelListInterface.do?user="+userName+"&token="+token+"&templateName="+templateName;
        ResponseEntity<String> entity = null;
        returnMap.put("token", token);//放入token到消息头
        returnMap.put("userName", userName);//放入token到消息头
        returnMap.put("templateName", templateName);//放入token到消息头
        try {
            String headJson = JSON.toJSONString(returnMap);
            headJson = URLEncoder.encode(headJson, "GBK");
            returnMap.put("paramsHeader", headJson);//放入token到消息头
            //处理json
            Map m = new HashMap();
            m.put("templateName", templateName);
            String jsons = JSON.toJSONString(m);
            entity = getAutoEntegorMiddleGroundResponseEntity(suffix_path, jsons, returnMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("中台调用失败！");
        }
        if (entity != null && entity.getStatusCodeValue() == Integer.valueOf(SUCCESS_CODE)) {
            String body = entity.getBody();
            Map topViewMap = JSONObject.parseObject(body);
            boolean state = Boolean.parseBoolean(String.valueOf(topViewMap.get("success")));
            if(state) {
                String  jsonString=String.valueOf(topViewMap.get("dataList"));
                JSONArray array = JSONArray.parseArray(jsonString);
                for (Iterator iter = array.iterator(); iter.hasNext();)
                {
                    JSONObject jsonObject = (JSONObject) iter.next();
                    Map maps = JSONObject.parseObject(jsonObject.toString());
                    retMap.put("templateId", maps.get("iid"));
                    retMap.put("templateType", maps.get("type"));
                    break;
                }
            }
        }
        return retMap;
    }
    /**
     * 调用python服务执行任务，创建工单。并返回执行结果
     * @param json
     * @return
     */
    public Map<String, Object> createItsmToMiddleUsePythonTemplate(String json) {
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("status", "-1");
        retMap.put("message", "");
        String suffix_path = entegorPyUrl+"/create/";
        ResponseEntity<String> entity = null;
        try {
            entity = getAutoEntegorMiddlePyResponseEntity(suffix_path, json, new HashMap<>());
            log.info("中台Create调用");
        } catch (Exception e) {
            log.error("中台Create调用",e);
            e.printStackTrace();
            throw new BusinessException("中台Create调用失败！");
        }
        if (entity != null && entity.getStatusCodeValue() == Integer.valueOf(SUCCESS_CODE)) {
            String body = entity.getBody();
            log.info("中台Create调用 ",body);
            Map topViewMap = JSONObject.parseObject(body);
            String state = String.valueOf(topViewMap.get("status"));
            String  jsonString=String.valueOf(topViewMap.get("message"));
            String  code=String.valueOf(topViewMap.get("code"));
            if("True".equalsIgnoreCase(state)) {
                retMap.put("status", "0");
            }
            retMap.put("message", "返回码:"+code+" msg:"+autoDizmap.get(code)+"\n 返回信息:"+jsonString);
        }
        return retMap;
    }
    public ResponseEntity<String> getAutoEntegorMiddlePyResponseEntity(String suffix_path, String json, Map uriVariables) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.APPLICATION_JSON_UTF8;
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> request = new HttpEntity<String>(json, headers);
        return restTemplate.postForEntity(suffix_path, request, String.class, uriVariables);
    }
    /**
     *  调用python服务获取执行任务，并返回执行结果
     * @param json
     * @return
     */
    public Map<String, Object> queryShItsmToMiddleUsePythonTemplate(String json) {
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("status", "-1");
        retMap.put("message", "");
        String suffix_path = entegorPyUrl+"/check/";
        ResponseEntity<String> entity = null;
        try {
            entity = getAutoEntegorMiddlePyResponseEntity(suffix_path, json, new HashMap<>());
            log.info("中台check调用");
        } catch (Exception e) {
            log.error("中台check调用",e);
            e.printStackTrace();
            throw new BusinessException("中台Check调用失败！");
        }
        if (entity != null && entity.getStatusCodeValue() == Integer.valueOf(SUCCESS_CODE)) {
            String body = entity.getBody();
            log.info("中台check调用 "+body);
            Map topViewMap = JSONObject.parseObject(body);
            String state = String.valueOf(topViewMap.get("status"));
            String  code=String.valueOf(topViewMap.get("code"));
            String  jsonString=String.valueOf(topViewMap.get("message"));
            if("True".equalsIgnoreCase(state)) {
                retMap.put("status", "0");
            }
            retMap.put("retJson",jsonString);
            retMap.put("message", "返回码:"+code+" msg:"+autoDizmap.get(code)+"\n 返回信息:"+jsonString);
        }
        return retMap;
    }
    private static Map<String, String> autoDizmap = null;
    static {
        autoDizmap = new HashMap<String, String>();
        autoDizmap.put("4000", "非原生接口问题，核实整体参数");
        autoDizmap.put("4301", "请求token接口出错 接口参数");
        autoDizmap.put("4302", "获取模板IID和type失败 接口参数 templateName");
        autoDizmap.put("4303", "获取模板参数和设备IID出错 接口参数 Params 和 ipist");
        autoDizmap.put("4304", "工单创建接口出错，接口参数itilNo （此码错误大概率为itilNo问题或者原接口出错）");
        autoDizmap.put("4306", "itilNo 为空");
        autoDizmap.put("4307", "工单状态查询错误，原生接口请求失败");
    }
}