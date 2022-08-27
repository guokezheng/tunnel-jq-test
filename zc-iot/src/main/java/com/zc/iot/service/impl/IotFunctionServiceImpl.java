package com.zc.iot.service.impl;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.zc.common.constant.RedisKeyConstants;
import com.zc.iot.domain.IotCategory;
import com.zc.iot.domain.IotDevice;
import com.zc.iot.domain.IotFunction;
import com.zc.iot.domain.IotProduct;
import com.zc.iot.mapper.IotFunctionMapper;
import com.zc.iot.service.IIotFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 功能定义Service业务层处理
 * 
 * @author YangChao
 * @date 2021-10-27
 */
@Service
public class IotFunctionServiceImpl implements IIotFunctionService
{
    @Autowired
    private IotFunctionMapper iotFunctionMapper;


    @Resource
    private RedisCache redisCache;

    @PostConstruct
    public void init()
    {
        /**
         * 添加数据到 redis 缓存
         */
        addIotFunctionCache();
    }


    /**
     * 添加数据到 redis 缓存
     */
    public void addIotFunctionCache()
    {
        // 获取全部品类列表数据
        List<IotFunction> iotCategories = selectIotFunctionList(null);
        if (iotCategories == null || iotCategories.isEmpty())
        {
            return;
        }

        // 清楚 redis 缓存数据
        redisCache.deleteObject(RedisKeyConstants.IOT_FUNCTION);

        // 添加 redis 缓存数据
        iotCategories.forEach(val -> redisCache.setCacheMapValue(RedisKeyConstants.IOT_FUNCTION, val.getId(), val));
    }

    /**
     * 查询功能定义
     * 
     * @param id 功能定义主键
     * @return 功能定义
     */
    @Override
    public IotFunction selectIotFunctionById(Long id)
    {
        return iotFunctionMapper.selectIotFunctionById(id);
    }

    /**
     * 查询功能定义列表
     * 
     * @param iotFunction 功能定义
     * @return 功能定义
     */
    @Override
    public List<IotFunction> selectIotFunctionList(IotFunction iotFunction)
    {
        return iotFunctionMapper.selectIotFunctionList(iotFunction);
    }

