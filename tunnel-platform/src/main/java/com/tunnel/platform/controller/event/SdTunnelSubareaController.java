package com.tunnel.platform.controller.event;

import java.util.List;

import com.tunnel.platform.domain.event.SdReservePlan;
import com.tunnel.platform.domain.event.SdTunnelSubarea;
import com.tunnel.platform.service.event.ISdTunnelSubareaService;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 隧道分区Controller
 *
 * @author ruoyi
 * @date 2022-08-25
 */
@RestController
@RequestMapping("/tunnel/subarea")
public class SdTunnelSubareaController extends BaseController
{
    @Autowired
    private ISdTunnelSubareaService sdTunnelSubareaService;

    /**
     * 查询隧道分区列表
     */
    @PreAuthorize("@ss.hasPermi('tunnel:subarea:list')")
    @GetMapping("/list")
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
    public AjaxResult export(SdTunnelSubarea sdTunnelSubarea)
    {
        List<SdTunnelSubarea> list = sdTunnelSubareaService.selectSdTunnelSubareaList(sdTunnelSubarea);
        ExcelUtil<SdTunnelSubarea> util = new ExcelUtil<SdTunnelSubarea>(SdTunnelSubarea.class);
        return util.exportExcel(list, "隧道分区数据");
    }

    /**
     * 获取隧道分区详细信息
     */
    @PreAuthorize("@ss.hasPermi('tunnel:subarea:query')")
    @GetMapping(value = "/{sId}")
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
    public AjaxResult remove(@PathVariable Long[] sIds)
    {
        return toAjax(sdTunnelSubareaService.deleteSdTunnelSubareaBySIds(sIds));
    }


}
