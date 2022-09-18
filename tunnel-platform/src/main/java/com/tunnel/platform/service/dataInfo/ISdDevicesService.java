package com.tunnel.platform.service.dataInfo;

import com.tunnel.platform.domain.dataInfo.SdDevices;

import java.util.List;
import java.util.Map;

/**
 * 设备Service接口
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
public interface ISdDevicesService
{
    /**
     * 查询设备
     *
     * @param eqId 设备ID
     * @return 设备
     */
    public SdDevices selectSdDevicesById(String eqId);

    /**
     * 查询设备详情
     *
     * @param eqId 设备ID
     * @return 设备
     */
    Map<String,String> queryDeviceById(String eqId);

    /**
     * 查询设备列表
     *
     * @param sdDevices 设备
     * @return 设备集合
     */
    public List<SdDevices> selectSdDevicesList(SdDevices sdDevices);

    /**
     * 新增设备
     *
     * @param sdDevices 设备
     * @return 结果
     */
    public int insertSdDevices(SdDevices sdDevices);

    /**
     * 修改设备
     *
     * @param sdDevices 设备
     * @return 结果
     */
    public int updateSdDevices(SdDevices sdDevices);

    /**
     * 批量删除设备
     *
     * @param eqIds 需要删除的设备ID
     * @return 结果
     */
    public int deleteSdDevicesByIds(String[] eqIds);

    /**
     * 删除设备信息
     *
     * @param eqId 设备ID
     * @return 结果
     */
    public int deleteSdDevicesById(String eqId);
    /**
     * 根据设备类型查询设备列表
     * @return
     */
    public List<SdDevices> selectEqListByEqTypes(String[] eqTypes);
    /**
     /**
     * 传感器信息查询
     * @return
     */
    public List<SdDevices> selectSensorList();
    /**
     /**
     * 根据隧道ID查询传感器信息
     * @return
     */

    public List<SdDevices> selectSensorListByTunnelId(String eqTunnelId,String direction);


    public List<SdDevices> selectRippleListByTunnelId(String eqTunnelId,String direction);
    /**
     * 通过plcId查询信息
     * @param eqHostId
     * @return
     */
   // public List<SdDevices> selectSdDevicesEqidList(Long eqHostId);
    /**
     * 通过隧道id查询设备类型
     * @param sdDevices
     * @return
     */
    public List<Map<String, Object>> selectSdDevicesByTunnelId(SdDevices sdDevices);
    /**
     * 检索查询，控制指令出错的数据
     */
    public List<SdDevices>  getChecklist(List<SdDevices> checklist);

    /**
     * 根据模式，ip，机位生成指令码
     * @param devices 设备
     * @param seat 机位
     * @param deviceState 模式状态：DM/CIO
     * @param deviceType 0：查询；1：控制
     * @return
     */
    public StringBuffer getCommandCode(SdDevices devices,String seat,String deviceState,String deviceType);
    /**
     * 数据长度4，不够4位左边补零
     */
    public String getIpleftPad(String ip);
    /**
     * 导入用户数据
     *
     * @param sdDevicesList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importSdDevices(List<SdDevices> sdDevicesList, Boolean isUpdateSupport, String operName);

    /**
     * 根据设备类型增加或者修改，删除设备控制表
     * @param sdDevices
     */
    public void insertOrUpdateOrDeleteSdDeviceCmd(SdDevices sdDevices);

    /**
     * 获取所有压力表信息
     */
    public List<SdDevices> getAllPressureGaugesMsg();

    public List<Map<String, Object>> getDevicesStatus(String tunnelId);

    public List<Map<String, Object>> obtainEquipmentEnergyConsumption(String tunnelId);

    String autoId(String tunnelId,Integer typeId);



    /**
     * 根据隧道id,方向,所属车道筛选出车道指示器
     * @param sdDevices
     * @return
     */
    public List<SdDevices> updateCarFingerById(List<SdDevices> sdDevices);

    public List<SdDevices> selectIsControlSdDevicesList(SdDevices sdDevices);

    public List<Map<String, Object>> getDevicesByTypeAndTunnel(SdDevices sdDevices);

    public int updateSdDevicesByFEqId(SdDevices sdDevices);
}
