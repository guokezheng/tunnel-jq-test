package com.tunnel.business.service.road.impl;


import com.alibaba.fastjson.JSONArray;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.roadState.SdRoadDetectorData;
import com.tunnel.business.domain.roadState.SdRoadDetectorDataDTO;
import com.tunnel.business.mapper.event.SdWarningInfoMapper;
import com.tunnel.business.mapper.roadState.SdRoadDetectorDataMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.road.ISdRoadDetectorDataService;
import com.tunnel.business.utils.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 路面监测信息Service业务层处理
 *
 * @author lihaodong
 * @date 2021-11-17
 */
@Service
public class SdRoadDetectorDataServiceImpl implements ISdRoadDetectorDataService {
    @Autowired
    private SdRoadDetectorDataMapper sdRoadDetectorDataMapper;

    @Autowired
    private SdWarningInfoMapper sdWarningInfoMapper;

    @Autowired
    private ISdTunnelsService sdTunnelsService;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询路面监测信息
     *
     * @param id 路面监测信息ID
     * @return 路面监测信息
     */
    @Override
    public SdRoadDetectorData selectSdRoadDetectorDataById(Integer id) {
        return sdRoadDetectorDataMapper.selectSdRoadDetectorDataById(id);
    }

    /**
     * 查询路面监测信息列表
     *
     * @param sdRoadDetectorDataDTO 路面监测信息
     * @return 路面监测信息
     */
    @Override
    public List<SdRoadDetectorDataDTO> selectSdRoadDetectorDataList(SdRoadDetectorDataDTO sdRoadDetectorDataDTO) {
        Long deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        sdRoadDetectorDataDTO.getParams().put("deptId", deptId);
        return sdRoadDetectorDataMapper.selectSdRoadDetectorDataList(sdRoadDetectorDataDTO);
    }

    /**
     * 新增路面监测信息
     *
     * @param sdRoadDetectorDataDTO 路面监测信息
     * @return 结果
     */
    @Override
    public int insertSdRoadDetectorData(SdRoadDetectorData sdRoadDetectorDataDTO) {
        return sdRoadDetectorDataMapper.insertSdRoadDetectorData(sdRoadDetectorDataDTO);
    }

    /**
     * 修改路面监测信息
     *
     * @param sdRoadDetectorDataDTO 路面监测信息
     * @return 结果
     */
    @Override
    public int updateSdRoadDetectorData(SdRoadDetectorData sdRoadDetectorDataDTO) {
        return sdRoadDetectorDataMapper.updateSdRoadDetectorData(sdRoadDetectorDataDTO);
    }

    /**
     * 批量删除路面监测信息
     *
     * @param ids 需要删除的路面监测信息ID
     * @return 结果
     */
    @Override
    public int deleteSdRoadDetectorDataByIds(Integer[] ids) {
        return sdRoadDetectorDataMapper.deleteSdRoadDetectorDataByIds(ids);
    }

    /**
     * 删除路面监测信息信息
     *
     * @param id 路面监测信息ID
     * @return 结果
     */
    @Override
    public int deleteSdRoadDetectorDataById(Integer id) {
        return sdRoadDetectorDataMapper.deleteSdRoadDetectorDataById(id);
    }

    @Override
    public Map<String, Object> statisticsRoadMonitoring() {
        return sdRoadDetectorDataMapper.statisticsRoadMonitoring();
    }

    @Override
    public Map<String, Object> statisticsWarningTrend() {
        Map<String, Object> result = new HashMap<>();
//        //List<SdTunnels> sdTunnels = sdTunnelsService.selectSdTunnelsList(null);
//        result.put("tunnels", sdTunnels);
//        SdDevices devices = new SdDevices();
//        devices.setEqTunnelId(sdTunnels.get(0).getTunnelId());
//        List<SdDevices> sdDevices = sdDevicesService.selectSdDevicesList(devices);
//        result.put("devices", sdDevices);
//        Map<String, Object> statistics = roadDetectorDataMapper.statisticsYearSdDevices(String.valueOf(LocalDateTime.now().getYear()), sdDevices.get(0).getEqId());
//        result.put("statistics", statistics);
        return result;
    }

    @Override
    public Map<String, Object> statisticsWarningTrendSearch(String type, String startTime, String endTime, String eqId) {
        Map<String, Object> statistics = null;
        if ("year".equals(type)) {
            statistics = sdRoadDetectorDataMapper.statisticsYearSdDevices(startTime, eqId);
        }
        if ("month".equals(type)) {
            statistics = sdRoadDetectorDataMapper.statisticsWarningTrendSearchMonth(startTime, endTime, eqId);
        }
        if ("day".equals(type)) {
            statistics = sdRoadDetectorDataMapper.statisticsWarningTrendSearchDay(startTime, endTime, eqId);
        }
        return statistics;
    }

