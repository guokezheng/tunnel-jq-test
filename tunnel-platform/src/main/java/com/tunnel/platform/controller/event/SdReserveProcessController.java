package com.tunnel.platform.controller.event;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.event.*;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.event.SdReserveProcessMapper;
import com.tunnel.business.mapper.event.SdStrategyMapper;
import com.tunnel.business.mapper.event.SdStrategyRlMapper;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.event.ISdReserveProcessService;
import com.tunnel.platform.service.SdDeviceControlService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 预案流程节点Controller
 * 
 * @author ruoyi
 * @date 2022-09-02
 */
@RestController
@RequestMapping("/plan/process")
public class SdReserveProcessController extends BaseController
{
    @Autowired
    private ISdReserveProcessService sdReserveProcessService;

    @Autowired
    private SdDeviceControlService sdDeviceControlService;

    @Autowired
    private ISdEventService sdEventService;


    /**
     * 查询预案流程节点列表
     */
    @PreAuthorize("@ss.hasPermi('plan:process:list')")
    @GetMapping("/list")
    @ApiOperation("查询预案流程节点列表")
    public TableDataInfo list(SdReserveProcess sdReserveProcess)
    {
        startPage();
        List<SdReserveProcess> list = sdReserveProcessService.selectSdReserveProcessList(sdReserveProcess);
        return getDataTable(list);
    }

    /**
     * 导出预案流程节点列表
     */
    @PreAuthorize("@ss.hasPermi('plan:process:export')")
    @GetMapping("/export")
    @ApiOperation("导出预案流程节点列表")
    public AjaxResult export(SdReserveProcess sdReserveProcess)
    {
        List<SdReserveProcess> list = sdReserveProcessService.selectSdReserveProcessList(sdReserveProcess);
        ExcelUtil<SdReserveProcess> util = new ExcelUtil<SdReserveProcess>(SdReserveProcess.class);
        return util.exportExcel(list, "预案流程节点数据");
    }

    /**
     * 获取预案流程节点详细信息
     */
    @PreAuthorize("@ss.hasPermi('plan:process:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取预案流程节点详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdReserveProcessService.selectSdReserveProcessById(id));
    }

    @GetMapping(value = "/getReservePlanProcess/{id}")
    @ApiOperation("获取预案流程节点")
    public AjaxResult getReservePlanProcess(@PathVariable("id") Long id)
    {
        List<SdReserveProcess> processList = SpringUtils.getBean(SdReserveProcessMapper.class).getProcessList(id);
        processList.stream().forEach(item -> {
            item.getProcessesList().stream().forEach(temp -> {
                temp.setEquipmentList(Arrays.asList(temp.getEquipments().split(",")));
            });
        });
        return AjaxResult.success(processList);
    }

    /**
     * 新增预案流程节点
     */
    @PreAuthorize("@ss.hasPermi('plan:process:add')")
    @PostMapping("/add")
    @ApiOperation("添加预案流程节点")
    public AjaxResult add(@RequestBody SdReserveProcess sdReserveProcess)
    {
        return toAjax(sdReserveProcessService.insertSdReserveProcess(sdReserveProcess));
    }

    /**
     * 批量添加预案流程节点
     *
     * @param sdReserveProcess
     * @return
     */
    //@PreAuthorize("@ss.hasPermi('plan:process:add')")
    @PostMapping
    @ApiOperation("批量添加预案流程节点")
    public AjaxResult add(@RequestBody SdReserveProcessModel sdReserveProcess)
    {
        return toAjax(sdReserveProcessService.batchSdReserveProcessed(sdReserveProcess));
    }

    /**
     * 修改预案流程节点
     */
    @PreAuthorize("@ss.hasPermi('plan:process:edit')")
    @PutMapping
    @ApiOperation("修改预案流程节点")
    public AjaxResult edit(@RequestBody SdReserveProcess sdReserveProcess)
    {
        return toAjax(sdReserveProcessService.updateSdReserveProcess(sdReserveProcess));
    }

