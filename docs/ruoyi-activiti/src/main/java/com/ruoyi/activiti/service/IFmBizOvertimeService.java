package com.ruoyi.activiti.service;

import com.ruoyi.activiti.domain.FmBizOverTime;

import java.util.List;

/**
 * 业务事件单考核超时Service接口
 *
 * @author ruoyi
 * @date 2021-03-15
 */
public interface IFmBizOvertimeService {
    /**
     * 查询业务事件单考核超时
     *
     * @param singelNumber 业务事件单考核超时ID
     * @return 业务事件单考核超时
     */
    public FmBizOverTime selectFmBizOvertimeById(String singelNumber);

    /**
     * 查询业务事件单考核超时列表
     *
     * @param fmBizOvertime 业务事件单考核超时
     * @return 业务事件单考核超时集合
     */
    public List<FmBizOverTime> selectFmBizOvertimeList(FmBizOverTime fmBizOvertime);

    /**
     * 新增业务事件单考核超时
     *
     * @param fmBizOvertime 业务事件单考核超时
     * @return 结果
     */
    public int insertFmBizOvertime(FmBizOverTime fmBizOvertime);

    /**
     * 修改业务事件单考核超时
     *
     * @param fmBizOvertime 业务事件单考核超时
     * @return 结果
     */
    public int updateFmBizOvertime(FmBizOverTime fmBizOvertime);

    /**
     * 批量删除业务事件单考核超时
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmBizOvertimeByIds(String ids);

    /**
     * 删除业务事件单考核超时信息
     *
     * @param singelNumber 业务事件单考核超时ID
     * @return 结果
     */
    public int deleteFmBizOvertimeById(String singelNumber);

    /**
     * 根据传入月份查询是否存在数据
     *
     * @param monthStr 月份
     * @return
     */
    public String existData(String monthStr);

    /**
     * 根据月份生成考核数据
     *
     * @param monthStr 月份
     */
    void createData(String monthStr);

    /**
     * 根据月份生成紧急事件单考核数据
     *
     * @param monthStr 月份
     */
    void getFmBizOvertimeDates(String monthStr);
/*
    void problemCount(String monthStr);*/
}
