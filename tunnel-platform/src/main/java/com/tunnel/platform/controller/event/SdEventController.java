package com.tunnel.platform.controller.event;

import com.google.gson.JsonObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdEventFlow;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.mapper.event.SdEventFlowMapper;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.mapper.logRecord.SdOperationLogMapper;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.utils.json.JSONObject;
import com.tunnel.platform.service.SdDeviceControlService;
import com.zc.common.core.websocket.WebSocketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 查询事件管理列表
     */
    @GetMapping("/list")
    @ApiOperation("查询事件管理列表")
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
    @ApiOperation("大屏查询事件报警列表")
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
    public Result performRecovery(String eventId) {
        List<SdOperationLog> logData = SpringUtils.getBean(SdOperationLogMapper.class).getEventOperationLog(eventId);
        if(logData.isEmpty()){
            return Result.error("处理失败，未获取到操作记录");
        }
        Map<String,Object> map = new HashMap<>();
        try {
            //默认值：诱导灯、疏散标志亮度为50，频率为60，疏散标志地址标号为255
            for(SdOperationLog data:logData){
                map.put("devId",data.getEqId());
                if(data.getBeforeState()==null){
                    continue;
                }
                map.put("state",data.getBeforeState());
                map.put("controlType","4");
                map.put("eventId",eventId);
                //疏散标志默认值
                if(data.getEqTypeId()==30L){
                    map.put("brightness","50");
                    map.put("frequency","60");
                    map.put("fireMark","255");
                }
                //诱导灯默认值
                if(data.getEqTypeId()==31L){
                    map.put("brightness","50");
                    map.put("frequency","60");
                }
                try {
                    map.put("operIp", InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                SpringUtils.getBean(SdDeviceControlService.class).controlDevices(map);
                map.clear();
            }
            //保存事件处理记录
            SdEventFlow flow = new SdEventFlow();
            flow.setFlowDescription("执行一键恢复操作");
            flow.setEventId(eventId);
            flow.setFlowTime(DateUtils.getNowDate());
            flow.setFlowHandler(SecurityUtils.getUsername());
            JSONObject json = new JSONObject();
            json.put("eventFlow",flow);
            WebSocketService.broadcast("eventFlow",json);
            SpringUtils.getBean(SdEventFlowMapper.class).insertSdEventFlow(flow);
        } catch (Exception e) {
            return Result.error("操作失败，数据处理异常");
        }
        return Result.success("操作成功!");
    }
}
