
package com.tunnel.business.datacenter.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CronUtil {


    public static void main(String[] args) {
       // if(!CronUtils.isValid(sty.getSchedulerTime())){
    }

    /**
     * 年月日转换Cron表达式
     * @param dateStr  日期
     * @param timeStr  时间
     * @return
     */
    public static String DateTimeConvertCron(String dateStr,String timeStr){

        SimpleDateFormat yyymmddFormat = new SimpleDateFormat("yyyy-mm-dd");
        //  String cron = "2023-03-31";
        Date date1 = null;
        try {
            date1 = yyymmddFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String dateFormat1 = "dd mm ? yyyy";
        SimpleDateFormat sd1 = new SimpleDateFormat(dateFormat1);
        String ddmmyyyyFormat = null;
        if (date1 != null) {
            ddmmyyyyFormat = sd1.format(date1);
        }

        SimpleDateFormat dataformatter = new SimpleDateFormat("HH:mm:ss");
        //  String cron = "08:00:00";
        Date date = null;
        try {
            date = dataformatter.parse(timeStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String dateFormat = "ss mm HH ";
        SimpleDateFormat sd = new SimpleDateFormat(dateFormat);
        String formatTime = null;
        if (date != null) {
            formatTime = sd.format(date);
        }

        String cronDate = formatTime + ddmmyyyyFormat;

        return cronDate;
    }

    /**
     * 时分秒转换Cron表达式
     * @param time
     * @return
     */
    public static String DateConvertCron(String time){

        SimpleDateFormat dataformatter = new SimpleDateFormat("HH:mm:ss");
      //  String cron = "08:00:00";
        Date date = null;
        try {
            date = dataformatter.parse(time);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String dateFormat = "ss mm HH ";
        SimpleDateFormat sd = new SimpleDateFormat(dateFormat);
        String formatTime = null;
        if (date != null) {
            formatTime = sd.format(date);
        }
        String cronDate = formatTime + "?" + " *" + " *" + " *";

        return cronDate;
    }


    /**
     * 支持简单的日期转换
     * Cron表达式 秒转换 日期
     * @param cronStr  表达式
     * @return
     */
    public static String CronConvertDate(String cronStr){

        String [] array = cronStr.split(" ");

        if(array[6].indexOf("*") > -1){
            return null;
        }

        return array[6]+"-"+array[4]+"-"+array[3];
    }

    /**
     * 支持简单的日期转换
     * Cron表达式 秒转换 时间
     * @param cronStr  表达式
     * @return
     */
    public static String CronConvertDateTime(String cronStr){

        String [] array = cronStr.split(" ");

        return array[2]+":"+array[1]+":"+array[0];
    }


}

