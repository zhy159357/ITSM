package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.PubBizSmsReceive;

import java.util.List;

/**
 * 接收短信Mapper接口
 * 
 * @author ruoyi
 * @date 2021-02-09
 */
public interface PubBizSmsReceiveMapper 
{
    /**
     * 查询接收短信
     * 
     * @param id 接收短信ID
     * @return 接收短信
     */
    public PubBizSmsReceive selectPubBizSmsReceiveById(String id);

    /**
     * 查询接收短信列表
     * 
     * @param pubBizSmsReceive 接收短信
     * @return 接收短信集合
     */
    public List<PubBizSmsReceive> selectPubBizSmsReceiveList(PubBizSmsReceive pubBizSmsReceive);

    /**
     * 新增接收短信
     * 
     * @param pubBizSmsReceive 接收短信
     * @return 结果
     */
    public int insertPubBizSmsReceive(PubBizSmsReceive pubBizSmsReceive);

    /**
     * 修改接收短信
     * 
     * @param pubBizSmsReceive 接收短信
     * @return 结果
     */
    public int updatePubBizSmsReceive(PubBizSmsReceive pubBizSmsReceive);

    /**
     * 删除接收短信
     * 
     * @param id 接收短信ID
     * @return 结果
     */
    public int deletePubBizSmsReceiveById(String id);

    /**
     * 批量删除接收短信
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deletePubBizSmsReceiveByIds(String[] ids);
}
