package com.tunnel.platform.controller.event;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.domain.dataInfo.SdAlarmModel;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.event.*;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.event.ISdEmergencyDeviceService;
import com.tunnel.business.service.event.ISdEmergencyPerService;
import com.tunnel.platform.service.event.ISdStrategyService;
import com.tunnel.business.service.event.ISdWarningInfoService;
import com.tunnel.business.utils.util.ImgTobase64;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 预警信息Controller
 *
 * @author gongfanfei
 * @date 2020-08-21
 */
@RestController
@RequestMapping("/warningInfo")
@Api(tags = "应急处置")
public class SdWarningInfoController extends BaseController
{
    @Autowired
    private ISdWarningInfoService sdWarningInfoService;
    @Autowired
    private ISdStrategyService sdStrategyService;
    @Autowired
    private ISdEmergencyDeviceService sdEmergencyDeviceService;
    @Autowired
    private ISdEmergencyPerService sdEmergencyPerService;

    /**
     * 查询预警信息列表
     */
    @GetMapping("/list")
    @ApiOperation("查询预警信息列表")
    public TableDataInfo<List<SysRole>> list(SdWarningInfo sdWarningInfo)
    {
        startPage();
        List<SdWarningInfo> list = sdWarningInfoService.selectSdWarningInfoList(sdWarningInfo);
        return getDataTable(list);
    }

    /**
     * 查询预警信息列表
     */
    @GetMapping("/warningList")
    @ApiOperation("查询预警信息列表")
    public TableDataInfo<List<SdWarningInfo>> warningList(SdWarningInfo sdWarningInfo)
    {
        startPage();
        List<SdWarningInfo> list = sdWarningInfoService.selectSdWarningList(sdWarningInfo);
        return getDataTable(list);
    }

    /**
     * 导出预警信息列表
     *//*
    @Log(title = "预警信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdWarningInfo sdWarningInfo) throws IOException
    {
        List<SdWarningInfo> list = sdWarningInfoService.selectSdWarningInfoList(sdWarningInfo);
        ExcelUtil<SdWarningInfo> util = new ExcelUtil<SdWarningInfo>(SdWarningInfo.class);
        util.exportExcel(response, list, "warningInfo");
    }*/

    /**
     * 查询当日报警数量
     */
    @GetMapping("/fireAlarmCount")
    @ApiOperation("查询当日报警数量")
    public Result  fireAlarmCount(SdWarningInfo sdWarningInfo)
    {
        startPage();
        Map<String ,Object> fireAlarmCount = sdWarningInfoService.selectSdWarningInfoCount(sdWarningInfo);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("unProcessed", fireAlarmCount);
        return Result.success(map);
    }
    /**
     * 查询最近7天报警数量
     */
    @GetMapping("/fireWeekList")
    @ApiOperation("查询最近7天报警数量")
    public Result  fireWeekList(SdWarningInfo sdWarningInfo)
    {
        List<Map<String,String>> echartsData = sdWarningInfoService.selectSdWarningInfoEcharts(sdWarningInfo);
        startPage();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("echartsData", echartsData);
        return Result.success(map);
    }

