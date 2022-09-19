package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdDeviceCmd;

import java.util.List;

/**
 * 设备指令Service接口
 *
 * @author zhangweitian
 * @date 2020-09-04
 */
public interface ISdDeviceCmdService {
    /**
     * 查询设备指令
     *
     * @param codeId 设备指令ID
     * @return 设备指令
     */
    SdDeviceCmd selectSdDeviceCmdById(Long codeId);

    /**
     * 查询设备指令列表
     *
     * @param sdDeviceCmd 设备指令
     * @return 设备指令集合
     */
    List<SdDeviceCmd> selectSdDeviceCmdList(SdDeviceCmd sdDeviceCmd);

    /**
     * 新增设备指令
     *
     * @param sdDeviceCmd 设备指令
     * @return 结果
     */
    int insertSdDeviceCmd(SdDeviceCmd sdDeviceCmd);

    /**
     * 修改设备指令
     *
     * @param sdDeviceCmd 设备指令
     * @return 结果
     */
    int updateSdDeviceCmd(SdDeviceCmd sdDeviceCmd);

    /**
     * 批量删除设备指令
     *
     * @param codeIds 需要删除的设备指令ID
     * @return 结果
     */
    int deleteSdDeviceCmdByIds(Long[] codeIds);

    /**
     * 删除设备指令信息
     *
     * @param codeId 设备指令ID
     * @return 结果
     */
    int deleteSdDeviceCmdById(Long codeId);

    /**
     * 查询设备报文信息
     *
     * @param devId
     * @param codeDeviceState
     * @return
     */
    SdDeviceCmd selectSdDeviceCmd(String devId, String codeDeviceState);

}