package com.tunnel.deal.vehicleinspection;


import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.Threads;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.*;
import com.tunnel.business.domain.enhancedLighting.SdEnhancedLightingConfig;
import com.tunnel.business.domain.event.SdStrategyRl;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.dataInfo.SdMicrowavePeriodicStatisticsMapper;
import com.tunnel.business.mapper.dataInfo.SdMicrowaveRealDataMapper;
import com.tunnel.business.mapper.digitalmodel.SdRadarDetectDataTemporaryMapper;
import com.tunnel.business.mapper.enhancedLighting.SdEnhancedLightingConfigMapper;
import com.tunnel.business.mapper.event.SdStrategyRlMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesProtocolService;
import com.tunnel.business.utils.util.RadixUtil;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.generalcontrol.service.GeneralControlService;
import com.tunnel.deal.light.impl.SanJingLight;
import com.tunnel.deal.light.impl.SansiLightImpl;
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
import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
@Component
public class MicrowaveNettyClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(MicrowaveNettyClientHandler.class);

    private SdDevicesMapper sdDevicesMapper = SpringUtils.getBean(SdDevicesMapper.class);

    private SdEnhancedLightingConfigMapper sdEnhancedLightingConfigService = SpringUtils.getBean(SdEnhancedLightingConfigMapper.class);

    private GeneralControlService generalControlService = SpringUtils.getBean(GeneralControlService.class);

    private SanJingLight sanJingLight =  SpringUtils.getBean(SanJingLight.class);

    private SansiLightImpl sansiLightImpl =  SpringUtils.getBean(SansiLightImpl.class);

    private SdRadarDetectDataTemporaryMapper sdRadarDetectDataTemporaryMapper =  SpringUtils.getBean(SdRadarDetectDataTemporaryMapper.class);

    private SdDeviceDataMapper sdDeviceDataMapper =  SpringUtils.getBean(SdDeviceDataMapper.class);

    private static Map<String,Thread[]> threadArrsMap = new HashMap<>();

    @Autowired
    private SdStrategyRlMapper sdStrategyRlMapper;

    /**
     * Redis缓存工具类
     * */
    private RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    @Autowired
    private ISdDevicesProtocolService sdDevicesProtocolService;


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

        log.info("接收到 " + host + ":" + port + "的数据");

        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqType(DevicesTypeEnum.WEI_BO_CHE_JIAN.getCode());
        sdDevices.setIp(host);
        sdDevices.setPort(String.valueOf(port));
        List<SdDevices> devices = sdDevicesMapper.selectSdDevicesListByParam(sdDevices);
        SdDevices dev = devices.get(0);
        dev.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
        dev.setEqStatusTime(new Date());
        sdDevicesMapper.updateSdDevices(dev);

//        //周期统计数据
        if(receiveStr.startsWith("FFF9") || receiveStr.startsWith("fff9")){
            dataAnalySis(receiveStr, dev.getEqId(), dev.getEqTunnelId(),dev.getEqDirection());
        }
        //单车数据
