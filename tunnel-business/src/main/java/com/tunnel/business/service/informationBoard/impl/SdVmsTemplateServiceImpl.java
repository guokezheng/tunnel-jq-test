package com.tunnel.business.service.informationBoard.impl;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.business.domain.informationBoard.SdVmsTemplate;
import com.tunnel.business.mapper.informationBoard.SdVmsTemplateContentMapper;
import com.tunnel.business.mapper.informationBoard.SdVmsTemplateMapper;
import com.tunnel.business.service.informationBoard.ISdVmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 情报板模板Service业务层处理
 *
 * @author 刘方堃
 * @date 2021-11-30
 */
@Service
public class SdVmsTemplateServiceImpl implements ISdVmsTemplateService {
    @Autowired
    private SdVmsTemplateMapper sdVmsTemplateMapper;

    @Autowired
    private SdVmsTemplateContentMapper sdVmsTemplateContentMapper;

    /**
     * 查询情报板模板
     *
     * @param id 情报板模板ID
     * @return 情报板模板
     */
    @Override
    public SdVmsTemplate selectSdVmsTemplateById(Long id) {
        return sdVmsTemplateMapper.selectSdVmsTemplateById(id);
    }

    /**
     * 查询情报板模板列表
     *
     * @param sdVmsTemplate 情报板模板
     * @return 情报板模板
     */
    @Override
    public List<SdVmsTemplate> selectSdVmsTemplateList(SdVmsTemplate sdVmsTemplate) {
        return sdVmsTemplateMapper.selectSdVmsTemplateList(sdVmsTemplate);
    }

    /**
     * 新增情报板模板
     *
     * @param jsonObject 情报板模板
     * @return 结果
     */
    @Override
    public Long insertSdVmsTemplate(JSONObject jsonObject) {
//        Map<String, Object> templatesMap = new HashMap<>();
//        for (Map.Entry<String, Object> entry : templatesMap.entrySet()) {
//            templatesMap.put(entry.getKey(), entry.getValue());
//        }
        SdVmsTemplate sdVmsTemplate = new SdVmsTemplate();
        sdVmsTemplate.setScreenSize(jsonObject.get("screenSize").toString());
        sdVmsTemplate.setInScreenMode(jsonObject.get("inScreenMode").toString());
        sdVmsTemplate.setRollSpeed(Long.parseLong(jsonObject.get("rollSpeed").toString()));
        sdVmsTemplate.setStopTime(Long.parseLong(jsonObject.get("stopTime").toString()));
        sdVmsTemplate.setApplyType(jsonObject.get("applyType").toString());
        if (jsonObject.get("category") == null || jsonObject.get("category").toString().equals("")) {
            throw new RuntimeException("情报板所属类别不能为空");
        }
        sdVmsTemplate.setCategory(jsonObject.get("category").toString());
    /*    sdVmsTemplate.setIsCurrency(Integer.parseInt(jsonObject.get("isCurrency").toString()));
        sdVmsTemplate.setTemplateType(Integer.parseInt(jsonObject.get("templateType").toString()));*/
        sdVmsTemplate.setVmsType(jsonObject.get("vmsType").toString());
        sdVmsTemplate.setRemark(jsonObject.get("remark").toString());
        int template = sdVmsTemplateMapper.insertSdVmsTemplate(sdVmsTemplate);
        if (template > 0) {
            Long id = sdVmsTemplateMapper.selectSdVmsTemplateId();
            return id;
        }
        return -1L;
    }

    /**
     * 修改情报板模板
     *
     * @param jsonObject 情报板模板
     * @return 结果
     */
    @Override
    public int updateSdVmsTemplate(JSONObject templatesMap) {
        SdVmsTemplate sdVmsTemplate = new SdVmsTemplate();
        sdVmsTemplate.setScreenSize(templatesMap.get("screenSize").toString());
        sdVmsTemplate.setInScreenMode(templatesMap.get("inScreenMode").toString());
        sdVmsTemplate.setRollSpeed(Long.parseLong(templatesMap.get("rollSpeed").toString()));
        sdVmsTemplate.setStopTime(Long.parseLong(templatesMap.get("stopTime").toString()));
        sdVmsTemplate.setApplyType(templatesMap.get("applyType").toString());
        if (templatesMap.get("category") == null || templatesMap.get("category").toString().equals("")) {
            throw new RuntimeException("情报板所属类别不能为空");
        }
        sdVmsTemplate.setCategory(templatesMap.get("category").toString());
      /*  sdVmsTemplate.setIsCurrency(Integer.parseInt(templatesMap.get("isCurrency").toString()));
        sdVmsTemplate.setTemplateType(Integer.parseInt(templatesMap.get("templateType").toString()));*/
        sdVmsTemplate.setVmsType(templatesMap.get("vmsType").toString());
        sdVmsTemplate.setId(Long.valueOf(templatesMap.get("id").toString()));
        sdVmsTemplate.setRemark(templatesMap.get("remark").toString());
        return sdVmsTemplateMapper.updateSdVmsTemplate(sdVmsTemplate);
    }

    /**
     * 批量删除情报板模板
     *
     * @param ids 需要删除的情报板模板ID
     * @return 结果
     */
    @Override
    public int deleteSdVmsTemplateByIds(Long[] ids) {
        for (int i = 0; i < ids.length; i++) {
            Long templateId = ids[i];
            //获取要删除的ID后先删除模板内容数据
            sdVmsTemplateContentMapper.deleteContentByTemplateId(templateId.toString());
        }
        return sdVmsTemplateMapper.deleteSdVmsTemplateByIds(ids);
    }

    /**
     * 删除情报板模板信息
     *
     * @param id 情报板模板ID
     * @return 结果
     */
    @Override
    public int deleteSdVmsTemplateById(Long id) {
        return sdVmsTemplateMapper.deleteSdVmsTemplateById(id);
    }

    @Override
    public List<Map<String, Object>> informationBoardRelease(JSONObject jsonObject) {
        return null;
    }

    @Override
    public List<Map<String, Object>> informationBoardAcquisition(JSONObject jsonObject) {
        return null;
    }
}
