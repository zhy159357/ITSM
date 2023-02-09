package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.TelSystemSupportgroupNumber;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2021-05-19
 */
public interface IOgGroupExtQueryService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public TelSystemSupportgroupNumber selectOgGroupExtQueryById(String id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param telSystemSupportgroupNumber 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TelSystemSupportgroupNumber> selectOgGroupExtQueryList(TelSystemSupportgroupNumber telSystemSupportgroupNumber);

    /**
     * 新增【请填写功能名称】
     * 
     * @param telSystemSupportgroupNumber 【请填写功能名称】
     * @return 结果
     */
    public int insertOgGroupExtQuery(TelSystemSupportgroupNumber telSystemSupportgroupNumber);

    /**
     * 修改【请填写功能名称】
     * 
     * @param telSystemSupportgroupNumber 【请填写功能名称】
     * @return 结果
     */
    public int updateOgGroupExtQuery(TelSystemSupportgroupNumber telSystemSupportgroupNumber);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOgGroupExtQueryByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOgGroupExtQueryById(String id);
}
