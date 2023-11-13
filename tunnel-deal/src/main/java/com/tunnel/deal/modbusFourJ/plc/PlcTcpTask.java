package com.tunnel.deal.modbusFourJ.plc;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.tunnel.business.datacenter.domain.enumeration.DevicePointControlTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.ISdDevicesProtocolService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.protocol.ISdDevicePointPlcService;
import com.tunnel.deal.enums.DeviceProtocolCodeEnum;
import com.tunnel.deal.modbusFourJ.util.Modbus4jReadMultipleUtil;
import com.tunnel.deal.modbusFourJ.util.Modbus4jReadUtil;
import com.tunnel.deal.modbusFourJ.util.ModbusTcpMaster;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralService;
import com.tunnel.deal.tcp.modbus.ModbusCmd;
import com.tunnel.deal.tcp.modbus.ModbusCmdResolver;
import com.tunnel.deal.tcp.modbus.ModbusFunctionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * describe: PLC modbus tcp定时任务类
 * 定时获取设备状态
 *
 * @author zs
 * @date 2023/10/26
 */
@Component("PlcTcpTask")
public class PlcTcpTask {

    @Autowired
    @Qualifier(value = "ModbusTcpMaster")
    ModbusTcpMaster masterTcp;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdDevicePointPlcService devicePointPlcService;

    @Autowired
    private TcpClientGeneralService tcpClientGeneralService;

    @Autowired
    private ISdDevicesProtocolService sdDevicesProtocolService;

    @Autowired
    private PlcTcpControl plcTcpControl;

    @Autowired
    private ModbusCmd modbusCmd;

    /**
     * 存储设备数据，key为deviceId,value为设备数据【ip,port】
     * 将HashMap替换为线程安全类ConcurrentHashMap
     */
    public static Map<String, Map> deviceMap = new ConcurrentHashMap<>();

    /**
     * 队列初始化容量大小
     * 按照四标PLC读取点位数设置
     */
    public static final int initialCapacity = 500;

    /**
     * 队列最大容量
     * 目前的四标PLC点位数小于这个数
     */
    public static final int maxCapacity = 2000;
    /**
     * 定义写入优先级
     */
    public static final int writePriority = 2;

    /**
     * 定义读取优先级
     */
    public static final int readPriority = 1;

    //    /**
//     * 定义优先级队列
//     * 指定初始化容量大小和排序规则
//     * 优先级相同的数据会乱序
//     */
//    public static PriorityBlockingQueue<Map<String,Object>> queue = new PriorityBlockingQueue(initialCapacity,new Comparator<Map<String, Object>>() {
//        @Override
//        public int compare(Map<String, Object> o1, Map<String, Object> o2) {
//            //根据优先级排序，2在前，1在后
//            String pOne = String.valueOf(o1.get("priority"));
//            String pTwo = String.valueOf(o2.get("priority"));
//            Integer pOneNum = Integer.valueOf(pOne);
//            Integer pTwoNum = Integer.valueOf(pTwo);
//            return pTwoNum - pOneNum;
//        }
//    });


    /**
     * 定义队列
     * 不指定容量，使用Integer.MAX_VALUE初始化容量
     */
    public static Queue<Map<String, Object>> queue = new LinkedBlockingQueue<>(maxCapacity);

    public static Long startTime;
    public static Long endTime;


//    /**
//     * 每个PLC一个队列
//     */
//    public static Map<String,Queue<Map<String, Object>>> queueMap = new ConcurrentHashMap<>();
//
//    /**
//     * 初始化队列Map集合
//     */
//    public void initQueue(){
//        deviceMap.forEach((deviceId,itemMap) ->{
//            Queue<Map<String, Object>> queue = new LinkedBlockingQueue<>(maxCapacity);
//            queueMap.put(deviceId,queue);
//        });
//    }


    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init()
    {
        deviceInfoCache();

        //只创建一次线程
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (!queue.isEmpty()) {
                        if(startTime == null){
                            startTime = System.currentTimeMillis();
                            System.out.println("第一次执行时间：startTime="+startTime);
                        }
                        //队列数据依次执行
                        plcTcpControl.executeQueue();
//                        modbusCmd.sleep(20);
                    }else{
                        if(startTime != null){
                            endTime = System.currentTimeMillis();
                            System.out.println("结束时间：endTime="+endTime);
                            startTime = null;
                        }
                    }
                }

            }
        };
        thread.start();
    }


    /**
     * 设备信息缓存
     *
     */
    public void deviceInfoCache(){
        tcpClientGeneralService.deviceInfoCache(deviceMap, DeviceProtocolCodeEnum.NEW_XI_MEN_ZI_PLC_TCP_PROTOCOL.getCode(), DevicesTypeEnum.PLC.getCode());
    }


    /**
     * 定时请求获取设备的实时数据
     */
