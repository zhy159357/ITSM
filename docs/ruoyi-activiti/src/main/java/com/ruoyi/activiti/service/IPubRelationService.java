package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.PubRelation;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 关联关系Service接口
 * 
 * @author zx
 * @date 2021-01-25
 */
public interface IPubRelationService
{
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
     * 批量删除关联关系
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePubRelationByIds(String ids);

    /**
     * 删除关联关系信息
     * 
     * @param relationId 关联关系ID
     * @return 结果
     */
    public int deletePubRelationById(String relationId);

    /**
     * 根据事件单ID删除关联的问题单
     * @param relationId
     * @return
     */
    public int deletePubRelationByFmId(String relationId);
}
