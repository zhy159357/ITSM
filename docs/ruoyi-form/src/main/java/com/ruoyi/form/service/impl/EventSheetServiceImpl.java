package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.activiti.bmp.entity.BmpEntity;
import com.ruoyi.activiti.bmp.service.IBmpService;
import com.ruoyi.common.core.domain.entity.OgTypeinfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.form.constants.EventFlowConstants;
import com.ruoyi.form.domain.EventSheet;
import com.ruoyi.form.enums.EventStatusEnum;
import com.ruoyi.form.mapper.EventSheetMapper;
import com.ruoyi.form.service.IEventSheetService;
import com.ruoyi.system.service.IOgTypeinfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 事件单Service业务层处理
 *
 * @author ruoyi
 * @date 2022-06-07
 */
@Service
public class EventSheetServiceImpl extends ServiceImpl<EventSheetMapper, EventSheet> implements IEventSheetService
{
    private final Logger log = LoggerFactory.getLogger(EventSheetServiceImpl.class);

    @Autowired
    private EventSheetMapper eventSheetMapper;

    @Autowired
    private IBmpService bmpService;

    @Autowired
    private IOgTypeinfoService ogTypeinfoService;

    private final String EVENT_SHEET_PROCESS_DEFINITION_KEY = "SjBk";

    /**
     * 查询事件单
     *
     * @param eventId 事件单ID
     * @return 事件单
     */
    @Override
    public EventSheet selectEventSheetById(String eventId)
    {
        return eventSheetMapper.selectEventSheetById(eventId);
    }

