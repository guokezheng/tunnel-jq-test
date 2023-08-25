package com.tunnel.business.service.event.impl;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.oss.OssUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdEquipmentState;
import com.tunnel.business.domain.dataInfo.SdEquipmentType;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.event.*;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.dataInfo.SdEquipmentStateMapper;
import com.tunnel.business.mapper.dataInfo.SdEquipmentTypeMapper;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.mapper.event.*;
import com.tunnel.business.service.event.ISdEventFlowService;
import com.tunnel.business.service.event.ISdReservePlanService;
import com.tunnel.business.utils.json.JSONObject;
import com.tunnel.business.utils.util.UUIDUtil;
import com.zc.common.core.websocket.WebSocketService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 预案信息Service业务层处理
 *
 * @author xuebi
 * @date 2020-09-10
 */
@Service
public class SdReservePlanServiceImpl implements ISdReservePlanService {

    private static final Logger logger = LoggerFactory.getLogger(SdReservePlanServiceImpl.class);

    @Autowired
    private SdReservePlanMapper sdReservePlanMapper;
    @Autowired
    private SdReservePlanFileMapper sdReservePlanFileMapper;
    @Autowired
    private SdStrategyMapper sdStrategyMapper;
    @Autowired
    private SdStrategyRlMapper sdStrategyRlMapper;
    @Autowired
    private SdEquipmentStateMapper sdEquipmentStateMapper;
    @Autowired
    private SdEquipmentTypeMapper sdEquipmentTypeMapper;
    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;
    @Autowired
    private SdTunnelSubareaMapper sdTunnelSubareaMapper;
    @Autowired
    private SdTunnelsMapper sdTunnelsMapper;
    @Autowired
    private SdReserveProcessMapper sdReserveProcessMapper;

    /**
     * 查询预案信息
     *
     * @param id 预案信息ID
     * @return 预案信息
     */
    @Override
    public SdReservePlan selectSdReservePlanById(Long id) {
        SdReservePlan plan = sdReservePlanMapper.selectSdReservePlanById(id);
        if (plan.getPlanFileId() != null) {
            SdReservePlanFile sdReservePlanFile = new SdReservePlanFile();
            sdReservePlanFile.setPlanFileId(plan.getPlanFileId());
            plan.setpFileList(sdReservePlanFileMapper.selectSdReservePlanFileList(sdReservePlanFile));
        }
//        Long subareaId = plan.getSubareaId();
//        SdTunnelSubarea sdTunnelSubarea = sdTunnelSubareaMapper.selectSdTunnelSubareaBySId(subareaId);
//        plan.setSdTunnelSubarea(sdTunnelSubarea);
        SdTunnels sdTunnels = sdTunnelsMapper.selectSdTunnelsById(plan.getTunnelId());
        plan.setSdTunnels(sdTunnels);
//        List<String> strategyNames = new ArrayList<>();
//        if (!"-1".equals(plan.getStrategyId()) && plan.getStrategyId() != null) {
//            String[] strategyAyy = plan.getStrategyId().split("；");
//            String things = "";
//            int index = 0;
//            for (String s : strategyAyy) {
//                if (s == null || s.equals("")) {
//                    continue;
//                }
//                index++;
//                SdStrategy sds = sdStrategyMapper.selectSdStrategyById(Long.parseLong(s));
//                if (sds == null) {
//                    logger.error("策略未找到！");
//                    continue;
//                }
//                things = things + index + "、" + sds.getStrategyName();
//                strategyNames.add(things);
//            }
//
//        }
//        plan.setStrategyNames(strategyNames);
        return plan;
    }

    /**
     * 查询预案信息列表
     *
     * @param sdReservePlan 预案信息
     * @return 预案信息
     */
    @Override
    public List<SdReservePlan> selectSdReservePlanList(SdReservePlan sdReservePlan) {
        String deptId = SecurityUtils.getDeptId();
        sdReservePlan.getParams().put("deptId", deptId);
        List<SdReservePlan> list = sdReservePlanMapper.selectSdReservePlanList(sdReservePlan);
        StringBuffer buffer = new StringBuffer();
        for(SdReservePlan item : list){
            //查询预案流程列表
            List<SdReserveProcess> processList = sdReserveProcessMapper.getProcessList(item.getId());
            //List<SdReserveProcess> processList = sdReserveProcessMapper.selectSdReserveProcessByRid(item.getId());
            List<String> processStr = processList.stream().map(s->s.getProcessStageName()).collect(Collectors.toList());
            item.setStrategyNames(processStr);

        }
        return list;
    }

