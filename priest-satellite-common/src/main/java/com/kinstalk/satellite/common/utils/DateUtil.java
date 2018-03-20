package com.kinstalk.satellite.common.utils;

/**
 * 提供日期的加减转换等功能 包含多数常用的日期格式
 * User: liuling
 * Date: 14-10-16
 * Time: 下午2:50
 */

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DateUtil {

    public static final String DATE_FMT_0 = "yyyyMMdd";
    public static final String DATE_FMT_1 = "yyyy/MM/dd";
    public static final String DATE_FMT_2 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FMT_3 = "yyyy-MM-dd";
    public static final String DATE_FMT_4 = "yyyy-MM-dd HH:mm";
    public static final String DATE_FMT_5 = "MM-dd";
    public static final String DATE_FMT_6 = "HH:mm";

    /* ------------------------- format/parse ------------------------- */

    public static String getHourSeconds(Long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FMT_6);//初始化Formatter的转换格式。
        String hms = formatter.format(time);
        return hms;
    }

    /**
     * 将string转成date类型
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date calculateDay(Long time) throws ParseException {
        String t = String.valueOf(time);
        DateFormat format = new SimpleDateFormat(DATE_FMT_0);
        Date d = format.parse(t);
//        String date = format(d, DATE_FMT_3);
        return d;
    }

    public static Long getBeforeWeekDate(Integer time) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT_3);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -time);
        String date = sdf.format(now.getTime());
        String result = date.replace("-", "");
        return Long.valueOf(result);
    }

    public static String getBeforeWeek(Integer time) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT_5);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -time);
        String date = sdf.format(now.getTime());
        return date;
    }

    public static Long getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT_3);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, 0);
        String date = sdf.format(now.getTime());
        String result = date.replace("-", "");
        return Long.valueOf(result);
    }

    public static Long getYesterdayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT_3);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, -1);
        String date = sdf.format(now.getTime());
        String result = date.replace("-", "");
        return Long.valueOf(result);
//        return now.getTime().getTime();
    }

    public static Long getTodayStartTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT_2);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, 0);
//        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);
//        System.out.println(sdf.format(now.getTime()));
        return now.getTime().getTime();
    }

    public static Long getTodayEndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT_2);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, 0);
//        now.set(Calendar.HOUR, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        now.set(Calendar.HOUR_OF_DAY, 23);
//        System.out.println(sdf.format(now.getTime()));
        return now.getTime().getTime();
    }

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
     * 获取某一日期下一天的起始时间（0点0分0秒），参数为null时则返回当前日期的起始时间
     *
     * @param date
     * @return
     */
    public static Date getNextDayByParams(Date date) {
        Calendar now = Calendar.getInstance();
        if (date != null) {
            now.setTime(date);
            now.add(Calendar.DAY_OF_MONTH, +1);
        }
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        return now.getTime();
    }

    public static Long getNextMinutes(Long param, Integer minutes) {
        try {
            String time = convertLongToDate(param);
            Date date = parse(time, "yyyy-MM-dd");
            Calendar now = Calendar.getInstance();
            if (date != null) {
                now.setTime(date);
                now.set(Calendar.MINUTE, minutes);
            }
            now.set(Calendar.HOUR_OF_DAY, 0);
            now.set(Calendar.SECOND, 0);
            now.set(Calendar.MILLISECOND, 0);
            return now.getTime().getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1l;
    }

    public static Long getNextDayForLong(Long param) {
        try {
            String time = convertLongToDate(param);
            Date date = parse(time, "yyyy-MM-dd");
            return getNextDayByParams(date).getTime();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return -1l;
    }

    /**
     * 获取某一日期的起始时间（0点0分0秒），参数为null时则返回当前日期的起始时间
     *
     * @param date
     * @return Date
     */
    public static Date getStartTimeSubDay(Date date, int day) {
        Calendar now = Calendar.getInstance();
        if (date != null) {
            now.setTime(date);
        }
        now.add(Calendar.DATE, -day);
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
            e.printStackTrace();
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

    public static Date test(Long time) {
        Date date = new Date(time);
        return date;
    }

    /**
     * 根据给定时间获取所属月第一天毫秒数
     *
     * @param time
     * @return
     */
    public static Long getMonthFirstDateBySecTime(Long time) {
        Date startDate = new Date(time);
        Date endDate = getMonthFirstDate(startDate);
        return endDate.getTime();
    }

    /**
     * 根据给定时间获取所属月最后一天毫秒数
     *
     * @param time
     * @return
     */
    public static Long getMonthLastDateBySecTime(Long time) {
        Date startDate = new Date(time);
        Date endDate = getMonthLastDate(startDate);
        return endDate.getTime();
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

    public static int calculateNumDay(Long max, Long min) {
        try {
            Date maxDate = calculateDay(max);
            Date minDate = calculateDay(min);
            return getDayNum(maxDate, minDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
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

    /**
     * 根据时间戳获取月,日,格:月-日
     *
     * @param timestamp
     * @return
     */
    public static String getDateByTimeStamp(long timestamp) {
        String date = new SimpleDateFormat(DATE_FMT_5).format(new Date(timestamp));
        return date;
    }

    /**
     * 根据时间戳获取月,日,格:月-日
     *
     * @param dateTime
     * @return
     */
    public static String getMonthByDate(Date dateTime) {
        String date = new SimpleDateFormat(DATE_FMT_5).format(dateTime);
        return date;
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
    public static String convertLongToDate(Long longDate) throws ParseException {
        Date date = new Date(longDate);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateText = df2.format(date);
        return dateText;
    }

    /**
     * 获取当年的第一天
     *
     * @return
     */
    public static Date getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的最后一天
     *
     * @return
     */
    public static Date getCurrYearLast() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    //获得下个月第一天的日期
    public static String getNextMonthFirst() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);//加一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }

    public int getRandomExcept(int RandMax, int[] ExceptNums) {
        Random rand = new Random();
        int num = rand.nextInt(RandMax);
        while (true) {
            int have = 0;
            for (int i = 0; i < ExceptNums.length; i++) {
                if (num == ExceptNums[i]) {
                    have = 1;
                }
            }
            if (have == 0) {
                return num;
            }
            num = rand.nextInt(RandMax);
        }
    }

    public static int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

	/**
     * 按date 格式返回增加天数的日期, 只支持以-分隔的日期,可以带 时分/时分秒
     * @param date
     * @param dayNums
     * @return
     */
    public static String addDay(String date, int dayNums) {
        if(date == null) {
            return "";
        }
        String result = null;


        try {
            SimpleDateFormat format = null;
            if(date.length() == DATE_FMT_0.length()) {
                format = new SimpleDateFormat(DATE_FMT_0);
            } else if(date.length() == DATE_FMT_2.length()) {
                format = new SimpleDateFormat(DATE_FMT_2);
            } else if(date.length() == DATE_FMT_3.length()) {
                format = new SimpleDateFormat(DATE_FMT_3);
            } else if(date.length() == DATE_FMT_4.length()) {
                format = new SimpleDateFormat(DATE_FMT_4);
            }

            if(format != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(format.parse(date));
                cal.add(Calendar.DATE, dayNums);

                result = format.format(cal.getTime());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result != null ? result : "";
    }

    public static void main(String args[]) throws Exception {
//
//        String date = getDateByTimeStamp(System.currentTimeMillis());
//        System.out.println(date);

//        Long startTime = getYesterdayStartTime();
//
//        Long endTime = getYesterdayEndTime();

//        Integer time = 7;
//        Long y = getBeforeWeekDate(time);
//        System.out.println(y);
//
//        Long s = 20160329L;
//        Date date = calculateDay(s);
//        System.out.println(date);
//
//        Long t = 20160403L;
//        Date date2 = calculateDay(t);
//        System.out.println(date2);

//        int day = getDayNum(date, date2);
//        System.out.println(day);
//
//        System.out.println(getMonthByDate(new Date(System.currentTimeMillis())));

//        Long time = System.currentTimeMillis();
//        String result = getHourSeconds(time);
//        System.out.println(result);

        String date = "20141005";
        System.out.println(DateUtil.addDay(date, 1));
        date = "2014-10-05 10:20";

        System.out.println(DateUtil.addDay(date, 1));
        date = "2014-10-05 10:30:22";

        System.out.println(DateUtil.addDay(date, 1));

    }

}
