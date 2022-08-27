package com.tunnel.plc.redisMessage;

import com.ruoyi.common.core.redis.RedisCache;
import com.tunnel.platform.datacenter.util.DateUtils;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.domain.dataInfo.SdSensorMessage;
import com.tunnel.platform.service.dataInfo.ISdDevicesService;
import com.tunnel.platform.service.dataInfo.ISdSensorMessageService;
import com.tunnel.platform.service.dataInfo.ISdTrafficStatisticsService;
import com.tunnel.platform.utils.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisMessage {

	 @Autowired
	 private RedisCache redisCache;
	 @Autowired
	 private ISdDevicesService sdDevicesService;
	 @Autowired
	 private ISdSensorMessageService sdSensorMessageService;
	 @Autowired
	 private ISdTrafficStatisticsService sdTrafficStatisticsService;
	 
	 
	 /**
	  * 传感器信息存储
	  * 
	  * @author yanghousheng
	  * @date 2020-11-18
	  */
	    @Scheduled(fixedRate = 1000*60*5)
	    public void selectRedisCache() {
			redisCache = (RedisCache) SpringContextUtils.getBean(RedisCache.class);
	        List<SdDevices> list = sdDevicesService.selectSensorList();
	        for(SdDevices sdDevice:list){
	        	redisCache.getCacheObject(String.valueOf(sdDevice.getEqId()));
	        	SdSensorMessage sdSensorMessage = new SdSensorMessage();
	        	sdSensorMessage.setEqId(String.valueOf(sdDevice.getEqId()));
	        	sdSensorMessage.setEqType(sdDevice.getEqType()+"");
	        	sdSensorMessage.setSensorValue(redisCache.getCacheObject(String.valueOf(sdDevice.getEqId())));
	        	sdSensorMessage.setEqTunnelId(sdDevice.getEqTunnelId());
	        	sdSensorMessage.setGettime(DateUtils.getNowDate());
	        	sdSensorMessage.setCreateTime(DateUtils.getNowDate());
	        	sdSensorMessage.setEqTunnelId(sdDevice.getEqTunnelId());
	        	sdSensorMessageService.insertSdSensorMessage(sdSensorMessage);
	        }
	    }
	    
	    /**
	     * 存储微波车检（定时每分钟存储一次）//1000*1 代表5秒    一分钟：1000*12
	     */
	    /*@Scheduled(fixedRate = 1000*12)
	    public void weiboStorage() {
	        redisCache = (RedisCache) SpringContextUtils.getBean(RedisCache.class);
	        List<SdDevices> list = sdDevicesService.selectEqListByEqTypes("108");
	        Date date = DateUtils.getNowDate();
	        for(SdDevices sdDevice:list){
	        	List<SdTrafficStatistics> carList = redisCache.getCacheObject(String.valueOf(sdDevice.getEqId()));
	        	for (int i = 0; i < carList.size(); i++) {
	        		SdTrafficStatistics sdTrs = new SdTrafficStatistics();//创建车流量对象
	        		sdTrs.setDeviceId(String.valueOf(sdDevice.getEqId()));//设备id
	        		sdTrs.setByLane(carList.get(i).getByLane());//车道号
	        		sdTrs.setBySpeed(carList.get(i).getBySpeed());//车道过车平均速度
	        		sdTrs.setfSpaceOccupyRation(carList.get(i).getfSpaceOccupyRation());//占有率，百分比计算
	        		sdTrs.setByVehicelNum(carList.get(i).getByVehicelNum());//区域车辆数
	        		sdTrs.setCreateBy("PLC");
	        		sdTrs.setCreateTime(date);
	        		sdTrafficStatisticsService.insertSdTrafficStatistics(sdTrs);
				}
	        }
	    }*/
	    
	    /**
	     * 电力监控数据
	     */
	    @Scheduled(fixedRate = 1000*60)
	    public void powerForwardingTable() {
	    	/*Modbus4jUtils.domain();*/
	    }
}
