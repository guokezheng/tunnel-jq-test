package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdDataTrafficHour;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.SdDataTrafficHourMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.service.dataInfo.ISdDataTrafficHourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * 各路段小时车流量统计Service业务层处理
 *
 * @author ruoyi
 * @date 2022-02-18
 */
@Service
public class SdDataTrafficHourServiceImpl implements ISdDataTrafficHourService {
    @Autowired
    private SdDataTrafficHourMapper sdDataTrafficHourMapper;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Resource
    private RedisCache redisCache;

    /**
     * 查询各路段小时车流量统计
     *
     * @param id 各路段小时车流量统计主键
     * @return 各路段小时车流量统计
     */
    @Override
    public SdDataTrafficHour selectSdDataTrafficHourById(Long id) {
        return sdDataTrafficHourMapper.selectSdDataTrafficHourById(id);
    }

    /**
     * 查询各路段小时车流量统计列表
     *
     * @param sdDataTrafficHour 各路段小时车流量统计
     * @return 各路段小时车流量统计
     */
    @Override
    public List<SdDataTrafficHour> selectSdDataTrafficHourList(SdDataTrafficHour sdDataTrafficHour) {
        return sdDataTrafficHourMapper.selectSdDataTrafficHourList(sdDataTrafficHour);
    }

    /**
     * 新增各路段小时车流量统计
     *
     * @param sdDataTrafficHour 各路段小时车流量统计
     * @return 结果
     */
    @Override
    public int insertSdDataTrafficHour(SdDataTrafficHour sdDataTrafficHour) {
        SdDevices sdDevices = sdDevicesMapper.selectSdDevicesById(sdDataTrafficHour.getDeviceId());
        sdDataTrafficHour.setEqType(sdDevices.getEqType());
        return sdDataTrafficHourMapper.insertSdDataTrafficHour(sdDataTrafficHour);
    }

    /**
     * 修改各路段小时车流量统计
     *
     * @param sdDataTrafficHour 各路段小时车流量统计
     * @return 结果
     */
    @Override
    public int updateSdDataTrafficHour(SdDataTrafficHour sdDataTrafficHour) {
        return sdDataTrafficHourMapper.updateSdDataTrafficHour(sdDataTrafficHour);
    }

    /**
     * 批量删除各路段小时车流量统计
     *
     * @param ids 需要删除的各路段小时车流量统计主键
     * @return 结果
     */
    @Override
    public int deleteSdDataTrafficHourByIds(Long[] ids) {
        return sdDataTrafficHourMapper.deleteSdDataTrafficHourByIds(ids);
    }

    /**
     * 删除各路段小时车流量统计信息
     *
     * @param id 各路段小时车流量统计主键
     * @return 结果
     */
    @Override
    public int deleteSdDataTrafficHourById(Long id) {
        return sdDataTrafficHourMapper.deleteSdDataTrafficHourById(id);
    }

    @Override
    public List<Map<String, Object>> getCarNumberByHour(SdDataTrafficHour sdDataTrafficHour) throws ParseException {
        String deptId = SecurityUtils.getDeptId();
        sdDataTrafficHour.getParams().put("deptId", deptId);
        if (null == sdDataTrafficHour.getTunnelId() || ("").equals(sdDataTrafficHour.getTunnelId())) {
            if (null == sdDataTrafficHour.getTunnelName() || ("").equals(sdDataTrafficHour.getTunnelName())) {
                throw new RuntimeException("查询条件中缺少隧道，请指定隧道后重试！");
            }
        }
        Map<String, Object> params = sdDataTrafficHour.getParams();
        if (null == params.get("day")) {
            throw new RuntimeException("查询时间条件为空，请选择查询时间！");
        }
        String tunnelId = sdDataTrafficHour.getTunnelId();
        String tunnelName = sdDataTrafficHour.getTunnelName();
        String metEnd = params.get("day").toString();
        params.put("datea", metEnd + " 00:00:00");
        params.put("dateb", metEnd + " 23:59:59");
        // 只查询微波车检类型的设备记录的数据
        params.put("eqType", "108");
        List<Map<String, Object>> daymaps = sdDataTrafficHourMapper.getCarNumberByHour(tunnelId, tunnelName, params);
        return daymaps;
    }

    @Override
    public List<Map<String, Object>> countYesterdayTrafficFlowData() {
        return sdDataTrafficHourMapper.countYesterdayTrafficFlowData();
    }

