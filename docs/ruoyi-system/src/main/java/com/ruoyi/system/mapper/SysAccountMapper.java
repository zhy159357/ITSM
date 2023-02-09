package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysAccount;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SysPost;

import java.util.List;

/**
 * 账户信息 数据层
 * 
 * @author dayong_sun
 */
public interface SysAccountMapper
{
    /**
     * 查询账号数据集合
     * 
     * @param sysAccount 账号信息
     * @return 账号数据集合
     */
    public List<SysAccount> selectAccountList(SysAccount sysAccount);

    /**
     * 查询所有账号
     * 
     * @return 账号列表
     */
    public List<SysAccount> selectAccountAll();

    /**
     * 根据用户ID查询账号
     * 
     * @param userId 用户ID
     * @return 账号列表
     */
    public List<SysAccount> selectAccountsById(Long userId);

    /**
     * 通过账号ID查询账号信息
     * 
     * @param accountId 账号ID
     * @return 账号信息
     */
    public SysAccount selectAccountById(Long accountId);

    /**
     * 批量删除账号信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAccountByIds(Long[] ids);

    /**
     * 批量删除账号用户信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserAccountByIds(Long[] ids);

    /**
     * 修改用户账号信息
     * 
     * @param account 账号信息
     * @return 结果
     */
    public int updateUserAccount(SysAccount account);

    /**
     * 修改账号信息
     *
     * @param account 账号信息
     * @return 结果
     */
    public int updateAccount(SysAccount account);

    /**
     * 新增账号信息
     * 
     * @param account 账号信息
     * @return 结果
     */
    public int insertAccount(SysAccount account);

    /**
     * 新增账号用户信息
     *
     * @param account 账号用户信息
     * @return 结果
     */
    public int insertUserAccount(SysAccount account);

    /**
     * 校验账号名称
     * 
     * @param accountName 账号名称
     * @return 结果
     */
    public SysAccount checkAccountNameUnique(String accountName);

    /**
     * 通过账号ID查询账号使用数量
     *
     * @param accountId 账号ID
     * @return 结果
     */
    public int countUserAccountById(Long accountId);

    /**
     * 根据条件分页查询用户列表
     *
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    public List<SysAccount> selectUserList(SysUser sysUser);

    /**
     * 根据userId查询用户账号关联表
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    public SysAccount selectUserAccountByUserId(SysUser sysUser);

    /**
     * 修改账号信息
     *
     * @param account 用户信息
     * @return 结果
     */
    public int changeStatus(SysAccount account);

    /**
     * 查询岗位数据集合
     *
     * @param post 岗位信息
     * @return 岗位数据集合
     */
    public List<SysPost> selectPostList(SysPost post);

    /**
     * 通过用户ID删除用户和角色关联
     *
     * @param accountId 用户ID
     * @return 结果
     */
    public int deleteAccountPostByAccountId(Long accountId);

    /**
     * 批量绑定岗位账号信息
     *
     * @param list 账号系统列表
     * @return 结果
     */
    public int insertAccountPost(List<SysAccount> list);

    /**
     * 根据账号ID查询岗位
     * @param account 账号ID
     * @return 岗位列表
     */
    public List<SysAccount> selectAccountsByAccountId(SysAccount account);

    /**
     * 通过账号查询用户信息
     *
     * @param accountName 账号名称
     * @return 用户对象信息
     */
    public String selectUserAccountById(String accountName);
}