//    @Scheduled(cron="0/5 * * * * ?")
    public void getDeviceData(){

        if(!queue.isEmpty()){
            //队列不为空时，中断添加新的读取指令
            return;
        }
//        if(queue.size() > maxCapacity){
//           return;
//        }


        long startTime = System.currentTimeMillis();
        System.out.println("轮询开启时间：startTime="+startTime);
        //拿到缓存设备数据
        Map<String, Map> deviceMap = PlcTcpTask.deviceMap;
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
//        List<String> fEqIdList = new ArrayList<>();
//        fEqIdList.add("JQ-ZiBo-TaiHe-QFL-PLC-001");
//        fEqIdList.add("JQ-ZiBo-TaiHe-QFL-PLC-002");
//        fEqIdList.add("JQ-ZiBo-TaiHe-PDS-PLC-001");
//        fEqIdList.add("JQ-ZiBo-TaiHe-PDS-PLC-002");

        sendMultiplePointCmd(fEqIdList,pointType);


//        sendSinglePointCmd(fEqIdList,pointType);
        long endTime = System.currentTimeMillis();
        System.out.println("轮询结束时间：endTime="+endTime);
        System.out.println("轮询一次的时间：endTime - startTime=" + (endTime - startTime));
    }


    /**
     * 多个点位下发一条指令
     * @param fEqIdList
     * @param pointType
     */
    public void sendMultiplePointCmd(List<String> fEqIdList,Long pointType){
        //功能码02的线圈状态多个点位同时查询，无法从返回数据中确定是哪些地址的数据，此方法只适用于03/04功能码发送指令

        List<String> functionCodeList = new ArrayList<>();
        functionCodeList.add(ModbusFunctionCode.CODE_THREE);
        functionCodeList.add(ModbusFunctionCode.CODE_FOUR);

        //根据父设备ID、点位类型筛选最小点位、最大点位  todo 数字转换待优化
        List<Map> pointList = devicePointPlcService.selectDevicePointByGroupNum(fEqIdList,functionCodeList,String.valueOf(pointType));

        for (Map map : pointList){

            //父设备ID
            String fEqId = map.get("fEqId") == null ? "" : map.get("fEqId").toString();
            //功能码
            String functionCode = map.get("functionCode") == null ? "" : map.get("functionCode").toString();

            //地址长度
            String dataLength = map.get("dataLength") == null ? "" : map.get("dataLength").toString();
            Integer dataLengthNum = Integer.valueOf(dataLength);
            //最小点位
            String minAddress = map.get("minAddress") == null ? "" : map.get("minAddress").toString();
            //最大点位
            String maxAddress = map.get("maxAddress") == null ? "" : map.get("maxAddress").toString();
            if(minAddress.contains(".")){
                minAddress = minAddress.substring(0,minAddress.indexOf("."));
            }
            if(maxAddress.contains(".")){
                maxAddress = maxAddress.substring(0,maxAddress.indexOf("."));
            }

            //modbus tcp 读取寄存器的个数有限制
            Integer maxAddressNum = Integer.valueOf(maxAddress);
            Integer minAddressNum = Integer.valueOf(minAddress);
            Integer num = maxAddressNum - minAddressNum;

            SdDevices fDevice = sdDevicesService.selectSdDevicesById(fEqId);
            String ip = fDevice.getIp();
            String port = fDevice.getPort();
            Integer portNum = Integer.valueOf(port);

//            Channel channel = getChannel(ip,portNum);
            int intervalNum = 50;

            //暂定一次下发50个寄存器地址查询
            if(num > intervalNum){
                for(int addressCursor = minAddressNum; addressCursor < maxAddressNum; ){
                    Integer addressEnd = addressCursor + intervalNum;
                    //超过点位最大地址，读取可能会报错，需要特殊处理
                    if(addressEnd > maxAddressNum){
                        addressEnd = maxAddressNum;
                    }
                    Integer cmdLength = addressEnd + dataLengthNum - addressCursor;

                    Map<String,Object> itemMap = new HashMap<>();
                    itemMap.put("ip",ip);
                    itemMap.put("portNum",portNum);
                    itemMap.put("fEqId",fEqId);
                    itemMap.put("offset",addressCursor);
                    itemMap.put("numberOfBits",cmdLength);
                    itemMap.put("functionCode",functionCode);
                    itemMap.put("priority",readPriority);
                    queue.add(itemMap);


//                    ModbusMaster master = masterTcp.getSlave("127.0.0.1", 502);
////                    System.out.println("下发了命令：fEqId="+fEqId+",addressCursor="+addressCursor+",addressEnd="+addressEnd+",time="+System.currentTimeMillis());
////                    ModbusMaster master = masterTcp.getSlave(ip, portNum);
//                        List<String> list =  Modbus4jReadMultipleUtil.readRegister(master,addressCursor,cmdLength,functionCode);
//                        if(list != null){
//                            plcTcpControl.dateParse(fEqId,functionCode,String.valueOf(addressCursor),list);
//                        }else{
//                            System.out.println("ip="+ip+",port="+port+",PLC="+fEqId+",读取不到数据");
//                        }

                    addressCursor += intervalNum;
                    //添加延时，避免发送数据过快，出现粘包（用助手循环发送测试，300毫米循环下发可以正常回复）
//                    modbusCmd.sleep(100);
                }
            }else{
                //计算读取指令的地址长度, 最大 + 地址长度 - 最小
                //比如：最大0026，地址长度=2，最小=0024，实际读取指令长度= （0026 + 2 - 1） - （0024 - 1） = 4
                Integer cmdLength = maxAddressNum + Integer.valueOf(dataLength) - minAddressNum;

                Map<String,Object> itemMap = new HashMap<>();
                itemMap.put("ip",ip);
                itemMap.put("portNum",portNum);
                itemMap.put("fEqId",fEqId);
                itemMap.put("offset",minAddressNum);
                itemMap.put("numberOfBits",cmdLength);
                itemMap.put("functionCode",functionCode);
                itemMap.put("priority",readPriority);
                queue.add(itemMap);

//                ModbusMaster master = masterTcp.getSlave("127.0.0.1", 502);
////
//                List<String> list =  Modbus4jReadMultipleUtil.readRegister(master,minAddressNum,cmdLength,functionCode);
//                if(list != null){
//                    plcTcpControl.dateParse(fEqId,functionCode,minAddress,list);
//                }else{
//                    System.out.println("ip="+ip+",port="+port+",PLC="+fEqId+",读取不到数据");
//                }

//                modbusCmd.sleep(100);
            }
        }
    }

    /**
     * 单个点位下发一条指令
     * 功能码：0F、02
     * @param fEqIdList
     * @param pointType
     */
    public void sendSinglePointCmd(List<String> fEqIdList,Long pointType){
//        //查询设备点位

        List<String> functionCodeList = new ArrayList<>();
        functionCodeList.add(ModbusFunctionCode.CODE_TWO);
//        functionCodeList.add(ModbusFunctionCode.CODE_FIFTEEN);

        List<Map> devicePointList = devicePointPlcService.selectPointMapByFEqId(fEqIdList, functionCodeList,String.valueOf(pointType));

        for(Map  map : devicePointList){

            //父设备ID
            String fEqId = map.get("fEqId") == null ? "" : map.get("fEqId").toString();
            //功能码
            String functionCode = map.get("functionCode") == null ? "" : map.get("functionCode").toString();
            //点位
            String address = map.get("address") == null ? "" : map.get("address").toString();
            //地址长度
            String dataLength = map.get("dataLength") == null ? "" : map.get("dataLength").toString();
//            System.out.println("sendSinglePointCmd 读取数据：设备ID="+fEqId+",功能码="+functionCode+",点位地址="+address+",读取长度="+dataLength+",时间："+System.currentTimeMillis());
            //2毫秒间隔，如果定时任务执行周期是2秒钟，支持1000个指令循环下发，大概可以支持1000个设备的查询指令
            //最长的盘顶山隧道按照200个设备计算，3个隧道，600个设备
//            modbusCmd.sleep(100);
//            modbusCmd.sendQueryCommand(deviceMap,fEqId,functionCode,address,String.valueOf(dataLength));
        }
    }



    /**
     * 监测设备在线/离线状态
     */
    public void monitorDeviceStatus(Integer offlineTime){
        String protocolCode = DeviceProtocolCodeEnum.NEW_XI_MEN_ZI_PLC_TCP_PROTOCOL.getCode();
        List<Long> noTypeList = new ArrayList<>();
        //查询配置该协议编码的所有的设备，剔除掉PLC检测
        noTypeList.add( DevicesTypeEnum.PLC.getCode());
        Long protocolId = sdDevicesProtocolService.selectProtocolIdByCode(protocolCode);
        List<SdDevices> list = sdDevicesService.getDevicesByProtocol(protocolId,noTypeList);
        //在线设备（离线设备可能在实时数据表中没有任何记录，筛选在线设备避免遗漏离线设备）
        List<String> onlineList = sdDevicesService.selectOnlineDeviceByUpdateTime(protocolId,noTypeList,offlineTime);
        //遍历所有设备
        list.forEach(device ->{
            String deviceId = device.getEqId();
            if(onlineList.contains(deviceId)){
                //在线
                sdDevicesService.updateOnlineStatus(deviceId,false);
            }else{
                //离线
                sdDevicesService.updateOfflineStatus(deviceId,false);
            }
        });
    }

}
