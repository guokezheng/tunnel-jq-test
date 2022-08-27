package com.tunnel.platform.service.informationBoard;

import com.tunnel.platform.domain.informationBoard.SdIotDevice;

import java.util.List;
import java.util.Map;

/**
 * 设备列表Service接口
 *
 * @author yangqichao
 * @date 2020-03-25
 */
public interface ISdIotDeviceService {
    /**
     * 查询设备列表
     *
     * @param deviceId 设备列表ID
     * @return 设备列表
     */
    public SdIotDevice selectIotDeviceById(Long deviceId);

    /**
     * 根据设备ID查询设备接入信息详情
     *
     * @param deviceId 设备列表ID
     * @return 设备列表
     */
    public SdIotDevice selectDeviceAccessById(Long deviceId);


    /**
     * 根据设备分类ID查询设备接入信息详情
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
     * @param iotDevice 设备类型
     * @return 设备列表集合
     */
    public List<SdIotDevice> getDevicePointInfo(SdIotDevice iotDevice);
    /**
     * 查询设备正常离线数量
     */
    public List<Map<String,Object>> getStateStatusCount(SdIotDevice iotDevice);

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
     * 批量删除设备列表
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteIotDeviceByIds(String ids);

    /**
     * 删除设备列表信息
     *
     * @param deviceId 设备列表ID
     * @return 结果
     */
    public int deleteIotDeviceById(Long deviceId);

    /*
     * 查询接入设备
     * */
    public Map selectIotDeviceAccessById(Long deviceId);
    /*
    * 查询违规词汇
    * */
    public List<Map<String, String>> getVerifyWord();

    /*
    * 查询交调设备关联信息
    * */
    public String getInterDevidByDeviceid(Long deviceId);
    /*
    * 查询设备在线离线数量
    * */
    public Map<String,String> getDeviceNumber(SdIotDevice iotDevice);

    public List<SdIotDevice> selectVehicleList();

	public List<SdIotDevice> selectIotDeviceArrayList(SdIotDevice iotDevice);

}
