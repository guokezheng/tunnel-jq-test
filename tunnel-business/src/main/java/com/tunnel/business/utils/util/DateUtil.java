package com.tunnel.business.utils.util;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * DateUtil
 *
 * @date: 2022/3/16 16:52
 * @author: why
 * @version: 1.0
 */
public class DateUtil {

    private static SimpleDateFormat  sdf=null;

    private static final String timeZone = "GMT+8:00";

    /**
     * 获取今天结束时间
     */
    public static Long getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime();
    }

    /**
     * 获取当月的结束时间戳
     */
    public static Long getMonthEndTime() {
        Long currentTime = System.currentTimeMillis();

        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTimeInMillis(currentTime);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        // 获取当前月最后一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取本年的结束时间
     */
    public static Long getEndDayOfYear() {

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, getNowYear());

        cal.set(Calendar.MONTH, Calendar.DECEMBER);

        cal.set(Calendar.DATE, 31);

        return getDayEndTime(cal.getTime());

    }

    /**
     * 获取今年是哪一年
     */
    public static Integer getNowYear() {

        Date date = new Date();

        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();

        gc.setTime(date);

        return gc.get(1);

    }

    /**
     * 获取某个日期的结束时间
     */
    public static Long getDayEndTime(Date d) {

        Calendar calendar = Calendar.getInstance();

        if(null != d){
            calendar.setTime(d);
        }

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),    calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);

        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTimeInMillis();

    }

    /**
     * 获取指定时间月份的最后一天
     */
    public static Long getLastDayOfMonth(Date date) {
        long currentTime = date.getTime();

        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTimeInMillis(currentTime);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        // 获取当前月最后一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }


    public static String getLongToString(Long date){
        Date millisecondDate= new Date(date);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(millisecondDate);
    }

    /**
     * 判断指定时间是否是月份最后一天
     */
    public static boolean isMaxDayOfMonth(Date date){
        if(Objects.isNull(date)){
            throw new RuntimeException("指定时间为null");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_MONTH) == getMaxDayOfMonth(date);
    }

    /**
     * 获取指定时间月份最后一天
     */
    public static int getMaxDayOfMonth(Date date){
        if(Objects.isNull(date)){
            throw new RuntimeException("指定时间为null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTime(date);

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 日期转换成字符串
     * @param date
     * @return str
     */
    public static String dateToStr(Date date, String pattern) {

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 获取给定日期所在周的第一天
     */
    public static Date getFirstDayOfWeek(Date date){
        if(Objects.isNull(date)){
            throw new RuntimeException("指定时间为null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        return calendar.getTime();
    }

    /**
     * 获取给定日期所在周的最后一天
     */
    public static Date getLastDayOfWeek(Date date){
        if(Objects.isNull(date)){
            throw new RuntimeException("指定时间为null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, 7);
        return calendar.getTime();
    }

    /**
     * 获取日期字符串 等同于MySQL DATE_FORMAT(date,'%X-%V')
     */
    public static String getYearWeekStr(Date date){
        if(Objects.isNull(date)){
            throw new RuntimeException("指定时间为null");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTime(date);
        calendar.setMinimalDaysInFirstWeek(4);

        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        return calendar.getWeekYear() + "-" + (week < 10 ? "0" : "") + week;
    }
    /**
     * 获取两个日期之间的天数字符串集合
     */
    public static List getListByDate(Date starDate , Date endDate){
        List<String> list = new ArrayList();
        SimpleDateFormat sf = new SimpleDateFormat("YYYY-MM-dd");
        while (starDate.before(endDate)){
            starDate = new Date(starDate.getTime() + (long) 24 * 60 * 60 * 1000);
            list.add(sf.format(starDate));
        }
        return list;
    }
    /**
     * 获取当月天数
     */
    public static List<String> getMonthFullDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        List<String> fullDayList = new ArrayList<String>();
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = 1;// 所有月份从1号开始
        Calendar cal = Calendar.getInstance();// 获得当前日期对象
        cal.clear();// 清除信息
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);// 1月从0开始
        cal.set(Calendar.DAY_OF_MONTH, day);
        int count = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int j = 0; j <= (count - 1); ) {
            if (sdf.format(cal.getTime()).equals(date)) {
                break;
            }
            cal.add(Calendar.DAY_OF_MONTH, j == 0 ? +0 : +1);
            j++;
            fullDayList.add(sdf.format(cal.getTime()));
        }
        return fullDayList;
    }
}
