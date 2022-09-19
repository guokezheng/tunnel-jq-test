package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.SdDeviceCmd;
import com.tunnel.business.mapper.dataInfo.SdDeviceCmdMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceCmdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备指令Service业务层处理
 *
 * @author zhangweitian
 * @date 2020-09-04
 */
@Service
public class SdDeviceCmdServiceImpl implements ISdDeviceCmdService {
    @Autowired
    private SdDeviceCmdMapper sdDeviceCmdMapper;

    /**
     * 查询设备指令
     *
     * @param codeId 设备指令ID
     * @return 设备指令
     */
    @Override
    public SdDeviceCmd selectSdDeviceCmdById(Long codeId) {
        return sdDeviceCmdMapper.selectSdDeviceCmdById(codeId);
    }

    /**
     * 查询设备指令列表
     *
     * @param sdDeviceCmd 设备指令
     * @return 设备指令
     */
    @Override
    public List<SdDeviceCmd> selectSdDeviceCmdList(SdDeviceCmd sdDeviceCmd) {
        return sdDeviceCmdMapper.selectDropSdDeviceCmdList(sdDeviceCmd);
    }

    /**
     * 新增设备指令
     *
     * @param sdDeviceCmd 设备指令
     * @return 结果
     */
    @Override
    public int insertSdDeviceCmd(SdDeviceCmd sdDeviceCmd) {
        sdDeviceCmd.setCreateTime(DateUtils.getNowDate());
        return sdDeviceCmdMapper.insertSdDeviceCmd(sdDeviceCmd);
    }

    /**
     * 修改设备指令
     *
     * @param sdDeviceCmd 设备指令
     * @return 结果
     */
    @Override
    public int updateSdDeviceCmd(SdDeviceCmd sdDeviceCmd) {
        sdDeviceCmd.setUpdateTime(DateUtils.getNowDate());
        return sdDeviceCmdMapper.updateSdDeviceCmd(sdDeviceCmd);
    }

    /**
     * 批量删除设备指令
     *
     * @param codeIds 需要删除的设备指令ID
     * @return 结果
     */
    @Override
    public int deleteSdDeviceCmdByIds(Long[] codeIds) {
        return sdDeviceCmdMapper.deleteSdDeviceCmdByIds(codeIds);
    }

    /**
     * 删除设备指令信息
     *
     * @param codeId 设备指令ID
     * @return 结果
     */
    @Override
    public int deleteSdDeviceCmdById(Long codeId) {
        return sdDeviceCmdMapper.deleteSdDeviceCmdById(codeId);
    }


    /**
     * 通过设备状态以及设备id查询设备指令
     */
    @Override
    public SdDeviceCmd selectSdDeviceCmd(String devId, String codeDeviceState) {
        System.out.println("devId==" + devId + "===codeDeviceState" + codeDeviceState);
        return sdDeviceCmdMapper.selectSdDeviceCmd(devId, codeDeviceState);
    }

}