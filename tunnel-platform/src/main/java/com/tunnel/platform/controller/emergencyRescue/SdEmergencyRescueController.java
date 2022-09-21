package com.tunnel.platform.controller.emergencyRescue;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.emergencyRescue.SdEmergencyRescue;
import com.tunnel.business.service.emergencyRescue.ISdEmergencyRescueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应急救援Controller
 * 
 * @author ruoyi
 * @date 2021-11-23
 */
@RestController
@RequestMapping("/emergencyRescue/rescue")
public class SdEmergencyRescueController extends BaseController
{
    @Autowired
    private ISdEmergencyRescueService sdEmergencyRescueService;

    /**
     * 查询应急救援列表
     */
    @PreAuthorize("@ss.hasPermi('emergencyRescue:rescue:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdEmergencyRescue sdEmergencyRescue)
    {
        startPage();
        List<SdEmergencyRescue> list = sdEmergencyRescueService.selectSdEmergencyRescueList(sdEmergencyRescue);
        return getDataTable(list);
    }

    /**
     * 导出应急救援列表
     */
    @PreAuthorize("@ss.hasPermi('emergencyRescue:rescue:export')")
    @Log(title = "应急救援", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdEmergencyRescue sdEmergencyRescue)
    {
        List<SdEmergencyRescue> list = sdEmergencyRescueService.selectSdEmergencyRescueList(sdEmergencyRescue);
        ExcelUtil<SdEmergencyRescue> util = new ExcelUtil<SdEmergencyRescue>(SdEmergencyRescue.class);
        return util.exportExcel(list, "应急救援列表");
    }

    /**
     * 获取应急救援详细信息
     */
    @PreAuthorize("@ss.hasPermi('emergencyRescue:rescue:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdEmergencyRescueService.selectSdEmergencyRescueById(id));
    }

    /**
     * 新增应急救援
     */
    @PreAuthorize("@ss.hasPermi('emergencyRescue:rescue:add')")
    @Log(title = "应急救援", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdEmergencyRescue sdEmergencyRescue)
    {
        return toAjax(sdEmergencyRescueService.insertSdEmergencyRescue(sdEmergencyRescue));
    }

    /**
     * 修改应急救援
     */
    @PreAuthorize("@ss.hasPermi('emergencyRescue:rescue:edit')")
    @Log(title = "应急救援", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdEmergencyRescue sdEmergencyRescue)
    {
        return toAjax(sdEmergencyRescueService.updateSdEmergencyRescue(sdEmergencyRescue));
    }

    /**
     * 删除应急救援
     */
    @PreAuthorize("@ss.hasPermi('emergencyRescue:rescue:remove')")
    @Log(title = "应急救援", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdEmergencyRescueService.deleteSdEmergencyRescueByIds(ids));
    }
}
