package com.zc.iot.service;

import java.util.List;
import com.zc.iot.domain.IotProduct;

/**
 * 产品Service接口
 * 
 * @author Athena-xiepufeng
 * @date 2021-10-28
 */
public interface IIotProductService 
{
    /**
     * 查询产品
     * 
     * @param id 产品主键
     * @return 产品
     */
    public IotProduct selectIotProductById(Long id);

    /**
     * 查询产品列表
     * 
     * @param iotProduct 产品
     * @return 产品集合
     */
    public List<IotProduct> selectIotProductList(IotProduct iotProduct);

    /**
     * 新增产品
     * 
     * @param iotProduct 产品
     * @return 结果
     */
    public int insertIotProduct(IotProduct iotProduct);

    /**
     * 修改产品
     * 
     * @param iotProduct 产品
     * @return 结果
     */
    public int updateIotProduct(IotProduct iotProduct);

    /**
     * 批量删除产品
     * 
     * @param ids 需要删除的产品主键集合
     * @return 结果
     */
    public int deleteIotProductByIds(Long[] ids);

    /**
     * 删除产品信息
     * 
     * @param id 产品主键
     * @return 结果
     */
    public int deleteIotProductById(Long id);
}
