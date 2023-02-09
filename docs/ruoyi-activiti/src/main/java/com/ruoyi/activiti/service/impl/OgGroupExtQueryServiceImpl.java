package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.domain.TelSystemSupportgroupNumber;
import com.ruoyi.activiti.mapper.OgGroupExtQueryMapper;
import com.ruoyi.activiti.service.IOgGroupExtQueryService;
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
public class OgGroupExtQueryServiceImpl implements IOgGroupExtQueryService
{
    @Autowired
    private OgGroupExtQueryMapper ogGroupExtQueryMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public TelSystemSupportgroupNumber selectOgGroupExtQueryById(String id)
    {
        return ogGroupExtQueryMapper.selectOgGroupExtQueryById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param telSystemSupportgroupNumber 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TelSystemSupportgroupNumber> selectOgGroupExtQueryList(TelSystemSupportgroupNumber telSystemSupportgroupNumber)
    {
        return ogGroupExtQueryMapper.selectOgGroupExtQueryList(telSystemSupportgroupNumber);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param telSystemSupportgroupNumber 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOgGroupExtQuery(TelSystemSupportgroupNumber telSystemSupportgroupNumber)
    {
        return ogGroupExtQueryMapper.insertOgGroupExtQuery(telSystemSupportgroupNumber);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param telSystemSupportgroupNumber 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOgGroupExtQuery(TelSystemSupportgroupNumber telSystemSupportgroupNumber)
    {
        return ogGroupExtQueryMapper.updateOgGroupExtQuery(telSystemSupportgroupNumber);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOgGroupExtQueryByIds(String ids)
    {
        return ogGroupExtQueryMapper.deleteOgGroupExtQueryByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOgGroupExtQueryById(String id)
    {
        return ogGroupExtQueryMapper.deleteOgGroupExtQueryById(id);
    }
}
