package com.tunnel.business.service.protocol.impl;

import java.util.List;
import java.util.Map;

import com.tunnel.business.domain.protocol.SdDevicePoint;
import com.tunnel.business.mapper.protocol.SdDevicePointMapper;
import com.tunnel.business.service.protocol.ISdDevicePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备点位状态详情Service业务层处理
 *
 * @author ruoyi
 * @date 2023-02-27
 */
@Service
public class SdDevicePointServiceImpl implements ISdDevicePointService
{
    @Autowired
    private SdDevicePointMapper sdDevicePointMapper;

    /**
     * 查询设备点位状态详情
     *
     * @param id 设备点位状态详情主键
     * @return 设备点位状态详情
     */
    @Override
    public SdDevicePoint selectSdDevicePointById(Long id)
    {
        return sdDevicePointMapper.selectSdDevicePointById(id);
    }

    /**
     * 查询设备点位状态详情列表
     *
     * @param sdDevicePoint 设备点位状态详情
     * @return 设备点位状态详情
     */
    @Override
    public List<SdDevicePoint> selectSdDevicePointList(SdDevicePoint sdDevicePoint)
    {
        return sdDevicePointMapper.selectSdDevicePointList(sdDevicePoint);
    }

    /**
     * 根据设备ID列表查询设备点位
     *
     * @param list      设备ID列表
     * @param pointType 点位类型
     * @return
     */
    @Override
    public List<SdDevicePoint> selectDevicePointByList(List<String> list, String pointType) {
        return sdDevicePointMapper.selectDevicePointByList(list,pointType);
    }

    /**
     * 根据父设备ID、点位类型筛选最小点位、最大点位
     *
     * @param list      父设备ID列表
     * @param codeList 功能码列表
     * @param pointType 点位类型
     * @return
     */
    @Override
    public List<Map> selectDevicePointByGroup(List<String> list,List<String> codeList,String pointType) {
        return sdDevicePointMapper.selectDevicePointByGroup(list,codeList,pointType);
    }

    /**
     * 根据父设备ID、点位类型查询点位信息
     *
     * @param list      父设备ID列表
     * @param codeList 功能码列表
     * @param pointType 点位类型
     * @return
     */
    @Override
    public List<Map> selectPointMapByFEqId(List<String> list,List<String> codeList, String pointType) {
        return sdDevicePointMapper.selectPointMapByFEqId(list,codeList,pointType);
    }

    /**
     * 根据父设备ID、点位类型筛选设备点位
     *
     * @param list      父设备ID列表
     * @param pointType 点位类型
     * @param functionCode 功能码
     * @return
     */
    @Override
    public List<SdDevicePoint> selectDevicePointByFEqId(List<String> list, Long pointType,String functionCode) {
        return sdDevicePointMapper.selectDevicePointByFEqId(list,pointType,functionCode);
    }

    /**
     * 新增设备点位状态详情
     *
     * @param sdDevicePoint 设备点位状态详情
     * @return 结果
     */
    @Override
    public int insertSdDevicePoint(SdDevicePoint sdDevicePoint)
    {
        return sdDevicePointMapper.insertSdDevicePoint(sdDevicePoint);
    }

    /**
     * 修改设备点位状态详情
     *
     * @param sdDevicePoint 设备点位状态详情
     * @return 结果
     */
    @Override
    public int updateSdDevicePoint(SdDevicePoint sdDevicePoint)
    {
        return sdDevicePointMapper.updateSdDevicePoint(sdDevicePoint);
    }

    /**
     * 批量删除设备点位状态详情
     *
     * @param ids 需要删除的设备点位状态详情主键
     * @return 结果
     */
    @Override
    public int deleteSdDevicePointByIds(Long[] ids)
    {
        return sdDevicePointMapper.deleteSdDevicePointByIds(ids);
    }

    /**
     * 删除设备点位状态详情信息
     *
     * @param id 设备点位状态详情主键
     * @return 结果
     */
    @Override
    public int deleteSdDevicePointById(Long id)
    {
        return sdDevicePointMapper.deleteSdDevicePointById(id);
    }
}
