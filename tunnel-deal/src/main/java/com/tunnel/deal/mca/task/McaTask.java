package com.tunnel.deal.mca.task;

import com.tunnel.business.datacenter.domain.enumeration.DevicePointControlTypeEnum;
import com.tunnel.business.service.protocol.ISdDevicePointService;
import com.tunnel.deal.mca.config.DeviceManager;
import com.tunnel.deal.mca.netty.MCASocketClient;
import com.tunnel.deal.mca.service.McaCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private McaCmd mcaCmd;


    /**
     * 定时重新连接MCA
     */
    public void connect(){
        MCASocketClient.getInstance().deviceConnect();
    }


    /**
     * 定时请求获取MCA设备的实时数据
     */
    public void getMcaDeviceData(){
        //拿到缓存设备数据-测控执行器
      Map<String, Map> deviceMap = DeviceManager.deviceMap;
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
        //根据父设备ID、点位类型筛选最小点位、最大点位
        List<Map> pointList = devicePointService.selectDevicePointByGroup(fEqIdList,String.valueOf(pointType));

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
            //比如：最大30026，地址长度=2，最小=30024，实际读取指令长度= （30026 + 2 - 1） - （30024 - 1） = 4
            Integer cmdLength = Integer.valueOf(maxAddress) + Integer.valueOf(dataLength) - Integer.valueOf(minAddress);
//            log.info("读取数据：设备ID="+fEqId+",功能码="+functionCode+",最小点位="+minAddress+",读取长度="+cmdLength);
            mcaCmd.sendQueryCommand(fEqId,functionCode,minAddress,String.valueOf(cmdLength));
        }


//        //所有子设备列表
//        List<SdDevices> deviceList =  sdDevicesService.selectDevicesByFEqId(fEqIdList);
//        //子设备ID列表
//        List<String> deviceIdList = deviceList.stream().map(SdDevices::getEqId).collect(Collectors.toList());

//        //查询设备点位
//        List<SdDevicePoint> devicePointList = devicePointService.selectDevicePointByList(deviceIdList,String.valueOf(pointType));
//
//        //子设备ID、父设备ID映射
//        Map<String,String> idMap = deviceList.stream().collect(Collectors.toMap(SdDevices::getEqId,SdDevices::getFEqId));

//        //拼接指令
//        for(SdDevicePoint devicePoint : devicePointList){
//            //子设备ID
//            String deviceId = devicePoint.getEqId();
//            //父设备ID
//            String fEqId = idMap.get(deviceId);
//            String functionCode = devicePoint.getFunctionCode();
//            String address = devicePoint.getAddress();
//            String dataLength = devicePoint.getDataLength();
//            mcaCmd.sendQueryCommand(fEqId,functionCode,address,dataLength);
//        }


    }
}
