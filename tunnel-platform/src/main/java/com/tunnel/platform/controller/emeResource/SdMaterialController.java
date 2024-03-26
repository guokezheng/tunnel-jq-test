package com.tunnel.platform.controller.emeResource;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.quartz.domain.SysJobLog;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskList;
import com.tunnel.business.domain.emeResource.SdMaterial;
import com.tunnel.business.domain.emeResource.SdMaterialRecord;
import com.tunnel.business.service.emeResource.ISdMaterialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 应急资源Controller
 * 
 * @author xuebi
 * @date 2020-08-28
 */
@RestController
@RequestMapping("/material")
@Api(tags = "应急资源")
public class SdMaterialController extends BaseController
{
    @Autowired
    private ISdMaterialService sdMaterialService;

    /**
     * 查询应急资源列表
     */
    @GetMapping("/list")
    @ApiOperation("查询应急资源列表")
    public TableDataInfo<List<SdMaterial>> list(SdMaterial sdMaterial)
    {
        startPage();
        List<SdMaterial> list = sdMaterialService.selectSdMaterialList(sdMaterial);
        return getDataTable(list);
    }
    /**
     * 查询出入库详细信息
     * @param sdmaterialrecord
     * @param sdmaterialrecord
     */	
    @GetMapping("/materialId")
    @ApiOperation("查询出入库详细信息")
    public Result<List<SdMaterialRecord>> detailWarehousing(SdMaterialRecord sdmaterialrecord)
    {
    	List<SdMaterialRecord> list = sdMaterialService.selectSdMaterialRecordList(sdmaterialrecord);
        return Result.success(list);
    }

	/**
     * 导出应急资源列表
     */
    @ApiOperation("导出应急资源列表")
    @Log(title = "应急资源", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdMaterial sdMaterial)
    {
        List<SdMaterial> list = sdMaterialService.selectSdMaterialList(sdMaterial);
        ExcelUtil<SdMaterial> util = new ExcelUtil<SdMaterial>(SdMaterial.class);
        return util.exportExcel(list, "应急物资");
    }


    /**
     * 获取应急资源详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取应急资源详细信息")
    @ApiImplicitParam(name = "id", value = "应急资源ID", required = true, dataType = "Long", paramType = "path",dataTypeClass = Long.class)
    public Result<SdMaterial> getInfo(@PathVariable("id") Long id)
    {
        return Result.success(sdMaterialService.selectSdMaterialById(id));
    }

    /**
     * 新增应急资源
     */
    @Log(title = "应急资源", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增应急资源")
    public Result add(@RequestBody SdMaterial sdMaterial)
    {
        return Result.toResult(sdMaterialService.insertSdMaterial(sdMaterial));
    }
	/**
     * 修改应急资源
     */
    @Log(title = "应急资源", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改应急资源")
    public Result edit(@RequestBody SdMaterial sdMaterial)
    {
    	sdMaterial.setCreateBy(SecurityUtils.getUsername());
    	int a = sdMaterialService.updateSdMaterial(sdMaterial);
        return Result.toResult(a);
    }
    /**
     * 修改应急资源出入库数量
     * @param sdmaterialRecord
     * @return
     */
    @Log(title = "应急资源", businessType = BusinessType.UPDATE)
    @PutMapping("/material")
    @ApiOperation("修改应急资源出入库数量")
    public Result editMaterial(@RequestBody SdMaterialRecord sdmaterialRecord)
    {
        sdmaterialRecord.setCreateBy(SecurityUtils.getUsername());
        return Result.toResult(sdMaterialService.insertSdMaterialRecord(sdmaterialRecord));
    }
    
    
    /**
     * 删除应急资源
     */
    @Log(title = "应急资源", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除应急资源")
    @ApiImplicitParam(name = "ids", value = "需要删除的应急资源ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result remove(@PathVariable Long[] ids)
    {
        return Result.toResult(sdMaterialService.deleteSdMaterialByIds(ids));
    }
}