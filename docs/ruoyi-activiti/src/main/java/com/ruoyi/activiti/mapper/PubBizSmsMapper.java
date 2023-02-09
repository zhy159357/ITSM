package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.PubBizSms;

import java.util.List;

/**
 * 短信发送Mapper接口
 * 
 * @author ruoyi
 * @date 2021-02-09
 */
public interface PubBizSmsMapper 
{
    /**
     * 查询短信发送
     * 
     * @param pubBizSmsId 短信发送ID
     * @return 短信发送
     */
    public PubBizSms selectPubBizSmsById(String pubBizSmsId);

    /**
     * 查询短信发送列表
     * 
     * @param pubBizSms 短信发送
     * @return 短信发送集合
     */
    public List<PubBizSms> selectPubBizSmsList(PubBizSms pubBizSms);

    /**
     * 新增短信发送
     * 
     * @param pubBizSms 短信发送
     * @return 结果
     */
    public int insertPubBizSms(PubBizSms pubBizSms);

    /**
     * 修改短信发送
     * 
     * @param pubBizSms 短信发送
     * @return 结果
     */
    public int updatePubBizSms(PubBizSms pubBizSms);

    /**
     * 删除短信发送
     * 
     * @param pubBizSmsId 短信发送ID
     * @return 结果
     */
    public int deletePubBizSmsById(String pubBizSmsId);

    /**
     * 批量删除短信发送
     * 
     * @param pubBizSmsIds 需要删除的数据ID
     * @return 结果
     */
    public int deletePubBizSmsByIds(String[] pubBizSmsIds);

    List<PubBizSms> selectPubBizSmsListTwo(PubBizSms pubBizSms);
}
