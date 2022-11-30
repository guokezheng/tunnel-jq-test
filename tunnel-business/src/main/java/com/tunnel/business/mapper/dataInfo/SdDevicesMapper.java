package com.tunnel.business.mapper.dataInfo;

import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdDevicesBrand;
import com.tunnel.business.domain.dataInfo.SdEquipmentType;
import com.tunnel.business.domain.digitalmodel.SdDeviceDataItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 设备Mapper接口
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
public interface SdDevicesMapper
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

	/*
	 * 查询设备列表
	 */
	public List<SdDevices> selectDropSdDevicesList(SdDevices sdDevices);

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
	 * 删除设备
	 *
	 * @param eqId 设备ID
	 * @return 结果
	 */
	public int deleteSdDevicesById(String eqId);

	/**
	 * 批量删除设备
	 *
	 * @param eqIds 需要删除的数据ID
	 * @return 结果
	 */
	public int deleteSdDevicesByIds(String[] eqIds);
	/**
	 * 查询传感器信息
	 * @return
	 */
	/**
	 * 根据设备类型查询相应设备列表
	 * @param eqTypes
	 * @return
	 */
	public List<SdDevices> selectEqListByEqTypes(String[] eqTypes);

	/**
	 * 查询所有传感器
	 * @return
	 */
	public List<SdDevices> selectSensorList();

	/**
	 * 根据隧道ID查询传感器信息
	 * @param eqTunnelId
	 * @return
	 */
	public List<SdDevices> selectSensorListByTunnelId(@Param("eqTunnelId") String eqTunnelId, @Param("direction") String direction);


	public List<SdDevices> selectRippleListByTunnelId(@Param("eqTunnelId") String eqTunnelId, @Param("direction") String direction);
	/**
	 * 通过隧道id查询设备类型
	 * @param sdDevices
	 * @return
	 */
	public List<SdDevices> selectSdDevicesByTunnelId(SdDevices sdDevices);

	/**
	 * 查询每种设备类型对应的数量
	 */
	public int selectTypeNameCount(@Param("eqType") Long eqType, @Param("eqTunnelId") String eqTunnelId);
	/**
	 * 通过plc主机id查询设备信息
	 * @param eqHostId
	 * @return
	 */
	//public List<SdDevices> selectSdDevicesEqidList(Long eqHostId);

	public List<SdDevices> getAllPressureGaugesMsg(@Param("deptId") String deptId);

	public List<Map<String, Object>> getDevicesStatus(@Param("tunnelId") String tunnelId);

	public List<Map<String, Object>> getDevicesType(@Param("tunnelId") String tunnelId);

	List<String> selectID(@Param("ss") String ss);

	List<SdEquipmentType> selectList();

	public List<SdDevices> selectCarFingerById(SdDevices sdDevices);

	/**
	 * 查询可控设备列表
	 * @param sdDevices
	 * @return
	 */
	public List<SdDevices> selectIsControlSdDevicesList(SdDevices sdDevices);

	List<SdDevices> selectDeviceByTidEqtp(@Param("list") List<SdDevices> list,
	  @Param("tunnelId") String tunnelId,@Param("lidarType") Integer lidarType,@Param("cameraType") Integer cameraType);

//	void updateSdDevicesBatch(List<SdDevices> list);

    String selecTunnelId(@Param("deviceId") String deviceId);

	List<SdDevices> selectDevice(@Param("tunnelId") String tunnelId);

	public String selectDeviceByHost(@Param("host") String host,@Param("loop") String loop,@Param("address") String address);

	public String selectDeviceByHostAndEqId(@Param("host") String host,@Param("eqId") String eqId);

	public List<Map<String, Object>> getDevicesByTypeAndTunnel(SdDevices sdDevices);

    String selectEqStatus(@Param("deviceId") String deviceId);

	SdDeviceDataItem selectDataUnit(@Param("eqId") String eqId);

	void updateSdDevicesBatch(@Param("eqId") String eqId, @Param("eqStatus") String eqStatus);

	public int updateSdDevicesByFEqId(SdDevices sdDevices);

	public List<SdDevices> selectFireComponentsList(SdDevices sdDevices);

	public List<Map<String, Object>> selectDeviceDataAndUnit(@Param("eqId") String eqId);

	public List<Map<String,Object>> selectDeviceDataAndState(@Param("tunnelId") String tunnelId);

	/**
	 * 查询车道数
	 * @return
	 */
	public int selectLaneSize();

	public List<SdDevices> batchControlCarFinger(SdDevices sdDevices);

	/*
	 * 查询设备列表-导出
	 */
	public List<SdDevices> selectSdDevicesList_exp(SdDevices sdDevices);

	public List<Map> getReserveProcessDevices(String[] param);

	public List<SdDevices> getDevicesListByFEqId(String fEqId);

	/**
	 * 故障管理页面--根据设备名称获取设备详情
	 * @param eqId
	 * @return
	 */
    List<SdDevices> getEquipmentInfo(String eqId);


	/**
	 * 查询设备列表--新增巡查点
	 * @param tunnelId
	 * @param deviceType
	 * @return
	 */
	List<SdDevices> getDevicesList(@Param("tunnelId")String tunnelId,@Param("deviceType")String deviceType);

	public List<String> getDevicesFireMarkList(SdDevices sdDevices);

	/**
	 * 批量获取设备信息
	 * @param eqIds
	 * @return
	 */
    List<SdDevices> batchGetDevicesList(String[] eqIds);

	/**
	 * 设备类型
	 * @param tunnelId
	 * @param eqStatus
	 * @param faultStatus
	 * @param falltRemoveStatue
	 * @return
	 */
	List<Map<String, Object>> getEquipmentType(@Param("tunnelId") String tunnelId,
											   @Param("eqStatus") String eqStatus,
											   @Param("faultStatus") String faultStatus,
											   @Param("falltRemoveStatue") String falltRemoveStatue);

    List<SdDevicesBrand> getDevBrandList();

	/**
	 * app端获取设备列表
	 * @param param
	 * @param eqType
	 * @param eqStatus
	 * @return
	 */
    List<SdDevices> getAppDevicesList(@Param("param")String param,@Param("eqType") String eqType,@Param("eqStatus") String eqStatus);

	/**
	 * 查询在线离线设备数量
	 * @param param
	 * @param eqType
	 * @param eqStatus
	 * @return
	 */
	List<SdDevices> getDevicesNum(String param, String eqType, String eqStatus);

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
    List<SdDevices> getAppDevicesStatus(@Param("eqId")String eqId);
}
