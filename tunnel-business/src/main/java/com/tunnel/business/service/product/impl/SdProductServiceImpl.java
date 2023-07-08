package com.tunnel.business.service.product.impl;

import java.util.List;

import com.tunnel.business.domain.product.SdProduct;
import com.tunnel.business.mapper.product.SdProductMapper;
import com.tunnel.business.service.product.ISdProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 产品Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-02-27
 */
@Service
public class SdProductServiceImpl implements ISdProductService
{
    @Autowired
    private SdProductMapper sdProductMapper;

    /**
     * 查询产品
     * 
     * @param id 产品主键
     * @return 产品
     */
    @Override
    public SdProduct selectSdProductById(Long id)
    {
        return sdProductMapper.selectSdProductById(id);
    }

    /**
     * 查询产品列表
     * 
     * @param sdProduct 产品
     * @return 产品
     */
    @Override
    public List<SdProduct> selectSdProductList(SdProduct sdProduct)
    {
        return sdProductMapper.selectSdProductList(sdProduct);
    }

    /**
     * 新增产品
     * 
     * @param sdProduct 产品
     * @return 结果
     */
    @Override
    public int insertSdProduct(SdProduct sdProduct)
    {
        return sdProductMapper.insertSdProduct(sdProduct);
    }

    /**
     * 修改产品
     * 
     * @param sdProduct 产品
     * @return 结果
     */
    @Override
    public int updateSdProduct(SdProduct sdProduct)
    {
        return sdProductMapper.updateSdProduct(sdProduct);
    }

    /**
     * 批量删除产品
     * 
     * @param ids 需要删除的产品主键
     * @return 结果
     */
    @Override
    public int deleteSdProductByIds(Long[] ids)
    {
        return sdProductMapper.deleteSdProductByIds(ids);
    }

    /**
     * 删除产品信息
     * 
     * @param id 产品主键
     * @return 结果
     */
    @Override
    public int deleteSdProductById(Long id)
    {
        return sdProductMapper.deleteSdProductById(id);
    }
}
