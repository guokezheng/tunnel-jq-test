package com.tunnel.deal.fire;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.platform.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.platform.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.domain.dataInfo.SdTunnels;
import com.tunnel.platform.domain.event.SdEvent;
import com.tunnel.platform.domain.event.SdEventType;
import com.tunnel.platform.domain.event.SdWarningInfo;
import com.tunnel.platform.domain.event.SdWarningType;
import com.tunnel.platform.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.platform.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.platform.mapper.event.SdEventMapper;
import com.tunnel.platform.mapper.event.SdEventTypeMapper;
import com.tunnel.platform.mapper.event.SdWarningInfoMapper;
import com.tunnel.platform.mapper.event.SdWarningTypeMapper;
import com.tunnel.platform.service.digitalmodel.RadarEventService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
* Title: FireNettyServerHandler
* Description:  消防数据接收
 */
public class FireNettyServerHandler extends  ChannelInboundHandlerAdapter {

	private EventLoopGroup group;

	private static SdDevicesMapper sdDevicesMapper = SpringUtils.getBean(SdDevicesMapper.class);
	private static RadarEventService radarEventService = SpringUtils.getBean(RadarEventService.class);
	private static SdEventTypeMapper sdEventTypeMapper = SpringUtils.getBean(SdEventTypeMapper.class);
	private static SdEventMapper sdEventMapper = SpringUtils.getBean(SdEventMapper.class);

	public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	private static final Logger log = LoggerFactory.getLogger(FireNettyServerHandler.class);

