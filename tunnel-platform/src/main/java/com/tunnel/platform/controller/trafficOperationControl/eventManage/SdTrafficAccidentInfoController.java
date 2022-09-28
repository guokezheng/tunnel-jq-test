package com.tunnel.platform.controller.trafficOperationControl.eventManage;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.trafficOperationControl.activeTrafficFlowControl.SdTrafficIncidentMeasure;
import com.tunnel.business.domain.trafficOperationControl.controlConfig.SdControlConfigMeasure;
import com.tunnel.business.domain.trafficOperationControl.controlConfig.SdControlLevelConfig;
import com.tunnel.business.domain.trafficOperationControl.eventManage.IncidentAndAccidentInfo;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficAccidentInfo;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficIncidentInfo;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.trafficOperationControl.activeTrafficFlowControl.ActiveTrafficFlowService;
import com.tunnel.business.service.trafficOperationControl.activeTrafficFlowControl.ISdTrafficIncidentMeasureService;
import com.tunnel.business.service.trafficOperationControl.controlConfig.ISdControlConfigMeasureService;
import com.tunnel.business.service.trafficOperationControl.controlConfig.ISdControlLevelConfigService;
import com.tunnel.business.service.trafficOperationControl.eventManage.ISdTrafficAccidentInfoService;
import com.tunnel.business.service.trafficOperationControl.eventManage.ISdTrafficIncidentInfoService;
import com.tunnel.business.service.trafficOperationControl.eventManage.ISdTrafficIncidentProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 交通事故和清障信息Controller
 *
 * @author ruoyi
 * @date 2022-02-18
 */
@RestController
@RequestMapping("/trafficAccident/info")
public class SdTrafficAccidentInfoController extends BaseController
{
    @Autowired
    private ISdTrafficAccidentInfoService sdTrafficAccidentInfoService;

    @Autowired
    private ISdTrafficIncidentInfoService incidentInfoService;

    @Autowired
    private ISdTrafficIncidentProcessService incidentProcessService;

    @Autowired
    private ActiveTrafficFlowService activeTrafficFlowService;

    @Autowired
    private ISdTrafficIncidentMeasureService incidentMeasureService;

    @Autowired
    private ISdControlConfigMeasureService configMeasureService;

    @Autowired
    private ISdTunnelsService tunnelsService;

    @Autowired
    private ISdControlLevelConfigService levelConfigService;

