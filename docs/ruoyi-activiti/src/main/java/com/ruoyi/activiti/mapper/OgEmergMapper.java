package com.ruoyi.activiti.mapper;

import com.ruoyi.system.domain.OgEmerg;

import java.util.List;

/**
 * 部门管理 数据层
 * 
 * @author ruoyi
 */
public interface OgEmergMapper
{

    /**
     * 查询事件
     * @param emerg 事件信息
     * @return 事件信息集合
     */
    public List<OgEmerg> selectEmergList(OgEmerg emerg);

    /**
     * 作废事件单
     * @param ids 参数列表
     * @return 结果
     */
    public int deleteEmergByIds(String[] ids);

    /**
     * 新建事件列表
     * @param emerg 参数列表
     * @return 结果
     */
    public int insertOgEmerg(OgEmerg emerg);

    OgEmerg selectEmergById(String id);

    /**
     * 修改保存事件单信息
     *
     * @param emerg 事件单信息
     * @return 结果
     */
    int updateEmerg(OgEmerg emerg);

    /**
     * 通过角色ID查询角色
     * @param id 角色ID
     * @return 角色对象信息
     */
    public OgEmerg selectById(String id);

    /**
     * 根据id查询
     *
     * @param emergId
     * @return 集合
     */
    public OgEmerg selectOgEmergById(String id);


    int updateEmergMark(OgEmerg emerg);
}
