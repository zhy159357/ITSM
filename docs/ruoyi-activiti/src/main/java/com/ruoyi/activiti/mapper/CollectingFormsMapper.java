package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.CollectingFormsInspection;

import java.util.List;

/**
 * 报表管理Mapper接口
 *
 * @author ruoyi
 * @date 2022-3-8
 */
public interface CollectingFormsMapper {

    /**
     * 查询采集巡检记录单
     *
     * @return 采集巡检记录单
     */
    List<CollectingFormsInspection> selectCollectingList(CollectingFormsInspection collectingFormsInspection);

    /**
     * 根据id删除采集巡检记录单
     *
     * @return
     */
    int deleteCollectingById(String id);

    /**
     * 新增记录
     *
     * @return
     */
    int insertCollecting(CollectingFormsInspection collectingFormsInspection);

    /**
     * 根据id删除采集巡检记录单
     *
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
