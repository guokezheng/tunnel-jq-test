package com.tunnel.deal.tcp.plc.ximenzi.task;

import com.tunnel.business.datacenter.domain.enumeration.DevicePointControlTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.protocol.ISdDevicePointPlcService;
import com.tunnel.deal.enums.DeviceProtocolCodeEnum;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralService;
import com.tunnel.deal.tcp.client.netty.TcpNettySocketClient;
import com.tunnel.deal.tcp.modbus.ModbusCmd;
import com.tunnel.deal.tcp.modbus.ModbusFunctionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * describe: 西门子PLC定时任务
 * 获取实时状态
 *
 * @author zs
 * @date 2023/9/17
 */
@Component("WZBXiMenZiPlcTask")
public class WZBXiMenZiPlcTask {


    @Autowired
    private ISdDevicePointPlcService devicePointPlcService;

    @Autowired
    private TcpClientGeneralService tcpClientGeneralService;

    @Autowired
    private ISdDevicesService sdDevicesService;


    @Autowired
    private ModbusCmd modbusCmd;

    /**
     * 存储设备数据，key为deviceId,value为设备数据【ip,port】
     * 将HashMap替换为线程安全类ConcurrentHashMap
     */
    public static Map<String, Map> deviceMap = new ConcurrentHashMap<>();



    /**
     * 固定时间间隔更新设备信息缓存,定时重新连接
     */
    public void connect(){
        deviceInfoCache();
        TcpNettySocketClient.getInstance().deviceConnect(deviceMap);
    }

    /**
     * 设备信息缓存
     *
     */
    public void deviceInfoCache(){
        tcpClientGeneralService.deviceInfoCache(deviceMap, DeviceProtocolCodeEnum.WZB_XI_MEN_ZI_PLC_TCP_PROTOCOL_CODE.getCode(), DevicesTypeEnum.PLC.getCode());
    }


    /**
     * 定时请求获取设备的实时数据
     */
//    @Scheduled(cron="0/5 * * * * ?")
    public void getDeviceData(){
//        long startTime = System.currentTimeMillis();
//        System.out.println("轮询开启时间：startTime="+startTime);
        //拿到缓存设备数据
        Map<String, Map> deviceMap = WZBXiMenZiPlcTask.deviceMap;
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
//        fEqIdList.add("JQ-JiNan-WenZuBei-MJY-PLC-011");
//        fEqIdList.add("JQ-JiNan-WenZuBei-MJY-PLC-012");
        sendMultiplePointCmd(fEqIdList,pointType);


        sendSinglePointCmd(fEqIdList,pointType);
//        long endTime = System.currentTimeMillis();
//        System.out.println("轮询结束时间：endTime="+endTime);
//        System.out.println("轮询一次的时间：endTime - startTime=" + (endTime - startTime));
    }

    /**
     * 多个点位下发一条指令
     * @param fEqIdList
     * @param pointType
     */
    public void sendMultiplePointCmd(List<String> fEqIdList,Long pointType){
        //功能码02的线圈状态多个点位同时查询，无法从返回数据中确定是哪些地址的数据，此方法只适用于03/04功能码发送指令

        List<String> functionCodeList = new ArrayList<>();
        functionCodeList.add(ModbusFunctionCode.CODE_ONE);
        functionCodeList.add(ModbusFunctionCode.CODE_FOUR);

        //根据父设备ID、点位类型筛选最小点位、最大点位  todo 数字转换待优化
        List<Map> pointList = devicePointPlcService.selectDevicePointByGroupNum(fEqIdList,functionCodeList,String.valueOf(pointType));

        for (Map map : pointList){
            if(!ModbusCmd.commandLock){
                //没有拿到锁,不执行查询指令
                continue;
            }

            //父设备ID
            String fEqId = map.get("fEqId") == null ? "" : map.get("fEqId").toString();
            //功能码
            String functionCode = map.get("functionCode") == null ? "" : map.get("functionCode").toString();

            //地址长度
            String dataLength = map.get("dataLength") == null ? "" : map.get("dataLength").toString();
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

//            SdDevices fDevice = sdDevicesService.selectSdDevicesById(fEqId);
//            String ip = fDevice.getIp();
//            String port = fDevice.getPort();
//            Integer portNum = Integer.valueOf(port);

//            Channel channel = getChannel(ip,portNum);

            //暂定一次下发20个寄存器地址查询
            if(num > 30){
                for(int addressCursor = minAddressNum; addressCursor < maxAddressNum; ){
                    Integer addressEnd = addressCursor + 50;
                    Integer cmdLength = addressEnd + Integer.valueOf(dataLength) - addressCursor;
                    modbusCmd.sendQueryCommand(deviceMap,fEqId,functionCode,String.valueOf(addressCursor),String.valueOf(cmdLength));

//                            System.out.println("下发了命令：fEqId="+fEqId+",addressCursor="+addressCursor+",addressEnd="+addressEnd+",time="+System.currentTimeMillis());
                    addressCursor += 50;
                    //添加延时，避免发送数据过快，出现粘包（用助手循环发送测试，300毫米循环下发可以正常回复）
                    modbusCmd.sleep(400);
                }
            }else{
                //计算读取指令的地址长度, 最大 + 地址长度 - 最小
                //比如：最大0026，地址长度=2，最小=0024，实际读取指令长度= （0026 + 2 - 1） - （0024 - 1） = 4
                Integer cmdLength = maxAddressNum + Integer.valueOf(dataLength) - minAddressNum;
//            System.out.println("sendMultiplePointCmd 读取数据：设备ID="+fEqId+",功能码="+functionCode+",最小点位="+minAddress+",读取长度="+cmdLength+",时间："+System.currentTimeMillis());

                modbusCmd.sendQueryCommand(deviceMap,fEqId,functionCode,minAddress,String.valueOf(cmdLength));
//                        System.out.println("下发了命令：fEqId="+fEqId+",minAddressNum="+minAddressNum+",maxAddressNum="+maxAddressNum+",time="+System.currentTimeMillis());
                modbusCmd.sleep(300);
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
        functionCodeList.add(ModbusFunctionCode.CODE_FIFTEEN);

        List<Map> devicePointList = devicePointPlcService.selectPointMapByFEqId(fEqIdList, functionCodeList,String.valueOf(pointType));

        for(Map  map : devicePointList){
            if(!ModbusCmd.commandLock){
                //没有拿到锁,不执行查询指令
                return;
            }

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
            modbusCmd.sleep(100);
            modbusCmd.sendQueryCommand(deviceMap,fEqId,functionCode,address,String.valueOf(dataLength));
        }
    }


}
