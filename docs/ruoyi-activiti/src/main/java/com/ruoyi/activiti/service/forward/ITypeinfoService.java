package com.ruoyi.activiti.service.forward;

import com.ruoyi.common.core.BusException;
import com.ruoyi.common.core.domain.entity.OgTypeinfo;

import java.util.List;

/**
 * 软研对接接口
 *
 * @author 14735
 */
public interface ITypeinfoService {

    /**
     * 变更分类 点击树加号
     * @param id
     * @return
     * @throws BusException
     */
    List<OgTypeinfo> getChildOgTypeinfos(String id, String typeTypeNo)
            throws BusException;
}
