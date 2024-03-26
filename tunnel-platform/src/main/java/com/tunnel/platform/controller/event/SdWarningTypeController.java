package com.tunnel.platform.controller.event;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.event.SdWarningType;
import com.tunnel.business.service.event.ISdWarningTypeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预警类型Controller
 *
 * @author gongfanfei
 * @date 2020-12-21
 */
@RestController
@RequestMapping("/warningType")
public class SdWarningTypeController extends BaseController
{
    @Autowired
    private ISdWarningTypeService sdWarningTypeService;

    /**
     * 查询预警类型列表
     */
    @ApiOperation("查询预警类型列表")
    @GetMapping("/list")
    public TableDataInfo list(SdWarningType sdWarningType)
    {
        startPage();
        List<SdWarningType> list = sdWarningTypeService.selectSdWarningTypeList(sdWarningType);
        return getDataTable(list);
    }

    /**
     * 导出预警类型列表
     */
    /*@Log(title = "预警类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdWarningType sdWarningType) throws IOException
    {
        List<SdWarningType> list = sdWarningTypeService.selectSdWarningTypeList(sdWarningType);
        ExcelUtil<SdWarningType> util = new ExcelUtil<SdWarningType>(SdWarningType.class);
        util.exportExcel(response, list, "type");
    }*/

    /**
     * 获取预警类型详细信息
     */
    @ApiOperation("获取预警类型详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdWarningTypeService.selectSdWarningTypeById(id));
    }

    /**
     * 新增预警类型
     */
    @ApiOperation("新增预警类型")
    @Log(title = "预警类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdWarningType sdWarningType)
    {
        if(!sdWarningTypeService.checkSdWarningTypeNameUnique(sdWarningType.getTypeName())) {
            return AjaxResult.error("预警类型不能重复");
        }
        return toAjax(sdWarningTypeService.insertSdWarningType(sdWarningType));
    }

    /**
     * 修改预警类型
     */
    @ApiOperation("修改预警类型")
    @Log(title = "预警类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdWarningType sdWarningType)
    {
        return toAjax(sdWarningTypeService.updateSdWarningType(sdWarningType));
    }

    /**
     * 删除预警类型
     */
    @ApiOperation("删除预警类型")
    @Log(title = "预警类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdWarningTypeService.deleteSdWarningTypeByIds(ids));
    }
}
