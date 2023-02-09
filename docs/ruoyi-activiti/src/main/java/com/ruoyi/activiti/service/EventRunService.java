package com.ruoyi.activiti.service;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.activiti.domain.EventRun;

import java.util.List;

/**
 * 运行事件单Service接口
 *
 * @author zx
 * @date 2021-03-04
 */
public interface EventRunService
{
    /**
     * 查询运行事件单
     *
     * @param eventId 运行事件单ID
     * @return 运行事件单
     */
    public EventRun selectEventRunById(String eventId);
    /**
     * 查询运行事件单h
     *
     * @param eventNo 运行事件单号
     * @return 运行事件单
     */
    public EventRun selectEventRunByNo(String eventNo);

    /**
     * 查询运行事件单列表
     *
     * @param eventRun 运行事件单
     * @return 运行事件单集合
     */
    public List<EventRun> selectEventRunList(EventRun eventRun);

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

    /**
     * 批量删除运行事件单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteEventRunByIds(String ids);

    /**
     * 删除运行事件单信息
     *
     * @param eventId 运行事件单ID
     * @return 结果
     */
    public int deleteEventRunById(String eventId);

    /**
     * 根据id跟新有效无效标识
     * @param id
     * @return
     */
    int updateFmYxByInvalidationMark(String id);

    /**
     * 任务耗时查询
     * @param eventRun
     * @return
     */
    List<EventRun> selectByTiming(EventRun eventRun);

    /**
     * 匹配值班人ID
     * @param eventRun
     * @return
     */
    public String selectDutyUserId(EventRun eventRun,String loginUserId);

    /**
     * 监控事件单转派
     * @param eventId
     * @param userId
     */
    public void reassign(String eventId,String userId);

    /**
     * 待办查询
     * @param eventRun
     * @return
     */
    public  List<EventRun> selectActEventRunList(EventRun eventRun);
    /**
     * 发送短信方法(不包含上行)
     *
     * @param
     * @param msg
     */
    public void sendMsgNoUp(String userId, String msg);

    /**
     * 查询我处理过的任务
     * @param eventRun
     * @return
     */
    public List<EventRun> selectMydealEventRunList(EventRun eventRun);

    /**
     * 系统通知
     * @param eventNo
     * @param userId
     */
    public void sysNotify(String eventNo,String userId);
    public void sysNotifyTime(String msgToAll, JSONObject jsonObject);
}