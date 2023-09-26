package com.tunnel.deal.tcp.plc.omron;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.ParameterException;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicePointControlTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DeviceStateTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceDataRecord;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.protocol.SdDevicePointPlc;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataRecordMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.protocol.ISdDevicePointPlcService;
import com.tunnel.deal.tcp.client.config.ChannelKey;
import com.tunnel.deal.tcp.client.netty.TcpNettySocketClient;
import com.tunnel.deal.tcp.modbus.ModbusCmdResolver;
import com.tunnel.deal.tcp.plc.omron.fins.*;
import com.tunnel.deal.tcp.plc.omron.task.OmronFinsTask;
import com.tunnel.deal.tcp.util.ByteBufUtil;
import com.zc.common.core.ThreadPool.ThreadPool;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static com.tunnel.deal.tcp.plc.omron.fins.FinsCmdValues.*;
import static com.tunnel.deal.tcp.plc.omron.task.OmronFinsTask.commandLock;

/**
 * @author zhai
 * @date 2023/9/20
 */
@Component
public class OmronFinsControlProcession {

    //定义先进先出队列
    public static Queue<Map<String, Object>> queue = new LinkedBlockingQueue<>();

    @Autowired
    private ISdDevicePointPlcService devicePointPlcService;

    @Value("${plc.server}")
    private String plcServer;

    @Autowired
    private FinsCmd finsCmd;

    @Autowired
    private ISdDevicesService devicesService;

    @Autowired
    private ISdDeviceDataService sdDeviceDataService;

    OmronFinsControlProcession(){
        OmronHandler omronHandler = new OmronHandler();
        Thread thread = new Thread(omronHandler);
        thread.start();
    }

    //发送线程
    class OmronHandler implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (queue.isEmpty()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }

