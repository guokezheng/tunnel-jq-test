package com.tunnel.deal.vehicleinspection;


import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.TunnelStepEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdMicrowavePeriodicStatistics;
import com.tunnel.business.domain.dataInfo.SdMicrowaveRealData;
import com.tunnel.business.domain.enhancedLighting.SdEnhancedLightingConfig;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.dataInfo.SdMicrowavePeriodicStatisticsMapper;
import com.tunnel.business.mapper.dataInfo.SdMicrowaveRealDataMapper;
import com.tunnel.business.mapper.digitalmodel.SdRadarDetectDataTemporaryMapper;
import com.tunnel.business.mapper.enhancedLighting.SdEnhancedLightingConfigMapper;
import com.tunnel.business.utils.util.RadixUtil;
import com.tunnel.deal.light.impl.SanJingLight;
import com.zc.common.core.ThreadPool.ThreadPool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@ChannelHandler.Sharable
@Component
public class MicrowaveNettyClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(MicrowaveNettyClientHandler.class);

    private static SdDevicesMapper sdDevicesMapper = SpringUtils.getBean(SdDevicesMapper.class);

    private static SdEnhancedLightingConfigMapper sdEnhancedLightingConfigMapper = SpringUtils.getBean(SdEnhancedLightingConfigMapper.class);

    private SanJingLight sanJingLight =  SpringUtils.getBean(SanJingLight.class);

    private SdRadarDetectDataTemporaryMapper sdRadarDetectDataTemporaryMapper =  SpringUtils.getBean(SdRadarDetectDataTemporaryMapper.class);


    private static Thread[] threadArrs = new Thread[1];

    private static Integer bright = 20;

    private int tallLuminance = 70;
    private int lowLuminance = 20;

    /**
     * Redis缓存工具类
     * */
    private RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    /**
     * 建立连接时
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        try {
            InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
            int port = ipSocket.getPort();
            String host = ipSocket.getHostString();

            log.info("与设备"+host+":"+port+"连接成功!");
            ctx.fireChannelActive();
        }
        catch (Exception e) {
            log.info("channelActive: " + e.getMessage());
        }
    }

    /**
     * 关闭连接时
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        try {
            InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
            int port = ipSocket.getPort();
            String host = ipSocket.getHostString();

            log.error("与设备" + host + ":" + port + "连接断开!");
            SdDevices sdDevices = new SdDevices();
            sdDevices.setEqType(DevicesTypeEnum.WEI_BO_CHE_JIAN.getCode());
            sdDevices.setIp(host);
            sdDevices.setPort(String.valueOf(port));
            List<SdDevices> devices = sdDevicesMapper.selectSdDevicesList(sdDevices);
            SdDevices dev = devices.get(0);
            dev.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
            dev.setEqStatusTime(new Date());
            sdDevicesMapper.updateSdDevices(dev);
            MicrowaveNettyClient.channels.remove(host+":"+String.valueOf(port));
            final EventLoop eventLoop = ctx.channel().eventLoop();
        }
        catch (Exception e) {
            log.info("channelInactive: " + e.getMessage());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("服务端连接异常：" + ctx.channel().id().asShortText());
    }

    /**
     * 收到报文，业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        int port = ipSocket.getPort();
        String host = ipSocket.getHostString();

        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] buffers = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(buffers);
        String receiveStr = ConvertCode.receiveHexToString(buffers);
        byteBuf.release();
        log.info("读取到的信息："+receiveStr);

        log.info("接收到 " + host + ":" + port + "的数据");
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqType(DevicesTypeEnum.WEI_BO_CHE_JIAN.getCode());
        sdDevices.setIp(host);
        sdDevices.setPort(String.valueOf(port));
        List<SdDevices> devices = sdDevicesMapper.selectSdDevicesList(sdDevices);
        SdDevices dev = devices.get(0);
        dev.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
        dev.setEqStatusTime(new Date());
        sdDevicesMapper.updateSdDevices(dev);
        //周期统计数据
        if(receiveStr.startsWith("FFF9") || receiveStr.startsWith("fff9")){
            dataAnalySis(receiveStr, dev.getEqId(), dev.getEqTunnelId());
        }
        //单车数据
        if(receiveStr.startsWith("FFF8") || receiveStr.startsWith("fff8")){
            dataAnalySingle(receiveStr, dev.getEqId(), dev.getEqTunnelId());
        }

        String direction =dev.getEqDirection();

        //增加  模式功能。根据选择不同模式 。做出响应策略。
        // 当前模式为自动模式时，根据过车信息来进行控制，当前加强照明。实现车来灯亮，车走灯灭
        // 当前模式为定时模式时，随时间段进行调整亮度照明。并增加根据当前时间  15分钟内隧道过车 流量数量来进行控制加强照明亮度浮动范围
        //  当前模式为ECO模式时，结合上述两种功能实现
        //redisCache.getCacheObject();

        //单车数据      测试使用
//        if(receiveStr.length()>=34&&(receiveStr.startsWith("FFF8") || receiveStr.startsWith("fff8"))){
//            //开启线程  推送加强照明灯指令。并  30秒后执行降低光照强度指令。
//            ThreadPool.executor.execute(() -> {
//                try {
//                    //测试数据  后去需要根据隧道id  以及方向  获取 所有加强照明信息
//                    List<String> deviceIds = new ArrayList<>();
//                    deviceIds.add("JQ-WeiFang-JiuLongYu-HSD-RLC-001");
//                    deviceIds.add("JQ-WeiFang-JiuLongYu-HSD-RLC-002");
//                    deviceIds.add("JQ-WeiFang-JiuLongYu-HSD-RLC-003");
//                    String operIp = "";
//                    try {
//                        operIp = InetAddress.getLocalHost().getHostAddress();
//                    } catch (UnknownHostException e) {
//                        e.printStackTrace();
//                    }
//                    //推送调光 指令。
//                    //bright
//                    if(threadArrs[0]==null||bright != lowLuminance){
//                        //线程开始推送加强照明指令
//                        sanJingLight.setBrightnessByList(deviceIds,bright,tallLuminance,"2",operIp);
//                    }
//                    //替换线程
//                    replaceThread(Thread.currentThread());
//                    //等待30秒后 执行 降低 光照强度功能
//                    Thread.sleep(30000);
//                    //降低光照强度执行完毕
//                    sanJingLight.setBrightnessByList(deviceIds,tallLuminance,lowLuminance,"2",operIp);
//                    //清除当前记录线程
//                    threadArrs[0] =  null;
//                    //记录当前亮度值
//                    bright = lowLuminance;
//                } catch (InterruptedException e) {
//                    log.error("异常被catch到");
//                }
//            });
//        }
        //MicrowaveNettyClient.channels.get(host + ":" + port).setActiveTime(new Timestamp(System.currentTimeMillis()));
    }

    private static void dataAnalySis(String firstContent, String id, String tunnelId) {
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
                data.setTunnelId(tunnelId);
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

    private static void dataAnalySingle(String firstContent, String id, String tunnelId) {
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
				realData.setTunnelId(tunnelId);
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


    /**
     *
     * @param tunnelId      隧道id
     * @param roadDir     方向
     * @param modeType      0 自动模式  1   定时模式    2节能模式
     */
    public void adjustBrightnessByRunMode(String tunnelId,String roadDir,Integer modeType){
        //根据隧道id获取对应隧道  加强照明策略 配置信息
        SdEnhancedLightingConfig sdEnhancedLightingConfig = new SdEnhancedLightingConfig();
        sdEnhancedLightingConfig.setTunnelId(tunnelId);
        SdEnhancedLightingConfig enhancedLightingConfig = sdEnhancedLightingConfigMapper.selectSdEnhancedLightingConfigListByParam(sdEnhancedLightingConfig);
        //获取当前时间。判断当前时间
        String json = enhancedLightingConfig.getTimeSlot();
        //配置信息 JSON
        List<Map> jsonArry  = JSONObject.parseArray(json, Map.class);
        //调光最大区间
        Integer  maxLuminanceRange = enhancedLightingConfig.getMaxLuminanceRange();
        //调光最小区间
        Integer  minLuminanceRange = enhancedLightingConfig.getMinLuminanceRange();
        //最大车流量
        Integer  maxTrafficFlow = enhancedLightingConfig.getMaxTrafficFlow();
        //响应时间
        Long respondTime = enhancedLightingConfig.getRespondTime();
        //从 sd_radar_detect_data_temporary  表中 获取当前1分钟内过车流量信息
        int nowTrafficFlow = sdRadarDetectDataTemporaryMapper.getSdRadarDetectDataCount();

        //缓存获取亮度值  与当前亮度值   与当前亮度值比对。如果相同 忽略当前操作。
        Integer num = redisCache.getCacheObject("control:"+tunnelId+"_LuminanceRange");

        //测试数据  后去需要根据隧道id  以及方向  获取 所有加强照明信息
        List<String> deviceIds = new ArrayList<>();
        deviceIds.add("JQ-WeiFang-JiuLongYu-HSD-RLC-001");
        deviceIds.add("JQ-WeiFang-JiuLongYu-HSD-RLC-002");
        deviceIds.add("JQ-WeiFang-JiuLongYu-HSD-RLC-003");
        String operIp = "";
        try {
            operIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        switch (modeType){
            case 1:
                //定时模式
                //根据隧道ID  以及方向查出所有 加强照明设备。
                for (Map map:jsonArry) {
                    String startTime = map.get("startTime").toString();
                    String endTimne = map.get("endTime").toString();
                    //获取当前时间  查看是否符合当前时间段
                    try {
                        //查看当前时间是否在此时间范围内
                        if(belongCalendar(startTime,endTimne)){
                            Integer luminanceRange =  Integer.parseInt(map.get("value").toString());
                            //查看1分钟内车流量  是否超过最大车流量  maxTrafficFlow
                            int nowLuminanceRange =  getLuminanceByParam(nowTrafficFlow,maxTrafficFlow,maxLuminanceRange,minLuminanceRange,luminanceRange);
                            if(num == null ){
                                num = luminanceRange;
                            }
                            log.info("当前亮度num："+num+" 根据车流量计算的亮度nowLuminanceRange:" +nowLuminanceRange);
                            if(num ==null || num != nowLuminanceRange){
                                redisCache.setCacheObject("control:"+tunnelId+"_LuminanceRange",nowLuminanceRange);
                                Integer finalNum = num;
                                String finalOperIp = operIp;
                                ThreadPool.executor.execute(() -> {
                                    //推送调光 指令。
                                    sanJingLight.setBrightnessByList(deviceIds, finalNum,nowLuminanceRange,"2", finalOperIp);
                                    log.info(Thread.currentThread().getName()+"开始推送调光指令");
                                });
                            }
                            break;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2://2节能模式
                ;
                break;
            case 0:  //自动模式
                //当前亮度值初始值
                Integer luminanceRange = enhancedLightingConfig.getBeforeLuminance();
                //查看1分钟内车流量  是否超过最大车流量  maxTrafficFlow
                int nowLuminanceRange =  getLuminanceByParam(nowTrafficFlow,maxTrafficFlow,maxLuminanceRange,minLuminanceRange,luminanceRange);
                if(num == null ){
                    //给与初始值
                    num = luminanceRange;
                }
                log.info("当前亮度num："+num+" 根据车流量计算的亮度nowLuminanceRange:" +nowLuminanceRange);
                redisCache.setCacheObject("control:"+tunnelId+"_LuminanceRange",nowLuminanceRange);
                Integer finalNum1 = num;
                String finalOperIp1 = operIp;
                ThreadPool.executor.execute(() -> {
                    try {
                        if(threadArrs[0]==null|| finalNum1 == null || finalNum1 != nowLuminanceRange){
                            //推送调光 指令。
                            sanJingLight.setBrightnessByList(deviceIds, finalNum1,nowLuminanceRange,"2", finalOperIp1);
                            log.info(Thread.currentThread().getName()+"开始推送调光指令");
                        }
                        //替换线程
                        replaceThread(Thread.currentThread());
                        //等待30秒后 执行 降低 光照强度功能
                        Thread.sleep(respondTime);
                        //降低光照强度执行完毕
                        sanJingLight.setBrightnessByList(deviceIds, finalNum1,lowLuminance,"2", finalOperIp1);
                        //清除当前记录线程
                        threadArrs[0] =  null;
                        //记录当前亮度值
                        redisCache.setCacheObject("control:"+tunnelId+"_LuminanceRange",lowLuminance);
                    } catch (InterruptedException e) {
                        log.error("经过一辆车，当前线程被阻断。");
                    }
                });
                break;
        }
    }


    /**
     *
     * @param thread
     */
    public static synchronized void replaceThread(Thread thread){
        if(threadArrs[0]!=null){
            Thread oldThread = threadArrs[0];
            log.info("删除旧线程"+oldThread.getName());
            //删除旧线程
            oldThread.interrupt();
        }
        //存入新线程
        threadArrs[0] = thread;
    }


    public static boolean belongCalendar(String startTimeStr, String endTimneStr) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // 设置日期格式
        Date nowTime = df.parse(df.format(new Date()));
        Date startTime = df.parse(startTimeStr);
        Date endTime = df.parse(endTimneStr);
        boolean result;
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar start = Calendar.getInstance();
        start.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        //查看当前  开始时间是否大于结束时间
        if(start.getTimeInMillis()>end.getTimeInMillis()){
            end.add(Calendar.DAY_OF_MONTH,1);
            if (date.after(start) && date.before(end)) {
                result =  true;
            } else {
                result = false;
            }
        }else{
            if (date.after(start) && date.before(end)) {
                result =  true;
            } else {
                result = false;
            }
        }
        return result;
    }


    /**
     *
     * @param nowTrafficFlow    当前车流量
     * @param maxTrafficFlow     最大车流量
     * @param maxLuminanceRange     最大调光区间值
     * @param minLuminanceRange     最小调光区间值
     * @param luminanceRange        当前时间段调光值
     * @return
     */
    public static int getLuminanceByParam(Integer nowTrafficFlow, Integer maxTrafficFlow, Integer maxLuminanceRange,Integer minLuminanceRange,Integer luminanceRange) {
        if(nowTrafficFlow >= maxTrafficFlow ){
            //当前车流量大于现在车流量
            return luminanceRange+maxLuminanceRange;
        }else{
            Integer regionLuminanceRange = maxLuminanceRange - minLuminanceRange;
            //计算公式  (当前车流量/最大车流量)*亮度区间值
            BigDecimal nowTrafficFlowBig = new BigDecimal(nowTrafficFlow);
            BigDecimal maxTrafficFlowBig = new BigDecimal(maxTrafficFlow);
            BigDecimal regionLuminanceRangeBig = new BigDecimal(regionLuminanceRange);
            nowTrafficFlowBig = nowTrafficFlowBig.divide(maxTrafficFlowBig, 2,BigDecimal.ROUND_HALF_UP).multiply(regionLuminanceRangeBig);
            return luminanceRange + (nowTrafficFlowBig.intValue()/5)*5;
        }
    }
}
