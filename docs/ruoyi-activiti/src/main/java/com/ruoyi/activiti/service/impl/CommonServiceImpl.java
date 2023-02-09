package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.VmBizInfoMapper;
import com.ruoyi.activiti.service.ICommonService;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.system.mapper.OgPersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 14735
 */
@Service
public class CommonServiceImpl implements ICommonService {

    @Autowired
    private OgPersonMapper ogPersonMapper;

    @Override
    public List<OgPerson> selectPersonByOrgAndRole (OgPerson person){
        //return ogPersonMapper.selectBusinessAuditList(person);
        return ogPersonMapper.selectOgPersonListByOrgId(person.getOrgId());
    }
}
