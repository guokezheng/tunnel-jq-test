package com.tunnel.platform.controller.dataInfo;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.event.SdTunnelSubarea;
import com.tunnel.business.service.dataInfo.ISdTunnelSubareaService;
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
    public AjaxResult export(SdTunnelSubarea sdTunnelSubarea) {
        List<SdTunnelSubarea> list = sdTunnelSubareaService.selectSdTunnelSubareaList(sdTunnelSubarea);
        for(int i = 0;i<list.size();i++){
            SdTunnelSubarea subarea = list.get(i);
            String direction = subarea.getDirection();
            if(StrUtil.isBlank(direction))
                direction = "0";
            direction = direction.equals("1")?"上行":"下行";
            subarea.setDirection(direction);
            list.set(i,subarea);
        }
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

    /**
     * 根据隧道Id查询隧道分区
     * @param tunnelId
     * @return
     */
    @GetMapping("/getSubareaByTunnelId")
    public AjaxResult getSdTunnelSubareaByTunnelId(@RequestParam("tunnelId") String tunnelId,@RequestParam("eventTypeId") String eventTypeId)
    {
        return AjaxResult.success(sdTunnelSubareaService.selectSdTunnelSubareaByTunnelId(tunnelId,eventTypeId));
    }


}
