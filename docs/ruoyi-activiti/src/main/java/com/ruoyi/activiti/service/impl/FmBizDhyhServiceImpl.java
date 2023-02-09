package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.mapper.FmBizDhyhMapper;
import com.ruoyi.activiti.service.IFmBizDhyhService;
import com.ruoyi.common.core.domain.entity.FmBizDhyh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 转发电话银行消息Service业务层处理
 *
 * @author ruoyi
 * @date 2021-02-18
 */
@Service
public class FmBizDhyhServiceImpl implements IFmBizDhyhService {
    @Autowired
    private FmBizDhyhMapper fmBizDhyhMapper;

    /**
     * 查询转发电话银行消息
     *
     * @param fdId 转发电话银行消息ID
     * @return 转发电话银行消息
     */
    @Override
    public FmBizDhyh selectFmBizDhyhById(String fdId) {
        return fmBizDhyhMapper.selectFmBizDhyhById(fdId);
    }

    /**
     * 查询转发电话银行消息列表
     *
     * @param fmBizDhyh 转发电话银行消息
     * @return 转发电话银行消息
     */
    @Override
    public List<FmBizDhyh> selectFmBizDhyhList(FmBizDhyh fmBizDhyh) {
        return fmBizDhyhMapper.selectFmBizDhyhList(fmBizDhyh);
    }

    /**
     * 新增转发电话银行消息
     *
     * @param fmBizDhyh 转发电话银行消息
     * @return 结果
     */
    @Override
    public int insertFmBizDhyh(FmBizDhyh fmBizDhyh) {
        return fmBizDhyhMapper.insertFmBizDhyh(fmBizDhyh);
    }

    /**
     * 修改转发电话银行消息
     *
     * @param fmBizDhyh 转发电话银行消息
     * @return 结果
     */
    @Override
    public int updateFmBizDhyh(FmBizDhyh fmBizDhyh) {
        return fmBizDhyhMapper.updateFmBizDhyh(fmBizDhyh);
    }

    /**
     * 删除转发电话银行消息对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFmBizDhyhByIds(String ids) {
        return fmBizDhyhMapper.deleteFmBizDhyhByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除转发电话银行消息信息
     *
     * @param fdId 转发电话银行消息ID
     * @return 结果
     */
    @Override
    public int deleteFmBizDhyhById(String fdId) {
        return fmBizDhyhMapper.deleteFmBizDhyhById(fdId);
    }
}
