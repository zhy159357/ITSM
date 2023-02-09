package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SynchronizeUser;

import java.util.List;

/**
 * 用户业务层
 * 
 * @author dayong_sun
 */
public interface ISynchronizeUserService
{
    /**
     * 根据条件分页查询用户数据
     * @return 角色数据集合信息
     */
    public List<SynchronizeUser> selectList();


}
