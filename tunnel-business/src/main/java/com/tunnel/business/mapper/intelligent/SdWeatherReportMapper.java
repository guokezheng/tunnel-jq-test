package com.tunnel.business.mapper.intelligent;


import com.tunnel.business.domain.intelligent.SdWeatherReport;

import java.util.List;

/**
 * 气象采集器信息Mapper接口
 *
 * @author ruoyi
 * @date 2021-11-22
 */
public interface SdWeatherReportMapper
{
    /**
     * 查询气象采集器信息
     *
     * @param reportId 气象采集器信息ID
     * @return 气象采集器信息
     */
    public SdWeatherReport selectSdWeatherReportById(Long reportId);

    /**
     * 查询气象采集器信息列表
     *
     * @param sdWeatherReport 气象采集器信息
     * @return 气象采集器信息集合
     */
    public List<SdWeatherReport> selectSdWeatherReportList(SdWeatherReport sdWeatherReport);

    /**
     * 新增气象采集器信息
     *
     * @param sdWeatherReport 气象采集器信息
     * @return 结果
     */
    public int insertSdWeatherReport(SdWeatherReport sdWeatherReport);

    /**
     * 修改气象采集器信息
     *
     * @param sdWeatherReport 气象采集器信息
     * @return 结果
     */
    public int updateSdWeatherReport(SdWeatherReport sdWeatherReport);

    /**
     * 删除气象采集器信息
     *
     * @param reportId 气象采集器信息ID
     * @return 结果
     */
    public int deleteSdWeatherReportById(Long reportId);

    /**
     * 批量删除气象采集器信息
     *
     * @param reportIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdWeatherReportByIds(Long[] reportIds);

    /**
     * 查询某隧道最新的能见度记录
     * @param tunnelId 隧道id
     * @return
     */
    List<SdWeatherReport> selectLatestWeatherList(String tunnelId);
}
