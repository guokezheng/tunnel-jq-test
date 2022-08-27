package com.tunnel.platform.service.logRecord.impl;

import com.tunnel.platform.domain.logRecord.SdOperationLog;
import com.tunnel.platform.mapper.logRecord.SdOperationLogMapper;
import com.tunnel.platform.service.logRecord.ISdOperationLogService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志Service业务层处理
 *
 * @author yanghousheng
 * @date 2020-09-03
 */
@Service
public class SdOperationLogServiceImpl implements ISdOperationLogService
{
    @Autowired
    private SdOperationLogMapper sdOperationLogMapper;

    /**
     * 查询操作日志
     *
     * @param id 操作日志ID
     * @return 操作日志
     */
    @Override
    public SdOperationLog selectSdOperationLogById(Long id)
    {
        return sdOperationLogMapper.selectSdOperationLogById(id);
    }

    /**
     * 查询操作日志列表
     *
     * @param sdOperationLog 操作日志
     * @return 操作日志
     */
    @Override
    public List<SdOperationLog> selectSdOperationLogList(SdOperationLog sdOperationLog)
    {
        return sdOperationLogMapper.selectSdOperationLogList(sdOperationLog);
    }

    /**
     * 新增操作日志
     *
     * @param sdOperationLog 操作日志
     * @return 结果
     */
    @Override
    public int insertSdOperationLog(SdOperationLog sdOperationLog)
    {
        sdOperationLog.setCreateTime(DateUtils.getNowDate());
        return sdOperationLogMapper.insertSdOperationLog(sdOperationLog);
    }

    /**
     * 修改操作日志
     *
     * @param sdOperationLog 操作日志
     * @return 结果
     */
    @Override
    public int updateSdOperationLog(SdOperationLog sdOperationLog)
    {
        sdOperationLog.setUpdateTime(DateUtils.getNowDate());
        return sdOperationLogMapper.updateSdOperationLog(sdOperationLog);
    }

    /**
     * 批量删除操作日志
     *
     * @param ids 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteSdOperationLogByIds(Long[] ids)
    {
        return sdOperationLogMapper.deleteSdOperationLogByIds(ids);
    }

    /**
     * 删除操作日志信息
     *
     * @param id 操作日志ID
     * @return 结果
     */
    @Override
    public int deleteSdOperationLogById(Long id)
    {
        return sdOperationLogMapper.deleteSdOperationLogById(id);
    }

    @Override
    public int deleteOprationLogOver3month() {
        List<SdOperationLog> sdOperationLogs = sdOperationLogMapper.selectOprationLogOver3month();
        if (sdOperationLogs.size() > 0) {
            for (int i = 0;i < sdOperationLogs.size();i++) {
                SdOperationLog sdOperationLog = sdOperationLogs.get(i);
                sdOperationLogMapper.deleteSdOperationLogById(sdOperationLog.getId());
            }
            return 1;
        }
        return 0;
    }
}
