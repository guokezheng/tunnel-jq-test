package com.tunnel.business.mapper.product;


import com.tunnel.business.domain.product.SdProduct;

import java.util.List;

/**
 * 产品Mapper接口
 * 
 * @author ruoyi
 * @date 2023-02-27
 */
public interface SdProductMapper 
{
    /**
     * 查询产品
     * 
     * @param id 产品主键
     * @return 产品
     */
    public SdProduct selectSdProductById(Long id);

    /**
     * 查询产品列表
     * 
     * @param sdProduct 产品
     * @return 产品集合
     */
    public List<SdProduct> selectSdProductList(SdProduct sdProduct);

    /**
     * 新增产品
     * 
     * @param sdProduct 产品
     * @return 结果
     */
    public int insertSdProduct(SdProduct sdProduct);

    /**
     * 修改产品
     * 
     * @param sdProduct 产品
     * @return 结果
     */
    public int updateSdProduct(SdProduct sdProduct);

    /**
     * 删除产品
     * 
     * @param id 产品主键
     * @return 结果
     */
    public int deleteSdProductById(Long id);

    /**
     * 批量删除产品
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdProductByIds(Long[] ids);
}
