package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.domain.SysLine;
import com.ruoyi.activiti.mapper.OgLineFaultMapper;
import com.ruoyi.activiti.service.IOgLineFaultService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-07-20
 */
@Service
public class OgLineFaultServiceImpl implements IOgLineFaultService
{
    @Autowired
    private OgLineFaultMapper ogLineFaultMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     * 查询【请填写功能名称】
     * 
     * @param hardwareId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public SysLine selectOgLineFaultById(String hardwareId)
    {
        return ogLineFaultMapper.selectOgLineFaultById(hardwareId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sysLine 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SysLine> selectOgLineFaultList(SysLine sysLine)
    {
        if("mysql".equals(dbType)){
            return ogLineFaultMapper.selectOgLineFaultListMysql(sysLine);
        }else{
            return ogLineFaultMapper.selectOgLineFaultList(sysLine);
        }
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param sysLine 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOgLineFault(SysLine sysLine)
    {
        sysLine.setCreateTime(DateUtils.getNowDate());
        return ogLineFaultMapper.insertOgLineFault(sysLine);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param sysLine 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOgLineFault(SysLine sysLine)
    {
        return ogLineFaultMapper.updateOgLineFault(sysLine);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOgLineFaultByIds(String ids)
    {
        return ogLineFaultMapper.deleteOgLineFaultByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param hardwareId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOgLineFaultById(String hardwareId)
    {
        return ogLineFaultMapper.deleteOgLineFaultById(hardwareId);
    }
}
