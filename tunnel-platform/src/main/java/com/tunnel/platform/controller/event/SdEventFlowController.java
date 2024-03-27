package com.tunnel.platform.controller.event;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.event.SdEventFlow;
import com.tunnel.business.service.event.ISdEventFlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "事件处理流程Controller")
@ApiSupport(order = 16)
public class SdEventFlowController extends BaseController
{
    @Autowired
    private ISdEventFlowService sdEventFlowService;

    /**
     * 查询事件处理流程列表
     */
    @ApiOperation("查询事件处理流程列表")
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
    @ApiOperation("获取事件处理流程详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdEventFlowService.selectSdEventFlowById(id));
    }

    /**
     * 新增事件处理流程
     */
    @ApiOperation("新增事件处理流程")
    @Log(title = "事件处理流程", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdEventFlow sdEventFlow)
    {
        return toAjax(sdEventFlowService.insertSdEventFlow(sdEventFlow));
    }

    /**
     * 用户确认事件保存记录
     * @param id
     * @return
     */
    @ApiOperation("用户确认事件保存记录")
    @GetMapping("/userConfirm/{id}")
    public AjaxResult saveUserConfirmFlow(@PathVariable("id") String id){
        return toAjax(sdEventFlowService.saveUserConfirmFlow(id));
    }

    /**
     * 用户结束事件保存记录
     * @param id
     * @return
     */
    @ApiOperation("用户结束事件保存记录")
    @GetMapping("/userEventEnded/{id}")
    public AjaxResult saveUserEventEnded(@PathVariable("id") String id){
        return toAjax(sdEventFlowService.saveUserEventEndedFlow(id));
    }

    /**
     * 修改事件处理流程
     */
    @ApiOperation("修改事件处理流程")
    @Log(title = "事件处理流程", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdEventFlow sdEventFlow)
    {
        return toAjax(sdEventFlowService.updateSdEventFlow(sdEventFlow));
    }

    /**
     * 删除事件处理流程
     */
    @ApiOperation("删除事件处理流程")
    @Log(title = "事件处理流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdEventFlowService.deleteSdEventFlowByIds(ids));
    }
}