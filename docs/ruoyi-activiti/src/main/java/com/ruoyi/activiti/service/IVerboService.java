package com.ruoyi.activiti.service;

import java.util.List;

/**
 * 告警监控接口
 */
public interface IVerboService {
    List selectSeverity();

    List targetList();

    List selectStatus();
}
