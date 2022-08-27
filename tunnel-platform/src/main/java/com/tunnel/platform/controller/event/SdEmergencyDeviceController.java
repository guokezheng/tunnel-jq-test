package com.tunnel.platform.controller.event;

import com.tunnel.platform.domain.event.SdEmergencyDevice;
import com.tunnel.platform.service.event.ISdEmergencyDeviceService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应急物资信息Controller
 * 
 * @author ruoyi
 * @date 2021-05-12
 */
@RestController
@RequestMapping("/sdEmergencyDevice")
public class SdEmergencyDeviceController extends BaseController
{
    @Autowired
    private ISdEmergencyDeviceService SdEmergencyDeviceService;

    /**
     * 查询应急物资信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:SdEmergencyDevice:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdEmergencyDevice SdEmergencyDevice)
    {
        startPage();
        List<SdEmergencyDevice> list = SdEmergencyDeviceService.selectSdEmergencyDeviceList(SdEmergencyDevice);
        return getDataTable(list);
    }

    /**
     * 导出应急物资信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:SdEmergencyDevice:export')")
    @Log(title = "应急物资信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdEmergencyDevice SdEmergencyDevice)
    {
        List<SdEmergencyDevice> list = SdEmergencyDeviceService.selectSdEmergencyDeviceList(SdEmergencyDevice);
        ExcelUtil<SdEmergencyDevice> util = new ExcelUtil<SdEmergencyDevice>(SdEmergencyDevice.class);
        return util.exportExcel(list, "应急物资信息列表");
    }

    /**
     * 获取应急物资信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:SdEmergencyDevice:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(SdEmergencyDeviceService.selectSdEmergencyDeviceById(id));
    }

    /**
     * 新增应急物资信息
     */
    @PreAuthorize("@ss.hasPermi('business:SdEmergencyDevice:add')")
    @Log(title = "应急物资信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdEmergencyDevice SdEmergencyDevice)
    {
        return toAjax(SdEmergencyDeviceService.insertSdEmergencyDevice(SdEmergencyDevice));
    }

    /**
     * 修改应急物资信息
     */
    @PreAuthorize("@ss.hasPermi('business:SdEmergencyDevice:edit')")
    @Log(title = "应急物资信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdEmergencyDevice SdEmergencyDevice)
    {
        return toAjax(SdEmergencyDeviceService.updateSdEmergencyDevice(SdEmergencyDevice));
    }

    /**
     * 删除应急物资信息
     */
    @PreAuthorize("@ss.hasPermi('business:SdEmergencyDevice:remove')")
    @Log(title = "应急物资信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(SdEmergencyDeviceService.deleteSdEmergencyDeviceByIds(ids));
    }
}
