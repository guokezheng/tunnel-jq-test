package com.tunnel.business.service.videoevents;


import com.tunnel.business.domain.videoevents.SdCreateDocker;

import java.util.List;

/**
 * 指令码固定字段表Service接口
 *
 * @author ruoyi
 * @date 2021-04-24
 */
public interface ISdCreateDockerService {
    /**
     * 查询指令码固定字段表
     *
     * @param id 指令码固定字段表ID
     * @return 指令码固定字段表
     */
    SdCreateDocker selectSdCreateDockerById(Integer id);

    /**
     * 查询指令码固定字段表列表
     *
     * @param sdCreateDocker 指令码固定字段表
     * @return 指令码固定字段表集合
     */
    List<SdCreateDocker> selectSdCreateDockerList(SdCreateDocker sdCreateDocker);

    /**
     * 新增指令码固定字段表
     *
     * @param sdCreateDocker 指令码固定字段表
     * @return 结果
     */
    int insertSdCreateDocker(SdCreateDocker sdCreateDocker);

    /**
     * 修改指令码固定字段表
     *
     * @param sdCreateDocker 指令码固定字段表
     * @return 结果
     */
    int updateSdCreateDocker(SdCreateDocker sdCreateDocker);

    /**
     * 批量删除指令码固定字段表
     *
     * @param ids 需要删除的指令码固定字段表ID
     * @return 结果
     */
    int deleteSdCreateDockerByIds(Integer[] ids);

    /**
     * 删除指令码固定字段表信息
     *
     * @param id 指令码固定字段表ID
     * @return 结果
     */
    int deleteSdCreateDockerById(Integer id);
}
