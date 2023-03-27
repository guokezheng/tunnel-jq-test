package com.tunnel.platform.controller.informationBoard;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.informationBoard.IotBoardTemplate;
import com.tunnel.business.service.informationBoard.IIotBoardTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 情报板模板Controller
 *
 * @author 刘方堃
 * @date 2021-11-30
 */
@RestController
@RequestMapping("/system/template")
public class IotBoardTemplateController extends BaseController
{
    @Autowired
    private IIotBoardTemplateService iotBoardTemplateService;

    /**
     * 查询情报板模板列表
     */
//    @PreAuthorize("@ss.hasPermi('system:templateConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(IotBoardTemplate iotBoardTemplate)
    {
        startPage();
        List<IotBoardTemplate> list = iotBoardTemplateService.selectSdVmsTemplateList(iotBoardTemplate);
        return getDataTable(list);
    }

    @GetMapping("/getAllVmsTemplate")
    public AjaxResult getAllVmsTemplate(String category, String devicePixel) {
        return AjaxResult.success(iotBoardTemplateService.getAllVmsTemplate(category, devicePixel));
    }

    /**
     * 导出情报板模板列表
     */
    @PreAuthorize("@ss.hasPermi('system:templateConfig:export')")
    @Log(title = "情报板模板", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(IotBoardTemplate iotBoardTemplate)
    {
        List<IotBoardTemplate> list = iotBoardTemplateService.selectSdVmsTemplateList(iotBoardTemplate);
        ExcelUtil<IotBoardTemplate> util = new ExcelUtil<IotBoardTemplate>(IotBoardTemplate.class);
        return util.exportExcel(list, "情报板模板列表");
    }

    /**
     * 获取情报板模板详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:templateConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(iotBoardTemplateService.selectSdVmsTemplateById(id));
    }

    /**
     * 新增情报板模板
     */
//    @PreAuthorize("@ss.hasPermi('system:templateConfig:add')")
    @Log(title = "情报板模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody JSONObject jsonObject)
    {
        return AjaxResult.success(iotBoardTemplateService.insertSdVmsTemplate(jsonObject));
    }

    /**
     * 修改情报板模板
     */
//    @PreAuthorize("@ss.hasPermi('system:templateConfig:edit')")
    @Log(title = "情报板模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody JSONObject jsonObject)
    {
        return toAjax(iotBoardTemplateService.updateSdVmsTemplate(jsonObject));
    }

    /**
     * 删除情报板模板
     */
    @PreAuthorize("@ss.hasPermi('system:templateConfig:remove')")
    @Log(title = "情报板模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(iotBoardTemplateService.deleteSdVmsTemplateByIds(ids));
    }

    /**
     * 情报板发布
     */
    @PostMapping("/informationBoardRelease")
    public AjaxResult informationBoardRelease(JSONObject jsonObject) {
        return AjaxResult.success(iotBoardTemplateService.informationBoardRelease(jsonObject));
    }

    /**
     * 情报板获取
     */
    @PostMapping("/informationBoardAcquisition")
    public AjaxResult informationBoardAcquisition(JSONObject jsonObject) {
        return AjaxResult.success(iotBoardTemplateService.informationBoardAcquisition(jsonObject));
    }

    /**
     * 根据设备ID和分类ID获取情报板模板数据集合
     */
    @PostMapping("/getVMSTemplatesByDevIdAndCategory")
    public AjaxResult getVMSTemplatesByDevIdAndCategory(@RequestBody List<String> devIds) {
        return AjaxResult.success(iotBoardTemplateService.getVMSTemplatesByDevIdAndCategory(devIds));
    }

    /**
     * 预案-除指定设备情况下查询情报板
     * @param sdDevices
     * @return
     */
    @GetMapping("/getVmsDataList")
    public AjaxResult getVmsDataList(SdDevices sdDevices){
        return iotBoardTemplateService.getVmsDataList(sdDevices);
    }
}
