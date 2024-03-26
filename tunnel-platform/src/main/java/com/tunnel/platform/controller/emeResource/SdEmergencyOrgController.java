package com.tunnel.platform.controller.emeResource;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.emeResource.SdEmergencyOrg;
import com.tunnel.business.service.emeResource.ISdEmergencyOrgService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应急机构Controller
 * 
 * @author ruoyi
 * @date 2022-08-09
 */
@RestController
@RequestMapping("/system/org")
public class SdEmergencyOrgController extends BaseController
{
    @Autowired
    private ISdEmergencyOrgService sdEmergencyOrgService;

    /**
     * 查询应急机构列表
     */
    @ApiOperation("查询应急机构列表")
    @PreAuthorize("@ss.hasPermi('system:org:list')")
    @GetMapping("/list")
    public List<SysDept> list(SysDept sysDept)
    {
        List<SysDept> list = sdEmergencyOrgService.selectSdEmergencyOrgList(sysDept);
        return list;
    }
}
