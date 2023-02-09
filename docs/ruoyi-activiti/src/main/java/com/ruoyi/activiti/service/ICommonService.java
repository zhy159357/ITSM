package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.OgPerson;

import java.util.List;

public interface ICommonService {

    /**
     * 根据条件查询审核人员
     * @return
     */
    public List<OgPerson> selectPersonByOrgAndRole(OgPerson person);
}
