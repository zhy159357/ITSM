package com.ruoyi.activiti.service;

import com.ruoyi.common.core.page.TableDataInfo;

public interface EndTaskService {

    TableDataInfo getEndPagerTasks(String processKey, String number);

    int delete(String id, String type) throws Exception;

    int close(String id, String type) throws Exception;
}
