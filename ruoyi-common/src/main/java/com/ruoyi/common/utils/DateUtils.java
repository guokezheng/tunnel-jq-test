package com.ruoyi.common.utils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间工具类
 * 
 *
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM","yyyy-MM-dd HH:mm:ss.SSS"};

    /**
     * 获取当前Date型日期
     * 
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     * 
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }
    //获取月初时间
    public static final String getBeginningTime()
    {
        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();

        // 获取月初时间
        LocalDateTime monthStart = currentTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        // 定义时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化时间为指定格式
        String formattedTime = monthStart.format(formatter);

        // 打印格式化后的时间
        System.out.println(formattedTime);
        return formattedTime;
    }
    //当前时间减一个月
    public static final String getsubtractOneTime(String dateStr)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 解析日期字符串为LocalDateTime对象
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);

        // 在给定日期上加一个月
        LocalDateTime oneMonthLater = dateTime.minusMonths(1);

        // 格式化时间为指定格式
        String formattedTime = oneMonthLater.format(formatter);

        // 打印格式化后的时间
        System.out.println(formattedTime);
        return formattedTime;
    }

    //当前时间减一个月
    public static final Date StrChangeDate(String dateStr)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(dateStr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }
    
    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
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
     * 判断当前时间是否在此时间范围内   格式默认为：HH:mm:ss
     * @param startTimeStr
     * @param endTimneStr
     * @return
     * @throws ParseException
     */
    public static boolean belongCalendar(String startTimeStr, String endTimneStr) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // 设置日期格式
        Date nowTime = df.parse(df.format(new Date()));
        Date startTime = df.parse(startTimeStr);
        Date endTime = df.parse(endTimneStr);
        boolean result;
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar start = Calendar.getInstance();
        start.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        //查看当前  开始时间是否大于结束时间
        if(start.getTimeInMillis()>end.getTimeInMillis()){
            end.add(Calendar.DAY_OF_MONTH,1);
            if (date.after(start) && date.before(end)) {
                result =  true;
            } else {
                result = false;
            }
        }else{
            if (date.after(start) && date.before(end)) {
                result =  true;
            } else {
                result = false;
            }
        }
        return result;
    }

    /**
     * 判断当前时间是否在此时间范围
     * @param startTimeStr 开始
     * @param endTimeStr 结束
     * @return
     */
    public static  boolean dateCompare(String startTimeStr,String endTimeStr){
        // 获取当前系统时间
        LocalTime currentTime = LocalTime.now();

        // 定义起始时间和结束时间
        LocalTime startTime = LocalTime.parse(startTimeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalTime endTime = LocalTime.parse(endTimeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));

        // 将当前时间格式化为目标格式
        String formattedCurrentTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // 比较当前时间是否在起始时间和结束时间之间
        boolean isBetween = currentTime.isAfter(startTime) && currentTime.isBefore(endTime);

        System.out.println("当前系统时间：" + formattedCurrentTime);
        System.out.println("起始时间：" + startTime);
        System.out.println("结束时间：" + endTime);
        System.out.println("当前时间是否在范围内：" + isBetween);
        return  isBetween;
    }
}
