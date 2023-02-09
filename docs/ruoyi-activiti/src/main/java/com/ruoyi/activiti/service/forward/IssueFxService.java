package com.ruoyi.activiti.service.forward;

import com.ruoyi.common.core.BusException;
import com.ruoyi.common.core.Record;
import com.ruoyi.common.netty.utils.Pager;
import com.ruoyi.common.netty.utils.PagerRecords;

import java.util.List;
import java.util.Map;

public interface IssueFxService {
    /**
     *  810004-#查询关联业务事件单
     * @param pager
     * @param params
     * @return
     * @throws BusException
     */
    PagerRecords getfmFmBizOrFmJk(Pager pager, Map<String,Object> params) throws BusException;
    /**
     *  810006-查询关联版本单
     * @param issueFxId
     * @return
     * @throws BusException
     */
    public List<Record> getVmBizByFoNo(String issueFxId) throws BusException;
    /**
     * 810018-- 查询转发次数
     * @param bizId
     * @return
     * @throws BusException
     */
    public String ifMultiCount(String bizId) throws BusException;
}
