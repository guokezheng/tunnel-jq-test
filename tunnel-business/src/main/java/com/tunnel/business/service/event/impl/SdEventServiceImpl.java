package com.tunnel.business.service.event.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.*;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.event.*;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.mapper.digitalmodel.RadarEventMapper;
import com.tunnel.business.mapper.event.*;
import com.tunnel.business.mapper.informationBoard.IotBoardTemplateMapper;
import com.tunnel.business.mapper.logRecord.SdOperationLogMapper;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficImageMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.digitalmodel.impl.RadarEventServiceImpl;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.utils.util.UUIDUtil;
import com.tunnel.business.utils.work.CustomXWPFDocument;
import com.tunnel.business.utils.work.WorderToNewWordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
            SdTrafficImage image = new SdTrafficImage();
            image.setBusinessId(id.toString());
            image.setImgType("1");
            //查询视频
            List<SdTrafficImage> sdTrafficImages = sdTrafficImageMapper.selectSdTrafficImageList(image);
            sdEvent.setVideoUrl(sdTrafficImages.size() > 0 ? sdTrafficImages.get(0).getImgUrl().split(";")[0] : "");
            //查询视频图片
            List<SdTrafficImage> image1 = sdTrafficImageMapper.selectImageByBusinessId(sdEvent.getId().toString());
            sdEvent.setIconUrlList(image1.subList(0,image1.size() > 10 ? 10 : image1.size()));
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
        sdEvents.stream().forEach(item -> {
            item.setPosition(item.getTunnelName().concat(DeviceDirectionEnum.getValue(item.getDirection())).concat(item.getStakeNum() == null ? "" : item.getStakeNum()));
            if(item.getVideoUrl()!=null){
                item.setVideoUrl(item.getVideoUrl().split(";")[0]);
            }
            SdTrafficImage image = new SdTrafficImage();
            image.setBusinessId(item.getId().toString());
            image.setImgType("1");
            //查询视频
            List<SdTrafficImage> sdTrafficImages = sdTrafficImageMapper.selectSdTrafficImageList(image);
            item.setVideoUrl(sdTrafficImages.size() > 0 ? sdTrafficImages.get(0).getImgUrl().split(";")[0] : "");
            //查询视频图片
            List<SdTrafficImage> image1 = sdTrafficImageMapper.selectImageByBusinessId(item.getId().toString());
            item.setIconUrlList(image1.subList(0,image1.size() > 10 ? 10 : image1.size()));
            item.setConfidenceList(radarEventMapper.selectConfidence(item.getId()));
            for(SdTrafficImage temp : image1){
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
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public int updateSdEvent(SdEvent sdEvent) {
        if ("1".equals(sdEvent.getEventState())) {
            instreEventFlowData(sdEvent.getId(),"事件已完结");
            sdEvent.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,DateUtils.getNowDate()));
        }else if("0".equals(sdEvent.getEventState()) && PrevControlTypeEnum.TRAFFIC_NCIDENT.getCode().equals(sdEvent.getPrevControlType())){
            //更新预案设备
            setStrategyRlEquipment(sdEvent);
            //如有处置中的普通事件则将处理中的安全预警状态改为已处理
            SdEvent aqSdevent = new SdEvent();
            aqSdevent.setEventState(EventStateEnum.processing.getCode());
            aqSdevent.setSearchValue(PrevControlTypeEnum.ACTIVE_SAFETY.getCode());
            aqSdevent.setTunnelId(sdEvent.getTunnelId());
            List<SdEvent> sdEvents = sdEventMapper.selectSdEventList(aqSdevent);
            for(SdEvent item : sdEvents){
                item.setEventState(EventStateEnum.processed.getCode());
                item.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,DateUtils.getNowDate()));
                sdEventMapper.updateSdEvent(item);
                instreEventFlowData(item.getId(),"事件因处理普通事件执行中断！");
            }
            sdEvent.setUpdateTime(DateUtils.getNowDate());
        }else {
            sdEvent.setUpdateTime(DateUtils.getNowDate());
        }
        sdEvent.setUpdateBy(SecurityUtils.getUsername());
        return sdEventMapper.updateSdEvent(sdEvent);
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
        data.put("${eventImgNum}","(最多3张)");

        //事件发现
        List<String[]> list1 = setDiscoveryMap(eventDiscovery);
        //图片，如果是多个图片，就新建多个map
        List<SdTrafficImage> imgList = eventDiscovery.getIconUrlList();
        int listLength = imgList.size() >= 3 ? 3 : imgList.size();
        for(int i = 0; i < listLength; i++){
            Map<String, Object> map = setImgMap(imgList.get(i).getImgUrl());
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
            for(Map<String, Object> plan : planDisposal){
                List<SdEventHandle> planList = (List<SdEventHandle>)plan.get("planList");
                list3.add(new String[]{plan.get("planName").toString(),""});
                for(SdEventHandle item : planList){
                    for(SdEventHandle temp : item.getChildren()){
                        list3.add(new String[]{item.getFlowContent(),temp.getFlowContent()});
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
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult getAllManagementDevices(SdEventHandle sdEventHandle) {
        //查询预案流程节点
        List<String> list1 = Arrays.asList(sdEventHandle.getReserveId().split(","));
        SdReserveProcessMapper processMapper = SpringUtils.getBean(SdReserveProcessMapper.class);
        //查询预案id
        SdReserveProcess sdReserveProcess1 = processMapper.selectSdReserveProcessById(Long.valueOf(list1.get(0)));
        SdReserveProcess sdReserveProcess = new SdReserveProcess();
        sdReserveProcess.setReserveId(sdReserveProcess1.getReserveId());
        List<SdReserveProcess> sdReserveProcesses = processMapper.selectSdReserveProcessList(sdReserveProcess);
        //分别查询设备详情
        List<Map<String, Object>> list = new ArrayList<>();
        for(SdReserveProcess item : sdReserveProcesses){
            Map<String, Object> mapData = deviceDateiled(item.getId(),Long.valueOf(sdEventHandle.getEventId()));
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
        Long processId = sdEventHandles.stream().filter(item -> item.getProcessId() != null).limit(1).collect(Collectors.toList()).get(0).getProcessId();
        Long reserveId = sdReserveProcessMapper.selectSdReserveProcessById(processId).getReserveId();
        String planName = sdReservePlanMapper.selectSdReservePlanById(reserveId).getPlanName();
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
        if(PrevControlTypeEnum.ACTIVE_SAFETY.getCode().equals(sdEvent.getPrevControlType())){
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
                    sdStrategyRls.add(sdStrategyRlMapper.selectSdStrategyRlById(temp.getStrategyId()));
                    maps.addAll(setDeviceAllList(sdStrategyRls));
                }
                map.put("devicesList", maps);
                map.put("tableName", item.getProcessStageName());
                map.put("prevControlType",sdEvent.getPrevControlType());
                list.add(map);
            }
        }
        return AjaxResult.success(list);
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
            titleDesc.append(directionDict);
        }
        if(!StringUtils.isEmpty(sdEvent.getStakeNum())){
            titleDesc.append("桩号").append(sdEvent.getStakeNum());
        }
        //事件类型名称
        String eventTypeName = eventTypeMap.get(sdEvent.getEventTypeId());
        titleDesc.append("发生").append(eventTypeName).append("事件");

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
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map> eventPopAll(String subIndex) {
        return sdEventMapper.eventPopAll(subIndex);
    }

    @Override
    public AjaxResult getHandle(SdEvent sdEvent) {
        sdEvent.setCurrencyId(sdEventMapper.selectSdEventById(sdEvent.getId()).getCurrencyId());
        //交通事件-添加流程树
        updateHandle(sdEvent,"add");
        SdEventHandle sdEventHandle = new SdEventHandle();
        sdEventHandle.setEventId(sdEvent.getId());
        List<SdEventHandle> sdEventHandles = sdEventHandleMapper.selectSdEventHandleList(sdEventHandle);
        return AjaxResult.success(sdEventHandles);
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
                map.put("inlet",minEqId.getEqId());
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
        String prevControlType = sdEventTypeMapper.selectSdEventTypeById(sdEventData.getEventTypeId()).getPrevControlType();
        //查询事件详情-事件发现
        SdEvent eventDiscovery = sdEventMapper.getEventDiscovery(sdEvent);
        //查询事件详情-事件发现-图片
        List<SdTrafficImage> image1 = sdTrafficImageMapper.selectImageByBusinessId(sdEvent.getId().toString());
        List<SdTrafficImage> images = image1.stream().filter(item -> "0".equals(item.getImgType())).collect(Collectors.toList());
        eventDiscovery.setIconUrlList(images.subList(0,images.size() > 10 ? 10 : images.size()));
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
                //查询事件详情-人工复核-当事目标
                String confidence = radarEventMapper.selectConfidence(sdEvent.getId());
                manualReview.setConfidenceList(confidence == null ? "" : confidence);
                map.put("manualReview",manualReview);
            }
        }
        if("0".equals(sdEventData.getEventState()) || "1".equals(sdEventData.getEventState())){
            //查询事件详情-预案处置
            if(prevControlType.equals(PrevControlTypeEnum.TRAFFIC_NCIDENT.getCode())){
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
                String endDatePoor = DateUtils.getDatePoor(DateUtils.parseDate(endReport.getEndTime()), DateUtils.parseDate(endReport.getStartTime()));
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
        BigDecimal laneNo = new BigDecimal(sdEvent.getLaneNo() == null ? "0" : sdEvent.getLaneNo());
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
        List<SdReserveProcess> processList = SpringUtils.getBean(SdReserveProcessMapper.class).getProcessList(Long.valueOf(sdEvent.getCurrencyId()));
        //插入预警信息
        int sort = setWaring(sdEvent1, sdEventType, model);
        setEventHandleData(sort,sort,sdEvent);

    }

    public void setEventHandleData(int sort, int id, SdEvent sdEvent){
        List<SdReserveProcess> processList = SpringUtils.getBean(SdReserveProcessMapper.class).getProcessList(Long.valueOf(sdEvent.getCurrencyId()));
        for(SdReserveProcess item : processList){
            SdEventHandle sdEventHandle = new SdEventHandle();
            sdEventHandle.setEventId(sdEvent.getId());
            sdEventHandle.setFlowId(Long.valueOf(id));
            sdEventHandle.setFlowContent(item.getProcessStageName());
            sdEventHandle.setFlowSort(sort+"");
            List<String> reserveIds = new ArrayList<>();
            for(SdReserveProcess temp : item.getProcessesList()){
                sort++;
                String flowId = id+"0";
                SdEventHandle sdEventHandle1 = new SdEventHandle();
                sdEventHandle1.setEventId(sdEvent.getId());
                sdEventHandle1.setFlowId(Long.valueOf(flowId + 1));
                sdEventHandle1.setFlowPid(Long.valueOf(id));
                sdEventHandle1.setFlowContent(temp.getProcessName());
                sdEventHandle1.setProcessId(temp.getId());
                sdEventHandle1.setFlowSort(sort+"");
                reserveIds.add(temp.getId().toString());
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
                .concat(EventGradeEnum.getValue(sdEvent.getEventGrade())).concat("】，复核状态为突发事件处置"));
        sdEventHandleMapper.insertSdEventHandle(sdEventHandle);
        //如第一次添加处置流程时新增事件流程记录
        if("add".equals(model)){
            SdEventFlowMapper flowMapper = SpringUtils.getBean(SdEventFlowMapper.class);
            SdEventFlow flow = new SdEventFlow();
            flow.setEventId(sdEvent.getId().toString());
            flow.setFlowTime(DateUtils.getNowDate());
            flow.setFlowHandler(SecurityUtils.getUsername());
            flow.setFlowDescription("预警记录".concat(name));
            flowMapper.insertSdEventFlow(flow);
            SdEventFlow eventFlow = new SdEventFlow();
            eventFlow.setEventId(sdEvent.getId().toString());
            //做区分+1秒
            Date nowDate = DateUtils.getNowDate();
            nowDate.setTime(DateUtils.getNowDate().getTime() + 1000);
            eventFlow.setFlowTime(nowDate);
            eventFlow.setFlowHandler(SecurityUtils.getUsername());
            eventFlow.setFlowDescription(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,DateUtils.getNowDate()).concat(" ")
                    .concat("复核事件为【").concat(sdEventType.getEventType()).concat("】、【")
                    .concat(EventGradeEnum.getValue(sdEvent.getEventGrade())).concat("】，复核状态为突发事件处置"));
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
            SdStrategyRl sdStrategyRl = rlMapper.selectSdStrategyRlById(item.getStrategyId());
            if(!"1".equals(sdStrategyRl.getRetrievalRule())){
                sdStrategyRl.setEquipments(getRlDevice(sdEvent.getId(),sdStrategyRl));
                rlMapper.updateSdStrategyRl(sdStrategyRl);
            }
        }
    }

    /**
     * 根据设备检索规则查询设备
     * @param eventId
     * @param sdStrategyRl
     * @return
     */
    public String getRlDevice(Long eventId, SdStrategyRl sdStrategyRl){
        SdEventMapper sdEventMapper = SpringUtils.getBean(SdEventMapper.class);
        SdDevicesMapper sdDevicesMapper = SpringUtils.getBean(SdDevicesMapper.class);
        //查询事件信息
        SdEvent sdEvent = sdEventMapper.selectSdEventById(eventId);
        //整形桩号
        Integer stakeNum = Integer.valueOf(sdEvent.getStakeNum().replaceAll("K", "").replaceAll(Pattern.quote("+"), "").replaceAll(" ", ""));
        List<String> rlDeviceList = new ArrayList<>();
        //检索规则条件
        String retrievalRule = sdStrategyRl.getRetrievalRule();
        //最近3公里
        if("2".equals(retrievalRule)){
            //前3公里
            Integer frontStakeNum = stakeNum - 3000;
            //后3公里
            Integer afterStakeNum = stakeNum + 3000;
            //查询区间设备
            rlDeviceList = sdDevicesMapper.getRlDevice(Integer.valueOf(sdStrategyRl.getEqTypeId()), sdEvent.getDirection(), frontStakeNum, afterStakeNum,sdEvent.getTunnelId());
        }
        //附近5个
        if("3".equals(retrievalRule)){
            List<String> frontLatelyFive = sdDevicesMapper.getFrontLatelyFive(Integer.valueOf(sdStrategyRl.getEqTypeId()), sdEvent.getDirection(), stakeNum,sdEvent.getTunnelId());
            List<String> afterLatelyFive = sdDevicesMapper.getAfterLatelyFive(Integer.valueOf(sdStrategyRl.getEqTypeId()), sdEvent.getDirection(), stakeNum,sdEvent.getTunnelId());
            rlDeviceList = setLatelyFive(frontLatelyFive,afterLatelyFive);
        }
        //事发上游所有
        if("4".equals(retrievalRule)){
            rlDeviceList = sdDevicesMapper.getRlDevice(Integer.valueOf(sdStrategyRl.getEqTypeId()), sdEvent.getDirection(), 0, stakeNum,sdEvent.getTunnelId());
        }
        //事发下游所有
        if("5".equals(retrievalRule)){
            rlDeviceList = sdDevicesMapper.getRlDevice(Integer.valueOf(sdStrategyRl.getEqTypeId()), sdEvent.getDirection(), stakeNum, 0,sdEvent.getTunnelId());
        }
        return org.apache.commons.lang3.StringUtils.join(rlDeviceList,",");
    }

    //最近5条
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
        //获取设备类型
        Long eqType = Long.valueOf(deviceList.get(0).get("eqType").toString());
        //根据状态类型查询不通类型得设备明细
        if(eqType == DevicesTypeEnum.VMS.getCode() || eqType == DevicesTypeEnum.MEN_JIA_VMS.getCode()){
            //查询情报板
            String vmsData = sdEventMapper.getManagementVmsLs(sdReserveProcess);
            Map<String, Object> sdVmsContent = SpringUtils.getBean(IotBoardTemplateMapper.class).getSdVmsTemplateContent(Long.valueOf(vmsData));
            map.put("vmsData",sdVmsContent);
        }else if(eqType == DevicesTypeEnum.LS.getCode()){
            //截取广播文件名称
            String lsData = sdEventMapper.getManagementVmsLs(sdReserveProcess);
            List<String> list = Arrays.asList(lsData.split("\\\\"));
            String lsContent = list.get(list.size() - 1);
            map.put("lsData",lsContent);
        }else {
            //查询普通设备状态
            List<Map<String, Object>> maps = sdEventMapper.getManagementDeviceState(sdReserveProcess);
            List<String> deviceIconUrl = new ArrayList<>();
            maps.stream().forEach(item -> {
                deviceIconUrl.add(item.get("url").toString());
            });
            map.put("deviceState",maps.get(0).get("stateName"));
            map.put("deviceIconUrl",deviceIconUrl);
        }
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
        List<SdEventHandle> planDisposal = (List<SdEventHandle>) getHandle(sdEventData).get("data");
        Map<String, Object> planMap = new HashMap<>();
        if(sdEventData.getCurrencyId() != null && sdEventData.getCurrencyId() != ""){
            planMap.put("planName",sdReservePlanMapper.selectSdReservePlanById(Long.valueOf(sdEventData.getCurrencyId())).getPlanName());
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
                maps = sdDevicesMapper.selectVmsDevices(item.getEquipments(), item.getState());
            }else if(eqTypeId == DevicesTypeEnum.LS.getCode()){
                maps = sdDevicesMapper.selectLsDevices(item.getEquipments());
                maps.stream().forEach(temp -> {
                    List<String> list = Arrays.asList(item.getState().split("\\\\"));
                    String lsContent = list.get(list.size() - 1);
                    temp.put("state",lsContent);
                });
            }else {
                maps = sdDevicesMapper.selectDevices(item.getEquipments(), item.getState());
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
    }
}
