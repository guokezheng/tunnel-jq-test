package com.tunnel.platform.datacenter.util;/*
package com.tunnel.workbench.util;


import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Date;


public class CronUtil {

    //上次执行时间
    public static long getLastTriggerTime(String cron){
        if(!CronExpression.isValidExpression(cron)){
            return 0;
        }
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("Caclulate Date").withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
        Date time0 = trigger.getStartTime();
        Date time1 = trigger.getFireTimeAfter(time0);
        Date time2 = trigger.getFireTimeAfter(time1);
        Date time3 = trigger.getFireTimeAfter(time2);
        long l = time1.getTime() -(time3.getTime() -time2.getTime());
//		Date date = new Date(l);
//		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return l;
    }
    //获取下次执行时间（getFireTimeAfter，也可以下下次...）
    public static long getNextTriggerTime(String cron){
        if(!CronExpression.isValidExpression(cron)){
            return 0;
        }
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity("Caclulate Date").withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
        Date time0 = trigger.getStartTime();
        Date time1 = trigger.getFireTimeAfter(time0);
        return time1.getTime();
    }
}*/
