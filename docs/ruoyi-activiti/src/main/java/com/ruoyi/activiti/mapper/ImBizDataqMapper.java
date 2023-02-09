package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.ImBizDataq;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2021-09-28
 */
public interface ImBizDataqMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param imId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public ImBizDataq selectImBizDataqById(String imId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param imBizDataq 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ImBizDataq> selectImBizDataqList(ImBizDataq imBizDataq);

    public List<ImBizDataq> pageList(ImBizDataq imBizDataq);
    /**
     * 新增【请填写功能名称】
     *
     * @param imBizDataq 【请填写功能名称】
     * @return 结果
     */
    public int insertImBizDataq(ImBizDataq imBizDataq);

    /**
     * 修改【请填写功能名称】
     *
     * @param imBizDataq 【请填写功能名称】
     * @return 结果
     */
    public int updateImBizDataq(ImBizDataq imBizDataq);

    /**
     * 删除【请填写功能名称】
     *
     * @param imId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteImBizDataqById(String imId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param imIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteImBizDataqByIds(String[] imIds);

    ImBizDataq selectImBizDataqByNo(String imNo);

    List<ImBizDataq> selectImBizDataqListById(String id);

    List<ImBizDataq> pageListBg(ImBizDataq imBizDataq);


}