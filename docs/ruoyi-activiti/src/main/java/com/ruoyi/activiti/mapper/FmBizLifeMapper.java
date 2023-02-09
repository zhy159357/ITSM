package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.FmBizLife;

import java.util.List;

/**
 * 事件单效率Mapper接口
 *
 * @author ruoyi
 * @date 2021-01-21
 */
public interface FmBizLifeMapper {
    /**
     * 查询事件单效率
     *
     * @param fmLifeId 事件单效率ID
     * @return 事件单效率
     */
    public FmBizLife selectFmBizLifeById(String fmLifeId);

    /**
     * 查询事件单效率列表
     *
     * @param fmBizLife 事件单效率
     * @return 事件单效率集合
     */
    public List<FmBizLife> selectFmBizLifeList(FmBizLife fmBizLife);

    /**
     * 新增事件单效率
     *
     * @param fmBizLife 事件单效率
     * @return 结果
     */
    public int insertFmBizLife(FmBizLife fmBizLife);

    /**
     * 修改事件单效率
     *
     * @param fmBizLife 事件单效率
     * @return 结果
     */
    public int updateFmBizLife(FmBizLife fmBizLife);

    /**
     * 删除事件单效率
     *
     * @param fmLifeId 事件单效率ID
     * @return 结果
     */
    public int deleteFmBizLifeById(String fmLifeId);

    /**
     * 批量删除事件单效率
     *
     * @param fmLifeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmBizLifeByIds(String[] fmLifeIds);

    /**
     * 查询剩余事件单数量（按系统）
     */
    public List<FmBizLife> getSysCount();

    /**
     * 查询处理事件单数量（按系统）
     */
    public List<FmBizLife> getSysDealCount(FmBizLife fmBizLife);

    public void deleteAll();
}
