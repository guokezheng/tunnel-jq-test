package com.tunnel.deal.tcp.plc.omron;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.ParameterException;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.*;
import com.tunnel.business.domain.dataInfo.SdDeviceDataRecord;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.digitalmodel.SdRadarDevice;
import com.tunnel.business.domain.protocol.SdDevicePointPlc;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataRecordMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.digitalmodel.impl.RadarEventServiceImpl;
import com.tunnel.business.service.protocol.ISdDevicePointPlcService;
import com.tunnel.deal.tcp.client.config.ChannelKey;
import com.tunnel.deal.tcp.client.netty.TcpNettySocketClient;
import com.tunnel.deal.tcp.modbus.ModbusCmdResolver;
import com.tunnel.deal.tcp.plc.omron.fins.*;
import com.tunnel.deal.tcp.plc.omron.task.OmronFinsTask;
import com.tunnel.deal.tcp.util.NumberSystemConvert;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.tunnel.deal.tcp.plc.omron.fins.FinsCmdValues.*;

/**
 * @author zhai
 * @date 2023/9/20
 */
@Component
public class OmronFinsControlProcession {

    private static final Logger log = LoggerFactory.getLogger(OmronFinsControlProcession.class);

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

    @Autowired
    private RadarEventServiceImpl radarEventServiceImpl;

    @Autowired
    @Qualifier("kafkaOneTemplate")
    private KafkaTemplate<String, String> kafkaOneTemplate;

