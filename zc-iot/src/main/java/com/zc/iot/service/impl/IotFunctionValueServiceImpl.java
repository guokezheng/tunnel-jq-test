package com.zc.iot.service.impl;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.zc.common.constant.RedisKeyConstants;
import com.zc.iot.domain.IotFunctionValue;
import com.zc.iot.mapper.IotFunctionValueMapper;
import com.zc.iot.service.IIotFunctionValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 功能值定义Service业务层处理
 *
 * @author YangChao
 * @date 2021-10-27
 */
@Service
public class IotFunctionValueServiceImpl implements IIotFunctionValueService {

    @Autowired
    private IotFunctionValueMapper iotFunctionValueMapper;

    @Resource
    private RedisCache redisCache;

    @PostConstruct
    public void init()
    {
        /*
          添加数据到 redis 缓存
         */
        addIotFunctionValueCache();
    }

    /**
     * 添加数据到 redis 缓存
     */
    public void addIotFunctionValueCache()
    {
        // 获取全部品类列表数据
        List<IotFunctionValue> iotCategories = selectIotFunctionValueList(null);

        if (iotCategories == null || iotCategories.isEmpty())
        {
            return;
        }

        // 清楚 redis 缓存数据
        redisCache.deleteObject(RedisKeyConstants.IOT_FUNCTION_VALUE);

        // 添加 redis 缓存数据
        iotCategories.forEach(val -> redisCache.setCacheMapValue(RedisKeyConstants.IOT_FUNCTION_VALUE, val.getId(), val));
    }

    /**
     * 查询功能值定义
     *
     * @param id 功能值定义主键
     * @return 功能值定义
     */
    @Override
    public IotFunctionValue selectIotFunctionValueById(Long id) {
        return iotFunctionValueMapper.selectIotFunctionValueById(id);
    }

    /**
     * 查询功能值定义列表
     *
     * @param iotFunctionValue 功能值定义
     * @return 功能值定义
     */
    @Override
    public List<IotFunctionValue> selectIotFunctionValueList(IotFunctionValue iotFunctionValue) {
        return iotFunctionValueMapper.selectIotFunctionValueList(iotFunctionValue);
    }

    /**
     * 新增功能值定义
     *
     * @param iotFunctionValue 功能值定义
     * @return 结果
     */
    @Override
    public int insertIotFunctionValue(IotFunctionValue iotFunctionValue) {
        iotFunctionValue.setCreateTime(DateUtils.getNowDate());

        int result = iotFunctionValueMapper.insertIotFunctionValue(iotFunctionValue);

        // 添加到缓存
        if (result == 1)
        {
            redisCache.setCacheMapValue(RedisKeyConstants.IOT_FUNCTION_VALUE, iotFunctionValue.getId(), iotFunctionValue);
        }
        return result;
    }

    /**
     * 修改功能值定义
     *
     * @param iotFunctionValue 功能值定义
     * @return 结果
     */
    @Override
    public int updateIotFunctionValue(IotFunctionValue iotFunctionValue) {

        iotFunctionValue.setUpdateTime(DateUtils.getNowDate());

        int result = iotFunctionValueMapper.updateIotFunctionValue(iotFunctionValue);
        // 更新缓存
        if (result == 1)
        {
            redisCache.setCacheMapValue(RedisKeyConstants.IOT_FUNCTION_VALUE, iotFunctionValue.getId(), iotFunctionValue);
        }
        return result;
    }

    /**
     * 批量删除功能值定义
     *
     * @param ids 需要删除的功能值定义主键
     * @return 结果
     */
    @Override
    public int deleteIotFunctionValueByIds(Long[] ids) {

        int result = iotFunctionValueMapper.deleteIotFunctionValueByIds(ids);

        /*删除缓存数据*/
        if (result > 0)
        {
            redisCache.delCacheMapValue(RedisKeyConstants.IOT_FUNCTION_VALUE, ids);
        }

        return result;
    }

    /**
     * 删除功能值定义信息
     *
     * @param id 功能值定义主键
     * @return 结果
     */
    @Override
    public int deleteIotFunctionValueById(Long id) {

        int result = iotFunctionValueMapper.deleteIotFunctionValueById(id);

        /*删除缓存*/
        if (result == 1)
        {
            redisCache.delCacheMapValue(RedisKeyConstants.IOT_FUNCTION_VALUE, id);
        }

        return result;
    }

    @Transactional
    @Override
    public int saveFvData(IotFunctionValue iotFunctionValue) {

        int res = 0;

        try {
            List<IotFunctionValue> iotFunctionValues = iotFunctionValue.getSaveFvDataList();

            if (iotFunctionValues == null || iotFunctionValues.isEmpty()){
                return res;
            }

            for (IotFunctionValue functionValue : iotFunctionValues) {

                if(StringUtils.isEmpty(functionValue.getValue()) || StringUtils.isEmpty(functionValue.getName())){
                    continue;
                }
                int up = updateIotFunctionValue(functionValue);
                // 更新失败 则新增
                if (up <= 0) {
                    insertIotFunctionValue(functionValue);
                }
            }

            res = 1;
        }catch (Exception e){
            e.printStackTrace();
            res = -1;
        }
        return res;
    }
}
