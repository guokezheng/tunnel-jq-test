package com.tunnel.business.service.event;


import com.tunnel.business.domain.event.SdTunnelSubarea;

import java.util.List;

/**
 * 隧道分区Service接口
 *
 * @author ruoyi
 * @date 2022-08-25
 */
public interface ISdTunnelSubareaService {
    /**
     * 查询隧道分区
     *
     * @param sId 隧道分区主键
     * @return 隧道分区
     */
    SdTunnelSubarea selectSdTunnelSubareaBySId(Long sId);

    /**
     * 查询隧道分区列表
     *
     * @param sdTunnelSubarea 隧道分区
     * @return 隧道分区集合
     */
    List<SdTunnelSubarea> selectSdTunnelSubareaList(SdTunnelSubarea sdTunnelSubarea);

    /**
     * 新增隧道分区
     *
     * @param sdTunnelSubarea 隧道分区
     * @return 结果
     */
    int insertSdTunnelSubarea(SdTunnelSubarea sdTunnelSubarea);

    /**
     * 修改隧道分区
     *
     * @param sdTunnelSubarea 隧道分区
     * @return 结果
     */
    int updateSdTunnelSubarea(SdTunnelSubarea sdTunnelSubarea);

    /**
     * 批量删除隧道分区
     *
     * @param sIds 需要删除的隧道分区主键集合
     * @return 结果
     */
    int deleteSdTunnelSubareaBySIds(Long[] sIds);

    /**
     * 删除隧道分区信息
     *
     * @param sId 隧道分区主键
     * @return 结果
     */
    int deleteSdTunnelSubareaBySId(Long sId);

    /**
     * 根据隧道Id查询隧道分区
     *
     * @param tunnelId
     * @return
     */
    List<SdTunnelSubarea> selectSdTunnelSubareaByTunnelId(String tunnelId);

}
