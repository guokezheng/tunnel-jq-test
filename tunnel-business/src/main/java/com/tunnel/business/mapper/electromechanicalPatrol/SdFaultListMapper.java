package com.tunnel.business.mapper.electromechanicalPatrol;


import com.tunnel.business.domain.electromechanicalPatrol.SdFaultList;
import com.tunnel.business.domain.electromechanicalPatrol.SdPatrolList;
import com.tunnel.business.domain.event.SdEvent;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

/**
 * 故障清单Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-02
 */
public interface SdFaultListMapper 
{
    /**
     * 查询故障清单
     * 
     * @param id 故障清单主键
     * @return 故障清单
     */
    public SdFaultList selectSdFaultListById(String id);

    /**
     * 查询故障清单列表
     * 
     * @param sdFaultList 故障清单
     * @return 故障清单集合
     */
    public List<SdFaultList> selectSdFaultListList(SdFaultList sdFaultList);

    /**
     * 新增故障清单
     * 
     * @param sdFaultList 故障清单
     * @return 结果
     */
    public int insertSdFaultList(SdFaultList sdFaultList);

    /**
     * 修改故障清单
     * 
     * @param sdFaultList 故障清单
     * @return 结果
     */
    public int updateSdFaultList(SdFaultList sdFaultList);

    /**
     * 删除故障清单
     * 
     * @param id 故障清单主键
     * @return 结果
     */
    public int deleteSdFaultListById(String id);

    /**
     * 批量删除故障清单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdFaultListByIds(String[] ids);

    /**
     * 查询故障检修记录
     * @param faultId
     * @return
     */
    public List<SdPatrolList> getFaultRepairInfo(String faultId);

    /**
     * 根据隧道、故障类型获取故障列表
     * @param tunnelId
     * @param faultLevel
     * @return
     */
    List<SdFaultList> getFaultList1(@Param("tunnelId")String tunnelId, @Param("faultLevel")String faultLevel,@Param("searchValue")String searchValue,@Param("eqType")String eqType);

    /**
     * 批量查询故障信息
     * @param faultIds
     * @return
     */
    List<SdFaultList> batchGetFaultList(String[] faultIds);

    /**
     * 设备故障状况统计
     * @return
     */
    List<Map<String, Object>> getEquipmentFault(@Param("eqSystem") String eqSystem,
                                                @Param("eqSystemType") String eqSystemType,
                                                @Param("tunnelId") String tunnelId,
                                                @Param("faultStatus") String faultStatus);

    /**
     * 未处理故障统计
     * @return
     */
    List<Map<String, Object>> getEquipmentFaultStatistics(@Param("eqSystem") String eqSystem,
                                                          @Param("tunnelId") String tunnelId,
                                                          @Param("faultStatus") String faultStatus,
                                                          @Param("falltRemoveStatue") String falltRemoveStatue);

    SdFaultList exportFaultReport(String faultId);

    /**
     * 查询设备运行状态
     * @param deviceId
     * @param deviceType
     * @return
     */
    String selectEqRunStatus(@Param("deviceId") String deviceId,
                             @Param("deviceType") Long deviceType);

    /**
     * 查询设备上报的故障数据
     * @return
     */
    List<SdFaultList> selectDeviceFault();

    /**
     * 巡查点故障检修记录
     * @param faultId
     * @return
     */
    List<SdPatrolList> getDevicesRepairInfo(String faultId);

    String selectSdFaultEqById(@Param("eqFaultId") String eqFaultId);

    List<SdPatrolList> getFaultRepairReportInfo(@Param("faultId")String faultId);

    int updateFaultRemoveState(@Param("faultId")String faultId,@Param("falltRemoveStatue")String falltRemoveStatue);

    /**
     * 高速云监听判断故障是否已存在
     * @param id
     * @return
     */
    SdFaultList selectSdFaultById(@Param("id")String id);

    /**
     * 故障未消除
     * @param faultId
     * @param falltRemoveStatue
     * @return
     */
    int updateFaultUnRemoveState(@Param("faultId")String faultId,@Param("falltRemoveStatue")String falltRemoveStatue);

    /**
     * app查询故障相关字典值
     * @return
     */
    List<SdFaultList> getFaultDictValue(@Param("dictType")String dictType);

    List<SdEvent> selectSdFaultList(SdFaultList sdFaultListData);

    /**
     * 查询设备故障信息
     * @param direction
     * @param eqId
     * @return
     */
    List<Map> selectSdFaultEqByDirection(@Param("direction") String direction,@Param("eqId")String eqId);

    /**
     * 设备状态
     * @param direction
     * @param eqId
     */
    int updateFalltRemoveStatueSuccess(@Param("direction") String direction,@Param("eqId")String eqId);
}
