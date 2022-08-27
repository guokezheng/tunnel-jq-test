package com.tunnel.platform.controller.xfWaterRecord;

import java.text.ParseException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.platform.domain.xfWaterRecord.SdXfwaterRecord;
import com.tunnel.platform.service.xfWaterRecord.ISdXfwaterRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 消防水设备监测记录Controller
 * @date 2022-02-11
 */
@RestController
@RequestMapping("/xfwater/record")
public class SdXfwaterRecordController extends BaseController
{
    @Autowired
    private ISdXfwaterRecordService sdXfwaterRecordService;

    /**
     * 查询消防水设备监测记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdXfwaterRecord sdXfwaterRecord)
    {
        startPage();
        List<SdXfwaterRecord> list = sdXfwaterRecordService.selectSdXfwaterRecordList(sdXfwaterRecord);
        return getDataTable(list);
    }

    /**
     * 导出消防水设备监测记录列表
     */
    @PreAuthorize("@ss.hasPermi('xfwater:record:export')")
    @Log(title = "消防水设备监测记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdXfwaterRecord sdXfwaterRecord)
    {
        List<SdXfwaterRecord> list = sdXfwaterRecordService.selectSdXfwaterRecordList(sdXfwaterRecord);
        ExcelUtil<SdXfwaterRecord> util = new ExcelUtil<SdXfwaterRecord>(SdXfwaterRecord.class);
        return util.exportExcel(list, "消防水设备监测记录数据");
    }

    /**
     * 获取消防水设备监测记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('xfwater:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdXfwaterRecordService.selectSdXfwaterRecordById(id));
    }

    /**
     * 新增消防水设备监测记录
     */
    @PreAuthorize("@ss.hasPermi('xfwater:record:add')")
    @Log(title = "消防水设备监测记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdXfwaterRecord sdXfwaterRecord)
    {
        return toAjax(sdXfwaterRecordService.insertSdXfwaterRecord(sdXfwaterRecord));
    }

    /**
     * 修改消防水设备监测记录
     */
    @PreAuthorize("@ss.hasPermi('xfwater:record:edit')")
    @Log(title = "消防水设备监测记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdXfwaterRecord sdXfwaterRecord)
    {
        return toAjax(sdXfwaterRecordService.updateSdXfwaterRecord(sdXfwaterRecord));
    }

    /**
     * 删除消防水设备监测记录
     */
    @PreAuthorize("@ss.hasPermi('xfwater:record:remove')")
    @Log(title = "消防水设备监测记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdXfwaterRecordService.deleteSdXfwaterRecordByIds(ids));
    }

    /**
     * 获取指定压力表压力上下限和压力数据
     */
    @PostMapping("/getNumberOfPressureGaugesCollected")
    public AjaxResult getNumberOfPressureGaugesCollectedPerDay(@RequestParam("equipmentId") String equipmentId,
                                                               @RequestParam("tunnelId") String tunnelId)
    {
        if(equipmentId.equals("") || equipmentId.isEmpty() || tunnelId.equals("") || tunnelId.isEmpty()){
            throw new RuntimeException("当前获取指定压力表压力上下限和压力数据的查询参数为空，无法进行后续操作!");
        }
        return AjaxResult.success(sdXfwaterRecordService.getNumberOfPressureGaugesCollectedPerDay(equipmentId,tunnelId));
    }

    /**
     * 获取所有压力表一天上报次数
     */
    @GetMapping("/getAllPressureGaugesCollectedPerDay")
    public AjaxResult getAllPressureGaugesCollectedPerDay()
    {
        return AjaxResult.success(sdXfwaterRecordService.getAllPressureGaugesCollectedPerDay());
    }

    /**
     * 获取指定隧道下所有压力表最近一条采集记录信息并判断是否已经离线
     */
    @PostMapping("/getObtainTheStatusOfAllPressureGauges")
    public AjaxResult getObtainTheStatusOfAllPressureGauges(String tunnelId)
    {
        if(tunnelId.equals("") || tunnelId.isEmpty()){
            throw new RuntimeException("当前获取指定隧道下所有压力表状态的查询参数为空，无法进行后续操作!");
        }
        return AjaxResult.success(sdXfwaterRecordService.getObtainTheStatusOfAllPressureGauges(tunnelId));
    }

    /**
     * 获取所有压力表最近一条采集记录信息并判断是否已经离线
     */
    @GetMapping("/getStatusOfAllPressureGauges")
    public AjaxResult getStatusOfAllPressureGauges()
    {
        return AjaxResult.success(sdXfwaterRecordService.getStatusOfAllPressureGauges());
    }

    /**
     * 压力表历史数据统计
     */
    @GetMapping("/getHistoryOfPressureGauges")
    public AjaxResult getHistoryOfPressureGauges(SdXfwaterRecord sdXfwaterRecord) throws ParseException {
        if ( null == sdXfwaterRecord.getStatisticalType() ||
                sdXfwaterRecord.getStatisticalType().isEmpty() || "".equals(sdXfwaterRecord.getStatisticalType())){
            throw new RuntimeException("统计类型不能为空");
        }
        return AjaxResult.success(sdXfwaterRecordService.getHistoryOfPressureGauges(sdXfwaterRecord));
    }

}
