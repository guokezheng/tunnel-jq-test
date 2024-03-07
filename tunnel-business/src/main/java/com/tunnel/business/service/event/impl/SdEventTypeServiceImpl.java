package com.tunnel.business.service.event.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdEquipmentStateIconFile;
import com.tunnel.business.domain.event.SdEventType;
import com.tunnel.business.mapper.event.SdEventTypeMapper;
import com.tunnel.business.service.event.ISdEventTypeService;
import com.tunnel.business.utils.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 事件类型Service业务层处理
 *
 * @author gongfanfei
 * @date 2020-08-24
 */
@Service
public class SdEventTypeServiceImpl implements ISdEventTypeService {
    @Autowired
    private SdEventTypeMapper sdEventTypeMapper;

    /**
     * 查询事件类型
     *
     * @param id 事件类型ID
     * @return 事件类型
     */
    @Override
    public SdEventType selectSdEventTypeById(Long id) {
        return sdEventTypeMapper.selectSdEventTypeById(id);
    }

    /**
     * 查询事件类型列表
     *
     * @param sdEventType 事件类型
     * @return 事件类型
     */
    @Override
    public List<SdEventType> selectSdEventTypeList(SdEventType sdEventType) {
        return sdEventTypeMapper.selectSdEventTypeList(sdEventType);
    }

    /**
     * 新增事件类型
     *
     * @param sdEventType 事件类型
     * @return 结果
     */
    @Override
    public AjaxResult insertSdEventType(MultipartFile[] file, SdEventType sdEventType) {
        int count = checkData(sdEventType);
        if(count > 0){
            return AjaxResult.error("事件类型已存在，添加失败，请检查填写内容！");
        }
        if (file.length > 0) {
            for (int i = 0; i < file.length; i++) {
                // 图片Base64
                String imageBaseStr = null;
                try {
                    String contentType = file[i].getContentType();
                    if (!contentType.contains("image")) {
                        throw new RuntimeException("文件类型不正确!");
                    }
                    byte[] imageBytes = file[i].getBytes();
                    BASE64Encoder base64Encoder = new BASE64Encoder();
                    imageBaseStr = "data:" + contentType + ";base64," + base64Encoder.encode(imageBytes);
                    imageBaseStr = imageBaseStr.replaceAll("[\\s*\t\n\r]", "");
                } catch (IOException e) {
                    throw new RuntimeException("图片转换base64异常");
                }
//                // 原图文件名
//                String filename = file[i].getOriginalFilename();
//                // 原图扩展名
//                String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
//                // 新的全名
//                String fileName = extendName;
                sdEventType.setIconUrl(imageBaseStr);
            }
        }
        int i = sdEventTypeMapper.insertSdEventType(sdEventType);
        if(i > 0){
            return AjaxResult.success();
        }else {
            return AjaxResult.error();
        }
    }

    /**
     * 修改事件类型
     *
     * @param sdEventType 事件类型
     * @return 结果
     */
    @Override
    public AjaxResult updateSdEventType(MultipartFile[] file,SdEventType sdEventType) {
        int count = checkData(sdEventType);
        if(count > 0){
            return AjaxResult.error("事件类型已存在，修改失败,请检查填写内容！");
        }
        if (file!=null) {
            for (int i = 0; i < file.length; i++) {
                // 图片Base64
                String imageBaseStr = null;
                try {
                    String contentType = file[i].getContentType();
                    if (!contentType.contains("image")) {
                        throw new RuntimeException("文件类型不正确!");
                    }
                    byte[] imageBytes = file[i].getBytes();
                    BASE64Encoder base64Encoder = new BASE64Encoder();
                    imageBaseStr = "data:" + contentType + ";base64," + base64Encoder.encode(imageBytes);
                    imageBaseStr = imageBaseStr.replaceAll("[\\s*\t\n\r]", "");
                } catch (IOException e) {
                    throw new RuntimeException("图片转换base64异常");
                }
//                // 原图文件名
//                String filename = file[i].getOriginalFilename();
//                // 原图扩展名
//                String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
//                // 新的全名
//                String fileName = extendName;
                sdEventType.setIconUrl(imageBaseStr);
            }
        }
//        else{
//            sdEventType.setIconUrl("");
//        }
        sdEventType.setUpdateTime(DateUtils.getNowDate());
        int i = sdEventTypeMapper.updateSdEventType(sdEventType);
        if(i > 0){
            return AjaxResult.success();
        }else {
            return AjaxResult.error();
        }
    }

    /**
     * 批量删除事件类型
     *
     * @param ids 需要删除的事件类型ID
     * @return 结果
     */
    @Override
    public int deleteSdEventTypeByIds(Long[] ids) {
        return sdEventTypeMapper.deleteSdEventTypeByIds(ids);
    }

    /**
     * 删除事件类型信息
     *
     * @param id 事件类型ID
     * @return 结果
     */
    @Override
    public int deleteSdEventTypeById(Long id) {
        return sdEventTypeMapper.deleteSdEventTypeById(id);
    }

    /**
     * 获取所有事件类型Map格式
     *
     * @return
     */
    @Override
    public Map<Long, String> getEventTypeMap() {
        List<SdEventType> list = sdEventTypeMapper.selectSdEventTypeList(new SdEventType());

        Map<Long,String> map = list.stream().collect(Collectors.toMap(SdEventType::getId, SdEventType::getSimplifyName));

        return map;
    }

    @Override
    public int updateSdEventTypeAudio(SdEventType sdEventType) {
        return sdEventTypeMapper.updateSdEventTypeAudio(sdEventType);
    }

    public int checkData(SdEventType sdEventType){
        int count = 0;
        SdEventType type = new SdEventType();
        type.setId(sdEventType.getId() == null ? 0 : sdEventType.getId());
        if(sdEventType.getEventType() != null && sdEventType.getEventType() != ""){
            type.setEventType(sdEventType.getEventType());
            count = sdEventTypeMapper.checkData(type);
            if(count > 0){
                return count;
            }
        }
        if(sdEventType.getSimplifyName() != null && sdEventType.getSimplifyName() != ""){
            type.setEventType(sdEventType.getSimplifyName());
            count = sdEventTypeMapper.checkData(type);
            if(count > 0){
                return count;
            }
        }
        if(sdEventType.getPrevControlType() != null && sdEventType.getPrevControlType() != ""){
            type.setEventType(sdEventType.getSimplifyName());
            count = sdEventTypeMapper.checkData(type);
            if(count > 0){
                return count;
            }
        }
        return count;
    }
}
