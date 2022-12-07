package com.tunnel.business.mapper.dataInfo;

import com.tunnel.business.domain.dataInfo.SdDevicesProtocol;

import java.util.List;

/**
 * 设备协议Mapper接口
 *
 * @author ruoyi
 * @date 2022-12-07
 */
public interface SdDevicesProtocolMapper {
    /**
     * 查询设备协议
     *
     * @param id 设备协议主键
     * @return 设备协议
     */
    public SdDevicesProtocol selectSdDevicesProtocolById(Integer id);

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
     * 删除设备协议
     *
     * @param id 设备协议主键
     * @return 结果
     */
    public int deleteSdDevicesProtocolById(Integer id);

    /**
     * 批量删除设备协议
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdDevicesProtocolByIds(Integer[] ids);

    List<SdDevicesProtocol> checkUniqueForUpdate(SdDevicesProtocol sdDevicesProtocol);

}
