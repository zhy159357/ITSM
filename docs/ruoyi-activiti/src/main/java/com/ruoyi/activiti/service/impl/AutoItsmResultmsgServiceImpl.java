package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.AutoItsmResultmsgMapper;
import com.ruoyi.activiti.service.IAutoItsmResultmsgService;
import com.ruoyi.common.core.domain.entity.AutoItsmResultmsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

import java.util.List;

/**
 * Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-03-22
 */
@Service
public class AutoItsmResultmsgServiceImpl implements IAutoItsmResultmsgService
{
    @Autowired
    private AutoItsmResultmsgMapper autoItsmResultmsgMapper;

    /**
     * 查询
     * 
     * @param resultId ID
     * @return
     */
    @Override
    public AutoItsmResultmsg selectAutoItsmResultmsgById(String resultId)
    {
        return autoItsmResultmsgMapper.selectAutoItsmResultmsgById(resultId);
    }

    /**
     * 查询列表
     * 
     * @param autoItsmResultmsg
     * @return
     */
    @Override
    public List<AutoItsmResultmsg> selectAutoItsmResultmsgList(AutoItsmResultmsg autoItsmResultmsg)
    {
        return autoItsmResultmsgMapper.selectAutoItsmResultmsgList(autoItsmResultmsg);
    }

    /**
     * 新增
     *
     * @param autoItsmResultmsg
     * @return 结果
     */
    @Override
    public int insertAutoItsmResultmsg(AutoItsmResultmsg autoItsmResultmsg)
    {
        return autoItsmResultmsgMapper.insertAutoItsmResultmsg(autoItsmResultmsg);
    }

    /**
     * 修改
     * 
     * @param autoItsmResultmsg
     * @return 结果
     */
    @Override
    public int updateAutoItsmResultmsg(AutoItsmResultmsg autoItsmResultmsg)
    {
        return autoItsmResultmsgMapper.updateAutoItsmResultmsg(autoItsmResultmsg);
    }

    /**
     * 删除对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAutoItsmResultmsgByIds(String ids)
    {
        return autoItsmResultmsgMapper.deleteAutoItsmResultmsgByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除信息
     * 
     * @param resultId ID
     * @return 结果
     */
    @Override
    public int deleteAutoItsmResultmsgById(String resultId)
    {
        return autoItsmResultmsgMapper.deleteAutoItsmResultmsgById(resultId);
    }
}
