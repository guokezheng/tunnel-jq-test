package com.tunnel.business.mapper.trafficOperationControl.eventManage;

import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 图片信息Mapper接口
 *
 * @author ruoyi
 * @date 2022-02-22
 */
public interface SdTrafficImageMapper
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
     * 删除图片信息
     *
     * @param imgId 图片信息主键
     * @return 结果
     */
    public int deleteSdTrafficImageByImgId(Long imgId);

    /**
     * 批量删除图片信息
     *
     * @param imgIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdTrafficImageByImgIds(Long[] imgIds);

    /**
     * 关联业务id与图片信息
     * @param businessId 业务id
     * @param imgList 图片信息
     * @return
     */
    int updateMultiImageByImgId(@Param("businessId") Long businessId, @Param("imgList")List<SdTrafficImage> imgList);

    /**
     * 新增图片信息
     * @param businessId 业务id
     * @param imgList 图片信息
     * @return
     */
    int insertMultiImage(@Param("businessId") Long businessId,@Param("imgList") List<SdTrafficImage> imgList);

    /**
     * 删除业务id关联的图片信息
     * @param businessIds 业务id数组
     * @return
     */
    int delImageByBusinessIds(@Param("businessIds") Long[] businessIds);

    /**
     * 查询流程关联的图片信息
     * @param list 流程id
     * @return
     */
    List<SdTrafficImage> selectImageByBusinessIds(List<Long> list);
    public List<SdTrafficImage> selectImageByBusinessId(String businessId);

    /**
     * 新增设备故障清单图片上传
     * @param list
     * @return
     */
    int brachInsertFaultIconFile(List<SdTrafficImage> list);

    /**
     * 查询检修记录图片
     * @param sdTrafficImage
     * @return
     */
    public List<SdTrafficImage> selectFaultImgFileList(SdTrafficImage sdTrafficImage);

    /**
     * 修改故障信息---删除原来存储的故障图片信息
     * @param removeIds
     * @return
     */
    int deleteFaultIconFileByIds(Long[] removeIds);
}
