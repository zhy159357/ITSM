package com.ruoyi.activiti.controller.out.system;

import com.alibaba.fastjson.JSON;
import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.service.IPubBizPresmsService;
import com.ruoyi.activiti.service.IPubBizSmsService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.netty.utils.DESUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IOgPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/message")
public class MessageController extends BaseController {

    @Autowired
    private IPubBizSmsService pubBizSmsService;

    @Autowired
    private IPubBizPresmsService pubBizPresmsService;

    @Autowired
    private IOgPersonService ogPersonService;

    @Value("${forward.encryKey}")
    private String encryKey;

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    /**
     * 移动运维调用itsm发送下行短信
     */
    @PostMapping("/findPreSmsAndSend")
    public void findPreSmsAndSend(HttpServletRequest request, HttpServletResponse response) {
        String params = ServletUtils.convertParam(request);
        if (StringUtils.isEmpty(params)) {
            throw new BusinessException("参数不可为空！");
        }
        PubBizPresms pubBizPresms;
        try {
            String decrypt = DESUtils.decrypt(params, encryKey);
            pubBizPresms = JSON.parseObject(decrypt, PubBizPresms.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("参数信息解密失败！");
        }
        pubBizPresmsService.insertPubBizPresms(pubBizPresms);
        log.debug("-------移动运维后台调用itsm平台发送短信开始-------");
        pubBizSmsService.findPreSmsAndSend(pubBizPresms);
        log.debug("-------移动运维后台调用itsm平台发送短信结束-------");
    }

    /**
     * 移动运维调用itsm发送上行短信
     */
    @PostMapping("/sendMsgUp")
    public void sendMsgUp(HttpServletRequest request, HttpServletResponse response) {
        String params = ServletUtils.convertParam(request);
        if (StringUtils.isEmpty(params)) {
            throw new BusinessException("参数不可为空！");
        }
        Map map;
        try {
            String decrypt = DESUtils.decrypt(params, encryKey);
            map = JSON.parseObject(decrypt, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("参数信息解密失败！");
        }
        String taskId = String.valueOf(map.get("taskId"));
        String bizId = String.valueOf(map.get("bizId"));
        String userId = String.valueOf(map.get("userId"));
        String smsUserId = String.valueOf(map.get("smsUserId"));
        String processDefinitionKey = String.valueOf(map.get("processDefinitionKey"));
        OgPerson person = ogPersonService.selectOgPersonEvoById(userId);
        OgPerson smsPerson = ogPersonService.selectOgPersonEvoById(smsUserId);
        log.debug("-------移动运维后台调用itsm平台发送上行短信开始-------");
        pubBizSmsService.sendMessage(taskId, bizId, person, smsPerson, processDefinitionKey);
        log.debug("-------移动运维后台调用itsm平台发送上行短信结束-------");
    }
}
