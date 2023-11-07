package com.tunnel.business.service.event.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.oss.OssUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.tunnel.business.datacenter.domain.dataReport.ExternalSystemCode;
import com.tunnel.business.datacenter.domain.enumeration.*;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.event.*;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import com.tunnel.business.mapper.dataInfo.ExternalSystemMapper;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.mapper.digitalmodel.RadarEventMapper;
import com.tunnel.business.mapper.event.*;
import com.tunnel.business.mapper.informationBoard.IotBoardTemplateMapper;
import com.tunnel.business.mapper.logRecord.SdOperationLogMapper;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficImageMapper;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.digitalmodel.impl.RadarEventServiceImpl;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.sendDataToKafka.SendDeviceStatusToKafkaService;
import com.tunnel.business.utils.util.UUIDUtil;
import com.tunnel.business.utils.work.CustomXWPFDocument;
import com.tunnel.business.utils.work.WorderToNewWordUtils;
import com.zc.common.core.websocket.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import scala.annotation.meta.param;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.ruoyi.common.utils.DictUtils.getCacheEventKey;

/**
 * 事件管理Service业务层处理
 *
 * @author gongfanfei
 * @date 2020-08-24
 */
@Service
public class SdEventServiceImpl implements ISdEventService {

    private static final Logger logger = LoggerFactory.getLogger(SdEventServiceImpl.class);

    @Autowired
    private SdEventMapper sdEventMapper;

    @Autowired
    private SdEventFlowMapper sdEventFlowMapper;

    @Autowired
    private SdTunnelSubareaMapper sdTunnelSubareaMapper;

    @Autowired
    private SdOperationLogMapper sdOperationLogMapper;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private SdTrafficImageMapper sdTrafficImageMapper;

    @Autowired
    private RadarEventMapper radarEventMapper;

    @Autowired
    private SdJoinTypeFlowMapper sdJoinTypeFlowMapper;

    @Autowired
    private SdEventHandleMapper sdEventHandleMapper;

    @Autowired
    private SdEventHandleHistoryMapper historyMapper;

    @Autowired
    private SdReservePlanMapper sdReservePlanMapper;

    @Autowired
    private SdReserveProcessMapper sdReserveProcessMapper;

    @Autowired
    private SdTunnelsMapper sdTunnelsMapper;

    @Autowired
    private SdStrategyMapper sdStrategyMapper;

    @Autowired
    private SdStrategyRlMapper sdStrategyRlMapper;

    @Autowired
    private RadarEventServiceImpl radarEventServiceImpl;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Autowired
    private SdEventTypeMapper sdEventTypeMapper;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    @Autowired
    private SdJoinReserveHandleMapper joinMapper;

    @Autowired
    private RedisCache redisCache;

    @Value("${video-platform.address}")
    private String address;

    @Resource(name = "HttpTemplate")
    private RestTemplate template;

    @Autowired
    private ISdTunnelsService tunnelsService;

    /**
     * 查询事件管理
     *
     * @param id 事件管理ID
     * @return 事件管理
     */
    @Override
    public SdEvent selectSdEventById(Long id) {
        SdEvent sdEvent = sdEventMapper.selectSdEventById(id);
        if(sdEvent != null){
            sdEvent.setIds(sdEvent.getId().toString());
            SdTrafficImage image = new SdTrafficImage();
            image.setBusinessId(id.toString());
            image.setImgType("1");
            //查询视频
            List<SdTrafficImage> sdTrafficImages = sdTrafficImageMapper.selectSdTrafficImageList(image);
            sdEvent.setVideoUrl(sdTrafficImages.size() > 0 ? sdTrafficImages.get(0).getImgUrl().split(";")[0] : "");
            //计算累计时间
            String endDatePoor = DateUtils.getDatePoor(DateUtils.parseDate(sdEvent.getEndTime()) == null ? DateUtils.getNowDate() : DateUtils.parseDate(sdEvent.getEndTime()), DateUtils.parseDate(sdEvent.getStartTime()));
            sdEvent.setContinuedTime(endDatePoor);
            //查询视频图片
            List<SdTrafficImage> image1 = sdTrafficImageMapper.selectImageByBusinessId(sdEvent.getId().toString());
            List<SdTrafficImage> collect = image1.stream().filter(item -> !"1".equals(item.getImgType())).collect(Collectors.toList());
            if(sdTrafficImages.size() > 0){
                collect.add(0,sdTrafficImages.get(0));
            }
            sdEvent.setIconUrlList(collect.subList(0,image1.size() > 10 ? 10 : image1.size()));
            sdEvent.setConfidenceList(radarEventMapper.selectConfidence(sdEvent.getId()));
        }
        return sdEvent;
    }

    /**
     * 查询事件管理列表
     *
     * @param sdEvent 事件管理
     * @return 事件管理
     */
    @Override
    public List<SdEvent> selectSdEventList(SdEvent sdEvent) {
        if (SecurityUtils.getDeptId() != null && !"".equals(SecurityUtils.getDeptId())) {
            String deptId = SecurityUtils.getDeptId();
            if (deptId == null) {
                throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
            }
            sdEvent.getParams().put("deptId", deptId);
        }
        List<SdEvent> sdEvents = sdEventMapper.selectSdEventList(sdEvent);
        //车道
        for(SdEvent item : sdEvents){
            if(item.getLaneNo() == null || "".equals(item.getLaneNo())){
                continue;
            }
            List<String> list = Arrays.asList(item.getLaneNo().split(","));
            List<String> lane = new ArrayList<>();
            for(int i = 0; i < list.size(); i++){
                if(list.get(i) == null || "".equals(list.get(i))){
                    continue;
                }
                String laneLabel = sysDictDataMapper.selectDictLabel("sd_lane_two", list.get(i));
                lane.add(laneLabel);
            }
            item.setLaneNoName(lane.size() == 0 ? "" : StringUtils.join(lane,"、"));
        }
        sdEvents.stream().forEach(item -> {
            item.setPosition(item.getTunnelName().concat(DeviceDirectionEnum.getValue(item.getDirection()) == null ? "" : DeviceDirectionEnum.getValue(item.getDirection())).concat(item.getStakeNum() == null ? "" : item.getStakeNum()));
            if(item.getVideoUrl()!=null){
                item.setVideoUrl(item.getVideoUrl().split(";")[0]);
            }
            //计算累计时间
            String endDatePoor = DateUtils.getDatePoor(item.getEndTime() == null ? DateUtils.getNowDate() : DateUtils.parseDate(item.getEndTime()), item.getStartTime() == null ? DateUtils.getNowDate() : DateUtils.parseDate(item.getStartTime()));;

            item.setContinuedTime(endDatePoor);
            SdTrafficImage image = new SdTrafficImage();
            image.setBusinessId(item.getId().toString());
            image.setImgType("1");
            //查询视频
            List<SdTrafficImage> sdTrafficImages = sdTrafficImageMapper.selectSdTrafficImageList(image);
            item.setVideoUrl(sdTrafficImages.size() > 0 ? sdTrafficImages.get(0).getImgUrl().split(";")[0] : "");
            //查询视频图片
            List<SdTrafficImage> image1 = sdTrafficImageMapper.selectImageByBusinessId(item.getId().toString());
            List<SdTrafficImage> collect = image1.stream().filter(age -> !"1".equals(age.getImgType())).collect(Collectors.toList());
            if(sdTrafficImages.size() > 0){
                SdTrafficImage image2 = sdTrafficImages.get(0);
                image2.setImgUrl(image2.getImgUrl().split(";")[0]);
                collect.add(0,image2);
            }
            item.setIconUrlList(collect.subList(0,collect.size() > 10 ? 10 : collect.size()));
            item.setConfidenceList(radarEventMapper.selectConfidence(item.getId()));
            for(SdTrafficImage temp : collect){
                if("0".equals(temp.getImgType())){
                    item.setEventImgUrl(temp.getImgUrl());
                    break;
                }
            }
        });
        return sdEvents;
    }

    /**
     * 查询事件管理列表(不做额外处理，单纯查询数据库SQL)
     *
     * @param sdEvent 事件管理
     * @return 事件管理集合
     */
    @Override
    public List<SdEvent> querySdEventList(SdEvent sdEvent) {
        return sdEventMapper.selectSdEventList(sdEvent);
    }

    /**
     * 新增事件管理
     *
     * @param sdEvent 事件管理
     * @return 结果
     */
    @Override
    public int insertSdEvent(SdEvent sdEvent) {
        String guid = UUIDUtil.getRandom32BeginTimePK();
        sdEvent.setCreateTime(DateUtils.getNowDate());
        sdEvent.setFlowId(guid);
        //添加事件处理流程 ：开始
        SdEventFlow eventFlow = new SdEventFlow();
        eventFlow.setEventId(guid);
        eventFlow.setFlowTime(sdEvent.getEventTime());
        eventFlow.setFlowDescription(sdEvent.getEventDescription());
        try{
            String username = SecurityUtils.getUsername();
            eventFlow.setFlowHandler(username);
        }catch (ServiceException e){
            logger.error("获取用户账户异常",e.getDetailMessage());
        }

        int result = sdEventFlowMapper.insertSdEventFlow(eventFlow);
        if (result > -1) {
            result = sdEventMapper.insertSdEvent(sdEvent);
        }
        return result;
    }

