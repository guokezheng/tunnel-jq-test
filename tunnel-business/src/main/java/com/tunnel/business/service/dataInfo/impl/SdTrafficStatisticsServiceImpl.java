package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdTrafficStatistics;
import com.tunnel.business.mapper.dataInfo.SdTrafficStatisticsMapper;
import com.tunnel.business.service.dataInfo.ISdTrafficStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 车流量信息Service业务层处理
 *
 * @author yanghousheng
 * @date 2020-11-10
 */
@Service
public class SdTrafficStatisticsServiceImpl implements ISdTrafficStatisticsService {
    @Autowired
    private SdTrafficStatisticsMapper sdTrafficStatisticsMapper;

    /**
     * 查询车流量信息
     *
     * @param statisticsId 车流量信息ID
     * @return 车流量信息
     */
    @Override
    public SdTrafficStatistics selectSdTrafficStatisticsById(Long statisticsId) {
        return sdTrafficStatisticsMapper.selectSdTrafficStatisticsById(statisticsId);
    }

    /**
     * 查询车流量信息列表
     *
     * @param sdTrafficStatistics 车流量信息
     * @return 车流量信息
     */
    @Override
    public List<SdTrafficStatistics> selectSdTrafficStatisticsList(SdTrafficStatistics sdTrafficStatistics) {
        String deptId = SecurityUtils.getDeptId();
        sdTrafficStatistics.getParams().put("deptId", deptId);
        return sdTrafficStatisticsMapper.selectSdTrafficStatisticsList(sdTrafficStatistics);
    }

    /**
     * 新增车流量信息
     *
     * @param sdTrafficStatistics 车流量信息
     * @return 结果
     */
    @Override
    public int insertSdTrafficStatistics(SdTrafficStatistics sdTrafficStatistics) {
        sdTrafficStatistics.setCreateTime(DateUtils.getNowDate());
        return sdTrafficStatisticsMapper.insertSdTrafficStatistics(sdTrafficStatistics);
    }

    /**
     * 修改车流量信息
     *
     * @param sdTrafficStatistics 车流量信息
     * @return 结果
     */
    @Override
    public int updateSdTrafficStatistics(SdTrafficStatistics sdTrafficStatistics) {
        return sdTrafficStatisticsMapper.updateSdTrafficStatistics(sdTrafficStatistics);
    }

    /**
     * 批量删除车流量信息
     *
     * @param statisticsIds 需要删除的车流量信息ID
     * @return 结果
     */
    @Override
    public int deleteSdTrafficStatisticsByIds(Long[] statisticsIds) {
        return sdTrafficStatisticsMapper.deleteSdTrafficStatisticsByIds(statisticsIds);
    }

    /**
     * 删除车流量信息信息
     *
     * @param statisticsId 车流量信息ID
     * @return 结果
     */
    @Override
    public int deleteSdTrafficStatisticsById(Long statisticsId) {
        return sdTrafficStatisticsMapper.deleteSdTrafficStatisticsById(statisticsId);
    }

    /**
     * 查询大中小型车流量信息
     */
    @Override
    public Map<String, Object> queryStatistics(String beginTime, String endTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<SdTrafficStatistics> list = sdTrafficStatisticsMapper.queryStatistics(beginTime, endTime);
        SdTrafficStatistics sdTrafficStatistics = list.get(0);
        Long count1 = sdTrafficStatistics.getDwHeavyVehicle();
        Long count2 = sdTrafficStatistics.getDwLightVehicle();
        Long count3 = sdTrafficStatistics.getDwMidVehicle();
        Long count = count1 + count2 + count3;
        map.put("count", count);
        return map;
    }

    @Override
    public List<SdTrafficStatistics> selectSdTrafficStatisticsToday(SdTrafficStatistics sd) {
        return sdTrafficStatisticsMapper.selectSdTrafficStatisticsToday(sd);
    }

