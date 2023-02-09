package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.AutoItsmFastfileMapper;
import com.ruoyi.activiti.service.IAutoItsmFastfileService;
import com.ruoyi.common.core.domain.entity.AutoItsmFastfile;
import com.ruoyi.common.core.text.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author ruoyi
 * @date 2021-03-21
 */
@Service
public class AutoItsmFastfileServiceImpl implements IAutoItsmFastfileService
{
    @Autowired
    private AutoItsmFastfileMapper autoItsmFastfileMapper;

    /**
     *
     * @param uuid
     * @return
     */
    @Override
    public AutoItsmFastfile selectAutoItsmFastfileById(String uuid)
    {
        return autoItsmFastfileMapper.selectAutoItsmFastfileById(uuid);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param autoItsmFastfile
     * @return
     */
    @Override
    public List<AutoItsmFastfile> selectAutoItsmFastfileList(AutoItsmFastfile autoItsmFastfile)
    {
        return autoItsmFastfileMapper.selectAutoItsmFastfileList(autoItsmFastfile);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param autoItsmFastfile
     * @return 结果
     */
    @Override
    public int insertAutoItsmFastfile(AutoItsmFastfile autoItsmFastfile)
    {
        return autoItsmFastfileMapper.insertAutoItsmFastfile(autoItsmFastfile);
    }

    /**
     *
     * @param autoItsmFastfile
     * @return 结果
     */
    @Override
    public int updateAutoItsmFastfile(AutoItsmFastfile autoItsmFastfile)
    {
        return autoItsmFastfileMapper.updateAutoItsmFastfile(autoItsmFastfile);
    }

    /**
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAutoItsmFastfileByIds(String ids)
    {
        return autoItsmFastfileMapper.deleteAutoItsmFastfileByIds(Convert.toStrArray(ids));
    }

    /**
     *
     * @param uuid 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteAutoItsmFastfileById(String uuid)
    {
        return autoItsmFastfileMapper.deleteAutoItsmFastfileById(uuid);
    }
}
