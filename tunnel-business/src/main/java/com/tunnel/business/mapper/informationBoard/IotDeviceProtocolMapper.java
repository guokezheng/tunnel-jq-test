package com.tunnel.business.mapper.informationBoard;


import com.tunnel.business.domain.informationBoard.IotDeviceProtocol;

import java.util.List;

/**
 * 通信协议Mapper接口
 * 
 * @author wangyaozong
 * @date 2020-03-28
 */
public interface IotDeviceProtocolMapper
{
    /**
     * 查询通信协议
     * 
     * @param protocolId 通信协议ID
     * @return 通信协议
     */
    public IotDeviceProtocol selectIotDeviceProtocolById(Long protocolId);

    /**
     * 查询通信协议列表
     * 
     * @param iotDeviceProtocol 通信协议
     * @return 通信协议集合
     */
    public List<IotDeviceProtocol> selectIotDeviceProtocolList(IotDeviceProtocol iotDeviceProtocol);

    /**
     * 查询通信协议函数
     *
     * @param deviceProtocol 通信协议
     * @return 通信协议集合
     */
    public List<IotDeviceProtocol> getProtocolFunctionByName(String deviceProtocol);

    /**
     * 新增通信协议
     * 
     * @param iotDeviceProtocol 通信协议
     * @return 结果
     */
    public int insertIotDeviceProtocol(IotDeviceProtocol iotDeviceProtocol);

    /**
     * 修改通信协议
     * 
     * @param iotDeviceProtocol 通信协议
     * @return 结果
     */
    public int updateIotDeviceProtocol(IotDeviceProtocol iotDeviceProtocol);

    /**
     * 删除通信协议
     * 
     * @param protocolId 通信协议ID
     * @return 结果
     */
    public int deleteIotDeviceProtocolById(Long protocolId);

    /**
     * 批量删除通信协议
     * 
     * @param protocolIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteIotDeviceProtocolByIds(String[] protocolIds);
}
