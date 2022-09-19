package com.tunnel.platform.controller.dataInfo;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.dataInfo.SdEmergencyResponse;
import com.tunnel.business.service.dataInfo.ISdEmergencyResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应急处置Controller
 * 
 * @author zhangweitian
 * @date 2020-11-18
 */
@RestController
@RequestMapping("/response")
public class SdEmergencyResponseController extends BaseController
{
    @Autowired
    private ISdEmergencyResponseService sdEmergencyResponseService;

    /**
     * 查询应急处置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdEmergencyResponse sdEmergencyResponse)
    {
        startPage();
        List<SdEmergencyResponse> list = sdEmergencyResponseService.selectSdEmergencyResponseList(sdEmergencyResponse);
        return getDataTable(list);
    }

    /**
     * 导出应急处置列表
     */
 /*   @PreAuthorize(hasPermi = "system:response:export")
    @Log(title = "应急处置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdEmergencyResponse sdEmergencyResponse) throws IOException
    {
        List<SdEmergencyResponse> list = sdEmergencyResponseService.selectSdEmergencyResponseList(sdEmergencyResponse);
        ExcelUtil<SdEmergencyResponse> util = new ExcelUtil<SdEmergencyResponse>(SdEmergencyResponse.class);
        util.exportExcel(response, list, "response");
    }*/

    /**
     * 获取应急处置详细信息
     */
    @GetMapping(value = "/{responseId}")
    public AjaxResult getInfo(@PathVariable("responseId") Long responseId)
    {
        return AjaxResult.success(sdEmergencyResponseService.selectSdEmergencyResponseById(responseId));
    }

    /**
     * 新增应急处置
     */
    @Log(title = "应急处置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdEmergencyResponse sdEmergencyResponse)
    {
        return toAjax(sdEmergencyResponseService.insertSdEmergencyResponse(sdEmergencyResponse));
    }

    /**
     * 修改应急处置
     */
    @Log(title = "应急处置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdEmergencyResponse sdEmergencyResponse)
    {
        return toAjax(sdEmergencyResponseService.updateSdEmergencyResponse(sdEmergencyResponse));
    }

    /**
     * 删除应急处置
     */
    @Log(title = "应急处置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{responseIds}")
    public AjaxResult remove(@PathVariable Long[] responseIds)
    {
        return toAjax(sdEmergencyResponseService.deleteSdEmergencyResponseByIds(responseIds));
    }
}
