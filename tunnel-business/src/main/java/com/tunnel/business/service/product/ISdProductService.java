package com.tunnel.business.service.product;

import java.util.List;
import com.tunnel.business.domain.product.SdProduct;

/**
 * 产品Service接口
 * 
 * @author ruoyi
 * @date 2023-02-27
 */
public interface ISdProductService 
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
     * 批量删除产品
     * 
     * @param ids 需要删除的产品主键集合
     * @return 结果
     */
    public int deleteSdProductByIds(Long[] ids);

    /**
     * 删除产品信息
     * 
     * @param id 产品主键
     * @return 结果
     */
    public int deleteSdProductById(Long id);
}
