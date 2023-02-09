package com.ruoyi.form.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.form.domain.EventSheet;

import java.util.List;

/**
 * 事件单Mapper接口
 *
 * @author ruoyi
 * @date 2022-06-07
 */
public interface EventSheetMapper extends BaseMapper<EventSheet>
{
    /**
     * 查询事件单
     *
     * @param eventId 事件单ID
     * @return 事件单
     */
    public EventSheet selectEventSheetById(String eventId);

    /**
     * 查询事件单列表
     *
     * @param eventSheet 事件单
     * @return 事件单集合
     */
    public List<EventSheet> selectEventSheetList(EventSheet eventSheet);

    /**
     * 新增事件单
     *
     * @param eventSheet 事件单
     * @return 结果
     */
    public int insertEventSheet(EventSheet eventSheet);

    /**
     * 修改事件单
     *
     * @param eventSheet 事件单
     * @return 结果
     */
    public int updateEventSheet(EventSheet eventSheet);

    /**
     * 删除事件单
     *
     * @param eventId 事件单ID
     * @return 结果
     */
    public int deleteEventSheetById(String eventId);

    /**
     * 批量删除事件单
     *
     * @param eventIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEventSheetByIds(String[] eventIds);

    EventSheet selectEventSheetByCondition(EventSheet eventSheet);

    /**
     * 查询指定天数前事件单数据
     *
     * @return
     */
    List<EventSheet> selectEventAutoCloseList();

    /**
     * 事件单自动关闭
     *
     * @param event
     */
    void updateEvent(EventSheet event);
}
