package com.tunnel.business.service.informationBoard.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.informationBoard.IotDeviceProtocol;
import com.tunnel.business.mapper.informationBoard.IotDeviceProtocolMapper;
import com.tunnel.business.service.informationBoard.IIotDeviceProtocolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通信协议Service业务层处理
 *
 * @author wangyaozong
 * @date 2020-03-28
 */
@Service
public class IotDeviceProtocolServiceImpl implements IIotDeviceProtocolService {
    @Autowired
    private IotDeviceProtocolMapper iotDeviceProtocolMapper;

    /**
     * 查询通信协议
     *
     * @param protocolId 通信协议ID
     * @return 通信协议
     */
    @Override
    public IotDeviceProtocol selectIotDeviceProtocolById(Long protocolId) {
        return iotDeviceProtocolMapper.selectIotDeviceProtocolById(protocolId);
    }

    /**
     * 查询通信协议列表
     *
     * @param iotDeviceProtocol 通信协议
     * @return 通信协议
     */
    @Override
    public List<IotDeviceProtocol> selectIotDeviceProtocolList(IotDeviceProtocol iotDeviceProtocol) {
        return iotDeviceProtocolMapper.selectIotDeviceProtocolList(iotDeviceProtocol);
    }

    /**
     * 查询通信协议函数
     *
     * @param deviceProtocol 通信协议
     * @return 通信协议
     */
    @Override
    public List<IotDeviceProtocol> getProtocolFunctionByName(String deviceProtocol) {
        return iotDeviceProtocolMapper.getProtocolFunctionByName(deviceProtocol);
    }

    /**
     * 新增通信协议
     *
     * @param iotDeviceProtocol 通信协议
     * @return 结果
     */
    @Override
    public int insertIotDeviceProtocol(IotDeviceProtocol iotDeviceProtocol) {
        iotDeviceProtocol.setCreateTime(DateUtils.getNowDate());
        return iotDeviceProtocolMapper.insertIotDeviceProtocol(iotDeviceProtocol);
    }

    /**
     * 修改通信协议
     *
     * @param iotDeviceProtocol 通信协议
     * @return 结果
     */
    @Override
    public int updateIotDeviceProtocol(IotDeviceProtocol iotDeviceProtocol) {
        iotDeviceProtocol.setUpdateTime(DateUtils.getNowDate());
        return iotDeviceProtocolMapper.updateIotDeviceProtocol(iotDeviceProtocol);
    }

    /**
     * 删除通信协议对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteIotDeviceProtocolByIds(String ids) {
        return iotDeviceProtocolMapper.deleteIotDeviceProtocolByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除通信协议信息
     *
     * @param protocolId 通信协议ID
     * @return 结果
     */
    @Override
    public int deleteIotDeviceProtocolById(Long protocolId) {
        return iotDeviceProtocolMapper.deleteIotDeviceProtocolById(protocolId);
    }
}
