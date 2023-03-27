package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.tunnel.business.domain.dataInfo.SdEquipmentStateIconFile;
import com.tunnel.business.domain.dataInfo.SdEquipmentType;
import com.tunnel.business.mapper.dataInfo.SdEquipmentIconFileMapper;
import com.tunnel.business.mapper.dataInfo.SdEquipmentTypeMapper;
import com.tunnel.business.mapper.event.SdStrategyMapper;
import com.tunnel.business.mapper.event.SdStrategyRlMapper;
import com.tunnel.business.service.dataInfo.ISdEquipmentTypeService;
import com.tunnel.business.utils.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备类型Service业务层处理
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
@Service
public class SdEquipmentTypeServiceImpl implements ISdEquipmentTypeService {
    @Autowired
    private SdEquipmentTypeMapper sdEquipmentTypeMapper;

    @Autowired
    private SdEquipmentIconFileMapper sdEquipmentIconFileMapper;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    @Autowired
    private SdStrategyMapper sdStrategyMapper;

    @Autowired
    private SdStrategyRlMapper sdStrategyRlMapper;

    /**
     * 查询设备类型
     *
     * @param typeId 设备类型ID
     * @return 设备类型
     */
    // @Override
    // public SdEquipmentType selectSdEquipmentTypeById(Long typeId)
    // {
    // return sdEquipmentTypeMapper.selectSdEquipmentTypeById(typeId);
    // }
    @Override
    public SdEquipmentType selectSdEquipmentTypeById(Long typeId) {
        SdEquipmentType type = sdEquipmentTypeMapper.selectSdEquipmentTypeById(typeId);
        String fileId = type.getIconFileId();
        if (fileId != null && !"".equals(fileId) && !"null".equals(fileId)) {
            SdEquipmentStateIconFile sdEquipmentStateIconFile = new SdEquipmentStateIconFile();
            sdEquipmentStateIconFile.setStateIconId(type.getIconFileId());
            type.setiFileList(sdEquipmentIconFileMapper.selectStateIconFileList(sdEquipmentStateIconFile));
        }
//		String bigType = type.getBigType();
//		if (bigType != null && !"".equals(bigType)) {
//			String bigTypeInfo = "";
//			String[] bigTypeArray = bigType.split(",");
//			for (int i = 0;i < bigTypeArray.length;i++) {
//				SdEquipmentType sdEquipmentType = sdEquipmentTypeMapper.selectSdEquipmentTypeById(Long.parseLong(bigTypeArray[i]));
//				if (sdEquipmentType != null) {
//					bigTypeInfo = bigTypeInfo + sdEquipmentType.getTypeName()+ ",";
//				}
//			}
//			bigTypeInfo = bigTypeInfo.substring(0,bigTypeInfo.length()-1);
//			type.setBigType(bigTypeInfo);
//		}
        return type;
    }

    /**
     * 查询设备类型列表
     *
     * @param sdEquipmentType 设备类型
     * @return 设备类型
     */
    @Override
    public List<SdEquipmentType> selectSdEquipmentTypeList(SdEquipmentType sdEquipmentType) {
        List<SdEquipmentType> list = sdEquipmentTypeMapper.selectSdEquipmentTypeList(sdEquipmentType);
        for (int i = 0; i < list.size(); i++) {
            String iconFileId = list.get(i).getIconFileId();
            if (iconFileId != null && !"".equals(iconFileId) && !"null".equals(iconFileId)) {
                if (!"-1".equals(iconFileId)) {
                    SdEquipmentStateIconFile sdEquipmentStateIconFile = new SdEquipmentStateIconFile();
                    sdEquipmentStateIconFile.setStateIconId(iconFileId);
                    list.get(i).setiFileList(sdEquipmentIconFileMapper.selectStateIconFileList(sdEquipmentStateIconFile));
                }
            }
        }
        return list;
    }

    /**
     * 查询是否存在重复的设备类型
     *
     * @param sdEquipmentType 设备类型
     * @return
     */
    @Override
    public Integer selectExistSameType(SdEquipmentType sdEquipmentType) {
        return sdEquipmentTypeMapper.selectExistSameType(sdEquipmentType);
    }

    /**
     * 查询数据项中拥有的设备类型
     *
     * @param sdEquipmentType
     * @return
     */
    @Override
    public List<SdEquipmentType> selectHasItemEqTypeList(SdEquipmentType sdEquipmentType) {
        List<SdEquipmentType> list = sdEquipmentTypeMapper.selectHasItemEqTypeList(sdEquipmentType);
        for (int i = 0; i < list.size(); i++) {
            String iconFileId = list.get(i).getIconFileId();
            if (iconFileId != null && !"".equals(iconFileId) && !"null".equals(iconFileId)) {
                if (!"-1".equals(iconFileId)) {
                    SdEquipmentStateIconFile sdEquipmentStateIconFile = new SdEquipmentStateIconFile();
                    sdEquipmentStateIconFile.setStateIconId(iconFileId);
                    list.get(i).setiFileList(sdEquipmentIconFileMapper.selectStateIconFileList(sdEquipmentStateIconFile));
                }
            }
        }
        return list;
    }

