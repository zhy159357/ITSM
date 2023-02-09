package com.ruoyi.activiti.service;

import com.ruoyi.common.core.page.TableDataInfo;

public interface BenchService {
    /**
     * 分页查询代办
     *
     * @param type
     * @return
     */
    public TableDataInfo getBenchPagerTasks(String userId, String type);

}
