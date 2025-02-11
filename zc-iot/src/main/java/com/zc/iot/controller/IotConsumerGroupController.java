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
import com.zc.iot.domain.IotConsumerGroup;
import com.zc.iot.service.IIotConsumerGroupService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 消费组Controller
 * 
 * @author Athena-gongfanfei
 * @date 2021-10-27
 */
@RestController
@RequestMapping("/iot/consumerGroup")
public class IotConsumerGroupController extends BaseController
{
    @Autowired
    private IIotConsumerGroupService iotConsumerGroupService;

    /**
     * 查询消费组列表
     */
    @PreAuthorize("@ss.hasPermi('iot:consumerGroup:list')")
    @GetMapping("/list")
    public TableDataInfo list(IotConsumerGroup iotConsumerGroup)
    {
        startPage();
        List<IotConsumerGroup> list = iotConsumerGroupService.selectIotConsumerGroupList(iotConsumerGroup);
        return getDataTable(list);
    }

    /**
     * 导出消费组列表
     */
    @PreAuthorize("@ss.hasPermi('iot:consumerGroup:export')")
    @Log(title = "消费组", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(IotConsumerGroup iotConsumerGroup)
    {
        List<IotConsumerGroup> list = iotConsumerGroupService.selectIotConsumerGroupList(iotConsumerGroup);
        ExcelUtil<IotConsumerGroup> util = new ExcelUtil<IotConsumerGroup>(IotConsumerGroup.class);
        return util.exportExcel(list, "消费组数据");
    }

    /**
     * 获取消费组详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:consumerGroup:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(iotConsumerGroupService.selectIotConsumerGroupById(id));
    }

    /**
     * 新增消费组
     */
    @PreAuthorize("@ss.hasPermi('iot:consumerGroup:add')")
    @Log(title = "消费组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody IotConsumerGroup iotConsumerGroup)
    {
        return toAjax(iotConsumerGroupService.insertIotConsumerGroup(iotConsumerGroup));
    }

    /**
     * 修改消费组
     */
    @PreAuthorize("@ss.hasPermi('iot:consumerGroup:edit')")
    @Log(title = "消费组", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody IotConsumerGroup iotConsumerGroup)
    {
        return toAjax(iotConsumerGroupService.updateIotConsumerGroup(iotConsumerGroup));
    }

    /**
     * 删除消费组
     */
    @PreAuthorize("@ss.hasPermi('iot:consumerGroup:remove')")
    @Log(title = "消费组", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(iotConsumerGroupService.deleteIotConsumerGroupByIds(ids));
    }
}
