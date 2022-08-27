package com.tunnel.platform.controller.trafficOperationControl.eventManage;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.platform.domain.trafficOperationControl.eventManage.SdTrafficIncidentInfo;
import com.tunnel.platform.service.trafficOperationControl.eventManage.ISdTrafficIncidentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 交通运行管控-事件信息管理Controller
 *
 * @author ruoyi
 * @date 2022-02-14
 */
@RestController
@RequestMapping("/incident/info")
public class SdTrafficIncidentInfoController extends BaseController
{
    @Autowired
    private ISdTrafficIncidentInfoService sdTrafficIncidentInfoService;

    /**
     * 查询交通运行管控-事件信息管理列表
     */
    @PreAuthorize("@ss.hasPermi('incident:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdTrafficIncidentInfo sdTrafficIncidentInfo)
    {
        startPage();
        List<SdTrafficIncidentInfo> list = sdTrafficIncidentInfoService.selectSdTrafficIncidentInfoList(sdTrafficIncidentInfo);
        return getDataTable(list);
    }

    /**
     * 导出交通运行管控-事件信息管理列表
     */
    @PreAuthorize("@ss.hasPermi('incident:info:export')")
    @Log(title = "交通运行管控-事件信息管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdTrafficIncidentInfo sdTrafficIncidentInfo)
    {
        List<SdTrafficIncidentInfo> list = sdTrafficIncidentInfoService.selectSdTrafficIncidentInfoList(sdTrafficIncidentInfo);
        ExcelUtil<SdTrafficIncidentInfo> util = new ExcelUtil<SdTrafficIncidentInfo>(SdTrafficIncidentInfo.class);
        return util.exportExcel(list, "交通运行管控-事件信息管理数据");
    }

    /**
     * 获取交通运行管控-事件信息管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('incident:info:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdTrafficIncidentInfoService.selectSdTrafficIncidentInfoById(id));
    }

    /**
     * 新增交通运行管控-事件信息管理
     */
    @PreAuthorize("@ss.hasPermi('incident:info:add')")
    @Log(title = "交通运行管控-事件信息管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdTrafficIncidentInfo sdTrafficIncidentInfo)
    {
        return toAjax(sdTrafficIncidentInfoService.insertSdTrafficIncidentInfo(sdTrafficIncidentInfo));
    }

    /**
     * 修改交通运行管控-事件信息管理
     */
    @PreAuthorize("@ss.hasPermi('incident:info:edit')")
    @Log(title = "交通运行管控-事件信息管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdTrafficIncidentInfo sdTrafficIncidentInfo)
    {
        return toAjax(sdTrafficIncidentInfoService.updateSdTrafficIncidentInfo(sdTrafficIncidentInfo));
    }

    /**
     * 删除交通运行管控-事件信息管理
     */
    @PreAuthorize("@ss.hasPermi('incident:info:remove')")
    @Log(title = "交通运行管控-事件信息管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdTrafficIncidentInfoService.deleteSdTrafficIncidentInfoByIds(ids));
    }


    /**
     * 获取已发布的交通事件信息
     * @return
     */
    @PostMapping("/getPublishIncidentInfo")
    public AjaxResult getPublishIncidentInfo(){
        List<Map> list = sdTrafficIncidentInfoService.getPublishIncidentInfo();

        return AjaxResult.success(list);
    }

}
