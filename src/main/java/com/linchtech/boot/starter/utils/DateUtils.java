package com.linchtech.boot.starter.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;


public final class DateUtils {

    /**
     * 隐藏构造器
     */
    private DateUtils() {

    }

    public static final TimeZone ZONE_GMT_8 = TimeZone.getTimeZone("ETC/GMT-8");
    public static final TimeZone ZONE_GMT = TimeZone.getTimeZone("GMT");
    public static final String YEAR_PATTERN = "yyyy";
    public static final String MONTH_PATTERN = "yyyy-MM";
    public static final String MONTH_PATTERN_1 = "yyyy年MM月";
    public static final String MONTH_PATTERN_SPECIAL = "yyyy_MM";
    public static final String MONTH_PATTERN_SPECIAL2 = "yyyyMM";
    public static final String MONTH_PATTERN_SPECIAL3 = "yyyy.MM";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_PATTERN_SLASH = "yyyy/MM/dd";
    public static final String DATE_PATTERN_SPOT = "yyyy.MM.dd";
    public static final String YEAR_MONTH_DAY_PATTERN = "yyyyMMdd";
    public static final String MONTH_DAY_H_PATTERN = "MM-dd";
    public static final String MONTH_DAY_PATTERN = "MMdd";
    public static final String YEAR_WEEK_PATTERN = "yyyyww";

    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String YEAR_MONTH_DAY_HOUR_PATTERN = "yyMMddHH";
    public static final String FORMAT_DATETIME_WITH_NUMBER = "yyyyMMddHH";
    public static final String MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String MINUTE_PATTERN2 = "yyyy-M-d HH:mm";
    public static final String MINUTE_PATTERN3 = "yyyy年MM月dd日 HH:mm:ss";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_PATTERN2 = "yyyy-M-d HH:mm:ss";
    public static final String DATE_TIME_PATTERN3 = "yyyy.MM.dd HH:mm:ss";
    public static final String DATE_TIME_PATTERN_SLASH = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_TIME_PATTERN_NUMBER = "yyyyMMddHHmmss";
    public static final String DATE_TIME_PATTERN_WITH_MINL = "yyyy-MM-dd HH:mm:ss.SSS"; // 2021-02-20 18:00:00.555
    public static final String DATE_TIME_PATTERN_8601 = "yyyy-MM-dd'T'HH:mm:ssXXX";     // 2021-02-20T18:00:00+08:00, 2021-02-20T18:00:00Z
    public static final String DATE_TIME_PATTERN_8601_1 = "yyyy-MM-dd'T'HH:mm:ssZ";     // 2021-02-20T18:00:00+0800
    public static final String DATE_TIME_PATTERN_8601_DEFAULT_TZ = "yyyy-MM-dd'T'HH:mm:ss";       // 2021-02-20T18:00:00
    public static final String DATE_TIME_PATTERN_8601_WITH_MINL = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"; // 2021-02-20T18:00:00.555+08:00, 2021-02-20T18:00:00.555Z
    public static final String DATE_TIME_PATTERN_8601_WITH_MINL_1 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"; // 2021-02-20T18:00:00.555+0800

    public static final String CRON_PATTERN = "ss mm HH dd MM ? yyyy";

    public static final Pattern DATE_TIME_TZ = Pattern.compile("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{0,}Z");

    /**
     * 日期时间格式
     */
    public static final String[] DATETIME_PATTERNS = new String[]{
            MONTH_PATTERN,
            MONTH_PATTERN_1,
            YEAR_MONTH_DAY_PATTERN,
            DATE_TIME_PATTERN_SLASH,
            DATE_PATTERN_SLASH,
            MINUTE_PATTERN2,
            MINUTE_PATTERN3,
            DATE_TIME_PATTERN2,
            DATE_TIME_PATTERN3,
            DATE_PATTERN,
            MINUTE_PATTERN,
            DATE_TIME_PATTERN,
            DATE_TIME_PATTERN_WITH_MINL,
            DATE_TIME_PATTERN_8601,
            DATE_TIME_PATTERN_8601_1,
            DATE_TIME_PATTERN_8601_DEFAULT_TZ,
            DATE_TIME_PATTERN_8601_WITH_MINL,
            DATE_TIME_PATTERN_8601_WITH_MINL_1
    };

