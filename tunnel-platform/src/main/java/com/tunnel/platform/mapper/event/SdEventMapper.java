package com.tunnel.platform.mapper.event;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.platform.domain.digitalmodel.WjConfidence;
import com.tunnel.platform.domain.event.SdEvent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    void insertEventConfidence(List<WjConfidence> list);

    void insertWjEvent(List<SdEvent> list);

    void insertPic(@Param("eventId") String eventId, @Param("imgUrl")String imgUrl,@Param("picName") String picName);

    void updateVideoById(@Param("eventId") Long eventId,@Param("eventVideoUrl") String eventVideoUrl);

    void insertVhc(@Param("recordSerialNumber") String recordSerialNumber,@Param("id") String id,@Param("tunelId") String tunelId,
                   @Param("originalType") String originalType,@Param("originalColor") String originalColor,
                   @Param("picLicense") String picLicense,@Param("vehicleColor") String vehicleColor);

}