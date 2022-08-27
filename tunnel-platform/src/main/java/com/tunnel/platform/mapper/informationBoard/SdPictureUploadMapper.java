package com.tunnel.platform.mapper.informationBoard;

import java.util.List;
import com.tunnel.platform.domain.informationBoard.SdPictureUpload;

/**
 * 情报板使用图片Mapper接口
 *
 * @author ruoyi
 * @date 2022-03-22
 */
public interface SdPictureUploadMapper
{
    /**
     * 查询情报板使用图片
     *
     * @param id 情报板使用图片主键
     * @return 情报板使用图片
     */
    public SdPictureUpload selectSdPictureUploadById(Long id);

    /**
     * 查询情报板使用图片列表
     *
     * @param sdPictureUpload 情报板使用图片
     * @return 情报板使用图片集合
     */
    public List<SdPictureUpload> selectSdPictureUploadList(SdPictureUpload sdPictureUpload);

    /**
     * 新增情报板使用图片
     *
     * @param sdPictureUpload 情报板使用图片
     * @return 结果
     */
    public int insertSdPictureUpload(SdPictureUpload sdPictureUpload);

    /**
     * 修改情报板使用图片
     *
     * @param sdPictureUpload 情报板使用图片
     * @return 结果
     */
    public int updateSdPictureUpload(SdPictureUpload sdPictureUpload);

    /**
     * 删除情报板使用图片
     *
     * @param id 情报板使用图片主键
     * @return 结果
     */
    public int deleteSdPictureUploadById(Long id);

    /**
     * 批量删除情报板使用图片
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdPictureUploadByIds(Long[] ids);
}