    /**
     * 判断是否日期格式（"yyyy-MM-dd'T'HH:mm:ss'Z'"）
     *
     * @param s str
     * @return boolean
     */
    public static boolean checkTZ(String s) {
        return DATE_TIME_TZ.matcher(s).matches();
    }

    /**
     * UTC转北京时间
     *
     * @param dateStr dateStr
     * @param format  format
     * @return date
     * @throws ParseException ParseException
     */
    public static Date utc2cst(String dateStr, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date date = sdf.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
        return calendar.getTime();
    }

    /**
     * 计算两个日期的日期差
     *
     * @param dateOne dateOne
     * @param dateTwo dateTwo
     * @return long
     */
    public static long getBetweenDays(Date dateOne, Date dateTwo) {
        long from = dayBegin(dateOne).getTime();
        long to = dayBegin(dateTwo).getTime();
        long days = (to - from) / (24 * 3600 * 1000);
        return days;
    }

    /**
     * 计算两个时间的小时差
     *
     * @param dateOne dateOne
     * @param dateTwo dateTwo
     * @return long
     * @throws
     * @author WangHan
     * @date 2018/8/22 20:16
     */
    public static long getBetweenHours(Date dateOne, Date dateTwo) {
        long from = dateOne.getTime();
        long to = dateTwo.getTime();
        return (to - from) / (1000 * 60 * 60);
    }

    /**
     * 计算两个时间的分钟差
     *
     * @param dateOne dateOne
     * @param dateTwo dateTwo
     * @return long
     * @throws
     * @author WangHan
     * @date 2018/8/22 20:16
     */
    public static long getBetweenMinutes(Date dateOne, Date dateTwo) {
        long from = dateOne.getTime();
        long to = dateTwo.getTime();
        return (to - from) / (1000 * 60);
    }

    /**
     * 计算两个时间的秒数差
     *
     * @param dateOne dateOne
     * @param dateTwo dateTwo
     * @return long
     * @throws
     * @author WangHan
     * @date 2018/8/22 20:16
     */
    public static long getBetweenSeconds(Date dateOne, Date dateTwo) {
        long from = dateOne.getTime();
        long to = dateTwo.getTime();
        return (to - from) / 1000;
    }

    /**
     * 返回特定日期的零点时间 </br> 2018-01-18 00:00:00
     *
     * @param date date
     * @return Date
     */
    public static Date dayBegin(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        return c.getTime();
    }

