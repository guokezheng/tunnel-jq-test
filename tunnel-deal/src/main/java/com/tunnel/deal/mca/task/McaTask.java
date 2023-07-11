package com.tunnel.deal.mca.task;

import com.tunnel.business.datacenter.domain.enumeration.DevicePointControlTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.service.protocol.ISdDevicePointService;
import com.tunnel.deal.enums.DeviceProtocolCodeEnum;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralService;
import com.tunnel.deal.tcp.client.netty.MCASocketClient;
import com.tunnel.deal.tcp.modbus.ModbusCmd;
import com.tunnel.deal.tcp.modbus.ModbusFunctionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * describe: 测控执行器--定时任务类
 *
 * @author zs
 * @date 2023/4/12
 */
@Component("McaTask")
public class McaTask {

    @Autowired
    private ISdDevicePointService devicePointService;

    @Autowired
    private TcpClientGeneralService tcpClientGeneralService;


    @Autowired
    private ModbusCmd mcaCmd;

    /**
     * 存储设备数据，key为deviceId,value为设备数据【ip,port】
     * 将HashMap替换为线程安全类ConcurrentHashMap
     */
    public static Map<String,Map> deviceMap = new ConcurrentHashMap<>();

    /**
     * 固定时间间隔更新测控执行器设备信息缓存,定时重新连接MCA
     */
    public void connect(){
        deviceInfoCache();
        MCASocketClient.getInstance().deviceConnect(deviceMap);
    }

    /**
     * 测控执行器设备信息缓存
     *
     */
    public void deviceInfoCache(){
        tcpClientGeneralService.deviceInfoCache(deviceMap,DeviceProtocolCodeEnum.ZHENGCHEN_MCA_PROTOCOL_CODE.getCode(),DevicesTypeEnum.CE_KONG_ZHI_XING_QI.getCode());
    }



    /**
     * 定时请求获取MCA设备的实时数据
     */
    public void getMcaDeviceData(){
        //拿到缓存设备数据-测控执行器
        Map<String, Map> deviceMap = McaTask.deviceMap;
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

        sendMultiplePointCmd(fEqIdList,pointType);

        sendSinglePointCmd(fEqIdList,pointType);

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

        //根据父设备ID、点位类型筛选最小点位、最大点位
        List<Map> pointList = devicePointService.selectDevicePointByGroup(fEqIdList,functionCodeList,String.valueOf(pointType));

        for (Map map : pointList){
            //测控执行器ID
            String fEqId = map.get("fEqId") == null ? "" : map.get("fEqId").toString();
            //功能码
            String functionCode = map.get("functionCode") == null ? "" : map.get("functionCode").toString();
            //最小点位
            String minAddress = map.get("minAddress") == null ? "" : map.get("minAddress").toString();
            //最大点位
            String maxAddress = map.get("maxAddress") == null ? "" : map.get("maxAddress").toString();
            //地址长度
            String dataLength = map.get("dataLength") == null ? "" : map.get("dataLength").toString();
            //计算读取指令的地址长度, 最大 + 地址长度 - 最小
            //比如：最大0026，地址长度=2，最小=0024，实际读取指令长度= （0026 + 2 - 1） - （0024 - 1） = 4
            Integer cmdLength = Integer.valueOf(maxAddress) + Integer.valueOf(dataLength) - Integer.valueOf(minAddress);
//            log.info("读取数据：设备ID="+fEqId+",功能码="+functionCode+",最小点位="+minAddress+",读取长度="+cmdLength);

            mcaCmd.sleep(2);
            mcaCmd.sendQueryCommand(deviceMap,fEqId,functionCode,minAddress,String.valueOf(cmdLength));
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
//        SdDevicePoint devicePoint = new SdDevicePoint();
//        devicePoint.setIsReserved(pointType);

        List<String> functionCodeList = new ArrayList<>();
        functionCodeList.add(ModbusFunctionCode.CODE_TWO);
        functionCodeList.add(ModbusFunctionCode.CODE_FIFTEEN);

        List<Map> devicePointList = devicePointService.selectPointMapByFEqId(fEqIdList, functionCodeList,String.valueOf(pointType));

        for(Map  map : devicePointList){
            //测控执行器ID
            String fEqId = map.get("fEqId") == null ? "" : map.get("fEqId").toString();
            //功能码
            String functionCode = map.get("functionCode") == null ? "" : map.get("functionCode").toString();
            //点位
            String address = map.get("address") == null ? "" : map.get("address").toString();
            //地址长度
            String dataLength = map.get("dataLength") == null ? "" : map.get("dataLength").toString();

            //2毫秒间隔，如果定时任务执行周期是2秒钟，支持1000个指令循环下发，大概可以支持1000个设备的查询指令
            //最长的盘顶山隧道按照200个设备计算，3个隧道，600个设备
            mcaCmd.sleep(2);
            mcaCmd.sendQueryCommand(deviceMap,fEqId,functionCode,address,String.valueOf(dataLength));
        }
    }


}
