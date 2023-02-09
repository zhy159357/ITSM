package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysPubFolder;
import com.ruoyi.common.core.domain.entity.SysUpdown;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author ruoyi
 */
public interface SysUpdownMapper
{
    /**
     * 根据条件分页查询角色数据
     * @return
     */
    public List<SysUpdown> selectUpdownList(SysUpdown updown);

    public List<SysUpdown> selectUpdownMysqlList(SysUpdown updown);

    /**
     * 查询部门管理数据
     * @param folder 部门信息
     */
    public List<SysPubFolder> selectFolderTree(SysPubFolder folder);
    /**
     * 新增附件
     * @param updown
     * @return
     */
    public int insertUpdown(SysUpdown updown);
    /**
     * 修改附件
     * @param updown
     * @return
     */
    public int updateUpdown(SysUpdown updown);

    public List<SysUpdown> selectList(SysUpdown updown);

    public List<SysUpdown> selectUpdList();

    public List<SysUpdown> selectUpdListTime(SysUpdown updown);
    /**
     * 删除附件
     * @param ids
     * @return
     */
    public int deleteUpdownByIds(String[] ids);
    public int deleteUpdownByTime(String id);
    public SysUpdown selectUpdownById(String id_);
}