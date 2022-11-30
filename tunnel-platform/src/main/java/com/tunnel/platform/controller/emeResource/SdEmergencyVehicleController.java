package com.tunnel.platform.controller.emeResource;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.emeResource.SdEmergencyOrg;
import com.tunnel.business.domain.emeResource.SdEmergencyVehicle;
import com.tunnel.business.service.emeResource.ISdEmergencyVehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应急车辆Controller
 * 
 * @author dzy
 * @date 2022-08-09
 */
@RestController
@RequestMapping("/system/vehicle")
@Api(tags = "应急车辆")
public class SdEmergencyVehicleController extends BaseController
{
    @Autowired
    private ISdEmergencyVehicleService sdEmergencyVehicleService;

    /**
     * 查询应急车辆列表
     */
    @PreAuthorize("@ss.hasPermi('system:vehicle:list')")
    @GetMapping("/list")
    @ApiOperation("查询应急车辆列表")
    public TableDataInfo list(SdEmergencyVehicle sdEmergencyVehicle)
    {
        startPage();
        List<SdEmergencyVehicle> list = sdEmergencyVehicleService.selectSdEmergencyVehicleList(sdEmergencyVehicle);
        return getDataTable(list);
    }

    /**
     * 导出应急车辆列表
     */
    @PreAuthorize("@ss.hasPermi('system:vehicle:export')")
    @Log(title = "应急车辆", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation("导出应急车辆列表")
    public AjaxResult export(SdEmergencyVehicle sdEmergencyVehicle)
    {
        List<SdEmergencyVehicle> list = sdEmergencyVehicleService.selectSdEmergencyVehicleList(sdEmergencyVehicle);
        ExcelUtil<SdEmergencyVehicle> util = new ExcelUtil<SdEmergencyVehicle>(SdEmergencyVehicle.class);
        return util.exportExcel(list, "应急车辆数据");
    }

    /**
     * 获取应急车辆详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:vehicle:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取应急车辆详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdEmergencyVehicleService.selectSdEmergencyVehicleById(id));
    }

    /**
     * 新增应急车辆
     */
    @PreAuthorize("@ss.hasPermi('system:vehicle:add')")
    @Log(title = "应急车辆", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增应急车辆")
    public AjaxResult add(SdEmergencyVehicle sdEmergencyVehicle)
    {
        return toAjax(sdEmergencyVehicleService.insertSdEmergencyVehicle(sdEmergencyVehicle));
    }

    /**
     * 修改应急车辆
     */
    @PreAuthorize("@ss.hasPermi('system:vehicle:edit')")
    @Log(title = "应急车辆", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改应急车辆")
    public AjaxResult edit(@RequestBody SdEmergencyVehicle sdEmergencyVehicle)
    {
        return toAjax(sdEmergencyVehicleService.updateSdEmergencyVehicle(sdEmergencyVehicle));
    }

    /**
     * 删除应急车辆
     */
    @PreAuthorize("@ss.hasPermi('system:vehicle:remove')")
    @Log(title = "应急车辆", businessType = BusinessType.DELETE)
	@DeleteMapping("/batchDelete")
    @ApiOperation("删除应急车辆")
    public AjaxResult remove(@RequestBody Long[] ids)
    {
        sdEmergencyVehicleService.deleteSdEmergencyVehicleByIds(ids);
        return success();
    }

    /**
     * 获取控制策略详细信息
     */
    @GetMapping("/getOrg")
    public AjaxResult getOrg(){
        List<SdEmergencyOrg> list=sdEmergencyVehicleService.getOrg();
        return AjaxResult.success(list);
    }
}
