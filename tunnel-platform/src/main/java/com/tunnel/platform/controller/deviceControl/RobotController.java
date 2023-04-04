package com.tunnel.platform.controller.deviceControl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.datacenter.domain.enumeration.DevicesBrandEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdDeviceDataRecord;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.intelligentMonitoring.TrafficManagement;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.business.service.dataInfo.ISdDeviceDataRecordService;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.deal.corniceTunnelRobot.CorniceTunnelRobot;
import com.tunnel.deal.corniceTunnelRobot.domain.FindAlarmThresholdConfigDto;
import com.tunnel.deal.corniceTunnelRobot.domain.StatusDto;
import com.tunnel.deal.corniceTunnelRobot.domain.VideoDto;
import com.tunnel.deal.corniceTunnelRobot.impl.ZhuoShiCorniceTunnelRobot;
import com.tunnel.deal.light.impl.SanJingLight;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api(tags = "隧道机器人")
@RestController
@RequestMapping("/robot")
public class RobotController {

    @Autowired
    @Qualifier("zhuoShiCorniceTunnelRobot")
    private CorniceTunnelRobot zhuoShiCorniceTunnelRobot;

    @Autowired
    private IExternalSystemService externalSystemService;

    @Autowired
    private ISdOperationLogService sdOperationLogService;


    @Autowired
    private ISdDeviceDataService sdDeviceDataService;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdDeviceDataRecordService sdDeviceDataRecordService;

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping(value = "/getUsers")
    @ApiOperation(value = "获取用户信息", httpMethod = "GET")
    public AjaxResult GetUsers(){

        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);

        if(list.size() == 0){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        String reslut = zhuoShiCorniceTunnelRobot.GetUsers(list.get(0).getUsername(),list.get(0).getPassword(),list.get(0).getSystemUrl());

        return  AjaxResult.success(reslut);
    }

    /**
     * 获取用户所属机器人信息
     * @param userId 用户id
     * @return
     */
    @GetMapping(value = "/getUserRobots")
    @ApiOperation(value = "获取用户所属机器人信息", httpMethod = "GET")
    public AjaxResult GetUserRobots(Integer userId){

        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);

        if(list.size() == 0){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        return  AjaxResult.success(zhuoShiCorniceTunnelRobot.GetUserRobots(userId,list.get(0).getSystemUrl()));
    }

    /**
     * 获取机器人状态
     * @param deviceId 机器人id
     * @return
     */
    @GetMapping(value = "/getStatus")
    @ApiOperation(value = "获取机器人状态", httpMethod = "GET")
    public AjaxResult GetStatus(String deviceId){

        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);

        if(list.size() == 0){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        StatusDto statusDto = zhuoShiCorniceTunnelRobot.GetStatus(deviceId,list.get(0).getSystemUrl());

        if(statusDto != null){

            // 同步机器人数据项
            sdDeviceDataService.updateDeviceData(deviceId,statusDto.getIsOnline(), DevicesTypeItemEnum.ROBOT_IS_ONLINE.getCode(),false);
            sdDeviceDataService.updateDeviceData(deviceId,statusDto.getCurrentDuration(), DevicesTypeItemEnum.ROBOT_CURRENT_DURATION.getCode(),false);
            sdDeviceDataService.updateDeviceData(deviceId,statusDto.getCurrentMileage(), DevicesTypeItemEnum.ROBOT_CURRENT_MILEAGE.getCode(),false);
            sdDeviceDataService.updateDeviceData(deviceId,statusDto.getElectricity(), DevicesTypeItemEnum.ROBOT_ELECTRICITY.getCode(),false);
            sdDeviceDataService.updateDeviceData(deviceId,statusDto.getCharge(), DevicesTypeItemEnum.ROBOT_CHARGE.getCode(),false);
            sdDeviceDataService.updateDeviceData(deviceId,statusDto.getVoltage(), DevicesTypeItemEnum.ROBOT_VOLTAGE.getCode(),false);
            sdDeviceDataService.updateDeviceData(deviceId,statusDto.getCurrent(), DevicesTypeItemEnum.ROBOT_CURRENT.getCode(),false);
            sdDeviceDataService.updateDeviceData(deviceId,statusDto.getBatteryTemp(), DevicesTypeItemEnum.ROBOT_BATTERYTEMP.getCode(),false);
            sdDeviceDataService.updateDeviceData(deviceId,statusDto.getPosition(), DevicesTypeItemEnum.ROBOT_POSITION.getCode(),false);
            sdDeviceDataService.updateDeviceData(deviceId,statusDto.getOxygenDensity(), DevicesTypeItemEnum.ROBOT_OXYGENDENSITY.getCode(),false);
            sdDeviceDataService.updateDeviceData(deviceId,statusDto.getCarbonMonoxideDensity(), DevicesTypeItemEnum.ROBOT_CARBON_MONOXIDE_DENSITY.getCode(),false);
           // sdDeviceDataService.updateDeviceData(deviceId,statusDto.getTemperature(), ,false);
           // sdDeviceDataService.updateDeviceData(deviceId,statusDto.getHumidity(), ,false);
           // sdDeviceDataService.updateDeviceData(deviceId,statusDto.getBrightness(), ,false);

            // 更新设备主表 设备状态
            if(statusDto.getIsOnline().equals(DevicesStatusEnum.DEVICE_OFF_LINE.getCode())){
                SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);
                // 更新设备在线状态
                device.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
                device.setEqStatusTime(new Date());
                sdDevicesService.updateSdDevices(device);
            }

            return  AjaxResult.success(statusDto);
        }

