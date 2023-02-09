package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.FmKind;
import java.util.List;

/**
 * 事件单系统分类Mapper接口
 * 
 * @author ruoyi
 * @date 2020-12-28
 */
public interface FmKindMapper 
{
    /**
     * 查询事件单系统分类
     * 
     * @param fmTypeId 事件单系统分类ID
     * @return 事件单系统分类
     */
    public FmKind selectFmKindById(String fmTypeId);

    /**
     * 查询事件单系统分类列表
     * 
     * @param fmKind 事件单系统分类
     * @return 事件单系统分类集合
     */
    public List<FmKind> selectFmKindList(FmKind fmKind);

    /**
     * 新增事件单系统分类
     * 
     * @param fmKind 事件单系统分类
     * @return 结果
     */
    public int insertFmKind(FmKind fmKind);

    /**
     * 修改事件单系统分类
     * 
     * @param fmKind 事件单系统分类
     * @return 结果
     */
    public int updateFmKind(FmKind fmKind);

    /**
     * 删除事件单系统分类
     * 
     * @param fmTypeId 事件单系统分类ID
     * @return 结果
     */
    public int deleteFmKindById(String fmTypeId);

    /**
     * 批量删除事件单系统分类
     * 
     * @param fmTypeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmKindByIds(String[] fmTypeIds);
    /**
     * 根据系统ID查询对应分类
     */
    public List<FmKind> selectFmKindBySysid(String sysid);
}
