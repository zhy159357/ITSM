package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.PubHoliday;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IPubHolidayService {


    //列表页面展示
    List<PubHoliday> selectHolidayList(PubHoliday holiday);


    Object selectholidayById(String holidayId);

    //
    public int deleteHolidayByIds(String ids);

    /**
     * 新增
     *
     * @param pubHoliday
     * @return
     */
    int insert(PubHoliday pubHoliday);


    /**
     * 修改节假日
     *
     * @param holiday
     * @return
     */
    int edit(PubHoliday holiday);

    /**
     * 根据开始结束时间计算排除非工作时间后的有效时间
     *
     * @param startTimeStr
     * @param endTimeStr
     * @return
     */
    public long getWorkDayUsedTimes(String startTimeStr, String endTimeStr);

    /*
     * 业务事件单使用[根据开始结束时间和系统类别计算排除非工作时间后的有效时间]
     * A 7*24 C 7*8 CD 5*8
     */
    public long getWorkDaySysTimes(String type, String startTimeStr, String endTimeStr);

    /**
     * 计算向前8、16、24小时
     *
     * @param hour
     * @param preDate
     * @return
     */
    public String getdateBefore(int hour, String preDate);

    public String getWorkDayPreTime(String preDate, String preTime);

    /**
     * 根据传入时间计算5*24下48小时之前的时间点
     * @param toQgzxTime
     * @return
     */
    public String getTimeByToQgzxTime(String toQgzxTime);
}
