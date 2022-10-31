package com.tunnel.platform.controller.workspace;

import cn.hutool.json.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.service.dataInfo.ISdDeviceCmdService;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.digitalmodel.ISdRadarDetectDataService;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.deal.guidancelamp.control.util.GuidanceLampHandle;
import com.tunnel.deal.plc.modbus.ModbusTcpHandle;
import com.tunnel.platform.service.SdDeviceControlService;
import com.tunnel.platform.service.SdOptDeviceService;
import com.zc.common.core.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作台
 *
 * @author
 * @date 2020-08-24
 */
@RestController
@RequestMapping("/workspace")
public class workspaceController extends BaseController {
    @Autowired
    private ISdEventService sdEventService;
    @Autowired
    private ISdDeviceCmdService sdDeviceCmdService;
    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private static RedisCache redisCache;
    @Autowired
    private ISdTunnelsService sdTunnelsService;
    @Autowired
    private ISdDeviceDataService sdDeviceDataService;
    @Autowired
    private ISdOperationLogService sdOperationLogService;
    @Autowired
    private ISdRadarDetectDataService sdRadarDetectDataService;
    @Autowired
    private SdDeviceControlService sdDeviceControlService;
    @Autowired
    private SdOptDeviceService sdOptDeviceService;


    @Value("${authorize.name}")
    private String deploymentType;

    //3d测试
    @PostMapping("/test")
    public String test() {
        JSONObject object = new JSONObject();
        object.put("dataList", 11111);
        WebSocketService.broadcast("dataList", object);
        return "get 3d info";
    }

