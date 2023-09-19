package com.tunnel.deal.tcp.plc.omron.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tunnel.business.datacenter.domain.enumeration.DevicePointControlTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.protocol.ISdDevicePointPlcService;
import com.tunnel.deal.enums.DeviceProtocolCodeEnum;
import com.tunnel.deal.tcp.client.config.ChannelKey;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralService;
import com.tunnel.deal.tcp.client.netty.TcpNettySocketClient;
import com.tunnel.deal.tcp.modbus.ModbusCmdResolver;
import com.tunnel.deal.tcp.plc.omron.FinsCmd;
import com.tunnel.deal.tcp.plc.omron.fins.FinsCmdResolver;
import com.tunnel.deal.tcp.plc.omron.fins.FinsCmdValues;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

import static com.tunnel.deal.tcp.plc.omron.fins.FinsCmdValues.D_AREA_CODE;
import static com.tunnel.deal.tcp.plc.omron.fins.FinsCmdValues.W_BIT_AREA_CODE;
import static com.tunnel.deal.tcp.plc.omron.fins.FinsCmdValues.W_WORD_AREA_CODE;

/**
 * describe: 欧姆龙PLC读取数据--定时任务
 *  区别于原代码：点位配置不同
 * @author zs
 * @date 2023/8/22
 **/
@Component("OmronFinsTask")
public class OmronFinsTask {

    private static final Logger log = LoggerFactory.getLogger(OmronFinsTask.class);

    @Autowired
    private ISdDevicesService devicesService;

    @Autowired
    private ISdDevicePointPlcService devicePointPlcService;

    @Autowired
    private ISdDeviceDataService sdDeviceDataService;

    @Autowired
    private TcpClientGeneralService tcpClientGeneralService;


    @Autowired
    private FinsCmd finsCmd;

    @Resource(name = "scheduledExecutorService")
    private ScheduledExecutorService executor;

    /**
     * 存储设备数据，key为deviceId,value为设备数据【ip,port】
     * 将HashMap替换为线程安全类ConcurrentHashMap
     */
    public static Map<String, Map> deviceMap = new ConcurrentHashMap<>();

    /**
     * 指令下发缓存
     */
    public static Map<String,String> pointCmdCache = new ConcurrentHashMap<>();

    @Value("${plc.server}")
    private String plcServer;

    /**
     * 欧姆龙PLC下发指令锁：控制指令、读取指令不同时下发
     * (不加锁：六标程序会出现异常情况：
     * 连续下发七八次控制指令后（同时开启查询的定时任务），PLC返回：46494e53000000080000000600000000)
     */
    public static Boolean commandLock = true;



    /**
     * 固定时间间隔更新PLC设备信息缓存,定时重新连接
     */
    public void connect(){
        deviceInfoCache();
//        TcpNettySocketClient.getInstance().deviceConnect(deviceMap);
    }

    /**
     * 欧姆龙PLC设备信息缓存
     *
     */
    public void deviceInfoCache(){
        tcpClientGeneralService.deviceInfoCache(deviceMap, DeviceProtocolCodeEnum.OU_MU_LONG_PLC_TCP_NETTY_PROTOCOL_CODE.getCode(), DevicesTypeEnum.PLC.getCode());
    }


