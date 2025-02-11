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
import com.zc.iot.domain.IotGroup;
import com.zc.iot.service.IIotGroupService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 分组Controller
 * 
 * @author Athena-xiepufeng
 * @date 2021-11-16
 */
@RestController
@RequestMapping("/iot/group")
public class IotGroupController extends BaseController
{
    @Autowired
    private IIotGroupService iotGroupService;

    /**
     * 查询分组列表
     */
    @PreAuthorize("@ss.hasPermi('iot:group:list')")
    @GetMapping("/list")
    public AjaxResult list(IotGroup iotGroup)
    {
        List<IotGroup> list = iotGroupService.selectIotGroupList(iotGroup);
        return AjaxResult.success(list);
    }

    /**
     * 导出分组列表
     */
    @PreAuthorize("@ss.hasPermi('iot:group:export')")
    @Log(title = "分组", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(IotGroup iotGroup)
    {
        List<IotGroup> list = iotGroupService.selectIotGroupList(iotGroup);
        ExcelUtil<IotGroup> util = new ExcelUtil<IotGroup>(IotGroup.class);
        return util.exportExcel(list, "分组数据");
    }

    /**
     * 获取分组详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:group:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(iotGroupService.selectIotGroupById(id));
    }

    /**
     * 新增分组
     */
    @PreAuthorize("@ss.hasPermi('iot:group:add')")
    @Log(title = "分组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody IotGroup iotGroup)
    {
        return toAjax(iotGroupService.insertIotGroup(iotGroup));
    }

    /**
     * 修改分组
     */
    @PreAuthorize("@ss.hasPermi('iot:group:edit')")
    @Log(title = "分组", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody IotGroup iotGroup)
    {
        return toAjax(iotGroupService.updateIotGroup(iotGroup));
    }

    /**
     * 删除分组
     */
    @PreAuthorize("@ss.hasPermi('iot:group:remove')")
    @Log(title = "分组", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(iotGroupService.deleteIotGroupByIds(ids));
    }
}
