package com.tunnel.platform.controller.emeResource;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.emeResource.SdEmergencyOrg;
import com.tunnel.business.domain.emeResource.SdEmergencyVehicle;
import com.tunnel.business.service.emeResource.ISdEmergencyVehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 应急车辆Controller
 *
 * @author dzy
 * @date 2022-08-09
 */
@RestController
@RequestMapping("/system/vehicle")
@Api(tags = "应急车辆")
@ApiSupport(order = 16)
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
     * 第三方查询应急车辆列表
     */
    @ApiOperation("第三方查询应急车辆列表")
    @GetMapping("/getVehiclelist")
    public String getVehiclelist()
    {
        return sdEmergencyVehicleService.synVehicleData();
    }

    /**
     * 获取车辆详细信息
     * @param sdEmergencyVehicle
     * @return
     */
    @ApiOperation("获取车辆详细信息")
    @GetMapping("/getVehicleDetails")
    public AjaxResult getVehicleDetails(SdEmergencyVehicle sdEmergencyVehicle){
        return AjaxResult.success(sdEmergencyVehicleService.getVehicleDetails(sdEmergencyVehicle.getPlateNumber()));
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
        return util.exportExcel(list, "应急车辆");
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
        return sdEmergencyVehicleService.updateSdEmergencyVehicle(sdEmergencyVehicle);
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
     * 新增应急车辆对应关联机构sd_emergency_org
     */
    @ApiOperation("新增应急车辆对应关联机构")
    @GetMapping("/getOrg")
    public List<Map<String, Object>> getOrg(){
        List<Map<String, Object>> list=sdEmergencyVehicleService.getOrg();
        return list;
    }

    /**
     * 同步应急车辆
     * @return
     */
    @ApiOperation("同步应急车辆")
    @GetMapping("/syncVehicle")
    public AjaxResult syncVehicle(){
        String syn = sdEmergencyVehicleService.synVehicleData();
        if(StringUtils.isNotEmpty(syn) && StringUtils.isNotNull(syn)){
            return AjaxResult.success("同步成功");
        }else {
            return AjaxResult.error("同步失败，网络未连接");
        }
    }
}
