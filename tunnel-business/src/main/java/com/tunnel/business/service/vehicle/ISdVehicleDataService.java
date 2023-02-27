package com.tunnel.business.service.vehicle;

import com.tunnel.business.domain.vehicle.SdVehicleData;

import java.util.List;
import java.util.Map;

/**
 * 隧道车辆数据（单车数据）Service接口
 *
 * @author ruoyi
 * @date 2023-02-25
 */
public interface ISdVehicleDataService
{
    /**
     * 查询隧道车辆数据（单车数据）
     *
     * @param id 隧道车辆数据（单车数据）主键
     * @return 隧道车辆数据（单车数据）
     */
    public SdVehicleData selectSdVehicleDataById(Long id);

    /**
     * 查询隧道车辆数据（单车数据）列表
     *
     * @param sdVehicleData 隧道车辆数据（单车数据）
     * @return 隧道车辆数据（单车数据）集合
     */
    public List<SdVehicleData> selectSdVehicleDataList(SdVehicleData sdVehicleData);

    /**
     * 新增隧道车辆数据（单车数据）
     *
     * @param sdVehicleData 隧道车辆数据（单车数据）
     * @return 结果
     */
    public int insertSdVehicleData(SdVehicleData sdVehicleData);

    /**
     * 修改隧道车辆数据（单车数据）
     *
     * @param sdVehicleData 隧道车辆数据（单车数据）
     * @return 结果
     */
    public int updateSdVehicleData(SdVehicleData sdVehicleData);

    /**
     * 批量删除隧道车辆数据（单车数据）
     *
     * @param ids 需要删除的隧道车辆数据（单车数据）主键集合
     * @return 结果
     */
    public int deleteSdVehicleDataByIds(Long[] ids);

    /**
     * 删除隧道车辆数据（单车数据）信息
     *
     * @param id 隧道车辆数据（单车数据）主键
     * @return 结果
     */
    public int deleteSdVehicleDataById(Long id);

    /**
     * 按小时统计当天的车辆数据
     * @param sdVehicleData 隧道ID
     * @return
     */
    List<Map> getDayVehicleData(SdVehicleData sdVehicleData);
}
