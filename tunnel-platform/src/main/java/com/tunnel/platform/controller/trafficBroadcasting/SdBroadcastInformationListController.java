package com.tunnel.platform.controller.trafficBroadcasting;

import com.tunnel.platform.domain.trafficBroadcasting.SdBroadcastInformationList;
import com.tunnel.platform.service.trafficBroadcasting.ISdBroadcastInformationListService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 广播信息列Controller
 * 
 * @author ruoyi
 * @date 2021-11-24
 */
@RestController
@RequestMapping("/broadcastInformation/list")
public class SdBroadcastInformationListController extends BaseController
{
    @Autowired
    private ISdBroadcastInformationListService sdBroadcastInformationListService;

    /**
     * 查询广播信息列列表
     */
    @PreAuthorize("@ss.hasPermi('broadcastInformation:list:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdBroadcastInformationList sdBroadcastInformationList)
    {
        startPage();
        List<SdBroadcastInformationList> list = sdBroadcastInformationListService.selectSdBroadcastInformationListList(sdBroadcastInformationList);
        return getDataTable(list);
    }

    /**
     * 导出广播信息列列表
     */
    @PreAuthorize("@ss.hasPermi('broadcastInformation:list:export')")
    @Log(title = "广播信息列", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdBroadcastInformationList sdBroadcastInformationList)
    {
        List<SdBroadcastInformationList> list = sdBroadcastInformationListService.selectSdBroadcastInformationListList(sdBroadcastInformationList);
        ExcelUtil<SdBroadcastInformationList> util = new ExcelUtil<SdBroadcastInformationList>(SdBroadcastInformationList.class);
        return util.exportExcel(list, "广播信息列表");
    }

    /**
     * 获取广播信息列详细信息
     */
    @PreAuthorize("@ss.hasPermi('broadcastInformation:list:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdBroadcastInformationListService.selectSdBroadcastInformationListById(id));
    }

    /**
     * 新增广播信息列
     */
    @PreAuthorize("@ss.hasPermi('broadcastInformation:list:add')")
    @Log(title = "广播信息列", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdBroadcastInformationList sdBroadcastInformationList)
    {
        return toAjax(sdBroadcastInformationListService.insertSdBroadcastInformationList(sdBroadcastInformationList));
    }

    /**
     * 修改广播信息列
     */
    @PreAuthorize("@ss.hasPermi('broadcastInformation:list:edit')")
    @Log(title = "广播信息列", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdBroadcastInformationList sdBroadcastInformationList)
    {
        return toAjax(sdBroadcastInformationListService.updateSdBroadcastInformationList(sdBroadcastInformationList));
    }

    /**
     * 删除广播信息列
     */
    @PreAuthorize("@ss.hasPermi('broadcastInformation:list:remove')")
    @Log(title = "广播信息列", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdBroadcastInformationListService.deleteSdBroadcastInformationListByIds(ids));
    }
}
