package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.system.service.ISysNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysNotifyMapper;
import com.ruoyi.system.domain.SysNotify;
import com.ruoyi.common.core.text.Convert;

/**
 * 系统通知Service业务层处理
 *
 * @author zx
 * @date 2021-08-26
 */
@Service
public class SysNotifyServiceImpl implements ISysNotifyService
{
    @Autowired
    private SysNotifyMapper sysNotifyMapper;

    /**
     * 查询系统通知
     *
     * @param id 系统通知ID
     * @return 系统通知
     */
    @Override
    public SysNotify selectSysNotifyById(String id)
    {
        return sysNotifyMapper.selectSysNotifyById(id);
    }

    /**
     * 查询系统通知列表
     *
     * @param sysNotify 系统通知
     * @return 系统通知
     */
    @Override
    public List<SysNotify> selectSysNotifyList(SysNotify sysNotify)
    {
        return sysNotifyMapper.selectSysNotifyList(sysNotify);
    }

    /**
     * 新增系统通知
     *
     * @param sysNotify 系统通知
     * @return 结果
     */
    @Override
    public int insertSysNotify(SysNotify sysNotify)
    {
        return sysNotifyMapper.insertSysNotify(sysNotify);
    }

    /**
     * 修改系统通知
     *
     * @param sysNotify 系统通知
     * @return 结果
     */
    @Override
    public int updateSysNotify(SysNotify sysNotify)
    {
        return sysNotifyMapper.updateSysNotify(sysNotify);
    }

    /**
     * 删除系统通知对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysNotifyByIds(String ids)
    {
        return sysNotifyMapper.deleteSysNotifyByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除系统通知信息
     *
     * @param id 系统通知ID
     * @return 结果
     */
    @Override
    public int deleteSysNotifyById(String id)
    {
        return sysNotifyMapper.deleteSysNotifyById(id);
    }
}
