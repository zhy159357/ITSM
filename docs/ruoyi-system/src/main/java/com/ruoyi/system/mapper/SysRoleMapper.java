package com.ruoyi.system.mapper;

import java.util.List;

import com.ruoyi.common.core.domain.entity.OgRole;
import com.ruoyi.common.core.domain.entity.SysAccount;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.OgPost;
import com.ruoyi.system.domain.SysPost;

/**
 * 角色表 数据层
 * 
 * @author ruoyi
 */
public interface SysRoleMapper
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
     * @return 角色列表
     */
    public List<OgRole> selectRolesByUserId(String userId);

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
    public int deleteRoleById(Long roleId);

    /**
     * 批量角色用户信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleByIds(String[] ids);

    /**
     * 修改角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    public int updateRole(OgRole role);

    /**
     * 新增角色信息
     * 
     * @param role 角色信息
     * @return 结果
     */
    public int insertRole(OgRole role);

    /**
     * 校验角色名称是否唯一
     * 
     * @param roleName 角色名称
     * @return 角色信息
     */
    public OgRole checkRoleNameUnique(String roleName);
    
    /**
     * 校验角色代码是否唯一
     * 
     * @param rid 色代码
     * @return 角色信息
     */
    public OgRole checkRoleKeyUnique(String rid);


//    public List<SysRole> selectRolesByPostId(Long postId);

    //根据条件分页查询未分配角色的岗位列表
 //   List<SysRole> selectUnallocatedListPost(SysRole role);

//    //角色页面
//    List<SysRole> selectAllocatedListPost(SysRole role);

    //角色页面
    List<OgRole> selectAllocatedListPost(OgRole role);

    /**
     *
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


//    List<SysUser> selectUsersByPostId(Long postId);

}
