package com.tunnel.platform.business.othersystem.netty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tunnel.platform.domain.event.SdWarningInfo;
import com.tunnel.platform.domain.xiaoFang.XfPointInfo;
import com.tunnel.platform.service.event.ISdWarningInfoService;
import com.tunnel.platform.service.xiaoFang.IXfPointInfoService;
import com.ruoyi.common.utils.spring.SpringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
* Title: FireNettyServerHandler
* Description:  消防数据接收
* @author gongfanfei
* @date 2021-3-3
 */
public class FireNettyServerHandler extends  ChannelInboundHandlerAdapter {
    
	private EventLoopGroup group;
	/*
     * 收到消息时，返回信息
     */
	@Override
	public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
		ByteBuf result = (ByteBuf) msg;
        byte[] result1 = new byte[result.readableBytes()];

        // msg中存储的是ByteBuf类型的数据，把数据读取到byte[]中
        result.readBytes(result1);
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String strIP = insocket.getAddress().getHostAddress();//客户端ip

        String str = new String(result1,"gb2312");
        System.out.println("原始值===="+str);
        if(str.contains("火警")||str.contains("启动")){
            dataAnalysis(str);
        }
        System.out.println(str);
        // 释放资源，这行很关键
        result.release();
        ctx.flush();
	}
	
	public static void main(String[] args) throws JsonProcessingException {
		dataAnalysis("模拟火警: 3号机1回路1号地址 火焰探测器 火警 层 2021/3/5 14:26:14");
	}
	private static void dataAnalysis(String alarmInfo) throws JsonProcessingException {
		Date dateTime = new Date();
		String[] alarm = {};
        alarm = alarmInfo.split(" ");
      //模拟火警: 3号机1回路1号地址 火焰探测器 火警 层 2021/3/5 14:26:14
        SdWarningInfo warn = new SdWarningInfo();
        warn.setWarningName("消防主机预警-" + alarm[0].replace(":",""));//预警名称
//        String alarmModel = alarm[2];//智能手报/感温
//        String alarmType = alarm[3];//火警/故障
        String tunnelId = "";//隧道Id
        warn.setWarningTime(dateTime);//预警时间
        warn.setWarningTypeId(20L);//2--消防事件
        
        //回路
        String loop = alarm[1].substring(alarm[1].indexOf("机")+1, alarm[1].indexOf("回"));
        //地址
        String address = alarm[1].substring(alarm[1].indexOf("路")+1, alarm[1].indexOf("地")-1);
        
        String[] sd = alarm[1].split("号机");
        switch (sd[0]){
           /* case "1":
                tunnelId = "S29-ZaoZhuangCompany-ShanTingStation-001";
                break;
            case "2":
                tunnelId = "S29-ZaoZhuangCompany-ShanTingStation-002";
                break;
            case "3":
                tunnelId = "S29-ZaoZhuangCompany-ShanTingStation-003";
                break;*/
            case "1":
                tunnelId = "S29-LinYiCompany-BaiYanStation-003";
                break;
            case "2":
                tunnelId = "S29-LinYiCompany-BaiYanStation-002";
                break;
            case "3":
                tunnelId = "S29-LinYiCompany-BaiYanStation-001";
                break;
	      case "4":
	    	  tunnelId = "S29-ZaoZhuangCompany-ShanTingStation-004";
	    	  break;
        }
        warn.setTunnelId(tunnelId);
        
        IXfPointInfoService pointinfoservice = SpringUtils.getBean(IXfPointInfoService.class);
        XfPointInfo xfPointInfo = new XfPointInfo();
        xfPointInfo.setTunnelId(tunnelId);
        
        String content = "";//方向
        //1回路是左洞，2回路是右洞
        if("1".equals(loop)){
        	//左洞
        	xfPointInfo.setDirection("Z");
        	warn.setHoleDirection("Z");
        	content = "左洞";
        }else if("2".equals(loop)){
        	//右洞
        	xfPointInfo.setDirection("Y");
        	warn.setHoleDirection("Y");
        	content = "右洞";
        }
        String eqPointName = "";
        //判断，确定点位
        if(alarmInfo.contains("火焰")){//火焰探测器
        	xfPointInfo.setFirePoint(address);
        	eqPointName = "火焰探测器预警";
        }else if(alarmInfo.contains("声光")){//声光报警器
        	xfPointInfo.setVoicePoint(address);
        	eqPointName = "声光报警器预警";
        }else if(alarmInfo.contains("手动")){//手动报警按钮
        	xfPointInfo.setHandPoint(address);
        	eqPointName = "手动报警按钮预警";
        }else if(alarmInfo.contains("烟感")){//烟感
        	xfPointInfo.setSmokePoint(address);
        	eqPointName = "烟感预警";
        }else if(alarmInfo.contains("温感")){//温感
        	xfPointInfo.setWarmPoint(address);
        	eqPointName = "温感预警";
        }else if(alarmInfo.contains("消火栓")){//消火栓按钮
        	xfPointInfo.setHydrantPoint(address);
        	eqPointName = "消火栓按钮预警";
        }
        List<XfPointInfo> info =  pointinfoservice.selectXfPointInfoList(xfPointInfo);
        if(info != null && info.size()>0 ){
        	
        	XfPointInfo pointInfo = info.get(0);
        	//获取关联摄像机
        	warn.setLinkedCamera(pointInfo.getLinkedCamera());//关联摄像机
        	warn.setPosition(pointInfo.getStakeMark());//桩号
        	content = content + pointInfo.getStakeMark()+eqPointName;
        }
        content = content + "："+alarm[0].replace(":","");
        
        /*String[] fx = sd[1].split("回路");
        if ("1".equals(fx[0])){
        	content = "台儿庄";
        }else{
        	content = "新泰";
        }*/
      //预警内容-姚家峪隧道--姚家方向--1号机1回路1号地址 //感温自动上报--发生故障 智能手报/感温   alarm[3]火警/
        //右洞 ZK90+102
        warn.setInforSources(content);
        warn.setTunnelId(tunnelId);
//        warn.setHandler("");
//        warn.setRemark("");
//        warn.setCreateBy("");
        warn.setCreateTime(dateTime);
        
        /*ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);
        RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
        List<SdDevices> list = sdDevicesService.selectSensorListByTunnelId(tunnelId);*/
        
       /* List<SdAlarmModel> mesageList = new ArrayList<SdAlarmModel>();
        for(SdDevices sdDevice:list){
        	redisCache.getCacheObject(String.valueOf(sdDevice.getEqId()));
        	SdAlarmModel sdalarmmodel = new SdAlarmModel();
        	sdalarmmodel.setEqId(String.valueOf(sdDevice.getEqId()));
        	sdalarmmodel.setEqName(sdDevice.getEqName());
        	sdalarmmodel.setEqTypeId(sdDevice.getEqType().toString());
        	sdalarmmodel.setEqTypeName(sdDevice.getTypeName().getTypeName());
        	sdalarmmodel.setSensorValue(redisCache.getCacheObject(String.valueOf(sdDevice.getEqId())));
        	sdalarmmodel.setTunnelId(tunnelId);
        	sdalarmmodel.setTunnelName(sdDevice.getTunnelName().getTunnelName());
        	sdalarmmodel.setHappentTime(dateTime);
        	mesageList.add(sdalarmmodel);
        }*/
        
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("baseInfo", warn);
     //   map.put("sensorInfo", mesageList);
        warn.setStarts(map.toString());
        SpringUtils.getBean(ISdWarningInfoService.class).insertSdWarningInfo(warn);
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
        ctx.writeAndFlush("client"+ InetAddress.getLocalHost().getHostName() + "connect！ \n");
        super.channelActive(ctx);
    }
   /**
    * 通道读取完成
    */
   @Override
   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	   
       super.channelReadComplete(ctx);
       ctx.flush();
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