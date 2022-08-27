package com.tunnel.platform.service.informationBoard.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tunnel.platform.mapper.informationBoard.SdPictureUploadMapper;
import com.tunnel.platform.domain.informationBoard.SdPictureUpload;
import com.tunnel.platform.service.informationBoard.ISdPictureUploadService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 情报板使用图片Service业务层处理
 *
 * @author ruoyi
 * @date 2022-03-22
 */
@Service
public class SdPictureUploadServiceImpl implements ISdPictureUploadService
{
    @Autowired
    private SdPictureUploadMapper sdPictureUploadMapper;

    /**
     * 查询情报板使用图片
     *
     * @param id 情报板使用图片主键
     * @return 情报板使用图片
     */
    @Override
    public SdPictureUpload selectSdPictureUploadById(Long id)
    {
        return sdPictureUploadMapper.selectSdPictureUploadById(id);
    }

    /**
     * 查询情报板使用图片列表
     *
     * @param sdPictureUpload 情报板使用图片
     * @return 情报板使用图片
     */
    @Override
    public List<SdPictureUpload> selectSdPictureUploadList(SdPictureUpload sdPictureUpload)
    {
        sdPictureUpload.setDeleteflag("0");
        return sdPictureUploadMapper.selectSdPictureUploadList(sdPictureUpload);
    }

    /**
     * 新增情报板使用图片
     *
     * @param sdPictureUpload 情报板使用图片
     * @return 结果
     */
    @Override
    public int insertSdPictureUpload(MultipartFile[] file, SdPictureUpload sdPictureUpload) throws IOException {
        sdPictureUpload.setCreateTime(DateUtils.getNowDate());
        sdPictureUpload.setCreateBy(SecurityUtils.getUsername());
        if (file != null && file.length > 0) {
            for (int i = 0; i < file.length; i++) {
                // 从缓存中获取文件存储路径
                String fileServerPath = RuoYiConfig.getUploadPath();
                // 原图文件名
                String filename = file[i].getOriginalFilename();
                // 原图扩展名
                String extendName = filename.substring(filename.lastIndexOf("\\") + 1, filename.length());
                // 新的全名
                String fileName = extendName;
                // 加路径全名
                File dir = new File(fileServerPath + "/vmsTemplatePicture/" + fileName);
                File filepath = new File(fileServerPath + "/vmsTemplatePicture");

                sdPictureUpload.setPictureName(fileName);
                sdPictureUpload.setPictureUrl(fileServerPath + "/vmsTemplatePicture/" + fileName);
                sdPictureUpload.setDeleteflag("0");

                if (!filepath.exists()) {
                    filepath.mkdirs();
                } else {
                }
                file[i].transferTo(dir);
            }
        }
        return sdPictureUploadMapper.insertSdPictureUpload(sdPictureUpload);
    }

    /**
     * 修改情报板使用图片
     *
     * @param sdPictureUpload 情报板使用图片
     * @return 结果
     */
    @Override
    public int updateSdPictureUpload(MultipartFile[] file, SdPictureUpload sdPictureUpload) throws IOException {
        sdPictureUpload.setUpdateTime(DateUtils.getNowDate());
        sdPictureUpload.setUpdateBy(SecurityUtils.getUsername());
        if (file != null && file.length > 0) {
            for (int i = 0; i < file.length; i++) {
                // 从缓存中获取文件存储路径
                String fileServerPath = RuoYiConfig.getUploadPath();
                // 原图文件名
                String filename = file[i].getOriginalFilename();
                // 原图扩展名
                String extendName = filename.substring(filename.lastIndexOf("\\") + 1, filename.length());
                // 新的全名
                String fileName = extendName;
                // 加路径全名
                File dir = new File(fileServerPath + "/vmsTemplatePicture/" + fileName);
                File filepath = new File(fileServerPath + "/vmsTemplatePicture");

                sdPictureUpload.setPictureName(fileName);
                sdPictureUpload.setPictureUrl(fileServerPath + "/vmsTemplatePicture/" + fileName);
                sdPictureUpload.setDeleteflag("0");

                if (!filepath.exists()) {
                    filepath.mkdirs();
                } else {
                }
                file[i].transferTo(dir);
            }
        }
        return sdPictureUploadMapper.updateSdPictureUpload(sdPictureUpload);
    }

    /**
     * 批量删除情报板使用图片
     *
     * @param ids 需要删除的情报板使用图片主键
     * @return 结果
     */
    @Override
    public int deleteSdPictureUploadByIds(Long[] ids)
    {
        return sdPictureUploadMapper.deleteSdPictureUploadByIds(ids);
    }

    /**
     * 删除情报板使用图片信息
     *
     * @param id 情报板使用图片主键
     * @return 结果
     */
    @Override
    public int deleteSdPictureUploadById(Long id)
    {
        return sdPictureUploadMapper.deleteSdPictureUploadById(id);
    }
}
