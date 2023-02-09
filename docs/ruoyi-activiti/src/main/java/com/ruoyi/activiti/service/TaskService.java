package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.SmBizTask;

public interface TaskService {


    /**
     * 根据id查询
     * @param id
     * @return
     */
    SmBizTask selectTaskById(String id);
    /**
     *
     * @param id
     * @return
     */

    public SmBizTask selectSchedulingId(String id);
    /**
     *
     * @param smBizTask
     */
    int insertTask(SmBizTask smBizTask);

    /**
     *修改
     * @param smBizTask
     */
    int updateTask(SmBizTask smBizTask);

    /**
     * 根据id删除
     * @param taskId
     * @return
     */
    int deleteById(String taskId);

    /**
     * 修改作业表发布时间
     * @param task
     * @return
     */
    int updateTaskByDelay(SmBizTask task);

}
