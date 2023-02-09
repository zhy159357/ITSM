package com.ruoyi.activiti.service;

import java.util.List;
import com.ruoyi.activiti.domain.PatrolInspection;

/**
 * 舆情监测系统巡检单Service接口
 */
public interface PatrolService {

    /**
     * 查询舆情监测系统巡检单列表
     *
     * @param patrolInspection 集合
     * @return 巡检单集合
     */
    public List<PatrolInspection> selectPatrolList(PatrolInspection patrolInspection);

    public PatrolInspection selectPatrolListById(String patrolId);

    public int insertPatrolInspection(PatrolInspection patrolInspection);

    public int updatePatrolInspection(PatrolInspection patrolInspection);

    public int deletePatrolByIds(String ids);
}
