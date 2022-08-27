package com.tunnel.platform.controller.trafficBroadcasting;

import com.tunnel.platform.domain.trafficBroadcasting.SdBroadcastRecord;
import com.tunnel.platform.service.trafficBroadcasting.ISdBroadcastRecordService;
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
 * 广播记录Controller
 * 
 * @author ruoyi
 * @date 2021-11-25
 */
@RestController
@RequestMapping("/broadcastRecord/record")
public class SdBroadcastRecordController extends BaseController
{
    @Autowired
    private ISdBroadcastRecordService sdBroadcastRecordService;

    /**
     * 查询广播记录列表
     */
    @PreAuthorize("@ss.hasPermi('broadcastRecord:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdBroadcastRecord sdBroadcastRecord)
    {
        startPage();
        List<SdBroadcastRecord> list = sdBroadcastRecordService.selectSdBroadcastRecordList(sdBroadcastRecord);
        return getDataTable(list);
    }

    /**
     * 导出广播记录列表
     */
    @PreAuthorize("@ss.hasPermi('broadcastRecord:record:export')")
    @Log(title = "广播记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdBroadcastRecord sdBroadcastRecord)
    {
        List<SdBroadcastRecord> list = sdBroadcastRecordService.selectSdBroadcastRecordList(sdBroadcastRecord);
        ExcelUtil<SdBroadcastRecord> util = new ExcelUtil<SdBroadcastRecord>(SdBroadcastRecord.class);
        return util.exportExcel(list, "广播记录列表");
    }

    /**
     * 获取广播记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('broadcastRecord:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdBroadcastRecordService.selectSdBroadcastRecordById(id));
    }

    /**
     * 新增广播记录
     */
    @PreAuthorize("@ss.hasPermi('broadcastRecord:record:add')")
    @Log(title = "广播记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdBroadcastRecord sdBroadcastRecord)
    {
        return toAjax(sdBroadcastRecordService.insertSdBroadcastRecord(sdBroadcastRecord));
    }

    /**
     * 修改广播记录
     */
    @PreAuthorize("@ss.hasPermi('broadcastRecord:record:edit')")
    @Log(title = "广播记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdBroadcastRecord sdBroadcastRecord)
    {
        return toAjax(sdBroadcastRecordService.updateSdBroadcastRecord(sdBroadcastRecord));
    }

    /**
     * 删除广播记录
     */
    @PreAuthorize("@ss.hasPermi('broadcastRecord:record:remove')")
    @Log(title = "广播记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdBroadcastRecordService.deleteSdBroadcastRecordByIds(ids));
    }
}
