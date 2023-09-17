package com.tunnel.business.service.protocol;

import java.util.List;
import java.util.Map;

import com.tunnel.business.domain.protocol.SdDevicePoint;
import com.tunnel.business.domain.protocol.SdDevicePointPlc;

/**
 * PLC设备点位(区别测控执行器)Service接口
 *
 * @author ruoyi
 * @date 2023-09-16
 */
public interface ISdDevicePointPlcService
{
    /**
     * 查询PLC设备点位(区别测控执行器)
     *
     * @param id PLC设备点位(区别测控执行器)主键
     * @return PLC设备点位(区别测控执行器)
     */
    public SdDevicePointPlc selectSdDevicePointPlcById(Long id);

    /**
     * 查询PLC设备点位(区别测控执行器)列表
     *
     * @param sdDevicePointPlc PLC设备点位(区别测控执行器)
     * @return PLC设备点位(区别测控执行器)集合
     */
    public List<SdDevicePointPlc> selectSdDevicePointPlcList(SdDevicePointPlc sdDevicePointPlc);

    /**
     * 新增PLC设备点位(区别测控执行器)
     *
     * @param sdDevicePointPlc PLC设备点位(区别测控执行器)
     * @return 结果
     */
    public int insertSdDevicePointPlc(SdDevicePointPlc sdDevicePointPlc);

    /**
     * 修改PLC设备点位(区别测控执行器)
     *
     * @param sdDevicePointPlc PLC设备点位(区别测控执行器)
     * @return 结果
     */
    public int updateSdDevicePointPlc(SdDevicePointPlc sdDevicePointPlc);

    /**
     * 批量删除PLC设备点位(区别测控执行器)
     *
     * @param ids 需要删除的PLC设备点位(区别测控执行器)主键集合
     * @return 结果
     */
    public int deleteSdDevicePointPlcByIds(Long[] ids);

    /**
     * 删除PLC设备点位(区别测控执行器)信息
     *
     * @param id PLC设备点位(区别测控执行器)主键
     * @return 结果
     */
    public int deleteSdDevicePointPlcById(Long id);



    /**
     * 根据父设备ID、点位类型筛选最小点位、最大点位
     * @param list 父设备ID列表
     * @param codeList 功能码列表
     * @param pointType 点位类型
     * @return
     */
    List<Map> selectDevicePointByGroup(List<String> list, List<String> codeList, String pointType);


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
    List<SdDevicePoint> selectDevicePointByFEqId(List<String> list, Long pointType, String functionCode);
}
