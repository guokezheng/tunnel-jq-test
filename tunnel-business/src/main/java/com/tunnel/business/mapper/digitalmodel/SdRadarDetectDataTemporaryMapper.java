package com.tunnel.business.mapper.digitalmodel;


import com.tunnel.business.domain.event.SdRadarDetectData;
import com.tunnel.business.domain.event.SdRadarDetectDataTemporary;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 雷达监测感知数据临时Mapper接口
 *
 * @author zhai
 * @date 2023-02-14
 */
public interface SdRadarDetectDataTemporaryMapper
{
    /**
     * 查询雷达监测感知数据
     *
     * @param id 雷达监测感知数据主键
     * @return 雷达监测感知数据
     */
    public SdRadarDetectDataTemporary selectSdRadarDetectDataById(String id);

    /**
     * 查询雷达监测感知数据列表
     *
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 雷达监测感知数据集合
     */
    public List<Map<String,String>> selectSdRadarDetectDataList(SdRadarDetectDataTemporary sdRadarDetectData);

    /**
     * 新增雷达监测感知数据
     *
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 结果
     */
    public int insertSdRadarDetectData(SdRadarDetectDataTemporary sdRadarDetectData);

    /**
     * 修改雷达监测感知数据
     *
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 结果
     */
    public int updateSdRadarDetectData(SdRadarDetectDataTemporary sdRadarDetectData);

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

    int deleteData();

    /**
     * 删除小车数据
     * @return
     */
    int deleteCatData();

    /**
     * 查询小车数据
     * @return
     */
    int countDevices();


    /**
     * 查询雷达监测感知数据列表
     * @return
     */
    public int getSdRadarDetectDataCount(@Param("tunnelId")String tunnelId,@Param("roadDir")String roadDir);

}
