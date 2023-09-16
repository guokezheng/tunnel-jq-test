package com.tunnel.business.mapper.protocol;

import com.tunnel.business.domain.protocol.SdDevicePoint;
import com.tunnel.business.domain.protocol.SdDevicePointPlc;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * PLC设备点位(区别测控执行器)Mapper接口
 *
 * @author ruoyi
 * @date 2023-09-16
 */
public interface SdDevicePointPlcMapper
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
     * 根据设备ID列表查询设备点位
     * @param list 设备ID列表
     * @param pointType 点位类型
     * @return
     */
    List<SdDevicePoint> selectDevicePointByList(@Param("list") List<String> list, @Param("pointType") String pointType);


    /**
     * 根据父设备ID、点位类型筛选最小点位、最大点位
     * @param list 父设备ID列表
     * @param codeList 功能码列表
     * @param pointType 点位类型
     * @return
     */
    List<Map> selectDevicePointByGroup(@Param("list") List<String> list, @Param("codeList") List<String> codeList, @Param("pointType") String pointType);


    /**
     * 根据父设备ID、点位类型查询点位信息
     * @param list 父设备ID列表
     * @param codeList 功能码列表
     * @param pointType 点位类型
     * @return
     */
    List<Map> selectPointMapByFEqId(@Param("list") List<String> list,@Param("codeList") List<String> codeList, @Param("pointType") String pointType);



    /**
     * 根据父设备ID、点位类型筛选设备点位
     * @param list 父设备ID列表
     * @param pointType 点位类型
     * @param functionCode 功能码
     * @return
     */
    List<SdDevicePoint> selectDevicePointByFEqId(@Param("list") List<String> list, @Param("pointType") Long pointType,
                                                 @Param("functionCode") String functionCode);


    /**
     * 设备id集合
     * @param sdDevicePoint
     * @return
     */
    public List<SdDevicePointPlc> selectSdDeviceIdList(SdDevicePointPlc sdDevicePoint);



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
     * 删除PLC设备点位(区别测控执行器)
     *
     * @param id PLC设备点位(区别测控执行器)主键
     * @return 结果
     */
    public int deleteSdDevicePointPlcById(Long id);

    /**
     * 批量删除PLC设备点位(区别测控执行器)
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdDevicePointPlcByIds(Long[] ids);
}
