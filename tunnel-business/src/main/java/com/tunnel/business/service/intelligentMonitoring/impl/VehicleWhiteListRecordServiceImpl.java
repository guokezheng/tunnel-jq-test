package com.tunnel.business.service.intelligentMonitoring.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.intelligentMonitoring.VehicleWhiteListRecord;
import com.tunnel.business.domain.intelligentMonitoring.VehicleWhiteListRecordDTO;
import com.tunnel.business.mapper.intelligentMonitoring.VehicleWhiteListRecordMapper;
import com.tunnel.business.service.intelligentMonitoring.IVehicleWhiteListRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 白名单车辆通行记录Service业务层处理
 *
 * @author ruoyi
 * @date 2021-11-30
 */
@Service
public class VehicleWhiteListRecordServiceImpl implements IVehicleWhiteListRecordService {
    @Autowired
    private VehicleWhiteListRecordMapper vehicleWhiteListRecordMapper;

    /**
     * 查询白名单车辆通行记录
     *
     * @param id 白名单车辆通行记录ID
     * @return 白名单车辆通行记录
     */
    @Override
    public VehicleWhiteListRecord selectVehicleWhiteListRecordById(Integer id) {
        return vehicleWhiteListRecordMapper.selectVehicleWhiteListRecordById(id);
    }

    /**
     * 查询白名单车辆通行记录列表
     *
     * @param vehicleWhiteListRecordDTO 白名单车辆通行记录
     * @return 白名单车辆通行记录
     */
    @Override
    public List<VehicleWhiteListRecord> selectVehicleWhiteListRecordList(VehicleWhiteListRecordDTO vehicleWhiteListRecordDTO) {
        return vehicleWhiteListRecordMapper.selectVehicleWhiteListRecordList(vehicleWhiteListRecordDTO);
    }

    /**
     * 新增白名单车辆通行记录
     *
     * @param vehicleWhiteListRecord 白名单车辆通行记录
     * @return 结果
     */
    @Override
    public int insertVehicleWhiteListRecord(VehicleWhiteListRecord vehicleWhiteListRecord) {
        vehicleWhiteListRecord.setCreateTime(DateUtils.getNowDate());
        return vehicleWhiteListRecordMapper.insertVehicleWhiteListRecord(vehicleWhiteListRecord);
    }

    /**
     * 修改白名单车辆通行记录
     *
     * @param vehicleWhiteListRecord 白名单车辆通行记录
     * @return 结果
     */
    @Override
    public int updateVehicleWhiteListRecord(VehicleWhiteListRecord vehicleWhiteListRecord) {
        return vehicleWhiteListRecordMapper.updateVehicleWhiteListRecord(vehicleWhiteListRecord);
    }

    /**
     * 批量删除白名单车辆通行记录
     *
     * @param ids 需要删除的白名单车辆通行记录ID
     * @return 结果
     */
    @Override
    public int deleteVehicleWhiteListRecordByIds(Integer[] ids) {
        return vehicleWhiteListRecordMapper.deleteVehicleWhiteListRecordByIds(ids);
    }

    /**
     * 删除白名单车辆通行记录信息
     *
     * @param id 白名单车辆通行记录ID
     * @return 结果
     */
    @Override
    public int deleteVehicleWhiteListRecordById(Integer id) {
        return vehicleWhiteListRecordMapper.deleteVehicleWhiteListRecordById(id);
    }
}
