package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.EventRun;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 运行事件单Mapper接口
 *
 * @author zx
 * @date 2021-03-04
 */
@Component
public interface EventRunMapper
{
    /**
     * 查询运行事件单
     *
     * @param eventId 运行事件单ID
     * @return 运行事件单
     */
    public EventRun selectEventRunById(String eventId);

    public EventRun selectEventRunByIdMysql(String eventId);

    /**
     * 查询运行事件单
     *
     * @param eventNo 运行事件单ID
     * @return 运行事件单
     */
    public EventRun selectEventRunByNo(String eventNo);

    public EventRun selectEventRunByNoMysql(String eventNo);

    /**
     * 查询运行事件单列表
     *
     * @param eventRun 运行事件单
     * @return 运行事件单集合
     */
    public List<EventRun> selectEventRunList(EventRun eventRun);

    public List<EventRun> selectEventRunListMysql(EventRun eventRun);

    /**
     * 新增运行事件单
     *
     * @param eventRun 运行事件单
     * @return 结果
     */
    public int insertEventRun(EventRun eventRun);

    /**
     * 修改运行事件单
     *
     * @param eventRun 运行事件单
     * @return 结果
     */
    public int updateEventRun(EventRun eventRun);

    public int updateEventRunMysql(EventRun eventRun);

    /**
     * 删除运行事件单
     *
     * @param eventId 运行事件单ID
     * @return 结果
     */
    public int deleteEventRunById(String eventId);

    /**
     * 批量删除运行事件单
     *
     * @param eventIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEventRunByIds(String[] eventIds);

    /**
     * 根据id更新有效无效标识
     * @param id
     * @return
     */
    int updateFmYxByInvalidationMark(String id);

    /**
     *任务耗时查询
     * @param eventRun
     * @return
     */
    List<EventRun> selectByTiming(EventRun eventRun);

    List<EventRun> selectByTimingMysql(EventRun eventRun);

    List<EventRun> selectActEventRunList(EventRun eventRun);

    List<EventRun> selectActEventRunListMysql(EventRun eventRun);

    List<EventRun> selectMydealEventRunList(EventRun eventRun);

    List<EventRun> selectMydealEventRunListMysql(EventRun eventRun);
}