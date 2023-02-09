package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.SmBizTask;

public interface TaskMapper {


    /**
     *
     * @param id
     * @return
     */
    SmBizTask selectTaskById(String id);

    /**
     *
     * @param id
     * @return
     */
    SmBizTask selectSchedulingId(String id);

    /**
     *
     * @param smBizTask
     */
    int insertTask(SmBizTask smBizTask);

    /**
     *
     * @param smBizTask
     */
    int updateTask(SmBizTask smBizTask);

    /**
     * 根据id删除
     * @param taskId
     * @return
     */
    int deleteById(String taskId);

    int updateTaskByDelay(SmBizTask task);


}
