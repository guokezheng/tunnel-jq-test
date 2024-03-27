package com.tunnel.platform.controller.protocol;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.protocol.SdDevicePointPlc;
import com.tunnel.business.service.protocol.ISdDevicePointPlcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PLC设备点位(区别测控执行器)Controller
 *
 * @author ruoyi
 * @date 2023-09-16
 */
@RestController
@RequestMapping("/system/plc")
@Api(tags = "PLC设备点位(区别测控执行器)Controller")
@ApiSupport(order = 16)
public class SdDevicePointPlcController extends BaseController
{
    @Autowired
    private ISdDevicePointPlcService sdDevicePointPlcService;

    /**
     * 查询PLC设备点位(区别测控执行器)列表
     */
    @PreAuthorize("@ss.hasPermi('system:plc:list')")
    @GetMapping("/list")
    @ApiOperation("查询PLC设备点位(区别测控执行器)列表")
    public TableDataInfo list(SdDevicePointPlc sdDevicePointPlc)
    {
        startPage();
        List<SdDevicePointPlc> list = sdDevicePointPlcService.selectSdDevicePointPlcList(sdDevicePointPlc);
        return getDataTable(list);
    }

    /**
     * 导出PLC设备点位(区别测控执行器)列表
     */
    @PreAuthorize("@ss.hasPermi('system:plc:export')")
    @Log(title = "PLC设备点位(区别测控执行器)", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation("导出PLC设备点位(区别测控执行器)列表")
    public AjaxResult export(SdDevicePointPlc sdDevicePointPlc)
    {
        List<SdDevicePointPlc> list = sdDevicePointPlcService.selectSdDevicePointPlcList(sdDevicePointPlc);
        ExcelUtil<SdDevicePointPlc> util = new ExcelUtil<SdDevicePointPlc>(SdDevicePointPlc.class);
        return util.exportExcel(list, "PLC设备点位(区别测控执行器)数据");
    }

    /**
     * 获取PLC设备点位(区别测控执行器)详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:plc:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取PLC设备点位(区别测控执行器)详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdDevicePointPlcService.selectSdDevicePointPlcById(id));
    }

    /**
     * 新增PLC设备点位(区别测控执行器)
     */
    @PreAuthorize("@ss.hasPermi('system:plc:add')")
    @Log(title = "PLC设备点位(区别测控执行器)", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增PLC设备点位(区别测控执行器)")
    public AjaxResult add(@RequestBody SdDevicePointPlc sdDevicePointPlc)
    {
        return toAjax(sdDevicePointPlcService.insertSdDevicePointPlc(sdDevicePointPlc));
    }

    /**
     * 修改PLC设备点位(区别测控执行器)
     */
    @PreAuthorize("@ss.hasPermi('system:plc:edit')")
    @Log(title = "PLC设备点位(区别测控执行器)", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改PLC设备点位(区别测控执行器)")
    public AjaxResult edit(@RequestBody SdDevicePointPlc sdDevicePointPlc)
    {
        return toAjax(sdDevicePointPlcService.updateSdDevicePointPlc(sdDevicePointPlc));
    }

    /**
     * 删除PLC设备点位(区别测控执行器)
     */
    @PreAuthorize("@ss.hasPermi('system:plc:remove')")
    @Log(title = "PLC设备点位(区别测控执行器)", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除PLC设备点位(区别测控执行器)")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdDevicePointPlcService.deleteSdDevicePointPlcByIds(ids));
    }
}