	/*
     * 收到消息时，返回信息
     */
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
		InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
		String clientIp = ipSocket.getAddress().getHostAddress();
		log.info("客户端ip地址：{}",clientIp);
		String fireAlarmData = (String) msg;
		//先进行判空操作
		if (null == fireAlarmData || fireAlarmData.isEmpty()){
			log.error("收到的消息为空！");
			return;
		}
		log.info("收到火灾报警消息：" + fireAlarmData);
        protocolAnalysis(fireAlarmData, clientIp);
        ctx.flush();
	}

	/**
	 * 消防主机协议解析
	 * @param data
	 */
	private static void protocolAnalysis(String data, String clientIp) {
		// 主机地址
		if("".equals(data) || data == null){
			return;
		}
		//拿到的报文就是纯文字的报文，直接进行解析
		if (data.contains(":")) {
			String alarmType = data.substring(0, data.indexOf(":"));
			System.err.println("alarmType:" + alarmType);
			data = data.substring(data.indexOf(":")+2);
			String host = data.substring(0, data.indexOf("号"));
			SdDevices devices = new SdDevices();
			devices.setIp(clientIp);
			List<SdDevices> devicesList = sdDevicesMapper.selectSdDevicesList(devices);
			SdDevices sdDevices = devicesList.get(0);
			if (data.contains("复位")) {
				log.info("主机{}进行了复位", sdDevices.getEqId());
				//复位清除预警
				SdEvent sdEvent = new SdEvent();
				sdEvent.setEventTypeId(101L);
				List<SdEvent> sdEvents = sdEventMapper.selectSdEventList(sdEvent);
				for (int i = 0;i < sdEvents.size();i++) {
					SdEvent event = sdEvents.get(i);
					event.setEventState("1");
					event.setEndTime(new Date().toString());
					sdEventMapper.updateSdEvent(event);
				}
				sdEvent.setEventTypeId(102L);
				sdEvents = sdEventMapper.selectSdEventList(sdEvent);
				for (int i = 0;i < sdEvents.size();i++) {
					SdEvent event = sdEvents.get(i);
					event.setEventState("1");
					event.setEndTime(new Date().toString());
					sdEventMapper.updateSdEvent(event);
				}
			}
			String fireComponentHosteqId = devicesList.get(0).getEqId();
			data = data.substring(data.indexOf("机")+1);
			String loop = data.substring(0, data.indexOf("回"));
			data = data.substring(data.indexOf("路")+1);
			String address = data.substring(0, data.indexOf("号"));
			data = data.substring(data.indexOf("址")+2);
			String sourceDevice = data.substring(0, data.indexOf(" "));
			data = data.substring(data.indexOf(" ")+1);
			String alarmTime = data.substring(data.indexOf("层")+2, data.length()-4);
			Date now = new Date();
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				now = format.parse(alarmTime);
			} catch (Exception e) {
				log.error("火灾报警日期转换出现异常：" + e.getMessage());
			}
			//根据火灾报警主机ID和回路确定是哪一批设备
			devices.setIp(null);
			devices.setFEqId(fireComponentHosteqId);
			devices.setSecureKey(loop);
			List<SdDevices> devicesLists = sdDevicesMapper.selectSdDevicesList(devices);
			//遍历确定是哪个部件
			String alarmComponentEqId = "";
			for (int i = 0;i < devicesLists.size();i++) {
				SdDevices component = devicesLists.get(i);
				String eqFeedbackAddress1 = component.getEqFeedbackAddress1();
				String[] componentAddress = eqFeedbackAddress1.split(",");
				for (int j = 0;j < componentAddress.length;j++) {
					if (componentAddress[j].equals(address)) {
						log.info("找到部件");
						alarmComponentEqId = component.getEqId();
						sdDevices = component;
						break;
					}
				}
				if (!alarmComponentEqId.equals("")) {
					break;
				}
			}
			//查询当前属于什么事件类型
			SdEventType sdEventType = new SdEventType();
			if (sourceDevice.equals("手报")) {
				sdEventType.setEventType(sourceDevice);
			} else if (sourceDevice.equals("火焰报警器")) {
				sdEventType.setEventType("火灾");
			}
			Long eventTypeId = 0L;
			if (sdEventType.getEventType() != null) {
				List<SdEventType> sdEventTypes = sdEventTypeMapper.selectSdEventTypeList(sdEventType);
				eventTypeId = sdEventTypes.get(0).getId();
			}
			if (eventTypeId == 0) {
				return;
			}
			//存储事件到事件表
			SdEvent sdEvent = new SdEvent();
			sdEvent.setTunnelId(sdDevices.getEqTunnelId());
			sdEvent.setEventTypeId(eventTypeId);
			if (alarmType.contains("故障")) {
				sdEvent.setEventTitle(sourceDevice + "故障事件");
			} else {
				sdEvent.setEventTitle(sourceDevice + "，声光报警器报警事件");
			}
			sdEvent.setEventSource("1");
			sdEvent.setEventState("0");
			sdEvent.setStakeNum(sdDevices.getPile());
			sdEvent.setStartTime(now.toString());
			sdEventMapper.insertSdEvent(sdEvent);
		} else {
			log.error("当前报文格式异常，请检查设备！");
			return;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) {
	 cause.printStackTrace();
	       ctx.close();
	}

    /*
     * 建立连接时，返回消息
     */
   @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
	   System.out.println("address:" + ctx.channel().remoteAddress());
	   String remoteAddress = ctx.channel().remoteAddress().toString();
	   remoteAddress = remoteAddress.substring(1,remoteAddress.indexOf(":"));
	   //获取到客户端IP，根据客户端IP查询需要更改状态的主机设备信息
	   changeFireAlarmHostAndComponentStatus(remoteAddress, DevicesStatusEnum.DEVICE_ON_LINE.getCode());
	   ctx.writeAndFlush("client"+ InetAddress.getLocalHost().getHostName() + "connect！ \n");
	   clients.add(ctx.channel());
	   super.channelActive(ctx);
    }
   /**
    * 通道读取完成
    */
   @Override
   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       super.channelReadComplete(ctx);
	   //断开链接的IP
	   String remoteAddress = ctx.channel().remoteAddress().toString();
	   remoteAddress = remoteAddress.substring(1,remoteAddress.indexOf(":"));
	   changeFireAlarmHostAndComponentStatus(remoteAddress, DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
	   ctx.flush();
   }

   private void changeFireAlarmHostAndComponentStatus(String ip, String status) {
	   SdDevices hostDevice = new SdDevices();
	   SdDevices fireComponentDevice = new SdDevices();
	   hostDevice.setIp(ip);
	   Long fireHostEqTypeId = Long.parseLong(String.valueOf(DevicesTypeEnum.FIRE_ALARM_HOST.getCode()));
	   hostDevice.setEqType(fireHostEqTypeId);
	   List<SdDevices> devicesList = sdDevicesMapper.selectSdDevicesList(hostDevice);
	   for (int i = 0;i < devicesList.size();i++) {
		   SdDevices devices = devicesList.get(i);
		   devices.setEqStatus(status);
		   devices.setEqStatusTime(new Date());
		   sdDevicesMapper.updateSdDevices(devices);
		   String feqId = devices.getEqId();
		   fireComponentDevice.setFEqId(feqId);
		   fireComponentDevice.setEqStatus(status);
		   fireComponentDevice.setEqStatusTime(new Date());
		   sdDevicesMapper.updateSdDevicesByFEqId(fireComponentDevice);
		   List<SdDevices> fireComponentsList = sdDevicesMapper.selectFireComponentsList(fireComponentDevice);
		   for (int j = 0;j < fireComponentsList.size();j++) {
			   //声光报警器数据推送到万基
			   SdDevices fireComponent = fireComponentsList.get(j);
			   Map<String, Object> map = new HashMap<>();
			   map.put("deviceId", fireComponent.getEqId());
			   map.put("deviceType", fireComponent.getEqType());
			   JSONObject jsonObject = new JSONObject();
			   jsonObject.put("runStatus", status);
			   map.put("deviceData", jsonObject);
			   radarEventService.sendBaseDeviceStatus(map);
		   }
	   }
   }

   private byte[] hexString2Bytes(String src) {
		int l = src.length() / 2;
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			ret[i] = (byte) Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
		}
		return ret;
	}

   @Override
   public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
       //设置的超时时间内关闭连接
       if (evt instanceof IdleStateEvent) {
           ctx.close();// 关闭连接
           if (group != null) {
               group.shutdownGracefully();// 释放线程池资源
           }
       } else {
           super.userEventTriggered(ctx, evt);
       }
   }

   public <T> String writeAsString(T t) throws JsonProcessingException {
       ObjectMapper objectMapper = new ObjectMapper();
       return objectMapper.writeValueAsString(t);
   }
}
