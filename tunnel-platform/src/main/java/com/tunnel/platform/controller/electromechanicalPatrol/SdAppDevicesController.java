package com.tunnel.platform.controller.electromechanicalPatrol;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.Result;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdEquipmentType;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdEquipmentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * app端设备Controller
 * 
 * @author tjw
 * @date 2022-11-29
 */
@RestController
@RequestMapping("/devices/list")
public class SdAppDevicesController extends BaseController
{

    @Autowired
    private ISdDevicesService devicesService;

    @Autowired
    private ISdEquipmentTypeService equipmentTypeService;



    /**
     * app端  查询设备类型
     * @return
     */
    @PostMapping("/app/getDevicesType")
    public Result getDevicesType(){
        List<SdEquipmentType> eqTypeList = equipmentTypeService.getDevicesType();
        return Result.success(eqTypeList);
    }


    /**
     * app端获取设备列表
     * （1-在线，2-离线，3-故障）
     * @param param
     * @param eqType
     * @param eqStatus
     * @return
     */
    @PostMapping("/app/getAppDevicesList")
    public Result getAppDevicesList(String param,String eqType,String eqStatus){

        Map<String,Object>map = devicesService.getAppDevicesList(param,eqType,eqStatus);
        return Result.success(map);
    }

    /**
     * app端获取设备信息
     * @param eqId
     * @return
     */
    @PostMapping("/app/getAppDevicesInfo")
    public Result getAppDevicesInfo(String eqId){
        Map<String,Object>map = new HashMap<>();
        List<SdDevices> eqList = devicesService.getAppDevicesInfo(eqId);
        List<SdDevices> statusList = devicesService.getAppDevicesStatus(eqId);
        if(statusList!=null&&statusList.size()>0){
            eqList.get(0).setEqStatus(statusList.get(0).getEqStatus());
            eqList.get(0).setRunStatus(statusList.get(0).getRunStatus());
        }
        return Result.success(eqList);
    }



}
