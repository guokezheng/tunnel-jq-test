package com.tunnel.platform.service.digitalmodel.impl;

import com.tunnel.platform.domain.digitalmodel.SdRadarDetectDatas;
import com.tunnel.platform.mapper.digitalmodel.SdRadarDetectDatasMapper;
import com.tunnel.platform.service.digitalmodel.ISdRadarDetectDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 雷达监测感知数据Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-09-04
 */
@Service
public class SdRadarDetectDataServiceImpl implements ISdRadarDetectDataService
{
    @Autowired
    private SdRadarDetectDatasMapper sdRadarDetectDataMapper;

    /**
     * 查询雷达监测感知数据
     * 
     * @param id 雷达监测感知数据主键
     * @return 雷达监测感知数据
     */
    @Override
    public SdRadarDetectDatas selectSdRadarDetectDataById(String id)
    {
        return sdRadarDetectDataMapper.selectSdRadarDetectDataById(id);
    }

    /**
     * 查询雷达监测感知数据列表
     * 
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 雷达监测感知数据
     */
    @Override
    public List<SdRadarDetectDatas> selectSdRadarDetectDataList(SdRadarDetectDatas sdRadarDetectData)
    {
        return sdRadarDetectDataMapper.selectSdRadarDetectDataList(sdRadarDetectData);
    }

    /**
     * 新增雷达监测感知数据
     * 
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 结果
     */
    @Override
    public int insertSdRadarDetectData(SdRadarDetectDatas sdRadarDetectData)
    {
        return sdRadarDetectDataMapper.insertSdRadarDetectData(sdRadarDetectData);
    }

    /**
     * 修改雷达监测感知数据
     * 
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 结果
     */
    @Override
    public int updateSdRadarDetectData(SdRadarDetectDatas sdRadarDetectData)
    {
        return sdRadarDetectDataMapper.updateSdRadarDetectData(sdRadarDetectData);
    }

    /**
     * 批量删除雷达监测感知数据
     * 
     * @param ids 需要删除的雷达监测感知数据主键
     * @return 结果
     */
    @Override
    public int deleteSdRadarDetectDataByIds(String[] ids)
    {
        return sdRadarDetectDataMapper.deleteSdRadarDetectDataByIds(ids);
    }

    /**
     * 删除雷达监测感知数据信息
     * 
     * @param id 雷达监测感知数据主键
     * @return 结果
     */
    @Override
    public int deleteSdRadarDetectDataById(String id)
    {
        return sdRadarDetectDataMapper.deleteSdRadarDetectDataById(id);
    }
    /**
     * 根据隧道id 查询24小时 感知数据
     *
     * @param tunnelId
     * @return
     */
    public Object[] eventById(String tunnelId)
    {
        List<Map> maps = sdRadarDetectDataMapper.eventById(tunnelId);
        ArrayList<Object> time = new ArrayList<>();
        ArrayList<Object> num = new ArrayList<>();
        Object[] resArr = new Object[2];
        for (Map map : maps) {
            Object timeData = map.get("time");
            time.add(timeData);
            Object numData = map.get("num");
            num.add(numData);
        }
        resArr[0] = time;
        resArr[1] = num;
        return resArr;
    }
}
