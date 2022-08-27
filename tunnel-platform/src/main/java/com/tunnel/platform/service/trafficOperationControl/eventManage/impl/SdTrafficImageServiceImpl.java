package com.tunnel.platform.service.trafficOperationControl.eventManage.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.platform.domain.trafficOperationControl.eventManage.SdTrafficImage;
import com.tunnel.platform.mapper.trafficOperationControl.eventManage.SdTrafficImageMapper;
import com.tunnel.platform.service.trafficOperationControl.eventManage.ISdTrafficImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 图片信息Service业务层处理
 *
 * @author ruoyi
 * @date 2022-02-22
 */
@Service
public class SdTrafficImageServiceImpl implements ISdTrafficImageService
{
    @Autowired
    private SdTrafficImageMapper sdTrafficImageMapper;

    /**
     * 查询图片信息
     *
     * @param imgId 图片信息主键
     * @return 图片信息
     */
    @Override
    public SdTrafficImage selectSdTrafficImageByImgId(Long imgId)
    {
        return sdTrafficImageMapper.selectSdTrafficImageByImgId(imgId);
    }

    /**
     * 查询图片信息列表
     *
     * @param sdTrafficImage 图片信息
     * @return 图片信息
     */
    @Override
    public List<SdTrafficImage> selectSdTrafficImageList(SdTrafficImage sdTrafficImage)
    {
        return sdTrafficImageMapper.selectSdTrafficImageList(sdTrafficImage);
    }

    /**
     * 新增图片信息
     *
     * @param sdTrafficImage 图片信息
     * @return 结果
     */
    @Override
    public int insertSdTrafficImage(SdTrafficImage sdTrafficImage)
    {
        sdTrafficImage.setCreateTime(DateUtils.getNowDate());
        return sdTrafficImageMapper.insertSdTrafficImage(sdTrafficImage);
    }

    /**
     * 修改图片信息
     *
     * @param sdTrafficImage 图片信息
     * @return 结果
     */
    @Override
    public int updateSdTrafficImage(SdTrafficImage sdTrafficImage)
    {
        sdTrafficImage.setUpdateTime(DateUtils.getNowDate());
        return sdTrafficImageMapper.updateSdTrafficImage(sdTrafficImage);
    }

    /**
     * 批量删除图片信息
     *
     * @param imgIds 需要删除的图片信息主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficImageByImgIds(Long[] imgIds)
    {
        return sdTrafficImageMapper.deleteSdTrafficImageByImgIds(imgIds);
    }

    /**
     * 删除图片信息信息
     *
     * @param imgId 图片信息主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficImageByImgId(Long imgId)
    {
        return sdTrafficImageMapper.deleteSdTrafficImageByImgId(imgId);
    }

    /**
     * 关联业务id与图片信息
     *
     * @param businessId 业务id
     * @param imgList    图片信息
     * @return
     */
    @Override
    public int updateMultiImageByImgId(Long businessId, List<SdTrafficImage> imgList) {
        return sdTrafficImageMapper.updateMultiImageByImgId(businessId,imgList);
    }

    /**
     * 新增图片信息
     *
     * @param businessId 业务id
     * @param imgList    图片信息
     * @return
     */
    @Override
    public int insertMultiImage(Long businessId, List<SdTrafficImage> imgList) {
        return sdTrafficImageMapper.insertMultiImage(businessId,imgList);
    }

    /**
     * 删除业务id关联的图片信息
     *
     * @param businessIds 业务id数组
     * @return
     */
    @Override
    public int delImageByBusinessIds(Long[] businessIds) {
        return sdTrafficImageMapper.delImageByBusinessIds(businessIds);
    }

    /**
     * 查询流程关联的图片信息
     *
     * @param list 流程id
     * @return
     */
    @Override
    public List<SdTrafficImage> selectImageByBusinessIds(List<Long> list) {
        return sdTrafficImageMapper.selectImageByBusinessIds(list);
    }

}
