package com.tunnel.deal.vehicleinspection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdMicrowaveRealData;
import com.tunnel.business.domain.dataInfo.SdMicrowavePeriodicStatistics;
import com.tunnel.business.mapper.dataInfo.SdMicrowaveRealDataMapper;
import com.tunnel.business.mapper.dataInfo.SdMicrowavePeriodicStatisticsMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.utils.util.RadixUtil;
import com.tunnel.business.utils.util.SpringContextUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
* Title: MicrowaveNettyServerHandler
* Description:  微波车检数据接收
 */
public class MicrowaveNettyServerHandler extends  ChannelInboundHandlerAdapter {
    
	private EventLoopGroup group;
//	@Autowired
//    private static RedisService redisService;
	@Autowired
    private static ISdDevicesService sdDevicesService;
	
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
        String strIP = insocket.getAddress().getHostAddress();
        SdDevices sdDevices = new SdDevices();
        sdDevices.setIp(strIP);
        sdDevices.setEqType(108l);
        sdDevicesService = (ISdDevicesService) SpringContextUtils.getBean(ISdDevicesService.class);
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(sdDevices);
//        if(list.size() != 1){
//        	return;
//        }
        String str = bytes2HexString(result1);
        System.out.println("原始值===="+str);
        /*if(str.contains("5A31")){
            dataAnalysis(str,list.get(0).getEqId());
        }*/
		//周期统计数据
        if(str.contains("FFF9")){
        	dataAnalySis(str,"test-eqId");
        }
        //实时数据
		if(str.contains("FFF8")){
			dataAnalySingle(str,"test-eqId");
		}
        System.out.println(str);
        // 释放资源
        result.release();
        ctx.flush();
	}
	
	private static void dataAnalySis(String firstContent, String id) {
		String strArr[] = firstContent.split("FFF9");
//		List<SdMicrowavePeriodicStatistics> list = new ArrayList<SdMicrowavePeriodicStatistics>();
        for(int i=0;i<strArr.length;i++){
        	String str = strArr[i];
        	if(str.length()>4){
        		str = "FFF9" + str;
/*    			String xxt = str.substring(0, 2); // 信息头
    			String bsf = str.substring(2, 4); // 标识符
    			String sjcd = str.substring(4, 6);// 数据长度
    			String time = str.substring(6, 14);// 时间
				//按位截取=======================================
*/    			String roadId = RadixUtil.hexToDecimal(str.substring(14, 16)); // 车道号
    			String totalNum = RadixUtil.hexToDecimal(str.substring(16, 20));// 总车流量
    			String smallNum = RadixUtil.hexToDecimal(str.substring(20, 24)); // 小车流量
    			String mediumNum = RadixUtil.hexToDecimal(str.substring(24, 28)); // 中车流量
    			String largeNum = RadixUtil.hexToDecimal(str.substring(28, 32)); // 大车流量
    			//String car4 = str.substring(32, 36);// 预留
    			//String car5 = str.substring(36, 40);
    			String avgSpeed = RadixUtil.hexToDecimal(str.substring(40, 42));// 平均车速
    			String smallSpeed = RadixUtil.hexToDecimal(str.substring(42, 44));//小车车速
    			String mediumSpeed = RadixUtil.hexToDecimal(str.substring(44, 46));//中型车车速
    			String largeSpeed = RadixUtil.hexToDecimal(str.substring(46, 48));//大型车车速
    			//String speed4 = str.substring(48, 50);//预留
    			//String speed5 = str.substring(50, 52);
//    			String avgHeadway = str.substring(52, 56);//平均车间据
				Object avgHeadway = new BigDecimal((double) Integer.parseInt(str.substring(52, 56), 16) / 10)
						.setScale(1, RoundingMode.HALF_UP).doubleValue();
//    			String avgLength = str.substring(56, 60);//平均车长
				Object avgLength = new BigDecimal((double) Integer.parseInt(str.substring(56, 60), 16))
						.setScale(1, RoundingMode.HALF_UP).doubleValue();
//    			String avgOccupancy = RadixUtil.hexToDecimal(str.substring(60, 64)); // 平均压占率
				Object avgOccupancy = new BigDecimal((double) Integer.parseInt(str.substring(60, 64), 16) / 10)
						.setScale(1, RoundingMode.HALF_UP).doubleValue();

            	Date nowDate = new Date();
            	SdMicrowavePeriodicStatistics data = new SdMicrowavePeriodicStatistics();//创建车流量对象
				data.setDeviceId(id);//设备id
				data.setLaneNo(Long.valueOf(roadId) + 1);//车道号
				data.setCreateTime(nowDate);
				data.setTrafficFlowTotal(totalNum);
				data.setSmallVehicleNum(smallNum);
				data.setSmallVehicleSpeed(smallSpeed);
				data.setMidVehicleNum(mediumNum);
				data.setMidVehicleSpeed(mediumSpeed);
				data.setHeavyVehicleNum(largeNum);
				data.setHeavyVehicleSpeed(largeSpeed);
				data.setAvgLength(avgLength.toString());
				data.setAvgHeadway(avgHeadway.toString());
				data.setAvgSpeed(avgSpeed);
				data.setAvgOccupancy(avgOccupancy.toString());
				SpringUtils.getBean(SdMicrowavePeriodicStatisticsMapper.class).insertSdMicrowavePeriodicStatistics(data);
//            	list.add(data);
        	}
        }
        //redis初始化
//        redisService = (RedisService) SpringContextUtils.getBean(RedisService.class);
//        redisService.setCacheObject(id,list);
		
	}

	private static void dataAnalySingle(String firstContent, String id) {
		String strArr[] = firstContent.split("FFF8");
//		List<SdMicrowaveRealData> list = new ArrayList<SdMicrowaveRealData>();
		for(int i=0;i<strArr.length;i++){
			String str = strArr[i];
			if(str.length()>4){
				str = "FFF8" + str;
				//按位截取=======================================
//    			String xxt = str.substring(0, 2); // 信息头
//    			String bsf = str.substring(2, 4); // 标识符
//    			String sjcd = str.substring(4, 6);// 数据长度
//    			String time = str.substring(6, 14);// 时间
    			String roadId = RadixUtil.hexToDecimal(str.substring(14, 16)); // 车道号
				String occupationTime = RadixUtil.hexToDecimal(str.substring(16, 20));// 压占时间
				String speed = RadixUtil.hexToDecimal(str.substring(20, 22));// 车速
    			String vehicleType = RadixUtil.hexToDecimal(str.substring(22, 24));//车种  添加枚举
				//String spacing = RadixUtil.hexToDecimal(str.substring(24, 28));// 车间距
				Object spacing = new BigDecimal((double) Integer.parseInt(str.substring(24, 28), 16) / 10)
						.setScale(1, RoundingMode.HALF_UP).doubleValue();
				String length = RadixUtil.hexToDecimal(str.substring(28, 32));// 车长
				Date nowDate = new Date();
				SdMicrowaveRealData realData = new SdMicrowaveRealData();//创建车流量对象
				realData.setDeviceId(id);//设备id
				realData.setLaneNo(Long.valueOf(roadId) + 1);//车道号
				realData.setCreateTime(nowDate);
//				realData.setDirection();
//				realData.setTunnelId();
				realData.setOccupationTime(occupationTime);
				realData.setVehicleLength(length);
				realData.setVehicleSpeed(speed);
				realData.setHeadway(spacing.toString());
				realData.setDeviceId(id);
				realData.setVehicleType(vehicleType);
				SpringUtils.getBean(SdMicrowaveRealDataMapper.class).insertSdMicrowaveRealData(realData);
			}
		}
	}

	private String bytes2HexString(byte[] b) {  
	    StringBuffer result = new StringBuffer();  
	    String hex;  
	    for (int i = 0; i < b.length; i++) {  
	        hex = Integer.toHexString(b[i] & 0xFF);  
	        if (hex.length() == 1) {  
	            hex = '0' + hex;  
	        }  
	        result.append(hex.toUpperCase());  
	    }  
	    return result.toString();  
	}
	
	 public String transJsgs(String strNums1,String strNums2){
	    	// 整数部分
	    	String zs = strNums1;//1车道平均车速
	    	// 小数部分
	    	String xs = strNums2;
	    	BigDecimal num1 = new BigDecimal(xs);
	    	BigDecimal num2 = new BigDecimal(256);
	    	BigDecimal num3 = new BigDecimal(zs);
	    	BigDecimal result = num1.divide(num2);
	    	return num3.add(result).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString().toString();
	    }
	 
	/**
	 * 5A31协议版本解析
	 * @throws JsonProcessingException
	 */
