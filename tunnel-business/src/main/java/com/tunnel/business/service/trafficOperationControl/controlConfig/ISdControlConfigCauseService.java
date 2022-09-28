package com.tunnel.business.service.trafficOperationControl.controlConfig;


import com.tunnel.business.domain.trafficOperationControl.controlConfig.SdControlConfigCause;

import java.util.List;

/**
 * 管控等级配置措施-管控原因Service接口
 *
 * @author ruoyi
 * @date 2022-02-15
 */
public interface ISdControlConfigCauseService {
    /**
     * 查询管控等级配置措施-管控原因
     *
     * @param id 管控等级配置措施-管控原因主键
     * @return 管控等级配置措施-管控原因
     */
    SdControlConfigCause selectSdControlConfigCauseById(Long id);

    /**
     * 查询管控等级配置措施-管控原因列表
     *
     * @param sdControlConfigCause 管控等级配置措施-管控原因
     * @return 管控等级配置措施-管控原因集合
     */
    List<SdControlConfigCause> selectSdControlConfigCauseList(SdControlConfigCause sdControlConfigCause);

    /**
     * 新增管控等级配置措施-管控原因
     *
     * @param sdControlConfigCause 管控等级配置措施-管控原因
     * @return 结果
     */
    int insertSdControlConfigCause(SdControlConfigCause sdControlConfigCause);

    /**
     * 修改管控等级配置措施-管控原因
     *
     * @param sdControlConfigCause 管控等级配置措施-管控原因
     * @return 结果
     */
    int updateSdControlConfigCause(SdControlConfigCause sdControlConfigCause);

    /**
     * 批量删除管控等级配置措施-管控原因
     *
     * @param ids 需要删除的管控等级配置措施-管控原因主键集合
     * @return 结果
     */
    int deleteSdControlConfigCauseByIds(Long[] ids);

    /**
     * 删除管控等级配置措施-管控原因信息
     *
     * @param id 管控等级配置措施-管控原因主键
     * @return 结果
     */
    int deleteSdControlConfigCauseById(Long id);


    /**
     * 根据等级配置id删除管控原因记录
     *
     * @param configLevelId 等级配置id
     * @return
     */
    int delConfigCauseByLevelId(Long configLevelId);


    /**
     * 删除管控等级关联的管控原因
     *
     * @param levelIds 等级id数组
     * @return
     */
    int delConfigCauseByLevelIds(Long[] levelIds);


    /**
     * 根据等级配置id查询管控原因
     *
     * @param configLevelId 等级配置id
     * @return
     */
    List<SdControlConfigCause> getConfigCauseByLevelId(Long configLevelId);

    /**
     * 查询可用的管控等级配置的管控原因
     *
     * @param status 状态
     * @return
     */
    List<SdControlConfigCause> selectValidConfigCauseList(String status);

    /**
     * 获取管控原因
     *
     * @param list
     * @return
     */
    String getControlCauseDescription(List<SdControlConfigCause> list);

}
