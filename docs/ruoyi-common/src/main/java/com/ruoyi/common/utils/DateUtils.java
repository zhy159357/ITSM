package com.ruoyi.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 时间工具类
 *
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    //日志记录
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DateUtils.class);
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYYMM = "yyyyMM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDD = "yyyyMMdd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM",
            YYYYMMDDHHMMSS};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String dateFromLongToStr(final String format, final String toFormat, final String ts) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat(format);
            SimpleDateFormat format2 = new SimpleDateFormat(toFormat);
            Date newDate = format1.parse(ts);
            return format2.format(newDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }


    public static final String dateTimeStr() {
        Date now = new Date();
        return DateFormatUtils.format(now, "HHmmss");
    }

    public static final String dateTimeHHmmStr() {
        Date now = new Date();
        return DateFormatUtils.format(now, "HH:mm");
    }

    /**
     * 日期路径 即年/月 如202012
     */
    public static final String dateYearMonth() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMM");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            if (str.toString().contains("Z")) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
                return format.parse(str.toString().replace("Z", " UTC"));
            } else {
                if (str.toString().length() == 10) {
                    SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
                    Date date = new Date(Long.valueOf(str.toString() + "000"));
                    return parseDate(format.format(date), parsePatterns);
                } else {
                    return parseDate(str.toString(), parsePatterns);
                }
            }
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 计算两个时间差
     */
    public static Map<String, Long> getDateDiff(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long second = diff % nd % nh % nm / ns;
        Map<String, Long> map = new HashMap<>();
        map.put("day", day);
        map.put("hour", hour);
        map.put("min", min);
        map.put("second", second);
        return map;
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss类型的时间格式化为yyyyMMddHHmmss
     *
     * @param dateStr
     * @return
     */
    public static String handleTimeYYYYMMDDHHMMSS(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return dateStr;
        }
        Date date = parseDate(dateStr);
        String formatDateStr = parseDateToStr(YYYYMMDDHHMMSS, date);
        return formatDateStr;
    }

    /**
     * 将yyyy-MM-dd类型的时间格式化为yyyyMMdd
     *
     * @param dateStr
     * @return
     */
    public static String handleTimeYYYYMMDD(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return dateStr;
        }
        Date date = parseDate(dateStr);
        String formatDateStr = parseDateToStr(YYYYMMDD, date);
        return formatDateStr;
    }

    public static String handleTimeYYYYMMDDHHMMSS(Date date) {
        if (date == null) {
            return null;
        }
        return parseDateToStr(YYYYMMDDHHMMSS, date);
    }

    /**
     * 缺省日期格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";
    /**
     * 缺省日期格式
     */
    public static final String DEFAULT_DATE_FORMAT2 = "yyyy-MM-dd";
    /**
     * 缺省时间格式
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    /**
     * 缺省月格式
     */
    public static final String DEFAULT_MONTH = "MONTH";

    /**
     * 缺省年格式
     */
    public static final String DEFAULT_YEAR = "YEAR";

    /**
     * 缺省日格式
     */
    public static final String DEFAULT_DATE = "DAY";
    public static final String DEFAULT_HOUR = "HOUR";
    public static final String DEFAULT_MINUTE = "MINUTE";
    public static final String DEFAULT_SECOND = "SECOND";

    /**
     * String日期转Date
     *
     * @param dateStr
     * @param format
     * @return 转换失败返回null
     */
    public static Date parseDate(String dateStr, String format) {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        try {
            return fmt.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static long diffTime(Date date, Date date2) {
        long diffTime = date.getTime() - date2.getTime();
        return diffTime / 1000;
    }

    /**
     * 时间加减处理
     *
     * @param date
     * @param field
     * @param amount
     * @return
     */
    public static Date add(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 时间加减处理
     *
     * @param dateStr
     * @param format
     * @param field
     * @param amount
     * @return
     */
    public static String add(String dateStr, String format, int field, int amount) {
        Date date = parseDate(dateStr, format);
        date = add(date, field, amount);
        return formatDate(date, format);
    }


    /**
     * 格式化时间
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat tofmt = new SimpleDateFormat(format);
        return tofmt.format(date);
    }

    public static String getDateStr(int year, int month, int day, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return formatDate(calendar.getTime(), format);
    }

    public static String getDateStr(int year, int month, int day) {
        return getDateStr(year, month, day, "yyyyMMdd");
    }

    public static String getTimeStr(int hour, int minute, int second, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return formatDate(calendar.getTime(), format);
    }

    public static String getTimeStr(int hour, int minute, int second) {
        return getTimeStr(hour, minute, second, "HHmmss");
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

    /**
     * 功能：对于给定的时间增加天数/月数/年数后的日期,按指定格式输出
     *
     * @param date      String 要改变的日期
     * @param field     int 日期改变的字段，YEAR,MONTH,DAY
     * @param amount    int改变量
     * @param strFormat 日期返回格式
     * @return
     * @throws ParseException
     * @author caohongbin
     */
    public static String getAddDay(String date, String field, int amount, String strFormat) {
        // 当前日期和前一天
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(strFormat);

        Calendar rightNow = Calendar.getInstance();
        Date tempdate;
        String day = "";
        try {
            tempdate = formatter.parse(date);
            rightNow.setTime(tempdate);

            int intField = 0;
            String tmpField = field.toUpperCase();

            intField = Calendar.DATE;
            if (tmpField.equals(DEFAULT_YEAR))
                intField = Calendar.YEAR;
            if (tmpField.equals(DEFAULT_MONTH))
                intField = Calendar.MONTH;
            if (tmpField.equals(DEFAULT_DATE))
                intField = Calendar.DATE;
            if (tmpField.equals(DEFAULT_HOUR))
                intField = Calendar.HOUR;
            if (tmpField.equals(DEFAULT_MINUTE))
                intField = Calendar.MINUTE;
            if (tmpField.equals(DEFAULT_SECOND))
                intField = Calendar.SECOND;

            rightNow.add(intField, amount);
            day = formatter.format(rightNow.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return day;
    }

    /**
     * 获得给定日期sDate所在周，星期几的日期值
     *
     * @param sDate
     * @param sFormat
     * @param week    1-星期一, 7-星期日
     * @return
     * @throws ParseException
     */
    public static String currentWeekDate(String sDate, String sFormat, int week) throws ParseException {
        Calendar c = Calendar.getInstance();
        if (week == 7) {
            sDate = DateUtils.getAddDay(sDate, "DAY", 7, sFormat);
        }
        c.setTime(DateUtils.parseDate(sDate, sFormat));

        switch (week) {
            case 1:
                c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                break;
            case 2:
                c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                break;
            case 3:
                c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                break;
            case 4:
                c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                break;
            case 5:
                c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                break;
            case 6:
                c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                break;
            case 7:
                c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                break;
        }

        return new SimpleDateFormat("yyyyMMdd").format(c.getTime());
    }

    /**
     * 获得给定日期sDate之后星期几的日期值
     *
     * @param sDate   给定日期
     * @param sFormat
     * @param week    1-星期一, 7-星期日
     * @return
     */
    public static String afterWeekDate(String sDate, String sFormat, int week) {
        String currentWeekDate = null;
        String afterWeekDate = null;
        try {
            currentWeekDate = DateUtils.currentWeekDate(sDate, sFormat, week);
            if (DateUtils.parseDate(currentWeekDate, sFormat).compareTo(DateUtils.parseDate(sDate, sFormat)) == -1) {
                afterWeekDate = DateUtils.getAddDay(currentWeekDate, "DAY", 7, sFormat);
                afterWeekDate = DateUtils.currentWeekDate(afterWeekDate, "yyyyMMdd", week);

                return afterWeekDate;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentWeekDate;
    }

    /**
     * 获得给定年加1
     *
     * @param year 给定年
     * @return
     */
    public static String addYear(String year) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.set(Integer.valueOf(year), 1, 1);
        rightNow.add(Calendar.YEAR, 1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        return formatter.format(rightNow.getTime());
    }

    /**
     * 指定时间加一天
     *
     * @param date
     * @return
     */
    public static Date addDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * 指定时间增加小时分钟
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date addHourMinute(Date date, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    public static void main(String[] args) {
        String s = convertLongTimeString(1665736396000L);
        System.out.println(s);
    }

    public static String getToday() {
        String time = "";
        time = getToday("yyyy-MM-dd");
        return time;
    }

    public static String getToday(String format) {
        return getDateStr(Calendar.getInstance().getTime(), format);
    }

    public static String getDateStr(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    /**
     * 计算时间差
     *
     * @param time 传入日期
     * @param day  减去天数
     * @return
     */
    public static final String getday5(String time, int day) {
        String date = add(time, "yyyyMMddHHmmss", Calendar.DATE, -day);
        return date;
    }

    /**
     * 字符串yyyyMMddHHmmss转yyyy-MM-dd HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String timeToTimeMillis(String time) {
        if (StringUtils.isEmpty(time)) {
            return time;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime ldt = LocalDateTime.parse(time, dtf);
        DateTimeFormatter fa = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String datetime2 = ldt.format(fa);
        return datetime2;
    }

    /**
     * 字符串yyyyMMddHHmmss转yyyy-MM-dd
     *
     * @param time
     * @return
     */
    public static String timeMillis(String time) {
        if (StringUtils.isEmpty(time)) {
            return time;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime ldt = LocalDateTime.parse(time, dtf);
        DateTimeFormatter fa = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String datetime2 = ldt.format(fa);
        return datetime2;
    }

    /**
     * 将yyyy-MM-dd类型的时间格式化为yyyyMMdd
     *
     * @param dateStr
     * @return
     */
    public static String handleTimeYYYYMM(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return dateStr;
        }
        Date date = parseDate(dateStr);
        String formatDateStr = parseDateToStr(YYYYMM, date);
        return formatDateStr;
    }

    public static String formatStartTime(String startTime) {
        return handleTimeYYYYMMDD(startTime) + "000000";
    }

    public static String formatEndTime(String endTime) {
        return handleTimeYYYYMMDD(endTime) + "235959";
    }

    /**
     * 根据传入参数计算减去天数之前的日期
     *
     * @param time yyyy-MM-dd HH:mm:ss 格式的日期
     * @param day  减去天数
     * @return
     */
    public static final String getSubtractionDay(String time, int day) {
        String date = add(time, "yyyy-MM-dd HH:mm:ss", Calendar.DATE, -day);
        return date;
    }

    public static String convertLongTimeString(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        return formatDate(c.getTime(), YYYY_MM_DD_HH_MM_SS);
    }
    
	public static String convertWeekDayStart(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		// 获得当前日期是一个星期的第几天
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayWeek == 1) {
			dayWeek = 8;
		}
		// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
		Date mondayDate = cal.getTime();
		String weekBegin = sdf.format(mondayDate);
		System.out.println("所在周星期一的日期：" + weekBegin);
		return weekBegin;
	}

	public static String convertWeekDayEnd(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		// 获得当前日期是一个星期的第几天
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayWeek == 1) {
			dayWeek = 8;
			cal.add(Calendar.DATE, 5 + cal.getFirstDayOfWeek());
		}else {
			cal.add(Calendar.DATE, 4 + cal.getFirstDayOfWeek());
		}
		Date sundayDate = cal.getTime();
		String weekEnd = sdf.format(sundayDate);
		System.out.println("所在周星期日的日期：" + weekEnd);
		return weekEnd;
	}
}
