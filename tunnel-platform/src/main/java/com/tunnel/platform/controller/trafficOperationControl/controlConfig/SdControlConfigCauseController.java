package com.tunnel.platform.controller.trafficOperationControl.controlConfig;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.trafficOperationControl.controlConfig.SdControlConfigCause;
import com.tunnel.business.service.trafficOperationControl.controlConfig.ISdControlConfigCauseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管控等级配置措施-管控原因Controller
 *
 * @author ruoyi
 * @date 2022-02-15
 */
@RestController
@RequestMapping("/controlConfig/cause")
@Api(tags = "管控等级配置措施-管控原因")
@ApiSupport(order = 16)
public class SdControlConfigCauseController extends BaseController
{
    @Autowired
    private ISdControlConfigCauseService sdControlConfigCauseService;

    /**
     * 查询管控等级配置措施-管控原因列表
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:cause:list')")
    @GetMapping("/list")
    @ApiOperation("查询管控等级配置措施-管控原因列表")
    public TableDataInfo list(SdControlConfigCause sdControlConfigCause)
    {
        startPage();
        List<SdControlConfigCause> list = sdControlConfigCauseService.selectSdControlConfigCauseList(sdControlConfigCause);
        return getDataTable(list);
    }

    /**
     * 导出管控等级配置措施-管控原因列表
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:cause:export')")
    @Log(title = "管控等级配置措施-管控原因", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation("出管控等级配置措施-管控原因列表")
    public AjaxResult export(SdControlConfigCause sdControlConfigCause)
    {
        List<SdControlConfigCause> list = sdControlConfigCauseService.selectSdControlConfigCauseList(sdControlConfigCause);
        ExcelUtil<SdControlConfigCause> util = new ExcelUtil<SdControlConfigCause>(SdControlConfigCause.class);
        return util.exportExcel(list, "管控等级配置措施-管控原因数据");
    }

    /**
     * 获取管控等级配置措施-管控原因详细信息
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:cause:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取管控等级配置措施-管控原因详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdControlConfigCauseService.selectSdControlConfigCauseById(id));
    }

    /**
     * 新增管控等级配置措施-管控原因
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:cause:add')")
    @Log(title = "管控等级配置措施-管控原因", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增管控等级配置措施-管控原因")
    public AjaxResult add(@RequestBody SdControlConfigCause sdControlConfigCause)
    {
        return toAjax(sdControlConfigCauseService.insertSdControlConfigCause(sdControlConfigCause));
    }

    /**
     * 修改管控等级配置措施-管控原因
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:cause:edit')")
    @Log(title = "管控等级配置措施-管控原因", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改管控等级配置措施-管控原因")
    public AjaxResult edit(@RequestBody SdControlConfigCause sdControlConfigCause)
    {
        return toAjax(sdControlConfigCauseService.updateSdControlConfigCause(sdControlConfigCause));
    }

    /**
     * 删除管控等级配置措施-管控原因
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:cause:remove')")
    @Log(title = "管控等级配置措施-管控原因", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("除管控等级配置措施-管控原因")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdControlConfigCauseService.deleteSdControlConfigCauseByIds(ids));
    }
}
