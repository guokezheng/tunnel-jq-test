package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdEquipmentState;
import com.tunnel.business.domain.dataInfo.SdEquipmentStateIconFile;
import com.tunnel.business.mapper.dataInfo.SdEquipmentIconFileMapper;
import com.tunnel.business.mapper.dataInfo.SdEquipmentStateMapper;
import com.tunnel.business.service.dataInfo.ISdEquipmentStateService;
import com.tunnel.business.utils.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 设备类型状态关系Service业务层处理
 *
 * @author gongfanfei
 * @date 2020-08-28
 */
@Service
public class SdEquipmentStateServiceImpl implements ISdEquipmentStateService {
    @Autowired
    private SdEquipmentStateMapper sdEquipmentStateMapper;

    @Autowired
    private SdEquipmentIconFileMapper sdEquipmentIconFileMapper;

    /**
     * 查询设备类型状态关系
     *
     * @param id 设备类型状态关系ID
     * @return 设备类型状态关系
     */
//    @Override
//    public SdEquipmentState selectSdEquipmentStateById(Long id)
//    {
//        return sdEquipmentStateMapper.selectSdEquipmentStateById(id);
//    }
    @Override
    public SdEquipmentState selectSdEquipmentStateById(Long id) {
//	        return sdEquipmentStateMapper.selectSdEquipmentStateById(id);
        SdEquipmentState state = sdEquipmentStateMapper.selectSdEquipmentStateById(id);
        if (null == state) {
            throw new RuntimeException("设备状态信息为空");
        }
        String iconFileId = state.getIconFileId();
        if (iconFileId != null && !"".equals(iconFileId) && !"null".equals(iconFileId)) {
            SdEquipmentStateIconFile sdEquipmentStateIconFile = new SdEquipmentStateIconFile();
            sdEquipmentStateIconFile.setStateIconId(state.getIconFileId());
            state.setiFileList(sdEquipmentIconFileMapper.selectStateIconFileList(sdEquipmentStateIconFile));
        }
        return state;
    }

