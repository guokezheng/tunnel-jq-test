package com.tunnel.platform.controller.event;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.event.SdPlanType;
import com.tunnel.business.service.event.ISdPlanTypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预案类型Controller
 * 
 * @author gongfanfei
 * @date 2020-08-25
 */
@RestController
@RequestMapping("/planType")
public class SdPlanTypeController extends BaseController
{
    @Autowired
    private ISdPlanTypeService sdPlanTypeService;

    /**
     * 查询预案类型列表
     */
    @ApiOperation("查询预案类型列表")
    @GetMapping("/list")
    public TableDataInfo list(SdPlanType sdPlanType)
    {
        startPage();
        List<SdPlanType> list = sdPlanTypeService.selectSdPlanTypeList(sdPlanType);
        return getDataTable(list);
    }

    /**
     * 导出预案类型列表
     */
    /*@PreAuthorize(hasPermi = "business:planType:export")
    @Log(title = "预案类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdPlanType sdPlanType) throws IOException
    {
        List<SdPlanType> list = sdPlanTypeService.selectSdPlanTypeList(sdPlanType);
        ExcelUtil<SdPlanType> util = new ExcelUtil<SdPlanType>(SdPlanType.class);
        util.exportExcel(response, list, "planType");
    }*/

    /**
     * 获取预案类型详细信息
     */
    @ApiOperation("获取预案类型详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdPlanTypeService.selectSdPlanTypeById(id));
    }

    /**
     * 新增预案类型
     */
    @ApiOperation("新增预案类型")
    @Log(title = "预案类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdPlanType sdPlanType)
    {
        return toAjax(sdPlanTypeService.insertSdPlanType(sdPlanType));
    }

    /**
     * 修改预案类型
     */
    @ApiOperation("修改预案类型")
    @Log(title = "预案类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdPlanType sdPlanType)
    {
        return toAjax(sdPlanTypeService.updateSdPlanType(sdPlanType));
    }

    /**
     * 删除预案类型
     */
    @ApiOperation("删除预案类型")
    @Log(title = "预案类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdPlanTypeService.deleteSdPlanTypeByIds(ids));
    }
}