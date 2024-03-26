package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdEventControl;
import com.tunnel.business.service.dataInfo.ISdEventControlService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 事件管控Controller
 * 
 * @author ruoyi
 * @date 2022-02-18
 */
@RestController
@RequestMapping("/event/control")
public class SdEventControlController extends BaseController
{
    @Autowired
    private ISdEventControlService sdEventControlService;

    /**
     * 查询事件管控列表
     */
    @ApiOperation("查询事件管控列表")
    @PreAuthorize("@ss.hasPermi('event:control:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdEventControl sdEventControl)
    {
        startPage();
        List<SdEventControl> list = sdEventControlService.selectSdEventControlList(sdEventControl);
        return getDataTable(list);
    }

    /**
     * 导出事件管控列表
     */
    @ApiOperation("导出事件管控列表")
    @PreAuthorize("@ss.hasPermi('event:control:export')")
    @Log(title = "事件管控", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdEventControl sdEventControl)
    {
        List<SdEventControl> list = sdEventControlService.selectSdEventControlList(sdEventControl);
        ExcelUtil<SdEventControl> util = new ExcelUtil<SdEventControl>(SdEventControl.class);
        return util.exportExcel(list, "事件管控数据");
    }

    /**
     * 获取事件管控详细信息
     */
    @ApiOperation("获取事件管控详细信息")
    @PreAuthorize("@ss.hasPermi('event:control:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdEventControlService.selectSdEventControlById(id));
    }

    /**
     * 新增事件管控
     */
    @ApiOperation("新增事件管控")
    @PreAuthorize("@ss.hasPermi('event:control:add')")
    @Log(title = "事件管控", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdEventControl sdEventControl)
    {
        return toAjax(sdEventControlService.insertSdEventControl(sdEventControl));
    }

    /**
     * 修改事件管控
     */
    @ApiOperation("修改事件管控")
    @PreAuthorize("@ss.hasPermi('event:control:edit')")
    @Log(title = "事件管控", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdEventControl sdEventControl)
    {
        return toAjax(sdEventControlService.updateSdEventControl(sdEventControl));
    }

    /**
     * 删除事件管控
     */
    @ApiOperation("删除事件管控")
    @PreAuthorize("@ss.hasPermi('event:control:remove')")
    @Log(title = "事件管控", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdEventControlService.deleteSdEventControlByIds(ids));
    }
}
