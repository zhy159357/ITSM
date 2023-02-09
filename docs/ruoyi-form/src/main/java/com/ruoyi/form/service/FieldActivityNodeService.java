package com.ruoyi.form.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.FieldActivityNode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.form.domain.FormStatusActivityNode;
import com.ruoyi.form.domain.FormStepStatus;

import java.util.List;

public interface FieldActivityNodeService extends IService<FieldActivityNode>{


    AjaxResult getFormField(String code,String sid);

    AjaxResult setFormFieldShowStatus(List<FieldActivityNode> fieldActivityNodes);

    AjaxResult setFormStatus(String code, List<FormStatusActivityNode> formStatusActivityNodes);

    AjaxResult setFormStep(String code, List<FormStepStatus> formStepStatuses);

    AjaxResult getCurrentNodeFormStep(String code, String bizStatusName);

    AjaxResult getCurrentNodeFormBizStatus(String code, String nodeId);

    AjaxResult getApproveTemplate();

    AjaxResult synchronize(String code,String type);
}
