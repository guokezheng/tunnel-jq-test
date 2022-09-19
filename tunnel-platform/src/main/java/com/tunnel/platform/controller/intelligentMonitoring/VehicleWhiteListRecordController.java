package com.tunnel.platform.controller.intelligentMonitoring;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.intelligentMonitoring.VehicleWhiteListRecord;
import com.tunnel.business.domain.intelligentMonitoring.VehicleWhiteListRecordDTO;
import com.tunnel.business.service.intelligentMonitoring.IVehicleWhiteListRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 白名单车辆通行记录Controller
 *
 * @author ruoyi
 * @date 2021-11-30
 */
@RestController
@RequestMapping("/business/vehicleWhiteListRecord")
public class VehicleWhiteListRecordController extends BaseController
{
    @Autowired
    private IVehicleWhiteListRecordService vehicleWhiteListRecordService;

    /**
     * 查询白名单车辆通行记录列表
     */
    @PreAuthorize("@ss.hasPermi('business:vehicleWhiteListRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(VehicleWhiteListRecordDTO vehicleWhiteListRecordDTO)
    {
        startPage();
        List<VehicleWhiteListRecord> list = vehicleWhiteListRecordService.selectVehicleWhiteListRecordList(vehicleWhiteListRecordDTO);
        return getDataTable(list);
    }

    /**
     * 导出白名单车辆通行记录列表
     */
    @PreAuthorize("@ss.hasPermi('business:vehicleWhiteListRecord:export')")
    @Log(title = "白名单车辆通行记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(VehicleWhiteListRecordDTO vehicleWhiteListRecordDTO)
    {
        List<VehicleWhiteListRecord> list = vehicleWhiteListRecordService.selectVehicleWhiteListRecordList(vehicleWhiteListRecordDTO);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0;i < list.size();i++) {
            VehicleWhiteListRecord whiteListRecord = list.get(i);
            String format = dateFormat.format(whiteListRecord.getCreateTime());
            whiteListRecord.setPassTime(format);
        }
        ExcelUtil<VehicleWhiteListRecord> util = new ExcelUtil<VehicleWhiteListRecord>(VehicleWhiteListRecord.class);
        return util.exportExcel(list, "白名单车辆通行记录列表");
    }

    /**
     * 获取白名单车辆通行记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:vehicleWhiteListRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(vehicleWhiteListRecordService.selectVehicleWhiteListRecordById(id));
    }

    /**
     * 新增白名单车辆通行记录
     */
    @PreAuthorize("@ss.hasPermi('business:vehicleWhiteListRecord:add')")
    @Log(title = "白名单车辆通行记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VehicleWhiteListRecord vehicleWhiteListRecord)
    {
        return toAjax(vehicleWhiteListRecordService.insertVehicleWhiteListRecord(vehicleWhiteListRecord));
    }

    /**
     * 修改白名单车辆通行记录
     */
    @PreAuthorize("@ss.hasPermi('business:vehicleWhiteListRecord:edit')")
    @Log(title = "白名单车辆通行记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VehicleWhiteListRecord vehicleWhiteListRecord)
    {
        return toAjax(vehicleWhiteListRecordService.updateVehicleWhiteListRecord(vehicleWhiteListRecord));
    }

    /**
     * 删除白名单车辆通行记录
     */
    @PreAuthorize("@ss.hasPermi('business:vehicleWhiteListRecord:remove')")
    @Log(title = "白名单车辆通行记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(vehicleWhiteListRecordService.deleteVehicleWhiteListRecordByIds(ids));
    }
}
