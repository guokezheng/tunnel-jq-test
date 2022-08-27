package com.tunnel.platform.service.informationBoard.impl;

import com.tunnel.platform.domain.informationBoard.SdAuditVocabulary;
import com.tunnel.platform.mapper.informationBoard.SdAuditVocabularyMapper;
import com.tunnel.platform.service.informationBoard.ISdAuditVocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 情报板敏感字管理Service业务层处理
 * 
 * @author 刘方堃
 * @date 2021-11-29
 */
@Service
public class SdAuditVocabularyServiceImpl implements ISdAuditVocabularyService
{
    @Autowired
    private SdAuditVocabularyMapper sdAuditVocabularyMapper;

    /**
     * 查询情报板敏感字管理
     * 
     * @param id 情报板敏感字管理ID
     * @return 情报板敏感字管理
     */
    @Override
    public SdAuditVocabulary selectSdAuditVocabularyById(Long id)
    {
        return sdAuditVocabularyMapper.selectSdAuditVocabularyById(id);
    }

    /**
     * 查询情报板敏感字管理列表
     * 
     * @param sdAuditVocabulary 情报板敏感字管理
     * @return 情报板敏感字管理
     */
    @Override
    public List<SdAuditVocabulary> selectSdAuditVocabularyList(SdAuditVocabulary sdAuditVocabulary)
    {
        return sdAuditVocabularyMapper.selectSdAuditVocabularyList(sdAuditVocabulary);
    }

    /**
     * 新增情报板敏感字管理
     * 
     * @param sdAuditVocabulary 情报板敏感字管理
     * @return 结果
     */
    @Override
    public int insertSdAuditVocabulary(SdAuditVocabulary sdAuditVocabulary)
    {
        return sdAuditVocabularyMapper.insertSdAuditVocabulary(sdAuditVocabulary);
    }

    /**
     * 修改情报板敏感字管理
     * 
     * @param sdAuditVocabulary 情报板敏感字管理
     * @return 结果
     */
    @Override
    public int updateSdAuditVocabulary(SdAuditVocabulary sdAuditVocabulary)
    {
        return sdAuditVocabularyMapper.updateSdAuditVocabulary(sdAuditVocabulary);
    }

    /**
     * 批量删除情报板敏感字管理
     * 
     * @param ids 需要删除的情报板敏感字管理ID
     * @return 结果
     */
    @Override
    public int deleteSdAuditVocabularyByIds(Long[] ids)
    {
        return sdAuditVocabularyMapper.deleteSdAuditVocabularyByIds(ids);
    }

    /**
     * 删除情报板敏感字管理信息
     * 
     * @param id 情报板敏感字管理ID
     * @return 结果
     */
    @Override
    public int deleteSdAuditVocabularyById(Long id)
    {
        return sdAuditVocabularyMapper.deleteSdAuditVocabularyById(id);
    }
}
