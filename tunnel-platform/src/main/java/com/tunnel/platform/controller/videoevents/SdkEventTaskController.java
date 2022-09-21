package com.tunnel.platform.controller.videoevents;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.videoevents.SdCreateDocker;
import com.tunnel.business.domain.videoevents.SdkEventAnalysis;
import com.tunnel.business.domain.videoevents.SdkEventLane;
import com.tunnel.business.domain.videoevents.SdkEventTask;
import com.tunnel.business.service.videoevents.ISdCreateDockerService;
import com.tunnel.business.service.videoevents.ISdkEventAnalysisService;
import com.tunnel.business.service.videoevents.ISdkEventLaneService;
import com.tunnel.business.service.videoevents.ISdkEventTaskService;
import com.tunnel.business.utils.util.Docker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 事件任务表Controller
 * 
 * @author ruoyi
 * @date 2021-04-24
 */
@RestController
@RequestMapping("/eventtask")
public class SdkEventTaskController extends BaseController
{
    @Autowired
    private ISdkEventTaskService sdkEventTaskService;
    @Autowired
    private ISdkEventLaneService sdkEventLaneService;
    @Autowired
    private ISdkEventAnalysisService sdkEventAnalysisService;
    @Autowired
    private ISdCreateDockerService sdCreateDockerService;

