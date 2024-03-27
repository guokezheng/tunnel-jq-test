package com.tunnel.platform.controller.product;

import java.util.List;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.tunnel.business.domain.product.SdProduct;
import com.tunnel.business.service.product.ISdProductService;
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
 * 产品Controller
 * 
 * @author ruoyi
 * @date 2023-02-27
 */
@RestController
@RequestMapping("/product")
@Api(tags = "产品Controller")
@ApiSupport(order = 16)
public class SdProductController extends BaseController
{
    @Autowired
    private ISdProductService sdProductService;

    /**
     * 查询产品列表
     */
    @PreAuthorize("@ss.hasPermi('product:list')")
    @GetMapping("/list")
    @ApiOperation("查询产品列表")
    public TableDataInfo list(SdProduct sdProduct)
    {
        startPage();
        List<SdProduct> list = sdProductService.selectSdProductList(sdProduct);
        return getDataTable(list);
    }

    /**
     * 导出产品列表
     */
    @ApiOperation("导出产品列表")
    @PreAuthorize("@ss.hasPermi('product:export')")
    @Log(title = "产品", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdProduct sdProduct)
    {
        List<SdProduct> list = sdProductService.selectSdProductList(sdProduct);
        ExcelUtil<SdProduct> util = new ExcelUtil<SdProduct>(SdProduct.class);
        return util.exportExcel(list, "产品数据");
    }

    /**
     * 获取产品详细信息
     */
    @PreAuthorize("@ss.hasPermi('product:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取产品详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdProductService.selectSdProductById(id));
    }

    /**
     * 新增产品
     */
    @PreAuthorize("@ss.hasPermi('product:add')")
    @Log(title = "产品", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增产品")
    public AjaxResult add(@RequestBody SdProduct sdProduct)
    {
        return toAjax(sdProductService.insertSdProduct(sdProduct));
    }

    /**
     * 修改产品
     */
    @PreAuthorize("@ss.hasPermi('product:edit')")
    @Log(title = "产品", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改产品")
    public AjaxResult edit(@RequestBody SdProduct sdProduct)
    {
        return toAjax(sdProductService.updateSdProduct(sdProduct));
    }

    /**
     * 删除产品
     */
    @PreAuthorize("@ss.hasPermi('product:remove')")
    @Log(title = "产品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除产品")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdProductService.deleteSdProductByIds(ids));
    }
}