    @Override
    public List<Map> selectTypeAndStrategy(SdEquipmentType sdEquipmentType) {
        List<SdEquipmentType> sdEquipmentTypes = sdEquipmentTypeMapper.selectSdEquipmentTypeList(sdEquipmentType);
        List<Map> list = new ArrayList<>();
        List<Map<String, String>> manualStrategy = sdStrategyMapper.getManualStrategy();
        for (int i = 0; i < sdEquipmentTypes.size(); i++) {
            SdEquipmentType equipmentType = sdEquipmentTypes.get(i);
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", equipmentType.getTypeId());
            hashMap.put("name", equipmentType.getTypeName());
            List<Map> mapList = new ArrayList<>();
            for (Map map : manualStrategy) {
                if (map.get("eqTypeId").equals(equipmentType.getTypeId())) {
                    Map<String, Object> objectMap = new HashMap<>();
                    objectMap.put("id", map.get("strategyId"));
                    objectMap.put("name", map.get("strategyName"));
                    mapList.add(objectMap);
                    hashMap.put("children", mapList);
                }
            }
            list.add(hashMap);
        }
        return list;
    }

    /**
     * app端查询设备类型
     * @return
     */
    @Override
    public List<SdEquipmentType> getDevicesType() {
        return sdEquipmentTypeMapper.getDevicesType();
    }

    /**
     * 新增设备类型
     *
     * @param sdEquipmentType 设备类型
     * @return 结果
     */
    @Override
    public int insertSdEquipmentType(MultipartFile[] file, SdEquipmentType sdEquipmentType) {
        // sdEquipmentType.setCreateTime(DateUtils.getNowDate());
        // return sdEquipmentTypeMapper.insertSdEquipmentType(sdEquipmentType);
        if (sdEquipmentType.getBigType() == null || sdEquipmentType.getBigType().equals("")) {
            throw new RuntimeException("请选择设备大类后重试");
        }
        int result = -1;
        List<SdEquipmentStateIconFile> list = new ArrayList<SdEquipmentStateIconFile>();
//        try {
            if ("-1".equals(sdEquipmentType.getTypeId())) {
                sdEquipmentType.setTypeId(null);
            }
            sdEquipmentType.setCreateTime(DateUtils.getNowDate());// 创建时间
            sdEquipmentType.setCreateBy(SecurityUtils.getUsername());// 设置当前创建人
            if (file != null && file.length > 0) {
                String guid = UUIDUtil.getRandom32BeginTimePK();// 生成guid
                sdEquipmentType.setIconFileId(guid);// 文件关联ID
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
//                    // 从缓存中获取文件存储路径
//                    String fileServerPath = RuoYiConfig.getUploadPath();
                    // 原图文件名
                    String filename = file[i].getOriginalFilename();
                    // 原图扩展名
                    String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
                    // 新的全名
                    String fileName = extendName;
//                    // 加路径全名
//                    File dir = new File(fileServerPath + "/equipmentIcon/" + fileName);
//                    File filepath = new File(fileServerPath + "/equipmentIcon");

                    SdEquipmentStateIconFile iconFile = new SdEquipmentStateIconFile();
                    iconFile.setStateIconId(guid);
                    iconFile.setUrl(imageBaseStr);
                    iconFile.setStateIconName(fileName);
                    iconFile.setCreateBy(SecurityUtils.getUsername());
                    iconFile.setCreateTime(DateUtils.getNowDate());
                    list.add(iconFile);

//                    if (!filepath.exists()) {
//                        filepath.mkdirs();
//                    } else {
//                    }
//                    file[i].transferTo(dir);
                }
                result = sdEquipmentIconFileMapper.brachInsertStateIconFile(list);
                if (result > -1) {
                    result = sdEquipmentTypeMapper.insertSdEquipmentType(sdEquipmentType);
                }

            } else {
                sdEquipmentType.setIconFileId(null);// 图标文件ID
                result = sdEquipmentTypeMapper.insertSdEquipmentType(sdEquipmentType);
            }

//        } catch (IOException e) {
//            e.printStackTrace();
//            return 0;
//        }
        return result;
    }

    /**
     * 修改设备类型
     *
     * @param sdEquipmentType 设备类型
     * @return 结果
     */
    // @Override
    // public int updateSdEquipmentType(SdEquipmentType sdEquipmentType) {
    // sdEquipmentType.setUpdateTime(DateUtils.getNowDate());
    // return sdEquipmentTypeMapper.updateSdEquipmentType(sdEquipmentType);
    // }
    @Override
    public int updateSdEquipmentType(MultipartFile[] file, SdEquipmentType sdEquipmentType, Long[] ids) {
        int result = 0;
        List<SdEquipmentStateIconFile> list = new ArrayList<SdEquipmentStateIconFile>();
//        try {
            if ("-1".equals(sdEquipmentType.getTypeId())) {
                sdEquipmentType.setTypeId(null);
            }
            sdEquipmentType.setUpdateTime(DateUtils.getNowDate());// 创建时间
            sdEquipmentType.setUpdateBy(SecurityUtils.getUsername());// 设置当前创建人
            String guid = sdEquipmentType.getIconFileId();// 关联ID--guid
            if (guid == null || guid.equals("null") || guid.equals("")) {
                guid = UUIDUtil.getRandom32BeginTimePK();// 生成guid
            }
            sdEquipmentType.setIconFileId(guid);// 文件关联ID
            if (file != null && file.length > 0) {
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
                    // 从缓存中获取文件存储路径
                    String fileServerPath = RuoYiConfig.getUploadPath();
                    // 原图文件名
                    String filename = file[i].getOriginalFilename();
                    // 原图扩展名
                    String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
                    // 新的全名
                    String fileName = extendName;
//                    // 加路径全名
//                    File dir = new File(fileServerPath + "/equipmentIcon/" + fileName);
//                    File filepath = new File(fileServerPath + "/equipmentIcon");

                    SdEquipmentStateIconFile iconFile = new SdEquipmentStateIconFile();
                    iconFile.setStateIconId(guid);
                    iconFile.setUrl(imageBaseStr);
                    iconFile.setStateIconName(fileName);
                    iconFile.setCreateBy(SecurityUtils.getUsername());
                    iconFile.setCreateTime(DateUtils.getNowDate());
                    list.add(iconFile);

//                    if (!filepath.exists()) {
//                        filepath.mkdirs();
//                    } else {
//                    }
//                    file[i].transferTo(dir);
                }
                result = sdEquipmentIconFileMapper.brachInsertStateIconFile(list);
            }
            if (ids.length > 0) {
                result = sdEquipmentIconFileMapper.deleteStateIconFileByIds(ids);//ids 为要删除的sd_equipment_state_icon_file id数组
            }
            if (result >= 0) {
                result = sdEquipmentTypeMapper.updateSdEquipmentType(sdEquipmentType);
            }


