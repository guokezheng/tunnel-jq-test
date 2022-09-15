package com.tunnel.platform.service.dataInfo.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tunnel.platform.mapper.dataInfo.SdDeviceDataRecordMapper;
import com.tunnel.platform.domain.dataInfo.SdDeviceDataRecord;
import com.tunnel.platform.service.dataInfo.ISdDeviceDataRecordService;

/**
 * 设备数据历史记录Service业务层处理
 *
 * @author ruoyi
 * @date 2022-09-15
 */
@Service
public class SdDeviceDataRecordServiceImpl implements ISdDeviceDataRecordService
{
    @Autowired
    private SdDeviceDataRecordMapper sdDeviceDataRecordMapper;

    /**
     * 查询设备数据历史记录
     *
     * @param id 设备数据历史记录主键
     * @return 设备数据历史记录
     */
    @Override
    public SdDeviceDataRecord selectSdDeviceDataRecordById(Long id)
    {
        return sdDeviceDataRecordMapper.selectSdDeviceDataRecordById(id);
    }

    /**
     * 查询设备数据历史记录列表
     *
     * @param sdDeviceDataRecord 设备数据历史记录
     * @return 设备数据历史记录
     */
    @Override
    public List<SdDeviceDataRecord> selectSdDeviceDataRecordList(SdDeviceDataRecord sdDeviceDataRecord)
    {
        return sdDeviceDataRecordMapper.selectSdDeviceDataRecordList(sdDeviceDataRecord);
    }

    /**
     * 新增设备数据历史记录
     *
     * @param sdDeviceDataRecord 设备数据历史记录
     * @return 结果
     */
    @Override
    public int insertSdDeviceDataRecord(SdDeviceDataRecord sdDeviceDataRecord)
    {
        sdDeviceDataRecord.setCreateTime(DateUtils.getNowDate());
        return sdDeviceDataRecordMapper.insertSdDeviceDataRecord(sdDeviceDataRecord);
    }

    /**
     * 修改设备数据历史记录
     *
     * @param sdDeviceDataRecord 设备数据历史记录
     * @return 结果
     */
    @Override
    public int updateSdDeviceDataRecord(SdDeviceDataRecord sdDeviceDataRecord)
    {
        return sdDeviceDataRecordMapper.updateSdDeviceDataRecord(sdDeviceDataRecord);
    }

    /**
     * 批量删除设备数据历史记录
     *
     * @param ids 需要删除的设备数据历史记录主键
     * @return 结果
     */
    @Override
    public int deleteSdDeviceDataRecordByIds(Long[] ids)
    {
        return sdDeviceDataRecordMapper.deleteSdDeviceDataRecordByIds(ids);
    }

    /**
     * 删除设备数据历史记录信息
     *
     * @param id 设备数据历史记录主键
     * @return 结果
     */
    @Override
    public int deleteSdDeviceDataRecordById(Long id)
    {
        return sdDeviceDataRecordMapper.deleteSdDeviceDataRecordById(id);
    }
}