    @Override
    public List<Map<String, Object>> getCacheTrafficFlowInformation() {
        // 查询体育中心和会展中心两条隧道中的所有车流量监视器，组成获取缓存的key值
        SdDevices tiyuDevices = new SdDevices();
        String tiyu = "JiNanCompany-Huanghe-001";
        String huizhan = "JiNanCompany-Huanghe-002";
        tiyuDevices.setEqType(126L);
        tiyuDevices.setEqTunnelId(tiyu);
        List<SdDevices> tiyuDevicesList = sdDevicesMapper.selectSdDevicesList(tiyuDevices);
        tiyuDevices.setEqTunnelId(huizhan);
        List<SdDevices> huizhanDevicesList = sdDevicesMapper.selectSdDevicesList(tiyuDevices);
        //将查询到的实时车流量状态保存到集合中
        List<Map<String, Object>> tiyuStateMap = new ArrayList<>();
        List<Map<String, Object>> huizhanStateMap = new ArrayList<>();
        for (int i = 0; i < tiyuDevicesList.size(); i++) {
            SdDevices devices = tiyuDevicesList.get(i);
            Map<String, Object> cacheMap = redisCache.getCacheMap(tiyu + "-" + devices.getPile() + "-" + devices.getEqId());
            if (cacheMap != null) {
                cacheMap.put("stakeMark", stakeMarkToNumber(devices.getPile()));
                cacheMap.put("deviceLane", devices.getLane());
                cacheMap.put("tunnelId", devices.getEqTunnelId());
                tiyuStateMap.add(cacheMap);
            }
        }
        for (int j = 0; j < huizhanDevicesList.size(); j++) {
            SdDevices devices = huizhanDevicesList.get(j);
            Map<String, Object> cacheMap = redisCache.getCacheMap(huizhan + "-" + devices.getPile() + "-" + devices.getEqId());
            if (cacheMap != null) {
                cacheMap.put("stakeMark", stakeMarkToNumber(devices.getPile()));
                cacheMap.put("deviceLane", devices.getLane());
                cacheMap.put("tunnelId", devices.getEqTunnelId());
                huizhanStateMap.add(cacheMap);
            }
        }
        //对两个状态集合内的数据按桩号排序
        Collections.sort(tiyuStateMap, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String id1 = (String) o1.get("stakeMark");
                String id2 = (String) o2.get("stakeMark");
                return id1.compareTo(id2);
            }
        });
        Collections.sort(huizhanStateMap, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String id1 = (String) o1.get("stakeMark");
                String id2 = (String) o2.get("stakeMark");
                return id1.compareTo(id2);
            }
        });
        List<Map<String, Object>> tiyuStateMapLaneOne = new ArrayList<>();
        List<Map<String, Object>> tiyuStateMapLaneTwo = new ArrayList<>();
        List<Map<String, Object>> tiyuStateMapLaneThree = new ArrayList<>();
        List<Map<String, Object>> tiyuStateMapLaneFour = new ArrayList<>();
        for (int i = 0; i < tiyuStateMap.size(); i++) {
            Map<String, Object> stringObjectMap = tiyuStateMap.get(i);
            String deviceLane = stringObjectMap.get("deviceLane").toString();
            if (deviceLane.equals("1")) {
                tiyuStateMapLaneOne.add(stringObjectMap);
            } else if (deviceLane.equals("2")) {
                tiyuStateMapLaneTwo.add(stringObjectMap);
            } else if (deviceLane.equals("3")) {
                tiyuStateMapLaneThree.add(stringObjectMap);
            } else if (deviceLane.equals("4")) {
                tiyuStateMapLaneFour.add(stringObjectMap);
            }
        }
        List<Map<String, Object>> huizhanStateMapLaneOne = new ArrayList<>();
        List<Map<String, Object>> huizhanStateMapLaneTwo = new ArrayList<>();
        List<Map<String, Object>> huizhanStateMapLaneThree = new ArrayList<>();
        List<Map<String, Object>> huizhanStateMapLaneFour = new ArrayList<>();
        for (int i = 0; i < huizhanStateMap.size(); i++) {
            Map<String, Object> stringObjectMap = huizhanStateMap.get(i);
            String deviceLane = stringObjectMap.get("deviceLane").toString();
            if (deviceLane.equals("1")) {
                huizhanStateMapLaneOne.add(stringObjectMap);
            } else if (deviceLane.equals("2")) {
                huizhanStateMapLaneTwo.add(stringObjectMap);
            } else if (deviceLane.equals("3")) {
                huizhanStateMapLaneThree.add(stringObjectMap);
            } else if (deviceLane.equals("4")) {
                huizhanStateMapLaneFour.add(stringObjectMap);
            }
        }
        //遍历状态集合构建前端需要的结构体
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> tiyuStateMapLaneOneResult = createReturnResult(tiyuStateMapLaneOne, "1");
        result = moveResult(tiyuStateMapLaneOneResult, result);
        List<Map<String, Object>> returnResult = createReturnResult(tiyuStateMapLaneTwo, "2");
        result = moveResult(returnResult, result);
        List<Map<String, Object>> returnResult1 = createReturnResult(tiyuStateMapLaneThree, "3");
        result = moveResult(returnResult1, result);
        List<Map<String, Object>> returnResult2 = createReturnResult(tiyuStateMapLaneFour, "4");
        result = moveResult(returnResult2, result);
        List<Map<String, Object>> returnResult3 = createReturnResult(huizhanStateMapLaneOne, "1");
        result = moveResult(returnResult3, result);
        List<Map<String, Object>> returnResult4 = createReturnResult(huizhanStateMapLaneTwo, "2");
        result = moveResult(returnResult4, result);
        List<Map<String, Object>> returnResult5 = createReturnResult(huizhanStateMapLaneThree, "3");
        result = moveResult(returnResult5, result);
        List<Map<String, Object>> returnResult6 = createReturnResult(huizhanStateMapLaneFour, "4");
        result = moveResult(returnResult6, result);
        return result;
    }

    public List<Map<String, Object>> moveResult(List<Map<String, Object>> from, List<Map<String, Object>> to) {
        for (int i = 0; i < from.size(); i++) {
            Map<String, Object> objectMap = from.get(i);
            to.add(objectMap);
        }
        return to;
    }

    public List<Map<String, Object>> createReturnResult(List<Map<String, Object>> data, String type) {
        // K5+639
        double start = 5.639;
        // K9+965.9
        double totel = 4.3269;
        double stakeMark = 0;
        String left = "";
        String previousPercent = "";
        double previous = 0;
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> stringObjectMap = data.get(i);
            String byLaneStateName = stringObjectMap.get("byLaneStateName") == null ? "" : stringObjectMap.get("byLaneStateName").toString();
            if (byLaneStateName.equals("拥挤")) {
                Map<String, Object> map = new HashMap<>();
                map.put("backgroundColor", "yellow");
                //存距离百分比的时候要判断是不是第一个设备
                stakeMark = Double.parseDouble(stringObjectMap.get("stakeMark").toString());
                if (i == 0) {
                    DecimalFormat format = new DecimalFormat("0.0000");
                    String s = format.format((stakeMark - start) / totel * 100);
                    left = s + "%";
                    map.put("left", "0%");
                    map.put("width", left);
                } else {
                    DecimalFormat format = new DecimalFormat("0.0000");
                    previous = Double.parseDouble(data.get(i - 1).get("stakeMark").toString());
                    String s = format.format((stakeMark - previous) / totel * 100);
                    left = s + "%";
                    map.put("width", left);
                    String s1 = format.format((previous - start) / totel * 100);
                    previousPercent = s1 + "%";
                    map.put("left", previousPercent);
                }
//                if (type.equals("1") || type.equals("4") || type.equals("5") || type.equals("8")) {
//                    map.put("left","0%");
//                    map.put("width",left);
//                } else {
//                    map.put("left",left);
//                    map.put("width","0%");
//                }
                map.put("tunnelId", stringObjectMap.get("tunnelId").toString());
                map.put("height", "16px");
                map.put("type", type);
                result.add(map);
            } else if (byLaneStateName.equals("堵塞")) {
                Map<String, Object> map = new HashMap<>();
                map.put("backgroundColor", "red");
                //存距离百分比的时候要判断是不是第一个设备
                stakeMark = Double.parseDouble(stringObjectMap.get("stakeMark").toString());
                if (i == 0) {
                    DecimalFormat format = new DecimalFormat("0.0000");
                    String s = format.format((stakeMark - start) / totel * 100);
                    left = s + "%";
                    map.put("left", "0%");
                    map.put("width", left);
                } else {
                    DecimalFormat format = new DecimalFormat("0.0000");
                    previous = Double.parseDouble(data.get(i - 1).get("stakeMark").toString());
                    String s = format.format((stakeMark - previous) / totel * 100);
                    left = s + "%";
                    map.put("width", left);
                    String s1 = format.format((previous - start) / totel * 100);
                    previousPercent = s1 + "%";
                    map.put("left", previousPercent);
                }
//                if (type.equals("1") || type.equals("4") || type.equals("5") || type.equals("8")) {
//                    map.put("left","0%");
//                    map.put("width",left);
//                } else {
//                    map.put("left",left);
//                    map.put("width","0%");
//                }
                map.put("tunnelId", stringObjectMap.get("tunnelId").toString());
                map.put("height", "16px");
                map.put("type", type);
                result.add(map);
            }
        }
        return result;
    }

    public String stakeMarkToNumber(String station) {
        //K10+234
        String exactStation = "";
        String offset = "";
        if (station.contains("+")) {
            if (station.contains("K")) {
                exactStation = station.substring(station.indexOf("K") + 1, station.indexOf("+"));
            } else if (station.contains("k")) {
                exactStation = station.substring(station.indexOf("k") + 1, station.indexOf("+"));
            }
            offset = station.substring(station.indexOf("+") + 1);
        } else {
            if (station.contains("K")) {
                exactStation = station.substring(station.indexOf("K") + 1);
            } else if (station.contains("k")) {
                exactStation = station.substring(station.indexOf("k") + 1);
            }
            offset = "0";
        }
        //10.234
        station = exactStation.concat(".").concat(offset);
        return station;
    }


    @Override
    public void addRedisData() {
        SdDevices tiyuDevices = new SdDevices();
        String tiyu = "JiNanCompany-Huanghe-001";
        String huizhan = "JiNanCompany-Huanghe-002";
        tiyuDevices.setEqTunnelId(tiyu);
        tiyuDevices.setEqType(126L);
        List<SdDevices> tiyuDevicesList = sdDevicesMapper.selectSdDevicesList(tiyuDevices);
        tiyuDevices.setEqTunnelId(huizhan);
        List<SdDevices> huizhanDevicesList = sdDevicesMapper.selectSdDevicesList(tiyuDevices);
        for (int i = 0; i < tiyuDevicesList.size(); i++) {
            SdDevices sdDevices1 = tiyuDevicesList.get(i);
            String redisKeyName = sdDevices1.getEqTunnelId() + "-" + sdDevices1.getPile() + "-" + sdDevices1.getEqId();
//                String realTimeTrafficFlowInformation = "通道号：" + channel + "; 时间：" + struTime + ";对应车道号：" + byLane
//                        + ";当前车道 从上到下车流量：" + dwDownwardFlow + ";当前车道 从下到上车流量：" + dwUpwardFlow
//                        + ";对应车速：" + bySpeed + "KM/H;当前车道拥挤状态：" + byLaneStateName;
            Map<String, Object> realTimeTrafficFlowInformationMap = new HashMap<>();
            realTimeTrafficFlowInformationMap.put("channel", i);
            realTimeTrafficFlowInformationMap.put("struTime", new Date());
            realTimeTrafficFlowInformationMap.put("byLane", sdDevices1.getLane());
            realTimeTrafficFlowInformationMap.put("dwDownwardFlow", new Random().nextInt(900));
            realTimeTrafficFlowInformationMap.put("dwUpwardFlow", new Random().nextInt(900));
            realTimeTrafficFlowInformationMap.put("bySpeed", new Random().nextInt(150));
            if (i / 2 == 0) {
                realTimeTrafficFlowInformationMap.put("byLaneStateName", "拥挤");
            } else if (i / 3 == 0) {
                realTimeTrafficFlowInformationMap.put("byLaneStateName", "堵塞");
            } else {
                realTimeTrafficFlowInformationMap.put("byLaneStateName", "畅通");
            }
            realTimeTrafficFlowInformationMap.put("eqDirection", sdDevices1.getEqDirection());
            //存储实时车流量到redis
            redisCache.setCacheMap(redisKeyName, realTimeTrafficFlowInformationMap);
        }
        for (int i = 0; i < huizhanDevicesList.size(); i++) {
            SdDevices sdDevices1 = huizhanDevicesList.get(i);
            String redisKeyName = sdDevices1.getEqTunnelId() + "-" + sdDevices1.getPile() + "-" + sdDevices1.getEqId();
            Map<String, Object> realTimeTrafficFlowInformationMap = new HashMap<>();
            realTimeTrafficFlowInformationMap.put("channel", i);
            realTimeTrafficFlowInformationMap.put("struTime", new Date());
            realTimeTrafficFlowInformationMap.put("byLane", sdDevices1.getLane());
            realTimeTrafficFlowInformationMap.put("dwDownwardFlow", new Random().nextInt(900));
            realTimeTrafficFlowInformationMap.put("dwUpwardFlow", new Random().nextInt(900));
            realTimeTrafficFlowInformationMap.put("bySpeed", new Random().nextInt(150));
            if (i / 2 == 0) {
                realTimeTrafficFlowInformationMap.put("byLaneStateName", "拥挤");
            } else if (i / 3 == 0) {
                realTimeTrafficFlowInformationMap.put("byLaneStateName", "堵塞");
            } else {
                realTimeTrafficFlowInformationMap.put("byLaneStateName", "畅通");
            }
            realTimeTrafficFlowInformationMap.put("eqDirection", sdDevices1.getEqDirection());
            //存储实时车流量到redis
            redisCache.setCacheMap(redisKeyName, realTimeTrafficFlowInformationMap);
        }

    }
}
