package com.ruoyi.form.service;

import com.ruoyi.form.domain.ChangeTaskOrg;

import java.util.List;

public interface IChangeTaskOrgService {
    int insert(ChangeTaskOrg changeTaskOrg);
    int updateById(ChangeTaskOrg changeTaskOrg);
    List<ChangeTaskOrg> getAll();
    ChangeTaskOrg getByOrgName(String orgName);
    ChangeTaskOrg getByOrgid(String orgId);
}