    /**
     * 线程池
     */
    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    /**
     * redis工具类
     */
    @Autowired
    private RedisCache redisCache;

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
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
            // 设置超时时间
            datagramSocket.setSoTimeout(5000);
            byte[] buf = Hex.decodeHex(message.toCharArray());
            InetAddress address = InetAddress.getByName(ipAddress);
            // 封装一个数据包
            int length1 = buf.length;
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, address, port);
            datagramSocket.send(datagramPacket);
            // 接收返回数据
            byte[] receiveBuf = new byte[2048];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuf, receiveBuf.length);
            datagramSocket.receive(receivePacket);
            int length = receivePacket.getLength();
            byte[] bytes = receivePacket.getData();
            receiveStr = byteToArray(bytes, length);
            datagramSocket.close();
        } catch (Exception e) {
            datagramSocket.close();
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
        String tunnelId = sdDevices.getEqTunnelId();
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

        //状态匹配
        JSONObject stateJson = new JSONObject();

        // 电伴热 模拟量写入
        if(sdDevices.getEqType().longValue() == Long.valueOf(DevicesTypeEnum.DIAN_BAN_RE.getCode()).longValue()){

            // 6标 双子山  仰天山  电伴热值*10  ushort
            if(sdDevices.getEqTunnelId().equals("JQ-WeiFang-YangTianShan-SZS") || sdDevices.getEqTunnelId().equals("JQ-WeiFang-YangTianShan-YTS")){
                BigDecimal dValue = new BigDecimal(state);
                dValue = dValue.multiply(BigDecimal.valueOf(10));
                state = String.valueOf(dValue);

                stateJson.put("value", Integer.toHexString(Integer.parseInt(state)));
            }

            // 5标  天赐山  泰和山    float
            else if(sdDevices.getEqTunnelId().equals("JQ-WeiFang-MiaoZi-BJY") || sdDevices.getEqTunnelId().equals("JQ-WeiFang-MiaoZi-WCL")) {
                stateJson.put("value", NumberSystemConvert.convertFloatToHex(Float.parseFloat(state)));
            }

            else{
                stateJson.put("value",  state);
            }
            //stateJson.put("value", to Float.floatToIntBits(Float.parseFloat(state)));
            //stateJson.put("value",  state);

        }else {
            //点位状态
            String stateStr = jsonConfig.getString("stateConfig");
            JSONArray jsonArray = JSONArray.parseArray(stateStr);
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                String stateConfig = jsonObject.getString("state");
                if (state.equals(stateConfig)) {
                    stateJson = jsonObject;
                    break;
                }
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
        log.error("队列长度="+queue.size());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");  // 指定日期时间格式
        //发送指令

        String command = FinsCmdGenerator.getUdpControlCommand(sourceAddress,destinationAddress,area,address,bitAddress,writeLength,controlState);
        System.out.println("下发指令："+command+"，时间="+sdf.format(Calendar.getInstance().getTime()));
        log.error("ip="+ip+",port="+portNum+"下发指令："+command+"，时间="+sdf.format(Calendar.getInstance().getTime()));
        try {
            String s = UdpClient(ip, portNum, command);
            System.out.println("返回指令："+s+"，时间="+sdf.format(Calendar.getInstance().getTime()));
            log.error("返回指令："+s+"，时间="+sdf.format(Calendar.getInstance().getTime()));
            JSONObject jsonObject = udpCommandParse(s);
        } catch (Exception e) {
            System.out.println(ip + ": "+sdDevices.getfEqId() + " 请求超时" + command + e.getMessage());
            //updateDevStatus(sdDevices.getfEqId());
            return AjaxResult.error("设备指令发送报错");
        }

        if(pointConfig.contains("timeDelay")){
            //如果点位配置了延时执行
            delayExecute(sourceAddress,destinationAddress,portNum,area,address,bitAddress,writeLength,stateJson);
        }
        if("JQ-WeiFang-JiuLongYu-JJL".equals(tunnelId) || "JQ-WeiFang-JiuLongYu-MAS".equals(tunnelId)){
            //马鞍山隧道、金家楼隧道的PLC批量控制间隔时间加长（间隔时间短的情况下依次控制同一个PLC的同一个地址，控制会失效，可能与地址内容被覆盖有关）
            sleep(1000);
        }
        return ajaxResult;
    }


    /**
     * 延时执行
     */
    public void delayExecute(String sourceAddress,String destinationAddress,Integer portNum,String area,String address,String bitAddress,String writeLength,JSONObject stateJson){
//{"stateConfig":[{"state":"1","text":"开启","value":"01","timeDelay":"4","timeUnit":"second","delayValue":"00"},{"state":"0","text":"关闭","value":"02","timeDelay":"1","timeUnit":"second","delayValue":"00"}],"area":"B1","address":"5","writeLength":"1"}
        String timeDelay = stateJson.getString("timeDelay");
        //默认单位是秒
        String timeUnit = stateJson.getString("timeUnit");
        String delayValue = stateJson.getString("delayValue");
        Integer timeDelayNum = Integer.valueOf(timeDelay) * 1000;
        sleep(timeDelayNum);
        String command = FinsCmdGenerator.getUdpControlCommand(sourceAddress,destinationAddress,area,address,bitAddress,writeLength,delayValue);
        try {
            String s = UdpClient(destinationAddress, portNum, command);
            JSONObject jsonObject = udpCommandParse(s);
        } catch (Exception e) {
            System.out.println(destinationAddress + ": " + " 请求超时" + command + e.getMessage());
            e.printStackTrace();
        }

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
//        List<String> fEqIdList = new ArrayList<>();
//        fEqIdList.add("JQ-WeiFang-JiuLongYu-JJL-PLC-001");

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
        List<Map> pointList = devicePointPlcService.selectDevicePointByGroupNum(fEqIdList,functionCodeList,String.valueOf(pointType));
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


        if(minAddress.contains(".")){
            minAddress = minAddress.substring(0,minAddress.indexOf("."));
        }
        if(maxAddress.contains(".")){
            maxAddress = maxAddress.substring(0,maxAddress.indexOf("."));
        }

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

            int intervalNum = 500;
            Integer maxAddressNum = Integer.valueOf(maxAddress);
            Integer minAddressNum = Integer.valueOf(minAddress);
            Integer dataLengthNum = Integer.valueOf(dataLength);
            Integer num = maxAddressNum - minAddressNum;

            if(num > intervalNum){
                for(int addressCursor = minAddressNum; addressCursor < maxAddressNum; ){
                    Integer addressEnd = addressCursor + intervalNum;
                    //超过点位最大地址，读取可能会报错，需要特殊处理
                    if(addressEnd > maxAddressNum){
                        addressEnd = maxAddressNum;
                    }
                    cmdLength = addressEnd + dataLengthNum - addressCursor;
                    sendCmd(ip,portNum,pList,fEqId,sourceAddress,destinationAddress,areaCode, String.valueOf(addressCursor),cmdLength,dataLength);
                    addressCursor += intervalNum;

                    sleep(200);
                }
            }else{
                sendCmd(ip,portNum,pList,fEqId,sourceAddress,destinationAddress,areaCode,minAddress,cmdLength,dataLength);
            }

        }
    }

    /**
     * 发送指令，解析数据
     * @param ip
     * @param portNum
     * @param pList
     * @param fEqId
     * @param sourceAddress
     * @param destinationAddress
     * @param areaCode
     * @param minAddress
     * @param cmdLength
     * @param dataLength
     */
    public void sendCmd(String ip,int portNum,List<Map> pList,String fEqId,String sourceAddress,
                        String destinationAddress,String areaCode,String minAddress,
                        Integer cmdLength,String dataLength){
        String command = FinsCmdGenerator.getUdpReadCommand(sourceAddress,
                destinationAddress,areaCode,minAddress,"",String.valueOf(cmdLength));
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");  // 指定日期时间格式
        try {
//            System.out.println("下发指令："+command+"，时间="+sdf.format(Calendar.getInstance().getTime()));
//            log.error("ip="+ip+",port="+portNum+"下发指令："+command+"，时间="+sdf.format(Calendar.getInstance().getTime()));
            String s = UdpClient(ip, portNum, command);
            JSONObject jsonObject = udpCommandParse(s);
//            System.out.println("返回指令："+s+"，时间="+sdf.format(Calendar.getInstance().getTime()));
//            log.error("返回指令："+s+"，时间="+sdf.format(Calendar.getInstance().getTime()));
            handleDeviceData(pList,fEqId,jsonObject.getString("value"),minAddress,cmdLength,dataLength,areaCode);
        } catch (Exception e) {
            System.out.println(ip + ": "+fEqId + " 请求超时" + command + e.getMessage());
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

        if(value == null || "".equals(value)){
            System.out.println("获取的数据为空：value="+value+",deviceId="+fEqId+",minAddress="+minAddress);
            return;
        }
        Map<Integer,String> valueMap = new HashMap<>();

        List<String> list = new ArrayList<>();
//        if(FinsCmdValues.D_AREA_CODE.equals(areaCode)){
        //2个字节一个数据
//            list = ModbusCmdResolver.handleTwoBytesData(value);
//        }
//        if(FinsCmdValues.W_WORD_AREA_CODE.equals(areaCode)){
        //2个字节一个数据
        list = FinsCmdResolver.handleTwoBytesData(value);
//        }

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
            String dataLengthStr = itemMap.get("dataLength") == null ? "":itemMap.get("dataLength").toString();
            Integer addressNum = Integer.valueOf(address);
            Integer dataLengthNum = Integer.valueOf(dataLengthStr);
            //原始数据
            if(address == null || "".equals(address)){
                System.out.println("点位未配置地址address，eqId="+eqId);
                continue;
            }
            String data = "";
            if(!(("JQ-WeiFang-JiuLongYu-JJL".equals(itemMap.get("eqTunnelId"))||"JQ-WeiFang-JiuLongYu-MAS".equals(itemMap.get("eqTunnelId"))
            )&&"48".equals(itemMap.get("eqType").toString()))){
                data = valueMap.get(Integer.valueOf(address));
                if("".equals(data) ||data ==null){
                    continue;
                }
                if(dataLengthNum == 2){
                    //模拟量，4个字节，2个寄存器地址，双字
                    data = data + valueMap.get(addressNum + 1);
                }
//            System.out.println("测试配置：pointConfig="+pointConfig+",eqId="+eqId);
                data = getFinalData(eqId,data,pointConfig,dataLengthStr);

                // Vi  数值千米换算成米 待优化 ，从数据库配置
                if(itemId.equals(String.valueOf(DevicesTypeItemEnum.VI.getCode())) && !"JQ-WeiFang-JiuLongYu-HSD".equals(itemMap.get("eqTunnelId"))){
                    BigDecimal dValue = new BigDecimal(data);
                    dValue = dValue.multiply(BigDecimal.valueOf(1000));
                    data = String.valueOf(dValue);
                }

                //存储实时数据
                dataSave(fEqId,eqId,data,itemId);

            }else{
                //报文拼接
                String sumDraugh = "";
                //读addressNums位拼接报文
                Integer addressNums = Integer.parseInt(address);
                for(int i = 0 ; i<=dataLengthNum-1; i++){
                    data = valueMap.get(Integer.valueOf(addressNums));
                    sumDraugh = sumDraugh+data;
                    addressNums = addressNums+1;
                }
                //说明设备故障
                if(sumDraugh.length()!=20||sumDraugh.indexOf("null")>-1){
                    continue;
                }
                //风机解析入库
                draughtAnalysis( itemId, sumDraugh,  eqId, fEqId);
            }
        }
    }

    /**
     * 风机解析入库
     * @param itemId
     * @param sumDraugh
     * @param eqId
     */
    private void draughtAnalysis(String itemId,String sumDraugh, String eqId, String fEqId){
        //风机振动振动传感器数据组装解析
        //速度浮点数数值
        String draughtSpeed = "";
        //幅度浮点数数值
        String draughtRange = "";
        //沉降浮点数数值
        String draughtDrop = "";
        //倾斜度浮点数数值
        String draughtAngle = "";
        //振动速度值  振动幅度值
        if("76".equals(itemId)){
            draughtSpeed = sumDraugh.substring(2, 10);
            draughtRange = sumDraugh.substring(10, 18);
            //转浮点小数
            draughtSpeed = NumberSystemConvert.reverseHex(draughtSpeed);
            Float draughtSpeedFloat = NumberSystemConvert.convertHexToFloat(draughtSpeed);
            draughtRange = NumberSystemConvert.reverseHex(draughtRange);
            Float draughtRangeFloat = NumberSystemConvert.convertHexToFloat(draughtRange);
            //振动速度 大于7.1要预警
            if (draughtSpeedFloat >   7.1) {
                //速度浮点数数值保存  0正常1报警 2危险
                dataSave(fEqId,eqId,"1","80");
            }else{
                dataSave(fEqId,eqId,"0","80");
            }
            //速度浮点数数值保存
            dataSave(fEqId,eqId,draughtSpeedFloat.toString(),"76");
            //幅度浮点数数值保存
            dataSave(fEqId,eqId,draughtRangeFloat.toString(),"77");
        }
        //沉降值  倾斜值
        if("78".equals(itemId)){
            draughtDrop = sumDraugh.substring(2, 10);
            draughtAngle = sumDraugh.substring(10, 18);
            //转浮点小数
            draughtDrop = NumberSystemConvert.reverseHex(draughtDrop);
            Float draughtDropFloat = NumberSystemConvert.convertHexToFloat(draughtDrop);
            draughtAngle = NumberSystemConvert.reverseHex(draughtAngle);
            Float draughtAngleFloat = NumberSystemConvert.convertHexToFloat(draughtAngle);
            //沉降 倾斜报警
            if(draughtDropFloat<-8 ||draughtAngleFloat<0.1){//低限位报警
                dataSave(fEqId,eqId,"1","81");
            }else if(draughtDropFloat>8 || draughtAngleFloat>1){//搞限位报警
                dataSave(fEqId,eqId,"1","81");
            }else{
                dataSave(fEqId,eqId,"0","81");
            }

            dataSave(fEqId,eqId, draughtDropFloat.toString(),"78");
            //倾斜度浮点数数值保存
            dataSave(fEqId,eqId,draughtAngleFloat.toString(),"79");

        }
    }

    private void dataSave(String fEqId,String eqId,String data ,String itemId){

        Long itemIdt = Long.valueOf(itemId);
        Long co = Long.valueOf(DevicesTypeItemEnum.CO.getCode());
        Long vi = Long.valueOf(DevicesTypeItemEnum.VI.getCode());
        Long fengsu = Long.valueOf(DevicesTypeItemEnum.FENG_SU.getCode());
       //如果数据最终的结果小于0，置为0,避免COVI等模拟量计算数据为负数
       if(itemIdt == co || itemIdt == vi || itemIdt == fengsu ){
           BigDecimal dValue = new BigDecimal(data);
           if(dValue.compareTo(new BigDecimal(0)) < 0) {
               dValue =  BigDecimal.ZERO;
               dValue = dValue.setScale(2,BigDecimal.ROUND_HALF_UP);
               data = String.valueOf(dValue);
           }
        }


        //存储实时数据
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqId(eqId);
        sdDeviceDataService.updateDeviceData(sdDevices,data,Long.valueOf(itemId));
        //储存历史数据
        setDeviceDataRecord(eqId,data,Long.valueOf(itemId));
        //设置设备在线
        devicesService.updateOnlineStatus(eqId,false);
        devicesService.updateOnlineStatus(fEqId,false);
        redisCache.setCacheObject("pushData:" + eqId,"",10, TimeUnit.MINUTES);
        //异步推送万集数据
        threadPoolTaskExecutor.execute(()->{
            pushWanJi(eqId);
        });
    }

    /**
     * 更新设备状态
     * @param fEqId
     */
    public void updateDevStatus(String fEqId){
        List<String> collect = sdDevicesMapper.getDevicesListByFEqId(fEqId).stream().map(SdDevices::getEqId).collect(Collectors.toList());
        sdDevicesMapper.updateSdDevicesBatch(fEqId, DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
        sdDevicesMapper.updateDeviceStatusBatch(collect,DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
    }

    /**
     * 推送万集
     * @param eqId
     */
    private void pushWanJi(String eqId){
        try{
            SdDevices sdDevices = devicesService.selectSdDevicesById(eqId);
            List<SdDevices> sdDevicesList =  new ArrayList<>();
            sdDevicesList.add(sdDevices);
            List<SdRadarDevice> deviceRadar = radarEventServiceImpl.getDeviceRadar(sdDevicesList);
            kafkaOneTemplate.send("baseDeviceStatus", JSON.toJSONString(deviceRadar));
        }catch (Exception e){
            e.getMessage();
        }
    }

    /**
     * 根据公式计算或者状态匹配，获得最终的数据
     * @param eqId 设备ID
     * @param data 原始数据
     * @param pointConfig 点位配置
     * @return
     */
    public String getFinalData(String eqId,String data,String pointConfig,String dataLength){
        if(pointConfig == null || "".equals(pointConfig)){
            //五六七标目前数据解析共分为4种情况：
            //前两种情况，pointConfig有数据，分为状态量配置（五六七标）、模拟量计算公式（七标模拟量,单字，dataLength为1）
            //第三种情况，pointConfig有数据，五标风机、卷帘门、消防泵的反馈点位（状态量配置）是位代码，dataLength=2,functionCode=31
            //第四种情况，pointConfig无数据，模拟量不需要计算（五六标模拟量需要做高低位转换CDAB，双字，浮点数，dataLength为2）

            Integer dataLengthNum = Integer.valueOf(dataLength);
            if(dataLengthNum == 2){
                data = NumberSystemConvert.reverseHex(data);
                Float dataNum = NumberSystemConvert.convertHexToFloat(data);
                BigDecimal dValue = new BigDecimal(dataNum);
                dValue = dValue.setScale(2,BigDecimal.ROUND_HALF_UP);
                return String.valueOf(dValue);
            }
        }
        JSONObject jsonConfig = JSONObject.parseObject(pointConfig);
        //获取公式计算
        String formula = jsonConfig.getString("formula");
        //formula:{divisor:'1000',multiple:'406.25',sub:'1625'}}
        if(formula != null && !"".equals(formula)){
            //七标的模拟量，原始数据转为十进制，再参与公式计算
            Integer num = Integer.parseInt(data,16);

            JSONObject formulaObject = JSONObject.parseObject(formula);
            BigDecimal divisor = formulaObject.getBigDecimal("divisor");
            BigDecimal multiple = formulaObject.getBigDecimal("multiple");
            BigDecimal sub = formulaObject.getBigDecimal("sub");

            BigDecimal dValue = new BigDecimal(num);
            dValue = dValue.divide(divisor,2,BigDecimal.ROUND_HALF_UP);
            if(multiple != null){
                dValue = dValue.multiply(multiple);
            }
            if(sub != null){
                dValue = dValue.subtract(sub);
            }
            if((dValue.compareTo(new BigDecimal(0)) < 0) && (dValue.compareTo(new BigDecimal(-0.01)) > 0)){
                //如果数据小于0且大于-0.01,将数据视为0，避免COVI等模拟量计算数据为负数
                dValue =  BigDecimal.ZERO;
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
            System.out.println(ip + ": "+fEqId + " 请求超时" + command + e.getMessage());
            //updateDevStatus(fEqId);
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
        String dataLength = map.get("dataLength") == null ? "" : map.get("dataLength").toString();
        //点位配置
        String pointConfig = map.get("pointConfig") == null ? "" : map.get("pointConfig").toString();

        String data = getFinalData(eqId,value,pointConfig,dataLength);

        dataSave(map.get("fEqId").toString(),eqId,data,itemId);
        /*//存储实时数据
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqId(eqId);
        sdDeviceDataService.updateDeviceData(sdDevices,data,Long.valueOf(itemId));
        //储存历史数据
        setDeviceDataRecord(eqId,data,Long.valueOf(itemId));
        //设置设备在线
        devicesService.updateOnlineStatus(eqId,false);*/
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
//                if(FinsCmdValues.NORMAL_REPLY_CODE.equals(errorCode)){
                    //读取成功
                    String value = msg.substring(28);
                    jsonObject.put("value",value);
//                }

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
        Long wendu = Long.valueOf(DevicesTypeItemEnum.WEN_DU_CHUANGAN.getCode());
        Long shidu  = Long.valueOf(DevicesTypeItemEnum.SHI_DU_CHUANGAN.getCode());
        Long shuijin  = Long.valueOf(DevicesTypeItemEnum.SHUI_JIN_LEVEL.getCode());

        Long fjZdsd  = Long.valueOf(DevicesTypeItemEnum.ZHEN_DONG_SU_DU.getCode());
        Long fjZdFd  = Long.valueOf(DevicesTypeItemEnum.ZHEN_DONG_FU_DU.getCode());
        Long fjCjz  = Long.valueOf(DevicesTypeItemEnum.CHEN_JIANG_ZHI.getCode());
        Long fjQxz  = Long.valueOf(DevicesTypeItemEnum.QING_XIE_ZHI.getCode());
        Long fjzdgj  = Long.valueOf(DevicesTypeItemEnum.ZHEN_DONG_GAO_JING.getCode());
        Long fjqxgj  = Long.valueOf(DevicesTypeItemEnum.CHEN_JIANG_QING_XIE_GAO_JING.getCode());

        if(itemId == co || itemId == vi || itemId == fengsu || itemId == fengxiang
                || itemId == dongnei || itemId == dongwai || itemId == yuanchuan
                || itemId == wendu || itemId == shidu || itemId == shuijin
                || itemId == fjZdsd || itemId == fjZdFd || itemId == fjCjz
                || itemId == fjQxz || itemId == fjzdgj || itemId == fjqxgj){
            SdDeviceDataRecord record = new SdDeviceDataRecord();
            record.setDeviceId(deviceId);
            record.setData(data);
            record.setItemId(itemId);
            record.setCreateTime(DateUtils.getNowDate());
            //获取bean
            SdDeviceDataRecordMapper bean = SpringUtils.getBean(SdDeviceDataRecordMapper.class);
            //将数据存入历史记录表
//            bean.insertSdDeviceDataRecord(record);
        }
    }
}
