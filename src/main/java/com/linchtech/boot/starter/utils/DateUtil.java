package com.linchtech.boot.starter.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author 107
 * @since 2019-03-03
 */
public final class DateUtil {

    private static final ThreadLocal<SimpleDateFormat> YYYY_MM_DD_THREAD_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));
    private static final ThreadLocal<SimpleDateFormat> yyyy_MM_dd =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
    private static final ThreadLocal<SimpleDateFormat> HOUR_THREAD_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat("HH"));
    private static final ThreadLocal<SimpleDateFormat> YYYY_MM_DD_HH_THREAD_LOCAL =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    private static final long DAY_OF_MILL = 24 * 3600 * 1000;
    public static final long MILLSECOND_HOURS = 60L * 60L * 1000L;

    public static int getYear(final int date) {
        return date / 10000;
    }

    public static int addYear(final int date, final int year) {
        return date + year * 10000;
    }

    public static Timestamp timestampNow() {
        return new Timestamp(System.currentTimeMillis());
    }


    public static String now(String format) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }

    public static int intTimeNow() {
        return toTimeSeconds(new Date());
    }


    /**
     * 获取指定日期从0点开始经过的秒数，范围在0--86400之间
     *
     * @param date
     * @return
     */
    public static int toTimeSeconds(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .get(ChronoField.SECOND_OF_DAY);
    }


    /**
     * 获取某天0点0分0秒的时间戳
     *
     * @param mill
     * @return
     */
    public static Long get0ClockMillSecond(long mill) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(mill));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 获取几小时以前的时间戳
     *
     * @param hour
     * @return
     */
    public static long getHoursAgoMillSecond(int hour) {
        return System.currentTimeMillis() - MILLSECOND_HOURS * hour;
    }

    /**
     * 毫秒时间戳转换int类型日期
     *
     * @param mill
     * @return
     */
    public static Integer intMillToIntDate(Long mill) {
        SimpleDateFormat format = YYYY_MM_DD_THREAD_LOCAL.get();
        String date = format.format(new Date(mill));
        YYYY_MM_DD_THREAD_LOCAL.remove();
        return Integer.parseInt(date);
    }


    /**
     * 获取某天23:59分59秒的时间戳
     *
     * @param mill
     * @return
     */
    public static long get24ClockMillSecond(long mill) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(mill));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime().getTime();
    }

    /**
     * 获取时间戳的小时数
     * @param mill
     * @return
     */
    public static int getHourNumber(long mill) {
        SimpleDateFormat format = HOUR_THREAD_LOCAL.get();
        String now = format.format(new Date(mill));
        HOUR_THREAD_LOCAL.remove();
        return Integer.parseInt(now);
    }

    public static String yyyyMMddHH(Date date) {
        SimpleDateFormat simpleDateFormat = YYYY_MM_DD_HH_THREAD_LOCAL.get();
        String format = simpleDateFormat.format(date);
        YYYY_MM_DD_HH_THREAD_LOCAL.remove();
        return format;
    }

    public static String yyyyMMdd() {
        SimpleDateFormat simpleDateFormat = yyyy_MM_dd.get();
        String format = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        yyyy_MM_dd.remove();
        return format;
    }

    public static String yyyyMMdd(Date date) {
        SimpleDateFormat simpleDateFormat = yyyy_MM_dd.get();
        String format = simpleDateFormat.format(date);
        yyyy_MM_dd.remove();
        return format;
    }


}
