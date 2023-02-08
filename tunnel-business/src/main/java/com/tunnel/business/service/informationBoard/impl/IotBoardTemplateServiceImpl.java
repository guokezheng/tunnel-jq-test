package com.tunnel.business.service.informationBoard.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.system.service.ISysDictDataService;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.informationBoard.SdIotDevice;
import com.tunnel.business.domain.informationBoard.IotBoardTemplate;
import com.tunnel.business.domain.informationBoard.IotBoardTemplateContent;
import com.tunnel.business.mapper.informationBoard.IotBoardTemplateContentMapper;
import com.tunnel.business.mapper.informationBoard.IotBoardTemplateMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.informationBoard.ISdIotDeviceService;
import com.tunnel.business.service.informationBoard.IIotBoardTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 情报板模板Service业务层处理
 *
 * @author 刘方堃
 * @date 2021-11-30
 */
@Service
public class IotBoardTemplateServiceImpl implements IIotBoardTemplateService {
    @Autowired
    private IotBoardTemplateMapper iotBoardTemplateMapper;

    @Autowired
    private IotBoardTemplateContentMapper iotBoardTemplateContentMapper;

    @Autowired
    private ISysDictDataService sysDictDataService;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdIotDeviceService sdIotDeviceService;

    /**
     * 查询情报板模板
     *
     * @param id 情报板模板ID
     * @return 情报板模板
     */
    @Override
    public IotBoardTemplate selectSdVmsTemplateById(Long id) {
        return iotBoardTemplateMapper.selectSdVmsTemplateById(id);
    }

    /**
     * 查询情报板模板列表
     *
     * @param iotBoardTemplate 情报板模板
     * @return 情报板模板
     */
    @Override
    public List<IotBoardTemplate> selectSdVmsTemplateList(IotBoardTemplate iotBoardTemplate) {
        return iotBoardTemplateMapper.selectSdVmsTemplateList(iotBoardTemplate);
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
        IotBoardTemplate iotBoardTemplate = new IotBoardTemplate();
        iotBoardTemplate.setScreenSize(jsonObject.get("screenSize").toString());
        iotBoardTemplate.setInScreenMode(jsonObject.get("inScreenMode").toString());
//        sdVmsTemplate.setRollSpeed(Long.parseLong(jsonObject.get("rollSpeed").toString()));
        iotBoardTemplate.setStopTime(Long.parseLong(jsonObject.get("stopTime").toString()));
        iotBoardTemplate.setApplyType(jsonObject.get("applyType").toString());
        if (jsonObject.get("category") == null || jsonObject.get("category").toString().equals("")) {
            throw new RuntimeException("情报板所属类别不能为空");
        }
        iotBoardTemplate.setCategory(jsonObject.get("category").toString());
    /*    sdVmsTemplate.setIsCurrency(Integer.parseInt(jsonObject.get("isCurrency").toString()));
        sdVmsTemplate.setTemplateType(Integer.parseInt(jsonObject.get("templateType").toString()));*/
        iotBoardTemplate.setVmsType(jsonObject.get("vmsType").toString());
        iotBoardTemplate.setRemark(jsonObject.get("remark").toString());
        int template = iotBoardTemplateMapper.insertSdVmsTemplate(iotBoardTemplate);
        if (template > 0) {
            Long id = iotBoardTemplateMapper.selectSdVmsTemplateId();
            return id;
        }
        return -1L;
    }

