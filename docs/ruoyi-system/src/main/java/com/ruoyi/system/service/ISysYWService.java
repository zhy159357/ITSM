package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysAddlist;

import java.util.List;

/**
 * 服务通讯录业务层
 * @author ye_xu
 */
public interface ISysYWService
{
    /**
     * 根据条件分页查询角色数据
     * @param fw 应用话机绑定信息
     */

    public List<SysAddlist> selectYWList(SysAddlist fw);

    public int insertYW(SysAddlist fw);

    /**
     * @param address_list_id 查看详情
     */
    //修改
    public SysAddlist selectYWById(String address_list_id);
    public int updateYW(SysAddlist fw);
    /**
     * 删除
     */
    public int deleteYWByIds(String ids);

}
