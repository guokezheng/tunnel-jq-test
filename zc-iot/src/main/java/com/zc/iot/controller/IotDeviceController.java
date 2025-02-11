package com.zc.iot.controller;

import java.util.List;

import com.zc.iot.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.zc.iot.service.IIotDeviceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设备Controller
 * 
 * @author Athena-xiepufeng
 * @date 2021-11-04
 */
@RestController
@RequestMapping("/iot/device")
public class IotDeviceController extends BaseController
{
    @Autowired
    private IIotDeviceService iotDeviceService;

    /**
     * 查询设备列表
     */
    @PreAuthorize("@ss.hasPermi('iot:device:list')")
    @GetMapping("/list")
    public TableDataInfo list(IotDevice iotDevice)
    {
        startPage();
        List<IotDevice> list = iotDeviceService.selectIotDeviceList(iotDevice);
        return getDataTable(list);
    }

    /**
     * 导出设备列表
     */
    @PreAuthorize("@ss.hasPermi('iot:device:export')")
    @Log(title = "设备", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(IotDevice iotDevice)
    {
        List<IotDevice> list = iotDeviceService.selectIotDeviceList(iotDevice);
        ExcelUtil<IotDevice> util = new ExcelUtil<IotDevice>(IotDevice.class);
        return util.exportExcel(list, "设备数据");
    }

    /**
     * 获取设备详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:device:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(iotDeviceService.selectIotDeviceById(id));
    }

    /**
     * 新增设备
     */
    @PreAuthorize("@ss.hasPermi('iot:device:add')")
    @Log(title = "设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody IotDevice iotDevice)
    {
        return toAjax(iotDeviceService.insertIotDevice(iotDevice));
    }

    /**
     * 修改设备
     */
    @PreAuthorize("@ss.hasPermi('iot:device:edit')")
    @Log(title = "设备", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody IotDevice iotDevice)
    {
        return toAjax(iotDeviceService.updateIotDevice(iotDevice));
    }

    /**
     * 删除设备
     */
    @PreAuthorize("@ss.hasPermi('iot:device:remove')")
    @Log(title = "设备", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(iotDeviceService.deleteIotDeviceByIds(ids));
    }

    /**
     * 查询设备功能
     */
    @PreAuthorize("@ss.hasPermi('iot:device:queryFunction')")
    @GetMapping("/function/{id}")
    public AjaxResult functionList(@PathVariable("id") Long id)
    {
        List<IotFunction> iotFunctions = iotDeviceService.selectIotDeviceFunction(id);

        if (iotFunctions == null) return AjaxResult.error();

        return AjaxResult.success(iotFunctions);
    }
    /**
     *  功能调试，属性设置
     * @param iotDebugAttribute 属性调试参数
     * @return 请求结果
     */
    @PreAuthorize("@ss.hasPermi('iot:device:debug')")
    @PutMapping("/debug/attribute")
    public AjaxResult setAttribute(@RequestBody IotDebugAttribute iotDebugAttribute)
    {
        if (iotDeviceService.setAttribute(iotDebugAttribute))
        {
            return AjaxResult.success();
        }

        return AjaxResult.error();
    }

    /**
     *  功能调试，属性获取
     * @param iotDebug
     * @return 请求结果
     */
    @PreAuthorize("@ss.hasPermi('iot:device:debug')")
    @GetMapping("/debug/attribute")
    public AjaxResult getAttribute(IotDebug iotDebug)
    {
        if (iotDeviceService.getAttribute(iotDebug))
        {

            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    /**
     *  功能调试，服务调用
     * @param iotDebugServe 服务调式参数
     * @return 请求结果
     */
    @PreAuthorize("@ss.hasPermi('iot:device:debug')")
    @PutMapping("/debug/service")
    public AjaxResult invokeService(@RequestBody IotDebugServe iotDebugServe)
    {
        if (iotDeviceService.invokeService(iotDebugServe))
        {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

}