                Map<String, Object> map = queue.poll();
                try {
                    omRun((SdDevices) map.get("sdDevices"),map.get("state").toString(),map.get("type").toString());
                }catch (Exception e){
                    e.getMessage();
                }

            }
        }
    }

    public void omRun(SdDevices devices, String state, String type){
        // 处理写入、查询指令
        if("2".equals(type)) {
            // 单个写入
            OmWrite(devices,state);
        }else if("1".equals(type)){
            // 读取
            OmRead();
        }
    }

    public static String UdpClient(String ipAddress, Integer port, String message) {
        String receiveStr = "";
        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            // 设置超时时间
            datagramSocket.setSoTimeout(5000);
            byte[] buf = Hex.decodeHex(message.toCharArray());
            InetAddress address = InetAddress.getByName(ipAddress);
            // 封装一个数据包
            int length1 = buf.length;
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, address, port);
            datagramSocket.send(datagramPacket);
            // 接收返回数据
            byte[] receiveBuf = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuf, receiveBuf.length);
            datagramSocket.receive(receivePacket);
            int length = receivePacket.getLength();
            byte[] bytes = receivePacket.getData();
            receiveStr = byteToArray(bytes, length);
            datagramSocket.close();
        } catch (Exception e) {
            throw new ParameterException(e.getMessage());
        }
        return receiveStr;
    }

    public static String byteToArray(byte[]data, int length){
        String result="";
        for (int i = 0; i < length; i++) {
            result+=Integer.toHexString((data[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3);
        }
        return result;
    }


    public AjaxResult OmWrite(SdDevices sdDevices, String state) {
        ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);
        SdDevices fDevice = sdDevicesService.selectSdDevicesById(sdDevices.getfEqId());
        String ip = fDevice.getIp();
        String port = fDevice.getPort();
        Integer portNum = Integer.valueOf(port);

        //点位类型：控制点位
        Long pointType = DevicePointControlTypeEnum.control_enable.getCode();
        //数据状态
        String runState = DeviceStateTypeEnum.data_status.getCode();
        String eqId = sdDevices.getEqId();
        //查询设备点位
        SdDevicePointPlc devicePointPlc = new SdDevicePointPlc();
        devicePointPlc.setEqId(eqId);
        devicePointPlc.setIsReserved(pointType);
        List<SdDevicePointPlc> devicePointList = devicePointPlcService.selectSdDevicePointPlcList(devicePointPlc);
        if(devicePointList == null || devicePointList.size() == 0){
            return AjaxResult.error("未配置设备点位");
        }
        devicePointPlc = devicePointList.get(0);

        String pointConfig = devicePointPlc.getPointConfig();
        JSONObject jsonConfig = JSONObject.parseObject(pointConfig);
        //点位状态
        String stateStr = jsonConfig.getString("stateConfig");
        JSONArray jsonArray = JSONArray.parseArray(stateStr);
        //状态匹配
        JSONObject stateJson = new JSONObject();
        for(Object obj : jsonArray){
            JSONObject jsonObject = (JSONObject) obj;
            String stateConfig = jsonObject.getString("state");
            if(state.equals(stateConfig)){
                stateJson = jsonObject;
                break;
            }
        }

        String controlState = stateJson.getString("value");
        if(controlState == null){
            return AjaxResult.error("设备点位配置不完整");
        }

        //存储分区代码
        String area = jsonConfig.getString("area");
        //写入地址
        String address = jsonConfig.getString("address");
        String bitAddress = jsonConfig.getString("bitAddress");
        String writeLength = jsonConfig.getString("writeLength");
        //默认操作失败
        AjaxResult ajaxResult = AjaxResult.success();
        //源地址
        String sourceAddress = plcServer;
        //目的地址
        String destinationAddress = ip;
        //发送指令
        String command = FinsCmdGenerator.getUdpControlCommand(sourceAddress,destinationAddress,area,address,bitAddress,writeLength,controlState);
        try {
            String s = UdpClient(ip, portNum, command);
            JSONObject jsonObject = udpCommandParse(s);
        } catch (Exception e) {
            e.printStackTrace();
            //  报错判定设备离线，将网关设备及子设备设置为离线
//                    sdDevicesService.updateOfflineStatus(deviceId,true);
            return AjaxResult.error("设备指令发送报错");
        }
        return ajaxResult;
    }

    public AjaxResult OmRead(){
        //拿到缓存设备数据-测;控执行器
        Map<String, Map> deviceMap = OmronFinsTask.deviceMap;
        //点位类型：只读点位
        Long pointType = DevicePointControlTypeEnum.only_read.getCode();

        //查询具体的子设备（温湿度传感器、交通信号灯、车指反馈点位），拿到点位，查询is_reserved = 1只读点位
        //取点位最大、最小的点位下发查询指令
        //提供一个多点位查询的方法，根据现场点位的规律实现多点位读取

        //父设备ID列表
        List<String> fEqIdList = new ArrayList<>(deviceMap.keySet());
        if(fEqIdList.size() == 0){
            return AjaxResult.error();
        }
        /*List<String> fEqIdList = new ArrayList<>();
        fEqIdList.add("JQ-WeiFang-MiaoZi-BJY-PLC-002");*/

//        sendSinglePointCmd(fEqIdList,pointType);

        //D区
        sendMultiplePointCmd(fEqIdList,pointType,D_AREA_CODE);
        sleep(50);
        //W字代码
        sendMultiplePointCmd(fEqIdList,pointType,W_WORD_AREA_CODE);
        //W位代码
        sendSinglePointCmd(fEqIdList,pointType,W_BIT_AREA_CODE);
        return AjaxResult.success();
    }

    /**
     * 多个点位下发一条指令
     * @param fEqIdList
     * @param pointType
     * @param areaCode 区域代号
     */
    public void sendMultiplePointCmd(List<String> fEqIdList,Long pointType,String areaCode){

        List<String> functionCodeList = new ArrayList<>();
        functionCodeList.add(areaCode);
        //根据父设备ID、点位类型筛选最小点位、最大点位
        List<Map> pointList = devicePointPlcService.selectDevicePointByGroup(fEqIdList,functionCodeList,String.valueOf(pointType));
        for (Map map : pointList){
            sendDdwPointCmd(map,functionCodeList,pointType,areaCode);
        }
    }

    public void sendDdwPointCmd(Map map, List<String> functionCodeList, Long pointType, String areaCode){
        //测控执行器ID
        String fEqId = map.get("fEqId") == null ? "" : map.get("fEqId").toString();
        //最小点位
        String minAddress = map.get("minAddress") == null ? "" : map.get("minAddress").toString();
        //最大点位
        String maxAddress = map.get("maxAddress") == null ? "" : map.get("maxAddress").toString();
        //地址长度
        String dataLength = map.get("dataLength") == null ? "" : map.get("dataLength").toString();

        //计算读取指令的地址长度, 最大 + 地址长度 - 最小
        //比如：最大0026，地址长度=2，最小=0024，实际读取指令长度= （0026 + 2 - 1） - （0024 - 1） = 4
        Integer cmdLength = Integer.valueOf(maxAddress) + Integer.valueOf(dataLength) - Integer.valueOf(minAddress);

        //查询设备信息
        Map deviceMap = OmronFinsTask.deviceMap.get(fEqId);
        String ip = deviceMap.get("ip") == null ? "" : deviceMap.get("ip").toString();
        String port = deviceMap.get("port") == null ? "" : deviceMap.get("port").toString();
        int portNum = Integer.valueOf(port);
        //查询具体的点位配置,待优化 todo
        List<String> idList = new ArrayList<>();
        idList.add(fEqId);
        List<Map> pList = devicePointPlcService.selectPointMapByFEqId(idList,functionCodeList,String.valueOf(pointType));
        if(pList != null && pList.size() > 0){
            String sourceAddress = plcServer;
            String destinationAddress = ip;
            String command = FinsCmdGenerator.getUdpReadCommand(sourceAddress,destinationAddress,areaCode,minAddress,"",String.valueOf(cmdLength));
            try {
                String s = UdpClient(ip, portNum, command);
                JSONObject jsonObject = udpCommandParse(s);
                handleDeviceData(pList,fEqId,jsonObject.getString("value"),minAddress,cmdLength,dataLength,areaCode);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    /**
     * 解析代码
     * @param pointList
     * @param fEqId
     * @param value
     */
    public void handleDeviceData(List<Map> pointList,String fEqId,String value,String minAddress,
                                 Integer cmdLength,String dataLength,String areaCode){
        Map<Integer,String> valueMap = new HashMap<>();

        List<String> list = new ArrayList<>();
        if(FinsCmdValues.D_AREA_CODE.equals(areaCode)){
            //2个字节一个数据
            list = ModbusCmdResolver.handleTwoBytesData(value);
        }
        if(FinsCmdValues.W_WORD_AREA_CODE.equals(areaCode)){
            //2个字节一个数据
            list = FinsCmdResolver.handleTwoBytesData(value);
        }

        // 配置的10进制地址直接转换
        Integer startAddress = Integer.valueOf(minAddress);
        for(int i = 0; i < cmdLength; i++){
            valueMap.put(startAddress,list.get(i));
            startAddress ++;
        }

        for(Map itemMap : pointList){
            String eqId = itemMap.get("eqId") == null ? "" : itemMap.get("eqId").toString();
            String address = itemMap.get("address") == null ? "" : itemMap.get("address").toString();
            String itemId = itemMap.get("itemId") == null ? "":itemMap.get("itemId").toString();
            String pointConfig = itemMap.get("pointConfig") == null ? "" : itemMap.get("pointConfig").toString();
            //原始数据
            if(address == null || "".equals(address)){
                continue;
            }
            String data = valueMap.get(Integer.valueOf(address));
//            System.out.println("测试配置：pointConfig="+pointConfig+",eqId="+eqId);
            data = getFinalData(eqId,data,pointConfig);

            //存储实时数据
            SdDevices sdDevices = new SdDevices();
            sdDevices.setEqId(eqId);
            sdDeviceDataService.updateDeviceData(sdDevices,data,Long.valueOf(itemId));
            //储存历史数据
            setDeviceDataRecord(eqId,data,Long.valueOf(itemId));
            //设置设备在线
            devicesService.updateOnlineStatus(eqId,false);
        }
    }


    /**
     * 根据公式计算或者状态匹配，获得最终的数据
     * @param eqId 设备ID
     * @param data 原始数据
     * @param pointConfig 点位配置
     * @return
     */
    public String getFinalData(String eqId,String data,String pointConfig){
        if(pointConfig == null || "".equals(pointConfig)){
            return data;
        }
        JSONObject jsonConfig = JSONObject.parseObject(pointConfig);
        //获取公式计算
        String formula = jsonConfig.getString("formula");
        //formula:{divisor:'1000',multiple:'406.25',sub:'1625'}}
        if(formula != null && !"".equals(formula)){
            JSONObject formulaObject = JSONObject.parseObject(formula);
            BigDecimal divisor = formulaObject.getBigDecimal("divisor");
            BigDecimal multiple = formulaObject.getBigDecimal("multiple");
            BigDecimal sub = formulaObject.getBigDecimal("sub");

            BigDecimal dValue = new BigDecimal(data);
            dValue = dValue.divide(divisor,2,BigDecimal.ROUND_HALF_UP);
            if(multiple != null){
                dValue = dValue.multiply(multiple);
            }
            if(sub != null){
                dValue = dValue.subtract(sub);
            }
            dValue = dValue.setScale(2,BigDecimal.ROUND_HALF_UP);

            data = String.valueOf(dValue);
            System.out.println("公式配置打印：pointConfig="+pointConfig);
        }
        //匹配状态值
        String stateConfigStr = jsonConfig.getString("stateConfig");
        if(stateConfigStr != null && !"".equals(stateConfigStr)){
            JSONArray jsonArray = JSONArray.parseArray(stateConfigStr);
            //状态匹配
            JSONObject stateJson = new JSONObject();
            for(Object obj : jsonArray){
                JSONObject jsonObject = (JSONObject) obj;
                String valueConfig = jsonObject.getString("value");
                if(data.equals(valueConfig)){
                    stateJson = jsonObject;
                    System.out.println("实时数据状态：stateJson="+stateJson+",eqId="+eqId);
                    break;
                }
            }
            String stateMapping = stateJson.getString("state");
            if(stateMapping == null){
                //报错
                System.out.println("点位配置不完整，pointConfig="+pointConfig+"eqId="+eqId);
            }else{
                data = stateMapping;
            }
        }
        return data;
    }


    /**
     * 单个点位下发一条指令
     * @param fEqIdList
     * @param pointType
     */
    public void sendSinglePointCmd(List<String> fEqIdList,Long pointType,String areaCode){

        List<String> functionCodeList = new ArrayList<>();
        functionCodeList.add(areaCode);
        List<Map> devicePointList = devicePointPlcService.selectPointMapByFEqId(fEqIdList, functionCodeList,String.valueOf(pointType));


        for(Map  map : devicePointList){
            //线程处理
            sendPointCmd(map,areaCode);
        }
    }

    public void sendPointCmd(Map map, String areaCode){
        //设备ID
        String fEqId = map.get("fEqId") == null ? "" : map.get("fEqId").toString();
        //子设备ID
        String eqId = map.get("eqId") == null ? "" : map.get("eqId").toString();
        String itemId = map.get("itemId") == null ? "": map.get("itemId").toString();
        //点位配置
        String pointConfig = map.get("pointConfig") == null ? "" : map.get("pointConfig").toString();
        String addressIndex = map.get("addressIndex") == null ? "" : map.get("addressIndex").toString();
        JSONObject jsonConfig = JSONObject.parseObject(pointConfig);
        //{sourceAddress:'127.0.0.1',destinationAddress:'127.0.0.1',area:'82',address:'502',bitAddress,'',readlength:'2'}

        String area = jsonConfig.getString("area");
        String address = jsonConfig.getString("address");
        //读取的位地址
        String bitAddress = jsonConfig.getString("bitAddress");
        String readLength = jsonConfig.getString("readLength");

        System.out.println("sendSinglePointCmd 读取数据：设备ID="+fEqId+",时间："+System.currentTimeMillis());
        //2毫秒间隔，如果定时任务执行周期是2秒钟，支持1000个指令循环下发，大概可以支持1000个设备的查询指令
//            mcaCmd.sleep(2);

        //查询设备信息
        Map deviceMap = OmronFinsTask.deviceMap.get(fEqId);
        String ip = deviceMap.get("ip") == null ? "" : deviceMap.get("ip").toString();
        String port = deviceMap.get("port") == null ? "" : deviceMap.get("port").toString();
        int portNum = Integer.valueOf(port);
        String sourceAddress = plcServer;
        String destinationAddress = ip;

        String command = FinsCmdGenerator.getUdpReadCommand(sourceAddress,destinationAddress,area,address,bitAddress,readLength);
        try {
            String s = UdpClient(ip, portNum, command);
            JSONObject jsonObject = udpCommandParse(s);
            handleSinglePointDeviceData(map,jsonObject.getString("value"));
        } catch (Exception e) {
            e.printStackTrace();
            //  报错判定设备离线，将网关设备及子设备设置为离线
//                    sdDevicesService.updateOfflineStatus(deviceId,true);
            return;
        }
    }

    /**
     * 解析单个点位的设备数据
     * @param map
     * @param value
     */
    public void handleSinglePointDeviceData(Map map,String value){

        //子设备ID
        String eqId = map.get("eqId") == null ? "" : map.get("eqId").toString();
        String itemId = map.get("itemId") == null ? "": map.get("itemId").toString();
        //点位配置
        String pointConfig = map.get("pointConfig") == null ? "" : map.get("pointConfig").toString();

        String data = getFinalData(eqId,value,pointConfig);

        //存储实时数据
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqId(eqId);
        sdDeviceDataService.updateDeviceData(sdDevices,data,Long.valueOf(itemId));
        //储存历史数据
        setDeviceDataRecord(eqId,data,Long.valueOf(itemId));
        //设置设备在线
        devicesService.updateOnlineStatus(eqId,false);
    }

    /**
     * 线程休眠固定时间
     * @param ms 毫秒
     */
    public void sleep(int ms){
        //间隔固定时间（毫秒）发送指令，避免同一个设备连续多次发送指令无回复
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject udpCommandParse(String msg){
        //信息接收成功的标志，成功之后可以关闭通道
        boolean flag = false;

        JSONObject jsonObject = new JSONObject();
        //46494E53 00000010 00000001 00000000 000000C8 00000001
        int cmdLength = msg.length();
        if(cmdLength < 28){
            System.out.println("报文长度错误");
            return null;
        }
        if(cmdLength >= 28){
            //46 49 4E 53 00 00 00 1A 00 00 00 02 00 00 00 00 C0 0002 00 C8 00 00 01 00 00 01 01 00 00 00 00 00 01
            //C00002 002300 000C00 00 0101 0000 0000
            String header = msg.substring(0,6);
            String da1 = msg.substring(8,10);
            String sa1 = msg.substring(14,16);
            String queryCode = msg.substring(20,24);

            if(FinsQueryCodeEnum.READ_CODE.getCode().equals(queryCode)){
                //读操作
                String errorCode = msg.substring(24,28);
                if(FinsCmdValues.NORMAL_REPLY_CODE.equals(errorCode)){
                    //读取成功
                    String value = msg.substring(28);
                    jsonObject.put("value",value);
                }

            }
            if(FinsQueryCodeEnum.WRITE_CODE.getCode().equals(queryCode)){
                //写操作
                String errorCode = msg.substring(24,28);
                if(FinsCmdValues.NORMAL_REPLY_CODE.equals(errorCode)){
                    //写入成功
                    flag = true;
                }
            }

        }
        jsonObject.put("flag",flag);
        return jsonObject;
    }

    /**
     * 解析读取的数据,2个字节作为一个数据
     * @param data
     * @return
     */
    public static List<String> handleTwoBytesData(String data){
        List<String> list = new ArrayList<>();
        for(int i = 0; i < data.length(); ){
            int len = 4;
            String item = data.substring(i,i+len);
            list.add(item);
            i += len;
        }
        return list;
    }

    /**
     * 储存设备数据历史记录表
     *
     * @param deviceId
     * @param data
     * @param itemId
     */
    public void setDeviceDataRecord(String deviceId,String data,Long itemId){
        Long co = Long.valueOf(DevicesTypeItemEnum.CO.getCode());
        Long vi = Long.valueOf(DevicesTypeItemEnum.VI.getCode());
        Long fengsu = Long.valueOf(DevicesTypeItemEnum.FENG_SU.getCode());
        Long fengxiang = Long.valueOf(DevicesTypeItemEnum.FENG_XIANG.getCode());
        Long dongnei = Long.valueOf(DevicesTypeItemEnum.LIANG_DU_INSIDE.getCode());
        Long dongwai = Long.valueOf(DevicesTypeItemEnum.LIANG_DU_OUTSIDE.getCode());
        Long yuanchuan = Long.valueOf(DevicesTypeItemEnum.YUAN_CHUAN_YA_LI_ZHI.getCode());
        if(itemId == co || itemId == vi || itemId == fengsu || itemId == fengxiang
                || itemId == dongnei || itemId == dongwai || itemId == yuanchuan){
            SdDeviceDataRecord record = new SdDeviceDataRecord();
            record.setDeviceId(deviceId);
            record.setData(data);
            record.setItemId(itemId);
            record.setCreateTime(DateUtils.getNowDate());
            //获取bean
            SdDeviceDataRecordMapper bean = SpringUtils.getBean(SdDeviceDataRecordMapper.class);
            //将数据存入历史记录表
            bean.insertSdDeviceDataRecord(record);
        }
    }
}
