package com.tunnel.platform.controller.dataInfo;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.dataReport.DeviceType;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.*;
import com.tunnel.business.domain.digitalmodel.SdRadarDevice;
import com.tunnel.business.domain.energyManagement.ElectricityData;
import com.tunnel.business.domain.energyManagement.EnergySjfx;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.dataInfo.SdMicrowavePeriodicStatisticsMapper;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.mapper.energyManagement.SdEnergyDataMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.digitalmodel.impl.RadarEventServiceImpl;
import com.tunnel.business.service.energyManagement.EnergySjfxElectricityService;
import io.swagger.annotations.ApiImplicitParam;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备实时数据（存储模拟量）Controller
 *
 * @author ruoyi
 * @date 2022-09-13
 */
@RestController
@RequestMapping("/system/data")
public class SdDeviceDataController extends BaseController
{
    @Autowired
    private ISdDeviceDataService sdDeviceDataService;

    @Autowired
    private SdEnergyDataMapper energyDataMapper;

    @Autowired
    private RadarEventServiceImpl radarEventServiceImpl;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Autowired
    private SdTunnelsMapper sdTunnelsMapper;

    @Autowired
    @Qualifier("kafkaOneTemplate")
    private KafkaTemplate<String, String> kafkaOneTemplate;


    /**
     * 线程池
     */
    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 查询设备实时数据（存储模拟量）列表
     */
    @PreAuthorize("@ss.hasPermi('system:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdDeviceData sdDeviceData)
    {
        startPage();
        List<SdDeviceData> list = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
        return getDataTable(list);
    }

    /**
     * 导出设备实时数据（存储模拟量）列表
     */
    @PreAuthorize("@ss.hasPermi('system:data:export')")
    @Log(title = "设备实时数据（存储模拟量）", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdDeviceData sdDeviceData)
    {
        List<SdDeviceData> list = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
        ExcelUtil<SdDeviceData> util = new ExcelUtil<SdDeviceData>(SdDeviceData.class);
        return util.exportExcel(list, "设备实时数据（存储模拟量）数据");
    }

    /**
     * 获取设备实时数据（存储模拟量）详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:data:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdDeviceDataService.selectSdDeviceDataById(id));
    }

    /**
     * 新增设备实时数据（存储模拟量）
     */
    @PreAuthorize("@ss.hasPermi('system:data:add')")
    @Log(title = "设备实时数据（存储模拟量）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdDeviceData sdDeviceData)
    {
        return toAjax(sdDeviceDataService.insertSdDeviceData(sdDeviceData));
    }

    /**
     * 修改设备实时数据（存储模拟量）
     */
    @PreAuthorize("@ss.hasPermi('system:data:edit')")
    @Log(title = "设备实时数据（存储模拟量）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdDeviceData sdDeviceData)
    {
        return toAjax(sdDeviceDataService.updateSdDeviceData(sdDeviceData));
    }

    /**
     * 删除设备实时数据（存储模拟量）
     */
    @PreAuthorize("@ss.hasPermi('system:data:remove')")
    @Log(title = "设备实时数据（存储模拟量）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdDeviceDataService.deleteSdDeviceDataByIds(ids));
    }


    /**
     * 根据隧道id查询当前设备的监测状态、实时数据或状态
     * @param tunnelId 隧道id
     * @return
     */
    @GetMapping("/getDeviceDataByTunnelId")
    public Result getDeviceDataByTunnelId(String tunnelId) {
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqTunnelId(tunnelId);
        List<Map<String,Object>> deviceList = sdDeviceDataService.getDeviceDataByTunnelId(tunnelId);
        //风机数量
        int fengJiNum = deviceList.stream().filter(item -> DevicesTypeEnum.FENG_JI.getCode().toString().equals(item.get("eqType").toString())).collect(Collectors.toList()).size();
        int zhaoMingNum = deviceList.stream().filter(item -> DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().toString().equals(item.get("eqType").toString()) || DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().toString().equals(item.get("eqType").toString())).collect(Collectors.toList()).size();
        EnergySjfx energySjfx = new EnergySjfx();
        energySjfx.setTunnelId(tunnelId);
        energySjfx.setCreateTime(DateUtils.getNowDate());
        //风机
        energySjfx.setItemizedCode("251d1eaadc9342629754ff85e519edb7");
        EnergySjfx feng = energyDataMapper.getFengOrZhao(energySjfx);
        //照明
        energySjfx.setItemizedCode("762f745413df400d84945b9d47c1bb37");
        EnergySjfx zhao = energyDataMapper.getFengOrZhao(energySjfx);
        //风机均电量
        BigDecimal fengCount = feng != null ? BigDecimal.valueOf(feng.getEnergyValue()).divide(new BigDecimal(fengJiNum),2) : new BigDecimal(0.0);
        //照明均电量
        BigDecimal zhaoCount = zhao != null ? BigDecimal.valueOf(zhao.getEnergyValue()).divide(new BigDecimal(zhaoMingNum),2) : new BigDecimal(0.0);
        Map<String,Map<String,String>> map = new HashMap<>();
        for(Map<String,Object> deviceMap : deviceList){
            String eqId = deviceMap.get("eqId").toString();
            if(map.get(eqId) == null){
                Map<String,String> itemMap = new HashMap<>();
                itemMap.put("eqId",eqId);
                itemMap.put("eqName",nullOrEn(deviceMap.get("eqName")));
                itemMap.put("pile",nullOrEn(deviceMap.get("pile")));
                itemMap.put("eqDirection",nullOrEn(deviceMap.get("eqDirection")));
                itemMap.put("eqStatus",nullOrEn(deviceMap.get("eqStatus")));
                itemMap.put("eqType",nullOrEn(deviceMap.get("eqType")));
                itemMap.put("eqTunnelId",nullOrEn(deviceMap.get("eqTunnelId")));
                itemMap.put("brightness",nullOrEn(deviceMap.get("brightness")));
                String eqType = itemMap.get("eqType");
                if(DevicesTypeEnum.FENG_JI.getCode().toString().equals(eqType)){
                    itemMap.put("electricity",fengCount + "kW·h");
                }
                if(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().toString().equals(eqType) || DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().toString().equals(eqType)){
                    itemMap.put("electricity",zhaoCount + "kW·h");
                }
                itemMap.put("unit",nullOrEn(deviceMap.get("unit")));
                String itemCode = nullOrEn(deviceMap.get("itemCode"));
                String data = nullOrEn(deviceMap.get("dataUnit"));
                if(itemCode != null){
                    itemMap.put(itemCode,data);
                }
                map.put(eqId,itemMap);
            }else{
                Map<String,String> itemMap = map.get(eqId);
                itemMap.put("unit",nullOrEn(deviceMap.get("unit")));
                String itemCode = nullOrEn(deviceMap.get("itemCode"));
                String data = nullOrEn(deviceMap.get("dataUnit"));

                if(itemCode != null && data != null){
                    itemMap.put(itemCode,data);
                }
            }
        }

        //向万集推送机电设备实时数据，使用线程池实现功能，避免影响工作台数据加载
        /*threadPoolTaskExecutor.execute(()->{
            setDeviceDataList(tunnelId);
        });*/

        return Result.success(map);
    }

    /**
     * 判断是否为空
     * @param item
     * @return
     */
    public String nullOrEn(Object item){
        return (item == null || "".equals(item)) ? "" : item.toString();
    }

    @GetMapping("/getTodayCOVIData/{deviceId}")
    @ApiImplicitParam(name = "deviceId", value = "设备ID", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class)
    public Result<Map> getTodayCOVIData(@PathVariable("deviceId") String deviceId)
    {
        Map<String, Object> todayCOVIData = sdDeviceDataService.getTodayCOVIData(deviceId);
        return Result.success(todayCOVIData);
    }

    @GetMapping("/getTodayFSFXData/{deviceId}")
    @ApiImplicitParam(name = "deviceId", value = "设备ID", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class)
    public Result<Map> getTodayFSFXData(@PathVariable("deviceId") String deviceId)
    {
        Map<String, Object> todayFSFXData = sdDeviceDataService.getTodayFSFXData(deviceId);
        return Result.success(todayFSFXData);
    }

    /**
     * 获取远传压力表数据
     * @param deviceId
     * @return
     */
    @GetMapping("/getTodayYcylData/{deviceId}")
    @ApiImplicitParam(name = "deviceId", value = "设备ID", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class)
    public Result<Map> getTodayYcylData(@PathVariable("deviceId") String deviceId){
        Map<String, Object> todayFSFXData = sdDeviceDataService.getTodayYcylData(deviceId);
        return Result.success(todayFSFXData);
    }

    @GetMapping("/getTodayLDData/{deviceId}")
    @ApiImplicitParam(name = "deviceId", value = "设备ID", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class)
    public Result<Map> getTodayLDData(@PathVariable("deviceId") String deviceId)
    {
        Map<String, Object> todayLDData = sdDeviceDataService.getTodayLDData(deviceId);
        return Result.success(todayLDData);
    }

    /**
     * 查询设备详情---列表数据
     * @param sdDeviceData
     * @return
     */
    @GetMapping("/dataLogInfoList")
    public TableDataInfo dataLogInfoList(SdDeviceData sdDeviceData)
    {
        startPage();
        List<Map<String, String>> list = sdDeviceDataService.dataLogInfoList(sdDeviceData);
        return getDataTable(list);
    }
    @GetMapping("/dataLogInfoLineList")
    public TableDataInfo dataLogInfoLineList(SdDeviceData sdDeviceData)
    {
        List<Map<String, String>> list = sdDeviceDataService.dataLogInfoLineList(sdDeviceData);
        return getDataTable(list);
    }

    /**
     * 工作台能耗图表
     * @param tunnelId
     * @return
     */
    @GetMapping(value = "/energyConsumptionDetection/{tunnelId}")
    public AjaxResult energyConsumptionDetection(@PathVariable("tunnelId") String tunnelId)
    {
        EnergySjfxElectricityService bean = SpringUtils.getBean(EnergySjfxElectricityService.class);
        List<String> list = new ArrayList<>();
        list.add(tunnelId);

        Date baseTime = new java.sql.Timestamp(DateUtils.getNowDate().getTime());

        try {
            //日报
            List<List<ElectricityData>> dayList = bean.getElectricityReportList(list, baseTime, 0, "1", null);
            //月报
            List<List<ElectricityData>> monthList = bean.getElectricityReportList(list, baseTime, 1, "1", null);
            //年报
            List<List<ElectricityData>> yearList = bean.getElectricityReportList(list, baseTime, 2, "1", null);
            Map<String, Object> map = new HashMap<>();
            map.put("day",dayList.get(0));
            map.put("month",monthList.get(0));
            map.put("year",yearList.get(0));
            return AjaxResult.success(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //return AjaxResult.success(sdDeviceDataService.energyConsumptionDetection(tunnelId));
    }

    /**
     * 获取风机安全检测仪实时数据
     * @param deviceId
     * @return
     */
    @GetMapping(value = "/getFanSafeData/{deviceId}")
    public AjaxResult getFanSafeData(@PathVariable("deviceId") String deviceId){
        return sdDeviceDataService.getFanSafeData(deviceId);
    }

    /**
     * 获取水浸传感器实时数据
     * @param deviceId
     * @return
     */
    @GetMapping(value = "/getLevelData/{deviceId}")
    public AjaxResult getLevelData(@PathVariable("deviceId") String deviceId){
        return sdDeviceDataService.getLevelData(deviceId);
    }


    /**
     * 查询设备列表
     * @param sdDeviceData
     * @return
     */
    @GetMapping("/dataDevicesLogInfoList")
    public TableDataInfo dataDevicesLogInfoList(SdDeviceData sdDeviceData)
    {
        startPage();
        List<Map<String, String>> list = sdDeviceDataService.dataDevicesLogInfoList(sdDeviceData);
        return getDataTable(list);
    }



    @Log(title = "数据报表", businessType = BusinessType.EXPORT)
    @GetMapping("/exportDatainforTab")
    public AjaxResult exportDatainforTab(SdDeviceData sdDeviceData)
    {
        List<SdDeviceData> list = sdDeviceDataService.exportDatainforTab(sdDeviceData);
        ExcelUtil<SdDeviceData> util = new ExcelUtil<SdDeviceData>(SdDeviceData.class);
        return util.exportExcel(list, "数据报表");
    }


    @Log(title = "数据报表", businessType = BusinessType.EXPORT)
    @GetMapping("/handleExportRecord")
    public AjaxResult handleExportRecord(SdDeviceCOVIData sdDeviceCOVIData)
    {
        if(DeviceType.COVIITEM.getCode().equals(sdDeviceCOVIData.getSearchValue())){//covi
            List<SdDeviceCOVIData> list = sdDeviceDataService.handleExportRecord(sdDeviceCOVIData);
            ExcelUtil<SdDeviceCOVIData> util = new ExcelUtil<SdDeviceCOVIData>(SdDeviceCOVIData.class);
            return util.exportExcel(list, DeviceType.COVIITEM.getName());
        }else if(DeviceType.FENGSHUFENGXIANGITEM.getCode().equals(sdDeviceCOVIData.getSearchValue())){//风速风向
            List<SdDeviceFSFXData> list = sdDeviceDataService.handleFSFXExportRecord(sdDeviceCOVIData);
            ExcelUtil<SdDeviceFSFXData> util = new ExcelUtil<SdDeviceFSFXData>(SdDeviceFSFXData.class);
            return util.exportExcel(list, DeviceType.FENGSHUFENGXIANGITEM.getName());
        }else if(DeviceType.DONGNEILIANGDUITEM.getCode().equals(sdDeviceCOVIData.getSearchValue())){//洞内亮度
            List<SdDeviceDNData> list = sdDeviceDataService.handleDNExportRecord(sdDeviceCOVIData);
            ExcelUtil<SdDeviceDNData> util = new ExcelUtil<SdDeviceDNData>(SdDeviceDNData.class);
            return util.exportExcel(list, DeviceType.DONGNEILIANGDUITEM.getName());
        }else if(DeviceType.DONGWAILIANGDUITEM.getCode().equals(sdDeviceCOVIData.getSearchValue())){//洞外亮度
            List<SdDeviceDWData> list = sdDeviceDataService.handleDWExportRecord(sdDeviceCOVIData);
            ExcelUtil<SdDeviceDWData> util = new ExcelUtil<SdDeviceDWData>(SdDeviceDWData.class);
            return util.exportExcel(list, DeviceType.DONGWAILIANGDUITEM.getName());
        }else if(DeviceType.DONGCATGDUITEM.getCode().equals(sdDeviceCOVIData.getSearchValue())){//微波车检
            List<Map<String, String>> maps = sdDeviceDataService.handleWBExportRecord(sdDeviceCOVIData);
            List<SdDeviceWBData> sdDeviceSJDataArray = new ArrayList<>();
            for (Map<String, String> map :maps){
                try {
                    SdDeviceWBData sdDeviceSJData = new SdDeviceWBData();
                    BeanUtils.populate(sdDeviceSJData, map);
                    sdDeviceSJDataArray.add(sdDeviceSJData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ExcelUtil<SdDeviceWBData> util = new ExcelUtil<SdDeviceWBData>(SdDeviceWBData.class);
            return util.exportExcel(sdDeviceSJDataArray, DeviceType.DONGCATGDUITEM.getName());
        }else if(DeviceType.DONGHUMIDGDUITEM.getCode().equals(sdDeviceCOVIData.getSearchValue())){//温湿度
            List<Map<String, String>> maps = sdDeviceDataService.handleWSDExportRecord(sdDeviceCOVIData);
            List<SdDeviceWSDData> sdDeviceSJDataArray = new ArrayList<>();
            for (Map<String, String> map :maps){
                try {
                    SdDeviceWSDData sdDeviceSJData = new SdDeviceWSDData();
                    BeanUtils.populate(sdDeviceSJData, map);
                    sdDeviceSJDataArray.add(sdDeviceSJData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ExcelUtil<SdDeviceWSDData> util = new ExcelUtil<SdDeviceWSDData>(SdDeviceWSDData.class);
            return util.exportExcel(sdDeviceSJDataArray, DeviceType.DONGHUMIDGDUITEM.getName());
        }else if(DeviceType.DONGWATERGDUITEM.getCode().equals(sdDeviceCOVIData.getSearchValue())){//水浸
            List<Map<String, String>> maps = sdDeviceDataService.handleSJExportRecord(sdDeviceCOVIData);
            List<SdDeviceSJData> sdDeviceSJDataArray = new ArrayList<>();
            for (Map<String, String> map :maps){
                try {
                    SdDeviceSJData sdDeviceSJData = new SdDeviceSJData();
                    BeanUtils.populate(sdDeviceSJData, map);
                    sdDeviceSJDataArray.add(sdDeviceSJData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ExcelUtil<SdDeviceSJData> util = new ExcelUtil<SdDeviceSJData>(SdDeviceSJData.class);
            return util.exportExcel(sdDeviceSJDataArray, DeviceType.DONGWATERGDUITEM.getName());
        }else if(DeviceType.DONGFANGDUITEM.getCode().equals(sdDeviceCOVIData.getSearchValue())){//风机
            List<Map<String, String>> maps = sdDeviceDataService.handleFJExportRecord(sdDeviceCOVIData);
            List<SdDeviceFJData> sdDeviceSJDataArray = new ArrayList<>();
            for (Map<String, String> map :maps){
                try {
                    SdDeviceFJData sdDeviceSJData = new SdDeviceFJData();
                    BeanUtils.populate(sdDeviceSJData, map);
                    sdDeviceSJDataArray.add(sdDeviceSJData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ExcelUtil<SdDeviceFJData> util = new ExcelUtil<SdDeviceFJData>(SdDeviceFJData.class);
            return util.exportExcel(sdDeviceSJDataArray, DeviceType.DONGFANGDUITEM.getName());
        }else{
            return null;
        }

    }
    /**
     * 小车跑数据控制
     * @param eqId
     * @param switchType
     * @return
     */
    @GetMapping(value = "/getFanSafeData/{eqId}/{switchType}")
    public void carSwitchType(@PathVariable("eqId") String eqId,@PathVariable("switchType") String switchType){
        sdDeviceDataService.getFanSafeData(eqId);
    }

    /**
     * 推送设备实时信息
     * @param tunnelId
     */
    public void setDeviceDataList(String tunnelId){
        List<SdDevices> devices = sdDevicesMapper.selectDevice(tunnelId);
        List<SdRadarDevice> deviceRadar = radarEventServiceImpl.getDeviceRadar(devices);
        deviceRadar.stream().forEach(item -> item.setTunnelId(tunnelId));
        kafkaOneTemplate.send("baseDeviceStatus",JSON.toJSONString(deviceRadar));
    }
}
