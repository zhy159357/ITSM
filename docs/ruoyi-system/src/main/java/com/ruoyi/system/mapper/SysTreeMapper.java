package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysFolder;

/**
 * 数据层
 *
 * @author ruoyi
 */
public interface SysTreeMapper {
    public SysFolder selectTreeById(String id_);

    /**
     * 新增
     *
     * @return
     */
    public int insertTree(SysFolder sysFolder);

    public String selectParentName(String par);

    /**
     * 修改
     *
     * @return
     */
    public int updateTree(SysFolder sysFolder);

    public int deleteTree(String id_);

    public int selectSelfTreeById(SysFolder sysFolder);

    public int selectTreeLeafById(String id_);

}