package com.tunnel.platform.controller.dataInfo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.platform.domain.dataInfo.SdDeviceDataRecord;
import com.tunnel.platform.service.dataInfo.ISdDeviceDataRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设备数据历史记录Controller
 *
 * @author ruoyi
 * @date 2022-09-15
 */
@RestController
@RequestMapping("/devicedata/record")
public class SdDeviceDataRecordController extends BaseController
{
    @Autowired
    private ISdDeviceDataRecordService sdDeviceDataRecordService;

    /**
     * 查询设备数据历史记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdDeviceDataRecord sdDeviceDataRecord)
    {
        startPage();
        List<SdDeviceDataRecord> list = sdDeviceDataRecordService.selectSdDeviceDataRecordList(sdDeviceDataRecord);
        return getDataTable(list);
    }

    /**
     * 导出设备数据历史记录列表
     */
    @Log(title = "设备数据历史记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdDeviceDataRecord sdDeviceDataRecord)
    {
        List<SdDeviceDataRecord> list = sdDeviceDataRecordService.selectSdDeviceDataRecordList(sdDeviceDataRecord);
        ExcelUtil<SdDeviceDataRecord> util = new ExcelUtil<SdDeviceDataRecord>(SdDeviceDataRecord.class);
        return util.exportExcel(list, "设备数据历史记录数据");
    }

    /**
     * 获取设备数据历史记录详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdDeviceDataRecordService.selectSdDeviceDataRecordById(id));
    }

    /**
     * 新增设备数据历史记录
     */
    @Log(title = "设备数据历史记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdDeviceDataRecord sdDeviceDataRecord)
    {
        return toAjax(sdDeviceDataRecordService.insertSdDeviceDataRecord(sdDeviceDataRecord));
    }

    /**
     * 修改设备数据历史记录
     */
    @Log(title = "设备数据历史记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdDeviceDataRecord sdDeviceDataRecord)
    {
        return toAjax(sdDeviceDataRecordService.updateSdDeviceDataRecord(sdDeviceDataRecord));
    }

    /**
     * 删除设备数据历史记录
     */
    @Log(title = "设备数据历史记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdDeviceDataRecordService.deleteSdDeviceDataRecordByIds(ids));
    }
}
