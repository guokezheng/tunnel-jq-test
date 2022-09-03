package com.tunnel.platform.service.event.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.platform.domain.event.SdTunnelSubarea;
import com.tunnel.platform.mapper.event.SdTunnelSubareaMapper;
import com.tunnel.platform.service.event.ISdTunnelSubareaService;
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
public class SdTunnelSubareaServiceImpl implements ISdTunnelSubareaService
{
    @Autowired
    private SdTunnelSubareaMapper sdTunnelSubareaMapper;

    /**
     * 查询隧道分区
     *
     * @param sId 隧道分区主键
     * @return 隧道分区
     */
    @Override
    public SdTunnelSubarea selectSdTunnelSubareaBySId(Long sId)
    {
        return sdTunnelSubareaMapper.selectSdTunnelSubareaBySId(sId);
    }

    /**
     * 查询隧道分区列表
     *
     * @param sdTunnelSubarea 隧道分区
     * @return 隧道分区
     */
    @Override
    public List<SdTunnelSubarea> selectSdTunnelSubareaList(SdTunnelSubarea sdTunnelSubarea)
    {
        return sdTunnelSubareaMapper.selectSdTunnelSubareaLists(sdTunnelSubarea);
    }

    /**
     * 新增隧道分区
     *
     * @param sdTunnelSubarea 隧道分区
     * @return 结果
     */
    @Override
    public int insertSdTunnelSubarea(SdTunnelSubarea sdTunnelSubarea)
    {
        sdTunnelSubarea.setCreateTime(DateUtils.getNowDate());
        return sdTunnelSubareaMapper.insertSdTunnelSubarea(sdTunnelSubarea);
    }

    /**
     * 修改隧道分区
     *
     * @param sdTunnelSubarea 隧道分区
     * @return 结果
     */
    @Override
    public int updateSdTunnelSubarea(SdTunnelSubarea sdTunnelSubarea)
    {
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
    public int deleteSdTunnelSubareaBySIds(Long[] sIds)
    {
        return sdTunnelSubareaMapper.deleteSdTunnelSubareaBySIds(sIds);
    }

    /**
     * 删除隧道分区信息
     * @param sId 隧道分区主键
     * @return 结果
     */
    @Override
    public int deleteSdTunnelSubareaBySId(Long sId)
    {
        return sdTunnelSubareaMapper.deleteSdTunnelSubareaBySId(sId);
    }


}
