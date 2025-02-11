package com.tunnel.platform.controller.dataInfo;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.event.SdTunnelSubarea;
import com.tunnel.business.service.dataInfo.ISdTunnelSubareaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 隧道分区Controller
 *
 * @author ruoyi
 * @date 2022-08-25
 */
@RestController
@RequestMapping("/tunnel/subarea")
@Api(tags = "隧道分区")
@ApiSupport(order = 16)
public class SdTunnelSubareaController extends BaseController
{
    @Autowired
    private ISdTunnelSubareaService sdTunnelSubareaService;

    /**
     * 查询隧道分区列表
     */
    @PreAuthorize("@ss.hasPermi('tunnel:subarea:list')")
    @GetMapping("/list")
    @ApiOperation("查询隧道分区列表")
    public TableDataInfo list(SdTunnelSubarea sdTunnelSubarea)
    {
        startPage();
        List<SdTunnelSubarea> list = sdTunnelSubareaService.selectSdTunnelSubareaList(sdTunnelSubarea);
        return getDataTable(list);
    }

    /**
     * 导出隧道分区列表
     */
    @PreAuthorize("@ss.hasPermi('tunnel:subarea:export')")
    @Log(title = "隧道分区", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation("导出隧道分区列表")
    public AjaxResult export(SdTunnelSubarea sdTunnelSubarea) {
        List<SdTunnelSubarea> list = sdTunnelSubareaService.selectSdTunnelSubareaList(sdTunnelSubarea);
        ExcelUtil<SdTunnelSubarea> util = new ExcelUtil<SdTunnelSubarea>(SdTunnelSubarea.class);
        return util.exportExcel(list, "隧道分区数据");
    }

    /**
     * 获取隧道分区详细信息
     */
    @PreAuthorize("@ss.hasPermi('tunnel:subarea:query')")
    @GetMapping(value = "/{sId}")
    @ApiOperation("获取隧道分区详细信息")
    public AjaxResult getInfo(@PathVariable("sId") Long sId)
    {
        return AjaxResult.success(sdTunnelSubareaService.selectSdTunnelSubareaBySId(sId));
    }

    /**
     * 新增隧道分区
     */
    @PreAuthorize("@ss.hasPermi('tunnel:subarea:add')")
    @Log(title = "隧道分区", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增隧道分区")
    public AjaxResult add(@RequestBody SdTunnelSubarea sdTunnelSubarea)
    {
        return toAjax(sdTunnelSubareaService.insertSdTunnelSubarea(sdTunnelSubarea));
    }

    /**
     * 修改隧道分区
     */
    @PreAuthorize("@ss.hasPermi('tunnel:subarea:edit')")
    @Log(title = "隧道分区", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改隧道分区")
    public AjaxResult edit(@RequestBody SdTunnelSubarea sdTunnelSubarea)
    {
        return toAjax(sdTunnelSubareaService.updateSdTunnelSubarea(sdTunnelSubarea));
    }

    /**
     * 删除隧道分区
     */
    @PreAuthorize("@ss.hasPermi('tunnel:subarea:remove')")
    @Log(title = "隧道分区", businessType = BusinessType.DELETE)
	@DeleteMapping("/{sIds}")
    @ApiOperation("删除隧道分区")
    public AjaxResult remove(@PathVariable Long[] sIds)
    {
        return toAjax(sdTunnelSubareaService.deleteSdTunnelSubareaBySIds(sIds));
    }

    /**
     * 根据隧道Id查询隧道分区
     * @param tunnelId
     * @return
     */
    @GetMapping("/getSubareaByTunnelId")
    @ApiOperation("根据隧道Id查询隧道分区")
    public AjaxResult getSdTunnelSubareaByTunnelId(@RequestParam("tunnelId") String tunnelId,@RequestParam("eventTypeId") Long eventTypeId)
    {
        return AjaxResult.success(sdTunnelSubareaService.selectSdTunnelSubareaByTunnelId(tunnelId,eventTypeId));
    }


}
