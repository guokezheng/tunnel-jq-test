package com.tunnel.platform.controller.workspace;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.service.ISysDictDataService;
import com.tunnel.business.datacenter.domain.enumeration.*;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceTypeItem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.domain.vehicle.SdVehicleData;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDeviceTypeItemMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.service.dataInfo.*;
import com.tunnel.business.service.digitalmodel.ISdRadarDetectDataService;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.business.service.vehicle.ISdVehicleDataService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.generalcontrol.service.GeneralControlService;
import com.tunnel.deal.guidancelamp.control.util.GuidanceLampHandle;
import com.tunnel.deal.plc.modbus.ModbusTcpHandle;
import com.tunnel.deal.warninglightstrip.WarningLightStripHandle;
import com.tunnel.platform.service.SdDeviceControlService;
import com.tunnel.platform.service.SdOptDeviceService;
import com.tunnel.platform.service.deviceControl.HongMengDevService;
import com.tunnel.platform.service.deviceControl.LightService;
import com.tunnel.platform.service.deviceFunctions.DeviceFunctionsService;
import com.zc.common.core.websocket.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

/**
 * 工作台
 *
 * @author
 * @date 2020-08-24
 */
@RestController
@RequestMapping("/workspace")
public class workspaceController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(workspaceController.class);

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
    @Autowired
    private ISysDictDataService sysDictDataService;
    @Autowired
    private ISdDeviceTypeItemService sdDeviceTypeItemService;
    @Autowired
    private LightService lightService;
    @Autowired
    private DeviceFunctionsService deviceFunctionsService;

    @Autowired
    private ISdVehicleDataService vehicleDataService;

    @Value("${authorize.name}")
    private String deploymentType;

    /**
     * 高速云端是否可控
     */
    @Value("${platform.control}")
    private String platformControl;

    @Autowired
    private HongMengDevService hongMengDevService;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Autowired
    private SdDeviceTypeItemMapper sdDeviceTypeItemMapper;

    @Autowired
    private SdDeviceControlService deviceControlService;

    @Autowired
    private GeneralControlService generalControlService;


    //3d测试
    @PostMapping("/test")
    public String test() {
        JSONObject object = new JSONObject();
        object.put("dataList", 11111);
        WebSocketService.broadcast("dataList", object);
        return "get 3d info";
    }

    //todo 所有走这个接口的设备类型

    /**
     * 设备控制接口
     * @param map
     * @return
     */
    @PostMapping("/generalControlDevice")
    public AjaxResult generalControlDevice(@RequestBody Map<String, Object> map) {
        //设备ID
        String devId = Optional.ofNullable(map.get("devId")).orElse("").toString();

        if (devId == null || "".equals(devId)) {
            AjaxResult.error("未指定设备");
        }
        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
        if (state == null || "".equals(state)) {
            AjaxResult.error("未指定设备需要变更的状态信息");
        }
        //设备信息
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);

        //查询配置状态是否是模拟控制
        boolean isopen = deviceControlService.queryAnalogControlConfig();
        if (isopen) {
            //设备模拟控制开启
            return deviceControlService.excecuteAnalogControl(sdDevices,map);
        }

        if ("GSY".equals(deploymentType)) {
            //高速云分发控制
         return deviceControlService.cloudPlatformControl(map);
        }

        //控制设备之前获取设备状态
        String beforeState = deviceControlService.selectBeforeState(sdDevices);
        String controlState = OperationLogEnum.STATE_ERROR.getCode();
        //设备控制
        GeneralControlBean generalControlBean = generalControlService.getProtocolBean(sdDevices);
        AjaxResult ajaxResult = generalControlBean.control(map,sdDevices);
        Integer code = Integer.valueOf(String.valueOf(ajaxResult.get("code")));
        if( code == HttpStatus.SUCCESS){
            controlState = OperationLogEnum.STATE_SUCCESS.getCode();
            ajaxResult.put("data",controlState);
        }
        //添加操作日志
        deviceControlService.addOperationLog(sdDevices,state,beforeState,controlState);
        return ajaxResult;
    }


    //PLC车指控制接口
    @PostMapping("/controlDevice")
    public AjaxResult controlDevice(@RequestBody Map<String, Object> map) {
        if (map.get("devId") == null || map.get("devId").toString().equals("")) {
            throw new RuntimeException("未指定设备，请联系管理员");
        } else if (map.get("state") == null || map.get("state").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的状态信息，请联系管理员");
        }

        //解析map 杭山东隧道下调用瑞华赢接口控制设备
        String devId = map.get("devId").toString();
        String state = map.get("state").toString();
        String brightness = map.get("brightness") == null ? "" : map.get("brightness").toString();
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);

        // 基本照明 亮度 不得小于30
        if((map.get("brightness") == null || Integer.parseInt(brightness) < 30) && state.equals("1")  && DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().equals(sdDevices.getEqType())){
            return AjaxResult.error("基本照明亮度不得低于30");
        }

        if(TunnelEnum.HANG_SHAN_DONG.getCode().equals(sdDevices.getEqTunnelId()) && DevicesHongTypeEnum.contains(sdDevices.getEqType()) && "AGREE".equals(platformControl)){
            Map<String, String> hongMap = hongMengDevService.updateHua(devId, state);
            Integer code = Integer.valueOf(hongMap.get("code"));
            String msg = hongMap.get("msg").toString();
            if(code == 200){
                return AjaxResult.success(1);
            }else {
                return AjaxResult.success(msg,0);
            }
        }
        if ("GSY".equals(deploymentType)) {
            map.put("controlType", "0");
            map.put("operIp", IpUtils.getIpAddr(ServletUtils.getRequest()));
            sdOptDeviceService.optSingleDevice(map);
            return AjaxResult.success(1);
        }

        //控制设备之前获取设备状态
        //获取当前设备状态
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(devId);
        sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode()));
        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
