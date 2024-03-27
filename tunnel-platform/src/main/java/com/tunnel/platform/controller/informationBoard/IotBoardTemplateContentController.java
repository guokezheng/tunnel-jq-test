package com.tunnel.platform.controller.informationBoard;

import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.informationBoard.IotBoardTemplateContent;
import com.tunnel.business.service.informationBoard.IIotBoardTemplateContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 发布模板内容Controller
 *
 * @date 2022-03-22
 */
@RestController
@RequestMapping("/system/content")
@Api(tags = "发布模板内容Controller")
@ApiSupport(order = 16)
public class IotBoardTemplateContentController extends BaseController
{
    @Autowired
    private IIotBoardTemplateContentService iotBoardTemplateContentService;

    /**
     * 查询发布模板内容列表
     */
//    @PreAuthorize("@ss.hasPermi('system:content:list')")
    @GetMapping("/list")
    @ApiOperation("查询发布模板内容列表")
    public TableDataInfo list(IotBoardTemplateContent iotBoardTemplateContent)
    {
        startPage();
        List<IotBoardTemplateContent> list = iotBoardTemplateContentService.selectSdVmsTemplateContentList(iotBoardTemplateContent);
        return getDataTable(list);
    }

    /**
     * 导出发布模板内容列表
     */
//    @PreAuthorize("@ss.hasPermi('system:content:export')")
    @Log(title = "发布模板内容", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation("导出发布模板内容列表")
    public AjaxResult export(IotBoardTemplateContent iotBoardTemplateContent)
    {
        List<IotBoardTemplateContent> list = iotBoardTemplateContentService.selectSdVmsTemplateContentList(iotBoardTemplateContent);
        ExcelUtil<IotBoardTemplateContent> util = new ExcelUtil<IotBoardTemplateContent>(IotBoardTemplateContent.class);
        return util.exportExcel(list, "发布模板内容数据");
    }

    /**
     * 获取发布模板内容详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:content:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取发布模板内容详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(iotBoardTemplateContentService.selectSdVmsTemplateContentById(id));
    }

    /**
     * 新增发布模板内容
     */
//    @PreAuthorize("@ss.hasPermi('system:content:add')")
    @Log(title = "发布模板内容", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增发布模板内容")
    public AjaxResult add(@RequestBody JSONObject jsonObject)
    {
        return toAjax(iotBoardTemplateContentService.insertSdVmsTemplateContent(jsonObject));
    }

    /**
     * 修改发布模板内容
     */
//    @PreAuthorize("@ss.hasPermi('system:content:edit')")
    @Log(title = "发布模板内容", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改发布模板内容")
    public AjaxResult edit(@RequestBody JSONObject jsonObject)
    {
        return toAjax(iotBoardTemplateContentService.updateSdVmsTemplateContent(jsonObject));
    }

    /**
     * 删除发布模板内容
     */
//    @PreAuthorize("@ss.hasPermi('system:content:remove')")
    @Log(title = "发布模板内容", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除发布模板内容")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(iotBoardTemplateContentService.deleteSdVmsTemplateContentByIds(ids));
    }
}
