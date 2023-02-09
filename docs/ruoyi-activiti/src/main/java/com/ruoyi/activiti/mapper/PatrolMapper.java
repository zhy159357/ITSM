package com.ruoyi.activiti.mapper;

import java.util.List;
import com.ruoyi.activiti.domain.PatrolInspection;

/**
 * 舆情监测系统巡检单Mapper接口
 *
 */
public interface PatrolMapper
{

    /**
     * 舆情监测系统巡检单列表
     * 
     * @param patrolInspection 巡检单
     * @return 巡检单集合
     */
    public List<PatrolInspection> selectPatrolList(PatrolInspection patrolInspection);

    public List<PatrolInspection> selectPatrolListMysql(PatrolInspection patrolInspection);

    public PatrolInspection selectPatrolListByIdMysql(String patrolId);

    public PatrolInspection selectPatrolListById(String patrolId);

    public int insertPatrolInspection(PatrolInspection patrolInspection);

    public int updatePatrolInspection(PatrolInspection patrolInspection);

    public int deletePatrolByIds(String[] ids);
}
