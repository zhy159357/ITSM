package com.ruoyi.form.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.entity.AutomationPlatFormsParams;
import com.ruoyi.form.entity.automation.CheckStatus;
import com.ruoyi.form.entity.automation.EquipmentInfo;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AutomationPlatformsService {
    /**
     * @param automationPlatFormsParams
     */
    List<CheckStatus> commitTask(AutomationPlatFormsParams automationPlatFormsParams);

    /**
     * 获取设备列表
     *
     * @param
     * @return
     */
    List<EquipmentInfo> getEquipmentInfoUrl();

    /**
     * 查询任务执行状态
     *
     * @param tokenUserNameJson 登陆信息
     * @param butterflyVersion  工单号
     * @return
     */
    List<CheckStatus> checkStatus(String tokenUserNameJson, String butterflyVersion);

    /**
     * 获取token
     * @param userName 用户名
     * @param userPwd 密码
     * @return
     */
    String getToken(String userName, String userPwd);
}
