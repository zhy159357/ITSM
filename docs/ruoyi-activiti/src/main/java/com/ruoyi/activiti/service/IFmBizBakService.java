package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.FmBiz;
import com.ruoyi.common.core.domain.entity.FmBizBak;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2021-10-14
 */
public interface IFmBizBakService {
    /**
     * 查询【请填写功能名称】
     *
     * @param fmId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public FmBizBak selectFmBizBakById(String fmId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param fmBizBak 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<FmBizBak> selectFmBizBakList(FmBizBak fmBizBak);

    /**
     * 新增【请填写功能名称】
     *
     * @param fmBizBak 【请填写功能名称】
     * @return 结果
     */
    public int insertFmBizBak(FmBizBak fmBizBak);

    /**
     * 修改【请填写功能名称】
     *
     * @param fmBizBak 【请填写功能名称】
     * @return 结果
     */
    public int updateFmBizBak(FmBizBak fmBizBak);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmBizBakByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param fmId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteFmBizBakById(String fmId);

    public List<FmBizBak> selectFmBizBakListPager(FmBizBak fmBizBak);

    public FmBizBak formatFmbizBak(FmBizBak fmBizBak);
}