//        String beforeState = deviceControlService.selectBeforeState(sdDevices);
        //控制设备
//        int controlState = ModbusTcpHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices);
        int controlState = 0;
        //查询配置状态是否是模拟控制
        boolean isopen = deviceControlService.queryAnalogControlConfig();
        long eqType = sdDevices.getEqType().longValue();

        //加强照明状态拼接
        String linState = "";
        if (isopen) {
            //设备模拟控制开启
//            return deviceControlService.excecuteAnalogControl(sdDevices,map);
            //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
            sdDevices.setEqStatus("1");
            sdDevices.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(sdDevices);
            SdDeviceTypeItem sdDeviceTypeItem = new SdDeviceTypeItem();
            sdDeviceTypeItem.setDeviceTypeId(sdDevices.getEqType());
            List<SdDeviceTypeItem> sdDeviceTypeItems = sdDeviceTypeItemService.selectSdDeviceTypeItemList(sdDeviceTypeItem);
            if (sdDeviceTypeItems.size() == 0) {
                throw new RuntimeException("当前设备没有设备类型数据项数据，请添加后重试！");
            }

            if(DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().equals(sdDevices.getEqType()) || DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().equals(sdDevices.getEqType())){
                sdDeviceTypeItems.stream().forEach(item -> {
                    if("brightness".equals(item.getItemCode()) &&
                            (
                                    DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().equals(sdDevices.getEqType()) ||
                                    DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().equals(sdDevices.getEqType())
                            )
                    ){
                        updateDeviceData(sdDevices, map.get("brightness").toString(), Integer.parseInt(item.getId().toString()));
                    }
                    if("state".equals(item.getItemCode())){
                        updateDeviceData(sdDevices, state, Integer.parseInt(item.getId().toString()));
                    }
                });
                brightness = brightness == null || "".equals(brightness) ? "0" : brightness;
                linState = state.equals("1")?"开启":"关闭";
                linState += "，亮度："+brightness + "%";
            }else {
                SdDeviceTypeItem typeItem = sdDeviceTypeItems.get(0);
                updateDeviceData(sdDevices, state, Integer.parseInt(typeItem.getId().toString()));
            }
            //添加操作记录
            SdOperationLog sdOperationLog = new SdOperationLog();
            sdOperationLog.setEqTypeId(sdDevices.getEqType());
            sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
            sdOperationLog.setEqId(sdDevices.getEqId());
            sdOperationLog.setCreateTime(new Date());
            if(DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().equals(sdDevices.getEqType()) || DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().equals(sdDevices.getEqType())){
                sdOperationLog.setOperationState(linState);
            }else {
                sdOperationLog.setOperationState(state);
            }
            sdOperationLog.setControlType("0");
            sdOperationLog.setState("1");
            sdOperationLog.setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
            sdOperationLogService.insertSdOperationLog(sdOperationLog);
            return AjaxResult.success(1);
        } else {
            // eqType == DevicesTypeEnum.SHUI_BENG.getCode().longValue() ||
//            if (
//                    eqType == DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode().longValue() ||
//                    eqType == DevicesTypeEnum.ZHUO_ZHUAN_CHE_ZHI.getCode().longValue() ||
//                    eqType == DevicesTypeEnum.JIAO_TONG_XIN_HAO_DENG.getCode().longValue() ||
//                    eqType == DevicesTypeEnum.ZUO_JIAO_TONG_XIN_HAO_DENG.getCode().longValue() ||
//                    eqType == DevicesTypeEnum.YING_JI_ZHAO_MING.getCode().longValue() ||
//                    eqType == DevicesTypeEnum.YIN_DAO_ZHAO_MING.getCode().longValue() ||
//                    eqType == DevicesTypeEnum.FENG_JI.getCode().longValue() ||
//                    eqType == DevicesTypeEnum.JUAN_LIAN_MEN.getCode().longValue() ||
//                    eqType == DevicesTypeEnum.SHENG_GUANG_BAO_JING.getCode().longValue()
//            ) {
//                //控制设备
//                controlState = ModbusTcpHandle.getInstance().toControlDev(Integer.parseInt(state), sdDevices);
//            } else if (eqType == DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().longValue() || eqType == DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().longValue()) {
//
//                controlState = lightService.lineControl(devId, Integer.parseInt(state), Integer.parseInt(brightness));
//            }

            //设备控制
            GeneralControlBean generalControlBean = generalControlService.getProtocolBean(sdDevices);
            if(generalControlBean == null){
                return AjaxResult.error("设备协议配置为空");
            }
            AjaxResult ajaxResult = generalControlBean.control(map,sdDevices);
            Integer code = Integer.valueOf(String.valueOf(ajaxResult.get("code")));
            if( code == HttpStatus.SUCCESS){
                controlState = Integer.valueOf(OperationLogEnum.STATE_SUCCESS.getCode());
//                ajaxResult.put("data",controlState);
            }
        }

        //添加操作记录
        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(sdDevices.getEqType());
        sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
        sdOperationLog.setEqId(sdDevices.getEqId());
        sdOperationLog.setCreateTime(new Date());
        if (data.size() > 0 && data.get(0) != null) {
            sdOperationLog.setBeforeState(data.get(0).getData());
        }
