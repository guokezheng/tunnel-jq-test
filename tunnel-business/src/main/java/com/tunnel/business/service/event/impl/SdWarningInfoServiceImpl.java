package com.tunnel.business.service.event.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.event.*;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.mapper.event.SdReservePlanFileMapper;
import com.tunnel.business.mapper.event.SdStrategyMapper;
import com.tunnel.business.mapper.event.SdWarningInfoMapper;
import com.tunnel.business.mapper.event.SdWarningTypeMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.event.ISdReservePlanService;
import com.tunnel.business.service.event.ISdWarningInfoService;
import com.tunnel.business.utils.util.ImgTobase64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 预警信息Service业务层处理
 *
 * @author gongfanfei
 * @date 2020-08-21
 */
@Service
public class SdWarningInfoServiceImpl implements ISdWarningInfoService {
    @Autowired
    private SdWarningInfoMapper sdWarningInfoMapper;
    @Autowired
    private SdWarningTypeMapper sdWarningTypeMapper;
    @Autowired
    private ISdReservePlanService sdReservePlanService;
    @Autowired
    private SdReservePlanFileMapper sdReservePlanFileMapper;
    @Autowired
    private SdStrategyMapper sdStrategyMapper;

    @Autowired
    private SdTunnelsMapper sdTunnelsMapper;
    @Autowired
    private ISdDevicesService sdDevicesService;

