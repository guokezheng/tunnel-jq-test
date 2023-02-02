package com.tunnel.business.service.informationBoard;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.business.domain.informationBoard.IotBoardTemplateContent;

import java.util.List;

/**
 * 发布模板内容Service接口
 *
 * @author ruoyi
 * @date 2022-03-22
 */
public interface IIotBoardTemplateContentService {
    /**
     * 查询发布模板内容
     *
     * @param id 发布模板内容主键
     * @return 发布模板内容
     */
    IotBoardTemplateContent selectSdVmsTemplateContentById(Long id);

    /**
     * 查询发布模板内容列表
     *
     * @param iotBoardTemplateContent 发布模板内容
     * @return 发布模板内容集合
     */
    List<IotBoardTemplateContent> selectSdVmsTemplateContentList(IotBoardTemplateContent iotBoardTemplateContent);

    /**
     * 新增发布模板内容
     *
     * @param sdVmsTemplateContent 发布模板内容
     * @return 结果
     */
    int insertSdVmsTemplateContent(JSONObject jsonObject);

    /**
     * 修改发布模板内容
     *
     * @param jsonObject 发布模板内容
     * @return 结果
     */
    int updateSdVmsTemplateContent(JSONObject jsonObject);

    /**
     * 批量删除发布模板内容
     *
     * @param ids 需要删除的发布模板内容主键集合
     * @return 结果
     */
    int deleteSdVmsTemplateContentByIds(Long[] ids);

    /**
     * 删除发布模板内容信息
     *
     * @param id 发布模板内容主键
     * @return 结果
     */
    int deleteSdVmsTemplateContentById(Long id);
}
