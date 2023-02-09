package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.domain.TelSupportPeropleExtension;
import com.ruoyi.activiti.mapper.OgPersonExtQueryMapper;
import com.ruoyi.activiti.service.IOgPersonExtQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-05-19
 */
@Service
public class OgPersonExtQueryServiceImpl implements IOgPersonExtQueryService
{
    @Autowired
    private OgPersonExtQueryMapper ogPersonExtQueryMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public TelSupportPeropleExtension selectOgPersonExtQueryById(String id)
    {
        return ogPersonExtQueryMapper.selectOgPersonExtQueryById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param telSupportPeropleExtension 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TelSupportPeropleExtension> selectOgPersonExtQueryList(TelSupportPeropleExtension telSupportPeropleExtension)
    {
        return ogPersonExtQueryMapper.selectOgPersonExtQueryList(telSupportPeropleExtension);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param telSupportPeropleExtension 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOgPersonExtQuery(TelSupportPeropleExtension telSupportPeropleExtension)
    {
        return ogPersonExtQueryMapper.insertOgPersonExtQuery(telSupportPeropleExtension);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param telSupportPeropleExtension 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOgPersonExtQuery(TelSupportPeropleExtension telSupportPeropleExtension)
    {
        return ogPersonExtQueryMapper.updateOgPersonExtQuery(telSupportPeropleExtension);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOgPersonExtQueryByIds(String ids)
    {
        return ogPersonExtQueryMapper.deleteOgPersonExtQueryByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOgPersonExtQueryById(String id)
    {
        return ogPersonExtQueryMapper.deleteOgPersonExtQueryById(id);
    }
}
