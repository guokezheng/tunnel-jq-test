package com.tunnel.platform.service.dataInfo.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.platform.domain.dataInfo.SdSensor;
import com.tunnel.platform.mapper.dataInfo.SdSensorMapper;
import com.tunnel.platform.service.dataInfo.ISdSensorService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 传感器列Service业务层处理
 *
 * @author yanghousheng
 * @date 2020-11-09
 */
@Service
public class SdSensorServiceImpl implements ISdSensorService
{
    @Autowired
    private SdSensorMapper sdSensorMapper;

    /**
     * 查询传感器列
     *
     * @param id 传感器列ID
     * @return 传感器列
     */
    @Override
    public SdSensor selectSdSensorById(Long id)
    {
        return sdSensorMapper.selectSdSensorById(id);
    }

    /**
     * 查询传感器列列表
     *
     * @param sdSensor 传感器列
     * @return 传感器列
     */
    @Override
    public List<SdSensor> selectSdSensorList(SdSensor sdSensor)
    {
        Long deptId = SecurityUtils.getDeptId();
        sdSensor.getParams().put("deptId",deptId);
        return sdSensorMapper.selectSdSensorList(sdSensor);
    }

    /**
     * 新增传感器列
     *
     * @param sdSensor 传感器列
     * @return 结果
     */
    @Override
    public int insertSdSensor(SdSensor sdSensor)
    {
        sdSensor.setCreateTime(DateUtils.getNowDate());
        return sdSensorMapper.insertSdSensor(sdSensor);
    }

    /**
     * 修改传感器列
     *
     * @param sdSensor 传感器列
     * @return 结果
     */
    @Override
    public int updateSdSensor(SdSensor sdSensor)
    {
        sdSensor.setUpdateTime(DateUtils.getNowDate());
        return sdSensorMapper.updateSdSensor(sdSensor);
    }

    /**
     * 批量删除传感器列
     *
     * @param ids 需要删除的传感器列ID
     * @return 结果
     */
    @Override
    public int deleteSdSensorByIds(Long[] ids)
    {
        return sdSensorMapper.deleteSdSensorByIds(ids);
    }

    /**
     * 删除传感器列信息
     *
     * @param id 传感器列ID
     * @return 结果
     */
    @Override
    public int deleteSdSensorById(Long id)
    {
        return sdSensorMapper.deleteSdSensorById(id);
    }
}
