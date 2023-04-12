package com.tunnel.business.mapper.protocol;

import java.util.List;
import com.tunnel.business.domain.protocol.SdDevicePoint;

/**
 * 设备点位状态详情Mapper接口
 * 
 * @author ruoyi
 * @date 2023-02-27
 */
public interface SdDevicePointMapper 
{
    /**
     * 查询设备点位状态详情
     * 
     * @param id 设备点位状态详情主键
     * @return 设备点位状态详情
     */
    public SdDevicePoint selectSdDevicePointById(Long id);

    /**
     * 查询设备点位状态详情列表
     * 
     * @param sdDevicePoint 设备点位状态详情
     * @return 设备点位状态详情集合
     */
    public List<SdDevicePoint> selectSdDevicePointList(SdDevicePoint sdDevicePoint);


    /**
     * 设备id集合
     * @param sdDevicePoint
     * @return
     */
    public List<SdDevicePoint> selectSdDeviceIdList(SdDevicePoint sdDevicePoint);

    /**
     * 新增设备点位状态详情
     * 
     * @param sdDevicePoint 设备点位状态详情
     * @return 结果
     */
    public int insertSdDevicePoint(SdDevicePoint sdDevicePoint);

    /**
     * 修改设备点位状态详情
     * 
     * @param sdDevicePoint 设备点位状态详情
     * @return 结果
     */
    public int updateSdDevicePoint(SdDevicePoint sdDevicePoint);

    /**
     * 删除设备点位状态详情
     * 
     * @param id 设备点位状态详情主键
     * @return 结果
     */
    public int deleteSdDevicePointById(Long id);

    /**
     * 批量删除设备点位状态详情
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdDevicePointByIds(Long[] ids);
}
