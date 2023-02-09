package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.SjFetchDemand;

import java.util.List;

/**
 * 【数据提取 需求单】Mapper接口
 *
 * @author ruoyi
 * @date 2021-11-23
 */
public interface SjFetchDemandMapper
{
    /**
     * 查询【数据提取需求单】
     *
     * @param fetchDemandId 【数据提取需求单】ID
     * @return 【查询数据提取需求单】
     */
    public SjFetchDemand selectSjFetchDemandById(String fetchDemandId);

    public SjFetchDemand selectSjFetchDemandByIdMysql(String fetchDemandId);
    /**
     * 查询【数据提取需求单】列表
     *
     * @param sjFetchDemand 【数据提取需求单】
     * @return 【数据提取需求单】集合
     */
    public List<SjFetchDemand> selectSjFetchDemandList(SjFetchDemand sjFetchDemand);

    public List<SjFetchDemand> selectSjFetchDemandListMysql(SjFetchDemand sjFetchDemand);

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
     * 删除【数据提取需求单】
     *
     * @param fetchDemandId 【数据提取需求单】ID
     * @return 结果
     */
    public int deleteSjFetchDemandById(String fetchDemandId);

    /**
     * 批量删除【数据提取需求单】
     *
     * @param fetchDemandIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSjFetchDemandByIds(String[] fetchDemandIds);

    SjFetchDemand selectSjFetchDemandByProcessId(String processId);

    SjFetchDemand selectSjFetchDemandByProcessIdMysql(String processId);

    List<SjFetchDemand> selectListByTask(SjFetchDemand sjFetchDemand);

    public int updateSjFetchDemandStatus(SjFetchDemand sjFetchDemand);
}