    /**
     * 删除预案流程节点
     */
    @PreAuthorize("@ss.hasPermi('plan:process:remove')")
	@DeleteMapping("/{ids}")
    @ApiOperation("批量删除节点信息")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdReserveProcessService.deleteSdReserveProcessByIds(ids));
    }

    /**
     * 根据预案id获得预案节点信息
     * @param reserveId
     * @return
     */
    @GetMapping("/getListByRId")
    @ApiOperation("根据预案id获得预案节点信息")
    public AjaxResult getReserveProcessByReserveId(Long reserveId) {
        return AjaxResult.success(sdReserveProcessService.selectSdReserveProcessByRId(reserveId));
    }

    /**
     * 预览展示
     * @param reserveId
     * @return
     */
    @GetMapping("/previewDisplay")
    @ApiOperation("预览展示")
    public List<Map> previewDisplay(Long reserveId,String eventId) {
        List<Map> mapList = sdReserveProcessService.selectPreviewDisplay(reserveId,eventId);
        return mapList;
    }


    /**
     * 预案执行
     * @param stringObjectMap
     * @return
     * @throws UnknownHostException
     */
    @PostMapping("/implement")
    @ApiModelProperty("预案执行")
    public AjaxResult implement(@RequestBody Map<String, String> stringObjectMap) throws UnknownHostException {
        String eventId = stringObjectMap.get("eventId");
        SdEvent event = sdEventService.selectSdEventById(Long.parseLong(eventId));
        Map<String,Object> issuedParam = new HashMap<>();
        Long reserveId = Long.parseLong(stringObjectMap.get("reserveId"));
        //预案流程节点
        List<SdReserveProcess> reserveProcesses = sdReserveProcessService.selectSdReserveProcessByRId(reserveId);
        if(reserveProcesses.size() < 1){
            return AjaxResult.error("当前预案未匹配到可执行策略");
        }
        Integer result = -1;
        //保存处理流程
        result = sdReserveProcessService.planImplementa(eventId,reserveId);
        for (SdReserveProcess process : reserveProcesses){
            List<SdStrategyRl> rlList = SpringUtils.getBean(SdStrategyRlMapper.class).selectSdStrategyRlByStrategyId(process.getStrategyId());
            if(rlList.size()<1){
                continue;
            }
            for (SdStrategyRl rl : rlList) {
                //疏散标志控制逻辑
                if(rl.getEqTypeId().equals("30")){
                    SdDevices searchObject = new SdDevices();
                    searchObject.setEqTunnelId(event.getTunnelId());
                    searchObject.setEqType(30L);
                    searchObject.setEqDirection(event.getDirection());
                    //事故点整形桩号
                    int compareValue = Integer.valueOf(event.getStakeNum().replace("K","").replace("+","").replace(" ",""));
                    List<SdDevices> list = SpringUtils.getBean(SdDevicesMapper.class).selectSdDevicesList(searchObject);
                    //同一方向上的疏散标志整形桩号去重
                    int[] allNum = list.stream().filter(s-> StringUtils.isNotBlank(s.getFEqId()))
                            .mapToInt(s->s.getPileNum().intValue()).distinct().toArray();
                    //查找事故点最近的疏散标志
                    int index = Math.abs(compareValue-allNum[0]);
                    int closest = allNum[0];
                    for (int i=0;i<allNum.length;i++) {
                        int abs = Math.abs(compareValue-allNum[i]);
                        if(abs <= index){
                            index = abs;
                            closest = allNum[i];
                        }
                    }
                    Long pile = new Long((long)closest);
                    list = list.stream().filter(devices->devices.getPileNum().equals(pile)).collect(Collectors.toList());
                    if(list.size()<1){
                        continue;
                    }
                    //报警点位设备ID
                    String alarmPointEqId = list.get(0).getEqId();
                    //fireMark标号位置信息
                    String fireMark = "0";
                    //需要下发的状态
                    String controlStatus = rl.getState();
                    //1关闭 2常亮 5报警
                    if(controlStatus.equals("5")){
                        fireMark = list.get(0).getQueryPointAddress();
                    }else if(controlStatus.equals("2")){
                        fireMark = "255";
                    }
                    //下发设备
                    issuedParam.put("brightness","50");
                    issuedParam.put("frequency","60");
                    issuedParam.put("fireMark",fireMark);
                    issuedParam.put("state",controlStatus);
                    issuedParam.put("devId",alarmPointEqId);
                    issuedParam.put("eventId",eventId);
                    issuedParam.put("controlType","4");
                    issuedParam.put("operIp", InetAddress.getLocalHost().getHostAddress());
                    result = sdDeviceControlService.controlDevices(issuedParam);
                    issuedParam.clear();
                }else{
                    String[] split = rl.getEquipments().split(",");
                    for (String devId : split){
                        issuedParam.put("devId",devId);
                        issuedParam.put("state",rl.getState());
                        issuedParam.put("eventId",eventId);
                        issuedParam.put("controlType","4");
                        //诱导灯默认值
                        if(rl.getEqTypeId().equals("31")){
                            issuedParam.put("brightness","50");
                            issuedParam.put("frequency","60");
                        }
                        issuedParam.put("operIp", InetAddress.getLocalHost().getHostAddress());
                        result = sdDeviceControlService.controlDevices(issuedParam);
                        issuedParam.clear();
                    }
                }
            }
        }
        return AjaxResult.success(result);
    }
}
