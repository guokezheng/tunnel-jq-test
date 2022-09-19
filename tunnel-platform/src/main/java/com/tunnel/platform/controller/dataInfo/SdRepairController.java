package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdRepair;
import com.tunnel.business.service.dataInfo.ISdRepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备巡检修Controller
 * 
 * @author liubaokui
 * @date 2021-05-12
 */
@RestController
@RequestMapping("/system/repair")
public class SdRepairController extends BaseController
{
    @Autowired
    private ISdRepairService sdRepairService;

    /**
     * 查询设备巡检修列表
     */
    @PreAuthorize("@ss.hasPermi('system:repair:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdRepair sdRepair)
    {
        startPage();
        List<SdRepair> list = sdRepairService.selectSdRepairList(sdRepair);
        return getDataTable(list);
    }

    /**
     * 导出设备巡检修列表
     */
    @PreAuthorize("@ss.hasPermi('system:repair:export')")
    @Log(title = "设备巡检修", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdRepair sdRepair)
    {
        List<SdRepair> list = sdRepairService.selectSdRepairList(sdRepair);
        ExcelUtil<SdRepair> util = new ExcelUtil<SdRepair>(SdRepair.class);
        return util.exportExcel(list, "设备巡检修列表");
    }

    /**
     * 获取设备巡检修详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:repair:query')")
    @GetMapping(value = "/{repairId}")
    public AjaxResult getInfo(@PathVariable("repairId") Long repairId)
    {
        return AjaxResult.success(sdRepairService.selectSdRepairById(repairId));
    }

    /**
     * 新增设备巡检修
     */
    @PreAuthorize("@ss.hasPermi('system:repair:add')")
    @Log(title = "设备巡检修", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdRepair sdRepair)
    {
        return toAjax(sdRepairService.insertSdRepair(sdRepair));
    }

    /**
     * 修改设备巡检修
     */
    @PreAuthorize("@ss.hasPermi('system:repair:edit')")
    @Log(title = "设备巡检修", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdRepair sdRepair)
    {
        return toAjax(sdRepairService.updateSdRepair(sdRepair));
    }

    /**
     * 删除设备巡检修
     */
    @PreAuthorize("@ss.hasPermi('system:repair:remove')")
    @Log(title = "设备巡检修", businessType = BusinessType.DELETE)
	@DeleteMapping("/{repairIds}")
    public AjaxResult remove(@PathVariable Long[] repairIds)
    {
        return toAjax(sdRepairService.deleteSdRepairByIds(repairIds));
    }
}
