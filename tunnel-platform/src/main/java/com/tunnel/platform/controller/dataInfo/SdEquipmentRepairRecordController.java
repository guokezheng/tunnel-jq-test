package com.tunnel.platform.controller.dataInfo;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdEquipmentRepairRecord;
import com.tunnel.business.service.dataInfo.ISdEquipmentRepairRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

 /*设备维修记录Controller
 @author zhangweitian
 @date 2020-12-29*/
@RestController
@RequestMapping("/repairRecord")
public class SdEquipmentRepairRecordController extends BaseController
{
    @Autowired
    private ISdEquipmentRepairRecordService sdEquipmentRepairRecordService;

     /*查询设备维修记录列表*/
    @ApiOperation("查询设备维修记录列表")
    @GetMapping("/list")
    public TableDataInfo list(SdEquipmentRepairRecord sdEquipmentRepairRecord)
    {
        startPage();
        List<SdEquipmentRepairRecord> list = sdEquipmentRepairRecordService.selectSdEquipmentRepairRecordList(sdEquipmentRepairRecord);
        return getDataTable(list);
    }

     /*导出设备维修记录列表*/
     @ApiOperation("导出设备维修记录列表")
    @Log(title = "设备维修记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdEquipmentRepairRecord sdEquipmentRepairRecord)
    {
        List<SdEquipmentRepairRecord> list = sdEquipmentRepairRecordService.selectSdEquipmentRepairRecordList(sdEquipmentRepairRecord);
        ExcelUtil<SdEquipmentRepairRecord> util = new ExcelUtil<SdEquipmentRepairRecord>(SdEquipmentRepairRecord.class);
        return util.exportExcel(list, "设备维修记录");
    }

     /*获取设备维修记录详细信息*/
     @ApiOperation("获取设备维修记录详细信息")
    @GetMapping(value = "/{repairId}")
    public AjaxResult getInfo(@PathVariable("repairId") Long repairId)
    {
        return AjaxResult.success(sdEquipmentRepairRecordService.selectSdEquipmentRepairRecordById(repairId));
    }

     /*新增设备维修记录*/
     @ApiOperation("新增设备维修记录")
    @Log(title = "设备维修记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdEquipmentRepairRecord sdEquipmentRepairRecord)
    {
        return toAjax(sdEquipmentRepairRecordService.insertSdEquipmentRepairRecord(sdEquipmentRepairRecord));
    }

     /*修改设备维修记录*/
     @ApiOperation("修改设备维修记录")
    @Log(title = "设备维修记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdEquipmentRepairRecord sdEquipmentRepairRecord)
    {
        return toAjax(sdEquipmentRepairRecordService.updateSdEquipmentRepairRecord(sdEquipmentRepairRecord));
    }

     /*删除设备维修记录*/
     @ApiOperation("删除设备维修记录")
    @Log(title = "设备维修记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{repairIds}")
    public AjaxResult remove(@PathVariable Long[] repairIds)
    {
        return toAjax(sdEquipmentRepairRecordService.deleteSdEquipmentRepairRecordByIds(repairIds));
    }
}
