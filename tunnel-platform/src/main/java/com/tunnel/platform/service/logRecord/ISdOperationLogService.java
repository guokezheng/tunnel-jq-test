package com.tunnel.platform.service.logRecord;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.platform.domain.logRecord.SdOperationLog;

import java.util.List;

/**
 * 操作日志Service接口
 *
 * @author yanghousheng
 * @date 2020-09-03
 */
public interface ISdOperationLogService
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
     * 批量删除操作日志
     *
     * @param ids 需要删除的操作日志ID
     * @return 结果
     */
    public int deleteSdOperationLogByIds(Long[] ids);

    /**
     * 删除操作日志信息
     *
     * @param id 操作日志ID
     * @return 结果
     */
    public int deleteSdOperationLogById(Long id);

    /**
     * 定时清理3个月前的操作日志
     */
    public int deleteOprationLogOver3month();

    /**
     * 新增时 设备执行记录接口 websocket推送
     * @param sdOperationLog
     * @return
     */
    public AjaxResult operationLog(SdOperationLog sdOperationLog);
}
