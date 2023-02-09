package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.DifficultEvents;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2021-11-23
 */
public interface DifficultEventsMapper {
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public DifficultEvents selectDifficultEventsById(String id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param difficultEvents 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<DifficultEvents> selectDifficultEventsList(DifficultEvents difficultEvents);

    /**
     * 新增【请填写功能名称】
     *
     * @param difficultEvents 【请填写功能名称】
     * @return 结果
     */
    public int insertDifficultEvents(DifficultEvents difficultEvents);

    /**
     * 修改【请填写功能名称】
     *
     * @param difficultEvents 【请填写功能名称】
     * @return 结果
     */
    public int updateDifficultEvents(DifficultEvents difficultEvents);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteDifficultEventsById(String id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDifficultEventsByIds(String[] ids);

    public List<DifficultEvents> getDealList(DifficultEvents difficultEvents);
}
