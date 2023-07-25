package com.tunnel.platform.controller.energyManagement.energyTools;

import com.tunnel.business.domain.energyManagement.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author riemann
 */
public class CompareEnergyDataListsTools {


    public static  List<EnergySjfx> getDiffSetSite(List<EnergySjfx>oldList, List<EnergySjfx>newList) {

        System.out.println("=============================得到差集 reduce2 (list2 - list1)==================================");
        List<EnergySjfx> reduce2 = newList.stream().filter(
                a -> !oldList.stream().map(
                        b -> b.getTunnelId() + "&" + b.getStatisticsType()+ "&" + new java.sql.Timestamp(b.getCreateTime().getTime())+ "&" + b.getEnergyValue()).collect(Collectors.toList()).contains(a.getTunnelId() + "&" + a.getStatisticsType()+ "&" + a.getCreateTime()+ "&" + a.getEnergyValue()))
                .collect(Collectors.toList());
        return reduce2;


    }


    public static List<EnergySjfx> getDiffSetItem(List<EnergySjfx> oldList, List<EnergySjfx> newList) {
        List<EnergySjfx> reduce2 = newList.stream().filter(
                a -> !oldList.stream().map(
                        b -> b.getTunnelId()+ "&" + b.getItemizedCode() + "&" + b.getStatisticsType()+ "&" + new java.sql.Timestamp(b.getCreateTime().getTime())+ "&" + b.getEnergyValue()).collect(Collectors.toList()).contains(a.getTunnelId() + a.getItemizedCode() + "&" + a.getStatisticsType()+ "&" + a.getCreateTime()+ "&" + a.getEnergyValue()))
                .collect(Collectors.toList());
        return reduce2;
    }

    public static List<EnergySjfx> getDiffSetClassif(List<EnergySjfx> oldList, List<EnergySjfx> newList) {
        List<EnergySjfx> reduce2 = newList.stream().filter(
                a -> !oldList.stream().map(
                        b -> b.getTunnelId()+ "&" + b.getClassificationCode() + "&" + b.getStatisticsType()+ "&" + new java.sql.Timestamp(b.getCreateTime().getTime())+ "&" + b.getEnergyValue()).collect(Collectors.toList()).contains(a.getTunnelId() + a.getClassificationCode() + "&" + a.getStatisticsType()+ "&" + a.getCreateTime()+ "&" + a.getEnergyValue()))
                .collect(Collectors.toList());
        return reduce2;
    }

    public static List<EnergyDayparting> getDiffSetFootprint(List<EnergyDayparting> oldList, List<EnergyDayparting> newList) {
        List<EnergyDayparting> reduce2 = newList.stream().filter(
                a -> !oldList.stream().map(
                        b -> b.getTunnelId()+ "&" + b.getSource() + "&" + b.getTarget() + "&" + b.getStatisticsType()+ "&" + new java.sql.Timestamp(b.getCreateTime().getTime())+ "&" + b.getValue()).collect(Collectors.toList()).contains(a.getTunnelId() + "&" + a.getSource() + "&" + a.getTarget() + "&" + a.getStatisticsType()+ "&" + a.getCreateTime()+ "&" + a.getValue()))
                .collect(Collectors.toList());
        return reduce2;
    }

    public static List<EnergyDayparting> getDiffSetDayparting(List<EnergyDayparting> oldList, List<EnergyDayparting> newList) {
        List<EnergyDayparting> reduce2 = newList.stream().filter(
                a -> !oldList.stream().map(
                        b -> b.getTunnelId()+ "&" + b.getfValue() + "&" + b.getjValue() + "&" + b.getpValue() + "&" + b.getgValue() + "&" + b.getsValue() + "&" + b.getStatisticsType()+ "&" + new java.sql.Timestamp(b.getCreateTime().getTime())+ "&" + b.getValue()).collect(Collectors.toList()).contains(a.getTunnelId() + "&" + a.getfValue() + "&" + a.getjValue() + "&" + a.getpValue() + "&" + a.getgValue() + "&" + a.getsValue() + "&" + a.getStatisticsType()+ "&" + a.getCreateTime()+ "&" + a.getValue()))
                .collect(Collectors.toList());
        return reduce2;
    }
}