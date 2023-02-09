package com.ruoyi.activiti.service.forward.impl;

import com.ruoyi.activiti.service.forward.IChangeService;
import com.ruoyi.common.core.ServiceParam;
import com.ruoyi.common.core.domain.entity.OgAgency;
import com.ruoyi.common.core.domain.entity.OgOrg;
import com.ruoyi.common.core.domain.entity.OgPerson;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.esb.data.EsbServiceMapping;

/**
 * 软研对接接口实现类
 *
 * @author 14735
 */
@Service("ChangeManager")
@Transactional(rollbackFor = Exception.class)
public class ChangeServiceImpl implements IChangeService {

    @Autowired
    private IOgPersonService ogPersonService;

    @Autowired
    ISysDeptService sysDeptService;

    /**
     * 710009
     * 申请单位/审批机构初始化默认值
     * @return
     */
    @Override
    @EsbServiceMapping
    public OgAgency getLoginAgency(@ServiceParam(name = "initiator", pubProperty = "userId") String initiator){
        OgPerson p = ogPersonService.selectOgPersonById(initiator);
        OgOrg ogOrg = sysDeptService.selectDeptById(p.getOrgId());

        OgAgency ogAgencyParent = new OgAgency();
        OgOrg ogOrgParent = sysDeptService.selectDeptById(ogOrg.getOrgId());
        ogAgencyParent.setOrgid(ogOrgParent.getOrgId());
        ogAgencyParent.setOrgcode(ogOrgParent.getOrgCode());
        ogAgencyParent.setOrgname(ogOrgParent.getOrgName());
        ogAgencyParent.setOrglv(ogOrgParent.getOrgLv());
        ogAgencyParent.setLevelCode(ogOrgParent.getLevelCode());

        //机构
        OgAgency ogAgency = new OgAgency();
        ogAgency.setOrgid(ogOrg.getOrgId());
        ogAgency.setOrgcode(ogOrg.getOrgCode());
        ogAgency.setOrgname(ogOrg.getOrgName());
        ogAgency.setParent(ogAgencyParent);
        ogAgency.setOrglv(ogOrg.getOrgLv());
        ogAgency.setLevelCode(ogOrg.getLevelCode());
        ogAgency.setInvalidationMark(ogOrg.getInvalidationMark());
        return ogAgency;
    }
}
