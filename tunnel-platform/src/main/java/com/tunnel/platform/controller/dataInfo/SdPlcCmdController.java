package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdPlcCmd;
import com.tunnel.business.service.dataInfo.ISdPlcCmdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * plc 报文Controller
 * 
 * @author zhangweitian
 * @date 2020-08-31
 */
@RestController
@RequestMapping("/cmd")
public class SdPlcCmdController extends BaseController
{
    @Autowired
    private ISdPlcCmdService sdPlcCmdService;

    /**
     * 查询plc 报文列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdPlcCmd sdPlcCmd)
    {
        startPage();
        List<SdPlcCmd> list = sdPlcCmdService.selectSdPlcCmdList(sdPlcCmd);
        return getDataTable(list);
    }

    /**
     * 导出plc 报文列表
     */
    @PreAuthorize("@ss.hasPermi('system:cmd:export')")
    @Log(title = "plc 报文", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdPlcCmd sdPlcCmd)
    {
        List<SdPlcCmd> list = sdPlcCmdService.selectSdPlcCmdList(sdPlcCmd);
        ExcelUtil<SdPlcCmd> util = new ExcelUtil<SdPlcCmd>(SdPlcCmd.class);
        return util.exportExcel(list, "PLC设备报文");
    }

    /**
     * 获取plc 报文详细信息
     */
    @GetMapping(value = "/{cmdId}")
    public AjaxResult getInfo(@PathVariable("cmdId") Long cmdId)
    {
        return AjaxResult.success(sdPlcCmdService.selectSdPlcCmdById(cmdId));
    }

    /**
     * 新增plc 报文
     */
    @Log(title = "plc 报文", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdPlcCmd sdPlcCmd)
    {
        return toAjax(sdPlcCmdService.insertSdPlcCmd(sdPlcCmd));
    }

    /**
     * 修改plc 报文
     */
    @Log(title = "plc 报文", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdPlcCmd sdPlcCmd)
    {
        return toAjax(sdPlcCmdService.updateSdPlcCmd(sdPlcCmd));
    }

    /**
     * 删除plc 报文
     */
    @Log(title = "plc 报文", businessType = BusinessType.DELETE)
	@DeleteMapping("/{cmdIds}")
    public AjaxResult remove(@PathVariable Long[] cmdIds)
    {
        return toAjax(sdPlcCmdService.deleteSdPlcCmdByIds(cmdIds));
    }
}
