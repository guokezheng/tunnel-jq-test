package com.tunnel.business.service.protocol.impl;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.protocol.SdDevicePoint;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.protocol.SdDevicePointMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.impl.SdDevicesServiceImpl;
import com.tunnel.business.service.protocol.ISdDevicePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备点位状态详情Service业务层处理
 *
 * @author ruoyi
 * @date 2023-02-27
 */
@Service
public class SdDevicePointServiceImpl implements ISdDevicePointService
{
    @Autowired
    private SdDevicePointMapper sdDevicePointMapper;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;


    @Autowired
    private ISdDevicesService sdDevicesService;

    /**
     * 查询设备点位状态详情
     *
     * @param id 设备点位状态详情主键
     * @return 设备点位状态详情
     */
    @Override
    public SdDevicePoint selectSdDevicePointById(Long id)
    {
        return sdDevicePointMapper.selectSdDevicePointById(id);
    }

    /**
     * 查询设备点位状态详情列表
     *
     * @param sdDevicePoint 设备点位状态详情
     * @return 设备点位状态详情
     */
    @Override
    public List<SdDevicePoint> selectSdDevicePointList(SdDevicePoint sdDevicePoint)
    {
        return sdDevicePointMapper.selectSdDevicePointList(sdDevicePoint);
    }

    /**
     * 根据设备ID列表查询设备点位
     *
     * @param list      设备ID列表
     * @param pointType 点位类型
     * @return
     */
    @Override
    public List<SdDevicePoint> selectDevicePointByList(List<String> list, String pointType) {
        return sdDevicePointMapper.selectDevicePointByList(list,pointType);
    }

    /**
     * 根据父设备ID、点位类型筛选最小点位、最大点位
     *
     * @param list      父设备ID列表
     * @param codeList 功能码列表
     * @param pointType 点位类型
     * @return
     */
    @Override
    public List<Map> selectDevicePointByGroup(List<String> list,List<String> codeList,String pointType) {
        return sdDevicePointMapper.selectDevicePointByGroup(list,codeList,pointType);
    }

    /**
     * 根据父设备ID、点位类型查询点位信息
     *
     * @param list      父设备ID列表
     * @param codeList 功能码列表
     * @param pointType 点位类型
     * @return
     */
    @Override
    public List<Map> selectPointMapByFEqId(List<String> list,List<String> codeList, String pointType) {
        return sdDevicePointMapper.selectPointMapByFEqId(list,codeList,pointType);
    }

    /**
     * 根据父设备ID、点位类型筛选设备点位
     *
     * @param list      父设备ID列表
     * @param pointType 点位类型
     * @param functionCode 功能码
     * @return
     */
    @Override
    public List<SdDevicePoint> selectDevicePointByFEqId(List<String> list, Long pointType,String functionCode) {
        return sdDevicePointMapper.selectDevicePointByFEqId(list,pointType,functionCode);
    }

    /**
     * 新增设备点位状态详情
     *
     * @param sdDevicePoint 设备点位状态详情
     * @return 结果
     */
    @Override
    public int insertSdDevicePoint(SdDevicePoint sdDevicePoint)
    {
        return sdDevicePointMapper.insertSdDevicePoint(sdDevicePoint);
    }

    /**
     * 修改设备点位状态详情
     *
     * @param sdDevicePoint 设备点位状态详情
     * @return 结果
     */
    @Override
    public int updateSdDevicePoint(SdDevicePoint sdDevicePoint)
    {
        return sdDevicePointMapper.updateSdDevicePoint(sdDevicePoint);
    }

    /**
     * 批量删除设备点位状态详情
     *
     * @param ids 需要删除的设备点位状态详情主键
     * @return 结果
     */
    @Override
    public int deleteSdDevicePointByIds(Long[] ids)
    {
        return sdDevicePointMapper.deleteSdDevicePointByIds(ids);
    }

    /**
     * 删除设备点位状态详情信息
     *
     * @param id 设备点位状态详情主键
     * @return 结果
     */
    @Override
    public int deleteSdDevicePointById(Long id)
    {
        return sdDevicePointMapper.deleteSdDevicePointById(id);
    }

    @Override
    public String importSdDevices(List<SdDevicePoint> devicePointList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(devicePointList) || devicePointList.size() == 0) {
            throw new ServiceException("导入设备数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (SdDevicePoint devicePoint : devicePointList) {
            try {
                SdDevices d = sdDevicesMapper.selectSdDevicesById(devicePoint.getEqId());
               // List<SdDevicePoint> dpList =  sdDevicePointMapper.selectSdDevicePointByDevId(devicePoint.getEqId());

                SdDevicePoint sdDevicePoint = sdDevicePointMapper.selectSdDevicePointByDevIdAndItemId(devicePoint.getEqId(),devicePoint.getItemId()+"");
                if (StringUtils.isNull(sdDevicePoint)) {
                    Map map = sdDevicesService.checkDevices(d,isUpdateSupport);
                    if ((Boolean) map.get("flag")) {
                        this.insertSdDevicePoint(devicePoint);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、设备ID " + devicePoint.getEqId() + " 点位导入成功");
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + map.get("failureMsg").toString());
                    }
                } else if (isUpdateSupport) {
                    Map map = sdDevicesService.checkDevices(d,isUpdateSupport);
                    if ((Boolean) map.get("flag")) {
                        devicePoint.setId(sdDevicePoint.getId());
                        this.updateSdDevicePoint(devicePoint);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、设备ID " + devicePoint.getEqId() + " 更新成功");
                    } else {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + map.get("failureMsg").toString());
                    }
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、设备ID " + devicePoint.getEqId() + " 点位已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、设备ID " + devicePoint.getEqId() + " 点位导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }
        if (failureNum > 0) {
            if (successNum > 0) {
                failureMsg.insert(0, successNum + " 条数据导入成功" + "，" + failureNum + " 条数据格式不正确，导入失败！错误如下：");
            } else {
                failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            }
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
