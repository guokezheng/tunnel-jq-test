package com.tunnel.platform.controller.trafficOperationControl.eventManage;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.platform.domain.trafficOperationControl.eventManage.SdTrafficIncidentPublishObject;
import com.tunnel.platform.service.trafficOperationControl.eventManage.ISdTrafficIncidentPublishObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 交通事件-发布对象Controller
 *
 * @author ruoyi
 * @date 2022-02-18
 */
@RestController
@RequestMapping("/trafficIncidentPublish/object")
public class SdTrafficIncidentPublishObjectController extends BaseController
{
    @Autowired
    private ISdTrafficIncidentPublishObjectService sdTrafficIncidentPublishObjectService;

    /**
     * 查询交通事件-发布对象列表
     */
    @PreAuthorize("@ss.hasPermi('trafficIncidentPublish:object:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdTrafficIncidentPublishObject sdTrafficIncidentPublishObject)
    {
        startPage();
        List<SdTrafficIncidentPublishObject> list = sdTrafficIncidentPublishObjectService.selectSdTrafficIncidentPublishObjectList(sdTrafficIncidentPublishObject);
        return getDataTable(list);
    }

    /**
     * 导出交通事件-发布对象列表
     */
    @PreAuthorize("@ss.hasPermi('trafficIncidentPublish:object:export')")
    @Log(title = "交通事件-发布对象", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdTrafficIncidentPublishObject sdTrafficIncidentPublishObject)
    {
        List<SdTrafficIncidentPublishObject> list = sdTrafficIncidentPublishObjectService.selectSdTrafficIncidentPublishObjectList(sdTrafficIncidentPublishObject);
        ExcelUtil<SdTrafficIncidentPublishObject> util = new ExcelUtil<SdTrafficIncidentPublishObject>(SdTrafficIncidentPublishObject.class);
        return util.exportExcel(list, "交通事件-发布对象数据");
    }

    /**
     * 获取交通事件-发布对象详细信息
     */
    @PreAuthorize("@ss.hasPermi('trafficIncidentPublish:object:query')")
    @GetMapping(value = "/{objectId}")
    public AjaxResult getInfo(@PathVariable("objectId") Long objectId)
    {
        return AjaxResult.success(sdTrafficIncidentPublishObjectService.selectSdTrafficIncidentPublishObjectByObjectId(objectId));
    }

    /**
     * 新增交通事件-发布对象
     */
    @PreAuthorize("@ss.hasPermi('trafficIncidentPublish:object:add')")
    @Log(title = "交通事件-发布对象", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdTrafficIncidentPublishObject sdTrafficIncidentPublishObject)
    {
        return toAjax(sdTrafficIncidentPublishObjectService.insertSdTrafficIncidentPublishObject(sdTrafficIncidentPublishObject));
    }

    /**
     * 修改交通事件-发布对象
     */
    @PreAuthorize("@ss.hasPermi('trafficIncidentPublish:object:edit')")
    @Log(title = "交通事件-发布对象", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdTrafficIncidentPublishObject sdTrafficIncidentPublishObject)
    {
        return toAjax(sdTrafficIncidentPublishObjectService.updateSdTrafficIncidentPublishObject(sdTrafficIncidentPublishObject));
    }

    /**
     * 删除交通事件-发布对象
     */
    @PreAuthorize("@ss.hasPermi('trafficIncidentPublish:object:remove')")
    @Log(title = "交通事件-发布对象", businessType = BusinessType.DELETE)
	@DeleteMapping("/{objectIds}")
    public AjaxResult remove(@PathVariable Long[] objectIds)
    {
        return toAjax(sdTrafficIncidentPublishObjectService.deleteSdTrafficIncidentPublishObjectByObjectIds(objectIds));
    }
}
