package com.tunnel.platform.controller.informationBoard;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.informationBoard.IotBoardReleaseLog;
import com.tunnel.business.service.informationBoard.IIotBoardReleaseLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 情报板内容发布日志Controller
 *
 * @author ruoyi
 * @date 2022-07-06
 */
@RestController
@RequestMapping("/system/log")
public class IotBoardReleaseLogController extends BaseController
{
    @Autowired
    private IIotBoardReleaseLogService iotBoardReleaseLogService;

    /**
     * 查询情报板内容发布日志列表
     */
    @GetMapping("/list")
    @ApiOperation("查询情报板内容发布日志列表")
    public TableDataInfo list(IotBoardReleaseLog iotBoardReleaseLog)
    {
        startPage();
        List<IotBoardReleaseLog> list = iotBoardReleaseLogService.selectIotBoardReleaseLogList(iotBoardReleaseLog);
        return getDataTable(list);
    }

    /**
     * 获取情报板内容发布日志详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取情报板内容发布日志详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(iotBoardReleaseLogService.selectIotBoardReleaseLogById(id));
    }

    /**
     * 新增情报板内容发布日志
     */
    @Log(title = "情报板内容发布日志", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增情报板内容发布日志")
    public AjaxResult add(@RequestBody IotBoardReleaseLog iotBoardReleaseLog)
    {
        return toAjax(iotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog));
    }

    /**
     * 同步情报板内容发布日志
     */
    @Log(title = "同步情报板内容发布日志", businessType = BusinessType.INSERT)
    @PostMapping("/addIotBoard")
    @ApiOperation("同步情报板内容发布日志")
    public AjaxResult addIotBoard(@RequestBody IotBoardReleaseLog iotBoardReleaseLog)
    {
        return toAjax(iotBoardReleaseLogService.synIotBoardReleaseLog(iotBoardReleaseLog));
    }

    /**
     * 修改情报板内容发布日志
     */
    @Log(title = "情报板内容发布日志", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改情报板内容发布日志")
    public AjaxResult edit(@RequestBody IotBoardReleaseLog iotBoardReleaseLog)
    {
        return toAjax(iotBoardReleaseLogService.updateIotBoardReleaseLog(iotBoardReleaseLog));
    }

    /**
     * 导出发布记录列表
     */
    @Log(title = "发布记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation("导出发布记录列表")
    public AjaxResult export(IotBoardReleaseLog iotBoardReleaseLog)
    {
//        List<IotBoardReleaseLog> list = iotBoardReleaseLogService.selectIotBoardReleaseLogList(iotBoardReleaseLog);
        List<IotBoardReleaseLog> list = iotBoardReleaseLogService.selectIotBoardReleaseLogListToExport(iotBoardReleaseLog);
        ExcelUtil<IotBoardReleaseLog> util = new ExcelUtil<IotBoardReleaseLog>(IotBoardReleaseLog.class);
        return util.exportExcel(list, "发送记录");
    }

    /**
     * 获取情报板内容发布日志最后10条信息
     */
    @GetMapping(value = "/getLastReleaseLogsByDeviceId/{deviceId}")
    @ApiOperation("获取情报板内容发布日志最后10条信息")
    public AjaxResult getLastReleaseLogsByDeviceId(String deviceId)
    {
        return AjaxResult.success(iotBoardReleaseLogService.getLastReleaseLogsByDeviceId(deviceId));
    }

}
