package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.AutoItsmFastfile;

import java.util.List;

/**
 *
 * @author ruoyi
 * @date 2021-03-21
 */
public interface AutoItsmFastfileMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param uuid 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public AutoItsmFastfile selectAutoItsmFastfileById(String uuid);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param autoItsmFastfile 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<AutoItsmFastfile> selectAutoItsmFastfileList(AutoItsmFastfile autoItsmFastfile);

    /**
     * 新增【请填写功能名称】
     * 
     * @param autoItsmFastfile 【请填写功能名称】
     * @return 结果
     */
    public int insertAutoItsmFastfile(AutoItsmFastfile autoItsmFastfile);

    /**
     * 修改【请填写功能名称】
     * 
     * @param autoItsmFastfile 【请填写功能名称】
     * @return 结果
     */
    public int updateAutoItsmFastfile(AutoItsmFastfile autoItsmFastfile);

    /**
     * 删除【请填写功能名称】
     * 
     * @param uuid 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteAutoItsmFastfileById(String uuid);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param uuids 需要删除的数据ID
     * @return 结果
     */
    public int deleteAutoItsmFastfileByIds(String[] uuids);
}
