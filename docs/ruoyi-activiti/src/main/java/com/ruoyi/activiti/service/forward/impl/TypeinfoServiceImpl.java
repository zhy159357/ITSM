package com.ruoyi.activiti.service.forward.impl;

import com.ruoyi.activiti.service.forward.ITypeinfoService;
import com.ruoyi.common.core.BusException;
import com.ruoyi.common.core.ServiceParam;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.esb.data.EsbServiceMapping;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 软研对接接口实现类
 *
 * @author 14735
 */
/**
 * 软研对接接口实现类
 *
 * @author 14735
 */
@Service("ogTypeinfoManager")
@Transactional(rollbackFor = Exception.class)
public class TypeinfoServiceImpl implements ITypeinfoService {

    @Autowired
    private IOgTypeinfoService ogTypeinfoService;

    /**
     *
     * 710004变更分类 点击树加号
     * @param id
     * @return
     * @throws BusException
     */
    @Override
    @EsbServiceMapping
    public List<OgTypeinfo> getChildOgTypeinfos(@ServiceParam(name="typeinfoId")String id, @ServiceParam(name="typeTypeNo")String typeTypeNo)
            throws BusException {

        OgTypeinfo ogTypeinfo=new OgTypeinfo();
        if (StringUtils.isEmpty(id)) {
            ogTypeinfo.setTypeNo(typeTypeNo);
        } else {
            ogTypeinfo.setParentId(id);
        }
        ogTypeinfo.setInvalidationMark("1");
        List<OgTypeinfo> list = ogTypeinfoService.selectOgTypeinfoList(ogTypeinfo);
        for (int i = 0 ; i < list.size() ; i ++) {
            OgTypeinfo typeinfo = ogTypeinfoService.selectOgTypeinfoById(list.get(i).getTypeinfoId());
            list.get(i).setParent(typeinfo);
        }
        return list;
    }
}
