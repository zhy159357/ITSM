package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.TelSupportPeropleExtension;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2021-05-19
 */
public interface IOgPersonExtQueryService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public TelSupportPeropleExtension selectOgPersonExtQueryById(String id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param telSupportPeropleExtension 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TelSupportPeropleExtension> selectOgPersonExtQueryList(TelSupportPeropleExtension telSupportPeropleExtension);

    /**
     * 新增【请填写功能名称】
     * 
     * @param telSupportPeropleExtension 【请填写功能名称】
     * @return 结果
     */
    public int insertOgPersonExtQuery(TelSupportPeropleExtension telSupportPeropleExtension);

    /**
     * 修改【请填写功能名称】
     * 
     * @param telSupportPeropleExtension 【请填写功能名称】
     * @return 结果
     */
    public int updateOgPersonExtQuery(TelSupportPeropleExtension telSupportPeropleExtension);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOgPersonExtQueryByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOgPersonExtQueryById(String id);
}