    public static List<Object> removeDuplicate(List<Object> list) {
        HashSet<Object> h = new HashSet<Object>(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }

    /**
     * 查询预警信息
     *
     * @param id 预警信息ID
     * @return 预警信息
     */
    @Override
    public SdWarningInfo selectSdWarningInfoById(Long id) {
        return sdWarningInfoMapper.selectSdWarningInfoById(id);
    }

    /**
     * 查询预警信息列表
     *
     * @param sdWarningInfo 预警信息
     * @return 预警信息
     */
    @Override
    public List<SdWarningInfo> selectSdWarningInfoList(SdWarningInfo sdWarningInfo) {
        Long deptId = SecurityUtils.getDeptId();
        sdWarningInfo.getParams().put("deptId", deptId);
        List<SdWarningInfo> list = sdWarningInfoMapper.selectSdWarningInfoList(sdWarningInfo);
        for (int i = 0; i < list.size(); i++) {
            String url = list.get(i).getPicture();
            if(StringUtils.isNotNull(url)){
                String base64url = ImgTobase64.ioToBase64(url);
                list.get(i).setPicture(base64url);
            }
        }
        return list;
    }

    /**
     * 新增预警信息
     *
     * @param sdWarningInfo 预警信息
     * @return 结果
     */
    @Override
    public int insertSdWarningInfo(SdWarningInfo sdWarningInfo) {
        sdWarningInfo.setCreateTime(DateUtils.getNowDate());
        return sdWarningInfoMapper.insertSdWarningInfo(sdWarningInfo);
    }

    /**
     * 修改预警信息
     *
     * @param sdWarningInfo 预警信息
     * @return 结果
     */
    @Override
    public int updateSdWarningInfo(SdWarningInfo sdWarningInfo) {
        SdWarningInfo warningInfo = new SdWarningInfo();
        warningInfo.setId(sdWarningInfo.getId());
        warningInfo.setUpdateTime(DateUtils.getNowDate());
        warningInfo.setHandler(SecurityUtils.getUsername());
        warningInfo.setProcessState(sdWarningInfo.getProcessState());
        return sdWarningInfoMapper.updateSdWarningInfo(warningInfo);
    }

    @Override
    public int updateSdWarningInfoByProcessState(SdWarningInfo warningInfo) {
        return sdWarningInfoMapper.updateSdWarningInfoByProcessState(warningInfo);
    }

    /**
     * 批量删除预警信息
     *
     * @param ids 需要删除的预警信息ID
     * @return 结果
     */
    @Override
    public int deleteSdWarningInfoByIds(Long[] ids) {
        return sdWarningInfoMapper.deleteSdWarningInfoByIds(ids);
    }

    /**
     * 删除预警信息信息
     *
     * @param id 预警信息ID
     * @return 结果
     */
    @Override
    public int deleteSdWarningInfoById(Long id) {
        return sdWarningInfoMapper.deleteSdWarningInfoById(id);
    }

    /**
     * 查询相关预案信息
     */
    @Override
    public List<SdReservePlan> seePlanListByWarningTypeId(Long warningTypeId) {
        List<SdReservePlan> planList = new ArrayList<SdReservePlan>();
        SdWarningType warnType = sdWarningTypeMapper.selectSdWarningTypeById(warningTypeId);//
        String planIds = warnType.getReservePlanIds();
        if (planIds != null && !"".equals(planIds)) {
            //获取预案id数组
            String[] planIdArray = planIds.split(",");
            for (int i = 0; i < planIdArray.length; i++) {
                SdReservePlan plan = sdReservePlanService.selectSdReservePlanById(Long.parseLong(planIdArray[i]));
                if (plan.getPlanFileId() != null) {
                    SdReservePlanFile sdReservePlanFile = new SdReservePlanFile();
                    sdReservePlanFile.setPlanFileId(plan.getPlanFileId());
                    plan.setpFileList(sdReservePlanFileMapper.selectSdReservePlanFileList(sdReservePlanFile));
                }
                planList.add(plan);
            }
        } else {
            throw new RuntimeException("当前预警类型下没有配置相关预案！请配置预案后重试！");
        }
        return planList;
    }

    /**
     * 查询当前预警信息，相关策略（预警类型--预案--策略）
     */
    @Override
    public List<SdStrategy> seeStrategyListById(Long warningTypeId) {
        List<SdStrategy> strategyList = new ArrayList<SdStrategy>();
        List<Object> list = new ArrayList<Object>();
        SdWarningType warnType = sdWarningTypeMapper.selectSdWarningTypeById(warningTypeId);//
        String planIds = warnType.getReservePlanIds();
        if (planIds != null && !"".equals(planIds)) {
            //获取预案id数组
            String[] planIdArray = planIds.split(",");
            for (int i = 0; i < planIdArray.length; i++) {
                SdReservePlan plan = sdReservePlanService.selectSdReservePlanById(Long.parseLong(planIdArray[i]));
                if (plan.getStrategyId() != null) {
                    //得到预案-----分割---查询策略 add
                    String[] stgyArr = plan.getStrategyId().split("；");
                    for (int j = 0; j < stgyArr.length; j++) {
						/*SdStrategy s = sdStrategyMapper.selectSdStrategyById(Long.parseLong(stgyArr[j]));
						strategyList.add(s);*/
                        list.add(Long.parseLong(stgyArr[j]));
                    }

                }
            }
            //list 去重
            list = removeDuplicate(list);
            for (int k = 0; k < list.size(); k++) {
                SdStrategy s = sdStrategyMapper.selectSdStrategyById((Long) list.get(k));
                strategyList.add(s);
            }

        } else {
            throw new RuntimeException("当前预警类型下没有配置相关策略！请配置策略后重试！");
        }
        return strategyList;
    }

    /**
     * 查询最近一周的报警数量
     */
    @Override
    public List<Map<String, String>> selectSdWarningInfoEcharts(SdWarningInfo sdWarningInfo) {
        List<Map<String, String>> list = sdWarningInfoMapper.selectSdWarningInfoEcharts(sdWarningInfo);
        return list;
    }

    /**
     * 查询报警数量
     */
    @Override
    public Map<String, Object> selectSdWarningInfoCount(SdWarningInfo sdWarningInfo) {
        return sdWarningInfoMapper.selectSdWarningInfoCount(sdWarningInfo);
    }

    @Override
    public List<SdWarningInfo> selectSdWarningList(SdWarningInfo sdWarningInfo) {
        Long deptId = SecurityUtils.getDeptId();
        sdWarningInfo.getParams().put("deptId", deptId);
        List<SdWarningInfo> list = sdWarningInfoMapper.selectSdWarningList(sdWarningInfo);
        return list;
    }

    @Override
    public List<SdWarningInfo> getAllPressureGaugesWarningMsg() {
        Long deptId = SecurityUtils.getDeptId();
        return sdWarningInfoMapper.getAllPressureGaugesWarningMsg(deptId);
    }

    @Override
    public List<Map<String, Object>> getWarningDataAnalysis(SdWarningInfo sdWarningInfo) throws ParseException {
        String tunnelId = null;
        if (null != sdWarningInfo.getTunnelId() && !sdWarningInfo.getTunnelId().equals("")) {
            tunnelId = sdWarningInfo.getTunnelId();
            SdTunnels sdTunnels = sdTunnelsMapper.selectSdTunnelsById(tunnelId);
            sdWarningInfo.setTunnelName(sdTunnels.getTunnelName());
        } else {
            return null;
        }
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> warn = new HashMap<>();
        //按年查询每月数量
        Map<String, Object> params = sdWarningInfo.getParams();
        Long deptId = SecurityUtils.getDeptId();
        params.put("deptId", deptId);
        if (null != sdWarningInfo.getYear()) {
            String year = sdWarningInfo.getYear().toString();
            List<Map<String, Object>> yearmaps = sdWarningInfoMapper.selectWarningInfoMsgByYear(tunnelId, year, params);
            warn.put("year", yearmaps);
        } else if (params.get("year") != null) {
            String metEnd = params.get("year").toString();
            String year = metEnd.substring(0, 4);
            List<Map<String, Object>> yearmaps = sdWarningInfoMapper.selectWarningInfoMsgByYear(tunnelId, year, params);
            warn.put("year", yearmaps);
        }
        //按月查询每日数量
        if (null != sdWarningInfo.getMonth()) {
            String month = sdWarningInfo.getMonth().toString();
            String lastDayOfMonth = getLastDayOfMonth(Integer.parseInt(month.substring(0, 4)), Integer.parseInt(month.substring(6)));
            List<Map<String, Object>> monthmaps = sdWarningInfoMapper.selectWarningInfoMsgByMonth(tunnelId, lastDayOfMonth, month, params);
            warn.put("month", monthmaps);
        } else if (params.get("month") != null) {
            String metEnd = params.get("month").toString();
            String month = metEnd.substring(0, 7);
            String lastDayOfMonth = getLastDayOfMonth(Integer.parseInt(month.substring(0, 4)), Integer.parseInt(month.substring(6)));
            List<Map<String, Object>> monthmaps = sdWarningInfoMapper.selectWarningInfoMsgByMonth(tunnelId, lastDayOfMonth, month, params);
            warn.put("month", monthmaps);
        }
        //按小时查询
        if (null != sdWarningInfo.getDay()) {
            String day = sdWarningInfo.getDay().toString();
            params.put("datea", day + " 00:00:00");
            params.put("dateb", day + " 23:59:59");
            List<Map<String, Object>> daymaps = sdWarningInfoMapper.selectWarningInfoMsgByDay(tunnelId, params);
            warn.put("date", daymaps);
        } else if (params.get("day") != null) {
            String metEnd = params.get("day").toString();
            params.put("datea", metEnd + " 00:00:00");
            params.put("dateb", metEnd + " 23:59:59");
            List<Map<String, Object>> daymaps = sdWarningInfoMapper.selectWarningInfoMsgByDay(tunnelId, params);
            warn.put("date", daymaps);
        }
        list.add(warn);
        return list;
    }

    @Override
    public List<Map<String, Object>> getTheWarningDataOfToday(SdWarningInfo sdWarningInfo) {
        Long deptId = SecurityUtils.getDeptId();
        sdWarningInfo.getParams().put("deptId", deptId);
        List<Map<String, Object>> theWarningDataOfToday = sdWarningInfoMapper.getTheWarningDataOfToday(sdWarningInfo);
        return theWarningDataOfToday;
    }

    @Override
    public List<String> getSystemWarningMsg() {
        Long deptId = SecurityUtils.getDeptId();
        List<String> strings = sdWarningInfoMapper.sdWarningInfoMapper(deptId);
//		if (strings.size() == 0) {
//			strings = new ArrayList<>();
//			strings.add("当前暂无系统预警");
//		}
        return strings;
    }

    @Override
    public List<String> getTrafficIncident() {
        Long deptId = SecurityUtils.getDeptId();
        List<String> trafficIncident = sdWarningInfoMapper.getTrafficIncident(deptId);
//		if (trafficIncident.size() == 0) {
//			trafficIncident = new ArrayList<>();
//			trafficIncident.add("当前暂无交通事件");
//		}
        return trafficIncident;
    }

    @Override
    public Map<String, Object> getWarningInfoCount() {
        Map<String, Object> warningInfoCount = sdWarningInfoMapper.getWarningInfoCount();
        String process = warningInfoCount.get("process").toString();
        String all = warningInfoCount.get("allmsg").toString();
        if (process.equals("0") || all.equals("0")) {
            warningInfoCount.put("proportion", "0%");
            return warningInfoCount;
        } else {
            int processToInt = Integer.parseInt(process);
            int allToInt = Integer.parseInt(all);
            double num = (double) processToInt / allToInt * 100;
            num = Math.round(num);
            String formatNumber = num + "%";
            warningInfoCount.put("proportion", formatNumber);
            return warningInfoCount;
        }
    }

    @Override
    public Map<String, Object> getCarPosition(String tunnelId) {
        if (tunnelId.equals("") || tunnelId == null) {
            throw new RuntimeException("隧道ID不能为空");
        }


        return null;
    }

    @Override
    public Map<String, Object> getWarningMessageCountOfToday(String tunnelId) {
        Map<String, Object> warningMessageCountOfToday = sdWarningInfoMapper.getWarningMessageCountOfToday(tunnelId);
        Map<String, Object> unProcessWarningOfMonth = sdWarningInfoMapper.getUnProcessWarningOfMonth(tunnelId);
        warningMessageCountOfToday.put("unProcessOfMonth", unProcessWarningOfMonth.get("unProcessOfMonth"));
        return warningMessageCountOfToday;
    }

    @Override
    public List<SdWarningInfo> getWarningMassageOfUnProcess(String tunnelId) {
        SdWarningInfo warningInfo = new SdWarningInfo();
        Long deptId = SecurityUtils.getDeptId();
        warningInfo.getParams().put("deptId", deptId);
        warningInfo.setTunnelId(tunnelId);
        warningInfo.setProcessState("0");
        List<SdWarningInfo> warningMassageOfUnProcess = sdWarningInfoMapper.getWarningMassageOfUnProcess(warningInfo);
        return warningMassageOfUnProcess;
    }
}
