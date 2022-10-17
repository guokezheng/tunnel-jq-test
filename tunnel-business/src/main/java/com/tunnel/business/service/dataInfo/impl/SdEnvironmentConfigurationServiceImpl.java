package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdEnvironmentConfiguration;
import com.tunnel.business.domain.dataInfo.SdEquipmentStateIconFile;
import com.tunnel.business.mapper.dataInfo.SdEnvironmentConfigurationMapper;
import com.tunnel.business.mapper.dataInfo.SdEquipmentIconFileMapper;
import com.tunnel.business.service.dataInfo.ISdEnvironmentConfigurationService;
import com.tunnel.business.utils.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 隧道环境配置Service业务层处理
 *
 * @author 刘方堃
 * @date 2021-12-13
 */
@Service
public class SdEnvironmentConfigurationServiceImpl implements ISdEnvironmentConfigurationService {
    @Autowired
    private SdEnvironmentConfigurationMapper sdEnvironmentConfigurationMapper;
    @Autowired
    private SdEquipmentIconFileMapper sdEquipmentIconFileMapper;

    /**
     * 查询隧道环境配置
     *
     * @param id 隧道环境配置主键
     * @return 隧道环境配置
     */
    @Override
    public SdEnvironmentConfiguration selectSdEnvironmentConfigurationById(Long id) {
        SdEnvironmentConfiguration sd = sdEnvironmentConfigurationMapper.selectSdEnvironmentConfigurationById(id);
        String fileId = sd.getUrl();
        if (fileId != null && !"".equals(fileId) && !"null".equals(fileId)) {
            SdEquipmentStateIconFile sdEquipmentStateIconFile = new SdEquipmentStateIconFile();
            sdEquipmentStateIconFile.setStateIconId(sd.getUrl());
            sd.setiFileList(sdEquipmentIconFileMapper.selectStateIconFileList(sdEquipmentStateIconFile));
        }
        return sd;
    }

    /**
     * 查询隧道环境配置列表
     *
     * @param sdEnvironmentConfiguration 隧道环境配置
     * @return 隧道环境配置
     */
    @Override
    public List<SdEnvironmentConfiguration> selectSdEnvironmentConfigurationList(SdEnvironmentConfiguration sdEnvironmentConfiguration) {
        List<SdEnvironmentConfiguration> list = sdEnvironmentConfigurationMapper.selectSdEnvironmentConfigurationList(sdEnvironmentConfiguration);
        list.forEach(e -> {
            String fileId = e.getUrl();
            if (fileId != null && !"".equals(fileId) && !"null".equals(fileId)) {
                SdEquipmentStateIconFile sdEquipmentStateIconFile = new SdEquipmentStateIconFile();
                sdEquipmentStateIconFile.setStateIconId(e.getUrl());
                e.setiFileList(sdEquipmentIconFileMapper.selectStateIconFileList(sdEquipmentStateIconFile));
            }
        });
        return list;
    }

