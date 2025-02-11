package com.tunnel.platform.controller.dataInfo;

import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.TokenService;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.PlatformAuthEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdDevicesBrand;
import com.tunnel.business.domain.informationBoard.SdIotDevice;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.informationBoard.ISdIotDeviceService;
import com.tunnel.platform.controller.platformAuthApi.PlatformApiController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.schema.Entry;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备Controller
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
@RestController
@RequestMapping("/devices")
@Api(tags = "设备管理")
@ApiSupport(order = 16)
public class SdDevicesController extends BaseController {
    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private ISdIotDeviceService sdIotDeviceService;
    @Autowired
    private TokenService tokenService;

    /**
     * 平台
     */
    @Value("${authorize.name}")
    private String platformName;

    /**
     * 推送、接收controller
     */
    @Autowired
    private PlatformApiController sdPlatformApiController;

    /**
     * redis工具类
     */
    @Autowired
    private RedisCache redisCache;

    /**
     * 查询设备列表
     */
    @ApiOperation("查询设备列表")
    @GetMapping("/list")
    public TableDataInfo<List<SdDevices>> list(SdDevices sdDevices) {
        if (null == sdDevices.getDeptId() || "".equals(sdDevices.getDeptId())) {
            String deptId = SecurityUtils.getDeptId();
            sdDevices.setDeptId(deptId);
        }
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(sdDevices);
        //1：代表执行校验指令SQL
        if ("1".equals(sdDevices.getEqDirection())) {
            List<String> eqIds = new ArrayList<String>();
            List<SdDevices> checklist = sdDevicesService.getChecklist(list);
            for (SdDevices devices : checklist) {
                eqIds.add(devices.getEqId());
            }
            startPage();
            sdDevices.setEqIds(eqIds);
            list = sdDevicesService.selectSdDevicesList(sdDevices);
        } else {
            startPage();
            list = sdDevicesService.selectSdDevicesList(sdDevices);
        }

        // 如果当前设备是加强照明 基本照明，双向显示设备去重
        if(StringUtils.isNull(TableSupport.buildPageRequest().getPageNum()) && list != null && list.size() > 0  && sdDevices.getEqType() != null &&
                (
                        sdDevices.getEqType().longValue() == DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().longValue()
                       || sdDevices.getEqType().longValue() == DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().longValue()
                )
        ){

            Collections.reverse(list);
            list = list.stream().collect(
                    Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SdDevices::getEqId))), ArrayList::new));
        }


        return getDataTable(list);
    }

    /**
     *按类型和隧道获取设备
     * @param sdDevices
     * @return
     */
    @ApiOperation("按类型和隧道获取设备")
    @GetMapping("/getDevicesByTypeAndTunnel")
    public TableDataInfo<List<Map<String, Object>>> getDevicesByTypeAndTunnel(SdDevices sdDevices) {
        List<Map<String, Object>> devicesByTypeAndTunnel = sdDevicesService.getDevicesByTypeAndTunnel(sdDevices);
        return getDataTable(devicesByTypeAndTunnel);
    }

    /**
     * 查询传感器设备列表
     */
    @ApiOperation("查询传感器设备列表")
    @GetMapping("/sensorList")
    public Result<List<SdDevices>> sensorList(SdDevices sdDevices) {
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(sdDevices);
        return Result.success(list);
    }

    /**
     * 大屏查询设备数据
     */
    @ApiOperation("大屏查询设备数据")
    @GetMapping("/bigscreenlist")
    // @ApiOperation("大屏查询设备数据")
    //  @ApiImplicitParam(name = "eqTunnelId", value = "所属隧道 ID", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class)
    public int[] bigscreenlist(String eqTunnelId) {
        SdDevices sd = new SdDevices();
        sd.setEqTunnelId(eqTunnelId);
        sd.setEqType(1L);
        int d1 = sdDevicesService.selectSdDevicesList(sd).size();//普通车道指示器
        sd.setEqType(2L);
        int d2 = sdDevicesService.selectSdDevicesList(sd).size();//带左转车道指示器
        sd.setEqType(3L);
        int d3 = sdDevicesService.selectSdDevicesList(sd).size();//普通交通信号灯
        sd.setEqType(4L);
        int d4 = sdDevicesService.selectSdDevicesList(sd).size();//带左转交通信号灯
        sd.setEqType(5L);
        int d5 = sdDevicesService.selectSdDevicesList(sd).size();//洞内亮度
        sd.setEqType(6L);
        int d6 = sdDevicesService.selectSdDevicesList(sd).size();//洞外亮度
        sd.setEqType(7L);
        int d7 = sdDevicesService.selectSdDevicesList(sd).size();//加强照明
        sd.setEqType(10L);
        int d10 = sdDevicesService.selectSdDevicesList(sd).size();//主风机
        sd.setEqType(17L);
        int d17 = sdDevicesService.selectSdDevicesList(sd).size();//卷帘门
        SdIotDevice sdIotDevice = new SdIotDevice();
        int qbblist = sdIotDeviceService.selectIotDeviceArrayList(sdIotDevice).size();//情报板
        int[] a = {d3 + d4, d1 + d2, d17, d10, d7, qbblist, d5 + d6};
        return a;
    }


    /**
     * 查询设备列表+情报板
     */
    @ApiOperation("查询设备列表+情报板")
    @GetMapping("/alllist")
    public TableDataInfo<List<SdDevices>> qbblist(SdDevices sdDevices) {
        startPage();
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(sdDevices);
        SdIotDevice iotDevice = new SdIotDevice();
        List<SdIotDevice> qbblist = sdIotDeviceService.selectIotDeviceArrayList(iotDevice);
        for (SdIotDevice io : qbblist) {
            SdDevices sd = new SdDevices();
            sd.setEqId(io.getDeviceId().toString());
            sd.setEqTunnelId(io.getTunnelId());
            sd.setEqType(100L);
            list.add(sd);
        }

        return getDataTable(list);
    }

    /**
     * 导出设备列表
     */
    @ApiOperation("导出设备列表")
    @Log(title = "设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public AjaxResult export(SdDevices sdDevices) {
        if (null == sdDevices.getDeptId() || "".equals(sdDevices.getDeptId())) {
            String deptId = SecurityUtils.getDeptId();
            sdDevices.setDeptId(deptId);
        }
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(sdDevices);
        //1：代表执行校验指令SQL
        if ("1".equals(sdDevices.getEqDirection())) {
            List<String> eqIds = new ArrayList<String>();
            List<SdDevices> checklist = sdDevicesService.getChecklist(list);
            for (SdDevices devices : checklist) {
                eqIds.add(devices.getEqId());
            }
            sdDevices.setEqIds(eqIds);
            list = sdDevicesService.selectSdDevicesList(sdDevices);
        } else {
            list = sdDevicesService.selectSdDevicesList(sdDevices);
        }
        /*List<SdDevices> list = sdDevicesService.selectSdDevicesList_exp(sdDevices);*/
        ExcelUtil<SdDevices> util = new ExcelUtil<SdDevices>(SdDevices.class);
        return util.exportExcel(list, "设备管理");
    }

    /**
     * 获取设备详细信息
     */
    @GetMapping(value = "/{eqId}")
    @ApiOperation("获取设备详细信息")
    @ApiImplicitParam(name = "eqId", value = "设备ID", required = true, dataType = "String", paramType = "path", dataTypeClass = String.class)
    public Result<SdDevices> getInfo(@PathVariable("eqId") String eqId) {
        SdDevices sd = sdDevicesService.selectSdDevicesById(eqId);
        return Result.success(sd);
    }


    /**
     * 获取设备详细信息
     */
    @GetMapping(value = "/getDevice/{eqId}")
    @ApiOperation("获取设备详细信息")
    @ApiImplicitParam(name = "eqId", value = "设备ID", required = true, dataType = "String", paramType = "path", dataTypeClass = String.class)
    public Result<Map> getDevice(@PathVariable("eqId") String eqId) {
        Map<String, String> sd = sdDevicesService.queryDeviceById(eqId);
        return Result.success(sd);
    }

    /**
     *获取防火标志清单
     * @param eqId
     * @return
     */
    @ApiOperation("获取防火标志清单")
    @GetMapping("/fireMarkList/{eqId}")
    public Result<List<String>> fireMarkList(@PathVariable("eqId") String eqId) {
        List<String> list = sdDevicesService.fireMarkList(eqId);
        return Result.success(list);
    }

    /**
     * 通过隧道id查询设备类型名称
     */
    /* @GetMapping(value = "/{tunnelId}")*/
    @GetMapping("/tunnelId")
    @ApiOperation("通过隧道id查询设备类型名称")
    @ApiImplicitParam(name = "eqTunnelId", value = "所属隧道 ID", required = true, dataType = "String", paramType = "path", dataTypeClass = String.class)
    public Result<List<Map<String, Object>>> getEquipmentInfo(String eqTunnelId) {
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqTunnelId(eqTunnelId);
        List<Map<String, Object>> list = sdDevicesService.selectSdDevicesByTunnelId(sdDevices);
        return Result.success(list);
    }

    /**
     * 新增设备
     */
    @Log(title = "设备", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增设备")
    public Result add(@RequestBody SdDevices sdDevices) {
        if (sdDevices.getDeliveryTime() != null && sdDevices.getWarrantyEndTime() != null
                && sdDevices.getDeliveryTime().getTime() > sdDevices.getWarrantyEndTime().getTime()) {
            throw new RuntimeException("出厂时间不能晚于维保截止时间");
        }
        if (sdDevices.getDeliveryTime() != null && sdDevices.getInstallTime() != null
                && sdDevices.getDeliveryTime().getTime() > sdDevices.getInstallTime().getTime()) {
            throw new RuntimeException("出厂时间不能晚于设备安装时间");
        }
        SdDevices sd = new SdDevices();
        sd.setEqId(sdDevices.getEqId());
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(sd);
        if (list.size() > 0) {
            // return Result.error(1,"当前设备ID重复，请重新输入");
            return Result.error("当前设备ID重复，请重新输入");
        }

        /*List<SdDevices> list1 = sdDevicesService.tunnelEqNameOnly(sdDevices.getEqTunnelId(), sdDevices.getEqName());
        if (list1.size() > 0) {
            return Result.error("当前设备名称已经存在，请核对后重试！");
        }*/

        int i = sdDevicesService.insertSdDevices(sdDevices);
        if (sdDevices.getEqType() != 31L) {
            sdDevicesService.insertOrUpdateOrDeleteSdDeviceCmd(sdDevices);
        }
        //管理站平台下推送
        if (PlatformAuthEnum.GLZ.getCode().equals(platformName) && i > 0) {
            List<SdDevices> sdDevicesList = new ArrayList<>();
            sdDevicesList.add(sdDevices);
            sdPlatformApiController.devicesPush(sdDevicesList, "add", null);
        }
        return Result.toResult(i);
    }

    /**
     *自动Id
     * @param tunnelId
     * @param typeId
     * @return
     */
    @ApiOperation("自动Id")
    @PostMapping("/autoId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tunnelId", value = "隧道ID", required = true, dataType = "String", paramType = "path", dataTypeClass = String.class),
            @ApiImplicitParam(name = "typeId", value = "类型ID", required = true, dataType = "Integer", paramType = "path", dataTypeClass = Integer.class),
    })

    public Result autoId(@RequestParam("tunnelId") String tunnelId, @RequestParam("typeId") Integer typeId) {
        return Result.success(sdDevicesService.autoId(tunnelId, typeId));
    }

    /**
     * 修改设备
     */
    @Log(title = "设备", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改设备")
    public Result edit(@RequestBody SdDevices sdDevices) {
        if (sdDevices.getDeliveryTime() != null && sdDevices.getWarrantyEndTime() != null
                && sdDevices.getDeliveryTime().getTime() > sdDevices.getWarrantyEndTime().getTime()) {
            throw new RuntimeException("出厂时间不能晚于维保截止时间");
        }
        if (sdDevices.getDeliveryTime() != null && sdDevices.getInstallTime() != null
                && sdDevices.getDeliveryTime().getTime() > sdDevices.getInstallTime().getTime()) {
            throw new RuntimeException("出厂时间不能晚于设备安装时间");
        }

        /*List<SdDevices> list1 = sdDevicesService.tunnelEqNameOnly(sdDevices.getEqTunnelId(), sdDevices.getEqName());
        if (list1.size() > 0 && !sdDevices.getEqId().equals(list1.get(0).getEqId())) {
            return Result.error("当前设备名称已经存在，请核对后重试！");
        }*/

        int i = sdDevicesService.updateSdDevices(sdDevices);
        if (sdDevices.getEqType() != 31L) {
            sdDevicesService.insertOrUpdateOrDeleteSdDeviceCmd(sdDevices);
        }
        //管理站平台下推送
        if (PlatformAuthEnum.GLZ.getCode().equals(platformName) && i > 0) {
            List<SdDevices> sdDevicesList = new ArrayList<>();
            sdDevicesList.add(sdDevices);
            sdPlatformApiController.devicesPush(sdDevicesList, "edit" , null);
        }
        return Result.toResult(i);
    }

    /**
     * 删除设备
     */
    @ApiOperation("删除设备")
    @Log(title = "设备", businessType = BusinessType.DELETE)
    @DeleteMapping("/{eqIds}")
    @ApiImplicitParam(name = "eqIds", value = "需要删除的设备ID", required = true, dataType = "String", paramType = "path", dataTypeClass = String.class)
    public Result remove(@PathVariable String[] eqIds) {
        int i = sdDevicesService.deleteSdDevicesByIds(eqIds);
        //管理站平台下推送
        if (PlatformAuthEnum.GLZ.getCode().equals(platformName) && i > 0) {
            List<SdDevices> sdDevicesList = new ArrayList<>();
            SdDevices sdDevices = new SdDevices();
            sdDevices.setEqIds(Arrays.asList(eqIds));
            sdDevices.setCreateTime(DateUtils.getNowDate());
            sdDevicesList.add(sdDevices);
            sdPlatformApiController.devicesPush(sdDevicesList, "del", null);
        }
        return Result.toResult(i);
    }

    /**
     *导入模板
     * @param response
     * @return
     * @throws IOException
     */
    @ApiOperation("导入模板")
    @PostMapping("/importTemplate")
    public AjaxResult importTemplate(HttpServletResponse response) throws IOException {
//        util.exportExcel( list, "设备详情");
        ExcelUtil<SdDevices> util = new ExcelUtil<SdDevices>(SdDevices.class);
        return util.importTemplateExcel("设备数据");
    }

    /**
     *导入数据
     * @param file
     * @param updateSupport
     * @return
     * @throws Exception
     */
    @ApiOperation("导入数据")
    @Log(title = "设备管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        if(file!=null){
            String str1 = file.getOriginalFilename();
            String str = str1.substring(str1.indexOf(".") + 1);
            System.out.println("文件类型为============"+str);
            if(!"xls".equals(str)&&!"xlsx".equals(str)){
                String message = "抱歉，导入失败，仅允许导入“xls”或“xlsx”格式文件！";
                return AjaxResult.success(message);
            }
        }
        ExcelUtil<SdDevices> util = new ExcelUtil<SdDevices>(SdDevices.class);
        List<SdDevices> userList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = sdDevicesService.importSdDevices(userList, updateSupport, operName);
        //管理站平台下推送
        if (PlatformAuthEnum.GLZ.getCode().equals(platformName)) {
            userList.stream().forEach(sdDevices -> sdDevices.setUpdateSupport(updateSupport));
            sdPlatformApiController.devicesPush(userList, "import", operName);
        }
        return AjaxResult.success(message);
    }

    /**
     * 生成控制指令
     */
    @ApiOperation("生成控制指令")
    @PostMapping(value = "/createDmcontrolSeat")
    public Map createDmcontrolSeat(@RequestBody SdDevices sdDevices) {
        Map<String, String> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        String deviceState = sdDevices.getInstruction();//指令模式
        //CIO 查询指令
        sb.append(sdDevicesService.getCommandCode(sdDevices, sdDevices.getSeat(), deviceState.split("_")[0], deviceState.split("_")[1]));
        sb.append(sdDevicesService.getIpleftPad(sdDevices.getqNumber()));//点位地址
        map.put("instruction", sb.toString());
        map.put("instructionAndseat", deviceState.split("_")[0] + "_" + sdDevices.getSeat());
        return map;
    }

    /**
     * 生成查询指令
     */
    @ApiOperation("生成查询指令")
    @PostMapping(value = "/createInstruction")
    public Map createInstruction(@RequestBody SdDevices sdDevices) {
        Map<String, String> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        //CIO 查询指令
//        sb.append(sdDevicesService.getCommandCode(sdDevices, sdDevices.getInstructionSeat().split("_")[1], sdDevices.getInstructionSeat().split("_")[0], "0"));
        sb.append(sdDevicesService.getIpleftPad(sdDevices.getqNumber()));//点位地址
        map.put("instructionSeat", sb.toString());
        return map;
    }


    /**
     * 校验指令
     */
    @ApiOperation("校验指令")
    @GetMapping("/checkInstruction")
    public Map checkInstruction(SdDevices sdDevices) {
        Map<String, Object> map = new HashMap<>();
        List<SdDevices> checklist = new ArrayList<SdDevices>();
        StringBuffer sb = new StringBuffer();
        //循环列表数据
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(sdDevices);
        map.put("sumDate", list.size());
        map.put("errorDate", sdDevicesService.getChecklist(list));
        return map;
    }

    /**
     * 获取所有压力表信息
     */
    @ApiOperation("获取所有压力表信息")
    @GetMapping("/getAllPressureGaugesMsg")
    public Result<List<SdDevices>> getAllPressureGaugesMsg() {
        return Result.success(sdDevicesService.getAllPressureGaugesMsg());
    }

    /**
     * 获取运营APP首页需要的设备信息
     */
    @ApiOperation("获取运营APP首页需要的设备信息")
    @PostMapping("/getDevicesMsgToApp")
    //  @ApiImplicitParam(name = "tunnelId", value = "隧道ID", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class)
    public Result getDevicesMsgToApp(String tunnelId) {
//        if(tunnelId.equals("") || tunnelId.isEmpty()){
//            throw new RuntimeException("当前获取查询参数为空，无法进行后续操作!");
//        }
        Map<String, Object> details = new HashMap<>();
        //正常设备数
        details.put("normalEquipment", "89");
        //故障设备数
        details.put("faultyEquipment", "9");
        //设备完好率
        details.put("equipmentIntactRate", "6");
        //今日修复
        details.put("repairToday", "0");
        //暂未修复
        details.put("notRepairedYet", "0");
        return Result.success(details);
    }

    /**
     *获取设备状态
     * @param tunnelId
     * @return
     */
    @ApiOperation("获取设备状态")
    @PostMapping("/getDevicesStatus")
    @ApiImplicitParam(name = "tunnelId", value = "隧道ID", required = true, dataType = "String", paramType = "path", dataTypeClass = String.class)
    public AjaxResult getDevicesStatus(String tunnelId) {
        return AjaxResult.success(sdDevicesService.getDevicesStatus(tunnelId));
    }

    /**
     *获得设备能量消耗
     * @param tunnelId
     * @return
     */
    @ApiOperation("获得设备能量消耗")
    @PostMapping("/obtainEquipmentEnergyConsumption")
    @ApiImplicitParam(name = "tunnelId", value = "隧道ID", required = true, dataType = "String", paramType = "path", dataTypeClass = String.class)
    public AjaxResult obtainEquipmentEnergyConsumption(String tunnelId) {
        return AjaxResult.success(sdDevicesService.obtainEquipmentEnergyConsumption(tunnelId));
    }

    /**
     * 查询可控设备列表
     */
    @ApiOperation("查询可控设备列表")
    @GetMapping("/isControl")
    public TableDataInfo<List<SdDevices>> isControlList(SdDevices sdDevices) {
        if (null == sdDevices.getDeptId() || "".equals(sdDevices.getDeptId())) {
            String deptId = SecurityUtils.getDeptId();
            sdDevices.setDeptId(deptId);
        }
        List<SdDevices> list = sdDevicesService.selectIsControlSdDevicesList(sdDevices);
        //1：代表执行校验指令SQL
        if ("1".equals(sdDevices.getEqDirection())) {
            List<String> eqIds = new ArrayList<String>();
            List<SdDevices> checklist = sdDevicesService.getChecklist(list);
            for (SdDevices devices : checklist) {
                eqIds.add(devices.getEqId());
            }
            startPage();
            sdDevices.setEqIds(eqIds);
            list = sdDevicesService.selectIsControlSdDevicesList(sdDevices);
        } else {
            startPage();
            list = sdDevicesService.selectIsControlSdDevicesList(sdDevices);
        }
        return getDataTable(list);
    }


    /**
     * 查询设备品牌列表
     */
    @GetMapping("/getDevBrandList")
    @ApiOperation("查询设备品牌列表")
    public AjaxResult getDevBrandList() {
        List<SdDevicesBrand> list = sdDevicesService.getDevBrandList();
        return AjaxResult.success(list);
    }

    /**
     * 查询左洞或右洞的广播设备
     * @param sdDevices
     * @return
     */
    @ApiOperation("查询左洞或右洞的广播设备")
    @GetMapping(value = "/getSpkList")
    public AjaxResult getSpkList(SdDevices sdDevices) {

        String eqTunnelId = sdDevices.getEqTunnelId();
        String eqDirection = sdDevices.getEqDirection();
        Assert.hasText("eqTunnelId", "请指定所属隧道！");
        Assert.hasText("eqDirection", "请指定隧道方向！");

        sdDevices.setEqType(DevicesTypeEnum.LS.getCode());
        List<SdDevices> list = sdDevicesService.getSpkList(sdDevices);
        return AjaxResult.success(list);
    }

    /**
     * 查询级联选择设备列表
     *
     * @param sdDevices
     * @return
     */
    @ApiOperation("查询级联选择设备列表")
    @GetMapping("/getTreeDeviceList")
    public AjaxResult getTreeDeviceList(SdDevices sdDevices){
        return sdDevicesService.getTreeDeviceList(sdDevices);
    }

    /**
     * 小车跑数据控制
     * @param eqId 隧道id
     * @param switchType 开关状态
     * @return
     */
    @ApiOperation("小车跑数据控制")
    @GetMapping(value = "/carSwitchType/{eqId}/{switchType}")
    public void carSwitchType(@PathVariable("eqId") String eqId,@PathVariable("switchType") String switchType){
        //获取token
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String token = loginUser.getToken();
        //判断该token是否存在
        Map<String, Object> cacheMap = redisCache.getCacheMap(getCacheKey(token));
        //不存在则新增  destroyed标志 前端页面 跳转 刷新 关闭所有运行
        if(StringUtils.isEmpty(cacheMap)&&!"destroyed".equals(eqId)){
            Map<String, String> objectHashMap = new HashMap<>();
            objectHashMap.put(eqId,switchType);
            // 缓存到redis中
            redisCache.setCacheMap(getCacheKey(token),objectHashMap);
            List<String> scanKey = redisCache.getScanKey(Constants.CAR_TOKEN + "*");
            redisCache.deleteObject("caKokenList");
            redisCache.setCacheList("caKokenList",scanKey);
            return;
        }

        //清空集合
//        cacheMap = new HashMap<>();
        // 特殊标记不要添加到map destroyed
        if(!"destroyed".equals(eqId)){
            cacheMap.replaceAll((key, value) -> "1");
            //添加 要运行的
            cacheMap.put(eqId,switchType);
        }else{//带有destroyed就说明页面跳转刷新了所以要删除这个token
            redisCache.deleteObject(getCacheKey(token));
            return;
        }
        //修改  先删除在新增
        redisCache.deleteObject(getCacheKey(token));
        redisCache.setCacheMap(getCacheKey(token),cacheMap);
        //获取所有需要发送消息客服端的token
        if("0".equals(switchType)){
            List<String> scanKey = redisCache.getScanKey(Constants.CAR_TOKEN + "*");
            redisCache.deleteObject("caKokenList");
            redisCache.setCacheList("caKokenList",scanKey);
        }else{
            redisCache.deleteObject("caKokenList");
        }

    }

    /**
     * 查询摄像机
     * @param sdDevices
     * @return
     */
    @ApiOperation("查询摄像机")
    @GetMapping("/getCamera")
    public AjaxResult getCamera(SdDevices sdDevices){
        return sdDevicesService.getCamera(sdDevices);
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    @ApiOperation("设置cache key")
    private String getCacheKey(String configKey)
    {
        return Constants.CAR_TOKEN + configKey;
    }

    /**
     * 高速云同步设备隧道
     * @param requestData
     */
    @ApiOperation("高速云同步设备隧道")
    @PostMapping("/syncData")
    public String syncData(@RequestBody HashMap<String, Object> requestData){
        if (PlatformAuthEnum.GSY.getCode().equals(platformName)) {
            sdDevicesService.syncData(requestData);
        }
        return "";
    }
}
