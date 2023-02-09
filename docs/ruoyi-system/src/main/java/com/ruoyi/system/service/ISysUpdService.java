package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysPubFolder;
import com.ruoyi.common.core.domain.entity.SysUpdown;

import java.util.List;

/**
 * @author ye_xu
 */
public interface ISysUpdService {
    /**
     * 根据id获取对象
     */
    public SysPubFolder selectUpdById(String id_);
    /*
     * 查询节点数据名称通过id
     */
    public String  selectOrgById(String orgId);

    /**
     * 新增
     * @param sysPubFolders
     * @return
     */
    public int insertTree(SysPubFolder sysPubFolders);

    /**
     * 修改
     * @param sysPubFolders
     * @return
     */
    public int updateUpd(SysPubFolder sysPubFolders);

    public int selectCountById(String id_);

    public String selectParent(String selecttreeId);
    public String selectParentName(String par);

    public int deleteUpd(String id_);

    public int selectSelfTreeById(SysPubFolder sysPubFolders);

    public int selectUpdLeafById(String id_);

    List<SysUpdown> selectUpdownById(SysUpdown updown);

}
