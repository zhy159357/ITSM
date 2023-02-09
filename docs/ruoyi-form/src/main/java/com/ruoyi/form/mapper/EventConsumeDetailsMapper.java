package com.ruoyi.form.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.form.domain.EventConsumeDetails;
import com.ruoyi.form.domain.EventDutyGroup;

public interface EventConsumeDetailsMapper extends BaseMapper<EventConsumeDetails> {

    EventConsumeDetails selectEventConsumeDetailsOneByBizNo(String bizNo);
}
