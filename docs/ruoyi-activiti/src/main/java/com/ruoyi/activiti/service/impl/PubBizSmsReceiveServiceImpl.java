package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.domain.PubBizSmsReceive;
import com.ruoyi.activiti.mapper.PubBizSmsReceiveMapper;
import com.ruoyi.activiti.service.IPubBizSmsReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 接收短信Service业务层处理
 *
 * @author ruoyi
 * @date 2021-02-09
 */
@Service("pubBizSmsReceiveService")
public class PubBizSmsReceiveServiceImpl implements IPubBizSmsReceiveService {
    @Autowired
    private PubBizSmsReceiveMapper pubBizSmsReceiveMapper;

    /**
     * 查询接收短信
     *
     * @param id 接收短信ID
     * @return 接收短信
     */
    @Override
    public PubBizSmsReceive selectPubBizSmsReceiveById(String id) {
        return pubBizSmsReceiveMapper.selectPubBizSmsReceiveById(id);
    }

    /**
     * 查询接收短信列表
     *
     * @param pubBizSmsReceive 接收短信
     * @return 接收短信
     */
    @Override
    public List<PubBizSmsReceive> selectPubBizSmsReceiveList(PubBizSmsReceive pubBizSmsReceive) {
        return pubBizSmsReceiveMapper.selectPubBizSmsReceiveList(pubBizSmsReceive);
    }

    /**
     * 新增接收短信
     *
     * @param pubBizSmsReceive 接收短信
     * @return 结果
     */
    @Override
    public int insertPubBizSmsReceive(PubBizSmsReceive pubBizSmsReceive) {
        return pubBizSmsReceiveMapper.insertPubBizSmsReceive(pubBizSmsReceive);
    }

    /**
     * 修改接收短信
     *
     * @param pubBizSmsReceive 接收短信
     * @return 结果
     */
    @Override
    public int updatePubBizSmsReceive(PubBizSmsReceive pubBizSmsReceive) {
        return pubBizSmsReceiveMapper.updatePubBizSmsReceive(pubBizSmsReceive);
    }

    /**
     * 删除接收短信对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deletePubBizSmsReceiveByIds(String ids) {
        return pubBizSmsReceiveMapper.deletePubBizSmsReceiveByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除接收短信信息
     *
     * @param id 接收短信ID
     * @return 结果
     */
    @Override
    public int deletePubBizSmsReceiveById(String id) {
        return pubBizSmsReceiveMapper.deletePubBizSmsReceiveById(id);
    }
}