    /**
     * 定时请求获取PLC设备的实时数据
     */
    public void getDeviceData(){
        //拿到缓存设备数据-测控执行器
        Map<String, Map> deviceMap = OmronFinsTask.deviceMap;
        //点位类型：只读点位
        Long pointType = DevicePointControlTypeEnum.only_read.getCode();

        //查询具体的子设备（温湿度传感器、交通信号灯、车指反馈点位），拿到点位，查询is_reserved = 1只读点位
        //取点位最大、最小的点位下发查询指令
        //提供一个多点位查询的方法，根据现场点位的规律实现多点位读取

        //父设备ID列表
        List<String> fEqIdList = new ArrayList<>(deviceMap.keySet());
        if(fEqIdList.size() == 0){
            return;
        }

//        sendSinglePointCmd(fEqIdList,pointType);

        //D区
        sendMultiplePointCmd(fEqIdList,pointType,D_AREA_CODE);
        sleep(50);
        //W字代码
        sendMultiplePointCmd(fEqIdList,pointType,W_WORD_AREA_CODE);
        //W位代码
        sendSinglePointCmd(fEqIdList,pointType,W_BIT_AREA_CODE);
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
            if(!commandLock){
                //没有拿到锁,不执行查询指令
                continue;
            }
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
                //点位配置
//                Map pointMap = pList.get(0);
//                String pointConfig = pointMap.get("pointConfig") == null ? "" : pointMap.get("pointConfig").toString();
//                JSONObject jsonConfig = JSONObject.parseObject(pointConfig);
                String sourceAddress = plcServer;
                String destinationAddress = ip;

                //获取连接通道
                TcpNettySocketClient.getInstance().connect(ip,portNum);

                int count = 50;
                int i = 0;
                boolean flag = false;
                //发送指令
                while(i < count){
                    Channel channel = TcpNettySocketClient.channels.get(ChannelKey.getChannelKey(ip,portNum));
                    if (channel != null && channel.isActive()) {
                        if(!flag){
                            //建立连接后，发送握手指令,只发送一次
                            finsCmd.sendHandshakeCommand(OmronFinsTask.deviceMap,fEqId,sourceAddress);
                            flag = true;
                        }
                        //判断握手是否成功
                        Object isHandshakeObj = OmronFinsTask.deviceMap.get(fEqId).get("handshake");
                        Boolean isHandshake = Boolean.valueOf(String.valueOf(isHandshakeObj));
                        if(isHandshake){
                            //握手成功
                            sleep(100);
                            finsCmd.sendQueryCommand(OmronFinsTask.deviceMap,fEqId,sourceAddress,destinationAddress,areaCode,minAddress,"",String.valueOf(cmdLength));

//                            long time = System.currentTimeMillis();

                            break;
                        }
                    }
                    i++;
                    sleep(20);
                }

                int j = 0;
                //等待指令返回
                while(j < count){
                 String value = OmronFinsTask.pointCmdCache.get(fEqId+"query");
                    if(value != null && !"".equals(value)){
                        handleDeviceData(pList,fEqId,value,minAddress,cmdLength,dataLength,areaCode);
                        OmronFinsTask.pointCmdCache.remove(fEqId+"query");
                        break;
                    }
                    j++;
                    sleep(20);
                }
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
            log.error("公式配置打印：pointConfig="+pointConfig);
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
                log.error("点位配置不完整，pointConfig="+pointConfig+"eqId="+eqId);
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

            //获取连接通道
            TcpNettySocketClient.getInstance().connect(ip,portNum);

            int count = 50;
            int i = 0;
            boolean flag = false;
            //发送指令
            while(i < count){
                Channel channel = TcpNettySocketClient.channels.get(ChannelKey.getChannelKey(ip,portNum));
                if (channel != null && channel.isActive()) {
                    if(!flag){
                        //建立连接后，发送握手指令,只发送一次
                        finsCmd.sendHandshakeCommand(OmronFinsTask.deviceMap,fEqId,sourceAddress);
                        flag = true;
                    }
                    //判断握手是否成功
                    Object isHandshakeObj = OmronFinsTask.deviceMap.get(fEqId).get("handshake");
                    Boolean isHandshake = Boolean.valueOf(String.valueOf(isHandshakeObj));
                    if(isHandshake){
                        //握手成功
                        finsCmd.sendQueryCommand(OmronFinsTask.deviceMap,fEqId,sourceAddress,destinationAddress,areaCode,address,bitAddress,readLength);

//                            long time = System.currentTimeMillis();

                        break;
                    }
                }
                i++;
                sleep(20);
            }

            int j = 0;
            //等待指令返回
            while(j < count){
                String value = OmronFinsTask.pointCmdCache.get(fEqId+"query");
                if(value != null && !"".equals(value)){
                    handleSinglePointDeviceData(map,value);
                    OmronFinsTask.pointCmdCache.remove(fEqId+"query");
                    break;
                }
                j++;
                sleep(20);
            }

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
}
