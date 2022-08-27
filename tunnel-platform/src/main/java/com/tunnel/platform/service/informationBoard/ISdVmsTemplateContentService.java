package com.tunnel.platform.service.informationBoard;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.platform.domain.informationBoard.SdVmsTemplateContent;

/**
 * 发布模板内容Service接口
 *
 * @author ruoyi
 * @date 2022-03-22
 */
public interface ISdVmsTemplateContentService
{
    /**
     * 查询发布模板内容
     *
     * @param id 发布模板内容主键
     * @return 发布模板内容
     */
    public SdVmsTemplateContent selectSdVmsTemplateContentById(Long id);

    /**
     * 查询发布模板内容列表
     *
     * @param sdVmsTemplateContent 发布模板内容
     * @return 发布模板内容集合
     */
    public List<SdVmsTemplateContent> selectSdVmsTemplateContentList(SdVmsTemplateContent sdVmsTemplateContent);

    /**
     * 新增发布模板内容
     *
     * @param sdVmsTemplateContent 发布模板内容
     * @return 结果
     */
    public int insertSdVmsTemplateContent(JSONObject jsonObject);

    /**
     * 修改发布模板内容
     *
     * @param sdVmsTemplateContent 发布模板内容
     * @return 结果
     */
    public int updateSdVmsTemplateContent(JSONObject jsonObject);

    /**
     * 批量删除发布模板内容
     *
     * @param ids 需要删除的发布模板内容主键集合
     * @return 结果
     */
    public int deleteSdVmsTemplateContentByIds(Long[] ids);

    /**
     * 删除发布模板内容信息
     *
     * @param id 发布模板内容主键
     * @return 结果
     */
    public int deleteSdVmsTemplateContentById(Long id);
}
