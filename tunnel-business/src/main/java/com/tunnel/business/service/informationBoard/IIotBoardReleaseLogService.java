package com.tunnel.business.service.informationBoard;


import com.tunnel.business.domain.informationBoard.IotBoardReleaseLog;

import java.util.List;

/**
 * 情报板内容发布日志Service接口
 *
 * @author wangyaozong
 * @date 2020-06-01
 */
public interface IIotBoardReleaseLogService {
    /**
     * 查询情报板内容发布日志
     *
     * @param id 情报板内容发布日志ID
     * @return 情报板内容发布日志
     */
    IotBoardReleaseLog selectIotBoardReleaseLogById(Long id);

    /**
     * 查询情报板内容发布日志列表
     *
     * @param iotBoardReleaseLog 情报板内容发布日志
     * @return 情报板内容发布日志集合
     */
    List<IotBoardReleaseLog> selectIotBoardReleaseLogList(IotBoardReleaseLog iotBoardReleaseLog);

    /**
     * 新增情报板内容发布日志
     *
     * @param iotBoardReleaseLog 情报板内容发布日志
     * @return 结果
     */
    int insertIotBoardReleaseLog(IotBoardReleaseLog iotBoardReleaseLog);

    /**
     * 修改情报板内容发布日志
     *
     * @param iotBoardReleaseLog 情报板内容发布日志
     * @return 结果
     */
    int updateIotBoardReleaseLog(IotBoardReleaseLog iotBoardReleaseLog);

    /**
     * 批量删除情报板内容发布日志
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteIotBoardReleaseLogByIds(String ids);

    /**
     * 删除情报板内容发布日志信息
     *
     * @param id 情报板内容发布日志ID
     * @return 结果
     */
    int deleteIotBoardReleaseLogById(Long id);

    List<IotBoardReleaseLog> getLastReleaseLogsByDeviceId(String deviceId);
}
