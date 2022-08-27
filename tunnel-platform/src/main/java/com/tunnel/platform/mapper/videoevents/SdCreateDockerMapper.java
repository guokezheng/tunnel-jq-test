package com.tunnel.platform.mapper.videoevents;


import com.tunnel.platform.domain.videoevents.SdCreateDocker;

import java.util.List;

/**
 * 指令码固定字段表Mapper接口
 * 
 * @author ruoyi
 * @date 2021-04-24
 */
public interface SdCreateDockerMapper 
{
    /**
     * 查询指令码固定字段表
     * 
     * @param id 指令码固定字段表ID
     * @return 指令码固定字段表
     */
    public SdCreateDocker selectSdCreateDockerById(Integer id);

    /**
     * 查询指令码固定字段表列表
     * 
     * @param sdCreateDocker 指令码固定字段表
     * @return 指令码固定字段表集合
     */
    public List<SdCreateDocker> selectSdCreateDockerList(SdCreateDocker sdCreateDocker);

    /**
     * 新增指令码固定字段表
     * 
     * @param sdCreateDocker 指令码固定字段表
     * @return 结果
     */
    public int insertSdCreateDocker(SdCreateDocker sdCreateDocker);

    /**
     * 修改指令码固定字段表
     * 
     * @param sdCreateDocker 指令码固定字段表
     * @return 结果
     */
    public int updateSdCreateDocker(SdCreateDocker sdCreateDocker);

    /**
     * 删除指令码固定字段表
     * 
     * @param id 指令码固定字段表ID
     * @return 结果
     */
    public int deleteSdCreateDockerById(Integer id);

    /**
     * 批量删除指令码固定字段表
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdCreateDockerByIds(Integer[] ids);
}
