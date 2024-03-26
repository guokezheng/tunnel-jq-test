package com.tunnel.platform.controller.trafficOperationControl.controlConfig;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.trafficOperationControl.controlConfig.SdControlConfigMeasure;
import com.tunnel.business.service.trafficOperationControl.controlConfig.ISdControlConfigMeasureService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管控等级配置措施-管控措施Controller
 *
 * @author ruoyi
 * @date 2022-02-15
 */
@RestController
@RequestMapping("/controlConfig/measure")
public class SdControlConfigMeasureController extends BaseController
{
    @Autowired
    private ISdControlConfigMeasureService sdControlConfigMeasureService;

    /**
     * 查询管控等级配置措施-管控措施列表
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:measure:list')")
    @GetMapping("/list")
    @ApiOperation("查询管控等级配置措施-管控措施列表")
    public TableDataInfo list(SdControlConfigMeasure sdControlConfigMeasure)
    {
        startPage();
        List<SdControlConfigMeasure> list = sdControlConfigMeasureService.selectSdControlConfigMeasureList(sdControlConfigMeasure);
        return getDataTable(list);
    }

    /**
     * 导出管控等级配置措施-管控措施列表
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:measure:export')")
    @Log(title = "管控等级配置措施-管控措施", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation("导出管控等级配置措施-管控措施列表")
    public AjaxResult export(SdControlConfigMeasure sdControlConfigMeasure)
    {
        List<SdControlConfigMeasure> list = sdControlConfigMeasureService.selectSdControlConfigMeasureList(sdControlConfigMeasure);
        ExcelUtil<SdControlConfigMeasure> util = new ExcelUtil<SdControlConfigMeasure>(SdControlConfigMeasure.class);
        return util.exportExcel(list, "管控等级配置措施-管控措施数据");
    }

    /**
     * 获取管控等级配置措施-管控措施详细信息
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:measure:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取管控等级配置措施-管控措施详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdControlConfigMeasureService.selectSdControlConfigMeasureById(id));
    }

    /**
     * 新增管控等级配置措施-管控措施
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:measure:add')")
    @Log(title = "管控等级配置措施-管控措施", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增管控等级配置措施-管控措施")
    public AjaxResult add(@RequestBody SdControlConfigMeasure sdControlConfigMeasure)
    {
        return toAjax(sdControlConfigMeasureService.insertSdControlConfigMeasure(sdControlConfigMeasure));
    }

    /**
     * 修改管控等级配置措施-管控措施
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:measure:edit')")
    @Log(title = "管控等级配置措施-管控措施", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改管控等级配置措施-管控措施")
    public AjaxResult edit(@RequestBody SdControlConfigMeasure sdControlConfigMeasure)
    {
        return toAjax(sdControlConfigMeasureService.updateSdControlConfigMeasure(sdControlConfigMeasure));
    }

    /**
     * 删除管控等级配置措施-管控措施
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:measure:remove')")
    @Log(title = "管控等级配置措施-管控措施", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除管控等级配置措施-管控措施")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdControlConfigMeasureService.deleteSdControlConfigMeasureByIds(ids));
    }
}
