package com.tunnel.platform.mapper.logRecord;

import com.tunnel.platform.domain.logRecord.SdOperationLog;

import java.util.List;

/**
 * 操作日志Mapper接口
 *
 * @author yanghousheng
 * @date 2020-09-03
 */
public interface SdOperationLogMapper
{
    /**
     * 查询操作日志
     *
     * @param id 操作日志ID
     * @return 操作日志
     */
    public SdOperationLog selectSdOperationLogById(Long id);

    /**
     * 查询操作日志列表
     *
     * @param sdOperationLog 操作日志
     * @return 操作日志集合
     */
    public List<SdOperationLog> selectSdOperationLogList(SdOperationLog sdOperationLog);

    /**
     * 新增操作日志
     *
     * @param sdOperationLog 操作日志
     * @return 结果
     */
    public int insertSdOperationLog(SdOperationLog sdOperationLog);

    /**
     * 修改操作日志
     *
     * @param sdOperationLog 操作日志
     * @return 结果
     */
    public int updateSdOperationLog(SdOperationLog sdOperationLog);

    /**
     * 删除操作日志
     *
     * @param id 操作日志ID
     * @return 结果
     */
    public int deleteSdOperationLogById(Long id);

    /**
     * 批量删除操作日志
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdOperationLogByIds(Long[] ids);

    public List<SdOperationLog> selectOprationLogOver3month();
}
