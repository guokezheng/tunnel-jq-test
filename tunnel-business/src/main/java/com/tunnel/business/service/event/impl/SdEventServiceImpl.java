package com.tunnel.business.service.event.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DictTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.PrevControlTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.TunnelDirectionEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.digitalmodel.WjConfidence;
import com.tunnel.business.domain.event.*;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.mapper.digitalmodel.RadarEventMapper;
import com.tunnel.business.mapper.event.*;
import com.tunnel.business.mapper.logRecord.SdOperationLogMapper;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficImageMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.utils.util.CommonUtil;
import com.tunnel.business.utils.util.UUIDUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 事件管理Service业务层处理
 *
 * @author gongfanfei
 * @date 2020-08-24
 */
@Service
public class SdEventServiceImpl implements ISdEventService {
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
                item.setPosition(eventTitle.substring(startLength,endLength));
            }
            item.setIconUrlList(sdTrafficImageMapper.selectImageByBusinessId(item.getId().toString()));
            item.setConfidenceList(radarEventMapper.selectConfidence(item.getId()));
        });
        return sdEvents;
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
        eventFlow.setFlowHandler(SecurityUtils.getUsername());
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
            eventFlow.setEventId(sdEvent.getFlowId());
            eventFlow.setFlowTime(sdEvent.getEventTime());
            eventFlow.setFlowDescription("问题解决");
            eventFlow.setFlowHandler(SecurityUtils.getUsername());
            sdEventFlowMapper.insertSdEventFlow(eventFlow);
        }
        if ("2".equals(sdEvent.getEventState())) {
            SdEventFlow eventFlow = new SdEventFlow();
            eventFlow.setEventId(sdEvent.getFlowId());
            eventFlow.setFlowTime(sdEvent.getEventTime());
            eventFlow.setFlowDescription("问题忽略");
            eventFlow.setFlowHandler(SecurityUtils.getUsername());
            sdEventFlowMapper.insertSdEventFlow(eventFlow);
        }
        //更新事件置信度
        if(sdEvent.getConfidenceList() != null){
            List<WjConfidence> confidenceList = sdEvent.getConfidenceList();
            for(WjConfidence item : confidenceList){
                radarEventMapper.updateEventConfidence(item);
            }
        }
        updateHandle(sdEvent);
        sdEvent.setUpdateTime(DateUtils.getNowDate());
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
        return sdEventMapper.selectSdEventById(id);
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
            boolean isDown = TunnelDirectionEnum.getTunnelDirection(direction).equals("下行");
            for(SdTunnelSubarea data:subareaData){
                Integer upLimit = isDown?Integer.parseInt(data.getPileMin()):Integer.parseInt(data.getPileMax());
                Integer downLimit = isDown?Integer.parseInt(data.getPileMax()):Integer.parseInt(data.getPileMin());
//                Integer upLimit = Integer.parseInt(data.getPileMax());
//                Integer downLimit = Integer.parseInt(data.getPileMin());
                if(upLimit >= compareValue && compareValue >= downLimit){
                    subareaId = data.getsId();
                    return subareaId;
                }
            }
            //如果没有取到 取最近的分区ID
            //所有分区桩号
            String s = subareaData.stream().map(p->p.getPileMin()+","+p.getPileMax()).collect(Collectors.joining(","));
            String[] pileStr = s.split(",");
            int[] allPile = Arrays.stream(pileStr).mapToInt(Integer::parseInt).sorted().toArray();
            //下行取反
            if(isDown){
                ArrayUtils.reverse(allPile);
            }
            int index = Math.abs(compareValue-allPile[0]);
            int result = allPile[0];
            int mark = 0;
            for (int i=0;i<allPile.length;i++) {
                int abs = Math.abs(compareValue-allPile[i]);
                if(abs <= index){
                    index = abs;
                    result = allPile[i];
                    mark = i+1;
                }
            }
            String pile = String.valueOf(result);
            List<SdTunnelSubarea> only = new ArrayList<>();
            if(mark %2 !=0){
                //最接近的值为桩号下限
                only = subareaData.stream().filter(area->area.getPileMin().equals(pile)).collect(Collectors.toList());
            }else{
                only = subareaData.stream().filter(area->area.getPileMax().equals(pile)).collect(Collectors.toList());
            }
            subareaId = only.get(0).getsId();
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
        devices.setEqType(23L);
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(devices);
        try {
            int param = Integer.valueOf(stakeNum.replace("K","").replace("+","").replace(" ",""));
            List<Integer> pileNum = list.stream().map(p->(p.getPileNum().intValue())).distinct().collect(Collectors.toList());
            pileNum.add(param);
            pileNum = pileNum.stream().sorted().collect(Collectors.toList());
            for(int i = 0;i < pileNum.size(); i++){
                if(pileNum.get(i)==param){
                    param = pileNum.get(i+1);
                    break;
                }
            }
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

    public int updateHandle(SdEvent sdEvent){
        //查询预案流程树
        List<SdJoinTypeFlow> sdJoinTypeFlows = sdJoinTypeFlowMapper.selectSdJoinTypeFlowById(sdEvent.getEventTypeId());
        List<SdJoinTypeFlow> flowsPidData = sdJoinTypeFlows.stream().filter(item -> item.getFlowPid() == null).collect(Collectors.toList());
        List<SdJoinTypeFlow> flowsIdData = sdJoinTypeFlows.stream().filter(item -> item.getFlowPid() != null).collect(Collectors.toList());
        for(SdJoinTypeFlow item : flowsPidData){
            List<SdJoinTypeFlow> list = new ArrayList<>();
            for(SdJoinTypeFlow temp : flowsIdData){
                if(item.getFlowId() == temp.getFlowPid()){
                    list.add(temp);
                }
            }
            if(list.size() > 0){
                item.setChildren(list);
            }
        }
        return 0;
    }
}
