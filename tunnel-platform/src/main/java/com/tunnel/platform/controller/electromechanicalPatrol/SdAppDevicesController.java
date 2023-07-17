package com.tunnel.platform.controller.electromechanicalPatrol;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdEquipmentState;
import com.tunnel.business.domain.dataInfo.SdEquipmentStateIconFile;
import com.tunnel.business.domain.dataInfo.SdEquipmentType;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdEquipmentStateService;
import com.tunnel.business.service.dataInfo.ISdEquipmentTypeService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.generalcontrol.service.GeneralControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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

    @Autowired
    private GeneralControlService generalControlService;

    @Autowired
    private ISdEquipmentStateService sdEquipmentStateService;

    @Autowired
    private ISdDeviceDataService sdDeviceDataService;



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
        List<SdDevices> stateNum = new ArrayList<>();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        int count = devicesService.getAppDevicesCountList(param,eqType,eqStatus,deptId);
        if(count > 0){
            List<SdDevices> list = devicesService.getAppDevicesList(param,eqType,eqStatus,pageSize,pageNum);
            TableDataInfo devicesList = new TableDataInfo(list,count);
            stateNum = devicesService.getDevicesNum(param,eqType,eqStatus,pageSize,pageNum);
            map.put("devicesList",devicesList);
            map.put("stateNum",stateNum);
            return map;
        }
        map.put("devicesList", null);
        map.put("stateNum",stateNum);
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

            SdEquipmentState sdEquipmentState = new SdEquipmentState();
            sdEquipmentState.setStateTypeId(eqList.get(0).getEqType());
            sdEquipmentState.setIsControl(1);
            sdEquipmentState.setStateType("2");

            // 获取设备可控
            List<SdEquipmentState> list = sdEquipmentStateService.selectSdEquipmentStateListGroupByStateType(sdEquipmentState);
            map.put("stateList",list);

            // 设备参数
            List<Map> itemData = sdDeviceDataService.getItemDataByEqId(eqId);
            map.put("itemData",itemData);

            eqList.get(0).setParams(map);
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

    /**
     * app端  查询控制执行器关联设备列表
     *
     * @param eqId 设备编号
     * @param state 设备状态
     * @param brightness 亮度
     * @param frequency 频率
     * @param fireMark 标号位置信息
     *
     * @return
     */
    @GetMapping("/app/controlDevice")
    public AjaxResult controlDevice(String eqId,String state,String brightness,String frequency,String fireMark){

        if(eqId == null){
            return AjaxResult.error("设备Id不能为空");
        }

        if(state == null){
            return AjaxResult.error("状态信息能为空");
        }

        SdDevices sdDevices = devicesService.selectSdDevicesById(eqId);

        if(Integer.parseInt(brightness) < 30 && state.equals("1") && DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().equals(sdDevices.getEqType())){
            return AjaxResult.error("基本照明亮度不得低于30");
        }
        //设备控制
        GeneralControlBean generalControlBean = generalControlService.getProtocolBean(sdDevices);
        if(generalControlBean == null){
            return AjaxResult.error("设备协议配置为空");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("state",state);
        map.put("brightness",brightness);
        map.put("frequency",frequency);
        map.put("fireMark",fireMark);

        AjaxResult ajaxResult = generalControlBean.control(map,sdDevices);

        return ajaxResult;
    }


    /**
     * app端  查询控制执行器关联设备列表
     * @return
     */
    @GetMapping("/app/getMcaList")
    public Result getMcaList(){



        return Result.success(devicesService.getMcaList());
    }

}