    /**
     * 新增预案信息
     *
     * @param sdReservePlan 预案信息
     * @return 结果
     */
    @Override
    public AjaxResult insertSdReservePlan(MultipartFile[] file, SdReservePlan sdReservePlan) {
        int result = -1;
        List<SdReservePlanFile> list = new ArrayList<SdReservePlanFile>();
        if ("#^#".equals(sdReservePlan.getPlanDescription())) {
            sdReservePlan.setPlanDescription(null);
        }
        //创建时间
        sdReservePlan.setCreateTime(DateUtils.getNowDate());
        //设置当前创建人
        sdReservePlan.setCreateBy(SecurityUtils.getUsername());
        //生成guid
        String guid = UUIDUtil.getRandom32BeginTimePK();
        if (file != null && file.length > 0) {
            //文件关联ID
            sdReservePlan.setPlanFileId(guid);
            for (int i = 0; i < file.length; i++) {
                String savePath = OssUtil.upload(file[i], "upload");
                // 从缓存中获取文件存储路径
                //String fileServerPath = RuoYiConfig.getUploadPath();
                // 原图文件名
                String filename = file[i].getOriginalFilename();
                // 原图扩展名
                String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
                // 新的全名
                String fileName = extendName;

                SdReservePlanFile planFile = new SdReservePlanFile();
                planFile.setPlanFileId(guid);
                //planFile.setUrl(fileServerPath + "/" + fileName);
                planFile.setUrl(savePath);
                planFile.setFileName(fileName);
                planFile.setCreateBy(SecurityUtils.getUsername());
                planFile.setCreateTime(DateUtils.getNowDate());
                list.add(planFile);
            }
            result = sdReservePlanMapper.insertSdReservePlan(sdReservePlan);
            if (result > -1) {
                result = sdReservePlanFileMapper.brachInsertSdReservePlanFile(list);
            }
        } else {
            sdReservePlan.setPlanFileId(null);//文件关联ID
            result = sdReservePlanMapper.insertSdReservePlan(sdReservePlan);
        }
        if(result > 0){
            return AjaxResult.success();
        }else {
            return AjaxResult.error();
        }
    }

    /**
     * 修改预案信息
     *
     * @param sdReservePlan 预案信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateSdReservePlan(MultipartFile[] file, SdReservePlan sdReservePlan, Long[] ids) {
        //查询预案名称是否存在
        /*int currCount = sdReservePlanMapper.checkCurrId(searchObj);
        if(currCount > 0){
            throw new RuntimeException("当前预案已被普通事件使用，请勿修改");
        }*/
        List<SdReservePlan> planList = sdReservePlanMapper.checkIfSingleReservePlan(sdReservePlan);
        if (planList.size() > 0 && ids == null) {
            throw new RuntimeException("当前预案修改内容已经存在，请勿重复添加！");
        }
        int result = 0;
        sdReservePlan.setUpdateBy(SecurityUtils.getUsername());
        sdReservePlan.setUpdateTime(DateUtils.getNowDate());
//        return sdReservePlanMapper.updateSdReservePlan(sdReservePlan);
        List<SdReservePlanFile> list = new ArrayList<SdReservePlanFile>();
        if ("-1".equals(sdReservePlan.getStrategyId())) {
            sdReservePlan.setStrategyId(null);
        }
        if ("#^#".equals(sdReservePlan.getPlanDescription())) {
            sdReservePlan.setPlanDescription(null);
        }
        sdReservePlan.setUpdateTime(DateUtils.getNowDate());//创建时间
        sdReservePlan.setUpdateBy(SecurityUtils.getUsername());//设置当前创建人
        sdReservePlan.setPlanFileId("null".equals(sdReservePlan.getPlanFileId()) ? null : sdReservePlan.getPlanFileId());
        String guid = UUIDUtil.getRandom32BeginTimePK();//关联ID--guid

