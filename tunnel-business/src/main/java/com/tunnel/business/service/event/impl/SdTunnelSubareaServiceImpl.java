package com.tunnel.business.service.event.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.event.SdTunnelSubarea;
import com.tunnel.business.mapper.event.SdTunnelSubareaMapper;
import com.tunnel.business.service.event.ISdTunnelSubareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


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
    public List<SdTunnelSubarea> selectSdTunnelSubareaByTunnelId(String tunnelId) {
        return sdTunnelSubareaMapper.selectSdTunnelSubareaByTunnelId(tunnelId);
    }
}
