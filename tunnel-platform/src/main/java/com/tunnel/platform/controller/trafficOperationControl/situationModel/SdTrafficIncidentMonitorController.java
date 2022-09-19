package com.tunnel.platform.controller.trafficOperationControl.situationModel;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.trafficOperationControl.situationModel.SdTrafficIncidentMonitor;
import com.tunnel.business.service.trafficOperationControl.situationModel.ISdTrafficIncidentMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）Controller
 *
 * @author ruoyi
 * @date 2022-03-29
 */
@RestController
@RequestMapping("/trafficIncident/monitor")
public class SdTrafficIncidentMonitorController extends BaseController
{
    @Autowired
    private ISdTrafficIncidentMonitorService sdTrafficIncidentMonitorService;

    /**
     * 查询交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）列表
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:monitor:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdTrafficIncidentMonitor sdTrafficIncidentMonitor)
    {
        startPage();
        List<SdTrafficIncidentMonitor> list = sdTrafficIncidentMonitorService.selectSdTrafficIncidentMonitorList(sdTrafficIncidentMonitor);
        return getDataTable(list);
    }

    /**
     * 导出交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）列表
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:monitor:export')")
    @Log(title = "交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdTrafficIncidentMonitor sdTrafficIncidentMonitor)
    {
        List<SdTrafficIncidentMonitor> list = sdTrafficIncidentMonitorService.selectSdTrafficIncidentMonitorList(sdTrafficIncidentMonitor);
        ExcelUtil<SdTrafficIncidentMonitor> util = new ExcelUtil<SdTrafficIncidentMonitor>(SdTrafficIncidentMonitor.class);
        return util.exportExcel(list, "交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）数据");
    }

    /**
     * 获取交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）详细信息
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:monitor:query')")
    @GetMapping(value = "/{monitorId}")
    public AjaxResult getInfo(@PathVariable("monitorId") Long monitorId)
    {
        return AjaxResult.success(sdTrafficIncidentMonitorService.selectSdTrafficIncidentMonitorByMonitorId(monitorId));
    }

    /**
     * 新增交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:monitor:add')")
    @Log(title = "交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdTrafficIncidentMonitor sdTrafficIncidentMonitor)
    {
        return toAjax(sdTrafficIncidentMonitorService.insertSdTrafficIncidentMonitor(sdTrafficIncidentMonitor));
    }

    /**
     * 修改交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:monitor:edit')")
    @Log(title = "交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdTrafficIncidentMonitor sdTrafficIncidentMonitor)
    {
        return toAjax(sdTrafficIncidentMonitorService.updateSdTrafficIncidentMonitor(sdTrafficIncidentMonitor));
    }

    /**
     * 删除交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:monitor:remove')")
    @Log(title = "交通事件监测信息（存储摄像头、雷达等设备监测到的事件信息）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{monitorIds}")
    public AjaxResult remove(@PathVariable Long[] monitorIds)
    {
        return toAjax(sdTrafficIncidentMonitorService.deleteSdTrafficIncidentMonitorByMonitorIds(monitorIds));
    }


    /**
     * 查询获取事件监测信息、以及当前隧道的车流量等信息
     * @param sdTrafficIncidentMonitor 查询条件
     * @return
     */
    @GetMapping("/getList")
    public TableDataInfo getList(SdTrafficIncidentMonitor sdTrafficIncidentMonitor){
        startPage();
        List<Map> list = sdTrafficIncidentMonitorService.getList(sdTrafficIncidentMonitor);
        return getDataTable(list);
    }
}
