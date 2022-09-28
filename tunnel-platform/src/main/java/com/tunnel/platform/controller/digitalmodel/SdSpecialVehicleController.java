package com.tunnel.platform.controller.digitalmodel;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.digitalmodel.SdSpecialVehicles;
import com.tunnel.business.service.digitalmodel.ISdSpecialVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 重点车辆Controller
 * 
 * @author ruoyi
 * @date 2022-09-04
 */
@RestController
@RequestMapping("/special/vehicle")
public class SdSpecialVehicleController extends BaseController
{
    @Autowired
    private ISdSpecialVehicleService sdSpecialVehicleService;

    /**
     * 查询重点车辆列表
     */
    @PreAuthorize("@ss.hasPermi('special:vehicle:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdSpecialVehicles sdSpecialVehicle)
    {
        startPage();
        List<Map<String,String>> list = sdSpecialVehicleService.selectSdSpecialVehicleList(sdSpecialVehicle);
        return getDataTable(list);
    }

    /**
     * 导出重点车辆列表
     */
    @PreAuthorize("@ss.hasPermi('special:vehicle:export')")
    @Log(title = "重点车辆", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdSpecialVehicles sdSpecialVehicle)
    {
        List<Map<String,String>> list = sdSpecialVehicleService.selectSdSpecialVehicleList(sdSpecialVehicle);
        ExcelUtil<SdSpecialVehicles> util = new ExcelUtil<SdSpecialVehicles>(SdSpecialVehicles.class);
        return util.exportExcel(null, "重点车辆数据");
    }

    /**
     * 获取重点车辆详细信息
     */
    @PreAuthorize("@ss.hasPermi('special:vehicle:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(sdSpecialVehicleService.selectSdSpecialVehicleById(id));
    }

    /**
     * 新增重点车辆
     */
    @PreAuthorize("@ss.hasPermi('special:vehicle:add')")
    @Log(title = "重点车辆", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdSpecialVehicles sdSpecialVehicle)
    {
        return toAjax(sdSpecialVehicleService.insertSdSpecialVehicle(sdSpecialVehicle));
    }

    /**
     * 修改重点车辆
     */
    @PreAuthorize("@ss.hasPermi('special:vehicle:edit')")
    @Log(title = "重点车辆", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdSpecialVehicles sdSpecialVehicle)
    {
        return toAjax(sdSpecialVehicleService.updateSdSpecialVehicle(sdSpecialVehicle));
    }

    /**
     * 删除重点车辆
     */
    @PreAuthorize("@ss.hasPermi('special:vehicle:remove')")
    @Log(title = "重点车辆", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(sdSpecialVehicleService.deleteSdSpecialVehicleByIds(ids));
    }
    /**
     * 根据隧道id 查询24小时 重点车辆
     * @param tunnelId
     * @return
     */
    @GetMapping("/specialById")
    public AjaxResult specialById(@RequestParam("tunnelId") String tunnelId)
    {
        return AjaxResult.success(sdSpecialVehicleService.specialById(tunnelId));
    }
}
