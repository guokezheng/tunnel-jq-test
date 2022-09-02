package com.tunnel.platform.controller.event;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.platform.domain.event.SdReleaseRecord;
import com.tunnel.platform.service.event.ISdReleaseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 发布记录Controller
 * 
 * @author ruoyi
 * @date 2022-09-01
 */
@RestController
@RequestMapping("/plan/record")
public class SdReleaseRecordController extends BaseController
{
    @Autowired
    private ISdReleaseRecordService sdReleaseRecordService;

    /**
     * 查询发布记录列表
     */
    @PreAuthorize("@ss.hasPermi('plan:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdReleaseRecord sdReleaseRecord)
    {
        startPage();
        List<SdReleaseRecord> list = sdReleaseRecordService.selectSdReleaseRecordList(sdReleaseRecord);
        return getDataTable(list);
    }

    /**
     * 导出发布记录列表
     */
    @PreAuthorize("@ss.hasPermi('plan:record:export')")
    @Log(title = "发布记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdReleaseRecord sdReleaseRecord)
    {
        List<SdReleaseRecord> list = sdReleaseRecordService.selectSdReleaseRecordList(sdReleaseRecord);
        ExcelUtil<SdReleaseRecord> util = new ExcelUtil<SdReleaseRecord>(SdReleaseRecord.class);
        return util.exportExcel(list, "发布记录数据");
    }

    /**
     * 获取发布记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('plan:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdReleaseRecordService.selectSdReleaseRecordById(id));
    }

    /**
     * 新增发布记录
     */
    @PreAuthorize("@ss.hasPermi('plan:record:add')")
    @Log(title = "发布记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdReleaseRecord sdReleaseRecord)
    {
        return toAjax(sdReleaseRecordService.insertSdReleaseRecord(sdReleaseRecord));
    }

    /**
     * 修改发布记录
     */
    @PreAuthorize("@ss.hasPermi('plan:record:edit')")
    @Log(title = "发布记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdReleaseRecord sdReleaseRecord)
    {
        return toAjax(sdReleaseRecordService.updateSdReleaseRecord(sdReleaseRecord));
    }

    /**
     * 删除发布记录
     */
    @PreAuthorize("@ss.hasPermi('plan:record:remove')")
    @Log(title = "发布记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdReleaseRecordService.deleteSdReleaseRecordByIds(ids));
    }
}
