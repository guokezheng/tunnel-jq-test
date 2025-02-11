package com.tunnel.platform.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.datacenter.domain.enumeration.TunnelEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.informationBoard.IotDeviceAccess;
import com.tunnel.business.domain.informationBoard.SdIotDevice;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.informationBoard.IIotDeviceAccessService;
import com.tunnel.business.service.informationBoard.ISdIotDeviceService;
import com.tunnel.platform.business.vms.device.DataUtils;
import com.tunnel.platform.business.vms.device.DeviceManagerFactory;
import com.tunnel.platform.controller.informationBoard.BoardController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * describe: 情报板定时任务
 *
 * @author zs
 * @date 2023/8/25
 */
@Component("VmsTask")
public class VmsTask {

    @Autowired
    private ISdIotDeviceService sdIotDeviceService;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private SdDeviceDataMapper deviceDataMapper;

    @Autowired
    private IIotDeviceAccessService iotDeviceAccessService;

    /**
     * 线程池
     */
    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     *
     * 定时检测一次情报板的状态
     */
    public void getVmsData() {
        List<SdIotDevice> list = sdIotDeviceService.selectIotDeviceList(new SdIotDevice());
        for(int i=0;i<list.size();i++){
            SdIotDevice iotDevice = list.get(i);
            Long deviceId = iotDevice.getDeviceId();
            SdDevices device = sdDevicesService.getDeviceByAssociationDeviceId(deviceId);
            try {
                Map<String, String> boardData = DeviceManagerFactory.getInstance().getDeviceDisplayListByDeviceId(String.valueOf(iotDevice.getDeviceId()));
                iotDevice.setDeviceStatus("0");
                sdIotDeviceService.updateIotDevice(iotDevice);
                device.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
                device.setEqStatusTime(new Date());
                sdDevicesService.updateSdDevices(device);
                String result = boardData.get("result");
                String protocolType = boardData.get("vender");
                String jsonResult = DataUtils.itemContentToJson(result, protocolType);
                //回读消息
                BoardController.releaseContentMap.put(deviceId.toString(),result);
                //实时消息
                JSONArray resultObj = JSONArray.parseArray(jsonResult);
                for (int q = 0;q < resultObj.size();q++) {
                    JSONObject jsonObject = resultObj.getJSONObject(q);
                    int length = String.valueOf(q).length();
                    String num = "";
                    if (length < 3) {
                        int size = 3-length;
                        for (int j = 0;j < size;j++) {
                            num = num + "0";
                        }
                        num = num + String.valueOf(q);
                    } else if (length == 3) {
                        num = String.valueOf(q);
                    }
                    JSONObject object = jsonObject.getJSONArray("ITEM" + num).getJSONObject(0);
                    String content = object.getString("CONTENT");
                    if (content.startsWith("S0")) {
                        object.put("CONTENT", content.substring(3));
                    }
                }
                JSONObject items = new JSONObject();
                items.put("content", resultObj);
                IotDeviceAccess deviceAccess = iotDeviceAccessService.selectIotDeviceAccessById(deviceId);
                items.put("devicePixel", deviceAccess.getDevicePixel());
                BoardController.nowContentMap.put(deviceId.toString(), items.toString());
                //隧道内
                Long sdnCode = Long.valueOf(DevicesTypeItemEnum.SUI_DAO_NEI_CONTENT.getCode());
                //门架式
                Long mjsCode = Long.valueOf(DevicesTypeItemEnum.MEN_JIA_CONTENT.getCode());
                //查询情报板实时内容
                SdDeviceData sdDeviceData = new SdDeviceData();
                sdDeviceData.setDeviceId(device.getEqId());
                if("16".equals(device.getEqType().toString())){
                    sdDeviceData.setItemId(sdnCode);
                }else {
                    sdDeviceData.setItemId(mjsCode);
                }
                List<SdDeviceData> sdDeviceDataList = deviceDataMapper.selectSdDeviceDataList(sdDeviceData);
                //存入实时数据
                if("16".equals(device.getEqType().toString())){
                    setDeviceData(device.getEqId(),sdnCode,jsonResult,sdDeviceDataList);
                }else {
                    setDeviceData(device.getEqId(),mjsCode,jsonResult,sdDeviceDataList);
                }
            } catch (Exception e) {
                iotDevice.setDeviceStatus("1");
                sdIotDeviceService.updateIotDevice(iotDevice);
                if(device != null){
                    device.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
                    device.setEqStatusTime(new Date());
                    sdDevicesService.updateSdDevices(device);
                }
            }
        }

    }

    public void setDeviceData(String deviceId, Long typeId, String content, List<SdDeviceData> sdDeviceDataList){
        if(sdDeviceDataList.size() > 0){
            SdDeviceData sdDeviceData1 = sdDeviceDataList.get(0);
            sdDeviceData1.setData(content);
            sdDeviceData1.setUpdateTime(DateUtils.getNowDate());
            deviceDataMapper.updateKafkaDeviceData(sdDeviceData1);
        }else {
            SdDeviceData sdDeviceData1 = new SdDeviceData();
            sdDeviceData1.setData(content);
            sdDeviceData1.setItemId(typeId);
            sdDeviceData1.setDeviceId(deviceId);
            sdDeviceData1.setCreateTime(DateUtils.getNowDate());
            deviceDataMapper.insertSdDeviceData(sdDeviceData1);
        }
    }

    /*@PostConstruct
    public void init() {
        threadPoolTaskExecutor.execute(()->{
            getVmsData();
        });
    }*/
}
