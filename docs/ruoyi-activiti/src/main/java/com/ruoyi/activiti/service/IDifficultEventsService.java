package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.DifficultEvents;
import com.ruoyi.common.core.domain.entity.FmBiz;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2021-11-23
 */
public interface IDifficultEventsService {
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
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDifficultEventsByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteDifficultEventsById(String id);

    /**
     * 根据传入事件单保存疑难事件信息
     *
     * @param fmBiz
     * @return
     */
    public int insertDifficultEventsByFmBiz(FmBiz fmBiz, String source);

    /**
     * 根据查询条件获取待处理疑难事件列表
     *
     * @param difficultEvents
     * @return
     */
    public List<DifficultEvents> getDealList(DifficultEvents difficultEvents);

    /**
     * 疑难事件处理
     *
     * @param difficultEvents
     * @return
     */
    public AjaxResult saveFlowDeal(DifficultEvents difficultEvents);

}
