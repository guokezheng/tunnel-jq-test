package com.tunnel.business.mapper.informationBoard;

import com.tunnel.business.domain.informationBoard.SdVmsTemplateContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 发布模板内容Mapper接口
 *
 * @author ruoyi
 * @date 2022-03-22
 */
public interface SdVmsTemplateContentMapper
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
    public int insertSdVmsTemplateContent(SdVmsTemplateContent sdVmsTemplateContent);

    /**
     * 修改发布模板内容
     *
     * @param sdVmsTemplateContent 发布模板内容
     * @return 结果
     */
    public int updateSdVmsTemplateContent(SdVmsTemplateContent sdVmsTemplateContent);

    /**
     * 删除发布模板内容
     *
     * @param id 发布模板内容主键
     * @return 结果
     */
    public int deleteSdVmsTemplateContentById(Long id);

    /**
     * 批量删除发布模板内容
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdVmsTemplateContentByIds(Long[] ids);

    public int deleteContentByTemplateId(@Param("templateId") String templateId);
}
