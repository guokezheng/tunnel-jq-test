package com.tunnel.business.mapper.dataInfo;

import com.tunnel.business.domain.dataInfo.SdDeviceCmd;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备指令Mapper接口
 * 
 * @author zhangweitian
 * @date 2020-09-04
 */
public interface SdDeviceCmdMapper 
{
    /**
     * 查询设备指令
     * 
     * @param codeId 设备指令ID
     * @return 设备指令
     */
    public SdDeviceCmd selectSdDeviceCmdById(Long codeId);

    /**
     * 查询设备指令列表
     * 
     * @param sdDeviceCmd 设备指令
     * @return 设备指令集合
     */
    public List<SdDeviceCmd> selectSdDeviceCmdList(SdDeviceCmd sdDeviceCmd);
    /**
     * 
     * @param sdDeviceCmd
     * @return
     */
    public List<SdDeviceCmd> selectDropSdDeviceCmdList(SdDeviceCmd sdDeviceCmd);
    
    /**
     * 新增设备指令
     * 
     * @param sdDeviceCmd 设备指令
     * @return 结果
     */
    public int insertSdDeviceCmd(SdDeviceCmd sdDeviceCmd);

    /**
     * 修改设备指令
     * 
     * @param sdDeviceCmd 设备指令
     * @return 结果
     */
    public int updateSdDeviceCmd(SdDeviceCmd sdDeviceCmd);

    /**
     * 删除设备指令
     * 
     * @param codeId 设备指令ID
     * @return 结果
     */
    public int deleteSdDeviceCmdById(Long codeId);

    /**
     * 批量删除设备指令
     * 
     * @param codeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdDeviceCmdByIds(Long[] codeIds);

    /**
     * 通过设备id以及设备状态查询设备指令
     * @param devId
     * @param codeDeviceState
     * @return
     */

    public SdDeviceCmd selectSdDeviceCmd(@Param("devId") String devId, @Param("codeDeviceState")String codeDeviceState);
}