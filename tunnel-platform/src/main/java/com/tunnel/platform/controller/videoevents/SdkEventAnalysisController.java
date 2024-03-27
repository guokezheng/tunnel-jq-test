package com.tunnel.platform.controller.videoevents;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.videoevents.SdkEventAnalysis;
import com.tunnel.business.service.videoevents.ISdkEventAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "车道事件Controller")
@ApiSupport(order = 16)
public class SdkEventAnalysisController extends BaseController
{
    @Autowired
    private ISdkEventAnalysisService sdkEventAnalysisService;

    /**
     * 查询车道事件列表
     */
    @PreAuthorize("@ss.hasPermi('system:eventanalysis:list')")
    @GetMapping("/list")
    @ApiOperation("查询车道事件列表")
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
    @ApiOperation("导出车道事件列表")
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
    @ApiOperation("获取车道事件详细信息")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(sdkEventAnalysisService.selectSdkEventAnalysisById(id));
    }
    @GetMapping(value = "/getEventAnalysisByTaskId/{id}")
    @ApiOperation("通过id获取车道事件详细信息")
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
    @ApiOperation("新增车道事件")
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
    @ApiOperation("修改车道事件")
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
    @ApiOperation("删除车道事件")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(sdkEventAnalysisService.deleteSdkEventAnalysisByIds(ids));
    }
}
