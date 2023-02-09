package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysRole;

import java.util.List;
import java.util.Map;

/**
 * 角色表 数据层
 *
 * @author ruoyi
 */
public interface SysAppMapper {
    /**
     * 根据条件分页查询角色数据
     *
     * @param app 应用系统信息
     * @return 角色数据集合信息
     */
    public List<SysApp> selectAppList(SysApp app);

    /**
     * 通过角色ID查询角色
     *
     * @param id 角色ID
     * @return 角色对象信息
     */
    public SysApp selectById(String id);

    /**
     * 通过角色ID删除角色
     *
     * @param id 角色ID
     * @return 结果
     */
    public int deleteAppById(String id);

    /**
     * 批量角色用户信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAppByIds(String[] ids);

    /**
     * 修改角色信息
     *
     * @param app 角色信息
     * @return 结果
     */
    public int updateApp(SysApp app);

    /**
     * 新增角色信息
     *
     * @param app 角色信息
     * @return 结果
     */
    public int insertApp(SysApp app);

    /**
     * 校验角色名称是否唯一
     *
     * @param appName 角色名称
     * @return 角色信息
     */
    public SysApp checkAppNameUnique(String appName);

    /**
     * 校验角色权限是否唯一
     *
     * @param appKey 角色权限
     * @return 角色信息
     */
    public SysRole checkRoleKeyUnique(String appKey);

    /**
     * 根据用户ID查询角色
     *
     * @param userid 用户ID
     * @return 角色列表
     */
    public List<SysApp> selectAppsByUserId(String userid);

    public List<OgRole> selectAppRoleRList(SysApp app);

    public List<OgRole> selectAppRoleLList(SysApp app);

    public int insertSysRole(List<SysApp> list);

    public int deleteAppRoleByIds(Map<String, Object> map);

    public String selectSysOrder(Map<String, Object> map);

    public int insertSysOrder(Map<String, Object> map);

    public int updateSysOrder(Map<String, Object> map);

    public List<SysApp> selectAppOrderList(SysApp app);
}
