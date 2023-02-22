package com.tunnel.business.mapper.event;


import com.tunnel.business.domain.event.SdEvent;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 事件管理Mapper接口
 *
 * @author gongfanfei
 * @date 2020-08-24
 */
public interface SdEventMapper
{
    /**
     * 查询事件管理
     *
     * @param id 事件管理ID
     * @return 事件管理
     */
    public SdEvent selectSdEventById(Long id);

    /**
     * 查询事件管理列表
     *
     * @param sdEvent 事件管理
     * @return 事件管理集合
     */
    public List<SdEvent> selectSdEventList(SdEvent sdEvent);

    /**
     * 查询事件状态为（已处理、处理中）的数量
     */
    public int selectEventStateCount(String eventState);

    /**
     * 新增事件管理
     *
     * @param sdEvent 事件管理
     * @return 结果
     */
    public int insertSdEvent(SdEvent sdEvent);

    /**
     * 修改事件管理
     *
     * @param sdEvent 事件管理
     * @return 结果
     */
    public int updateSdEvent(SdEvent sdEvent);

    /**
     * 删除事件管理
     *
     * @param id 事件管理ID
     * @return 结果
     */
    public int deleteSdEventById(Long id);

    /**
     * 批量删除事件管理
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdEventByIds(Long[] ids);
    /**
     * 预警事件查询全部
     * @return
     */
    public List<SdEvent> getEvent(SdEvent sdEvent);


    /**
     * 根据事件id列表查询事件信息
     * @param eventIdList 事件id列表
     * @return
     */
    List<SdEvent> getEventList(List<Long> eventIdList);

    /**
     * 事件统计今日数量
     * @return
     */
    Map getTodayEventCount();

    /**
     * 当日全部未处理事件
     * @param subIndex
     * @return
     */
    List<Map> eventPopAll(String subIndex);

    /**
     * 事件当日未处理总条数
     * @return
     */
    Integer getEventUntreatedNum();

    /**
     * 事件弹窗设备故障
     * @return
     */
    List<Map> eventPopFault(String subIndex);

    /**
     * 设备故障条数
     * @return
     */
    @Select("select count(id) from sd_fault_list where " +
            "fault_status = '0' and TO_DAYS(create_time) = TO_DAYS(NOW())")
    int eventPopFaultCount();

}
