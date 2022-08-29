package com.tunnel.platform.service.dataInfo;

import com.tunnel.platform.domain.dataInfo.SdTunnels;
import com.tunnel.platform.domain.event.SdTunnelSubarea;

import java.util.List;

/**
 * 隧道Service接口
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
public interface ISdTunnelsService
{
    /**
     * 查询隧道
     *
     * @param tunnelId 隧道ID
     * @return 隧道
     */
    public SdTunnels selectSdTunnelsById(String tunnelId);

    /**
     * 查询隧道列表
     *
     * @param sdTunnels 隧道
     * @return 隧道集合
     */
    public List<SdTunnels> selectSdTunnelsList(SdTunnels sdTunnels);

    /**
     * 新增隧道
     *
     * @param sdTunnels 隧道
     * @return 结果
     */
    public int insertSdTunnels(SdTunnels sdTunnels);

    /**
     * 修改隧道
     *
     * @param sdTunnels 隧道
     * @return 结果
     */
    public int updateSdTunnels(SdTunnels sdTunnels);

    /**
     * 批量删除隧道
     *
     * @param tunnelIds 需要删除的隧道ID
     * @return 结果
     */
    public int deleteSdTunnelsByIds(String[] tunnelIds);

    /**
     * 删除隧道信息
     *
     * @param tunnelId 隧道ID
     * @return 结果
     */
    public int deleteSdTunnelsById(String tunnelId);


    public List<SdTunnels> selectSdTunnelsSubList(SdTunnels sdTunnels);


}
