package com.tunnel.deal.mca.service;

import com.alibaba.fastjson.JSONArray;
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
import com.tunnel.deal.tcp.modbus.ModbusCmdResolver;
import com.tunnel.deal.tcp.modbus.ModbusFunctionCode;
import com.tunnel.deal.tcp.util.Obj2ListUtil;
import com.tunnel.deal.tcp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

//    @Autowired
//    private ISdDevicePointStateService devicePointStateService;
//
//    @Autowired
//    private ISdEquipmentStateService equipmentStateService;



    /**
     * 返回数据解析
     * 将原始指令解析成对应格式的数据
     * @param ip
     * @param deviceId
     * @param msg
     */
    public void readDataParse(String ip,String deviceId,String msg){

        JSONObject jsonObject = ModbusCmdResolver.commandParse(ip,deviceId,msg);

        String functionCode = jsonObject.getString("functionCode");
        String readData = jsonObject.getString("readData");
        if(ModbusFunctionCode.CODE_TWO.equals(functionCode)){

            Integer num = Integer.parseInt(readData,16);
            String binaryNum = Integer.toBinaryString(num);
            binaryNum = StringUtil.fillStringWithZero(binaryNum,4);
            jsonObject.put("data",binaryNum);
        }
        if(ModbusFunctionCode.CODE_THREE.equals(functionCode) || ModbusFunctionCode.CODE_FOUR.equals(functionCode)){
            Integer length = readData.length();
//            //按照功能码分析
//            if(ModbusFunctionCode.CODE_THREE.equals(functionCode)){
                //两个字节一个数字
                if(length % 4 == 0){
                    List<String> list = ModbusCmdResolver.handleTwoBytesData(readData);
                    jsonObject.put("data",list);
                }
//            }
//            if(ModbusFunctionCode.CODE_FOUR.equals(functionCode)){
//                //两个字节一个数字
//                if(length % 4 == 0){
//                    List<String> list = ModbusCmdResolver.handleTwoBytesData(readData);
//                    jsonObject.put("data",list);
//                }
//            }
        }
        if(ModbusFunctionCode.CODE_SIX.equals(functionCode)){
            //控制指令的返回指令，不需要解析
        }

        dataParse(ip,deviceId,jsonObject);
    }


    /**
     * 数据解析
     * @param jsonObject 数据
     */
    public void dataParse(String ip,String deviceId,JSONObject jsonObject){

        //功能码
        String functionCode = jsonObject.getString("functionCode");
        //起始地址
        String address = jsonObject.getString("address");
        Object data = jsonObject.get("data");
        if(data == null){
            return;
        }

        List<String> idList = new ArrayList<>();
        idList.add(deviceId);

        //只读点位
        Long pointType = DevicePointControlTypeEnum.only_read.getCode();

        List<SdDevicePoint> pointList = devicePointService.selectDevicePointByFEqId(idList,pointType,functionCode);


        switch (functionCode){
            case ModbusFunctionCode.CODE_TWO:
                getFunctionTwoData(pointList, String.valueOf(data),address);
                break;
            case ModbusFunctionCode.CODE_THREE:
            case ModbusFunctionCode.CODE_FOUR:
                //实时数据
                List<String> dataList = Obj2ListUtil.objToList(data,String.class);
                getFunctionThreeData(pointList,dataList,address);
            default: break;
        }

    }

    /**
     * 功能码03、04返回数据解析
     * 点位配置解析：需要计算公式，将原始数据（模拟量）转为具有实际意义的数字
     * @param pointList
     * @param dataList
     * @param address
     */
    public void getFunctionThreeData(List<SdDevicePoint> pointList,List<String> dataList,String address){
        //按父设备ID、功能码筛选设备点位表中的地址，按照点位地址从小到大的顺序，将list数据跟设备点位对应
        // 将读取的数据保存到实时数据表中
        //将读取的数据按照计算公式转换为具有实际意义的数据   todo 16进制直接转换可能有问题，待测试
        Integer addressCursor = Integer.valueOf(address);
        for(int i = 0, j = 0; j < pointList.size() && i < dataList.size();  j++){
            SdDevicePoint devicePoint = pointList.get(j);
            //设备ID
            String eqId = devicePoint.getEqId();
            //设备类型数据项ID
            Long itemId = devicePoint.getItemId();
            //点位地址
            Integer pointAddress = Integer.valueOf(devicePoint.getAddress());
            //点位长度
            Integer dataLength = Integer.valueOf(devicePoint.getDataLength());
            if(addressCursor.equals(pointAddress)){

                String pointConfig = devicePoint.getPointConfig();
                JSONObject jsonObject = JSONObject.parseObject(pointConfig);
                //{address:'0001',byte:'2',rangeMin:'4',rangeMax:'20',realRangeMin:'-40',realRangeMax:'80'}
                Integer rangeMin = jsonObject.getInteger("rangeMin");
                Integer rangeMax = jsonObject.getInteger("rangeMax");
                Integer realRangeMin = jsonObject.getInteger("realRangeMin");
                Integer realRangeMax = jsonObject.getInteger("realRangeMax");
                //4174十进制是16756 ，7fff十进制是32767，16756*25/32767=12.78,量程：4-20mA,温度：-40~80℃，按照0~120℃计算，(12.78-4)/(20-4)*120 - 40=25.88℃
//            湿度解析：3865十进制是14437，7fff十进制是32767，14437*25/32767=11.01,量程：4-20mA,湿度：0~100%，(11.12-4)/(20-4)*100% - 0=43.8%
                //地址匹配，组装实时数据
                String data = dataList.get(i);
                Integer fixed = Integer.parseInt("7FFF",16);
                BigDecimal dataNum = new BigDecimal(data);
                //精确2位小数
                BigDecimal bd = dataNum.multiply(new BigDecimal(25)).divide(new BigDecimal(fixed),3,BigDecimal.ROUND_HALF_UP);
                BigDecimal rangeSub = new BigDecimal(rangeMax - rangeMin);
                BigDecimal realRangeSub = new BigDecimal(realRangeMax - realRangeMin);
                BigDecimal realRangeMinAbs = new BigDecimal(Math.abs(realRangeMin));
                BigDecimal result =  bd.subtract(new BigDecimal(rangeMin)).divide(rangeSub,3,BigDecimal.ROUND_HALF_UP).multiply(realRangeSub).subtract(realRangeMinAbs);
                //计算过程中保留3位小数，最终结果保留2位小数
                result = result.setScale(2,BigDecimal.ROUND_HALF_UP);
                //更新实时数据
                SdDevices sdDevices = new SdDevices();
                sdDevices.setEqId(eqId);
                deviceDataService.updateDeviceData(sdDevices, String.valueOf(result),itemId);

                //如果匹配不成功，进入下次循环继续匹配
                i++;
                addressCursor = addressCursor + dataLength;
            }

        }
    }

    /**
     * 功能码02返回数据解析
     * @param pointList
     * @param data
     * @param address
     */
    public void getFunctionTwoData(List<SdDevicePoint> pointList,String data,String address){
        //根据IP查询出对应网关设备，查询网关设备所有子设备的配置点位信息，根据点位地址过滤，确定子设备；
        //根据配置的状态等信息，确定实时状态，修改数据库实时数据
        List<SdDevicePoint> selectedList = pointList.stream().filter(sdDevicePoint -> address.equals(sdDevicePoint.getAddress())).collect(Collectors.toList());
        if(selectedList.size() > 0){
            SdDevicePoint devicePoint = selectedList.get(0);

            //设备ID
            String eqId = devicePoint.getEqId();
            //设备类型数据项ID
            Long itemId = devicePoint.getItemId();
            String pointConfig = devicePoint.getPointConfig();
            JSONArray jsonArray = JSONArray.parseArray(pointConfig);

            String state = "";
            for(Object obj : jsonArray){
                JSONObject jsonObject = (JSONObject) obj;
                String pointValue = jsonObject.getString("pointValue");
                if(data.equals(pointValue)){
                    state = jsonObject.getString("state");
                    break;
                }
            }

            if(!"".equals(state)){
                SdDevices sdDevices = new SdDevices();
                sdDevices.setEqId(eqId);
                deviceDataService.updateDeviceData(sdDevices,state,itemId);
            }
        }

    }


