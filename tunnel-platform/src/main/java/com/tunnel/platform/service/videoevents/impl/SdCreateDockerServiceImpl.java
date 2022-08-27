package com.tunnel.platform.service.videoevents.impl;

import com.tunnel.platform.domain.videoevents.SdCreateDocker;
import com.tunnel.platform.mapper.videoevents.SdCreateDockerMapper;
import com.tunnel.platform.service.videoevents.ISdCreateDockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 指令码固定字段表Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-04-24
 */
@Service
public class SdCreateDockerServiceImpl implements ISdCreateDockerService
{
    @Autowired
    private SdCreateDockerMapper sdCreateDockerMapper;

    /**
     * 查询指令码固定字段表
     * 
     * @param id 指令码固定字段表ID
     * @return 指令码固定字段表
     */
    @Override
    public SdCreateDocker selectSdCreateDockerById(Integer id)
    {
        return sdCreateDockerMapper.selectSdCreateDockerById(id);
    }

    /**
     * 查询指令码固定字段表列表
     * 
     * @param sdCreateDocker 指令码固定字段表
     * @return 指令码固定字段表
     */
    @Override
    public List<SdCreateDocker> selectSdCreateDockerList(SdCreateDocker sdCreateDocker)
    {
        return sdCreateDockerMapper.selectSdCreateDockerList(sdCreateDocker);
    }

    /**
     * 新增指令码固定字段表
     * 
     * @param sdCreateDocker 指令码固定字段表
     * @return 结果
     */
    @Override
    public int insertSdCreateDocker(SdCreateDocker sdCreateDocker)
    {
        return sdCreateDockerMapper.insertSdCreateDocker(sdCreateDocker);
    }

    /**
     * 修改指令码固定字段表
     * 
     * @param sdCreateDocker 指令码固定字段表
     * @return 结果
     */
    @Override
    public int updateSdCreateDocker(SdCreateDocker sdCreateDocker)
    {
        return sdCreateDockerMapper.updateSdCreateDocker(sdCreateDocker);
    }

    /**
     * 批量删除指令码固定字段表
     * 
     * @param ids 需要删除的指令码固定字段表ID
     * @return 结果
     */
    @Override
    public int deleteSdCreateDockerByIds(Integer[] ids)
    {
        return sdCreateDockerMapper.deleteSdCreateDockerByIds(ids);
    }

    /**
     * 删除指令码固定字段表信息
     * 
     * @param id 指令码固定字段表ID
     * @return 结果
     */
    @Override
    public int deleteSdCreateDockerById(Integer id)
    {
        return sdCreateDockerMapper.deleteSdCreateDockerById(id);
    }
}
