package com.tunnel.platform.controller.workspace;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.system.service.ISysDictDataService;
import com.tunnel.business.datacenter.domain.enumeration.DevicesHongTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.datacenter.domain.enumeration.TunnelEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceTypeItem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.mapper.dataInfo.SdDeviceTypeItemMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.service.dataInfo.*;
import com.tunnel.business.service.digitalmodel.ISdRadarDetectDataService;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.deal.guidancelamp.control.util.GuidanceLampHandle;
import com.tunnel.deal.plc.modbus.ModbusTcpHandle;
import com.tunnel.platform.service.SdDeviceControlService;
import com.tunnel.platform.service.SdOptDeviceService;
import com.tunnel.platform.service.deviceControl.HongMengDevService;
import com.tunnel.platform.service.deviceControl.LightService;
import com.zc.common.core.websocket.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
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

    @Value("${authorize.name}")
    private String deploymentType;

    @Autowired
    private HongMengDevService hongMengDevService;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Autowired
    private SdDeviceTypeItemMapper sdDeviceTypeItemMapper;

    @Autowired
    private RestTemplate restTemplate;

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

        String devId = map.get("devId").toString();
        String state = map.get("state").toString();
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);

        if ("GSY".equals(deploymentType)) {
            if(TunnelEnum.HANG_SHAN_DONG.getCode().equals(sdDevices.getEqTunnelId()) && DevicesHongTypeEnum.contains(sdDevices.getEqType())){
                Map<String, String> hongMap = hongMengDevService.updateHua(devId, state);
                Integer code = Integer.valueOf(hongMap.get("code"));
                String msg = hongMap.get("msg").toString();
                if(code == 200){
                    return AjaxResult.success(1);
                }else {
                    return AjaxResult.success(msg,0);
                }
            }
            map.put("controlType", "0");
            map.put("operIp", IpUtils.getIpAddr(ServletUtils.getRequest()));
            sdOptDeviceService.optSingleDevice(map);
            return AjaxResult.success(1);
        }

        //获取当前设备状态
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(devId);
        sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode()));
        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
        //控制设备
