package com.ruoyi.activiti.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.DutyScheduling;
import com.ruoyi.common.core.domain.entity.DutyTypeinfo;
import com.ruoyi.common.core.domain.entity.DutyVersion;
import com.ruoyi.common.core.domain.entity.OgUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service接口
 * @author dayong_sun
 * @date 2021-07-01
 */
public interface IDutyInfoService
{
    /**
     * 统一监控调用接口
     * @param startTime 开始值班时间
     * @param endTime 结束值班时间
     * @param typeNo 类编编码
     * @return
     */
    public List<Map<String,Object>> selectDutyInfoList(String startTime, String endTime, String typeNo);

    /**
     * 通过柜员号获取User信息
     * @param custNo 柜员号
     * @return
     */
    public OgUser selectOgUserByCustNo(String custNo);

    /**
     * 值班日历日期高亮接口
     * @param custNo   柜员号
     * @param dutyDate 时间
     * @return
     */
    public AjaxResult selectDutyCalendar(String custNo, String dutyDate);

    /**
     * 获取值班类型的前端查询项集合
     * @return
     */
    public AjaxResult getDutyTypeInfoChooseList();

    /**
     * 获取值班的excel 接口
     * @param dutyDate 月份时间
     * @param typeNo   值班大类型（全部、白班、夜班、领导）
     * @return
     */
    public AjaxResult getDutyCalendarExcel(String dutyDate, String typeNo);
}
