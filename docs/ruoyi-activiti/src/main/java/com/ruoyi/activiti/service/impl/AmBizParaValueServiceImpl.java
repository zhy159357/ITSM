package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.mapper.AmBizParaValueMapper;
import com.ruoyi.activiti.service.IAmBizParaValueService;
import com.ruoyi.common.core.domain.entity.AmBizParaValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-01-19
 */
@Service
public class AmBizParaValueServiceImpl implements IAmBizParaValueService
{
    @Autowired
    private AmBizParaValueMapper amBizParaValueMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param amParaValueId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public AmBizParaValue selectAmBizParaValueById(String amParaValueId)
    {
        return amBizParaValueMapper.selectAmBizParaValueById(amParaValueId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param amBizParaValue 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<AmBizParaValue> selectAmBizParaValueList(AmBizParaValue amBizParaValue)
    {
        return amBizParaValueMapper.selectAmBizParaValueList(amBizParaValue);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param amBizParaValue 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertAmBizParaValue(AmBizParaValue amBizParaValue)
    {

        return amBizParaValueMapper.insertAmBizParaValue(amBizParaValue);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param amBizParaValue 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateAmBizParaValue(AmBizParaValue amBizParaValue)
    {
        return amBizParaValueMapper.updateAmBizParaValue(amBizParaValue);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAmBizParaValueByIds(String ids)
    {
        return amBizParaValueMapper.deleteAmBizParaValueByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param amParaValueId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteAmBizParaValueById(String amParaValueId)
    {
        return amBizParaValueMapper.deleteAmBizParaValueById(amParaValueId);
    }
}
