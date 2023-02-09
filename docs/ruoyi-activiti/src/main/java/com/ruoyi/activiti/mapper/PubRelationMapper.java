package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.PubRelation;

import java.util.List;

/**
 * 关联关系Mapper接口
 *
 * @author zx
 * @date 2021-01-25
 */
public interface PubRelationMapper {
    /**
     * 查询关联关系
     *
     * @param relationId 关联关系ID
     * @return 关联关系
     */
    public PubRelation selectPubRelationById(String relationId);

    /**
     * 查询关联关系列表
     *
     * @param pubRelation 关联关系
     * @return 关联关系集合
     */
    public List<PubRelation> selectPubRelationList(PubRelation pubRelation);

    /**
     * 新增关联关系
     *
     * @param pubRelation 关联关系
     * @return 结果
     */
    public int insertPubRelation(PubRelation pubRelation);

    /**
     * 修改关联关系
     *
     * @param pubRelation 关联关系
     * @return 结果
     */
    public int updatePubRelation(PubRelation pubRelation);

    /**
     * 删除关联关系
     *
     * @param relationId 关联关系ID
     * @return 结果
     */
    public int deletePubRelationById(String relationId);

    /**
     * 批量删除关联关系
     *
     * @param relationIds 需要删除的数据ID
     * @return 结果
     */
    public int deletePubRelationByIds(String[] relationIds);

    public int deletePubRelationByFmId(String fmId);
}
