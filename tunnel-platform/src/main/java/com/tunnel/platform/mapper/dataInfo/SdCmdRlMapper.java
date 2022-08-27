package com.tunnel.platform.mapper.dataInfo;


import com.tunnel.platform.domain.dataInfo.SdCmdRl;

import java.util.List;

/**
 * plc 报文关联设备Mapper接口
 * 
 * @author zhangweitian
 * @date 2020-09-02
 */
public interface SdCmdRlMapper 
{
    /**
     * 查询plc 报文关联设备
     * 
     * @param cmdRlId plc 报文关联设备ID
     * @return plc 报文关联设备
     */
    public SdCmdRl selectSdCmdRlById(Long cmdRlId);

    /**
     * 查询plc 报文关联设备列表
     * 
     * @param sdCmdRl plc 报文关联设备
     * @return plc 报文关联设备集合
     */
    public List<SdCmdRl> selectSdCmdRlList(SdCmdRl sdCmdRl);

    /**
     * 新增plc 报文关联设备
     * 
     * @param sdCmdRl plc 报文关联设备
     * @return 结果
     */
    public int insertSdCmdRl(SdCmdRl sdCmdRl);

    /**
     * 修改plc 报文关联设备
     * 
     * @param sdCmdRl plc 报文关联设备
     * @return 结果
     */
    public int updateSdCmdRl(SdCmdRl sdCmdRl);

    /**
     * 删除plc 报文关联设备
     * 
     * @param cmdRlId plc 报文关联设备ID
     * @return 结果
     */
    public int deleteSdCmdRlById(Long cmdRlId);

    /**
     * 批量删除plc 报文关联设备
     * 
     * @param cmdRlIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdCmdRlByIds(Long[] cmdRlIds);
}