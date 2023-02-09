package com.ruoyi.activiti.mapper;


import com.ruoyi.common.core.domain.entity.SmBizLxbgApply;
import com.ruoyi.common.core.domain.entity.SmBizScheduling;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DelayLxbgMapper {


    int insertDelayLxbg(SmBizLxbgApply smBizLxbgApply);


    SmBizLxbgApply selectByScId(String schedulingId);


    int updateSmBizLxbgapply(SmBizLxbgApply smBizLxbgApply);


    List<SmBizLxbgApply> selectSmBizLxbgapplyList(SmBizLxbgApply smBizLxbgApply);

}
