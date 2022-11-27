package com.tunnel.business.mapper.informationBoard;


import com.tunnel.business.domain.informationBoard.SdVmsTemplate;

import java.util.List;

/**
 * 情报板模板Mapper接口
 *
 * @author 刘方堃
 * @date 2021-11-30
 */
public interface SdVmsTemplateMapper
{
    /**
     * 查询情报板模板
     *
     * @param id 情报板模板ID
     * @return 情报板模板
     */
    public SdVmsTemplate selectSdVmsTemplateById(Long id);

    /**
     * 查询情报板模板列表
     *
     * @param sdVmsTemplate 情报板模板
     * @return 情报板模板集合
     */
    public List<SdVmsTemplate> selectSdVmsTemplateList(SdVmsTemplate sdVmsTemplate);

    /**
     * 新增情报板模板
     *
     * @param sdVmsTemplate 情报板模板
     * @return 结果
     */
    public int insertSdVmsTemplate(SdVmsTemplate sdVmsTemplate);

    /**
     * 修改情报板模板
     *
     * @param sdVmsTemplate 情报板模板
     * @return 结果
     */
    public int updateSdVmsTemplate(SdVmsTemplate sdVmsTemplate);

    /**
     * 删除情报板模板
     *
     * @param id 情报板模板ID
     * @return 结果
     */
    public int deleteSdVmsTemplateById(Long id);

    /**
     * 批量删除情报板模板
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdVmsTemplateByIds(Long[] ids);

    public Long selectSdVmsTemplateId();

    public List<SdVmsTemplate> selectTemplateList(String category);
}
