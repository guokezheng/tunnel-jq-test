package com.tunnel.platform.service.dataInfo;

import com.tunnel.platform.domain.dataInfo.SdSensorType;

import java.util.List;

/**
 * 传感器类型Service接口
 * 
 * @author yanghousheng
 * @date 2020-11-10
 */
public interface ISdSensorTypeService 
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
     * 批量删除传感器类型
     * 
     * @param ids 需要删除的传感器类型ID
     * @return 结果
     */
    public int deleteSdSensorTypeByIds(Long[] ids);

    /**
     * 删除传感器类型信息
     * 
     * @param id 传感器类型ID
     * @return 结果
     */
    public int deleteSdSensorTypeById(Long id);
}
