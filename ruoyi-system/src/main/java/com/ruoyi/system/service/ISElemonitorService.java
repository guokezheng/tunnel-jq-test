package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysOperLog;

import java.util.List;
import java.util.Map;

/**
 * 外部系统信息 服务层
 * 
 */
public interface ISElemonitorService
{
    /**
     * 获取能源外部系统信息
     * @return
     */
    List<Map> getExternalSystemEnergy(String tunnelId,String sysName);
}
