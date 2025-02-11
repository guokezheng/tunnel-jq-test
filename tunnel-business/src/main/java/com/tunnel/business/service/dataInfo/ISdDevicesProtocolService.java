package com.tunnel.business.service.dataInfo;

import com.tunnel.business.domain.dataInfo.SdDevicesProtocol;

import java.util.List;

/**
 * 设备协议Service接口
 *
 * @author ruoyi
 * @date 2022-12-07
 */
public interface ISdDevicesProtocolService {
    /**
     * 查询设备协议
     *
     * @param id 设备协议主键
     * @return 设备协议
     */
    public SdDevicesProtocol selectSdDevicesProtocolById(Long id);

    /**
     * 查询设备协议列表
     *
     * @param sdDevicesProtocol 设备协议
     * @return 设备协议集合
     */
    public List<SdDevicesProtocol> selectSdDevicesProtocolList(SdDevicesProtocol sdDevicesProtocol);

    /**
     * 新增设备协议
     *
     * @param sdDevicesProtocol 设备协议
     * @return 结果
     */
    public int insertSdDevicesProtocol(SdDevicesProtocol sdDevicesProtocol);

    /**
     * 修改设备协议
     *
     * @param sdDevicesProtocol 设备协议
     * @return 结果
     */
    public int updateSdDevicesProtocol(SdDevicesProtocol sdDevicesProtocol);

    /**
     * 批量删除设备协议
     *
     * @param ids 需要删除的设备协议主键集合
     * @return 结果
     */
    public int deleteSdDevicesProtocolByIds(Long[] ids);

    /**
     * 删除设备协议信息
     *
     * @param id 设备协议主键
     * @return 结果
     */
    public int deleteSdDevicesProtocolById(Long id);

    /**
     * 修改时校验唯一
     *
     * @param sdDevicesProtocol 设备协议
     * @return 结果
     */
    boolean checkUniqueForUpdate(SdDevicesProtocol sdDevicesProtocol);

    /**
     * 根据协议编码查询对应的协议ID
     * @param protocolCode 协议编码
     * @return 协议ID
     */
    Long selectProtocolIdByCode(String protocolCode);
}
