package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SynchronizeUser;

import java.util.List;

/**
 * 角色表 数据层
 * 
 * @author ruoyi
 */
public interface SynchronizeUserMapper
{
    /**
     * 根据条件分页查询角色数据
     * @return 角色数据集合信息
     */
    public List<SynchronizeUser> selectList();


}
