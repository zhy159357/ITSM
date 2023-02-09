package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.PubHoliday;

import java.util.List;

public interface PubHolidayMapper {


   public List<PubHoliday> selectHolidayList(PubHoliday holiday);

    public List<PubHoliday> selectHolidayListMysql(PubHoliday holiday);

    Object selectholidayById(String holidayId);

    Object selectholidayByIdMysql(String holidayId);

    //
    int deleteHolidayByIds(String[] holidayIds);

    /**
     * 新增
     * @param pubHoliday
     * @return
     */
    int insert(PubHoliday pubHoliday);

    int insertMysql(PubHoliday pubHoliday);

 /**
  * 修改
  * @param holidayoliday
  * @return
  */
    int edit(PubHoliday holidayoliday);

    int editMysql(PubHoliday holidayoliday);
//    int countUserHolidayById(Long holidayId);

 /**
  * 查询休息日期
  * @return
  */
 public List<PubHoliday> getHolidays();

 /**
  * 查询工作日期
  * @return
  */
 public List<PubHoliday> getWorkdays();
}
