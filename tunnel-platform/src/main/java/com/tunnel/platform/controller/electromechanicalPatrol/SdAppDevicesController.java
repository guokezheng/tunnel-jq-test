package com.tunnel.platform.controller.electromechanicalPatrol;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdEquipmentStateIconFile;
import com.tunnel.business.domain.dataInfo.SdEquipmentType;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdEquipmentTypeService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/app/devices/list")
public class SdAppDevicesController extends BaseController
{

    @Autowired
    private ISdDevicesService devicesService;

    @Autowired
    private ISdEquipmentTypeService equipmentTypeService;

    @Autowired
    private ISdOperationLogService sdOperationLogService;



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
    public Map<String,Object> getAppDevicesList(String param,String eqType,String eqStatus,Integer pageSize,Integer pageNum){
        Map<String,Object>map = new HashMap();
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        int count = devicesService.getAppDevicesCountList(param,eqType,eqStatus,deptId);
        if(count > 0){
            List<SdDevices> list = devicesService.getAppDevicesList(param,eqType,eqStatus,pageSize,pageNum);
            TableDataInfo devicesList = new TableDataInfo(list,count);
            List<SdDevices> stateNum = devicesService.getDevicesNum(param,eqType,eqStatus,pageSize,pageNum);
            map.put("devicesList",devicesList);
            map.put("stateNum",stateNum);
            return map;
        }

        return map;





        //Map<String,Object>map = devicesService.getAppDevicesList(param,eqType,eqStatus);
        //return Result.success(map);
    }

    /**
     * app端获取设备信息
     * @param eqId
     * @return
     */
    @GetMapping("/app/getAppDevicesInfo")
    public Result getAppDevicesInfo(String eqId){
        Map<String,Object>map = new HashMap<>();
        List<SdDevices> eqList = devicesService.getAppDevicesInfo(eqId);
        List<SdDevices> statusList = devicesService.getAppDevicesStatus(eqId);
        if(statusList!=null&&statusList.size()>0){
            eqList.get(0).setEqStatus(statusList.get(0).getEqState());
            eqList.get(0).setRunStatus(statusList.get(0).getRunState());
            eqList.get(0).setUpdateTime(statusList.get(0).getUpdateTime());
            eqList.get(0).setiFileList(statusList.get(0).getiFileList());
        }

        return Result.success(eqList);
    }

    /**
     * app端获取操控日志
     * @param time
     * @return
     */
    @PostMapping("/app/logList")
    public TableDataInfo getLogList(String eqId,String time,Integer pageSize,Integer pageNum){
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        int count = sdOperationLogService.selectAppOperationLogCountList(eqId,time,deptId);
        if(count > 0){
            List<SdOperationLog> list = sdOperationLogService.selectAppOperationLogList(eqId,time,deptId,pageSize,pageNum);
            return new TableDataInfo(list,count);
        }

        return new TableDataInfo(null,0);





    }

}
