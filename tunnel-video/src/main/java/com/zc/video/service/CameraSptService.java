package com.zc.video.service;


/**
 * @author ：xieyuwei
 * @date ：Created in 2022/7/12 8:57
 * 文件说明： </p>
 */
public interface CameraSptService {
    /**
     * 相机实时流
     *
     * @param type  系统编号 默认1 后期通知更改
     * @param camId 相机编号
     * @return
     */
    public String liveStreaming(String type, String camId);

    /**
     * 查询附近相机
     *
     * @param devLat   摄像机纬度
     * @param devLon   摄像机经度
     * @param distance 距离单位米
     * @return
     */
    public Object queryNearbyCamera(String devLat, String devLon, String distance);

    /**
     * 云台控制
     *
     * @param msgType 传入值“3”
     * @param camID   相机编号
     * @param cmdType
     * @param speed   当cmdType小于等于28时为云台动作的速度，预置位设置及调用表示对应的预置位编号；
     * @param type
     * @return
     */
    public Object gimbalControl(String type, String camID, String msgType, String cmdType, String speed);
}
