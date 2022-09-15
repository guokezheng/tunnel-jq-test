package com.tunnel.platform.service.dataInfo.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.platform.datacenter.domain.enumeration.DevicesTypeItemEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tunnel.platform.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.platform.domain.dataInfo.SdDeviceData;
import com.tunnel.platform.service.dataInfo.ISdDeviceDataService;

/**
 * 设备实时数据（存储模拟量）Service业务层处理
 *
 * @author ruoyi
 * @date 2022-09-13
 */
@Service
public class SdDeviceDataServiceImpl implements ISdDeviceDataService
{
    @Autowired
    private SdDeviceDataMapper sdDeviceDataMapper;

    /**
     * 查询设备实时数据（存储模拟量）
     *
     * @param id 设备实时数据（存储模拟量）主键
     * @return 设备实时数据（存储模拟量）
     */
    @Override
    public SdDeviceData selectSdDeviceDataById(Long id)
    {
        return sdDeviceDataMapper.selectSdDeviceDataById(id);
    }

    /**
     * 查询设备实时数据（存储模拟量）列表
     *
     * @param sdDeviceData 设备实时数据（存储模拟量）
     * @return 设备实时数据（存储模拟量）
     */
    @Override
    public List<SdDeviceData> selectSdDeviceDataList(SdDeviceData sdDeviceData)
    {
        return sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
    }

    /**
     * 新增设备实时数据（存储模拟量）
     *
     * @param sdDeviceData 设备实时数据（存储模拟量）
     * @return 结果
     */
    @Override
    public int insertSdDeviceData(SdDeviceData sdDeviceData)
    {
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
    public int updateSdDeviceData(SdDeviceData sdDeviceData)
    {
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
    public int deleteSdDeviceDataByIds(Long[] ids)
    {
        return sdDeviceDataMapper.deleteSdDeviceDataByIds(ids);
    }

    /**
     * 删除设备实时数据（存储模拟量）信息
     *
     * @param id 设备实时数据（存储模拟量）主键
     * @return 结果
     */
    @Override
    public int deleteSdDeviceDataById(Long id)
    {
        return sdDeviceDataMapper.deleteSdDeviceDataById(id);
    }

    @Override
    public Map<String, Object> getTodayCOVIData(String deviceId) {
        Long itemId = Long.valueOf(DevicesTypeItemEnum.CO.getCode());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(new Date());
        List<Map<String, Object>> todayCOData = sdDeviceDataMapper.getTodayCOVIData(deviceId, itemId, today);
        itemId = Long.valueOf(DevicesTypeItemEnum.VI.getCode());
        List<Map<String, Object>> todayVIData = sdDeviceDataMapper.getTodayCOVIData(deviceId, itemId, today);
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("todayCOData", todayCOData);
        map.put("todayVIData", todayVIData);
        return map;
    }
}
