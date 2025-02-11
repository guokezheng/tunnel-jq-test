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
import com.zc.iot.domain.IotServiceSub;
import com.zc.iot.service.IIotServiceSubService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 服务订阅Controller
 * 
 * @author Athena-gongfanfei
 * @date 2021-10-27
 */
@RestController
@RequestMapping("/iot/serviceSub")
public class IotServiceSubController extends BaseController
{
    @Autowired
    private IIotServiceSubService iotServiceSubService;

    /**
     * 查询服务订阅列表
     */
    @PreAuthorize("@ss.hasPermi('iot:serviceSub:list')")
    @GetMapping("/list")
    public TableDataInfo list(IotServiceSub iotServiceSub)
    {
        startPage();
        List<IotServiceSub> list = iotServiceSubService.selectIotServiceSubList(iotServiceSub);
        return getDataTable(list);
    }

    /**
     * 导出服务订阅列表
     */
    @PreAuthorize("@ss.hasPermi('iot:serviceSub:export')")
    @Log(title = "服务订阅", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(IotServiceSub iotServiceSub)
    {
        List<IotServiceSub> list = iotServiceSubService.selectIotServiceSubList(iotServiceSub);
        ExcelUtil<IotServiceSub> util = new ExcelUtil<IotServiceSub>(IotServiceSub.class);
        return util.exportExcel(list, "服务订阅数据");
    }

    /**
     * 获取服务订阅详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:serviceSub:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(iotServiceSubService.selectIotServiceSubById(id));
    }

    /**
     * 新增服务订阅
     */
    @PreAuthorize("@ss.hasPermi('iot:serviceSub:add')")
    @Log(title = "服务订阅", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody IotServiceSub iotServiceSub)
    {
        return toAjax(iotServiceSubService.insertIotServiceSub(iotServiceSub));
    }

    /**
     * 修改服务订阅
     */
    @PreAuthorize("@ss.hasPermi('iot:serviceSub:edit')")
    @Log(title = "服务订阅", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody IotServiceSub iotServiceSub)
    {
        return toAjax(iotServiceSubService.updateIotServiceSub(iotServiceSub));
    }

    /**
     * 删除服务订阅
     */
    @PreAuthorize("@ss.hasPermi('iot:serviceSub:remove')")
    @Log(title = "服务订阅", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(iotServiceSubService.deleteIotServiceSubByIds(ids));
    }
}
