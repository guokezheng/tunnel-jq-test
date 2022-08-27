package com.zc.iot.service.impl;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.zc.common.constant.RedisKeyConstants;
import com.zc.common.core.foreignkeyservice.AssociatedService;
import com.zc.common.core.foreignkeyservice.ForeignKeyService;
import com.zc.common.core.foreignkeyservice.LinkModel;
import com.zc.iot.domain.IotProduct;
import com.zc.iot.mapper.IotProductMapper;
import com.zc.iot.service.IIotProductService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 产品Service业务层处理
 * 
 * @author Athena-xiepufeng
 * @date 2021-10-28
 */
@Service
public class IotProductServiceImpl implements IIotProductService, ForeignKeyService
{
    @Resource
    private IotProductMapper iotProductMapper;

    @Resource(name="iotCategoryServiceImpl")
    private AssociatedService associatedService;


    @Resource
    private RedisCache redisCache;

    @PostConstruct
    public void init()
    {
        /*
          外键注册
         */
        registerForeignKey();
        /*
          添加数据到 redis 缓存
         */
        addIotProductCache();
    }

    /**
     * 添加数据到 redis 缓存
     */
    public void addIotProductCache()
    {
        // 获取全部品类列表数据
        List<IotProduct> iotCategories = selectIotProductList(null);
        if (iotCategories == null || iotCategories.isEmpty())
        {
            return;
        }

        // 清楚 redis 缓存数据
        redisCache.deleteObject(RedisKeyConstants.IOT_PRODUCT);

        // 添加 redis 缓存数据
        iotCategories.forEach(val -> redisCache.setCacheMapValue(RedisKeyConstants.IOT_PRODUCT, val.getId(), val));
    }

    /**
     * 查询产品
     * 
     * @param id 产品主键
     * @return 产品
     */
    @Override
    public IotProduct selectIotProductById(Long id)
    {
        return iotProductMapper.selectIotProductById(id);
    }

    /**
     * 查询产品列表
     * 
     * @param iotProduct 产品
     * @return 产品
     */
    @Override
    public List<IotProduct> selectIotProductList(IotProduct iotProduct)
    {
        return iotProductMapper.selectIotProductList(iotProduct);
    }

    /**
     * 新增产品
     * 
     * @param iotProduct 产品
     * @return 结果
     */
    @Override
    public int insertIotProduct(IotProduct iotProduct)
    {
        iotProduct.setCreateTime(DateUtils.getNowDate());

        int result = iotProductMapper.insertIotProduct(iotProduct);
        // 添加到缓存
        if (result == 1)
        {
            redisCache.setCacheMapValue(RedisKeyConstants.IOT_PRODUCT, iotProduct.getId(), iotProduct);
        }
        return result;
    }

    /**
     * 修改产品
     * 
     * @param iotProduct 产品
     * @return 结果
     */
    @Override
    public int updateIotProduct(IotProduct iotProduct)
    {
        iotProduct.setUpdateTime(DateUtils.getNowDate());

        int result = iotProductMapper.updateIotProduct(iotProduct);
        // 更新缓存
        if (result == 1)
        {
            redisCache.setCacheMapValue(RedisKeyConstants.IOT_PRODUCT, iotProduct.getId(), iotProduct);
        }
        return result;
    }

    /**
     * 批量删除产品
     * 
     * @param ids 需要删除的产品主键
     * @return 结果
     */
    @Override
    public int deleteIotProductByIds(Long[] ids)
    {

        int result = iotProductMapper.deleteIotProductByIds(ids);

        if (result > 0)
        {
            redisCache.delCacheMapValue(RedisKeyConstants.IOT_PRODUCT, ids);
        }

        return result;
    }

    /**
     * 删除产品信息
     * 
     * @param id 产品主键
     * @return 结果
     */
    @Override
    public int deleteIotProductById(Long id)
    {
        int result =  iotProductMapper.deleteIotProductById(id);
        // 删除缓存
        if (result == 1)
        {
            redisCache.delCacheMapValue(RedisKeyConstants.IOT_PRODUCT, id);
        }

        return result;
    }

    @Override
    public LinkModel findByForeignKey(String foreignKey, Long foreignKeyValue) {

        IotProduct iotProduct = new IotProduct();
        iotProduct.setCategoryKey(foreignKeyValue);

        LinkModel linkModel = new LinkModel();

        List<IotProduct>  iotProducts = selectIotProductList(iotProduct);

        if (iotProducts == null) {
            linkModel.setDescription("服务异常");
            return linkModel;
        }

        if (!iotProducts.isEmpty()) {
            linkModel.setCount(iotProducts.size());
            linkModel.setDescription("产品数据已关联");
            return linkModel;
        }

        linkModel.setCount(0);

        return linkModel;
    }

    @Override
    public boolean deleteAllByForeignKey(String foreignKey, Long foreignKeyValue)
    {

        IotProduct iotProduct = new IotProduct();
        iotProduct.setCategoryKey(foreignKeyValue);
        List<IotProduct> iotProducts = selectIotProductList(iotProduct);

        if (iotProducts == null)
        {
            return false;
        }

        if (iotProducts.isEmpty())
        {
            return true;
        }

        /*删除缓存数据*/
        iotProducts.forEach(val -> redisCache.delCacheMapValue(RedisKeyConstants.IOT_PRODUCT, val.getId()));

        if (iotProductMapper.deleteIotProductByCategoryKey(foreignKeyValue) > 0)
        {
            return true;
        }

        return false;
    }

    @Override
    public void registerForeignKey() {
        associatedService.registerForeignKey(this, "categoryKey");
    }
}