    /**
     * 修改事件管理
     *
     * @param sdEvent 事件管理
     * @return 结果
     */
    @Override
    public int updateSdEvent(SdEvent sdEvent) {
        //更新事件信息
        int count = upEvent(sdEvent);
        if(EventStateEnum.processed.getCode().equals(sdEvent.getEventState())){
            sdEvent = sdEventMapper.selectSdEventById(sdEvent.getId());
        }
        //查询图片视频
        List<SdTrafficImage> list = sdTrafficImageMapper.selectImageByBusinessId(sdEvent.getId().toString());
        Object[] objects = list.stream().filter(item -> "0".equals(item.getImgType())).map(SdTrafficImage::getImgUrl).toArray();
        List<SdTrafficImage> videoList = list.stream().filter(item -> "1".equals(item.getImgType())).collect(Collectors.toList());
        //图片
        sdEvent.setEventImgUrl(StringUtils.join(objects,","));
        sdEvent.setVideoUrl(videoList.size()>0 ? videoList.get(0).getImgUrl().split(";")[0]:"");
        //推送至物联
        sendGsy(sdEvent);
        return count;
    }

    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public int upEvent(SdEvent sdEvent){
        if (EventStateEnum.processed.getCode().equals(sdEvent.getEventState())) {
            instreEventFlowData(sdEvent.getId(),"事件已完结");
        }else if(EventStateEnum.processing.getCode().equals(sdEvent.getEventState())){
            //更新预案设备
            setStrategyRlEquipment(sdEvent);
            //如有处置中的普通事件则将处理中的安全预警状态改为已处理
            /*SdEvent aqSdevent = new SdEvent();
            aqSdevent.setEventState(EventStateEnum.processing.getCode());
            aqSdevent.setSearchValue(PrevControlTypeEnum.ACTIVE_SAFETY.getCode());
            aqSdevent.setTunnelId(sdEvent.getTunnelId());
            List<SdEvent> sdEvents = sdEventMapper.selectSdEventList(aqSdevent);
            for(SdEvent item : sdEvents){
                item.setEventState(EventStateEnum.processed.getCode());
                item.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,DateUtils.getNowDate()));
                sdEventMapper.updateSdEvent(item);
                instreEventFlowData(item.getId(),"事件因处理普通事件执行中断！");
            }*/
            sdEvent.setUpdateTime(DateUtils.getNowDate());
        }else {
            sdEvent.setUpdateTime(DateUtils.getNowDate());
        }
        if(!EventStateEnum.processing.getCode().equals(sdEvent.getEventState())){
            sdEvent.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,DateUtils.getNowDate()));
        }
        sdEvent.setUpdateBy(SecurityUtils.getUsername());
        int count = sdEventMapper.updateSdEvent(sdEvent);
        if(sdEvent.getConfidenceList() != null && !"".equals(sdEvent.getConfidenceList())){
            String carPa = radarEventMapper.selectConfidence(sdEvent.getId());
            if(carPa == null){
                sdEventMapper.insertEventConfidence(sdEvent);
            }else {
                sdEventMapper.updateEventConfidence(sdEvent);
            }
        }
        return count;
    }

    //推送至高速云
    public void sendGsy(SdEvent sdEvent){
        if(!EventStateEnum.processing.getCode().equals(sdEvent.getEventState())){
            Executor executor = Executors.newSingleThreadExecutor();
            CompletionService<Object> completionService = new ExecutorCompletionService<>(executor);
            Future<?> future = completionService.submit(new Callable<Object>() {
                public Object call() throws Exception {
                    // 这里是要执行的方法
                    noNullStringAttr(sdEvent);
                    Map<String, Object> map = setEventData(sdEvent);
                    radarEventServiceImpl.sendDataToOtherSystem(map);
                    return 1;
                }
            });

            // 获取执行结果
            try {
                Object result = completionService.poll(3000L, TimeUnit.MILLISECONDS);
                if (result == null) {
                    System.out.println("超时了，结束该方法的执行");
                    future.cancel(true);
                } else {
                    // 方法执行完毕，处理执行结果
                    System.out.println("方法执行完毕，结果：" + result.toString());
                }
            } catch (InterruptedException e) {
                System.out.println("出现异常，结束该方法的执行");
                future.cancel(true);
            }
        }
    }

    /**
     * 华为Kafka更新事件操作
     * @param sdEvent
     * @return
     */
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public int updateSdEventHw(SdEvent sdEvent) {
        return sdEventMapper.updateSdEvent(sdEvent);
    }

    @Override
    public void detailExport(HttpServletResponse response, SdEvent sdEvent) {
        //获取事件详情数据
        Map<String, Object> detailData = (Map<String, Object>) getEventDetail(sdEvent).get("data");
        String eventState = detailData.get("eventState").toString();
        //动态表格集合
        List<List<String[]>> list = new ArrayList<>();
        //事件发现
        SdEvent eventDiscovery = (SdEvent) detailData.get("eventDiscovery");
        //人工复核
        SdEvent manualReview = (SdEvent)detailData.get("manualReview");
        //事件处置-预案流程
        List<Map<String, Object>> planDisposal = (List<Map<String, Object>>)detailData.get("planDisposal");
        //事件处置-处置记录
        List<SdEventFlow> disposalRecord = (List<SdEventFlow>) detailData.get("disposalRecord");
        //事件处置-完结报告
        SdEvent endReport = (SdEvent) detailData.get("endReport");

        //需要进行 文本替换的信息
        Map <String, Object> data = new HashMap <String, Object>();
        data.put("${SJFX}", "一、事件发现");
        data.put("${RGFH}", "二、人工复核");
        data.put("${SJCZ}", "三、事件处置");
        data.put("${YAXX}", "预案信息");
        data.put("${CZJL}", "处置记录");
        data.put("${WJBG}", "四、完结报告");
        data.put("${eventImg}","事件图片");

        //事件发现
        List<String[]> list1 = setDiscoveryMap(eventDiscovery);
        //图片，如果是多个图片，就新建多个map
        List<SdTrafficImage> imgList = eventDiscovery.getIconUrlList();
        List<SdTrafficImage> images = imgList.stream().filter(item -> "0".equals(item.getImgType())).collect(Collectors.toList());
        for(int i = 0; i < images.size(); i++){
            Map<String, Object> map = setImgMap(images.get(i).getImgUrl());
            if(map == null){
                continue;
            }
            data.put("${picture"+(i+1)+"}",map);
        }
        list.add(list1);
        if(!"3".equals(eventState)){
            //人工复核
            list.add(setReviewMap(manualReview));
        }

        if("0".equals(eventState) || "1".equals(eventState)){
            //事件处置-预案信息
            List<String[]> list3 = new ArrayList<>();
            if(planDisposal != null){
                for(Map<String, Object> plan : planDisposal){
                    List<SdEventHandle> planList = (List<SdEventHandle>)plan.get("planList");
                    list3.add(new String[]{plan.get("planName") == null ? "" : plan.get("planName").toString(),""});
                    for(SdEventHandle item : planList){
                        for(SdEventHandle temp : item.getChildren()){
                            list3.add(new String[]{item.getFlowContent(),temp.getFlowContent()});
                        }
                    }
                }
            }

            //事件处置-处置记录
            List<String[]> list4 = new ArrayList<>();
            list4.add(new String[]{"处置日期","处置人","处置描述"});
            Collections.reverse(disposalRecord);
            disposalRecord.stream().forEach(item -> {
                item.setFlowDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,item.getFlowTime()));
                list4.add(new String[]{item.getFlowDate(),item.getFlowHandler(),item.getFlowDescription()});
            });
            list.add(list3);
            list.add(list4);
        }
        if("1".equals(eventState)){
            //完结报告
            list.add(setReportMap(endReport));
        }
        try {
            CustomXWPFDocument doc = WorderToNewWordUtils.changWord(EventExportEnum.getValue(eventState),data,list,3,3);
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(sdEvent.getId()+".docx", "UTF-8"));
            response.setContentType("application/octet-stream");
            ServletOutputStream outputStream = response.getOutputStream();
            doc.write(outputStream);
            outputStream.close();
            doc.close();
        }catch (Exception e){
            e.getMessage();
        }
    }

    @Override
    public AjaxResult getSituationUpgrade(SdEvent sdEvent) {
        return AjaxResult.success(sdEventMapper.getSituationUpgrade(sdEvent));
    }

    @Override
    public AjaxResult getManagementDevice(SdReserveProcess sdReserveProcess) {
        //获取应急处置单条设备详情
        Map<String, Object> map = deviceDateiled(sdReserveProcess.getId(),0L);
        if(map == null){
            return AjaxResult.success(new HashMap<>());
        }
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult getAllManagementDevices(SdEventHandle sdEventHandle) {
        //查询预案流程节点
        List<String> list1 = Arrays.asList(sdEventHandle.getReserveId().split(","));
        SdReserveProcessMapper processMapper = SpringUtils.getBean(SdReserveProcessMapper.class);
        List<Map<String, Object>> list = new ArrayList<>();
        for(String process : list1){
            //分别查询设备详情
            Map<String, Object> mapData = deviceDateiled(Long.valueOf(process),Long.valueOf(sdEventHandle.getEventId()));
            //将设备详情整合
            list.add(mapData);
        }
        return AjaxResult.success(list);
    }

    @Override
    public int updateSituationUpgrade(SdEvent sdEvent) {
        //保存事件流程记录
        SdEventFlow eventFlow = new SdEventFlow();
        eventFlow.setEventId(sdEvent.getId().toString());
        eventFlow.setFlowTime(DateUtils.getNowDate());
        eventFlow.setFlowDescription("事件等级修改为：" + EventGradeEnum.getValue(sdEvent.getEventGrade()) +",  "+
                "应急预案修改为：" + sdReservePlanMapper.selectSdReservePlanById(Long.valueOf(sdEvent.getCurrencyId())).getPlanName() +",  "+
                "升级原因为：" + sdEvent.getRemark());
        eventFlow.setFlowHandler(SecurityUtils.getUsername());
        sdEventFlowMapper.insertSdEventFlow(eventFlow);
        sdEvent.setUpdateTime(DateUtils.getNowDate());
        //储存历史记录
        SdEventHandle handleHistory = new SdEventHandle();
        handleHistory.setEventId(sdEvent.getId());
        List<SdEventHandle> sdEventHandles = sdEventHandleMapper.selectHistoryHandleList(handleHistory);
        String planName = sdEventHandles.stream().filter(item -> item.getProcessId() != null).limit(1).collect(Collectors.toList()).get(0).getReserveName();
        //Long reserveId = sdReserveProcessMapper.selectSdReserveProcessById(processId).getReserveId();
        //String planName = sdReservePlanMapper.selectSdReservePlanById(reserveId).getPlanName();
        //查询是否存在历史数据
        String num = historyMapper.selectNum(sdEvent.getId());
        sdEventHandles.stream().forEach(item -> {
            if(item.getReserveId() != null && item.getReserveId() != ""){
                item.setReserveId(planName);
            }
            item.setFlowNum((Integer.valueOf(num) + 1)+"");
            historyMapper.insertSdEventHandle(item);
        });

        //删除事件处置
        SdEventHandle sdEventHandle = new SdEventHandle();
        sdEventHandle.setEventId(sdEvent.getId());
        sdEventHandleMapper.deleteRelation(sdEventHandle);
        //更新预案设备
        sdEvent.setLaneNo(sdEventMapper.selectSdEventById(sdEvent.getId()).getLaneNo());
        setStrategyRlEquipment(sdEvent);
        //交通事件-添加流程树
        updateHandle(sdEvent,"update");
        //更新事件信息
        sdEvent.setUpdateTime(DateUtils.getNowDate());
        sdEvent.setUpdateBy(SecurityUtils.getUsername());
        return sdEventMapper.updateSdEvent(sdEvent);
    }

    @Override
    public AjaxResult getEventInif(SdEvent sdEvent) {
        return AjaxResult.success(sdEventMapper.getEventInif(sdEvent));
    }

    @Override
    public AjaxResult examineDeviceDetail(SdEvent sdEvent) {
        Map<String, Object> map = new HashMap<>();
        //应急预案or联控策略设备总和
        List<Map<String, Object>> list = new ArrayList<>();
        List<SdReserveProcess> sdReserveProcesses = sdReserveProcessMapper.getProcessList(Long.valueOf(sdEvent.getCurrencyId()));
        for(SdReserveProcess item : sdReserveProcesses){
            List<Map<String, Object>> maps = new ArrayList<>();
            map = new HashMap<>();
            for(SdReserveProcess temp : item.getProcessesList()){
                List<SdStrategyRl> sdStrategyRls = new ArrayList<>();
                SdStrategyRl sdStrategyRl = sdStrategyRlMapper.selectSdStrategyRlById(temp.getStrategyId());
                if(sdStrategyRl.getEquipments() != null && !"".equals(sdStrategyRl.getEquipments())){
                    sdStrategyRl.setEquipments(sdStrategyRl.getEquipments());
                }else {
                    sdStrategyRl.setEquipments(getRlDevice(sdEvent, sdStrategyRl));
                }
                sdStrategyRls.add(sdStrategyRl);
                maps.addAll(setDeviceAllList(sdStrategyRls));
            }
            map.put("devicesList", maps);
            map.put("tableName", item.getProcessStageName());
            map.put("prevControlType",sdEvent.getPrevControlType());
            list.add(map);
        }
        /*if(PrevControlTypeEnum.ACTIVE_SAFETY.getCode().equals(sdEvent.getPrevControlType())){
            String strategyName = sdStrategyMapper.selectSdStrategyById(Long.valueOf(sdEvent.getCurrencyId())).getStrategyName();
            //查询联控流程
            List<SdStrategyRl> sdStrategyRls = sdStrategyRlMapper.selectSdStrategyRlByStrategyId(Long.valueOf(sdEvent.getCurrencyId()));
            map.put("devicesList",setDeviceAllList(sdStrategyRls));
            map.put("tableName",strategyName);
            map.put("prevControlType",sdEvent.getPrevControlType());
            list.add(map);
        }else {
            List<SdReserveProcess> sdReserveProcesses = sdReserveProcessMapper.getProcessList(Long.valueOf(sdEvent.getCurrencyId()));
            for(SdReserveProcess item : sdReserveProcesses){
                List<Map<String, Object>> maps = new ArrayList<>();
                map = new HashMap<>();
                for(SdReserveProcess temp : item.getProcessesList()){
                    List<SdStrategyRl> sdStrategyRls = new ArrayList<>();
                    SdStrategyRl sdStrategyRl = sdStrategyRlMapper.selectSdStrategyRlById(temp.getStrategyId());
                    if(sdStrategyRl.getEquipments() != null && !"".equals(sdStrategyRl.getEquipments())){
                        sdStrategyRl.setEquipments(sdStrategyRl.getEquipments());
                    }else {
                        sdStrategyRl.setEquipments(getRlDevice(sdEvent, sdStrategyRl));
                    }
                    sdStrategyRls.add(sdStrategyRl);
                    maps.addAll(setDeviceAllList(sdStrategyRls));
                }
                map.put("devicesList", maps);
                map.put("tableName", item.getProcessStageName());
                map.put("prevControlType",sdEvent.getPrevControlType());
                list.add(map);
            }
        }*/
        return AjaxResult.success(list);
    }

    @Override
    public List<Map<String, Object>> eventPopData(SdEvent sdEvent) {
        //事件数据集合
        List<Map<String, Object>> mapList = new ArrayList<>();
        //事件防控类型
        String prevControlType = sdEvent.getPrevControlType();
        if(prevControlType == null || "".equals(prevControlType)){
            mapList = sdEventMapper.eventPopAll(sdEvent);
        }else if(PrevControlTypeEnum.TRAFFIC_NCIDENT.getCode().equals(prevControlType) || PrevControlTypeEnum.ACTIVE_SAFETY.getCode().equals(prevControlType)){
            mapList = sdEventMapper.eventOrdinaryOrSecurity(sdEvent);
        }else {
            mapList = sdEventMapper.eventPopFault(sdEvent);
        }
        return mapList;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public AjaxResult batchHandleEvent(SdEvent sdEvent) {
        sdEvent.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,DateUtils.getNowDate()));
        sdEvent.setUpdateTime(DateUtils.getNowDate());
        sdEvent.setUpdateBy(SecurityUtils.getUsername());
        int count = sdEventMapper.batchUpdateSdEvent(sdEvent);
        if(count == 0){
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult closeVedio(String camId, String playId) {
        Map<String, Object> vedioData = getVedioData(camId, null, playId);
        if(vedioData != null && "0".equals(vedioData.get("recCode"))){
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    @Override
    public void downLoadVedio(String camId, String downLoadTime, HttpServletResponse response) {
        Map<String, Object> map = downloadVedio(camId, downLoadTime);
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        RandomAccessFile randomAccessFile = null;
        try {
            // 1.获取连接对象
            URL url = new URL(map.get("fileUrl").toString());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Range", "bytes=0-");
            connection.connect();
            if(connection.getResponseCode() / 100 != 2) {
                System.out.println("连接失败...");
                return;
            }
            // 2.获取连接对象的流
            inputStream = connection.getInputStream();
            //已下载的大小
            int downloaded = 0;
            //总文件的大小
            int fileSize = connection.getContentLength();
            String fileName = url.getFile();
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
            // 3.把资源写入文件
            randomAccessFile = new RandomAccessFile(fileName,"rw");
            while(downloaded < fileSize) {
                // 3.1设置缓存流的大小
                byte[] buffer = null;
                if(fileSize - downloaded >= 1000000) {
                    buffer = new byte[1000000];
                }else {
                    buffer = new byte[fileSize - downloaded];
                }
                // 3.2把每一次缓存的数据写入文件
                int read = -1;
                int currentDownload = 0;
                long startTime = System.currentTimeMillis();
                while(currentDownload < buffer.length) {
                    read = inputStream.read();
                    buffer[currentDownload ++] = (byte) read;
                }
                long endTime = System.currentTimeMillis();
                double speed = 0.0;
                if(endTime - startTime > 0) {
                    speed = currentDownload / 1024.0 / ((double)(endTime - startTime)/1000);
                }
                randomAccessFile.write(buffer);
                downloaded += currentDownload;
                randomAccessFile.seek(downloaded);
                System.out.printf("下载了进度:%.2f%%,下载速度：%.1fkb/s(%.1fM/s)%n",downloaded * 1.0 / fileSize * 10000 / 100,speed,speed/1000);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                connection.disconnect();
                inputStream.close();
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void eventDemonstrate(String hyData) {
        protocolAnalysis(hyData);
    }

    /**
     * 批量删除事件管理
     *
     * @param ids 需要删除的事件管理ID
     * @return 结果
     */
    @Override
    public int deleteSdEventByIds(Long[] ids) {
        return sdEventMapper.deleteSdEventByIds(ids);
    }

    /**
     * 删除事件管理信息
     *
     * @param id 事件管理ID
     * @return 结果
     */
    @Override
    public int deleteSdEventById(Long id) {
        return sdEventMapper.deleteSdEventById(id);
    }


    /**
     * 根据id查询事件 ---视频
     *
     * @param id
     * @return
     */
    @Override
    public SdEvent getById(Long id) {
        SdEvent event = sdEventMapper.selectSdEventById(id);
        SdTrafficImage image = new SdTrafficImage();
        image.setBusinessId(id.toString());
        image.setImgType("1");
        List<SdTrafficImage> sdTrafficImages = sdTrafficImageMapper.selectSdTrafficImageList(image);
        event.setVideoUrl(sdTrafficImages.size() > 0 ? sdTrafficImages.get(0).getImgUrl().split(";")[0] : "");
        return event;
    }

    /**
     * 预警事件查询全部
     *
     * @return
     */
    @Override
    public List<SdEvent> getEvent(SdEvent sdEvent) {
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        sdEvent.getParams().put("deptId", deptId);
        return sdEventMapper.getEvent(sdEvent);
    }

    @Override
    public Map getTodayEventCount() {
        return sdEventMapper.getTodayEventCount();
    }

    /**
     * 根据事件id列表查询事件信息
     *
     * @param eventIdList 事件id列表
     * @return
     */
    @Override
    public List<SdEvent> getEventList(List<Long> eventIdList) {
        return sdEventMapper.getEventList(eventIdList);
    }

    /**
     * 拼接得到默认的事件标题
     * @param sdEvent 事件信息
     * @param tunnelMap 隧道名称Map
     * @param eventTypeMap 事件类型Map
     * @return
     */
    @Override
    public String getDefaultEventTitle(SdEvent sdEvent,Map<String,String> tunnelMap,Map<Long,String> eventTypeMap) {
        //拼接事件标题,格式：##隧道##方向桩号##发生##类型事件
        StringBuffer titleDesc = new StringBuffer();

        //隧道名称
        String tunnelName = tunnelMap.get(sdEvent.getTunnelId());
        titleDesc.append(tunnelName);

        //查询方向字典值
        String directionDict = "";
        if(!StringUtils.isEmpty(sdEvent.getDirection())){
            directionDict = DictUtils.getDictLabel(DictTypeEnum.sd_direction.getCode(), sdEvent.getDirection());
        }
        if(!StringUtils.isEmpty(directionDict)){
            titleDesc.append(" " + directionDict);
        }
        /*if(!StringUtils.isEmpty(sdEvent.getStakeNum())){
            titleDesc.append("桩号").append(sdEvent.getStakeNum());
        }*/
        //事件类型名称
        String eventTypeName = eventTypeMap.get(sdEvent.getEventTypeId());
        titleDesc.append("," + eventTypeName);
        //titleDesc.append("发生").append(eventTypeName).append("事件");

        return titleDesc.toString();
    }

    @Override
    public Long getSubareaByStakeNum(String tunnelId,String stakeNum,String direction) {
        //0是下行1是上行   上行，桩号增大；下行，桩号减小
        //1.根据隧道、方向获取所有同一方向上的分区数据
        Long subareaId =0L;
        SdTunnelSubarea subarea = new SdTunnelSubarea();
        subarea.setTunnelId(tunnelId);
        subarea.setDirection(direction);
        List<SdTunnelSubarea> subareaData = sdTunnelSubareaMapper.selectSdTunnelSubareaList(subarea);
        if(subareaData.size() < 1){
            return subareaId;
        }
        //2.根据桩号遍历匹配
        try{
            Integer compareValue = Integer.parseInt(stakeNum.replace("K","").replace("+","").replace(" ",""));
            for(SdTunnelSubarea data:subareaData){
                Integer min = Integer.parseInt(data.getPileMin());
                Integer max = Integer.parseInt(data.getPileMax());
                if(min > max){
                    Integer temp = max;
                    max = min;
                    min = temp;
                }
                if(max >= compareValue && compareValue >= min){
                    subareaId = data.getsId();
                    return subareaId;
                }
            }
            Integer minAbs = compareValue;
            for(SdTunnelSubarea data:subareaData){
                Integer start = Integer.parseInt(data.getPileMin());
                Integer end = Integer.parseInt(data.getPileMax());
                Integer absStart = Math.abs(compareValue- start);
                Integer absEnd = Math.abs(compareValue- end);

                if(absStart < minAbs){
                    minAbs = absStart;
                    subareaId = data.getsId();
                }
                if(absEnd < minAbs){
                    minAbs = absEnd;
                    subareaId = data.getsId();
                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return subareaId;
    }

    @Override
    public List<SdDevices> getEventCamera(String tunnelId, String stakeNum, String direction) {
        SdDevices devices = new SdDevices();
        devices.setEqTunnelId(tunnelId);
        devices.setEqDirection(direction);
        devices.setEqType(DevicesTypeEnum.CAMERA_BOX.getCode());
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(devices);
        try {
            int param = Integer.valueOf(stakeNum.replaceAll("[a-zA-Z]", "").replace("+","").replace(" ",""));
            List<Integer> pileNum = list.stream().map(p->(p.getPileNum().intValue())).distinct().collect(Collectors.toList());
            //视频检测来源
            if(pileNum.contains(param)){
                int filter = param;
                return list.stream().filter(p->p.getPileNum().intValue()==filter).collect(Collectors.toList());
            }
            //非视频检测
            pileNum.add(param);
            pileNum = pileNum.stream().sorted().collect(Collectors.toList());
            int paramIndex = pileNum.indexOf(param);
            param = direction.equals(TunnelDirectionEnum.DOWN_DIRECTION.getCode())?pileNum.get(paramIndex+1):pileNum.get(paramIndex-1);
            devices.setPileNum(new Long((long)param));
            List<SdDevices> result = sdDevicesService.selectSdDevicesList(devices);
            //TODO 演示暂时写死
            /*List<SdDevices> result = new ArrayList<>();
            SdDevices sdDevices = new SdDevices();
            sdDevices.setEqId("JQ-JiNan-WenZuBei-MJY-CAM_BOX-003");
            result.add(sdDevices);
            sdDevices.setEqId("JQ-JiNan-WenZuBei-MJY-CAM_BOX-039");
            result.add(sdDevices);*/
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*@Override
    public List<Map> eventPopAll(String subIndex) {
        return sdEventMapper.eventPopAll(subIndex);
    }*/

    @Override
    public List<SdEventHandle> getHandle(SdEvent sdEvent) {
        sdEvent.setCurrencyId(sdEventMapper.selectSdEventById(sdEvent.getId()).getCurrencyId());
        //交通事件-添加流程树
        updateHandle(sdEvent,"add");
        SdEventHandle sdEventHandle = new SdEventHandle();
        sdEventHandle.setEventId(sdEvent.getId());
        List<SdEventHandle> sdEventHandles = sdEventHandleMapper.selectSdEventHandleList(sdEventHandle);
        return sdEventHandles;
    }

    @Override
    public AjaxResult getRelation(SdReservePlan sdReservePlan) {
        //查询是否存在预案联控流程
        SdEventHandle eventHandle = new SdEventHandle();
        eventHandle.setEventId(Long.valueOf(sdReservePlan.getEventId()));
        eventHandle.setFlowId(Long.valueOf(7));
        List<SdEventHandle> sdEventHandle1 = sdEventHandleMapper.selectSdEventHandleList(eventHandle);
        if(sdEventHandle1.size() > 0){
            if(sdEventHandle1.get(0).getReserveId() != null){
                return AjaxResult.error("已存在预案联控流程");
            }
        }
        //查询预案是否存在
        List<SdReservePlan> relation = sdReservePlanMapper.getRelation(sdReservePlan);
        String concat = sdReservePlan.getEventId().toString().concat("700");
        Long relationId = Long.valueOf(concat);
        int sort = 0;
        for(SdReservePlan item : relation){
            if(relation.size() == 1 && item.getProcessName() == null || item.getProcessName() == ""){
                return AjaxResult.error("暂无此预案相关联控流程");
            }
            sort = sort + 1;
            relationId = relationId + 1;
            SdEventHandle sdEventHandle = new SdEventHandle();
            sdEventHandle.setEventId(Long.valueOf(sdReservePlan.getEventId()));
            sdEventHandle.setFlowId(Long.valueOf(relationId));
            sdEventHandle.setFlowPid(Long.valueOf(7));
            sdEventHandle.setFlowContent(item.getProcessName());
            sdEventHandle.setProcessId(item.getProcessId());
            sdEventHandle.setFlowSort(sort+"");
            sdEventHandle.setUpdateTime(DateUtils.getNowDate());
            sdEventHandleMapper.insertSdEventHandle(sdEventHandle);
        }
        int count = 0;
        if(relation.size() > 0){
            SdEventHandle sdEventHandle = new SdEventHandle();
            sdEventHandle.setEventId(Long.valueOf(sdReservePlan.getEventId()));
            sdEventHandle.setFlowId(Long.valueOf(7));
            sdEventHandle.setReserveId(relation.get(0).getId().toString());
            sdEventHandle.setUpdateTime(DateUtils.getNowDate());
            count = sdEventHandleMapper.updateSdEventHandleRelation(sdEventHandle);
        }
        if(count == 0){
            return AjaxResult.error("暂无此事件相关预案");
        }
        return AjaxResult.success("关联成功");
    }

    @Override
    public AjaxResult getReserveId(SdReservePlan sdReservePlan) {
        return AjaxResult.success(sdReservePlanMapper.getReserveId(sdReservePlan));
    }

    @Override
    public AjaxResult getEntranceExitVideo(SdEvent sdEvent) {
        SdDevicesMapper sdDevicesMapper = SpringUtils.getBean(SdDevicesMapper.class);
        //查询设备列表
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqDirection(sdEvent.getDirection());
        sdDevices.setEqTunnelId(sdEvent.getTunnelId());
        sdDevices.setEqType(DevicesTypeEnum.CAMERA_BOX.getCode());
        List<SdDevices> sdDevicesList = sdDevicesMapper.getEntranceExitVideo(sdDevices);
        //如果为空则返回
        if(sdDevicesList.size() == 0){
            return AjaxResult.success(new ArrayList<>());
        }
        //根据整形桩号查询出入口设备id
        SdDevices minEqId = sdDevicesList.stream().min(Comparator.comparing(SdDevices::getPileNum)).get();
        SdDevices maxEqId = sdDevicesList.stream().max(Comparator.comparing(SdDevices::getPileNum)).get();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        if(sdEvent.getTunnelId().equals(TunnelEnum.FENG_HUANG_SHAN.getCode())){
            map.put("outlet",maxEqId.getEqId());
            map.put("inlet",minEqId.getEqId());
            map.put("outletName",maxEqId.getEqDirection().concat("出口"));
            map.put("inletName",minEqId.getEqDirection().concat("入口"));
        }else {
            if("1".equals(sdEvent.getDirection())){
                map.put("outlet",maxEqId.getEqId());
                //TODO 演示暂时写死
                map.put("inlet",minEqId.getEqId());
                //map.put("inlet","JQ-JiNan-WenZuBei-MJY-CAM_BOX-001");
                map.put("outletName",maxEqId.getEqDirection().concat("出口"));
                map.put("inletName",minEqId.getEqDirection().concat("入口"));
            }else {
                map.put("outlet",minEqId.getEqId());
                map.put("inlet",maxEqId.getEqId());
                map.put("outletName",minEqId.getEqDirection().concat("出口"));
                map.put("inletName",maxEqId.getEqDirection().concat("入口"));
            }
        }
        list.add(map);
        return AjaxResult.success(list);
    }

    @Override
    public AjaxResult getEventDetail(SdEvent sdEvent) {
        Map<String, Object> map = new HashMap<>();
        SdEvent sdEventData = sdEventMapper.selectSdEventById(sdEvent.getId());
        //查询事件详情-事件发现
        SdEvent eventDiscovery = sdEventMapper.getEventDiscovery(sdEvent);
        //查询事件详情-事件发现-图片
        List<SdTrafficImage> image1 = sdTrafficImageMapper.selectImageByBusinessId(sdEvent.getId().toString());
        image1.stream().filter(item -> "1".equals(item.getImgType())).forEach(item -> item.setImgUrl(item.getImgUrl().split(";")[0]));
        eventDiscovery.setIconUrlList(image1.subList(0,image1.size() > 10 ? 10 : image1.size()));
        //计算持续时间
        String datePoor = "";
        if("3".equals(sdEventData.getEventState())){
            datePoor = DateUtils.getDatePoor(DateUtils.getNowDate(), DateUtils.parseDate(eventDiscovery.getContinuedTime()));
        }else {
            datePoor = DateUtils.getDatePoor(eventDiscovery.getUpdateTime(), DateUtils.parseDate(eventDiscovery.getContinuedTime()));
        }
        eventDiscovery.setContinuedTime(datePoor);
        map.put("eventDiscovery",eventDiscovery);
        if(!"3".equals(sdEventData.getEventState())){
            //查询事件详情-人工复核
            SdEvent manualReview = sdEventMapper.getManualReview(sdEvent);
            if(manualReview != null){
                //车道
                List<String> list = Arrays.asList(manualReview.getLaneNo().split(","));
                List<String> lane = new ArrayList<>();
                for(int i = 0; i < list.size(); i++){
                    if(list.get(i) == null || "".equals(list.get(i))){
                        continue;
                    }
                    String laneLabel = sysDictDataMapper.selectDictLabel("sd_lane_two", list.get(i));
                    lane.add(laneLabel);
                }
                manualReview.setLaneNo(StringUtils.join(lane,"、"));
//                manualReview.setLaneNo(manualReview.getDirection().concat("/").concat(StringUtils.join(lane,"、")));
                //查询事件详情-人工复核-当事目标
                String confidence = radarEventMapper.selectConfidence(sdEvent.getId());
                manualReview.setConfidenceList(confidence == null ? "" : confidence);
                map.put("manualReview",manualReview);
            }
        }
        if("0".equals(sdEventData.getEventState()) || "1".equals(sdEventData.getEventState())){
            //查询事件详情-预案处置
            if("0".equals(sdEventData.getIsAuto())){
                //最新的预案
                Map<String, Object> planMap = setPlanDataMap(sdEventData);
                //查询历史预案
                List<Map<String, Object>> historyPlanMapList = setHistoryPlanDataMap(sdEventData);
                historyPlanMapList.add(planMap);
                Collections.reverse(historyPlanMapList);
                map.put("planDisposal",historyPlanMapList);
            }else {
                List<SdStrategyRl> sdStrategyRls = sdStrategyRlMapper.selectSdStrategyRlByStrategyId(Long.valueOf(sdEventData.getCurrencyId()));
                List<Map<String, Object>> maps = setDeviceAllList(sdStrategyRls);
                map.put("tacticsList",maps);
            }
            map.put("isAuto",sdEventData.getIsAuto());
            //查询事件详情-处置记录
            SdEventFlow sdEventFlow = new SdEventFlow();
            sdEventFlow.setEventId(sdEvent.getId().toString());
            List<SdEventFlow> disposalRecord = sdEventFlowMapper.selectSdEventFlowList(sdEventFlow);
            map.put("disposalRecord",disposalRecord);
        }
        if("1".equals(sdEventData.getEventState())){
            //查询事件详情-完结报告
            SdEvent endReport = sdEventMapper.getEndReport(sdEvent);
            SdEventFlow sdEventFlow = new SdEventFlow();
            sdEventFlow.setEventId(sdEvent.getId().toString());
            String flowDescription = sdEventFlowMapper.selectSdEventFlowList(sdEventFlow).get(0).getFlowDescription();
            endReport.setRemark(flowDescription);
            //计算累计时间
            if("1".equals(endReport.getEventState())){
                String endDatePoor = DateUtils.getDatePoor(DateUtils.parseDate(endReport.getEndTime()) == null ? DateUtils.getNowDate() : DateUtils.parseDate(endReport.getEndTime()), DateUtils.parseDate(endReport.getStartTime()));
                endReport.setContinuedTime(endDatePoor);
            }
            map.put("endReport",endReport);
        }
        map.put("eventState",sdEventData.getEventState());
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult getAccidentPoint(SdEvent sdEvent) {
        SdTunnels sdTunnels = sdTunnelsMapper.selectSdTunnelsById(sdEvent.getTunnelId());
        //隧道终点桩号
        BigDecimal endPileNum = new BigDecimal(sdTunnels.getEndPileNum());
        //隧道起点桩号
        BigDecimal startPileNum = new BigDecimal(sdTunnels.getStartPileNum());
        //隧道总长度
        BigDecimal num = endPileNum.subtract(startPileNum);
        //事件桩号整形
        BigDecimal stakeNum = new BigDecimal(sdEvent.getStakeNum().replaceAll("K", "").replaceAll("\\+", ""));
        //使用事件桩号-隧道起点桩号得出事件发送位置
        BigDecimal eventPositionNum = stakeNum.subtract(startPileNum);
        //隧道Left值
        BigDecimal tunnelLeft = eventPositionNum.divide(num, 2, BigDecimal.ROUND_HALF_UP);
        //车道数量
        BigDecimal lane = new BigDecimal(sdTunnels.getLane());
        //事件车道号
        BigDecimal laneNo = new BigDecimal((sdEvent.getLaneNo() == null || "".equals(sdEvent.getLaneNo())) ? "0" : sdEvent.getLaneNo());
        //隧道Top值
        BigDecimal tunnelTop = laneNo.subtract(new BigDecimal(1)).divide(lane,2,BigDecimal.ROUND_HALF_UP);
        Map<String, Object> map = new HashMap<>();
        map.put("tunnelLeft", tunnelLeft);
        map.put("tunnelTop",tunnelTop);
        return AjaxResult.success(map);
    }

    public void updateHandle(SdEvent sdEvent, String model){
        int count = sdEventHandleMapper.selectSdEventHandle(sdEvent.getId());
        if(count > 0){
            return;
        }
        SdEvent sdEvent1 = sdEventMapper.selectSdEventById(sdEvent.getId());
        //查询事件类型
        SdEventTypeMapper typeMapper = SpringUtils.getBean(SdEventTypeMapper.class);
        SdEventType sdEventType = typeMapper.selectSdEventTypeById(sdEvent1.getEventTypeId());
        //查询预案
        //List<SdReserveProcess> processList = SpringUtils.getBean(SdReserveProcessMapper.class).getProcessList(Long.valueOf(sdEvent.getCurrencyId()));
        //插入预警信息
        int sort = setWaring(sdEvent1, sdEventType, model);
        setEventHandleData(sort,sort,sdEvent);
    }

    public void setEventHandleData(int sort, int id, SdEvent sdEvent){
        if(sdEvent.getCurrencyId() == null || "".equals(sdEvent.getCurrencyId())){
            return;
        }
        SdReservePlan sdReservePlan = sdReservePlanMapper.selectSdReservePlanById(Long.valueOf(sdEvent.getCurrencyId()));
        List<SdReserveProcess> processList = SpringUtils.getBean(SdReserveProcessMapper.class).getProcessList(Long.valueOf(sdEvent.getCurrencyId()));
        for(SdReserveProcess item : processList){
            SdEventHandle sdEventHandle = new SdEventHandle();
            sdEventHandle.setEventId(sdEvent.getId());
            sdEventHandle.setFlowId(Long.valueOf(id));
            sdEventHandle.setFlowContent(item.getProcessStageName());
            sdEventHandle.setFlowSort(sort+"");
            sdEventHandle.setReserveName(sdReservePlan.getPlanName());
            List<String> reserveIds = new ArrayList<>();
            for(SdReserveProcess temp : item.getProcessesList()){
                SdJoinReserveHandle joinReserveHandle = new SdJoinReserveHandle();
                joinReserveHandle.setEventId(sdEvent.getId());
                joinReserveHandle.setStrategyRlId(temp.getStrategyId());
                List<SdJoinReserveHandle> joinDevList = joinMapper.selectSdJoinReserveHandleList(joinReserveHandle);
                if(joinDevList.size() == 0){
                    continue;
                }
                sort++;
                String flowId = id+"0";
                SdEventHandle sdEventHandle1 = new SdEventHandle();
                sdEventHandle1.setEventId(sdEvent.getId());
                sdEventHandle1.setFlowId(Long.valueOf(flowId + 1));
                sdEventHandle1.setFlowPid(Long.valueOf(id));
                sdEventHandle1.setFlowContent(temp.getProcessName());
                sdEventHandle1.setProcessId(joinDevList.get(0).getId());
                sdEventHandle1.setFlowSort(sort+"");
                sdEventHandle1.setReserveName(sdReservePlan.getPlanName());
                reserveIds.add(joinDevList.get(0).getId().toString());
                sdEventHandleMapper.insertSdEventHandle(sdEventHandle1);
            }
            sdEventHandle.setReserveId(StringUtils.join(reserveIds,","));
            sdEventHandleMapper.insertSdEventHandle(sdEventHandle);
            id++;
            sort++;
        }
    }

    /**
     * 插入预警信息
     * @param sdEvent
     * @param sdEventType
     * @return
     */
    public int setWaring(SdEvent sdEvent, SdEventType sdEventType, String model){
        SdEventHandle sdEventHandle = new SdEventHandle();
        sdEventHandle.setEventId(sdEvent.getId());
        sdEventHandle.setFlowId(1L);
        sdEventHandle.setFlowContent("预警");
        sdEventHandle.setFlowSort("1");
        sdEventHandleMapper.insertSdEventHandle(sdEventHandle);
        String name = EventDescEnum.getName(sdEvent.getEventSource());
        sdEventHandle = new SdEventHandle();
        sdEventHandle.setEventId(sdEvent.getId());
        sdEventHandle.setFlowId(2L);
        sdEventHandle.setFlowPid(1L);
        sdEventHandle.setFlowContent("预警记录".concat(name));
        sdEventHandle.setEventState("1");
        sdEventHandle.setFlowSort("2");
        sdEventHandleMapper.insertSdEventHandle(sdEventHandle);
        sdEventHandle.setFlowId(3L);
        sdEventHandle.setFlowSort("3");
        sdEventHandle.setFlowContent(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,DateUtils.getNowDate()).concat(" ")
                .concat("复核事件为【").concat(sdEventType.getEventType()).concat("】、【")
                .concat(EventGradeEnum.getValue(sdEvent.getEventGrade())).concat("】，复核事件为【突发事件处置】"));
        sdEventHandleMapper.insertSdEventHandle(sdEventHandle);
        //如第一次添加处置流程时新增事件流程记录
        if("add".equals(model)){
            SdEventFlowMapper flowMapper = SpringUtils.getBean(SdEventFlowMapper.class);
            SdEventFlow flow = new SdEventFlow();
            flow.setEventId(sdEvent.getId().toString());
            flow.setFlowTime(sdEvent.getEventTime());
            flow.setFlowHandler(SecurityUtils.getUsername());
            flow.setFlowDescription("预警记录".concat(name));
            flowMapper.insertSdEventFlow(flow);
            SdEventFlow eventFlow = new SdEventFlow();
            eventFlow.setEventId(sdEvent.getId().toString());
            //做区分+1秒
            Date nowDate = DateUtils.getNowDate();
            nowDate.setTime(DateUtils.getNowDate().getTime() + 1000);
            eventFlow.setFlowTime(sdEvent.getUpdateTime());
            eventFlow.setFlowHandler(SecurityUtils.getUsername());
            eventFlow.setFlowDescription(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,sdEvent.getUpdateTime()).concat(" ")
                    .concat("复核事件为【").concat(sdEventType.getEventType()).concat("】、【")
                    .concat(EventGradeEnum.getValue(sdEvent.getEventGrade())).concat("】，复核事件为【突发事件处置】"));
            flowMapper.insertSdEventFlow(eventFlow);
        }
        return 4;
    }

    /**
     * 事件详情-事件发现
     * @param eventDiscovery
     * @param eventDiscovery
     * @return
     */
    public List<String[]> setDiscoveryMap(SdEvent eventDiscovery){
        List<String[]> list = new ArrayList<>();
        list.add(new String[]{"告警来源",eventDiscovery.getEventSource()});
        list.add(new String[]{"告警时间",DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,eventDiscovery.getEventTime())});
        list.add(new String[]{"持续时长",eventDiscovery.getContinuedTime()});
        list.add(new String[]{"事发路段",eventDiscovery.getTunnelName()});
        list.add(new String[]{"事发位置",eventDiscovery.getStakeNum()});
        list.add(new String[]{"所属方向",eventDiscovery.getDirection()});
        return list;
    }

    /**
     * 事件详情-人工复核
     * @param manualReview
     * @return
     */
    public List<String[]> setReviewMap(SdEvent manualReview){
        List<String[]> list = new ArrayList<>();
        list.add(new String[]{"影响车道",manualReview.getLaneNo()});
        list.add(new String[]{"影响方向",manualReview.getDirection()});
        list.add(new String[]{"当事目标",manualReview.getConfidenceList()});
        list.add(new String[]{"影响描述",manualReview.getEventDescription()});
        list.add(new String[]{"预估类型",manualReview.getEventTypeName()});
        list.add(new String[]{"预估等级",manualReview.getEventGrade()});
        list.add(new String[]{"复核结果",manualReview.getEventState()});
        list.add(new String[]{"复核人",manualReview.getUpdateBy()});
        list.add(new String[]{"复核时间",DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,manualReview.getUpdateTime())});
        list.add(new String[]{"复核描述",manualReview.getReviewRemark()});
        return list;
    }

    /**
     * 事件详情-完结报告
     * @param endReport
     * @return
     */
    public List<String[]> setReportMap(SdEvent endReport){
        List<String[]> list = new ArrayList<>();
        list.add(new String[]{"完结时间",endReport.getEndTime()});
        list.add(new String[]{"确认人",endReport.getUpdateBy()});
        list.add(new String[]{"累计耗时",endReport.getContinuedTime()});
        list.add(new String[]{"完结原因",endReport.getRemark()});
        return list;
    }

    /**
     * 更新预案设备(除了指定设备)
     * @param sdEvent
     */
    public void setStrategyRlEquipment(SdEvent sdEvent){
        //查询预案
        SdReservePlanMapper planMapper = SpringUtils.getBean(SdReservePlanMapper.class);
        SdReservePlan sdReservePlan = planMapper.selectSdReservePlanById(Long.valueOf(sdEvent.getCurrencyId()));
        //查询流程
        SdReserveProcessMapper processMapper = SpringUtils.getBean(SdReserveProcessMapper.class);
        SdReserveProcess sdReserveProcess = new SdReserveProcess();
        sdReserveProcess.setReserveId(sdReservePlan.getId());
        List<SdReserveProcess> sdReserveProcesses = processMapper.selectSdReserveProcessList(sdReserveProcess);
        SdStrategyRlMapper rlMapper = SpringUtils.getBean(SdStrategyRlMapper.class);
        for(SdReserveProcess item : sdReserveProcesses){
            //将设备分离储存
            SdStrategyRl sdStrategyRl = rlMapper.selectSdStrategyRlById(item.getStrategyId());
            SdJoinReserveHandle joinReserveHandle = new SdJoinReserveHandle();
            joinReserveHandle.setEquipments("".equals(sdStrategyRl.getEquipments()) || sdStrategyRl.getEquipments() == null ? getRlDevice(sdEvent,sdStrategyRl) : sdStrategyRl.getEquipments());
            joinReserveHandle.setStrategyRlId(sdStrategyRl.getId());
            joinReserveHandle.setEventId(sdEvent.getId());
            joinReserveHandle.setState(sdStrategyRl.getState());
            joinReserveHandle.setEqTypeId(Long.valueOf(sdStrategyRl.getEqTypeId()));
            joinReserveHandle.setProcessName(item.getProcessName());
            joinReserveHandle.setCreateTime(DateUtils.getNowDate());
            //将情报板内容分离储存
            if(DevicesTypeEnum.VMS.getCode().toString().equals(sdStrategyRl.getEqTypeId()) || DevicesTypeEnum.MEN_JIA_VMS.getCode().toString().equals(sdStrategyRl.getEqTypeId())){
                //查询情报板信息
                IotBoardTemplateMapper templateMapper = SpringUtils.getBean(IotBoardTemplateMapper.class);
                SdJoinPlanStrategyMapper planStrategyMapper = SpringUtils.getBean(SdJoinPlanStrategyMapper.class);
                Map<String, Object> vmsData = new HashMap<>();
                //首先查询是否分离储存
                SdJoinPlanStrategy planStrategy1 = new SdJoinPlanStrategy();
                planStrategy1.setCurrentId(item.getId());
                planStrategy1.setTemplateId(sdStrategyRl.getState());
                planStrategy1.setType("2");
                Map<String, Object> templateContent = planStrategyMapper.getTemplateContent(planStrategy1);
                vmsData = templateContent == null ? templateMapper.getSdVmsTemplateContent(Long.valueOf(sdStrategyRl.getState())) : templateContent;
                if(vmsData == null){
                    continue;
                }
                SdJoinPlanStrategy planStrategy = new SdJoinPlanStrategy();
                planStrategy.setScreenSize(vmsData.get("screen_size").toString());
                planStrategy.setContent(vmsData.get("content").toString());
                planStrategy.setCoordinate(vmsData.get("coordinate").toString());
                planStrategy.setCurrentId(sdEvent.getId());
                planStrategy.setFontColor(vmsData.get("font_color").toString());
                planStrategy.setFontSize(Long.valueOf(vmsData.get("font_size").toString()));
                planStrategy.setFontSpacing(Long.valueOf(vmsData.get("font_spacing").toString()));
                planStrategy.setFontType(vmsData.get("font_type").toString());
                planStrategy.setTemplateId(sdStrategyRl.getState());
                //0：预案 1：策略
                planStrategy.setType("0");
                planStrategy.setCreateTime(DateUtils.getNowDate());
                planStrategy.setStopTime(Long.valueOf(vmsData.get("stop_time").toString()));
                planStrategyMapper.insertSdJoinPlanStrategy(planStrategy);
            }
            joinMapper.insertSdJoinReserveHandle(joinReserveHandle);
        }
    }

    /**
     * 根据设备检索规则查询设备
     * @param sdEventData
     * @param sdStrategyRl
     * @return
     */
    public String getRlDevice(SdEvent sdEventData, SdStrategyRl sdStrategyRl){
        SdEventMapper sdEventMapper = SpringUtils.getBean(SdEventMapper.class);
        SdDevicesMapper sdDevicesMapper = SpringUtils.getBean(SdDevicesMapper.class);
        //查询事件信息
        SdEvent sdEvent = sdEventMapper.selectSdEventById(sdEventData.getId());
        //整形桩号
        Integer stakeNum = Integer.valueOf(sdEvent.getStakeNum().replaceAll("ZK","").replaceAll("YK","").replaceAll("K", "").replaceAll(Pattern.quote("+"), "").replaceAll(" ", ""));
        List<String> rlDeviceList = new ArrayList<>();
        //检索规则条件
        String retrievalRule = sdStrategyRl.getRetrievalRule();
        //最近3公里
        if(EventSearchRulesEnum.TWO.getCode().equals(retrievalRule)){
            //获取设备信息
            rlDeviceList = threeKm(stakeNum,sdStrategyRl,sdEvent,sdEventData);
        }
        //附近5个
        if(EventSearchRulesEnum.THREE.getCode().equals(retrievalRule)){
            //获取设备信息
            rlDeviceList = latelyFive(stakeNum,sdStrategyRl,sdEvent,sdEventData);
        }
        //事发上游所有
        if(EventSearchRulesEnum.FOUR.getCode().equals(retrievalRule)){
            //获取设备信息
            rlDeviceList = eventUpstreamAll(stakeNum,sdStrategyRl,sdEvent,sdEventData);
        }
        //事发下游所有
        if(EventSearchRulesEnum.FIVE.getCode().equals(retrievalRule)){
            //获取设备信息
            rlDeviceList = eventDownstreamAll(stakeNum,sdStrategyRl,sdEvent,sdEventData);
        }
        //事件下游最近1个
        if(EventSearchRulesEnum.SEVEN.getCode().equals(retrievalRule)){
            //获取设备信息
            rlDeviceList = eventDownstreamOne(stakeNum,sdStrategyRl,sdEvent,sdEventData);
        }
        return StringUtils.join(rlDeviceList,",");
    }

    /**
     * 获取车道号
     * @param sdStrategyRl
     * @param sdEventData
     * @return
     */
    public String getLaneData(SdStrategyRl sdStrategyRl, SdEvent sdEventData){
        //车道
        String lane = null;
        //普通车指设备类型
        String czTypeId = DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode().toString();
        String zczTypeId = DevicesTypeEnum.ZHUO_ZHUAN_CHE_ZHI.getCode().toString();
        if(sdEventData.getLaneNo() != null && !"".equals(sdEventData.getLaneNo()) && czTypeId.equals(sdStrategyRl.getEqTypeId()) || zczTypeId.equals(sdStrategyRl.getEqTypeId())){
            lane = sdEventData.getLaneNo();
        }
        return lane;
    }

    /**
     * 最近3公里
     * @param stakeNum
     * @param sdStrategyRl
     * @param sdEvent
     * @return
     */
    public List<String> threeKm(Integer stakeNum, SdStrategyRl sdStrategyRl, SdEvent sdEvent, SdEvent sdEventData){
        //前3公里
        Integer frontStakeNum = stakeNum - 3000;
        //后3公里
        Integer afterStakeNum = stakeNum + 3000;
        //车道
        String lane = getLaneData(sdStrategyRl,sdEventData);
        //查询区间设备
        return sdDevicesMapper.getRlDevice(Integer.valueOf(sdStrategyRl.getEqTypeId()), sdEvent.getDirection(), frontStakeNum, afterStakeNum,sdEvent.getTunnelId(), lane);
    }

    /**
     * 最近5个
     * @param stakeNum
     * @param sdStrategyRl
     * @param sdEvent
     * @param sdEventData
     * @return
     */
    public List<String> latelyFive(Integer stakeNum, SdStrategyRl sdStrategyRl, SdEvent sdEvent, SdEvent sdEventData){
        //车道
        String lane = getLaneData(sdStrategyRl,sdEventData);
        List<String> frontLatelyFive = sdDevicesMapper.getFrontLatelyFive(Integer.valueOf(sdStrategyRl.getEqTypeId()), sdEvent.getDirection(), stakeNum,sdEvent.getTunnelId(), lane);
        List<String> afterLatelyFive = sdDevicesMapper.getAfterLatelyFive(Integer.valueOf(sdStrategyRl.getEqTypeId()), sdEvent.getDirection(), stakeNum,sdEvent.getTunnelId(), lane);
        return setLatelyFive(frontLatelyFive,afterLatelyFive);
    }

    /**
     * 事发上游所有
     * @param stakeNum
     * @param sdStrategyRl
     * @param sdEvent
     * @param sdEventData
     * @return
     */
    public List<String> eventUpstreamAll(Integer stakeNum, SdStrategyRl sdStrategyRl, SdEvent sdEvent, SdEvent sdEventData){
        //车道
        String lane = getLaneData(sdStrategyRl,sdEventData);
        return sdDevicesMapper.getRlDevice(Integer.valueOf(sdStrategyRl.getEqTypeId()), sdEvent.getDirection(), 0, stakeNum,sdEvent.getTunnelId(), lane);
    }

    /**
     * 事发下游所有
     * @param stakeNum
     * @param sdStrategyRl
     * @param sdEvent
     * @param sdEventData
     * @return
     */
    public List<String> eventDownstreamAll(Integer stakeNum, SdStrategyRl sdStrategyRl, SdEvent sdEvent, SdEvent sdEventData){
        //车道
        String lane = getLaneData(sdStrategyRl,sdEventData);
        return sdDevicesMapper.getRlDevice(Integer.valueOf(sdStrategyRl.getEqTypeId()), sdEvent.getDirection(), stakeNum, 0,sdEvent.getTunnelId(), lane);
    }

    /**
     * 事件下游最近1个
     * @param stakeNum
     * @param sdStrategyRl
     * @param sdEvent
     * @param sdEventData
     * @return
     */
    public List<String> eventDownstreamOne(Integer stakeNum, SdStrategyRl sdStrategyRl, SdEvent sdEvent, SdEvent sdEventData){
        //车道
        String lane = getLaneData(sdStrategyRl,sdEventData);
        List<String> afterLatelyFive = sdDevicesMapper.getAfterLatelyFive(Integer.valueOf(sdStrategyRl.getEqTypeId()), sdEvent.getDirection(), stakeNum, sdEvent.getTunnelId(), lane);
        return afterLatelyFive.size() == 0 ? new ArrayList<>() : afterLatelyFive.subList(0,1);
    }

    /**
     * 寻找最近5个
     * @param frontLatelyFive
     * @param afterLatelyFive
     * @return
     */
    public List<String> setLatelyFive(List<String> frontLatelyFive, List<String> afterLatelyFive){
        int listSize = frontLatelyFive.size() + afterLatelyFive.size();
        if(listSize == 5 || listSize > 0 && listSize < 5){
            for(String item : afterLatelyFive){
                frontLatelyFive.add(item);
            }
            return frontLatelyFive;
        }
        if(listSize > 5){
            if(frontLatelyFive.size() > afterLatelyFive.size() && afterLatelyFive.size() >= 2){
                List<String> list = new ArrayList<>();
                for(String item : frontLatelyFive){
                    list.add(item);
                    if(list.size() == 3){
                        break;
                    }
                }
                for(String item : afterLatelyFive){
                    list.add(item);
                    if(list.size() == 5){
                        break;
                    }
                }
                return list;
            }
            if(frontLatelyFive.size() > afterLatelyFive.size() && afterLatelyFive.size() == 1){
                List<String> list = new ArrayList<>();
                for(String item : frontLatelyFive){
                    list.add(item);
                    if(list.size() == 4){
                        break;
                    }
                }
                for(String item : afterLatelyFive){
                    list.add(item);
                    if(list.size() == 5){
                        break;
                    }
                }
                return list;
            }
            if(frontLatelyFive.size() > afterLatelyFive.size() && afterLatelyFive.size() == 0){
                List<String> list = new ArrayList<>();
                for(String item : frontLatelyFive){
                    list.add(item);
                    if(list.size() == 5){
                        break;
                    }
                }
                return list;
            }

            if(frontLatelyFive.size() < afterLatelyFive.size() && frontLatelyFive.size() >= 2){
                List<String> list = new ArrayList<>();
                for(String item : frontLatelyFive){
                    list.add(item);
                    if(list.size() == 2){
                        break;
                    }
                }
                for(String item : afterLatelyFive){
                    list.add(item);
                    if(list.size() == 5){
                        break;
                    }
                }
                return list;
            }
            if(frontLatelyFive.size() < afterLatelyFive.size() && frontLatelyFive.size() == 1){
                List<String> list = new ArrayList<>();
                for(String item : frontLatelyFive){
                    list.add(item);
                    if(list.size() == 1){
                        break;
                    }
                }
                for(String item : afterLatelyFive){
                    list.add(item);
                    if(list.size() == 5){
                        break;
                    }
                }
                return list;
            }
            if(frontLatelyFive.size() < afterLatelyFive.size() && frontLatelyFive.size() == 0){
                List<String> list = new ArrayList<>();
                for(String item : afterLatelyFive){
                    list.add(item);
                    if(list.size() == 5){
                        break;
                    }
                }
                return list;
            }
        }
        return new ArrayList<>();
    }

    /**
     * 获取应急处置设备详情
     * @param rpId
     * @return
     */
    public Map<String, Object> deviceDateiled(Long rpId, Long eventId){
        Map<String, Object> map = new HashMap<>();
        if(eventId != 0){
            //查询事件流程状态
            SdEventHandle sdEventHandle = new SdEventHandle();
            sdEventHandle.setEventId(eventId);
            sdEventHandle.setProcessId(rpId);
            String handleState = sdEventHandleMapper.selectSdEventHandleList(sdEventHandle).get(0).getEventState();
            map.put("handleState",handleState);
        }
        //查询设备列表
        SdReserveProcess sdReserveProcess = new SdReserveProcess();
        sdReserveProcess.setId(rpId);
        List<Map<String, Object>> deviceList = sdEventMapper.getManagementDevice(sdReserveProcess);
        SdJoinReserveHandle joinReserveHandle = joinMapper.selectSdJoinReserveHandleById(rpId);
        //获取设备类型
        Long eqType = joinReserveHandle.getEqTypeId();
        //根据状态类型查询不通类型得设备明细
        if(eqType == DevicesTypeEnum.VMS.getCode() || eqType == DevicesTypeEnum.MEN_JIA_VMS.getCode()){
            //查询情报板
            //String vmsData = sdEventMapper.getManagementVmsLs(sdReserveProcess);
            //Map<String, Object> sdVmsContent = SpringUtils.getBean(IotBoardTemplateMapper.class).getSdVmsTemplateContent(Long.valueOf(joinReserveHandle.getState()));
            SdJoinPlanStrategy planStrategy = new SdJoinPlanStrategy();
            planStrategy.setCurrentId(joinReserveHandle.getEventId());
            planStrategy.setTemplateId(joinReserveHandle.getState());
            //0：预案 1：策略
            planStrategy.setType("0");
            Map<String, Object> sdVmsContent = SpringUtils.getBean(SdJoinPlanStrategyMapper.class).getTemplateContent(planStrategy);
            map.put("vmsData",sdVmsContent);
        }else if(eqType == DevicesTypeEnum.LS.getCode()){
            //截取广播文件名称
            //String lsData = sdEventMapper.getManagementVmsLs(sdReserveProcess);
            List<String> list = Arrays.asList(joinReserveHandle.getState().split("\\\\"));
            String lsContent = list.get(list.size() - 1);
            map.put("lsData",lsContent);
        }else {
            //查询普通设备状态
            List<Map<String, Object>> maps = new ArrayList<>();
            if(eqType == DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode() || eqType == DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode()){
                maps = sdEventMapper.getManagementJiaQiangState(rpId,Integer.valueOf(joinReserveHandle.getState()) > 0 ? "1" : "2");
                map.put("deviceState",Integer.valueOf(joinReserveHandle.getState()) > 0 ? maps.get(0).get("stateName") + ",亮度值：" + joinReserveHandle.getState() + "%" : maps.get(0).get("stateName"));
            }else {
                maps = sdEventMapper.getManagementDeviceState(rpId);
                map.put("deviceState",maps.get(0).get("stateName"));
            }
            List<String> deviceIconUrl = new ArrayList<>();
            maps.stream().forEach(item -> {
                deviceIconUrl.add(item.get("url").toString());
            });
            map.put("deviceIconUrl",deviceIconUrl);
        }
        map.put("processName",joinReserveHandle.getProcessName());
        map.put("deviceList",deviceList);
        map.put("deviceType",eqType);
        return map;
    }

    /**
     * 查询最新预案
     * @param sdEventData
     * @return
     */
    public Map<String, Object> setPlanDataMap(SdEvent sdEventData){
        //查询事件详情-预案处置
        List<SdEventHandle> planDisposal = getHandle(sdEventData);
        Map<String, Object> planMap = new HashMap<>();
        if(sdEventData.getCurrencyId() != null && sdEventData.getCurrencyId() != ""){
            planMap.put("planName",sdEventMapper.getEventInif(sdEventData).get("planName"));
        }else {
            planMap.put("planName",null);
        }
        List<SdEventHandle> treePlanList = getTreePlanList(planDisposal);
        planMap.put("planList",treePlanList);
        return planMap;
    }

    /**
     * 查询历史预案
     * @param sdEventData
     * @return
     */
    public List<Map<String, Object>> setHistoryPlanDataMap(SdEvent sdEventData){
        //历史预案集合
        List<Map<String, Object>> mapList = new ArrayList<>();
        //查询有几次历史记录
        String num = historyMapper.selectNum(sdEventData.getId());
        //如果不存在历史记录则返回
        if("0".equals(num)){
            return new ArrayList<>();
        }
        //查询预案节点
        SdEventHandle handleHistory = new SdEventHandle();
        handleHistory.setEventId(sdEventData.getId());
        List<SdEventHandle> handleList = historyMapper.selectSdEventHandleList(handleHistory);
        for(int i = 0; i < Integer.valueOf(num); i++){
            Map<String, Object> historyPlanMap = new HashMap<>();
            //拆分历史记录
            String number = i+1+"";
            List<SdEventHandle> collect = handleList.stream().filter(item -> item.getFlowNum().equals(number)).collect(Collectors.toList());
            List<SdEventHandle> treePlanList = getTreePlanList(collect);
            //查询预案名称
            List<SdEventHandle> collect1 = collect.stream().filter(item -> item.getReserveId() != null).collect(Collectors.toList());
            if(collect1.size() > 0){
                historyPlanMap.put("planName",collect1.get(0).getReserveId());
            }
            historyPlanMap.put("planList",treePlanList);
            mapList.add(historyPlanMap);
        }
        return mapList;
    }

    /**
     * 数据转为tree数据
     * @return
     */
    public List<SdEventHandle> getTreePlanList(List<SdEventHandle> planDisposal){
        //将数据转为tree数据
        List<SdEventHandle> collectPid = planDisposal.stream().filter(item -> item.getFlowPid() == null).collect(Collectors.toList());
        List<SdEventHandle> collectId = planDisposal.stream().filter(item -> item.getFlowPid() != null).collect(Collectors.toList());
        collectPid.stream().forEach(item -> {
            List<SdEventHandle> list = new ArrayList<>();
            collectId.stream().forEach(temp -> {
                if(item.getFlowId() == temp.getFlowPid()){
                    list.add(temp);
                }
            });
            item.setChildren(list);
        });
        return collectPid;
    }

    /**
     * 导出图片内容
     * @param imgUrl
     * @return
     */
    public Map<String, Object> setImgMap(String imgUrl){
        Map <String,Object> map = new HashMap <String, Object>();
        byte[] fileStream = WorderToNewWordUtils.getFileStream(imgUrl);
        if(fileStream == null){
            return null;
        }
        map.put("width", 550);
        map.put("height", 300);
        map.put("type", "jpg");
        map.put("content", fileStream);
        return map;
    }

    /**
     * 应急预案or联控策略设备集合
     * @param sdStrategyRls
     */
    public List<Map<String, Object>> setDeviceAllList(List<SdStrategyRl> sdStrategyRls){
        //设备集合
        List<Map<String, Object>> deviceList = new ArrayList<>();
        for(SdStrategyRl item : sdStrategyRls){
            Long eqTypeId = Long.valueOf(item.getEqTypeId());
            //查询设备信息以及状态
            List<Map<String, Object>> maps = new ArrayList<>();
            if(eqTypeId == DevicesTypeEnum.VMS.getCode() || eqTypeId == DevicesTypeEnum.MEN_JIA_VMS.getCode()){
                maps = sdDevicesMapper.selectVmsDevicesOld(item.getEquipments(), item.getState(), "2", item.getCurrentId());
                if(maps.size() == 0){
                    maps = sdDevicesMapper.selectVmsDevices(item.getEquipments(), item.getState());
                }
            }else if(eqTypeId == DevicesTypeEnum.LS.getCode()){
                maps = sdDevicesMapper.selectLsDevices(item.getEquipments());
                maps.stream().forEach(temp -> {
                    List<String> list = Arrays.asList(item.getState().split("\\\\"));
                    String lsContent = list.get(list.size() - 1);
                    temp.put("state",lsContent);
                });
            }else {
                if(eqTypeId == DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode() || eqTypeId == DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode()){
                    List<Map<String, Object>> linkList = sdDevicesMapper.selectDevices(item.getEquipments(), Integer.valueOf(item.getState()) > 0 ? "1" : "2");
                    linkList.stream().forEach(tem -> {
                        String mes = null;
                        if(Integer.valueOf(item.getState()) > 0 && item.getStateNum() != null && !"".equals(item.getStateNum())){
                            mes = tem.get("stateName") + "," + "亮度值：" + item.getStateNum() + "%";
                        }else if(Integer.valueOf(item.getState()) > 0 && item.getStateNum() == null || "".equals(item.getStateNum())){
                            mes = tem.get("stateName") + "," + "亮度值：" + item.getState() + "%";
                        }else if(Integer.valueOf(item.getState()) == 0){
                            mes = tem.get("stateName").toString();
                        }
                        //String stateData = Integer.valueOf(item.getState()) > 0 ? tem.get("stateName") + "," + "亮度值：" + item.getState() + "%" : tem.get("stateName").toString();
                        tem.put("stateName",mes);
                    });
                    maps = linkList;
                }else {
                    maps = sdDevicesMapper.selectDevices(item.getEquipments(), item.getState());
                }
            }
            deviceList.addAll(maps);
        }
        return deviceList;
    }

    /**
     * 储存事件处置流程
     * @param eventId
     * @param content
     */
    public void instreEventFlowData(Long eventId, String content){
        SdEventFlow eventFlow = new SdEventFlow();
        eventFlow.setEventId(eventId.toString());
        eventFlow.setFlowTime(DateUtils.getNowDate());
        eventFlow.setFlowDescription(content);
        eventFlow.setFlowHandler(SecurityUtils.getUsername());
        sdEventFlowMapper.insertSdEventFlow(eventFlow);
        //事件完结删除redis事故信息 前端不在展示
        redisCache.deleteObject(getCacheEventKey(eventId.toString()));
    }

    /**
     * 获取事件详情录像信息
     * @param sdEvent
     * @return
     */
    @Override
    public AjaxResult vedioData(SdEvent sdEvent){
        Map<String, Object> map = new HashMap<>();
        //查询事件信息
        SdEvent sdEvent1 = sdEventMapper.selectSdEventById(sdEvent.getId());
        //查询事件桩号附近摄像机
        List<SdDevices> eventCamera = getEventCamera(sdEvent1.getTunnelId(), sdEvent1.getStakeNum(), sdEvent1.getDirection());
        if(eventCamera.size() > 0){
            SdDevices sdDevices = eventCamera.get(0);
            if(sdDevices.getExternalDeviceId() == null || "".equals(sdDevices.getExternalDeviceId())){
                return AjaxResult.success(map);
            }
            //获取录像视频流
            Map<String, Object> vedioData = getVedioData(sdDevices.getExternalDeviceId(), sdEvent.getStartTime(), null);
            if(vedioData != null && !"".equals(vedioData) && vedioData.get("recCode") != null){
                List<SdTrafficImage> list = new ArrayList<>();
                SdTrafficImage image = new SdTrafficImage();
                image.setImgType("2");
                image.setImgUrl(vedioData.get("liveUrl").toString());
                list.add(image);
                sdEvent.setHistoryUrlList(list);
                map.put("vedioList",sdEvent.getHistoryUrlList());
                map.put("vedioData",vedioData);
                /*map.put("camId", vedioData.get("camID"));
                map.put("playId", vedioData.get("playId"));
                map.put("liveUrl", vedioData.get("liveUrl"));*/
            }else {
                return AjaxResult.success(map);
            }
        }
        return AjaxResult.success(map);
    }

    /**
     * 获取相机token
     *
     * @return
     */
    public String getToken(){

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        String userName = "hsdsdVideo";
        String password = "hsdsdVideo";

        ExternalSystem system = new ExternalSystem();
        system.setSystemCode(ExternalSystemCode.VIDEO_MANGE.getCode());
        List<ExternalSystem> list = SpringUtils.getBean(IExternalSystemService.class).selectExternalSystemList(system);

        ExternalSystem externalSystem = list.get(0);
        // 获取数据库第三方配置信息
        if(externalSystem != null){
            address = externalSystem.getSystemUrl();
            userName = externalSystem.getUsername();
            password = externalSystem.getPassword();
          //  deptId = externalSystem.getSystemParam();
        }

        String url = address+"/apiLogin";

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", userName);
        requestBody.put("password", password);

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);
        /*UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("username","hsdsdVideo")
                .queryParam("password","hsdsdVideo");
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);*/
        try{
            //ResponseEntity<Map> exchange = template.exchange(builder.build().toUri(), HttpMethod.POST, requestEntity, Map.class);
            ResponseEntity<Map> exchange = template.exchange(url, HttpMethod.POST, httpEntity, Map.class);
            Map body = exchange.getBody();
            return Optional.ofNullable(body.get("token")).orElseGet(()->"").toString();
        }catch(Exception ex){
            logger.info("获取相继录像失败：{}",ex.getMessage());
            return null;
        }
    }

    /**
     * 获取缓存token
     * @return
     */
    public String getCacheToken(){
        //token缓存key值
        String key = "video_platform_token";
        //token有效时间15分钟
        Integer expireTime = 15;
        //获取缓存token
        String token = redisCache.getCacheObject(key);
        if(token == null || "".equals(token)){
            //缓存中获取不到token，重新从接口中获取，更新缓存
            token = getToken();
            redisCache.setCacheObject( key, token, expireTime, TimeUnit.MINUTES);
        }
        return token;
    }

    /**
     * 下载事件录像
     * @return
     */
    public Map<String, Object> downloadVedio(String camId, String startTime){
        HttpEntity<String> requestEntity = setHttpEntity();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(address + "/videoInfo/api/downloadVideotape")
                //.queryParam("camId", camId)
                .queryParam("camId", "52054")
                .queryParam("startTime", startTime)
                .queryParam("duration", 5);
        try {
            ResponseEntity<String> exchange = template.exchange(builder.build().toUri(), HttpMethod.GET, requestEntity, String.class);
            JSONObject object = JSONObject.parseObject(exchange.getBody()).getJSONObject("data");
            return Optional.ofNullable(object).orElseGet(() -> JSONObject.parseObject(exchange.getBody()));
        }catch (Exception e){
            logger.error("下载事件录像发生异常：{}",e.getMessage());
            return null;
        }
    }

    /**
     * 获取事件录像
     * @param camId
     * @param startTime
     * @param playId
     * @return
     */
    public Map<String, Object> getVedioData(String camId, String startTime, String playId) {
        HttpEntity<String> requestEntity = setHttpEntity();

        UriComponentsBuilder builder = null;
        if(playId == null || "".equals(playId)){
            builder = UriComponentsBuilder.fromHttpUrl(address + "/videoInfo/api/openVideotape")
                    //.queryParam("camId", camId)
                    .queryParam("camId", "52054")
                    .queryParam("startTime", startTime);
        }else {
            builder = UriComponentsBuilder.fromHttpUrl(address + "/videoInfo/api/closeVideotape")
                    //.queryParam("camId", camId)
                    .queryParam("camId", "52054")
                    .queryParam("playId", playId);
        }
        try {
            ResponseEntity<String> exchange = template.exchange(builder.build().toUri(), HttpMethod.GET, requestEntity, String.class);
            JSONObject object = JSONObject.parseObject(exchange.getBody()).getJSONObject("data");
            return Optional.ofNullable(object).orElseGet(() -> JSONObject.parseObject(exchange.getBody()));
        }catch (Exception e){
            logger.error("打开相机录像发生异常：{}",e.getMessage());
            return null;
        }

        /*Map<String, Object> map = new HashMap<>();
        map.put("camID","111");
        map.put("playId","222");
        map.put("liveUrl","http://10.7.187.37:8002/videoRep/2023-03-04/50013_intrusion2023-03-04-13-07-30.mp4");
        map.put("recCode","0");
        return map;*/
    }

    /**
     * 下载事件录像视频
     * @param camId
     * @param startTime
     * @param playId
     * @return
     */
    public Map<String, Object> getDownloadVedio(String camId, String startTime, String playId) {
        HttpEntity<String> requestEntity = setHttpEntity();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(address + "/videoInfo/api/downloadVideotape")
                .queryParam("camId", camId)
                .queryParam("startTime", startTime)
                .queryParam("duration",5);
        try {
            ResponseEntity<String> exchange = template.exchange(builder.build().toUri(), HttpMethod.GET, requestEntity, String.class);
            JSONObject object = JSONObject.parseObject(exchange.getBody()).getJSONObject("data");
            return Optional.ofNullable(object).orElseGet(() -> JSONObject.parseObject(exchange.getBody()));
        }catch (Exception e){
            logger.error("打开相机录像发生异常：{}",e.getMessage());
            return null;
        }

        /*Map<String, Object> map = new HashMap<>();
        map.put("camID","111");
        map.put("playId","222");
        map.put("liveUrl","http://10.7.187.37:8002/videoRep/2023-03-04/50013_intrusion2023-03-04-13-07-30.mp4");
        map.put("recCode","0");
        return map;*/
    }

    /**
     * 添加http头部
     * @return
     */
    public HttpEntity<String> setHttpEntity(){
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        if(getCacheToken() == null){
            return null;
        }
        headers.add("Authorization", getCacheToken());
        headers.setContentType(type);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        return requestEntity;
    }

    //组装event数据
    public Map<String, Object> setEventData(SdEvent sdEvent){
        Map<String, Object> map = new HashMap<>();
        map.put("endTime",sdEvent.getEndTime());
        map.put("id",sdEvent.getId());
        map.put("eventSource",sdEvent.getEventSource());
        map.put("eventState",sdEvent.getEventState());
        map.put("eventLongitude",sdEvent.getEventLongitude());
        map.put("eventLatitude",sdEvent.getEventLatitude());
        map.put("eventTypeId",sdEvent.getEventTypeId());
        map.put("laneNo",sdEvent.getLaneNo());
        map.put("passengerCarNum",sdEvent.getPassengerCarNum());
        map.put("slightInjured",sdEvent.getSlightInjured());
        map.put("smallCarNum",sdEvent.getSmallCarNum());
        map.put("stakeNum",sdEvent.getStakeNum());
        map.put("startTime",sdEvent.getStartTime());
        map.put("tankerNum",sdEvent.getTankerNum());
        map.put("truckNum",sdEvent.getTankerNum());
        map.put("tunnelId",sdEvent.getTunnelId());
        map.put("eventTitle",sdEvent.getEventTitle());
        map.put("eventTime",sdEvent.getEventTime());
        map.put("eventGrade",sdEvent.getEventGrade());
        map.put("direction",sdEvent.getDirection());
        map.put("eventDescription",sdEvent.getEventDescription());
        map.put("currencyId",sdEvent.getCurrencyId());
        map.put("flowId",sdEvent.getFlowId());
        map.put("stakeEndNum",sdEvent.getStakeEndNum());
        map.put("videoUrl",sdEvent.getVideoUrl());
        map.put("createTime",sdEvent.getCreateTime());
        map.put("updateBy",sdEvent.getUpdateBy());
        map.put("updateTime",sdEvent.getUpdateTime());
        map.put("remark",sdEvent.getRemark());
        map.put("eventImgUrl",sdEvent.getEventImgUrl());
        map.put("reviewRemark",sdEvent.getReviewRemark());
        map.put("simplifyName",sdEvent.getSimplifyName());
        map.put("tunnelName",sdEvent.getTunnelName());
        return map;
    }

    //将类对象里面的null数据以及date等进行转换
    public static <T> T noNullStringAttr(T cls) {
        Field[] fields = cls.getClass().getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return cls;
        }
        for (Field field : fields) {
            if ("String".equals(field.getType().getSimpleName()) || "Date".equals(field.getType().getSimpleName()) || "Long".equals(field.getType().getSimpleName())) {
                field.setAccessible(true);
                try {
                    Object value = field.get(cls);
                    if (value == null) {
                        field.set(cls, "");
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return cls;
    }

    private void protocolAnalysis(String data) {
        SendDeviceStatusToKafkaService sendData = SpringUtils.getBean(SendDeviceStatusToKafkaService.class);
        ExternalSystemMapper externalSystemMapper = SpringUtils.getBean(ExternalSystemMapper.class);
        SdDevicesMapper sdDevicesMapper = SpringUtils.getBean(SdDevicesMapper.class);
        SdEventTypeMapper sdEventTypeMapper = SpringUtils.getBean(SdEventTypeMapper.class);
        SdDeviceDataMapper sdDeviceDataMapper = SpringUtils.getBean(SdDeviceDataMapper.class);
        // 主机地址
        if ("".equals(data) || data == null) {
            return;
        }
        if (!data.contains("火警") && !data.contains("模块或探头故障") && !data.contains("模块或探头恢复")
                && !data.contains("全部声光启动") && !data.contains("全部声光停止")) {
            return;
        }
        //拿到的报文就是纯文字的报文，直接进行解析
        //传输格式例：火警: 1号机1回路3地址 点型感烟   001层 西核彩桥F102     2022-12-27 08:42:18
        //         事件:+空格+机号+回路号+地址号+空格+设备类型+空格+层号+空格+地理位置+空格+年月日+空格+时分秒
        if (data.contains(":")) {
            String alarmType = data.substring(0, data.indexOf(":"));

            data = data.substring(data.indexOf(":") + 2);
            String host = data.substring(0, data.indexOf("号"));
            //查询外部系统ID
            ExternalSystem externalSystem = new ExternalSystem();
            externalSystem.setSystemName("火灾报警系统");
            externalSystem.setSystemUrl("100.7.187.7");
            List<ExternalSystem> externalSystems = externalSystemMapper.selectExternalSystemList(externalSystem);
            ExternalSystem system = externalSystems.get(0);
            if (externalSystems.isEmpty()) {
                return;
            }
            Long systemId = system.getId();
            String address = data.substring(0, data.indexOf("号"));
            data = data.substring(data.indexOf("机") + 1);
            String loop = data.substring(0, data.indexOf("回"));
            data = data.substring(data.indexOf("路") + 1);

            data = data.substring(data.indexOf("址") + 2);
            String sourceDevice = data.substring(0, data.indexOf(" "));
            //剩层号后的内容
//            data = data.substring(data.indexOf(" ") + 1);
            //剩地理位置后的内容
//            data = data.substring(data.indexOf(" ") + 1);
            String alarmTime = data.substring(data.length() - 19);
            Date now = new Date();
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                now = format.parse(alarmTime);
            } catch (Exception e) {

            }
            //根据火灾报警主机外部系统ID和回路确定是哪一批设备
            SdDevices devices = new SdDevices();
            devices.setExternalSystemId(systemId);
            devices.setExternalDeviceId(loop);
            List<SdDevices> devicesLists = sdDevicesMapper.selectSdDevicesList(devices);
            //遍历确定是哪个部件
            String alarmComponentEqId = "";
            SdDevices sdDevices = new SdDevices();
            for (int i = 0; i < devicesLists.size(); i++) {
                SdDevices component = devicesLists.get(i);
                String eqFeedbackAddress1 = component.getQueryPointAddress();
                if (eqFeedbackAddress1.equals(address)) {
                    alarmComponentEqId = component.getEqId();
                    sdDevices = component;
                    break;
                }
            }
            //确定报警类型
            Long itemId = 0L;
            //查询当前属于什么事件类型
            SdEventType sdEventType = new SdEventType();
            sdEventType.setEventType("火灾");
            if (sourceDevice.equals("手报")) {
                sourceDevice = "智能手动报警按钮报警";
                itemId = Long.valueOf(DevicesTypeItemEnum.SHOU_BAO_ALARM.getCode());
            } else if (sourceDevice.equals("探测器")) {
                sourceDevice = "火焰探测器报警";
                itemId = Long.valueOf(DevicesTypeItemEnum.FLAME_DETECTOR_ALARM.getCode());
            } else if (sourceDevice.equals("声光")) {
                sourceDevice = "声光报警器报警";
                itemId = Long.valueOf(DevicesTypeItemEnum.SHENG_GUANG_ALARM.getCode());
            }
            Long eventTypeId = 0L;
            if (sdEventType.getEventType() != null) {
                List<SdEventType> sdEventTypes = sdEventTypeMapper.selectSdEventTypeList(sdEventType);
                eventTypeId = sdEventTypes.get(0).getId();
            }
            if (eventTypeId == 0) {
                return;
            }
            //事件相关的设备要把数据更新到device_data中
            SdDeviceData sdDeviceData = new SdDeviceData();
            sdDeviceData.setDeviceId(sdDevices.getEqId());
            sdDeviceData.setItemId(itemId);
            List<SdDeviceData> deviceData = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
            if (deviceData.isEmpty()) {
                sdDeviceData.setData("1");
                sdDeviceData.setCreateTime(new Date());
                sdDeviceDataMapper.insertSdDeviceData(sdDeviceData);
            } else {
                SdDeviceData devData = deviceData.get(0);
                devData.setUpdateTime(new Date());
                sdDeviceDataMapper.updateSdDeviceData(devData);
            }
            //存储事件到事件表
            SdEvent sdEvent = new SdEvent();
            sdEvent.setTunnelId(sdDevices.getEqTunnelId());
            sdEvent.setEventTypeId(eventTypeId);
            if (alarmType.contains("故障")) {
                sdEvent.setEventTitle(sourceDevice + "故障事件");
            } else {
                sdEvent.setEventTitle(sourceDevice + "，火灾报警事件");
            }
            sdEvent.setEventSource("2");
            //要改
            sdEvent.setEventState(EventStateEnum.unprocessed.getCode());
            //事件描述
            sdEvent.setEventDescription(alarmType);
            sdEvent.setStakeNum(sdDevices.getPile());
            if(sdEvent.getStakeNum().contains("ZK")){
                sdEvent.setDirection("2");
            }else{
                sdEvent.setDirection("1");
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = formatter.format(new Date());
            sdEvent.setEventTime(dateZh(time));
            sdEvent.setCreateTime(dateZh(time));
            sdEvent.setStartTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,DateUtils.getNowDate()));
            sdEvent.setEventGrade("1");
            sdEventMapper.insertSdEvent(sdEvent);
            eventSendWeb(sdEvent);
        } else {
            return;
        }
    }

    public Date dateZh(String timeData){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //转换
        Date time = null;
        try {
            if(timeData != null && !"".equals(timeData)){
                time = sdf.parse(timeData);
                return time;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 将事件推送到前端
     *  @param sdEvent
     */
    public void eventSendWeb(SdEvent sdEvent){
        //新增后再查询数据库，返回给前端事件图标等字段
        SdEvent sdEventData = new SdEvent();
        sdEventData.setId(sdEvent.getId());
        List<SdEvent> sdEventList = sdEventMapper.selectSdEventList(sdEventData);
        sdEventList.stream().forEach(item -> item.setIds(item.getId().toString()));
        List<SdTunnels> sdTunnelsList = tunnelsService.selectTunnels(SecurityUtils.getDeptId());
        List<SdTunnels> collect = sdTunnelsList.stream().filter(item -> item.getTunnelId().equals(sdEventList.get(0).getTunnelId())).collect(Collectors.toList());
        if(collect.size() > 0){
            JSONObject object = new JSONObject();
            object.put("sdEventList", sdEventList);
            WebSocketService.broadcast("sdEventList",object.toString());
        }
    }
}
