package com.ruoyi.activiti.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.activiti.mapper.PubRelationMapper;
import com.ruoyi.activiti.domain.PubRelation;
import com.ruoyi.activiti.service.IPubRelationService;
import com.ruoyi.common.core.text.Convert;

/**
 * 关联关系Service业务层处理
 *
 * @author zx
 * @date 2021-01-25
 */
@Service
public class PubRelationServiceImpl implements IPubRelationService {
    @Autowired
    private PubRelationMapper pubRelationMapper;

    /**
     * 查询关联关系
     *
     * @param relationId 关联关系ID
     * @return 关联关系
     */
    @Override
    public PubRelation selectPubRelationById(String relationId) {
        return pubRelationMapper.selectPubRelationById(relationId);
    }

    /**
     * 查询关联关系列表
     *
     * @param pubRelation 关联关系
     * @return 关联关系
     */
    @Override
    public List<PubRelation> selectPubRelationList(PubRelation pubRelation) {
        return pubRelationMapper.selectPubRelationList(pubRelation);
    }

    /**
     * 新增关联关系
     *
     * @param pubRelation 关联关系
     * @return 结果
     */
    @Override
    public int insertPubRelation(PubRelation pubRelation) {
        return pubRelationMapper.insertPubRelation(pubRelation);
    }

    /**
     * 修改关联关系
     *
     * @param pubRelation 关联关系
     * @return 结果
     */
    @Override
    public int updatePubRelation(PubRelation pubRelation) {
        return pubRelationMapper.updatePubRelation(pubRelation);
    }

    /**
     * 删除关联关系对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deletePubRelationByIds(String ids) {
        return pubRelationMapper.deletePubRelationByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除关联关系信息
     *
     * @param relationId 关联关系ID
     * @return 结果
     */
    @Override
    public int deletePubRelationById(String relationId) {
        return pubRelationMapper.deletePubRelationById(relationId);
    }

    @Override
    public int deletePubRelationByFmId(String fmId) {
        return pubRelationMapper.deletePubRelationByFmId(fmId);
    }
}
