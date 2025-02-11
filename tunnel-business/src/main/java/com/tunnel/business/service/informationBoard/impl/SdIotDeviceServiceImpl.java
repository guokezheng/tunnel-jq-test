package com.tunnel.business.service.informationBoard.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysDictDataService;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.informationBoard.SdIotDevice;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.informationBoard.SdIotDeviceMapper;
import com.tunnel.business.service.informationBoard.ISdIotDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private ISysDictDataService sysDictDataService;
    @Autowired
    private SdDevicesMapper sdDevicesMapper;

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
        if (sdIotDevice.getManageAgencyId() == null || sdIotDevice.getManageAgencyId().equals("")) {
            sdIotDevice.setManageAgencyId(SecurityUtils.getDeptId());
        }
        if (sdIotDevice.getEqDirection() != null && !sdIotDevice.getEqDirection().equals("") && sdIotDevice.getEqDirection() == "3") {
            sdIotDevice.setEqDirection(null);
        }
        return sdIotDeviceMapper.selectIotDeviceArrayList(sdIotDevice);
    }

    @Override
    public List<Map<String, Object>> getDevicesSize(SdIotDevice iotDevice) {
        return sdIotDeviceMapper.getDevicesSize(iotDevice.getTunnelId(), iotDevice.getLocalInfo());
    }

    @Override
    public List<Long> selectIotDevicesByTunnelId(String tunnelId) {
        return sdIotDeviceMapper.selectIotDevicesByTunnelId(tunnelId);
    }

    @Override
    public List<Map<String, Object>> getIotBoardList(SdIotDevice iotDevice) {
        List<Map<String, Object>> group = new ArrayList<>();
        List<SysDictData> types = sysDictDataService.getSysDictDataByDictType("iot_devices_type");
        List<Map<String, Object>> allDevicesSize = sdIotDeviceMapper.getDevicesSize(null, null);
        if (iotDevice.getEqDirection() != null && !iotDevice.getEqDirection().equals("")
                && iotDevice.getEqDirection().equals("3")) {
            iotDevice.setEqDirection(null);
        }
        List<SdIotDevice> sdIotDevices = sdIotDeviceMapper.selectIotDeviceArrayList(iotDevice);
        for (int m = 0;m < types.size();m++) {
            SysDictData sysDictData = types.get(m);
            String localInfo = sysDictData.getDictValue();
            Map<String, Object> map = new HashMap<>();
            for (int i = 0;i < allDevicesSize.size();i++) {
                Map<String, Object> objectMap = allDevicesSize.get(i);
                String device_pixel = objectMap.get("device_pixel").toString();
                List<SdIotDevice> list = new ArrayList<>();
                for (int y = 0;y < sdIotDevices.size();y++) {
                    SdIotDevice sdIotDevice = sdIotDevices.get(y);
                    if (localInfo.equals(sdIotDevice.getLocalInfo().toString())
                            && device_pixel.equals(sdIotDevice.getDevicePixel())) {
                        list.add(sdIotDevice);
                    }
                }
                if (!list.isEmpty()) {
                    map.put("label", sysDictData.getDictLabel() + device_pixel);
                    map.put("devicePixel", device_pixel);
                    map.put("list", list);
                    group.add(map);
                }
            }
        }
        return group;
    }
}
