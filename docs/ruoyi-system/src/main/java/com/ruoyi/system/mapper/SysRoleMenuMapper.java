package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.system.domain.SysRoleMenu;

import java.util.List;

/**
 * 角色与菜单关联表 数据层
 *
 * @author ruoyi
 */
public interface SysRoleMenuMapper {
    /**
     * 通过角色ID删除角色和菜单关联
     *
     * @param roleId 角色ID
     * @return 结果
     */
    public int deleteRoleMenuByRoleId(String roleId);

    /**
     * 批量删除角色菜单关联信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleMenu(Long[] ids);

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    public int selectCountRoleMenuByMenuId(Long menuId);

    /**
     * 批量新增角色菜单信息
     *
     * @param roleMenuList 角色菜单列表
     * @return 结果
     */
    public int batchRoleMenu(List<SysRoleMenu> roleMenuList);

    /**
     * 新增角色菜单信息
     *
     * @param menu 角色菜单
     * @return 结果
     */
    public int insertRoleMenu(SysRoleMenu menu);

    public int insertRoleMenuMysql(SysRoleMenu menu);

    public List<OgRole> getByRole(Long menuId);
}
