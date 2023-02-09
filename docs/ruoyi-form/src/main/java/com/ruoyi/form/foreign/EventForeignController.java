package com.ruoyi.form.foreign;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.form.domain.CustomerVo;
import com.ruoyi.form.domain.EventForeignVo;
import com.ruoyi.form.domain.EventIncConfirmVo;
import com.ruoyi.form.domain.EventIncCreateVo;
import com.ruoyi.form.domain.EventIncModifyVo;
import com.ruoyi.form.domain.EventIncSearchReqVo;
import com.ruoyi.form.domain.EventIncSearchRespVo;
import com.ruoyi.form.domain.EventMonitorVo;
import com.ruoyi.form.service.EventForeignService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

/**
 * 外接系统对接接口
 */
@RestController
@RequestMapping("/foreign/event")
public class EventForeignController extends BaseController {

    @Autowired
    private EventForeignService eventForeignService;

    @PostMapping("/startProcess")
    @ResponseBody
    public AjaxResult startEventProcess(@RequestBody EventForeignVo vo) {
        return eventForeignService.startEventProcess(vo);
    }

    @PostMapping("/searchEvent")
    @ResponseBody
    public TableDataInfo searchEvent(@RequestBody EventIncSearchReqVo vo) {
        // 查询中的分页参数若为空，默认pageNum=1 pageSize=20
        //PageHelper.startPage(vo.getPageNum() == null ? 1 : vo.getPageNum() , vo.getPageSize() == null ? 20 : vo.getPageSize());
        if(vo.getPageNum() == null) {
            vo.setPageNum(1);
        }
        if(vo.getPageSize() == null) {
            vo.setPageSize(20);
        }
        vo.setPageStart((vo.getPageNum() - 1) * vo.getPageSize());
        String imStatus = vo.getImStatus();
        if(StringUtils.isNotEmpty(imStatus)) {
            vo.setStatus(Arrays.asList(imStatus.split(",")));
        }
        Map<String, Object> resultMap = eventForeignService.searchEvent(vo);
        List<EventIncSearchRespVo> resultList = (List<EventIncSearchRespVo>) resultMap.get("resultList");
        Long count = (Long) resultMap.get("count");
        return getDataTable(resultList, count, "查询成功！");

    }

    @Log(businessType = BusinessType.INSERT)
    @PostMapping("/createEvent")
    @ResponseBody
    public Map<String, Object> createEvent(@RequestBody EventIncCreateVo vo) {
        return eventForeignService.createEvent(vo);
    }

    @Log
    @PostMapping("/addInfoEvent")
    @ResponseBody
    public Map<String, Object> addInfoEvent(@RequestBody EventIncModifyVo vo) {
        return eventForeignService.addInfoEvent(vo);
    }

    @Log
    @PostMapping("/cancelEvent")
    @ResponseBody
    public Map<String, Object> cancelEvent(@RequestBody EventIncModifyVo vo) {
        return eventForeignService.cancelEvent(vo);
    }

    @Log
    @PostMapping("/confirmEvent")
    @ResponseBody
    public Map<String, Object> confirmEvent(@RequestBody EventIncConfirmVo vo) {
        return eventForeignService.confirmEvent(vo);
    }

    @Log
    @PostMapping("/modifyEventPriority")
    @ResponseBody
    public Map<String, Object> modifyEventPriority(@RequestBody EventMonitorVo vo) {
        return eventForeignService.modifyEventPriority(vo);
    }

    @PostMapping("/modifyEventselfHealingState")
    @ResponseBody
    public Map<String, Object> modifyEventselfHealingState(@RequestBody EventMonitorVo vo) {
        return eventForeignService.modifyEventPriority(vo);
    }

