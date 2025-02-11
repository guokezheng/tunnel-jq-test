package com.tunnel.business.mapper.dataInfo;


import com.tunnel.business.domain.dataInfo.SdTunnels;
import org.apache.ibatis.annotations.Param;

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
     * 查询隧道是否重复
     *
     * @param sdTunnels 隧道
     * @return 隧道集合
     */
    public List<SdTunnels> verifyTunnelOnly(SdTunnels sdTunnels);

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

    /**
     * 查询隧道list --根据权限
     * @param deptId
     * @return
     */
    List<SdTunnels> selectTunnelsList(String deptId);

    /**
     * 查询隧道list --根据权限
     * @param deptId
     * @return
     */
    List<SdTunnels> selectTunnelsDeptIdList(String deptId);

    /**
     * 查询各管理站隧道详细信息
     * @param deptId
     * @return
     */
    List<SdTunnels> getTunnelDataDept(@Param("deptId") String deptId);

    /**
     * 查询隧道list
     * @return
     */
    List<SdTunnels> selectTunList(SdTunnels sdTunnels);


    /**
     * 查询隧道列表
     * @param deptId
     * @return
     */
    List<SdTunnels> selectTunnelList(@Param("deptId")String deptId,@Param("tunnelId")String tunnelId);

    /**
     * 查询当前登录者所属
     * @return
     */
    List<SdTunnels> getJlyTunnel(@Param("deptId") String deptId);

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
     * 查询所有可使用隧道
     * @return
     */
    List<SdTunnels> selectSdTunnelsLists();

    /**
     * 查询隧道所属部门
     * @param tunnelId
     * @return
     */
    String selectTunnelDept(@Param("tunnelId") String tunnelId);

    List<SdTunnels> selectTunnelLineList(String deptId);

    String isTunnel(@Param("deviceId") String deviceId);

    List<SdTunnels> selectTunnels(@Param("deptId") String deptId);

    List<SdTunnels> selectSiteTunnelList(@Param("deptId")String deptId, @Param("deptName")String deptName);

    List<SdTunnels> getChildCodeList(String deptCode);

    List<SdTunnels>  deptIdList( String[] ids);
}
