package com.tunnel.platform.controller.dataInfo;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.dataInfo.SdSensor;
import com.tunnel.business.service.dataInfo.ISdSensorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 传感器列Controller
 * 
 * @author yanghousheng
 * @date 2020-11-09
 */
@RestController
@RequestMapping("/sensor")
@Api(tags = "传感器")
@ApiSupport(order = 16)
public class SdSensorController extends BaseController
{
    @Autowired
    private ISdSensorService sdSensorService;

    /**
     * 查询传感器列列表
     */
    @ApiOperation("查询传感器列列表")
    @GetMapping("/list")
    public TableDataInfo list(SdSensor sdSensor)
    {
        startPage();
        List<SdSensor> list = sdSensorService.selectSdSensorList(sdSensor);
        return getDataTable(list);
    }

    /**
     * 导出传感器列列表
     */
    /*@PreAuthorize(hasPermi = "system:sensor:export")
    @Log(title = "传感器列", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdSensor sdSensor) throws IOException
    {
        List<SdSensor> list = sdSensorService.selectSdSensorList(sdSensor);
        ExcelUtil<SdSensor> util = new ExcelUtil<SdSensor>(SdSensor.class);
        util.exportExcel(response, list, "sensor");
    }*/

    /**
     * 获取传感器列详细信息
     */
    @ApiOperation("获取传感器列详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdSensorService.selectSdSensorById(id));
    }

    /**
     * 新增传感器列
     */
    @ApiOperation("新增传感器列")
    @Log(title = "传感器列", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdSensor sdSensor)
    {
        return toAjax(sdSensorService.insertSdSensor(sdSensor));
    }

    /**
     * 修改传感器列
     */
    @ApiOperation("修改传感器列")
    @Log(title = "传感器列", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdSensor sdSensor)
    {
        return toAjax(sdSensorService.updateSdSensor(sdSensor));
    }

    /**
     * 删除传感器列
     */
    @ApiOperation("删除传感器列")
    @Log(title = "传感器列", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdSensorService.deleteSdSensorByIds(ids));
    }
}