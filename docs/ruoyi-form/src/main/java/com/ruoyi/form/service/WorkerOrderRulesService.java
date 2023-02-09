package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.CustomerFormContent;
import com.ruoyi.form.domain.CustomerVo;

import java.util.Map;

/**
 * @ClassName WorkerOrderRelusInterface
 * @Description 工单规则接口
 * @Author JiaQi Zhang
 * @Date 2022/12/26 8:59
 */
public interface WorkerOrderRulesService {
    AjaxResult insertOrUpdate(String code, CustomerFormContent customerFormContent);

    AjaxResult getStartProcessCondition(String code);

    AjaxResult processSubmit(String code, String businessKey, String version, Map<String, Object> variables);

    AjaxResult approvalPopUp(String code, String processId, Integer id, String type, String version);

    AjaxResult complete(String code, String taskId, String instanceId, String statusStr, CustomerVo customerVo);

    AjaxResult list(String code, String type, Page page);
}
