package com.ruoyi.activiti.controller.out.system;

import com.alibaba.fastjson.JSON;
import com.ruoyi.activiti.service.IOutSystemSmsCodeService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.netty.utils.DESUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.OutSystemSmsCode;
import com.ruoyi.system.service.IPubParaValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对外系统短信验证码Controller
 *
 * @author zhangchao
 * @date 2021-04-25
 */
@Controller
@RequestMapping("/system/code")
public class OutSystemSmsCodeController extends BaseController {

    @Autowired
    private IOutSystemSmsCodeService smsCodeService;

    @Autowired
    private IPubParaValueService pubParaValueService;

    @Value("${forward.encryKey}")
    private String encryKey;

    private final String outSystemNameCodeKey = "out_system_name_code";

    /**
     * 外系统调用该接口获取短信验证码
     */
    @PostMapping("/getSmsCode")
    public void getSmsCode(HttpServletRequest request, HttpServletResponse response) {
        String params = ServletUtils.convertParam(request);
        OutSystemSmsCode outSystemSmsCode = decrypt(params);
        logger.debug("---------外接系统请求ITSM平台获取短信验证码【" + request.getRequestURI() + "】，输入参数为：" + outSystemSmsCode.toString());
        String volatileFlag = "getSmsCode";
        this.volatileParams(outSystemSmsCode, volatileFlag);
        Map map = smsCodeService.sendSmsCode(outSystemSmsCode);
        AjaxResult ajaxResult;
        if ((boolean) map.get("flag")) {
            ajaxResult = AjaxResult.success((String) map.get("message"));
            ajaxResult.put("smsCode", map.get("smsCode"));
        } else {
            ajaxResult = AjaxResult.error((String) map.get("message"));
        }
        renderString(response, ajaxResult);
    }

    /**
     * 外系统调用该接口验证短信验证码是否正确
     */
    @PostMapping("/volatileSmsCode")
    public void volatileSmsCode(HttpServletRequest request, HttpServletResponse response) {
        String params = ServletUtils.convertParam(request);
        OutSystemSmsCode outSystemSmsCode = decrypt(params);
        logger.debug("---------外接系统请求ITSM平台验证短信验证码【" + request.getRequestURI() + "】，输入参数为：" + outSystemSmsCode.toString());
        String volatileFlag = "volatileSmsCode";
        this.volatileParams(outSystemSmsCode, volatileFlag);
        Map map = smsCodeService.volatileSmsCode(outSystemSmsCode);
        AjaxResult ajaxResult = (boolean) map.get("flag") ?
                AjaxResult.success((String) map.get("message")) : AjaxResult.error((String) map.get("message"));
        renderString(response, ajaxResult);
    }

    /**
     * 外系统调用该接口发送短信验证码
     */
    @PostMapping("/sendMsg")
    public void sendMsg(HttpServletRequest request, HttpServletResponse response){
        String params = ServletUtils.convertParam(request);
        OutSystemSmsCode outSystemSmsCode = decrypt(params);
        logger.debug("---------外接系统请求ITSM平台发送短信【" + request.getRequestURI() + "】，短信内容为：" + outSystemSmsCode.getMsg());
        String volatileFlag = "volatileSendMsg";
        this.volatileParams(outSystemSmsCode, volatileFlag);
        Map map = smsCodeService.sendMsg(outSystemSmsCode);
        AjaxResult ajaxResult = (boolean) map.get("flag") ?
                AjaxResult.success((String) map.get("message")) : AjaxResult.error((String) map.get("message"));
        renderString(response, ajaxResult);
    }

    /**
     * 移动端短信验证码查询
     */
    @GetMapping("/mobileAppMessage")
    public String mobileAppMessage() {
        return "mobileAppMsg/messageList";
    }

    @PostMapping("/messageList")
    @ResponseBody
    public TableDataInfo selectMessageList(OutSystemSmsCode outSystemSmsCode) {
        startPage();
        List<OutSystemSmsCode> outSystemSmsCodes = smsCodeService.selectOutSystemSmsCodeList(outSystemSmsCode);
        return getDataTable(outSystemSmsCodes);
    }

    /**
     * 验证参数
     *
     * @param outSystemSmsCode
     */
    public void volatileParams(OutSystemSmsCode outSystemSmsCode, final String volatileFlag) {
        String phoneNumber = outSystemSmsCode.getPhoneNumber();
        if (StringUtils.isEmpty(phoneNumber)) {
            throw new BusinessException("手机号不可为空！");
        }
        if (!StringUtils.isValidPhoneNumber(phoneNumber)) {
            throw new BusinessException("手机号格式不正确！");
        }
        String sysName = outSystemSmsCode.getSysName();
        String sysCode = outSystemSmsCode.getSysCode();
        if (StringUtils.isEmpty(sysName) || StringUtils.isEmpty(sysCode)) {
            throw new BusinessException("系统名称和系统编码不可为空！");
        }
        String value = pubParaValueService.selectPubParaValueByNameValue(outSystemNameCodeKey, sysCode);
        if (!sysName.equals(value)) {
            throw new BusinessException("根据系统编码[" + sysCode + "]未找到对应的系统名称[" + sysName + "]，请到运维管理平台/参数管理/参数设置维护，" +
                    "参数项名称[短信对接外系统系统名称-编码对应关系]-参数项代码[" + outSystemNameCodeKey + "]！");
        }
        if ("volatileSmsCode".equals(volatileFlag)) {
            String smsCode = outSystemSmsCode.getSmsCode();
            if (StringUtils.isEmpty(smsCode)) {
                throw new BusinessException("验证码不可为空！");
            }
        }
        if ("volatileSendMsg".equals(volatileFlag)) {
            String msg = outSystemSmsCode.getMsg();
            if (StringUtils.isEmpty(msg)) {
                throw new BusinessException("短信信息不可为空！");
            } else if(msg.length() > 1000){
                throw new BusinessException("短信长度不得超过1000字符！");
            }
        }
    }

