package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysBind;

import java.util.List;

/**
 * 应用系统业务层
 * 
 * @author dayong_sun
 */
public interface ISysBindService
{
    /**
     * 根据条件分页查询角色数据
     * @param bind 应用系统信息
     * @return 角色数据集合信息
     */
    public List<SysBind> selectBindList(SysBind bind);

    /**
     * 查询所有应用系统
     *
     * @return 系统列表
     */
    public List<SysBind> selectBindAll();

    /**
     * 通过角色ID查询角色
     * 
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
    public boolean deleteBindById(Long id);

    /**
     * 批量删除角色用户信息
     * @param ids 需要删除的数据ID
     * @return 结果
     * @throws Exception 异常
     */
    public int deleteBindByIds(String ids) throws Exception;

    /**
     * 新增保存角色信息
     *
     * @param bind 角色信息
     * @return 结果
     */
    public int insertBind(SysBind bind);

    /**
     * 修改保存角色信息
     *
     * @param bind 角色信息
     * @return 结果
     */
    public int updateBind(SysBind bind);


    /**
     * 校验系统名称是否唯一
     *
     * @param bind 角色信息
     * @return 结果
     */
    public String checkBindNameUnique(SysBind bind);

    /**
     * 状态修改
     * 
     * @param bind 角色信息
     * @return 结果
     */
    public int changeStatus(SysBind bind);

    /**
     * 校验角色是否允许操作
     *
     * @param bind 角色信息
     */
    public void checkBindAllowed(SysBind bind);

    /**
     * 根据用户ID查询应用系统
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<SysBind> selectBindsByUserId(Long userId);

}
