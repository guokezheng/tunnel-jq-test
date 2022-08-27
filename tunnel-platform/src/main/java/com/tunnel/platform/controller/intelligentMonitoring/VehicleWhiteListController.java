package com.tunnel.platform.controller.intelligentMonitoring;

import com.tunnel.platform.domain.intelligentMonitoring.VehicleWhiteList;
import com.tunnel.platform.service.intelligentMonitoring.IVehicleWhiteListService;
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
 * 车辆通行白名单Controller
 *
 * @author ruoyi
 * @date 2021-11-30
 */
@RestController
@RequestMapping("/business/vehicleWhiteList")
public class VehicleWhiteListController extends BaseController
{
    @Autowired
    private IVehicleWhiteListService vehicleWhiteListService;

    /**
     * 查询车辆通行白名单列表
     */
    @PreAuthorize("@ss.hasPermi('business:vehicleWhiteList:list')")
    @GetMapping("/list")
    public TableDataInfo list(VehicleWhiteList vehicleWhiteList)
    {
        startPage();
        List<VehicleWhiteList> list = vehicleWhiteListService.selectVehicleWhiteListList(vehicleWhiteList);
        return getDataTable(list);
    }

    /**
     * 导出车辆通行白名单列表
     */
    @PreAuthorize("@ss.hasPermi('business:vehicleWhiteList:export')")
    @Log(title = "车辆通行白名单", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(VehicleWhiteList vehicleWhiteList)
    {
        List<VehicleWhiteList> list = vehicleWhiteListService.selectVehicleWhiteListList(vehicleWhiteList);
        ExcelUtil<VehicleWhiteList> util = new ExcelUtil<VehicleWhiteList>(VehicleWhiteList.class);
        return util.exportExcel(list, "车辆通行白名单列表");
    }

    /**
     * 获取车辆通行白名单详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:vehicleWhiteList:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(vehicleWhiteListService.selectVehicleWhiteListById(id));
    }

    /**
     * 新增车辆通行白名单
     */
    @PreAuthorize("@ss.hasPermi('business:vehicleWhiteList:add')")
    @Log(title = "车辆通行白名单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VehicleWhiteList vehicleWhiteList)
    {
        return toAjax(vehicleWhiteListService.insertVehicleWhiteList(vehicleWhiteList));
    }

    /**
     * 修改车辆通行白名单
     */
    @PreAuthorize("@ss.hasPermi('business:vehicleWhiteList:edit')")
    @Log(title = "车辆通行白名单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VehicleWhiteList vehicleWhiteList)
    {
        return toAjax(vehicleWhiteListService.updateVehicleWhiteList(vehicleWhiteList));
    }

    /**
     * 删除车辆通行白名单
     */
    @PreAuthorize("@ss.hasPermi('business:vehicleWhiteList:remove')")
    @Log(title = "车辆通行白名单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(vehicleWhiteListService.deleteVehicleWhiteListByIds(ids));
    }
}
