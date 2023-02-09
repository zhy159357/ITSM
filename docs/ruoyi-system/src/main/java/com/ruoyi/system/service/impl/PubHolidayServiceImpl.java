package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.PubHoliday;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.PubHolidayMapper;
import com.ruoyi.system.service.IPubHolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PubHolidayServiceImpl implements IPubHolidayService {

    @Autowired
    private PubHolidayMapper holidayMapper;

    @Value("${pagehelper.helperDialect}")
    private String dbType;

    //根据分页条件查出节假日信息
    @Override
//    @DataScope(deptAlias = "d", userAlias = "u")
    public List<PubHoliday> selectHolidayList(PubHoliday holiday) {
        if("mysql".equals(dbType)){
            return holidayMapper.selectHolidayListMysql(holiday);
        }else{
            return holidayMapper.selectHolidayList(holiday);
        }
    }

    //通过节假日id查询节假日信息
    @Override
    public Object selectholidayById(String holidayId) {
        if("mysql".equals(dbType)){
            return holidayMapper.selectholidayByIdMysql(holidayId);
        }else{
            return holidayMapper.selectholidayById(holidayId);
        }

    }

    /**
     * 批量删除节假日信息
     *
     * @param ids 需要删除的数据ID
     * @throws Exception
     */
    @Override
    public int deleteHolidayByIds(String ids) throws BusinessException {
        String[] holidayIds = Convert.toStrArray(ids);
        for (String holidayId : holidayIds) {
            PubHoliday holiday = (PubHoliday) selectholidayById(holidayId);

        }
        return holidayMapper.deleteHolidayByIds(holidayIds);
    }

    /**
     * 新增节假日
     *
     * @param pubHoliday
     * @return
     */
    @Override
    public int insert(PubHoliday pubHoliday) {
        if("mysql".equals(dbType)){
            return holidayMapper.insertMysql(pubHoliday);
        }else{
            return holidayMapper.insert(pubHoliday);
        }
    }

    /**
     * 修改
     *
     * @param holidayoliday
     * @return
     */
    @Override
    public int edit(PubHoliday holidayoliday) {
        if("mysql".equals(dbType)){
            return holidayMapper.editMysql(holidayoliday);
        }else{
            return holidayMapper.edit(holidayoliday);
        }
    }

    /**
     * 根据开始时间和结束时间，去除节假日和非工作时间计算时长（秒）
     * start
     *
     * @param startTimeStr 格式为yyyyMMddHHmmss
     * @param endTimeStr 格式为yyyyMMddHHmmss
     * @return
     */
    static SimpleDateFormat hms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMdd");
    static SimpleDateFormat ymd_ = new SimpleDateFormat("yyyy-MM-dd");
    static List<String> holidays = new ArrayList<String>();// 节假日
    static List<String> workDays = new ArrayList<String>();// 工作日

    public String getNextDay(Date day) {
        Calendar c = Calendar.getInstance();
        c.setTime(day);
        c.add(Calendar.DAY_OF_MONTH, 1);
        String nextDay = ymd_.format(c.getTime());//后一天
        return nextDay;
    }

    /**
     * 对开始时间进行预处理，使其处于工作日内的工作时间段内。
     * 上班时间8:30,下班时间17:30，工作时长9小时
     * 预处理开始时间，如果开始时间介于00:00-8:30，开始时间设置为当日8:30；介于17:30-23:59,开始时间设置为次日8:30,
     * 如果开始时间为节假日或周末，那么将开始时间向后推移到最近的工作日的8:30。
     */
    public String processStartTime2(String startTimeStr) {
        try {
            //对开始时间进行预处理，使其处于工作日内的工作时间段内
            //String startTimeStr = "2016-09-29 08:00:00";
            Date day = ymd_.parse(startTimeStr);//当前日期2016-09-29
            String sTime = ymd_.format(day);//当前日期"2016-09-29"
            String timeStr1 = sTime + " 08:30:00";//上班时间2016-09-29 08:30:00
            String timeStr2 = sTime + " 17:30:00";//下班时间2016-09-29 17:30:00
            String timeStr3 = getNextDay(day) + " 08:30:00";//次日上班时间2016-09-30 08:30:00
            if (startTimeStr.compareTo(timeStr1) < 0) {
                //开始时间小于当日上班时间，将开始时间赋值为当日上班时间
                startTimeStr = timeStr1;
            } else if (startTimeStr.compareTo(timeStr2) >= 0) {
                //开始时间大于等于当日下班时间，将开始时间赋值为次日上班时间
                startTimeStr = timeStr3;
            }
            return startTimeStr;
        } catch (ParseException e) {

            return "如期格式转换失败";
        }
    }

    public static String formatDateStr(String date, String format, String toFormat) {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        SimpleDateFormat tofmt = new SimpleDateFormat(toFormat);
        Date d;
        try {
            d = fmt.parse(date);
            return tofmt.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PubHoliday> getHolidays() {
        List<PubHoliday> list = holidayMapper.getHolidays();
        return list;
    }

    public List<PubHoliday> getWorkdays() {
        List<PubHoliday> list = holidayMapper.getWorkdays();
        return list;
    }

    public List<String> getHolidayList() {
        List<String> hs = new ArrayList<String>();// 节假日
        List<PubHoliday> pubHolidayList = getHolidays();//获取该年份下配置的法定节假日
        if (pubHolidayList.size() > 0) {
            //已经配置节假日，遍历，取得节假日日期
            for (PubHoliday pubHoliday : pubHolidayList) {
                try {
                    hs.add(ymd_.format(ymd.parse(pubHoliday.getDay())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return hs;
    }

    public List<String> getWorkdayList() {
        List<String> hs = new ArrayList<String>();// 工作日
        List<PubHoliday> pubHolidayList = getWorkdays();//获取该年份下配置的法定工作日
        if (pubHolidayList.size() > 0) {
            //已经配置工作日日，遍历，取得工作日日期
            for (PubHoliday pubHoliday : pubHolidayList) {
                try {
                    hs.add(ymd_.format(ymd.parse(pubHoliday.getDay())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return hs;
    }

    //判断是否是节假日
    public boolean isHoliday(Date day) {
        if (CollectionUtils.isEmpty(holidays)) {
            holidays = getHolidayList();
        }
        if (CollectionUtils.isEmpty(workDays)) {
            workDays = getWorkdayList();
        }
        boolean isHoliday = false;
        Calendar c = Calendar.getInstance();
        c.setTime(day);
        if ((c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || holidays.contains(ymd_.format(day))) {
            if (!workDays.contains(ymd_.format(day))) {
                isHoliday = true;
            }
        }
        return isHoliday;
    }

    /**
     * 如果开始时间为节假日或周末，那么将开始时间向后推移到最近的工作日的8:30
     *
     * @param startTimeStr
     */
    public String getWorkDayStartTime(String startTimeStr) {
        String workDayStartTime = startTimeStr;
        try {
            Date startTime = ymd_.parse(startTimeStr);
            boolean isH = isHoliday(startTime);//判断是否是节假日
            if (isH) {
                //是节假日，将日期向后移动一天，判断是否是节假日
                startTimeStr = getNextDay(startTime) + " 08:30:00";
                workDayStartTime = getWorkDayStartTime(startTimeStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return workDayStartTime;
    }

    /**
     * 对开始时间进行预处理，使其处于工作日内的工作时间段内。
     * 上班时间8:30,下班时间17:30，工作时长9小时
     * 预处理开始时间，如果开始时间介于00:00-8:30，开始时间设置为当日8:30；介于17:30-23:59,开始时间设置为次日8:30,
     * 如果开始时间为节假日或周末，那么将开始时间向后推移到最近的工作日的8:30。
     */
    public String processStartTime(String startTimeStr) {
        try {
            //对开始时间进行预处理，使其处于工作日内的工作时间段内
            //String startTimeStr = "2016-09-29 08:00:00";
            Date day = ymd_.parse(startTimeStr);//当前日期2016-09-29
            String sTime = ymd_.format(day);//当前日期"2016-09-29"
            String timeStr1 = sTime + " 08:30:00";//上班时间2016-09-29 08:30:00
            String timeStr2 = sTime + " 17:30:00";//下班时间2016-09-29 17:30:00
            String timeStr3 = getNextDay(day) + " 08:30:00";//次日上班时间2016-09-30 08:30:00
            if (startTimeStr.compareTo(timeStr1) < 0) {
                //开始时间小于当日上班时间，将开始时间赋值为当日上班时间
                startTimeStr = timeStr1;
            } else if (startTimeStr.compareTo(timeStr2) >= 0) {
                //开始时间大于等于当日下班时间，将开始时间赋值为次日上班时间
                startTimeStr = timeStr3;
            }
            //如果开始时间为节假日或周末，那么将开始时间向后推移到最近的工作日的8:30。
            return getWorkDayStartTime(startTimeStr);

        } catch (ParseException e) {
            return "";
        }
    }

    public static String getPreDay(Date day) {
        Calendar c = Calendar.getInstance();
        c.setTime(day);
        c.add(Calendar.DAY_OF_MONTH, -1);
        String preDay = ymd_.format(c.getTime());//前一天
        return preDay;
    }

    /**
     * 如果结束时间为节假日或周末，那么将结束时间向后推移到最近的工作日的17:30
     *
     * @param endTimeStr
     */
    public String getWorkDayEndTime(String endTimeStr) {
        String workDayEndTime = endTimeStr;
        try {
            Date endTime = ymd_.parse(endTimeStr);
            boolean isH = isHoliday(endTime);//判断是否是节假日
            if (isH) {
                //是节假日，将日期向前移动一天，判断是否是节假日
                endTimeStr = getPreDay(endTime) + " 17:30:00";
                workDayEndTime = getWorkDayEndTime(endTimeStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return workDayEndTime;
    }

    /**
     * 对结束时间进行预处理，使其处于工作日内的工作时间段内。
     * 上班时间8:30,下班时间17:30，工作时长9小时。
     * 预处理结束时间，如果结束时间介于17:30-次日8:30,结束时间设置为17:30,
     * 如果结束时间为节假日或周末，那么将结束时间向前推移到最近的工作日的17:30。
     */
    public String processEndTime(String endTimeStr) {
        try {
            //结束时间进行预处理，使其处于工作日内的工作时间段内
            //String endTimeStr = "2016-09-29 08:00:00";
            Date day = ymd_.parse(endTimeStr);//当前日期2016-09-29
            String sTime = ymd_.format(day);//当前日期"2016-09-29"
            String timeStr1 = sTime + " 08:30:00";//当日上班时间2016-09-29 08:30:00
            String timeStr2 = sTime + " 17:30:00";//当日下班时间2016-09-29 17:30:00
            String timeStr3 = getPreDay(day) + " 17:30:00";//前一天下班时间2016-09-28 17:30:00
            if (endTimeStr.compareTo(timeStr1) < 0) {
                //结束时间小于当日上班时间，将结束时间赋值为前一天下班时间
                endTimeStr = timeStr3;
            } else if (endTimeStr.compareTo(timeStr2) >= 0) {
                //结束时间大于等于当日下班时间，将结束时间赋值为当日下班时间
                endTimeStr = timeStr2;
            }
            //如果结束时间为节假日或周末，那么将结束时间向前推移到最近的工作日的17:30。
            return getWorkDayEndTime(endTimeStr);

        } catch (ParseException e) {
            return "";
        }

    }

    public Map<String, Integer> getHolidayCount(String startTimeStr, String endTimeStr) {
        try {
            int holidayCount = 0;//节假日数量
            int workdayCount = 0;//工作日数量
            Map<String, Integer> countMap = new HashMap<String, Integer>();
            Date startTime = ymd_.parse(startTimeStr);
            Date endTime = ymd_.parse(endTimeStr);
            while (startTime.compareTo(endTime) < 0) {
                if (isHoliday(startTime)) {
                    holidayCount++;
                } else {
                    workdayCount++;
                }
                startTime = ymd_.parse(getNextDay(startTime));
            }
            countMap.put("holidayCount", holidayCount);
            countMap.put("workdayCount", workdayCount);
            return countMap;
        } catch (ParseException e) {
            return null;
        }
    }

    public String processEndTime2(String endTimeStr) {
        try {
            //结束时间进行预处理，使其处于工作日内的工作时间段内
            Date day = ymd_.parse(endTimeStr);//当前日期2016-09-29
            String sTime = ymd_.format(day);//当前日期"2016-09-29"
            String timeStr1 = sTime + " 08:30:00";//当日上班时间2016-09-29 08:30:00
            String timeStr2 = sTime + " 17:30:00";//当日下班时间2016-09-29 17:30:00
            String timeStr3 = getPreDay(day) + " 17:30:00";//前一天下班时间2016-09-28 17:30:00
            if (endTimeStr.compareTo(timeStr1) < 0) {
                //结束时间小于当日上班时间，将结束时间赋值为前一天下班时间
                endTimeStr = timeStr3;
            } else if (endTimeStr.compareTo(timeStr2) >= 0) {
                //结束时间大于等于当日下班时间，将结束时间赋值为当日下班时间
                endTimeStr = timeStr2;
            }
            return endTimeStr;

        } catch (ParseException e) {
            return "";
        }
    }

    public Map<String, Integer> getHolidayCount2(String startTimeStr, String endTimeStr) {
        try {
            int holidayCount = 0;//节假日数量
            int workdayCount = 0;//工作日数量
            Map<String, Integer> countMap = new HashMap<String, Integer>();
            Date startTime = ymd_.parse(startTimeStr);
            Date endTime = ymd_.parse(endTimeStr);
            while (startTime.compareTo(endTime) < 0) {
                workdayCount++;
                startTime = ymd_.parse(getNextDay(startTime));
            }
            countMap.put("holidayCount", holidayCount);
            countMap.put("workdayCount", workdayCount);
            return countMap;
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public long getWorkDayUsedTimes(String startTimeStr, String endTimeStr) {
        long usedTimes = 0;
        startTimeStr = formatDateStr(startTimeStr, "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
        endTimeStr = formatDateStr(endTimeStr, "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
        holidays = getHolidayList();
        workDays = getWorkdayList();
        startTimeStr = processStartTime(startTimeStr);//预处理开始时间
        endTimeStr = processEndTime(endTimeStr);//预处理结束时间
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = hms.parse(startTimeStr);
            endTime = hms.parse(endTimeStr);
            /*
             * 开始时间、结束时间预处理之后的情况：
             * 1.开始时间大于等于结束时间，（开始时间和结束时间处于非工作日或工作日的非工作时段）
             * 2.开始时间小于结束时间。（结束时间-开始时间）/1000-（节假日天数*24*60*60）-（工作日天数*15*60*60）
             */
            if (startTimeStr.compareTo(endTimeStr) >= 0) {
                //预处理之后，开始时间大于等于结束时间，用时为0（开始时间和结束时间处于非工作日或工作日的非工作时段）
                usedTimes = 0;
            } else {
                //开始时间小于结束时间
                //计算开始时间与结束时间段之间的时长（秒）
                usedTimes = (endTime.getTime() - startTime.getTime()) / 1000;
                //计算开始时间和结束时间包括的节假日天数和工作日天数
                Map<String, Integer> countMap = getHolidayCount(startTimeStr, endTimeStr);
                int holidayCount = countMap.get("holidayCount");//节假日数量
                int workdayCount = countMap.get("workdayCount");//工作日数量
                usedTimes = usedTimes - (holidayCount * 24 * 60 * 60) - (workdayCount * 15 * 60 * 60);
            }
            return usedTimes;
        } catch (ParseException e1) {
            return 0;
        }
    }

    @Override
    public long getWorkDaySysTimes(String type, String startTimeStr, String endTimeStr) {
        long usedTimes = 0;
        startTimeStr = formatDateStr(startTimeStr, "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
        endTimeStr = formatDateStr(endTimeStr, "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
        //A不需要预处理时间B去预处理非工作时间CD预处理节假日周末和非工作时间
        if ("2".equals(type)) {
            startTimeStr = processStartTime2(startTimeStr);//预处理开始时间
            endTimeStr = processEndTime2(endTimeStr);//预处理结束时间
        }
        if ("3".equals(type) || "4".equals(type)) {
            startTimeStr = processStartTime(startTimeStr);//预处理开始时间
            endTimeStr = processEndTime(endTimeStr);//预处理结束时间
        }

        Date startTime = null;
        Date endTime = null;
        try {
            startTime = hms.parse(startTimeStr);
            endTime = hms.parse(endTimeStr);
            /*
             * 开始时间、结束时间预处理之后的情况：
             * 1.开始时间大于等于结束时间，（开始时间和结束时间处于非工作日或工作日的非工作时段）
             * 2.开始时间小于结束时间。（结束时间-开始时间）/1000-（节假日天数*24*60*60）-（工作日天数*15*60*60）
             */
            if (startTimeStr.compareTo(endTimeStr) >= 0) {
                //预处理之后，开始时间大于等于结束时间，用时为0（开始时间和结束时间处于非工作日或工作日的非工作时段）
                usedTimes = 0;
            } else {
                //开始时间小于结束时间
                //计算开始时间与结束时间段之间的时长（秒）
                usedTimes = (endTime.getTime() - startTime.getTime()) / 1000;
                //计算开始时间和结束时间包括的节假日天数和工作日天数
                if ("2".equals(type)) {
                    Map<String, Integer> countMap = getHolidayCount2(startTimeStr, endTimeStr);
                    int holidayCount = countMap.get("holidayCount");//节假日数量
                    int workdayCount = countMap.get("workdayCount");//工作日数量
                    usedTimes = usedTimes - (holidayCount * 24 * 60 * 60) - (workdayCount * 15 * 60 * 60);
                }
                if ("3".equals(type) || "4".equals(type)) {
                    Map<String, Integer> countMap = getHolidayCount(startTimeStr, endTimeStr);
                    int holidayCount = countMap.get("holidayCount");//节假日数量
                    int workdayCount = countMap.get("workdayCount");//工作日数量
                    usedTimes = usedTimes - (holidayCount * 24 * 60 * 60) - (workdayCount * 15 * 60 * 60);
                }

            }
            return usedTimes;
        } catch (ParseException e1) {
            return 0;
        }
    }

    public static String getPreDay(Date day, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(day);
        c.add(Calendar.HOUR, -hour);
        String preDay = hms.format(c.getTime());//前一天
        return preDay;
    }

    @Override
    public String getdateBefore(int hour, String preDate) {
        try {
            String preTime = "";
            String date = "";
            int times = 0;
            if (hour == 12) {

                preDate = processEndTime(preDate);

                date = preDate.split(" ")[0] + " " + "08:30:00";

                preTime = preDate.split(" ")[1];

                Date day = hms.parse(preDate);

                if (day.getTime() - hms.parse(date).getTime() >= 3 * 60 * 60 * 1000) {
                    preDate = getPreDay(hms.parse(preDate), 9);
                    // 往前推一天
                    preDate = getPreDay(ymd_.parse(preDate)) + " " + preTime;

                    preDate = getWorkDayPreTime(preDate, preTime);

//					System.out
//							.println("------preDate333333---------" + preDate);

                    SimpleDateFormat dateformat = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    // 加上一小时
                    preDate = dateformat
                            .format(hms.parse(preDate).getTime() - 60 * 60 * 3000);

                } else {

                    // 往前推一天
                    preDate = getPreDay(ymd_.parse(preDate)) + " " + preTime;

                    preDate = getWorkDayPreTime(preDate, preTime);
                    preDate = getPreDay(ymd_.parse(preDate)) + " " + preTime;
                    preDate = getWorkDayPreTime(preDate, preTime);

                    SimpleDateFormat dateformat = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    // 加上一小时
                    preDate = dateformat
                            .format(hms.parse(preDate).getTime() + 60 * 60 * 6000);

                }
            } else {
                times = hour / 8;

                for (int i = 0; i < times; i++) {

                    preDate = processEndTime(preDate);
                    date = preDate.split(" ")[0] + " " + "08:30:00";
                    preTime = preDate.split(" ")[1];
                    Date day = hms.parse(preDate);
                    if (day.getTime() - hms.parse(date).getTime() >= 8 * 60 * 60 * 1000) {
                        preDate = getPreDay(hms.parse(preDate), 8);
                    } else {
                        // 往前推一天
                        preDate = getPreDay(ymd_.parse(preDate)) + " " + preTime;
                        preDate = getWorkDayPreTime(preDate, preTime);

                        SimpleDateFormat dateformat = new SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss");
                        // 加上一小时
                        preDate = dateformat
                                .format(hms.parse(preDate).getTime() + 60 * 60 * 1000);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("日期转换失败");
        }
        return preDate;
    }

    @Override
    public String getWorkDayPreTime(String preDate, String preTime) {
        String workDaypreDate = preDate;
        try {
            Date startTime = ymd_.parse(workDaypreDate);
            boolean isH = isHoliday(startTime);// 判断是否是节假日
            if (isH) {
                if (StringUtils.isEmpty(preTime)) {
                    preDate = getPreDay(startTime) + " 17:30:00";
                } else {
                    preDate = getPreDay(startTime) + " " + preTime;
                }
                workDaypreDate = getWorkDayPreTime(preDate, preTime);
            }
        } catch (ParseException e) {
            System.out.println("日期转换失败");
        }
        return workDaypreDate;
    }

    @Override
    public String getTimeByToQgzxTime(String toQgzxTime) {
        //如果时间为节假日或周末，那么将结束时间向前推移到最近48小时工作时间之前
        String workDayTime = getWorkDayTime(toQgzxTime, "1");
        for (int i = 0; i < 2; i++) {
            workDayTime = getWorkDayTime(DateUtils.getSubtractionDay(workDayTime, 1), "2");
        }
        return workDayTime;
    }

    public String getWorkDayTime(String toQgzxTime, String flag) {
        String workDayTime = toQgzxTime;
        try {
            Date endTime = ymd_.parse(toQgzxTime);
            boolean isH = isHoliday(endTime);//判断是否是节假日
            if (isH) {
                //是节假日，将日期向前移动一天，判断是否是节假日
                if ("1".equals(flag)) {//判断是否第一次调用
                    toQgzxTime = getPreDay(endTime) + " 23:59:59";
                    workDayTime = getWorkDayTime(toQgzxTime, "1");
                } else {
                    toQgzxTime = getPreDay(hms.parse(toQgzxTime), 24);
                    workDayTime = getWorkDayTime(toQgzxTime, "2");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return workDayTime;
    }

    public static void main(String[] args) {
        PubHolidayServiceImpl pl = new PubHolidayServiceImpl();
        String l = pl.getTimeByToQgzxTime("2021-10-06 13:33:00");
//        long l=pl.getWorkDaySysTimes("3", "20200722100915", "20200803101343");
        System.out.println("计算后的转全国中心时间是：" + l);
    }

}
