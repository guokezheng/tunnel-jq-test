package com.tunnel.platform.controller.event;

import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.word.WordExportUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.tunnel.business.datacenter.domain.enumeration.*;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.event.*;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.mapper.event.SdEventFlowMapper;
import com.tunnel.business.mapper.event.SdEventHandleMapper;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.mapper.logRecord.SdOperationLogMapper;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.event.ISdEventHandleService;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.event.impl.SdEventServiceImpl;
import com.tunnel.deal.corniceTunnelRobot.CorniceTunnelRobot;
import com.tunnel.deal.corniceTunnelRobot.domain.StatusDto;
import com.tunnel.platform.controller.deviceControl.RobotController;
import com.tunnel.platform.service.SdDeviceControlService;
import com.tunnel.platform.service.deviceControl.PhoneSpkService;
import com.zc.common.core.websocket.WebSocketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    @Autowired
    private SdEventMapper sdEventMapper;

    @Autowired
    private SdEventServiceImpl sdEventServiceImpl;

    @Autowired
    private ISdTunnelsService tunnelsService;

    @Autowired
    private IExternalSystemService externalSystemService;

    /**
     * 线程池
     */
    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    @Qualifier("kafkaOneTemplate")
    private KafkaTemplate<String, String> kafkaOneTemplate;

    public static Integer roBotNum = 0;

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
    public Result getEventUntreatedNum(String tunnelId) {
        return  Result.success(SpringUtils.getBean(SdEventMapper.class).getEventUntreatedNum(tunnelId));
    }

    /*@GetMapping("/eventPopAll")
    @ApiOperation("事件弹窗当日事件")
    public Result eventPopAll(String subIndex) {
        Integer allNum = SpringUtils.getBean(SdEventMapper.class).getEventUntreatedNum();
        Map<String,Object> map = new HashMap<>();
        map.put("total",allNum);
        map.put("data",sdEventService.eventPopAll(subIndex));
        return Result.success(map);
    }*/

    /**
     * 右上角事件弹窗数据
     * @param sdEvent
     * @return
     */
    @GetMapping("/eventPopData")
    public TableDataInfo eventPopData(SdEvent sdEvent){
        startPage();
        sdEvent.setDept(SecurityUtils.getDeptId());
        return getDataTable(sdEventService.eventPopData(sdEvent));
    }

    /*@GetMapping("/eventPopFault")
    @ApiOperation("事件弹窗设备故障")
    public Result eventPopFault(String subIndex) {
        SdEventMapper mapper = SpringUtils.getBean(SdEventMapper.class);
        Integer allNum = mapper.eventPopFaultCount();
        Map<String,Object> map = new HashMap<>();
        map.put("total",allNum);
        map.put("data",mapper.eventPopFault(subIndex));
        return Result.success(map);
    }*/

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
        List<SdEventHandle> handle = sdEventService.getHandle(sdEvent);
        SdEvent sdEvent1 = sdEventService.selectSdEventById(sdEvent.getId());
        /*threadPoolTaskExecutor.execute(()->{
            rodro(handle,sdEvent1);
        });*/
        return AjaxResult.success(handle);
    }

    /**
     * 机器人移动
     * @param
     */
    @GetMapping("/roBotDong")
    public void roBotDong(@RequestParam("eventId") Long eventId, @RequestParam("handleId") Long handleId){
        CorniceTunnelRobot corniceTunnelRobot = SpringUtils.getBean(CorniceTunnelRobot.class);
        SdEvent sdEvent1 = sdEventMapper.selectSdEventById(eventId);
        SdTunnels sdTunnels = tunnelsService.selectSdTunnelsById(TunnelEnum.HU_SHAN.getCode());
        //事件桩号
        Integer eventPile = Integer.valueOf(sdEvent1.getStakeNum().replaceAll("YK", "").replaceAll("ZK", "").replaceAll("K", "").replaceAll("\\+", ""));
        //隧道终点桩号
        Integer tunnelPile = Integer.valueOf(sdTunnels.getEndPileNum());
        //计算距离
        Integer distance = tunnelPile - eventPile;
        //Integer distance = 2256;
        //查询地址
        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);
        int count = corniceTunnelRobot.OneClickArrival(list.get(0).getParam(), (distance - 10) + "", null, null, list.get(0).getSystemUrl());
        if(count == 1){
            roBotNum = distance - 10;
            SdEventHandle sdEventHandle = new SdEventHandle();
            sdEventHandle.setEventState("1");
            sdEventHandle.setId(handleId);
            sdEventHandleMapper.updateSdEventHandle(sdEventHandle);
            //机器人语音播报
            robotVoice(sdEvent1);
        }
    }

    /**
     * 机器人语音播报
     * @param sdEvent
     * @return
     */
    public int robotVoice(SdEvent sdEvent){
        CorniceTunnelRobot corniceTunnelRobot = SpringUtils.getBean(CorniceTunnelRobot.class);
        ExternalSystem system = new ExternalSystem();
        system.setBrandId(DevicesBrandEnum.ZHUO_SHI_ZHI_TONG.getCode());
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(system);
        return corniceTunnelRobot.Broadcast(list.get(0).getParam(), RoBotVoiceEnum.getValue(sdEvent.getEventTypeId().toString()),list.get(0).getSystemUrl(),"20");
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
     * @param sdEventHandle
     * @return
     */
    @GetMapping("/getAllManagementDevices")
    public AjaxResult getAllManagementDevices(SdEventHandle sdEventHandle){
        return sdEventService.getAllManagementDevices(sdEventHandle);
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

    /**
     * 查看所选预案或策略的设备详情
     * @param sdEvent
     * @return
     */
    @GetMapping("/examineDeviceDetail")
    public AjaxResult examineDeviceDetail(SdEvent sdEvent){
        return sdEventService.examineDeviceDetail(sdEvent);
    }

    /**
     * 批量处理事件
     * @param sdEvent
     * @return
     */
    @GetMapping("/batchHandleEvent")
    public AjaxResult batchHandleEvent(SdEvent sdEvent){
        return sdEventService.batchHandleEvent(sdEvent);
    }

    /**
     * 查看历史录像
     * @param sdEvent
     * @return
     */
    @GetMapping("/vedioData")
    public AjaxResult vedioData(SdEvent sdEvent){
        return sdEventService.vedioData(sdEvent);
    }

    /**
     * 关闭相机录像视频流
     * @param camId
     * @param playId
     * @return
     */
    @GetMapping("/closeVedio")
    public AjaxResult closeVedio(@RequestParam("camId") String camId, @RequestParam("playId") String playId){
        return sdEventService.closeVedio(camId,playId);
    }

    /**
     * 下载历史录像视频
     * @param camId
     * @param downLoadTime
     * @return
     */
    @GetMapping("/downLoadVedio")
    public void downLoadVedio(@RequestParam("camId") String camId, @RequestParam("downLoadTime") String downLoadTime,HttpServletResponse response){
        sdEventService.downLoadVedio(camId,downLoadTime,response);
    }

    /**
     * 紧急电话火灾报警模拟事件接口
     */
    @GetMapping("/eventDemonstrate")
    public void eventDemonstrate(String xdData,String ldData,String hzData, String model){
        if("xd".equals(model)){
            //兴电
            PhoneSpkService phoneSpkService = SpringUtils.getBean(PhoneSpkService.class);
            JSONObject jsonObject = JSONObject.parseObject(xdData);
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("data",jsonObject);
            phoneSpkService.onMessage(jsonObject1);
        }else if("ld".equals(model)){

        }else if("hz".equals(model)){
            sdEventService.eventDemonstrate(hzData);
        }
    }
    /**
     * 消防炮模拟事件接口
     */
    @GetMapping("/eventDemonFireMonitor")
    public void eventDemonFireMonitor(){
        //log.info("设备状态为火警");
        //存 故障清单表sd_event
        SdEvent sdEvent = new SdEvent();
        sdEvent.setTunnelId("JQ-JiNan-WenZuBei-MJY");//隧道
        sdEvent.setEventSource("3");//事件来源  消防炮
        sdEvent.setEventTypeId((long) 20);//事件类型
        sdEvent.setEventTitle("胡山隧道,火灾");//事件标题  隧道+方向+桩号+发生火警
        sdEvent.setEventTime(DateUtils.getNowDate());//时间
        sdEvent.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,DateUtils.getNowDate()));//开始时间
        sdEvent.setEventState("3");//状态  待确认
        sdEvent.setEventGrade("1");//事件等级 一般
        sdEvent.setStakeNum("YK16+698");//事件桩号
        sdEvent.setCreateTime(DateUtils.getNowDate());//创建时间
        sdEvent.setDirection("1");//方向
        sdEventMapper.insertSdEvent(sdEvent);
        sdEventServiceImpl.eventSendWeb(sdEvent);//事件推送
        Map<String, Object> map = new HashMap<>();
        map.put("tunnelId","胡山隧道");
        map.put("eventSource","火灾报警系统");
        map.put("eventTypeId","火灾");
        map.put("eventTitle",sdEvent.getEventTitle());
        map.put("eventTime",sdEvent.getEventTime());
        map.put("startTime",sdEvent.getStartTime());
        map.put("eventState", EventStateEnum.unprocessed.getName());
        map.put("eventGrade",EventGradeEnum.YI_BAN.getName());
        map.put("stakeNum",sdEvent.getStakeNum());
        map.put("direction",DeviceDirectionEnum.WEI_FANG.getName());
        kafkaOneTemplate.send("fireEvent",JSON.toJSONString(map));
    }

    /**
     * 第三方新增断电告警时间
     * @param jsonData
     * @return
     */
    @PostMapping("/addOutageEvent")
    public AjaxResult addOutageEvent(@RequestBody String jsonData){
        JSONObject jsonObject = JSONObject.parseObject(jsonData);
        JSONObject parameters = jsonObject.getJSONObject("parameters");
        SdEvent sdEvent = new SdEvent();
        sdEvent.setTunnelId(parameters.getString("tunnelId"));//隧道
        sdEvent.setEventSource("4");//事件来源  消防炮
        sdEvent.setEventTypeId(parameters.getLongValue("eventTypeId"));//事件类型
        sdEvent.setEventTitle(parameters.getString("eventTitle"));//事件标题  隧道+方向+桩号+发生火警
        sdEvent.setEventTime(DateUtils.parseDate(parameters.getString("eventTime")));//时间
        sdEvent.setStartTime(parameters.getString("eventTime"));//开始时间
        sdEvent.setEventState("3");//状态  待确认
        sdEvent.setEventGrade("1");//事件等级 一般
        sdEvent.setCreateTime(DateUtils.getNowDate());//创建时间
        sdEventMapper.insertSdEvent(sdEvent);
        return AjaxResult.success();
    }

    /**
     * 万集推送事件
     * @param eventJson
     */
    @PostMapping("/upload")
    public void upload(@RequestBody String eventJson){
        threadPoolTaskExecutor.execute(()->{
            sdEventService.upload(eventJson);
        });
    }

    /**
     * 万集推送结束事件
     * @param eventJson
     */
    @PostMapping("/wjEventUpdate")
    public void wjEventUpdate(@RequestBody String eventJson){
        sdEventService.wjEventUpdate(eventJson);
    }
}