//        if(receiveStr.startsWith("FFF8") || receiveStr.startsWith("fff8")){
//            dataAnalySingle(receiveStr, dev.getEqId(), dev.getEqTunnelId());
//        }

        //单车数据
        //增加  模式功能。根据选择不同模式 。做出响应策略。
        // 当前模式为自动模式时，根据过车信息来进行控制，当前加强照明。实现车来灯亮，车走灯灭
        // 当前模式为定时模式时，随时间段进行调整亮度照明。并增加根据当前时间  15分钟内隧道过车 流量数量来进行控制加强照明亮度浮动范围
        //  当前模式为ECO模式时，结合上述两种功能实现
        //redisCache.getCacheObject();
        if(receiveStr.length()==34&&(receiveStr.startsWith("FFF8") || receiveStr.startsWith("fff8"))){
            //查询当前加强照明配置信息
            SdEnhancedLightingConfig sdEnhancedLightingConfig = null;
            sdEnhancedLightingConfig = redisCache.getCacheObject("control:lightFixedTimeTask:"+dev.getEqTunnelId());
            if(sdEnhancedLightingConfig == null){
                SdEnhancedLightingConfig param = new SdEnhancedLightingConfig();
                param.setTunnelId(dev.getEqTunnelId());
                //根据隧道id获取对应隧道  加强照明策略 配置信息
                List<SdEnhancedLightingConfig> sdEnhancedLightingConfigList =  sdEnhancedLightingConfigService.selectSdEnhancedLightingConfigList(param);
                sdEnhancedLightingConfig = sdEnhancedLightingConfigList.get(0);
                redisCache.setCacheObject("control:lightFixedTimeTask:"+dev.getEqTunnelId(),sdEnhancedLightingConfig,60, TimeUnit.SECONDS);//每分钟，重新请求一次
            }
            //隧道id , 方向
            //查看当前隧道 照明配置信息
            //1模式类型是否为定时模式，如果为定时模式 则继续执行调光指令    2是否开启调光模式  0关闭  1开启
            if(sdEnhancedLightingConfig.getIsStatus() == 1&&sdEnhancedLightingConfig.getModeType() != 0){
                //推送当前指令
                adjustBrightnessByRunMode(dev.getEqTunnelId(),dev.getEqDirection(),sdEnhancedLightingConfig);
            }
        }
        //更新当前设备 连接时间。
        MicrowaveNettyClient.channels.get(host + ":" + port).setActiveTime(new Timestamp(System.currentTimeMillis()));
    }
    private static String dataAnalysis(String str, Integer number, Integer numberTwo,String type){
        String totalNum = null ;
        if( type=="1"|| type=="2"|| type=="3"|| type=="4"){//总车流量
            //判断是单个数字的不需要解析
            if(str.substring(number, numberTwo).replaceAll("0+$", "").length()==1&&
                    str.substring(number, numberTwo).replaceAll("0+$", "").matches("\\d+")){

                totalNum = str.substring(number, numberTwo).replaceAll("0+$", "");
            }else{

                if(StringUtils.isNotNull(str.substring(number, numberTwo).replaceAll("0+$", ""))
                        &&str.substring(number, numberTwo).replaceAll("0+$", "").length()>0){
                    totalNum = RadixUtil.hexToDecimal(str.substring(number, numberTwo).replaceAll("0+$", ""));
                }else{
                    totalNum = "";
                }

            }
        }else{
            String isNull= str.substring(number, numberTwo);
            if(StringUtils.isNotNull(isNull)){
                totalNum = RadixUtil.hexToDecimal(str.substring(number, numberTwo));// 总车流量
            }else{
                totalNum = "";// 总车流量
            }
        }
        return  totalNum;
    }

    private static void dataAnalySis(String firstContent, String id, String tunnelId,String eqDirection) {
        String strArr[] = firstContent.split("fff9");
//		List<SdMicrowavePeriodicStatistics> list = new ArrayList<SdMicrowavePeriodicStatistics>();
        for(int i=0;i<strArr.length;i++){
            String str = strArr[i];
            if(str.length()>4){
                str = "FFF9" + str;
                String totalNum = dataAnalysis(str, 19, 21,"1");
                String smallNum = dataAnalysis(str, 23, 25,"2"); // 小型车
                String mediumNum = dataAnalysis(str, 27, 29,"3");// 中型车
                String largeNum = dataAnalysis(str, 31, 33,"4");// 大型车
                String avgSpeed = dataAnalysis(str, 40, 42,"5");// 平均车速
                String smallSpeed = dataAnalysis(str, 42, 44,"6");// 车速c0
                String mediumSpeed = dataAnalysis(str, 44, 46,"7");// 车速c1
                String largeSpeed = dataAnalysis(str, 46, 48,"8");// 车速c2
                //平均车长
                String avgLength = dataAnalysis(str, 56, 60,"7");// 平均车长
                String avgHeadway = dataAnalysis(str, 52, 56,"8");// 平均车间距
                String avgOccupancy = dataAnalysis(str, 60, 64,"7");// 平均压占率



                Date nowDate = new Date();
                SdMicrowavePeriodicStatistics data = new SdMicrowavePeriodicStatistics();//创建车流量对象
                data.setDeviceId(id);//设备id
                data.setTunnelId(tunnelId);
                data.setLaneNo(Long.valueOf(i) );//车道号
                data.setCreateTime(nowDate);
                //总车辆
                data.setTrafficFlowTotal(totalNum);
                //小型车
                data.setSmallVehicleNum(smallNum);
                data.setSmallVehicleSpeed(smallSpeed);

                data.setMidVehicleNum(mediumNum);
                data.setMidVehicleSpeed(mediumSpeed);

                data.setHeavyVehicleNum(largeNum);
                data.setHeavyVehicleSpeed(largeSpeed);
                //平均车长
                data.setAvgLength(avgLength.toString());
                //平均车间距
                data.setAvgHeadway(avgHeadway.toString());
                data.setAvgSpeed(avgSpeed);
                //平均压占率
                data.setAvgOccupancy(avgOccupancy.toString());
                data.setEqDirection(eqDirection);
                data.setVehicleSpeedReserve5(str);
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
     * 根据过车信息，执行调光任务。实现车来灯亮，车走灯灭效果。
     * 当前默认一辆车经过隧道后， 定时查看当前redis 内缓存当前隧道内车辆信息。判断隧道内是否有车辆存在。
     * @param tunnelId      隧道id
     * @param roadDir     方向
     */
    public void adjustBrightnessByRunMode(String tunnelId,String roadDir,SdEnhancedLightingConfig sdEnhancedLightingConfig){
        //模式
        Integer modeType = sdEnhancedLightingConfig.getModeType();
        log.info("加强照明调光 当前模式为"+modeType);
        //获取当前时间。判断当前时间
//        String json = sdEnhancedLightingConfig.getTimeSlot();
//        //配置信息 JSON
//        List<Map> jsonArry  = JSONObject.parseArray(json, Map.class);
//        //调光最大区间
//        Integer  maxLuminanceRange = sdEnhancedLightingConfig.getMaxLuminanceRange();
//        //调光最小区间
//        Integer  minLuminanceRange = sdEnhancedLightingConfig.getMinLuminanceRange();
//        //最大车流量
////        Integer  maxTrafficFlow = Math.toIntExact(sdEnhancedLightingConfig.getMaxTrafficFlow());
//        //响应时间
        Long respondTime = sdEnhancedLightingConfig.getRespondTime();
//        //最小亮度值
        Integer  minLuminance = sdEnhancedLightingConfig.getMinLuminance();
//        //当前亮度值
        Integer beforeLuminance = sdEnhancedLightingConfig.getBeforeLuminance();

        Integer luminanceRange;
        //放入redis缓存
        //查找所有加强照明与基本照明
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqTunnelId(tunnelId);
        sdDevices.setEqDirection(roadDir);
        sdDevices.setEqTypes(new Long[]{DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode(),DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode()});
        List<SdDevices> deviceIds = sdDevicesMapper.selectSdDevicesListByParam(sdDevices);

        //推送加强照明
        int nowTrafficFlow;
        //是否开启车流量模式
//        if(sdEnhancedLightingConfig.getIsTrafficVolume() == 1){
//            //从 sd_radar_detect_data_temporary  表中 获取当前1分钟内过车流量信息
//            nowTrafficFlow = sdRadarDetectDataTemporaryMapper.getSdRadarDetectDataCount(tunnelId,roadDir);
//        }else{
//            nowTrafficFlow = 0;
//        }
        //1  自动模式  2 节能模式   0定时模式
        switch (modeType){
            case 1:  //自动模式  废弃

                //忽略车流量
                //查看1分钟内车流量  是否超过最大车流量  maxTrafficFlow
                //int nowLuminanceRange =  sdEnhancedLightingConfigService.getLuminanceByParam(nowTrafficFlow,maxTrafficFlow,maxLuminanceRange,minLuminanceRange,luminanceRange);

                //亮度值计算:
                // 后期修改为 根据洞外亮度计算当前亮度值
                // 当前亮度值 根据定时模式获取当前路段亮度。
//                int nowLuminanceRange =  beforeLuminance;
//
//                //推送调光值时，按照隧道入口 -> 出口顺序处理
//                ThreadPool.executor.execute(() -> {
//                    log.info("【"+Thread.currentThread().getName()+"】开始准备调光：");
//                    //查看当前线程  threadArrs  集合是否存在。
//                    Thread[] threadArrs;
//                    String key = tunnelId+"_"+roadDir;
//                    if(threadArrsMap.containsKey(key)){
//                        threadArrs = threadArrsMap.get(key);
//                    }else{
//                        threadArrs = new Thread[1];
//                        threadArrsMap.put(key,threadArrs);
//                    }
//                    ThreadPool.executor.execute(() -> {
//                        for (SdDevices devices:deviceIds) {
//                            //查看当前设备是否关闭。  如果为关闭。则开启当前设备
//                            SdDeviceData sdDeviceData = new SdDeviceData();
//                            sdDeviceData.setDeviceId(devices.getEqId());
//                            List<SdDeviceData> sdDeviceDataList =   sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
//                            //当前设备开关
//                            String deviceState = null;
//                            //当前设备亮度
//                            String luminance = null;
//
//                            //解析  开关状态与亮度值
//                            for (SdDeviceData sdDeviceDataInfo:sdDeviceDataList) {
//                                //获取开关状态
//                                if(DevicesTypeItemEnum.JQ_LIGHT_OPENCLOSE.getCode() == sdDeviceDataInfo.getItemId()||DevicesTypeItemEnum.JI_BEN_ZHAO_MING_OPENCLOSE.getCode() == sdDeviceDataInfo.getItemId()){
//                                    deviceState = sdDeviceDataInfo.getData();
//                                }else if(DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode() == sdDeviceDataInfo.getItemId()||DevicesTypeItemEnum.JB_LIGHT_BRIGHNESS.getCode() == sdDeviceDataInfo.getItemId()){
//                                    luminance = sdDeviceDataInfo.getData();
//                                }
//                            }
//
//                            String redisLuminanceRangeKey = "control:"+devices.getEqId()+"_LuminanceRange";
//
//                            //缓存获取亮度值  与当前亮度值   与当前亮度值比对。如果相同 忽略当前操作。
//                            Integer num = redisCache.getCacheObject(redisLuminanceRangeKey);
//
//                            if(threadArrs[0]==null|| num == null || num != nowLuminanceRange){
//                                //查看原有亮度是否为null
//                                if(num == null ){
//                                    if(sdDeviceDataList.size()<=0){
//                                        //无亮度 默认给个最小亮度值
//                                        num = sdEnhancedLightingConfig.getMinLuminance();
//                                    }else{
//                                        num = Integer.parseInt(luminance);
//                                    }
//                                    redisCache.setCacheObject(redisLuminanceRangeKey,num);
//                                }
//                                //循环  开启灯光
//                                try{
//                                    //1开启   2关闭
//                                    if("2".equals(deviceState)){
//                                        log.info("当前[{}]设备状态为关闭状态,需要开启照明方可进行调光。",devices.getEqId());
//                                        int flag  = sanJingLight.lineControlAddLog(devices.getEqId(),1,null);
//                                        SdDevicesProtocol devicesProtocol = sdDevicesProtocolService.selectSdDevicesProtocolById(sdDevices.getProtocolId());
//                                        if(devicesProtocol == null){
//                                            continue;
//                                        }
//                                        String className = devicesProtocol.getClassName();
//                                        if(className.contains("sansi")){
//                                            flag = sansiLightImpl.setBrightnessByDevice(devices,nowLuminanceRange,minLuminance,"2");
//                                        }else{
//                                            flag = sanJingLight.setBrightnessByDevice(devices,num,nowLuminanceRange,"2");
//                                        }
//                                        if(flag == 0){
//                                            log.error("当前[{}]加强照明为关闭状态，开启失败。请联系管理员",devices.getEqId());
//                                            continue;
//                                        }
//                                    }
//                                    log.info("开始亮光值:[{}]当前亮度num：{} 根据车流量计算的亮度nowLuminanceRange:{}", devices.getEqId(), num, nowLuminanceRange);
//                                    int flag = sanJingLight.setBrightnessByDevice(devices,num,nowLuminanceRange,"2");
//                                    if(flag == 0){
//                                        log.error("【{}】推送调光指令异常，未能成功发送调光指令",devices.getEqId());
//                                    }
//                                }catch (Exception e){
//                                    log.error("【{}】推送调光指令异常，未能成功发送调光指令",devices.getEqId());
//                                }
//                            }
//                        }
//                    });
//                    try{
//                        //替换线程
//                        Threads.replaceThread(threadArrs,Thread.currentThread());
//                        //等待30秒后 执行 降低 光照强度功能
//                        //降低光照强度执行完毕,推送调光 指令。
//                        Thread.sleep(respondTime);
//                        ThreadPool.executor.execute(() -> {
//                            for (SdDevices devices:deviceIds) {
//                                log.info("结束亮光值:["+ tunnelId +"]当前亮度nowLuminanceRange："+nowLuminanceRange+" 结束推送亮度值" +minLuminance);
//                                SdDevicesProtocol devicesProtocol = sdDevicesProtocolService.selectSdDevicesProtocolById(sdDevices.getProtocolId());
//                                if(devicesProtocol == null){
//                                    continue;
//                                }
//                                String className = devicesProtocol.getClassName();
//                                int flag =0;
//                                if(className.contains("sansi")){
//                                     flag = sansiLightImpl.setBrightnessByDevice(devices,nowLuminanceRange,minLuminance,"2");
//                                }else{
//                                    flag = sanJingLight.setBrightnessByDevice(devices,nowLuminanceRange,minLuminance,"2");
//                                }
////                                int flag = sanJingLight.setBrightnessByDevice(devices,nowLuminanceRange,minLuminance,"2");
//                                if(flag == 0){
//                                    log.error(Thread.currentThread().getName()+"推送调光指令异常，未能成功发送调光指令");
//                                }
//                            }
//                        });
//                        //清除当前记录线程
//                        threadArrs[0] =  null;
//                    }catch (Exception e){
//                        log.error("经过一辆车，当前线程被阻断。");
//                    }
//                });
                break;
            case 2://2节能模式
                for (SdDevices devices:deviceIds) {
                    //定时原有亮度值
                    String redisRegularLuminanceRangeKey = "control_regular:"+devices.getEqId()+"_LuminanceRange";
                    Integer nowLuminanceRange1 = redisCache.getCacheObject(redisRegularLuminanceRangeKey);

                    //获取设备id 下 所有运行状态为已运行的定时任务
                    List<SdStrategyRl> sdStrategyRls = sdStrategyRlMapper.selectSdStrategyREquipments(sdDevices.getEqId());

                    // 获取当前系统时间
                    LocalTime currentTime = LocalTime.now();
                    // 使用Stream筛选出距离当前时间最近且小于当前时间的时间
                    SdStrategyRl sdStrategyRl = sdStrategyRls.stream()
                            .filter(time -> LocalTime.parse(time.getControlTime()).isBefore(currentTime))
                            .max((e1, e2) -> LocalTime.parse(e1.getControlTime()).compareTo(LocalTime.parse(e2.getControlTime())))
                            .orElse(null);

                    if(sdStrategyRl == null || StringUtils.isEmpty(sdStrategyRl.getStateNum())){
                        sdStrategyRl = new SdStrategyRl();
                        sdStrategyRl.setStateNum("30");
                    }

                    if(nowLuminanceRange1 == null || nowLuminanceRange1 != Integer.parseInt(sdStrategyRl.getStateNum())){
                        nowLuminanceRange1 = Integer.parseInt(sdStrategyRl.getStateNum());
                        //后期根据当前  控制策略 定时任务 中  每个段 亮度值进行赋值
                        //设备状态
                        Map param = new HashMap();
                        param.put("state",1);
                        param.put("brightness",nowLuminanceRange1);
                        param.put("devId",devices.getEqId());
                        //设备控制
                        GeneralControlBean generalControlBean = generalControlService.getProtocolBean(devices);
                        if(generalControlBean == null){
                            log.error("设备协议配置为空");
                        }
                        Integer flag = generalControlBean.controlDevices(param);
                        if(flag == 1){
                            redisCache.setCacheObject(redisRegularLuminanceRangeKey,nowLuminanceRange1);
                        }
                    }
                }
                break;
        }
    }
}
