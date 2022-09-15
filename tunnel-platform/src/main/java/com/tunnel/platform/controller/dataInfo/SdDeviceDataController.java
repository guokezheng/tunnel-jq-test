package com.tunnel.platform.controller.dataInfo;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.platform.domain.dataInfo.SdDeviceData;
import com.tunnel.platform.service.dataInfo.ISdDeviceDataService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

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

    @GetMapping("/getTodayCOVIData")
    public AjaxResult getTodayCOVIData(String deviceId)
    {
        Map<String, Object> todayCOVIData = sdDeviceDataService.getTodayCOVIData(deviceId);
        return AjaxResult.success(todayCOVIData);
    }
}
