package com.tunnel.platform.controller.event;

import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.word.WordExportUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.event.*;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.mapper.event.SdEventFlowMapper;
import com.tunnel.business.mapper.event.SdEventHandleMapper;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.mapper.logRecord.SdOperationLogMapper;
import com.tunnel.business.service.event.ISdEventHandleService;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.utils.json.JSONObject;
import com.tunnel.platform.service.SdDeviceControlService;
import com.zc.common.core.websocket.WebSocketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件管理Controller
 *
 * @author gongfanfei
 * @date 2020-08-24
 */
@RestController
@RequestMapping("/event")
@Api(tags = "事件管理")
public class SdEventController extends BaseController
{
    @Autowired
    private ISdEventService sdEventService;

    @Autowired
    private ISdEventHandleService sdEventHandleService;

    @Autowired
    private SdEventHandleMapper sdEventHandleMapper;

    /**
     * 查询事件管理列表
     */
    @GetMapping("/list")
    public TableDataInfo<List<SysRole>> list(SdEvent sdEvent)
    {
        startPage();
        List<SdEvent> list = sdEventService.selectSdEventList(sdEvent);
        return getDataTable(list);
    }
    /**
     * 大屏查询事件报警列表
     */
    @GetMapping("/bigscreenEventList")
    public Result bigscreenEventList(SdEvent sdEvent)
    {
        List<SdEvent> list = sdEventService.selectSdEventList(sdEvent);
        return Result.success("成功", list);
    }

    /**
     * 导出事件管理列表
     */
    /*@Log(title = "事件管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdEvent sdEvent) throws IOException
    {
        List<SdEvent> list = sdEventService.selectSdEventList(sdEvent);
        ExcelUtil<SdEvent> util = new ExcelUtil<SdEvent>(SdEvent.class);
        util.exportExcel(response, list, "event");
    }*/

    /**
     * 导出事件详情
     */
    @PostMapping("/detailExport")
    public void detailExport(HttpServletResponse response,@RequestBody SdEvent sdEvent) throws IOException {
        sdEventService.detailExport(response,sdEvent);
    }