    /**
     * 分析
     *
     * @param tunnelId 隧道ID
     */
    @Override
    public Map<String, List<SdTrafficStatistics>> getAnalysisData(String tunnelId, String holes) {
        // 年度
        List<SdTrafficStatistics> yearData = sdTrafficStatisticsMapper.getAnalysisData("year", tunnelId, holes);
        List<SdTrafficStatistics> monthData = sdTrafficStatisticsMapper.getAnalysisData("month", tunnelId, holes);
        List<SdTrafficStatistics> quarterData = sdTrafficStatisticsMapper.getAnalysisData("quarter", tunnelId, holes);
        List<SdTrafficStatistics> dayData = sdTrafficStatisticsMapper.getAnalysisData("day", tunnelId, holes);
        Map<String, Object> params = new HashMap<String, Object>();
        List<SdTrafficStatistics> timeData = sdTrafficStatisticsMapper.analysisDataByHourTime(params, tunnelId, holes);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        params.put("beginTime", sdf.format(getStartTime(new Date())));
        params.put("endTime", sdf.format(getEndTime(new Date())));
        List<SdTrafficStatistics> hourData = sdTrafficStatisticsMapper.analysisDataByHourTime(params, tunnelId, holes);
        Map<String, List<SdTrafficStatistics>> map
                = new HashMap<String, List<SdTrafficStatistics>>();

        // 加0
        emptyToZero("quarter", quarterData);
        emptyToZero("month", monthData);
        emptyToZero("day", dayData);

        map.put("yearData", yearData);
        map.put("monthData", monthData);
        map.put("quarterData", quarterData);
        map.put("dayData", dayData);
        map.put("hourData", hourData);
        map.put("timeData", timeData);
        return map;
    }

    /**
     * 加0操作
     */
    private void emptyToZero(String type, List<SdTrafficStatistics> data) {
        // 月
        if (data.size() <= 0) {
            return;
        }
        if ("month".equals(type)) {
            for (int i = 1; i <= 12; i++) {
                boolean flag = false;
                String iDate = "";
                if (i < 10) {
                    iDate = "0" + i;
                } else {
                    iDate = "" + i;
                }
                for (int j = 0; j < data.size(); j++) {
                    String date = data.get(j).getDate();
                    String strDate = date.substring(date.length() - 2);
                    if (iDate.equals(strDate)) {
                        flag = true;
                    }
                }
                if (!flag) {
                    SdTrafficStatistics sdTrafficStatistics = new SdTrafficStatistics();
                    sdTrafficStatistics.setDate(data.get(0).getDate().split("-")[0] + "-" + iDate);
                    sdTrafficStatistics.setByVehicelNum(0L);
                    data.add(i - 1, sdTrafficStatistics);
                }
            }
        }
        // 季度
        else if ("quarter".equals(type)) {
            for (int i = 1; i <= 4; i++) {
                boolean flag = false;
                for (int j = 0; j < data.size(); j++) {
                    String date = data.get(j).getDate();
                    if (String.valueOf(i).equals(date)) {
                        flag = true;
                    }
                }
                if (!flag) {
                    SdTrafficStatistics sdTrafficStatistics = new SdTrafficStatistics();
                    sdTrafficStatistics.setDate(String.valueOf(i));
                    sdTrafficStatistics.setByVehicelNum(0L);
                    data.add(sdTrafficStatistics);
                }
            }
        }
        // 天
        else if ("day".equals(type)) {
            // 判断年份
            if (data.size() > 0) {
                String date = data.get(0).getDate();
                // 获取月份
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date nowDate = new Date();
                try {
                    nowDate = simpleDateFormat.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // 获取天数
                int days = getDaysOfMonth(nowDate);
                for (int i = 1; i <= days; i++) {
                    boolean flag = false;
                    String iDate = "";
                    if (i < 10) {
                        iDate = "0" + i;
                    } else {
                        iDate = "" + i;
                    }
                    for (int j = 0; j < data.size(); j++) {
                        String date1 = data.get(j).getDate();
                        String strDate = date1.substring(date1.length() - 2);
                        if (iDate.equals(strDate)) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        SdTrafficStatistics sdTrafficStatistics = new SdTrafficStatistics();
                        sdTrafficStatistics.setDate(data.get(0).getDate().split("-")[0] + "-" + data.get(0).getDate().split("-")[1] + "-" + iDate);
                        sdTrafficStatistics.setByVehicelNum(0L);
                        data.add(i - 1, sdTrafficStatistics);
                    }
                }
            }
        }
    }

    public int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

    }

