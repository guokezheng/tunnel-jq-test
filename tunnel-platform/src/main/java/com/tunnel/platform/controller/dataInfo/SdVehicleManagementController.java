package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdVehicleManagement;
import com.tunnel.business.service.dataInfo.ISdVehicleManagementService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车辆管理Controller
 * 
 * @author ruoyi
 * @date 2022-02-18
 */
@RestController
@RequestMapping("/vehicle/management")
public class SdVehicleManagementController extends BaseController
{
    @Autowired
    private ISdVehicleManagementService sdVehicleManagementService;

    /**
     * 查询车辆管理列表
     */
    @ApiOperation("查询车辆管理列表")
    @PreAuthorize("@ss.hasPermi('vehicle:management:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdVehicleManagement sdVehicleManagement)
    {
        startPage();
        List<SdVehicleManagement> list = sdVehicleManagementService.selectSdVehicleManagementList(sdVehicleManagement);
        return getDataTable(list);
    }

    /**
     * 导出车辆管理列表
     */
    @ApiOperation("导出车辆管理列表")
    @PreAuthorize("@ss.hasPermi('vehicle:management:export')")
    @Log(title = "车辆管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdVehicleManagement sdVehicleManagement)
    {
        List<SdVehicleManagement> list = sdVehicleManagementService.selectSdVehicleManagementList(sdVehicleManagement);
        ExcelUtil<SdVehicleManagement> util = new ExcelUtil<SdVehicleManagement>(SdVehicleManagement.class);
        return util.exportExcel(list, "车辆管理数据");
    }

    /**
     * 获取车辆管理详细信息
     */
    @ApiOperation("获取车辆管理详细信息")
    @PreAuthorize("@ss.hasPermi('vehicle:management:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdVehicleManagementService.selectSdVehicleManagementById(id));
    }

    /**
     * 新增车辆管理
     */
    @ApiOperation("新增车辆管理")
    @PreAuthorize("@ss.hasPermi('vehicle:management:add')")
    @Log(title = "车辆管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdVehicleManagement sdVehicleManagement)
    {
        return toAjax(sdVehicleManagementService.insertSdVehicleManagement(sdVehicleManagement));
    }

    /**
     * 修改车辆管理
     */
    @ApiOperation("修改车辆管理")
    @PreAuthorize("@ss.hasPermi('vehicle:management:edit')")
    @Log(title = "车辆管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdVehicleManagement sdVehicleManagement)
    {
        return toAjax(sdVehicleManagementService.updateSdVehicleManagement(sdVehicleManagement));
    }

    /**
     * 删除车辆管理
     */
    @ApiOperation("删除车辆管理")
    @PreAuthorize("@ss.hasPermi('vehicle:management:remove')")
    @Log(title = "车辆管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdVehicleManagementService.deleteSdVehicleManagementByIds(ids));
    }
}
