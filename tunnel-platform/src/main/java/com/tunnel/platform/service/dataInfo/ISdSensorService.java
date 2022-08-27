package com.tunnel.platform.service.dataInfo;

import com.tunnel.platform.domain.dataInfo.SdSensor;

import java.util.List;

/**
 * 传感器列Service接口
 * 
 * @author yanghousheng
 * @date 2020-11-09
 */
public interface ISdSensorService 
{
    /**
     * 查询传感器列
     * 
     * @param id 传感器列ID
     * @return 传感器列
     */
    public SdSensor selectSdSensorById(Long id);

    /**
     * 查询传感器列列表
     * 
     * @param sdSensor 传感器列
     * @return 传感器列集合
     */
    public List<SdSensor> selectSdSensorList(SdSensor sdSensor);

    /**
     * 新增传感器列
     * 
     * @param sdSensor 传感器列
     * @return 结果
     */
    public int insertSdSensor(SdSensor sdSensor);

    /**
     * 修改传感器列
     * 
     * @param sdSensor 传感器列
     * @return 结果
     */
    public int updateSdSensor(SdSensor sdSensor);

    /**
     * 批量删除传感器列
     * 
     * @param ids 需要删除的传感器列ID
     * @return 结果
     */
    public int deleteSdSensorByIds(Long[] ids);

    /**
     * 删除传感器列信息
     * 
     * @param id 传感器列ID
     * @return 结果
     */
    public int deleteSdSensorById(Long id);
}
