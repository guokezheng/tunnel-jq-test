package com.tunnel.platform.controller.event;

import java.util.List;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.tunnel.business.domain.event.SdJoinTypeFlow;
import com.tunnel.business.domain.event.SdPlanFlow;
import com.tunnel.business.service.event.ISdJoinTypeFlowService;
import io.swagger.annotations.Api;
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
 * 事件类型预案流程关联Controller
 * 
 * @author ruoyi
 * @date 2022-12-10
 */
@RestController
@RequestMapping("/system/flow")
@Api(tags = "事件类型预案流程关联Controller")
@ApiSupport(order = 16)
public class SdJoinTypeFlowController extends BaseController
{
    @Autowired
    private ISdJoinTypeFlowService sdJoinTypeFlowService;

    /**
     * 查询事件类型预案流程关联列表
     */
    @ApiOperation("查询事件类型预案流程关联列表")
    @PreAuthorize("@ss.hasPermi('system:flow:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdJoinTypeFlow sdJoinTypeFlow)
    {
        startPage();
        List<SdJoinTypeFlow> list = sdJoinTypeFlowService.selectSdJoinTypeFlowList(sdJoinTypeFlow);
        return getDataTable(list);
    }

    /**
     * 导出事件类型预案流程关联列表
     */
    @ApiOperation("导出事件类型预案流程关联列表")
    @PreAuthorize("@ss.hasPermi('system:flow:export')")
    @Log(title = "事件类型预案流程关联", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdJoinTypeFlow sdJoinTypeFlow)
    {
        List<SdJoinTypeFlow> list = sdJoinTypeFlowService.selectSdJoinTypeFlowList(sdJoinTypeFlow);
        ExcelUtil<SdJoinTypeFlow> util = new ExcelUtil<SdJoinTypeFlow>(SdJoinTypeFlow.class);
        return util.exportExcel(list, "预案流程");
    }

    /**
     * 获取事件类型预案流程关联详细信息
     */
    @ApiOperation("获取事件类型预案流程关联详细信息")
    @PreAuthorize("@ss.hasPermi('system:flow:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdJoinTypeFlowService.selectSdJoinTypeFlowById(id));
    }

    /**
     * 新增事件类型预案流程关联
     */
    @ApiOperation("新增事件类型预案流程关联")
    @PreAuthorize("@ss.hasPermi('system:flow:add')")
    @Log(title = "事件类型预案流程关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdJoinTypeFlow sdJoinTypeFlow)
    {
        return toAjax(sdJoinTypeFlowService.insertSdJoinTypeFlow(sdJoinTypeFlow));
    }

    /**
     * 修改事件类型预案流程关联
     */
    @ApiOperation("修改事件类型预案流程关联")
    @PreAuthorize("@ss.hasPermi('system:flow:edit')")
    @Log(title = "事件类型预案流程关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdJoinTypeFlow sdJoinTypeFlow)
    {
        return toAjax(sdJoinTypeFlowService.updateSdJoinTypeFlow(sdJoinTypeFlow));
    }

    /**
     * 删除事件类型预案流程关联
     */
    @ApiOperation("删除事件类型预案流程关联")
    @PreAuthorize("@ss.hasPermi('system:flow:remove')")
    @Log(title = "事件类型预案流程关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdJoinTypeFlowService.deleteSdJoinTypeFlowByIds(ids));
    }

    /**
     * 获取预案流程树
     * @return
     */
    @ApiOperation("获取预案流程树")
    @GetMapping("/getTypeFlowList")
    public List<SdPlanFlow> getTypeFlowList(){
        return sdJoinTypeFlowService.getTypeFlowList();
    }

    /**
     * 校验该事件类型预案流程是否存在
     * @param eventTypeId
     * @return
     */
    @ApiOperation("校验该事件类型预案流程是否存在")
    @GetMapping("/checkData")
    public AjaxResult checkData(String eventTypeId){
        return sdJoinTypeFlowService.checkData(eventTypeId);
    }
}
