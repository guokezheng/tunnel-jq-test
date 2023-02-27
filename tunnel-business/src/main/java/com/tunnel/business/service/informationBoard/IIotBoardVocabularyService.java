package com.tunnel.business.service.informationBoard;


import com.tunnel.business.domain.informationBoard.IotBoardVocabulary;

import java.util.List;
import java.util.Map;

/**
 * 情报板敏感字管理Service接口
 *
 * @author 刘方堃
 * @date 2021-11-29
 */
public interface IIotBoardVocabularyService {
    /**
     * 查询情报板敏感字管理
     *
     * @param id 情报板敏感字管理ID
     * @return 情报板敏感字管理
     */
    IotBoardVocabulary selectIotBoardVocabularyById(Long id);

    /**
     * 查询情报板敏感字管理列表
     *
     * @param iotBoardVocabulary 情报板敏感字管理
     * @return 情报板敏感字管理集合
     */
    List<IotBoardVocabulary> selectIotBoardVocabularyList(IotBoardVocabulary iotBoardVocabulary);

    /**
     * 新增情报板敏感字管理
     *
     * @param iotBoardVocabulary 情报板敏感字管理
     * @return 结果
     */
    int insertIotBoardVocabulary(IotBoardVocabulary iotBoardVocabulary);

    /**
     * 修改情报板敏感字管理
     *
     * @param iotBoardVocabulary 情报板敏感字管理
     * @return 结果
     */
    int updateIotBoardVocabulary(IotBoardVocabulary iotBoardVocabulary);

    /**
     * 批量删除情报板敏感字管理
     *
     * @param ids 需要删除的情报板敏感字管理ID
     * @return 结果
     */
    int deleteIotBoardVocabularyByIds(Long[] ids);

    /**
     * 删除情报板敏感字管理信息
     *
     * @param id 情报板敏感字管理ID
     * @return 结果
     */
    int deleteIotBoardVocabularyById(Long id);

    int checkIotBoardContent(Map<String, Object> map);
}
