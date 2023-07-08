package com.tunnel.business.mapper.informationBoard;


import com.tunnel.business.domain.informationBoard.IotBoardReleaseLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 情报板内容发布日志Mapper接口
 *
 * @author wangyaozong
 * @date 2020-06-01
 */
public interface IotBoardReleaseLogMapper
{
    /**
     * 查询情报板内容发布日志
     *
     * @param id 情报板内容发布日志ID
     * @return 情报板内容发布日志
     */
    public IotBoardReleaseLog selectIotBoardReleaseLogById(Long id);

    /**
     * 查询情报板内容发布日志列表
     *
     * @param iotBoardReleaseLog 情报板内容发布日志
     * @return 情报板内容发布日志集合
     */
    public List<IotBoardReleaseLog> selectIotBoardReleaseLogList(IotBoardReleaseLog iotBoardReleaseLog);

    /**
     * 新增情报板内容发布日志
     *
     * @param iotBoardReleaseLog 情报板内容发布日志
     * @return 结果
     */
    public int insertIotBoardReleaseLog(IotBoardReleaseLog iotBoardReleaseLog);

    /**
     * 修改情报板内容发布日志
     *
     * @param iotBoardReleaseLog 情报板内容发布日志
     * @return 结果
     */
    public int updateIotBoardReleaseLog(IotBoardReleaseLog iotBoardReleaseLog);

    /**
     * 删除情报板内容发布日志
     *
     * @param id 情报板内容发布日志ID
     * @return 结果
     */
    public int deleteIotBoardReleaseLogById(Long id);

    /**
     * 批量删除情报板内容发布日志
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteIotBoardReleaseLogByIds(String[] ids);

    public List<IotBoardReleaseLog> getLastReleaseLogsByDeviceId(@Param("deviceId") String deviceId);

    /**
     * 查询情报板日志记录(用于恢复)
     * @return
     */
    IotBoardReleaseLog selectBoardContentList(@Param("deviceId") String deviceId);
}
