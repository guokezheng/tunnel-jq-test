package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdEnvironmentConfiguration;
import com.tunnel.business.service.dataInfo.ISdEnvironmentConfigurationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 隧道环境配置Controller
 *
 * @author 刘方堃
 * @date 2021-12-13
 */
@RestController
@RequestMapping("/system/configuration")
@Api(tags = "隧道环境配置")
public class SdEnvironmentConfigurationController extends BaseController {
    @Autowired
    private ISdEnvironmentConfigurationService sdEnvironmentConfigurationService;

    /**
     * 查询隧道环境配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:configuration:list')")
    @GetMapping("/list")
    @ApiOperation("查询隧道环境配置列表")
    public TableDataInfo<List<SdEnvironmentConfiguration>> list(SdEnvironmentConfiguration sdEnvironmentConfiguration) {
        startPage();
        List<SdEnvironmentConfiguration> list = sdEnvironmentConfigurationService.selectSdEnvironmentConfigurationList(sdEnvironmentConfiguration);
        return getDataTable(list);
    }

    /**
     * 导出隧道环境配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:configuration:export')")
    @Log(title = "隧道环境配置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdEnvironmentConfiguration sdEnvironmentConfiguration) {
        List<SdEnvironmentConfiguration> list = sdEnvironmentConfigurationService.selectSdEnvironmentConfigurationList(sdEnvironmentConfiguration);
        ExcelUtil<SdEnvironmentConfiguration> util = new ExcelUtil<SdEnvironmentConfiguration>(SdEnvironmentConfiguration.class);
        return util.exportExcel(list, "隧道环境配置数据");
    }

    /**
     * 获取隧道环境配置详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:configuration:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取隧道环境配置详细信息")
    @ApiImplicitParam(name = "id", value = "隧道环境配置主键", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result<SdEnvironmentConfiguration> getInfo(@PathVariable("id") Long id) {
        return Result.success(sdEnvironmentConfigurationService.selectSdEnvironmentConfigurationById(id));
    }

    /**
     * 新增隧道环境配置
     */
//    @PreAuthorize("@ss.hasPermi('system:configuration:add')")
    @Log(title = "隧道环境配置", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增隧道环境配置")
    public Result add(@RequestParam("file") MultipartFile[] file, SdEnvironmentConfiguration sdEnvironmentConfiguration) {
        return Result.toResult(sdEnvironmentConfigurationService.insertSdEnvironmentConfiguration(file, sdEnvironmentConfiguration));
    }

    /**
     * 修改隧道环境配置
     */
//    @PreAuthorize("@ss.hasPermi('system:configuration:edit')")
    @Log(title = "隧道环境配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改隧道环境配置")
    public Result edit( MultipartFile[] file,
                           @RequestParam("id") Long id,
                           @RequestParam("url") String url,
                           @RequestParam("sdName") String sdName,
                           @RequestParam("environmentType") String environmentType,
                           @RequestParam("width") String width,
                           @RequestParam("height") String height,
                           @RequestParam("direction") String direction,
                           @RequestParam("remark") String remark,
                           @RequestParam("removeIds") Long[] removeIds) {
        SdEnvironmentConfiguration sdEnvironmentConfiguration = new SdEnvironmentConfiguration();
        sdEnvironmentConfiguration.setUrl(url.equals("null")?null:url);
        sdEnvironmentConfiguration.setDirection(direction.equals("null")?null:direction);
        sdEnvironmentConfiguration.setEnvironmentType(environmentType.equals("null")?null:environmentType);
        sdEnvironmentConfiguration.setHeight(height.equals("null")?null:height);
        sdEnvironmentConfiguration.setId(id);
        sdEnvironmentConfiguration.setSdName(sdName.equals("null")?null:sdName);
        sdEnvironmentConfiguration.setWidth(width.equals("null")?null:width);
        sdEnvironmentConfiguration.setRemark(remark.equals("null")?null:remark);
        if (width.equals("0")) {
            throw new RuntimeException("图标宽度不能为0");
        } else if (height.equals("0")) {
            throw new RuntimeException("图标高度不能为0");
        }
        return Result.toResult(sdEnvironmentConfigurationService.updateSdEnvironmentConfiguration(file, sdEnvironmentConfiguration,removeIds));
    }

    /**
     * 删除隧道环境配置
     */
    @PreAuthorize("@ss.hasPermi('system:configuration:remove')")
    @Log(title = "隧道环境配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation("删除隧道环境配置")
    @ApiImplicitParam(name = "ids", value = "需要删除的隧道环境配置主键集合", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result remove(@PathVariable Long[] ids) {
        return Result.toResult(sdEnvironmentConfigurationService.deleteSdEnvironmentConfigurationByIds(ids));
    }
}
