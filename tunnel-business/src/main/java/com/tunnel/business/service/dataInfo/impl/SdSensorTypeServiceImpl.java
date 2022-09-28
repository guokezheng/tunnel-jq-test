package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.SdSensorType;
import com.tunnel.business.mapper.dataInfo.SdSensorTypeMapper;
import com.tunnel.business.service.dataInfo.ISdSensorTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 传感器类型Service业务层处理
 *
 * @author yanghousheng
 * @date 2020-11-10
 */
@Service
public class SdSensorTypeServiceImpl implements ISdSensorTypeService {
    @Autowired
    private SdSensorTypeMapper sdSensorTypeMapper;

    /**
     * 查询传感器类型
     *
     * @param id 传感器类型ID
     * @return 传感器类型
     */
    @Override
    public SdSensorType selectSdSensorTypeById(Long id) {
        return sdSensorTypeMapper.selectSdSensorTypeById(id);
    }

    /**
     * 查询传感器类型列表
     *
     * @param sdSensorType 传感器类型
     * @return 传感器类型
     */
    @Override
    public List<SdSensorType> selectSdSensorTypeList(SdSensorType sdSensorType) {
        return sdSensorTypeMapper.selectSdSensorTypeList(sdSensorType);
    }

    /**
     * 新增传感器类型
     *
     * @param sdSensorType 传感器类型
     * @return 结果
     */
    @Override
    public int insertSdSensorType(SdSensorType sdSensorType) {
        sdSensorType.setCreateTime(DateUtils.getNowDate());
        return sdSensorTypeMapper.insertSdSensorType(sdSensorType);
    }

    /**
     * 修改传感器类型
     *
     * @param sdSensorType 传感器类型
     * @return 结果
     */
    @Override
    public int updateSdSensorType(SdSensorType sdSensorType) {
        sdSensorType.setUpdateTime(DateUtils.getNowDate());
        return sdSensorTypeMapper.updateSdSensorType(sdSensorType);
    }

    /**
     * 批量删除传感器类型
     *
     * @param ids 需要删除的传感器类型ID
     * @return 结果
     */
    @Override
    public int deleteSdSensorTypeByIds(Long[] ids) {
        return sdSensorTypeMapper.deleteSdSensorTypeByIds(ids);
    }

    /**
     * 删除传感器类型信息
     *
     * @param id 传感器类型ID
     * @return 结果
     */
    @Override
    public int deleteSdSensorTypeById(Long id) {
        return sdSensorTypeMapper.deleteSdSensorTypeById(id);
    }
}
