package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.SdDevicesProtocol;
import com.tunnel.business.mapper.dataInfo.SdDevicesProtocolMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesProtocolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 设备协议Service业务层处理
 *
 * @author ruoyi
 * @date 2022-12-07
 */
@Service
public class SdDevicesProtocolServiceImpl implements ISdDevicesProtocolService {
    @Autowired
    private SdDevicesProtocolMapper sdDevicesProtocolMapper;

    /**
     * 查询设备协议
     *
     * @param id 设备协议主键
     * @return 设备协议
     */
    @Override
    public SdDevicesProtocol selectSdDevicesProtocolById(Integer id) {
        return sdDevicesProtocolMapper.selectSdDevicesProtocolById(id);
    }

    /**
     * 查询设备协议列表
     *
     * @param sdDevicesProtocol 设备协议
     * @return 设备协议
     */
    @Override
    public List<SdDevicesProtocol> selectSdDevicesProtocolList(SdDevicesProtocol sdDevicesProtocol) {
        return sdDevicesProtocolMapper.selectSdDevicesProtocolList(sdDevicesProtocol);
    }

    /**
     * 新增设备协议
     *
     * @param sdDevicesProtocol 设备协议
     * @return 结果
     */
    @Override
    public int insertSdDevicesProtocol(SdDevicesProtocol sdDevicesProtocol) {
        SdDevicesProtocol protocol = new SdDevicesProtocol();
        protocol.setBrandId(sdDevicesProtocol.getBrandId());
        protocol.setEqType(sdDevicesProtocol.getEqType());
        List<SdDevicesProtocol> list = sdDevicesProtocolMapper.selectSdDevicesProtocolList(protocol);
        Assert.notEmpty(list,"已存在相同设备品牌和类型的数据，请确认");

        sdDevicesProtocol.setCreateTime(DateUtils.getNowDate());
        sdDevicesProtocol.setIsDel(0);
        return sdDevicesProtocolMapper.insertSdDevicesProtocol(sdDevicesProtocol);
    }

    /**
     * 修改设备协议
     *
     * @param sdDevicesProtocol 设备协议
     * @return 结果
     */
    @Override
    public int updateSdDevicesProtocol(SdDevicesProtocol sdDevicesProtocol) {
        if (!checkUniqueForUpdate(sdDevicesProtocol)) {
            throw new RuntimeException("已存在相同设备品牌和类型的数据，请确认！");
        }

        sdDevicesProtocol.setUpdateTime(DateUtils.getNowDate());
        return sdDevicesProtocolMapper.updateSdDevicesProtocol(sdDevicesProtocol);
    }

    /**
     * 批量删除设备协议
     *
     * @param ids 需要删除的设备协议主键
     * @return 结果
     */
    @Override
    public int deleteSdDevicesProtocolByIds(Integer[] ids) {
        return sdDevicesProtocolMapper.deleteSdDevicesProtocolByIds(ids);
    }

    /**
     * 删除设备协议信息
     *
     * @param id 设备协议主键
     * @return 结果
     */
    @Override
    public int deleteSdDevicesProtocolById(Integer id) {
        return sdDevicesProtocolMapper.deleteSdDevicesProtocolById(id);
    }

    @Override
    public boolean checkUniqueForUpdate(SdDevicesProtocol sdDevicesProtocol) {
        List<SdDevicesProtocol> list = sdDevicesProtocolMapper.checkUniqueForUpdate(sdDevicesProtocol);
        return CollectionUtils.isEmpty(list) ? true : false;
    }
}
