package com.tunnel.platform.controller.informationBoard;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.informationBoard.SdReleaseRecord;
import com.tunnel.business.service.informationBoard.ISdReleaseRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 发布记录Controller
 * 
 * @author 刘方堃
 * @date 2021-11-29
 */
@RestController
@RequestMapping("/system/record")
public class SdReleaseRecordController extends BaseController
{
    @Autowired
    private ISdReleaseRecordService sdReleaseRecordService;

    /**
     * 查询发布记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    @ApiOperation("查询发布记录列表")
    public TableDataInfo list(SdReleaseRecord sdReleaseRecord)
    {
        startPage();
        List<SdReleaseRecord> list = sdReleaseRecordService.selectSdReleaseRecordList(sdReleaseRecord);
        return getDataTable(list);
    }

    /**
     * 导出发布记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "发布记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation("导出发布记录列表")
    public AjaxResult export(SdReleaseRecord sdReleaseRecord)
    {
        List<SdReleaseRecord> list = sdReleaseRecordService.selectSdReleaseRecordList(sdReleaseRecord);
        ExcelUtil<SdReleaseRecord> util = new ExcelUtil<SdReleaseRecord>(SdReleaseRecord.class);
        return util.exportExcel(list, "发布记录列表");
    }

    /**
     * 获取发布记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取发布记录详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdReleaseRecordService.selectSdReleaseRecordById(id));
    }

    /**
     * 新增发布记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "发布记录", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增发布记录")
    public AjaxResult add(@RequestBody SdReleaseRecord sdReleaseRecord)
    {
        return toAjax(sdReleaseRecordService.insertSdReleaseRecord(sdReleaseRecord));
    }

    /**
     * 修改发布记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "发布记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改发布记录")
    public AjaxResult edit(@RequestBody SdReleaseRecord sdReleaseRecord)
    {
        return toAjax(sdReleaseRecordService.updateSdReleaseRecord(sdReleaseRecord));
    }

    /**
     * 删除发布记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "发布记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除发布记录")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdReleaseRecordService.deleteSdReleaseRecordByIds(ids));
    }
}