        return  AjaxResult.error("第三方服务异常,请联系管理员");

    }

    /**
     * 获取机器人视频流地址
     * @param deviceId 机器人id
     * @return
     */
    @GetMapping(value = "/getVideoUrl")
    @ApiOperation(value = "获取机器人视频流地址", httpMethod = "GET")
    public AjaxResult getVideoUrl(String deviceId){

        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);

        if(list.size() == 0){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        VideoDto repData = zhuoShiCorniceTunnelRobot.GetVideoUrl(deviceId,list.get(0).getSystemUrl());

        if(repData != null){

            // 同步本地数据库
            sdDeviceDataService.updateDeviceData(deviceId,repData.getHd(), DevicesTypeItemEnum.ROBOT_HD_VIDEO.getCode(),false);
            sdDeviceDataService.updateDeviceData(deviceId,repData.getInfrared(), DevicesTypeItemEnum.ROBOT_INFRARE_VIDEO.getCode(),false);

            return  AjaxResult.success(repData);
        }

        return  AjaxResult.error("第三方服务异常,请联系管理员");

    }


    /**
     * 设置机器人投光灯
     * @param deviceId  设备id
     * @param control   0 关闭  1打开
     * @return
     */
    @GetMapping(value = "/setLEDLight")
    @ApiOperation(value = "设置机器人投光灯", httpMethod = "GET")
    public AjaxResult setLEDLight(@RequestParam(name = "deviceId")String deviceId,
                                  @RequestParam(name = "control")Integer control){

        if(deviceId.equals("JQ-WeiFang-JiuLongYu-JJL-ROBOT-001")){
            return  AjaxResult.success(1);
        }else if(true){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);

        if(list.size() == 0){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        int res = zhuoShiCorniceTunnelRobot.SetLEDLight(deviceId,control,list.get(0).getSystemUrl());

        if(res != 0){
            // 同步本地数据库  // TODO 枚举键未匹配
            sdDeviceDataService.updateDeviceData(deviceId,control+"", DevicesTypeItemEnum.ROBOT_HD_VIDEO.getCode(),true);

            return  AjaxResult.success(res);
        }

        return  AjaxResult.error("第三方服务异常,请联系管理员");
    }


    /**
     * 设置机器人报警灯
     * @param deviceId 机器人id
     * @param control  0 关闭  1打开
     * @return
     */
    @GetMapping(value = "/setAlarmLight")
    @ApiOperation(value = "设置机器人报警灯", httpMethod = "GET")
    public AjaxResult SetAlarmLight(@RequestParam(name = "deviceId")String deviceId,
                                    @RequestParam(name = "control")Integer control){


        if(deviceId.equals("JQ-WeiFang-JiuLongYu-JJL-ROBOT-001")){
            return  AjaxResult.success(1);
        }else if(true){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);

        if(list.size() == 0){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        return  AjaxResult.success(zhuoShiCorniceTunnelRobot.SetAlarmLight(deviceId,control,list.get(0).getSystemUrl()));
    }

    /**
     * 设置机器人一键充电
     * @param deviceId 机器人id
     * @return
     */
    @GetMapping(value = "/charge")
    @ApiOperation(value = "设置机器人一键充电", httpMethod = "GET")
    public AjaxResult Charge(@RequestParam(name = "deviceId")String deviceId){


        if(deviceId.equals("JQ-WeiFang-JiuLongYu-JJL-ROBOT-001")){
            return  AjaxResult.success(1);
        }else if(true){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);

        if(list.size() == 0){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        return  AjaxResult.success(zhuoShiCorniceTunnelRobot.Charge(deviceId,list.get(0).getSystemUrl()));
    }


    /**
     *  设置机器人到达预置点
     * @param deviceId 机器人id
     * @param prestId  预置为编号
     * @return
     */
    @GetMapping(value = "/gotoPreset")
    @ApiOperation(value = "设置机器人到达预置点", httpMethod = "GET")
    public AjaxResult GotoPreset(@RequestParam(name = "deviceId")String deviceId,
                                 @RequestParam(name = "presetId")Integer presetId){

        if(deviceId.equals("JQ-WeiFang-JiuLongYu-JJL-ROBOT-001")){
            return  AjaxResult.success(1);
        }else if(true){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);

        if(list.size() == 0){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        return  AjaxResult.success(zhuoShiCorniceTunnelRobot.GotoPreset(deviceId,presetId,list.get(0).getSystemUrl()));
    }


    /**
     *  设置机器人移动
     * @param deviceId 机器人id
     * @param control  0 停止 1向前 2向后
     * @param speed   速度 1-5
     * @return
     */
    @GetMapping(value = "/move")
    @ApiOperation(value = "设置机器人移动", httpMethod = "GET")
    public AjaxResult move(@RequestParam(name = "deviceId")String deviceId,
                           @RequestParam(name = "control")Integer control,
                           @RequestParam(name = "speed", required =false)Integer speed){

        if(deviceId.equals("JQ-WeiFang-JiuLongYu-JJL-ROBOT-001")){
            return  AjaxResult.success(1);
        }else if(true){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        //速度 默认 1
        if(null == speed || 0 == speed){
            speed = 1;
        }
        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);

        if(list.size() == 0){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        return  AjaxResult.success(zhuoShiCorniceTunnelRobot.Move(deviceId,control,speed,list.get(0).getSystemUrl()));
    }


    /**
     *  设置机器人云台
     * @param deviceId 机器人id
     * @param control  0停止 1 上 3 左 5 下 7 右 9 放大 10 缩小
     * @return
     */
    @GetMapping(value = "/ptz")
    @ApiOperation(value = "设置机器人云台", httpMethod = "GET")
    public AjaxResult PTZ(@RequestParam(name = "deviceId")String deviceId,
                          @RequestParam(name = "control")Integer control){

        if(deviceId.equals("JQ-WeiFang-JiuLongYu-JJL-ROBOT-001")){
            return  AjaxResult.success(1);
        }else if(true){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);

        if(list.size() == 0){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        return  AjaxResult.success(zhuoShiCorniceTunnelRobot.PTZ(deviceId,control,list.get(0).getSystemUrl()));
    }


    /**
     * 语言播报功能
     * @param deviceId 机器人id
     * @param text  播报文字内容
     * @return
     */
    @GetMapping(value = "/broadcast")
    @ApiOperation(value = "语言播报功能", httpMethod = "GET")
    public AjaxResult broadcast(@RequestParam(name = "deviceId")String deviceId,
                                @RequestParam(name = "text")String text){


        if(deviceId.equals("JQ-WeiFang-JiuLongYu-JJL-ROBOT-001")){
            return  AjaxResult.success(1);
        }else if(true){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);

        if(list.size() == 0){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        return  AjaxResult.success(zhuoShiCorniceTunnelRobot.Broadcast(deviceId,text,list.get(0).getSystemUrl()));
    }


    /**
     * 设置机器人自动巡航
     * @param deviceId  机器人id
     * @param carmode   切换模式  0 自动巡航  1 手动
     * @return
     */
    @GetMapping(value = "/changeControl")
    @ApiOperation(value = "设置机器人自动巡航", httpMethod = "GET")
    public AjaxResult ChangeControl(@RequestParam(name = "deviceId")String deviceId,
                                    @RequestParam(name = "carmode")Integer carmode){

        if(deviceId.equals("JQ-WeiFang-JiuLongYu-JJL-ROBOT-001")){
            return  AjaxResult.success(1);
        }else if(true){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);

        if(list.size() == 0){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        return  AjaxResult.success(zhuoShiCorniceTunnelRobot.ChangeControl(deviceId,carmode,list.get(0).getSystemUrl()));
    }

    /**
     * 控制机器人雨刷
     * @param deviceId 机器人id
     * @return
     */
    @GetMapping(value = "/controlWindscreen")
    @ApiOperation(value = "控制机器人雨刷", httpMethod = "GET")
    public AjaxResult ControlWindscreen(@RequestParam(name = "deviceId")String deviceId){

        if(deviceId.equals("JQ-WeiFang-JiuLongYu-JJL-ROBOT-001")){
            return  AjaxResult.success(1);
        }else if(true){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);

        if(list.size() == 0){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        return  AjaxResult.success(zhuoShiCorniceTunnelRobot.ControlWindscreen(deviceId,list.get(0).getSystemUrl()));
    }

    /**
     * 获取电量阈值配置
     * @param deviceId 机器人id
     * @return
     */
    @GetMapping(value = "/findAlarmThresholdConfig")
    @ApiOperation(value = "获取电量阈值配置", httpMethod = "GET")
    public AjaxResult FindAlarmThresholdConfig(@RequestParam(name = "deviceId") String deviceId){

        if(deviceId.equals("JQ-WeiFang-JiuLongYu-JJL-ROBOT-001")){
            FindAlarmThresholdConfigDto dto = new FindAlarmThresholdConfigDto();
            dto.setId("1");
            dto.setDeviceId("JQ-WeiFang-JiuLongYu-HSD-ROBOT-001");
            dto.setBeginTime("2023-02-15");
            dto.setEndTime("2023-02-15");
            dto.setPowerLowerLimit("30%");
            dto.setPowerTopLimit("90%");
            dto.setGroupName("默认告警阈值");
            dto.setSendStatus("1");
            return  AjaxResult.success(dto);
        }else if(true){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);

        if(list.size() == 0){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        return  AjaxResult.success(zhuoShiCorniceTunnelRobot.FindAlarmThresholdConfig(deviceId,list.get(0).getSystemUrl()));
    }

    /**
     * 编辑电量阈值配置
     * @param deviceId 机器人 id
     * @param id   主键 id
     * @param groupName 电量阈值配置名称
     * @param beginTime 电量阈值生效开始时间
     * @param endTime   电量阈值生效结束时间
     * @param powerEnable   电池开启状态 1 开启 0 不开启
     * @param powerLowerLimit   电池低电量
     * @param powerTopLimit 电池高电量
     * @return
     */
    @PostMapping(value = "/setAlarmThresholdConfig")
    @ApiOperation(value = "编辑电量阈值配置", httpMethod = "GET")
    public AjaxResult SetAlarmThresholdConfig(@RequestBody FindAlarmThresholdConfigDto findAlarmThresholdConfigDto){

        if(findAlarmThresholdConfigDto.getDeviceId().equals("JQ-WeiFang-JiuLongYu-JJL-ROBOT-001")){
            return  AjaxResult.success(1);
        }else if(true){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);

        if(list.size() == 0){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        return  AjaxResult.success(zhuoShiCorniceTunnelRobot.SetAlarmThresholdConfig(findAlarmThresholdConfigDto,list.get(0).getSystemUrl()));
    }

    /**
     * 初始化机器人
     * @param deviceId  机器人id
     * @return
     */
    @GetMapping(value = "/initializeRobot")
    @ApiOperation(value = "初始化机器人", httpMethod = "GET")
    public AjaxResult InitializeRobot(@RequestParam(name = "deviceId")String deviceId){

        if(deviceId.equals("JQ-WeiFang-JiuLongYu-JJL-ROBOT-001")){
            return  AjaxResult.success(1);
        }else if(true){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);

        if(list.size() == 0){
            return  AjaxResult.error("未查询到外部配置，请核实后再操作");
        }

        return  AjaxResult.success(zhuoShiCorniceTunnelRobot.InitializeRobot(deviceId,list.get(0).getSystemUrl()));
    }


























}
