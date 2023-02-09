package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysPubFolder;
import com.ruoyi.common.core.domain.entity.SysUpdown;

import java.util.List;

/**
 * 数据层
 *
 * @author ruoyi
 */
public interface SysUpdMapper {
    public SysPubFolder selectUpdById(String id_);

    public String selectOrgById(String orgId);

    /**
     * 新增
     *
     * @return
     */
    public int insertTree(SysPubFolder sysPubFolder);

    /**
     * 修改
     *
     * @return
     */
    public int updateUpd(SysPubFolder sysPubFolder);

    public int selectCountById(String id_);
    public String selectParent(String selecttreeId);
    public String selectParentName(String par);
    public int deleteUpd(String id_);

    public int selectSelfTreeById(SysPubFolder sysPubFolder);

    public int selectUpdLeafById(String id_);

    List<SysUpdown> selectUpdownById(SysUpdown updown);

}