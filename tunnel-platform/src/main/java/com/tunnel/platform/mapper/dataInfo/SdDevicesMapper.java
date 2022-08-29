package com.tunnel.platform.mapper.dataInfo;

import com.tunnel.platform.domain.dataInfo.SdDeviceCmd;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.domain.dataInfo.SdEquipmentType;
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
	public List<SdDevices> selectEqListByEqTypes(String eqTypes);

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
	public int selectTypeNameCount(Long eqType, String eqTunnelId);
	/**
	 * 通过plc主机id查询设备信息
	 * @param eqHostId
	 * @return
	 */
	public List<SdDevices> selectSdDevicesEqidList(Long eqHostId);

	public List<SdDevices> getAllPressureGaugesMsg(@Param("deptId") Long deptId);

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
}
