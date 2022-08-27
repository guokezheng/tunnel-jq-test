package com.zc.iot.service.impl;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.zc.common.constant.RedisKeyConstants;
import com.zc.common.core.foreignkeyservice.AssociatedService;
import com.zc.common.core.foreignkeyservice.LinkModel;
import com.zc.iot.domain.IotCategory;
import com.zc.iot.mapper.IotCategoryMapper;
import com.zc.iot.service.IIotCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 品类Service业务层处理
 * 
 * @author YangChao
 * @date 2021-10-29
 */
@Service
public class IotCategoryServiceImpl extends AssociatedService implements IIotCategoryService
{
    @Autowired
    private IotCategoryMapper iotCategoryMapper;

    @Resource
    private RedisCache redisCache;

    @PostConstruct
    public void init()
    {
        /*
          添加数据到 redis 缓存
         */
        addIotCategoryCache();
    }

    /**
     * 添加数据到 redis 缓存
     */
    public void addIotCategoryCache()
    {
        // 获取全部品类列表数据
        List<IotCategory> iotCategories = selectIotCategoryList(null);
        if (iotCategories == null || iotCategories.isEmpty())
        {
            return;
        }

        // 清楚 redis 缓存数据
        redisCache.deleteObject(RedisKeyConstants.IOT_CATEGORY);

        // 添加 redis 缓存数据
        iotCategories.forEach(val -> redisCache.setCacheMapValue(RedisKeyConstants.IOT_CATEGORY, val.getId(), val));
    }

    /**
     * 查询品类
     * 
     * @param id 品类主键
     * @return 品类
     */
    @Override
    public IotCategory selectIotCategoryById(Long id)
    {
        return iotCategoryMapper.selectIotCategoryById(id);
    }

    /**
     * 查询品类列表
     * 
     * @param iotCategory 品类
     * @return 品类
     */
    @Override
    public List<IotCategory> selectIotCategoryList(IotCategory iotCategory)
    {
        return iotCategoryMapper.selectIotCategoryList(iotCategory);
    }

    /**
     * 新增品类
     * 
     * @param iotCategory 品类
     * @return 结果
     */
    @Override
    public int insertIotCategory(IotCategory iotCategory)
    {
        iotCategory.setCreateTime(DateUtils.getNowDate());
        int result = iotCategoryMapper.insertIotCategory(iotCategory);
        // 添加到缓存
        if (result == 1)
        {
            redisCache.setCacheMapValue(RedisKeyConstants.IOT_CATEGORY, iotCategory.getId(), iotCategory);
        }
        return result;
    }

    /**
     * 修改品类
     * 
     * @param iotCategory 品类
     * @return 结果
     */
    @Override
    public int updateIotCategory(IotCategory iotCategory)
    {
        iotCategory.setUpdateTime(DateUtils.getNowDate());
        int result = iotCategoryMapper.updateIotCategory(iotCategory);
        // 更新缓存
        if (result == 1)
        {
            redisCache.setCacheMapValue(RedisKeyConstants.IOT_CATEGORY, iotCategory.getId(), iotCategory);
        }
        return result;
    }

    /**
     * 批量删除品类
     * 
     * @param ids 需要删除的品类主键
     * @return 结果
     */
    @Override
    public int deleteIotCategoryByIds(Long[] ids)
    {

        // 查看是否有数据关联，有则不能删除
        LinkModel linkModel = getWhoReferenceMe(ids);

        if (linkModel != null && linkModel.getCount() > 0)
        {
            throw new ServiceException(linkModel.getDescription(), HttpStatus.ERROR);
        }

        int result = iotCategoryMapper.deleteIotCategoryByIds(ids);

        /*删除缓存数据*/
        if (result > 0)
        {
            // 删除品类缓存数据
            redisCache.delCacheMapValue(RedisKeyConstants.IOT_CATEGORY, ids);
            // 删除功能标识符和功能id的映射关系
            Arrays.asList(ids).forEach(id -> redisCache.deleteObject(RedisKeyConstants.getCategoryFunctionIdKey(id)));
        }

        return result;
    }

    /**
     * 删除品类信息
     * 
     * @param id 品类主键
     * @return 结果
     */
    @Override
    public int deleteIotCategoryById(Long id)
    {

        // 获取关联数据信息
        LinkModel linkModel = getWhoReferenceMe(id);

        if (linkModel != null && linkModel.getCount() > 0)
        {
            return 0;
        }

        int result =  iotCategoryMapper.deleteIotCategoryById(id);

        /*删除缓存*/
        if (result == 1)
        {
            // 删除品类缓存数据
            redisCache.delCacheMapValue(RedisKeyConstants.IOT_CATEGORY, id);

            // 删除功能标识符和功能id的映射关系
            redisCache.deleteObject(RedisKeyConstants.getCategoryFunctionIdKey(id));
        }

        return result;
    }
}
