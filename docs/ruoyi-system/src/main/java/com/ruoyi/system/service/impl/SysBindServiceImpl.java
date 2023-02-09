package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysBind;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.mapper.SysBindMapper;
import com.ruoyi.system.mapper.SysRoleMenuMapper;
import com.ruoyi.system.service.ISysBindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统业务层处理
 */
@Service
public class SysBindServiceImpl implements ISysBindService
{
    @Autowired
    private SysBindMapper bindMbinder;

    @Autowired
    private SysRoleMenuMapper roleMenuMbinder;

    /**
     * 查询所有系统
     *
     * @return 系统列表
     */
    @Override
    public List<SysBind> selectBindAll()
    {
        return SpringUtils.getAopProxy(this).selectBindList(new SysBind());
    }
    /**
     * 通过系统ID查询角色
     * @param id 系统ID
     * @return 系统对象信息
     */
    @Override
    public SysBind selectById(Long id)
    {
        return bindMbinder.selectById(id);
    }

    /**
     * 通过系统ID删除系统
     *
     * @param roleId 系统ID
     * @return 结果
     */
    @Override
    public boolean deleteBindById(Long roleId)
    {
        return bindMbinder.deleteBindById(roleId) > 0 ? true : false;
    }

    @Override
    public int deleteBindByIds(String ids) throws Exception {
        Long[] bindIds = Convert.toLongArray(ids);
        return bindMbinder.deleteBindByIds(bindIds);
    }

    /**
     * 新增保存系统信息
     *
     * @param bind 系统信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertBind(SysBind bind)
    {
        // 新增角色信息
        return bindMbinder.insertBind(bind);
    }

    /**
     * 修改保存系统信息
     *
     * @param bind 系统信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateBind(SysBind bind)
    {
        // 修改角色信息
        return bindMbinder.updateBind(bind);
    }

    /**
     * 校验系统名称是否唯一
     *
     * @param bind 系统信息
     * @return 结果
     */
    @Override
    public String checkBindNameUnique(SysBind bind)
    {
        return UserConstants.ROLE_NAME_UNIQUE;
    }

    @Override
    public int changeStatus(SysBind bind) {
        return 0;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param bind 角色信息
     */
    @Override
    public void checkBindAllowed(SysBind bind)
    {
    }

    /**
     * 根据条件分页查询角色数据
     * @param bind 系统信息
     * @return 角色数据集合信息
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysBind> selectBindList(SysBind bind)
    {
        return bindMbinder.selectBindList(bind);
    }


    /**
     * 根据用户ID查询应用系统
     *
     * @param userId 用户ID
     * @return 应用系统列表
     */
    @Override
    public List<SysBind> selectBindsByUserId(Long userId)
    {
        List<SysBind> userBinds = bindMbinder.selectBindsByUserId(userId);
        List<SysBind> binds = selectBindAll();
        for (SysBind bind : binds)
        {
            for (SysBind userRole : userBinds)
            {
            }
        }
        return binds;
    }

}