        if (file != null && file.length > 0) {
            for (int i = 0; i < file.length; i++) {
                String savePath = OssUtil.upload(file[i], "upload");
                // 从缓存中获取文件存储路径
                //String fileServerPath = RuoYiConfig.getUploadPath();//Global.getUploadPath()
                // 原图文件名
                String filename = file[i].getOriginalFilename();
                // 原图扩展名
                String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
                // 新的全名
                String fileName = extendName;

                SdReservePlanFile planFile = new SdReservePlanFile();
                planFile.setPlanFileId(sdReservePlan.getPlanFileId() == null ? guid : sdReservePlan.getPlanFileId());
                sdReservePlan.setPlanFileId(planFile.getPlanFileId());//文件关联ID
                //planFile.setUrl(fileServerPath + "/" + fileName);
                planFile.setUrl(savePath);
                planFile.setFileName(fileName);
                planFile.setCreateBy(SecurityUtils.getUsername());
                planFile.setCreateTime(DateUtils.getNowDate());
                list.add(planFile);

                // 加路径全名
                   /* File dir = new File(fileServerPath + "/" + fileName);
                    File filepath = new File(fileServerPath);
                    if (!filepath.exists()) {
                        filepath.mkdirs();
                    } else {
                    }
                    file[i].transferTo(dir);*/
            }
            result = sdReservePlanFileMapper.brachInsertSdReservePlanFile(list);
        } else {
            logger.info("当前文件信息为空或文件没有发生改动");
        }
        if (ids.length > 0) {
            result = sdReservePlanFileMapper.deleteSdReservePlanFileByIds(ids);//ids 为要删除的sd_reserve_plan_file id数组
            //判断此预案是否存在文件，不存在的话文件id设为空
            if(file == null && sdReservePlan.getPlanFileId() != null && sdReservePlan.getPlanFileId() != ""){
                SdReservePlanFile sdReservePlanFile = new SdReservePlanFile();
                sdReservePlanFile.setPlanFileId(sdReservePlan.getPlanFileId());
                List<SdReservePlanFile> sdReservePlanFiles = sdReservePlanFileMapper.selectSdReservePlanFileList(sdReservePlanFile);
                if(sdReservePlanFiles.size() == 0){
                    sdReservePlan.setPlanFileId(null);
                }
            }
        }
        if (result >= 0) {
            SdReservePlan sdReservePlan1 = sdReservePlanMapper.selectSdReservePlanById(sdReservePlan.getId());
            //判断当前传入的隧道id和数据库落地的是否相同
            if(!sdReservePlan.getTunnelId().equals(sdReservePlan1.getTunnelId())){
                //如果不相同则删除原有的配置策略
                sdReserveProcessMapper.deleteSdReserveProcessByPlanId(sdReservePlan.getId());
            }
            result = sdReservePlanMapper.updateSdReservePlan(sdReservePlan);
        }
        if(result > 0){
            return AjaxResult.success();
        }else {
            return AjaxResult.error();
        }
    }

    /**
     * 批量删除预案信息
     *
     * @param rlIds 需要删除的预案信息ID
     * @return 结果
     */
    @Override
    public int deleteSdReservePlanByIds(String[] rlIds) {
        int result = sdReservePlanMapper.deleteSdReservePlanByRlIds(rlIds);
        if (result >= 0) {
            result = sdReservePlanFileMapper.deleteSdReservePlanFileByRlIds(rlIds);
        }
        return result;
    }

    /**
     * 删除预案信息信息
     *
     * @param id 预案信息ID
     * @return 结果
     */
    @Override
    public int deleteSdReservePlanById(Long id) {
        int result = -1;
        SdReservePlan sdReservePlan = sdReservePlanMapper.selectSdReservePlanById(id);
        //查询此预案是否被使用
        /*int currCount = sdReservePlanMapper.checkCurrId(sdReservePlan);
        if(currCount > 0){
            throw new RuntimeException("当前预案已被普通事件使用，请勿删除");
        }*/
        if (sdReservePlan != null) {
            result = sdReservePlanMapper.deleteSdReservePlanById(id);
            if (result > 0) {
                sdReservePlanFileMapper.deleteSdReservePlanFileByPlanFileId(sdReservePlan.getPlanFileId());
                sdReserveProcessMapper.deleteSdReserveProcessByPlanId(sdReservePlan.getId());
                //删除策略设备信息表
                sdStrategyRlMapper.deleteSdStrategyRlByPlanId(id);
            }
        }
        return result;
    }

    /**
     * 根据预案id，查询策略信息列表
     *
     * @param id 预案信息
     * @return 预案信息
     */
    @Override
    public List<SdStrategy> selectStrategyListByPlanId(Long id) {
        List<SdStrategy> list = new ArrayList<SdStrategy>();
        SdReservePlan plan = sdReservePlanMapper.selectSdReservePlanById(id);
        String strategyIds = plan.getStrategyId();
        if (!"-1".equals(strategyIds)) {
            String[] strategyIdAyy = strategyIds.split("；");
            for (String s : strategyIdAyy) {
                SdStrategy sds = sdStrategyMapper.selectSdStrategyById(Long.parseLong(s));
                list.add(sds);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            List<String> sList = new ArrayList<String>();
            SdStrategyRl rl = new SdStrategyRl();
            String strategyType = list.get(i).getStrategyType();
            if ("1".equals(strategyType)) {
                sList.add(list.get(i).getStrategyInfo());
            }
            List<SdStrategyRl> rlList = sdStrategyRlMapper.selectSdStrategyRlList(rl);
            //策略关联表信息
            for (int j = 0; j < rlList.size(); j++) {
                SdEquipmentType typeobject = sdEquipmentTypeMapper.selectSdEquipmentTypeById(Long.parseLong(rlList.get(j).getEqTypeId()));
                String typeName = typeobject.getTypeName();//设备类型名称
                SdEquipmentState stateobject = sdEquipmentStateMapper.selectSdEquipmentStateById(Long.parseLong(rlList.get(j).getState()));
                String stateName = stateobject.getStateName();//设备状态名称
                sList.add(typeName + "控制执行：" + stateName + "；");
            }

            list.get(i).setSlist(sList);
        }
        return list;
    }

    /**
     * 查询预案类型
     *
     * @return
     */
    @Override
    public List<Map> selectPlanCategory() {
        return sdReservePlanMapper.selectPlanCategory();
    }

    /**
     * 根据分区id查询预案
     *
     * @param sdReservePlan
     * @return
     */
    @Override
    public List<SdReservePlan> selectSdReservePlanBySubareaId(SdReservePlan sdReservePlan) {
        List<SdReservePlan> sdReservePlans = sdReservePlanMapper.selectSdReservePlanBySubareaId(sdReservePlan);
        return sdReservePlans;
    }

    @Override
    public List<Map> selectSdReservePlanByTunnelId(String tunnelId) {
        List<Map> mapList = new ArrayList<>();
        List<SdTunnelSubarea> sdTunnelSubareas = SpringUtils.getBean(SdTunnelSubareaMapper.class).selectSdTunnelSubareaByTunnelId(tunnelId);
        for (SdTunnelSubarea sdTunnelSubarea : sdTunnelSubareas) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sdTunnelSubarea.getsId());
            map.put("SubareaName", sdTunnelSubarea.getsName());
            map.put("direction", sdTunnelSubarea.getDirection());
            SdReservePlan sdReservePlan = new SdReservePlan();
            sdReservePlan.setSubareaId(sdTunnelSubarea.getsId());
            List<SdReservePlan> sdReservePlans = sdReservePlanMapper.selectSdReservePlanBySubareaId(sdReservePlan);
            map.put("reservePlans", sdReservePlans);
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public AjaxResult getReservePlanData(SdReservePlan sdReservePlan) {
        List<SdReservePlan> sdReservePlans = sdReservePlanMapper.selectSdReservePlanList(sdReservePlan);
        List<SdReservePlan> list = new ArrayList<>();
        //查询是否存在策略
        for(SdReservePlan item : sdReservePlans){
            SdReserveProcess sdReserveProcess = new SdReserveProcess();
            sdReserveProcess.setReserveId(item.getId());
            List<SdReserveProcess> sdReserveProcesses = sdReserveProcessMapper.selectSdReserveProcessList(sdReserveProcess);
            if(sdReserveProcesses.size() > 0){
                list.add(item);
            }
        }
        return AjaxResult.success(list);
    }

    @Override
    public AjaxResult checkPlanName(SdReservePlan sdReservePlan) {
        SdReservePlan searchObj = new SdReservePlan();
        searchObj.setId(sdReservePlan.getId());
        searchObj.setTunnelId(sdReservePlan.getTunnelId());
        searchObj.setPlanName(sdReservePlan.getPlanName());
        //校验预案名称
        int nameCount = sdReservePlanMapper.checkPlanName(searchObj);
        if(nameCount > 0){
            return AjaxResult.error("预案名称已存在请重新输入！");
        }
        return AjaxResult.success();
    }
}
