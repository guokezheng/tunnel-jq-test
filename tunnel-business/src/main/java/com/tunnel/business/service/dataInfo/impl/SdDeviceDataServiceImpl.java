package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备实时数据（存储模拟量）Service业务层处理
 *
 * @author ruoyi
 * @date 2022-09-13
 */
@Service
public class SdDeviceDataServiceImpl implements ISdDeviceDataService {
    @Autowired
    private SdDeviceDataMapper sdDeviceDataMapper;
    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    /**
     * 查询设备实时数据（存储模拟量）
     *
     * @param id 设备实时数据（存储模拟量）主键
     * @return 设备实时数据（存储模拟量）
     */
    @Override
    public SdDeviceData selectSdDeviceDataById(Long id) {
        return sdDeviceDataMapper.selectSdDeviceDataById(id);
    }

    /**
     * 查询设备实时数据（存储模拟量）列表
     *
     * @param sdDeviceData 设备实时数据（存储模拟量）
     * @return 设备实时数据（存储模拟量）
     */
    @Override
    public List<SdDeviceData> selectSdDeviceDataList(SdDeviceData sdDeviceData) {
        return sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
    }

    /**
     * 新增设备实时数据（存储模拟量）
     *
     * @param sdDeviceData 设备实时数据（存储模拟量）
     * @return 结果
     */
    @Override
    public int insertSdDeviceData(SdDeviceData sdDeviceData) {
        sdDeviceData.setCreateTime(DateUtils.getNowDate());
        return sdDeviceDataMapper.insertSdDeviceData(sdDeviceData);
    }

    /**
     * 修改设备实时数据（存储模拟量）
     *
     * @param sdDeviceData 设备实时数据（存储模拟量）
     * @return 结果
     */
    @Override
    public int updateSdDeviceData(SdDeviceData sdDeviceData) {
        sdDeviceData.setUpdateTime(DateUtils.getNowDate());
        return sdDeviceDataMapper.updateSdDeviceData(sdDeviceData);
    }

    /**
     * 批量删除设备实时数据（存储模拟量）
     *
     * @param ids 需要删除的设备实时数据（存储模拟量）主键
     * @return 结果
     */
    @Override
    public int deleteSdDeviceDataByIds(Long[] ids) {
        return sdDeviceDataMapper.deleteSdDeviceDataByIds(ids);
    }

    /**
     * 删除设备实时数据（存储模拟量）信息
     *
     * @param id 设备实时数据（存储模拟量）主键
     * @return 结果
     */
    @Override
    public int deleteSdDeviceDataById(Long id) {
        return sdDeviceDataMapper.deleteSdDeviceDataById(id);
    }

    /**
     * 根据隧道id查询当前设备的监测状态、实时数据或状态
     *
     * @param tunnelId 隧道id
     * @return
     */
    @Override
    public List<Map<String, String>> getDeviceDataByTunnelId(String tunnelId) {
        return sdDeviceDataMapper.getDeviceDataByTunnelId(tunnelId);
    }

    @Override
    public Map<String, Object> getTodayCOVIData(String deviceId) {
        Long itemId = Long.valueOf(DevicesTypeItemEnum.CO.getCode());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(new Date());
        SdDeviceData data = new SdDeviceData();
        data.setDeviceId(deviceId);
        data.setItemId(Long.valueOf(itemId));
        Map<String, Object> map = new HashMap<String, Object>();
        SdDeviceData devData = sdDeviceDataMapper.selectLastRecord(data);
        if (devData.getUpdateTime() != null) {
            today = simpleDateFormat.format(devData.getUpdateTime());
        } else {
            today = simpleDateFormat.format(devData.getCreateTime());
        }
        if (devData != null) {
            map.put("COnowData", devData.getData());
        } else {
            map.put("COnowData", null);
        }
        List<Map<String, Object>> todayCOData = sdDeviceDataMapper.getTodayCOVIData(deviceId, itemId, today);
        itemId = Long.valueOf(DevicesTypeItemEnum.VI.getCode());
        data.setItemId(Long.valueOf(itemId));
        devData = sdDeviceDataMapper.selectLastRecord(data);
        if (devData.getUpdateTime() != null) {
            today = simpleDateFormat.format(devData.getUpdateTime());
        } else {
            today = simpleDateFormat.format(devData.getCreateTime());
        }
        if (devData != null) {
            map.put("VInowData", devData.getData());
        } else {
            map.put("VInowData", null);
        }
        List<Map<String, Object>> todayVIData = sdDeviceDataMapper.getTodayCOVIData(deviceId, itemId, today);
        map.put("todayCOData", todayCOData);
        map.put("todayVIData", todayVIData);
        return map;
    }

