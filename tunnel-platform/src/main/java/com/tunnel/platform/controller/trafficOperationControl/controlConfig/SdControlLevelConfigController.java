package com.tunnel.platform.controller.trafficOperationControl.controlConfig;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.platform.domain.trafficOperationControl.controlConfig.SdControlConfigCause;
import com.tunnel.platform.domain.trafficOperationControl.controlConfig.SdControlConfigMeasure;
import com.tunnel.platform.domain.trafficOperationControl.controlConfig.SdControlLevelConfig;
import com.tunnel.platform.service.trafficOperationControl.activeTrafficFlowControl.ActiveTrafficFlowService;
import com.tunnel.platform.service.trafficOperationControl.controlConfig.ISdControlConfigCauseService;
import com.tunnel.platform.service.trafficOperationControl.controlConfig.ISdControlConfigMeasureService;
import com.tunnel.platform.service.trafficOperationControl.controlConfig.ISdControlLevelConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管控等级配置Controller
 *
 * @author ruoyi
 * @date 2022-02-12
 */
@RestController
@RequestMapping("/controlConfig/config")
public class SdControlLevelConfigController extends BaseController
{
    @Autowired
    private ISdControlLevelConfigService sdControlLevelConfigService;

    @Autowired
    private ISdControlConfigCauseService causeService;

    @Autowired
    private ISdControlConfigMeasureService measureService;

    @Autowired
    private ActiveTrafficFlowService activeTrafficFlowService;

    /**
     * 查询管控等级配置列表
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdControlLevelConfig sdControlLevelConfig)
    {
        startPage();
        List<SdControlLevelConfig> list = sdControlLevelConfigService.selectSdControlLevelConfigList(sdControlLevelConfig);
        return getDataTable(list);
    }

    /**
     * 导出管控等级配置列表
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:config:export')")
    @Log(title = "管控等级配置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdControlLevelConfig sdControlLevelConfig)
    {
        List<SdControlLevelConfig> list = sdControlLevelConfigService.selectSdControlLevelConfigList(sdControlLevelConfig);
        ExcelUtil<SdControlLevelConfig> util = new ExcelUtil<SdControlLevelConfig>(SdControlLevelConfig.class);
        return util.exportExcel(list, "管控等级配置数据");
    }

    /**
     * 获取管控等级配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:config:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdControlLevelConfigService.selectSdControlLevelConfigById(id));
    }

    /**
     * 新增管控等级配置
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:config:add')")
    @Log(title = "管控等级配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdControlLevelConfig sdControlLevelConfig)
    {
        //新增前检查是否已有相同的管控类别、管控级别
        int num = sdControlLevelConfigService.querySameLevelConfig(sdControlLevelConfig);
        if(num > 0){
            return AjaxResult.error("已存在相同的管控类别、管控级别的数据！");
        }

        return toAjax(sdControlLevelConfigService.insertSdControlLevelConfig(sdControlLevelConfig));
    }

    /**
     * 修改管控等级配置
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:config:edit')")
    @Log(title = "管控等级配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdControlLevelConfig sdControlLevelConfig)
    {
        //修改前检查是否已有相同的管控类别、管控级别
        int num = sdControlLevelConfigService.querySameLevelConfig(sdControlLevelConfig);
        if(num > 0){
            return AjaxResult.error("已存在相同的管控类别、管控级别的数据！");
        }
        return toAjax(sdControlLevelConfigService.updateSdControlLevelConfig(sdControlLevelConfig));
    }

    /**
     * 删除管控等级配置
     */
    @PreAuthorize("@ss.hasPermi('controlConfig:config:remove')")
    @Log(title = "管控等级配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        causeService.delConfigCauseByLevelIds(ids);
        measureService.delConfigMeasureByLevelIds(ids);
        return toAjax(sdControlLevelConfigService.deleteSdControlLevelConfigByIds(ids));
    }


    /**
     * 保存配置措施
     * @return
     */
    @RequestMapping("/saveControlMeasure")
    public AjaxResult saveControlMeasure(@RequestBody JSONObject jsonObject){

        sdControlLevelConfigService.saveControlMeasure(jsonObject);
        return AjaxResult.success();
    }


    /**
     * 获取管控等级配置措施
     * @param controlLevelConfig
     * @return
     */
    @GetMapping(value = "/getControlMeasure")
    public JSONObject getControlMeasure(SdControlLevelConfig controlLevelConfig){
        Long configLevelId = controlLevelConfig.getId();
        List<SdControlConfigCause> causeList = causeService.getConfigCauseByLevelId(configLevelId);
        List<SdControlConfigMeasure> measureList = measureService.getConfigMeasureByLevelId(configLevelId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("causeList",causeList);
        jsonObject.put("measureList",measureList);
        return jsonObject;
    }

//    /**
//     * 查询配置的管控类别
//     * @return
//     */
//    @GetMapping("/getControlTypeList")
//    public AjaxResult getControlTypeList(){
//        List<Map> list = sdControlLevelConfigService.getControlTypeList();
//        return AjaxResult.success(list);
//    }
//
//
//    /**
//     * 根据管控类别查询配置的管控级别
//     * @return
//     */
//    @GetMapping("/getControlLevelByType")
//    public AjaxResult getControlLevelByType(String controlType){
//        List<Map> list = sdControlLevelConfigService.getControlLevelByType(controlType);
//        return AjaxResult.success(list);
//    }


    /**
     * 根据管控类别、管控级别查询配置的管控措施
     * @param controlType 管控类别
     * @param controlLevel 管控级别
     * @return
     */
    @GetMapping("/getControlMeasureByTypeLevel")
    public AjaxResult getControlMeasureByTypeLevel(String controlType,String controlLevel){
        SdControlLevelConfig levelConfig = new SdControlLevelConfig();
        levelConfig.setControlType(controlType);
        levelConfig.setControlLevel(controlLevel);
        levelConfig = sdControlLevelConfigService.getConfigInfo(levelConfig);

        JSONObject jsonObject = new JSONObject();
        if(levelConfig != null){
            Long levelConfigId = levelConfig.getId();

            List<SdControlConfigCause> causeList = causeService.getConfigCauseByLevelId(levelConfigId);
            //管控原因
            String controlReason = causeService.getControlCauseDescription(causeList);
            List<SdControlConfigMeasure> list = measureService.getConfigMeasureByLevelId(levelConfigId);
            //管控措施
            String measureDetail = activeTrafficFlowService.getCompleteMeasureDetail(list);

            jsonObject.put("measureList",list);
            jsonObject.put("measureDetail",measureDetail);
            jsonObject.put("controlReason",controlReason);
        }

        return AjaxResult.success(jsonObject);
    }


}
