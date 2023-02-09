package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.SjFetchDemand;

import java.util.List;

/**
 * 【数据提取需求单】Service接口
 *
 * @author ruoyi
 * @date 2021-11-23
 */
public interface ISjFetchDemandService
{
    /**
     * 查询【数据提取需求单】
     *
     * @param fetchDemandId 【数据提取需求单】ID
     * @return 【数据提取需求单】
     */
    public SjFetchDemand selectSjFetchDemandById(String fetchDemandId);

    /**
     * 查询【数据提取需求单】列表
     *
     * @param sjFetchDemand 【数据提取需求单】
     * @return 【数据提取需求单】集合
     */
    public List<SjFetchDemand> selectSjFetchDemandList(SjFetchDemand sjFetchDemand);

    /**
     * 新增【数据提取需求单】
     *
     * @param sjFetchDemand 【数据提取需求单】
     * @return 结果
     */
    public int insertSjFetchDemand(SjFetchDemand sjFetchDemand);

    /**
     * 修改【数据提取需求单】
     *
     * @param sjFetchDemand 【数据提取需求单】
     * @return 结果
     */
    public int updateSjFetchDemand(SjFetchDemand sjFetchDemand);

    /**
     * 批量删除【数据提取需求单】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSjFetchDemandByIds(String ids);

    /**
     * 删除【数据提取需求单】信息
     *
     * @param fetchDemandId 【数据提取需求单】ID
     * @return 结果
     */
    public int deleteSjFetchDemandById(String fetchDemandId);

    SjFetchDemand selectSjFetchDemandByProcessId(String processId);

    List<SjFetchDemand> selectListByTask(SjFetchDemand sjFetchDemand);

    public int updateSjFetchDemandStatus(SjFetchDemand sjFetchDemand);
}
