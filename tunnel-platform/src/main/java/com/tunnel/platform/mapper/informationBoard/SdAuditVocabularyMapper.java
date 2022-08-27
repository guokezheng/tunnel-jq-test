package com.tunnel.platform.mapper.informationBoard;

import com.tunnel.platform.domain.informationBoard.SdAuditVocabulary;

import java.util.List;

/**
 * 情报板敏感字管理Mapper接口
 * 
 * @author 刘方堃
 * @date 2021-11-29
 */
public interface SdAuditVocabularyMapper 
{
    /**
     * 查询情报板敏感字管理
     * 
     * @param id 情报板敏感字管理ID
     * @return 情报板敏感字管理
     */
    public SdAuditVocabulary selectSdAuditVocabularyById(Long id);

    /**
     * 查询情报板敏感字管理列表
     * 
     * @param sdAuditVocabulary 情报板敏感字管理
     * @return 情报板敏感字管理集合
     */
    public List<SdAuditVocabulary> selectSdAuditVocabularyList(SdAuditVocabulary sdAuditVocabulary);

    /**
     * 新增情报板敏感字管理
     * 
     * @param sdAuditVocabulary 情报板敏感字管理
     * @return 结果
     */
    public int insertSdAuditVocabulary(SdAuditVocabulary sdAuditVocabulary);

    /**
     * 修改情报板敏感字管理
     * 
     * @param sdAuditVocabulary 情报板敏感字管理
     * @return 结果
     */
    public int updateSdAuditVocabulary(SdAuditVocabulary sdAuditVocabulary);

    /**
     * 删除情报板敏感字管理
     * 
     * @param id 情报板敏感字管理ID
     * @return 结果
     */
    public int deleteSdAuditVocabularyById(Long id);

    /**
     * 批量删除情报板敏感字管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdAuditVocabularyByIds(Long[] ids);
}
