package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysNotify;
import java.util.List;

/**
 * 系统通知Mapper接口
 *
 * @author zx
 * @date 2021-08-26
 */
public interface SysNotifyMapper
{
    /**
     * 查询系统通知
     *
     * @param id 系统通知ID
     * @return 系统通知
     */
    public SysNotify selectSysNotifyById(String id);

    /**
     * 查询系统通知列表
     *
     * @param sysNotify 系统通知
     * @return 系统通知集合
     */
    public List<SysNotify> selectSysNotifyList(SysNotify sysNotify);

    /**
     * 新增系统通知
     *
     * @param sysNotify 系统通知
     * @return 结果
     */
    public int insertSysNotify(SysNotify sysNotify);

    /**
     * 修改系统通知
     *
     * @param sysNotify 系统通知
     * @return 结果
     */
    public int updateSysNotify(SysNotify sysNotify);

    /**
     * 删除系统通知
     *
     * @param id 系统通知ID
     * @return 结果
     */
    public int deleteSysNotifyById(String id);

    /**
     * 批量删除系统通知
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysNotifyByIds(String[] ids);
}