    /**
     * 返回特定日期的零点时间 </br> 2018-01-18 00:00:00
     *
     * @param timeStamp date 时间戳
     * @return Date
     */
    public static Date dayBegin(long timeStamp) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(timeStamp));
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        return c.getTime();
    }

    /**
     * 本周第一天
     *
     * @return Date
     * @throws ParseException ParseException
     */
    public static Date getWeekFirstDay() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, 0);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        Date time = cal.getTime();
        return cal.getTime();
    }

    /**
     * 本月第一天
     *
     * @return Date
     * @throws ParseException ParseException
     */
    public static Date getMonthFirstDay() throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.MONTH, 0);
        ca.set(Calendar.DAY_OF_MONTH, 1); //设置为1号,当前日期既为本月第一天
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        return dateParse(sdf.format(ca.getTime()), DATE_PATTERN);
    }

    /**
     * 本年第一天
     *
     * @return Date
     * @throws ParseException ParseException
     */
    public static Date getYearFirstDay() throws ParseException {
        String format = new SimpleDateFormat("yyyy").format(new Date()) + "-01-01";
        return dateParse(format, DATE_PATTERN);
    }

    /**
     * 传入时间的月第一天
     *
     * @param date date
     * @return Date
     * @throws ParseException ParseException
     */
    public static Date getMonthFirstDay(Date date) throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.MONTH, 0);
        ca.set(Calendar.DAY_OF_MONTH, 1); //设置为1号,当前日期既为本月第一天
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        return dateParse(sdf.format(ca.getTime()), DATE_PATTERN);
    }

    /**
     * 本月最后一天
     *
     * @return Date
     * @throws ParseException ParseException
     */
    public static Date getMonthLastDay() throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);

        return dateParse(sdf.format(ca.getTime()), DATE_PATTERN);
    }


    /**
     * 本月最后一天
     *
     * @param date date
     * @return Date
     * @throws ParseException ParseException
     */
    public static Date getMonthLastDay(Date date) throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);

        return dateParse(sdf.format(ca.getTime()), DATE_PATTERN);
    }

    /**
     * 日期相加减天数
     *
     * @param date        如果为Null，则为当前时间
     * @param days        加减天数
     * @param includeTime 是否包括时分秒,true表示包含
     * @return Date
     * @throws ParseException ParseException
     */
    public static Date dateAdd(Date date, int days, boolean includeTime) throws ParseException {
        if (date == null) {
            date = new Date();
        }
        if (!includeTime) {
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_PATTERN);
            date = sdf.parse(sdf.format(date));
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    /**
     * 时间格式化成字符串
     *
     * @param date    Date
     * @param pattern StringUtil.DATE_TIME_PATTERN || StringUtil.DATE_PATTERN， 如果为空，则为yyyy-MM-dd
     * @return String
     * @throws ParseException ParseException
     */
    public static String dateFormat(Date date, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            pattern = DateUtils.DATE_PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 字符串解析成时间对象
     *
     * @param dateTimeString String
     * @return Date
     * @throws ParseException ParseException
     */
    public static Date dateParse(String dateTimeString) throws ParseException {
        return org.apache.commons.lang3.time.DateUtils.parseDate(dateTimeString, DATETIME_PATTERNS);
    }

    /**
     * 字符串解析成时间对象
     *
     * @param dateTimeString String
     * @param pattern        StringUtil.DATE_TIME_PATTERN || StringUtil.DATE_PATTERN，如果为空，则为yyyy-MM-dd
     * @return Date
     * @throws ParseException ParseException
     */
    public static Date dateParse(String dateTimeString, String pattern) throws ParseException {
        if (StringUtils.isBlank(pattern)) {
            pattern = DateUtils.DATE_PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(dateTimeString);
    }

    /**
     * 字符串解析成时间对象
     *
     * @param dateTimeString (- / . :)
     * @return 时间
     * @throws ParseException 解析异常
     */
    public static Date dateParse2(String dateTimeString) throws ParseException {
        if (StringUtils.isBlank(dateTimeString)) {
            return null;
        }
        String pattern = null;
        if (dateTimeString.contains("-")) {
            pattern = DateUtils.DATE_PATTERN;
        } else if (dateTimeString.contains("/")) {
            pattern = DateUtils.DATE_PATTERN_SLASH;
        } else if (dateTimeString.contains(".")) {
            pattern = DateUtils.DATE_PATTERN_SPOT;
        } else if (dateTimeString.contains(":")) {
            pattern = DateUtils.DATE_TIME_PATTERN;
        }
        if (pattern != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateTimeString);
        }
        return null;
    }

    /**
     * 字符串解析成时间对象
     *
     * @param dateTimeString String
     * @param pattern        StringUtil.DATE_TIME_PATTERN || StringUtil.DATE_PATTERN，如果为空，则为yyyy-MM-dd
     * @return Date
     * @throws ParseException ParseException
     */
    public static Date dateParseOfNull(String dateTimeString, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            pattern = DateUtils.DATE_PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateTimeString);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 字符串解析成时间对象
     *
     * @param dateTimeString String
     * @param pattern        StringUtil.DATE_TIME_PATTERN || StringUtil.DATE_PATTERN，如果为空，则为yyyy-MM-dd
     * @param zone           zone
     * @return Date
     * @throws ParseException ParseException
     */
    public static Date dateParse(String dateTimeString, String pattern, TimeZone zone) throws ParseException {
        if (StringUtils.isBlank(pattern)) {
            pattern = DateUtils.DATE_PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(zone);
        return sdf.parse(dateTimeString);
    }


    /**
     * 将日期时间格式成只有日期的字符串（可以直接使用dateFormat，Pattern为Null进行格式化）
     *
     * @param dateTime Date
     * @return String
     */
    public static String dateTimeToDateString(Date dateTime) {
        String dateTimeString = DateUtils.dateFormat(dateTime, DateUtils.DATE_TIME_PATTERN);
        return dateTimeString.substring(0, 10);
    }

    /**
     * 当时、分、秒为00:00:00时，将日期时间格式成只有日期的字符串，
     * 当时、分、秒不为00:00:00时，直接返回
     *
     * @param dateTime Date
     * @return String
     */
    public static String dateTimeToDateStringIfTimeEndZero(Date dateTime) {
        String dateTimeString = DateUtils.dateFormat(dateTime, DateUtils.DATE_TIME_PATTERN);
        if (dateTimeString.endsWith("00:00:00")) {
            return dateTimeString.substring(0, 10);
        } else {
            return dateTimeString;
        }
    }

    /**
     * 将日期时间格式成日期对象，和dateParse互用
     *
     * @param dateTime Date
     * @return Date
     */
    public static Date dateTimeToDate(Date dateTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTime);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 时间加减小时
     *
     * @param startDate 要处理的时间，Null则为当前时间
     * @param hours     加减的小时
     * @return Date
     */
    public static Date dateAddHours(Date startDate, int hours) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.HOUR, c.get(Calendar.HOUR) + hours);
        return c.getTime();
    }

    /**
     * 时间加减分钟
     *
     * @param startDate 要处理的时间，Null则为当前时间
     * @param minutes   加减的分钟
     * @return Date
     */
    public static Date dateAddMinutes(Date startDate, int minutes) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + minutes);
        return c.getTime();
    }

    /**
     * 时间加减秒数
     *
     * @param startDate 要处理的时间，Null则为当前时间
     * @param seconds   加减的秒数
     * @return Date
     */
    public static Date dateAddSeconds(Date startDate, int seconds) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.SECOND, c.get(Calendar.SECOND) + seconds);
        return c.getTime();
    }

    /**
     * 时间加减天数
     *
     * @param startDate 要处理的时间，Null则为当前时间
     * @param days      加减的天数
     * @return Date
     */
    public static Date dateAddDays(Date startDate, int days) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.DATE, c.get(Calendar.DATE) + days);
        return c.getTime();
    }

    /**
     * 时间加减月数
     *
     * @param startDate 要处理的时间，Null则为当前时间
     * @param months    加减的月数
     * @return Date
     */
    public static Date dateAddMonths(Date startDate, int months) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + months);
        return c.getTime();
    }

    /**
     * 时间加减周
     *
     * @param startDate 要处理的时间，Null则为当前时间
     * @param weeks     加减的周数
     * @return Date
     */
    public static Date dateAddWeeks(Date startDate, int weeks) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.WEEK_OF_YEAR, c.get(Calendar.WEEK_OF_YEAR) + weeks);
        return c.getTime();
    }

    /**
     * 时间加减年数
     *
     * @param startDate 要处理的时间，Null则为当前时间
     * @param years     加减的年数
     * @return Date
     */
    public static Date dateAddYears(Date startDate, int years) {
        if (startDate == null) {
            startDate = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) + years);
        return c.getTime();
    }

    /**
     * 时间比较（如果myDate>compareDate返回1，<返回-1，相等返回0）
     *
     * @param myDate      时间
     * @param compareDate 要比较的时间
     * @return int
     */
    public static int dateCompare(Date myDate, Date compareDate) {
        Calendar myCal = Calendar.getInstance();
        Calendar compareCal = Calendar.getInstance();
        if (myDate == null && compareDate == null) {
            return 0;
        } else if (myDate == null) {
            return -1;
        } else if (compareDate == null) {
            return 1;
        }
        myCal.setTime(myDate);
        compareCal.setTime(compareDate);
        return myCal.compareTo(compareCal);
    }

    /**
     * 获取两个时间中最小的一个时间
     *
     * @param date        date
     * @param compareDate compareDate
     * @return Date
     */
    public static Date dateMin(Date date, Date compareDate) {
        if (date == null) {
            return compareDate;
        }
        if (compareDate == null) {
            return date;
        }
        if (1 == dateCompare(date, compareDate)) {
            return compareDate;
        } else if (-1 == dateCompare(date, compareDate)) {
            return date;
        }
        return date;
    }

    /**
     * 获取两个时间中最大的一个时间
     *
     * @param date        date
     * @param compareDate compareDate
     * @return Date
     */
    public static Date dateMax(Date date, Date compareDate) {
        if (date == null) {
            return compareDate;
        }
        if (compareDate == null) {
            return date;
        }
        if (1 == dateCompare(date, compareDate)) {
            return date;
        } else if (-1 == dateCompare(date, compareDate)) {
            return compareDate;
        }
        return date;
    }

    /**
     * 获取两个日期（不含时分秒）相差的天数，不包含今天
     *
     * @param startDate startDate
     * @param endDate   endDate
     * @return int
     */
    public static int dateBetweenYear(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / 1000 / 60 / 60 / 24 / 365);
    }

    /**
     * 获取两个日期（不含时分秒）相差的月
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 两个日期（不含时分秒）相差的月
     */
    public static int dateBetweenMonths(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / 1000 / 60 / 60 / 24 / 30);
    }

    /**
     * 获取两个日期（不含时分秒）相差的天数，不包含今天
     *
     * @param startDate startDate
     * @param endDate   endDate
     * @return 两个日期（不含时分秒）相差的天数
     */
    public static int dateBetween(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / 1000 / 60 / 60 / 24);
    }

    /**
     * 获取两个时间相差的秒数
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 两个时间相差的秒数
     */
    public static long dateBetweenSeconds(Date startDate, Date endDate) {
        return (endDate.getTime() - startDate.getTime()) / 1000;
    }

    /**
     * 获取两个时间相差的毫秒数
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 两个时间相差的毫秒数
     */
    public static long dateBetweenMilliseconds(Date startDate, Date endDate) {
        return endDate.getTime() - startDate.getTime();
    }

    /**
     * @param startDate startDate
     * @param endDate   endDate
     * @return long
     */
    public static long dateBetweenHours(Date startDate, Date endDate) {
        return dateBetweenSeconds(startDate, endDate) / 60;
    }


    /**
     * 获取两个日期（不含时分秒）相差的天数，不包含今天
     *
     * @param date      date
     * @param startDate startDate
     * @param endDate   endDate
     * @return boolean
     * @throws ParseException ParseException
     */
    public static boolean isDateBetween(Date date, Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return true;
        }
        return date.getTime() >= startDate.getTime() && date.getTime() <= endDate.getTime();

    }

    /**
     * 获取两个日期（不含时分秒）相差的天数，包含今天
     *
     * @param startDate startDate
     * @param endDate   endDate
     * @return int
     */
    public static int dateBetweenIncludeToday(Date startDate, Date endDate) {
        return dateBetween(startDate, endDate) + 1;
    }

    /**
     * 获取日期时间的年份，如2017-02-13，返回2017
     *
     * @param date date
     * @return int
     */
    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取日期时间的月份，如2017年2月13日，返回2
     *
     * @param date date
     * @return int
     */
    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日期时间的第几天（即返回日期的dd），如2017-02-13，返回13
     *
     * @param date date
     * @return int
     */
    public static int getDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    /**
     * 获取日期时间的周数
     *
     * @param date date
     * @return int
     */
    public static int getWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取日期时间当月的总天数，如2017-02-13，返回28
     *
     * @param date date
     * @return int
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取日期时间当月的总天数，如2017-02-13，返回28
     *
     * @param date date
     * @return int
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取日期时间当月的第几天，如2017-02-13，返回13
     *
     * @param date date
     * @return int
     */
    public static int getDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取日期时间当月的总天数，如2017-02-13，返回28
     *
     * @param date date
     * @return int
     */
    public static int getDaysOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取日期时间当年的第几天，如2017-02-13，返回2017年的53
     *
     * @param date date
     * @return int
     */
    public static int getDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取日期时间当年的总天数，如2017-02-13，返回2017年的总天数
     *
     * @param date date
     * @return int
     */
    public static int getDaysOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取日期时间当周的第几天，即星期几
     *
     * @param date date
     * @return int
     */
    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }


    /**
     * 根据时间获取当月最大的日期
     * <li>2017-02-13，返回2017-02-28</li>
     * <li>2016-02-13，返回2016-02-29</li>
     * <li>2016-01-11，返回2016-01-31</li>
     *
     * @param date Date
     * @return Date
     * @throws Exception Exception
     */
    public static Date maxDateOfMonth(Date date) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int value = cal.getActualMaximum(Calendar.DATE);
        return dateParse(dateFormat(date, MONTH_PATTERN) + "-" + value, null);
    }

    /**
     * 根据时间获取当月最小的日期，也就是返回当月的1号日期对象
     *
     * @param date Date
     * @return Date
     * @throws Exception Exception
     */
    public static Date minDateOfMonth(Date date) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int value = cal.getActualMinimum(Calendar.DATE);
        return dateParse(dateFormat(date, MONTH_PATTERN) + "-" + value, null);
    }

    /**
     * 根据时间获取当月最小的日期，也就是返回当月的1号日期对象
     *
     * @param time 整形时间
     * @return Date
     */
    public static Date parse(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.getTime();
    }

    /**
     * @param time time
     * @return Date
     */
    public static Date parseShort(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time * 1000);
        return cal.getTime();
    }

    /**
     * 是否是同一天
     *
     * @param date1 date1
     * @param date2 date2
     * @return boolean
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return isSameDay(cal1, cal2);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    /**
     * 是否是同一月
     *
     * @param date1 date1
     * @param date2 date2
     * @return boolean
     */
    public static boolean isSameMonth(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return isSameMonth(cal1, cal2);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    /**
     * @param cal1 cal1
     * @param cal2 cal2
     * @return boolean
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    /**
     * @param cal1 cal1
     * @param cal2 cal2
     * @return boolean
     */
    public static boolean isSameMonth(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(2) == cal2.get(2);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    /**
     * @param date date
     * @return Date
     */
    public static Date getDateEndOfTheDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * @param date date
     * @return Date
     */
    public static Date getDateStartOfTheDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * @param startTime startTime
     * @param endTime   endTime
     * @return Long
     */
    public static Long computeSecondDiff(Date startTime, Date endTime) {
        return (endTime.getTime() - startTime.getTime()) / 1000L;
    }

    /**
     * 返回当前日期
     *
     * @return Date
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 返回明日日期
     *
     * @return Date
     */
    public static Date tomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now());
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        return calendar.getTime();
    }

    /**
     * 返回昨日日期
     *
     * @return Date
     */
    public static Date yesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now());
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        return calendar.getTime();
    }

    /**
     * 格式化时间 HH:mm:ss
     *
     * @param date date
     * @return String
     */
    public static String formatTime(Date date) {
        return new SimpleDateFormat(TIME_PATTERN).format(date);
    }

    /**
     * 格式化日期 yyyy-MM-dd HH:mm:ss
     *
     * @param date date
     * @return String
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(DATE_TIME_PATTERN).format(date);
    }

    /**
     * 格式化日期 yyyy-MM-dd HH:mm:ss
     *
     * @param date 时间
     * @param tz   时区
     * @return 格式化数据
     */
    public static String formatDateTime(Date date, TimeZone tz) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
        sdf.setTimeZone(tz);
        return sdf.format(date);
    }

    /**
     * 格式化日期 yyyy-MM-dd
     *
     * @param date date
     * @return String
     */
    public static String formatDate(Date date) {
        return new SimpleDateFormat(DATE_PATTERN).format(date);
    }

    /**
     * 返回特定日期偏移后的日期
     *
     * @param date   date
     * @param field  field
     * @param amount amount
     * @return Date
     */
    public static Date dateOffset(Date date, int field, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(field, amount);
        return c.getTime();
    }

    /**
     * @param date date
     * @return Date
     */
    public static Date addHourMinutesSecond(Date date) {
        Calendar c = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        c.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
        return c.getTime();
    }

    /**
     * 获取10天的时长
     *
     * @return long
     */
    public static long tenDaysTime() {
        return 24 * 3600 * 1000 * 10;
    }

    /**
     * @param now now
     * @return Date
     */
    public static Date setHotTime(Date now) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 14);
        return c.getTime();
    }

    /**
     * 是否是晚间，6-18点为晚间
     *
     * @param now now
     * @return boolean
     */
    public static boolean isEvening(Date now) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 6);
        Date beginTime = c.getTime();

        c.setTime(now);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 18);
        Date endTime = c.getTime();
        return !isEffectiveDate(now, beginTime, endTime);
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，不包含临界时间
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return boolean
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        return date.after(begin) && date.before(end);
    }

    /**
     * 检查当前日期是否生日
     *
     * @param currentDate currentDate
     * @param birthday    birthday
     * @return Boolean
     */
    public static Boolean checkBirthday(Date currentDate, Date birthday) {
        if (birthday != null && currentDate != null) {
            return getMonth(currentDate) == getMonth(birthday)
                    && getDate(currentDate) == getDate(birthday);
        } else {
            return false;
        }
    }


    /**
     * 时间格式转化
     *
     * @param date    Date
     * @param pattern StringUtil.DATE_TIME_PATTERN || StringUtil.DATE_PATTERN， 如果为空，则为yyyy-MM-dd
     * @return String
     * @throws ParseException ParseException
     */
    public static Date format(Date date, String pattern) throws ParseException {
        if (StringUtils.isBlank(pattern)) {
            pattern = DateUtils.DATE_PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return dateParse(sdf.format(date), pattern);
    }

    /**
     * 时间格式转化
     *
     * @param date    Date
     * @param pattern StringUtil.DATE_TIME_PATTERN || StringUtil.DATE_PATTERN， 如果为空，则为yyyy-MM-dd
     * @return String
     * @throws ParseException ParseException
     */
    public static String formatDate(Date date, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            pattern = DateUtils.DATE_PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 转成gmt时间
     *
     * @param date 时间
     * @return 格式化数据
     */
    public static String getGMT(Date date) {
        TimeZone tz = DateUtils.ZONE_GMT;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(tz);
        return sdf.format(date);
    }

    /**
     * 转成utc时间
     *
     * @param date 时间
     * @return 格式化数据
     */
    public static String toISO8601UTC(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN_8601);
        df.setTimeZone(tz);
        return df.format(date);
    }

    /**
     * 转成格式化时间
     *
     * @param dateStr 格式化数据
     * @return 时间
     */
    public static Date fromISO8601UTC(String dateStr) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN_8601);
        df.setTimeZone(tz);

        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            // 序列号异常
        }
        return null;
    }

    /**
     * 获取当月最后时刻
     *
     * @param date 当月的任意一天
     * @return Date
     * @throws ParseException 时间序列号异常
     */
    public static Date getMonthLast(Date date) throws ParseException {
        Calendar call = Calendar.getInstance();
        call.setTime(date);
        int maxDays = call.getActualMaximum(Calendar.DAY_OF_MONTH);
        call.set(Calendar.DAY_OF_MONTH, maxDays);
        return getDateEndOfTheDate(call.getTime());
    }

    /**
     * 获取当月第一天的零时刻
     *
     * @param date 当月的任意一天
     * @return Date
     */
    public static Date getMonthBeginZero(Date date) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return getDateStartOfTheDate(cale.getTime());
    }

    /**
     * 获取两个时间的相差时长（天时分秒）
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 两个时间的相差时长（天时分秒）
     */
    public static String getDistanceTime(Date startTime, Date endTime) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            long time1 = startTime.getTime();
            long time2 = endTime.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = day + "天" + hour + "时" + min + "分" + sec + "秒";
        if (day <= 0) {
            result = result.substring(result.indexOf("天") + 1);
        }

        if (day <= 0 && hour <= 0) {
            result = result.substring(result.indexOf("时") + 1);
        }

        if (day <= 0 && hour <= 0 && min <= 0) {
            result = result.substring(result.indexOf("分") + 1);
        }
        return result;
    }

    /**
     * 将秒转换成 天-时-分-秒
     *
     * @param seconds 秒
     * @return x天x时x分x秒 格式的时间
     */
    public static String convertSeconds(Long seconds) {
        if (null == seconds) {
            return null;
        }
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            long diff = seconds * 1000;
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = day + "天" + hour + "时" + min + "分" + sec + "秒";
        if (day <= 0) {
            result = result.substring(result.indexOf("天") + 1);
        }

        if (day <= 0 && hour <= 0) {
            result = result.substring(result.indexOf("时") + 1);
        }

        if (day <= 0 && hour <= 0 && min <= 0) {
            result = result.substring(result.indexOf("分") + 1);
        }
        return result;
    }

    /**
     * 将秒转换成 天-时-分
     *
     * @param seconds 秒
     * @return result
     */
    public static String convertMinutes(Long seconds) {
        if (null == seconds) {
            return null;
        }
        long day = 0;
        long hour = 0;
        long min = 0;
        try {
            long diff = seconds * 1000;
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = day + "天" + hour + "时" + min + "分";
        if (day <= 0) {
            result = result.substring(result.indexOf("天") + 1);
        }

        if (day <= 0 && hour <= 0) {
            result = result.substring(result.indexOf("时") + 1);
        }
        return result;
    }

    /**
     * 返回时间段内每一天
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 时间段内每一天
     */
    public static List<Date> getEveryDay(Date beginTime, Date endTime) {
        List<Date> list = new ArrayList<Date>();
        list.add(beginTime);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(beginTime);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(endTime);
        // 测试此日期是否在指定日期之后
        while (endTime.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            list.add(calBegin.getTime());
        }
        return list;
    }

    /**
     * 返回时间段内每一周
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 时间段内每一周
     */
    public static List<String> getEveryWeek(Date beginTime, Date endTime) {
        List<String> list = new ArrayList<>();
        Calendar c1 = Calendar.getInstance();
        c1.setFirstDayOfWeek(Calendar.MONDAY);
        c1.setMinimalDaysInFirstWeek(4);
        c1.setTime(beginTime);
        Calendar c2 = Calendar.getInstance();
        c2.setFirstDayOfWeek(Calendar.MONDAY);
        c2.setMinimalDaysInFirstWeek(4);
        c2.setTime(endTime);
        int year2 = c2.get(Calendar.YEAR);
        int weekOfYear2 = c2.get(Calendar.WEEK_OF_YEAR);
        int month2 = c2.get(Calendar.MONTH) + 1;
        // 处理12月最后一周的问题
        if (month2 == 12 && weekOfYear2 == 1) {
            year2++;
        }
        // 处理1月第一周的问题
        if (month2 == 1 && weekOfYear2 >= 52) {
            year2--;
        }
        while (true) {
            int year1 = c1.get(Calendar.YEAR);
            int weekOfYear1 = c1.get(Calendar.WEEK_OF_YEAR);
            int month1 = c1.get(Calendar.MONTH) + 1;
            // 处理12月第一周的问题
            if (month1 == 12 && weekOfYear1 == 1) {
                year1++;
            }
            // 处理1月第一周的问题
            if (month1 == 1 && weekOfYear1 >= 52) {
                year1--;
            }
            list.add(year1 + "" + (weekOfYear1 < 10 ? "0" + weekOfYear1 : weekOfYear1));
            if (year1 == year2 && weekOfYear1 == weekOfYear2) {
                break;
            }
            //增加7天
            c1.setTimeInMillis(c1.getTimeInMillis() + 1000 * 60 * 60 * 24 * 7);
        }
        return list;
    }

    /**
     * 返回时间段内每一月
     *
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @return yyyy.MM
     */
    public static List<String> getEveryMonth(Date beginDate, Date endDate) {
        if (beginDate.after(endDate)) {
            throw new RuntimeException("参数错误");
        }
        List<String> list = new ArrayList<>();

        Calendar c1 = Calendar.getInstance();
        c1.setTime(beginDate);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(endDate);
        int targetYear = c2.get(Calendar.YEAR);
        int targetMonth = c2.get(Calendar.MONTH) + 1;
        while (true) {
            int currentYear = c1.get(Calendar.YEAR);
            int currentMonth = c1.get(Calendar.MONTH) + 1;
            String monthOfYear = currentYear + "." + (currentMonth < 10 ? "0" + currentMonth : "" + currentMonth);
            list.add(monthOfYear);
            if (targetYear == currentYear && targetMonth == currentMonth) {
                break;
            }
            c1.set(Calendar.MONTH, c1.get(Calendar.MONTH) + 1);
            c1.set(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }

    /**
     * 查询当前传入周的星期一
     *
     * @param year    年
     * @param week    周
     * @param pattern 格式
     * @return 当前传入周的星期一
     */
    public static String getWeekMonday(int year, int week, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(4);
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置周
        cal.set(Calendar.WEEK_OF_YEAR, week);
        //设置该周第一天为星期一
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        //格式化日期
        pattern = null == pattern ? DateUtils.DATE_PATTERN : pattern;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String monday = sdf.format(cal.getTime());
        return monday;
    }

    /**
     * 查询当前传入周的星期天
     *
     * @param year    年
     * @param week    周
     * @param pattern 格式
     * @return 当前传入周的星期天
     */
    public static String getWeekSunday(int year, int week, String pattern) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(4);
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置周
        cal.set(Calendar.WEEK_OF_YEAR, week);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //格式化日期
        pattern = null == pattern ? DateUtils.DATE_PATTERN : pattern;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String sunday = sdf.format(cal.getTime());
        return sunday;
    }
}