package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdSensorMessage;
import com.tunnel.business.domain.event.SdWarningInfo;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.dataInfo.SdSensorMessageMapper;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.mapper.event.SdWarningInfoMapper;
import com.tunnel.business.service.dataInfo.ISdSensorMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 传感器采集数据信息Service业务层处理
 *
 * @author yanghousheng
 * @date 2020-12-25
 */
@Service
public class SdSensorMessageServiceImpl implements ISdSensorMessageService {
    @Autowired
    private SdSensorMessageMapper sdSensorMessageMapper;

    @Autowired
    private SdWarningInfoMapper sdWarningInfoMapper;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Autowired
    private SdTunnelsMapper sdTunnelsMapper;

    /**
     * 查询传感器采集数据信息
     *
     * @param id 传感器采集数据信息ID
     * @return 传感器采集数据信息
     */
    @Override
    public SdSensorMessage selectSdSensorMessageById(Long id) {
        return sdSensorMessageMapper.selectSdSensorMessageById(id);
    }

    /**
     * 查询传感器采集数据信息列表
     *
     * @param sdSensorMessage 传感器采集数据信息
     * @return 传感器采集数据信息
     */
    @Override
    public List<SdSensorMessage> selectSdSensorMessageList(SdSensorMessage sdSensorMessage) {
        return sdSensorMessageMapper.selectSdSensorMessageList(sdSensorMessage);
    }

    /**
     * 新增传感器采集数据信息
     *
     * @param sdSensorMessage 传感器采集数据信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSdSensorMessage(SdSensorMessage sdSensorMessage) {
        String eqType = sdSensorMessage.getEqType();
        int sensorValue = sdSensorMessage.getSensorValue()
                != null ? Integer.valueOf(sdSensorMessage.getSensorValue()) : 0;
        String eqId = sdSensorMessage.getEqId();
        String tunnelId = sdSensorMessage.getEqTunnelId();
        String tunnelName = sdTunnelsMapper.selectSdTunnelsById(tunnelId)
                == null ? "" : sdTunnelsMapper.selectSdTunnelsById(tunnelId).getTunnelName();
        // 如果是Co检测并且值超过400或是VI检测器值超过30或路面检测器值超过45,报警
        boolean flag = ("14".equals(eqType) && sensorValue > 400) || ("15".equals(eqType) && sensorValue > 30)
                || ("120".equals(eqType) && sensorValue > 45);
        if (flag) {
            SdDevices sdDevices = sdDevicesMapper.selectSdDevicesById(eqId);
            SdWarningInfo sdWarningInfo = new SdWarningInfo();
            sdWarningInfo.setTunnelId(tunnelId);
            if ("14".equals(eqType)) {
                sdWarningInfo.setWarningTypeId(1L);
                sdWarningInfo.setWarningName("Co预警");
                sdWarningInfo.setInforSources(tunnelName + sdDevices.getPile() + "发生Co预警事件信息");
            } else if ("15".equals(eqType)) {
                sdWarningInfo.setWarningTypeId(2L);
                sdWarningInfo.setWarningName("VI预警");
                sdWarningInfo.setInforSources(tunnelName + sdDevices.getPile() + "发生能见度预警事件信息");
            } else {
                sdWarningInfo.setWarningTypeId(55L);
                sdWarningInfo.setWarningName("路面温度预警");
                sdWarningInfo.setInforSources(tunnelName + sdDevices.getPile() + "发生路面温度预警事件信息");
            }
            sdWarningInfo.setEqId(eqId);
            sdWarningInfo.setProcessState("0");
            sdWarningInfo.setWarningTime(new Date());
            sdWarningInfo.setPosition(sdDevices.getPile());
            sdWarningInfo.setCreateTime(new Date());
            sdWarningInfoMapper.insertSdWarningInfo(sdWarningInfo);
        }

        sdSensorMessage.setCreateTime(DateUtils.getNowDate());
        return sdSensorMessageMapper.insertSdSensorMessage(sdSensorMessage);
    }

    /**
     * 修改传感器采集数据信息
     *
     * @param sdSensorMessage 传感器采集数据信息
     * @return 结果
     */
    @Override
    public int updateSdSensorMessage(SdSensorMessage sdSensorMessage) {
        sdSensorMessage.setUpdateTime(DateUtils.getNowDate());
        return sdSensorMessageMapper.updateSdSensorMessage(sdSensorMessage);
    }

    /**
     * 批量删除传感器采集数据信息
     *
     * @param ids 需要删除的传感器采集数据信息ID
     * @return 结果
     */
    @Override
    public int deleteSdSensorMessageByIds(Long[] ids) {
        return sdSensorMessageMapper.deleteSdSensorMessageByIds(ids);
    }

    /**
     * 删除传感器采集数据信息信息
     *
     * @param id 传感器采集数据信息ID
     * @return 结果
     */
    @Override
    public int deleteSdSensorMessageById(Long id) {
        return sdSensorMessageMapper.deleteSdSensorMessageById(id);
    }

    @Override
    public List<SdSensorMessage> selectSdSensorMessageNow(SdSensorMessage sdSensorMessage) {
        return sdSensorMessageMapper.selectSdSensorMessageNow(sdSensorMessage);
    }

    @Override
    public List<SdSensorMessage> seleteSdSensorMessageByTime(SdSensorMessage sdSensorMessage) {
        return sdSensorMessageMapper.selectSdSensorMessageByTime(sdSensorMessage);
    }
}
