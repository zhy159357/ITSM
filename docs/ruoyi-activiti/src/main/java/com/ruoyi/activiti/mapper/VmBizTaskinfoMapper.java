package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.VmBizTaskinfo;

import java.util.List;

/**
 * 版本任务Mapper接口
 * 
 * @author ruoyi
 * @date 2021-01-06
 */
public interface VmBizTaskinfoMapper 
{
    /**
     * 查询版本任务
     * 
     * @param taskId 版本任务ID
     * @return 版本任务
     */
    public VmBizTaskinfo selectVmBizTaskinfoById(String taskId);

    /**
     * 查询版本任务
     *
     * @param taskNo 版本任务编号
     * @return 版本任务
     */
    public VmBizTaskinfo selectVmBizTaskinfoByTaskNo(String taskNo);

    /**
     * 查询版本任务列表
     * 
     * @param vmBizTaskinfo 版本任务
     * @return 版本任务集合
     */
    public List<VmBizTaskinfo> selectVmBizTaskinfoList(VmBizTaskinfo vmBizTaskinfo);

    /**
     * 查询版本任务列表
     *
     * @param vmBizTaskinfo 版本任务
     * @return 版本任务集合
     */
    public List<VmBizTaskinfo> selectVmBizTaskinfoListUnion(VmBizTaskinfo vmBizTaskinfo);

    /**
     * 新增版本任务
     * 
     * @param vmBizTaskinfo 版本任务
     * @return 结果
     */
    public int insertVmBizTaskinfo(VmBizTaskinfo vmBizTaskinfo);

    /**
     * 修改版本任务
     * 
     * @param vmBizTaskinfo 版本任务
     * @return 结果
     */
    public int updateVmBizTaskinfo(VmBizTaskinfo vmBizTaskinfo);

    /**
     * 删除版本任务
     * 
     * @param taskId 版本任务ID
     * @return 结果
     */
    public int deleteVmBizTaskinfoById(String taskId);

    /**
     * 批量删除版本任务
     * 
     * @param taskIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteVmBizTaskinfoByIds(String[] taskIds);
}
