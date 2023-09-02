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
     * 查询隧道list根据权限
     * @param deptId
     * @return
     */
    List<SdTunnels> selectTunnelsDeptIdList(String deptId);

    /**
     * 查询隧道列表
     * @param deptId
     * @return
     */
    List<SdTunnels> selectTunnelList(String deptId,String tunnelId);

    /**
     * 查询当前登录者所属
     * @return
     */
    List<SdTunnels> getJlyTunnel();

    List<SdTunnels> selectAllSdTunnelsList();

    /**
     * 查询所有的隧道列表
     * @param sdTunnels
     * @return
     */
    List<SdTunnels> selectSdTunnelsList1(SdTunnels sdTunnels);

    /**
     * 外部系统获取隧道下拉
     * @return
     */
    List<SdTunnels> selectAllSdTunnelsList1();

    /**
     * 查询隧道所属部门
     * @param tunnelId
     * @return
     */
    String selectTunnelDept(String tunnelId);

    /**
     * 查询数据报表折线图设备树
     * @param deptId
     * @return
     */
    List<SdTunnels> selectTunnelLineList(String deptId);

    /**
     * 获取部门下的所有隧道
     * @param deptId
     * @return
     */
    List<SdTunnels> selectTunnels(String deptId);

    List<SdTunnels> selectSiteTunnelList(String deptId, String deptName);

    /**
     * 查询所包含隧道
     * @param deptCode
     * @return
     */
    /*List<String> getChildCodeList(String deptCode);*/


    List<SdTunnels> deptIdList(String[] ids);
}
