package com.tunnel.platform.controller.trafficOperationControl.eventManage;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.platform.domain.dataInfo.SdTunnels;
import com.tunnel.platform.domain.trafficOperationControl.activeTrafficFlowControl.SdTrafficIncidentMeasure;
import com.tunnel.platform.domain.trafficOperationControl.controlConfig.SdControlConfigCause;
import com.tunnel.platform.domain.trafficOperationControl.controlConfig.SdControlConfigMeasure;
import com.tunnel.platform.domain.trafficOperationControl.controlConfig.SdControlLevelConfig;
import com.tunnel.platform.domain.trafficOperationControl.eventManage.IncidentAndControlInfo;
import com.tunnel.platform.domain.trafficOperationControl.eventManage.SdTrafficControlInfo;
import com.tunnel.platform.domain.trafficOperationControl.eventManage.SdTrafficIncidentInfo;
import com.tunnel.platform.domain.trafficOperationControl.eventManage.SdTrafficIncidentPublishObject;
import com.tunnel.platform.service.dataInfo.ISdTunnelsService;
import com.tunnel.platform.service.trafficOperationControl.activeTrafficFlowControl.ActiveTrafficFlowService;
import com.tunnel.platform.service.trafficOperationControl.activeTrafficFlowControl.ISdTrafficIncidentMeasureService;
import com.tunnel.platform.service.trafficOperationControl.controlConfig.ISdControlConfigCauseService;
import com.tunnel.platform.service.trafficOperationControl.controlConfig.ISdControlConfigMeasureService;
import com.tunnel.platform.service.trafficOperationControl.controlConfig.ISdControlLevelConfigService;
import com.tunnel.platform.service.trafficOperationControl.eventManage.ISdTrafficControlInfoService;
import com.tunnel.platform.service.trafficOperationControl.eventManage.ISdTrafficIncidentInfoService;
import com.tunnel.platform.service.trafficOperationControl.eventManage.ISdTrafficIncidentPublishObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 交通管制信息Controller
 *
 * @author ruoyi
 * @date 2022-02-16
 */
@RestController
@RequestMapping("/trafficControl/info")
public class SdTrafficControlInfoController extends BaseController
{
    @Autowired
    private ISdTrafficControlInfoService sdTrafficControlInfoService;

    @Autowired
    private ISdTrafficIncidentInfoService incidentInfoService;

    @Autowired
    private ISdTrafficIncidentPublishObjectService publishObjectService;

    @Autowired
    private ISdTrafficIncidentMeasureService incidentMeasureService;

    @Autowired
    private ISdControlConfigCauseService configCauseService;

    @Autowired
    private ISdControlConfigMeasureService configMeasureService;

    @Autowired
    private ActiveTrafficFlowService activeTrafficFlowService;

    @Autowired
    private ISdTunnelsService tunnelsService;

    @Autowired
    private ISdControlLevelConfigService levelConfigService;

