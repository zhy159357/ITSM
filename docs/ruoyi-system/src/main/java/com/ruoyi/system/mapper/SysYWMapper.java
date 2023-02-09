package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysAddlist;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author ruoyi
 */
public interface SysYWMapper
{
    /**
     * 查询
     */
    public List<SysAddlist> selectYWList(SysAddlist fw);
    //新增保存
    public int insertYW(SysAddlist fw);
    //查看详情
    public SysAddlist selectYWById(String address_list_id);
    //修改
    public SysAddlist updateYW(String address_list_id);
    public int updateYW(SysAddlist fw);
    //删除
    public int deleteYWByIds(String[] ids);
}