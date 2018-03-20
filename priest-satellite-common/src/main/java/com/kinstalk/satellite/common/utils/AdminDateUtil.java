package com.kinstalk.satellite.common.utils;

/**
 * 提供日期的加减转换等功能 包含多数常用的日期格式
 * User: erin
 * Date: 14-10-16
 * Time: 下午2:50
 */

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AdminDateUtil {

    public static final String DATE_FMT_0 = "yyyyMMdd";
    public static final String DATE_FMT_1 = "yyyy/MM/dd";
    public static final String DATE_FMT_2 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FMT_3 = "yyyy-MM-dd";

	/* ------------------------- format/parse ------------------------- */


    /**
     * 将Date类型的时间按某种格式转换为String型
     */
    public static String format(Date time, String pattern) {
        if (time == null || StringUtils.isEmpty(pattern)) {
            return StringUtils.EMPTY;
        }
        SimpleDateFormat format = getFormat(pattern);
        return format.format(time);
    }

    /**
     * 将Date类型的时间按某种格式转换为String型
     */
    public static String format(Long time, String pattern) {
        if (time == null || StringUtils.isEmpty(pattern)) {
            return StringUtils.EMPTY;
        }
        SimpleDateFormat format = getFormat(pattern);
        return format.format(time);
    }

    /**
     * 获取某一日期的起始时间（0点0分0秒），参数为null时则返回当前日期的起始时间
     *
     * @param date
     * @return Date
     */
    public static Date getStartTime(Date date) {
        Calendar now = Calendar.getInstance();
        if (date != null) {
            now.setTime(date);
        }
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        return now.getTime();
    }

    /**
     * 获取某一日期的结束时间（23点59分59秒），参数为null时则返回当前日期的结束时间
     *
     * @param date
     * @return Date
     */
    public static Date getEndTime(Date date) {
        Calendar now = Calendar.getInstance();
        if (date != null) {
            now.setTime(date);
        }
        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        now.set(Calendar.MILLISECOND, 999);
        return now.getTime();
    }

    /**
     * 从String类型的时间获取年份
     */
    public static int getYear(String time, String pattern) {
        Date date = parse(time, pattern);
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        int year = ca.get(Calendar.YEAR);
        return year;
    }

    /**
     * 从String类型的时间获取月份
     */
    public static int getMonth(String time, String pattern) {
        Date date = parse(time, pattern);
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        int month = ca.get(Calendar.MONTH); // 一月为0
        return month + 1;
    }

    /**
     * 从String类型的时间获取日期
     */
    public static int getDay(String time, String pattern) {
        Date date = parse(time, pattern);
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        int day = ca.get(Calendar.DATE);
        return day;
    }


    /**
     * 将String类型的时间按某种格式转换为Date型
     */
    public static Date parse(String time, String pattern) {
        try {
            SimpleDateFormat format = getFormat(pattern);
            return format.parse(time);
        } catch (Exception e) {
        }
        return _emptyDate;
    }

    /**
     * 获取Date所属月的第一天日期
     *
     * @return Date 默认null
     */
    public static Date getMonthFirstDate(Date date) {
        if (null == date) {
            return null;
        }

        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        ca.set(Calendar.DAY_OF_MONTH, 1);

        Date firstDate = ca.getTime();
        return firstDate;
    }


    /**
     * 获取Date所属月的最后一天日期
     *
     * @return Date 默认null
     */
    public static Date getMonthLastDate(Date date) {
        if (null == date) {
            return null;
        }

        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        ca.add(Calendar.MONTH, 1);
        ca.add(Calendar.DAY_OF_MONTH, -1);

        Date lastDate = ca.getTime();
        return lastDate;
    }


    /**
     * 计算两个日期间隔的秒数
     *
     * @param firstDate 小者
     * @param lastDate  大者
     * @return int 默认-1
     */
    public static long getTimeIntervalMins(Date firstDate, Date lastDate) {
        if (null == firstDate || null == lastDate) {
            return -1;
        }

        long intervalMilli = lastDate.getTime() - firstDate.getTime();
        return (intervalMilli / (1000));
    }

    /**
     * 获取系统当前日期
     *
     * @return
     * @throws java.text.ParseException
     */
    public static Date generateCurrentDate() throws ParseException {
        String temp_str;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        temp_str = sdf.format(date);
        return parse(temp_str, "yyyy-MM-dd");
        //注意format的格式要与日期String的格式相匹配
//        date = sdf.parse(temp_str);
//        return date;
    }

    /**
     * 计算两个日期间隔的天数
     *
     * @param firstDate 小者
     * @param lastDate  大者
     * @return int 默认-1
     */
    public static int getDayNum(Date firstDate, Date lastDate) {
        long timeInterval = getTimeIntervalMins(firstDate, lastDate);
        long between_days = timeInterval / (3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

	/* ------------------------- format/parse impl ------------------------- */

    static SimpleDateFormat getFormat(String pattern) {
        Map<String, SimpleDateFormat> cache = _simpleDateFormatCache.get();
        SimpleDateFormat format = cache.get(pattern);
        if (format != null) {
            return format;
        }

        // 防止cache过大
        if (cache.size() > 20) {
            cache.clear();
        }
        cache.put(pattern, format = new SimpleDateFormat(pattern));
        return format;
    }

    static
    ThreadLocal<Map<String, SimpleDateFormat>>
            _simpleDateFormatCache =
            new ThreadLocal<Map<String, SimpleDateFormat>>() {
                protected Map<String, SimpleDateFormat> initialValue() {
                    return new ConcurrentHashMap<String, SimpleDateFormat>();
                }
            };
    static final Date _emptyDate = new Date(0);

    /**
     * 生成token过期时间点
     */
    public static long generatorVaildTime(int expiresIn) {
        DateTime dateTime = new DateTime();
        long vaildTime = dateTime.plusSeconds(expiresIn).getMillis();
        return vaildTime;
    }

    public static int getIntervalSec(long t1, long t2) {
        return (int) (t1 - t2);
    }

    public static String getDateByTimeStamp(long timestamp) {
        Long timestampMs = timestamp * 1000;
        String date = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss").format(new Date(timestampMs));
        return date;
    }

    public static String getDateFmt0() {
        return DATE_FMT_0;
    }

    public static String getDateFmt1() {
        return DATE_FMT_1;
    }

    public static String getDateFmt2() {
        return DATE_FMT_2;
    }

    public static String getDateFmt3() {
        return DATE_FMT_3;
    }

    /**
     * 根据某人的生日生成某人的年龄，粗粒度的
     *
     * @param birthday
     * @return
     */
    public static long generateAge(Date birthday) throws ParseException {
        Date currentDate = generateCurrentDate();
        long countDays = (currentDate.getTime() - birthday.getTime()) / (1000 * 60 * 60 * 24);
        long age = countDays / 365;
        return age;
    }

    /**
     * 将Long型转换为Date类型
     *
     * @return
     * @throws java.text.ParseException
     */
    public static String convertLongToDate(Long longDate, String format) throws ParseException {
        if (longDate == null || format == null) {
            return "";
        }
        Date date = new Date(longDate);
        SimpleDateFormat df2 = new SimpleDateFormat(format);
        String dateText = df2.format(date);
        return dateText;
    }

    public static void main(String args[]) throws Exception {
//        int maxAge = 9988888;
//        long expire = AdminDateUtil.generatorVaildTime(maxAge);
//        String str = getDateByTimeStamp(expire / 1000);
//        System.out.println("str:" + str);
        long c = System.currentTimeMillis();
        String d = convertLongToDate(c, DATE_FMT_2);
        System.out.println(d);
//        Date date = generateCurrentDate();
//        System.out.println("current date:" + date);
//        String birthday = "1988-10-02";
//
//        Date birthdayD = parse(birthday, "yyyy-MM-dd");
//        long countDays = (AdminDateUtil.generateCurrentDate().getTime() - birthdayD.getTime()) / (1000 * 60 * 60 * 24);
//        long year = countDays / 365;
//        long day = countDays % 365;
//        long month = day / 12;
//        System.out.println("year:" + year + " day:" + day + " month:" + month);
//
//        System.out.println("age:" + generateAge(birthdayD));
    }

    public static Integer getAge(Long start) {
        DateTime birthday = new DateTime(start);
        Integer birthdayYear = birthday.getYear();
        Integer nowYear = DateTime.now().getYear();

        return nowYear - birthdayYear + 1;
    }

}
