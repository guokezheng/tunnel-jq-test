package com.tunnel.business.mapper.informationBoard;


import com.tunnel.business.domain.informationBoard.IotBoardVocabulary;

import java.util.List;

/**
 * 情报板敏感字管理Mapper接口
 * 
 * @author 刘方堃
 * @date 2021-11-29
 */
public interface IotBoardVocabularyMapper
{
    /**
     * 查询情报板敏感字管理
     * 
     * @param id 情报板敏感字管理ID
     * @return 情报板敏感字管理
     */
    public IotBoardVocabulary selectIotBoardVocabularyById(Long id);

    /**
     * 查询情报板敏感字管理列表
     * 
     * @param iotBoardVocabulary 情报板敏感字管理
     * @return 情报板敏感字管理集合
     */
    public List<IotBoardVocabulary> selectIotBoardVocabularyList(IotBoardVocabulary iotBoardVocabulary);

    /**
     * 新增情报板敏感字管理
     * 
     * @param iotBoardVocabulary 情报板敏感字管理
     * @return 结果
     */
    public int insertIotBoardVocabulary(IotBoardVocabulary iotBoardVocabulary);

    /**
     * 修改情报板敏感字管理
     * 
     * @param iotBoardVocabulary 情报板敏感字管理
     * @return 结果
     */
    public int updateIotBoardVocabulary(IotBoardVocabulary iotBoardVocabulary);

    /**
     * 删除情报板敏感字管理
     * 
     * @param id 情报板敏感字管理ID
     * @return 结果
     */
    public int deleteIotBoardVocabularyById(Long id);

    /**
     * 批量删除情报板敏感字管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteIotBoardVocabularyByIds(Long[] ids);
}
