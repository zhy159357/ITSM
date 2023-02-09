package com.ruoyi.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.utils.RestTemplateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IAccountSyncService;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.IOgUserService;
import com.ruoyi.system.service.ISysDeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountSyncServiceImpl implements IAccountSyncService {

    //日志记录
    private static final Logger log = LoggerFactory.getLogger(AccountSyncServiceImpl.class);

    @Value("${sso.portalAccountSyncUrl}")
    private String portalAccountSyncUrl;

    @Value("${sso.token}")
    private String token;

    @Autowired
    private IOgUserService ogUserService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private IOgPersonService personService;

    @Autowired
    private RestTemplate restTemplate;
    /*新增修改调用*/
    @Override
    public boolean syncAccount(List<OgUser> ogUserList, String flag) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (OgUser user : ogUserList) {
            Map<String, Object> params = new HashMap<>();
            Map<String, Object> extra = new HashMap<>();
            params.put("userId", user.getUserId());// 账号id
            OgPerson ogPerson = personService.selectOgPersonById(user.getpId());
            //要修改的手机号
            params.put("username", user.getUsername());// 账号
            if ("add".equals(flag)) {
                params.put("mobilPhone", ogPerson.getMobilPhone());// 手机号
            } else {
                params.put("mobilPhone", ogPerson.getNewMobilPhone());// 手机号
                extra.put("newMobilPhone", ogPerson.getMobilPhone());
            }

            params.put("pName", user.getPname());// 名称
            params.put("custNo", user.getCustNo());// 柜员号
            params.put("orgName", user.getOrgname());// 所属机构
            params.put("invalidationMark", user.getInvalidationMark());// 有效标识

            // 人员对应的机构信息不为空传递该参数
            String orgCode = this.selectOrgCodeByUserId(user.getUserId());
            if (StringUtils.isNotEmpty(orgCode)) {
                extra.put("orgCode", orgCode);
            }

            params.put("extra", extra);
            mapList.add(params);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("accountList", mapList);
        boolean sendSuccess = sendSyncAccount(map, flag);
        return sendSuccess;
    }
    /*同步调用*/
    @Override
    public boolean syncAccountTb(List<OgUser> ogUserList,String flag) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (OgUser user : ogUserList) {
            Map<String, Object> params = new HashMap<>();
            Map<String, Object> extra = new HashMap<>();
            params.put("userId", user.getUserId());// 账号id
            OgPerson ogPerson = personService.selectOgPersonById(user.getpId());
            //要修改的手机号
            params.put("username", user.getUsername());// 账号

            params.put("mobilPhone", ogPerson.getMobilPhone());// 手机号
            params.put("pName", user.getPname());// 名称
            params.put("custNo", user.getCustNo());// 柜员号
            params.put("orgName", user.getOrgname());// 所属机构
            params.put("invalidationMark", user.getInvalidationMark());// 有效标识

            // 人员对应的机构信息不为空传递该参数
            String orgCode = this.selectOrgCodeByUserId(user.getUserId());
            if (StringUtils.isNotEmpty(orgCode)) {
                extra.put("orgCode", orgCode);
            }

            params.put("extra", extra);
            mapList.add(params);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("accountList", mapList);
        boolean sendSuccess = sendSyncAccountTb(map,flag);
        return sendSuccess;
    }

    @Override
    public boolean enabledUser(String userId, String invalidationMark) {
        OgUser user = ogUserService.selectOgUserByUserId(userId);
        if (null == user) {
            log.info("-------对应的人员未绑定账号信息不做同步处理，userId:{}", userId);
            return false;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("userId", user.getUserId());// 账号id
        params.put("username", user.getUsername());// 账号
        params.put("mobilPhone", user.getMobilPhone());// 手机号
        params.put("invalidationMark", invalidationMark);// 有效标识
        boolean sendSuccess = sendSyncAccount(params, "enable");
        return sendSuccess;
    }

    public boolean sendSyncAccount(Map<String, Object> params, String methodFlag) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        String json = JSON.toJSONString(params);
        log.info("-----itsm同步账号信息到统一门户系统，参数信息为:" + json + "----");
        HttpEntity<String> requests = new HttpEntity<>(json, headers);

        String url = portalAccountSyncUrl;
        if ("enable".equals(methodFlag)) {
            url += "enableOrDisableUser";
        } else if ("add".equals(methodFlag)) {
            url += "addOrModUser";
        } else {
            url += "modUser";
        }
        // url开始路径是https表示生产环境，http表示测试环境
        if (url.startsWith("https")) {
            log.info("--------------统一门户使用https地址 url:{}", url);
            restTemplate = RestTemplateUtils.restTemplate();
        }

        boolean flag = false;
        try {
            ResponseEntity<String> entity = restTemplate.postForEntity(url, requests, String.class);
            if (entity != null) {
                log.info("------ITSM账号同步调用门户系统，返回码值 status:{}，返回体 body:{}", entity.getStatusCodeValue(), entity.getBody());
                if (entity.getStatusCodeValue() == 200)
                    flag = true;
            } else {
                log.info("------ITSM同步账号信息到门户系统失败------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /*同步调用*/
    public boolean sendSyncAccountTb(Map<String, Object> params, String methodFlag) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        String json = JSON.toJSONString(params);
        log.info("-----itsm同步账号信息到统一门户系统，参数信息为:" + json + "----");
        HttpEntity<String> requests = new HttpEntity<>(json, headers);

        String url = portalAccountSyncUrl;
        if ("enable".equals(methodFlag)) {
            url += "enableOrDisableUser";
        }else{
            url += "addOrModUser";
        }
        // url开始路径是https表示生产环境，http表示测试环境
        if (url.startsWith("https")) {
            log.info("--------------统一门户使用https地址 url:{}", url);
            restTemplate = RestTemplateUtils.restTemplate();
        }

        boolean flag = false;
        try {
            ResponseEntity<String> entity = restTemplate.postForEntity(url, requests, String.class);
            if (entity != null) {
                log.info("------ITSM账号同步调用门户系统，返回码值 status:{}，返回体 body:{}", entity.getStatusCodeValue(), entity.getBody());
                if (entity.getStatusCodeValue() == 200)
                    flag = true;
            } else {
                log.info("------ITSM同步账号信息到门户系统失败------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据当前登录人获取机构编码
     *
     * @param userId
     * @return
     */
    private String selectOrgCodeByUserId(String userId) {
        String orgCode = "";
        OgPerson person = personService.selectOgPersonById(userId);
        if (null != person) {
            OgOrg ogOrg = deptService.selectDeptById(person.getOrgId());
            if (null != ogOrg) {
                orgCode = ogOrg.getOrgCode();
            }
        }
        return orgCode;
    }
}
