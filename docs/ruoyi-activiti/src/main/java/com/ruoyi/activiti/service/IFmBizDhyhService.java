package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.entity.FmBizDhyh;

import java.util.List;

/**
 * 转发电话银行消息Service接口
 *
 * @author ruoyi
 * @date 2021-02-18
 */
public interface IFmBizDhyhService {
    /**
     * 查询转发电话银行消息
     *
     * @param fdId 转发电话银行消息ID
     * @return 转发电话银行消息
     */
    public FmBizDhyh selectFmBizDhyhById(String fdId);

    /**
     * 查询转发电话银行消息列表
     *
     * @param fmBizDhyh 转发电话银行消息
     * @return 转发电话银行消息集合
     */
    public List<FmBizDhyh> selectFmBizDhyhList(FmBizDhyh fmBizDhyh);

    /**
     * 新增转发电话银行消息
     *
     * @param fmBizDhyh 转发电话银行消息
     * @return 结果
     */
    public int insertFmBizDhyh(FmBizDhyh fmBizDhyh);

    /**
     * 修改转发电话银行消息
     *
     * @param fmBizDhyh 转发电话银行消息
     * @return 结果
     */
    public int updateFmBizDhyh(FmBizDhyh fmBizDhyh);

    /**
     * 批量删除转发电话银行消息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmBizDhyhByIds(String ids);

    /**
     * 删除转发电话银行消息信息
     *
     * @param fdId 转发电话银行消息ID
     * @return 结果
     */
    public int deleteFmBizDhyhById(String fdId);
}
