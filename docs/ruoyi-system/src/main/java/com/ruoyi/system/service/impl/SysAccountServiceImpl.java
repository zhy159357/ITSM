package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysAccount;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.mapper.SysAccountMapper;
import com.ruoyi.system.mapper.SysPostMapper;
import com.ruoyi.system.service.ISysAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 账号信息 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysAccountServiceImpl implements ISysAccountService
{
    @Autowired
    private SysAccountMapper sysAccountMapper;

    @Autowired
    private SysPostMapper sysPostMapper;

    /**
     * 查询账号信息集合
     * 
     * @param sysAccount 账号信息
     * @return 账号信息集合
     */
    @Override
    public List<SysAccount> selectAccountList(SysAccount sysAccount)
    {
        return sysAccountMapper.selectAccountList(sysAccount);
    }

    /**
     * 查询所有账号
     * 
     * @return 账号列表
     */
    @Override
    public List<SysAccount> selectAccountAll()
    {
        return sysAccountMapper.selectAccountAll();
    }

    /**
     * 根据用户ID查询账号
     * 
     * @param id 用户ID
     * @return 账号列表
     */
    @Override
    public List<SysAccount> selectAccountsById(Long id)
    {
        List<SysAccount> accountPosts = sysAccountMapper.selectAccountsById(id);
        List<SysAccount> accounts = sysAccountMapper.selectAccountAll();
        for (SysAccount sysAccount : accounts)
        {
            for (SysAccount accountRole : accountPosts)
            {
                if (sysAccount.getPostId().longValue() == accountRole.getPostId().longValue())
                {
                    sysAccount.setFlag(true);
                    break;
                }
            }
        }
        return accounts;
    }

    /**
     * 通过账号ID查询账号信息
     * 
     * @param accountId 账号ID
     * @return 角色对象信息
     */
    @Override
    public SysAccount selectAccountById(Long accountId)
    {
        return sysAccountMapper.selectAccountById(accountId);
    }

    /**
     * 批量删除账号信息
     * 
     * @param ids 需要删除的数据ID
     * @throws Exception
     */
    @Override
    public int deleteAccountByIds(String ids) throws BusinessException
    {
        Long[] accountIds = Convert.toLongArray(ids);
        sysAccountMapper.deleteUserAccountByIds(accountIds);
        return sysAccountMapper.deleteAccountByIds(accountIds);
    }

    /**
     * 通过账号ID查询账号使用数量
     *
     * @param accountId 账号ID
     * @return 结果
     */
    @Override
    public int countUserAccountById(Long accountId)
    {
        return sysAccountMapper.countUserAccountById(accountId);
    }

    /**
     * 新增保存账号信息
     * 
     * @param account 账号信息
     * @return 结果
     */
    @Override
    public int insertAccount(SysAccount account)
    {
        return sysAccountMapper.insertAccount(account);
    }

    /**
     * 新增保存账号用户信息
     *
     * @param account 账号用户信息
     * @return 结果
     */
    @Override
    public int insertUserAccount(SysAccount account)
    {
        return sysAccountMapper.insertUserAccount(account);
    }

    /**
     * 修改保存用户账号信息
     * 
     * @param account 账号信息
     * @return 结果
     */
    @Override
    public int updateUserAccount(SysAccount account)
    {
        return sysAccountMapper.updateUserAccount(account);
    }

    /**
     * 修改保存账号信息
     *
     * @param account 账号信息
     * @return 结果
     */
    @Override
    public int updateAccount(SysAccount account)
    {
        return sysAccountMapper.updateAccount(account);
    }

    /**
     * 校验账号名称是否唯一
     * 
     * @param account 账号信息
     * @return 结果
     */
    @Override
    public String checkAccountNameUnique(SysAccount account)
    {
        Long accountId = StringUtils.isNull(account.getId()) ? -1L : account.getId();
        SysAccount info = sysAccountMapper.checkAccountNameUnique(account.getAccountName());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != accountId.longValue())
        {
            return UserConstants.ACCOUNT_NAME_NOT_UNIQUE;
        }
        return UserConstants.ACCOUNT_NAME_UNIQUE;
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysAccount> selectUserList(SysUser user)
    {
        List<SysAccount> accountList = sysAccountMapper.selectUserList(user);
        SysAccount account = new SysAccount();
        if (null != user.getUserId()) {
            account = sysAccountMapper.selectUserAccountByUserId(user);
        }
        for (SysAccount sysAccount : accountList)
        {
            if (null != account.getUserId() && account.getUserId().longValue() == sysAccount.getUserId().longValue())
            {
                sysAccount.setFlag(true);
                break;
            }
        }
        return accountList;
    }

    /**
     * 用户状态修改
     *
     * @param account 用户信息
     * @return 结果
     */
    @Override
    public int changeStatus(SysAccount account)
    {
        return sysAccountMapper.changeStatus(account);
    }

    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    @Override
    public List<SysPost> selectPostList(SysPost post)
    {
        return sysAccountMapper.selectPostList(post);
    }

    /**
     * 用户授权角色
     *
     * @param accountId 用户ID
     * @param postIds 角色组
     */
    @Override
    public void insertPostAccount(Long accountId, Long[] postIds)
    {
        sysAccountMapper.deleteAccountPostByAccountId(accountId);
        insertAccountPost(accountId, postIds);
    }

    /**
     * 新增用户角色信息
     *
     * @param postIds 用户对象
     */
    public void insertAccountPost(Long accountId, Long[] postIds)
    {
        if (StringUtils.isNotNull(postIds))
        {
            // 新增用户与角色管理
            List<SysAccount> list = new ArrayList<>();
            for (Long postId : postIds)
            {
                SysAccount account = new SysAccount();
                account.setId(accountId);
                account.setPostId(postId);
                list.add(account);
            }
            if (list.size() > 0)
            {
                sysAccountMapper.insertAccountPost(list);
            }
        }
    }

    /**
     * 根据账号ID查询岗位
     * @param account 账号ID
     * @return 岗位列表
     */
    @Override
    public List<SysPost> selectAccountsByAccountId(SysAccount account)
    {
        SysPost sysPost = new SysPost();
        sysPost.setPostName(account.getPostName());
        List<SysAccount> accountList = sysAccountMapper.selectAccountsByAccountId(account);
        List<SysPost> sysPosts = sysPostMapper.selectPostList(sysPost);
        for (SysPost syspost : sysPosts)
        {
            for (SysAccount sa : accountList)
            {
//                if (syspost.getPostId().longValue() == sa.getPostId().longValue())
//                {
                    syspost.setFlag(true);
                    break;
//                }
            }
        }
        return sysPosts;
    }

    /**
     * 通过账号查询用户信息
     *
     * @param accountName 账号名称
     * @return 用户对象信息
     */
    @Override
    public String selectUserAccountById(String accountName){
        return sysAccountMapper.selectUserAccountById(accountName);
    }
}
