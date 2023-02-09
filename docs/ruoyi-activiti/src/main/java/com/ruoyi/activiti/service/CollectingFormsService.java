package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.CollectingFormsInspection;


import java.util.List;


public interface CollectingFormsService {

    /**
     * 查询日常巡检记录单
     * @param collectingFormsInspection
     * @return
     */
    List<CollectingFormsInspection> selectCollectingList(CollectingFormsInspection collectingFormsInspection);

    /**
     * 根据id删除日常巡检记录单
     * @param id
     * @return
     */
    int deleteCollectingById(String id);

    /**
     * 新增日常巡检记录单
     * @param collectingFormsInspection
     * @return
     */
    int insertCollecting(CollectingFormsInspection collectingFormsInspection);

    /**
     * 修改日常巡检记录单
     * @param collectingFormsInspection
     * @return
     */
    int updateCollecting(CollectingFormsInspection collectingFormsInspection);

    /**
     * 根据id查询巡检记录
     * @param id
     * @return
     */
    CollectingFormsInspection selectCollectingById(String id);
}
