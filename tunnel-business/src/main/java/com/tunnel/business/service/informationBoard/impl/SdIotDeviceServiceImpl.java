package com.tunnel.business.service.informationBoard.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.informationBoard.SdIotDevice;
import com.tunnel.business.mapper.informationBoard.SdIotDeviceMapper;
import com.tunnel.business.service.informationBoard.ISdIotDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备列表Service业务层处理
 *
 * @author yangqichao
 * @date 2020-03-25
 */
@Service("sdIotDeviceService")
public class SdIotDeviceServiceImpl implements ISdIotDeviceService {

    @Autowired
    private SdIotDeviceMapper sdIotDeviceMapper;

    /**
     * 查询设备列表
     *
     * @param deviceId 设备列表ID
     * @return 设备列表
     */
    @Override
    public SdIotDevice selectIotDeviceById(Long deviceId) {
        return sdIotDeviceMapper.selectIotDeviceById(deviceId);
    }

    /**
     * 查询设备接入信息详情
     *
     * @param deviceId 设备列表ID
     * @return 设备列表
     */
    @Override
    public SdIotDevice selectDeviceAccessById(Long deviceId) {
        return sdIotDeviceMapper.selectDeviceAccessById(deviceId);
    }

    @Override
    public List<SdIotDevice> selectDeviceAccessByDeviceTypeId(SdIotDevice sdIotDevice) {
        sdIotDevice.setIsMonitor(1);
        return sdIotDeviceMapper.selectDeviceAccessByDeviceTypeId(sdIotDevice);
    }

    /**
     * 查询设备列表列表
     *
     * @param sdIotDevice 设备列表
     * @return 设备列表
     */
    @Override
    public List<SdIotDevice> selectIotDeviceList(SdIotDevice sdIotDevice) {
        sdIotDevice.setIsMonitor(1);
        return sdIotDeviceMapper.selectIotDeviceList(sdIotDevice);
    }

    /**
     * 查询设备列表列表（根据设备类型，使用单位获取设备打点信息）
     *
     * @param sdIotDevice 设备列表
     * @return 设备列表
     */
    @Override
    public List<SdIotDevice> getDevicePointInfo(SdIotDevice sdIotDevice) {
        return sdIotDeviceMapper.selectIotDeviceListbyDeviceTypeId(sdIotDevice);
    }

    /**
     * 查询设备状态
     */
    @Override
    public List<Map<String, Object>> getStateStatusCount(SdIotDevice sdIotDevice) {
        return sdIotDeviceMapper.getStateStatusCount(sdIotDevice);
    }

    /**
     * 新增设备列表
     *
     * @param sdIotDevice 设备列表
     * @return 结果
     */
    @Override
    public int insertIotDevice(SdIotDevice sdIotDevice) {
        sdIotDevice.setCreateTime(DateUtils.getNowDate());
        return sdIotDeviceMapper.insertIotDevice(sdIotDevice);
    }

    /**
     * 修改设备列表
     *
     * @param sdIotDevice 设备列表
     * @return 结果
     */
    @Override
    public int updateIotDevice(SdIotDevice sdIotDevice) {
        sdIotDevice.setUpdateTime(DateUtils.getNowDate());
        return sdIotDeviceMapper.updateIotDevice(sdIotDevice);
    }

    /**
     * 删除设备列表对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteIotDeviceByIds(String ids) {
        return sdIotDeviceMapper.deleteIotDeviceByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除设备列表信息
     *
     * @param deviceId 设备列表ID
     * @return 结果
     */
    @Override
    public int deleteIotDeviceById(Long deviceId) {
        return sdIotDeviceMapper.deleteIotDeviceById(deviceId);
    }

    @Override
    public Map selectIotDeviceAccessById(Long deviceId) {
        return sdIotDeviceMapper.selectIotDeviceAccessById(deviceId);
    }

    /*
     * 获取敏感词
     * */
    @Override
    public List<Map<String, String>> getVerifyWord() {
        return sdIotDeviceMapper.getVerifyWord();
    }

    @Override
    public String getInterDevidByDeviceid(Long deviceId) {
        return sdIotDeviceMapper.getInterDevidByDeviceid(deviceId);
    }

    /*
     * 获取设备在线离线数量
     * */
    @Override
    @DataScope(deptAlias = "d")
    public Map<String, String> getDeviceNumber(SdIotDevice sdIotDevice) {
        Map map = new HashMap<>();
        String deviceNumber = sdIotDeviceMapper.getDeviceNumber(sdIotDevice);//正常
        sdIotDevice.setDeviceStatus("0");
        String normalNumber = sdIotDeviceMapper.getDeviceNumber(sdIotDevice);//正常
        sdIotDevice.setDeviceStatus("1");
        String offNumber = sdIotDeviceMapper.getDeviceNumber(sdIotDevice);//离线
        map.put("deviceNumber", deviceNumber);
        map.put("normalNumber", normalNumber);
        map.put("offNumber", offNumber);
        return map;
    }

    @Override
    public List<SdIotDevice> selectVehicleList() {
        return null;
    }

    @Override
    public List<SdIotDevice> selectIotDeviceArrayList(SdIotDevice sdIotDevice) {
//        sdIotDevice.setIsMonitor(1);
        Long deptId = SecurityUtils.getDeptId();
        sdIotDevice.setManageAgencyId(deptId);
        return sdIotDeviceMapper.selectIotDeviceArrayList(sdIotDevice);
    }

    @Override
    public List<Map<String, Object>> getDevicesSize() {
        return sdIotDeviceMapper.getDevicesSize();
    }
}
