package com.tunnel.platform.service.informationBoard.impl;

import com.tunnel.platform.domain.informationBoard.IotBoardReleaseLog;
import com.tunnel.platform.mapper.informationBoard.IotBoardReleaseLogMapper;
import com.tunnel.platform.service.informationBoard.IIotBoardReleaseLogService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 情报板内容发布日志Service业务层处理
 * 
 * @author wangyaozong
 * @date 2020-06-01
 */
@Service
public class IotBoardReleaseLogServiceImpl implements IIotBoardReleaseLogService
{
    @Autowired
    private IotBoardReleaseLogMapper iotBoardReleaseLogMapper;

    /**
     * 查询情报板内容发布日志
     * 
     * @param id 情报板内容发布日志ID
     * @return 情报板内容发布日志
     */
    @Override
    public IotBoardReleaseLog selectIotBoardReleaseLogById(Long id)
    {
        return iotBoardReleaseLogMapper.selectIotBoardReleaseLogById(id);
    }

    /**
     * 查询情报板内容发布日志列表
     * 
     * @param iotBoardReleaseLog 情报板内容发布日志
     * @return 情报板内容发布日志
     */
    @Override
    public List<IotBoardReleaseLog> selectIotBoardReleaseLogList(IotBoardReleaseLog iotBoardReleaseLog)
    {
        return iotBoardReleaseLogMapper.selectIotBoardReleaseLogList(iotBoardReleaseLog);
    }

    /**
     * 新增情报板内容发布日志
     * 
     * @param iotBoardReleaseLog 情报板内容发布日志
     * @return 结果
     */
    @Override
    public int insertIotBoardReleaseLog(IotBoardReleaseLog iotBoardReleaseLog)
    {
        return iotBoardReleaseLogMapper.insertIotBoardReleaseLog(iotBoardReleaseLog);
    }

    /**
     * 修改情报板内容发布日志
     * 
     * @param iotBoardReleaseLog 情报板内容发布日志
     * @return 结果
     */
    @Override
    public int updateIotBoardReleaseLog(IotBoardReleaseLog iotBoardReleaseLog)
    {
        return iotBoardReleaseLogMapper.updateIotBoardReleaseLog(iotBoardReleaseLog);
    }

    /**
     * 删除情报板内容发布日志对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteIotBoardReleaseLogByIds(String ids)
    {
        return iotBoardReleaseLogMapper.deleteIotBoardReleaseLogByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除情报板内容发布日志信息
     * 
     * @param id 情报板内容发布日志ID
     * @return 结果
     */
    @Override
    public int deleteIotBoardReleaseLogById(Long id)
    {
        return iotBoardReleaseLogMapper.deleteIotBoardReleaseLogById(id);
    }
}
