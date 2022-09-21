package com.tunnel.business.service.informationBoard;

import com.tunnel.business.domain.informationBoard.SdPictureUpload;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 情报板使用图片Service接口
 *
 * @author ruoyi
 * @date 2022-03-22
 */
public interface ISdPictureUploadService {
    /**
     * 查询情报板使用图片
     *
     * @param id 情报板使用图片主键
     * @return 情报板使用图片
     */
    SdPictureUpload selectSdPictureUploadById(Long id);

    /**
     * 查询情报板使用图片列表
     *
     * @param sdPictureUpload 情报板使用图片
     * @return 情报板使用图片集合
     */
    List<SdPictureUpload> selectSdPictureUploadList(SdPictureUpload sdPictureUpload);

    /**
     * 新增情报板使用图片
     *
     * @param sdPictureUpload 情报板使用图片
     * @return 结果
     */
    int insertSdPictureUpload(MultipartFile[] file, SdPictureUpload sdPictureUpload) throws IOException;

    /**
     * 修改情报板使用图片
     *
     * @param sdPictureUpload 情报板使用图片
     * @return 结果
     */
    int updateSdPictureUpload(MultipartFile[] file, SdPictureUpload sdPictureUpload) throws IOException;

    /**
     * 批量删除情报板使用图片
     *
     * @param ids 需要删除的情报板使用图片主键集合
     * @return 结果
     */
    int deleteSdPictureUploadByIds(Long[] ids);

    /**
     * 删除情报板使用图片信息
     *
     * @param id 情报板使用图片主键
     * @return 结果
     */
    int deleteSdPictureUploadById(Long id);
}
