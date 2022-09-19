package com.tunnel.business.service.trafficBroadcasting;


import com.tunnel.business.domain.trafficBroadcasting.SdBroadcastTemplate;

import java.util.List;

/**
 * 广播模板Service接口
 *
 * @author ruoyi
 * @date 2021-12-03
 */
public interface ISdBroadcastTemplateService {
    /**
     * 查询广播模板
     *
     * @param id 广播模板ID
     * @return 广播模板
     */
    SdBroadcastTemplate selectSdBroadcastTemplateById(Long id);

    /**
     * 查询广播模板列表
     *
     * @param sdBroadcastTemplate 广播模板
     * @return 广播模板集合
     */
    List<SdBroadcastTemplate> selectSdBroadcastTemplateList(SdBroadcastTemplate sdBroadcastTemplate);

    /**
     * 新增广播模板
     *
     * @param sdBroadcastTemplate 广播模板
     * @return 结果
     */
    int insertSdBroadcastTemplate(SdBroadcastTemplate sdBroadcastTemplate);

    /**
     * 修改广播模板
     *
     * @param sdBroadcastTemplate 广播模板
     * @return 结果
     */
    int updateSdBroadcastTemplate(SdBroadcastTemplate sdBroadcastTemplate);

    /**
     * 批量删除广播模板
     *
     * @param ids 需要删除的广播模板ID
     * @return 结果
     */
    int deleteSdBroadcastTemplateByIds(Long[] ids);

    /**
     * 删除广播模板信息
     *
     * @param id 广播模板ID
     * @return 结果
     */
    int deleteSdBroadcastTemplateById(Long id);
}