    /**
     * 获取事件管理详细信息
     */
    @GetMapping(value = "/{id}")
    @ApiOperation("获取事件管理详细信息")
    @ApiImplicitParam(name = "id", value = "事件管理ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result<SdEvent> getInfo(@PathVariable("id") Long id)
    {
        return Result.success(sdEventService.selectSdEventById(id));
    }

    /**
     * 新增事件管理
     */
    @Log(title = "事件管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增事件管理")
    public Result add(@RequestBody SdEvent sdEvent)
    {
        return Result.toResult(sdEventService.insertSdEvent(sdEvent));
    }

    /**
     * 修改事件管理
     */
    @Log(title = "事件管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改事件管理")
    public Result edit(@RequestBody SdEvent sdEvent)
    {
        return Result.toResult(sdEventService.updateSdEvent(sdEvent));
    }

    /**
     * 删除事件管理
     */
    @Log(title = "事件管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation("删除事件管理")
    @ApiImplicitParam(name = "ids", value = "需要删除的事件管理ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result remove(@PathVariable Long[] ids)
    {
        return Result.toResult(sdEventService.deleteSdEventByIds(ids));
    }

    /**
     * 根据id查询事件 ---视频
     * @param id
     * @return
     */
    @GetMapping("/evntId")
    public AjaxResult getById(@RequestParam("id") Long id){
        return AjaxResult.success(sdEventService.getById(id));
    }

    /**
     * 预警事件查询全部
     * @return
     */
    @GetMapping("/getEvent")
    public AjaxResult getEvent(SdEvent sdEvent)
    {
        return AjaxResult.success(sdEventService.getEvent(sdEvent));
    }

    /**
     * 统计今日事件
     * @return
     */
    @GetMapping("/getTodayEventCount")
    @ApiOperation("统计今日事件")
    public Result getTodayEventCount() {
        return Result.success(sdEventService.getTodayEventCount());
    }

    /**
     * 获取事件附近枪机
     * @param tunnelId
     * @param stakeNum
     * @param direction
     * @return
     */
    @GetMapping("/getEventCamera")
    @ApiOperation("获取事件附近枪机")
    public Result getEventCamera(@RequestParam("tunnelId") String tunnelId,
                                       @RequestParam("stakeNum") String stakeNum,
                                       @RequestParam("direction")String direction){
        return Result.success(sdEventService.getEventCamera(tunnelId,stakeNum,direction));
    }

    /**
     * 根据事件属性获取所在分区ID
     * @param tunnelId
     * @param stakeNum
     * @param direction
     * @return
     */
    @GetMapping("/getSubareaByStakeNum")
    @ApiOperation("根据事件桩号获取所在的分区")
    public Result getSubareaByStakeNum(@RequestParam("tunnelId") String tunnelId,
                                       @RequestParam("stakeNum") String stakeNum,
                                       @RequestParam("direction")String direction){
        return Result.success(sdEventService.getSubareaByStakeNum(tunnelId,stakeNum,direction));
    }



    @GetMapping("/getEventUntreatedNum")
    @ApiOperation("当日未处理事件数量")
    public Result getEventUntreatedNum() {
        return  Result.success(SpringUtils.getBean(SdEventMapper.class).getEventUntreatedNum());
    }

    @GetMapping("/eventPopAll")
    @ApiOperation("事件弹窗当日事件")
    public Result eventPopAll(String subIndex) {
        Integer allNum = SpringUtils.getBean(SdEventMapper.class).getEventUntreatedNum();
        Map<String,Object> map = new HashMap<>();
        map.put("total",allNum);
        map.put("data",sdEventService.eventPopAll(subIndex));
        return Result.success(map);
    }

    @GetMapping("/eventPopFault")
    @ApiOperation("事件弹窗设备故障")
    public Result eventPopFault(String subIndex) {
        SdEventMapper mapper = SpringUtils.getBean(SdEventMapper.class);
        Integer allNum = mapper.eventPopFaultCount();
        Map<String,Object> map = new HashMap<>();
        map.put("total",allNum);
        map.put("data",mapper.eventPopFault(subIndex));
        return Result.success(map);
    }

    @GetMapping("/performRecovery")
    @ApiOperation("应急调度一键恢复")
    public Result performRecovery(@RequestParam("eventId") String eventId,
                                  @RequestParam("handleId") String handleId) {
        List<SdOperationLog> logData = SpringUtils.getBean(SdOperationLogMapper.class).getEventOperationLog(eventId);
        if(logData.isEmpty()){
            return Result.error("处理失败，未获取到操作记录");
        }
        Map<String,Object> issuedParam = new HashMap<>();
        try {
            //默认值：诱导灯、疏散标志亮度为50，频率为60，疏散标志地址标号为255
            for(SdOperationLog data:logData){
                if(StrUtil.isBlank(data.getBeforeState())){
                    continue;
                }
                issuedParam.put("eventId",eventId);
                issuedParam.put("devId",data.getEqId());
                issuedParam.put("state",data.getBeforeState());
                issuedParam.put("controlType","4");
                //疏散标志
                if(data.getEqTypeId().equals(DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode())){
                    issuedParam.put("brightness","50");
                    issuedParam.put("frequency","60");
                    issuedParam.put("fireMark","255");
                    issuedParam.put("state","2");
                }
                //诱导灯
                if(data.getEqTypeId().equals(DevicesTypeEnum.YOU_DAO_DENG.getCode())){
                    issuedParam.put("brightness","50");
                    issuedParam.put("frequency","60");
                    if(data.getBeforeState().equals("2")){
                        issuedParam.put("fireMark","255");
                    }
                }
                //情报板
                if(data.getEqTypeId().equals(DevicesTypeEnum.MEN_JIA_VMS.getCode()) || data.getEqTypeId().equals(DevicesTypeEnum.VMS.getCode())){
                    continue;
                    //issuedParam.put("templateId",data.getBeforeState());
                }
                try {
                    issuedParam.put("operIp", InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                SpringUtils.getBean(SdDeviceControlService.class).controlDevices(issuedParam);
                issuedParam.clear();
            }
            //保存事件处理记录
            SdEventFlow flow = new SdEventFlow();
            flow.setFlowDescription("执行解除管控操作");
            flow.setEventId(eventId);
            flow.setFlowTime(DateUtils.getNowDate());
            flow.setFlowHandler(SecurityUtils.getUsername());
            JSONObject json = new JSONObject();
            json.put("eventFlow",flow);
            WebSocketService.broadcast("eventFlow",json);
            SpringUtils.getBean(SdEventFlowMapper.class).insertSdEventFlow(flow);
            //更新事件处置记录状态
            SdEventHandle sdEventHandle = new SdEventHandle();
            sdEventHandle.setId(Long.valueOf(handleId));
            //0:未完成 1:已完成'
            sdEventHandle.setEventState("1");
            sdEventHandle.setUpdateTime(DateUtils.getNowDate());
            sdEventHandleMapper.updateSdEventHandle(sdEventHandle);
        } catch (Exception e) {
            return Result.error("操作失败，请联系管理员");
        }
        return Result.success("操作成功!");
    }

    /**
     * 交通事件-复核-处置获取预案流程
     * @param sdEvent
     * @return
     */
    @GetMapping("/getHandle")
    public AjaxResult getHandle(SdEvent sdEvent){
        return sdEventService.getHandle(sdEvent);
    }

    /**
     * 主动安全-复核-处置获取预案流程
     * @param sdEvent
     * @return
     */
    @GetMapping("/getSafetyHandle")
    public AjaxResult getSafetyHandle(SdEvent sdEvent){
        return sdEventService.getSafetyHandle(sdEvent);
    }

    /**
     * 更新事件处置
     * @param sdEvent
     * @return
     */
    @GetMapping("/updateHandle")
    public AjaxResult updateHandle(SdEvent sdEvent){
        return sdEventHandleService.updateSdEventHandle(sdEvent);
    }

    /**
     * 应急调度关联策略
     * @param sdReservePlan
     * @return
     */
    @GetMapping("/getRelation")
    public AjaxResult getRelation(SdReservePlan sdReservePlan){
        return sdEventService.getRelation(sdReservePlan);
    }

    /**
     * 计算事故点
     * @param sdEvent
     * @return
     */
    @GetMapping("/getAccidentPoint")
    public AjaxResult getAccidentPoint(SdEvent sdEvent){
        return sdEventService.getAccidentPoint(sdEvent);
    }

    /**
     * 查询预案id
     * @param sdReservePlan
     * @return
     */
    @GetMapping("/getReserveId")
    public AjaxResult getReserveId(SdReservePlan sdReservePlan){
        return sdEventService.getReserveId(sdReservePlan);
    }

    /**
     * 查询应急调度出入口视频
     * @param sdEvent
     * @return
     */
    @GetMapping("/getEntranceExitVideo")
    public AjaxResult getEntranceExitVideo(SdEvent sdEvent){
        return sdEventService.getEntranceExitVideo(sdEvent);
    }

    /**
     * 查看事件详情
     *
     * @param sdEvent
     * @return
     */
    @ApiOperation("查看事件详情")
    @GetMapping("/getEventDetail")
    public AjaxResult getEventDetail(SdEvent sdEvent){
        return sdEventService.getEventDetail(sdEvent);
    }

    /**
     * 警情升级返现
     * @param sdEvent
     * @return
     */
    @GetMapping("/getSituationUpgrade")
    public AjaxResult getSituationUpgrade(SdEvent sdEvent){
        return sdEventService.getSituationUpgrade(sdEvent);
    }

    /**
     * 应急调度-处置设备详情（单条）
     * @param sdReserveProcess
     * @return
     */
    @GetMapping("/getManagementDevice")
    public AjaxResult getManagementDevice(SdReserveProcess sdReserveProcess){
        return sdEventService.getManagementDevice(sdReserveProcess);
    }

    /**
     * 应急调度-处置设备详情（阶段）
     * @param sdReservePlan
     * @return
     */
    @GetMapping("/getAllManagementDevices")
    public AjaxResult getAllManagementDevices(SdReservePlan sdReservePlan){
        return sdEventService.getAllManagementDevices(sdReservePlan);
    }

    /**
     * 修改警情升级
     * @param sdEvent
     * @return
     */
    @GetMapping("/updateSituationUpgrade")
    public Result updateSituationUpgrade(SdEvent sdEvent){
        return  Result.toResult(sdEventService.updateSituationUpgrade(sdEvent));
    }

    /**
     * 查询事件等级以及预案名称
     * @param sdEvent
     * @return
     */
    @GetMapping("/getEventInif")
    public AjaxResult getEventInif(SdEvent sdEvent){
        return sdEventService.getEventInif(sdEvent);
    }
}
