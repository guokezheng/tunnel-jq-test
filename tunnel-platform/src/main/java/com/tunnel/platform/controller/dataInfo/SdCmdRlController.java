package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.dataInfo.SdCmdRl;
import com.tunnel.business.service.dataInfo.ISdCmdRlService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * plc 报文关联设备Controller
 * 
 * @author zhangweitian
 * @date 2020-09-02
 */
@RestController
@RequestMapping("/rl")
public class SdCmdRlController extends BaseController
{
    @Autowired
    private ISdCmdRlService sdCmdRlService;

    /**
     * 查询plc 报文关联设备列表
     */
    @ApiOperation("查询plc 报文关联设备列表")
    @GetMapping("/list")
    public TableDataInfo list(SdCmdRl sdCmdRl)
    {
        startPage();
        List<SdCmdRl> list = sdCmdRlService.selectSdCmdRlList(sdCmdRl);
        return getDataTable(list);
    }

    /**
     * 导出plc 报文关联设备列表
     */
   /* @Log(title = "plc 报文关联设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdCmdRl sdCmdRl) throws IOException
    {
        List<SdCmdRl> list = sdCmdRlService.selectSdCmdRlList(sdCmdRl);
        ExcelUtil<SdCmdRl> util = new ExcelUtil<SdCmdRl>(SdCmdRl.class);
        util.exportExcel(response, list, "rl");
    }*/

    /**
     * 获取plc 报文关联设备详细信息
     */
    @ApiOperation("获取plc 报文关联设备详细信息")
    @GetMapping(value = "/{cmdRlId}")
    public AjaxResult getInfo(@PathVariable("cmdRlId") Long cmdRlId)
    {
        return AjaxResult.success(sdCmdRlService.selectSdCmdRlById(cmdRlId));
    }

    /**
     * 新增plc 报文关联设备
     */
    @ApiOperation("新增plc 报文关联设备")
    @Log(title = "plc 报文关联设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdCmdRl sdCmdRl)
    {
        return toAjax(sdCmdRlService.insertSdCmdRl(sdCmdRl));
    }

    /**
     * 修改plc 报文关联设备
     */
    @ApiOperation("修改plc 报文关联设备")
    @Log(title = "plc 报文关联设备", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdCmdRl sdCmdRl)
    {
        return toAjax(sdCmdRlService.updateSdCmdRl(sdCmdRl));
    }

    /**
     * 删除plc 报文关联设备
     */
    @ApiOperation("删除plc 报文关联设备")
    @Log(title = "plc 报文关联设备", businessType = BusinessType.DELETE)
	@DeleteMapping("/{cmdRlIds}")
    public AjaxResult remove(@PathVariable Long[] cmdRlIds)
    {
        return toAjax(sdCmdRlService.deleteSdCmdRlByIds(cmdRlIds));
    }
}