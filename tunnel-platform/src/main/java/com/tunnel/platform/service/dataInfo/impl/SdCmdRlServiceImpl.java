package com.tunnel.platform.service.dataInfo.impl;

import com.tunnel.platform.domain.dataInfo.SdCmdRl;
import com.tunnel.platform.mapper.dataInfo.SdCmdRlMapper;
import com.tunnel.platform.service.dataInfo.ISdCmdRlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * plc 报文关联设备Service业务层处理
 * 
 * @author zhangweitian
 * @date 2020-09-02
 */
@Service
public class SdCmdRlServiceImpl implements ISdCmdRlService
{
    @Autowired
    private SdCmdRlMapper sdCmdRlMapper;

    /**
     * 查询plc 报文关联设备
     * 
     * @param cmdRlId plc 报文关联设备ID
     * @return plc 报文关联设备
     */
    @Override
    public SdCmdRl selectSdCmdRlById(Long cmdRlId)
    {
        return sdCmdRlMapper.selectSdCmdRlById(cmdRlId);
    }

    /**
     * 查询plc 报文关联设备列表
     * 
     * @param sdCmdRl plc 报文关联设备
     * @return plc 报文关联设备
     */
    @Override
    public List<SdCmdRl> selectSdCmdRlList(SdCmdRl sdCmdRl)
    {
        return sdCmdRlMapper.selectSdCmdRlList(sdCmdRl);
    }

    /**
     * 新增plc 报文关联设备
     * 
     * @param sdCmdRl plc 报文关联设备
     * @return 结果
     */
    @Override
    public int insertSdCmdRl(SdCmdRl sdCmdRl)
    {
        return sdCmdRlMapper.insertSdCmdRl(sdCmdRl);
    }

    /**
     * 修改plc 报文关联设备
     * 
     * @param sdCmdRl plc 报文关联设备
     * @return 结果
     */
    @Override
    public int updateSdCmdRl(SdCmdRl sdCmdRl)
    {
        return sdCmdRlMapper.updateSdCmdRl(sdCmdRl);
    }

    /**
     * 批量删除plc 报文关联设备
     * 
     * @param cmdRlIds 需要删除的plc 报文关联设备ID
     * @return 结果
     */
    @Override
    public int deleteSdCmdRlByIds(Long[] cmdRlIds)
    {
        return sdCmdRlMapper.deleteSdCmdRlByIds(cmdRlIds);
    }

    /**
     * 删除plc 报文关联设备信息
     * 
     * @param cmdRlId plc 报文关联设备ID
     * @return 结果
     */
    @Override
    public int deleteSdCmdRlById(Long cmdRlId)
    {
        return sdCmdRlMapper.deleteSdCmdRlById(cmdRlId);
    }
}