    /**
     * 查询事件任务表列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdkEventTask sdkEventTask)
    {
        startPage();
        List<SdkEventTask> list = sdkEventTaskService.selectSdkEventTaskList(sdkEventTask);
        return getDataTable(list);
    }

    /**
     * 导出事件任务表列表
     */
    @Log(title = "事件任务表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdkEventTask sdkEventTask)
    {
        List<SdkEventTask> list = sdkEventTaskService.selectSdkEventTaskList(sdkEventTask);
        ExcelUtil<SdkEventTask> util = new ExcelUtil<SdkEventTask>(SdkEventTask.class);
        return util.exportExcel(list, "eventtask");
    }

    /**
     * 获取事件任务表详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(sdkEventTaskService.selectSdkEventTaskById(id));
    }


    /**
     * 新增事件任务表
     */
    @Log(title = "事件任务表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdkEventTask sdkEventTask)
    {
        try{
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            if (!sdkEventTask.getResults().contains("://")){
                sdkEventTask.setResults("http://"+hostAddress+":"+sdkEventTask.getResults()+"/receiveEvent/event");
            }
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return toAjax(sdkEventTaskService.insertSdkEventTask(sdkEventTask));
    }

    /**
     * 修改事件任务表
     */
    @PreAuthorize("@ss.hasPermi('system:eventtask:edit')")
    @Log(title = "事件任务表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdkEventTask sdkEventTask)
    {
        try{
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            if (!sdkEventTask.getResults().contains("://")){
                sdkEventTask.setResults("http://"+hostAddress+":"+sdkEventTask.getResults()+"/receiveEvent/event");
            }
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return toAjax(sdkEventTaskService.updateSdkEventTask(sdkEventTask));
    }

    /**
     * 删除事件任务表
     */
    @Log(title = "事件任务表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        for (Integer id: ids){
            SdkEventTask sdkEventTask = sdkEventTaskService.selectSdkEventTaskById(id);
            isDeleteTask(sdkEventTask);
        }
        return toAjax(sdkEventTaskService.deleteSdkEventTaskByIds(ids));
    }

    /**
     * 新增车道、事件任务表
     */
    @Log(title = "车道、事件任务表", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addTaskEvent")
    public AjaxResult addTaskEvent(@RequestBody JSONObject jsonObject)
    {

        SdkEventLane lane=new SdkEventLane();
        int taskId = Integer.parseInt(jsonObject.get("taskId").toString());
        sdkEventLaneService.deleteSdkEventLaneById(taskId);
        sdkEventAnalysisService.deleteSdkEventAnalysisById(taskId);
        lane.setTaskId(taskId);
        if (StringUtils.isNotNull(jsonObject.get("direction")) && !"direction".equals("")){
            String[] direction = jsonObject.get("direction").toString().trim().split("\\s+");
            lane.setStart(direction[0]);
            lane.setEnd(direction[1]);
            lane.setCoordinates(jsonObject.get("coordinates").toString().trim());
            lane.setVirtualLoop(jsonObject.get("virtualLoop").toString().trim());
            lane.setLaneType("BusLane");
            sdkEventLaneService.insertSdkEventLane(lane);
        }
        if (StringUtils.isNotNull(jsonObject.get("directionE")) && !jsonObject.get("directionE").toString().equals("")){
            String[] directionE = jsonObject.get("directionE").toString().trim().split("\\s+");
            lane.setStart(directionE[0]);
            lane.setEnd(directionE[1]);
            lane.setCoordinates(jsonObject.get("coordinatesE").toString().trim());
            lane.setVirtualLoop(jsonObject.get("virtualLoopE").toString().trim());
            lane.setLaneType("EmergencyLane");
            sdkEventLaneService.insertSdkEventLane(lane);
        }
        SdkEventAnalysis analysis=new SdkEventAnalysis();
        analysis.setTaskId(taskId);
        analysis.setCoordinates(jsonObject.get("arCoordinates").toString().trim());
        List<String> selectedArray=(ArrayList<String>)jsonObject.get("selectedArray");
        String incident="";
        for (String coordinates:selectedArray){
            incident+=coordinates.trim()+",";
        }
        analysis.setIncident(incident.substring(0,incident.length()-1));
//        sdkEventAnalysisService.insertSdkEventAnalysis(analysis);
        return toAjax(sdkEventAnalysisService.insertSdkEventAnalysis(analysis));
    }
    /**
     * 启动任务
     */
    @GetMapping(value = "/startEventTask/{id}")
    public AjaxResult startEventTask(@PathVariable("id") Integer id)
    {
        SdkEventTask sdkEventTask = sdkEventTaskService.selectSdkEventTaskById(id);
        List<SdkEventLane> sdkEventLanes = sdkEventLaneService.selectSdkEventLaneByTaskId(sdkEventTask.getId());
        List<SdkEventAnalysis> sdkEventAnalyses = sdkEventAnalysisService.selectSdkEventAnalysisByTaskId(sdkEventTask.getId());
        SdCreateDocker sdCreateDocker = sdCreateDockerService.selectSdCreateDockerById(sdkEventTask.getDockerId());
        if (sdkEventAnalyses.size()>0 && sdkEventLanes.size()>0){
            Boolean aBoolean = Docker.addTask(sdkEventTask, sdkEventLanes, sdkEventAnalyses, sdCreateDocker);
            if (aBoolean){
                return    AjaxResult.success(sdkEventTask);
            }
        }else {
            return AjaxResult.error("启动失败,请配置事件！");
        }
        return AjaxResult.error("启动失败,请检查网络和参数配置！");
    }
    /**
     * 查询任务状态
     */
    @GetMapping(value = "/searchEventTask/{id}")
    public AjaxResult searchEventTask(@PathVariable("id") Integer id)
    {
        SdkEventTask sdkEventTask = sdkEventTaskService.selectSdkEventTaskById(id);
        SdCreateDocker sdCreateDocker = sdCreateDockerService.selectSdCreateDockerById(sdkEventTask.getDockerId());
        JSONObject taskAllStatus = Docker.getTaskAllStatus(sdCreateDocker);
        if(StringUtils.isNotNull(taskAllStatus)){
            if(Integer.parseInt(taskAllStatus.get("taskNum").toString())>0){
                List<Map<String,Object>> taskStatus = (List<Map<String,Object>>)taskAllStatus.get("taskStatus");
                for (Map<String,Object> map:taskStatus){
                    if (map.get("channel_id").equals(sdkEventTask.getChannelId())){
                        return AjaxResult.success(map.get("status"));
                    }
                }
                return AjaxResult.error("查询失败，当前任务未启动");
            }
        }

        return AjaxResult.error("查询失败，当前任务未启动");
    }
    /**
     * 删除状态
     */
    @GetMapping(value = "/stopEventTask/{id}")
    public AjaxResult stopEventTask(@PathVariable("id") Integer id)
    {
        SdkEventTask sdkEventTask = sdkEventTaskService.selectSdkEventTaskById(id);
        if (isDeleteTask(sdkEventTask)){
            return AjaxResult.success(sdkEventTask);
        }
        return AjaxResult.error("暂停失败,请检查网络和参数配置！");
    }

    public boolean isDeleteTask(SdkEventTask sdkEventTask){
        SdCreateDocker sdCreateDocker = sdCreateDockerService.selectSdCreateDockerById(sdkEventTask.getDockerId());
        Boolean aBoolean = Docker.deleteTask(sdkEventTask.getChannelId(), sdCreateDocker);
        return aBoolean;
    }

}
