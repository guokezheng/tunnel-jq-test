package com.tunnel.platform.controller.event;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.platform.domain.event.SdReserveProcess;
import com.tunnel.platform.service.event.ISdReserveProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预案流程节点Controller
 * 
 * @author ruoyi
 * @date 2022-09-02
 */
@RestController
@RequestMapping("/system/process")
public class SdReserveProcessController extends BaseController
{
    @Autowired
    private ISdReserveProcessService sdReserveProcessService;

    /**
     * 查询预案流程节点列表
     */
    @PreAuthorize("@ss.hasPermi('system:process:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdReserveProcess sdReserveProcess)
    {
        startPage();
        List<SdReserveProcess> list = sdReserveProcessService.selectSdReserveProcessList(sdReserveProcess);
        return getDataTable(list);
    }

    /**
     * 导出预案流程节点列表
     */
    @PreAuthorize("@ss.hasPermi('system:process:export')")
    @Log(title = "预案流程节点", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdReserveProcess sdReserveProcess)
    {
        List<SdReserveProcess> list = sdReserveProcessService.selectSdReserveProcessList(sdReserveProcess);
        ExcelUtil<SdReserveProcess> util = new ExcelUtil<SdReserveProcess>(SdReserveProcess.class);
        return util.exportExcel(list, "预案流程节点数据");
    }

    /**
     * 获取预案流程节点详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:process:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdReserveProcessService.selectSdReserveProcessById(id));
    }

    /**
     * 新增预案流程节点
     */
    @PreAuthorize("@ss.hasPermi('system:process:add')")
    @Log(title = "预案流程节点", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdReserveProcess sdReserveProcess)
    {
        return toAjax(sdReserveProcessService.insertSdReserveProcess(sdReserveProcess));
    }

    /**
     * 修改预案流程节点
     */
    @PreAuthorize("@ss.hasPermi('system:process:edit')")
    @Log(title = "预案流程节点", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdReserveProcess sdReserveProcess)
    {
        return toAjax(sdReserveProcessService.updateSdReserveProcess(sdReserveProcess));
    }

    /**
     * 删除预案流程节点
     */
    @PreAuthorize("@ss.hasPermi('system:process:remove')")
    @Log(title = "预案流程节点", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdReserveProcessService.deleteSdReserveProcessByIds(ids));
    }
}
