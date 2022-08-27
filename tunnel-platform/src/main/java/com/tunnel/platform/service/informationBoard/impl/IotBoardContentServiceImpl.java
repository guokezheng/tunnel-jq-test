package com.tunnel.platform.service.informationBoard.impl;

import com.tunnel.platform.domain.informationBoard.IotBoardContent;
import com.tunnel.platform.mapper.informationBoard.IotBoardContentMapper;
import com.tunnel.platform.service.informationBoard.IIotBoardContentService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 情报板常用发布信息Service业务层处理
 * 
 * @author wangyaozong
 * @date 2020-04-29
 */
@Service
public class IotBoardContentServiceImpl implements IIotBoardContentService
{
    @Autowired
    private IotBoardContentMapper iotBoardContentMapper;

    /**
     * 查询情报板常用发布信息
     * 
     * @param id 情报板常用发布信息ID
     * @return 情报板常用发布信息
     */
    @Override
    public IotBoardContent selectIotBoardContentById(Long id)
    {
        return iotBoardContentMapper.selectIotBoardContentById(id);
    }

    /**
     * 查询情报板常用发布信息列表
     * 
     * @param iotBoardContent 情报板常用发布信息
     * @return 情报板常用发布信息
     */
    @Override
    public List<IotBoardContent> selectIotBoardContentList(IotBoardContent iotBoardContent)
    {
        return iotBoardContentMapper.selectIotBoardContentList(iotBoardContent);
    }

    /**
     * 新增情报板常用发布信息
     * 
     * @param iotBoardContent 情报板常用发布信息
     * @return 结果
     */
    @Override
    public int insertIotBoardContent(IotBoardContent iotBoardContent)
    {
        return iotBoardContentMapper.insertIotBoardContent(iotBoardContent);
    }

    /**
     * 修改情报板常用发布信息
     * 
     * @param iotBoardContent 情报板常用发布信息
     * @return 结果
     */
    @Override
    public int updateIotBoardContent(IotBoardContent iotBoardContent)
    {
        return iotBoardContentMapper.updateIotBoardContent(iotBoardContent);
    }

    /**
     * 删除情报板常用发布信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteIotBoardContentByIds(String ids)
    {
        return iotBoardContentMapper.deleteIotBoardContentByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除情报板常用发布信息信息
     * 
     * @param id 情报板常用发布信息ID
     * @return 结果
     */
    @Override
    public int deleteIotBoardContentById(Long id)
    {
        return iotBoardContentMapper.deleteIotBoardContentById(id);
    }
}
