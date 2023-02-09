package com.ruoyi.form.service.impl;

import com.ruoyi.form.domain.ChangeTaskOrg;
import com.ruoyi.form.mapper.IChangeTaskOrgMapper;
import com.ruoyi.form.service.IChangeTaskOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChangeTaskOrgServiceImpl implements IChangeTaskOrgService {

    @Autowired
    IChangeTaskOrgMapper changeTaskOrgMapper;

    @Override
    public int insert(ChangeTaskOrg changeTaskOrg) {
        return changeTaskOrgMapper.insert(changeTaskOrg);
    }

    @Override
    public int updateById(ChangeTaskOrg changeTaskOrg) {
        return changeTaskOrgMapper.updateById(changeTaskOrg);
    }

    @Override
    public List<ChangeTaskOrg> getAll() {
        return changeTaskOrgMapper.getAll();
    }

    @Override
    public ChangeTaskOrg getByOrgName(String orgName) {
        return changeTaskOrgMapper.getByOrgName(orgName);
    }

    @Override
    public ChangeTaskOrg getByOrgid(String orgId) {
        return changeTaskOrgMapper.getByOrgid(orgId);
    }

}
