package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdCmdRl;

import java.util.List;

/**
 * plc 报文关联设备Service接口
 *
 * @author zhangweitian
 * @date 2020-09-02
 */
public interface ISdCmdRlService {
    /**
     * 查询plc 报文关联设备
     *
     * @param cmdRlId plc 报文关联设备ID
     * @return plc 报文关联设备
     */
    SdCmdRl selectSdCmdRlById(Long cmdRlId);

    /**
     * 查询plc 报文关联设备列表
     *
     * @param sdCmdRl plc 报文关联设备
     * @return plc 报文关联设备集合
     */
    List<SdCmdRl> selectSdCmdRlList(SdCmdRl sdCmdRl);

    /**
     * 新增plc 报文关联设备
     *
     * @param sdCmdRl plc 报文关联设备
     * @return 结果
     */
    int insertSdCmdRl(SdCmdRl sdCmdRl);

    /**
     * 修改plc 报文关联设备
     *
     * @param sdCmdRl plc 报文关联设备
     * @return 结果
     */
    int updateSdCmdRl(SdCmdRl sdCmdRl);

    /**
     * 批量删除plc 报文关联设备
     *
     * @param cmdRlIds 需要删除的plc 报文关联设备ID
     * @return 结果
     */
    int deleteSdCmdRlByIds(Long[] cmdRlIds);

    /**
     * 删除plc 报文关联设备信息
     *
     * @param cmdRlId plc 报文关联设备ID
     * @return 结果
     */
    int deleteSdCmdRlById(Long cmdRlId);
}