package com.tunnel.platform.controller.workspace;

import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.config.ThreadPoolConfig;
import com.ruoyi.system.service.ISysDictDataService;
import com.tunnel.business.datacenter.domain.enumeration.*;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.domain.vehicle.SdVehicleData;
import com.tunnel.business.mapper.dataInfo.SdDeviceTypeItemMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.service.dataInfo.*;
import com.tunnel.business.service.digitalmodel.ISdRadarDetectDataService;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.business.service.vehicle.ISdVehicleDataService;
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.generalcontrol.service.GeneralControlService;
import com.tunnel.deal.guidancelamp.control.util.GuidanceLampHandle;
import com.tunnel.deal.tcp.modbus.ModbusCmd;
import com.tunnel.deal.tcp.plc.ximenzi.XiMenZiPlcControl;
import com.tunnel.deal.tcp.plc.ximenzi.task.XiMenZiPlcTask;
import com.tunnel.platform.service.SdDeviceControlService;
import com.tunnel.platform.service.SdOptDeviceService;
import com.tunnel.platform.service.deviceControl.HongMengDevService;
import com.tunnel.platform.service.deviceControl.LightService;
import com.tunnel.platform.service.deviceFunctions.DeviceFunctionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = "工作台Controller")
@ApiSupport(order = 16)
public class workspaceController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(workspaceController.class);

    @Autowired
    private ISdEventService sdEventService;
    @Autowired
    private ISdDeviceCmdService sdDeviceCmdService;
    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private RedisCache redisCache;
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

    @Autowired
    private CommonControlService commonControlService;

    private XiMenZiPlcControl xiMenZiPlcControl = SpringUtils.getBean(XiMenZiPlcControl.class);


//    //3d测试
//    @PostMapping("/test")
//    public String test() {
//        JSONObject object = new JSONObject();
//        object.put("dataList", 11111);
//        WebSocketService.broadcast("dataList", object);
//        return "get 3d info";
//    }



