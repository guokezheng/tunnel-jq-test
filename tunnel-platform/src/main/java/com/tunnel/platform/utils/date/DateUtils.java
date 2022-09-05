package com.tunnel.platform.utils.date;


import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DateUtils {
    /**
     * 查询24小时 数据检测
     * @param inputJudgeDate
     * @return
     */
    public static boolean isToday(Date inputJudgeDate) {
        boolean flag = false;
        //获取当前系统时间
        long longDate = System.currentTimeMillis();
        Date nowDate = new Date(longDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(nowDate);
        String subDate = format.substring(0, 10);
        //定义每天的24h时间范围
        String startTime = subDate + " 00:00:00";
        String endTime = subDate + " 23:59:59";
        Date paseStartTime = null;
        Date paseEndTime = null;
        try {
            paseStartTime = dateFormat.parse(startTime);
            paseEndTime = dateFormat.parse(endTime);

        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        if(inputJudgeDate.after(paseStartTime) && inputJudgeDate.before(paseEndTime)) {
            flag = true;
        }
        return flag;
    }
}
