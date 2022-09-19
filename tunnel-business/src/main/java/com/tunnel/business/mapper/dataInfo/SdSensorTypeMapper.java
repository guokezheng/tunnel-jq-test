package com.tunnel.business.mapper.dataInfo;


import com.tunnel.business.domain.dataInfo.SdSensorType;

import java.util.List;

/**
 * 传感器类型Mapper接口
 * 
 * @author yanghousheng
 * @date 2020-11-10
 */
public interface SdSensorTypeMapper 
{
    /**
     * 查询传感器类型
     * 
     * @param id 传感器类型ID
     * @return 传感器类型
     */
    public SdSensorType selectSdSensorTypeById(Long id);

    /**
     * 查询传感器类型列表
     * 
     * @param sdSensorType 传感器类型
     * @return 传感器类型集合
     */
    public List<SdSensorType> selectSdSensorTypeList(SdSensorType sdSensorType);

    /**
     * 新增传感器类型
     * 
     * @param sdSensorType 传感器类型
     * @return 结果
     */
    public int insertSdSensorType(SdSensorType sdSensorType);

    /**
     * 修改传感器类型
     * 
     * @param sdSensorType 传感器类型
     * @return 结果
     */
    public int updateSdSensorType(SdSensorType sdSensorType);

    /**
     * 删除传感器类型
     * 
     * @param id 传感器类型ID
     * @return 结果
     */
    public int deleteSdSensorTypeById(Long id);

    /**
     * 批量删除传感器类型
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdSensorTypeByIds(Long[] ids);
}