//    /**
//     * 设备控制接口
//     * @param map
//     * @return
//     */
//    @PostMapping("/generalControlDevice")
//    public AjaxResult generalControlDevice(@RequestBody Map<String, Object> map) {
//        //设备ID
//        String devId = Optional.ofNullable(map.get("devId")).orElse("").toString();
//
//        if (devId == null || "".equals(devId)) {
//            AjaxResult.error("未指定设备");
//        }
//        //设备状态
//        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
//        if (state == null || "".equals(state)) {
//            AjaxResult.error("未指定设备需要变更的状态信息");
//        }
//        //设备信息
//        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);
//
//        //查询配置状态是否是模拟控制
//        boolean isopen = commonControlService.queryAnalogControlConfig();
//        if (isopen) {
//            //设备模拟控制开启
//            return commonControlService.excecuteAnalogControl(sdDevices,map);
//        }
//
//        if ("GSY".equals(deploymentType)) {
//            //高速云分发控制
//         return deviceControlService.cloudPlatformControl(map);
//        }
//
//        //控制设备之前获取设备状态
//        String beforeState = commonControlService.selectBeforeState(sdDevices);
//        String controlState = OperationLogEnum.STATE_ERROR.getCode();
//        //设备控制
//        GeneralControlBean generalControlBean = generalControlService.getProtocolBean(sdDevices);
//        AjaxResult ajaxResult = generalControlBean.control(map,sdDevices);
//        Integer code = Integer.valueOf(String.valueOf(ajaxResult.get("code")));
//        if( code == HttpStatus.SUCCESS){
//            controlState = OperationLogEnum.STATE_SUCCESS.getCode();
//            ajaxResult.put("data",controlState);
//        }
//        //添加操作日志
//        commonControlService.addOperationLog(sdDevices,map,beforeState,controlState);
//        return ajaxResult;
//    }


    //PLC车指控制接口
    @PostMapping("/controlDevice")
    @ApiOperation("首页设备控制接口")
    public AjaxResult controlDevice(@RequestBody Map<String, Object> map) {
        if (map.get("devId") == null || map.get("devId").toString().equals("")) {
            throw new RuntimeException("未指定设备，请联系管理员");
        } else if (map.get("state") == null || map.get("state").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的状态信息");
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

//        if(TunnelEnum.HANG_SHAN_DONG.getCode().equals(sdDevices.getEqTunnelId()) && DevicesHongTypeEnum.contains(sdDevices.getEqType()) && "AGREE".equals(platformControl)){
//            Map<String, String> hongMap = hongMengDevService.updateHua(devId, state);
//            Integer code = Integer.valueOf(hongMap.get("code"));
//            String msg = hongMap.get("msg").toString();
//            if(code == 200){
//                return AjaxResult.success(1);
//            }else {
//                return AjaxResult.success(msg,0);
//            }
//        }
        if ("GSY".equals(deploymentType)) {
            //高速云分发控制
            return deviceControlService.cloudPlatformControl(map);
        }

        //控制设备
//        int controlState = ModbusTcpHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices);
        int controlState = 0;
        //查询配置状态是否是模拟控制
//        boolean isopen = commonControlService.queryAnalogControlConfig();
//        long eqType = sdDevices.getEqType().longValue();

//
//        if (isopen) {
//            //设备模拟控制开启
//            return commonControlService.excecuteAnalogControl(sdDevices,map);
//        } else {
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

            //照明控制增加
            if( DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().toString().equals(map.get("eqType").toString())||
                    DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().toString().equals(map.get("eqType").toString())){
                //将当前设备亮度存储redis缓存。
                String redisLuminanceRangeKey = "control_regular:"+sdDevices.getEqId()+"_LuminanceRange";
                redisCache.setCacheObject(redisLuminanceRangeKey,Integer.valueOf(brightness));
                //将当前设备亮度存储redis缓存。
                String redisStateLuminanceRangeKey = "control_regular:"+sdDevices.getEqId()+"_state";
                redisCache.setCacheObject(redisStateLuminanceRangeKey,state);
            }

            AjaxResult ajaxResult = generalControlBean.control(map,sdDevices);
//            Integer code = Integer.valueOf(String.valueOf(ajaxResult.get("code")));
//            if( code == HttpStatus.SUCCESS){
//                controlState = Integer.valueOf(OperationLogEnum.STATE_SUCCESS.getCode());
//            }
//        }

        return ajaxResult;
    }

    //诱导灯控制接口
    @PostMapping("/controlGuidanceLampDevice")
    @ApiOperation("诱导灯控制接口")
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
                sdDevicesService.updateSdDevicesByFEqId(devices);
            }
            if (sdDevices.getEqType().longValue() == DevicesTypeEnum.YOU_DAO_DENG_CONTROL.getCode().longValue()) {
                //父级设备变更
                sdDeviceDataService.updateDeviceData(sdDevices, state, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode()));
                sdDeviceDataService.updateDeviceData(sdDevices, brightness, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode()));
                sdDeviceDataService.updateDeviceData(sdDevices, frequency, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode()));
                //子级设备变更
                SdDevices dev = new SdDevices();
                dev.setFEqId(fEqId);
                List<SdDevices> list = sdDevicesService.selectSdDevicesList(dev);
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        SdDevices devo = list.get(i);
                        sdDeviceDataService.updateDeviceData(devo, state, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode()));
                        sdDeviceDataService.updateDeviceData(devo, brightness, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode()));
                        sdDeviceDataService.updateDeviceData(devo, frequency, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode()));
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
            //正晨诱导灯控制
            controlState = GuidanceLampHandle.getInstance().toControlDev(fEqId, Integer.parseInt(state), sdDevices, brightness, frequency, fireMark);
        } else if (sdDevices.getEqType().longValue() == DevicesTypeEnum.YOU_DAO_DENG_CONTROL.getCode().longValue() && !sdDevices.getBrandId().equals("0057")) {
            //显科的设备一个路段可能会有多个控制器，都需要下发同样的指令
            SdDevices xiankeDevices = new SdDevices();
            xiankeDevices.setEqTunnelId(sdDevices.getEqTunnelId());
            xiankeDevices.setEqType(sdDevices.getEqType());
            xiankeDevices.setEqDirection(sdDevices.getEqDirection());
            List<SdDevices> xiankeDeviceList = sdDevicesService.selectSdDevicesList(xiankeDevices);
            //深圳显科诱导灯没有逆向流水模式
            if (state.equals("3")) {
                state = "2";
            }
            for (int i = 0;i < xiankeDeviceList.size();i++) {
                SdDevices dev = xiankeDeviceList.get(i);
                if (dev.getIp() == null || dev.getIp().equals("")) {
                    continue;
                }
                controlState = asynInduce(dev,state,brightness,frequency);
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
    @ApiOperation("警示灯带控制接口")
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
        String brightness = (map.get("brightness") != null && map.get("brightness") != "") ? map.get("brightness").toString() : "0";
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);

        //设备控制
        GeneralControlBean generalControlBean = generalControlService.getProtocolBean(sdDevices);
        if(generalControlBean == null){
            return AjaxResult.error("设备协议配置为空");
        }
        AjaxResult ajaxResult = generalControlBean.control(map,sdDevices);
        return ajaxResult;
//        //获取当前设备状态
//        SdDeviceData sdDeviceData = new SdDeviceData();
//        sdDeviceData.setDeviceId(sdDevices.getEqId());
//        sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.JING_SHI_DENG_DAI.getCode()));
//        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
//
//        //根据字典中配置的设备模拟控制值进行模拟状态展示
//        List<SysDictData> isopenList = sysDictDataService.getSysDictDataByDictType("sys_analog_control_isopen");
//        if (isopenList.size() == 0) {
//            throw new RuntimeException("设备模拟控制是否开启字典值不存在，请添加后重试");
//        }
//        SysDictData sysDictData = isopenList.get(0);
//        String isopen = sysDictData.getDictValue();
//        if (isopen != null && !isopen.equals("") && isopen.equals("1")) {
//            //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
//            sdDevices.setEqStatus("1");
//            sdDevices.setEqStatusTime(new Date());
//            sdDevicesService.updateSdDevices(sdDevices);
//            if (sdDevices.getEqType().longValue() == DevicesTypeEnum.JING_SHI_DENG_DAI.getCode().longValue()) {
//                sdDeviceDataService.updateDeviceData(sdDevices, state, Long.valueOf(DevicesTypeItemEnum.JING_SHI_DENG_DAI.getCode()));
//                sdDeviceDataService.updateDeviceData(sdDevices, brightness, Long.valueOf(DevicesTypeItemEnum.JING_SHI_DENG_DAI_STATUS.getCode()));
//            }
//            //添加操作记录
//            SdOperationLog sdOperationLog = new SdOperationLog();
//            sdOperationLog.setEqTypeId(sdDevices.getEqType());
//            sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
//            sdOperationLog.setEqId(sdDevices.getEqId());
//            sdOperationLog.setCreateTime(new Date());
//            if (data.size() > 0 && data.get(0) != null) {
//                sdOperationLog.setBeforeState(data.get(0).getData());
//            }
//            sdOperationLog.setOperationState(state);
//            sdOperationLog.setControlType("0");
//            sdOperationLog.setState("1");
//            sdOperationLog.setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
//            sdOperationLogService.insertSdOperationLog(sdOperationLog);
//            return AjaxResult.success(1);
//        }
//        //控制设备
//        int controlState = WarningLightStripHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices);
//        //添加操作记录
//        SdOperationLog sdOperationLog = new SdOperationLog();
//        sdOperationLog.setEqTypeId(sdDevices.getEqType());
//        sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
//        sdOperationLog.setEqId(sdDevices.getEqId());
//        sdOperationLog.setCreateTime(new Date());
//        if (data.size() > 0 && data.get(0) != null) {
//            sdOperationLog.setBeforeState(data.get(0).getData());
//        }
//        sdOperationLog.setOperationState(state);
//        sdOperationLog.setControlType("0");
//        sdOperationLog.setState(String.valueOf(controlState));
//        sdOperationLog.setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
//        sdOperationLogService.insertSdOperationLog(sdOperationLog);
//        return AjaxResult.success(controlState);
    }

    //疏散标志控制接口
    @PostMapping("/controlEvacuationSignDevice")
    @ApiOperation("疏散标志控制接口")
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
                sdDevicesService.updateSdDevicesByFEqId(devices);
            }
            if (sdDevices.getEqType().longValue() == DevicesTypeEnum.SHU_SAN_BIAO_ZHI_CONTROL.getCode().longValue()) {
                //父级设备变更
                sdDeviceDataService.updateDeviceData(sdDevices, state, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()));
                sdDeviceDataService.updateDeviceData(sdDevices, brightness, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode()));
                sdDeviceDataService.updateDeviceData(sdDevices, frequency, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode()));
                sdDeviceDataService.updateDeviceData(sdDevices, fireMark, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode()));
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
                            sdDeviceDataService.updateDeviceData(devo, state, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()));
                            sdDeviceDataService.updateDeviceData(devo, brightness, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode()));
                            sdDeviceDataService.updateDeviceData(devo, frequency, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode()));
                            sdDeviceDataService.updateDeviceData(devo, fireMark, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode()));
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
                            sdDeviceDataService.updateDeviceData(devices, fireMark, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode()));
                            sdDeviceDataService.updateDeviceData(devices, state, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()));
                            sdDeviceDataService.updateDeviceData(devices, brightness, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode()));
                            sdDeviceDataService.updateDeviceData(devices, frequency, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode()));
                        }
                    } else {
                        //疏散标志开灯无报警点
                        state = "2";
                        for (int i = 0;i < list.size();i++) {
                            SdDevices devo = list.get(i);
                            sdDeviceDataService.updateDeviceData(devo, state, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()));
                            sdDeviceDataService.updateDeviceData(devo, brightness, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode()));
                            sdDeviceDataService.updateDeviceData(devo, frequency, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode()));
                            sdDeviceDataService.updateDeviceData(devo, fireMark, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode()));
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
        if (sdDevices.getEqType().longValue() == DevicesTypeEnum.SHU_SAN_BIAO_ZHI_CONTROL.getCode().longValue()) {
            //查询子设备
            SdDevices dev = new SdDevices();
            dev.setFEqId(fEqId);
            List<SdDevices> list = sdDevicesService.selectSdDevicesList(dev);
            sdOperationLog.setEqTypeId(list.get(0).getEqType());
        }else {
            sdOperationLog.setEqTypeId(sdDevices.getEqType());
        }
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
        return controlState == 0 ? AjaxResult.error("操作失败",controlState) : AjaxResult.success(controlState);
    }



    @PostMapping("/vehicleMonitoringInRecent24Hours")
    @ApiOperation("车辆监测查询")
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
    @ApiOperation("车辆监测查询")
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
    @ApiOperation("统计当天24小时重点车辆")
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
    @ApiOperation("车指批量控制接口")
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

        long startTime = System.currentTimeMillis();

        Integer controlDevices = 0;
        if(list == null || list.size() == 0){
            //控制失败
            log.error("车指批量控制报错：未查询到可控制设备");
            return AjaxResult.success(controlDevices);
        }
        SdDevices sdDevices  = list.get(0);
        String protocolId = String.valueOf(sdDevices.getProtocolId());
        if(XiMenZiPlcTask.XIMENZI_PROTOCOL_ID.equals(protocolId)){
            //西门子PLC协议，特殊处理
            XiMenZiPlcControl.batchControl = true;

            for (int i = 0; i < list.size(); i++) {
                String eqId = list.get(i).getEqId();
                String state = carFingerDevices.get("state").toString();
                map.put("devId", eqId);
                map.put("state", state);
                map.put("controlType", "0");
                controlDevices = sdDeviceControlService.controlDevices(map);
                sleep(300);
            }
            //开启线程，执行控制
//            ThreadPool.executor.execute(()->{
                xiMenZiPlcControl.sendFailCmd();
//            });
            ModbusCmd.commandLock = true;
            XiMenZiPlcControl.batchControl = false;
        }else{
            for (int i = 0; i < list.size(); i++) {
                String eqId = list.get(i).getEqId();
                String state = carFingerDevices.get("state").toString();
                map.put("devId", eqId);
                map.put("state", state);
                map.put("controlType", "0");
                controlDevices = sdDeviceControlService.controlDevices(map);
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("车指批量控制：endTime-startTime="+(endTime-startTime));
        return AjaxResult.success(controlDevices);
    }

    /**
     * 批量控制设备
     * @param deviceMap
     * @return
     */
    @PostMapping("/batchControlDevice")
    @ApiOperation("批量控制设备接口")
    public AjaxResult batchControlDevice(@RequestBody Map<String, Object> deviceMap){
        List<String> eqIdList = Arrays.asList(deviceMap.get("eqId").toString().split(","));
        String state = deviceMap.get("state").toString();
        // 附加值
        // 基本照明  亮度 不得小于30
        // 加强照明
        String stateNum = deviceMap.get("brightness") == null ? null : deviceMap.get("brightness").toString();
      //  String eqType = deviceMap.get("eqType") == null ? null : deviceMap.get("eqType").toString();

        String frequency = deviceMap.get("frequency") == null ? null : deviceMap.get("frequency").toString();



        boolean isopen = commonControlService.queryAnalogControlConfig();

        Map<String, Object> map = new HashMap<>();
        map.put("operIp", IpUtils.getIpAddr(ServletUtils.getRequest()));

        int count = 0;
        for(String devId : eqIdList){

            SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);

            // 基本照明 亮度 不得小于30
            if((stateNum == null || Integer.parseInt(stateNum) < 30) && state.equals("1") &&  sdDevices.getEqType().equals(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().toString())){
                return AjaxResult.error("基本照明亮度不得低于30");
            }

            map.put("devId", devId);
            map.put("state", state);
            map.put("controlType", "0");
            map.put("frequency", frequency);
            map.put("brightness", stateNum);

            if (!isopen) {
                count = sdDeviceControlService.controlDevices(map);
            } else {
                count = commonControlService.analogControl(map,sdDevices);

                SdOperationLog sdOperationLog = commonControlService.getOperationLog(map,sdDevices,count);
                sdOperationLogService.insertSdOperationLog(sdOperationLog);
            }
        }
        return AjaxResult.success(count);
    }

    @GetMapping("/getDeviceDataAndState")
    public AjaxResult selectDeviceDataAndState(String tunnelId) {
        return AjaxResult.success(sdDevicesService.getDeviceAndState(tunnelId));
    }


    /**
     * 控制单个设备
     * @param params
     * @return
     */
    @PostMapping("/commonControl")
    @ApiOperation("控制单个设备")
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
    @ApiOperation("消防水泵控制接口")
    public AjaxResult controlDeviceByParam(@RequestBody Map<String, Object> params){
        //参数校验
        if (CollectionUtils.isEmpty(params)) {
            return AjaxResult.error("控制设备参数为空");
        }
//        //获取当前传输数据协议类型
//        if ( params.get("comType") == null ||  params.get("comType").toString().equals("")) {
//            return AjaxResult.error("未指定设备通讯类型");
//        } else
        if ( params.get("state") == null || params.get("state").toString().equals("")) {
            return AjaxResult.error("未指定设备需要变更的状态信息");
        } else if (params.get("devId") == null || params.get("devId").toString().equals("")) {
            return AjaxResult.error("未指定设备id");
        }

        String devId = params.get("devId").toString();
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);
        Integer  controlState = 0;
        //设备控制
        GeneralControlBean generalControlBean = generalControlService.getProtocolBean(sdDevices);
        if(generalControlBean == null){
            throw new RuntimeException("设备协议配置为空");
        }else{
            controlState = generalControlBean.controlDevices(params);
        }
        return AjaxResult.success(controlState);
    }

    /**
     * 异步控制诱导标
     * @param dev
     * @param state
     * @param brightness
     * @param frequency
     * @return
     */
    @Async(value = "induceExecutor")
    @ApiOperation("异步控制诱导标")
    public int asynInduce(SdDevices dev, String state, String brightness, String frequency){
        System.out.println(dev.getIp() + "：进来了");
        return GuidanceLampHandle.getInstance().toControlXianKeDev(dev.getEqId(), Integer.parseInt(state), dev, brightness, frequency);
    }


    /**
     * 线程休眠固定时间
     * @param ms 毫秒
     */
    public void sleep(int ms){
        //间隔固定时间（毫秒）发送指令，避免同一个设备连续多次发送指令无回复
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