    /**
     * 获取预警信息详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取预警信息详细信息")
    @ApiImplicitParam(name = "id", value = "预警信息ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result<SdWarningInfo> getInfo(@PathVariable("id") Long id)
    {
        return Result.success(sdWarningInfoService.selectSdWarningInfoById(id));
    }

    /**
     * 获取策略列表信息
     */
    @GetMapping(value = "/getStrategyList/{id}")
    @ApiOperation("获取策略列表信息")
    @ApiImplicitParam(name = "id", value = "预警信息ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result getStrategyList(@PathVariable("id") Long id)
    {
        SdWarningInfo sdWarningInfo = sdWarningInfoService.selectSdWarningInfoById(id);
        SdStrategy sdStrategy=new SdStrategy();
        sdStrategy.setTunnelId(sdWarningInfo.getTunnelId());
        sdStrategy.setDirection(("Z".equals(sdWarningInfo.getHoleDirection()) ? "1":"0"));
        sdStrategy.setStrategyType("0");
        return Result.success(sdStrategyService.selectSdStrategyList(sdStrategy));
    }
    /**
     * 获取应急物资列表信息
     */
    @GetMapping(value = "/emdeviceList/{id}")
    @ApiOperation("获取应急物资列表信息")
    @ApiImplicitParam(name = "id", value = "预警信息ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result emdeviceList(@PathVariable("id") Long id)
    {
        SdWarningInfo sdWarningInfo = sdWarningInfoService.selectSdWarningInfoById(id);
        SdEmergencyDevice sdEmergencyDevice=new SdEmergencyDevice();
        sdEmergencyDevice.setTunnelId(sdWarningInfo.getTunnelId());
        sdEmergencyDevice.setDirection(("Z".equals(sdWarningInfo.getHoleDirection()) ? "1":"0"));
       // sdEmergencyDevice.setMileage(sdWarningInfo.getPosition());
        List<SdEmergencyDevice> sdEmergencyDevices = sdEmergencyDeviceService.selectSdEmergencyDeviceList(sdEmergencyDevice);
        Integer wMileage = mileageToInteger(sdWarningInfo.getPosition());
        for (SdEmergencyDevice eDevice:sdEmergencyDevices){
            Integer dMileage = mileageToInteger(eDevice.getMileage());
            int diffNum = Math.abs(dMileage - wMileage);
            eDevice.setRemark(String.valueOf(diffNum));
        }
        Collections.sort(sdEmergencyDevices,(s1, s2) -> s1.getRemark().compareTo(s2.getRemark()));
       /* List<SdEmergencyDevice> eDeviceList=new ArrayList<SdEmergencyDevice>();
        if(sdEmergencyDevices.size()<5){
            eDeviceList = sdEmergencyDevices.subList(0,sdEmergencyDevices.size());
        }else {
            eDeviceList = sdEmergencyDevices.subList(0, 4);
        }*/
        return Result.success(sdEmergencyDevices);
    }
    @GetMapping(value = "/emergencyPerList/{id}")
    @ApiOperation("获取应急人员信息列表")
    @ApiImplicitParam(name = "id", value = "预警信息ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result emergencyPerList(@PathVariable("id") Long id)
    {
        SdWarningInfo sdWarningInfo = sdWarningInfoService.selectSdWarningInfoById(id);
        SdEmergencyPer sdEmergencyPer = new SdEmergencyPer();
        sdEmergencyPer.setTunnelId(sdWarningInfo.getTunnelId());
        return Result.success(sdEmergencyPerService.selectSdEmergencyPerList(sdEmergencyPer));
    }


    public Integer mileageToInteger (String mileage){
        if(StringUtils.isNotNull(mileage) && StringUtils.isNotEmpty(mileage)){
            String[] split = mileage.split("\\+");
            String k=split[0];
            String s = filterUnNumber(k.substring(k.indexOf("K"), k.length()));
            return (Integer.parseInt(s)*1000)+Integer.parseInt(split[1]);
        }
        return 0;
    }
    public String filterUnNumber(String str) {
        // 只允数字
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        //替换与模式匹配的所有字符（即非数字的字符将被""替换）
        return m.replaceAll("").trim();
    }
    /**
     * 获取设备环境参数信息
     */
    @GetMapping(value = "/deviceinfo")
    @ApiOperation("获取设备环境参数信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tunnelId", value = "隧道ID", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "direction", value = "方向", dataType = "String", dataTypeClass = String.class)
    })
    public Result getDeviceInfo(@RequestParam("tunnelId") String tunnelId,
                                    @RequestParam("direction") String direction)
    {
        ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);
        RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
        List<SdDevices> list = sdDevicesService.selectSensorListByTunnelId(tunnelId,("Z".equals(direction) ? "1":"0"));
        List<SdAlarmModel> mesageList = new ArrayList<SdAlarmModel>();
        for(SdDevices sdDevice:list){
            redisCache.getCacheObject(String.valueOf(sdDevice.getEqId()));
            SdAlarmModel sdalarmmodel = new SdAlarmModel();
            sdalarmmodel.setEqId(String.valueOf(sdDevice.getEqId()));
            sdalarmmodel.setEqName(sdDevice.getEqName());
            sdalarmmodel.setEqTypeId(sdDevice.getEqType().toString());
            sdalarmmodel.setEqTypeName(sdDevice.getTypeName().getTypeName());
            sdalarmmodel.setSensorValue(redisCache.getCacheObject(String.valueOf(sdDevice.getEqId())));
            sdalarmmodel.setTunnelId(tunnelId);
            sdalarmmodel.setTunnelName(sdDevice.getTunnelName().getTunnelName());
            sdalarmmodel.setRemark(sdDevice.getPile());
            mesageList.add(sdalarmmodel);
        }
        return Result.success(mesageList);
    }
    /**
     * 获取交通参数信息
     */
    @GetMapping(value = "/trafficinfo")
    @ApiOperation("获取交通参数信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tunnelId", value = "隧道ID", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "direction", value = "方向", dataType = "String", dataTypeClass = String.class)
    })
    public static Result getTrafficInfo(@RequestParam("tunnelId") String tunnelId,
                                            @RequestParam("direction") String direction)
    {
        ISdDevicesService sdDevicesService = SpringUtils.getBean(ISdDevicesService.class);
        RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
        List<SdDevices> list = sdDevicesService.selectRippleListByTunnelId(tunnelId,("Z".equals(direction) ? "1":"0"));
        /*List<List<SdTrafficStatistics> > mesageList = new ArrayList<List<SdTrafficStatistics> >();
        for(SdDevices sdDevice:list){
            List<SdTrafficStatistics>   trafficStatisticsList = (List<SdTrafficStatistics>) redisCache.getCacheObject(String.valueOf(sdDevice.getEqId()));
            if (trafficStatisticsList!=null){
                if(trafficStatisticsList.size()>0 && mesageList.size()<1){
                    mesageList.add(trafficStatisticsList);
                }
            }
        }*/

        return Result.success();
    }
    /**
     * 循环启动手动控制策略
     */
    @GetMapping(value = "/handleStrategy")
    public void handleStrategy(@RequestParam("ids") Long[] ids,
                               @RequestParam("warId") Long warId)
    {
            sdStrategyService.handleStrategyByIds(ids,warId);
    }
    @GetMapping(value = "/handleStrategyRollBack")
    public void handleStrategyRollBack(@RequestParam("warId") Long warId)
    {
            sdStrategyService.handleStrategyRollBack(warId);
    }
    /**
     * 新增预警信息
     */
    @Log(title = "预警信息", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增预警信息")
    public Result add(@RequestBody SdWarningInfo sdWarningInfo)
    {
        return Result.toResult(sdWarningInfoService.insertSdWarningInfo(sdWarningInfo));
    }

    /**
     * 修改预警信息
     */
    @Log(title = "预警信息", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改预警信息")
    public Result edit(@RequestBody SdWarningInfo sdWarningInfo)
    {
        return Result.toResult(sdWarningInfoService.updateSdWarningInfo(sdWarningInfo));
    }
    /**
     * 忽略预警信息
     */
    @GetMapping(value = "/getIgnoreEvent/{id}")
    @ApiOperation("忽略预警信息")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result ignoreEvent(@PathVariable Long id)
    {
        SdWarningInfo sdWarningInfo=new SdWarningInfo();
        sdWarningInfo.setId(id);
        sdWarningInfo.setProcessState("2");
        return Result.toResult(sdWarningInfoService.updateSdWarningInfo(sdWarningInfo));
    }
    @GetMapping(value = "/oneIgnore")
    public Result oneIgnore()
    {
        SdWarningInfo sdWarningInfo=new SdWarningInfo();
        sdWarningInfo.setProcessState("2");
        return Result.toResult(sdWarningInfoService.updateSdWarningInfoByProcessState(sdWarningInfo));
    }

    /**
     * 删除预警信息
     */
    @Log(title = "预警信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除预警信息")
    @ApiImplicitParam(name = "ids", value = "需要删除的预警信息ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result remove(@PathVariable Long[] ids)
    {
        return Result.toResult(sdWarningInfoService.deleteSdWarningInfoByIds(ids));
    }
    /**'
     * 查看相关预案信息
     * @param warningTypeId 预警类型ID
     * seePlanListById
     */
	@GetMapping("/seePlanListById/{warningTypeId}")
    @ApiOperation("查看相关预案信息")
    @ApiImplicitParam(name = "warningTypeId", value = "预警类型ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public List<SdReservePlan>  seePlanListByWarningTypeId(@PathVariable Long warningTypeId)
    {
        List<SdReservePlan> list = sdWarningInfoService.seePlanListByWarningTypeId(warningTypeId);
        return list;
    }

	/**'
     * 执行相关策略
     * @param warningTypeId 预警类型ID
     * seePlanListById
     */
	@GetMapping("/seeStrategyListById/{warningTypeId}")
    @ApiOperation("执行相关策略")
    @ApiImplicitParam(name = "warningTypeId", value = "预警类型ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public List<SdStrategy>  seeStrategyListById(@PathVariable Long warningTypeId)
    {
        List<SdStrategy> list = sdWarningInfoService.seeStrategyListById(warningTypeId);
        return list;
    }
    @GetMapping(value = "/ioToBase64")
    public AjaxResult ioToBase64(@RequestParam("url") String url)
    {
        return AjaxResult.success(ImgTobase64.ioToBase64(url));
    }

    /**
     * 获取当天压力表预警信息
     */
    @GetMapping("/getAllPressureGaugesWarningMsg")
    @ApiOperation("获取当天压力表预警信息")
    public Result getAllPressureGaugesWarningMsg()
    {
        return Result.success(sdWarningInfoService.getAllPressureGaugesWarningMsg());
    }

    /**
     * 运营APP数据分析
     */
    @GetMapping("/getWarningDataAnalysis")
    @ApiOperation("运营APP数据分析")
    public Result getWarningDataAnalysis(SdWarningInfo sdWarningInfo) throws ParseException {
        return Result.success(sdWarningInfoService.getWarningDataAnalysis(sdWarningInfo));
    }

    /**
     * 查询当天预警事件总数以事件类型进行分组计数
     */
    @GetMapping("/getTheWarningDataOfToday")
    @ApiOperation("查询当天预警事件总数以事件类型进行分组计数")
    public Result getTheWarningDataOfToday(SdWarningInfo sdWarningInfo) {
        return Result.success(sdWarningInfoService.getTheWarningDataOfToday(sdWarningInfo));
    }

    /**
     * 系统预警查询
     */
    @GetMapping("/getSystemWarningMsg")
    @ApiOperation("系统预警查询")
    public Result getSystemWarningMsg() {
        return Result.success(sdWarningInfoService.getSystemWarningMsg());
    }

    /**
     * 交通事件查询
     */
    @GetMapping("/getTrafficIncident")
    @ApiOperation("交通事件查询")
    public Result getTrafficIncident() {
        return Result.success(sdWarningInfoService.getTrafficIncident());
    }

    @GetMapping("/getWarningInfoCount")
    @ApiOperation("查询最近一周的报警数量")
    public Result getWarningInfoCount() {
        return Result.success(sdWarningInfoService.getWarningInfoCount());
    }

    /**
     * 获取指定隧道内车辆经纬度
     * */
    @PostMapping("/getCarPosition")
    @ApiOperation("获取指定隧道内车辆经纬度")
    @ApiImplicitParam(name = "tunnelId", value = "隧道ID", required = true, dataType = "String", paramType = "path", dataTypeClass = String.class)
    public Result getCarPosition(String tunnelId) {
        return Result.success(sdWarningInfoService.getCarPosition(tunnelId));
    }

    /**
     * 预警检测
     * */
    @PostMapping("/getWarningMessageCountOfToday")
    @ApiOperation("预警检测")
    @ApiImplicitParam(name = "tunnelId", value = "隧道ID", required = true, dataType = "String", paramType = "path", dataTypeClass = String.class)
    public Result getWarningMessageCountOfToday(String tunnelId) {
        return Result.success(sdWarningInfoService.getWarningMessageCountOfToday(tunnelId));
    }

    /**
     * 实时预警信息
     * */
    @PostMapping("/getWarningMassageOfUnProcess")
    @ApiOperation("实时预警信息")
    @ApiImplicitParam(name = "tunnelId", value = "隧道ID", required = true, dataType = "String", paramType = "path", dataTypeClass = String.class)
    public Result getWarningMassageOfUnProcess(String tunnelId) {
        return Result.success(sdWarningInfoService.getWarningMassageOfUnProcess(tunnelId));
    }
}
