package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.OgRPywRywcz;

import java.util.List;

/**
 * 数据变更短信通知关联Service接口
 * 
 * @author ruoyi
 * @date 2021-06-29
 */
public interface IOgRPywRywczService 
{
    /**
     * 查询数据变更短信通知关联
     * 
     * @param ywid 数据变更短信通知关联ID
     * @return 数据变更短信通知关联
     */
    public OgRPywRywcz selectOgRPywRywczById(String ywid);

    /**
     * 查询数据变更短信通知关联列表
     * 
     * @param ogRPywRywcz 数据变更短信通知关联
     * @return 数据变更短信通知关联集合
     */
    public List<OgRPywRywcz> selectOgRPywRywczList(OgRPywRywcz ogRPywRywcz);

    /**
     * 新增数据变更短信通知关联
     * 
     * @param ogRPywRywcz 数据变更短信通知关联
     * @return 结果
     */
    public int insertOgRPywRywcz(OgRPywRywcz ogRPywRywcz);

    /**
     * 修改数据变更短信通知关联
     * 
     * @param ogRPywRywcz 数据变更短信通知关联
     * @return 结果
     */
    public int updateOgRPywRywcz(OgRPywRywcz ogRPywRywcz);

    /**
     * 批量删除数据变更短信通知关联
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOgRPywRywczByIds(String ids);

    /**
     * 删除数据变更短信通知关联信息
     * 
     * @param ywid 数据变更短信通知关联ID
     * @return 结果
     */
    public int deleteOgRPywRywczById(String ywid);
}
