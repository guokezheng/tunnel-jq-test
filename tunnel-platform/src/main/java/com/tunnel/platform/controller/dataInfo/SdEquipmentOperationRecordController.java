package com.tunnel.platform.controller.dataInfo;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdEquipmentOperationRecord;
import com.tunnel.business.service.dataInfo.ISdEquipmentOperationRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备运行记录Controller
 * 
 * @author ruoyi
 * @date 2022-01-21
 */
@RestController
@RequestMapping("/system/equipmentOperationRecord")
@Api(tags = "设备运行记录")
@ApiSupport(order = 16)
public class SdEquipmentOperationRecordController extends BaseController
{
    @Autowired
    private ISdEquipmentOperationRecordService sdEquipmentOperationRecordService;

    /**
     * 查询设备运行记录列表
     */
    @ApiOperation("查询设备运行记录列表")
    @PreAuthorize("@ss.hasPermi('system:equipmentOperationRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdEquipmentOperationRecord sdEquipmentOperationRecord)
    {
        startPage();
        List<SdEquipmentOperationRecord> list = sdEquipmentOperationRecordService.selectSdEquipmentOperationRecordList(sdEquipmentOperationRecord);
        return getDataTable(list);
    }

    /**
     * 导出设备运行记录列表
     */
    @ApiOperation("导出设备运行记录列表")
    @PreAuthorize("@ss.hasPermi('system:equipmentOperationRecord:export')")
    @Log(title = "设备运行记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdEquipmentOperationRecord sdEquipmentOperationRecord)
    {
        List<SdEquipmentOperationRecord> list = sdEquipmentOperationRecordService.selectSdEquipmentOperationRecordList(sdEquipmentOperationRecord);
        ExcelUtil<SdEquipmentOperationRecord> util = new ExcelUtil<SdEquipmentOperationRecord>(SdEquipmentOperationRecord.class);
        return util.exportExcel(list, "设备运行记录数据");
    }

    /**
     * 获取设备运行记录详细信息
     */
    @ApiOperation("获取设备运行记录详细信息")
    @PreAuthorize("@ss.hasPermi('system:equipmentOperationRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdEquipmentOperationRecordService.selectSdEquipmentOperationRecordById(id));
    }

    /**
     * 新增设备运行记录
     */
    @ApiOperation("新增设备运行记录")
    @PreAuthorize("@ss.hasPermi('system:equipmentOperationRecord:add')")
    @Log(title = "设备运行记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdEquipmentOperationRecord sdEquipmentOperationRecord)
    {
        return toAjax(sdEquipmentOperationRecordService.insertSdEquipmentOperationRecord(sdEquipmentOperationRecord));
    }

    /**
     * 修改设备运行记录
     */
    @ApiOperation("修改设备运行记录")
    @PreAuthorize("@ss.hasPermi('system:equipmentOperationRecord:edit')")
    @Log(title = "设备运行记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdEquipmentOperationRecord sdEquipmentOperationRecord)
    {
        return toAjax(sdEquipmentOperationRecordService.updateSdEquipmentOperationRecord(sdEquipmentOperationRecord));
    }

    /**
     * 删除设备运行记录
     */
    @ApiOperation("删除设备运行记录")
    @PreAuthorize("@ss.hasPermi('system:equipmentOperationRecord:remove')")
    @Log(title = "设备运行记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdEquipmentOperationRecordService.deleteSdEquipmentOperationRecordByIds(ids));
    }
}
