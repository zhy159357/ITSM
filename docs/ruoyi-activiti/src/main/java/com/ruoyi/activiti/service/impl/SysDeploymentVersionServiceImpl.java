package com.ruoyi.activiti.service.impl;

import java.util.List;

import com.ruoyi.activiti.domain.SysDeploymentVersion;
import com.ruoyi.activiti.mapper.SysDeploymentVersionMapper;
import com.ruoyi.activiti.service.ISysDeploymentVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.text.Convert;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-11-18
 */
@Service
public class SysDeploymentVersionServiceImpl implements ISysDeploymentVersionService
{
    @Autowired
    private SysDeploymentVersionMapper sysDeploymentVersionMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param dvId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public SysDeploymentVersion selectSysDeploymentVersionById(String dvId)
    {
        return sysDeploymentVersionMapper.selectSysDeploymentVersionById(dvId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sysDeploymentVersion 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SysDeploymentVersion> selectSysDeploymentVersionList(SysDeploymentVersion sysDeploymentVersion)
    {
        return sysDeploymentVersionMapper.selectSysDeploymentVersionList(sysDeploymentVersion);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param sysDeploymentVersion 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertSysDeploymentVersion(SysDeploymentVersion sysDeploymentVersion)
    {
        return sysDeploymentVersionMapper.insertSysDeploymentVersion(sysDeploymentVersion);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param sysDeploymentVersion 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateSysDeploymentVersion(SysDeploymentVersion sysDeploymentVersion)
    {
        return sysDeploymentVersionMapper.updateSysDeploymentVersion(sysDeploymentVersion);
    }

    /**
     * 删除【请填写功能名称】对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysDeploymentVersionByIds(String ids)
    {
        return sysDeploymentVersionMapper.deleteSysDeploymentVersionByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param dvId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteSysDeploymentVersionById(String dvId)
    {
        return sysDeploymentVersionMapper.deleteSysDeploymentVersionById(dvId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param sysDeploymentVersion 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SysDeploymentVersion> selectSysDeploymentVersionListNew(SysDeploymentVersion sysDeploymentVersion)
    {
        return sysDeploymentVersionMapper.selectSysDeploymentVersionListNew(sysDeploymentVersion);
    }
}
