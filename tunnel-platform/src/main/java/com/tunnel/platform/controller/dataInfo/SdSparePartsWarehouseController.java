package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdSparePartsWarehouse;
import com.tunnel.business.domain.dataInfo.vo.SdSparePartsWarehouseVO;
import com.tunnel.business.service.dataInfo.ISdSparePartsWarehouseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 备品备件库Controller
 * 
 * @author ruoyi
 * @date 2022-01-21
 */
@RestController
@RequestMapping("/system/warehouse")
@Api(tags = "设备备件库")
public class SdSparePartsWarehouseController extends BaseController
{
    @Autowired
    private ISdSparePartsWarehouseService sdSparePartsWarehouseService;

    /**
     * 查询备品备件库列表
     */
    @PreAuthorize("@ss.hasPermi('system:warehouse:list')")
    @GetMapping("/list")
    @ApiOperation("查询备品备件库列表")
    public TableDataInfo<List<SdSparePartsWarehouse>> list(SdSparePartsWarehouse sdSparePartsWarehouse)
    {
        startPage();
        List<SdSparePartsWarehouse> list = sdSparePartsWarehouseService.selectSdSparePartsWarehouseList(sdSparePartsWarehouse);
        return getDataTable(list);
    }

    @GetMapping(value = "/getHsdSparePartList")
    @ApiOperation("瑞华赢获取备件库接口")
    public AjaxResult getHsdSparePartList() {
        SdSparePartsWarehouse warehouse = new SdSparePartsWarehouse();
        warehouse.setTunnelId("JQ-WeiFang-JiuLongYu-HSD");
        List<SdSparePartsWarehouseVO> list = sdSparePartsWarehouseService.getHsdSparePartList(warehouse);
        return AjaxResult.success(list);
    }


    /**
     * 导出备品备件库列表
     */
    @PreAuthorize("@ss.hasPermi('system:warehouse:export')")
    @Log(title = "备品备件库", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdSparePartsWarehouse sdSparePartsWarehouse)
    {
        List<SdSparePartsWarehouse> list = sdSparePartsWarehouseService.selectSdSparePartsWarehouseList(sdSparePartsWarehouse);
        ExcelUtil<SdSparePartsWarehouse> util = new ExcelUtil<SdSparePartsWarehouse>(SdSparePartsWarehouse.class);
        return util.exportExcel(list, "备品备件数据");
    }

    /**
     * 获取备品备件库详细信息
     */
    @ApiOperation("获取备品备件库详细信息")
    @PreAuthorize("@ss.hasPermi('system:warehouse:query')")
    @GetMapping(value = "/{id}")
    @ApiImplicitParam(name = "id", value = "备品备件库主键", required = true, dataType = "Long", paramType = "path",dataTypeClass = Long.class)
    public Result<SdSparePartsWarehouse> getInfo(@PathVariable("id") Long id)
    {
        return Result.success(sdSparePartsWarehouseService.selectSdSparePartsWarehouseById(id));
    }

    /**
     * 新增备品备件库
     */
    @PreAuthorize("@ss.hasPermi('system:warehouse:add')")
    @Log(title = "备品备件库", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增备品备件库")
    public Result add(@RequestBody SdSparePartsWarehouse sdSparePartsWarehouse)
    {
        return Result.toResult(sdSparePartsWarehouseService.insertSdSparePartsWarehouse(sdSparePartsWarehouse));
    }

    /**
     * 修改备品备件库
     */
    @PreAuthorize("@ss.hasPermi('system:warehouse:edit')")
    @Log(title = "备品备件库", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改备品备件库")
    public Result edit(@RequestBody SdSparePartsWarehouse sdSparePartsWarehouse)
    {
        return Result.toResult(sdSparePartsWarehouseService.updateSdSparePartsWarehouse(sdSparePartsWarehouse));
    }

    /**
     * 删除备品备件库
     */
    @PreAuthorize("@ss.hasPermi('system:warehouse:remove')")
    @Log(title = "备品备件库", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除备品备件库")
    @ApiImplicitParam(name = "ids", value = "需要删除的备品备件库主键集合", required = true, dataType = "Long", paramType = "path",dataTypeClass = Long.class)
    public Result remove(@PathVariable Long[] ids)
    {
        return Result.toResult(sdSparePartsWarehouseService.deleteSdSparePartsWarehouseByIds(ids));
    }
}
