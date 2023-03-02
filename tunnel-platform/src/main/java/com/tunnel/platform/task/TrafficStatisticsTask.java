package com.tunnel.platform.task;


import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.domain.dataInfo.SdDataTrafficDay;
import com.tunnel.business.domain.dataInfo.SdDataTrafficHour;
import com.tunnel.business.domain.dataInfo.SdDataTrafficMonth;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.mapper.digitalmodel.SdRadarDetectDataTemporaryMapper;
import com.tunnel.business.service.dataInfo.*;
import com.tunnel.business.service.vehicle.ISdVehicleDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component("trafficStatisticsTask")
public class TrafficStatisticsTask {

    private static final Logger log = LoggerFactory.getLogger(TrafficStatisticsTask.class);

    private static ISdTunnelsService sdTunnelsService = SpringUtils.getBean(ISdTunnelsService.class);

    private static ISdVehicleDataService sdVehicleDataService = SpringUtils.getBean(ISdVehicleDataService.class);

    private ISdDataTrafficHourService trafficHourService = SpringUtils.getBean(ISdDataTrafficHourService.class);

    private ISdDataTrafficDayService trafficDayService = SpringUtils.getBean(ISdDataTrafficDayService.class);

    private ISdDataTrafficMonthService trafficMonthService = SpringUtils.getBean(ISdDataTrafficMonthService.class);



    /** 车流量定时任务 小时 */
    public void clientSendHour() throws ParseException {
        log.info("开始计算小时车流量。");
        List<SdTunnels> sdTunnels = sdTunnelsService.selectSdTunnelsList(new SdTunnels());
        for (SdTunnels sdTunnel:sdTunnels) {
            Calendar calendar = Calendar.getInstance();
            //往后推一天
            calendar.add(Calendar.HOUR,-1);
            int yesr = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            Date date = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
            String startDate = sdf.format(date)+":00:00";
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Map param = new HashMap<>();
            param.put("startDate",startDate);
            param.put("endDate",sdf.format(date)+":59:59");
            param.put("tunnelId",sdTunnel.getTunnelId());
            //获取上行
            param.put("direction","1");
            List<Map<String, Object>> xlist = sdVehicleDataService.getVehicleListsByHour(param);
            pushVehicleHourNum(xlist,sdTunnel,yesr,month,day,hour,sdf1.parse(startDate),"1");
            //获取下行
            param.put("direction","2");
            List<Map<String, Object>> xlist1 = sdVehicleDataService.getVehicleListsByHour(param);
            pushVehicleHourNum(xlist1,sdTunnel,yesr,month,day,hour,sdf1.parse(startDate),"2");
        }
    }


    /**
     * 定时任务   每日车流量
     * @throws ParseException
     */
    public void clientSendDay() throws ParseException {
        log.info("开始计算日车流量....");
        List<SdTunnels> sdTunnels = sdTunnelsService.selectSdTunnelsList(new SdTunnels());
        for (SdTunnels sdTunnel:sdTunnels) {
            String tunnelId = sdTunnel.getTunnelId();
            Calendar calendar = Calendar.getInstance();
            //往后推一天
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            int yesr = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            Date date = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startDate = sdf.format(date)+" 00:00:00";
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map param = new HashMap<>();
            param.put("startDate",startDate);
            param.put("endDate",sdf.format(date)+" 23:59:59");
            param.put("tunnelId",tunnelId);
            //获取上行数据
            param.put("direction","1");
            Map<String, Object> map = sdVehicleDataService.getVehicleListsByDay(param);
            pushVehicleDayNum(map,sdTunnel,yesr,month,day,sdf1.parse(startDate),"1");
            //获取下行数据
            param.put("direction","2");
            Map<String, Object> map2 = sdVehicleDataService.getVehicleListsByDay(param);
            pushVehicleDayNum(map2,sdTunnel,yesr,month,day,sdf1.parse(startDate),"2");
        }
    }

