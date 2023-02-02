package com.tunnel.business.mapper.informationBoard;

import com.tunnel.business.domain.informationBoard.IotBoardTemplateContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 发布模板内容Mapper接口
 *
 * @author ruoyi
 * @date 2022-03-22
 */
public interface IotBoardTemplateContentMapper
{
    /**
     * 查询发布模板内容
     *
     * @param id 发布模板内容主键
     * @return 发布模板内容
     */
    public IotBoardTemplateContent selectSdVmsTemplateContentById(Long id);

    /**
     * 查询发布模板内容列表
     *
     * @param iotBoardTemplateContent 发布模板内容
     * @return 发布模板内容集合
     */
    public List<IotBoardTemplateContent> selectSdVmsTemplateContentList(IotBoardTemplateContent iotBoardTemplateContent);

    /**
     * 新增发布模板内容
     *
     * @param iotBoardTemplateContent 发布模板内容
     * @return 结果
     */
    public int insertSdVmsTemplateContent(IotBoardTemplateContent iotBoardTemplateContent);

    /**
     * 修改发布模板内容
     *
     * @param iotBoardTemplateContent 发布模板内容
     * @return 结果
     */
    public int updateSdVmsTemplateContent(IotBoardTemplateContent iotBoardTemplateContent);

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
