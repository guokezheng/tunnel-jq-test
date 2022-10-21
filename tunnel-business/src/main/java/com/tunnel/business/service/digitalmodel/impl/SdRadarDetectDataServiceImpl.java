package com.tunnel.business.service.digitalmodel.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.event.SdRadarDetectData;
import com.tunnel.business.mapper.digitalmodel.SdRadarDetectDataMapper;
import com.tunnel.business.service.digitalmodel.ISdRadarDetectDataService;
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
public class SdRadarDetectDataServiceImpl implements ISdRadarDetectDataService {
    @Autowired
    private SdRadarDetectDataMapper sdRadarDetectDataMapper;

    /**
     * 查询雷达监测感知数据
     *
     * @param id 雷达监测感知数据主键
     * @return 雷达监测感知数据
     */
    @Override
    public SdRadarDetectData selectSdRadarDetectDataById(String id) {
        return sdRadarDetectDataMapper.selectSdRadarDetectDataById(id);
    }

    /**
     * 查询雷达监测感知数据列表
     *
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 雷达监测感知数据
     */
    @Override
    public List<Map<String,String>> selectSdRadarDetectDataList(SdRadarDetectData sdRadarDetectData) {
        Long deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        sdRadarDetectData.setDeptId(deptId);
        return sdRadarDetectDataMapper.selectSdRadarDetectDataList(sdRadarDetectData);
    }

    /**
     * 新增雷达监测感知数据
     *
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 结果
     */
    @Override
    public int insertSdRadarDetectData(SdRadarDetectData sdRadarDetectData) {
        return sdRadarDetectDataMapper.insertSdRadarDetectData(sdRadarDetectData);
    }

    /**
     * 修改雷达监测感知数据
     *
     * @param sdRadarDetectData 雷达监测感知数据
     * @return 结果
     */
    @Override
    public int updateSdRadarDetectData(SdRadarDetectData sdRadarDetectData) {
        return sdRadarDetectDataMapper.updateSdRadarDetectData(sdRadarDetectData);
    }

    /**
     * 批量删除雷达监测感知数据
     *
     * @param ids 需要删除的雷达监测感知数据主键
     * @return 结果
     */
    @Override
    public int deleteSdRadarDetectDataByIds(String[] ids) {
        return sdRadarDetectDataMapper.deleteSdRadarDetectDataByIds(ids);
    }

    /**
     * 删除雷达监测感知数据信息
     *
     * @param id 雷达监测感知数据主键
     * @return 结果
     */
    @Override
    public int deleteSdRadarDetectDataById(String id) {
        return sdRadarDetectDataMapper.deleteSdRadarDetectDataById(id);
    }

    /**
     * 根据隧道id 查询24小时 感知数据
     *
     * @param tunnelId
     * @return
     */
    @Override
    public Object[] eventById(String tunnelId) {
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

    @Override
    public List<Map<String, Object>> vehicleMonitoringInRecent24Hours(String tunnelId) {
        List<Map<String, Object>> maps = sdRadarDetectDataMapper.vehicleMonitoringInRecent24Hours(tunnelId);
        return maps;
    }
}
