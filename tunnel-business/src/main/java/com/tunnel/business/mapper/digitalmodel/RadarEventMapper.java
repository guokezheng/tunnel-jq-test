package com.tunnel.business.mapper.digitalmodel;

import com.tunnel.business.domain.digitalmodel.WjConfidence;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.domain.event.SdRadarDetectData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author dzy
 * @date 2022/9/4 16:53
 */
public interface RadarEventMapper {

    void insertEventConfidence(List<WjConfidence> list);

    void insertWjEvent(List<SdEvent> list);

    void insertPic(@Param("eventId") String eventId, @Param("imgUrl") String imgUrl, @Param("imgType") String imgType,
                   @Param("picName") String picName, @Param("createBy") String createBy);

    void updateVideoById(@Param("eventId") Long eventId,@Param("eventVideoUrl") String eventVideoUrl);

    void insertVhc(@Param("recordSerialNumber") String recordSerialNumber,@Param("id") String id,@Param("tunelId") String tunelId,
                   @Param("originalType") String originalType,@Param("originalColor") String originalColor,
                   @Param("picLicense") String picLicense,@Param("vehicleColor") String vehicleColor);

    void insertRadarDetect(List<SdRadarDetectData> list);

    Integer selectID(@Param("eventId") Long eventId);

    void updateEvent(SdEvent sdEvent);

    /**
     * 查询事件置信度集合
     * @param eventId
     * @return
     */
    List<WjConfidence> selectConfidence(@Param("eventId") Long eventId);

    /**
     * 修改事件置信度
     * @param list
     * @return
     */
    int updateEventConfidence(WjConfidence list);
}
