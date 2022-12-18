package com.tunnel.business.service.trafficOperationControl.situationModel.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.trafficOperationControl.situationModel.SdTrafficIncidentMonitor;
import com.tunnel.business.mapper.trafficOperationControl.situationModel.SdTrafficIncidentMonitorMapper;
import com.tunnel.business.service.trafficOperationControl.situationModel.ISdTrafficIncidentMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）Service业务层处理
 *
 * @author ruoyi
 * @date 2022-03-29
 */
@Service
public class SdTrafficIncidentMonitorServiceImpl implements ISdTrafficIncidentMonitorService {
    @Autowired
    private SdTrafficIncidentMonitorMapper sdTrafficIncidentMonitorMapper;

    /**
     * 查询交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     *
     * @param monitorId 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）主键
     * @return 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     */
    @Override
    public SdTrafficIncidentMonitor selectSdTrafficIncidentMonitorByMonitorId(Long monitorId) {
        return sdTrafficIncidentMonitorMapper.selectSdTrafficIncidentMonitorByMonitorId(monitorId);
    }

    /**
     * 查询交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）列表
     *
     * @param sdTrafficIncidentMonitor 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     * @return 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     */
    @Override
    public List<SdTrafficIncidentMonitor> selectSdTrafficIncidentMonitorList(SdTrafficIncidentMonitor sdTrafficIncidentMonitor) {
        return sdTrafficIncidentMonitorMapper.selectSdTrafficIncidentMonitorList(sdTrafficIncidentMonitor);
    }

    /**
     * 新增交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     *
     * @param sdTrafficIncidentMonitor 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     * @return 结果
     */
    @Override
    public int insertSdTrafficIncidentMonitor(SdTrafficIncidentMonitor sdTrafficIncidentMonitor) {
        sdTrafficIncidentMonitor.setCreateTime(DateUtils.getNowDate());
        return sdTrafficIncidentMonitorMapper.insertSdTrafficIncidentMonitor(sdTrafficIncidentMonitor);
    }

    /**
     * 修改交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     *
     * @param sdTrafficIncidentMonitor 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     * @return 结果
     */
    @Override
    public int updateSdTrafficIncidentMonitor(SdTrafficIncidentMonitor sdTrafficIncidentMonitor) {
        sdTrafficIncidentMonitor.setUpdateTime(DateUtils.getNowDate());
        return sdTrafficIncidentMonitorMapper.updateSdTrafficIncidentMonitor(sdTrafficIncidentMonitor);
    }

    /**
     * 批量删除交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     *
     * @param monitorIds 需要删除的交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficIncidentMonitorByMonitorIds(Long[] monitorIds) {
        return sdTrafficIncidentMonitorMapper.deleteSdTrafficIncidentMonitorByMonitorIds(monitorIds);
    }

    /**
     * 删除交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）信息
     *
     * @param monitorId 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficIncidentMonitorByMonitorId(Long monitorId) {
        return sdTrafficIncidentMonitorMapper.deleteSdTrafficIncidentMonitorByMonitorId(monitorId);
    }

    /**
     * 查询获取事件监测信息、以及当前隧道的车流量等信息
     *
     * @param sdTrafficIncidentMonitor 查询条件
     * @return
     */
    @Override
    public List<Map> getList(SdTrafficIncidentMonitor sdTrafficIncidentMonitor) {
        String deptId = SecurityUtils.getDeptId();
        sdTrafficIncidentMonitor.getParams().put("deptId", deptId);
//        查询隧道实时车流量信息【暂时从交通流状态表中获取】 todo
        List<Map> list = sdTrafficIncidentMonitorMapper.getList(sdTrafficIncidentMonitor);

        return list;
    }
}
