package com.ruoyi.form.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.form.domain.EventDutyGroup;
import com.ruoyi.form.mapper.EventDutyGroupMapper;
import com.ruoyi.form.service.EventDutyGroupService;
import org.springframework.stereotype.Service;

@Service
public class EventDutyGroupServiceImpl extends ServiceImpl<EventDutyGroupMapper, EventDutyGroup> implements EventDutyGroupService {

    @Override
    public EventDutyGroup selectEventDutyGroupByName(String groupName) {
        EventDutyGroup dutyGroup = baseMapper.selectOne(new QueryWrapper<EventDutyGroup>().eq("duty_group_name", groupName));
        return dutyGroup;
    }
}
