package com.ruoyi.activiti.mapper;

import com.ruoyi.common.core.domain.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 参数类表Mapper接口
 * @author dayong_sun
 * @date 2020-12-06
 */
public interface DutyViewMapper
{

    /**
     * 查询列表
     * @param dutyScheduling 参数类表
     * @return 参数类表集合
     */
    public List<DutyScheduling> selectViewListByDutyDate(DutyScheduling dutyScheduling);

    /**
     * 查询版本列表
     * @param dutyVersion 版本
     * @return 集合
     */
    public List<DutyVersion> selectVersionByParentId(DutyVersion dutyVersion);

    /**
     * 查询版本数据
     * @param dutyVersion 版本信息
     * @return 版本数据集合信息
     */
    public List<DutyVersion> selectCalendar(DutyVersion dutyVersion);
    public List<DutyVersion> selectCalendarMysql(DutyVersion dutyVersion);

    /**
     * 根据排班信息查询版本数据
     * @param dutySubstitute 版本信息
     * @return 版本数据集合信息
     */
    public List<DutyVersion> selectSchedulingById(DutySubstitute dutySubstitute);

    /**
     * 根据排班信息查询版本数据
     * @param dutySubstitute 版本信息
     * @return 版本数据集合信息
     */
    public List<DutyVersion> selectSchedulingByNotId(DutySubstitute dutySubstitute);

    /**
     * 根据排班信息查询版本数据
     * @param dutySubstitute 版本信息
     * @return 版本数据集合信息
     */
    public List<DutyVersion> exportSchedulingList(DutySubstitute dutySubstitute);

    /**
     * 新增替班申请
     * @param dutySubstitute 参数列表
     * @return 结果
     */
    public DutySubstitute selectSubstituteBySchedulingId(DutySubstitute dutySubstitute);

    List<DutyScheduling> selectViewListByDutyDateAndTypeNo(@Param("dutyDate") String dutyDate,@Param("typeNo") String key);
}