//    /**
//     * 保存实时数据
//     * 普通点位配置解析：不需要计算公式
//     * @param pointList 设备点位列表
//     * @param dataList 设备实时数据列表
//     * @param startAddress 起始地址
//     */
//    private void addDeviceData(List<SdDevicePoint> pointList,List<String> dataList,String startAddress){
////        //点位跟数据匹配
////        if(pointList.size() == dataList.size()){
////            addDeviceData(pointList,dataList);
////            return;
////        }
////        //点位与数据不匹配
//        Integer addressCursor = Integer.valueOf(startAddress);
//        for(int i = 0, j = 0; i < dataList.size(); i++){
//            SdDevicePoint devicePoint = pointList.get(j);
//            //设备ID
//            String eqId = devicePoint.getEqId();
//            //设备类型数据项ID
//            Long itemId = devicePoint.getItemId();
//            //点位地址
//            Integer pointAddress = Integer.valueOf(devicePoint.getAddress());
//            //点位长度
//            Integer dataLength = Integer.valueOf(devicePoint.getDataLength());
////            log.info("待修改的实时数据：eqId="+eqId+",点位地址="+pointAddress+",itemId="+itemId);
//            if(addressCursor.equals(pointAddress)){
//                //地址匹配，组装实时数据
//                String data = dataList.get(i);
//
//                //更新实时数据
//                SdDevices sdDevices = new SdDevices();
//                sdDevices.setEqId(eqId);
//                //根据设备点位ID、设备实时状态查询平台定义的设备状态
//                //todo 如果是普通数据（不需要映射状态），需要多走一遍查询，待优化
//                // todo 获取点位映射状态代码待优化,改为json字段配置
//                data = getPointState(devicePoint,data);
//                //精确2位小数
//                data = getRoundData(data);
////                log.info("修改实时数据：eqId="+eqId+",点位地址="+pointAddress+",实时数据="+data+",itemId="+itemId);
//                deviceDataService.updateDeviceData(sdDevices,data,itemId);
//
//                //如果匹配不成功，进入下次循环继续匹配
//                j++;
//            }
//
//            addressCursor = addressCursor + dataLength;
//        }
//
//    }
//
//    /**
//     * 保存实时数据
//     * @param pointList 设备点位列表
//     * @param dataList 设备实时数据列表
//     */
//    private void addDeviceData(List<SdDevicePoint> pointList,List<String> dataList){
////        List<SdDeviceData> deviceDataList = new ArrayList<>();
//        for(int i = 0; i < pointList.size(); i++){
//            SdDevicePoint devicePoint = pointList.get(i);
//            //设备ID
//            String eqId = devicePoint.getEqId();
//            //设备类型数据项ID
//            Long itemId = devicePoint.getItemId();
//            //实时数据
//            String data = dataList.get(i);
////            SdDeviceData deviceData = new SdDeviceData();
////            deviceData.setDeviceId(eqId);
////            deviceData.setItemId(itemId);
////            deviceData.setData(data);
//
//            //更新实时数据
//            SdDevices sdDevices = new SdDevices();
//            sdDevices.setEqId(eqId);
//            deviceDataService.updateDeviceData(sdDevices,data,itemId);
////            deviceDataList.add(deviceData);
//        }
//
////        if(deviceDataList.size() > 0){
////            //批量新增实时数据
////            deviceDataService.insertDeviceDataBatch(deviceDataList);
////        }
//
//    }


