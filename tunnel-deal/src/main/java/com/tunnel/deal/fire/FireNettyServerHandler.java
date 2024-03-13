package com.tunnel.deal.fire;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.electromechanicalPatrol.SdFaultList;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdEventType;
import com.tunnel.business.mapper.dataInfo.ExternalSystemMapper;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.electromechanicalPatrol.SdFaultListMapper;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.mapper.event.SdEventTypeMapper;
import com.tunnel.business.service.digitalmodel.RadarEventService;
import com.tunnel.business.service.electromechanicalPatrol.ISdFaultListService;
import com.tunnel.business.service.sendDataToKafka.SendDeviceStatusToKafkaService;
import com.tunnel.business.utils.util.UUIDUtil;
import com.tunnel.deal.xiaofangpao.msgEnum.DevicesDirectionCodeEnum;
import com.zc.common.core.websocket.WebSocketService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Title: FireNettyServerHandler
 * Description:  消防数据接收
 */
@ChannelHandler.Sharable
public class FireNettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(FireNettyServerHandler.class);
    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static SdDevicesMapper sdDevicesMapper = SpringUtils.getBean(SdDevicesMapper.class);
    private static RadarEventService radarEventService = SpringUtils.getBean(RadarEventService.class);
    private static SdEventTypeMapper sdEventTypeMapper = SpringUtils.getBean(SdEventTypeMapper.class);
    private static SdEventMapper sdEventMapper = SpringUtils.getBean(SdEventMapper.class);
    private static SdDeviceDataMapper sdDeviceDataMapper = SpringUtils.getBean(SdDeviceDataMapper.class);
    private static SendDeviceStatusToKafkaService sendData = SpringUtils.getBean(SendDeviceStatusToKafkaService.class);
    private static ExternalSystemMapper externalSystemMapper = SpringUtils.getBean(ExternalSystemMapper.class);

    private static SdFaultListMapper sdFaultListMapper = SpringUtils.getBean(SdFaultListMapper.class);

    private static ISdFaultListService sdFaultListService = SpringUtils.getBean(ISdFaultListService.class);

    private EventLoopGroup group;

    /**
     * 消防主机协议解析
     *
     * @param data
     * @param clientIp
     */
    private static void protocolAnalysis(String data, String clientIp) {

        try {


            // 主机地址
            if ("".equals(data) || data == null) {
                return;
            }
            if (!data.contains("火警") && !data.contains("模块或探头故障") && !data.contains("模块或探头恢复") && !data.contains("启动")
                    && !data.contains("全部声光启动") && !data.contains("全部声光停止") && !data.contains("控制器复位")) {
                return;
            }

            int state = 1;

            if(data.contains("火警")) {
                state = 4;
            }
            if(data.contains("模块或探头故障") || data.contains("模块或探头恢复")) {
                state = 3;
            }

            //拿到的报文就是纯文字的报文，直接进行解析
            //传输格式例：火警: 1号机1回路3地址 点型感烟   001层 西核彩桥F102     2022-12-27 08:42:18
            //         事件:+空格+机号+回路号+地址号+空格+设备类型+空格+层号+空格+地理位置+空格+年月日+空格+时分秒


            // 回路  左右洞？
            //
            if (data.contains(":")) {
                String alarmType = data.substring(0, data.indexOf(":"));
                log.info("alarmType:" + alarmType);
                data = data.substring(data.indexOf(":") + 1);
                String host = data.substring(0, data.indexOf("号"));
                //查询外部系统ID
                ExternalSystem externalSystem = new ExternalSystem();
                externalSystem.setSystemName("火灾报警系统");
               // externalSystem.setSystemUrl(clientIp);
                List<ExternalSystem> externalSystems = externalSystemMapper.selectExternalSystemList(externalSystem);
                if (externalSystems.isEmpty()) {
                    return;
                }
                ExternalSystem system = externalSystems.get(0);

                if (data.contains("恢复") || data.contains("全部声光停止") || data.contains("控制器复位")) {
                    log.info("主机{}进行了复位，事件类型为{}", system.getSystemUrl(), alarmType);
                    //复位清除设备报警状态
                    SdDevices dev = new SdDevices();
                    dev.setExternalSystemId(system.getId());

                    // 指定控制器进行复位
                    if(alarmType.contains("控制器复位")){
                        dev.setQueryPointAddress(host);
                    }

                    List<SdDevices> fireComponentsList = sdDevicesMapper.selectSdDevicesList(dev);
                    SdDeviceData sdDeviceData = new SdDeviceData();
                    for (int j = 0;j < fireComponentsList.size();j++) {
                        String eqId = fireComponentsList.get(j).getEqId();
                        sdDeviceData.setDeviceId(eqId);
                        List<SdDeviceData> dataList = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
                        for (int s = 0;s < dataList.size();s++) {
                            SdDeviceData deviceData = dataList.get(s);
                            deviceData.setData("0");
                            deviceData.setUpdateTime(new Date());
                            sdDeviceDataMapper.updateSdDeviceData(deviceData);
                           // sendData.pushDevicesDataNowTime(deviceData);
                        }
                    }
                    //复位清除预警
//                    SdEvent sdEvent = new SdEvent();
//                    sdEvent.setEventTypeId(20L);
//                    List<SdEvent> sdEvents = sdEventMapper.selectSdEventList(sdEvent);
//                    for (int i = 0; i < sdEvents.size(); i++) {
//                        SdEvent event = sdEvents.get(i);
//                        event.setEventState("1");
//                        event.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,DateUtils.getNowDate()));
//                        event.setUpdateTime(new Date());
//                        sdEventMapper.updateSdEvent(event);
//                    }
                   // 模块或探头故障: 5号机2回路87地址 声光   青风岭YK66+850右 2023-10-13 10:36:02
//                sdEvent.setEventTypeId(102L);
//                sdEvents = sdEventMapper.selectSdEventList(sdEvent);
//                for (int i = 0; i < sdEvents.size(); i++) {
//                    SdEvent event = sdEvents.get(i);
//                    event.setEventState("1");
//                    event.setEndTime(new Date().toString());
//                    sdEventMapper.updateSdEvent(event);
//                }
                }

                Long systemId = system.getId();
      /*      String address = data.substring(0, data.indexOf("号"));
            data = data.substring(data.indexOf("机") + 1);
            // 左洞1 右洞2
            String loop = data.substring(0, data.indexOf("回"));
            data = data.substring(data.indexOf("路") + 1);
            // 设备编码
            data = data.substring(data.indexOf("址") + 2);
            String sourceDevice = data.substring(0, data.indexOf(" "));*/
                //模块或探头故障: 5号机2回路87地址 声光   青风岭YK66+850右 2023-10-13 10:36:02
              //  String address = data.substring(0, data.indexOf("号"));
                data = data.substring(data.indexOf("机") + 1);
                // 左洞1 右洞2
                String loop = data.substring(0, data.indexOf("回"));
                data = data.substring(data.indexOf("路") + 1);

                String sn = data.substring(0,data.indexOf("号地"));
                data = data.substring(data.indexOf("址") + 2);
                // 设备编码
                //data = data.substring(data.indexOf("址") + 2);
                String sourceDevice = data.substring(0, data.indexOf("  "));

                //剩层号后的内容
//            data = data.substring(data.indexOf(" ") + 1);
                //剩地理位置后的内容
//            data = data.substring(data.indexOf(" ") + 1);
                String alarmTime = data.substring(data.length() - 19);
                Date now = new Date();
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    now = format.parse(alarmTime);
                } catch (Exception e) {
                    log.error("火灾报警日期转换出现异常：" + e.getMessage());
                }
                //根据火灾报警主机外部系统ID和回路确定是哪一批设备
                SdDevices devices = new SdDevices();
                devices.setExternalSystemId(systemId);
                devices.setExternalDeviceId(sn);
                devices.setQueryPointAddress(host);


                // 火灾报警  1左洞 2右洞
                // 隧道平台  1右洞 2左洞
                if(loop.equals("2")){
                    devices.setEqDirection("1");
                }else{
                    devices.setEqDirection("2");
                }
                List<SdDevices> devicesLists = sdDevicesMapper.selectSdDevicesList(devices);

                // 火灾报警 设备匹配
                if(devicesLists == null || devicesLists.size() == 0){
                    log.error("火灾报警externalDeviceId未找到：" + sn);
                }
                // 更新设备状态
                devices.setExternalDeviceId(sn);
                devices.setEqStatus(state+"");
                devices.setEqStatusTime(new Date());
                devices.setQueryPointAddress(host);
                sdDevicesMapper.updateSdDevicesByExternalDevIdAndDirection(devices);


                SdDevices sdDevices = devicesLists.get(0);
//            //遍历确定是哪个部件
//            String alarmComponentEqId = "";
//            SdDevices sdDevices = new SdDevices();
//            for (int i = 0; i < devicesLists.size(); i++) {
//                SdDevices component = devicesLists.get(i);
//                String eqFeedbackAddress1 = component.getQueryPointAddress();
//                if (eqFeedbackAddress1.equals(address)) {
//                    log.info("找到部件");
//                    alarmComponentEqId = component.getEqId();
//                    sdDevices = component;
//                    break;
//                }
//            }
                //确定报警类型
                Long itemId = 0L;
                //查询当前属于什么事件类型
                SdEventType sdEventType = new SdEventType();
                sdEventType.setEventType("火灾");
                if (sourceDevice.equals("手报")) {
                    sourceDevice = "智能手动报警按钮报警";
                    itemId = Long.valueOf(DevicesTypeItemEnum.SHOU_BAO_ALARM.getCode());
                } else if (sourceDevice.equals("探测器")) {
                    sourceDevice = "火焰探测器报警";
                    itemId = Long.valueOf(DevicesTypeItemEnum.FLAME_DETECTOR_ALARM.getCode());
                } else if (sourceDevice.equals("声光")) {
                    sourceDevice = "声光报警器报警";
                    itemId = Long.valueOf(DevicesTypeItemEnum.SHENG_GUANG_ALARM.getCode());
                } else if (sourceDevice.equals("点型感温")) {
                    sourceDevice = "智能感温探测器";
                    itemId = Long.valueOf(DevicesTypeItemEnum.ZHI_NENG_GAN_WEN_TAN_CE_QI.getCode());
                } else if (sourceDevice.equals("点型感烟")) {
                    sourceDevice = "智能感烟探测器";
                    itemId = Long.valueOf(DevicesTypeItemEnum.ZHI_NENG_GAN_YAN_TAN_CE_QI.getCode());
                }
                Long eventTypeId = 0L;
                if (sdEventType.getEventType() != null) {
                    List<SdEventType> sdEventTypes = sdEventTypeMapper.selectSdEventTypeList(sdEventType);
                    eventTypeId = sdEventTypes.get(0).getId();
                }
                if (eventTypeId == 0) {
                    return;
                }
                //事件相关的设备要把数据更新到device_data中
                SdDeviceData sdDeviceData = new SdDeviceData();
                sdDeviceData.setDeviceId(sdDevices.getEqId());
                sdDeviceData.setItemId(itemId);



                List<SdDeviceData> deviceData = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
                if (deviceData.isEmpty()) {
                    sdDeviceData.setData(state+"");
                    sdDeviceData.setCreateTime(new Date());
                    sdDeviceDataMapper.insertSdDeviceData(sdDeviceData);
                   // sendData.pushDevicesDataNowTime(sdDeviceData);
                } else {
                    SdDeviceData devData = deviceData.get(0);
                    devData.setUpdateTime(new Date());
                    sdDeviceDataMapper.updateSdDeviceData(devData);
                   // sendData.pushDevicesDataNowTime(devData);
                }

                // 设备故障
                if (alarmType.contains("故障")) {
                    String direction="";
                    if(sdDevices.getEqDirection().equals(DevicesDirectionCodeEnum.JINANFANGXIANG.getCode())){
                        direction = DevicesDirectionCodeEnum.JINANFANGXIANG.getName()+sdDevices.getPile();
                    }else{
                        direction = DevicesDirectionCodeEnum.WEIFANGFANGXIANG.getName()+sdDevices.getPile();
                    }

                    // 查询 方向+桩号拼接 故障信息
                    List<Map> faultList = sdFaultListMapper.selectSdFaultEqByDirection(direction,sdDevices.getEqId());

                    // 不存在，则插入故障数据。已存在，不在插入
                    if(faultList.size() == 0) {

                        //故障位置处理
                        //存 故障清单表sd_fault_list
                        SdFaultList sdFaultList = new SdFaultList();
                        sdFaultList.setId(UUIDUtil.getRandom32BeginTimePK());
                        sdFaultList.setFaultTbtime(DateUtils.getNowDate());//故障填报时间
                        sdFaultList.setCreateTime(DateUtils.getNowDate());// 创建时间
                        sdFaultList.setTunnelId(sdDevices.getEqTunnelId());//隧道
                        sdFaultList.setFaultLocation(direction);//设备位置   方向+桩号拼接
                        sdFaultList.setFaultType("6");//故障类型  其他
                        sdFaultList.setFaultEscalationType("1");//故障来源  系统上报
                        sdFaultList.setFaultFxtime(DateUtils.getNowDate());//故障发现时间
                        sdFaultList.setEqId(sdDevices.getEqId());//设备id
                        sdFaultList.setEqStatus("3");//设备状态  故障
                        sdFaultList.setFaultLevel("0");//故障等级  一般
                        sdFaultList.setFalltRemoveStatue("1");//故障消除状态  未消除
                        sdFaultList.setFaultStatus("0");//故障状态  已发布
                        sdFaultListMapper.insertSdFaultList(sdFaultList);
                        //    sdFaultListService.faultSendWeb(sdFaultList);//故障推送
                    }
                }

                // 设备故障恢复
                if (alarmType.contains("恢复")) {
                    String direction="";
                    if(sdDevices.getEqDirection().equals(DevicesDirectionCodeEnum.JINANFANGXIANG.getCode())){
                        direction = DevicesDirectionCodeEnum.JINANFANGXIANG.getName()+sdDevices.getPile();
                    }else{
                        direction = DevicesDirectionCodeEnum.WEIFANGFANGXIANG.getName()+sdDevices.getPile();
                    }
                    //设备故障从未消除 变成 已消除
                    sdFaultListMapper.updateFalltRemoveStatueSuccess(direction,sdDevices.getEqId());
                }

                // 火警产生事件
                if (alarmType.contains("火警")) {
                    //查询配置提示音事件类型
                    SdEventType eventType = new SdEventType();
                    eventType.setIsUsable("1");
                    eventType.setIsConfig("1");
                    List<SdEventType> typeList = sdEventTypeMapper.selectSdEventTypeList(eventType);
                    //存储事件到事件表
                    SdEvent sdEvent = new SdEvent();
                    sdEvent.setTunnelId(sdDevices.getEqTunnelId());
                    sdEvent.setEventTypeId(eventTypeId);
                    sdEvent.setEventTitle(sourceDevice + "，火灾报警事件");
                    sdEvent.setEventSource("3");
                    sdEvent.setStartTime(DateUtils.getTime());
                    //要改
                    sdEvent.setEventState("3");
                    //事件描述
                    sdEvent.setEventDescription(alarmType);
                    sdEvent.setStakeNum(sdDevices.getPile());
                    if(sdEvent.getStakeNum().contains("ZK")){
                        sdEvent.setDirection("2");
                    }else{
                        sdEvent.setDirection("1");
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String time = formatter.format(new Date());
                    sdEvent.setEventTime(dateZh(time));
                    sdEvent.setCreateTime(dateZh(time));
                    sdEventMapper.insertSdEvent(sdEvent);
                    for(SdEventType item : typeList){
                        if(sdEvent.getEventTypeId() == item.getId()){
                            eventAudio();
                        }
                    }
                    eventSendWeb(sdEvent);
                }
            } else {
                log.error("当前报文格式异常，请检查设备！");
            }
        }catch (Exception e){
            log.error("当前报文格式异常，请检查设备！");
        }
    }

    /**
     * 提示音
     */
    public static void eventAudio(){
        try {
            /*File file = new File("/home/tunnel/video/ding.WAV");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();*/
            WebSocketService.broadcast("playvideo",1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 将事件推送到前端
     *  @param sdEvent
     */
    public static void eventSendWeb(SdEvent sdEvent){
        //新增后再查询数据库，返回给前端事件图标等字段
        SdEvent sdEventData = new SdEvent();
        sdEventData.setId(sdEvent.getId());
        List<SdEvent> sdEventList = sdEventMapper.selectSdEventList(sdEventData);
        //新增事件后推送前端  弹出视频
        JSONObject object = new JSONObject();
        object.put("sdEventList", sdEventList);
        WebSocketService.broadcast("sdEventList",object.toString());
    }

    /*
     * 收到消息时，返回信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        InetSocketAddress ipSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = ipSocket.getAddress().getHostAddress();
        log.info("客户端ip地址：{}", clientIp);
        ByteBuf btf = (ByteBuf) msg;
        String fireAlarmData = btf.toString(Charset.forName("GBK"));
//        String fireAlarmData = msg.toString();
        //先进行判空操作
        if (null == fireAlarmData || fireAlarmData.isEmpty()) {
            log.error("收到的消息为空！");
            return;
        }
        log.error("收到火灾报警消息：" + fireAlarmData);
        protocolAnalysis(fireAlarmData.replaceAll("活", ""), clientIp);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /*
     * 建立连接时，返回消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        try {
            System.out.println("address:" + ctx.channel().remoteAddress());
            String remoteAddress = ctx.channel().remoteAddress().toString();
            remoteAddress = remoteAddress.substring(1, remoteAddress.indexOf(":"));
            //获取到客户端IP，根据客户端IP查询需要更改状态的主机设备信息
            changeFireAlarmHostAndComponentStatus(remoteAddress, DevicesStatusEnum.DEVICE_ON_LINE.getCode(), "0");

            ctx.writeAndFlush("client" + InetAddress.getLocalHost().getHostName() + "connect！ \n");
            clients.add(ctx.channel());
            super.channelActive(ctx);
        }catch (Exception e){
            log.info("建立连接时失败了");
        }

    }


    /**
     * 当 Channel 处于非活动状态（已经断开连接）时调用。
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        //断开链接的IP
        String remoteAddress = ctx.channel().remoteAddress().toString();
        remoteAddress = remoteAddress.substring(1, remoteAddress.indexOf(":"));
        changeFireAlarmHostAndComponentStatus(remoteAddress, DevicesStatusEnum.DEVICE_OFF_LINE.getCode(), "1");
        ctx.flush();
        ctx.disconnect();
    }

    /**
     * 通道读取完成
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
//        //断开链接的IP
//        String remoteAddress = ctx.channel().remoteAddress().toString();
//        remoteAddress = remoteAddress.substring(1, remoteAddress.indexOf(":"));
//        changeFireAlarmHostAndComponentStatus(remoteAddress, DevicesStatusEnum.DEVICE_OFF_LINE.getCode(), "1");
        ctx.flush();
    }

    private void changeFireAlarmHostAndComponentStatus(String ip, String status, String isAlarm) {
        //火灾报警主机已经迁移到外部系统表，通过此处修改
        ExternalSystem externalSystem = new ExternalSystem();
        externalSystem.setSystemName("火灾报警系统");
        externalSystem.setSystemUrl(ip);
        List<ExternalSystem> systemList = externalSystemMapper.selectExternalSystemList(externalSystem);
        if (systemList.isEmpty()) {
            return;
        }
        ExternalSystem system = systemList.get(0);
//        SdDevices hostDevice = new SdDevices();
        SdDevices fireComponentDevice = new SdDevices();
//        hostDevice.setIp(ip);
//        Long fireHostEqTypeId = Long.parseLong(String.valueOf(DevicesTypeEnum.FIRE_ALARM_HOST.getCode()));
//        hostDevice.setEqType(fireHostEqTypeId);
//        List<SdDevices> devicesList = sdDevicesMapper.selectSdDevicesList(hostDevice);
//        for (int i = 0; i < devicesList.size(); i++) {
//            SdDevices devices = devicesList.get(i);
//            devices.setEqStatus(status);
//            devices.setEqStatusTime(new Date());
//            sdDevicesMapper.updateSdDevices(devices);
        system.setNetworkStatus(status);
        system.setUpdateTime(new Date());
        externalSystemMapper.updateExternalSystem(system);
//            if (status.equals(DevicesStatusEnum.DEVICE_OFF_LINE.getCode())) {
//                sendData.pushDevicesStatusToOtherSystem(devices, "1", "off");
//            } else if (status.equals(DevicesStatusEnum.DEVICE_ON_LINE.getCode())) {
//                sendData.pushDevicesStatusToOtherSystem(devices, "1", "on");
//            }
//            String feqId = devices.getEqId();
//            fireComponentDevice.setFEqId(feqId);
        Long systemId = system.getId();
        fireComponentDevice.setExternalSystemId(systemId);
        fireComponentDevice.setEqStatus(status);
        fireComponentDevice.setEqStatusTime(new Date());
//            sdDevicesMapper.updateSdDevicesByFEqId(fireComponentDevice);
        sdDevicesMapper.updateSdDevicesByExternalSystemId(fireComponentDevice);
        //等推送内容确定后再修改
//            if (status.equals(DevicesStatusEnum.DEVICE_OFF_LINE.getCode())) {
//                sendData.pushDevicesStatusToOtherSystem(fireComponentDevice, "2", "off");
//            } else if (status.equals(DevicesStatusEnum.DEVICE_ON_LINE.getCode())) {
//                sendData.pushDevicesStatusToOtherSystem(fireComponentDevice, "2", "on");
//            }

        List<SdDevices> fireComponentsList = sdDevicesMapper.selectDevicesListByExternalSystemId(fireComponentDevice);
        for (int j = 0; j < fireComponentsList.size(); j++) {
            SdDevices fireComponent = fireComponentsList.get(j);
            if ("1".equals(isAlarm)) {
                //当前主机已经断开链接，没有必要存储无用的设备状态信息
                SdDeviceData data = new SdDeviceData();
                data.setDeviceId(fireComponent.getEqId());
                List<SdDeviceData> sdDeviceData = sdDeviceDataMapper.selectSdDeviceDataList(data);
                for (int z = 0;z < sdDeviceData.size();z++) {
                    Long id = sdDeviceData.get(z).getId();
                    sdDeviceDataMapper.deleteSdDeviceDataById(id);
                }
            } else {
                //添加正常数据到device_data
                if (fireComponent.getEqType().longValue() == DevicesTypeEnum.SHENG_GUANG_BAO_JING.getCode().longValue()) {
                    saveDataIntoSdDeviceData(fireComponent, isAlarm, DevicesTypeItemEnum.SHENG_GUANG_ALARM.getCode());
                } else if (fireComponent.getEqType().longValue() == DevicesTypeEnum.SHOU_BAO.getCode().longValue()) {
                    saveDataIntoSdDeviceData(fireComponent, isAlarm, DevicesTypeItemEnum.SHOU_BAO_ALARM.getCode());
                } else if (fireComponent.getEqType().longValue() == DevicesTypeEnum.HUO_YAN_TAN_CE_QI.getCode().longValue()) {
                    saveDataIntoSdDeviceData(fireComponent, isAlarm, DevicesTypeItemEnum.FLAME_DETECTOR_ALARM.getCode());
                }
            }
            //声光报警器数据推送到万基
//                Map<String, Object> map = new HashMap<>();
//                map.put("deviceId", fireComponent.getEqId());
//                map.put("deviceType", fireComponent.getEqType());
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("runStatus", status);
//                jsonObject.put("alarmSource", "0");
//                map.put("deviceData", jsonObject);
//                radarEventService.sendBaseDeviceStatus(map);
        }
//        }
    }

    private void saveDataIntoSdDeviceData(SdDevices sdDevices, String value, Integer itemId) {
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        sdDeviceData.setItemId(Long.valueOf(itemId));
        List<SdDeviceData> deviceData = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
        if (deviceData.size() > 0) {
            SdDeviceData data = deviceData.get(0);
            data.setData(value);
            data.setUpdateTime(new Date());
            sdDeviceDataMapper.updateSdDeviceData(data);
          //  sendData.pushDevicesDataNowTime(data);
        } else {
            sdDeviceData.setData(value);
            sdDeviceData.setCreateTime(new Date());
            sdDeviceDataMapper.insertSdDeviceData(sdDeviceData);
         //   sendData.pushDevicesDataNowTime(sdDeviceData);
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
    public static Date dateZh(String timeData){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换
        Date time = null;
        try {
            if(timeData != null && !"".equals(timeData)){
                time = sdf.parse(timeData);
                return time;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}
