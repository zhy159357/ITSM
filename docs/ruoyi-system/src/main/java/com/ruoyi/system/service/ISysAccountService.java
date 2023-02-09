package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysAccount;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SysPost;

import java.util.List;

/**
 * 账号信息 服务层
 * 
 * @author ruoyi
 */
public interface ISysAccountService
{
    /**
     * 查询账号信息集合
     * 
     * @param account 账号信息
     * @return 账号信息集合
     */
    public List<SysAccount> selectAccountList(SysAccount account);

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
     * @return 角色对象信息
     */
    public SysAccount selectAccountById(Long accountId);

    /**
     * 批量删除账号信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     * @throws Exception 异常
     */
    public int deleteAccountByIds(String ids) throws Exception;

    /**
     * 通过账号ID查询账号使用数量
     *
     * @param accountId 账号ID
     * @return 结果
     */
    public int countUserAccountById(Long accountId);

    /**
     * 新增保存账号信息
     * 
     * @param account 账号信息
     * @return 结果
     */
    public int insertAccount(SysAccount account);

    /**
     * 新增保存账号用户信息
     *
     * @param account 账号用户信息
     * @return 结果
     */
    public int insertUserAccount(SysAccount account);

    /**
     * 修改保存用户账号信息
     * 
     * @param account 账号信息
     * @return 结果
     */
    public int updateUserAccount(SysAccount account);

    /**
     * 修改保存账号信息
     *
     * @param account 账号信息
     * @return 结果
     */
    public int updateAccount(SysAccount account);

    /**
     * 校验账号名称
     * 
     * @param account 账号信息
     * @return 结果
     */
    public String checkAccountNameUnique(SysAccount account);

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysAccount> selectUserList(SysUser user);

    /**
     * 账号状态修改
     *
     * @param account 用户信息
     * @return 结果
     */
    public int changeStatus(SysAccount account);

    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    public List<SysPost> selectPostList(SysPost post);

    /**
     * 账号分配岗位
     *
     * @param accountId 账号ID
     * @param postIds 角色组
     */
    public void insertPostAccount(Long accountId, Long[] postIds);

    /**
     * 根据账号ID查询岗位
     *
     * @param account 账号ID
     * @return 角色列表
     */
    public List<SysPost> selectAccountsByAccountId(SysAccount account);

    /**
     * 通过账号查询用户信息
     *
     * @param accountName 账号名称
     * @return 用户对象信息
     */
    public String selectUserAccountById(String accountName);
}