    /**
     * 新增功能定义
     * 
     * @param iotFunction 功能定义
     * @return 结果
     */
    @Override
    public int insertIotFunction(IotFunction iotFunction)
    {

        Long categoryKey = iotFunction.getCategoryKey();
        String identifier = iotFunction.getIdentifier();

        if (categoryKey == null)
        {
            throw new ServiceException("所属品类不存在", HttpStatus.BAD_REQUEST);
        }

        IotCategory iotCategory = redisCache.getCacheMapValue(RedisKeyConstants.IOT_CATEGORY, categoryKey);

        if (iotCategory == null)
        {
            throw new ServiceException("所属品类不存在", HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isEmpty(identifier))
        {
            throw new ServiceException("功能标识不存在", HttpStatus.BAD_REQUEST);
        }
        IotFunction iotFunction1 = new IotFunction();
        iotFunction1.setIdentifier(identifier);
        iotFunction1.setCategoryKey(categoryKey);
        List<IotFunction> iotFunctions = selectIotFunctionList(iotFunction1);

        if (iotFunctions != null && !iotFunctions.isEmpty())
        {
            throw new ServiceException("相同品类下标识符不可重复", HttpStatus.CONFLICT);
        }

        iotFunction.setCreateTime(DateUtils.getNowDate());

        int result = iotFunctionMapper.insertIotFunction(iotFunction);
        // 添加到缓存
        if (result == 1)
        {
            // 品类数据添加到缓存
            redisCache.setCacheMapValue(RedisKeyConstants.IOT_FUNCTION, iotFunction.getId(), iotFunction);

            // 功能标识符和功能id的映射关系添加到缓存
            redisCache.setCacheMapValue(RedisKeyConstants.getCategoryFunctionIdKey(categoryKey), identifier, iotFunction.getId());
        }
        return result;
    }

    /**
     * 修改功能定义
     * 
     * @param iotFunction 功能定义
     * @return 结果
     */
    @Override
    public int updateIotFunction(IotFunction iotFunction)
    {

        Long categoryKey = iotFunction.getCategoryKey();
        Long id = iotFunction.getId();
        String identifier = iotFunction.getIdentifier();

        if (categoryKey == null)
        {
            throw new ServiceException("所属品类不存在", HttpStatus.BAD_REQUEST);
        }

        IotCategory iotCategory = redisCache.getCacheMapValue(RedisKeyConstants.IOT_CATEGORY, categoryKey);

        if (iotCategory == null)
        {
            throw new ServiceException("所属品类不存在", HttpStatus.BAD_REQUEST);
        }

        if (id == null)
        {
            throw new ServiceException("主键不存在", HttpStatus.BAD_REQUEST);
        }

        IotFunction iotFunction1 = selectIotFunctionById(id);

        if (iotFunction1 == null)
        {
            throw new ServiceException("没有找到对应的数据", HttpStatus.CONFLICT);
        }

        if (StringUtils.isEmpty(identifier))
        {
            throw new ServiceException("功能标识不存在", HttpStatus.BAD_REQUEST);
        }

        IotFunction iotFunction2 = new IotFunction();
        iotFunction2.setIdentifier(identifier);
        iotFunction2.setCategoryKey(categoryKey);
        List<IotFunction> iotFunctions = selectIotFunctionList(iotFunction2);

        if (iotFunctions != null && !iotFunctions.isEmpty() && !iotFunctions.get(0).getId().equals(id))
        {
            throw new ServiceException("相同品类下标识符不可重复", HttpStatus.CONFLICT);
        }

        iotFunction.setUpdateTime(DateUtils.getNowDate());

        int result = iotFunctionMapper.updateIotFunction(iotFunction);
        // 更新缓存
        if (result == 1)
        {
            /*更新功能缓存数据*/
            redisCache.setCacheMapValue(RedisKeyConstants.IOT_FUNCTION, iotFunction.getId(), iotFunction);

            if (!identifier.equals(iotFunction1.getIdentifier()))
            {
                redisCache.delCacheMapValue(RedisKeyConstants.getCategoryFunctionIdKey(categoryKey), iotFunction1.getIdentifier());
                redisCache.setCacheMapValue(RedisKeyConstants.getCategoryFunctionIdKey(categoryKey), identifier, id);
            }
        }
        return result;
    }

    /**
     * 批量删除功能定义
     * 
     * @param ids 需要删除的功能定义主键
     * @return 结果
     */
    @Override
    public int deleteIotFunctionByIds(Long[] ids)
    {
        int result = iotFunctionMapper.deleteIotFunctionByIds(ids);

        if (result > 0)
        {
            for (Long id : ids)
            {
                IotFunction iotFunction = redisCache.getCacheMapValue(RedisKeyConstants.IOT_FUNCTION, id);
                if (iotFunction == null) continue;
                redisCache.delCacheMapValue(RedisKeyConstants.getCategoryFunctionIdKey(iotFunction.getCategoryKey()), iotFunction.getIdentifier());
                deleteDeviceFunctionValueCache(id);
            }
            
            /*删除功能缓存数据*/
            redisCache.delCacheMapValue(RedisKeyConstants.IOT_FUNCTION, ids);

        }

        return result;
    }

    /**
     * 删除功能定义信息
     * 
     * @param id 功能定义主键
     * @return 结果
     */
    @Override
    public int deleteIotFunctionById(Long id)
    {
        int result = iotFunctionMapper.deleteIotFunctionById(id);

        // 删除缓存
        if (result == 1)
        {
            IotFunction iotFunction = redisCache.getCacheMapValue(RedisKeyConstants.IOT_FUNCTION, id);

            if (iotFunction == null) return 0;

            redisCache.delCacheMapValue(RedisKeyConstants.getCategoryFunctionIdKey(iotFunction.getCategoryKey()), iotFunction.getIdentifier());

            redisCache.delCacheMapValue(RedisKeyConstants.IOT_FUNCTION, id);
            // 删除缓存实时数据
            deleteDeviceFunctionValueCache(id);
        }

        return result;
    }

    /**
     * 删除缓存实时数据
     * @param functionId
     */
    private void deleteDeviceFunctionValueCache(Long functionId)
    {
        if (functionId == null)
        {
            return;
        }

        Map<String, IotDevice> iotDeviceMap = redisCache.getCacheMapValue(RedisKeyConstants.IOT_DEVICE);

        if (iotDeviceMap == null || iotDeviceMap.isEmpty())
        {
            return;
        }

        iotDeviceMap.forEach((deviceId, iotDevice) ->
        {
            redisCache.delCacheMapValue(RedisKeyConstants.getDeviceFunctionValueKey(iotDevice.getId()), functionId);
        });

    }
}
