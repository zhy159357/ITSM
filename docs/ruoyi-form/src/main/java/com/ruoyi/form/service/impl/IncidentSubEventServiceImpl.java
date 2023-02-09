package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ruoyi.common.core.domain.entity.OgUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.form.domain.IncidentSubEvent;
import com.ruoyi.form.mapper.IncidentSubEventMapper;
import com.ruoyi.form.service.IncidentSubEventService;
import com.ruoyi.framework.interceptor.CustomerBizInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentSubEventServiceImpl implements IncidentSubEventService {

    @Autowired
    private IncidentSubEventMapper incidentSubEventMapper;

    @Override
    public int insertIncidentSubEvent(IncidentSubEvent subEvent) {
        IncidentSubEvent incidentSubEvent = selectIncidentSubEventByEventNo(subEvent.getEventNo());
        if(incidentSubEvent == null) {
            return incidentSubEventMapper.insert(subEvent);
        }
        return 0;
    }

    @Override
    public int updateIncidentSubEvent(IncidentSubEvent subEvent) {
        return incidentSubEventMapper.updateById(subEvent);
    }

    @Override
    public int updateIncidentSubEventByNo(IncidentSubEvent subEvent) {
        UpdateWrapper<IncidentSubEvent> updateWrapper = new UpdateWrapper<>();
        if(StringUtils.isNotEmpty(subEvent.getSolveTime())) {
            // 记录解决人，解决人部门
            OgUser ogUser = CustomerBizInterceptor.currentUserThread.get();
            if(StringUtils.isNotEmpty(ogUser)) {
                subEvent.setSolvePerson(ogUser.getPname());
                subEvent.setSolveOrg(ogUser.getOrgname());
            }
        }
        updateWrapper.eq("event_no", subEvent.getEventNo());
        return incidentSubEventMapper.update(subEvent, updateWrapper);
    }

    @Override
    public List<IncidentSubEvent> selectIncidentSubEventList(IncidentSubEvent subEvent) {
        return incidentSubEventMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public IncidentSubEvent selectIncidentSubEventById(Long id) {
        return incidentSubEventMapper.selectById(id);
    }

    @Override
    public IncidentSubEvent selectIncidentSubEventByEventNo(String eventNo) {
        QueryWrapper<IncidentSubEvent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_no", eventNo);
        return incidentSubEventMapper.selectOne(queryWrapper);
    }

    @Override
    public int updateIncidentSubEvent(String eventNo, String flag) {
        IncidentSubEvent incidentSubEvent = new IncidentSubEvent();
        incidentSubEvent.setEventNo(eventNo);
        incidentSubEvent.setBackCompletionFlag(flag);
        return this.updateIncidentSubEventByNo(incidentSubEvent);
    }
}

