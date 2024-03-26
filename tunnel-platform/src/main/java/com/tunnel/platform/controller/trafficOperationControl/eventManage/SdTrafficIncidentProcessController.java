package com.tunnel.platform.controller.trafficOperationControl.eventManage;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.trafficOperationControl.eventManage.IncidentAndProcess;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficIncidentProcess;
import com.tunnel.business.service.trafficOperationControl.eventManage.ISdTrafficImageService;
import com.tunnel.business.service.trafficOperationControl.eventManage.ISdTrafficIncidentProcessService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 交通事件-处理流程Controller
 *
 * @author ruoyi
 * @date 2022-02-18
 */
@RestController
@RequestMapping("/trafficIncident/process")
public class SdTrafficIncidentProcessController extends BaseController
{
    @Autowired
    private ISdTrafficIncidentProcessService sdTrafficIncidentProcessService;

    @Autowired
    private ISdTrafficImageService imageService;

    /**
     * 查询交通事件-处理流程列表
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:process:list')")
    @GetMapping("/list")
    @ApiOperation("查询交通事件-处理流程列表")
    public TableDataInfo list(SdTrafficIncidentProcess sdTrafficIncidentProcess)
    {
        startPage();
        List<SdTrafficIncidentProcess> list = sdTrafficIncidentProcessService.selectSdTrafficIncidentProcessList(sdTrafficIncidentProcess);
        return getDataTable(list);
    }

    /**
     * 导出交通事件-处理流程列表
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:process:export')")
    @Log(title = "交通事件-处理流程", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation("导出交通事件-处理流程列表")
    public AjaxResult export(SdTrafficIncidentProcess sdTrafficIncidentProcess)
    {
        List<SdTrafficIncidentProcess> list = sdTrafficIncidentProcessService.selectSdTrafficIncidentProcessList(sdTrafficIncidentProcess);
        ExcelUtil<SdTrafficIncidentProcess> util = new ExcelUtil<SdTrafficIncidentProcess>(SdTrafficIncidentProcess.class);
        return util.exportExcel(list, "交通事件-处理流程数据");
    }

    /**
     * 获取交通事件-处理流程详细信息
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:process:query')")
    @GetMapping(value = "/{processId}")
    @ApiOperation("获取交通事件-处理流程详细信息")
    public AjaxResult getInfo(@PathVariable("processId") Long processId)
    {
        return AjaxResult.success(sdTrafficIncidentProcessService.selectSdTrafficIncidentProcessByProcessId(processId));
    }

    /**
     * 新增交通事件-处理流程
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:process:add')")
    @Log(title = "交通事件-处理流程", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增交通事件-处理流程")
    public AjaxResult add(@RequestBody SdTrafficIncidentProcess sdTrafficIncidentProcess)
    {
        return toAjax(sdTrafficIncidentProcessService.insertSdTrafficIncidentProcess(sdTrafficIncidentProcess));
    }

    /**
     * 修改交通事件-处理流程
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:process:edit')")
    @Log(title = "交通事件-处理流程", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改交通事件-处理流程")
    public AjaxResult edit(@RequestBody SdTrafficIncidentProcess sdTrafficIncidentProcess)
    {
        return toAjax(sdTrafficIncidentProcessService.updateSdTrafficIncidentProcess(sdTrafficIncidentProcess));
    }

    /**
     * 删除交通事件-处理流程
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:process:remove')")
    @Log(title = "交通事件-处理流程", businessType = BusinessType.DELETE)
	@DeleteMapping("/{processIds}")
    @ApiOperation("删除交通事件-处理流程")
    public AjaxResult remove(@PathVariable Long[] processIds)
    {
        return toAjax(sdTrafficIncidentProcessService.deleteSdTrafficIncidentProcessByProcessIds(processIds));
    }

    /**
     * 获取交通事件-处理流程列表
     */
    @GetMapping(value = "/getProcessList")
    @ApiOperation("获取交通事件-处理流程列表")
    public AjaxResult getProcessList(Long incidentId)
    {
        return AjaxResult.success(sdTrafficIncidentProcessService.getProcessList(incidentId));
    }

    /**
     * 保存交通事件-处理流程
     */
    @PostMapping("/saveProcessList")
//    @Transactional(rollbackFor = Exception.class)
    @ApiOperation("保存交通事件-处理流程")
    public AjaxResult saveProcessList(@RequestBody IncidentAndProcess incidentAndProcess)
    {
        Long incidentId = incidentAndProcess.getMeasuresId();
        List<SdTrafficIncidentProcess> processList = incidentAndProcess.getEveList();

        //删除事件关联的流程以及图片信息
        sdTrafficIncidentProcessService.delProcessAndImage(incidentId);

        if(processList != null && processList.size() > 0){

            processList.forEach(incidentProcess -> {
                incidentProcess.setIncidentId(incidentId);
                //先保存process流程，再保存关联的图片信息
                sdTrafficIncidentProcessService.insertSdTrafficIncidentProcess(incidentProcess);

                Long processId = incidentProcess.getProcessId();
                List<SdTrafficImage> imgList = incidentProcess.getImgList();
                if(imgList != null && imgList.size() > 0){
                    imageService.insertMultiImage(processId,imgList);
                }
            });
        }

        return AjaxResult.success();
    }


}
