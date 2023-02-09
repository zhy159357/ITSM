package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.domain.OgRPywRywcz;
import com.ruoyi.activiti.mapper.OgRPywRywczMapper;
import com.ruoyi.activiti.service.IOgRPywRywczService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 数据变更短信通知关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-06-29
 */
@Service
public class OgRPywRywczServiceImpl implements IOgRPywRywczService
{
    @Autowired
    private OgRPywRywczMapper ogRPywRywczMapper;

    /**
     * 查询数据变更短信通知关联
     * 
     * @param ywid 数据变更短信通知关联ID
     * @return 数据变更短信通知关联
     */
    @Override
    public OgRPywRywcz selectOgRPywRywczById(String ywid)
    {
        return ogRPywRywczMapper.selectOgRPywRywczById(ywid);
    }

    /**
     * 查询数据变更短信通知关联列表
     * 
     * @param ogRPywRywcz 数据变更短信通知关联
     * @return 数据变更短信通知关联
     */
    @Override
    public List<OgRPywRywcz> selectOgRPywRywczList(OgRPywRywcz ogRPywRywcz)
    {
        return ogRPywRywczMapper.selectOgRPywRywczList(ogRPywRywcz);
    }

    /**
     * 新增数据变更短信通知关联
     * 
     * @param ogRPywRywcz 数据变更短信通知关联
     * @return 结果
     */
    @Override
    public int insertOgRPywRywcz(OgRPywRywcz ogRPywRywcz)
    {
        return ogRPywRywczMapper.insertOgRPywRywcz(ogRPywRywcz);
    }

    /**
     * 修改数据变更短信通知关联
     * 
     * @param ogRPywRywcz 数据变更短信通知关联
     * @return 结果
     */
    @Override
    public int updateOgRPywRywcz(OgRPywRywcz ogRPywRywcz)
    {
        return ogRPywRywczMapper.updateOgRPywRywcz(ogRPywRywcz);
    }

    /**
     * 删除数据变更短信通知关联对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOgRPywRywczByIds(String ids)
    {
        return ogRPywRywczMapper.deleteOgRPywRywczByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除数据变更短信通知关联信息
     * 
     * @param ywid 数据变更短信通知关联ID
     * @return 结果
     */
    @Override
    public int deleteOgRPywRywczById(String ywid)
    {
        return ogRPywRywczMapper.deleteOgRPywRywczById(ywid);
    }
}
