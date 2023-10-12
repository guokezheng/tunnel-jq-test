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
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.mapper.event.SdEventTypeMapper;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.deal.phone.LdPhoneSpeak;
import com.tunnel.deal.phone.PhoneSpeak;
import com.zc.common.core.secret.smutil.SecretUtil;
import com.zc.common.core.websocket.WebSocketService;
import org.apache.catalina.manager.util.SessionUtils;
import org.apache.commons.codec.binary.Hex;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    @Autowired
    private ISdTunnelsService tunnelsService;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    private DatagramSocket socket;

    public void socketInfo(){
        try {
            socket = new DatagramSocket(6116);
        } catch (SocketException e) {
            e.getMessage();
        }
    }

    @Override
    public int playVoice(Map<String, Object> map, SdDevices sdDevices) {
        /*
         * 向服务器端发送数据
         */
        // 1.定义服务器的地址、端口号、数据
        try {
            List<String> devices = (List<String>) map.get("spkDeviceIds");
            SdDevices sdDevices1 = sdDevicesMapper.selectSdDevicesById(devices.get(0));
            String eqDirection = sdDevices1.getEqDirection();
            InetAddress address = InetAddress.getByName(map.get("systemUrl").toString());
            int port = 6117;//定义端口类型
            //主机地址
            String spkAddr = "1";
            //组号
            String groupNumber = null;
            if("1".equals(eqDirection)){
                groupNumber = "2";
            }else {
                groupNumber = "1";
            }
            //文件名称
            List<String> fileNames = (List<String>) map.get("fileNames");
            //状态
            String status = map.get("loopStatus").toString();
            String file = fileNames.get(0);
            String ajsj = "@"+spkAddr+"@"+groupNumber+"@"+file+status;
            char[] chars = ajsj.toCharArray();
            //String hexString = "";
            List<Integer> list = new ArrayList<>();
            for(int i = 0; i < chars.length; i++){
                list.add((int) chars[i]);
                list.add(0);
                //hexString += Integer.toHexString((int) chars[i]) + " 00 ";
            }
            byte[] data= new byte[list.size()];
            for(int i = 0; i < list.size(); i++){
                data[i] = (byte)(int)list.get(i);
            }
            //2.创建数据报，包含发送的数据信息
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            //4.向服务器端发送数据报
            socket.send(packet);
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
        String direction = dictMapper.selectDictLabel("sd_direction",sdDevices.getEqDirection());
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
        sdEvent.setDirection(sdDevices.getEqDirection());
        sdEvent.setStakeNum(sdDevices.getPile());
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
        sdEventList.stream().forEach(item -> item.setIds(item.getId().toString()));
        JSONObject object = new JSONObject();
        object.put("sdEventList", sdEventList);
        WebSocketService.broadcast("sdEventList",object.toString());
    }

    @PostConstruct
    public void init() {
        socketInfo();
        runThread();
    }

    public void runThread() {
        new Thread() {
            public void run() {
                try{
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
                        String pile = list.get(1);
                        SdDevices device = new SdDevices();
                        //device.setPile(pile);
                        device.setEqType(DevicesTypeEnum.ET.getCode());
                        device.setExternalDeviceId(list.get(0));
                        SdDevices etDeviceData = sdDevicesMapper.getEtDeviceData(device);
                        //新增事件
                        if(PhoneSpkEnum.INSTRUCT.getCode().equals(insturct) && PhoneSpkEnum.INSTRUCT_OFF.getCode().equals(offOrOn)){
                            SdDevices sdDevices = new SdDevices();
                            sdDevices.setEqStatus("4");
                            sdDevices.setEqId(etDeviceData.getEqId());
                            sdDevicesMapper.updateSdDevices(sdDevices);
                            setEventData(etDeviceData.getEqTunnelId(),pile,etDeviceData);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
