package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysFWlist;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author ruoyi
 */
public interface SysFWMapper
{
    /**
     * 查询
     */
    public List<SysFWlist> selectFWList(SysFWlist fw);

    public List<SysFWlist> selectFWMysqlList(SysFWlist fw);
    //新增保存
    public int insertFW(SysFWlist fw);
    //查看详情
    public SysFWlist selectFWById(String address_list_id);
    //修改
    public SysFWlist updateFW(String address_list_id);
    public int updateFW(SysFWlist fw);
    //删除
    public int deleteFWByIds(String[] ids);
}