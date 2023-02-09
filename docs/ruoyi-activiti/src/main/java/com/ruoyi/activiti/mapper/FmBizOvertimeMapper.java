package com.ruoyi.activiti.mapper;

import com.ruoyi.activiti.domain.FmBizOverTime;

import java.util.List;

/**
 * 业务事件单考核超时Mapper接口
 *
 * @author ruoyi
 * @date 2021-03-15
 */
public interface FmBizOvertimeMapper {
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
     * 删除业务事件单考核超时
     *
     * @param singelNumber 业务事件单考核超时ID
     * @return 结果
     */
    public int deleteFmBizOvertimeById(String singelNumber);

    /**
     * 批量删除业务事件单考核超时
     *
     * @param singelNumbers 需要删除的数据ID
     * @return 结果
     */
    public int deleteFmBizOvertimeByIds(String[] singelNumbers);

    /**
     * 查询事件单紧急非紧急数量
     * @param fmBizOvertime
     * @return
     */
    public FmBizOverTime findIfjjCount(FmBizOverTime fmBizOvertime);
}
