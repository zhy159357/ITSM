package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.domain.PubBizPresms;
import com.ruoyi.activiti.mapper.PubBizPresmsMapper;
import com.ruoyi.activiti.service.IPubBizPresmsService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 短信存储Service业务层处理
 *
 * @author ruoyi
 * @date 2021-02-09
 */
@Service("pubBizPresmsService")
public class PubBizPresmsServiceImpl implements IPubBizPresmsService {
    @Autowired
    private PubBizPresmsMapper pubBizPresmsMapper;

    /**
     * 查询短信存储
     *
     * @param pubBizPresmsId 短信存储ID
     * @return 短信存储
     */
    @Override
    public PubBizPresms selectPubBizPresmsById(String pubBizPresmsId) {
        return pubBizPresmsMapper.selectPubBizPresmsById(pubBizPresmsId);
    }

    /**
     * 查询短信存储列表
     *
     * @param pubBizPresms 短信存储
     * @return 短信存储
     */
    @Override
    public List<PubBizPresms> selectPubBizPresmsList(PubBizPresms pubBizPresms) {
        return pubBizPresmsMapper.selectPubBizPresmsList(pubBizPresms);
    }

    /**
     * 新增短信存储
     *
     * @param pubBizPresms 短信存储
     * @return 结果
     */
    @Override
    public int insertPubBizPresms(PubBizPresms pubBizPresms) {
        return pubBizPresmsMapper.insertPubBizPresms(pubBizPresms);
    }

    /**
     * 修改短信存储
     *
     * @param pubBizPresms 短信存储
     * @return 结果
     */
    @Override
    public int updatePubBizPresms(PubBizPresms pubBizPresms) {

        return pubBizPresmsMapper.updatePubBizPresms(pubBizPresms);
    }

    /**
     * 删除短信存储对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deletePubBizPresmsByIds(String ids) {
        return pubBizPresmsMapper.deletePubBizPresmsByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除短信存储信息
     *
     * @param pubBizPresmsId 短信存储ID
     * @return 结果
     */
    @Override
    public int deletePubBizPresmsById(String pubBizPresmsId) {
        return pubBizPresmsMapper.deletePubBizPresmsById(pubBizPresmsId);
    }
}
