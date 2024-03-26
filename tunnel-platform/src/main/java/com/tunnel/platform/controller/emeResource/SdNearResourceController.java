package com.tunnel.platform.controller.emeResource;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.emeResource.SdNearResource;
import com.tunnel.business.service.emeResource.ISdNearResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 周边资源Controller
 * 
 * @author ruoyi
 * @date 2021-11-22
 */
@RestController
@RequestMapping("/business/emeResource")
@Api(tags = "周边资源")
public class SdNearResourceController extends BaseController
{
    @Autowired
    private ISdNearResourceService sdNearResourceService;

    /**
     * 查询周边资源列表
     */
    @PreAuthorize("@ss.hasPermi('business:emeResource:list')")
    @GetMapping("/list")
    @ApiOperation("查询周边资源列表")
    public TableDataInfo<List<SdNearResource>> list(SdNearResource sdNearResource)
    {
        startPage();
        List<SdNearResource> list = sdNearResourceService.selectSdNearResourceList(sdNearResource);
        return getDataTable(list);
    }

    /**
     * 导出周边资源列表
     */
    @ApiOperation("导出周边资源列表")
    @PreAuthorize("@ss.hasPermi('business:emeResource:export')")
    @Log(title = "周边资源", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdNearResource sdNearResource)
    {
        List<SdNearResource> list = sdNearResourceService.selectSdNearResourceList(sdNearResource);
        ExcelUtil<SdNearResource> util = new ExcelUtil<SdNearResource>(SdNearResource.class);
        return util.exportExcel(list, "周边资源");
    }

    /**
     * 获取周边资源详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:emeResource:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取周边资源详细信息")
    @ApiImplicitParam(name = "id", value = "周边资源ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result<SdNearResource> getInfo(@PathVariable("id") Long id)
    {
        return Result.success(sdNearResourceService.selectSdNearResourceById(id));
    }

    /**
     * 新增周边资源
     */
    @PreAuthorize("@ss.hasPermi('business:emeResource:add')")
    @Log(title = "周边资源", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增周边资源")
    public Result add(@RequestBody SdNearResource sdNearResource)
    {
        return Result.toResult(sdNearResourceService.insertSdNearResource(sdNearResource));
    }

    /**
     * 修改周边资源
     */
    @PreAuthorize("@ss.hasPermi('business:emeResource:edit')")
    @Log(title = "周边资源", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改周边资源")
    public Result edit(@RequestBody SdNearResource sdNearResource)
    {
        return Result.toResult(sdNearResourceService.updateSdNearResource(sdNearResource));
    }

    /**
     * 删除周边资源
     */
    @PreAuthorize("@ss.hasPermi('business:emeResource:remove')")
    @Log(title = "周边资源", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除周边资源")
    @ApiImplicitParam(name = "ids", value = "需要删除的周边资源ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result remove(@PathVariable Long[] ids)
    {
        return Result.toResult(sdNearResourceService.deleteSdNearResourceByIds(ids));
    }
}
