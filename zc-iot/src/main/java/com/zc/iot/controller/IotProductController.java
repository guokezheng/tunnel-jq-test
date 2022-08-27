package com.zc.iot.controller;

import java.util.List;
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
import com.zc.iot.domain.IotProduct;
import com.zc.iot.service.IIotProductService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 产品Controller
 * 
 * @author Athena-xiepufeng
 * @date 2021-10-28
 */
@RestController
@RequestMapping("/iot/product")
public class IotProductController extends BaseController
{
    @Autowired
    private IIotProductService iotProductService;

    /**
     * 查询产品列表
     */
    @PreAuthorize("@ss.hasPermi('iot:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(IotProduct iotProduct)
    {
        startPage();
        List<IotProduct> list = iotProductService.selectIotProductList(iotProduct);
        return getDataTable(list);
    }

    /**
     * 导出产品列表
     */
    @PreAuthorize("@ss.hasPermi('iot:product:export')")
    @Log(title = "产品", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(IotProduct iotProduct)
    {
        List<IotProduct> list = iotProductService.selectIotProductList(iotProduct);
        ExcelUtil<IotProduct> util = new ExcelUtil<IotProduct>(IotProduct.class);
        return util.exportExcel(list, "产品数据");
    }

    /**
     * 获取产品详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:product:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(iotProductService.selectIotProductById(id));
    }

    /**
     * 新增产品
     */
    @PreAuthorize("@ss.hasPermi('iot:product:add')")
    @Log(title = "产品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody IotProduct iotProduct)
    {
        return toAjax(iotProductService.insertIotProduct(iotProduct));
    }

    /**
     * 修改产品
     */
    @PreAuthorize("@ss.hasPermi('iot:product:edit')")
    @Log(title = "产品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody IotProduct iotProduct)
    {
        return toAjax(iotProductService.updateIotProduct(iotProduct));
    }

    /**
     * 删除产品
     */
    @PreAuthorize("@ss.hasPermi('iot:product:remove')")
    @Log(title = "产品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(iotProductService.deleteIotProductByIds(ids));
    }
}
