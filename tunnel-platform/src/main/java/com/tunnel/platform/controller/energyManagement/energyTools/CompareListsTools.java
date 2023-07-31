package com.tunnel.platform.controller.energyManagement.energyTools;

import com.tunnel.business.domain.energyManagement.EnergyConfigcenterClassification;
import com.tunnel.business.domain.energyManagement.EnergyConfigcenterElectricityPrice;
import com.tunnel.business.domain.energyManagement.EnergyConfigcenterItemized;
import com.tunnel.business.domain.energyManagement.EnergySjfx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author riemann
 */
public class CompareListsTools {


    public static Map<String, List<EnergyConfigcenterClassification>> getDiffSet(List<EnergyConfigcenterClassification>oldList, List<EnergyConfigcenterClassification>newList) {
        List<List<EnergyConfigcenterClassification>> resultList = null;
        Map<String, List<EnergyConfigcenterClassification>> resultMap = new HashMap<>();
        List<EnergyConfigcenterClassification>updateEnergyTypeDataList =null;
        List<EnergyConfigcenterClassification>insertEnergyTypeDataList =null;
        List<String> oldIds = new ArrayList<>();
        List<String> newIds = new ArrayList<>();
        oldList.stream().forEach(it->oldIds.add(it.getClassificationCode()));
        newList.stream().forEach(it->newIds.add(it.getClassificationCode()));
        //取交集id
        System.out.println("-----------------交集----------------------");
        List<String> collectUpdate = newIds.stream().filter(it -> oldIds.contains(it)).collect(Collectors.toList());
        collectUpdate.stream().forEach(System.out::println);

        //取对应交集的对象
        System.out.println("------------------交集的对象---------------------");
        //updateEnergyTypeDataList = newList.stream().filter(o -> o.getId() == id).findFirst().get();
        updateEnergyTypeDataList = newList.stream().filter(it -> collectUpdate.contains(it.getClassificationCode())).collect(Collectors.toList());

        //取old的差集
        System.out.println("-----------------old的差集----------------------");
        List<String> collectDelete = oldIds.stream().filter(it -> !newIds.contains(it)).collect(Collectors.toList());
        collectDelete.stream().forEach((System.out::println));
        //取对应old差集对象
        System.out.println("-----------------old差集对象----------------------");
        insertEnergyTypeDataList = oldList.stream().filter(it -> collectDelete.contains(it.getClassificationCode())).collect(Collectors.toList());
        resultMap.put("update",updateEnergyTypeDataList);
        resultMap.put("insert",insertEnergyTypeDataList);
        return resultMap;

    }


    public static Map<String, List<EnergyConfigcenterElectricityPrice>> getDiffSetPrice(List<EnergyConfigcenterElectricityPrice> oldList, List<EnergyConfigcenterElectricityPrice> newList) {
        List<List<EnergyConfigcenterElectricityPrice>> resultList = null;
        Map<String, List<EnergyConfigcenterElectricityPrice>> resultMap = new HashMap<>();
        List<EnergyConfigcenterElectricityPrice>updateEnergyTypeDataList =null;
        List<EnergyConfigcenterElectricityPrice>insertEnergyTypeDataList =null;
        List<String> oldIds = new ArrayList<>();
        List<String> newIds = new ArrayList<>();
        oldList.stream().forEach(it->oldIds.add(it.getMonth()));
        newList.stream().forEach(it->newIds.add(it.getMonth()));
        //取交集id
        System.out.println("-----------------交集----------------------");
        List<String> collectUpdate = newIds.stream().filter(it -> oldIds.contains(it)).collect(Collectors.toList());
        collectUpdate.stream().forEach(System.out::println);

        //取对应交集的对象
        System.out.println("------------------交集的对象---------------------");
        //updateEnergyTypeDataList = newList.stream().filter(o -> o.getId() == id).findFirst().get();
        updateEnergyTypeDataList = newList.stream().filter(it -> collectUpdate.contains(it.getMonth())).collect(Collectors.toList());

        //取old的差集
        System.out.println("-----------------old的差集----------------------");
        List<String> collectDelete = oldIds.stream().filter(it -> !newIds.contains(it)).collect(Collectors.toList());
        collectDelete.stream().forEach((System.out::println));
        //取对应old差集对象
        System.out.println("-----------------old差集对象----------------------");
        insertEnergyTypeDataList = oldList.stream().filter(it -> collectDelete.contains(it.getMonth())).collect(Collectors.toList());
        resultMap.put("update",updateEnergyTypeDataList);
        resultMap.put("insert",insertEnergyTypeDataList);
        return resultMap;

    }

    public static Map<String, List<EnergyConfigcenterItemized>> getDiffSetItem(List<EnergyConfigcenterItemized> oldList, List<EnergyConfigcenterItemized> newList) {
        List<List<EnergyConfigcenterItemized>> resultList = null;
        Map<String, List<EnergyConfigcenterItemized>> resultMap = new HashMap<>();
        List<EnergyConfigcenterItemized>updateEnergyTypeDataList =null;
        List<EnergyConfigcenterItemized>insertEnergyTypeDataList =null;
        List<String> oldIds = new ArrayList<>();
        List<String> newIds = new ArrayList<>();
        oldList.stream().forEach(it->oldIds.add(it.getItemizedCode()));
        newList.stream().forEach(it->newIds.add(it.getItemizedCode()));
        //取交集id
        System.out.println("-----------------交集----------------------");
        List<String> collectUpdate = newIds.stream().filter(it -> oldIds.contains(it)).collect(Collectors.toList());
        collectUpdate.stream().forEach(System.out::println);

        //取对应交集的对象
        System.out.println("------------------交集的对象---------------------");
        //updateEnergyTypeDataList = newList.stream().filter(o -> o.getId() == id).findFirst().get();
        updateEnergyTypeDataList = newList.stream().filter(it -> collectUpdate.contains(it.getItemizedCode())).collect(Collectors.toList());

        //取old的差集
        System.out.println("-----------------old的差集----------------------");
        List<String> collectDelete = oldIds.stream().filter(it -> !newIds.contains(it)).collect(Collectors.toList());
        collectDelete.stream().forEach((System.out::println));
        //取对应old差集对象
        System.out.println("-----------------old差集对象----------------------");
        insertEnergyTypeDataList = oldList.stream().filter(it -> collectDelete.contains(it.getItemizedCode())).collect(Collectors.toList());
        resultMap.put("update",updateEnergyTypeDataList);
        resultMap.put("insert",insertEnergyTypeDataList);
        return resultMap;


    }


}