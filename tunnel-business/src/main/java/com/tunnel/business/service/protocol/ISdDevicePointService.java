package com.tunnel.business.service.protocol;

import com.tunnel.business.domain.protocol.SdDevicePoint;

import java.util.List;
import java.util.Map;

/**
 * 设备点位状态详情Service接口
 *
 * @author ruoyi
 * @date 2023-02-27
 */
public interface ISdDevicePointService
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
     * 根据设备ID列表查询设备点位
     * @param list 设备ID列表
     * @param pointType 点位类型
     * @return
     */
    List<SdDevicePoint> selectDevicePointByList(List<String> list,String pointType);


    /**
     * 根据父设备ID、点位类型筛选最小点位、最大点位
     * @param list 父设备ID列表
     * @param codeList 功能码列表
     * @param pointType 点位类型
     * @return
     */
    List<Map> selectDevicePointByGroup(List<String> list,List<String> codeList,String pointType);


    /**
     * 根据父设备ID、点位类型查询点位信息
     * @param list 父设备ID列表
     * @param codeList 功能码列表
     * @param pointType 点位类型
     * @return
     */
    List<Map> selectPointMapByFEqId(List<String> list,List<String> codeList,String pointType);


    /**
     * 根据父设备ID、点位类型筛选设备点位
     * @param list 父设备ID列表
     * @param pointType 点位类型
     * @param functionCode 功能码
     * @return
     */
    List<SdDevicePoint> selectDevicePointByFEqId(List<String> list, Long pointType,String functionCode);

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
     * 批量删除设备点位状态详情
     *
     * @param ids 需要删除的设备点位状态详情主键集合
     * @return 结果
     */
    public int deleteSdDevicePointByIds(Long[] ids);

    /**
     * 删除设备点位状态详情信息
     *
     * @param id 设备点位状态详情主键
     * @return 结果
     */
    public int deleteSdDevicePointById(Long id);
}
