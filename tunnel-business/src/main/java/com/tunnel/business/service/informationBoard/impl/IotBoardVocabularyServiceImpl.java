package com.tunnel.business.service.informationBoard.impl;

import com.tunnel.business.domain.informationBoard.IotBoardVocabulary;
import com.tunnel.business.mapper.informationBoard.IotBoardVocabularyMapper;
import com.tunnel.business.service.informationBoard.IIotBoardVocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * 情报板敏感字管理Service业务层处理
 *
 * @author 刘方堃
 * @date 2021-11-29
 */
@Service
public class IotBoardVocabularyServiceImpl implements IIotBoardVocabularyService {
    @Autowired
    private IotBoardVocabularyMapper iotBoardVocabularyMapper;

    /**
     * 查询情报板敏感字管理
     *
     * @param id 情报板敏感字管理ID
     * @return 情报板敏感字管理
     */
    @Override
    public IotBoardVocabulary selectIotBoardVocabularyById(Long id) {
        return iotBoardVocabularyMapper.selectIotBoardVocabularyById(id);
    }

    /**
     * 查询情报板敏感字管理列表
     *
     * @param iotBoardVocabulary 情报板敏感字管理
     * @return 情报板敏感字管理
     */
    @Override
    public List<IotBoardVocabulary> selectIotBoardVocabularyList(IotBoardVocabulary iotBoardVocabulary) {
        return iotBoardVocabularyMapper.selectIotBoardVocabularyList(iotBoardVocabulary);
    }

    /**
     * 新增情报板敏感字管理
     *
     * @param iotBoardVocabulary 情报板敏感字管理
     * @return 结果
     */
    @Override
    public int insertIotBoardVocabulary(IotBoardVocabulary iotBoardVocabulary) {
        return iotBoardVocabularyMapper.insertIotBoardVocabulary(iotBoardVocabulary);
    }

    /**
     * 修改情报板敏感字管理
     *
     * @param sdAuditVocabulary 情报板敏感字管理
     * @return 结果
     */
    @Override
    public int updateIotBoardVocabulary(IotBoardVocabulary iotBoardVocabulary) {
        return iotBoardVocabularyMapper.updateIotBoardVocabulary(iotBoardVocabulary);
    }

    /**
     * 批量删除情报板敏感字管理
     *
     * @param ids 需要删除的情报板敏感字管理ID
     * @return 结果
     */
    @Override
    public int deleteIotBoardVocabularyByIds(Long[] ids) {
        return iotBoardVocabularyMapper.deleteIotBoardVocabularyByIds(ids);
    }

    /**
     * 删除情报板敏感字管理信息
     *
     * @param id 情报板敏感字管理ID
     * @return 结果
     */
    @Override
    public int deleteIotBoardVocabularyById(Long id) {
        return iotBoardVocabularyMapper.deleteIotBoardVocabularyById(id);
    }

    @Override
    public int checkIotBoardContent(String content) {
        if (content == null || content.equals("")) {
            return 2;
        } else {
            Boolean flag = false;
            List<IotBoardVocabulary> iotBoardVocabularies = iotBoardVocabularyMapper.selectIotBoardVocabularyList(null);
            try {
                content = URLDecoder.decode(content, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
            for (int g = 0;g < iotBoardVocabularies.size();g++) {
                String word = iotBoardVocabularies.get(g).getWord();
                if (content.contains(word)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                return 0;
            }
        }
        return 1;
    }
}
