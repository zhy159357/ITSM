package com.ruoyi.form.entity;

import com.ruoyi.form.entity.automation.JsonParamArr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutomationPlatFormsParams implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 设备id 多个用,隔开
     */
    private String equipmentIds;
    /**
     * 工单id
     */
    private String workOrderId;

    /**
     * 特殊参数
     */
    private List<JsonParamArr> jsonParamArrList;


}
