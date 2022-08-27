package com.tunnel.platform.service.dataInfo.impl;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.platform.domain.dataInfo.SdLipowerDevice;
import com.tunnel.platform.mapper.dataInfo.SdLipowerDeviceMapper;
import com.tunnel.platform.service.dataInfo.ISdLipowerDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 照明设备信息Service业务层处理
 * 
 * @author wangx
 * @date 2021-03-15
 */
@Service
public class SdLipowerDeviceServiceImpl implements ISdLipowerDeviceService {
	@Autowired
	private SdLipowerDeviceMapper sdLipowerDeviceMapper;

	/**
	 * 查询照明设备信息列表
	 * 
	 * @param sdLipowerDevice
	 *            照明设备信息
	 * @return 照明设备信息
	 */
	@Override
	public List<SdLipowerDevice> selectSdLipowerDeviceList(SdLipowerDevice sdLipowerDevice) {
		return sdLipowerDeviceMapper.selectSdLipowerDeviceList(sdLipowerDevice);
	}

	

	/**
	 * 获取设备信息
	 */
	@Override
	public int getLightingSystemInfo(String groupId,List<SdLipowerDevice> list) {
		try{
			// 查询设备状态：
			String ip = "http://10.7.97.20:8080";
			String address = "/SanjingLight/Status/LightStatus_refresh?group_id=" + groupId;
			String result = "-1";//InterfaceAccessUtil.doGet(ip + address, null);
			if("-1".equals(result)){
				return -2;
			}
			JSONObject lightingJsonObject = JSONObject.parseObject(result);
			// 隧道相关调光/调色温支路列表，单数id为调光，双数id为调色温
			JSONObject lightListJsonObject = lightingJsonObject.getJSONObject("light_list");
			Iterator<String> keys = lightListJsonObject.keySet().iterator();
			while (keys.hasNext()) {
				// 调光ID
				SdLipowerDevice lightObjDevice = new SdLipowerDevice();
				lightObjDevice.setTunnelId(groupId);
				String key = keys.next();
				JSONObject lightObj = lightListJsonObject.getJSONObject(key);
				// 调光/调色温系数
				String value = lightObj.get("value").toString();
				// 是否开启定时调光
				String lightingSetEnable = lightObj.get("lightingSetEnable").toString();
				// 调光/调色温支路名称
				String name = lightObj.get("name").toString();
				// 状态
				String state = lightObj.get("state_list").toString();
				// 定时列表
				// String lightSetList = lightObj.get("light_set_list").toString();
				if(state.contains("正")){
					lightObjDevice.setSwitchStatus("1");
				}else{
					lightObjDevice.setSwitchStatus("2");
				}
				lightObjDevice.setEqId(key);
				lightObjDevice.setLightValue(value);
				lightObjDevice.setLightEnable(lightingSetEnable);
				lightObjDevice.setEqName(name);
				lightObjDevice.setCreateTime(new Date());
				list.add(lightObjDevice);
			}

			// 隧道相关供电设备柜列表
			JSONObject deviceListJsonObject = lightingJsonObject.getJSONObject("device_list");
			keys = deviceListJsonObject.keySet().iterator();
			while (keys.hasNext()) {
				// 供电设备ID
				SdLipowerDevice powerObjDevice = new SdLipowerDevice();
				powerObjDevice.setTunnelId(groupId);
				String key = keys.next();
				JSONObject powerObj = deviceListJsonObject.getJSONObject(key);
				// 供电柜名称
				String name = powerObj.get("name").toString();
				powerObjDevice.setEqId(key);
				// 状态
				String state = powerObj.getJSONObject("state_list").toString();
				if(state.contains("正")){
					powerObjDevice.setSwitchStatus("1");
				}else{
					powerObjDevice.setSwitchStatus("2");
				}
				powerObjDevice.setEqName(name);
				powerObjDevice.setCreateTime(new Date());
				list.add(powerObjDevice);
			}
			// 隧道相关调光分组列表，记录调光分组关系，可忽略
			JSONObject powerListJsonObject = lightingJsonObject.getJSONObject("power_list");
			keys = powerListJsonObject.keySet().iterator();
			while (keys.hasNext()) {
				// 供电支路
				SdLipowerDevice powerObjDevice = new SdLipowerDevice();
				powerObjDevice.setTunnelId(groupId);
				String key = keys.next();
				JSONObject powerObj = powerListJsonObject.getJSONObject(key);
				// 供电支路名称
				String name = powerObj.get("name").toString();
				// 状态
				String state = powerObj.getJSONObject("state_list").toString();
				if(state.contains("正")){
					powerObjDevice.setSwitchStatus("1");
				}else{
					powerObjDevice.setSwitchStatus("2");
				}
				// 供电支路的电流
				String value = powerObj.getJSONObject("text_list").get("支路电流").toString();
				powerObjDevice.setLightValue(value);
				powerObjDevice.setEqId(key);
				powerObjDevice.setEqName(name);
				powerObjDevice.setCreateTime(new Date());
				list.add(powerObjDevice);
			}
		}catch(Exception e){
			return -1;
		}
		return 0;
		//int resultData = sdLipowerDeviceMapper.insertSdLipowerDeviceList(list);
	}
	
}
