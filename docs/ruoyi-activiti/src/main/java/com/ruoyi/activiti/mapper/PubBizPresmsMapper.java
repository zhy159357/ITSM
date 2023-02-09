package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.PubBizPresms;

import java.util.List;

/**
 * 短信存储Mapper接口
 * 
 * @author ruoyi
 * @date 2021-02-09
 */
public interface PubBizPresmsMapper 
{
    /**
     * 查询短信存储
     * 
     * @param pubBizPresmsId 短信存储ID
     * @return 短信存储
     */
    public PubBizPresms selectPubBizPresmsById(String pubBizPresmsId);

    /**
     * 查询短信存储列表
     * 
     * @param pubBizPresms 短信存储
     * @return 短信存储集合
     */
    public List<PubBizPresms> selectPubBizPresmsList(PubBizPresms pubBizPresms);

    /**
     * 新增短信存储
     * 
     * @param pubBizPresms 短信存储
     * @return 结果
     */
    public int insertPubBizPresms(PubBizPresms pubBizPresms);

    /**
     * 修改短信存储
     * 
     * @param pubBizPresms 短信存储
     * @return 结果
     */
    public int updatePubBizPresms(PubBizPresms pubBizPresms);

    /**
     * 删除短信存储
     * 
     * @param pubBizPresmsId 短信存储ID
     * @return 结果
     */
    public int deletePubBizPresmsById(String pubBizPresmsId);

    /**
     * 批量删除短信存储
     * 
     * @param pubBizPresmsIds 需要删除的数据ID
     * @return 结果
     */
    public int deletePubBizPresmsByIds(String[] pubBizPresmsIds);
}
