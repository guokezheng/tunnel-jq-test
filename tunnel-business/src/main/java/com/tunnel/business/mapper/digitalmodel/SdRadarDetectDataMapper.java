package com.tunnel.business.mapper.digitalmodel;


import com.tunnel.business.domain.event.SdRadarDetectData;

import java.util.List;
import java.util.Map;

/**
 * 雷达监测感知数据Mapper接口
 * 
 * @author ruoyi
 * @date 2022-09-04
 */
public interface SdRadarDetectDataMapper
{
    /**
     * 查询雷达监测感知数据
     * 
     * @param id 雷达监测感知数据主键
     * @return 雷达监测感知数据
     */
    public SdRadarDetectData selectSdRadarDetectDataById(String id);

    /**
     * 查询雷达监测感知数据列表
     * 
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 雷达监测感知数据集合
     */
    public List<SdRadarDetectData> selectSdRadarDetectDataList(SdRadarDetectData sdRadarDetectData);

    /**
     * 新增雷达监测感知数据
     * 
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 结果
     */
    public int insertSdRadarDetectData(SdRadarDetectData sdRadarDetectData);

    /**
     * 修改雷达监测感知数据
     * 
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 结果
     */
    public int updateSdRadarDetectData(SdRadarDetectData sdRadarDetectData);

    /**
     * 删除雷达监测感知数据
     * 
     * @param id 雷达监测感知数据主键
     * @return 结果
     */
    public int deleteSdRadarDetectDataById(String id);

    /**
     * 批量删除雷达监测感知数据
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdRadarDetectDataByIds(String[] ids);
    /**
     * 根据隧道id 查询24小时 感知数据
     * @param tunnelId
     * @return
     */
    public List<Map> eventById(String tunnelId);


}