//        } catch (IOException e) {
//            e.printStackTrace();
//            return 0;
//        }


        return result;
    }

    /**
     * 批量删除设备类型
     *
     * @param typeIds 需要删除的设备类型ID
     * @return 结果
     */
    @Override
    public int deleteSdEquipmentTypeByIds(Long[] typeIds) {
        return sdEquipmentTypeMapper.deleteSdEquipmentTypeByIds(typeIds);
    }

    /**
     * 删除设备类型信息
     *
     * @param typeId 设备类型ID
     * @return 结果
     */
    @Override
    public int deleteSdEquipmentTypeById(Long typeId) {
        return sdEquipmentTypeMapper.deleteSdEquipmentTypeById(typeId);
    }

    @Override
    public List<SdEquipmentType> selectSdEquipmentTypeHasList(String tunnelId) {
        List<SdEquipmentType> list = sdEquipmentTypeMapper.selectSdEquipmentTypeHasList(tunnelId);
        return list;
    }

    @Override
    public List<SdEquipmentType> selectSdEquipmentTypeByBigType(String bigType) {
        if (StringUtils.isNotBlank(bigType)) {
            List<SdEquipmentType> list = sdEquipmentTypeMapper.selectSdEquipmentTypeByBigType(bigType);
            return list;
        }
        return null;

        //		List<SysDictData> sysNameList = sysDictDataMapper.selectDictDataByType("sd_sys_name");
//		for (int i = 0;i < sysNameList.size();i++) {
//			SysDictData sysDictData = sysNameList.get(i);
//			if (sysDictData.getDictLabel().equals(bigType)){
//				bigType = sysDictData.getDictValue();
//				if (bigType.equals("0")) {
//					bigType = "";
//				}
//				break;
//			}
//		}
    }

    @Override
    public List<SdEquipmentType> selectSdEquipmentTypeGroupByBigType() {
        List<SdEquipmentType> list = sdEquipmentTypeMapper.selectSdEquipmentTypeGroupByBigType();
        return list;
    }

    @Override
    public List<SdEquipmentType> selectList() {
        return sdEquipmentTypeMapper.selectList();
    }
}
