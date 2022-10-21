package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.event.SdReservePlan;
import com.tunnel.business.domain.event.SdTunnelSubarea;
import com.tunnel.business.mapper.event.SdReservePlanMapper;
import com.tunnel.business.mapper.event.SdTunnelSubareaMapper;
import com.tunnel.business.service.dataInfo.ISdTunnelSubareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 隧道分区Service业务层处理
 *
 * @author ruoyi
 * @date 2022-08-25
 */
@Service
public class SdTunnelSubareaServiceImpl implements ISdTunnelSubareaService {
    @Autowired
    private SdTunnelSubareaMapper sdTunnelSubareaMapper;

    @Autowired
    private SdReservePlanMapper sdReservePlanMapper;

    /**
     * 查询隧道分区
     *
     * @param sId 隧道分区主键
     * @return 隧道分区
     */
    @Override
    public SdTunnelSubarea selectSdTunnelSubareaBySId(Long sId) {
        return sdTunnelSubareaMapper.selectSdTunnelSubareaBySId(sId);
    }

    /**
     * 查询隧道分区列表
     *
     * @param sdTunnelSubarea 隧道分区
     * @return 隧道分区
     */
    @Override
    public List<SdTunnelSubarea> selectSdTunnelSubareaList(SdTunnelSubarea sdTunnelSubarea) {
        Long deptId = SecurityUtils.getDeptId();
        sdTunnelSubarea.getParams().put("deptId", deptId);
        return sdTunnelSubareaMapper.selectSdTunnelSubareaLists(sdTunnelSubarea);
    }

    /**
     * 新增隧道分区
     *
     * @param sdTunnelSubarea 隧道分区
     * @return 结果
     */
    @Override
    public int insertSdTunnelSubarea(SdTunnelSubarea sdTunnelSubarea) {
        int result = -1;
        sdTunnelSubarea.setCreateTime(DateUtils.getNowDate());
        List<SdTunnelSubarea> sdTunnelSubareas = sdTunnelSubareaMapper.selectSdTunnelSubareaList(sdTunnelSubarea);
        if (sdTunnelSubareas.size() == 0) {
            result = sdTunnelSubareaMapper.insertSdTunnelSubarea(sdTunnelSubarea);
        } else {
            throw new RuntimeException("该隧道分区已存在!");
        }
        return result;
    }

    /**
     * 修改隧道分区
     *
     * @param sdTunnelSubarea 隧道分区
     * @return 结果
     */
    @Override
    public int updateSdTunnelSubarea(SdTunnelSubarea sdTunnelSubarea) {
        sdTunnelSubarea.setUpdateTime(DateUtils.getNowDate());
        return sdTunnelSubareaMapper.updateSdTunnelSubarea(sdTunnelSubarea);
    }

    /**
     * 批量删除隧道分区
     *
     * @param sIds 需要删除的隧道分区主键
     * @return 结果
     */
    @Override
    public int deleteSdTunnelSubareaBySIds(Long[] sIds) {
        return sdTunnelSubareaMapper.deleteSdTunnelSubareaBySIds(sIds);
    }

    /**
     * 删除隧道分区信息
     *
     * @param sId 隧道分区主键
     * @return 结果
     */
    @Override
    public int deleteSdTunnelSubareaBySId(Long sId) {
        return sdTunnelSubareaMapper.deleteSdTunnelSubareaBySId(sId);
    }

    /**
     * 根据隧道id查询隧道分区
     *
     * @param tunnelId
     * @return
     */
    @Override
    public List<Map> selectSdTunnelSubareaByTunnelId(String tunnelId,String eventTypeId) {
        List<Map> mapList = new ArrayList<>();
        List<SdTunnelSubarea> sdTunnelSubareas = sdTunnelSubareaMapper.selectSdTunnelSubareaByTunnelId(tunnelId);
        for (SdTunnelSubarea sdTunnelSubarea : sdTunnelSubareas) {
            Map<String, Object> map = new HashMap<>();
            map.put("sId",sdTunnelSubarea.getsId());
            map.put("sName",sdTunnelSubarea.getsName());
            map.put("tunnelId",sdTunnelSubarea.getTunnelId());
            map.put("pileMin",sdTunnelSubarea.getPileMin());
            map.put("pileMax",sdTunnelSubarea.getPileMax());
            map.put("direction",sdTunnelSubarea.getDirection());
            map.put("eqIdMin",sdTunnelSubarea.getEqIdMin());
            map.put("eqIdMax",sdTunnelSubarea.getEqIdMax());
            SdReservePlan sdReservePlan = new SdReservePlan();
            sdReservePlan.setSubareaId(sdTunnelSubarea.getsId());
            sdReservePlan.setPlanTypeId(Long.valueOf(eventTypeId));
            List<SdReservePlan> sdReservePlans = sdReservePlanMapper.selectSdReservePlanBySubareaId(sdReservePlan);
            map.put("reservePlans", sdReservePlans);
            mapList.add(map);
        }
        return mapList;
    }
}
