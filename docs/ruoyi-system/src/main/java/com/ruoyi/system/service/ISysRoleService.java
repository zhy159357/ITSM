package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.domain.SysUserRole;

import java.util.List;
import java.util.Set;

/**
 * 角色业务层
 * 
 * @author ruoyi
 */
public interface ISysRoleService
{
    /**
     * 根据条件分页查询角色数据
     * 
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    public List<OgRole> selectRoleList(OgRole role);

    /**
     * 根据用户ID查询角色
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectRoleKeys(String userId);

    /**
     * 根据用户ID查询角色
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<OgRole> selectRolesByUserId(String userId);

    /**
     * 查询所有角色
     * 
     * @return 角色列表
     */
    public List<OgRole> selectRoleAll();

    /**
     * 通过角色ID查询角色
     * 
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    public OgRole selectRoleById(String roleId);

    /**
     * 通过角色ID查询岗位
     *
     * @param post
     * @return 岗位对象信息
     */
    public List<OgPost> selectPostsByRoleId(OgPost post);

    /**
     * 通过角色ID删除角色
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public boolean deleteRoleById(Long roleId);

    /**
     * 批量删除角色用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     * @throws Exception 异常
     */
    public int deleteRoleByIds(String ids) throws Exception;

    /**
     * 新增保存角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    public int insertRole(OgRole role);

    /**
     * 修改保存角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    public int updateRole(OgRole role);

    /**
     * 修改数据权限信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    public int authDataScope(OgRole role);

    /**
     * 校验角色名称是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    public String checkRoleNameUnique(OgRole role);

    /**
     * 校验角色权限是否唯一
     * 
     * @param role 角色信息
     * @return 结果
     */
    public String checkRoleKeyUnique(OgRole role);

    /**
     * 校验角色是否允许操作
     * 
     * @param role 角色信息
     */
    public void checkRoleAllowed(OgRole role);

    /**
     * 通过角色ID查询角色使用数量
     * 
     * @param roleId 角色ID
     * @return 结果
     */
    public int countUserRoleByRoleId(String roleId);

    /**
     * 角色状态修改
     * 
     * @param role 角色信息
     * @return 结果
     */
    public int changeStatus(OgRole role);

    /**
     * 取消授权用户角色
     * 
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    public int deleteAuthUser(SysUserRole userRole);

    /**
     * 批量取消授权用户角色
     * 
     * @param roleId 角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public int deleteAuthUsers(Long roleId, String userIds);

    /**
     * 批量选择授权用户角色
     * 
     * @param roleId 角色ID
     * @param userIds 需要删除的用户数据ID
     * @return 结果
     */
    public int insertAuthUsers(Long roleId, String userIds);

//    //角色页
//    List<SysRole> selectRolesByPostId(Long postId);

    //查询未分配岗位的角色列表
   // List<SysRole> selectUnallocatedListPost(SysRole role);


//    /**
//     *
//     * @param role 角色
//     * @return
//     */
//    List<SysRole> selectAllocatedListPost(SysRole role);


    /**
     *
     * @param role
     * @return
     */
    List<OgRole> selectAllocatedListPost(OgRole role);



    /**
     *  //查询未分配岗位的角色列表
     * @param role
     * @return
     */
    List<OgRole> selectUnallocatedListPost(OgRole role);

    String getroleId(String id);
    /**
     * 根据roleId查询用户id
     * @param roleId
     * @return
     */
    public List<String> selectUserIdByRoleId(String roleId);

    public List<OgRole> getByRole(Long menuId);

    boolean judgeHasRole(String roleId, String userId);

    Set<String> selectAllRoleKeys(String userId);
}
