package com.tunnel.platform.controller.electromechanicalPatrol;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.SdEquipmentCategoryDto;
import com.ruoyi.common.core.domain.TreeCategorySelect;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.system.service.ISysDictTypeService;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdEquipmentState;
import com.tunnel.business.domain.dataInfo.SdEquipmentStateIconFile;
import com.tunnel.business.domain.dataInfo.SdEquipmentType;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.service.dataInfo.*;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.generalcontrol.service.GeneralControlService;
import com.tunnel.platform.service.SdDeviceControlService;
import com.tunnel.platform.service.deviceControl.PhoneSpkService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * app端设备Controller
 * 
 * @author tjw
 * @date 2022-11-29
 */
@RestController
@RequestMapping("/app/devices/list")
public class SdAppDevicesController extends BaseController {

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

    @Autowired
    private ISdTunnelsService sdTunnelsService;

    @Autowired
    private ISdEquipmentCategoryService sdEquipmentCategoryService;


    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private CommonControlService commonControlService;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private SdDeviceControlService sdDeviceControlService;

    @Autowired
    private PhoneSpkService phoneSpkService;


    /**
     * app端  查询设备类型
     *
     * @return
     */
    @ApiOperation("app端  查询设备类型")
    @PostMapping("/app/getDevicesType")
    public Result getDevicesType() {
        List<SdEquipmentType> eqTypeList = equipmentTypeService.getDevicesType();
        return Result.success(eqTypeList);
    }


    /**
     * app端获取设备列表
     * （1-在线，2-离线，3-故障）
     *
     * @param param
     * @param eqType
     * @param eqStatus
     * @return
     */
    @ApiOperation("app端获取设备列表")
    @PostMapping("/app/getAppDevicesList")
    public Map<String, Object> getAppDevicesList(String param, String eqType, String eqStatus, String tunnelId, Integer pageSize, Integer pageNum) {
        param = param.replace(" ", "+");
        Map<String, Object> map = new HashMap();
        String deptId = SecurityUtils.getDeptId();
        List<SdDevices> stateNum = new ArrayList<>();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        int count = devicesService.getAppDevicesCountList(param, eqType, tunnelId, eqStatus, deptId);
        if (count > 0) {
            List<SdDevices> list = devicesService.getAppDevicesList(param, eqType, tunnelId, eqStatus, pageSize, pageNum);
            TableDataInfo devicesList = new TableDataInfo(list, count);
            stateNum = devicesService.getDevicesNum(param, eqType, tunnelId, eqStatus, pageSize, pageNum);
            map.put("devicesList", devicesList);
            map.put("stateNum", stateNum);
            return map;
        }
        map.put("devicesList", null);
        map.put("stateNum", stateNum);
        return map;


        //Map<String,Object>map = devicesService.getAppDevicesList(param,eqType,eqStatus);
        //return Result.success(map);
    }

    /**
     * app端获取设备信息
     *
     * @param eqId
     * @return
     */
    @ApiOperation("app端获取设备信息")
    @GetMapping("/app/getAppDevicesInfo")
    public Result getAppDevicesInfo(String eqId) {
        Map<String, Object> map = new HashMap<>();
        List<SdDevices> eqList = devicesService.getAppDevicesInfo(eqId);
        List<SdDevices> statusList = devicesService.getAppDevicesStatus(eqId);
        if (statusList != null && statusList.size() > 0) {
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
            map.put("stateList", list);

            // 设备参数
            List<Map> itemData = sdDeviceDataService.getItemDataByEqId(eqId);
            map.put("itemData", itemData);

            eqList.get(0).setParams(map);
        }


        return Result.success(eqList);
    }

