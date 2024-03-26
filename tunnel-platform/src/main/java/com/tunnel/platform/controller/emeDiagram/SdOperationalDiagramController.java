package com.tunnel.platform.controller.emeDiagram;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.emeDiagram.SdOperationalDiagram;
import com.tunnel.business.service.emeDiagram.ISdOperationalDiagramService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 作战示意图Controller
 * 
 * @author ruoyi
 * @date 2021-11-22
 */
@RestController
@RequestMapping("/business/emeDiagram")
public class SdOperationalDiagramController extends BaseController
{
    @Autowired
    private ISdOperationalDiagramService sdOperationalDiagramService;

    /**
     * 查询作战示意图列表
     */
    @ApiOperation("查询作战示意图列表")
    @PreAuthorize("@ss.hasPermi('business:emeDiagram:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdOperationalDiagram sdOperationalDiagram)
    {
        startPage();
        List<SdOperationalDiagram> list = sdOperationalDiagramService.selectSdOperationalDiagramList(sdOperationalDiagram);
        return getDataTable(list);
    }

    /**
     * 导出作战示意图列表
     */
    @ApiOperation("导出作战示意图列表")
    @PreAuthorize("@ss.hasPermi('business:emeDiagram:export')")
    @Log(title = "作战示意图", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdOperationalDiagram sdOperationalDiagram)
    {
        List<SdOperationalDiagram> list = sdOperationalDiagramService.selectSdOperationalDiagramList(sdOperationalDiagram);
        ExcelUtil<SdOperationalDiagram> util = new ExcelUtil<SdOperationalDiagram>(SdOperationalDiagram.class);
        return util.exportExcel(list, "作战示意图");
    }

    /**
     * 获取作战示意图详细信息
     */
    @ApiOperation("获取作战示意图详细信息")
    @PreAuthorize("@ss.hasPermi('business:emeDiagram:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdOperationalDiagramService.selectSdOperationalDiagramById(id));
    }

    /**
     * 新增作战示意图
     */
    @ApiOperation("新增作战示意图")
    @PreAuthorize("@ss.hasPermi('business:emeDiagram:add')")
    @Log(title = "作战示意图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdOperationalDiagram sdOperationalDiagram)
    {
        return toAjax(sdOperationalDiagramService.insertSdOperationalDiagram(sdOperationalDiagram));
    }

    /**
     * 修改作战示意图
     */
    @ApiOperation("修改作战示意图")
    @PreAuthorize("@ss.hasPermi('business:emeDiagram:edit')")
    @Log(title = "作战示意图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdOperationalDiagram sdOperationalDiagram)
    {
        return toAjax(sdOperationalDiagramService.updateSdOperationalDiagram(sdOperationalDiagram));
    }

    /**
     * 删除作战示意图
     */
    @ApiOperation("删除作战示意图")
    @PreAuthorize("@ss.hasPermi('business:emeDiagram:remove')")
    @Log(title = "作战示意图", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdOperationalDiagramService.deleteSdOperationalDiagramByIds(ids));
    }
}
