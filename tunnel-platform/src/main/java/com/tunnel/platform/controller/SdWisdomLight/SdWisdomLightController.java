package com.tunnel.platform.controller.SdWisdomLight;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.wisdomLight.SdWisdomLight;
import com.tunnel.business.service.wisdomLight.ISdWisdomLightService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 【智慧调光】Controller
 *
 * @author ruoyi
 * @date 2023-08-10
 */
@RestController
@RequestMapping("/wisdomLight")
@Api(tags = "【智慧调光】Controller")
@ApiSupport(order = 16)
public class SdWisdomLightController extends BaseController
{
    @Autowired
    private ISdWisdomLightService sdWisdomLightService;

    /**
     * 查询【智慧调光】列表
     */
    @GetMapping("/list")
    @ApiOperation("查询【智慧调光】列表")
    public TableDataInfo list(SdWisdomLight sdWisdomLight)
    {
        startPage();
        List<SdWisdomLight> list = sdWisdomLightService.selectSdWisdomLightList(sdWisdomLight);
        return getDataTable(list);
    }

    /**
     * 新增【智慧调光】
     */
    @Log(title = "【加强照明配置】", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增【智慧调光】")
    public AjaxResult add(@RequestBody SdWisdomLight sdWisdomLight)
    {
        return AjaxResult.success(sdWisdomLightService.insertSdWisdomLight(sdWisdomLight));
    }

    /**
     * 修改【智慧调光】
     */
    @Log(title = "【加强照明配置】", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改【智慧调光】")
    public AjaxResult edit(@RequestBody SdWisdomLight sdWisdomLight)
    {
        return toAjax(sdWisdomLightService.updateSdWisdomLight(sdWisdomLight));
    }
    /**
     * 删除【加强照明配置】
     */
    @Log(title = "【加强照明配置】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation("删除【加强照明配置】")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdWisdomLightService.deleteSdWisdomLight(ids));
    }

    /**
     * 删除【加强照明配置】
     */
    @Log(title = "【加强照明配置】", businessType = BusinessType.UPDATE)
    @PostMapping("/updateSdWisdomIsStatus")
    @ApiOperation("删除【加强照明配置】")
    public AjaxResult updateSdWisdomIsStatus(@RequestBody SdWisdomLight sdWisdomLight) {
        return  toAjax(sdWisdomLightService.updateSdWisdomIsStatus(sdWisdomLight));
    }

}
