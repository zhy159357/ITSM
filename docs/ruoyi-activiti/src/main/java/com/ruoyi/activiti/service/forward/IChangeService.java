package com.ruoyi.activiti.service.forward;

import com.ruoyi.common.core.Record;
import com.ruoyi.common.core.domain.entity.OgAgency;

/**
 * 软研对接接口
 *
 * @author 14735
 */
public interface IChangeService {

    /**
     * 申请单位/审批机构初始化默认值
     * @return
     */
    OgAgency getLoginAgency(String initiator);
}
