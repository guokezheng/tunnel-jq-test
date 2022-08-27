package com.tunnel.platform.controller.roadState;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.platform.domain.roadState.SdRoadDetectorData;
import com.tunnel.platform.domain.roadState.SdRoadDetectorDataDTO;
import com.tunnel.platform.service.road.ISdRoadDetectorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 路面监测信息Controller
 *
 * @author lihaodong
 * @date 2021-11-17
 */
@RestController
@RequestMapping("/business/roadState")
public class RoadDetectorDataController extends BaseController {
    @Autowired
    private ISdRoadDetectorDataService sdRoadDetectorDataService;

    /**
     * 查询路面监测信息列表
     */
//    @PreAuthorize("@ss.hasPermi('business:roadState:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdRoadDetectorDataDTO sdRoadDetectorDataDTO) {
        startPage();
        List<SdRoadDetectorDataDTO> list = sdRoadDetectorDataService.selectSdRoadDetectorDataList(sdRoadDetectorDataDTO);
        return getDataTable(list);
    }

    /**
     * 导出路面监测信息列表
     */
    @PreAuthorize("@ss.hasPermi('business:roadState:export')")
    @Log(title = "路面监测信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdRoadDetectorDataDTO sdRoadDetectorDataDTO) {
        List<SdRoadDetectorDataDTO> list = sdRoadDetectorDataService.selectSdRoadDetectorDataList(sdRoadDetectorDataDTO);
        ExcelUtil<SdRoadDetectorDataDTO> util = new ExcelUtil<SdRoadDetectorDataDTO>(SdRoadDetectorDataDTO.class);
        return util.exportExcel(list, "路面监测信息");
    }

    /**
     * 获取路面监测信息详细信息
     */
//    @PreAuthorize("@ss.hasPermi('business:roadState:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id) {
        return AjaxResult.success(sdRoadDetectorDataService.selectSdRoadDetectorDataById(id));
    }

    /**
     * 新增路面监测信息
     */
    @PreAuthorize("@ss.hasPermi('business:roadState:add')")
    @Log(title = "路面监测信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdRoadDetectorData sdRoadDetectorData) {
        return toAjax(sdRoadDetectorDataService.insertSdRoadDetectorData(sdRoadDetectorData));
    }

    /**
     * 修改路面监测信息
     */
    @PreAuthorize("@ss.hasPermi('business:roadState:edit')")
    @Log(title = "路面监测信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdRoadDetectorData sdRoadDetectorData) {
        return toAjax(sdRoadDetectorDataService.updateSdRoadDetectorData(sdRoadDetectorData));
    }

    /**
     * 删除路面监测信息
     */
    @PreAuthorize("@ss.hasPermi('business:roadState:remove')")
    @Log(title = "路面监测信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(sdRoadDetectorDataService.deleteSdRoadDetectorDataByIds(ids));
    }

    /**
     * 获取路面状态监测图数据
     */
    @GetMapping("statisticsRoadMonitoring")
    public AjaxResult statisticsRoadMonitoring() {
        return AjaxResult.success(sdRoadDetectorDataService.statisticsRoadMonitoring());
    }

    /**
     * 获取路面状态监测图数据
     */
    @GetMapping("statisticsWarningTrend")
    public AjaxResult statisticsWarningTrend() {
        return AjaxResult.success(sdRoadDetectorDataService.statisticsWarningTrend());
    }


    /**
     * 搜索路面状态监测图数据
     *
     * @param type
     * @param startTime
     * @param endTime
     * @param eqId
     * @return
     */
    @GetMapping("statisticsWarningTrendSearch")
    public AjaxResult statisticsWarningTrendSearch(String type, String startTime, String endTime, String eqId) {
        return AjaxResult.success(sdRoadDetectorDataService.statisticsWarningTrendSearch(type, startTime, endTime, eqId));
    }

    /**
     * 根据隧道和年、月、日查询路面温度、路面温度预警信息、积水厚度和积雪厚度
     */
//    @PreAuthorize("@ss.hasPermi('business:roadState:statisticsSearch')")
    @GetMapping("statisticsSearch")
    public AjaxResult statisticsSearch(String type, String startTime, String tunnelId) {
        return AjaxResult.success(sdRoadDetectorDataService.statisticsSearch(type, startTime, tunnelId));
    }

    /**
     * 根据隧道和年、月、日查询预警次数
     */
//    @PreAuthorize("@ss.hasPermi('business:roadState:statisticsSearcWarningInfo')")
    @GetMapping("statisticsSearcWarningInfo")
    public AjaxResult statisticsSearcWarningInfo(String type, String startTime, String tunnelId) {
        String str = ">5 0200 0222 0100   xxxx xxxx   150   00   000x  ";
        String eqNumber = "S29-LinYiCompany-BaiYanStation-002-LMZT-001";
        sdRoadDetectorDataService.dataAnalySis(str, eqNumber);
        return AjaxResult.success(sdRoadDetectorDataService.statisticsSearchWarningInfo(type, startTime, tunnelId));
    }

    /**
     * 根据隧道和年、月、日查询路面状态
     */
//    @PreAuthorize("@ss.hasPermi('business:roadState:statisticsSearchPavementstatus')")
    @GetMapping("statisticsSearchPavementstatus")
    public AjaxResult statisticsSearchPavementstatus(String type, String startTime, String tunnelId) {
        return AjaxResult.success(sdRoadDetectorDataService.statisticsSearchPavementstatus(type, startTime, tunnelId));
    }

    /**
     * 查询本年度路面最高问题
     * @param startTime
     * @return
     */
//    @PreAuthorize("@ss.hasPermi('business:roadState:statisticsSearcMaxPavementtemp')")
    @GetMapping("statisticsSearcMaxPavementtemp")
    public AjaxResult statisticsSearcMaxPavementtemp(String startTime) {
        return AjaxResult.success(sdRoadDetectorDataService.statisticsSearcMaxPavementtemp(startTime));
    }

    /**
     * 查询本年度最大预警次数
     * @param startTime
     * @return
     */
//    @PreAuthorize("@ss.hasPermi('business:roadState:statisticsSearcMaxpWarningInfo')")
    @GetMapping("statisticsSearcMaxpWarningInfo")
    public AjaxResult statisticsSearcMaxpWarningInfo(String startTime) {
        return AjaxResult.success(sdRoadDetectorDataService.statisticsSearcMaxpWarningInfo(startTime));
    }

    /**
     * 根据设备编号查询采集的最新数据
     * @param eqNumber
     * @return
     */
    @GetMapping("selectByEqDeno")
    public AjaxResult selectByEqDeno(@ApiIgnore @RequestParam Map<String, Object> eqNumber) {
        String eqId = eqNumber.get("eqId").toString();
        return AjaxResult.success(sdRoadDetectorDataService.selectByEqDeno(eqId));
    }
}
