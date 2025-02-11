package com.tunnel.platform.controller.dataInfo;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.dataInfo.SdInspectionFile;
import com.tunnel.business.service.dataInfo.ISdInspectionFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 巡检任务文件Controller
 * 
 * @author ruoyi
 * @date 2022-02-25
 */
@RestController
@RequestMapping("/system/file")
@Api(tags = "巡检任务文件")
@ApiSupport(order = 16)
public class SdInspectionFileController extends BaseController
{
    @Autowired
    private ISdInspectionFileService sdInspectionFileService;

    /**
     * 查询巡检任务文件列表
     */
    @ApiOperation("查询巡检任务文件列表")
    @PreAuthorize("@ss.hasPermi('system:file:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdInspectionFile sdInspectionFile)
    {
        startPage();
        List<SdInspectionFile> list = sdInspectionFileService.selectSdInspectionFileList(sdInspectionFile);
        return getDataTable(list);
    }

    /**
     * 获取巡检任务文件详细信息
     */
    @ApiOperation("获取巡检任务文件详细信息")
    @PreAuthorize("@ss.hasPermi('system:file:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdInspectionFileService.selectSdInspectionFileById(id));
    }

    /**
     * 新增巡检任务文件
     */
    @ApiOperation("新增巡检任务文件")
    @PreAuthorize("@ss.hasPermi('system:file:add')")
    @Log(title = "巡检任务文件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdInspectionFile sdInspectionFile)
    {
        return toAjax(sdInspectionFileService.insertSdInspectionFile(sdInspectionFile));
    }

    /**
     * 修改巡检任务文件
     */
    @ApiOperation("修改巡检任务文件")
    @PreAuthorize("@ss.hasPermi('system:file:edit')")
    @Log(title = "巡检任务文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdInspectionFile sdInspectionFile)
    {
        return toAjax(sdInspectionFileService.updateSdInspectionFile(sdInspectionFile));
    }

    /**
     * 删除巡检任务文件
     */
    @ApiOperation("删除巡检任务文件")
    @PreAuthorize("@ss.hasPermi('system:file:remove')")
    @Log(title = "巡检任务文件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdInspectionFileService.deleteSdInspectionFileByIds(ids));
    }
}
