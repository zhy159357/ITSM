package com.ruoyi.activiti.service.forward;

import com.ruoyi.common.core.BusException;
import com.ruoyi.common.core.ServiceParam;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.Record;
import com.ruoyi.common.core.domain.entity.OgAgency;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.esb.data.EsbServiceMapping;

import java.util.List;
import java.util.Map;


/**
 * 软研对接接口
 *
 * @author 14735
 */
public interface IAgencyService {

    /**
     * 获取登录人的二级机构树
     * 如果登录人属于一级机构，返回整个机构树
     * @return
     * @throws BusException
     */
    List<OgAgency> getLvTAgencysTree(String id, String initiator);

    /**
     * 根据上级查找下一级机构（用于机构树展示）- 无权限认证
     * @param id initiator
     * @return
     * @throws BusException
     */
    List<OgAgency> getChildOgAgencys(String id, String initiator) throws BusException;
    /**
     * 810002
     * @param issuefxId
     * @return
     * @throws BusException
     */
    @EsbServiceMapping
    public OgOrg getZxAll(String issuefxId)throws BusException;
}
