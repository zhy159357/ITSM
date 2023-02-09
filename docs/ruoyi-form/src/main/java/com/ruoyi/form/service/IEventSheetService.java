package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.activiti.bmp.entity.BmpEntity;
import com.ruoyi.form.domain.EventSheet;

import java.util.List;
import java.util.Map;

/**
 * 事件单Service接口
 *
 * @author ruoyi
 * @date 2022-06-07
 */
public interface IEventSheetService extends IService<EventSheet>
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
     * 根据事件单编号查询
     * @param eventNo
     * @return
     */
    public EventSheet selectEventSheetByNo(String eventNo);

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
     * 批量删除事件单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteEventSheetByIds(String ids);

    /**
     * 删除事件单信息
     *
     * @param eventId 事件单ID
     * @return 结果
     */
    public int deleteEventSheetById(String eventId);

    /**
     * 启动流程
     * @param eventId
     * @param variables
     */
    BmpEntity startProcess(String eventId, Map<String,Object> variables);

    /**
     * 撤销事件单
     * @param id
     */
    void cancelEventSheetById(String id);

    /**
     * 查询待办
     */
    List<EventSheet> selectEventAgencyList(String processDefinitionKey, String description, EventSheet eventSheet);

    /**
     * 事件单分派
     * @param eventSheet
     */
    void assignEvent(EventSheet eventSheet);

    /**
     * 接单环节
     * @param eventSheet
     */
    void receiveEvent(EventSheet eventSheet);

    /**
     * 处理环节
     * @param eventSheet
     */
    void dealEvent(EventSheet eventSheet);

    /**
     * 事件单二线解决环节
     * @param eventSheet
     */
    void preSolutionEvent(EventSheet eventSheet);

    /**
     * 事件单一线解决环节
     * @param eventSheet
     */
    void solutionEvent(EventSheet eventSheet);

    /**
     * 事件单关闭评价
     * @param eventSheet
     */
    void evaluateEvent(EventSheet eventSheet);

    /**
     * 转换事件单分类及类别
     * @param eventSheet
     */
    void translateEventType(EventSheet eventSheet);

    /**
     * 事件单退回补全
     * @param eventSheet
     */
    void supplyEvent(EventSheet eventSheet);

    /**
     * 查询待关闭的事件单
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
