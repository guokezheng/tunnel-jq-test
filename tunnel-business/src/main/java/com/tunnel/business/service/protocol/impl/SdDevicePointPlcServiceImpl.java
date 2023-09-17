package com.tunnel.business.service.protocol.impl;


import com.tunnel.business.domain.protocol.SdDevicePointPlc;
import com.tunnel.business.mapper.protocol.SdDevicePointPlcMapper;
import com.tunnel.business.service.protocol.ISdDevicePointPlcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * PLC设备点位(区别测控执行器)Service业务层处理
 *
 * @author ruoyi
 * @date 2023-09-16
 */
@Service
public class SdDevicePointPlcServiceImpl implements ISdDevicePointPlcService
{
    @Autowired
    private SdDevicePointPlcMapper sdDevicePointPlcMapper;

    /**
     * 查询PLC设备点位(区别测控执行器)
     *
     * @param id PLC设备点位(区别测控执行器)主键
     * @return PLC设备点位(区别测控执行器)
     */
    @Override
    public SdDevicePointPlc selectSdDevicePointPlcById(Long id)
    {
        return sdDevicePointPlcMapper.selectSdDevicePointPlcById(id);
    }

    /**
     * 查询PLC设备点位(区别测控执行器)列表
     *
     * @param sdDevicePointPlc PLC设备点位(区别测控执行器)
     * @return PLC设备点位(区别测控执行器)
     */
    @Override
    public List<SdDevicePointPlc> selectSdDevicePointPlcList(SdDevicePointPlc sdDevicePointPlc)
    {
        return sdDevicePointPlcMapper.selectSdDevicePointPlcList(sdDevicePointPlc);
    }

    /**
     * 新增PLC设备点位(区别测控执行器)
     *
     * @param sdDevicePointPlc PLC设备点位(区别测控执行器)
     * @return 结果
     */
    @Override
    public int insertSdDevicePointPlc(SdDevicePointPlc sdDevicePointPlc)
    {
        return sdDevicePointPlcMapper.insertSdDevicePointPlc(sdDevicePointPlc);
    }

    /**
     * 修改PLC设备点位(区别测控执行器)
     *
     * @param sdDevicePointPlc PLC设备点位(区别测控执行器)
     * @return 结果
     */
    @Override
    public int updateSdDevicePointPlc(SdDevicePointPlc sdDevicePointPlc)
    {
        return sdDevicePointPlcMapper.updateSdDevicePointPlc(sdDevicePointPlc);
    }

    /**
     * 批量删除PLC设备点位(区别测控执行器)
     *
     * @param ids 需要删除的PLC设备点位(区别测控执行器)主键
     * @return 结果
     */
    @Override
    public int deleteSdDevicePointPlcByIds(Long[] ids)
    {
        return sdDevicePointPlcMapper.deleteSdDevicePointPlcByIds(ids);
    }

    /**
     * 删除PLC设备点位(区别测控执行器)信息
     *
     * @param id PLC设备点位(区别测控执行器)主键
     * @return 结果
     */
    @Override
    public int deleteSdDevicePointPlcById(Long id)
    {
        return sdDevicePointPlcMapper.deleteSdDevicePointPlcById(id);
    }

    /**
     * 根据父设备ID、点位类型筛选最小点位、最大点位
     *
     * @param list      父设备ID列表
     * @param codeList  功能码列表
     * @param pointType 点位类型
     * @return
     */
    @Override
    public List<Map> selectDevicePointByGroup(List<String> list, List<String> codeList, String pointType) {
        return sdDevicePointPlcMapper.selectDevicePointByGroup(list,codeList,pointType);
    }

    /**
     * 根据父设备ID、点位类型筛选最小点位、最大点位
     *
     * @param list      父设备ID列表
     * @param codeList  功能码列表
     * @param pointType 点位类型
     * @return
     */
    @Override
    public List<Map> selectDevicePointByGroupNum(List<String> list, List<String> codeList, String pointType) {
        return sdDevicePointPlcMapper.selectDevicePointByGroupNum(list,codeList,pointType);
    }

    /**
     * 根据父设备ID、点位类型查询点位信息
     *
     * @param list      父设备ID列表
     * @param codeList  功能码列表
     * @param pointType 点位类型
     * @return
     */
    @Override
    public List<Map> selectPointMapByFEqId(List<String> list, List<String> codeList, String pointType) {
        return sdDevicePointPlcMapper.selectPointMapByFEqId(list,codeList,pointType);
    }

    /**
     * 根据父设备ID、点位类型筛选设备点位
     *
     * @param list         父设备ID列表
     * @param pointType    点位类型
     * @param functionCode 功能码
     * @return
     */
    @Override
    public List<SdDevicePointPlc> selectDevicePointByFEqId(List<String> list, Long pointType, String functionCode) {
        return sdDevicePointPlcMapper.selectDevicePointByFEqId(list,pointType,functionCode);
    }
}