//	private void dataAnalysis(String firstContent,String id) throws JsonProcessingException {
//		String strArr[] = firstContent.split("5A31");
//		List<SdMicrowavePeriodicStatistics> list = new ArrayList<SdMicrowavePeriodicStatistics>();
//        for(int i=0;i<strArr.length;i++){
//        	String weiboCont = strArr[i];
//        	String twoLaneCar = "0";//1车道车流量
//        	String twoZhanyoulv = "0";//1车道占有率
//        	String twoPingjun = "0";//1车道平均车速
//        	if(weiboCont.length()>10){
//        		twoLaneCar = RadixUtil.hexToDecimal(weiboCont.substring(31*2, 31*2 + 6));//1车道车流量
//        		// 整数部分
//            	String zylzs = RadixUtil.hexToDecimal(weiboCont.substring(34*2, 34*2 + 2));
//            	// 小数部分
//            	/*String zylxs = RadixUtil.hexToDecimal(weiboCont.substring(35*2, 35*2 + 2));
//            	twoZhanyoulv = transJsgs(zylzs,zylxs);*/
//            	// 整数部分
//            	String cszs = RadixUtil.hexToDecimal(weiboCont.substring(29*2, 29*2 + 2));//1车道平均车速
//            	// 小数部分
//            	/*String csxs = RadixUtil.hexToDecimal(weiboCont.substring(30*2, 30*2 + 2));
//            	twoPingjun = transJsgs(cszs,csxs);*/
//            	//按位截取=======================================
//            	Date nowDate = new Date();
//            	SdMicrowavePeriodicStatistics sdTrs1 = new SdMicrowavePeriodicStatistics();//创建车流量对象
//            	sdTrs1.setDeviceId(id);//设备id
//            	sdTrs1.setByLane(Long.valueOf(i));//车道号
//            	sdTrs1.setCreateTime(nowDate);
//            	sdTrs1.setByVehicelNum(Long.parseLong(zylzs));//车流量数
//            	sdTrs1.setfSpaceOccupyRation(Long.parseLong(twoZhanyoulv));//占有率
//            	sdTrs1.setBySpeed(Long.parseLong(cszs));//平均速度
//            	list.add(sdTrs1);
//        	}
//        }
//        //redis初始化
////        redisService = (RedisService) SpringContextUtils.getBean(RedisService.class);
////        redisService.setCacheObject(id,list);
//    }

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