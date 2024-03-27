package com.tunnel.platform.controller.event;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.event.SdEmergencyPer;
import com.tunnel.business.service.event.ISdEmergencyPerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应急人员信息Controller
 *
 * @author ruoyi
 * @date 2021-05-12
 */
@RestController
@RequestMapping("/SdEmergencyPer")
@Api(tags = "应急人员")
@ApiSupport(order = 16)
public class SdEmergencyPerController extends BaseController
{
    @Autowired
    private ISdEmergencyPerService sdEmergencyPerService;

    /**
     * 查询应急人员信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:SdEmergencyPer:list')")
    @GetMapping("/list")
    @ApiOperation("查询应急人员信息列表")
    public TableDataInfo<List<SdEmergencyPer>> list(SdEmergencyPer sdEmergencyPer)
    {
        startPage();
        List<SdEmergencyPer> list = sdEmergencyPerService.selectSdEmergencyPerList(sdEmergencyPer);
        return getDataTable(list);
    }

    /**
     * 导出应急人员信息列表
     */
    @ApiOperation("导出应急人员信息列表")
    @PreAuthorize("@ss.hasPermi('business:SdEmergencyPer:export')")
    @Log(title = "应急人员信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdEmergencyPer sdEmergencyPer)
    {
        List<SdEmergencyPer> list = sdEmergencyPerService.selectSdEmergencyPerList(sdEmergencyPer);
        ExcelUtil<SdEmergencyPer> util = new ExcelUtil<SdEmergencyPer>(SdEmergencyPer.class);
        return util.exportExcel(list, "应急人员");
    }

    /**
     * 获取应急人员信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:SdEmergencyPer:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取应急人员信息详细信息")
    @ApiImplicitParam(name = "id", value = "应急人员信息ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result<SdEmergencyPer> getInfo(@PathVariable("id") Long id)
    {
        return Result.success(sdEmergencyPerService.selectSdEmergencyPerById(id));
    }

    /**
     * 新增应急人员信息
     */
    @PreAuthorize("@ss.hasPermi('business:SdEmergencyPer:add')")
    @Log(title = "应急人员信息", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增应急人员信息")
    public Result add(@RequestBody SdEmergencyPer sdEmergencyPer)
    {
        return Result.toResult(sdEmergencyPerService.insertSdEmergencyPer(sdEmergencyPer));
    }

    /**
     * 修改应急人员信息
     */
    @PreAuthorize("@ss.hasPermi('business:SdEmergencyPer:edit')")
    @Log(title = "应急人员信息", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改应急人员信息")
    public Result edit(@RequestBody SdEmergencyPer sdEmergencyPer)
    {
        return Result.toResult(sdEmergencyPerService.updateSdEmergencyPer(sdEmergencyPer));
    }

    /**
     * 删除应急人员信息
     */
    @PreAuthorize("@ss.hasPermi('business:SdEmergencyPer:remove')")
    @Log(title = "应急人员信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除应急人员信息")
    @ApiImplicitParam(name = "ids", value = "需要删除的应急人员信息ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result remove(@PathVariable Long[] ids)
    {
        return Result.toResult(sdEmergencyPerService.deleteSdEmergencyPerByIds(ids));
    }
}