    /** 车流量定时任务  月*/
    public void clientSendMonth() throws ParseException {
        log.info("开始计算月车流量。");
        List<SdTunnels> sdTunnels = sdTunnelsService.selectSdTunnelsList(new SdTunnels());
        for (SdTunnels sdTunnel:sdTunnels) {
            Calendar calendar = Calendar.getInstance();
            //往后推一天
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            Date date = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map param = new HashMap();
            param.put("endDate",sdf.format(date)+" 23:59:59");
            Calendar calendar1 = Calendar.getInstance();
            calendar1.add(Calendar.MONTH,-1);
            Integer year = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH)+1;
            Date date1 = calendar1.getTime();
            String startDate = sdf.format(date1)+" 00:00:00";
            param.put("startDate",startDate);
            param.put("tunnelId",sdTunnel.getTunnelId());
            param.put("direction","1");
            Map<String, Object> dataMap = sdVehicleDataService.getVehicleListsByMonth(param);
            pushVehicleMonthNum(dataMap,sdTunnel,year,month,sdf1.parse(startDate),"1");
            //获取下行数据
            param.put("direction","2");
            Map<String, Object> dataMap2 = sdVehicleDataService.getVehicleListsByMonth(param);
            pushVehicleMonthNum(dataMap,sdTunnel,year,month,sdf1.parse(startDate),"2");
        }
    }



    public void pushVehicleHourNum(List<Map<String, Object>> list,SdTunnels sdTunnel,Integer year,Integer month,Integer day,Integer hour,Date startTime,String direction){
        SdDataTrafficHour trafficHour = new SdDataTrafficHour();
        Integer vehicleSpeedAvg;
        BigDecimal vehicleSpeedSum = new BigDecimal(0);
        int  num = 0;
        if(list.size()>0&&list!=null){
            for (Map map:list) {
                BigDecimal vehicleSpeed;
                if(map.containsKey("num")&&map.get("num")!=null){
                    num += Integer.parseInt(map.get("num").toString());
                }else{
                    num += 0;
                }
                if(map.containsKey("vehicleSpeed")&&map.get("vehicleSpeed")!=null){
                    vehicleSpeed =  new BigDecimal(map.get("vehicleSpeed").toString());
                }else{
                    vehicleSpeed = new BigDecimal(0);
                }
                vehicleSpeedSum =  vehicleSpeedSum.add(vehicleSpeed);
            }
            //填写年月日时间
            trafficHour.setYear(year.longValue());
            trafficHour.setMonth(month.longValue());
            trafficHour.setDay(day.longValue());
            trafficHour.setHour(hour.longValue());
            trafficHour.setTunnelId(sdTunnel.getTunnelId());
            trafficHour.setTunnelName(sdTunnel.getTunnelName());
            trafficHour.setStatTime(startTime);
            trafficHour.setTotalFlow((long) num);
//            trafficHour.setFlowSveh(flowSveh.longValue());
//            trafficHour.setFlowMveh(flowMveh.longValue());
//            trafficHour.setFlowBveh(flowBveh.longValue());
            vehicleSpeedAvg = vehicleSpeedSum.divide(new BigDecimal(list.size()),BigDecimal.ROUND_CEILING).intValue();
            trafficHour.setAvgSpeed(vehicleSpeedAvg.toString());
            //定时获取制定方向
            trafficHour.setDirection(direction);
            trafficHourService.insertSdDataTrafficHour(trafficHour);
        }else{
            //填写年月日时间
            trafficHour.setYear(year.longValue());
            trafficHour.setMonth(month.longValue());
            trafficHour.setDay(day.longValue());
            trafficHour.setHour(hour.longValue());
            trafficHour.setTunnelId(sdTunnel.getTunnelId());
            trafficHour.setTunnelName(sdTunnel.getTunnelName());
            trafficHour.setStatTime(startTime);
            trafficHour.setTotalFlow(0L);
//            trafficHour.setFlowSveh(0L);
//            trafficHour.setFlowMveh(0L);
//            trafficHour.setFlowBveh(0L);
            trafficHour.setAvgSpeed("0");
            trafficHour.setDirection(direction);
            trafficHourService.insertSdDataTrafficHour(trafficHour);
        }
    }


    /**
     * 每日过车数据。
     */
    public void pushVehicleDayNum(Map<String, Object> dataMap,SdTunnels sdTunnel,Integer year,Integer month,Integer day,Date startTime,String direction){
        SdDataTrafficDay trafficDay = new SdDataTrafficDay();
//        Integer flowSveh = 0;
//        if(dataMap!=null&&dataMap.containsKey("flowSveh")&&dataMap.get("flowSveh")!=null){
//            flowSveh = Integer.parseInt(dataMap.get("flowSveh").toString());
//        }
//        Integer flowBveh = 0;
//        if(dataMap!=null&&dataMap.containsKey("flowBveh")&&dataMap.get("flowBveh")!=null){
//            flowBveh = Integer.parseInt(dataMap.get("flowBveh").toString());
//        }
//        Integer flowMveh = 0;
//        if(dataMap!=null&&dataMap.containsKey("flowMveh")&&dataMap.get("flowMveh")!=null){
//            flowMveh = Integer.parseInt(dataMap.get("flowMveh").toString());
//        }
        String avgSpeed = "0";
        if(dataMap!=null&&dataMap.containsKey("avgSpeed")&&dataMap.get("avgSpeed")!=null){
            avgSpeed = dataMap.get("avgSpeed").toString();
        }
        Integer totalFlow = 0;
        if(dataMap!=null&&dataMap.containsKey("totalFlow")&&dataMap.get("totalFlow")!=null){
            totalFlow = Integer.parseInt(dataMap.get("totalFlow").toString());
        }
        //填写年月日时间
        trafficDay.setYear(year.longValue());
        trafficDay.setMonth(month.longValue());
        trafficDay.setDay(day.longValue());
        trafficDay.setTunnelId(sdTunnel.getTunnelId());
        trafficDay.setTunnelName(sdTunnel.getTunnelName());
        trafficDay.setDirection(direction);
        trafficDay.setStatTime(startTime);
        trafficDay.setTotalFlow(totalFlow.longValue());
//        trafficDay.setFlowSveh(flowSveh.longValue());
//        trafficDay.setFlowMveh(flowMveh.longValue());
//        trafficDay.setFlowBveh(flowBveh.longValue());
        trafficDay.setAvgSpeed(avgSpeed);
        trafficDayService.insertSdDataTrafficDay(trafficDay);
    }



    public void pushVehicleMonthNum(Map<String, Object> dataMap,SdTunnels sdTunnel,Integer year,Integer month,Date startTime,String direction){
//        Integer flowSveh = 0;
//        if(dataMap!=null&&dataMap.containsKey("flowSveh")&&dataMap.get("flowSveh")!=null){
//            flowSveh = Integer.parseInt(dataMap.get("flowSveh").toString());
//        }
//        Integer flowBveh = 0;
//        if(dataMap!=null&&dataMap.containsKey("flowBveh")&&dataMap.get("flowBveh")!=null){
//            flowBveh = Integer.parseInt(dataMap.get("flowBveh").toString());
//        }
//        Integer flowMveh = 0;
//        if(dataMap!=null&&dataMap.containsKey("flowMveh")&&dataMap.get("flowMveh")!=null){
//            flowMveh = Integer.parseInt(dataMap.get("flowMveh").toString());
//        }
        String avgSpeed = "0";
        if(dataMap!=null&&dataMap.containsKey("avgSpeed")&&dataMap.get("avgSpeed")!=null){
            avgSpeed = dataMap.get("avgSpeed").toString();
        }
        Integer totalFlow = 0;
        if(dataMap!=null&&dataMap.containsKey("totalFlow")&&dataMap.get("totalFlow")!=null){
            totalFlow = Integer.parseInt(dataMap.get("totalFlow").toString());
        }
        SdDataTrafficMonth trafficMonth = new SdDataTrafficMonth();
        //填写年月日时间
        trafficMonth.setYear(year.longValue());
        trafficMonth.setMonth(month.longValue());
        trafficMonth.setTunnelId(sdTunnel.getTunnelId());
        trafficMonth.setTunnelName(sdTunnel.getTunnelName());
        trafficMonth.setDirection(direction);
        trafficMonth.setStatTime(startTime);
        trafficMonth.setTotalFlow(totalFlow.longValue());
//        trafficMonth.setFlowSveh(flowSveh.longValue());
//        trafficMonth.setFlowMveh(flowMveh.longValue());
//        trafficMonth.setFlowBveh(flowBveh.longValue());
        trafficMonth.setAvgSpeed(avgSpeed);
        trafficMonthService.insertSdDataTrafficMonth(trafficMonth);
    }
}
