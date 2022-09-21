package com.tunnel.platform.controller.event;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.event.SdReserveProcess;
import com.tunnel.business.domain.event.SdReserveProcessModel;
import com.tunnel.business.service.event.ISdReserveProcessService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 预案流程节点Controller
 * 
 * @author ruoyi
 * @date 2022-09-02
 */
@RestController
@RequestMapping("/plan/process")
public class SdReserveProcessController extends BaseController
{
    @Autowired
    private ISdReserveProcessService sdReserveProcessService;

    /**
     * 查询预案流程节点列表
     */
    @PreAuthorize("@ss.hasPermi('plan:process:list')")
    @GetMapping("/list")
    @ApiOperation("查询预案流程节点列表")
    public TableDataInfo list(SdReserveProcess sdReserveProcess)
    {
        startPage();
        List<SdReserveProcess> list = sdReserveProcessService.selectSdReserveProcessList(sdReserveProcess);
        return getDataTable(list);
    }

    /**
     * 导出预案流程节点列表
     */
    @PreAuthorize("@ss.hasPermi('plan:process:export')")
    @GetMapping("/export")
    @ApiOperation("导出预案流程节点列表")
    public AjaxResult export(SdReserveProcess sdReserveProcess)
    {
        List<SdReserveProcess> list = sdReserveProcessService.selectSdReserveProcessList(sdReserveProcess);
        ExcelUtil<SdReserveProcess> util = new ExcelUtil<SdReserveProcess>(SdReserveProcess.class);
        return util.exportExcel(list, "预案流程节点数据");
    }

    /**
     * 获取预案流程节点详细信息
     */
    @PreAuthorize("@ss.hasPermi('plan:process:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取预案流程节点详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdReserveProcessService.selectSdReserveProcessById(id));
    }

    /**
     * 新增预案流程节点
     */
    @PreAuthorize("@ss.hasPermi('plan:process:add')")
    @PostMapping("/add")
    @ApiOperation("添加预案流程节点")
    public AjaxResult add(@RequestBody SdReserveProcess sdReserveProcess)
    {
        return toAjax(sdReserveProcessService.insertSdReserveProcess(sdReserveProcess));
    }

    /**
     * 批量添加预案流程节点
     *
     * @param sdReserveProcess
     * @return
     */
    @PreAuthorize("@ss.hasPermi('plan:process:add')")
    @PostMapping
    @ApiOperation("批量添加预案流程节点")
    public AjaxResult add(@RequestBody SdReserveProcessModel sdReserveProcess)
    {
        return toAjax(sdReserveProcessService.batchSdReserveProcessed(sdReserveProcess));
    }

    /**
     * 修改预案流程节点
     */
    @PreAuthorize("@ss.hasPermi('plan:process:edit')")
    @PutMapping
    @ApiOperation("修改预案流程节点")
    public AjaxResult edit(@RequestBody SdReserveProcess sdReserveProcess)
    {
        return toAjax(sdReserveProcessService.updateSdReserveProcess(sdReserveProcess));
    }

    /**
     * 删除预案流程节点
     */
    @PreAuthorize("@ss.hasPermi('plan:process:remove')")
	@DeleteMapping("/{ids}")
    @ApiOperation("批量删除节点信息")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdReserveProcessService.deleteSdReserveProcessByIds(ids));
    }

    /**
     * 根据预案id获得预案节点信息
     * @param reserveId
     * @return
     */
    @GetMapping("/getListByRId")
    @ApiOperation("根据预案id获得预案节点信息")
    public AjaxResult getReserveProcessByReserveId(Long reserveId) {
        return AjaxResult.success(sdReserveProcessService.selectSdReserveProcessByRId(reserveId));
    }

    /**
     * 预览展示
     * @param reserveId
     * @return
     */
    @GetMapping("/previewDisplay")
    @ApiOperation("预览展示")
    public List<Map> previewDisplay(Long reserveId) {
        List<Map> mapList = sdReserveProcessService.selectPreviewDisplay(reserveId);
        return mapList;
    }
}
