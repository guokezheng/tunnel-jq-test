package com.tunnel.business.service.informationBoard;


import com.tunnel.business.domain.informationBoard.SdAuditVocabulary;

import java.util.List;

/**
 * 情报板敏感字管理Service接口
 *
 * @author 刘方堃
 * @date 2021-11-29
 */
public interface ISdAuditVocabularyService {
    /**
     * 查询情报板敏感字管理
     *
     * @param id 情报板敏感字管理ID
     * @return 情报板敏感字管理
     */
    SdAuditVocabulary selectSdAuditVocabularyById(Long id);

    /**
     * 查询情报板敏感字管理列表
     *
     * @param sdAuditVocabulary 情报板敏感字管理
     * @return 情报板敏感字管理集合
     */
    List<SdAuditVocabulary> selectSdAuditVocabularyList(SdAuditVocabulary sdAuditVocabulary);

    /**
     * 新增情报板敏感字管理
     *
     * @param sdAuditVocabulary 情报板敏感字管理
     * @return 结果
     */
    int insertSdAuditVocabulary(SdAuditVocabulary sdAuditVocabulary);

    /**
     * 修改情报板敏感字管理
     *
     * @param sdAuditVocabulary 情报板敏感字管理
     * @return 结果
     */
    int updateSdAuditVocabulary(SdAuditVocabulary sdAuditVocabulary);

    /**
     * 批量删除情报板敏感字管理
     *
     * @param ids 需要删除的情报板敏感字管理ID
     * @return 结果
     */
    int deleteSdAuditVocabularyByIds(Long[] ids);

    /**
     * 删除情报板敏感字管理信息
     *
     * @param id 情报板敏感字管理ID
     * @return 结果
     */
    int deleteSdAuditVocabularyById(Long id);
}
