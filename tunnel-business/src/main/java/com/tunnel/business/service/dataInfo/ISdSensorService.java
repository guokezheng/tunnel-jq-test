package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdSensor;

import java.util.List;

/**
 * 传感器列Service接口
 *
 * @author yanghousheng
 * @date 2020-11-09
 */
public interface ISdSensorService {
    /**
     * 查询传感器列
     *
     * @param id 传感器列ID
     * @return 传感器列
     */
    SdSensor selectSdSensorById(Long id);

    /**
     * 查询传感器列列表
     *
     * @param sdSensor 传感器列
     * @return 传感器列集合
     */
    List<SdSensor> selectSdSensorList(SdSensor sdSensor);

    /**
     * 新增传感器列
     *
     * @param sdSensor 传感器列
     * @return 结果
     */
    int insertSdSensor(SdSensor sdSensor);

    /**
     * 修改传感器列
     *
     * @param sdSensor 传感器列
     * @return 结果
     */
    int updateSdSensor(SdSensor sdSensor);

    /**
     * 批量删除传感器列
     *
     * @param ids 需要删除的传感器列ID
     * @return 结果
     */
    int deleteSdSensorByIds(Long[] ids);

    /**
     * 删除传感器列信息
     *
     * @param id 传感器列ID
     * @return 结果
     */
    int deleteSdSensorById(Long id);
}
