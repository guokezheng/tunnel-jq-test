
package com.tunnel.business.datacenter.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CronUtil {
    /**
     * 时分秒转换Cron表达式
     * @param time
     * @return
     */
    public static String CronDate(String time){

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
}

