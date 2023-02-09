package com.ruoyi.activiti.service;

import com.ruoyi.system.domain.OgEmerg;

import java.util.List;

/**
 * 部门管理 服务层
 * 
 * @author ruoyi
 */
public interface IOgEmergService
{
    /**
     * 查询事件
     *
     * @param emerg 事件信息
     * @return 事件信息集合
     */
    public List<OgEmerg> selectEmergList(OgEmerg emerg);

    /**
     * 批量删除事件单
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteEmergByIds(String ids);

    /**
     * 新增参数列别
     *
     * @param emerg 参数列别
     * @return 结果
     */
    public int insertOgEmerg(OgEmerg emerg);

    Object selectEmergById(String postId);

    /**
     * 修改保存岗位信息
     *
     * @param emerg 岗位信息
     * @return 结果
     */
    int updateEmerg(OgEmerg emerg);

    /**
     * 通过角色ID查询事件单
     *
     * @param emergId 事件单ID
     * @return 事件单对象信息
     */
    public OgEmerg selectById(String emergId);

    /**
     * 根据id查询
     *
     * @param emergId
     * @return 集合
     */
    public OgEmerg selectOgEmergById(String emergId);

    int updateEmergMark(OgEmerg emerg);



}
