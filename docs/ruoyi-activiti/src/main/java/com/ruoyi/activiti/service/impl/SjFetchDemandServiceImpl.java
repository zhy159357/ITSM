package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.domain.SjFetchDemand;
import com.ruoyi.activiti.mapper.SjFetchDemandMapper;
import com.ruoyi.activiti.service.ISjFetchDemandService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2021-11-23
 */
@Service
public class SjFetchDemandServiceImpl implements ISjFetchDemandService
{
    @Autowired
    private SjFetchDemandMapper sjFetchDemandMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    /**
     * 查询【数据提取需求单】
     *
     * @param fetchDemandId 【数据提取需求单】ID
     * @return 【数据提取需求单】
     */
    @Override
    public SjFetchDemand selectSjFetchDemandById(String fetchDemandId)
    {
        if("mysql".equals(dbType)){
            return sjFetchDemandMapper.selectSjFetchDemandByIdMysql(fetchDemandId);
        }else{
            return sjFetchDemandMapper.selectSjFetchDemandById(fetchDemandId);
        }
    }

    /**
     * 查询【数据提取需求单】列表
     *
     * @param sjFetchDemand 【数据提取需求单】
     * @return 【数据提取需求单】
     */
    @Override
    public List<SjFetchDemand> selectSjFetchDemandList(SjFetchDemand sjFetchDemand)
    {
        if("mysql".equals(dbType)){
            return sjFetchDemandMapper.selectSjFetchDemandListMysql(sjFetchDemand);
        }else{
            return sjFetchDemandMapper.selectSjFetchDemandList(sjFetchDemand);
        }
    }

    /**
     * 新增【数据提取需求单】
     *
     * @param sjFetchDemand 【数据提取需求单】
     * @return 结果
     */
    @Override
    public int insertSjFetchDemand(SjFetchDemand sjFetchDemand)
    {
        return sjFetchDemandMapper.insertSjFetchDemand(sjFetchDemand);
    }

    /**
     * 修改【数据提取需求单】
     *
     * @param sjFetchDemand 【数据提取需求单】
     * @return 结果
     */
    @Override
    public int updateSjFetchDemand(SjFetchDemand sjFetchDemand)
    {
        return sjFetchDemandMapper.updateSjFetchDemand(sjFetchDemand);
    }

    /**
     * 删除【数据提取需求单】对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSjFetchDemandByIds(String ids)
    {
        return sjFetchDemandMapper.deleteSjFetchDemandByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除【数据提取需求单】信息
     *
     * @param fetchDemandId 【数据提取需求单】ID
     * @return 结果
     */
    @Override
    public int deleteSjFetchDemandById(String fetchDemandId)
    {
        return sjFetchDemandMapper.deleteSjFetchDemandById(fetchDemandId);
    }

    @Override
    public SjFetchDemand selectSjFetchDemandByProcessId(String processId) {
        if("mysql".equals(dbType)){
            return sjFetchDemandMapper.selectSjFetchDemandByProcessIdMysql(processId);
        }else{
            return sjFetchDemandMapper.selectSjFetchDemandByProcessId(processId);
        }
    }

    @Override
    public List<SjFetchDemand> selectListByTask(SjFetchDemand sjFetchDemand) {
        return sjFetchDemandMapper.selectListByTask(sjFetchDemand);
    }

    @Override
    public int updateSjFetchDemandStatus(SjFetchDemand sjFetchDemand) {
        return sjFetchDemandMapper.updateSjFetchDemandStatus(sjFetchDemand);
    }
}
