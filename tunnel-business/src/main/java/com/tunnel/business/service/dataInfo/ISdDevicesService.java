package com.tunnel.business.service.dataInfo;


import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdDevicesBrand;

import java.util.List;
import java.util.Map;

/**
 * 设备Service接口
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
public interface ISdDevicesService {
    /**
     * 查询设备
     *
     * @param eqId 设备ID
     * @return 设备
     */
    SdDevices selectSdDevicesById(String eqId);


    SdDevices selectBoardSdDevicesById(String eqId);

    /**
     * 查询设备详情
     *
     * @param eqId 设备ID
     * @return 设备
     */
    Map<String, String> queryDeviceById(String eqId);

    /**
     * 查询设备列表
     *
     * @param sdDevices 设备
     * @return 设备集合
     */
    List<SdDevices> selectSdDevicesList(SdDevices sdDevices);


    /**
     * 根据隧道ID、设备类型获取设备列表
     * @param tunnelId 隧道ID
     * @param typeList 设备类型列表
     * @return
     */
    List<SdDevices> selectDeviceList(String tunnelId,List<String> typeList);

    /**
     * 新增设备
     *
     * @param sdDevices 设备
     * @return 结果
     */
    int insertSdDevices(SdDevices sdDevices);

    /**
     * 修改设备
     *
     * @param sdDevices 设备
     * @return 结果
     */
    int updateSdDevices(SdDevices sdDevices);

    /**
     * 批量删除设备
     *
     * @param eqIds 需要删除的设备ID
     * @return 结果
     */
    int deleteSdDevicesByIds(String[] eqIds);

    /**
     * 删除设备信息
     *
     * @param eqId 设备ID
     * @return 结果
     */
    int deleteSdDevicesById(String eqId);

    /**
     * 根据设备类型查询设备列表
     *
     * @return
     */
    List<SdDevices> selectEqListByEqTypes(String[] eqTypes);

    /**
     * /**
     * 传感器信息查询
     *
     * @return
     */
    List<SdDevices> selectSensorList();

    /**
     * /**
     * 根据隧道ID查询传感器信息
     *
     * @return
     */

    List<SdDevices> selectSensorListByTunnelId(String eqTunnelId, String direction);


    List<SdDevices> selectRippleListByTunnelId(String eqTunnelId, String direction);
    /**
     * 通过plcId查询信息
     * @param eqHostId
     * @return
     */
    // public List<SdDevices> selectSdDevicesEqidList(Long eqHostId);

    /**
     * 通过隧道id查询设备类型
     *
     * @param sdDevices
     * @return
     */
    List<Map<String, Object>> selectSdDevicesByTunnelId(SdDevices sdDevices);

    /**
     * 检索查询，控制指令出错的数据
     */
    List<SdDevices> getChecklist(List<SdDevices> checklist);

    /**
     * 根据模式，ip，机位生成指令码
     *
     * @param devices     设备
     * @param seat        机位
     * @param deviceState 模式状态：DM/CIO
     * @param deviceType  0：查询；1：控制
     * @return
     */
    StringBuffer getCommandCode(SdDevices devices, String seat, String deviceState, String deviceType);

    /**
     * 数据长度4，不够4位左边补零
     */
    String getIpleftPad(String ip);

    /**
     * 导入用户数据
     *
     * @param sdDevicesList   用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importSdDevices(List<SdDevices> sdDevicesList, Boolean isUpdateSupport, String operName);

    /**
     * 根据设备类型增加或者修改，删除设备控制表
     *
     * @param sdDevices
     */
    void insertOrUpdateOrDeleteSdDeviceCmd(SdDevices sdDevices);

    /**
     * 获取所有压力表信息
     */
    List<SdDevices> getAllPressureGaugesMsg();

    List<Map<String, Object>> getDevicesStatus(String tunnelId);

    List<Map<String, Object>> obtainEquipmentEnergyConsumption(String tunnelId);

    String autoId(String tunnelId, Integer typeId);


    /**
     * 一键车道控制
     * @return
     */
    List<SdDevices> batchControlCarFinger(Map<String,Object> map);

    List<SdDevices> selectIsControlSdDevicesList(SdDevices sdDevices);

    List<Map<String, Object>> getDevicesByTypeAndTunnel(SdDevices sdDevices);

    int updateSdDevicesByFEqId(SdDevices sdDevices);

    /**
     * 查询设备的实时状态
     *
     * @param tunnelId@return
     */
    public List<Map<String,Object>> getDeviceAndState(String tunnelId);

    /**
     * 查询设备管理列表-导出
     * @param sdDevices
     * @return
     */
    List<SdDevices> selectSdDevicesList_exp(SdDevices sdDevices);

    /**
     * 故障管理页面--根据设备id获取设备详情
     * @param eqId
     * @return
     */
    public List<SdDevices> getEquipmentInfo(String eqId);

    /**
     * 查询设备列表--新增巡查点
     * @param tunnelId
     * @param deviceType
     * @return
     */
    List<SdDevices> getDevicesList(String searchValue,String tunnelId,String deviceType,String eqType);

    List<String> fireMarkList(String eqId);

    List<SdDevicesBrand> getDevBrandList();



    /**
     * app端获取设备列表
     * @return
     */
    List<SdDevices> getAppDevicesList(String param, String eqType,String tunnelId,  String eqStatus, Integer pageSize, Integer pageNum);

    /**
     * app端设备信息
     * @param eqId
     * @return
     */
    List<SdDevices> getAppDevicesInfo(String eqId);

    /**
     * app端查询设备状态
     * @param eqId
     * @return
     */
    List<SdDevices> getAppDevicesStatus(String eqId);

    List<SdDevices> selectSdDevicesListByEqTypes(Long guidanceLampTypeId, Long lunKuoBiaoTypeId);

    /**
     * 根据隧道+方向+类型 获取广播设备
     * @param sdDevices
     * @return
     */
    List<SdDevices> getSpkList(SdDevices sdDevices);

    /**
     * 根据隧道+方向+类型+段号(通过external_device_id字段关联) 获取广播设备
     * @param sdDevices
     * @return
     */
    SdDevices getLight(SdDevices sdDevices);

    SdDevices getDeviceByAssociationDeviceId(Long deviceId);

    List<SdDevices> selectDevicesLineList(String deptId,String eqtype);

    /**
     * 查询级联选择设备列表
     * @param sdDevices
     * @return
     */
    AjaxResult getTreeDeviceList(SdDevices sdDevices);

    /**
     *  查询指定设备名称是否重复
     * @param eqName
     * @return
     */
    List<SdDevices> verifyEqNameOnly(String eqId,String eqName);
    /**
     *  查询指定隧道下设备名称是否重复
     * @param eqName
     * @return
     */
    List<SdDevices> tunnelEqNameOnly(String eqTunnelId,String eqName);

    int getAppDevicesCountList(String param, String eqType,String tunnelId, String eqStatus, String deptId);

    List<SdDevices> getDevicesNum(String param, String eqType,String tunnelId, String eqStatus, Integer pageSize, Integer pageNum);

    /**
     * 修改设备以及子设备状态在线
     * @param deviceId 设备ID
     * @param cascade 是否级联修改子设备状态，true为修改
     */
    int updateOnlineStatus(String deviceId,boolean cascade);

    /**
     * 修改设备以及子设备状态离线
     * @param deviceId 设备ID
     * @param cascade 是否级联修改子设备状态，true为修改
     */
     int updateOfflineStatus(String deviceId,boolean cascade);


    /**
     * 修改设备以及子设备状态故障
     * @param deviceId 设备ID
     * @param cascade 是否级联修改子设备状态，true为修改
     */
     int updateFaultStatus(String deviceId,boolean cascade);


    /**
     * 设置设备以及子设备状态
     * @param deviceId 设备ID
     * @param status 设备状态
     * @param cascade 是否级联修改子设备状态，true为修改
     */
     int updateDeviceStatus(String deviceId,String status,boolean cascade);


     /**
     * 批量设置设备状态
     * @param list 设备ID集合
     * @param status 设备状态
     */
    int updateDeviceStatusBatch(List<String> list,String status);



    /**
     * 根据外部设备ID查询设备信息
     * @param externalId 外部设备ID
     * @return
     */
    SdDevices getDevicesListByExternalId(String externalId);


    /**
     * 检查设备信息
     * @param devices
     * @param isUpdateSupport
     * @return
     */
     Map checkDevices(SdDevices devices, Boolean isUpdateSupport);
    /**
     * 根据协议Id查询设备列表
     * @param sdDevices 设备信息
     * @return
     */
    List<SdDevices> selectDevicesByProtocol(SdDevices sdDevices);

    /**
     * 根据协议Id、设备IP等条件查询设备列表
     *
     * @param protocolId 协议ID
     * @param noTypeList 排除的设备类型
     * @return
     */
    List<SdDevices> getDevicesByProtocol(Long protocolId,List<Long> noTypeList);


    /**
     * 按照设备实时数据的时间筛选在线设备
     * @param protocolId 协议ID
     * @param noTypeList 排除的设备类型
     * @param offlineTime 离线时间
     * @return
     */
    List<String> selectOnlineDeviceByUpdateTime(Long protocolId,List<Long> noTypeList,int offlineTime);

    /**
     * 获取全部测控执行器下的设备
     * @return
     */
    List<Map> getMcaDevList();

    /**
     * 获取全部测控执行器
     * @return
     */
    List<Map> getMcaList();

    /**
     * 查询摄像机
     * @param sdDevices
     * @return
     */
    AjaxResult getCamera(SdDevices sdDevices);

    /**
     * 查询同隧道同方向测控执行器
     * @param mac
     * @return
     */
    List<Map> getMoreMcaListByMac(String mac);

    /**
     * 查询同隧道同方向指定类型测控执行器
     * @param mac
     * @param eqType
     * @return
     */
    List<Map> getMoreDevListByMacAndEqType(String mac,String eqType);

    /**
     * 查询mac设备所在洞内的设备
     * @param mac
     * @param eqType
     * @param lane
     * @return
     */
    List<String> getDevicesListByMacAndEqTypeAndLane(String mac, String eqType, String lane);

    /**
     * 更新设备父设备状态
     * @param deviceId
     * @param devicesStatusEnum
     * @return
     */
    int updateFDeviceStatusByEqId(String deviceId, String status);
}
