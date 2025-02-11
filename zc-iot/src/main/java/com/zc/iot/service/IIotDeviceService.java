package com.zc.iot.service;

import java.util.List;

import com.google.gson.JsonObject;
import com.zc.iot.domain.*;

/**
 * 设备Service接口
 * 
 * @author Athena-xiepufeng
 * @date 2021-11-04
 */
public interface IIotDeviceService 
{
    /**
     * 查询设备
     * 
     * @param id 设备主键
     * @return 设备
     */
    public IotDevice selectIotDeviceById(Long id);

    /**
     * 查询设备列表
     * 
     * @param iotDevice 设备
     * @return 设备集合
     */
    public List<IotDevice> selectIotDeviceList(IotDevice iotDevice);

    /**
     * 新增设备
     * 
     * @param iotDevice 设备
     * @return 结果
     */
    public int insertIotDevice(IotDevice iotDevice);

    /**
     * 修改设备
     * 
     * @param iotDevice 设备
     * @return 结果
     */
    public int updateIotDevice(IotDevice iotDevice);

    /**
     * 批量删除设备
     * 
     * @param ids 需要删除的设备主键集合
     * @return 结果
     */
    public int deleteIotDeviceByIds(Long[] ids);

    /**
     * 删除设备信息
     * 
     * @param id 设备主键
     * @return 结果
     */
    public int deleteIotDeviceById(Long id);

    /**
     * 查询设备功能
     *
     * @param id 设备主键
     * @return 功能集合
     */
    List<IotFunction> selectIotDeviceFunction(Long id);

    /**
     *  功能调试，属性设置
     * @param iotDebugAttribute 属性调试参数
     * @return 请求结果
     */
    boolean setAttribute(IotDebugAttribute iotDebugAttribute);

    /**
     *  功能调试，属性获取
     * @param iotDebug
     * @return 请求结果
     */
    boolean getAttribute(IotDebug iotDebug);


    /**
     *  功能调试，服务调用
     * @param iotDebugServe 服务调式参数
     * @return 请求结果
     */
    boolean invokeService(IotDebugServe iotDebugServe);
}