    /**
     * 查询交通管制信息列表
     */
    @PreAuthorize("@ss.hasPermi('trafficControl:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdTrafficControlInfo sdTrafficControlInfo)
    {
        startPage();
        List<SdTrafficControlInfo> list = sdTrafficControlInfoService.selectSdTrafficControlInfoList(sdTrafficControlInfo);
        return getDataTable(list);
    }

    /**
     * 查询交通管制信息列表
     */
    @PreAuthorize("@ss.hasPermi('trafficControl:info:getList')")
    @GetMapping("/getList")
    public TableDataInfo getList(SdTrafficIncidentInfo incidentInfo)
    {
        Object obj = incidentInfo.getParams().get("occurTime");
        String occurTimeQuery = Optional.ofNullable(obj).orElse("").toString();
        if(!occurTimeQuery.isEmpty()){
            String[] array = occurTimeQuery.split(",");
            if(array.length == 2){
                incidentInfo.getParams().put("startTime",array[0]);
                incidentInfo.getParams().put("endTime",array[1]);
            }
        }
        startPage();
        List<Map> list = sdTrafficControlInfoService.selectControlInfoList(incidentInfo);
        return getDataTable(list);
    }

    /**
     * 导出交通管制信息列表
     */
    @PreAuthorize("@ss.hasPermi('trafficControl:info:export')")
    @Log(title = "交通管制信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdTrafficControlInfo sdTrafficControlInfo)
    {
        List<SdTrafficControlInfo> list = sdTrafficControlInfoService.selectSdTrafficControlInfoList(sdTrafficControlInfo);
        ExcelUtil<SdTrafficControlInfo> util = new ExcelUtil<SdTrafficControlInfo>(SdTrafficControlInfo.class);
        return util.exportExcel(list, "交通管制信息数据");
    }

    /**
     * 获取交通管制信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('trafficControl:info:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdTrafficControlInfoService.selectSdTrafficControlInfoById(id));
    }

    /**
     * 新增交通管制信息
     */
    @PreAuthorize("@ss.hasPermi('trafficControl:info:add')")
    @Log(title = "交通管制信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdTrafficControlInfo sdTrafficControlInfo)
    {
        return toAjax(sdTrafficControlInfoService.insertSdTrafficControlInfo(sdTrafficControlInfo));
    }

    /**
     * 修改交通管制信息
     */
    @PreAuthorize("@ss.hasPermi('trafficControl:info:edit')")
    @Log(title = "交通管制信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdTrafficControlInfo sdTrafficControlInfo)
    {
        return toAjax(sdTrafficControlInfoService.updateSdTrafficControlInfo(sdTrafficControlInfo));
    }

//    /**
//     * 删除交通管制信息
//     */
//    @PreAuthorize("@ss.hasPermi('trafficControl:info:remove')")
//    @Log(title = "交通管制信息", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(sdTrafficControlInfoService.deleteSdTrafficControlInfoByIds(ids));
//    }

    /**
     * 获取交通管制信息
     * @return
     */
    @GetMapping(value = "/getTrafficControlInfo/{id}")
    public JSONObject getTrafficControlInfo(@PathVariable("id") Long incidentId){
        JSONObject jsonObject = new JSONObject();

        SdTrafficIncidentInfo incidentInfo = incidentInfoService.selectSdTrafficIncidentInfoById(incidentId);
        SdTrafficControlInfo controlInfo = sdTrafficControlInfoService.getControlInfoByIncidentId(incidentId);

        //获取管控措施列表、管控措施详情
        Map map = activeTrafficFlowService.getIncidentInfoMeasure(incidentId);
        jsonObject.putAll(map);

        //获取发布对象列表
        List<SdTrafficIncidentPublishObject> publishObjectList = publishObjectService.getPublishObjectByIncidentId(incidentId);
        List<String> list = new ArrayList<>();
        publishObjectList.forEach(publishObject -> {
            list.add(publishObject.getPublishObject());
        });
        incidentInfo.setPublishObject(list);

        jsonObject.put("incidentInfo",incidentInfo);
        jsonObject.put("controlInfo",controlInfo);
        return jsonObject;
    }

    /**
     * 新增交通管制事件信息
     * @return
     */
    @PostMapping("/addControlInfo")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addControlInfo(@RequestBody IncidentAndControlInfo incidentAndControlInfo){
        SdTrafficIncidentInfo incidentInfo = incidentAndControlInfo.getIncidentInfo();
        SdTrafficControlInfo controlInfo = incidentAndControlInfo.getControlInfo();
        SdControlLevelConfig levelConfig = incidentAndControlInfo.getLevelConfig();

        SdControlLevelConfig configInfo = levelConfigService.getConfigInfo(levelConfig);
        if(configInfo == null){
            return AjaxResult.error("请选择有效的管控等级配置数据！");
        }

        incidentInfoService.insertSdTrafficIncidentInfo(incidentInfo);

        Long incidentId = incidentInfo.getId();

        //保存发布对象信息
        List<String> publishObjectList = incidentInfo.getPublishObject();
        if(publishObjectList != null && publishObjectList.size() > 0){
            publishObjectService.savePublishObject(publishObjectList,incidentId);
        }


        controlInfo.setIncidentId(incidentId);
        sdTrafficControlInfoService.insertSdTrafficControlInfo(controlInfo);

        //保存管控措施
        AjaxResult ajaxResult = incidentMeasureService.saveIncidentMeasure(configInfo,incidentId);

        return ajaxResult;
    }

    /**
     * 修改交通管制事件信息
     * @return
     */
    @PutMapping("/updateControlInfo")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateControlInfo(@RequestBody IncidentAndControlInfo incidentAndControlInfo){
        SdTrafficIncidentInfo incidentInfo = incidentAndControlInfo.getIncidentInfo();
        SdTrafficControlInfo controlInfo = incidentAndControlInfo.getControlInfo();

        SdControlLevelConfig levelConfig = incidentAndControlInfo.getLevelConfig();
        SdControlLevelConfig configInfo = levelConfigService.getConfigInfo(levelConfig);
        if(configInfo == null){
            return AjaxResult.error("请选择有效的管控等级配置数据！");
        }

        Long incidentId = incidentInfo.getId();
        //先删除发布对象记录
        publishObjectService.delMultiPublishObject(incidentId);
        //再添加发布对象记录
        List<String> publishObjectList = incidentInfo.getPublishObject();
        if(publishObjectList != null && publishObjectList.size() > 0){
            publishObjectService.savePublishObject(publishObjectList,incidentId);
        }

        incidentInfoService.updateSdTrafficIncidentInfo(incidentInfo);
        sdTrafficControlInfoService.updateSdTrafficControlInfo(controlInfo);

        //删除原有的措施列表
        incidentMeasureService.delMultiIncidentMeasure(incidentId);

        //保存管控措施
        AjaxResult ajaxResult = incidentMeasureService.saveIncidentMeasure(configInfo,incidentId);


        return ajaxResult;
    }

    /**
     * 删除交通管制信息
     */
    @DeleteMapping("/{ids}")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult delControlInfo(@PathVariable Long[] ids)
    {

        for(Long id : ids){
            //删除发布对象记录
            publishObjectService.delMultiPublishObject(id);

            //删除事件信息
            incidentInfoService.deleteSdTrafficIncidentInfoById(id);
            sdTrafficControlInfoService.delControlInfoByIncidentId(id);

            //删除关联的管控措施
            incidentMeasureService.delMultiIncidentMeasure(id);
        }

        return AjaxResult.success();
    }


    /**
     * 获取发布内容
     * @param incidentId 事件id
     * @return
     */
    @GetMapping(value = "/getPublishContent/{id}")
    public AjaxResult getPublishContent(@PathVariable("id") Long incidentId){
        StringBuffer content = new StringBuffer();
        //查询关联的管控措施
        List<SdTrafficIncidentMeasure> measureList = incidentMeasureService.getIncidentMeasureByIncidentId(incidentId);
        //获取管控措施列表
        if(measureList != null && measureList.size() > 0){
            Long configLevelId = measureList.get(0).getConfigLevelId();
            List<SdControlConfigCause> causeList = configCauseService.getConfigCauseByLevelId(configLevelId);
            //获取管控原因的描述信息
            String causeDesc = configCauseService.getControlCauseDescription(causeList);

            List<SdControlConfigMeasure> list = configMeasureService.getConfigMeasureByLevelId(configLevelId);
            //###隧道，采取交通管制，限速或限行等措施
            SdTrafficIncidentInfo incidentInfo = incidentInfoService.selectSdTrafficIncidentInfoById(incidentId);
            String tunnelId = incidentInfo.getTunnelId();
            SdTunnels tunnel = tunnelsService.selectSdTunnelsById(tunnelId);
            if(tunnel != null){
                String tunnelName = tunnel.getTunnelName();
                content.append(tunnelName).append( ",");

                String occurTime = incidentInfo.getOccurTime();
                content.append(occurTime).append(",");

                content.append(causeDesc).append(",");

                String measureDetail = activeTrafficFlowService.getCompleteMeasureDetail(list);
                content.append(measureDetail);
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content",content);
        return AjaxResult.success(jsonObject);
    }

}
