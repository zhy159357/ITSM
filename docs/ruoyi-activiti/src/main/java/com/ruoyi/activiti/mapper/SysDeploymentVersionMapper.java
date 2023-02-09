package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.SysDeploymentVersion;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author ruoyi
 * @date 2021-11-18
 */
public interface SysDeploymentVersionMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param dvId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public SysDeploymentVersion selectSysDeploymentVersionById(String dvId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param sysDeploymentVersion 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SysDeploymentVersion> selectSysDeploymentVersionList(SysDeploymentVersion sysDeploymentVersion);

    /**
     * 新增【请填写功能名称】
     * 
     * @param sysDeploymentVersion 【请填写功能名称】
     * @return 结果
     */
    public int insertSysDeploymentVersion(SysDeploymentVersion sysDeploymentVersion);

    /**
     * 修改【请填写功能名称】
     * 
     * @param sysDeploymentVersion 【请填写功能名称】
     * @return 结果
     */
    public int updateSysDeploymentVersion(SysDeploymentVersion sysDeploymentVersion);

    /**
     * 删除【请填写功能名称】
     * 
     * @param dvId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteSysDeploymentVersionById(String dvId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param dvIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysDeploymentVersionByIds(String[] dvIds);

    List<SysDeploymentVersion> selectSysDeploymentVersionListNew(SysDeploymentVersion sysDeploymentVersion);
}
