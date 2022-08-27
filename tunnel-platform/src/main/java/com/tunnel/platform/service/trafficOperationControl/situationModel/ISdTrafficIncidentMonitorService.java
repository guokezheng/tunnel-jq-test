package com.tunnel.platform.service.trafficOperationControl.situationModel;

import com.tunnel.platform.domain.trafficOperationControl.situationModel.SdTrafficIncidentMonitor;

import java.util.List;
import java.util.Map;

/**
 * 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）Service接口
 *
 * @author ruoyi
 * @date 2022-03-29
 */
public interface ISdTrafficIncidentMonitorService
{
    /**
     * 查询交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     *
     * @param monitorId 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）主键
     * @return 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     */
    public SdTrafficIncidentMonitor selectSdTrafficIncidentMonitorByMonitorId(Long monitorId);

    /**
     * 查询交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）列表
     *
     * @param sdTrafficIncidentMonitor 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     * @return 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）集合
     */
    public List<SdTrafficIncidentMonitor> selectSdTrafficIncidentMonitorList(SdTrafficIncidentMonitor sdTrafficIncidentMonitor);

    /**
     * 新增交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     *
     * @param sdTrafficIncidentMonitor 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     * @return 结果
     */
    public int insertSdTrafficIncidentMonitor(SdTrafficIncidentMonitor sdTrafficIncidentMonitor);

    /**
     * 修改交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     *
     * @param sdTrafficIncidentMonitor 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     * @return 结果
     */
    public int updateSdTrafficIncidentMonitor(SdTrafficIncidentMonitor sdTrafficIncidentMonitor);

    /**
     * 批量删除交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     *
     * @param monitorIds 需要删除的交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）主键集合
     * @return 结果
     */
    public int deleteSdTrafficIncidentMonitorByMonitorIds(Long[] monitorIds);

    /**
     * 删除交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）信息
     *
     * @param monitorId 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）主键
     * @return 结果
     */
    public int deleteSdTrafficIncidentMonitorByMonitorId(Long monitorId);

    /**
     * 查询获取事件监测信息、以及当前隧道的车流量等信息
     * @param sdTrafficIncidentMonitor 查询条件
     * @return
     */
    List<Map> getList(SdTrafficIncidentMonitor sdTrafficIncidentMonitor);
}