    /**
     * 查询设备类型状态关系列表
     *
     * @param sdEquipmentState 设备类型状态关系
     * @return 设备类型状态关系
     */
    @Override
    public List<SdEquipmentState> selectSdEquipmentStateList(SdEquipmentState sdEquipmentState) {
//        return sdEquipmentStateMapper.selectDropSdEquipmentStateList(sdEquipmentState);
        List<SdEquipmentState> list = sdEquipmentStateMapper.selectDropSdEquipmentStateList(sdEquipmentState);
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
     * 查询可控设备类型状态关系列表根据状态类型分组
     *
     * @param sdEquipmentState
     * @return
     */
    @Override
    public List<SdEquipmentState> selectSdEquipmentStateListGroupByStateType(SdEquipmentState sdEquipmentState) {
        List<SdEquipmentState> list = sdEquipmentStateMapper.selectSdEquipmentStateListGroupByStateType(sdEquipmentState);
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
     * 查询设备关系状态关系表(按设备类型分组查询)
     *
     * @param sdEquipmentState
     * @return
     */
    @Override
    public List<Map> getGroupList(SdEquipmentState sdEquipmentState) {
        List<Map> list = sdEquipmentStateMapper.getGroupList(sdEquipmentState);
        return list;
    }

    /**
     * 根据设备类型查询设备类型状态关系列表
     *
     * @param sdEquipmentState
     * @return
     */
    @Override
    public List<SdEquipmentState> selectDropSdEquipmentStateListByTypeId(SdEquipmentState sdEquipmentState) {
        List<SdEquipmentState> list = sdEquipmentStateMapper.selectDropSdEquipmentStateListByTypeId(sdEquipmentState);
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
     * 查询设备类型状态关系列表
     *
     * @param sdEquipmentState 设备类型状态关系（可控制选项）
     * @return 设备类型状态关系
     */
    @Override
    public List<SdEquipmentState> selectIsControlList(SdEquipmentState sdEquipmentState) {
//    	return sdEquipmentStateMapper.selectIsControlList(sdEquipmentState);
        List<SdEquipmentState> list = sdEquipmentStateMapper.selectIsControlList(sdEquipmentState);
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
     * 新增设备类型状态关系
     *
     * @param sdEquipmentState 设备类型状态关系
     * @return 结果
     */
//    @Override
//    public int insertSdEquipmentState(SdEquipmentState sdEquipmentState)
//    {
//        return sdEquipmentStateMapper.insertSdEquipmentState(sdEquipmentState);
//    }
    @Override
    public int insertSdEquipmentState(MultipartFile[] file, SdEquipmentState sdEquipmentState) {
//        return sdEquipmentStateMapper.insertSdEquipmentState(sdEquipmentState);
        int result = -1;
        List<SdEquipmentStateIconFile> list = new ArrayList<SdEquipmentStateIconFile>();
        try {
            if ("-1".equals(sdEquipmentState.getId())) {
                sdEquipmentState.setId(null);
            }
            if (file.length > 0) {
                String guid = UUIDUtil.getRandom32BeginTimePK();// 生成guid
                sdEquipmentState.setIconFileId(guid);// 文件关联ID
                for (int i = 0; i < file.length; i++) {
                    // 从缓存中获取文件存储路径
                    String fileServerPath = RuoYiConfig.getUploadPath();
                    // 原图文件名
                    String filename = file[i].getOriginalFilename();
                    // 原图扩展名
                    String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
                    // 新的全名
                    String fileName = extendName;
                    // 加路径全名
                    File dir = new File(fileServerPath + "/equipmentIcon/" + fileName);
                    File filepath = new File(fileServerPath + "/equipmentIcon");

                    SdEquipmentStateIconFile iconFile = new SdEquipmentStateIconFile();
                    iconFile.setStateIconId(guid);
                    iconFile.setUrl(fileServerPath + "/equipmentIcon/" + fileName);
                    iconFile.setStateIconName(fileName);
                    iconFile.setCreateBy(SecurityUtils.getUsername());
                    iconFile.setCreateTime(DateUtils.getNowDate());
                    list.add(iconFile);

                    if (!filepath.exists()) {
                        filepath.mkdirs();
                    } else {
                    }
                    file[i].transferTo(dir);
                }
                result = sdEquipmentIconFileMapper.brachInsertStateIconFile(list);
                if (result > -1) {
                    result = sdEquipmentStateMapper.insertSdEquipmentState(sdEquipmentState);
                }

            } else {
                sdEquipmentState.setIconFileId(null);// 图标文件ID
                result = sdEquipmentStateMapper.insertSdEquipmentState(sdEquipmentState);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }

    /**
     * 批量增加设备状态图标对象(返回主键和guid)
     *
     * @param file
     * @return
     */
    @Override
    public String insertSdEquipmentStateIconFile(MultipartFile[] file) {
        int result = -1;
        List<SdEquipmentStateIconFile> list = new ArrayList<SdEquipmentStateIconFile>();
        try {
            if (file.length > 0) {
//				String guid = UUIDUtil.getRandom32BeginTimePK();// 生成guid
                for (int i = 0; i < file.length; i++) {
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
                    // 加路径全名
                    File dir = new File(fileServerPath + "/equipmentIcon/" + fileName);
                    File filepath = new File(fileServerPath + "/equipmentIcon");
                    SdEquipmentStateIconFile iconFile = new SdEquipmentStateIconFile();
                    iconFile.setUrl(imageBaseStr);
                    iconFile.setStateIconName(fileName);
                    iconFile.setCreateBy(SecurityUtils.getUsername());
                    iconFile.setCreateTime(DateUtils.getNowDate());
                    list.add(iconFile);
                    if (!filepath.exists()) {
                        filepath.mkdirs();
                    } else {
                    }
                    file[i].transferTo(dir);
                }
                result = sdEquipmentIconFileMapper.brachInsertStateIconFiles(list);
                if (result > 0) {
                    StringBuffer sb = new StringBuffer();
                    list.forEach(x -> sb.append(x.getId()));
                    return sb.toString();
                }
            }
        } catch (IOException e) {
            System.out.println();
        }
        return "";
    }

    /**
     * 根据stateIconId删除设备状态图标
     *
     * @param id
     * @return
     */
    @Override
    public int delSdEquipmentStateIconFileById(Long id) {
        int result = 0;
        int i = sdEquipmentIconFileMapper.deleteStateIconFileById(id);
        result += i;
        return result;
    }

    /**
     * 批量新增设备类型状态关系(没有图片)
     *
     * @param sdEquipmentStates
     * @return
     */
    @Override
    public int insertSdEquipmentStateList(List<SdEquipmentState> sdEquipmentStates) {
        Iterator<SdEquipmentState> iterator = sdEquipmentStates.iterator();
        while (iterator.hasNext()) {
            // 生成guid
            String guid = UUIDUtil.getRandom32BeginTimePK();
            SdEquipmentState sdEquipmentState = iterator.next();
            String[] split = sdEquipmentState.getIconFileId().split(",");
            Iterator<String> iterator1 = Arrays.stream(split).iterator();
            while (iterator1.hasNext()) {
                try {
                    long id = Long.parseLong(iterator1.next());
                    SdEquipmentStateIconFile file = sdEquipmentIconFileMapper.selectStateIconFileById(id);
                    file.setStateIconId(guid);
                    sdEquipmentIconFileMapper.updateStateIconFileById(file);
                } catch (Exception e) {
                    System.out.println();
                }
            }
            sdEquipmentState.setIconFileId(guid);
        }
        return sdEquipmentStateMapper.insertSdEquipmentStateList(sdEquipmentStates);
    }

    /**
     * 修改设备类型状态关系
     *
     * @param sdEquipmentState 设备类型状态关系
     * @return 结果
     */
//    @Override
//    public int updateSdEquipmentState(SdEquipmentState sdEquipmentState)
//    {
//        return sdEquipmentStateMapper.updateSdEquipmentState(sdEquipmentState);
//    }
    @Override
    public int updateSdEquipmentState(MultipartFile[] file, SdEquipmentState sdEquipmentState, Long[] ids) {
        int result = 0;
        List<SdEquipmentStateIconFile> list = new ArrayList<SdEquipmentStateIconFile>();
        try {
            if ("-1".equals(sdEquipmentState.getId())) {
                sdEquipmentState.setId(null);
            }
            String guid = sdEquipmentState.getIconFileId();// 关联ID--guid
            if (guid == null || guid.equals("null") || guid.equals("")) {
                guid = UUIDUtil.getRandom32BeginTimePK();// 生成guid
            }
            sdEquipmentState.setIconFileId(guid);// 文件关联ID
            if (file != null && file.length > 0) {
                for (int i = 0; i < file.length; i++) {
                    // 从缓存中获取文件存储路径
                    String fileServerPath = RuoYiConfig.getUploadPath();
                    // 原图文件名
                    String filename = file[i].getOriginalFilename();
                    // 原图扩展名
                    String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
                    // 新的全名
                    String fileName = extendName;
                    // 加路径全名
                    File dir = new File(fileServerPath + "/equipmentIcon/" + fileName);
                    File filepath = new File(fileServerPath + "/equipmentIcon");

                    SdEquipmentStateIconFile iconFile = new SdEquipmentStateIconFile();
                    iconFile.setStateIconId(guid);
                    iconFile.setUrl(fileServerPath + "/equipmentIcon/" + fileName);
                    iconFile.setStateIconName(fileName);
                    iconFile.setCreateBy(SecurityUtils.getUsername());
                    iconFile.setCreateTime(DateUtils.getNowDate());
                    list.add(iconFile);

                    if (!filepath.exists()) {
                        filepath.mkdirs();
                    } else {
                    }
                    file[i].transferTo(dir);
                }
                result = sdEquipmentIconFileMapper.brachInsertStateIconFile(list);
            }
            if (ids.length > 0) {
                result = sdEquipmentIconFileMapper.deleteStateIconFileByIds(ids);//ids 为要删除的sd_equipment_state_icon_file id数组
            }
            if (result >= 0) {
                result = sdEquipmentStateMapper.updateSdEquipmentState(sdEquipmentState);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }

    /**
     * 批量修改设备状态
     *
     * @param sdEquipmentStates
     * @return
     */
    @Override
    public int updateSdEquipmentStates(List<SdEquipmentState> sdEquipmentStates) {
        int result = 0;
        //更新设备状态更新图片
        List<SdEquipmentState> updateSdEquipmentStates = new ArrayList<>();
        //新增设备状态
        List<SdEquipmentState> insertSdEquipmentStates = new ArrayList<>();
        Iterator<SdEquipmentState> iterator = sdEquipmentStates.iterator();
        while (iterator.hasNext()) {
            SdEquipmentState sdEquipmentState = iterator.next();
            if (sdEquipmentState.getId() == null) {
                insertSdEquipmentStates.add(sdEquipmentState);
            }
            if (sdEquipmentState.getId() != null && sdEquipmentState.getIconFileId().indexOf(",") > 0) {
                updateSdEquipmentStates.add(sdEquipmentState);
            }
        }
        sdEquipmentStates.removeAll(insertSdEquipmentStates);
        sdEquipmentStates.removeAll(updateSdEquipmentStates);
        if (sdEquipmentStates.size() > 0) {
            for (SdEquipmentState sdEquipmentState : sdEquipmentStates) {
                int i = sdEquipmentStateMapper.updateSdEquipmentState(sdEquipmentState);
                result += i;
            }
        }
        if (insertSdEquipmentStates.size() > 0) {
            int addInt = insertSdEquipmentStateList(insertSdEquipmentStates);
            result += addInt;
        }
        Iterator<SdEquipmentState> iterator2 = updateSdEquipmentStates.iterator();
        while (iterator2.hasNext()) {
            // 生成guid
            String guid = UUIDUtil.getRandom32BeginTimePK();
            List<Long> ids = new ArrayList<>();
            List<String> iconFileIds = new ArrayList<>();
            SdEquipmentState sdEquipmentState = iterator2.next();
            String[] split = sdEquipmentState.getIconFileId().split(",");
            iconFileIds.addAll(Arrays.asList(split));
            for (int i = 0; i < split.length; i++) {
                try {
                    long id = Long.parseLong(split[i]);
                    iconFileIds.remove(split[i]);
                    ids.add(id);
                } catch (Exception e) {
                    System.out.println();
                }
            }
            Iterator<Long> iterator1 = ids.iterator();
            while (iterator1.hasNext()) {
                long id = iterator1.next();
                SdEquipmentStateIconFile file = sdEquipmentIconFileMapper.selectStateIconFileById(id);
                if (iconFileIds.size() > 0 && ids.size() < 2) {
                    file.setStateIconId(iconFileIds.get(0));
                    sdEquipmentIconFileMapper.updateStateIconFileById(file);
                } else {
                    file.setStateIconId(guid);
                    sdEquipmentIconFileMapper.updateStateIconFileById(file);
                }
//				file.setStateIconId(guid);
//				sdEquipmentIconFileMapper.updateStateIconFileById(file);
            }
            if (iconFileIds.size() > 0 && ids.size() < 2) {
                sdEquipmentState.setIconFileId(iconFileIds.get(0));
            } else {
                sdEquipmentState.setIconFileId(guid);
            }
        }
        for (SdEquipmentState sdEquipmentState : updateSdEquipmentStates) {
            int i = sdEquipmentStateMapper.updateSdEquipmentState(sdEquipmentState);
            result += i;
        }
        return result;
    }

    /**
     * 批量删除设备类型状态关系
     *
     * @param ids 需要删除的设备类型状态关系ID
     * @return 结果
     */
    @Override
    public int deleteSdEquipmentStateByIds(Long[] ids) {
        return sdEquipmentStateMapper.deleteSdEquipmentStateByIds(ids);
    }

    /**
     * 根据id删除设备类型状态关系信息
     *
     * @param id 设备类型状态关系ID
     * @return 结果
     */
    @Override
    public int deleteSdEquipmentStateById(Long id) {
        return sdEquipmentStateMapper.deleteSdEquipmentStateById(id);
    }

    /**
     * 根据设备类型删除设备状态信息
     *
     * @param typeId
     * @return
     */
    @Override
    public int deleteSdEquipmentStateByTypeId(Long[] typeId) {
        return sdEquipmentStateMapper.deleteSdEquipmentStateByTypeId(typeId);
    }
}