    private Date getStartTime(Date date) {
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTime(date);
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    private Date getEndTime(Date date) {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTime(date);
        todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    /**
     * 检索用
     *
     * @param sdTrafficStatistics
     */
    @Override
    public List<SdTrafficStatistics> analysisDataByTime(SdTrafficStatistics sdTrafficStatistics) {
        try {
            if ("time".equals(sdTrafficStatistics.getEqDirection())) {
                List<SdTrafficStatistics> yearData
                        = sdTrafficStatisticsMapper.analysisDataByHourTime(
                        sdTrafficStatistics.getParams(), sdTrafficStatistics.getTunnelId(), sdTrafficStatistics.getHoles());
                return yearData;
            } else if ("hour".equals(sdTrafficStatistics.getEqDirection())) {
                Map<String, Object> map = sdTrafficStatistics.getParams();
                if (map.get("beginTime") != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = map.get("beginTime").toString();
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("endTime", sdf.format(getEndTime(sdf.parse(time))));
                    params.put("beginTime", sdf.format(getStartTime(sdf.parse(time))));
                    sdTrafficStatistics.setParams(params);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("beginTime", sdf.format(getStartTime(new Date())));
                    params.put("endTime", sdf.format(getEndTime(new Date())));
                    sdTrafficStatistics.setParams(params);
                }
                List<SdTrafficStatistics> yearData
                        = sdTrafficStatisticsMapper.analysisDataByHourTime(
                        sdTrafficStatistics.getParams(), sdTrafficStatistics.getTunnelId(), sdTrafficStatistics.getHoles());
                return yearData;
            } else {
                List<SdTrafficStatistics> yearData = sdTrafficStatisticsMapper.analysisDataByTime(sdTrafficStatistics);
                emptyToZero(sdTrafficStatistics.getEqDirection(), yearData);
                return yearData;
            }
        } catch (Exception e) {
            return new ArrayList<SdTrafficStatistics>();
        }

    }

    @Override
    public Map<String, Object> getTrafficFlowMsgToApp() {
        Map<String, Object> details = new HashMap<>();
        //日累计车流量
        Map<String, Object> sumTrafficFlow = sdTrafficStatisticsMapper.selectSumTrafficFlow();
        if ("0".equals(sumTrafficFlow.get("sumtraffic").toString())) {
            details.put("sumTrafficFlow", "0");
            details.put("recentTrafficFlow", "0");
            details.put("carFreePeriod", "0");
            return details;
        }
        details.put("sumTrafficFlow", sumTrafficFlow.get("sumtraffic"));
        //当前车流量
        //先获取已经记录的设备ID
        List<SdTrafficStatistics> devicesIdInBase = sdTrafficStatisticsMapper.getDevicesIdInBase();
        //查询设备列表中设备最近一条检测记录
        long recentTrafficFlow = 0L;
        for (int i = 0; i < devicesIdInBase.size(); i++) {
            String deviceId = devicesIdInBase.get(i).getDeviceId();
            Map<String, Object> recentFlow = sdTrafficStatisticsMapper.selectRecentTrafficFlow(deviceId);
            long recentsum = Long.parseLong(recentFlow.get("recentsum").toString());
            recentTrafficFlow = recentTrafficFlow + recentsum;
        }
        details.put("recentTrafficFlow", recentTrafficFlow);
        //当天隧道无车时间
        details.put("carFreePeriod", "6");
        return details;
    }

    /**
     * 查询某隧道最近的车流量相关数据
     *
     * @param tunnelId 隧道id
     * @return
     */
    @Override
    public List<SdTrafficStatistics> selectLatestTrafficFlowList(String tunnelId) {
        return sdTrafficStatisticsMapper.selectLatestTrafficFlowList(tunnelId);
    }
}
