package com.tunnel.platform.controller.emeDrill;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.emeDrill.SdEmergencyDrill;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.emeDrill.ISdEmergencyDrillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 应急演练Controller
 * 
 * @author ruoyi
 * @date 2021-11-22
 */
@RestController
@RequestMapping("/business/emeDrill")
public class SdEmergencyDrillController extends BaseController
{
    @Autowired
    private ISdEmergencyDrillService sdEmergencyDrillService;
    @Autowired
    private ISdTunnelsService sdTunnelsService;
    /**
     * 查询应急演练列表
     */
    @PreAuthorize("@ss.hasPermi('business:emeDrill:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdEmergencyDrill sdEmergencyDrill)
    {
        startPage();
        List<SdEmergencyDrill> list = sdEmergencyDrillService.selectSdEmergencyDrillList(sdEmergencyDrill);
        SdTunnels sdTunnels=new SdTunnels();
        sdTunnels.setPoll(0L);
        List<SdTunnels> sdTunnelsList = sdTunnelsService.selectSdTunnelsList(sdTunnels);
        list.forEach(e->{
            List<SdTunnels>listObj=sdTunnelsList.stream().filter(a->a.getTunnelId().equals(e.getTunnelId())).collect(Collectors.toList());
            if (listObj.size()>0){
                e.setTunnelName(listObj.get(0).getTunnelName());
            }
        });
        return getDataTable(list);
    }

    /**
     * 导出应急演练列表
     */
    @PreAuthorize("@ss.hasPermi('business:emeDrill:export')")
    @Log(title = "应急演练", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdEmergencyDrill sdEmergencyDrill)
    {
        List<SdEmergencyDrill> list = sdEmergencyDrillService.selectSdEmergencyDrillList(sdEmergencyDrill);
        SdTunnels sdTunnels=new SdTunnels();
        sdTunnels.setPoll(0L);
        List<SdTunnels> sdTunnelsList = sdTunnelsService.selectSdTunnelsList(sdTunnels);
        list.forEach(e->{
            List<SdTunnels>listObj=sdTunnelsList.stream().filter(a->a.getTunnelId().equals(e.getTunnelId())).collect(Collectors.toList());
            if (listObj.size()>0){
                e.setTunnelName(listObj.get(0).getTunnelName());
            }
        });
        ExcelUtil<SdEmergencyDrill> util = new ExcelUtil<SdEmergencyDrill>(SdEmergencyDrill.class);
        return util.exportExcel(list, "应急演练");
    }

    /**
     * 获取应急演练详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:emeDrill:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdEmergencyDrillService.selectSdEmergencyDrillById(id));
    }

    /**
     * 新增应急演练
     */
    @PreAuthorize("@ss.hasPermi('business:emeDrill:add')")
    @Log(title = "应急演练", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdEmergencyDrill sdEmergencyDrill)
    {
        return toAjax(sdEmergencyDrillService.insertSdEmergencyDrill(sdEmergencyDrill));
    }

    /**
     * 修改应急演练
     */
    @PreAuthorize("@ss.hasPermi('business:emeDrill:edit')")
    @Log(title = "应急演练", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdEmergencyDrill sdEmergencyDrill)
    {
        return toAjax(sdEmergencyDrillService.updateSdEmergencyDrill(sdEmergencyDrill));
    }

    /**
     * 删除应急演练
     */
    @PreAuthorize("@ss.hasPermi('business:emeDrill:remove')")
    @Log(title = "应急演练", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdEmergencyDrillService.deleteSdEmergencyDrillByIds(ids));
    }
}
