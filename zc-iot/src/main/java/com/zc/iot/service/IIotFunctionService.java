package com.zc.iot.service;

import java.util.List;
import com.zc.iot.domain.IotFunction;

/**
 * 功能定义Service接口
 * 
 * @author YangChao
 * @date 2021-10-27
 */
public interface IIotFunctionService
{
    /**
     * 查询功能定义
     * 
     * @param id 功能定义主键
     * @return 功能定义
     */
    public IotFunction selectIotFunctionById(Long id);

    /**
     * 查询功能定义列表
     * 
     * @param iotFunction 功能定义
     * @return 功能定义集合
     */
    public List<IotFunction> selectIotFunctionList(IotFunction iotFunction);

    /**
     * 新增功能定义
     * 
     * @param iotFunction 功能定义
     * @return 结果
     */
    public int insertIotFunction(IotFunction iotFunction);

    /**
     * 修改功能定义
     * 
     * @param iotFunction 功能定义
     * @return 结果
     */
    public int updateIotFunction(IotFunction iotFunction);

    /**
     * 批量删除功能定义
     * 
     * @param ids 需要删除的功能定义主键集合
     * @return 结果
     */
    public int deleteIotFunctionByIds(Long[] ids);

    /**
     * 删除功能定义信息
     * 
     * @param id 功能定义主键
     * @return 结果
     */
    public int deleteIotFunctionById(Long id);
}
