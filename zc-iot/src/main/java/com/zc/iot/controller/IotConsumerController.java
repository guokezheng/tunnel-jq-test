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
import com.zc.iot.domain.IotConsumer;
import com.zc.iot.service.IIotConsumerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 消费者Controller
 * 
 * @author Athena-gongfanfei
 * @date 2021-10-27
 */
@RestController
@RequestMapping("/iot/consumer")
public class IotConsumerController extends BaseController
{
    @Autowired
    private IIotConsumerService iotConsumerService;

    /**
     * 查询消费者列表
     */
    @PreAuthorize("@ss.hasPermi('iot:consumer:list')")
    @GetMapping("/list")
    public TableDataInfo list(IotConsumer iotConsumer)
    {
        startPage();
        List<IotConsumer> list = iotConsumerService.selectIotConsumerList(iotConsumer);
        return getDataTable(list);
    }

    /**
     * 导出消费者列表
     */
    @PreAuthorize("@ss.hasPermi('iot:consumer:export')")
    @Log(title = "消费者", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(IotConsumer iotConsumer)
    {
        List<IotConsumer> list = iotConsumerService.selectIotConsumerList(iotConsumer);
        ExcelUtil<IotConsumer> util = new ExcelUtil<IotConsumer>(IotConsumer.class);
        return util.exportExcel(list, "消费者数据");
    }

    /**
     * 获取消费者详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:consumer:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(iotConsumerService.selectIotConsumerById(id));
    }

    /**
     * 新增消费者
     */
    @PreAuthorize("@ss.hasPermi('iot:consumer:add')")
    @Log(title = "消费者", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody IotConsumer iotConsumer)
    {
        return toAjax(iotConsumerService.insertIotConsumer(iotConsumer));
    }

    /**
     * 修改消费者
     */
    @PreAuthorize("@ss.hasPermi('iot:consumer:edit')")
    @Log(title = "消费者", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody IotConsumer iotConsumer)
    {
        return toAjax(iotConsumerService.updateIotConsumer(iotConsumer));
    }

    /**
     * 删除消费者
     */
    @PreAuthorize("@ss.hasPermi('iot:consumer:remove')")
    @Log(title = "消费者", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(iotConsumerService.deleteIotConsumerByIds(ids));
    }
}
