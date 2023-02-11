package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 隧道Service业务层处理
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
@Service
public class SdTunnelsServiceImpl implements ISdTunnelsService {
    @Autowired
    private SdTunnelsMapper sdTunnelsMapper;

    /**
     * 查询隧道
     *
     * @param tunnelId 隧道ID
     * @return 隧道
     */
    @Override
    public SdTunnels selectSdTunnelsById(String tunnelId) {
        return sdTunnelsMapper.selectSdTunnelsById(tunnelId);
    }

    /**
     * 查询隧道列表
     *
     * @param sdTunnels 隧道
     * @return 隧道
     */
    @Override
    public List<SdTunnels> selectSdTunnelsList(SdTunnels sdTunnels) {
        return sdTunnelsMapper.selectSdTunnelsList(sdTunnels);
    }

    /**
     * 新增隧道
     *
     * @param sdTunnels 隧道
     * @return 结果
     */
    @Override
    public int insertSdTunnels(SdTunnels sdTunnels) {
        SdTunnels tunnelsById = sdTunnelsMapper.selectSdTunnelsById(sdTunnels.getTunnelId());
        if (tunnelsById != null) {
            throw new RuntimeException("当前隧道ID已经存在，请核对后重试！");
        }
        SdTunnels onlyTunnelName = new SdTunnels();
        onlyTunnelName.setTunnelName(sdTunnels.getTunnelName());
        List<SdTunnels> tunnels = sdTunnelsMapper.verifyTunnelOnly(onlyTunnelName);
        if (tunnels.size() > 0) {
            throw new RuntimeException("当前隧道名称已经存在，请核对后重试！");
        }
//        SdTunnels onlyDeptId = new SdTunnels();
//        onlyDeptId.setDeptId(sdTunnels.getDeptId());
//        tunnels = sdTunnelsMapper.verifyTunnelOnly(onlyDeptId);
//        if (tunnels.size() > 0) {
//            throw new RuntimeException("当前部门已添加隧道！");
//        }
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
    public int updateSdTunnels(SdTunnels sdTunnels) {
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
    public int deleteSdTunnelsByIds(String[] tunnelIds) {
        return sdTunnelsMapper.deleteSdTunnelsByIds(tunnelIds);
    }

    /**
     * 删除隧道信息
     *
     * @param tunnelId 隧道ID
     * @return 结果
     */
    @Override
    public int deleteSdTunnelsById(String tunnelId) {
        return sdTunnelsMapper.deleteSdTunnelsById(tunnelId);
    }

    /**
     * 查询隧道分区
     *
     * @param sdTunnels
     * @return
     */
    @Override
    public List<SdTunnels> selectSdTunnelsSubList(SdTunnels sdTunnels) {
        return sdTunnelsMapper.selectSdTunnelsSubList(sdTunnels);
    }

    @Override
    public List<SdTunnels> deptId(Long deptId) {
        return sdTunnelsMapper.deptId(deptId);
    }

    /**
     * 获取所有隧道的Map格式
     * key: 隧道ID tunnelId,value:隧道名称tunnelName
     *
     * @return
     */
    @Override
    public Map<String, String> getTunnelNameMap() {
        List<Map<String,String>> list = sdTunnelsMapper.getTunnelList();
        Map<String,String> map = list.stream().collect(Collectors.toMap(s-> s.get("tunnelId"),s -> s.get("tunnelName") ));
        return map;
    }

    /**
     * 查询隧道list根据权限
     * @param deptId
     * @return
     */
    @Override
    public List<SdTunnels> selectTunnelsList(String deptId) {
        return sdTunnelsMapper.selectTunnelsList(deptId);
    }

    /**
     * 查询隧道列表
     * @param deptId
     * @return
     */
    @Override
    public List<SdTunnels> selectTunnelList(String deptId,String tunnelId) {
        return sdTunnelsMapper.selectTunnelList(deptId,tunnelId);
    }

    @Override
    public List<SdTunnels> getJlyTunnel() {
        String deptId = SecurityUtils.getDeptId();
        List<SdTunnels> jlyTunnel = sdTunnelsMapper.getJlyTunnel(deptId);
        return jlyTunnel;
    }



    /**
     * 查询隧道列表
     * @return 隧道
     */
    @Override
    public List<SdTunnels> selectAllSdTunnelsList() {
        return sdTunnelsMapper.selectAllSdTunnelsList();
    }

    /**
     * 查询所有的隧道列表
     * @param sdTunnels
     * @return
     */
    @Override
    public List<SdTunnels> selectSdTunnelsList1(SdTunnels sdTunnels) {
        return sdTunnelsMapper.selectSdTunnelsList1(sdTunnels);
    }

    /**
     * 外部系统获取隧道下拉
     * @return
     */
    @Override
    public List<SdTunnels> selectAllSdTunnelsList1() {
        return sdTunnelsMapper.selectAllSdTunnelsList1();
    }

    /**
     * 查询隧道所属部门
     * @param tunnelId
     * @return
     */
    @Override
    public String selectTunnelDept(String tunnelId) {
        return sdTunnelsMapper.selectTunnelDept(tunnelId);
    }
}
