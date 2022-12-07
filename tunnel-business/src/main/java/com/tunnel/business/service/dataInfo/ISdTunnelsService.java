package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdTunnels;

import java.util.List;
import java.util.Map;

/**
 * 隧道Service接口
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
public interface ISdTunnelsService {
    /**
     * 查询隧道
     *
     * @param tunnelId 隧道ID
     * @return 隧道
     */
    SdTunnels selectSdTunnelsById(String tunnelId);

    /**
     * 查询隧道列表
     *
     * @param sdTunnels 隧道
     * @return 隧道集合
     */
    List<SdTunnels> selectSdTunnelsList(SdTunnels sdTunnels);

    /**
     * 新增隧道
     *
     * @param sdTunnels 隧道
     * @return 结果
     */
    int insertSdTunnels(SdTunnels sdTunnels);

    /**
     * 修改隧道
     *
     * @param sdTunnels 隧道
     * @return 结果
     */
    int updateSdTunnels(SdTunnels sdTunnels);

    /**
     * 批量删除隧道
     *
     * @param tunnelIds 需要删除的隧道ID
     * @return 结果
     */
    int deleteSdTunnelsByIds(String[] tunnelIds);

    /**
     * 删除隧道信息
     *
     * @param tunnelId 隧道ID
     * @return 结果
     */
    int deleteSdTunnelsById(String tunnelId);

    /**
     * 查询隧道分区
     *
     * @param sdTunnels
     * @return
     */
    List<SdTunnels> selectSdTunnelsSubList(SdTunnels sdTunnels);

    List<SdTunnels> deptId(Long deptId);


    /**
     * 获取所有隧道的Map格式
     * key: 隧道ID tunnelId,value:隧道名称tunnelName
     * @return
     */
    Map<String,String> getTunnelNameMap();

    /**
     * 查询隧道list根据权限
     * @param deptId
     * @return
     */
    List<SdTunnels> selectTunnelsList(String deptId);

    /**
     * 查询隧道列表
     * @param deptId
     * @return
     */
    List<SdTunnels> selectTunnelList(String deptId);

    /**
     * 查询当前登录者所属
     * @return
     */
    List<SdTunnels> getJlyTunnel();
}
