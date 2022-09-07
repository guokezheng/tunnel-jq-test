package com.tunnel.platform.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.platform.domain.dataInfo.SdTunnels;
import com.tunnel.platform.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.platform.service.dataInfo.ISdTunnelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 隧道Service业务层处理
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
@Service
public class SdTunnelsServiceImpl implements ISdTunnelsService
{
    @Autowired
    private SdTunnelsMapper sdTunnelsMapper;

    /**
     * 查询隧道
     *
     * @param tunnelId 隧道ID
     * @return 隧道
     */
    @Override
    public SdTunnels selectSdTunnelsById(String tunnelId)
    {
        return sdTunnelsMapper.selectSdTunnelsById(tunnelId);
    }

    /**
     * 查询隧道列表
     *
     * @param sdTunnels 隧道
     * @return 隧道
     */
    @Override
    public List<SdTunnels> selectSdTunnelsList(SdTunnels sdTunnels)
    {
        return sdTunnelsMapper.selectSdTunnelsList(sdTunnels);
    }

    /**
     * 新增隧道
     *
     * @param sdTunnels 隧道
     * @return 结果
     */
    @Override
    public int insertSdTunnels(SdTunnels sdTunnels)
    {
        List<SdTunnels> tunnels = sdTunnelsMapper.selectSdTunnelsList(sdTunnels);
        if (tunnels.size() > 0) {
            throw new RuntimeException("当前隧道已经存在，请核对后重试！");
        }
        SdTunnels tunnelsById = sdTunnelsMapper.selectSdTunnelsById(sdTunnels.getTunnelId());
        if (tunnelsById != null) {
            throw new RuntimeException("当前隧道ID已经存在，请核对后重试！");
        }
        sdTunnels.setCreateTime(DateUtils.getNowDate());
        return sdTunnelsMapper.insertSdTunnels(sdTunnels);
    }

    /**
     * 修改隧道
     *
     * @param sdTunnels 隧道
     * @return 结果
     */
    @Override
    public int updateSdTunnels(SdTunnels sdTunnels)
    {
        sdTunnels.setUpdateTime(DateUtils.getNowDate());
        return sdTunnelsMapper.updateSdTunnels(sdTunnels);
    }

    /**
     * 批量删除隧道
     *
     * @param tunnelIds 需要删除的隧道ID
     * @return 结果
     */
    @Override
    public int deleteSdTunnelsByIds(String[] tunnelIds)
    {
        return sdTunnelsMapper.deleteSdTunnelsByIds(tunnelIds);
    }

    /**
     * 删除隧道信息
     *
     * @param tunnelId 隧道ID
     * @return 结果
     */
    @Override
    public int deleteSdTunnelsById(String tunnelId)
    {
        return sdTunnelsMapper.deleteSdTunnelsById(tunnelId);
    }

    @Override
    public List<SdTunnels> selectSdTunnelsSubList(SdTunnels sdTunnels) {
        return sdTunnelsMapper.selectSdTunnelsSubList(sdTunnels);
    }

    @Override
    public List<SdTunnels> deptId(Long deptId) {
        return sdTunnelsMapper.deptId(deptId);
    }
}
