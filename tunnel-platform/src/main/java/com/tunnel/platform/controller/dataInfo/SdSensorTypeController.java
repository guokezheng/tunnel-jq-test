package com.tunnel.platform.controller.dataInfo;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.dataInfo.SdSensorType;
import com.tunnel.business.service.dataInfo.ISdSensorTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 传感器类型Controller
 * 
 * @author yanghousheng
 * @date 2020-11-10
 */
@RestController
@RequestMapping("/sensorType")
@Api(tags = "传感器类型")
@ApiSupport(order = 16)
public class SdSensorTypeController extends BaseController
{
    @Autowired
    private ISdSensorTypeService sdSensorTypeService;

    /**
     * 查询传感器类型列表
     */
    @ApiOperation("查询传感器类型列表")
    @GetMapping("/list")
    public TableDataInfo list(SdSensorType sdSensorType)
    {
        startPage();
        List<SdSensorType> list = sdSensorTypeService.selectSdSensorTypeList(sdSensorType);
        return getDataTable(list);
    }

    /**
     * 导出传感器类型列表
     */
    /*@Log(title = "传感器类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdSensorType sdSensorType) throws IOException
    {
        List<SdSensorType> list = sdSensorTypeService.selectSdSensorTypeList(sdSensorType);
        ExcelUtil<SdSensorType> util = new ExcelUtil<SdSensorType>(SdSensorType.class);
        util.exportExcel(response, list, "type");
    }*/

    /**
     * 获取传感器类型详细信息
     */
    @ApiOperation("获取传感器类型详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdSensorTypeService.selectSdSensorTypeById(id));
    }

    /**
     * 新增传感器类型
     */
    @ApiOperation("新增传感器类型")
    @Log(title = "传感器类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdSensorType sdSensorType)
    {
        return toAjax(sdSensorTypeService.insertSdSensorType(sdSensorType));
    }

    /**
     * 修改传感器类型
     */
    @ApiOperation("修改传感器类型")
    @Log(title = "传感器类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdSensorType sdSensorType)
    {
        return toAjax(sdSensorTypeService.updateSdSensorType(sdSensorType));
    }

    /**
     * 删除传感器类型
     */
    @ApiOperation("删除传感器类型")
    @Log(title = "传感器类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdSensorTypeService.deleteSdSensorTypeByIds(ids));
    }
}
