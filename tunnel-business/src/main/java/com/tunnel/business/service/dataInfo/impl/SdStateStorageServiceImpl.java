package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.SdStateStorage;
import com.tunnel.business.mapper.dataInfo.SdStateStorageMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdStateStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 隧道数据存储表Service业务层处理
 *
 * @author 刘方堃
 * @date 2022-01-07
 */
@Service
public class SdStateStorageServiceImpl implements ISdStateStorageService {
    @Autowired
    private SdStateStorageMapper sdStateStorageMapper;
    @Autowired
    private ISdDevicesService iSdDevicesService;

    /**
     * 查询隧道数据存储表
     *
     * @param id 隧道数据存储表主键
     * @return 隧道数据存储表
     */
    @Override
    public SdStateStorage selectSdStateStorageById(Long id) {
        return sdStateStorageMapper.selectSdStateStorageById(id);
    }

    /**
     * 查询隧道数据存储表列表
     *
     * @param sdStateStorage 隧道数据存储表
     * @return 隧道数据存储表
     */
    @Override
    public List<SdStateStorage> selectSdStateStorageList(SdStateStorage sdStateStorage) {
        return sdStateStorageMapper.selectSdStateStorageList(sdStateStorage);
    }

    /**
     * 新增隧道数据存储表
     *
     * @param sdStateStorage 隧道数据存储表
     * @return 结果
     */
    @Override
    public int insertSdStateStorage(SdStateStorage sdStateStorage) {
        sdStateStorage.setCreateTime(DateUtils.getNowDate());
        return sdStateStorageMapper.insertSdStateStorage(sdStateStorage);
    }

    /**
     * 修改隧道数据存储表
     *
     * @param sdStateStorage 隧道数据存储表
     * @return 结果
     */
    @Override
    public int updateSdStateStorage(SdStateStorage sdStateStorage) {
        String deviceId = sdStateStorage.getDeviceId();
        SdStateStorage deviceState = sdStateStorageMapper.selectSdStateStorage(deviceId);
        if (null == deviceState) {
            SdStateStorage storage = new SdStateStorage();
            storage.setDeviceId(deviceId);
            storage.setState(sdStateStorage.getState());
            storage.setCreateTime(new Date());
            return sdStateStorageMapper.insertSdStateStorage(storage);
        }
        deviceState.setState(sdStateStorage.getState());
        deviceState.setCreateTime(new Date());
        return sdStateStorageMapper.updateSdStateStorage(deviceState);
    }

    /**
     * 批量删除隧道数据存储表
     *
     * @param ids 需要删除的隧道数据存储表主键
     * @return 结果
     */
    @Override
    public int deleteSdStateStorageByIds(Long[] ids) {
        return sdStateStorageMapper.deleteSdStateStorageByIds(ids);
    }

    /**
     * 删除隧道数据存储表信息
     *
     * @param id 隧道数据存储表主键
     * @return 结果
     */
    @Override
    public int deleteSdStateStorageById(Long id) {
        return sdStateStorageMapper.deleteSdStateStorageById(id);
    }

    @Override
    public SdStateStorage selectSdStateStorage(String deviceId) {
        // TODO Auto-generated method stub
        return sdStateStorageMapper.selectSdStateStorage(deviceId);
    }
}