    /**
     * app端获取操控日志
     *
     * @param time
     * @return
     */
    @ApiOperation("app端获取操控日志")
    @PostMapping("/app/logList")
    public TableDataInfo getLogList(String eqId, String time, Integer pageSize, Integer pageNum) {
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        int count = sdOperationLogService.selectAppOperationLogCountList(eqId, time, deptId);
        if (count > 0) {
            List<SdOperationLog> list = sdOperationLogService.selectAppOperationLogList(eqId, time, deptId, pageSize, pageNum);
            return new TableDataInfo(list, count);
        }

        return new TableDataInfo(null, 0);

    }

    /**
     * app端  设备控制接口  （仅支持广播设备）
     *
     * @param eqId       设备编号
     * @param state      设备状态
     * @param brightness 亮度
     * @param frequency  频率
     * @param fireMark   标号位置信息
     * @return
     */
    @ApiOperation("app端  设备控制接口  （仅支持广播设备）")
    @GetMapping("/app/controlDevice")
    public AjaxResult controlDevice(String eqId, String state, String brightness, String frequency, String fireMark) {

        if (eqId == null) {
            return AjaxResult.error("设备Id不能为空");
        }

        if (state == null) {
            return AjaxResult.error("状态信息能为空");
        }

        SdDevices sdDevices = devicesService.selectSdDevicesById(eqId);

        if (sdDevices == null) {
            return AjaxResult.error("设备不存在");
        }

        if (DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().equals(sdDevices.getEqType())) {
            if (Integer.parseInt(brightness) < 30 && state.equals("1")) {
                return AjaxResult.error("基本照明亮度不得低于30");
            }
        }
        //设备控制
        GeneralControlBean generalControlBean = generalControlService.getProtocolBean(sdDevices);
        if (generalControlBean == null) {
            return AjaxResult.error("设备协议配置为空");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("devId", eqId);
        map.put("state", state);
        map.put("brightness", brightness);
        map.put("frequency", frequency);
        map.put("fireMark", fireMark);

        AjaxResult ajaxResult = generalControlBean.control(map, sdDevices);

        return ajaxResult;
    }

    /**
     * app端  批量设备控制接口  （仅支持车道指示器，风机，卷帘门）
     *
     * @param eqType     设备类型
     * @param state      设备状态
     * @param mac        MAC地址
     * @param lane       车道
     * @return
     */
    @ApiOperation("app端  批量设备控制接口  （仅支持车道指示器，风机，卷帘门）")
    @GetMapping("/app/batchControlDevice")
    public AjaxResult batchControlDevice(String eqType,String mac,String lane,String state) {

        if (eqType == null || !(eqType.equals("1") || eqType.equals("12") || eqType.equals("10"))) {
            return AjaxResult.error("快捷操作只支持车道指示器，风机，卷帘门");
        }
        if (mac == null) {
            return AjaxResult.error("MAC信息不能为空");
        }
        //卷连门不设置车道
        if (lane == null && !eqType.equals("12")) {
            return AjaxResult.error("车道信息不能为空");
        }
        if (state == null) {
            return AjaxResult.error("状态信息能为空");
        }

        boolean isopen = commonControlService.queryAnalogControlConfig();

        Map<String, Object> map = new HashMap<>();
        map.put("operIp", IpUtils.getIpAddr(ServletUtils.getRequest()));

        List<String> eqIdList = devicesService.getDevicesListByMacAndEqTypeAndLane(mac,eqType,lane);

        int count = 0;
        for(String devId : eqIdList){

            SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);

            map.put("devId", devId);
            map.put("state", state);
            map.put("controlType", "0");
          //  map.put("frequency", frequency);
          //  map.put("brightness", stateNum);

            if (!isopen) {
                count = sdDeviceControlService.controlDevices(map);
            } else {
                count = commonControlService.analogControl(map,sdDevices);

                SdOperationLog sdOperationLog = commonControlService.getOperationLog(map,sdDevices,count);
                sdOperationLogService.insertSdOperationLog(sdOperationLog);
            }
        }
        if(count != 0){
            return AjaxResult.success(count);
        }

        return AjaxResult.error("未查询到设备");

    }

