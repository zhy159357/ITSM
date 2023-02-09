package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysRole;

import java.util.List;

/**
 * 应用系统业务层
 *
 * @author dayong_sun
 */
public interface ISysAppService {
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
    public boolean deleteAppById(String id);

    /**
     * 批量删除角色用户信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     * @throws Exception 异常
     */
    public int deleteAppByIds(String ids) throws Exception;

    /**
     * 新增保存角色信息
     *
     * @param app 角色信息
     * @return 结果
     */
    public int insertApp(SysApp app);

    /**
     * 修改保存角色信息
     *
     * @param app 角色信息
     * @return 结果
     */
    public int updateApp(SysApp app);


    /**
     * 校验系统名称是否唯一
     *
     * @param app 角色信息
     * @return 结果
     */
    public String checkAppNameUnique(SysApp app);

    /**
     * 状态修改
     *
     * @param app 角色信息
     * @return 结果
     */
    public int changeStatus(SysApp app);

    /**
     * 校验角色是否允许操作
     *
     * @param app 角色信息
     */
    public void checkAppAllowed(SysApp app);

    /**
     * 根据用户ID查询应用系统
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<SysApp> selectAppsByUserId(String userId);

    /**
     * 应用系统绑定的角色
     *
     * @param app
     * @return
     */
    public List<OgRole> selectAppRoleRList(SysApp app);

    public List<OgRole> selectAppRoleLList(SysApp app);

    public int insertSysRole(String appid, String ids);

    public int deleteAppRoleByIds(String appid, String ids);

    public int changeSysOrder(String userId, String id, String value);

    public List<SysApp> selectAppOrderList(SysApp app);
}
