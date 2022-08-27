package com.zc.iot.service;

import java.util.List;
import com.zc.iot.domain.IotCategory;

/**
 * 品类Service接口
 * 
 * @author YangChao
 * @date 2021-10-29
 */
public interface IIotCategoryService 
{
    /**
     * 查询品类
     * 
     * @param id 品类主键
     * @return 品类
     */
    public IotCategory selectIotCategoryById(Long id);

    /**
     * 查询品类列表
     * 
     * @param iotCategory 品类
     * @return 品类集合
     */
    public List<IotCategory> selectIotCategoryList(IotCategory iotCategory);

    /**
     * 新增品类
     * 
     * @param iotCategory 品类
     * @return 结果
     */
    public int insertIotCategory(IotCategory iotCategory);

    /**
     * 修改品类
     * 
     * @param iotCategory 品类
     * @return 结果
     */
    public int updateIotCategory(IotCategory iotCategory);

    /**
     * 批量删除品类
     * 
     * @param ids 需要删除的品类主键集合
     * @return 结果
     */
    public int deleteIotCategoryByIds(Long[] ids);

    /**
     * 删除品类信息
     * 
     * @param id 品类主键
     * @return 结果
     */
    public int deleteIotCategoryById(Long id);
}