    /**
     * 批量控制广播设备
     * @param map
     * @return
     */
    @ApiOperation("批量控制广播设备")
    @PostMapping(value = "/app/playVoice")
    public AjaxResult playVoice(@RequestBody Map<String, Object> map) {
        return phoneSpkService.playVoice(map);
    }


    /**
     * app端  查询控制执行器关联设备列表
     *
     * @return
     */
    @ApiOperation("app端  查询控制执行器关联设备列表")
    @GetMapping("/app/getMcaDevList")
    public Result getMcaDevList() {

        return Result.success(devicesService.getMcaDevList());
    }

    /**
     * app端  查询测控执行器列表
     * @return
     */
    @ApiOperation("app端  查询测控执行器列表")
    @GetMapping("/app/getMcaList")
    public Result getMcaList() {

        return Result.success(devicesService.getMcaList());
    }

    /**
     * 根据MAC所在隧道查询当前洞内所有测控执行器
     * @return
     */
    @ApiOperation("根据MAC所在隧道查询当前洞内所有测控执行器")
    @GetMapping("/app/getMoreMcaListByMac")
    public Result getMoreMcaListByMac(String mac) {

        return Result.success(devicesService.getMoreMcaListByMac(mac));
    }

    /**
     * 根据MAC所在隧道和设备类型查询当前洞内所有设备
     * @return
     */
    @ApiOperation("根据MAC所在隧道和设备类型查询当前洞内所有设备")
    @GetMapping("/app/getMoreDevListByMacAndEqType")
    public Result getMoreDevListByMacAndEqType(String mac,String eqType) {

        return Result.success(devicesService.getMoreDevListByMacAndEqType(mac,eqType));
    }

    /**
     * 获取广播文件列表
     * @return
     */
    @ApiOperation("获取广播文件列表")
    @GetMapping("/app/getVideoItem")
    public AjaxResult getVideoItem(String dictType) {

        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data))
        {
            data = new ArrayList<>();
        }
        return AjaxResult.success(data);
    }


    /**
     * 查询当前登录者所属
     *
     * @return
     */
    @ApiOperation("查询当前登录者所属")
    @GetMapping("/app/getJlyTunnel")
    public AjaxResult getJlyTunnel() {
        return AjaxResult.success(sdTunnelsService.getJlyTunnel());
    }

    /**
     * 根据设备名称查询设备详情
     */
    @ApiOperation("根据设备名称查询设备详情")
    @GetMapping(value = "/app/getEquipmentInfo")
    public AjaxResult getEquipmentInfo(String eqId) {
        //eqId = "JQ-JiNan-WenZuBei-CAM-RSU-012";//模拟数据
        return AjaxResult.success(devicesService.getEquipmentInfo(eqId));
    }


    /**
     * 查询设备类型
     *
     * @return
     */
    @ApiOperation("查询设备类型")
    @GetMapping(value = "/app/getCategoryTree")
    public AjaxResult getCategoryTree() {
        List<SdEquipmentCategoryDto> list = sdEquipmentCategoryService.getCategoryList();

        List<TreeCategorySelect> treeCategory = sdEquipmentCategoryService.buildCategoryTreeSelect(list);

        return AjaxResult.success(treeCategory);
    }


    /**
     * 设备类型树结构
     * 三级：大类--设备类型--设备
     *
     * @return
     */
    @ApiOperation("设备类型树结构")
    @GetMapping(value = "/app/getCategoryDeviceTree")
    public AjaxResult getCategoryDeviceTree() {
        List<SdEquipmentCategoryDto> list = sdEquipmentCategoryService.getCategoryTypeDeviceList();

        List<TreeCategorySelect> treeCategoryDevice = sdEquipmentCategoryService.buildCategoryTreeSelect(list);


        return AjaxResult.success(treeCategoryDevice);

    }
}
