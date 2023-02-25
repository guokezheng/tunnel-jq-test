package com.tunnel.platform.controller.vehicle;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.vehicle.SdVehicleData;
import com.tunnel.business.service.vehicle.ISdVehicleDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 隧道车辆数据（单车数据）Controller
 *
 * @author ruoyi
 * @date 2023-02-25
 */
@RestController
@RequestMapping("/vehicle/data")
public class SdVehicleDataController extends BaseController
{
    @Autowired
    private ISdVehicleDataService sdVehicleDataService;

    /**
     * 查询隧道车辆数据（单车数据）列表
     */
    @PreAuthorize("@ss.hasPermi('vehicle:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdVehicleData sdVehicleData)
    {
        startPage();
        List<SdVehicleData> list = sdVehicleDataService.selectSdVehicleDataList(sdVehicleData);
        return getDataTable(list);
    }

    /**
     * 导出隧道车辆数据（单车数据）列表
     */
    @PreAuthorize("@ss.hasPermi('vehicle:data:export')")
    @Log(title = "隧道车辆数据（单车数据）", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdVehicleData sdVehicleData)
    {
        List<SdVehicleData> list = sdVehicleDataService.selectSdVehicleDataList(sdVehicleData);
        ExcelUtil<SdVehicleData> util = new ExcelUtil<SdVehicleData>(SdVehicleData.class);
        return util.exportExcel(list, "隧道车辆数据（单车数据）数据");
    }

    /**
     * 获取隧道车辆数据（单车数据）详细信息
     */
    @PreAuthorize("@ss.hasPermi('vehicle:data:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdVehicleDataService.selectSdVehicleDataById(id));
    }

    /**
     * 新增隧道车辆数据（单车数据）
     */
    @PreAuthorize("@ss.hasPermi('vehicle:data:add')")
    @Log(title = "隧道车辆数据（单车数据）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdVehicleData sdVehicleData)
    {
        return toAjax(sdVehicleDataService.insertSdVehicleData(sdVehicleData));
    }

    /**
     * 修改隧道车辆数据（单车数据）
     */
    @PreAuthorize("@ss.hasPermi('vehicle:data:edit')")
    @Log(title = "隧道车辆数据（单车数据）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdVehicleData sdVehicleData)
    {
        return toAjax(sdVehicleDataService.updateSdVehicleData(sdVehicleData));
    }

    /**
     * 删除隧道车辆数据（单车数据）
     */
    @PreAuthorize("@ss.hasPermi('vehicle:data:remove')")
    @Log(title = "隧道车辆数据（单车数据）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdVehicleDataService.deleteSdVehicleDataByIds(ids));
    }
}
