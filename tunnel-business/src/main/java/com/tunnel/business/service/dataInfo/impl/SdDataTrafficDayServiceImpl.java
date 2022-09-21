package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdDataTrafficDay;
import com.tunnel.business.mapper.dataInfo.SdDataTrafficDayMapper;
import com.tunnel.business.service.dataInfo.ISdDataTrafficDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 各路段日车流量统计Service业务层处理
 *
 * @author ruoyi
 * @date 2022-02-18
 */
@Service
public class SdDataTrafficDayServiceImpl implements ISdDataTrafficDayService {
    @Autowired
    private SdDataTrafficDayMapper sdDataTrafficDayMapper;

    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }

    /**
     * 查询各路段日车流量统计
     *
     * @param id 各路段日车流量统计主键
     * @return 各路段日车流量统计
     */
    @Override
    public SdDataTrafficDay selectSdDataTrafficDayById(Long id) {
        return sdDataTrafficDayMapper.selectSdDataTrafficDayById(id);
    }

    /**
     * 查询各路段日车流量统计列表
     *
     * @param sdDataTrafficDay 各路段日车流量统计
     * @return 各路段日车流量统计
     */
    @Override
    public List<SdDataTrafficDay> selectSdDataTrafficDayList(SdDataTrafficDay sdDataTrafficDay) {
        return sdDataTrafficDayMapper.selectSdDataTrafficDayList(sdDataTrafficDay);
    }

    /**
     * 新增各路段日车流量统计
     *
     * @param sdDataTrafficDay 各路段日车流量统计
     * @return 结果
     */
    @Override
    public int insertSdDataTrafficDay(SdDataTrafficDay sdDataTrafficDay) {
        return sdDataTrafficDayMapper.insertSdDataTrafficDay(sdDataTrafficDay);
    }

    /**
     * 修改各路段日车流量统计
     *
     * @param sdDataTrafficDay 各路段日车流量统计
     * @return 结果
     */
    @Override
    public int updateSdDataTrafficDay(SdDataTrafficDay sdDataTrafficDay) {
        return sdDataTrafficDayMapper.updateSdDataTrafficDay(sdDataTrafficDay);
    }

    /**
     * 批量删除各路段日车流量统计
     *
     * @param ids 需要删除的各路段日车流量统计主键
     * @return 结果
     */
    @Override
    public int deleteSdDataTrafficDayByIds(Long[] ids) {
        return sdDataTrafficDayMapper.deleteSdDataTrafficDayByIds(ids);
    }

    /**
     * 删除各路段日车流量统计信息
     *
     * @param id 各路段日车流量统计主键
     * @return 结果
     */
    @Override
    public int deleteSdDataTrafficDayById(Long id) {
        return sdDataTrafficDayMapper.deleteSdDataTrafficDayById(id);
    }

    @Override
    public List<Map<String, Object>> getCarNumberByDay(SdDataTrafficDay sdDataTrafficDay) throws ParseException {
        if (null == sdDataTrafficDay.getTunnelId() || ("").equals(sdDataTrafficDay.getTunnelId())) {
            if (null == sdDataTrafficDay.getTunnelName() || ("").equals(sdDataTrafficDay.getTunnelName())) {
                throw new RuntimeException("查询条件中缺少隧道，请指定隧道后重试！");
            }
        }
        Map<String, Object> params = sdDataTrafficDay.getParams();
        if (null == params.get("month")) {
            throw new RuntimeException("查询时间条件为空，请选择查询时间！");
        }
        String tunnelId = sdDataTrafficDay.getTunnelId();
        String tunnelName = sdDataTrafficDay.getTunnelName();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String metEnd = params.get("month").toString();
//        String metEndTemp = metEnd.replace("GMT", "").replaceAll("\\(.*\\)", "");
//        SimpleDateFormat format2 = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
//        metEnd=format.format(format2.parse(metEndTemp));
        String substring = metEnd.substring(0, 7);
        params.put("month", substring);
        String lastDayOfMonth = getLastDayOfMonth(Integer.parseInt(substring.substring(0, 4)), Integer.parseInt(substring.substring(6)));
        List<Map<String, Object>> monthmaps = sdDataTrafficDayMapper.getCarNumberByDay(tunnelId, tunnelName, lastDayOfMonth, params);
        return monthmaps;
    }

    @Override
    public List<Map<String, Object>> getCarFlowNumberOfTodayGroupByTunnel() {
        Long deptId = SecurityUtils.getDeptId();
        List<Map<String, Object>> carFlowNumberOfTodayGroupByTunnel = sdDataTrafficDayMapper.getCarFlowNumberOfTodayGroupByTunnel(deptId);
        return carFlowNumberOfTodayGroupByTunnel;
    }

    @Override
    public List<Map<String, Object>> getCarTypeOfToday(SdDataTrafficDay sdDataTrafficDay) {
        if (null == sdDataTrafficDay.getTunnelId()) {
            throw new RuntimeException("请选择需要查询的隧道");
        }
        String tunnelId = sdDataTrafficDay.getTunnelId();
        List<Map<String, Object>> carTypeOfToday = sdDataTrafficDayMapper.getCarTypeOfToday(tunnelId);
        return carTypeOfToday;
    }

    @Override
    public List<Map<String, Object>> summarizeTrafficFlowData(List<Map<String, Object>> sdDataTrafficHours) {
        long year = 0L;
        long month = 0L;
        for (int i = 0; i < sdDataTrafficHours.size(); i++) {
            Map<String, Object> sdDataTrafficHour = sdDataTrafficHours.get(i);
            SdDataTrafficDay sdDataTrafficDay = new SdDataTrafficDay();
            try {
                sdDataTrafficDay.setStatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdDataTrafficHour.get("stat_time").toString().replace("T", " ")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            sdDataTrafficDay.setSegmentNo(sdDataTrafficHour.get("segment_no").toString());
            year = Long.parseLong(sdDataTrafficHour.get("year").toString());
            sdDataTrafficDay.setYear(year);
            month = Long.parseLong(sdDataTrafficHour.get("month").toString());
            sdDataTrafficDay.setMonth(month);
            sdDataTrafficDay.setDay(Long.parseLong(sdDataTrafficHour.get("day").toString()));
            sdDataTrafficDay.setTotalFlow(Long.parseLong(sdDataTrafficHour.get("total_flow").toString()));
            sdDataTrafficDay.setFlowSveh(Long.parseLong(sdDataTrafficHour.get("flow_sveh").toString()));
            sdDataTrafficDay.setFlowMveh(Long.parseLong(sdDataTrafficHour.get("flow_mveh").toString()));
            sdDataTrafficDay.setFlowBveh(Long.parseLong(sdDataTrafficHour.get("flow_bveh").toString()));
            sdDataTrafficDay.setAvgSpeed(sdDataTrafficHour.get("avg_speed").toString());
            sdDataTrafficDay.setTunnelId(sdDataTrafficHour.get("tunnel_id").toString());
            sdDataTrafficDay.setTunnelName(sdDataTrafficHour.get("tunnel_name").toString());
            sdDataTrafficDay.setDirection(sdDataTrafficHour.get("direction").toString());
            sdDataTrafficDayMapper.insertSdDataTrafficDay(sdDataTrafficDay);
        }
        List<Map<String, Object>> maps = sdDataTrafficDayMapper.selectSdDataTrafficDayCountList(year, month);
        return maps;
    }
}
