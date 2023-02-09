package com.ruoyi.activiti.adapter;

import com.ruoyi.common.core.page.TableDataInfo;


/**
 * 结束任务流程接口
 */
public interface IEndTaskAdapter {

    /**
     * 指定类型下的所有的任务列表数据
     * @param processKey
     * @param number
     * @return
     */
    TableDataInfo getEndPagerTasks(String processKey,String number);


    /**
     * 流程删除
     * @param id 单子Id
     * @return
     */
    int remove(String id);

    /**
     * 流程关闭
     * @param id
     * @return
     */
    int close(String id);
}
