package com.tunnel.platform.controller.trafficOperationControl.trafficOperationStatus;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.trafficOperationControl.trafficOperationStatus.SdTrafficFlowState;
import com.tunnel.business.service.trafficOperationControl.trafficOperationStatus.ISdTrafficFlowStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 【交通流状态记录】Controller
 *
 * @author ruoyi
 * @date 2022-02-14
 */
@RestController
@RequestMapping("/trafficFlow/state")
public class SdTrafficFlowStateController extends BaseController
{
    @Autowired
    private ISdTrafficFlowStateService sdTrafficFlowStateService;

    /**
     * 查询【交通流状态记录】列表
     */
    @PreAuthorize("@ss.hasPermi('trafficFlow:state:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdTrafficFlowState sdTrafficFlowState)
    {
        startPage();
        List<SdTrafficFlowState> list = sdTrafficFlowStateService.selectSdTrafficFlowStateList(sdTrafficFlowState);
        return getDataTable(list);
    }

    /**
     * 导出【交通流状态记录】列表
     */
    @PreAuthorize("@ss.hasPermi('trafficFlow:state:export')")
    @Log(title = "【交通流状态记录】", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdTrafficFlowState sdTrafficFlowState)
    {
        List<SdTrafficFlowState> list = sdTrafficFlowStateService.selectSdTrafficFlowStateList(sdTrafficFlowState);
        ExcelUtil<SdTrafficFlowState> util = new ExcelUtil<SdTrafficFlowState>(SdTrafficFlowState.class);
        return util.exportExcel(list, "【交通流状态记录】数据");
    }

    /**
     * 获取【交通流状态记录】详细信息
     */
    @PreAuthorize("@ss.hasPermi('trafficFlow:state:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdTrafficFlowStateService.selectSdTrafficFlowStateById(id));
    }

    /**
     * 新增【交通流状态记录】
     */
    @PreAuthorize("@ss.hasPermi('trafficFlow:state:add')")
    @Log(title = "【交通流状态记录】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdTrafficFlowState sdTrafficFlowState)
    {
        return toAjax(sdTrafficFlowStateService.insertSdTrafficFlowState(sdTrafficFlowState));
    }

    /**
     * 修改【交通流状态记录】
     */
    @PreAuthorize("@ss.hasPermi('trafficFlow:state:edit')")
    @Log(title = "【交通流状态记录】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdTrafficFlowState sdTrafficFlowState)
    {
        return toAjax(sdTrafficFlowStateService.updateSdTrafficFlowState(sdTrafficFlowState));
    }

    /**
     * 删除【交通流状态记录】
     */
    @PreAuthorize("@ss.hasPermi('trafficFlow:state:remove')")
    @Log(title = "【交通流状态记录】", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdTrafficFlowStateService.deleteSdTrafficFlowStateByIds(ids));
    }


    /**
     * 获取交通数据处理与分析页面-图表数据
     * 展示各隧道最近24小时内各个种类车型的车流量和平均速度的趋势图
     * @param tunnelId 隧道id
     * @return
     */
    @RequestMapping("/getChartData")
    public AjaxResult getChartData(String tunnelId){

        return AjaxResult.success(sdTrafficFlowStateService.getChartData(tunnelId));
    }
}
