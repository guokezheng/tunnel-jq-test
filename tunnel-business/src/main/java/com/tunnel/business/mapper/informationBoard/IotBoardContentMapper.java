package com.tunnel.business.mapper.informationBoard;


import com.tunnel.business.domain.informationBoard.IotBoardContent;

import java.util.List;

/**
 * 情报板常用发布信息Mapper接口
 * 
 * @author wangyaozong
 * @date 2020-04-29
 */
public interface IotBoardContentMapper 
{
    /**
     * 查询情报板常用发布信息
     * 
     * @param id 情报板常用发布信息ID
     * @return 情报板常用发布信息
     */
    public IotBoardContent selectIotBoardContentById(Long id);

    /**
     * 查询情报板常用发布信息列表
     * 
     * @param iotBoardContent 情报板常用发布信息
     * @return 情报板常用发布信息集合
     */
    public List<IotBoardContent> selectIotBoardContentList(IotBoardContent iotBoardContent);

    /**
     * 新增情报板常用发布信息
     * 
     * @param iotBoardContent 情报板常用发布信息
     * @return 结果
     */
    public int insertIotBoardContent(IotBoardContent iotBoardContent);

    /**
     * 修改情报板常用发布信息
     * 
     * @param iotBoardContent 情报板常用发布信息
     * @return 结果
     */
    public int updateIotBoardContent(IotBoardContent iotBoardContent);

    /**
     * 删除情报板常用发布信息
     * 
     * @param id 情报板常用发布信息ID
     * @return 结果
     */
    public int deleteIotBoardContentById(Long id);

    /**
     * 批量删除情报板常用发布信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteIotBoardContentByIds(String[] ids);
}
