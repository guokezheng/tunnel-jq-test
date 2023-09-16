package com.tunnel.deal.corniceTunnelRobot;

import com.tunnel.deal.corniceTunnelRobot.domain.FindAlarmThresholdConfigDto;
import com.tunnel.deal.corniceTunnelRobot.domain.RobotsDto;
import com.tunnel.deal.corniceTunnelRobot.domain.StatusDto;
import com.tunnel.deal.corniceTunnelRobot.domain.VideoDto;

import java.util.List;

public interface CorniceTunnelRobot {

    /**
     *  获取用户信息
     *  @param userName   用户名
     *  @param userPassword 用户密码
     * @return
     */
    String GetUsers(String userName,String userPassword,String baseUrl);

    /**
     *  获取用户所属机器人信息
     *  @param userId   用户id
     * @return
     */
    List<RobotsDto> GetUserRobots(Integer userId, String baseUrl);

    /**
     * 获取机器人状态
     *
     * @param deviceId 机器人id
     * @return
     */
    StatusDto GetStatus(String deviceId, String baseUrl);

    /**
     * 获取机器人视频流地址
     * @param deviceId  机器人id
     * @return
     */
    VideoDto GetVideoUrl(String deviceId, String baseUrl);

    /**
     * 设置机器人投光灯
     * @param deviceId  机器人id
     * @param control   0 关闭  1打开
     * @return
     */
    int SetLEDLight(String deviceId,Integer control,String baseUrl);

    /**
     * 设置机器人报警灯
     * @param deviceId 机器人id
     * @param control  0 关闭  1打开
     * @return
     */
    int SetAlarmLight(String deviceId,Integer control,String baseUrl);

    /**
     *  设置机器人一键充电
     * @param deviceId 机器人id
     * @return
     */
    int Charge(String deviceId,String baseUrl);

    /**
     *  设置机器人到达预置点
     * @param deviceId 机器人id
     * @param prestId  预置为编号
     * @return
     */
    int GotoPreset(String deviceId,Integer presetId,String baseUrl);

    /**
     * 设置机器人移动
     * @param deviceId 机器人id
     * @param control  0 停止 1向前 2向后
     * @param speed   速度 1-5
     * @return
     */
    int Move(String deviceId,Integer control,Integer speed,String baseUrl);

    /**
     *  设置机器人云台
     * @param deviceId 机器人id
     * @param control  0停止 1 上 3 左 5 下 7 右 9 放大 10 缩小
     * @return
     */
    int PTZ(String deviceId,Integer control,String baseUrl);

    /**
     * 语言播报功能
     * @param deviceId 机器人id
     * @param text  播报文字内容
     * @return
     */
    int Broadcast(String deviceId,String text,String baseUrl,String numberCycles);

    /**
     * 设置机器人自动巡航
     * @param deviceId  机器人id
     * @param carmode   切换模式  0 自动巡航  1 手动
     * @return
     */
    int ChangeControl(String deviceId,Integer carmode,String baseUrl);

    /**
     * 控制机器人雨刷
     * @param deviceId 机器人id
     * @return
     */
    int ControlWindscreen(String deviceId,String baseUrl);

    /**
     * 获取电量阈值配置
     * @param deviceId 机器人id
     * @return
     */
    FindAlarmThresholdConfigDto FindAlarmThresholdConfig(String deviceId,String baseUrl);

    /**
     * 编辑电量阈值配置
     * @param deviceId 机器人 id
     * @param id   主键 id
     * @param groupName 电量阈值配置名称
     * @param beginTime 电量阈值生效开始时间
     * @param endTime   电量阈值生效结束时间
     * @param powerEnable   电池开启状态 1 开启 0 不开启
     * @param powerLowerLimit   电池低电量
     * @param powerTopLimit 电池高电量
     * @return
     */
    int SetAlarmThresholdConfig(FindAlarmThresholdConfigDto findAlarmThresholdConfigDto,String baseUrl);

    /**
     * 初始化机器人
     * @param deviceId  机器人id
     * @return
     */
    int InitializeRobot(String deviceId,String baseUrl);

    /**
     * 一键到达指定位置
     * @param deviceId
     * @param posX
     * @param posY
     * @param angle
     * @param baseUrl
     * @return
     */
    int OneClickArrival(String deviceId, String posX, String posY, String angle, String baseUrl);
}
