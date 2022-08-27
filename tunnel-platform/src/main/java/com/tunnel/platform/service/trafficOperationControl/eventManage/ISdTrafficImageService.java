package com.tunnel.platform.service.trafficOperationControl.eventManage;

import com.tunnel.platform.domain.trafficOperationControl.eventManage.SdTrafficImage;

import java.util.List;

/**
 * 图片信息Service接口
 *
 * @author ruoyi
 * @date 2022-02-22
 */
public interface ISdTrafficImageService
{
    /**
     * 查询图片信息
     *
     * @param imgId 图片信息主键
     * @return 图片信息
     */
    public SdTrafficImage selectSdTrafficImageByImgId(Long imgId);

    /**
     * 查询图片信息列表
     *
     * @param sdTrafficImage 图片信息
     * @return 图片信息集合
     */
    public List<SdTrafficImage> selectSdTrafficImageList(SdTrafficImage sdTrafficImage);

    /**
     * 新增图片信息
     *
     * @param sdTrafficImage 图片信息
     * @return 结果
     */
    public int insertSdTrafficImage(SdTrafficImage sdTrafficImage);

    /**
     * 修改图片信息
     *
     * @param sdTrafficImage 图片信息
     * @return 结果
     */
    public int updateSdTrafficImage(SdTrafficImage sdTrafficImage);

    /**
     * 批量删除图片信息
     *
     * @param imgIds 需要删除的图片信息主键集合
     * @return 结果
     */
    public int deleteSdTrafficImageByImgIds(Long[] imgIds);

    /**
     * 删除图片信息信息
     *
     * @param imgId 图片信息主键
     * @return 结果
     */
    public int deleteSdTrafficImageByImgId(Long imgId);


    /**
     * 关联业务id与图片信息
     * @param businessId 业务id
     * @param imgList 图片信息
     * @return
     */
    int updateMultiImageByImgId(Long businessId,List<SdTrafficImage> imgList);


    /**
     * 新增图片信息
     * @param businessId 业务id
     * @param imgList 图片信息
     * @return
     */
    int insertMultiImage(Long businessId,List<SdTrafficImage> imgList);


    /**
     * 删除业务id关联的图片信息
     * @param businessIds 业务id数组
     * @return
     */
    int delImageByBusinessIds(Long[] businessIds);


    /**
     * 查询流程关联的图片信息
     * @param list 流程id
     * @return
     */
    List<SdTrafficImage> selectImageByBusinessIds(List<Long> list);
}
