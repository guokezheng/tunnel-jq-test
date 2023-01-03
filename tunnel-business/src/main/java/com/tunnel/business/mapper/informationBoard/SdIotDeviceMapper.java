package com.tunnel.business.mapper.informationBoard;


import com.tunnel.business.domain.informationBoard.SdIotDevice;

import java.util.List;
import java.util.Map;

/**
 * 设备列表Mapper接口
 *
 * @author yangqichao
 * @date 2020-03-25
 */
public interface SdIotDeviceMapper {
    /**
     * 查询设备列表
     *
     * @param deviceId 设备列表ID
     * @return 设备列表
     */
    public SdIotDevice selectIotDeviceById(Long deviceId);

    /**
     * 查询设备接入信息详情
     *
     * @param deviceId 设备列表ID
     * @return 设备列表
     */
    public SdIotDevice selectDeviceAccessById(Long deviceId);

    /**
     * 查询设备接入信息详情
     *
     * @param iotDevice 设备类型ID
     * @return 设备列表
     */
    public List<SdIotDevice> selectDeviceAccessByDeviceTypeId(SdIotDevice iotDevice);

    /**
     * 查询设备列表列表
     *
     * @param iotDevice 设备列表
     * @return 设备列表集合
     */
    public List<SdIotDevice> selectIotDeviceList(SdIotDevice iotDevice);

    /**
     * 查询设备列表列表（根据设备类型获取设备打点信息）
     *
     * @param iotDevice 设备列表
     * @return 设备列表集合
     */
    public List<SdIotDevice> selectIotDeviceListbyDeviceTypeId(SdIotDevice iotDevice);

    /**
     * 查询设备状态信息
     */
    public List<Map<String, Object>> getStateStatusCount(SdIotDevice iotDevice);



    /**
     * 新增设备列表
     *
     * @param iotDevice 设备列表
     * @return 结果
     */
    public int insertIotDevice(SdIotDevice iotDevice);

    /**
     * 修改设备列表
     *
     * @param iotDevice 设备列表
     * @return 结果
     */
    public int updateIotDevice(SdIotDevice iotDevice);

    /**
     * 删除设备列表
     *
     * @param deviceId 设备列表ID
     * @return 结果
     */
    public int deleteIotDeviceById(Long deviceId);

    /**
     * 批量删除设备列表
     *
     * @param deviceIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteIotDeviceByIds(String[] deviceIds);

    /**
     * 通过设备id查询信息
     * @param deviceId
     * @return
     */
    Map selectIotDeviceAccessById(Long deviceId);

    /**
     * 获取敏感词
     * @return
     */
    List<Map<String, String>> getVerifyWord();

    /**
     * 查询交调设备关联信息
     * @param deviceId
     * @return
     */
    String getInterDevidByDeviceid(Long deviceId);
    /**
     * 获取设备在线离线数量
     * @param iotDevice
     * @return
     */
    String getDeviceNumber(SdIotDevice iotDevice);

    List<SdIotDevice> selectVehicleList();
    /**
     * 查询设备列表列表
     *
     * @param iotDevice 设备列表
     * @return 设备列表集合
     */
	public List<SdIotDevice> selectIotDeviceArrayList(SdIotDevice iotDevice);

    public List<Map<String, Object>> getDevicesSize();

    public List<Long> selectIotDevicesByTunnelId(String tunnelId);
}