//        sdOperationLog.setBeforeState(beforeState);
        if(DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().equals(sdDevices.getEqType()) || DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().equals(sdDevices.getEqType())){
            if(brightness != null){
                linState = "1".equals(state) ?"开启":"关闭";
                linState += "，亮度："+brightness + "%";
            }
            sdOperationLog.setOperationState(linState);
        }else {
            sdOperationLog.setOperationState(state);
        }
        sdOperationLog.setControlType("0");
        sdOperationLog.setState(String.valueOf(controlState));
        sdOperationLog.setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sdOperationLogService.insertSdOperationLog(sdOperationLog);


//        deviceControlService.addOperationLog(sdDevices,state,beforeState,String.valueOf(controlState));
        return AjaxResult.success(controlState);
    }

    //诱导灯控制接口
    @PostMapping("/controlGuidanceLampDevice")
    public AjaxResult controlGuidanceLampDevice(@RequestBody Map<String, Object> map) {
        if (map.get("devId") == null || map.get("devId").toString().equals("")) {
            throw new RuntimeException("未指定设备");
        } else if (map.get("state") == null || map.get("state").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的状态信息");
        } else if (map.get("brightness") == null || map.get("brightness").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的亮度信息");
        } else if (map.get("frequency") == null || map.get("frequency").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的频率信息");
        }
        if ("GSY".equals(deploymentType)) {
            map.put("controlType", "0");
            map.put("operIp", IpUtils.getIpAddr(ServletUtils.getRequest()));
            sdOptDeviceService.optSingleDevice(map);
            return AjaxResult.success();
        }
        String devId = map.get("devId").toString();
        String state = map.get("state").toString();
        String fDeviceState = state;
        String brightness = map.get("brightness").toString();
        String frequency = map.get("frequency").toString();
        String fireMark = "";
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);
        String fEqId = devId;
        if (!sdDevices.getFEqId().equals("") || sdDevices.getFEqId() != null) {
            //当前诱导灯和疏散标志在工作台上展示的都是从部件，需要查询主机的信息进行IP和端口的获取
            fEqId = sdDevices.getFEqId();
            sdDevices = sdDevicesService.selectSdDevicesById(sdDevices.getFEqId());
        }
        //获取当前设备状态
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode()));
        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);

        //根据字典中配置的设备模拟控制值进行模拟状态展示
        List<SysDictData> isopenList = sysDictDataService.getSysDictDataByDictType("sys_analog_control_isopen");
        if (isopenList.size() == 0) {
            throw new RuntimeException("设备模拟控制是否开启字典值不存在，请添加后重试");
        }
        SysDictData sysDictData = isopenList.get(0);
        String isopen = sysDictData.getDictValue();
        if (isopen != null && !isopen.equals("") && isopen.equals("1")) {
            //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
            sdDevices.setEqStatus("1");
            sdDevices.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(sdDevices);
            if (fEqId != null && !"".equals(fEqId)) {
                SdDevices devices = new SdDevices();
                devices.setEqStatus("1");
                devices.setEqStatusTime(new Date());
                devices.setFEqId(fEqId);
                sdDevicesService.updateSdDevicesByFEqId(sdDevices);
            }
            if (sdDevices.getEqType().longValue() == DevicesTypeEnum.YOU_DAO_DENG_CONTROL.getCode().longValue()) {
                //父级设备变更
                updateDeviceData(sdDevices, state, DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode());
                updateDeviceData(sdDevices, brightness, DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode());
                updateDeviceData(sdDevices, frequency, DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode());
                //子级设备变更
                SdDevices dev = new SdDevices();
                dev.setFEqId(fEqId);
                List<SdDevices> list = sdDevicesService.selectSdDevicesList(dev);
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        SdDevices devo = list.get(i);
                        updateDeviceData(devo, state, DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode());
                        updateDeviceData(devo, brightness, DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode());
                        updateDeviceData(devo, frequency, DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode());
                    }
                }
            }
            //添加操作记录
            SdOperationLog sdOperationLog = new SdOperationLog();
            sdOperationLog.setEqTypeId(sdDevices.getEqType());
            sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
            sdOperationLog.setEqId(sdDevices.getEqId());
            sdOperationLog.setCreateTime(new Date());
            if (data.size() > 0 && data.get(0) != null) {
                sdOperationLog.setBeforeState(data.get(0).getData());
            }
            sdOperationLog.setOperationState(fDeviceState);
            sdOperationLog.setControlType("0");
            sdOperationLog.setState("1");
            sdOperationLog.setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
            sdOperationLogService.insertSdOperationLog(sdOperationLog);
            return AjaxResult.success(1);
        }
        //控制设备
        int controlState = 0;
        if (sdDevices.getBrandId() != null && sdDevices.getBrandId().equals("0057")) {
            controlState = GuidanceLampHandle.getInstance().toControlDev(fEqId, Integer.parseInt(state), sdDevices, brightness, frequency, fireMark);
        } else if (sdDevices.getEqType().longValue() == DevicesTypeEnum.YOU_DAO_DENG_CONTROL.getCode().longValue() && !sdDevices.getBrandId().equals("0057")) {
            //显科的设备一个路段可能会有多个控制器，都需要下发同样的指令
            SdDevices xiankeDevices = new SdDevices();
            xiankeDevices.setEqTunnelId(sdDevices.getEqTunnelId());
            xiankeDevices.setEqType(sdDevices.getEqType());
            xiankeDevices.setEqDirection(sdDevices.getEqDirection());
            List<SdDevices> xiankeDeviceList = sdDevicesService.selectSdDevicesList(xiankeDevices);
            if (state.equals("3")) {
                state = "2";
            }
            for (int i = 0;i < xiankeDeviceList.size();i++) {
                SdDevices dev = xiankeDeviceList.get(i);
                if (dev.getIp() == null || dev.getIp().equals("")) {
                    continue;
                }
                controlState = GuidanceLampHandle.getInstance().toControlXianKeDev(dev.getEqId(), Integer.parseInt(state), dev, brightness, frequency);
            }
//            controlState = GuidanceLampHandle.getInstance().toControlXianKeDev(fEqId, Integer.parseInt(state), sdDevices, brightness, frequency);
        }
        //添加操作记录
        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(sdDevices.getEqType());
        sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
        sdOperationLog.setEqId(sdDevices.getEqId());
        sdOperationLog.setCreateTime(new Date());
        if (data.size() > 0 && data.get(0) != null) {
            sdOperationLog.setBeforeState(data.get(0).getData());
        }
        sdOperationLog.setOperationState(fDeviceState);
        sdOperationLog.setControlType("0");
        sdOperationLog.setState(String.valueOf(controlState));
        sdOperationLog.setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
        return AjaxResult.success(controlState);
    }

    //警示灯带控制接口
    @PostMapping("/controlWarningLightStripDevice")
    public AjaxResult controlWarningLightStripDevice(@RequestBody Map<String, Object> map) {
        if (map.get("devId") == null || map.get("devId").toString().equals("")) {
            throw new RuntimeException("未指定设备");
        } else if (map.get("state") == null || map.get("state").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的状态信息");
        }
        if ("GSY".equals(deploymentType)) {
            map.put("controlType", "0");
            map.put("operIp", IpUtils.getIpAddr(ServletUtils.getRequest()));
            sdOptDeviceService.optSingleDevice(map);
            return AjaxResult.success();
        }
        String devId = map.get("devId").toString();
        String state = map.get("state").toString();
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);
        //获取当前设备状态
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.JING_SHI_DENG_DAI.getCode()));
        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);

        //根据字典中配置的设备模拟控制值进行模拟状态展示
        List<SysDictData> isopenList = sysDictDataService.getSysDictDataByDictType("sys_analog_control_isopen");
        if (isopenList.size() == 0) {
            throw new RuntimeException("设备模拟控制是否开启字典值不存在，请添加后重试");
        }
        SysDictData sysDictData = isopenList.get(0);
        String isopen = sysDictData.getDictValue();
        if (isopen != null && !isopen.equals("") && isopen.equals("1")) {
            //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
            sdDevices.setEqStatus("1");
            sdDevices.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(sdDevices);
            if (sdDevices.getEqType().longValue() == DevicesTypeEnum.JING_SHI_DENG_DAI.getCode().longValue()) {
                updateDeviceData(sdDevices, state, DevicesTypeItemEnum.JING_SHI_DENG_DAI.getCode());
            }
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
            sdOperationLog.setState("1");
            sdOperationLog.setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
            sdOperationLogService.insertSdOperationLog(sdOperationLog);
            return AjaxResult.success(1);
        }
        //控制设备
        int controlState = WarningLightStripHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices);
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
        sdOperationLog.setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
        return AjaxResult.success(controlState);
    }

    //疏散标志控制接口
    @PostMapping("/controlEvacuationSignDevice")
    public AjaxResult controlEvacuationSignDevice(@RequestBody Map<String, Object> map) {
        if (map.get("devId") == null || map.get("devId").toString().equals("")) {
            throw new RuntimeException("未指定设备");
        } else if (map.get("state") == null || map.get("state").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的状态信息");
        } else if (map.get("brightness") == null || map.get("brightness").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的亮度信息");
        } else if (map.get("frequency") == null || map.get("frequency").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的频率信息");
        }
        if ("GSY".equals(deploymentType)) {
            map.put("controlType", "0");
            map.put("operIp", IpUtils.getIpAddr(ServletUtils.getRequest()));
            sdOptDeviceService.optSingleDevice(map);
            return AjaxResult.success();
        }
        String devId = map.get("devId").toString();
        String state = map.get("state").toString();
        String fDeviceState = state;
        String brightness = map.get("brightness").toString();
        String frequency = map.get("frequency").toString();
        String fireMark = "";
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);
        String fEqId = devId;
        if (!sdDevices.getFEqId().equals("") || sdDevices.getFEqId() != null) {
            //当前诱导灯和疏散标志在工作台上展示的都是从部件，需要查询主机的信息进行IP和端口的获取
            fEqId = sdDevices.getFEqId();
            sdDevices = sdDevicesService.selectSdDevicesById(sdDevices.getFEqId());
        }
        //获取当前设备状态
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        if (sdDevices.getEqType().longValue() == DevicesTypeEnum.SHU_SAN_BIAO_ZHI_CONTROL.getCode().longValue()) {
            if (state.equals("2") && map.get("fireMark").toString().equals("0") && map.get("fireMark") != null) {
                map.put("fireMark", "255");
            }
            if (map.get("fireMark") == null || map.get("fireMark").toString().equals("")) {
                throw new RuntimeException("未指定设备需要变更的标号位置信息");
            } else {
                sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()));
                fireMark = map.get("fireMark").toString();
            }
        }
        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);

        //根据字典中配置的设备模拟控制值进行模拟状态展示
        List<SysDictData> isopenList = sysDictDataService.getSysDictDataByDictType("sys_analog_control_isopen");
        if (isopenList.size() == 0) {
            throw new RuntimeException("设备模拟控制是否开启字典值不存在，请添加后重试");
        }
        SysDictData sysDictData = isopenList.get(0);
        String isopen = sysDictData.getDictValue();
        if (isopen != null && !isopen.equals("") && isopen.equals("1")) {
            //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
            sdDevices.setEqStatus("1");
            sdDevices.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(sdDevices);
            if (fEqId != null && !"".equals(fEqId)) {
                SdDevices devices = new SdDevices();
                devices.setEqStatus("1");
                devices.setEqStatusTime(new Date());
                devices.setFEqId(fEqId);
                sdDevicesService.updateSdDevicesByFEqId(sdDevices);
            }
            if (sdDevices.getEqType().longValue() == DevicesTypeEnum.SHU_SAN_BIAO_ZHI_CONTROL.getCode().longValue()) {
                //父级设备变更
                updateDeviceData(sdDevices, state, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                updateDeviceData(sdDevices, brightness, DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
                updateDeviceData(sdDevices, frequency, DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode());
                updateDeviceData(sdDevices, fireMark, DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                //子级设备变更
                SdDevices dev = new SdDevices();
                dev.setFEqId(fEqId);
                List<SdDevices> list = sdDevicesService.selectSdDevicesList(dev);
                if (!list.isEmpty()) {
                    //疏散标志关灯
                    if (fireMark.equals("0") && !fireMark.equals("255")) {
                        state = "1";
                        for (int i = 0;i < list.size();i++) {
                            SdDevices devo = list.get(i);
                            updateDeviceData(devo, state, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                            updateDeviceData(devo, brightness, DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
                            updateDeviceData(devo, frequency, DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode());
                            updateDeviceData(devo, fireMark, DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                        }
                    //疏散标志报警点更新
                    } else if (!fireMark.equals("0") && !fireMark.equals("255")) {
                        BigDecimal fMark = new BigDecimal(fireMark);
                        for (int i = 0;i < list.size();i++) {
                            SdDevices devices = list.get(i);
                            BigDecimal addressMark = new BigDecimal(devices.getQueryPointAddress());
                            if (fMark.compareTo(addressMark) < 0) {
                                state = "6";
                            } else if (fMark.compareTo(addressMark) == 0) {
                                state = "5";
                            } else if (fMark.compareTo(addressMark) > 0) {
                                state = "4";
                            }
                            updateDeviceData(devices, fireMark, DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                            updateDeviceData(devices, state, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                            updateDeviceData(devices, brightness, DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
                            updateDeviceData(devices, frequency, DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode());
                        }
                    } else {
                        //疏散标志开灯无报警点
                        state = "2";
                        for (int i = 0;i < list.size();i++) {
                            SdDevices devo = list.get(i);
                            updateDeviceData(devo, state, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                            updateDeviceData(devo, brightness, DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
                            updateDeviceData(devo, frequency, DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode());
                            updateDeviceData(devo, fireMark, DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                        }
                    }
                }
            }
            //添加操作记录
            SdOperationLog sdOperationLog = new SdOperationLog();
            sdOperationLog.setEqTypeId(sdDevices.getEqType());
            sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
            sdOperationLog.setEqId(sdDevices.getEqId());
            sdOperationLog.setCreateTime(new Date());
            if (data.size() > 0 && data.get(0) != null) {
                sdOperationLog.setBeforeState(data.get(0).getData());
            }
            sdOperationLog.setOperationState(fDeviceState);
            sdOperationLog.setControlType("0");
            sdOperationLog.setState("1");
            sdOperationLog.setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
            sdOperationLogService.insertSdOperationLog(sdOperationLog);
            return AjaxResult.success(1);
        }
        //控制设备
        int controlState = 0;
        if (sdDevices.getBrandId() != null && sdDevices.getBrandId().equals("0057")) {
            controlState = GuidanceLampHandle.getInstance().toControlDev(fEqId, Integer.parseInt(state), sdDevices, brightness, frequency, fireMark);
        }
        //添加操作记录
        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(sdDevices.getEqType());
        sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
        sdOperationLog.setEqId(sdDevices.getEqId());
        sdOperationLog.setCreateTime(new Date());
        if (data.size() > 0 && data.get(0) != null) {
            sdOperationLog.setBeforeState(data.get(0).getData());
        }
        sdOperationLog.setOperationState(fDeviceState);
        sdOperationLog.setControlType("0");
        sdOperationLog.setState(String.valueOf(controlState));
        sdOperationLog.setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
        return AjaxResult.success(controlState);
    }

    private void updateDeviceData(SdDevices sdDevices, String value, Integer itemId) {
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        sdDeviceData.setItemId(Long.valueOf(itemId));
        List<SdDeviceData> deviceData = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
        if (deviceData.size() > 0) {
            SdDeviceData data = deviceData.get(0);
            data.setData(value);
            data.setUpdateTime(new Date());
            sdDeviceDataService.updateSdDeviceData(data);
        } else {
            sdDeviceData.setData(value);
            sdDeviceData.setCreateTime(new Date());
            sdDeviceDataService.insertSdDeviceData(sdDeviceData);
        }
    }

    @PostMapping("/vehicleMonitoringInRecent24Hours")
    public AjaxResult vehicleMonitoringInRecent24Hours(@RequestBody Map<String, Object> map) {
        if (map == null || map.isEmpty() || map.get("tunnelId") == null || map.get("tunnelId").toString().equals("")) {
            throw new RuntimeException("车辆监测查询条件中隧道不能为空");
        }
        String tunnelId = String.valueOf(map.get("tunnelId"));
        //避免数据量大时查询过慢超时，改为从单车数据中查询
        SdVehicleData vehicleData = new SdVehicleData();
        vehicleData.setTunnelId(tunnelId);
        List<Map> list = vehicleDataService.getDayVehicleData(vehicleData);
//        List<Map<String, Object>> vehicleMonitoringInRecent24Hours = sdRadarDetectDataService.vehicleMonitoringInRecent24Hours(map.get("tunnelId").toString());
        return AjaxResult.success(list);
    }

    /**
     * 查询24小时客车、货车、重点车辆客流量
     * @param map
     * @return
     */
    @PostMapping("/vehicleMonitoringInRecent24HoursByVehicleType")
    public AjaxResult vehicleMonitoringInRecent24HoursByVehicleType(@RequestBody Map<String, Object> map) {
        if (map == null || map.isEmpty() || map.get("tunnelId") == null || map.get("tunnelId").toString().equals("")) {
            throw new RuntimeException("车辆监测查询条件中隧道不能为空");
        }
        String tunnelId = String.valueOf(map.get("tunnelId"));
        //避免数据量大时查询过慢超时，改为从单车数据中查询
        SdVehicleData vehicleData = new SdVehicleData();
        vehicleData.setTunnelId(tunnelId);
        List<Map> list = vehicleDataService.getDayVehicleDataByVehicleType(vehicleData);
        return AjaxResult.success(list);
    }

    /**
     * 统计当天24小时重点车辆
     * @param map
     * @return
     */
    @PostMapping("/specialVehicleMonitoringInRecent24Hours")
    public AjaxResult specialVehicleMonitoringInRecent24Hours(@RequestBody Map<String, Object> map) {
        if (map == null || map.isEmpty() || map.get("tunnelId") == null || map.get("tunnelId").toString().equals("")) {
            throw new RuntimeException("车辆监测查询条件中隧道不能为空");
        }
        List<Map<String, Object>> vehicleMonitoringInRecent24Hours = sdRadarDetectDataService.specialVehicleMonitoringInRecent24Hours(map.get("tunnelId").toString());
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
        map.put("operIp", IpUtils.getIpAddr(ServletUtils.getRequest()));

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

    /**
     * 批量控制设备
     * @param deviceMap
     * @return
     */
    @PostMapping("/batchControlDevice")
    public AjaxResult batchControlDevice(@RequestBody Map<String, Object> deviceMap){
        List<String> eqIdList = Arrays.asList(deviceMap.get("eqId").toString().split(","));
        String state = deviceMap.get("state").toString();
        // 附加值
        // 基本照明  亮度 不得小于30
        // 加强照明
        String stateNum = deviceMap.get("brightness") == null ? null : deviceMap.get("brightness").toString();
      //  String eqType = deviceMap.get("eqType") == null ? null : deviceMap.get("eqType").toString();


        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(eqIdList.get(0));

        // 基本照明 亮度 不得小于30
        if((stateNum == null || Integer.parseInt(stateNum) < 30) && state.equals("1") &&  sdDevices.getEqType().equals(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().toString())){
            return AjaxResult.error("基本照明亮度不得低于30");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("operIp", IpUtils.getIpAddr(ServletUtils.getRequest()));

        int count = 0;
        for(String devId : eqIdList){
            map.put("devId", devId);
            map.put("state", state);
            map.put("controlType", "0");
            map.put("stateNum", stateNum);
            count = sdDeviceControlService.controlDevices(map);
        }
        return AjaxResult.success(count);
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
        String operIp = (String) params.get("operIp");
        Assert.hasText(devId, "设备参数{devId}必传");
        Assert.hasText(state, "设备控制状态参数{state}必传");
        Assert.hasText(operIp, "操作方IP地址参数{operIp}必传");
        Integer controlState = sdDeviceControlService.controlDevices(params);
        return controlState;
    }


    /**
     * Omron 消防水泵控制  (后期优化。所有控制走当前一个接口)
     * @param params
     * @return
     */
    @PostMapping("/controlDeviceByParam")
    public AjaxResult controlDeviceByParam(@RequestBody Map<String, Object> params){
        //参数校验
        if (CollectionUtils.isEmpty(params)) {
            return AjaxResult.error("控制设备参数为空");
        }
        //获取当前传输数据协议类型
        if ( params.get("comType") == null ||  params.get("comType").toString().equals("")) {
            return AjaxResult.error("未指定设备通讯类型");
        } else if ( params.get("data") == null || params.get("data").toString().equals("")) {
            return AjaxResult.error("未指定设备需要变更的状态信息");
        } else if (params.get("eqId") == null || params.get("eqId").toString().equals("")) {
            return AjaxResult.error("未指定设备id");
        }
        boolean  b = deviceFunctionsService.deviceControlByParam( params.get("comType").toString(), params.get("eqId").toString(), params.get("data").toString());
        if(b){
            return AjaxResult.success("控制成功");
        }
        return AjaxResult.error("控制失败");
    }
}
