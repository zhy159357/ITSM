package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.mapper.AmBizParaMapper;
import com.ruoyi.activiti.mapper.AmBizParaValueMapper;
import com.ruoyi.activiti.service.IAmBizParaService;
import com.ruoyi.common.core.domain.entity.AmBizPara;
import com.ruoyi.common.core.domain.entity.AmBizParaValue;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-01-19
 */
@Service("AmpubParaValue")
public class AmBizParaServiceImpl implements IAmBizParaService
{
    @Autowired
    private AmBizParaMapper amBizParaMapper;

    @Autowired
    private AmBizParaValueMapper amBizParaValueMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param amParaId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public AmBizPara selectAmBizParaById(String amParaId)
    {
        return amBizParaMapper.selectAmBizParaById(amParaId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param amBizPara 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<AmBizPara> selectAmBizParaList(AmBizPara amBizPara)
    {
        return amBizParaMapper.selectAmBizParaList(amBizPara);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param amBizPara 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertAmBizPara(AmBizPara amBizPara)
    {
        amBizPara.setAmParaId(DateUtils.dateTimeNow());
        return amBizParaMapper.insertAmBizPara(amBizPara);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param amBizPara 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateAmBizPara(AmBizPara amBizPara)
    {
        return amBizParaMapper.updateAmBizPara(amBizPara);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAmBizParaByIds(String ids)
    {
        if (StringUtils.isNotEmpty(ids)) {
            amBizParaValueMapper.deleteAmBizParaValueByAmParaId(ids);
        }
        return amBizParaMapper.deleteAmBizParaByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param amParaId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteAmBizParaById(String amParaId)
    {
        return amBizParaMapper.deleteAmBizParaById(amParaId);
    }
}