    /**
     * 修改情报板模板
     *
     * @param templatesMap 情报板模板
     * @return 结果
     */
    @Override
    public int updateSdVmsTemplate(JSONObject templatesMap) {
        IotBoardTemplate iotBoardTemplate = new IotBoardTemplate();
        iotBoardTemplate.setScreenSize(templatesMap.get("screenSize").toString());
        iotBoardTemplate.setInScreenMode(templatesMap.get("inScreenMode").toString());
//        sdVmsTemplate.setRollSpeed(Long.parseLong(templatesMap.get("rollSpeed").toString()));
        iotBoardTemplate.setStopTime(Long.parseLong(templatesMap.get("stopTime").toString()));
        iotBoardTemplate.setApplyType(templatesMap.get("applyType").toString());
        if (templatesMap.get("category") == null || templatesMap.get("category").toString().equals("")) {
            throw new RuntimeException("情报板所属类别不能为空");
        }
        iotBoardTemplate.setCategory(templatesMap.get("category").toString());
      /*  sdVmsTemplate.setIsCurrency(Integer.parseInt(templatesMap.get("isCurrency").toString()));
        sdVmsTemplate.setTemplateType(Integer.parseInt(templatesMap.get("templateType").toString()));*/
        iotBoardTemplate.setVmsType(templatesMap.get("vmsType").toString());
        iotBoardTemplate.setId(Long.valueOf(templatesMap.get("id").toString()));
        iotBoardTemplate.setRemark(templatesMap.get("remark").toString());
        return iotBoardTemplateMapper.updateSdVmsTemplate(iotBoardTemplate);
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
            iotBoardTemplateContentMapper.deleteContentByTemplateId(templateId.toString());
        }
        return iotBoardTemplateMapper.deleteSdVmsTemplateByIds(ids);
    }

    /**
     * 删除情报板模板信息
     *
     * @param id 情报板模板ID
     * @return 结果
     */
    @Override
    public int deleteSdVmsTemplateById(Long id) {
        return iotBoardTemplateMapper.deleteSdVmsTemplateById(id);
    }

    @Override
    public List<Map<String, Object>> informationBoardRelease(JSONObject jsonObject) {
        return null;
    }

    @Override
    public List<Map<String, Object>> informationBoardAcquisition(JSONObject jsonObject) {
        return null;
    }

