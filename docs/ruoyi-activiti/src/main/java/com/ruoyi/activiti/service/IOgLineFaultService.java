package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.SysLine;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author ruoyi
 * @date 2021-07-20
 */
public interface IOgLineFaultService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param hardwareId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public SysLine selectOgLineFaultById(String hardwareId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sysLine 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SysLine> selectOgLineFaultList(SysLine sysLine);

    /**
     * 新增【请填写功能名称】
     * 
     * @param sysLine 【请填写功能名称】
     * @return 结果
     */
    public int insertOgLineFault(SysLine sysLine);

    /**
     * 修改【请填写功能名称】
     * 
     * @param sysLine 【请填写功能名称】
     * @return 结果
     */
    public int updateOgLineFault(SysLine sysLine);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOgLineFaultByIds(String ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param hardwareId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOgLineFaultById(String hardwareId);
}
