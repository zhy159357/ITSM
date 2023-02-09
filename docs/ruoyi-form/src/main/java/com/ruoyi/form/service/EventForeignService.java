package com.ruoyi.form.service;

import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.form.domain.CustomerVo;
import com.ruoyi.form.domain.EventForeignVo;
import com.ruoyi.form.domain.EventIncConfirmVo;
import com.ruoyi.form.domain.EventIncCreateVo;
import com.ruoyi.form.domain.EventIncModifyVo;
import com.ruoyi.form.domain.EventIncSearchReqVo;
import com.ruoyi.form.domain.EventMonitorVo;

public interface EventForeignService {
    AjaxResult startEventProcess(EventForeignVo vo);

    /**
     * 创建事件单
     * @param incCreate
     * @return
     */
    AjaxResult createEvent(EventIncCreateVo incCreate);

    /**
     * 值班组名
     * @param groupName
     * @return
     */
    Map<String, Object> selectDutyPersonByGroupName(String groupName);

    /**
     * 客服、柜面追加事件单
     * @param incModifyVo
     * @return
     */
    AjaxResult addInfoEvent(EventIncModifyVo incModifyVo);

    /**
     * 客服撤单
     * @param incModifyVo
     * @return
     */
    AjaxResult cancelEvent(EventIncModifyVo incModifyVo);

    /**
     * 客服、柜面事件确认
     * @param incConfirmVo
     * @return
     */
    AjaxResult confirmEvent(EventIncConfirmVo incConfirmVo);

    /**
     * 查询事件单
     * @param vo
     * @return
     */
    Map<String, Object> searchEvent(EventIncSearchReqVo vo);

    /**
     * 更新事件等级
     * @param vo
     * @return
     */
    Map<String,Object> modifyEventPriority(EventMonitorVo vo);

    /**
     * ①客服派单更新信息（主要是状态变化）
     * ②派单退回
     * @param instanceId
     * @param status
     */
    void sendMsgTokeFu(String instanceId, String status, String statusStr);

    /**
     * 前端页面查询客服派单详情
     * @param bizNo
     * @param page
     * @return
     */
    AjaxResult selectServiceAssignDetailsByNo(String bizNo, Page page);

    /**
     * 判断是否接口发起退回
     * @param bizNo
     * @return
     */
    boolean judgeInterfaceCreateBack(String bizNo);

    /**
     * 判断是否监控发起的事件单
     * @param bizNo
     * @return
     */
    boolean judgeMonitorCreateIncident(String bizNo);

    /**
     * 根据流程实例id判断是否监控发起
     * @param bizNo
     * @return
     */
    boolean judgeMonitorCreateIncidentByInstanceId(String bizNo);

    /**
     * 提供根据工单号查询对应告警接口 itsm
     * @param itsmId
     * @return
     */
	Map<String, Object> getAlertListByItsmId(String itsmId);
	
    /**
     * 根据工单号关闭对应告警接口 itsm
     * @param map
     * @return
     */
	Map<String, Object> closeAlertByItsmId(Map<String, Object> params);

	Map<String, Object> queryEventByState(String jsonData);

	Map<String, Object> queryEventById(String jsonData);

	Map<String, Object> updateEventState(String jsonData);

	Map<String, Object> queryPredict(String jsonData);

    /**
     * 批量关闭查询
     * @param jsonData
     * @return
     */
    Map<String, Object> muliCloseEvent(String jsonData);

    /**
     * 告警批量解决
     * @param code
     * @param taskId
     * @param instanceId
     * @param statusStr
     * @param customerVo
     * @return
     */
    AjaxResult execMuliCloseEvent(String code,String taskId, String instanceId,String statusStr, CustomerVo customerVo);

    AjaxResult selectIncidentListByDealClose(Page page, Map<String, Object>  map);

    void sendEmailMessage(String userId, Map<String, Object> map);

    void sendEmailMessageTimeTask();

    AjaxResult batchDealEvent(String code, String taskId, String instanceId, String statusStr, CustomerVo customerVo);

    Map<String,Object> selectEventByMobile(String jsonData);

    AjaxResult receiveEvent(Map<String,Object> params);

    AjaxResult eventTransfer(Map<String,Object> params);

    AjaxResult solveEvent(Map<String,Object> params);

    void clearCurrentDealByStatus();
}
