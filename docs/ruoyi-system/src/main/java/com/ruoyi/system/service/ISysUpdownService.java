package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.SysPubFolder;
import com.ruoyi.common.core.domain.entity.SysUpdown;

import java.util.List;

/**
 * 上传下载
 * @author ye_xu
 */
public  interface ISysUpdownService
{
    /**
     * 上传下载业务
     * @param updown
     * @return
     */
    public List<SysUpdown> selectUpdownList(SysUpdown updown);
    //查询左侧树列表
    public List<Ztree> selectFolderTree(SysPubFolder folder);
    /**
     * 新增附件
     * @param updown
     * @return
     */
    public int insertUpdown(SysUpdown updown);

    /**
     * 修改附件保存
     * @param updown
     * @return
     */
    public int updateUpdown(SysUpdown updown);

    /**
     * 删除附件
     * @param ids
     * @return
     */
    public int deleteUpdownByIds(String ids);
    //通过id查询
    public SysUpdown selectUpdownById(String id_);
    //查询列表
    public List<SysUpdown> selectList(SysUpdown updown);

    //=========定时任务=========
    public void selectUpdownByTime();

    /**
     *
     * 根据文件名和创建时间模糊
     * @param sysUpdown
     * @return
     */
    public List<SysUpdown> selectListTime(SysUpdown sysUpdown);



}