    @Override
    public Map<String, Object> getTodayFSFXData(String deviceId) {
        Long itemId = Long.valueOf(DevicesTypeItemEnum.FENG_SU.getCode());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(new Date());
        SdDeviceData data = new SdDeviceData();
        data.setDeviceId(deviceId);
        data.setItemId(Long.valueOf(itemId));
        SdDeviceData devData = sdDeviceDataMapper.selectLastRecord(data);
        if (devData.getUpdateTime() != null) {
            today = simpleDateFormat.format(devData.getUpdateTime());
        } else {
            today = simpleDateFormat.format(devData.getCreateTime());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (devData != null) {
            map.put("nowData", devData.getData());
        } else {
            map.put("nowData", null);
        }
        List<Map<String, Object>> todayFSData = sdDeviceDataMapper.getTodayCOVIData(deviceId, itemId, today);
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(deviceId);
        itemId = Long.valueOf(DevicesTypeItemEnum.FENG_XIANG.getCode());
        sdDeviceData.setItemId(itemId);
        SdDeviceData deviceData = sdDeviceDataMapper.selectLastRecord(sdDeviceData);
        map.put("todayFSData", todayFSData);
        if (deviceData != null) {
            map.put("windDirection", deviceData.getData());
        } else {
            map.put("windDirection", null);
        }
        return map;
    }

    @Override
    public Map<String, Object> getTodayLDData(String deviceId) {
        SdDevices sdDevices = sdDevicesMapper.selectSdDevicesById(deviceId);
        Long ldInsideTypeCode = DevicesTypeEnum.LIANG_DU_JIAN_CE_INSIDE.getCode();
        Long ldOutsideTypeCode = DevicesTypeEnum.LIANG_DU_JIAN_CE.getCode();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, Object>> todayLDData = null;
        Map<String, Object> map = new HashMap<String, Object>();
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(deviceId);
        if (sdDevices != null && sdDevices.getEqType().longValue() == ldInsideTypeCode.longValue()) {
            sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.LIANG_DU_INSIDE.getCode()));
            SdDeviceData deviceData = sdDeviceDataMapper.selectLastRecord(sdDeviceData);
            String today = "";
            if (deviceData.getUpdateTime() != null) {
                today = simpleDateFormat.format(deviceData.getUpdateTime());
            } else {
                today = simpleDateFormat.format(deviceData.getCreateTime());
            }
            todayLDData = sdDeviceDataMapper.getTodayCOVIData(deviceId, Long.valueOf(DevicesTypeItemEnum.LIANG_DU_INSIDE.getCode()), today);
            map.put("todayLDInsideData", todayLDData);
            if (deviceData != null) {
                map.put("nowData", deviceData.getData());
            } else {
                map.put("nowData", null);
            }
        } else if (sdDevices != null && sdDevices.getEqType().longValue() == ldOutsideTypeCode.longValue()) {
            sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.LIANG_DU_OUTSIDE.getCode()));
            SdDeviceData deviceData = sdDeviceDataMapper.selectLastRecord(sdDeviceData);
            String today = "";
            if (deviceData.getUpdateTime() != null) {
                today = simpleDateFormat.format(deviceData.getUpdateTime());
            } else {
                today = simpleDateFormat.format(deviceData.getCreateTime());
            }
            todayLDData = sdDeviceDataMapper.getTodayCOVIData(deviceId, Long.valueOf(DevicesTypeItemEnum.LIANG_DU_OUTSIDE.getCode()), today);
            map.put("todayLDOutsideData", todayLDData);
            if (deviceData != null) {
                map.put("nowData", deviceData.getData());
            } else {
                map.put("nowData", null);
            }
        }
        return map;
    }
}