    /**
     * ①客服派单更新信息
     * ②客服退回
     * @param params
     * @return
     */
    @PostMapping("/sendMsgTokeFu")
    @ResponseBody
    public AjaxResult sendMsgTokeFu(@RequestBody Map<String, Object> params) {
        String instanceId = (String) params.get("instanceId");
        String status = (String) params.get("status");
        String statusStr = (String) params.get("statusStr");
        eventForeignService.sendMsgTokeFu(instanceId, status, statusStr);
        return AjaxResult.success();
    }
    /**
     * 监控查询列表
     * @param jsonData
     * @return
     */
    @PostMapping("/monitor/getAlertListByItsmId")
    @ResponseBody
    public Map<String, Object> getAlertListByItsmId(@RequestBody String jsonData) {
    	return eventForeignService.getAlertListByItsmId(jsonData);
    }
    /**
     * 监控关闭
     * @param jsonData
     * @return
     */
    @PostMapping("/monitor/closeAlertByItsmId")
    @ResponseBody
    public Map<String, Object> closeAlertByItsmId(@RequestBody Map<String, Object> jsonData) {
    	return eventForeignService.closeAlertByItsmId(jsonData);
    }
    /**
     * E事通查询状态
     * @param jsonData
     * @return
     */
    @PostMapping("/est/queryEventByState")
    @ResponseBody
    public Map<String, Object> queryEventByState(@RequestBody String jsonData) {
    	//workNo\eventStatus\timeszone：
        return eventForeignService.queryEventByState(jsonData);
    }
    /**
     * E事通事件信息
     * @param jsonData
     * @return
     */
    @PostMapping("/est/queryEventById")
    @ResponseBody
    public Map<String, Object> queryEventById(@RequestBody String jsonData) {
    	//eventNo
    	return eventForeignService.queryEventById(jsonData);
    }
    /**
     * E事通更新 反馈内容
     * @param jsonData
     * @return
     */
    @PostMapping("/est/updateEventState")
    @ResponseBody
    public Map<String, Object> updateEventState(@RequestBody String jsonData) {
    	//工单号 eventNo 是否完成 result  0:未解决  1:已解决  反馈内容: feedbackInfo 
    	return eventForeignService.updateEventState(jsonData);
    }
    /**
     * 知识库
     * @param jsonData
     * @return
     */
    @PostMapping("/nlp/queryPredict")
    @ResponseBody
    public Map<String, Object> queryPredict(@RequestBody String jsonData) {
        return eventForeignService.queryPredict(jsonData);
    }
    
    @PostMapping("/muliCloseEvent")
    @ResponseBody
    public Map<String, Object> muliCloseEvent(@RequestBody String jsonData) {
    	return eventForeignService.muliCloseEvent(jsonData);
    }
    @PostMapping("/execMuliCloseEvent")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "code",value = "业务表code",required = true),
        @ApiImplicitParam(name = "taskId",value = "列表任务ID",required = true),
        @ApiImplicitParam(name = "instanceId",value = "流程实例ID",required = true),
        @ApiImplicitParam(name = "statusStr",value = "当前节点按钮的中文含义",required = true),
        @ApiImplicitParam(name = "customerVo",value = "参数对象",paramType = "body",required = true)})
    @ResponseBody
    public AjaxResult execMuliCloseEvent(String code,String taskId, String instanceId,String statusStr, @RequestBody CustomerVo customerVo,@RequestBody String eventNos) {
    	return eventForeignService.execMuliCloseEvent(code,taskId, instanceId,statusStr, customerVo);
    }

    @PostMapping("/sendEmailMessageTimeTask")
    public AjaxResult sendEmailMessageTimeTask() {
        eventForeignService.sendEmailMessageTimeTask();
        return AjaxResult.success();
    }

    /**
     * 移动端查询事件单待办列表
     * @param jsonData
     * @return
     */
    @PostMapping("/selectEventByMobile")
    public Map<String, Object> selectEventByMobile(@RequestBody String jsonData) {
        Map<String, Object> map = eventForeignService.selectEventByMobile(jsonData);
        return map;
    }

    /**
     * 移动端接单
     * @param params
     * @return
     */
    @PostMapping("/receiveEvent")
    public AjaxResult receiveEvent(@RequestBody Map<String, Object> params) {
        AjaxResult ajaxResult = eventForeignService.receiveEvent(params);
        return ajaxResult;
    }

    /**
     * 移动端转派
     * @param params
     * @return
     */
    @PostMapping("/eventTransfer")
    public AjaxResult eventTransfer(@RequestBody Map<String, Object> params) {
        AjaxResult ajaxResult = eventForeignService.eventTransfer(params);
        return ajaxResult;
    }

    /**
     * 移动端解决
     * @param params
     * @return
     */
    @PostMapping("/solveEvent")
    public AjaxResult solveEvent(@RequestBody Map<String, Object> params) {
        AjaxResult ajaxResult = eventForeignService.solveEvent(params);
        return ajaxResult;
    }
}
