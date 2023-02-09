package com.ruoyi.activiti.adapter;

import com.ruoyi.common.core.page.TableDataInfo;

import java.util.List;

/**
 * 工作台代办适配器
 *
 * @author LiuPeng
 */
public interface IBenchAdapter {
    /**
     * 是否支持该用户、角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    public boolean support(String userId, List<String> roleIds);

    /**
     * 代办类型
     *
     * @return
     */
    public String getBenchType();

    /**
     * 是否单独主页显示
     *
     * @return
     */
    public boolean isMainTask();

    /**
     * 获取代办任务总数
     *
     * @param type
     * @return
     */
    public long getTotalCount(String userId, String type);

    /**
     * 分页获取代办
     *
     * @param type
     * @return
     */
    public TableDataInfo getBenchPagerTasks(String userId, String type);

}