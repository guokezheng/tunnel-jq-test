package com.tunnel.platform.service.digitalmodel;


import com.tunnel.platform.domain.digitalmodel.SdRadarDetectDatas;

import java.util.List;

/**
 * 雷达监测感知数据Service接口
 * 
 * @author ruoyi
 * @date 2022-09-04
 */
public interface ISdRadarDetectDataService 
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
     * 批量删除雷达监测感知数据
     * 
     * @param ids 需要删除的雷达监测感知数据主键集合
     * @return 结果
     */
    public int deleteSdRadarDetectDataByIds(String[] ids);

    /**
     * 删除雷达监测感知数据信息
     * 
     * @param id 雷达监测感知数据主键
     * @return 结果
     */
    public int deleteSdRadarDetectDataById(String id);
}
