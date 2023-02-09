package com.ruoyi.form.entity.automation;

import lombok.Data;

import java.io.Serializable;

@Data
public class DoubleAuditor implements Serializable {
    /**
     * 工单号
     */
    private  String butterflyversion;

    /**
     * workItemId
     */
    private String iid;

}