    /**
     * 查询交通事故和清障信息列表
     */
    @PreAuthorize("@ss.hasPermi('trafficAccident:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdTrafficAccidentInfo sdTrafficAccidentInfo)
    {
        startPage();
        List<SdTrafficAccidentInfo> list = sdTrafficAccidentInfoService.selectSdTrafficAccidentInfoList(sdTrafficAccidentInfo);
        return getDataTable(list);
    }

    /**
     * 导出交通事故和清障信息列表
     */
    @PreAuthorize("@ss.hasPermi('trafficAccident:info:export')")
    @Log(title = "交通事故和清障信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdTrafficAccidentInfo sdTrafficAccidentInfo)
    {
        List<SdTrafficAccidentInfo> list = sdTrafficAccidentInfoService.selectSdTrafficAccidentInfoList(sdTrafficAccidentInfo);
        ExcelUtil<SdTrafficAccidentInfo> util = new ExcelUtil<SdTrafficAccidentInfo>(SdTrafficAccidentInfo.class);
        return util.exportExcel(list, "交通事故和清障信息数据");
    }

    /**
     * 获取交通事故和清障信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('trafficAccident:info:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdTrafficAccidentInfoService.selectSdTrafficAccidentInfoById(id));
    }

    /**
     * 新增交通事故和清障信息
     */
    @PreAuthorize("@ss.hasPermi('trafficAccident:info:add')")
    @Log(title = "交通事故和清障信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdTrafficAccidentInfo sdTrafficAccidentInfo)
    {
        return toAjax(sdTrafficAccidentInfoService.insertSdTrafficAccidentInfo(sdTrafficAccidentInfo));
    }

    /**
     * 修改交通事故和清障信息
     */
    @PreAuthorize("@ss.hasPermi('trafficAccident:info:edit')")
    @Log(title = "交通事故和清障信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdTrafficAccidentInfo sdTrafficAccidentInfo)
    {
        return toAjax(sdTrafficAccidentInfoService.updateSdTrafficAccidentInfo(sdTrafficAccidentInfo));
    }

//    /**
//     * 删除交通事故和清障信息
//     */
//    @PreAuthorize("@ss.hasPermi('trafficAccident:info:remove')")
//    @Log(title = "交通事故和清障信息", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(sdTrafficAccidentInfoService.deleteSdTrafficAccidentInfoByIds(ids));
//    }

    /**
     * 获取交通事故和清障信息
     * @return
     */
    @GetMapping(value = "/getTrafficAccidentInfo")
    public JSONObject getTrafficAccidentInfo(String incidentId){
        Long id = Long.valueOf(incidentId);
        SdTrafficIncidentInfo incidentInfo = incidentInfoService.selectSdTrafficIncidentInfoById(id);
        SdTrafficAccidentInfo accidentInfo = sdTrafficAccidentInfoService.getAccidentInfoByIncidentId(incidentId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("incidentInfo",incidentInfo);
        jsonObject.put("accidentInfo",accidentInfo);
        return jsonObject;
    }

    /**
     * 新增交通事故和清障信息
     * @return
     */
    @RequestMapping("/addAccidentInfo")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addAccidentInfo(@RequestBody IncidentAndAccidentInfo incidentAndAccidentInfo){
        SdTrafficIncidentInfo incidentInfo = incidentAndAccidentInfo.getIncidentInfo();
        SdTrafficAccidentInfo accidentInfo = incidentAndAccidentInfo.getAccidentInfo();

        incidentInfoService.insertSdTrafficIncidentInfo(incidentInfo);

        accidentInfo.setIncidentId(incidentInfo.getId());
        sdTrafficAccidentInfoService.insertSdTrafficAccidentInfo(accidentInfo);

        return AjaxResult.success();
    }

    /**
     * 修改交通事故和清障信息
     * @return
     */
    @RequestMapping("/updateAccidentInfo")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateAccidentInfo(@RequestBody IncidentAndAccidentInfo incidentAndAccidentInfo){
        SdTrafficIncidentInfo incidentInfo = incidentAndAccidentInfo.getIncidentInfo();
        SdTrafficAccidentInfo accidentInfo = incidentAndAccidentInfo.getAccidentInfo();

        incidentInfoService.updateSdTrafficIncidentInfo(incidentInfo);
        sdTrafficAccidentInfoService.updateSdTrafficAccidentInfo(accidentInfo);

        return AjaxResult.success();
    }

//    /**
//     * 删除交通事故和清障信息
//     */
//    @DeleteMapping("/delAccidentInfo")
//    @Transactional(rollbackFor = Exception.class)
//    public AjaxResult delAccidentInfo(Long id)
//    {
//        incidentInfoService.deleteSdTrafficIncidentInfoById(id);
//        sdTrafficAccidentInfoService.delAccidentInfoByIncidentId(id);
//
//        return AjaxResult.success();
//    }



    /**
     * *******************交通突发事件暂定目前几个字段，使用的前后台接口方法****************************
     */

    /**
     * 查询交通事故和清障信息列表
     */
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
        List<Map> list = sdTrafficAccidentInfoService.selectAccidentInfoList(incidentInfo);
        return getDataTable(list);
    }

    /**
     * 获取交通事故和清障信息
     * @return
     */
    @GetMapping(value = "/getAccident/{id}")
    public AjaxResult getAccident(@PathVariable("id") Long id){
        Map map = sdTrafficAccidentInfoService.getAccident(id);

        //获取管控措施列表、管控措施详情
        Map detailMap = activeTrafficFlowService.getIncidentInfoMeasure(id);
        map.putAll(detailMap);

        return AjaxResult.success(map);
    }

    /**
     * 新增交通事故和清障信息
     * @return
     */
    @RequestMapping("/addAccident")
    @Transactional( rollbackFor = Exception.class)
    public AjaxResult addAccident(@RequestBody JSONObject jsonObject){

        //隧道id
        String tunnelId = jsonObject.getString("tunnelId");
        //事件类型
        String incidentType = jsonObject.getString("incidentType");
        //事件级别
        String incidentGrade = jsonObject.getString("incidentGrade");
        //报告时间
        String reportTime = jsonObject.getString("reportTime");
        //方向
        String direction = jsonObject.getString("direction");
        //发生时间
        String occurTime = jsonObject.getString("occurTime");
        //事件内容
        String incidentContent = jsonObject.getString("incidentContent");

        //管控级别
        String controlLevel = jsonObject.getString("controlLevel");

        //管控类别
        String controlType = jsonObject.getString("controlType");



        SdControlLevelConfig levelConfig = new SdControlLevelConfig();
        levelConfig.setControlLevel(controlLevel);
        levelConfig.setControlType(controlType);

        SdControlLevelConfig configInfo = levelConfigService.getConfigInfo(levelConfig);
        if(configInfo == null){
            return AjaxResult.error("请选择有效的管控等级配置数据！");
        }


        SdTrafficIncidentInfo incidentInfo = new SdTrafficIncidentInfo();
        incidentInfo.setTunnelId(tunnelId);
        incidentInfo.setIncidentType(incidentType);
        incidentInfo.setIncidentGrade(incidentGrade);
        incidentInfo.setReportTime(reportTime);
        incidentInfo.setIncidentContent(incidentContent);
        incidentInfo.setOccurTime(occurTime);
        incidentInfoService.insertSdTrafficIncidentInfo(incidentInfo);

        SdTrafficAccidentInfo accidentInfo = new SdTrafficAccidentInfo();
        accidentInfo.setIncidentId(incidentInfo.getId());
        accidentInfo.setDirection(direction);
        sdTrafficAccidentInfoService.insertSdTrafficAccidentInfo(accidentInfo);


        //保存管控措施
        AjaxResult ajaxResult = incidentMeasureService.saveIncidentMeasure(configInfo,incidentInfo.getId());

        return ajaxResult;
    }

    /**
     * 修改交通事故和清障信息
     * @return
     */
    @RequestMapping("/updateAccident")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateAccident(@RequestBody JSONObject jsonObject){

        //事件id
        Long id = jsonObject.getLong("id");
        //隧道id
        String tunnelId = jsonObject.getString("tunnelId");
        //事件类型
        String incidentType = jsonObject.getString("incidentType");
        //事件级别
        String incidentGrade = jsonObject.getString("incidentGrade");
        //报告时间
        String reportTime = jsonObject.getString("reportTime");
        //方向
        String direction = jsonObject.getString("direction");
        //发生时间
        String occurTime = jsonObject.getString("occurTime");
        //事件内容
        String incidentContent = jsonObject.getString("incidentContent");

        //管控级别
        String controlLevel = jsonObject.getString("controlLevel");

        //管控类别
        String controlType = jsonObject.getString("controlType");

        SdControlLevelConfig levelConfig = new SdControlLevelConfig();
        levelConfig.setControlLevel(controlLevel);
        levelConfig.setControlType(controlType);

        SdControlLevelConfig configInfo = levelConfigService.getConfigInfo(levelConfig);
        if(configInfo == null){
            return AjaxResult.error("请选择有效的管控等级配置数据！");
        }

        SdTrafficIncidentInfo incidentInfo = new SdTrafficIncidentInfo();
        incidentInfo.setId(id);
        incidentInfo.setTunnelId(tunnelId);
        incidentInfo.setIncidentType(incidentType);
        incidentInfo.setIncidentGrade(incidentGrade);
        incidentInfo.setReportTime(reportTime);
        incidentInfo.setIncidentContent(incidentContent);
        incidentInfo.setOccurTime(occurTime);
        incidentInfoService.updateSdTrafficIncidentInfo(incidentInfo);

        SdTrafficAccidentInfo accidentInfo = new SdTrafficAccidentInfo();
        accidentInfo.setIncidentId(incidentInfo.getId());
        accidentInfo.setDirection(direction);
        //根据事件incidentId修改交通事故和清障信息
        sdTrafficAccidentInfoService.updateAccidentInfo(accidentInfo);

        //删除原有的措施列表
        incidentMeasureService.delMultiIncidentMeasure(incidentInfo.getId());


        //保存管控措施
        AjaxResult ajaxResult = incidentMeasureService.saveIncidentMeasure(configInfo,incidentInfo.getId());

        return ajaxResult;
    }

    /**
     * 删除交通事故和清障信息
     */
    @DeleteMapping("/{ids}")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult delAccident(@PathVariable Long[] ids)
    {
        for(Long id : ids) {
            //删除事件信息
            incidentInfoService.deleteSdTrafficIncidentInfoById(id);
            //删除交通事故、清障信息
            sdTrafficAccidentInfoService.delAccidentInfoByIncidentId(id);

            //删除事件关联的流程以及图片信息
            incidentProcessService.delProcessAndImage(id);

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
            List<SdControlConfigMeasure> list = configMeasureService.getConfigMeasureByLevelId(configLevelId);
            //###隧道，[发生交通事故，或执行道路清障],采取交通管制，限速或限行等措施
            SdTrafficIncidentInfo incidentInfo = incidentInfoService.selectSdTrafficIncidentInfoById(incidentId);
            String tunnelId = incidentInfo.getTunnelId();
            SdTunnels tunnel = tunnelsService.selectSdTunnelsById(tunnelId);
            if(tunnel != null){
                String tunnelName = tunnel.getTunnelName();
                String occurTime = incidentInfo.getOccurTime();
                String incidentType = incidentInfo.getIncidentType();
                content.append(sdTrafficAccidentInfoService.joinPublishContent(tunnelName,occurTime,incidentType,list));
//                String measureDetail = activeTrafficFlowService.getCompleteMeasureDetail(list);
//                content.append(tunnelName).append(",");
//                content.append(reportTime).append(",");
//                if(EmergencyIncidentTypeEnum.traffic_accident.getCode().equals(incidentType)){
//                    //交通事故
//                    content.append("发生交通事故").append(",");
//                }
//                if(EmergencyIncidentTypeEnum.barrier_clear.getCode().equals(incidentType)){
//                    //道路清障
//                    content.append("实施道路清障").append(",");
//                }
//                content.append(measureDetail);
            }

        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content",content);
        return AjaxResult.success(jsonObject);
    }

}
