package com.tunnel.business.mapper.trafficBroadcasting;


import com.tunnel.business.domain.trafficBroadcasting.SdBroadcastTemplate;

import java.util.List;

/**
 * 广播模板Mapper接口
 * 
 * @author ruoyi
 * @date 2021-12-03
 */
public interface SdBroadcastTemplateMapper 
{
    /**
     * 查询广播模板
     * 
     * @param id 广播模板ID
     * @return 广播模板
     */
    public SdBroadcastTemplate selectSdBroadcastTemplateById(Long id);

    /**
     * 查询广播模板列表
     * 
     * @param sdBroadcastTemplate 广播模板
     * @return 广播模板集合
     */
    public List<SdBroadcastTemplate> selectSdBroadcastTemplateList(SdBroadcastTemplate sdBroadcastTemplate);

    /**
     * 新增广播模板
     * 
     * @param sdBroadcastTemplate 广播模板
     * @return 结果
     */
    public int insertSdBroadcastTemplate(SdBroadcastTemplate sdBroadcastTemplate);

    /**
     * 修改广播模板
     * 
     * @param sdBroadcastTemplate 广播模板
     * @return 结果
     */
    public int updateSdBroadcastTemplate(SdBroadcastTemplate sdBroadcastTemplate);

    /**
     * 删除广播模板
     * 
     * @param id 广播模板ID
     * @return 结果
     */
    public int deleteSdBroadcastTemplateById(Long id);

    /**
     * 批量删除广播模板
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdBroadcastTemplateByIds(Long[] ids);
}