    @Override
    public List<Map<String, Object>> statisticsSearch(String type, String startTime, String tunnelId) {
        List<Map<String, Object>> statistics = null;
        if ("year".equals(type)) {
            statistics = sdRoadDetectorDataMapper.statisticsSearchYear(tunnelId, startTime);
        }
        if ("month".equals(type)) {
            statistics = sdRoadDetectorDataMapper.statisticsSearchMon(tunnelId, startTime);
        }
        if ("day".equals(type)) {
            statistics = sdRoadDetectorDataMapper.statisticsSearchDay(tunnelId, startTime);
        }
        return statistics;
    }

    @Override
    public List<Map<String, Object>> statisticsSearchWarningInfo(String type, String startTime, String tunnelId) {
        List<Map<String, Object>> statistics = null;
        if ("year".equals(type)) {
            statistics = sdWarningInfoMapper.statisticsSearcWarningYear(tunnelId, startTime);
        }
        if ("month".equals(type)) {
            statistics = sdWarningInfoMapper.statisticsSearchWarningMon(tunnelId, startTime);
        }
        if ("day".equals(type)) {
            statistics = sdWarningInfoMapper.statisticsSearchWarningDay(tunnelId, startTime);
        }
        return statistics;
    }

    @Override
    public List<Map<String, Object>> statisticsSearchPavementstatus(String type, String startTime, String tunnelId) {
        List<Map<String, Object>> statistics = null;
        if ("year".equals(type)) {
            statistics = sdRoadDetectorDataMapper.statisticsSearchPavementstatusYear(tunnelId, startTime);
        }
        if ("month".equals(type)) {
            statistics = sdRoadDetectorDataMapper.statisticsSearchPavementstatusMonth(tunnelId, startTime);
        }
        if ("day".equals(type)) {
            statistics = sdRoadDetectorDataMapper.statisticsSearchPavementstatusDay(tunnelId, startTime);
        }
        return statistics;
    }

    /**
     * 查询本年度隧道路面最高温度
     */
    @Override
    public Map<String, Object> statisticsSearcMaxPavementtemp(String startTime) {
        Map<String, Object> statistics = null;
        statistics = sdRoadDetectorDataMapper.statisticsSearcMaxPavementtemp(startTime);
        return statistics;
    }

    @Override
    public Map<String, Object> statisticsSearcMaxpWarningInfo(String startTime) {
        Map<String, Object> statistics = null;
        statistics = sdWarningInfoMapper.statisticsSearcMaxpWarningInfo(startTime);
        return statistics;
    }

