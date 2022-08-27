package com.tunnel.platform.service.intelligent.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.platform.domain.intelligent.SdWeatherReport;
import com.tunnel.platform.mapper.intelligent.SdWeatherReportMapper;
import com.tunnel.platform.service.intelligent.ISdWeatherReportService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 气象采集器信息Service业务层处理
 *
 * @author ruoyi
 * @date 2021-11-22
 */
@Service
public class SdWeatherReportServiceImpl implements ISdWeatherReportService
{
    @Autowired
    private SdWeatherReportMapper sdWeatherReportMapper;

    /**
     * 查询气象采集器信息
     *
     * @param reportId 气象采集器信息ID
     * @return 气象采集器信息
     */
    @Override
    public SdWeatherReport selectSdWeatherReportById(Long reportId)
    {
        return sdWeatherReportMapper.selectSdWeatherReportById(reportId);
    }

    /**
     * 查询气象采集器信息列表
     *
     * @param sdWeatherReport 气象采集器信息
     * @return 气象采集器信息
     */
    @Override
    public List<SdWeatherReport> selectSdWeatherReportList(SdWeatherReport sdWeatherReport)
    {
        Long deptId = SecurityUtils.getDeptId();
        sdWeatherReport.getParams().put("deptId",deptId);
        return sdWeatherReportMapper.selectSdWeatherReportList(sdWeatherReport);
    }

    /**
     * 新增气象采集器信息
     *
     * @param sdWeatherReport 气象采集器信息
     * @return 结果
     */
    @Override
    public int insertSdWeatherReport(SdWeatherReport sdWeatherReport)
    {
        sdWeatherReport.setCreateTime(DateUtils.getNowDate());
        return sdWeatherReportMapper.insertSdWeatherReport(sdWeatherReport);
    }

    /**
     * 修改气象采集器信息
     *
     * @param sdWeatherReport 气象采集器信息
     * @return 结果
     */
    @Override
    public int updateSdWeatherReport(SdWeatherReport sdWeatherReport)
    {
        return sdWeatherReportMapper.updateSdWeatherReport(sdWeatherReport);
    }

    /**
     * 批量删除气象采集器信息
     *
     * @param reportIds 需要删除的气象采集器信息ID
     * @return 结果
     */
    @Override
    public int deleteSdWeatherReportByIds(Long[] reportIds)
    {
        return sdWeatherReportMapper.deleteSdWeatherReportByIds(reportIds);
    }

    /**
     * 删除气象采集器信息信息
     *
     * @param reportId 气象采集器信息ID
     * @return 结果
     */
    @Override
    public int deleteSdWeatherReportById(Long reportId)
    {
        return sdWeatherReportMapper.deleteSdWeatherReportById(reportId);
    }

    /**
     * 查询某隧道最新的能见度记录
     *
     * @param tunnelId 隧道id
     * @return
     */
    @Override
    public List<SdWeatherReport> selectLatestWeatherList(String tunnelId) {
        return sdWeatherReportMapper.selectLatestWeatherList(tunnelId);
    }
}
