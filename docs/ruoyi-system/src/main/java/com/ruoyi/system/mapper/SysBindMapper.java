package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysBind;
import com.ruoyi.common.core.domain.entity.SysRole;

import java.util.List;

/**
 * 角色表 数据层
 * 
 * @author ruoyi
 */
public interface SysBindMapper
{
    /**
     * 根据条件分页查询角色数据
     * 
     * @param bind 应用系统信息
     * @return 角色数据集合信息
     */
    public List<SysBind> selectBindList(SysBind bind);

    /**
     * 通过角色ID查询角色
     * @param id 角色ID
     * @return 角色对象信息
     */
    public SysBind selectById(Long id);

    /**
     * 通过角色ID删除角色
     * 
     * @param id 角色ID
     * @return 结果
     */
    public int deleteBindById(Long id);

    /**
     * 批量角色用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteBindByIds(Long[] ids);

    /**
     * 修改角色信息
     * 
     * @param bind 角色信息
     * @return 结果
     */
    public int updateBind(SysBind bind);

    /**
     * 新增角色信息
     * 
     * @param bind 角色信息
     * @return 结果
     */
    public int insertBind(SysBind bind);

    /**
     * 校验角色名称是否唯一
     * @param bindName 角色名称
     * @return 角色信息
     */
    public SysBind checkBindNameUnique(String bindName);
    
    /**
     * 校验角色权限是否唯一
     * 
     * @param bindKey 角色权限
     * @return 角色信息
     */
    public SysRole checkRoleKeyUnique(String bindKey);

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<SysBind> selectBindsByUserId(Long userId);
}
