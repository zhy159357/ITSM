package com.ruoyi.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.form.domain.EventDutyGroup;

public interface EventDutyGroupService extends IService<EventDutyGroup> {

    EventDutyGroup selectEventDutyGroupByName(String groupName);
}
