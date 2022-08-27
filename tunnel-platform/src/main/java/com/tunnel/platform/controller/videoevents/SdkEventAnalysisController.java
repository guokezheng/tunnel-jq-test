package com.tunnel.platform.controller.videoevents;

import com.tunnel.platform.domain.videoevents.SdkEventAnalysis;
import com.tunnel.platform.service.videoevents.ISdkEventAnalysisService;
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
 * 车道事件Controller
 * 
 * @author ruoyi
 * @date 2021-04-26
 */
@RestController
@RequestMapping("/eventanalysis")
public class SdkEventAnalysisController extends BaseController
{
    @Autowired
    private ISdkEventAnalysisService sdkEventAnalysisService;

    /**
     * 查询车道事件列表
     */
    @PreAuthorize("@ss.hasPermi('system:eventanalysis:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdkEventAnalysis sdkEventAnalysis)
    {
        startPage();
        List<SdkEventAnalysis> list = sdkEventAnalysisService.selectSdkEventAnalysisList(sdkEventAnalysis);
        return getDataTable(list);
    }

    /**
     * 导出车道事件列表
     */
    @PreAuthorize("@ss.hasPermi('system:eventanalysis:export')")
    @Log(title = "车道事件", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdkEventAnalysis sdkEventAnalysis)
    {
        List<SdkEventAnalysis> list = sdkEventAnalysisService.selectSdkEventAnalysisList(sdkEventAnalysis);
        ExcelUtil<SdkEventAnalysis> util = new ExcelUtil<SdkEventAnalysis>(SdkEventAnalysis.class);
        return util.exportExcel(list, "eventanalysis");
    }

    /**
     * 获取车道事件详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:eventanalysis:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(sdkEventAnalysisService.selectSdkEventAnalysisById(id));
    }
    @GetMapping(value = "/getEventAnalysisByTaskId/{id}")
    public AjaxResult getInfoByTaskId(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(sdkEventAnalysisService.selectSdkEventAnalysisByTaskId(id));
    }
    /**
     * 新增车道事件
     */
    @PreAuthorize("@ss.hasPermi('system:eventanalysis:add')")
    @Log(title = "车道事件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdkEventAnalysis sdkEventAnalysis)
    {
        return toAjax(sdkEventAnalysisService.insertSdkEventAnalysis(sdkEventAnalysis));
    }

    /**
     * 修改车道事件
     */
    @PreAuthorize("@ss.hasPermi('system:eventanalysis:edit')")
    @Log(title = "车道事件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdkEventAnalysis sdkEventAnalysis)
    {
        return toAjax(sdkEventAnalysisService.updateSdkEventAnalysis(sdkEventAnalysis));
    }

    /**
     * 删除车道事件
     */
    @PreAuthorize("@ss.hasPermi('system:eventanalysis:remove')")
    @Log(title = "车道事件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(sdkEventAnalysisService.deleteSdkEventAnalysisByIds(ids));
    }
}
