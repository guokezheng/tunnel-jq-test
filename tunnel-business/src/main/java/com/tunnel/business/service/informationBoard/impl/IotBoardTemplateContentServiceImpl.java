package com.tunnel.business.service.informationBoard.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.domain.informationBoard.IotBoardTemplateContent;
import com.tunnel.business.domain.informationBoard.IotBoardVocabulary;
import com.tunnel.business.mapper.informationBoard.IotBoardTemplateContentMapper;
import com.tunnel.business.service.informationBoard.IIotBoardVocabularyService;
import com.tunnel.business.service.informationBoard.IIotBoardTemplateContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 发布模板内容Service业务层处理
 *
 * @author ruoyi
 * @date 2022-03-22
 */
@Service
public class IotBoardTemplateContentServiceImpl implements IIotBoardTemplateContentService {
    @Autowired
    private IotBoardTemplateContentMapper iotBoardTemplateContentMapper;
    @Autowired
    private IIotBoardVocabularyService iotBoardVocabularyService;

    /**
     * 查询发布模板内容
     *
     * @param id 发布模板内容主键
     * @return 发布模板内容
     */
    @Override
    public IotBoardTemplateContent selectSdVmsTemplateContentById(Long id) {
        return iotBoardTemplateContentMapper.selectSdVmsTemplateContentById(id);
    }

    /**
     * 查询发布模板内容列表
     *
     * @param iotBoardTemplateContent 发布模板内容
     * @return 发布模板内容
     */
    @Override
    public List<IotBoardTemplateContent> selectSdVmsTemplateContentList(IotBoardTemplateContent iotBoardTemplateContent) {
        return iotBoardTemplateContentMapper.selectSdVmsTemplateContentList(iotBoardTemplateContent);
    }

    /**
     * 新增发布模板内容
     *
     * @param iotBoardTemplateContent 发布模板内容
     * @return 结果
     */
    @Override
    public int insertSdVmsTemplateContent(JSONObject jsonObject) {
        if (!jsonObject.isEmpty()) {
            String templateId = jsonObject.getJSONObject("templateId").get("data").toString();
            JSONArray templateContent = jsonObject.getJSONArray("templateContent");
            Boolean flag = false;
            List<IotBoardVocabulary> iotBoardVocabularies = iotBoardVocabularyService.selectIotBoardVocabularyList(null);
            if (templateContent.size() > 0) {
                int count = 0;
                for (int i = 0; i < templateContent.size(); i++) {
                    JSONObject tempContent = templateContent.getJSONObject(i);
                    IotBoardTemplateContent iotBoardTemplateContent = new IotBoardTemplateContent();
                    iotBoardTemplateContent.setTemplateId(templateId);
                    String content = tempContent.get("content").toString();
                    String word = "";
                    for (int g = 0;g < iotBoardVocabularies.size();g++) {
                        word = iotBoardVocabularies.get(i).getWord();
                        if (!word.equals("") && content.contains(word)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        content = content.replaceAll(word, "");
                    }
                    iotBoardTemplateContent.setContent(content);
                    iotBoardTemplateContent.setCoordinate(tempContent.get("coordinate").toString());
                    iotBoardTemplateContent.setFontColor(tempContent.get("fontColor").toString());
                    iotBoardTemplateContent.setFontSize(Long.parseLong(tempContent.get("fontSize").toString()));
                    iotBoardTemplateContent.setFontType(tempContent.get("fontType").toString());
                    iotBoardTemplateContent.setFontSpacing(Long.parseLong(tempContent.get("fontSpacing").toString()));
                    /*   iotBoardTemplateContent.setImageUrl(tempContent.get("img").toString());*/
                    iotBoardTemplateContentMapper.insertSdVmsTemplateContent(iotBoardTemplateContent);
                    count++;
                }
                return count;
            }
        }
        return 0;
    }

    /**
     * 修改发布模板内容
     *
     * @param jsonObject 发布模板内容
     * @return 结果
     */
    @Override
    public int updateSdVmsTemplateContent(JSONObject jsonObject) {
        if (!jsonObject.isEmpty()) {
            String templateId = jsonObject.get("templateId").toString();
            JSONArray templateContent = jsonObject.getJSONArray("templateContent");
            JSONArray templateDelContent = jsonObject.getJSONArray("templateDelContent");
            if(templateDelContent.size() > 0){
                for(int i = 0; i < templateDelContent.size(); i++){
                    JSONObject jsonObject1 = templateDelContent.getJSONObject(i);
                    iotBoardTemplateContentMapper.deleteSdVmsTemplateContentById(Long.valueOf(jsonObject1.get("id").toString()));
                }
            }
            if (templateContent.size() > 0) {
                int count = 0;
                Boolean flag = false;
                List<IotBoardVocabulary> iotBoardVocabularies = iotBoardVocabularyService.selectIotBoardVocabularyList(null);
                for (int i = 0; i < templateContent.size(); i++) {
                    JSONObject tempContent = templateContent.getJSONObject(i);
                    IotBoardTemplateContent iotBoardTemplateContent = new IotBoardTemplateContent();
                    iotBoardTemplateContent.setTemplateId(templateId);
                    String content = tempContent.get("content").toString();
                    String word = "";
                    for (int g = 0;g < iotBoardVocabularies.size();g++) {
                        word = iotBoardVocabularies.get(i).getWord();
                        if (!word.equals("") && content.contains(word)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        content = content.replaceAll(word, "");
                    }
                    iotBoardTemplateContent.setContent(content);
                    iotBoardTemplateContent.setCoordinate(tempContent.get("coordinate").toString());
                    iotBoardTemplateContent.setFontColor(tempContent.get("fontColor").toString());
                    iotBoardTemplateContent.setFontSize(Long.parseLong(tempContent.get("fontSize").toString()));
                    iotBoardTemplateContent.setFontType(tempContent.get("fontType").toString());
                    iotBoardTemplateContent.setFontSpacing(Long.parseLong(tempContent.get("fontSpacing").toString()));
               /*     if(tempContent.containsKey("img") && tempContent.get("img") != null){
                    	iotBoardTemplateContent.setImageUrl(tempContent.get("img").toString());
                    }*/
                    Object id = tempContent.get("id");
                    if(StringUtils.isNotNull(id)){
                        iotBoardTemplateContent.setId(Long.parseLong(tempContent.get("id").toString()));
                        iotBoardTemplateContentMapper.updateSdVmsTemplateContent(iotBoardTemplateContent);
                    }else {
                        iotBoardTemplateContentMapper.insertSdVmsTemplateContent(iotBoardTemplateContent);
                    }
                    count++;
                }
                return count;
            }
        }
        return 0;
    }

    /**
     * 批量删除发布模板内容
     *
     * @param ids 需要删除的发布模板内容主键
     * @return 结果
     */
    @Override
    public int deleteSdVmsTemplateContentByIds(Long[] ids) {
        return iotBoardTemplateContentMapper.deleteSdVmsTemplateContentByIds(ids);
    }

    /**
     * 删除发布模板内容信息
     *
     * @param id 发布模板内容主键
     * @return 结果
     */
    @Override
    public int deleteSdVmsTemplateContentById(Long id) {
        return iotBoardTemplateContentMapper.deleteSdVmsTemplateContentById(id);
    }
}
