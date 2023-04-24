package com.tunnel.deal.mca.service;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.Arith;
import com.tunnel.business.datacenter.domain.enumeration.DevicePointControlTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdEquipmentState;
import com.tunnel.business.domain.protocol.SdDevicePoint;
import com.tunnel.business.domain.protocol.SdDevicePointState;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdEquipmentStateService;
import com.tunnel.business.service.protocol.ISdDevicePointService;
import com.tunnel.business.service.protocol.ISdDevicePointStateService;
import com.tunnel.deal.mca.config.DeviceManager;
import com.tunnel.deal.mca.util.Obj2ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * describe: 测控执行器数据解析类
 *
 * @author zs
 * @date 2023/4/13
 */
@Component
public class McaDataParse {

    private static final Logger log = LoggerFactory.getLogger(McaDataParse.class);


    @Autowired
    private ISdDevicePointService devicePointService;

    @Autowired
    private ISdDeviceDataService deviceDataService;

    @Autowired
    private ISdDevicePointStateService devicePointStateService;

    @Autowired
    private ISdEquipmentStateService equipmentStateService;


    /**
     * 数据解析
     * @param jsonObject 数据
     */
    public void dataParse(String ip,JSONObject jsonObject){

        //功能码
        String functionCode = jsonObject.getString("functionCode");
        //起始地址
        String address = jsonObject.getString("address");
        Object data = jsonObject.get("data");
        if(data == null){
            return;
        }
        //实时数据
        List<String> dataList = Obj2ListUtil.objToList(data,String.class);
        //筛选对应的设备ID
        String deviceId = getMcaDeviceId(ip);

//        //设置设备及子设备在线
//        sdDevicesService.updateOnlineStatus(deviceId,true);

        List<String> idList = new ArrayList<>();
        idList.add(deviceId);

        //只读点位
        Long pointType = DevicePointControlTypeEnum.only_read.getCode();
        //按父设备ID、功能码筛选设备点位表中的地址，按照点位地址从小到大的顺序，将list数据跟设备点位对应
        //todo 考虑redis缓存设备点位
        List<SdDevicePoint> pointList = devicePointService.selectDevicePointByFEqId(idList,pointType,functionCode);

        // 将读取的数据保存到实时数据表中
        addDeviceData(pointList,dataList,address);
    }

    /**
     * 根据设备IP筛选设备ID
     * @param ip 设备IP
     * @return
     */
    public String getMcaDeviceId(String ip){
        List<String> idList =  DeviceManager.deviceMap.entrySet()
                .stream().filter(x-> Objects.equals(x.getValue().get("ip"), ip)).map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if(idList.size() > 0){
            return idList.get(0);
        }
        return null;
    }


    /**
     * 保存实时数据
     * @param pointList 设备点位列表
     * @param dataList 设备实时数据列表
     * @param startAddress 起始地址
     */
    private void addDeviceData(List<SdDevicePoint> pointList,List<String> dataList,String startAddress){
//        //点位跟数据匹配
//        if(pointList.size() == dataList.size()){
//            addDeviceData(pointList,dataList);
//            return;
//        }
//        //点位与数据不匹配
        Integer addressCursor = Integer.valueOf(startAddress);
        for(int i = 0, j = 0; i < dataList.size(); i++){
            SdDevicePoint devicePoint = pointList.get(j);
            //设备ID
            String eqId = devicePoint.getEqId();
            //设备类型数据项ID
            Long itemId = devicePoint.getItemId();
            //点位地址
            Integer pointAddress = Integer.valueOf(devicePoint.getAddress());
            //点位长度
            Integer dataLength = Integer.valueOf(devicePoint.getDataLength());
//            log.info("待修改的实时数据：eqId="+eqId+",点位地址="+pointAddress+",itemId="+itemId);
            if(addressCursor.equals(pointAddress)){
                //地址匹配，组装实时数据
                String data = dataList.get(i);

                //更新实时数据
                SdDevices sdDevices = new SdDevices();
                sdDevices.setEqId(eqId);
                //根据设备点位ID、设备实时状态查询平台定义的设备状态
                //todo 如果是普通数据（不需要映射状态），需要多走一遍查询，待优化
                data = getPointState(devicePoint,data);
                //精确2位小数
                data = getRoundData(data);
//                log.info("修改实时数据：eqId="+eqId+",点位地址="+pointAddress+",实时数据="+data+",itemId="+itemId);
                deviceDataService.updateDeviceData(sdDevices,data,itemId);

                //如果匹配不成功，进入下次循环继续匹配
                j++;
            }

            addressCursor = addressCursor + dataLength;
        }

    }

    /**
     * 保存实时数据
     * @param pointList 设备点位列表
     * @param dataList 设备实时数据列表
     */
    private void addDeviceData(List<SdDevicePoint> pointList,List<String> dataList){
//        List<SdDeviceData> deviceDataList = new ArrayList<>();
        for(int i = 0; i < pointList.size(); i++){
            SdDevicePoint devicePoint = pointList.get(i);
            //设备ID
            String eqId = devicePoint.getEqId();
            //设备类型数据项ID
            Long itemId = devicePoint.getItemId();
            //实时数据
            String data = dataList.get(i);
//            SdDeviceData deviceData = new SdDeviceData();
//            deviceData.setDeviceId(eqId);
//            deviceData.setItemId(itemId);
//            deviceData.setData(data);

            //更新实时数据
            SdDevices sdDevices = new SdDevices();
            sdDevices.setEqId(eqId);
            deviceDataService.updateDeviceData(sdDevices,data,itemId);
//            deviceDataList.add(deviceData);
        }

//        if(deviceDataList.size() > 0){
//            //批量新增实时数据
//            deviceDataService.insertDeviceDataBatch(deviceDataList);
//        }

    }


    /**
     * 根据设备点位ID、设备实时状态查询平台定义的设备状态
     * @param devicePoint 设备点位
     * @param data 设备实时状态
     * @return
     */
    public String getPointState(SdDevicePoint devicePoint,String data){
        String eqState;
        //查询对应的设备状态
        SdDevicePointState sdDevicePointState = new SdDevicePointState();
        sdDevicePointState.setDevicePointId(devicePoint.getId());
        //设备实际状态
        sdDevicePointState.setControlState(data);
        List<SdDevicePointState> pointStateList = devicePointStateService.selectSdDevicePointStateList(sdDevicePointState);
        if(pointStateList != null && pointStateList.size() > 0){
            sdDevicePointState = pointStateList.get(0);
          Long deviceStateId = sdDevicePointState.getDeviceStateId();
          SdEquipmentState equipmentState = equipmentStateService.selectSdEquipmentStateById(deviceStateId);
          //平台定义的设备状态
          eqState = equipmentState.getDeviceState();
        }else{
//         无配置信息默认原始数据
            eqState = data;
        }

        return eqState;

    }

    /**
     * 模拟量数据精确2位小数
     * @return
     */
    private String getRoundData(String data){
        String pointStr = ".";
        String result;
        if(data.indexOf(pointStr) > 0){
            //包含小数
            double d = Double.valueOf(data);
            //2位小数
            result = String.valueOf(Arith.round(d,2));
        }else{
            //状态数据直接返回原数据
            result = data;
        }

        return result;
    }
}