    /**
     * 查询事件单列表
     *
     * @param eventSheet 事件单
     * @return 事件单
     */
    @Override
    public List<EventSheet> selectEventSheetList(EventSheet eventSheet)
    {
        List<EventSheet> list = eventSheetMapper.selectEventSheetList(eventSheet);
        if(!CollectionUtils.isEmpty(list)) {
            list = list.stream().map(event -> {
                this.translateEventType(event);
                return event;
            }).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public EventSheet selectEventSheetByNo(String eventNo) {
        QueryWrapper<EventSheet> queryWrapper = new QueryWrapper<EventSheet>().eq("event_no", eventNo);
        return eventSheetMapper.selectOne(queryWrapper);
    }

    /**
     * 新增事件单
     *
     * @param eventSheet 事件单
     * @return 结果
     */
    @Override
    public int insertEventSheet(EventSheet eventSheet)
    {
        eventSheet.setEventId(UUID.getUUIDStr());
        eventSheet.setCreateBy(ShiroUtils.getUserId());
        eventSheet.setCreateTime(DateUtils.getNowDate());
        eventSheet.setEventStatus(EventStatusEnum.CREATED.getCode());
        return eventSheetMapper.insertEventSheet(eventSheet);
    }

    /**
     * 修改事件单
     *
     * @param eventSheet 事件单
     * @return 结果
     */
    @Override
    public int updateEventSheet(EventSheet eventSheet)
    {
        eventSheet.setUpdateTime(DateUtils.getNowDate());
        eventSheet.setUpdateBy(ShiroUtils.getUserId());
        return eventSheetMapper.updateEventSheet(eventSheet);
    }

    /**
     * 删除事件单对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteEventSheetByIds(String ids)
    {
        return eventSheetMapper.deleteEventSheetByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除事件单信息
     *
     * @param eventId 事件单ID
     * @return 结果
     */
    @Override
    public int deleteEventSheetById(String eventId)
    {
        return eventSheetMapper.deleteEventSheetById(eventId);
    }

    @Transactional
    @Override
    public BmpEntity startProcess(String eventId, Map<String,Object> variables) {
        BmpEntity bmpEntity = new BmpEntity();
        bmpEntity.setBusinessKey(eventId);
        bmpEntity.setProcessDefinitionKey(EVENT_SHEET_PROCESS_DEFINITION_KEY);
        bmpEntity.setProcessVariables(variables);
        bmpService.startProcess(bmpEntity);
        return bmpEntity;
    }

    @Transactional
    @Override
    public void cancelEventSheetById(String id) {
        EventSheet eventSheet = this.selectEventSheetById(id);
        if(EventStatusEnum.CREATED.getCode().equals(eventSheet.getEventStatus())) {
            throw new BusinessException("该事件单处于待提交环节，不允许撤销.");
        }
        String userId = ShiroUtils.getUserId();
        if(!userId.equals(eventSheet.getCreateBy())) {
            throw new BusinessException("当前操作人不是事件单发起人，不允许撤销.");
        }
        // 撤销
        bmpService.cancelProcess(id);
        // 更新事件单状态
        EventSheet event = new EventSheet();
        event.setEventId(id);
        event.setEventStatus(EventStatusEnum.CLOSED.getCode());// 已撤销
        event.setUpdateBy(ShiroUtils.getUserId());
        event.setUpdateTime(DateUtils.getNowDate());
        eventSheetMapper.updateEventSheet(event);
    }

    @Override
    public List<EventSheet> selectEventAgencyList(String processDefinitionKey, String description, EventSheet eventSheet) {
        BmpEntity bmpEntity = new BmpEntity();
        bmpEntity.setProcessDefinitionKey(processDefinitionKey);
        bmpEntity.setDescription(description);
        // 是否查询组任务标识
        String groupFlag = (String) eventSheet.getParams().get("groupFlag");
        bmpEntity.setGroupFlag(StringUtils.isNotEmpty(groupFlag) ? Boolean.valueOf(groupFlag) : false);
        List<BmpEntity> userTask = bmpService.getUserTask(bmpEntity);
        if(CollectionUtils.isEmpty(userTask)) {
            return new ArrayList<>();
        }
        List<EventSheet> collect = userTask.stream().map(bmp -> {
            eventSheet.setEventId(bmp.getBusinessKey());
            EventSheet event = this.selectEventSheetByCondition(eventSheet);
            if(event != null) {
                event.getParams().put("taskId", bmp.getTaskId());
                // 设置1，2，3级类型名称
                this.translateEventType(event);
            }
            return event;
        }).filter(item -> {
            return item != null;
        }).sorted(Comparator.comparing(EventSheet::getCreateTime).reversed())
                .collect(Collectors.toList());
        return collect;
    }

    @Transactional
    @Override
    public void assignEvent(EventSheet eventSheet) {
        String assignedGroup = eventSheet.getAssignedGroup();
        BmpEntity bmpEntity = new BmpEntity();
        this.setFlowCommonPrams(bmpEntity, eventSheet);
        bmpEntity.setFlowApprovalKey(EventFlowConstants.EVENT_FLOW_NODE_KEY_RECEIVE);
        bmpEntity.setFlowApprovalValue(assignedGroup);
        bmpService.complete(bmpEntity);
        // 流程执行成功更新状态
        eventSheet.setEventStatus(EventStatusEnum.ASSIGNED.getCode());
        this.updateEventSheet(eventSheet);
    }

    @Transactional
    @Override
    public void receiveEvent(EventSheet eventSheet) {
        BmpEntity bmpEntity = new BmpEntity();
        this.setFlowCommonPrams(bmpEntity, eventSheet);
        bmpEntity.setFlowApprovalKey(EventFlowConstants.EVENT_FLOW_NODE_KEY_DEAL);
        bmpEntity.setFlowApprovalValue(ShiroUtils.getUserId());
        bmpEntity.setFlowConditionKey(EventFlowConstants.EVENT_FLOW_BRANCH_KEY);
        bmpEntity.setFlowConditionValue(EventFlowConstants.EVENT_FLOW_BRANCH_POSITIVE1);
        bmpService.complete(bmpEntity);
        // 流程执行成功更新状态
        eventSheet.setEventStatus(EventStatusEnum.RESOLVED.getCode());
        this.updateEventSheet(eventSheet);
    }

    @Transactional
    @Override
    public void dealEvent(EventSheet eventSheet) {
        BmpEntity bmpEntity = new BmpEntity();
        this.setFlowCommonPrams(bmpEntity, eventSheet);
        bmpEntity.setFlowConditionKey(EventFlowConstants.EVENT_FLOW_BRANCH_KEY);
        EventSheet eventSheetDb = this.selectEventSheetById(eventSheet.getEventId());
        // 根据remark值来判断流程走向
        String flag = eventSheet.getRemark();
        switch (flag) {
            // 组内转发
            case EventFlowConstants.EVENT_FLOW_BRANCH_ZERO:
                bmpEntity.setFlowApprovalKey(EventFlowConstants.EVENT_FLOW_NODE_KEY_RECEIVE);
                bmpEntity.setFlowApprovalValue(eventSheetDb.getAssignedGroup());
                bmpEntity.setFlowConditionValue(EventFlowConstants.EVENT_FLOW_BRANCH_ZERO);
                break;
            case EventFlowConstants.EVENT_FLOW_BRANCH_POSITIVE1:
                bmpEntity.setFlowApprovalKey(EventFlowConstants.EVENT_FLOW_NODE_KEY_PRE_SOLUTION);
                bmpEntity.setFlowApprovalValue(eventSheet.getPreSolutionId());
                bmpEntity.setFlowConditionValue(EventFlowConstants.EVENT_FLOW_BRANCH_POSITIVE1);
                break;
            case EventFlowConstants.EVENT_FLOW_BRANCH_POSITIVE2:
                bmpEntity.setFlowApprovalKey(EventFlowConstants.EVENT_FLOW_NODE_KEY_SOLUTION);
                bmpEntity.setFlowApprovalValue(ShiroUtils.getUserId());
                bmpEntity.setFlowConditionValue(EventFlowConstants.EVENT_FLOW_BRANCH_POSITIVE2);
                break;
        }
        bmpService.complete(bmpEntity);
        // 流程执行成功更新状态
        eventSheet.setEventStatus(EventStatusEnum.CLOSED.getCode());
        this.updateEventSheet(eventSheet);
    }

    @Transactional
    @Override
    public void preSolutionEvent(EventSheet eventSheet) {
        EventSheet _eventSheet = this.selectEventSheetById(eventSheet.getEventId());
        BmpEntity bmpEntity = new BmpEntity();
        this.setFlowCommonPrams(bmpEntity, eventSheet);
        bmpEntity.setFlowConditionKey(EventFlowConstants.EVENT_FLOW_BRANCH_KEY);
        // 根据remark值来判断流程分支走向
        String flag = eventSheet.getRemark();
        switch (flag) {
            // 退回补全
            case EventFlowConstants.EVENT_FLOW_BRANCH_MINUS2:
                bmpEntity.setFlowConditionValue(EventFlowConstants.EVENT_FLOW_BRANCH_MINUS2);
                bmpEntity.setFlowApprovalKey(EventFlowConstants.EVENT_FLOW_NODE_KEY_BACK_CREATE);
                bmpEntity.setFlowApprovalValue(_eventSheet.getCreateBy());
                eventSheet.setEventStatus(EventStatusEnum.COMPLETION.getCode());
                eventSheet.setBackFlag(EventFlowConstants.BACK_TWO_LINE_BACK);
                break;
            // 退回服务台
            case EventFlowConstants.EVENT_FLOW_BRANCH_MINUS1:
                bmpEntity.setFlowConditionValue(EventFlowConstants.EVENT_FLOW_BRANCH_MINUS1);
                bmpEntity.setFlowApprovalKey(EventFlowConstants.EVENT_FLOW_NODE_KEY_ASSIGN);
                bmpEntity.setFlowApprovalValue(_eventSheet.getAssignedGroup());
                eventSheet.setEventStatus(EventStatusEnum.ASSIGNED.getCode());
                break;
            // 退回一线处理环节
            case EventFlowConstants.EVENT_FLOW_BRANCH_ZERO:
                bmpEntity.setFlowConditionValue(EventFlowConstants.EVENT_FLOW_BRANCH_ZERO);
                bmpEntity.setFlowApprovalKey(EventFlowConstants.EVENT_FLOW_NODE_KEY_DEAL);
                bmpEntity.setFlowApprovalValue(_eventSheet.getDealId());
                eventSheet.setEventStatus(EventStatusEnum.RESOLVED.getCode());
                break;
            // 二线解决通过提交到一线解决
            case EventFlowConstants.EVENT_FLOW_BRANCH_POSITIVE1:
                bmpEntity.setFlowConditionValue(EventFlowConstants.EVENT_FLOW_BRANCH_POSITIVE1);
                bmpEntity.setFlowApprovalKey(EventFlowConstants.EVENT_FLOW_NODE_KEY_SOLUTION);
                bmpEntity.setFlowApprovalValue(_eventSheet.getDealId());
                eventSheet.setSolutionId(_eventSheet.getDealId());
                eventSheet.setEventStatus(EventStatusEnum.RESOLVED.getCode());
                break;
        }
        bmpService.complete(bmpEntity);
        this.updateEventSheet(eventSheet);
    }

    @Override
    public void solutionEvent(EventSheet eventSheet) {
        EventSheet _eventSheet = this.selectEventSheetById(eventSheet.getEventId());
        BmpEntity bmpEntity = new BmpEntity();
        this.setFlowCommonPrams(bmpEntity, eventSheet);
        bmpEntity.setFlowConditionKey(EventFlowConstants.EVENT_FLOW_BRANCH_KEY);
        String flag = eventSheet.getRemark();
        switch (flag) {
            // 退回补全
            case EventFlowConstants.EVENT_FLOW_BRANCH_MINUS1:
                bmpEntity.setFlowConditionValue(EventFlowConstants.EVENT_FLOW_BRANCH_MINUS1);
                bmpEntity.setFlowApprovalKey(EventFlowConstants.EVENT_FLOW_NODE_KEY_BACK_CREATE);
                bmpEntity.setFlowApprovalValue(_eventSheet.getCreateBy());
                eventSheet.setEventStatus(EventStatusEnum.COMPLETION.getCode());
                eventSheet.setBackFlag(EventFlowConstants.BACK_ONE_LINE_BACK);
                break;
            // 退回服务台
            case EventFlowConstants.EVENT_FLOW_BRANCH_ZERO:
                bmpEntity.setFlowConditionValue(EventFlowConstants.EVENT_FLOW_BRANCH_ZERO);
                bmpEntity.setFlowApprovalKey(EventFlowConstants.EVENT_FLOW_NODE_KEY_ASSIGN);
                bmpEntity.setFlowApprovalValue(_eventSheet.getAssignedGroup());
                eventSheet.setEventStatus(EventStatusEnum.ASSIGNED.getCode());
                break;
            // 结束
            case EventFlowConstants.EVENT_FLOW_BRANCH_POSITIVE1:
                bmpEntity.setFlowConditionValue(EventFlowConstants.EVENT_FLOW_BRANCH_POSITIVE1);
                eventSheet.setEventStatus(EventStatusEnum.CLOSED.getCode());
                break;
            // 关闭评价
            case EventFlowConstants.EVENT_FLOW_BRANCH_POSITIVE2:
                bmpEntity.setFlowConditionValue(EventFlowConstants.EVENT_FLOW_BRANCH_POSITIVE2);
                bmpEntity.setFlowApprovalKey(EventFlowConstants.EVENT_FLOW_NODE_KEY_CLOSE);
                bmpEntity.setFlowApprovalValue(StringUtils.isEmpty(eventSheet.getCloseId()) ? ShiroUtils.getUserId() : eventSheet.getCloseId());
                eventSheet.setEventStatus(EventStatusEnum.CLOSED.getCode());
                break;
        }
        bmpService.complete(bmpEntity);
        this.updateEventSheet(eventSheet);
    }

    @Override
    public void evaluateEvent(EventSheet eventSheet) {
        BmpEntity bmpEntity = new BmpEntity();
        this.setFlowCommonPrams(bmpEntity, eventSheet);
        bmpService.complete(bmpEntity);
        eventSheet.setEventStatus(EventStatusEnum.CLOSED.getCode());
        this.updateEventSheet(eventSheet);
    }


    /**
     * 查询待关闭的事件单
     *
     * @return
     */
    @Override
    public List<EventSheet> selectEventAutoCloseList() {
        return eventSheetMapper.selectEventAutoCloseList();
    }

    /**
     * 事件单自动关闭
     *
     * @param event
     */
    @Override
    public void updateEvent(EventSheet event) {
        eventSheetMapper.updateEvent(event);
    }

    @Override
    public void supplyEvent(EventSheet eventSheet) {
        BmpEntity bmpEntity = new BmpEntity();
        this.setFlowCommonPrams(bmpEntity, eventSheet);
        // TODO 此处需要判断流程是否走分派环节逻辑
//        variables.put("reCode", "0");
//        String roleId = "1000";// IT服务台角色id
//        variables.put("receptionId", roleId);
        EventSheet eventSheetDb = this.selectEventSheetById(eventSheet.getEventId());
        String backFlag = eventSheetDb.getBackFlag();
        bmpEntity.setFlowConditionKey(EventFlowConstants.EVENT_FLOW_BRANCH_KEY);
        if(StringUtils.isEmpty(backFlag)) {
            bmpEntity.setFlowConditionValue(EventFlowConstants.EVENT_FLOW_BRANCH_ZERO);
            bmpEntity.setFlowApprovalKey(EventFlowConstants.EVENT_FLOW_NODE_KEY_ASSIGN);
            bmpEntity.setFlowApprovalValue("1000");// 现阶段默认都是走的分派环节，指定分派环节的角色id为1000
        } else if(EventFlowConstants.BACK_ONE_LINE_BACK.equals(backFlag)) {
            // 一线退回补全
            bmpEntity.setFlowConditionValue(EventFlowConstants.EVENT_FLOW_BRANCH_MINUS2);
            bmpEntity.setFlowApprovalKey(EventFlowConstants.EVENT_FLOW_NODE_KEY_SOLUTION);
            bmpEntity.setFlowApprovalValue(eventSheetDb.getSolutionId());
        } else if(EventFlowConstants.BACK_TWO_LINE_BACK.equals(backFlag)) {
            // 二线退回补全
            bmpEntity.setFlowConditionValue(EventFlowConstants.EVENT_FLOW_BRANCH_MINUS1);
            bmpEntity.setFlowApprovalKey(EventFlowConstants.EVENT_FLOW_NODE_KEY_PRE_SOLUTION);
            bmpEntity.setFlowApprovalValue(eventSheetDb.getPreSolutionId());
        }

        if(StringUtils.isEmpty(bmpEntity.getComment())) {
            bmpEntity.setComment("事件信息补全");
        }
        bmpService.complete(bmpEntity);
        eventSheet.setEventStatus(EventStatusEnum.ASSIGNED.getCode());
        this.updateEventSheet(eventSheet);
    }

    @Override
    public void translateEventType(EventSheet eventSheet) {
        String[] typeArr = new String[3];
        List<OgTypeinfo> ogTypeInfoList = ogTypeinfoService.selectOgTypeinfoList(new OgTypeinfo());
        if(StringUtils.isNotEmpty(eventSheet.getEventFirstType())) {
            /*OgTypeinfo ogTypeinfo = ogTypeinfoService.selectOgTypeInfoByTypeNo(eventSheet.getEventFirstType());
            if(ogTypeinfo != null) {
                typeArr[0] = ogTypeinfo.getTypeName();
            }*/
            ogTypeInfoList.forEach(ogTypeInfo -> {
                if(eventSheet.getEventFirstType().equals(ogTypeInfo.getTypeNo())) {
                    typeArr[0] = ogTypeInfo.getTypeName();
                }
            });
        }
        if(StringUtils.isNotEmpty(eventSheet.getEventSecondType())) {
            ogTypeInfoList.forEach(ogTypeInfo -> {
                if(eventSheet.getEventSecondType().equals(ogTypeInfo.getTypeNo())) {
                    typeArr[1] = ogTypeInfo.getTypeName();
                }
            });
        }
        if(StringUtils.isNotEmpty(eventSheet.getEventThreeType())) {
            ogTypeInfoList.forEach(ogTypeInfo -> {
                if(eventSheet.getEventThreeType().equals(ogTypeInfo.getTypeNo())) {
                    typeArr[2] = ogTypeInfo.getTypeName();
                }
            });
        }
        if(StringUtils.isNotEmpty(typeArr)) {
            eventSheet.setEventFirstTypeName(typeArr[0]);
            eventSheet.setEventSecondTypeName(typeArr[1]);
            eventSheet.setEventThreeTypeName(typeArr[2]);
        }
        this.transEventCategory(ogTypeInfoList, eventSheet);
    }

    // 翻译事件单分类
    public void transEventCategory(List<OgTypeinfo> ogTypeInfoList, EventSheet eventSheet) {
        String[] categoryArr = new String[4];
        // 翻译类别
        if(StringUtils.isNotEmpty(eventSheet.getEventCategory())) {
            ogTypeInfoList.forEach(ogTypeInfo -> {
                if(eventSheet.getEventCategory().equals(ogTypeInfo.getTypeNo())) {
                    categoryArr[0] = ogTypeInfo.getTypeName();
                }
            });
        }
        // 翻译子类
        if(StringUtils.isNotEmpty(eventSheet.getEventSubclass())) {
            ogTypeInfoList.forEach(ogTypeInfo -> {
                if(eventSheet.getEventSubclass().equals(ogTypeInfo.getTypeNo())) {
                    categoryArr[1] = ogTypeInfo.getTypeName();
                }
            });
        }
        // 翻译条目
        if(StringUtils.isNotEmpty(eventSheet.getEventEntry())) {
            ogTypeInfoList.forEach(ogTypeInfo -> {
                if(eventSheet.getEventEntry().equals(ogTypeInfo.getTypeNo())) {
                    categoryArr[2] = ogTypeInfo.getTypeName();
                }
            });
        }
        // 翻译子条目
        if(StringUtils.isNotEmpty(eventSheet.getEventSubentry())) {
            ogTypeInfoList.forEach(ogTypeInfo -> {
                if(eventSheet.getEventSubentry().equals(ogTypeInfo.getTypeNo())) {
                    categoryArr[3] = ogTypeInfo.getTypeName();
                }
            });
        }
        if(StringUtils.isNotEmpty(categoryArr)) {
            eventSheet.setEventCategoryName(categoryArr[0]);
            eventSheet.setEventSubclassName(categoryArr[1]);
            eventSheet.setEventEntryName(categoryArr[2]);
            eventSheet.setEventSubentryName(categoryArr[3]);
        }
    }

    /**
     * 设置流程公用参数
     * @param bmpEntity
     * @param eventSheet
     */
    public void setFlowCommonPrams(BmpEntity bmpEntity, EventSheet eventSheet) {
        bmpEntity.setBusinessKey(eventSheet.getEventId());
        bmpEntity.setTaskId((String) eventSheet.getParams().get("taskId"));
        bmpEntity.setComment((String) eventSheet.getParams().get("comment"));
        bmpEntity.setUserId(ShiroUtils.getUserId());
        bmpEntity.setProcessDefinitionKey(EVENT_SHEET_PROCESS_DEFINITION_KEY);
    }

    private EventSheet selectEventSheetByCondition(EventSheet eventSheet) {
        // 这里拼装其他的查询条件
        EventSheet event = eventSheetMapper.selectEventSheetByCondition(eventSheet);
        return event;
    }

}
