package com.tunnel.business.service.informationBoard;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.business.domain.informationBoard.IotBoardTemplate;

import java.util.List;
import java.util.Map;

/**
 * 情报板模板Service接口
 *
 * @author 刘方堃
 * @date 2021-11-30
 */
public interface IIotBoardTemplateService {
    /**
     * 查询情报板模板
     *
     * @param id 情报板模板ID
     * @return 情报板模板
     */
    IotBoardTemplate selectSdVmsTemplateById(Long id);

    /**
     * 查询情报板模板列表
     *
     * @param iotBoardTemplate 情报板模板
     * @return 情报板模板集合
     */
    List<IotBoardTemplate> selectSdVmsTemplateList(IotBoardTemplate iotBoardTemplate);

    /**
     * 新增情报板模板
     *
     * @param jsonObject 情报板模板
     * @return 结果
     */
    Long insertSdVmsTemplate(JSONObject jsonObject);

    /**
     * 修改情报板模板
     *
     * @param jsonObject 情报板模板
     * @return 结果
     */
    int updateSdVmsTemplate(JSONObject jsonObject);

    /**
     * 批量删除情报板模板
     *
     * @param ids 需要删除的情报板模板ID
     * @return 结果
     */
    int deleteSdVmsTemplateByIds(Long[] ids);

    /**
     * 删除情报板模板信息
     *
     * @param id 情报板模板ID
     * @return 结果
     */
    int deleteSdVmsTemplateById(Long id);

    List<Map<String, Object>> informationBoardRelease(JSONObject jsonObject);

    List<Map<String, Object>> informationBoardAcquisition(JSONObject jsonObject);

    List<IotBoardTemplate> getAllVmsTemplate(String category, String devicePixel);

    List<Map<String, Object>> getVMSTemplatesByDevIdAndCategory(List<String> devIds);
}
