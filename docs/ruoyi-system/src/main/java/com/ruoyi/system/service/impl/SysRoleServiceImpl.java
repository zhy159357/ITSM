package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.domain.SysRoleMenu;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.mapper.SysMenuMapper;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.mapper.SysRoleMenuMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import com.ruoyi.system.service.IOgPersonService;
import com.ruoyi.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 角色 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService
{
    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    IOgPersonService iOgPersonService;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     * 根据条件分页查询角色数据
     * 
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    /*@DataScope(deptAlias = "d")*/
    public List<OgRole> selectRoleList(OgRole role)
    {
        return roleMapper.selectRoleList(role);
    }

    /**
     * 根据用户ID查询权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRoleKeys(String userId)
    {
        List<OgRole> perms = roleMapper.selectRolesByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (OgRole perm : perms)
        {
            if (StringUtils.isNotNull(perm))
            {
                permsSet.addAll(Arrays.asList("admin".split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询角色
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<OgRole> selectRolesByUserId(String userId)
    {
        List<OgRole> userRoles = roleMapper.selectRolesByUserId(userId);
      /*  List<OgRole> roles = selectRoleAll();
        for (OgRole role : roles)
        {
            for (SysRole userRole : userRoles)
            {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue())
                {
                    role.setFlag(true);
                    break;
                }
            }
        }*/
        return userRoles;
    }

    /**
     * 查询所有角色
     * 
     * @return 角色列表
     */
    @Override
    public List<OgRole> selectRoleAll()
    {
        return SpringUtils.getAopProxy(this).selectRoleList(new OgRole());
    }

    /**
     * 通过角色ID查询角色
     * 
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public OgRole selectRoleById(String roleId)
    {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     * 通过角色ID查询岗位
     *
     * @param post
     * @return 岗位对象信息
     */
    @Override
    public List<OgPost> selectPostsByRoleId(OgPost post)
    {
        return roleMapper.selectPostsByRoleId(post);
    }

    /**
     * 通过角色ID删除角色
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public boolean deleteRoleById(Long roleId)
    {
        return roleMapper.deleteRoleById(roleId) > 0 ? true : false;
    }

    /**
     * 批量删除角色信息
     * 
     * @param ids 需要删除的数据ID
     * @throws Exception
     */
    @Override
    public int deleteRoleByIds(String ids) throws BusinessException
    {
        String[] roleIds = Convert.toStrArray(ids);
        for (String roleId : roleIds)
        {
            OgRole ogRole = selectRoleById(roleId);
            List<String> menuIds = menuMapper.selectMenuTree(roleId);
            // 校验角色是否关联菜单，如果存在关联关系返回错误信息
            if (!CollectionUtils.isEmpty(menuIds))
            {
                throw new BusinessException("角色["+ogRole.getrName()+"] 拥有功能权限或者某些岗位拥有该角色 不能删除");
            }
            OgPost ogPost = new OgPost();
            ogPost.setRoleId(roleId);
            List<OgPost> ogPosts = selectPostsByRoleId(ogPost);
            // 校验角色是否关联岗位，如果存在关联关系返回错误信息
            if(!CollectionUtils.isEmpty(ogPosts)){
                throw new BusinessException("角色["+ogRole.getrName()+"] 拥有功能权限或者某些岗位拥有该角色 不能删除");
            }
        }
        return roleMapper.deleteRoleByIds(roleIds);
    }

    /**
     * 新增保存角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRole(OgRole role)
    {
        // 新增角色信息
        int rows = roleMapper.insertRole(role);
        // 新增角色时如果有关联menuId才插入中间表，否则不插入
        if(StringUtils.isNotEmpty(role.getMenuIds())){
            return insertRoleMenu(role);
        }
        return rows;
    }

    /**
     * 修改保存角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRole(OgRole role)
    {
        // 修改角色信息
        int rows = roleMapper.updateRole(role);
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRid());
        insertRoleMenu(role);
        return rows;
    }

    /**
     * 修改数据权限信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int authDataScope(OgRole role)
    {
        // 修改角色信息
        roleMapper.updateRole(role);
        // 删除角色与部门关联
        //roleDeptMapper.deleteRoleDeptByRoleId(role.getRid());
        // 新增角色和部门信息（数据权限）
        return insertRoleDept(role);
    }

    /**
     * 新增角色菜单信息
     * 
     * @param role 角色对象
     */
    public int insertRoleMenu(OgRole role)
    {
        int rows = 0;
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (Long menuId : role.getMenuIds())
        {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRid());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0)
        {
            //rows = roleMenuMapper.batchRoleMenu(list);
            for (SysRoleMenu menu : list) {
                if("mysql".equals(dbType)){
                    rows += roleMenuMapper.insertRoleMenuMysql(menu);
                }else{
                    rows += roleMenuMapper.insertRoleMenu(menu);
                }

            }
        }
        return rows;
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    public int insertRoleDept(OgRole role)
    {
        int rows = 1;
        // 新增角色与部门（数据权限）管理
        /*List<SysRoleDept> list = new ArrayList<SysRoleDept>();
        for (Long deptId : role.getDeptIds())
        {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getRoleId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (list.size() > 0)
        {
            rows = roleDeptMapper.batchRoleDept(list);
        }*/
        return rows;
    }

    /**
     * 校验角色名称是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(OgRole role)
    {
        String roleId = StringUtils.isNull(role.getRid()) ? "" : role.getRid();
        OgRole info = null;
        info = roleMapper.checkRoleNameUnique(role.getrName());
        if (StringUtils.isNotNull(info))
        {
            return UserConstants.ROLE_NAME_NOT_UNIQUE;
        }
        return UserConstants.ROLE_NAME_UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleKeyUnique(OgRole role)
    {
        String roleId = StringUtils.isNull(role.getRid()) ? "" : role.getRid();
        OgRole info = roleMapper.checkRoleKeyUnique(role.getRid());
        if (StringUtils.isNotNull(info) && info.getRid().equals(roleId))
        {
            return UserConstants.ROLE_KEY_NOT_UNIQUE;
        }
        return UserConstants.ROLE_KEY_UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     * 
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(OgRole role)
    {
        if (StringUtils.isNotNull(role.getRid()) /*&& role.isAdmin()*/)
        {
            throw new BusinessException("不允许操作超级管理员角色");
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(String roleId)
    {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * 角色状态修改
     * 
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int changeStatus(OgRole role)
    {
        return roleMapper.updateRole(role);
    }

    /**
     * 取消授权用户角色
     * 
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    @Override
    public int deleteAuthUser(SysUserRole userRole)
    {
        return userRoleMapper.deleteUserRoleInfo(userRole);
    }

    /**
     * 批量取消授权用户角色
     * 
     * @param roleId 角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    @Override
    public int deleteAuthUsers(Long roleId, String userIds)
    {
        return userRoleMapper.deleteUserRoleInfos(roleId, Convert.toLongArray(userIds));
    }

    /**
     * 批量选择授权用户角色
     * 
     * @param roleId 角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    @Override
    public int insertAuthUsers(Long roleId, String userIds)
    {
        Long[] users = Convert.toLongArray(userIds);
        // 新增用户与角色管理
        List<SysUserRole> list = new ArrayList<SysUserRole>();
        for (Long userId : users)
        {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return userRoleMapper.batchUserRole(list);
    }

//    //角色页面
//    @Override
//    public List<SysRole> selectRolesByPostId(Long postId) {
//        List<SysRole> postRoles = roleMapper.selectRolesByPostId(postId);
//        List<SysRole> roles = selectRoleAll();
//        for (SysRole role : roles)
//        {
//            for (SysRole postRole : postRoles)
//            {
//                if (role.getRoleId().longValue() == postRole.getRoleId().longValue())
//                {
//                    role.setFlag(true);
//                    break;
//                }
//            }
//        }
//        return roles;
//
//    }



    /**
     *
     * 根据条件分页查询已分配用户角色列表
     * @param role 角色
     * @return
     */
    @Override
    public List<OgRole> selectAllocatedListPost(OgRole role) {

        return roleMapper.selectAllocatedListPost(role);
    }



    @Override
    public List<OgRole> selectUnallocatedListPost(OgRole role) {

        return roleMapper.selectUnallocatedListPost(role);
    }

    @Override
    public String getroleId(String id) {
        return roleMapper.getroleId(id);
    }
    /**
     * 根据roleId查询用户id
     * @param roleId
     * @return
     */
    @Override
    public List<String> selectUserIdByRoleId(String roleId){
        return roleMapper.selectUserIdByRoleId(roleId);
    }

    public List<OgRole> getByRole(Long menuId){
        return roleMenuMapper.getByRole(menuId);
    }

    /**
     * 判断当前登录人是否具有某种角色
     * @param roleId 角色id
     * @param userId 用户id
     * @return
     */
    public boolean judgeHasRole(String roleId, String userId) {
        List<OgRole> ogRoles = this.selectRolesByUserId(userId);
        for(OgRole role : ogRoles) {
            if(role.getRid().equals(roleId)) {
                return true;
            }
        }
        return false;
    }
    public Set<String> selectAllRoleKeys(String userId){
        List<OgRole> perms = roleMapper.selectRolesByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (OgRole perm : perms)
        {
            if (StringUtils.isNotNull(perm))
            {
                permsSet.addAll(Collections.singleton(perm.getRid()));
            }
        }
        return permsSet;
    }
}
