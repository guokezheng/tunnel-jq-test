package com.tunnel.business.mapper.informationBoard;


import com.tunnel.business.domain.informationBoard.IotBoardTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 情报板模板Mapper接口
 *
 * @author 刘方堃
 * @date 2021-11-30
 */
public interface IotBoardTemplateMapper
{
    /**
     * 查询情报板模板
     *
     * @param id 情报板模板ID
     * @return 情报板模板
     */
    public IotBoardTemplate selectSdVmsTemplateById(Long id);

    /**
     * 查询情报板模板列表
     *
     * @param iotBoardTemplate 情报板模板
     * @return 情报板模板集合
     */
    public List<IotBoardTemplate> selectSdVmsTemplateList(IotBoardTemplate iotBoardTemplate);

    /**
     * 新增情报板模板
     *
     * @param iotBoardTemplate 情报板模板
     * @return 结果
     */
    public int insertSdVmsTemplate(IotBoardTemplate iotBoardTemplate);

    /**
     * 修改情报板模板
     *
     * @param iotBoardTemplate 情报板模板
     * @return 结果
     */
    public int updateSdVmsTemplate(IotBoardTemplate iotBoardTemplate);

    /**
     * 删除情报板模板
     *
     * @param id 情报板模板ID
     * @return 结果
     */
    public int deleteSdVmsTemplateById(Long id);

    /**
     * 批量删除情报板模板
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdVmsTemplateByIds(Long[] ids);

    public Long selectSdVmsTemplateId();

    public List<IotBoardTemplate> selectTemplateList(@Param("category") String category, @Param("devicePixel") String devicePixel);

    public List<Map<String, Object>> getAllSdVmsTemplateList();

    /**
     * 应急调度下查询情报板模板内容
     * @param id
     * @return
     */
    List<Map<String, Object>> getSdVmsTemplateContent(@Param("id") Long id);
}
