package com.tunnel.platform.mapper.digitalmodel;

import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.domain.digitalmodel.SdRadarDevice;
import com.tunnel.platform.domain.digitalmodel.WjConfidence;
import com.tunnel.platform.domain.digitalmodel.WjEvent;
import com.tunnel.platform.domain.event.SdEvent;
import com.tunnel.platform.domain.event.SdRadarDetectData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author dzy
 * @date 2022/9/4 16:53
 */
public interface RadarEventMapper {

    void insertEventConfidence(List<WjConfidence> list);

    void insertWjEvent(List<SdEvent> list);

    void insertPic(@Param("eventId") String eventId, @Param("imgUrl")String imgUrl, @Param("picName") String picName);

    void updateVideoById(@Param("eventId") Long eventId,@Param("eventVideoUrl") String eventVideoUrl);

    void insertVhc(@Param("recordSerialNumber") String recordSerialNumber,@Param("id") String id,@Param("tunelId") String tunelId,
                   @Param("originalType") String originalType,@Param("originalColor") String originalColor,
                   @Param("picLicense") String picLicense,@Param("vehicleColor") String vehicleColor);

    void insertRadarDetect(List<SdRadarDetectData> list);

    Integer selectID(@Param("eventId") Long eventId);

    void updateEvent(SdEvent sdEvent);
}
