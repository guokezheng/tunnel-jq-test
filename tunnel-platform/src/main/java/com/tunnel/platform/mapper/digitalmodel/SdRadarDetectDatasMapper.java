package com.tunnel.platform.mapper.digitalmodel;


import com.tunnel.platform.domain.digitalmodel.SdRadarDetectDatas;

import java.util.List;
import java.util.Map;

/**
 * 雷达监测感知数据Mapper接口
 * 
 * @author ruoyi
 * @date 2022-09-04
 */
public interface SdRadarDetectDatasMapper
{
    /**
     * 查询雷达监测感知数据
     * 
     * @param id 雷达监测感知数据主键
     * @return 雷达监测感知数据
     */
    public SdRadarDetectDatas selectSdRadarDetectDataById(String id);

    /**
     * 查询雷达监测感知数据列表
     * 
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 雷达监测感知数据集合
     */
    public List<SdRadarDetectDatas> selectSdRadarDetectDataList(SdRadarDetectDatas sdRadarDetectData);

    /**
     * 新增雷达监测感知数据
     * 
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 结果
     */
    public int insertSdRadarDetectData(SdRadarDetectDatas sdRadarDetectData);

    /**
     * 修改雷达监测感知数据
     * 
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 结果
     */
    public int updateSdRadarDetectData(SdRadarDetectDatas sdRadarDetectData);

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