    /**
     * 解密参数
     *
     * @param params
     * @return
     */
    public OutSystemSmsCode decrypt(String params) {
        if (StringUtils.isEmpty(params)) {
            throw new BusinessException("参数不可为空！");
        }
        OutSystemSmsCode outSystemSmsCode = null;
        try {
            String decrypt = DESUtils.decrypt(params, encryKey);
            outSystemSmsCode = JSON.parseObject(decrypt, OutSystemSmsCode.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("参数信息解密失败！");
        }
        return outSystemSmsCode;
    }

    /**
     * response统一返回加密信息
     *
     * @param response
     * @param ajaxResult
     */
    public void renderString(HttpServletResponse response, AjaxResult ajaxResult) {
        try {
            logger.debug("外接系统访问ITSM系统调用短信平台返回参数为：" + ajaxResult.toString());
            String encrypt = DESUtils.encrypt(encryKey, JSON.toJSONString(ajaxResult));
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String encryKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJNiOGqfM1q8VfeZDE7ZNaON7elmJk3e0KchUg8b2iJoDQ79DSrN5vrvRLCjlUfK9K9yjQwWeTYPJOkW4CDwTZkESAlEXtd1jJWfnMo5Al5n9PvgFj8FfQqvWYm51f6nspJ2Sa9h4y8TJTJNLLKyhzbEzIAndmcfUTdvAgMBAAECgYBRptTCnTiCSF0IM05SUJwa5IKuD92gvpSyQwHN1LRM8XQrKMyE3zL6PKiJYnlBVDLh9eVwUIIos38JKQG8O2yZ9pkM0LRokSRSeaw94TvOxuVas77niX9VeawykLueUfKTKLlcsAvYj8BW5YnceeBjVgfGeAPct9KO4QJBANcisLwOi3NbveG5nieGyQEsqzJlHIGO4ehYGuzEtyBO6GQtsVR2YKT8DrdMVvg5aKIoETm3l1LYLZHVZu7sCQQCvYPpapog8EhTBQbExmufWzB9xgHEWvAqkqXJU1UZEDberIgaGXtsk1xvUtKZlDu2jOYDLEIXgb7P6QuM1oXdAkA29v7Syt2SZjI7u5OWrY5xnWGpE7eBimYbOPfRjGb9P2d8mADsyHr9bxUBTScAaoVMKO6327KVTbfLKw33jX2RAkEAjTo0AOHQHUeWLRYggcizX3aa74S2DM6ZmUJa6UfZBgSe3hGVXYlvQZhzkfMzd3fxB5sbyupwVI6SQ2pBshQJBAMFfu0XsE0V8h3zga7fKifnOFXqMtDSSxzl2BDQSLlERQi24GwuqCS2DsD3bticvS2n88qAFI799prrxUxM";
        //getSmsCodeTest(encryKey);
        //volatileSmsCodeTest(encryKey);
        sendMsg(encryKey);
    }

    // 测试短信发送验证码
    public static void getSmsCodeTest(String encryKey) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.APPLICATION_JSON_UTF8;
        headers.setContentType(type);
        headers.add("Accept", MediaType.ALL.toString());
        Map<Object, Object> params = new HashMap<>();
        params.put("phoneNumber", "18311052132");
        params.put("sysName", "一体化运维系统");
        params.put("sysCode", "AOMS");
        String json = JSON.toJSONString(params);
        json = DESUtils.encrypt(encryKey, json);
        HttpEntity request = new HttpEntity(json, headers);
        ResponseEntity<String> entity = restTemplate.postForEntity("http://localhost:9999/system/code/getSmsCode", request, String.class);
        System.out.println(entity.getBody());
        System.out.println(DESUtils.decrypt(entity.getBody(), encryKey));
    }

    // 测试短信验证码
    public static void volatileSmsCodeTest(String encryKey) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.APPLICATION_JSON_UTF8;
        headers.setContentType(type);
        headers.add("Accept", MediaType.ALL.toString());
        Map<Object, Object> params = new HashMap<>();
        params.put("phoneNumber", "18311052132");
        params.put("sysName", "一体化运维系统");
        params.put("sysCode", "AOMS");
        params.put("smsCode", "734711");
        String json = JSON.toJSONString(params);
        json = DESUtils.encrypt(encryKey, json);
        HttpEntity request = new HttpEntity(json, headers);
        ResponseEntity<String> entity = restTemplate.postForEntity("http://localhost:9999/system/code/volatileSmsCode", request, String.class);
        System.out.println(entity.getBody());
        System.out.println(DESUtils.decrypt(entity.getBody(), encryKey));
    }

    // 测试发送短信
    public static void sendMsg(String encryKey) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.APPLICATION_JSON_UTF8;
        headers.setContentType(type);
        headers.add("Accept", MediaType.ALL.toString());
        Map<Object, Object> params = new HashMap<>();
        params.put("phoneNumber", "18311052935");
        params.put("sysName", "一体化运维系统");
        params.put("sysCode", "AOMS");
        params.put("msg", "测试发送短息11111");
        String json = JSON.toJSONString(params);
        json = DESUtils.encrypt(encryKey, json);
        HttpEntity request = new HttpEntity(json, headers);
        ResponseEntity<String> entity = restTemplate.postForEntity("http://localhost:9999/system/code/sendMsg", request, String.class);
        System.out.println(entity.getBody());
        System.out.println(DESUtils.decrypt(entity.getBody(), encryKey));
    }
}