    //PLC车指控制接口
    @PostMapping("/controlDevice")
    public AjaxResult controlDevice(@RequestBody Map<String, Object> map) {
        if (map.get("devId") == null || map.get("devId").toString().equals("")) {
            throw new RuntimeException("未指定设备，请联系管理员");
        } else if (map.get("state") == null || map.get("state").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的状态信息，请联系管理员");
        }

        if ("GSY".equals(deploymentType)) {
            sdOptDeviceService.optSingleDevice(map);
            return AjaxResult.success(1);
        }


        String devId = map.get("devId").toString();
        String state = map.get("state").toString();
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);
        //获取当前设备状态
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(devId);
        sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode()));
        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
        //控制设备
        int controlState = ModbusTcpHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices);
        //添加操作记录
        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(sdDevices.getEqType());
        sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
        sdOperationLog.setEqId(sdDevices.getEqId());
        sdOperationLog.setCreateTime(new Date());
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        sdOperationLog.setOperIp(ip);
        if (data.size() > 0 && data.get(0) != null) {
            sdOperationLog.setBeforeState(data.get(0).getData());
        }
        sdOperationLog.setOperationState(state);
        sdOperationLog.setControlType("0");
        sdOperationLog.setState(String.valueOf(controlState));
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
        return AjaxResult.success(controlState);
    }

    //诱导灯控制接口
    @PostMapping("/controlGuidanceLampDevice")
    public AjaxResult controlGuidanceLampAndEvacuationSignDevice(@RequestBody Map<String, Object> map) {
        if (map.get("devId") == null || map.get("devId").toString().equals("")) {
            throw new RuntimeException("未指定设备，请联系管理员");
        } else if (map.get("state") == null || map.get("state").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的状态信息，请联系管理员");
        } else if (map.get("brightness") == null || map.get("brightness").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的亮度信息，请联系管理员");
        } else if (map.get("frequency") == null || map.get("frequency").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的频率信息，请联系管理员");
        }

        if ("GSY".equals(deploymentType)) {
            sdOptDeviceService.optSingleDevice(map);
            return AjaxResult.success();
        }
        String devId = map.get("devId").toString();
        String state = map.get("state").toString();
        String brightness = map.get("brightness").toString();
        String frequency = map.get("frequency").toString();
        String fireMark = "";
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);
        //获取当前设备状态
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(devId);
        sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode()));
        if (sdDevices.getEqType().longValue() == DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode().longValue()) {
            if (map.get("fireMark") == null || map.get("fireMark").toString().equals("")) {
                throw new RuntimeException("未指定设备需要变更的标号位置信息，请联系管理员");
            } else {
                sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()));
                fireMark = map.get("fireMark").toString();
            }
        }
        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
        //控制设备
        int controlState = GuidanceLampHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices, brightness, frequency, fireMark);
        //添加操作记录
        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(sdDevices.getEqType());
        sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
        sdOperationLog.setEqId(sdDevices.getEqId());
        sdOperationLog.setCreateTime(new Date());
        if (data.size() > 0 && data.get(0) != null) {
            sdOperationLog.setBeforeState(data.get(0).getData());
        }
        sdOperationLog.setOperationState(state);
        sdOperationLog.setControlType("0");
        sdOperationLog.setState(String.valueOf(controlState));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        sdOperationLog.setOperIp(ip);
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
        return AjaxResult.success(controlState);
    }

    @PostMapping("/vehicleMonitoringInRecent24Hours")
    public AjaxResult vehicleMonitoringInRecent24Hours(@RequestBody Map<String, Object> map) {
        if (map == null || map.isEmpty() || map.get("tunnelId") == null || map.get("tunnelId").toString().equals("")) {
            throw new RuntimeException("车辆监测查询条件中隧道不能为空");
        }
        List<Map<String, Object>> vehicleMonitoringInRecent24Hours = sdRadarDetectDataService.vehicleMonitoringInRecent24Hours(map.get("tunnelId").toString());
        return AjaxResult.success(vehicleMonitoringInRecent24Hours);
    }

    /**
     * 根据隧道id,方向,所属车道筛选车道指示器
     *
     * @return
     */
    @PostMapping("/batchControlCarFinger")
    public AjaxResult batchControlCarFinger(@RequestBody Map<String, Object> carFingerDevices) {
        //查询所有需要批量控制的车指设备
        if (carFingerDevices == null || carFingerDevices.isEmpty()) {
            throw new RuntimeException("车指批量控制设备信息为空");
        } else if (carFingerDevices.get("tunnelId") == null || carFingerDevices.get("tunnelId").toString().equals("")) {
            throw new RuntimeException("车指批量控制隧道信息为空");
        } else if (carFingerDevices.get("direction") == null || carFingerDevices.get("direction").toString().equals("")) {
            throw new RuntimeException("车指批量控制隧道方向信息为空");
        } else if (carFingerDevices.get("state") == null || carFingerDevices.get("state").toString().equals("")) {
            throw new RuntimeException("车指批量控制状态信息为空");
        }
        List<SdDevices> list = sdDevicesService.batchControlCarFinger(carFingerDevices);
        Map<String, Object> map = new HashMap<>();
        Integer controlDevices = 0;
        for (int i = 0; i < list.size(); i++) {
            String eqId = list.get(i).getEqId();
            String state = carFingerDevices.get("state").toString();
            map.put("devId", eqId);
            map.put("state", state);
            map.put("controlType", "0");
            controlDevices = sdDeviceControlService.controlDevices(map);
        }
        return AjaxResult.success(controlDevices);
    }

    @GetMapping("/getDeviceDataAndState")
    public AjaxResult selectDeviceDataAndState(String tunnelId) {
        return AjaxResult.success(sdDevicesService.getDeviceAndState(tunnelId));
    }


    @PostMapping("/commonControl")
    public Integer commonControl(@RequestBody Map<String, Object> params) {
        //参数校验
        Assert.notEmpty(params, "控制设备参数为空");
        String devId = (String) params.get("devId");
        String state = (String) params.get("state");
        Assert.hasText(devId, "设备参数{devId}必传");
        Assert.hasText(state, "设备控制状态参数{state}必传");

        Integer controlState  = sdDeviceControlService.controlDevices(params);
        return controlState;
    }

}
