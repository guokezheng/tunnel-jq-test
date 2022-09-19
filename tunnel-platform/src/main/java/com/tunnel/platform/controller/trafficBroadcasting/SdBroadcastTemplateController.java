package com.tunnel.platform.controller.trafficBroadcasting;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.trafficBroadcasting.SdBroadcastTemplate;
import com.tunnel.business.service.trafficBroadcasting.ISdBroadcastTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 广播模板Controller
 * 
 * @author ruoyi
 * @date 2021-12-03
 */
@RestController
@RequestMapping("/broadcastTemplate/template")
public class SdBroadcastTemplateController extends BaseController
{
    @Autowired
    private ISdBroadcastTemplateService sdBroadcastTemplateService;

    /**
     * 查询广播模板列表
     */
    @PreAuthorize("@ss.hasPermi('broadcastTemplate:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdBroadcastTemplate sdBroadcastTemplate)
    {
        startPage();
        List<SdBroadcastTemplate> list = sdBroadcastTemplateService.selectSdBroadcastTemplateList(sdBroadcastTemplate);
        return getDataTable(list);
    }

    /**
     * 导出广播模板列表
     */
    @PreAuthorize("@ss.hasPermi('broadcastTemplate:template:export')")
    @Log(title = "广播模板", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdBroadcastTemplate sdBroadcastTemplate)
    {
        List<SdBroadcastTemplate> list = sdBroadcastTemplateService.selectSdBroadcastTemplateList(sdBroadcastTemplate);
        ExcelUtil<SdBroadcastTemplate> util = new ExcelUtil<SdBroadcastTemplate>(SdBroadcastTemplate.class);
        return util.exportExcel(list, "广播模板列表");
    }

    /**
     * 获取广播模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('broadcastTemplate:template:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdBroadcastTemplateService.selectSdBroadcastTemplateById(id));
    }

    /**
     * 新增广播模板
     */
    @PreAuthorize("@ss.hasPermi('broadcastTemplate:template:add')")
    @Log(title = "广播模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdBroadcastTemplate sdBroadcastTemplate)
    {
        return toAjax(sdBroadcastTemplateService.insertSdBroadcastTemplate(sdBroadcastTemplate));
    }

    /**
     * 修改广播模板
     */
    @PreAuthorize("@ss.hasPermi('broadcastTemplate:template:edit')")
    @Log(title = "广播模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdBroadcastTemplate sdBroadcastTemplate)
    {
        return toAjax(sdBroadcastTemplateService.updateSdBroadcastTemplate(sdBroadcastTemplate));
    }

    /**
     * 删除广播模板
     */
    @PreAuthorize("@ss.hasPermi('broadcastTemplate:template:remove')")
    @Log(title = "广播模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdBroadcastTemplateService.deleteSdBroadcastTemplateByIds(ids));
    }
}
