package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysOperLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 外部系统 数据层
 * 
 */
public interface SysElemonitorMapper
{
    /**
     * 获取能源外部系统信息
     * @return
     */
    List<Map> getExternalSystemEnergy(@Param("tunnelId") String tunnelId, @Param("sysName")String sysName);
}
