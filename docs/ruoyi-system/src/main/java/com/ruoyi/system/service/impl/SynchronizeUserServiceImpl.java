package com.ruoyi.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.core.domain.entity.SynchronizeUser;
import com.ruoyi.system.mapper.SynchronizeUserMapper;
import com.ruoyi.system.service.ISynchronizeUserService;

/**
 * 系统业务层处理
 *
 * @author dayong_sun
 */

@Service
public class SynchronizeUserServiceImpl implements ISynchronizeUserService
{
    @Autowired
    private SynchronizeUserMapper synchronizeUserMapper;

    /**
     * 查询所有系统
     *
     * @return 系统列表
     */
    @Override
    public List<SynchronizeUser> selectList()
    {
        return synchronizeUserMapper.selectList();
    }

}
