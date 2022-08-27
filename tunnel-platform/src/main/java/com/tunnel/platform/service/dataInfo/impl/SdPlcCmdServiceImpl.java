package com.tunnel.platform.service.dataInfo.impl;


import com.tunnel.platform.domain.dataInfo.SdPlcCmd;
import com.tunnel.platform.mapper.dataInfo.SdPlcCmdMapper;
import com.tunnel.platform.service.dataInfo.ISdPlcCmdService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * plc 报文Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-12-01
 */
@Service
public class SdPlcCmdServiceImpl implements ISdPlcCmdService
{
    @Autowired
    private SdPlcCmdMapper sdPlcCmdMapper;

    /**
     * 查询plc 报文
     * 
     * @param cmdId plc 报文ID
     * @return plc 报文
     */
    @Override
    public SdPlcCmd selectSdPlcCmdById(Long cmdId)
    {
        return sdPlcCmdMapper.selectSdPlcCmdById(cmdId);
    }

    /**
     * 查询plc 报文列表
     * 
     * @param sdPlcCmd plc 报文
     * @return plc 报文
     */
    @Override
    public List<SdPlcCmd> selectSdPlcCmdList(SdPlcCmd sdPlcCmd)
    {
        return sdPlcCmdMapper.selectSdPlcCmdList(sdPlcCmd);
    }

    /**
     * 新增plc 报文
     * 
     * @param sdPlcCmd plc 报文
     * @return 结果
     */
    @Override
    public int insertSdPlcCmd(SdPlcCmd sdPlcCmd)
    {
        sdPlcCmd.setCreateTime(DateUtils.getNowDate());
        return sdPlcCmdMapper.insertSdPlcCmd(sdPlcCmd);
    }

    /**
     * 修改plc 报文
     * 
     * @param sdPlcCmd plc 报文
     * @return 结果
     */
    @Override
    public int updateSdPlcCmd(SdPlcCmd sdPlcCmd)
    {
        sdPlcCmd.setUpdateTime(DateUtils.getNowDate());
        return sdPlcCmdMapper.updateSdPlcCmd(sdPlcCmd);
    }

    /**
     * 批量删除plc 报文
     * 
     * @param cmdIds 需要删除的plc 报文ID
     * @return 结果
     */
    @Override
    public int deleteSdPlcCmdByIds(Long[] cmdIds)
    {
        return sdPlcCmdMapper.deleteSdPlcCmdByIds(cmdIds);
    }

    /**
     * 删除plc 报文信息
     * 
     * @param cmdId plc 报文ID
     * @return 结果
     */
    @Override
    public int deleteSdPlcCmdById(Long cmdId)
    {
        return sdPlcCmdMapper.deleteSdPlcCmdById(cmdId);
    }
}
