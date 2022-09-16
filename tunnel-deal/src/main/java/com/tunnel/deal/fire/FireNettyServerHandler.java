package com.tunnel.deal.fire;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.platform.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.platform.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.domain.dataInfo.SdTunnels;
import com.tunnel.platform.domain.event.SdWarningInfo;
import com.tunnel.platform.domain.event.SdWarningType;
import com.tunnel.platform.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.platform.mapper.dataInfo.SdTunnelsMapper;
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
			if (data.contains("复位")) {
				log.info("主机{}进行了复位", host);
				//复位清除预警
				SdWarningInfo info = new SdWarningInfo();
				info.setWarningTypeId(20L);
				info.setProcessState("0");
				List<SdWarningInfo> list = SpringUtils.getBean(SdWarningInfoMapper.class).selectSdWarningInfoList(info);
				for (int i = 0;i < list.size();i++) {
					SdWarningInfo info1 = list.get(i);
					String eqId = info1.getEqId();
					String result = sdDevicesMapper.selectDeviceByHostAndEqId(host, eqId);
					if (!result.equals("") || result != null) {
						info1.setProcessState("2");
						SpringUtils.getBean(SdWarningInfoMapper.class).updateSdWarningInfo(info1);
					}
				}
			}
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
			String eqId = sdDevicesMapper.selectDeviceByHost(host, loop, address);
			SdDevices sdDevices = sdDevicesMapper.selectSdDevicesById(eqId);
			SdWarningInfo warningInfo = new SdWarningInfo();
			warningInfo.setTunnelId(sdDevices.getEqTunnelId());
			warningInfo.setWarningTypeId(20L);
			SdWarningType sdWarningType = SpringUtils.getBean(SdWarningTypeMapper.class).selectSdWarningTypeById(20L);
			warningInfo.setWarningName(sdWarningType.getTypeName());
			warningInfo.setWarningTime(now);
			SdTunnels sdTunnels = SpringUtils.getBean(SdTunnelsMapper.class).selectSdTunnelsById(sdDevices.getEqTunnelId());
			if (alarmType.contains("故障")) {
				warningInfo.setInforSources(sdTunnels.getTunnelName() + sdDevices.getPile() + sdDevices.getEqName() + "出现" + alarmType);
			} else {
				warningInfo.setInforSources(sdTunnels.getTunnelName() + sdDevices.getPile() + sdDevices.getEqName() + "发出" + sdWarningType.getTypeName());
			}
			warningInfo.setPosition(sdDevices.getPile());
			warningInfo.setProcessState("0");
			warningInfo.setCreateTime(new Date());
			warningInfo.setEqId(sdDevices.getEqId());
			SpringUtils.getBean(SdWarningInfoMapper.class).insertSdWarningInfo(warningInfo);
		} else if (data.equals("活")) {
			log.info("当前设备运行正常");
			SdDevices sdDevices = new SdDevices();
			sdDevices.setIp(clientIp);
			List<SdDevices> devicesList = sdDevicesMapper.selectSdDevicesList(sdDevices);
			for (int i = 0;i < devicesList.size();i++) {
				SdDevices devices = devicesList.get(i);
				String eqStatus = devices.getEqStatus();
				if (!eqStatus.equals("1")) {
					devices.setEqStatus("1");
					devices.setEqStatusTime(new Date());
					sdDevicesMapper.updateSdDevices(devices);
				}
			}
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
