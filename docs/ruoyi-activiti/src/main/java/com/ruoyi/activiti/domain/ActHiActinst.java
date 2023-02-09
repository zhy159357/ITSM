package com.ruoyi.activiti.domain;

import lombok.Data;

import java.util.Date;

/**
 * create by:hecaili
 * Date:2022/8/3 17:25
 */

@Data
public class ActHiActinst {
    private String procInstId;
    private String taskId;
    private Date startTime;
}
