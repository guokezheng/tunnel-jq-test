package com.ruoyi.system.service.impl;

import com.ruoyi.system.mapper.SysElemonitorMapper;
import com.ruoyi.system.service.ISElemonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 外部系统信息 服务层处理
 * 
 */
@Service
public class SysElemonitorServiceImpl implements ISElemonitorService
{
    @Autowired
    private SysElemonitorMapper sysElemonitorMapper;

    /**
     * 获取能源外部系统信息
     * @return
     */
    @Override
    public List<Map> getExternalSystemEnergy(String tunnelId,String sysName) {
        return sysElemonitorMapper.getExternalSystemEnergy(tunnelId,sysName);
    }
}
