package com.tunnel.platform.controller.logRecord;

import com.tunnel.platform.controller.informationBoard.AjaxResultb;
import com.tunnel.platform.domain.logRecord.SdOperationLog;
import com.tunnel.platform.service.logRecord.ISdOperationLogService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 操作日志Controller
 * 
 * @author yanghousheng
 * @date 2020-09-03
 */
@RestController
@RequestMapping("/log")
public class SdOperationLogController extends BaseController
{
    @Autowired
    private ISdOperationLogService sdOperationLogService;

    /**
     * 查询操作日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdOperationLog sdOperationLog)
    {
        startPage();
        List<SdOperationLog> list = sdOperationLogService.selectSdOperationLogList(sdOperationLog);
        return getDataTable(list);
    }

    /**
     * 导出操作日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:log:export')")
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdOperationLog sdOperationLog) throws IOException
    {
        List<SdOperationLog> list = sdOperationLogService.selectSdOperationLogList(sdOperationLog);
        ExcelUtil<SdOperationLog> util = new ExcelUtil<SdOperationLog>(SdOperationLog.class);
        util.exportExcel(list, "操作日志列表");
    }

    /**
     * 获取操作日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:log:query')")
    @GetMapping(value = "/{id}")
    public AjaxResultb getInfo(@PathVariable("id") Long id)
    {
        return AjaxResultb.success(sdOperationLogService.selectSdOperationLogById(id));
    }

    /**
     * 新增操作日志
     */
    @PreAuthorize("@ss.hasPermi('system:log:add')")
    @Log(title = "操作日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdOperationLog sdOperationLog)
    {
        return toAjax(sdOperationLogService.insertSdOperationLog(sdOperationLog));
    }

    /**
     * 修改操作日志
     */
    @PreAuthorize("@ss.hasPermi('system:log:edit')")
    @Log(title = "操作日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdOperationLog sdOperationLog)
    {
        return toAjax(sdOperationLogService.updateSdOperationLog(sdOperationLog));
    }

    /**
     * 删除操作日志
     */
    @PreAuthorize("@ss.hasPermi('system:log:remove')")
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdOperationLogService.deleteSdOperationLogByIds(ids));
    }
}