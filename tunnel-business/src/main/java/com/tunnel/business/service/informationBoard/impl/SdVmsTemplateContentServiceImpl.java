package com.tunnel.business.service.informationBoard.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tunnel.business.domain.informationBoard.SdVmsTemplateContent;
import com.tunnel.business.mapper.informationBoard.SdVmsTemplateContentMapper;
import com.tunnel.business.service.informationBoard.ISdVmsTemplateContentService;
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
public class SdVmsTemplateContentServiceImpl implements ISdVmsTemplateContentService {
    @Autowired
    private SdVmsTemplateContentMapper sdVmsTemplateContentMapper;

    /**
     * 查询发布模板内容
     *
     * @param id 发布模板内容主键
     * @return 发布模板内容
     */
    @Override
    public SdVmsTemplateContent selectSdVmsTemplateContentById(Long id) {
        return sdVmsTemplateContentMapper.selectSdVmsTemplateContentById(id);
    }

    /**
     * 查询发布模板内容列表
     *
     * @param sdVmsTemplateContent 发布模板内容
     * @return 发布模板内容
     */
    @Override
    public List<SdVmsTemplateContent> selectSdVmsTemplateContentList(SdVmsTemplateContent sdVmsTemplateContent) {
        return sdVmsTemplateContentMapper.selectSdVmsTemplateContentList(sdVmsTemplateContent);
    }

    /**
     * 新增发布模板内容
     *
     * @param sdVmsTemplateContent 发布模板内容
     * @return 结果
     */
    @Override
    public int insertSdVmsTemplateContent(JSONObject jsonObject) {
        if (!jsonObject.isEmpty()) {
            String templateId = jsonObject.getJSONObject("templateId").get("data").toString();
            JSONArray templateContent = jsonObject.getJSONArray("templateContent");
            if (templateContent.size() > 0) {
                int count = 0;
                for (int i = 0; i < templateContent.size(); i++) {
                    JSONObject tempContent = templateContent.getJSONObject(i);
                    SdVmsTemplateContent sdVmsTemplateContent = new SdVmsTemplateContent();
                    sdVmsTemplateContent.setTemplateId(templateId);
                    sdVmsTemplateContent.setContent(tempContent.get("content").toString());
                    sdVmsTemplateContent.setCoordinate(tempContent.get("coordinate").toString());
                    sdVmsTemplateContent.setFontColor(tempContent.get("fontColor").toString());
                    sdVmsTemplateContent.setFontSize(Long.parseLong(tempContent.get("fontSize").toString()));
                    sdVmsTemplateContent.setFontType(tempContent.get("fontType").toString());
                    sdVmsTemplateContent.setFontSpacing(Long.parseLong(tempContent.get("fontSpacing").toString()));
                    /*   sdVmsTemplateContent.setImageUrl(tempContent.get("img").toString());*/
                    sdVmsTemplateContentMapper.insertSdVmsTemplateContent(sdVmsTemplateContent);
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
     * @param sdVmsTemplateContent 发布模板内容
     * @return 结果
     */
    @Override
    public int updateSdVmsTemplateContent(JSONObject jsonObject) {
        if (!jsonObject.isEmpty()) {
            String templateId = jsonObject.get("templateId").toString();
            JSONArray templateContent = jsonObject.getJSONArray("templateContent");
            if (templateContent.size() > 0) {
                int count = 0;
                for (int i = 0; i < templateContent.size(); i++) {
                    JSONObject tempContent = templateContent.getJSONObject(i);
                    SdVmsTemplateContent sdVmsTemplateContent = new SdVmsTemplateContent();
                    sdVmsTemplateContent.setId(Long.parseLong(tempContent.get("id").toString()));
                    sdVmsTemplateContent.setTemplateId(templateId);
                    sdVmsTemplateContent.setContent(tempContent.get("content").toString());
                    sdVmsTemplateContent.setCoordinate(tempContent.get("coordinate").toString());
                    sdVmsTemplateContent.setFontColor(tempContent.get("fontColor").toString());
                    sdVmsTemplateContent.setFontSize(Long.parseLong(tempContent.get("fontSize").toString()));
                    sdVmsTemplateContent.setFontType(tempContent.get("fontType").toString());
                    sdVmsTemplateContent.setFontSpacing(Long.parseLong(tempContent.get("fontSpacing").toString()));
               /*     if(tempContent.containsKey("img") && tempContent.get("img") != null){
                    	sdVmsTemplateContent.setImageUrl(tempContent.get("img").toString());
                    }*/

                    sdVmsTemplateContentMapper.updateSdVmsTemplateContent(sdVmsTemplateContent);
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
        return sdVmsTemplateContentMapper.deleteSdVmsTemplateContentByIds(ids);
    }

    /**
     * 删除发布模板内容信息
     *
     * @param id 发布模板内容主键
     * @return 结果
     */
    @Override
    public int deleteSdVmsTemplateContentById(Long id) {
        return sdVmsTemplateContentMapper.deleteSdVmsTemplateContentById(id);
    }
}
