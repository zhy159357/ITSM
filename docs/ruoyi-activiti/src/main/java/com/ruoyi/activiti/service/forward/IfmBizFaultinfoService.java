package com.ruoyi.activiti.service.forward;

import com.ruoyi.common.core.BusException;
import com.ruoyi.common.core.Record;

public interface IfmBizFaultinfoService {
    /**
     * 810007-查询业务事件单详情信息
     * @param id
     * @return
     * @throws BusException
     */
    public Record getFmBizFaultinfo(String id) throws BusException;
}
