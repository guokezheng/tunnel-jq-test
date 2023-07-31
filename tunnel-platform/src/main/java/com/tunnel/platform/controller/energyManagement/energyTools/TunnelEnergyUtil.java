package com.tunnel.platform.controller.energyManagement.energyTools;

import com.tunnel.platform.controller.energyManagement.enumeration.EnergyTunnelEnum;

import java.util.Calendar;
import java.util.Date;

/**
 * @author riemann
 */
public class TunnelEnergyUtil {


    public static String getCurrentTime(String flag) {
        Calendar cal = Calendar.getInstance();
        Date date=new Date();//现在的日期
        cal.setTime(date);
        Integer year=cal.get(Calendar.YEAR);//获取年
        Integer month = cal.get(Calendar.MONTH)+1;//获取月（月份从0开始，如果按照中国的习惯，需要加一）
        Integer day=cal.get(Calendar.DAY_OF_MONTH);//获取日（月中的某一天）
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        String yue= String.valueOf(month);
        String ri= String.valueOf(day);
        if(month<10){ //小于两位数前面补0
            yue="0"+month;
        }
        if(day<10){//小于两位数前面补0yue
            ri="0"+day;
        }
        if(flag.equals("0")){
            //return year+"-"+yue+"-"+ri;
            return "2023-07-12";
        }else if(flag.equals("1")){
            //return year+"-"+yue;
            return "2023-07";
        }else{
            //return String.valueOf(year);
            return "2023";
        }

    }


    public static String tunnelConvertEnergy(String tunnel) {
        String tunnelId = "";
        if(tunnel.equals(EnergyTunnelEnum.BEIJIAYU.getTunnel())){
            tunnelId = EnergyTunnelEnum.BEIJIAYU.getEnergy();
        }else if(tunnel.equals(EnergyTunnelEnum.HANGSHANDONG.getTunnel())){
            tunnelId = EnergyTunnelEnum.HANGSHANDONG.getEnergy();
        }else if(tunnel.equals(EnergyTunnelEnum.JINJIALOU.getTunnel())){
            tunnelId = EnergyTunnelEnum.JINJIALOU.getEnergy();
        }else if(tunnel.equals(EnergyTunnelEnum.MAANSHAN.getTunnel())){
            tunnelId = EnergyTunnelEnum.MAANSHAN.getEnergy();
        }else if(tunnel.equals(EnergyTunnelEnum.SHUANGZISHAN.getTunnel())){
            tunnelId = EnergyTunnelEnum.SHUANGZISHAN.getEnergy();
        }else if(tunnel.equals(EnergyTunnelEnum.YANGTIANSHAN.getTunnel())){
            tunnelId = EnergyTunnelEnum.YANGTIANSHAN.getEnergy();
        }else if(tunnel.equals(EnergyTunnelEnum.WANCHANGLIU.getTunnel())){
            tunnelId = EnergyTunnelEnum.WANCHANGLIU.getEnergy();
        }else if(tunnel.equals(EnergyTunnelEnum.PANDINGSHAN.getTunnel())){
            tunnelId = EnergyTunnelEnum.PANDINGSHAN.getEnergy();
        }else if(tunnel.equals(EnergyTunnelEnum.QINGFENGLING.getTunnel())){
            tunnelId = EnergyTunnelEnum.QINGFENGLING.getEnergy();
        }else if(tunnel.equals(EnergyTunnelEnum.MAJIAYU.getTunnel())){
            tunnelId = EnergyTunnelEnum.MAJIAYU.getEnergy();
        }
        return tunnelId;
    }

    public static String EnergyConvertTunnel(String tunnel) {
        String tunnelId = "";
        if(tunnel.equals(EnergyTunnelEnum.BEIJIAYU.getEnergy())){
            tunnelId = EnergyTunnelEnum.BEIJIAYU.getTunnel();
        }else if(tunnel.equals(EnergyTunnelEnum.HANGSHANDONG.getEnergy())){
            tunnelId = EnergyTunnelEnum.HANGSHANDONG.getTunnel();
        }else if(tunnel.equals(EnergyTunnelEnum.JINJIALOU.getEnergy())){
            tunnelId = EnergyTunnelEnum.JINJIALOU.getTunnel();
        }else if(tunnel.equals(EnergyTunnelEnum.MAANSHAN.getEnergy())){
            tunnelId = EnergyTunnelEnum.MAANSHAN.getTunnel();
        }else if(tunnel.equals(EnergyTunnelEnum.SHUANGZISHAN.getEnergy())){
            tunnelId = EnergyTunnelEnum.SHUANGZISHAN.getTunnel();
        }else if(tunnel.equals(EnergyTunnelEnum.YANGTIANSHAN.getEnergy())){
            tunnelId = EnergyTunnelEnum.YANGTIANSHAN.getTunnel();
        }else if(tunnel.equals(EnergyTunnelEnum.WANCHANGLIU.getEnergy())){
            tunnelId = EnergyTunnelEnum.WANCHANGLIU.getTunnel();
        }else if(tunnel.equals(EnergyTunnelEnum.PANDINGSHAN.getEnergy())){
            tunnelId = EnergyTunnelEnum.PANDINGSHAN.getTunnel();
        }else if(tunnel.equals(EnergyTunnelEnum.QINGFENGLING.getEnergy())){
            tunnelId = EnergyTunnelEnum.QINGFENGLING.getTunnel();
        }else if(tunnel.equals(EnergyTunnelEnum.MAJIAYU.getEnergy())){
            tunnelId = EnergyTunnelEnum.MAJIAYU.getTunnel();
        }
        return tunnelId;
    }


}