package com.tunnel.platform.controller.informationBoard;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.platform.domain.informationBoard.SdVmsTemplate;
import com.tunnel.platform.service.informationBoard.ISdVmsTemplateService;
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
 * 情报板模板Controller
 *
 * @author 刘方堃
 * @date 2021-11-30
 */
@RestController
@RequestMapping("/system/template")
public class SdVmsTemplateController extends BaseController
{
    @Autowired
    private ISdVmsTemplateService sdVmsTemplateService;

    /**
     * 查询情报板模板列表
     */
//    @PreAuthorize("@ss.hasPermi('system:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdVmsTemplate sdVmsTemplate)
    {
        startPage();
        List<SdVmsTemplate> list = sdVmsTemplateService.selectSdVmsTemplateList(sdVmsTemplate);
        return getDataTable(list);
    }

    /**
     * 导出情报板模板列表
     */
//    @PreAuthorize("@ss.hasPermi('system:template:export')")
    @Log(title = "情报板模板", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdVmsTemplate sdVmsTemplate)
    {
        List<SdVmsTemplate> list = sdVmsTemplateService.selectSdVmsTemplateList(sdVmsTemplate);
        ExcelUtil<SdVmsTemplate> util = new ExcelUtil<SdVmsTemplate>(SdVmsTemplate.class);
        return util.exportExcel(list, "情报板模板列表");
    }

    /**
     * 获取情报板模板详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:template:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdVmsTemplateService.selectSdVmsTemplateById(id));
    }

    /**
     * 新增情报板模板
     */
//    @PreAuthorize("@ss.hasPermi('system:template:add')")
    @Log(title = "情报板模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JSONObject jsonObject)
    {
        return AjaxResult.success(sdVmsTemplateService.insertSdVmsTemplate(jsonObject));
    }

    /**
     * 修改情报板模板
     */
//    @PreAuthorize("@ss.hasPermi('system:template:edit')")
    @Log(title = "情报板模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JSONObject jsonObject)
    {
        return toAjax(sdVmsTemplateService.updateSdVmsTemplate(jsonObject));
    }

    /**
     * 删除情报板模板
     */
//    @PreAuthorize("@ss.hasPermi('system:template:remove')")
    @Log(title = "情报板模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdVmsTemplateService.deleteSdVmsTemplateByIds(ids));
    }

    /**
     * 情报板发布
     */
    @PostMapping("/informationBoardRelease")
    public AjaxResult informationBoardRelease(JSONObject jsonObject) {
        return AjaxResult.success(sdVmsTemplateService.informationBoardRelease(jsonObject));
    }

    /**
     * 情报板获取
     */
    @PostMapping("/informationBoardAcquisition")
    public AjaxResult informationBoardAcquisition(JSONObject jsonObject) {
        return AjaxResult.success(sdVmsTemplateService.informationBoardAcquisition(jsonObject));
    }
}
