package com.tunnel.business.mapper.dataInfo;

import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdDevicesBrand;
import com.tunnel.business.domain.dataInfo.SdEquipmentStateIconFile;
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

	SdDevices getDevicesListByExternalId(String externalId);
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

	public int updateSdDevicesByExternalSystemId(SdDevices sdDevices);

	public List<SdDevices> selectFireComponentsList(SdDevices sdDevices);

	public List<SdDevices> selectDevicesListByExternalSystemId(SdDevices sdDevices);

	public List<Map<String, Object>> selectDeviceDataAndUnit(@Param("eqId") String eqId);

	public List<Map<String,Object>> selectDeviceDataAndState(@Param("tunnelId") String tunnelId);

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
	List<SdDevices> getDevicesNum(@Param("param")String param,@Param("eqType") String eqType,@Param("eqStatus") String eqStatus);

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

	public List<SdDevices> selectSdDevicesListByEqTypes(@Param("guidanceLampTypeId") Long guidanceLampTypeId, @Param("lunKuoBiaoTypeId") Long lunKuoBiaoTypeId);

	/**
	 * 根据类型和外部设备ID查询紧急电话和广播
	 * @param devices
	 * @return
	 */
	SdDevices selectPhoneSpk(SdDevices devices);

	/**
	 * 查询左洞或者右洞的设备
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

	/**
	 * 查询风机对应的振动仪检测器
	 * @param deviceId
	 * @return
	 */
	SdDevices selectZdyDevice(@Param("deviceId") String deviceId,
							  @Param("eqType") Long eqType);

	SdDevices getDeviceByAssociationDeviceId(@Param("deviceId") Long deviceId);


    List<SdDevices> selectDevicesLineList(@Param("deptId")String deptId,@Param("eqtype")String eqtype);

	/**
	 * 查询设备方向
	 * @param sdDevices
	 * @return
	 */
	List<String> getTunnelDirection(SdDevices sdDevices);

	/**
	 * 查询级联选择双向设备
	 * @param sdDevices
	 * @return
	 */
	List<Map<String, Object>> getTreeDevicesData(SdDevices sdDevices);

	/**
	 * 查询最近3km设备
	 * @param eqType
	 * @param direction
	 * @param frontStakeNum
	 * @param afterStakeNum
	 * @return
	 */
	List<String> getRlDevice(@Param("eqType") int eqType,
							 @Param("direction") String direction,
							 @Param("frontStakeNum") int frontStakeNum,
							 @Param("afterStakeNum") int afterStakeNum,
							 @Param("tunnelId") String tunnelId);

	//前5个
	List<String> getFrontLatelyFive(@Param("eqType") int eqType,
									@Param("direction") String direction,
									@Param("stakeNum") int stakeNum,
									@Param("tunnelId") String tunnelId);

	//后5个
	List<String> getAfterLatelyFive(@Param("eqType") int eqType,
									@Param("direction") String direction,
									@Param("stakeNum") int stakeNum,
									@Param("tunnelId") String tunnelId);

	/**
	 * 查询出入口视频
	 * @param sdDevices
	 * @return
	 */
	List<SdDevices> getEntranceExitVideo(SdDevices sdDevices);

	/**
	 * 批量查询设备信息
	 * @param ids
	 * @return
	 */
	List<Map<String, Object>> selectDevices(@Param("ids") String ids,
											@Param("state") String state);

	/**
	 * 批量查询情报板设备信息
	 * @param ids
	 * @return
	 */
	List<Map<String, Object>> selectVmsDevices(@Param("ids") String ids,
											@Param("state") String state);

	/**
	 * 批量查询扬声器设备信息
	 * @param ids
	 * @return
	 */
	List<Map<String, Object>> selectLsDevices(@Param("ids") String ids);
}
