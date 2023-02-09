package com.ruoyi.activiti.service.impl;

import com.ruoyi.activiti.mapper.PatrolMapper;
import com.ruoyi.common.core.text.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ruoyi.activiti.domain.PatrolInspection;
import com.ruoyi.activiti.service.PatrolService;
import java.util.List;

/**
 * Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-12-16
 */
@Service
public class PatrolServiceImpl implements PatrolService
{
    @Autowired
    private PatrolMapper patrolMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;


    /**
     * 查询舆情监测系统巡检单列表
     * 
     * @param  patrolInspection 巡检单
     * @return 巡检单
     */
    @Override
    public List<PatrolInspection> selectPatrolList(PatrolInspection patrolInspection)
    {

        if("mysql".equals(dbType)){
            return patrolMapper.selectPatrolListMysql(patrolInspection);
        }else{
            return patrolMapper.selectPatrolList(patrolInspection);
        }
    }
    public PatrolInspection  selectPatrolListById(String patrolId){
        if("mysql".equals(dbType)){
            return patrolMapper.selectPatrolListByIdMysql(patrolId);
        }else{
            return patrolMapper.selectPatrolListById(patrolId);
        }
    }
    public int insertPatrolInspection(PatrolInspection patrolInspection){
        return patrolMapper.insertPatrolInspection(patrolInspection);
    }

    public int updatePatrolInspection(PatrolInspection patrolInspection){
        return patrolMapper.updatePatrolInspection(patrolInspection);
    }

    public int deletePatrolByIds(String ids){
        return patrolMapper.deletePatrolByIds(Convert.toStrArray(ids));
    }

}