    /**
     * 新增隧道环境配置
     *
     * @param sdEnvironmentConfiguration 隧道环境配置
     * @return 结果
     */
    @Override
    public int insertSdEnvironmentConfiguration(MultipartFile[] file, SdEnvironmentConfiguration sdEnvironmentConfiguration) {
        sdEnvironmentConfiguration.setCreateTime(DateUtils.getNowDate());
        sdEnvironmentConfiguration.setDirection(sdEnvironmentConfiguration.getDirection().equals("null") ? null : sdEnvironmentConfiguration.getDirection());
        sdEnvironmentConfiguration.setEnvironmentType(sdEnvironmentConfiguration.getEnvironmentType().equals("null") ? null : sdEnvironmentConfiguration.getEnvironmentType());
        sdEnvironmentConfiguration.setHeight(sdEnvironmentConfiguration.getHeight().equals("null") ? null : sdEnvironmentConfiguration.getHeight());
        sdEnvironmentConfiguration.setSdName(sdEnvironmentConfiguration.getSdName().equals("null") ? null : sdEnvironmentConfiguration.getSdName());
        sdEnvironmentConfiguration.setWidth(sdEnvironmentConfiguration.getWidth().equals("null") ? null : sdEnvironmentConfiguration.getWidth());
        sdEnvironmentConfiguration.setRemark(sdEnvironmentConfiguration.getRemark().equals("null") ? null : sdEnvironmentConfiguration.getRemark());
        sdEnvironmentConfiguration.setCreateBy(SecurityUtils.getUsername());//当前创建人
        List<SdEquipmentStateIconFile> list = new ArrayList<SdEquipmentStateIconFile>();
        if (file.length > 0) {
            String guid = UUIDUtil.getRandom32BeginTimePK();// 生成guid
            sdEnvironmentConfiguration.setUrl(guid);
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
                // 加路径全名
                File dir = new File(fileServerPath + "/equipmentIcon/" + fileName);
                File filepath = new File(fileServerPath + "/equipmentIcon");

                SdEquipmentStateIconFile iconFile = new SdEquipmentStateIconFile();
                iconFile.setStateIconId(guid);
                // iconFile.setUrl(fileServerPath + "/equipmentIcon/" + fileName);
                iconFile.setUrl(imageBaseStr);
                iconFile.setStateIconName(fileName);
                iconFile.setCreateBy(SecurityUtils.getUsername());
                iconFile.setCreateTime(DateUtils.getNowDate());
                list.add(iconFile);

                if (!filepath.exists()) {
                    filepath.mkdirs();
                } else {
                }
                try {
                    file[i].transferTo(dir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sdEquipmentIconFileMapper.brachInsertStateIconFile(list);
        }
        return sdEnvironmentConfigurationMapper.insertSdEnvironmentConfiguration(sdEnvironmentConfiguration);
    }

    /**
     * 修改隧道环境配置
     *
     * @param sdEnvironmentConfiguration 隧道环境配置
     * @return 结果
     */
    @Override
    public int updateSdEnvironmentConfiguration(MultipartFile[] file, SdEnvironmentConfiguration sdEnvironmentConfiguration, Long[] ids) {
        List<SdEquipmentStateIconFile> list = new ArrayList<SdEquipmentStateIconFile>();
        sdEnvironmentConfiguration.setUpdateTime(DateUtils.getNowDate());
        String guid = sdEnvironmentConfiguration.getUrl();// 关联ID--guid
        if (guid == null || guid.equals("null") || guid.equals("")) {
            guid = UUIDUtil.getRandom32BeginTimePK();// 生成guid
        }
        sdEnvironmentConfiguration.setUrl(guid);// 文件关联ID
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
                // 加路径全名
                File dir = new File(fileServerPath + "/equipmentIcon/" + fileName);
                File filepath = new File(fileServerPath + "/equipmentIcon");

                SdEquipmentStateIconFile iconFile = new SdEquipmentStateIconFile();
                iconFile.setStateIconId(guid);
                // iconFile.setUrl(fileServerPath + "/equipmentIcon/" + fileName);
                iconFile.setUrl(imageBaseStr);
                iconFile.setStateIconName(fileName);
                iconFile.setCreateBy(SecurityUtils.getUsername());
                iconFile.setCreateTime(DateUtils.getNowDate());
                list.add(iconFile);

                if (!filepath.exists()) {
                    filepath.mkdirs();
                } else {
                }
                try {
                    file[i].transferTo(dir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sdEquipmentIconFileMapper.brachInsertStateIconFile(list);
        }
        if (ids.length > 0) {
            sdEquipmentIconFileMapper.deleteStateIconFileByIds(ids);//ids 为要删除的sd_equipment_state_icon_file id数组
        }
        return sdEnvironmentConfigurationMapper.updateSdEnvironmentConfiguration(sdEnvironmentConfiguration);
    }

    /**
     * 批量删除隧道环境配置
     *
     * @param ids 需要删除的隧道环境配置主键
     * @return 结果
     */
    @Override
    public int deleteSdEnvironmentConfigurationByIds(Long[] ids) {
        return sdEnvironmentConfigurationMapper.deleteSdEnvironmentConfigurationByIds(ids);
    }

    /**
     * 删除隧道环境配置信息
     *
     * @param id 隧道环境配置主键
     * @return 结果
     */
    @Override
    public int deleteSdEnvironmentConfigurationById(Long id) {
        return sdEnvironmentConfigurationMapper.deleteSdEnvironmentConfigurationById(id);
    }

    /**
     * 查询隧道环境配置列表-导出
     *
     * @param sdEnvironmentConfiguration 隧道环境配置
     * @return 隧道环境配置
     */
    @Override
    public List<SdEnvironmentConfiguration> selectSdEnvironmentConfigurationList_exp(SdEnvironmentConfiguration sdEnvironmentConfiguration) {
        List<SdEnvironmentConfiguration> list = sdEnvironmentConfigurationMapper.selectSdEnvironmentConfigurationList_exp(sdEnvironmentConfiguration);
        list.forEach(e -> {
            String fileId = e.getUrl();
            if (fileId != null && !"".equals(fileId) && !"null".equals(fileId)) {
                SdEquipmentStateIconFile sdEquipmentStateIconFile = new SdEquipmentStateIconFile();
                sdEquipmentStateIconFile.setStateIconId(e.getUrl());
                e.setiFileList(sdEquipmentIconFileMapper.selectStateIconFileList(sdEquipmentStateIconFile));
            }
        });
        return list;
    }
}
