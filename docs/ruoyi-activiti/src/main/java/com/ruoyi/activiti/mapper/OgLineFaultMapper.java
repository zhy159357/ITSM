package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.SysLine;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2021-07-20
 */
public interface OgLineFaultMapper 
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

    public List<SysLine> selectOgLineFaultListMysql(SysLine sysLine);

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
     * 删除【请填写功能名称】
     * 
     * @param hardwareId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOgLineFaultById(String hardwareId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param hardwareIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOgLineFaultByIds(String[] hardwareIds);
}
