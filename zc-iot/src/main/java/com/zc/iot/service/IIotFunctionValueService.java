package com.zc.iot.service;

import com.zc.iot.domain.IotFunctionValue;

import java.util.List;

/**
 * 功能值定义Service接口
 * 
 * @author YangChao
 * @date 2021-10-27
 */
public interface IIotFunctionValueService
{
    /**
     * 查询功能值定义
     * 
     * @param id 功能值定义主键
     * @return 功能值定义
     */
    public IotFunctionValue selectIotFunctionValueById(Long id);

    /**
     * 查询功能值定义列表
     * 
     * @param iotFunctionValue 功能值定义
     * @return 功能值定义集合
     */
    public List<IotFunctionValue> selectIotFunctionValueList(IotFunctionValue iotFunctionValue);

    /**
     * 新增功能值定义
     * 
     * @param iotFunctionValue 功能值定义
     * @return 结果
     */
    public int insertIotFunctionValue(IotFunctionValue iotFunctionValue);

    /**
     * 修改功能值定义
     * 
     * @param iotFunctionValue 功能值定义
     * @return 结果
     */
    public int updateIotFunctionValue(IotFunctionValue iotFunctionValue);

    /**
     * 批量删除功能值定义
     * 
     * @param ids 需要删除的功能值定义主键集合
     * @return 结果
     */
    public int deleteIotFunctionValueByIds(Long[] ids);

    /**
     * 删除功能值定义信息
     * 
     * @param id 功能值定义主键
     * @return 结果
     */
    public int deleteIotFunctionValueById(Long id);


    /**
     * 保存抽屉数据
     */
    public int saveFvData(IotFunctionValue iotFunctionValue);
}