//    /**
//     * 根据设备点位ID、设备实时状态查询平台定义的设备状态
//     * @param devicePoint 设备点位
//     * @param data 设备实时状态
//     * @return
//     */
//    public String getPointState(SdDevicePoint devicePoint,String data){
//        String eqState;
//        //查询对应的设备状态
//        SdDevicePointState sdDevicePointState = new SdDevicePointState();
//        sdDevicePointState.setDevicePointId(devicePoint.getId());
//        //设备实际状态
//        sdDevicePointState.setControlState(data);
//        List<SdDevicePointState> pointStateList = devicePointStateService.selectSdDevicePointStateList(sdDevicePointState);
//        if(pointStateList != null && pointStateList.size() > 0){
//            sdDevicePointState = pointStateList.get(0);
//          Long deviceStateId = sdDevicePointState.getDeviceStateId();
//          SdEquipmentState equipmentState = equipmentStateService.selectSdEquipmentStateById(deviceStateId);
//          //平台定义的设备状态
//          eqState = equipmentState.getDeviceState();
//        }else{
////         无配置信息默认原始数据
//            eqState = data;
//        }
//
//        return eqState;
//
//    }
//
//    /**
//     * 模拟量数据精确2位小数
//     * @return
//     */
//    private String getRoundData(String data){
//        String pointStr = ".";
//        String result;
//        if(data.indexOf(pointStr) > 0){
//            //包含小数
//            double d = Double.valueOf(data);
//            //2位小数
//            result = String.valueOf(Arith.round(d,2));
//        }else{
//            //状态数据直接返回原数据
//            result = data;
//        }
//
//        return result;
//    }
}
