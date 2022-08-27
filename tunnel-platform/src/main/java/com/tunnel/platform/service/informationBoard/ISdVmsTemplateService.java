package com.tunnel.platform.service.informationBoard;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.platform.domain.informationBoard.SdVmsTemplate;

import java.util.List;
import java.util.Map;

/**
 * 情报板模板Service接口
 *
 * @author 刘方堃
 * @date 2021-11-30
 */
public interface ISdVmsTemplateService
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
     * @param jsonObject 情报板模板
     * @return 结果
     */
    public Long insertSdVmsTemplate(JSONObject jsonObject);

    /**
     * 修改情报板模板
     *
     * @param jsonObject 情报板模板
     * @return 结果
     */
    public int updateSdVmsTemplate(JSONObject jsonObject);

    /**
     * 批量删除情报板模板
     *
     * @param ids 需要删除的情报板模板ID
     * @return 结果
     */
    public int deleteSdVmsTemplateByIds(Long[] ids);

    /**
     * 删除情报板模板信息
     *
     * @param id 情报板模板ID
     * @return 结果
     */
    public int deleteSdVmsTemplateById(Long id);

    public List<Map<String, Object>> informationBoardRelease(JSONObject jsonObject);

    public List<Map<String, Object>> informationBoardAcquisition(JSONObject jsonObject);
}
