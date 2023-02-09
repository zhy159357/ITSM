package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysFWlist;

import java.util.List;

/**
 * 服务通讯录业务层
 * @author ye_xu
 */
public interface ISysFWService
{
    /**
     * 根据条件分页查询角色数据
     * @param fw 应用话机绑定信息
     */

    public List<SysFWlist> selectFWList(SysFWlist fw);

    public int insertFW(SysFWlist fw);

    /**
     * @param address_list_id 查看详情
     */
    //修改
    public SysFWlist selectFWById(String address_list_id);
    public int updateFW(SysFWlist fw);
    /**
     * 删除
     */
    public int deleteFWByIds(String ids);

}
