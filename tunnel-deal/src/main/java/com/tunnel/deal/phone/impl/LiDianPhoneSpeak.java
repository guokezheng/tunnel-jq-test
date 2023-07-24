package com.tunnel.deal.phone.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.tunnel.business.datacenter.domain.enumeration.*;
import com.tunnel.business.domain.dataInfo.EventTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdEventType;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.mapper.event.SdEventTypeMapper;
import com.tunnel.deal.phone.LdPhoneSpeak;
import com.tunnel.deal.phone.PhoneSpeak;
import com.zc.common.core.secret.smutil.SecretUtil;
import com.zc.common.core.websocket.WebSocketService;
import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhai
 * @date 2023/6/9
 */
@Component
public class LiDianPhoneSpeak implements LdPhoneSpeak {

    @Autowired
    private SdTunnelsMapper sdTunnelsMapper;

    @Autowired
    private SysDictDataMapper dictMapper;

    @Autowired
    private SdEventTypeMapper eventTypeMapper;

    @Autowired
    private SdEventMapper sdEventMapper;

    @Override
    public int playVoice(Map<String, Object> map, SdDevices sdDevices) {
        /*
         * 向服务器端发送数据
         */
        // 1.定义服务器的地址、端口号、数据
        try {
            InetAddress address = InetAddress.getByName("localhost");
            int port = 6117;//定义端口类型
            //主机地址
            String spkAddr = map.get("spkAddr").toString();
            //组号
            String groupNumber = map.get("groupNumber").toString();
            //文件名称
            String fileName = map.get("fileName").toString();
            //状态
            String status = map.get("status").toString();
            byte[] data= ("@"+spkAddr+"@"+groupNumber+"@"+fileName+status).getBytes();
            //2.创建数据报，包含发送的数据信息
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            //3.创建DatagramSocket对象
            DatagramSocket socket = new DatagramSocket(6116);
            //4.向服务器端发送数据报
            socket.send(packet);
            //5.关闭资源
            socket.close();
            return 1;
        }catch (Exception e){
            e.getMessage();
            return 0;
        }
    }

    /**
     * 新增事件
     * @param tunnelId
     * @param pile
     * @param sdDevices
     */
    public void setEventData(String tunnelId,String pile,SdDevices sdDevices){
        //查询隧道信息
        SdTunnels sdTunnels = sdTunnelsMapper.selectSdTunnelsById(tunnelId);
        //查询方向信息
        String direction = dictMapper.selectDictLabel("sd_direction",sdDevices.getDirection());
        //事件类型信息
        SdEventType sdEventType = eventTypeMapper.selectSdEventTypeById(21L);
        //事件信息
        SdEvent sdEvent = new SdEvent();
        sdEvent.setStakeNum(pile);
        sdEvent.setTunnelId(tunnelId);
        sdEvent.setEventTime(DateUtils.getNowDate());
        sdEvent.setEventTypeId(21L);
        sdEvent.setEventGrade(EventGradeEnum.YI_BAN.getCode());
        sdEvent.setEventSource(EventDescEnum.event_source_phone.getCode());
        sdEvent.setEventTitle(sdTunnels.getTunnelName().concat(",").concat(direction).concat(" ").concat(sdEventType.getEventType()));
        sdEvent.setEventState(EventStateEnum.unprocessed.getCode());
        sdEvent.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,DateUtils.getNowDate()));
        sdEvent.setDirection(sdDevices.getDirection());
        sdEventMapper.insertSdEvent(sdEvent);
        eventSendWeb(sdEvent);
    }

    /**
     * 将事件推送到前端
     *  @param sdEvent
     */
    public void eventSendWeb(SdEvent sdEvent){
        //新增后再查询数据库，返回给前端事件图标等字段
        SdEvent sdEventData = new SdEvent();
        sdEventData.setId(sdEvent.getId());
        List<SdEvent> sdEventList = sdEventMapper.selectSdEventList(sdEventData);
        //新增事件后推送前端  弹出视频
        JSONObject object = new JSONObject();
        object.put("sdEventList", sdEventList);
        WebSocketService.broadcast("sdEventList",object.toString());
    }

//    @PostConstruct
//    public void init() {
//        runThread();
//    }

    public void runThread() {
        new Thread() {
            public void run() {
                DatagramSocket socket = null;
                try{
                    socket = new DatagramSocket(6116);
                    while(true){
                        /*
                         * 接收服务器端响应的数据
                         */
                        //1.创建数据报，用于接收服务器端响应的数据
                        byte[] data2 = new byte[1024];
                        DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
                        //2.接收服务器响应的数据
                        socket.receive(packet2);
                        //3.读取数据
                        String reply = new String(data2, 0, packet2.getLength());

                        String str = reply.replaceAll("\\*", "").replaceAll("&", "");
                        System.out.println("接收到数据：" + str);
                        List<String> list = Arrays.asList(str.split("@"));
                        String insturct = list.get(2);
                        String offOrOn = list.get(3);

                        //新增事件
                        if(PhoneSpkEnum.INSTRUCT.getCode().equals(insturct) && PhoneSpkEnum.INSTRUCT_OFF.getCode().equals(offOrOn)){
                            /*SdDevices sdDevices = new SdDevices();
                            sdDevices.setEqStatus("4");
                            sdDevices.setEqId(device.getEqId());
                            sdDevicesMapper.updateSdDevices(sdDevices);
                            deviceDataService.updateDeviceData(device, data, Long.valueOf(itemId));*/
                            //setEventData(map.get("tunnelId").toString(),list.get(1),sdDevices);
                        }
                    }
                }catch (Exception e){
                    socket.close();
                    e.printStackTrace();
                }
            }
        }.start();
        /*DatagramSocket socket = null;
        try{
            socket = new DatagramSocket(6116);
            while(true){
                *//*
                 * 接收服务器端响应的数据
                 *//*
                //1.创建数据报，用于接收服务器端响应的数据
                byte[] data2 = new byte[1024];
                DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
                //2.接收服务器响应的数据
                socket.receive(packet2);
                //3.读取数据
                String reply = new String(data2, 0, packet2.getLength());

                String str = reply.replaceAll("\\*", "").replaceAll("&", "");
                List<String> list = Arrays.asList(str.split("@"));
                String insturct = list.get(2);
                String offOrOn = list.get(3);
                socket.close();
            }
        }catch (Exception e){
            socket.close();
            e.printStackTrace();
        }*/
    }
}
