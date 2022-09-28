package com.tunnel.business.mapper.dataInfo;


import com.tunnel.business.domain.dataInfo.SdTunnels;

import java.util.List;
import java.util.Map;

/**
 * 隧道Mapper接口
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
public interface SdTunnelsMapper
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
     * 删除隧道
     *
     * @param tunnelId 隧道ID
     * @return 结果
     */
    public int deleteSdTunnelsById(String tunnelId);

    /**
     * 批量删除隧道
     *
     * @param tunnelIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdTunnelsByIds(String[] tunnelIds);

    /**
     * 查询隧道分区
     * @param sdTunnels
     * @return
     */
    public List<SdTunnels> selectSdTunnelsSubList(SdTunnels sdTunnels);
    public List<SdTunnels> deptId(Long deptId);


    /**
     * 查询所有隧道列表
     * 查询隧道表部分字段，避免频繁查询大字段store_configure
     * @return
     */
    List<Map<String,String>> getTunnelList();

}
