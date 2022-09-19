package com.tunnel.platform.controller.event;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.event.SdEventFlow;
import com.tunnel.business.service.event.ISdEventFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 事件处理流程Controller
 * 
 * @author gongfanfei
 * @date 2020-09-17
 */
@RestController
@RequestMapping("/eventFlow")
public class SdEventFlowController extends BaseController
{
    @Autowired
    private ISdEventFlowService sdEventFlowService;

    /**
     * 查询事件处理流程列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdEventFlow sdEventFlow)
    {
        startPage();
        List<SdEventFlow> list = sdEventFlowService.selectSdEventFlowList(sdEventFlow);
        return getDataTable(list);
    }

    /**
     * 导出事件处理流程列表
     */
    /*@PreAuthorize(hasPermi = "business:eventFlow:export")
    @Log(title = "事件处理流程", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdEventFlow sdEventFlow) throws IOException
    {
        List<SdEventFlow> list = sdEventFlowService.selectSdEventFlowList(sdEventFlow);
        ExcelUtil<SdEventFlow> util = new ExcelUtil<SdEventFlow>(SdEventFlow.class);
        util.exportExcel(response, list, "eventFlow");
    }*/

    /**
     * 获取事件处理流程详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdEventFlowService.selectSdEventFlowById(id));
    }

    /**
     * 新增事件处理流程
     */
    @Log(title = "事件处理流程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdEventFlow sdEventFlow)
    {
        return toAjax(sdEventFlowService.insertSdEventFlow(sdEventFlow));
    }

    /**
     * 修改事件处理流程
     */
    @Log(title = "事件处理流程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdEventFlow sdEventFlow)
    {
        return toAjax(sdEventFlowService.updateSdEventFlow(sdEventFlow));
    }

    /**
     * 删除事件处理流程
     */
    @Log(title = "事件处理流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdEventFlowService.deleteSdEventFlowByIds(ids));
    }
}