package com.tunnel.business.service.event;


import com.tunnel.business.domain.event.SdReservePlan;
import com.tunnel.business.domain.event.SdStrategy;
import com.tunnel.business.domain.event.SdWarningInfo;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 预警信息Service接口
 *
 * @author gongfanfei
 * @date 2020-08-21sd_sensor_message
 */
public interface ISdWarningInfoService {
    /**
     * 查询预警信息
     *
     * @param id 预警信息ID
     * @return 预警信息
     */
    SdWarningInfo selectSdWarningInfoById(Long id);

    /**
     * 查询预警信息列表
     *
     * @param sdWarningInfo 预警信息
     * @return 预警信息集合
     */
    List<SdWarningInfo> selectSdWarningInfoList(SdWarningInfo sdWarningInfo);

    /**
     * 新增预警信息
     *
     * @param sdWarningInfo 预警信息
     * @return 结果
     */
    int insertSdWarningInfo(SdWarningInfo sdWarningInfo);

    /**
     * 修改预警信息
     *
     * @param sdWarningInfo 预警信息
     * @return 结果
     */
    int updateSdWarningInfo(SdWarningInfo sdWarningInfo);

    int updateSdWarningInfoByProcessState(SdWarningInfo sdWarningInfo);

    /**
     * 批量删除预警信息
     *
     * @param ids 需要删除的预警信息ID
     * @return 结果
     */
    int deleteSdWarningInfoByIds(Long[] ids);

    /**
     * 删除预警信息信息
     *
     * @param id 预警信息ID
     * @return 结果
     */
    int deleteSdWarningInfoById(Long id);

    /**
     * 查询相关预案信息
     *
     * @param id 预警类型ID
     * @return 预警信息
     */
    List<SdReservePlan> seePlanListByWarningTypeId(Long warningTypeId);

    /**
     * 查询相关策略信息
     *
     * @param id 预警类型ID
     * @return 预警信息
     */
    List<SdStrategy> seeStrategyListById(Long warningTypeId);

    Map<String, Object> selectSdWarningInfoCount(SdWarningInfo sdWarningInfo);

    /**
     * 查询最近一周的报警数量
     *
     * @param sdWarningInfo
     * @return
     */
    List<Map<String, String>> selectSdWarningInfoEcharts(SdWarningInfo sdWarningInfo);

    List<SdWarningInfo> selectSdWarningList(SdWarningInfo sdWarningInfo);

    List<SdWarningInfo> getAllPressureGaugesWarningMsg();

    List<Map<String, Object>> getWarningDataAnalysis(SdWarningInfo sdWarningInfo) throws ParseException;

    List<Map<String, Object>> getTheWarningDataOfToday(SdWarningInfo sdWarningInfo);

    List<String> getSystemWarningMsg();

    List<String> getTrafficIncident();

    Map<String, Object> getWarningInfoCount();

    Map<String, Object> getCarPosition(String tunnelId);

    Map<String, Object> getWarningMessageCountOfToday(String tunnelId);

    List<SdWarningInfo> getWarningMassageOfUnProcess(String tunnelId);

}