    @Override
    public void dataAnalySis(String firstContent, String eqNumber) {
        SdRoadDetectorData sdRoadDetectorData = new SdRoadDetectorData();
        List<SdRoadDetectorData> list = new ArrayList<>();
        Date newDate = new Date();
        //按位截取=======================================
        if (firstContent.length() != 49) {
            throw new ServiceException("设备命令不完整！");
        } else {
            // 设备编号
            sdRoadDetectorData.setDevno(eqNumber);
            // 路面温度
            Integer pavementtemp1 = Integer.valueOf(firstContent.substring(3, 7));
            double pavementtemp;
            if (pavementtemp1 > 200) {
                pavementtemp = Math.abs(pavementtemp1 - 200) / 10;
            } else if (pavementtemp1 == 200) {
                pavementtemp = Math.abs(pavementtemp1 - 200) / 10;
            } else {
                pavementtemp = Math.abs(pavementtemp1 - 200) / 10;
                if (pavementtemp > 0) {
                    pavementtemp = -pavementtemp;
                }
            }
            sdRoadDetectorData.setPavementtemp(pavementtemp + "");
            // 水膜厚度、冰层厚度、积雪厚度、积水厚度
            String waterfilmheigh = toThickness(firstContent.substring(8, 12));
            // 滑动系数
            Double frictionalcoe = Double.parseDouble(firstContent.substring(13, 17));
            sdRoadDetectorData.setFrictionalcoe(frictionalcoe.toString());
            // 路面状态
            String pavementstatus = firstContent.substring(38, 40);
            switch (pavementstatus) {
                // 干燥
                case "00":
                    sdRoadDetectorData.setPavementstatus("干燥");
                    sdRoadDetectorData.setWaterfilmheigh(waterfilmheigh);
                    sdRoadDetectorData.setWaterthickness(waterfilmheigh);
                    sdRoadDetectorData.setSnowthickness(waterfilmheigh);
                    sdRoadDetectorData.setIcethickness(waterfilmheigh);
                    break;
                case "01":
                    sdRoadDetectorData.setPavementstatus("潮湿");
                    sdRoadDetectorData.setWaterfilmheigh(waterfilmheigh);
                    sdRoadDetectorData.setWaterthickness(waterfilmheigh);
                    sdRoadDetectorData.setSnowthickness("0");
                    sdRoadDetectorData.setIcethickness("0");
                    break;
                case "02":
                    sdRoadDetectorData.setPavementstatus("积水");
                    sdRoadDetectorData.setWaterfilmheigh(waterfilmheigh);
                    sdRoadDetectorData.setWaterthickness(waterfilmheigh);
                    sdRoadDetectorData.setSnowthickness("0");
                    sdRoadDetectorData.setIcethickness("0");
                    break;
                case "03":
                    sdRoadDetectorData.setPavementstatus("结冰");
                    sdRoadDetectorData.setWaterfilmheigh("0");
                    sdRoadDetectorData.setWaterthickness("0");
                    sdRoadDetectorData.setSnowthickness("0");
                    sdRoadDetectorData.setIcethickness(waterfilmheigh);
                    break;
                case "04":
                    sdRoadDetectorData.setPavementstatus("积雪");
                    sdRoadDetectorData.setWaterfilmheigh("0");
                    sdRoadDetectorData.setWaterthickness("0");
                    sdRoadDetectorData.setSnowthickness(waterfilmheigh);
                    sdRoadDetectorData.setIcethickness("0");
                    break;
                case "05":
                    sdRoadDetectorData.setPavementstatus("冰水混合物");
                    sdRoadDetectorData.setWaterfilmheigh(waterfilmheigh);
                    sdRoadDetectorData.setWaterthickness(waterfilmheigh);
                    sdRoadDetectorData.setSnowthickness("0");
                    sdRoadDetectorData.setIcethickness(waterfilmheigh);
                    break;
                default:
                    sdRoadDetectorData.setPavementstatus("泥泞");
                    sdRoadDetectorData.setWaterfilmheigh(waterfilmheigh);
                    sdRoadDetectorData.setWaterthickness(waterfilmheigh);
                    sdRoadDetectorData.setSnowthickness("0");
                    sdRoadDetectorData.setIcethickness("0");
                    break;
            }
            sdRoadDetectorData.setTimeCollect(newDate);
            sdRoadDetectorData.setTimeUpdate(newDate);
            list.add(sdRoadDetectorData);
            insertSdRoadDetectorData(sdRoadDetectorData);
        }
        //redis初始化
        redisCache = (RedisCache) SpringContextUtils.getBean(RedisCache.class);
        redisCache.setCacheObject(eqNumber, list);
    }

    @Override
    public Map<String, Object> selectByEqDeno(String eqNumber) {
        Map<String, Object> statistics = new HashMap<>();
        if (redisCache.getCacheObject(eqNumber) != null) {
            JSONArray jsonArray = redisCache.getCacheObject(eqNumber);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (jsonArray != null && !jsonArray.isEmpty()) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    SdRoadDetectorData sdRoadDetectorData = (SdRoadDetectorData) jsonArray.get(i);
                    statistics.put("frictionalcoe", sdRoadDetectorData.getFrictionalcoe());
                    statistics.put("icethickness", sdRoadDetectorData.getIcethickness());
                    statistics.put("pavementstatus", sdRoadDetectorData.getPavementstatus());
                    statistics.put("pavementtemp", sdRoadDetectorData.getPavementtemp());
                    statistics.put("snowthickness", sdRoadDetectorData.getSnowthickness());
                    statistics.put("waterfilmheigh", sdRoadDetectorData.getWaterfilmheigh());
                    statistics.put("waterthickness", sdRoadDetectorData.getWaterthickness());
                    statistics.put("timeCollect", sdRoadDetectorData.getTimeCollect());
                    statistics.put("id", 1);
                }
            }
        } else {
            statistics = sdRoadDetectorDataMapper.selectByEqDeno(eqNumber);
            statistics.put("id", 2);
        }
        return statistics;
    }

    /**
     * 计算水膜，冰层，积雪的厚度
     *
     * @return
     */
    public String toThickness(String str) {
        double thickness = Math.abs(Double.parseDouble(str)) / 100;
        return thickness + "";
    }
}
