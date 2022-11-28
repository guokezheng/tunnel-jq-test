package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<Map<String,String>> deviceList = sdDeviceDataService.getDeviceDataByTunnelId(tunnelId);
        Map<String,Map<String,String>> map = new HashMap<>();

        for(Map<String,String> deviceMap : deviceList){
            String eqId = deviceMap.get("eqId");
            if(map.get(eqId) == null){
                Map<String,String> itemMap = new HashMap<>();
                itemMap.put("eqId",eqId);
                itemMap.put("eqName",deviceMap.get("eqName"));
                itemMap.put("eqDirection",deviceMap.get("eqDirection"));
                itemMap.put("eqStatus",deviceMap.get("eqStatus"));
                itemMap.put("eqType",deviceMap.get("eqType"));
                itemMap.put("eqTunnelId",deviceMap.get("eqTunnelId"));
                String itemCode = deviceMap.get("itemCode");
                String data = deviceMap.get("dataUnit");
                if(itemCode != null){
                    itemMap.put(itemCode,data);
                }
                map.put(eqId,itemMap);
            }else{
                Map<String,String> itemMap = map.get(eqId);
                String itemCode = deviceMap.get("itemCode");
                String data = deviceMap.get("dataUnit");
                if(itemCode != null){
                    itemMap.put(itemCode,data);
                }
            }
        }
        return Result.success(map);
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

    @GetMapping("/getTodayLDData/{deviceId}")
    @ApiImplicitParam(name = "deviceId", value = "设备ID", required = true, dataType = "String", paramType = "path",dataTypeClass = String.class)
    public Result<Map> getTodayLDData(@PathVariable("deviceId") String deviceId)
    {
        Map<String, Object> todayLDData = sdDeviceDataService.getTodayLDData(deviceId);
        return Result.success(todayLDData);
    }

    @PostMapping("/dataLogInfoList")
    public TableDataInfo dataLogInfoList(@RequestBody Map<String, Object> maps)
    {
        startPage();
        List<Map<String, String>> list = sdDeviceDataService.dataLogInfoList(maps);
        return getDataTable(list);
    }
}