//    @Override
//    public List<IotBoardTemplate> getAllVmsTemplate(String category, String devicePixel) {
//        List<IotBoardTemplate> iotBoardTemplates = iotBoardTemplateMapper.selectTemplateList(category, devicePixel);
//        List<IotBoardTemplateContent> iotBoardTemplateContents = iotBoardTemplateContentMapper.selectSdVmsTemplateContentList(null);
//        List<IotBoardTemplateContent> contents = new ArrayList<>();
//        List<IotBoardTemplate> template = new ArrayList<>();
//        for (int j = 0; j < iotBoardTemplates.size(); j++) {
//            contents = new ArrayList<>();
//            IotBoardTemplate iotBoardTemplate = iotBoardTemplates.get(j);
//            if (!category.equals(iotBoardTemplate.getCategory())) {
//                continue;
//            }
//            Long id = iotBoardTemplate.getId();
//            for (int z = 0; z < iotBoardTemplateContents.size(); z++) {
//                IotBoardTemplateContent iotBoardTemplateContent = iotBoardTemplateContents.get(z);
//                if (iotBoardTemplateContent.getTemplateId().equals("") || iotBoardTemplateContent.getTemplateId() == null) {
//                    continue;
//                }
//                Long templateId = Long.parseLong(iotBoardTemplateContent.getTemplateId());
//                if (id.longValue() == templateId.longValue()) {
//                    contents.add(iotBoardTemplateContent);
//                }
//            }
//            iotBoardTemplate.setTcontents(contents);
//            template.add(iotBoardTemplate);
//        }
//        return template;
//    }

    @Override
    public Map<String, List<IotBoardTemplate>> getAllVmsTemplate(String category, String devicePixel) {
        Map<String, List<IotBoardTemplate>> map = new HashMap<>();
        List<SysDictData> categorys = sysDictDataService.getSysDictDataByDictType("iot_template_category");
        List<IotBoardTemplate> iotBoardTemplates = iotBoardTemplateMapper.selectTemplateList(null, devicePixel);
        List<IotBoardTemplateContent> iotBoardTemplateContents = iotBoardTemplateContentMapper.selectSdVmsTemplateContentList(null);
        List<IotBoardTemplateContent> contents = new ArrayList<>();
        List<IotBoardTemplate> template = new ArrayList<>();
        if (!categorys.isEmpty()) {
            for (int i = 0;i < categorys.size();i++) {
                template = new ArrayList<>();
                String dictValue = categorys.get(i).getDictValue();
                for (int j = 0; j < iotBoardTemplates.size(); j++) {
                    contents = new ArrayList<>();
                    IotBoardTemplate iotBoardTemplate = iotBoardTemplates.get(j);
                    if (!dictValue.equals(iotBoardTemplate.getCategory())) {
                        continue;
                    }
                    Long id = iotBoardTemplate.getId();
                    for (int z = 0; z < iotBoardTemplateContents.size(); z++) {
                        IotBoardTemplateContent iotBoardTemplateContent = iotBoardTemplateContents.get(z);
                        if (iotBoardTemplateContent.getTemplateId().equals("") || iotBoardTemplateContent.getTemplateId() == null) {
                            continue;
                        }
                        Long templateId = Long.parseLong(iotBoardTemplateContent.getTemplateId());
                        if (id.longValue() == templateId.longValue()) {
                            contents.add(iotBoardTemplateContent);
                        }
                    }
                    if (contents.isEmpty()) {
                        continue;
                    }
                    iotBoardTemplate.setTcontents(contents);
                    template.add(iotBoardTemplate);
                }
                map.put(dictValue, template);
            }
        }
        return map;
    }

    @Override
    public List<Map<String, Object>> getVMSTemplatesByDevIdAndCategory(List<String> devIds) {
        List<SysDictData> categorys = sysDictDataService.getSysDictDataByDictType("iot_template_category");
        if (devIds.isEmpty()) {
            return null;
        } else if (categorys.isEmpty()) {
            throw new RuntimeException("情报板模板类型数据为空！");
        }
        List<Map<String, Object>> listMap = new ArrayList<>();
        List<Map<String, Object>> sdVmsTemplates = iotBoardTemplateMapper.getAllSdVmsTemplateList();
        for (int i = 0;i < devIds.size();i++) {
            String devId = devIds.get(i);
            SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);
            Long associatedDeviceId = sdDevices.getAssociatedDeviceId();
            if (associatedDeviceId.longValue() == 0L) {
                break;
            }
            String devicePixel = "";
            SdIotDevice sdIotDevice = sdIotDeviceService.selectIotDeviceById(associatedDeviceId);
            if (sdIotDevice != null && sdIotDevice.getDevicePixel() != null && !sdIotDevice.getDevicePixel().equals("")) {
                devicePixel = sdIotDevice.getDevicePixel();
            } else {
                throw new RuntimeException("情报板设备分辨率信息不全，无法查询到可以匹配的模板信息");
            }
            for (int j = 0;j < categorys.size();j++) {
                Map<String, Object> map = new HashMap<>();
                List<Map<String, Object>> childrenMap = new ArrayList<>();
                SysDictData sysDictData = categorys.get(j);
                String dictValue = sysDictData.getDictValue();
                map.put("value", dictValue);
                map.put("label", sysDictData.getDictLabel());
                for (int z = 0;z < sdVmsTemplates.size();z++) {
                    Map<String, Object> objectMap = sdVmsTemplates.get(z);
                    if (objectMap.get("devicePixel") == null || objectMap.get("category") == null) {
                        continue;
                    }
                    String pixel = objectMap.get("devicePixel").toString();
                    String category = objectMap.get("category").toString();
                    if (!pixel.equals("") && pixel.equals(devicePixel)
                            && !category.equals("") && dictValue.equals(category)) {
                        childrenMap.add(objectMap);
                    }
                }
                map.put("children", childrenMap);
                listMap.add(map);
            }
        }
        return listMap;
    }
}
