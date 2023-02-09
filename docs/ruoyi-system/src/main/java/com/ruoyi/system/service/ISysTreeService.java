package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysFolder;

/**
 * @author ye_xu
 */
public interface ISysTreeService {
    /**
     * 根据id获取对象
     */
    public SysFolder selectTreeById(String id_);
    //查询父节点名字
    public String selectParentName(String par);

    /**
     * 新增
     *
     * @param sysFolder
     * @return
     */
    public int insertTree(SysFolder sysFolder);

    /**
     * 修改
     *
     * @param sysFolder
     * @return
     */
    public int updateTree(SysFolder sysFolder);

    public int deleteTree(String id_);

    public int selectSelfTreeById(SysFolder sysFolder);

    public int selectTreeLeafById(String id_);

}