//        int controlState = ModbusTcpHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices);
        int controlState = 0;
        //根据字典中配置的设备模拟控制值进行模拟状态展示
        List<SysDictData> isopenList = sysDictDataService.getSysDictDataByDictType("sys_analog_control_isopen");
        if (isopenList.size() == 0) {
            throw new RuntimeException("设备模拟控制是否开启字典值不存在，请联系管理员添加后重试");
        }
        SysDictData sysDictData = isopenList.get(0);
        String isopen = sysDictData.getDictValue();
        long eqType = sdDevices.getEqType().longValue();

        if (isopen != null && !isopen.equals("") && isopen.equals("1")) {
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
            SdDeviceTypeItem typeItem = sdDeviceTypeItems.get(0);
            updateDeviceData(sdDevices, state, Integer.parseInt(typeItem.getId().toString()));
            //添加操作记录
            SdOperationLog sdOperationLog = new SdOperationLog();
            sdOperationLog.setEqTypeId(sdDevices.getEqType());
            sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
            sdOperationLog.setEqId(sdDevices.getEqId());
            sdOperationLog.setCreateTime(new Date());
            sdOperationLog.setOperationState(state);
            sdOperationLog.setControlType("0");
            sdOperationLog.setState("1");
            sdOperationLog.setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
            sdOperationLogService.insertSdOperationLog(sdOperationLog);
            return AjaxResult.success(1);
        } else if (isopen != null && !isopen.equals("") && isopen.equals("0")) {
            // eqType == DevicesTypeEnum.SHUI_BENG.getCode().longValue() ||
            if (
                    eqType == DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode().longValue() ||
                    eqType == DevicesTypeEnum.ZHUO_ZHUAN_CHE_ZHI.getCode().longValue() ||
                    eqType == DevicesTypeEnum.JIAO_TONG_XIN_HAO_DENG.getCode().longValue() ||
                    eqType == DevicesTypeEnum.ZUO_JIAO_TONG_XIN_HAO_DENG.getCode().longValue() ||
                    eqType == DevicesTypeEnum.YING_JI_ZHAO_MING.getCode().longValue() ||
                    eqType == DevicesTypeEnum.YIN_DAO_ZHAO_MING.getCode().longValue() ||
                    eqType == DevicesTypeEnum.FENG_JI.getCode().longValue() ||
                    eqType == DevicesTypeEnum.JUAN_LIAN_MEN.getCode().longValue() ||
                    eqType == DevicesTypeEnum.SHENG_GUANG_BAO_JING.getCode().longValue()
            ) {
                //控制设备
                controlState = ModbusTcpHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices);
            } else if (eqType == DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().longValue() || eqType == DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().longValue()) {
                controlState = lightService.lineControl(devId, Integer.parseInt(state));
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
        sdOperationLog.setOperationState(state);
        sdOperationLog.setControlType("0");
        sdOperationLog.setState(String.valueOf(controlState));
        sdOperationLog.setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
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
        Assert.hasText(operIp, "IP参数{operIp}必传");
        Integer controlState = sdDeviceControlService.controlDevices(params);
        return controlState;
    }

    public void updateHua(String deviceId, String devStatus){
        //查询设备信息
        SdDevices sdDevices = sdDevicesMapper.selectSdDevicesById(deviceId);
        //查询设备类型数据项表
        SdDeviceTypeItem sdDeviceTypeItem = new SdDeviceTypeItem();
        sdDeviceTypeItem.setDeviceTypeId(sdDevices.getEqType());
        SdDeviceTypeItem deviceTypeItem = sdDeviceTypeItemMapper.selectSdDeviceTypeItemList(sdDeviceTypeItem).get(0);
        //状态匹配
        devStatus = devStatusCync(deviceTypeItem.getId(), devStatus);
        //修改地址
        String url = getUrl(deviceTypeItem.getId());
        HttpHeaders requestHeaders = new HttpHeaders();
        //设置JSON格式数据
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceId",deviceId);
        jsonObject.put("runStatus",devStatus);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toString(), requestHeaders);
        try {
            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("http://10.7.187.28:60400/qxsd-iot"+url, requestEntity, String.class);
            log.info("返回值 --> {}", stringResponseEntity.getBody());
        } catch (Exception e) {
            log.error("设备控制失败！{}", e.getMessage());
        }
    }

    /**
     * 设备运行状态与数据库匹配
     * @param itemId
     * @param runStatus
     * @return
     */
    public String devStatusCync(Long itemId,String runStatus){
        if(DevicesTypeItemEnum.FENG_JI_STATUS.getCode() == itemId){
            if("3".equals(runStatus)){
                return "00";
            }
        }
        if(DevicesTypeItemEnum.XIN_HAO_DENG.getCode() == itemId){
            if("4".equals(runStatus)){
                return "00";
            }
            if("2".equals(runStatus)){
                return "01";
            }
            if("1".equals(runStatus)){
                return "02";
            }
        }
        if(DevicesTypeItemEnum.ZHUO_ZHUAN_XIN_HAO_DENG.getCode() == itemId){
            if("1".equals(runStatus)){
                return "02";
            }
            if("2".equals(runStatus)){
                return "01";
            }
            if("4".equals(runStatus)){
                return "00";
            }
            if("5".equals(runStatus)){
                return "04";
            }
        }
        if(DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode() == itemId){
            if("4".equals(runStatus)){
                return "00";
            }
        }
        if(DevicesTypeItemEnum.ZHUO_ZHUAN_CHE_ZHI.getCode() == itemId){
            if("5".equals(runStatus)){
                return "04";
            }
            if("4".equals(runStatus)){
                return "00";
            }
        }
        if(DevicesTypeItemEnum.JUAN_LIAN_MEN.getCode() == itemId){
            if("3".equals(runStatus)){
                return "00";
            }
        }
        return "0" + runStatus;
    }

    /**
     * 获取相关设备修改地址
     * @param itemId
     * @return
     */
    public String getUrl(Long itemId){
        if(itemId == DevicesTypeItemEnum.FENG_JI_STATUS.getCode()){
            return "/deviceCtrl/fan/alterFanRunStatus";
        }
        if(itemId == DevicesTypeItemEnum.XIN_HAO_DENG.getCode() || itemId == DevicesTypeItemEnum.ZHUO_ZHUAN_XIN_HAO_DENG.getCode()){
            return "/deviceCtrl/trafficLight/alterTrafficLightRunStatus";
        }
        if(itemId == DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode() || itemId == DevicesTypeItemEnum.ZHUO_ZHUAN_CHE_ZHI.getCode()){
            return "/deviceCtrl/laneIndicator/alterLaneIndicatorRunStatus";
        }
        if(itemId == DevicesTypeItemEnum.JUAN_LIAN_MEN.getCode()){
            return "/deviceCtrl/rollDoor/alterRollDoorRunStatus";
        }
        return null;
    }
}
