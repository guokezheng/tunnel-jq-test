package com.tunnel.platform.controller.event;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.platform.domain.event.SdTrigger;
import com.tunnel.platform.service.event.ISdTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 触发器Controller
 * 
 * @author ruoyi
 * @date 2022-09-01
 */
@RestController
@RequestMapping("/strategy/trigger")
public class SdTriggerController extends BaseController
{
    @Autowired
    private ISdTriggerService sdTriggerService;

    /**
     * 查询触发器列表
     */
    @PreAuthorize("@ss.hasPermi('strategy:trigger:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdTrigger sdTrigger)
    {
        startPage();
        List<SdTrigger> list = sdTriggerService.selectSdTriggerList(sdTrigger);
        return getDataTable(list);
    }

    /**
     * 导出触发器列表
     */
    @PreAuthorize("@ss.hasPermi('strategy:trigger:export')")
    @Log(title = "触发器", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdTrigger sdTrigger)
    {
        List<SdTrigger> list = sdTriggerService.selectSdTriggerList(sdTrigger);
        ExcelUtil<SdTrigger> util = new ExcelUtil<SdTrigger>(SdTrigger.class);
        return util.exportExcel(list, "触发器数据");
    }

    /**
     * 获取触发器详细信息
     */
    @PreAuthorize("@ss.hasPermi('strategy:trigger:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdTriggerService.selectSdTriggerById(id));
    }

    /**
     * 新增触发器
     */
    @PreAuthorize("@ss.hasPermi('strategy:trigger:add')")
    @Log(title = "触发器", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdTrigger sdTrigger)
    {
        return toAjax(sdTriggerService.insertSdTrigger(sdTrigger));
    }

    /**
     * 修改触发器
     */
    @PreAuthorize("@ss.hasPermi('strategy:trigger:edit')")
    @Log(title = "触发器", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdTrigger sdTrigger)
    {
        return toAjax(sdTriggerService.updateSdTrigger(sdTrigger));
    }

    /**
     * 删除触发器
     */
    @PreAuthorize("@ss.hasPermi('strategy:trigger:remove')")
    @Log(title = "触发器", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdTriggerService.deleteSdTriggerByIds(ids));
    }
}
