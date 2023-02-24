package com.tunnel.business.service.event.impl;

import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.word.WordExportUtil;
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
import com.tunnel.business.mapper.logRecord.SdOperationLogMapper;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficImageMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.digitalmodel.impl.RadarEventServiceImpl;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.utils.util.UUIDUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
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
    private SdReservePlanMapper sdReservePlanMapper;

    @Autowired
    private SdTunnelsMapper sdTunnelsMapper;

    @Autowired
    private SdStrategyMapper sdStrategyMapper;

    @Autowired
    private RadarEventServiceImpl radarEventServiceImpl;

    /**
     * 查询事件管理
     *
     * @param id 事件管理ID
     * @return 事件管理
     */
    @Override
    public SdEvent selectSdEventById(Long id) {
        return sdEventMapper.selectSdEventById(id);
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
            String eventTitle = item.getEventTitle();
            int startLength = eventTitle.indexOf(item.getTunnelName()) + item.getTunnelName().length();
            int endLength = eventTitle.indexOf(item.getStakeNum()) + item.getStakeNum().length();
            if(eventTitle.length() > endLength){
                item.setPosition(item.getTunnelName() + eventTitle.substring(startLength,endLength).replaceAll("桩号",""));
            }
            if(item.getVideoUrl()!=null){
                item.setVideoUrl(item.getVideoUrl().split(";")[0]);
            }
            item.setIconUrlList(sdTrafficImageMapper.selectImageByBusinessId(item.getId().toString()));
            item.setConfidenceList(radarEventMapper.selectConfidence(item.getId()));
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
            SdEventFlow eventFlow = new SdEventFlow();
            eventFlow.setEventId(sdEvent.getId().toString());
            eventFlow.setFlowTime(DateUtils.getNowDate());
            eventFlow.setFlowDescription("事件已完结");
            eventFlow.setFlowHandler(SecurityUtils.getUsername());
            sdEventFlowMapper.insertSdEventFlow(eventFlow);
            sdEvent.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,DateUtils.getNowDate()));
        }else {
            sdEvent.setUpdateTime(DateUtils.getNowDate());
        }
        if("0".equals(sdEvent.getEventState())){
            //更新预案设备
            setStrategyRlEquipment(sdEvent);
        }
        /*if ("2".equals(sdEvent.getEventState())) {
            SdEventFlow eventFlow = new SdEventFlow();
            eventFlow.setEventId(sdEvent.getFlowId());
            eventFlow.setFlowTime(sdEvent.getEventTime());
            eventFlow.setFlowDescription("问题忽略");
            eventFlow.setFlowHandler(SecurityUtils.getUsername());
            sdEventFlowMapper.insertSdEventFlow(eventFlow);
            //主动安全-状态更新为已忽略时推送值高速云
            radarEventServiceImpl.sendDataToOtherSystem(null,sdEventMapper.selectSdEventById(sdEvent.getId()));
        }*/
        //更新事件置信度
        /*if(sdEvent.getConfidenceList() != null){
            List<WjConfidence> confidenceList = sdEvent.getConfidenceList();
            for(WjConfidence item : confidenceList){
                radarEventMapper.updateEventConfidence(item);
            }
        }*/
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
        Map<String, Object> data = (Map<String, Object>) getEventDetail(sdEvent).get("data");
        String eventState = data.get("eventState").toString();
        //word模板list
        List<Map<String, Object>> wordList = new ArrayList<>();

        //事件发现
        SdEvent eventDiscovery = (SdEvent) data.get("eventDiscovery");
        //人工复核
        SdEvent manualReview = (SdEvent)data.get("manualReview");
        //事件处置-预案流程
        Map<String, Object> planDisposal = (Map<String, Object>)data.get("planDisposal");
        //事件处置-处置记录
        List<SdEventFlow> disposalRecord = (List<SdEventFlow>) data.get("disposalRecord");
        //事件处置-完结报告
        SdEvent endReport = (SdEvent) data.get("endReport");

        try {
            XWPFDocument doc = null;
            //3:待确认
            if("3".equals(eventState)){
                wordList.add(setDiscoveryMap(eventDiscovery));
                doc = WordExportUtil.exportWord07(
                        "exporttemplate/事件详情-待确认.docx", wordList);
            }
            //2:已挂起 4:已确认 5:误报
            if("2".equals(eventState) || "4".equals(eventState) || "5".equals(eventState)){
                Map<String, Object> disMap = setDiscoveryMap(eventDiscovery);
                Map<String, Object> revMap = setReviewMap(manualReview);
                disMap.putAll(revMap);
                wordList.add(disMap);
                doc = WordExportUtil.exportWord07(
                        "exporttemplate/事件详情-确认挂起误报.docx", wordList);
            }
            //0:处理中
            if("0".equals(eventState)){
                Map<String, Object> disMap = setDiscoveryMap(eventDiscovery);
                Map<String, Object> revMap = setReviewMap(manualReview);
                Map<String, Object> planMap = setPlanMap(planDisposal, disposalRecord);
                disMap.putAll(revMap);
                disMap.putAll(planMap);
                wordList.add(disMap);
                doc = WordExportUtil.exportWord07(
                        "exporttemplate/事件详情-处理中.docx", wordList);

            }
            //1:已处理
            if("1".equals(eventState)){
                Map<String, Object> disMap = setDiscoveryMap(eventDiscovery);
                Map<String, Object> revMap = setReviewMap(manualReview);
                Map<String, Object> planMap = setPlanMap(planDisposal, disposalRecord);
                Map<String, Object> repMap = setReportMap(endReport);
                disMap.putAll(revMap);
                disMap.putAll(planMap);
                disMap.putAll(repMap);
                wordList.add(disMap);
                doc = WordExportUtil.exportWord07(
                        "exporttemplate/事件详情-已处理.docx", wordList);
            }
            /*XWPFDocument doc = WordExportUtil.exportWord07(
                    "D:/谷歌下载/事件详情-待确认.docx", setDiscoveryMap(eventDiscovery));*/
            /*XWPFDocument doc = WordExportUtil.exportWord07(
                    "exporttemplate/事件详情-已处理.docx", setDiscoveryMap(eventDiscovery));*/
            //response.setHeader("Content-disposition","attachment;filename=事件详情.docx");
            /*response.setContentType("application/octet-stream");
            response.setHeader("Access-Control-Expose-Headers","Content-Disposition");
            response.setHeader("Content-Disposition","attachment;fileName=" + URLEncoder.encode("事件详情.docx","UTF-8"));*/
            /*response.setStatus(200);*/
            //response.reset();
            //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            //response.setContentType("application/octet-stream;charset=UTF-8");
            /*response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("事件详情"+".docx", "UTF-8"));
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");
            response.setContentType("application/x-download");
            ServletOutputStream outputStream = response.getOutputStream();
            doc.write(outputStream);
            outputStream.close();
            doc.close();*/

            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(sdEvent.getId()+".docx", "UTF-8"));
            response.setContentType("application/octet-stream");
            ServletOutputStream outputStream = response.getOutputStream();
            doc.write(outputStream);
            outputStream.close();
            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public AjaxResult getSituationUpgrade(SdEvent sdEvent) {
        return AjaxResult.success(sdEventMapper.getSituationUpgrade(sdEvent));
    }

    @Override
    public AjaxResult getManagementDevice(SdReserveProcess sdReserveProcess) {
        Map<String, Object> map = new HashMap<>();
        //查询设备列表
        map.put("deviceList",sdEventMapper.getManagementDevice(sdReserveProcess));
        //查询状态
        map.put("deviceState",sdEventMapper.getManagementDeviceState(sdReserveProcess));
        return AjaxResult.success(map);
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
        if(event.getVideoUrl()!=null){
            event.setVideoUrl(event.getVideoUrl().split(";")[0]);
        }
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
//            boolean isDown = TunnelDirectionEnum.getTunnelDirection(direction).equals("下行");
            for(SdTunnelSubarea data:subareaData){
//                Integer upLimit = isDown?Integer.parseInt(data.getPileMin()):Integer.parseInt(data.getPileMax());
//                Integer downLimit = isDown?Integer.parseInt(data.getPileMax()):Integer.parseInt(data.getPileMin());
                Integer min = Integer.parseInt(data.getPileMin());
                Integer max = Integer.parseInt(data.getPileMax());

                if(min > max){
                    Integer temp = max;
                    max = min;
                    min = temp;
                }
//                Integer upLimit = Integer.parseInt(data.getPileMax());
//                Integer downLimit = Integer.parseInt(data.getPileMin());
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


            //如果没有取到 取最近的分区ID
            //所有分区桩号
//            String s = subareaData.stream().map(p->p.getPileMin()+","+p.getPileMax()).collect(Collectors.joining(","));
//            String[] pileStr = s.split(",");
//            int[] allPile = Arrays.stream(pileStr).mapToInt(Integer::parseInt).sorted().toArray();
//            //下行取反
//            if(isDown){
//                ArrayUtils.reverse(allPile);
//            }
//            int index = Math.abs(compareValue-allPile[0]);
//            int result = allPile[0];
//            int mark = 0;
//            for (int i=0;i<allPile.length;i++) {
//                int abs = Math.abs(compareValue-allPile[i]);
//                if(abs <= index){
//                    index = abs;
//                    result = allPile[i];
//                    mark = i+1;
//                }
//            }
//            String pile = String.valueOf(result);
//            List<SdTunnelSubarea> only = new ArrayList<>();
//            if(mark %2 !=0){
//                //最接近的值为桩号下限
//                only = subareaData.stream().filter(area->area.getPileMin().equals(pile)).collect(Collectors.toList());
//            }else{
//                only = subareaData.stream().filter(area->area.getPileMax().equals(pile)).collect(Collectors.toList());
//            }
//            subareaId = only.get(0).getsId();
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
//        devices.setEqType(DevicesTypeEnum.CAMERA_DOME.getCode());
//        list.addAll(sdDevicesService.selectSdDevicesList(devices));
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
    public AjaxResult getSafetyHandle(SdEvent sdEvent) {
        updateSecurityHandle(sdEvent);
        //查询处置记录流程树
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
        //eventHandle.setFlowPid(Long.valueOf(7));
        //sdEventHandleMapper.deleteRelation(eventHandle);
        List<SdEventHandle> sdEventHandle1 = sdEventHandleMapper.selectSdEventHandleList(eventHandle);
        if(sdEventHandle1.size() > 0){
            if(sdEventHandle1.get(0).getReserveId() != null){
                return AjaxResult.error("已存在预案联控流程");
            }
        }
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
            sdEventHandle.setReserveId(relation.get(0).getId());
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
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqDirection(sdEvent.getDirection());
        sdDevices.setEqTunnelId(sdEvent.getTunnelId());
        sdDevices.setEqType(DevicesTypeEnum.CAMERA_BOX.getCode());
        List<SdDevices> sdDevicesList = sdDevicesMapper.selectSdDevicesList(sdDevices);
        String minEqId = sdDevicesList.stream().min(Comparator.comparing(SdDevices::getPileNum)).get().getEqId();
        String maxEqId = sdDevicesList.stream().max(Comparator.comparing(SdDevices::getPileNum)).get().getEqId();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        if("1".equals(sdEvent.getDirection())){
            map.put("outlet",maxEqId);
            map.put("inlet",minEqId);
        }else {
            map.put("outlet",minEqId);
            map.put("inlet",maxEqId);
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
        eventDiscovery.setIconUrlList(sdTrafficImageMapper.selectImageByBusinessId(sdEvent.getId().toString()));
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
            //查询事件详情-人工复核-当事目标
            String confidence = radarEventMapper.selectConfidence(sdEvent.getId());
            manualReview.setConfidenceList(confidence == null ? "" : confidence);
            map.put("manualReview",manualReview);
        }
        if("0".equals(sdEventData.getEventState()) || "1".equals(sdEventData.getEventState())){
            //查询事件详情-预案处置
            List<SdEventHandle> planDisposal = (List<SdEventHandle>) getHandle(sdEventData).get("data");
            Map<String, Object> planMap = new HashMap<>();
            if(sdEventData.getCurrencyId() != null && sdEventData.getCurrencyId() != ""){
                planMap.put("planName",sdReservePlanMapper.selectSdReservePlanById(Long.valueOf(sdEventData.getCurrencyId())).getPlanName());
            }else {
                planMap.put("planName",null);
            }
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
            planMap.put("planList",collectPid);
            //查询事件详情-处置记录
            SdEventFlow sdEventFlow = new SdEventFlow();
            sdEventFlow.setEventId(sdEvent.getId().toString());
            List<SdEventFlow> disposalRecord = sdEventFlowMapper.selectSdEventFlowList(sdEventFlow);
            map.put("planDisposal",planMap);
            map.put("disposalRecord",disposalRecord);
        }
        if("1".equals(sdEventData.getEventState())){
            //查询事件详情-完结报告
            SdEvent endReport = sdEventMapper.getEndReport(sdEvent);
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

    /**
     * 交通事件-添加流程树
     * @param sdEvent
     */
    public void updateHandle(SdEvent sdEvent, String model){
        int count = sdEventHandleMapper.selectSdEventHandle(sdEvent.getId());
        if(count == 0){
            SdEvent sdEvent1 = sdEventMapper.selectSdEventById(sdEvent.getId());
            //查询预案流程树
            List<SdJoinTypeFlow> sdJoinTypeFlows = sdJoinTypeFlowMapper.selectSdJoinTypeFlowById(sdEvent.getEventTypeId());
            List<SdJoinTypeFlow> flowsPidData = sdJoinTypeFlows.stream().filter(item -> item.getFlowPid() == null).collect(Collectors.toList());
            List<SdJoinTypeFlow> flowsIdData = sdJoinTypeFlows.stream().filter(item -> item.getFlowPid() != null).collect(Collectors.toList());
            int sort = 0;
            for(SdJoinTypeFlow item : flowsPidData){
                sort = sort + 1;
                if(item.getFlowId() == 7){
                    //插入预案流程
                    sort = setEventHandleData(sort,item,sdEvent);
                    continue;
                }
                SdEventHandle sdEventHandle = new SdEventHandle();
                sdEventHandle.setEventId(sdEvent.getId());
                sdEventHandle.setFlowId(item.getFlowId());
                sdEventHandle.setFlowPid(item.getFlowPid());
                sdEventHandle.setFlowContent(item.getFlowName());
                sdEventHandle.setFlowSort(sort+"");
                sdEventHandleMapper.insertSdEventHandle(sdEventHandle);
                int number = 0;
                for(SdJoinTypeFlow temp : flowsIdData){
                    if(item.getFlowId() == temp.getFlowPid()){
                        number = number + 1;
                        SdEventHandle sdEventHandle1 = new SdEventHandle();
                        sdEventHandle1.setEventId(sdEvent.getId());
                        sdEventHandle1.setFlowId(temp.getFlowId());
                        sdEventHandle1.setFlowPid(temp.getFlowPid());
                        if("2".equals(temp.getFlowId().toString())){
                            String name = EventDescEnum.getName(sdEvent1.getEventSource());
                            sdEventHandle1.setFlowContent(temp.getFlowName().concat(name));
                            sdEventHandle1.setEventState("1");
                            //如第一次添加处置流程时新增事件流程记录
                            if("add".equals(model)){
                                SdEventFlow flow = new SdEventFlow();
                                flow.setEventId(sdEvent.getId().toString());
                                flow.setFlowTime(DateUtils.getNowDate());
                                flow.setFlowHandler(SecurityUtils.getUsername());
                                flow.setFlowDescription(item.getFlowName().concat(name));
                                SpringUtils.getBean(SdEventFlowMapper.class).insertSdEventFlow(flow);
                            }
                        }else {
                            sdEventHandle1.setFlowContent(temp.getFlowName());
                        }
                        sdEventHandle1.setFlowSort(number+"");
                        sdEventHandleMapper.insertSdEventHandle(sdEventHandle1);
                    }
                }
            }
        }
    }

    public void updateSecurityHandle(SdEvent sdEvent){
        int count = sdEventHandleMapper.selectSdEventHandle(sdEvent.getId());
        //judgeData(sdEvent);
        if(count == 0){
            SdEvent sdEvent1 = sdEventMapper.selectSdEventById(sdEvent.getId());
            //查询预案流程树
            List<SdJoinTypeFlow> sdJoinTypeFlows = sdJoinTypeFlowMapper.selectSdJoinTypeFlowById(sdEvent.getEventTypeId());
            List<SdJoinTypeFlow> flowsPidData = sdJoinTypeFlows.stream().filter(item -> item.getFlowPid() == null).collect(Collectors.toList());
            List<SdJoinTypeFlow> flowsIdData = sdJoinTypeFlows.stream().filter(item -> item.getFlowPid() != null).collect(Collectors.toList());
            int sort = 0;
            for(SdJoinTypeFlow item : flowsPidData){
                sort = sort + 1;
                SdEventHandle sdEventHandle = new SdEventHandle();
                sdEventHandle.setEventId(sdEvent.getId());
                sdEventHandle.setFlowId(item.getFlowId());
                sdEventHandle.setFlowPid(item.getFlowPid());
                sdEventHandle.setFlowContent(item.getFlowName());
                sdEventHandle.setFlowSort(sort+"");
                sdEventHandleMapper.insertSdEventHandle(sdEventHandle);
                int number = 0;
                for(SdJoinTypeFlow temp : flowsIdData){
                    if(item.getFlowId() == temp.getFlowPid()){
                        number = number + 1;
                        SdEventHandle sdEventHandle1 = new SdEventHandle();
                        sdEventHandle1.setEventId(sdEvent.getId());
                        sdEventHandle1.setFlowId(temp.getFlowId());
                        sdEventHandle1.setFlowPid(temp.getFlowPid());
                        if("2".equals(temp.getFlowId().toString())){
                            String name = EventDescEnum.getName(sdEvent1.getEventSource());
                            sdEventHandle1.setFlowContent(temp.getFlowName().concat(name));
                            sdEventHandle1.setEventState("1");
                            SdEventFlow flow = new SdEventFlow();
                            flow.setEventId(sdEvent.getId().toString());
                            flow.setFlowTime(DateUtils.getNowDate());
                            flow.setFlowHandler(SecurityUtils.getUsername());
                            flow.setFlowDescription(item.getFlowName().concat(name));
                            SpringUtils.getBean(SdEventFlowMapper.class).insertSdEventFlow(flow);
                        }else {
                            sdEventHandle1.setFlowContent(temp.getFlowName());
                        }
                        sdEventHandle1.setFlowSort(number+"");
                        sdEventHandleMapper.insertSdEventHandle(sdEventHandle1);
                    }
                }
            }
            //新增主动安全设备管控子节点
            SdStrategy strategy = new SdStrategy();
            strategy.setTunnelId(sdEvent.getTunnelId());
            strategy.setEventType(sdEvent.getEventTypeId().toString());
            strategy.setDirection(sdEvent.getDirection());
            List<SdStrategy> safetyHandle = sdStrategyMapper.getSafetyHandle(strategy);
            String concat = sdEvent.getId().toString().concat("7000");
            Long relationId = Long.valueOf(concat);
            int sort1 = 0;
            for(SdStrategy item : safetyHandle){
                sort1 = sort1 + 1;
                relationId = relationId + 1;
                SdEventHandle sdEventHandle = new SdEventHandle();
                sdEventHandle.setEventId(Long.valueOf(sdEvent.getId()));
                sdEventHandle.setFlowId(Long.valueOf(relationId));
                sdEventHandle.setFlowPid(Long.valueOf(7));
                sdEventHandle.setFlowContent(item.getDisposalName());
                sdEventHandle.setProcessId(item.getStrategyRlId());
                sdEventHandle.setFlowSort(sort1+"");
                sdEventHandle.setUpdateTime(DateUtils.getNowDate());
                sdEventHandleMapper.insertSdEventHandle(sdEventHandle);
            }
            if(safetyHandle.size() > 0){
                SdEventHandle sdEventHandle = new SdEventHandle();
                sdEventHandle.setEventId(Long.valueOf(sdEvent.getId()));
                sdEventHandle.setFlowId(Long.valueOf(7));
                sdEventHandle.setReserveId(safetyHandle.get(0).getId());
                sdEventHandle.setUpdateTime(DateUtils.getNowDate());
                sdEventHandleMapper.updateSdEventHandleRelation(sdEventHandle);
            }
        }
    }

    /*public int judgeData(SdEvent sdEvent){
        SdEventHandle sdEventHandle2 = new SdEventHandle();
        sdEventHandle2.setEventId(sdEvent.getId());
        //查询现在预案流程是否已存在
        List<SdEventHandle> sdEventHandles = sdEventHandleMapper.selectSdEventHandleList(sdEventHandle2);
        List<SdEventHandle> collectPid = sdEventHandles.stream().filter(item -> item.getFlowPid() == null).collect(Collectors.toList());
        List<Long> collect = collectPid.stream().map(SdEventHandle::getFlowId).collect(Collectors.toList());
        //查询最新的预案流程
        List<SdJoinTypeFlow> sdJoinTypeFlows = sdJoinTypeFlowMapper.selectSdJoinTypeFlowById(sdEvent.getEventTypeId());
        List<SdJoinTypeFlow> flowsPidData = sdJoinTypeFlows.stream().filter(item -> item.getFlowPid() == null).collect(Collectors.toList());
        List<Long> collect1 = flowsPidData.stream().map(SdJoinTypeFlow::getFlowId).collect(Collectors.toList());
        if(collect1.containsAll(collect)){
            return 0;
        }
        return 1;
    }*/

    /**
     * 插入预案流程
     *
     * @param sort
     * @param sdJoinTypeFlow
     * @param sdEvent
     * @return
     */
    public int setEventHandleData(int sort, SdJoinTypeFlow sdJoinTypeFlow, SdEvent sdEvent){
        List<SdReserveProcess> processList = SpringUtils.getBean(SdReserveProcessMapper.class).getProcessList(Long.valueOf(sdEvent.getCurrencyId()));
        String pid = sdJoinTypeFlow.getFlowId().toString();
        for(SdReserveProcess item : processList){
            SdEventHandle sdEventHandle = new SdEventHandle();
            sdEventHandle.setEventId(sdEvent.getId());
            sdEventHandle.setFlowId(Long.valueOf(pid));
            sdEventHandle.setFlowPid(sdJoinTypeFlow.getFlowPid());
            sdEventHandle.setFlowContent(item.getProcessStageName());
            sdEventHandle.setReserveId(item.getReserveId());
            sdEventHandle.setFlowSort(sort+"");
            sdEventHandleMapper.insertSdEventHandle(sdEventHandle);
            sort = sort + 1;
            int number = 0;
            for(SdReserveProcess temp : item.getProcessesList()){
                String flowId = pid.concat("0");
                number = number + 1;
                SdEventHandle sdEventHandle1 = new SdEventHandle();
                sdEventHandle1.setEventId(sdEvent.getId());
                sdEventHandle1.setFlowId(Long.valueOf(flowId + 1));
                sdEventHandle1.setFlowPid(Long.valueOf(pid));
                sdEventHandle1.setFlowContent(temp.getProcessName());
                sdEventHandle1.setProcessId(temp.getId());
                sdEventHandle1.setFlowSort(number+"");
                sdEventHandleMapper.insertSdEventHandle(sdEventHandle1);
            }
            pid = pid + "7";
        }
        return sort;
    }

    /**
     * 事件详情-事件发现
     * @param eventDiscovery
     * @return
     */
    public Map<String, Object> setDiscoveryMap(SdEvent eventDiscovery){
        Map<String, Object> map = new HashMap<>();
        map.put("eventSource", eventDiscovery.getEventSource());
        map.put("eventTime",DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,eventDiscovery.getEventTime()));
        map.put("continuedTime",eventDiscovery.getContinuedTime());
        map.put("tunnelName",eventDiscovery.getTunnelName());
        map.put("stakeNum",eventDiscovery.getStakeNum());
        map.put("direction",eventDiscovery.getDirection());
        //图片集合
        List<SdTrafficImage> iconUrlList = eventDiscovery.getIconUrlList();
        List<ImageEntity> imageList = new ArrayList<>();
        if(iconUrlList.size() >= 3){
            for(int i = 0; i < 3; i++){
                ImageEntity image = new ImageEntity();
                image.setHeight(200);
                image.setWidth(500);
                image.setUrl(iconUrlList.get(i).getImgUrl());
                image.setType(ImageEntity.URL);
                imageList.add(image);
            }
        }else {
            for(int i = 0; i < iconUrlList.size(); i++){
                ImageEntity image = new ImageEntity();
                image.setHeight(200);
                image.setWidth(500);
                image.setUrl(iconUrlList.get(i).getImgUrl());
                image.setType(ImageEntity.URL);
                imageList.add(image);
            }
        }
        map.put("image", imageList);
        return map;
    }

    /**
     * 事件详情-人工复核
     * @param manualReview
     * @return
     */
    public Map<String, Object> setReviewMap(SdEvent manualReview){
        Map<String, Object> map = new HashMap<>();
        map.put("confidenceList",manualReview.getConfidenceList());
        map.put("eventDescription",manualReview.getEventDescription());
        map.put("eventTypeName",manualReview.getEventTypeName());
        map.put("eventGrade",manualReview.getEventGrade());
        map.put("eventState",manualReview.getEventState());
        map.put("updateBy",manualReview.getUpdateBy());
        map.put("updateTime",DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,manualReview.getUpdateTime()));
        map.put("reviewRemark",manualReview.getReviewRemark());
        return map;
    }

    /**
     * 事件详情-事件处置
     * @param planDisposal
     * @param disposalRecord
     * @return
     */
    public Map<String, Object> setPlanMap(Map<String, Object> planDisposal, List<SdEventFlow> disposalRecord){
        Map<String, Object> map = new HashMap<>();
        List<SdEventHandle> planList = (List<SdEventHandle>) planDisposal.get("planList");
        List<SdEventHandle> list = new ArrayList<>();
        int count = 0;
        for(SdEventHandle item : planList){
            count = 0;
            for(SdEventHandle temp : item.getChildren()){
                SdEventHandle sdEventHandle = new SdEventHandle();
                sdEventHandle.setFlowName(item.getFlowContent());
                if(count == 1){
                    sdEventHandle.setFlowName("");
                }
                sdEventHandle.setFlowContent(temp.getFlowContent());
                list.add(sdEventHandle);
                count = 1;
            }
        }
        map.put("planName",planDisposal.get("planName"));
        map.put("planList",list);

        disposalRecord.stream().forEach(item -> {
            item.setFlowDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,item.getFlowTime()));
        });
        map.put("handleList",disposalRecord);
        return map;
    }

    /**
     * 事件详情-完结报告
     * @param endReport
     * @return
     */
    public Map<String, Object> setReportMap(SdEvent endReport){
        Map<String, Object> map = new HashMap<>();
        map.put("endTime",endReport.getEndTime());
        map.put("endUpdateBy",endReport.getUpdateBy());
        map.put("endContinuedTime",endReport.getContinuedTime());
        map.put("remark",endReport.getRemark());
        return map;
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
}
