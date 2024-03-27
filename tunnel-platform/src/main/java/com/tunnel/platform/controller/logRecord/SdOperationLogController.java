package com.tunnel.platform.controller.logRecord;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.datacenter.domain.enumeration.DeviceControlTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DeviceDirectionEnum;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.domain.logRecord.SdOperationLogDTO;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.platform.controller.informationBoard.AjaxResultb;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 操作日志Controller
 *
 * @author yanghousheng
 * @date 2020-09-03
 */
@RestController
@RequestMapping("/log")
@Api(tags = "操作日志Controller")
@ApiSupport(order = 16)
public class SdOperationLogController extends BaseController
{
    @Autowired
    private ISdOperationLogService sdOperationLogService;

    /**
     * 查询操作日志列表
     */
//    @PreAuthorize("@ss.hasPermi('system:log:list')")
    @GetMapping("/list")
    @ApiOperation("查询操作日志列表")
    public TableDataInfo list(SdOperationLog sdOperationLog)
    {
        int count = sdOperationLogService.selectSdOperationLogCountList(sdOperationLog);
        if(count > 0){
            PageDomain pageDomain = TableSupport.getPageDomain();
            sdOperationLog.getParams().put("pageSize", pageDomain.getPageSize());
            sdOperationLog.getParams().put("pageNum", (pageDomain.getPageNum() - 1) *pageDomain.getPageSize());
            List<SdOperationLog> list = sdOperationLogService.selectSdOperationLogList(sdOperationLog);
            return new TableDataInfo(list,count);
        }

        return new TableDataInfo(null,0);
    }

    /**
     * 导出操作日志列表
     */
//    @PreAuthorize("@ss.hasPermi('system:log:export')")
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation("导出操作日志列表")
    public void export(HttpServletResponse response, SdOperationLog sdOperationLog) throws IOException
    {
        List<SdOperationLog> list = sdOperationLogService.selectSdOperationLogList(sdOperationLog);
        ExcelUtil<SdOperationLog> util = new ExcelUtil<SdOperationLog>(SdOperationLog.class);
        util.exportExcel(list, "操作日志列表");
    }

    /**
     * 获取操作日志详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:log:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取操作日志详细信息")
    public AjaxResultb getInfo(@PathVariable("id") Long id)
    {
        return AjaxResultb.success(sdOperationLogService.selectSdOperationLogById(id));
    }

    /**
     * 新增操作日志
     */
//    @PreAuthorize("@ss.hasPermi('system:log:add')")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增操作日志")
    public AjaxResult add(@RequestBody SdOperationLog sdOperationLog)
    {
        return toAjax(sdOperationLogService.insertSdOperationLog(sdOperationLog));
    }

    /**
     * 修改操作日志
     */
//    @PreAuthorize("@ss.hasPermi('system:log:edit')")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改操作日志")
    public AjaxResult edit(@RequestBody SdOperationLog sdOperationLog)
    {
        return toAjax(sdOperationLogService.updateSdOperationLog(sdOperationLog));
    }

    /**
     * 删除操作日志
     */
//    @PreAuthorize("@ss.hasPermi('system:log:remove')")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除操作日志")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdOperationLogService.deleteSdOperationLogByIds(ids));
    }

    /**
     * 新增时 设备执行记录接口 websocket推送
     * @param sdOperationLog
     * @return
     */
    @PostMapping("/operationLog")
    @ApiOperation("新增时 设备执行记录接口 websocket推送")
    public AjaxResult operationLog(@RequestBody SdOperationLog sdOperationLog)
    {
        return AjaxResult.success(sdOperationLogService.operationLog(sdOperationLog));
    }

    /**
     * 应急调度-已执行
     * @param eventId
     * @return
     */
    @GetMapping("/dispatchExecuted")
    @ApiOperation("应急调度-已执行")
    public AjaxResult getDispatchExecuted(String eventId)
    {
        return AjaxResult.success(sdOperationLogService.getDispatchExecuted(eventId));
    }

    /**
     * 导出操作日志
     * @param sdOperationLog
     * @return
     */
    @GetMapping("/export")
    @ApiOperation("导出操作日志")
    public AjaxResult export(SdOperationLog sdOperationLog)
    {

        String excelName = "操作日志";
        String filename = UUID.randomUUID().toString() + "_" + excelName + ".xlsx";
        String downloadPath = RuoYiConfig.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }

        List<SdOperationLog> list = sdOperationLogService.selectSdOperationLogList(sdOperationLog);

        List<SdOperationLogDTO> excelExcel = new ArrayList<>();
        for (SdOperationLog obj : list ) {
            SdOperationLogDTO operationLogDTO = new SdOperationLogDTO();
            operationLogDTO.setTunnelName(obj.getTunnelName().getTunnelName());
            operationLogDTO.setTypeName(obj.getTypeName().getTypeName());
            operationLogDTO.setEqName(obj.getEqName().getEqName());
            operationLogDTO.setStateName(obj.getStateName().getStateName());
            operationLogDTO.setPile(obj.getPile());
            operationLogDTO.setDirection(DeviceDirectionEnum.getValue(obj.getDirection()));
            operationLogDTO.setControlType(DeviceControlTypeEnum.getValue(obj.getControlType()));
            operationLogDTO.setState(obj.getState());
            operationLogDTO.setOperIp(obj.getOperIp());
            operationLogDTO.setCreateTime(obj.getCreateTime());
            excelExcel.add(operationLogDTO);
        }
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        EasyExcel.write(downloadPath, SdOperationLogDTO.class).registerWriteHandler(horizontalCellStyleStrategy).sheet(excelName).doWrite(excelExcel);

        return AjaxResult.success(filename);
    }


}
