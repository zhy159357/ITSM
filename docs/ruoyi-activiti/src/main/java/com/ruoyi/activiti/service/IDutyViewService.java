package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 值班视图Service接口
 * @author dayong_sun
 * @date 2020-01-19
 */
public interface IDutyViewService
{
    /**
     * 查询值班视图
     * @param dutyScheduling 值班视图ID
     * @return 值班视图
     */
    public List<DutyScheduling> selectDutySchedulingList(DutyScheduling dutyScheduling);

    /**
     * 查询值班视图列表
     * @param dutyVersion 值班视图
     * @return 值班视图集合
     */
    public Map<String,Object> selectVersionByDutyDate(DutyVersion dutyVersion);
    public Map<String,Object> selectVersionByDutyDateMysql(DutyVersion dutyVersion);

    /**
     * 查询值班视图列表
     * @param dutyVersion 值班视图
     * @return 值班视图集合
     */
    public List<DutyVersion> selectCalendar(DutyVersion dutyVersion);

    /**
     * 查询值班视图列表
     * @param dutyDate 值班视图
     * @return 值班视图集合
     */
    public List<DutyVersion> selectSchedulingById(String dutyDate);

    /**
     * 查询值班视图列表
     * @return 值班视图集合
     */
    public AjaxResult exportExcel(DutyVersion dutyVersion);
    public AjaxResult exportExcelMysql(DutyVersion dutyVersion);


    /**
     * 新增排班信息
     * @param dutySubstitute 排班信息
     * @return 结果
     */
    public int addCheck(DutySubstitute dutySubstitute);

    /**
     * 新增排班信息
     * @param dutySubstitute 排班信息
     * @return 结果
     */
    public int addDateCheck(DutySubstitute dutySubstitute);

    /**
     * 新增排班信息
     * @param dutySubstitute 替班信息
     * @return 结果
     */
    public int insertDutySubstitute(DutySubstitute dutySubstitute);

}
