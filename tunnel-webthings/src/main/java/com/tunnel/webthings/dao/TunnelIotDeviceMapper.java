package com.tunnel.webthings.dao;

import com.tunnel.webthings.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 对物联中台设备
 * @author ZHC
 * @date 2022/7/18 9:49
 */
public interface TunnelIotDeviceMapper {

    /**
     * 返回设备类型
     * @param devType 设备类型
     * @return
     */
    String selectIotDeviceType(String devType);

    /**
     * 添加主动发光标志信息到设备信息表
     * @param vo
     * @return
     */
    int addActiveLuminousSigns(SdActiveLuminousSignsVO vo);

    /**
     * 添加合流区预警设备故障告警到设备信息表
     * @param vo
     * @return
     */
    int addConfluenceDevFaultWarn(SdConfluenceDevFaultWarnVO vo);

    /**
     * 添加雷达信息到设备信息表
     * @param vo
     * @return
     */
    int addRadarMsgTopic(SdRadarMsgTopicVO vo);

    /**
     * 添加设备更改信息到设备信息表
     * @param vo
     * @return
     */
    int addStateStorage(SdStateStorageVO vo);

    List<String> selectFile();

    void update(@Param("s1") String s1,@Param("s") String s);

    int selectCount();

    void insertDevice(List<ResponseVO> list);

    void deleteDevice();
}
