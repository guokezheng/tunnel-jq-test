package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdDataTrafficMonth;
import com.tunnel.business.mapper.dataInfo.SdDataTrafficMonthMapper;
import com.tunnel.business.service.dataInfo.ISdDataTrafficMonthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 各路段月车流量统计Service业务层处理
 *
 * @author ruoyi
 * @date 2022-02-18
 */
@Service
public class SdDataTrafficMonthServiceImpl implements ISdDataTrafficMonthService {
    @Autowired
    private SdDataTrafficMonthMapper sdDataTrafficMonthMapper;

    /**
     * 查询各路段月车流量统计
     *
     * @param id 各路段月车流量统计主键
     * @return 各路段月车流量统计
     */
    @Override
    public SdDataTrafficMonth selectSdDataTrafficMonthById(Long id) {
        return sdDataTrafficMonthMapper.selectSdDataTrafficMonthById(id);
    }

    /**
     * 查询各路段月车流量统计列表
     *
     * @param sdDataTrafficMonth 各路段月车流量统计
     * @return 各路段月车流量统计
     */
    @Override
    public List<SdDataTrafficMonth> selectSdDataTrafficMonthList(SdDataTrafficMonth sdDataTrafficMonth) {
        return sdDataTrafficMonthMapper.selectSdDataTrafficMonthList(sdDataTrafficMonth);
    }

    /**
     * 新增各路段月车流量统计
     *
     * @param sdDataTrafficMonth 各路段月车流量统计
     * @return 结果
     */
    @Override
    public int insertSdDataTrafficMonth(SdDataTrafficMonth sdDataTrafficMonth) {
        return sdDataTrafficMonthMapper.insertSdDataTrafficMonth(sdDataTrafficMonth);
    }

    /**
     * 修改各路段月车流量统计
     *
     * @param sdDataTrafficMonth 各路段月车流量统计
     * @return 结果
     */
    @Override
    public int updateSdDataTrafficMonth(SdDataTrafficMonth sdDataTrafficMonth) {
        return sdDataTrafficMonthMapper.updateSdDataTrafficMonth(sdDataTrafficMonth);
    }

    /**
     * 批量删除各路段月车流量统计
     *
     * @param ids 需要删除的各路段月车流量统计主键
     * @return 结果
     */
    @Override
    public int deleteSdDataTrafficMonthByIds(Long[] ids) {
        return sdDataTrafficMonthMapper.deleteSdDataTrafficMonthByIds(ids);
    }

    /**
     * 删除各路段月车流量统计信息
     *
     * @param id 各路段月车流量统计主键
     * @return 结果
     */
    @Override
    public int deleteSdDataTrafficMonthById(Long id) {
        return sdDataTrafficMonthMapper.deleteSdDataTrafficMonthById(id);
    }

    @Override
    public List<Map<String, Object>> getCarNumberByMonth(SdDataTrafficMonth sdDataTrafficMonth) throws ParseException {
        if (null == sdDataTrafficMonth.getTunnelId() || ("").equals(sdDataTrafficMonth.getTunnelId())) {
            if (null == sdDataTrafficMonth.getTunnelName() || ("").equals(sdDataTrafficMonth.getTunnelName())) {
                throw new RuntimeException("查询条件中缺少隧道，请指定隧道后重试！");
            }
        }
        Map<String, Object> params = sdDataTrafficMonth.getParams();
        Long deptId = SecurityUtils.getDeptId();
        params.put("deptId", deptId);
        if (null == params.get("year")) {
            throw new RuntimeException("查询时间条件为空，请选择查询时间！");
        }
        String tunnelId = sdDataTrafficMonth.getTunnelId();
        String tunnelName = sdDataTrafficMonth.getTunnelName();
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String metEnd = params.get("year").toString();
//        String metEndTemp = metEnd.replace("GMT", "").replaceAll("\\(.*\\)", "");
//        SimpleDateFormat format2 = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss", Locale.ENGLISH);
//        metEnd=format.format(format2.parse(metEndTemp));
        String substring = metEnd.substring(0, 4);
        params.put("year", substring);
        List<Map<String, Object>> yearmaps = sdDataTrafficMonthMapper.getCarNumberByMonth(tunnelId, tunnelName, params);
        return yearmaps;
    }

    @Override
    public void summarizeTrafficFlowData(List<Map<String, Object>> sdDataTrafficDays) {
        if (sdDataTrafficDays.size() == 0) {
            System.err.println("按天统计车流量数据表中没有数据！");
            return;
        }
        for (int i = 0; i < sdDataTrafficDays.size(); i++) {
            Map<String, Object> map = sdDataTrafficDays.get(i);
            SdDataTrafficMonth sdDataTrafficMonth = new SdDataTrafficMonth();
            try {
                sdDataTrafficMonth.setStatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map.get("stat_time").toString().replace("T", " ")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            sdDataTrafficMonth.setSegmentNo(map.get("segment_no").toString());
            sdDataTrafficMonth.setYear(Long.parseLong(map.get("year").toString()));
            sdDataTrafficMonth.setMonth(Long.parseLong(map.get("month").toString()));
            sdDataTrafficMonth.setTunnelId(map.get("tunnel_id").toString());
            sdDataTrafficMonth.setDirection(map.get("direction").toString());
            List<SdDataTrafficMonth> sdDataTrafficMonths = sdDataTrafficMonthMapper.selectSdDataTrafficMonthList(sdDataTrafficMonth);
            sdDataTrafficMonth.setTunnelName(map.get("tunnel_name").toString());
            sdDataTrafficMonth.setTotalFlow(Long.parseLong(map.get("total_flow").toString()));
            sdDataTrafficMonth.setFlowSveh(Long.parseLong(map.get("flow_sveh").toString()));
            sdDataTrafficMonth.setFlowMveh(Long.parseLong(map.get("flow_mveh").toString()));
            sdDataTrafficMonth.setFlowBveh(Long.parseLong(map.get("flow_bveh").toString()));
            sdDataTrafficMonth.setAvgSpeed(map.get("avg_speed").toString());
            if (sdDataTrafficMonths.size() > 0) {
                sdDataTrafficMonthMapper.updateSdDataTrafficMonth(sdDataTrafficMonth);
            } else {
                sdDataTrafficMonthMapper.insertSdDataTrafficMonth(sdDataTrafficMonth);
            }
        }
    }
}
