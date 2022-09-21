package com.tunnel.business.service.trafficOperationControl.eventManage;


import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficIncidentInfo;

import java.util.List;
import java.util.Map;

/**
 * 交通运行管控-事件信息管理Service接口
 *
 * @author ruoyi
 * @date 2022-02-14
 */
public interface ISdTrafficIncidentInfoService {
    /**
     * 查询交通运行管控-事件信息管理
     *
     * @param id 交通运行管控-事件信息管理主键
     * @return 交通运行管控-事件信息管理
     */
    SdTrafficIncidentInfo selectSdTrafficIncidentInfoById(Long id);

    /**
     * 查询交通运行管控-事件信息管理列表
     *
     * @param sdTrafficIncidentInfo 交通运行管控-事件信息管理
     * @return 交通运行管控-事件信息管理集合
     */
    List<SdTrafficIncidentInfo> selectSdTrafficIncidentInfoList(SdTrafficIncidentInfo sdTrafficIncidentInfo);

    /**
     * 新增交通运行管控-事件信息管理
     *
     * @param sdTrafficIncidentInfo 交通运行管控-事件信息管理
     * @return 结果
     */
    int insertSdTrafficIncidentInfo(SdTrafficIncidentInfo sdTrafficIncidentInfo);

    /**
     * 修改交通运行管控-事件信息管理
     *
     * @param sdTrafficIncidentInfo 交通运行管控-事件信息管理
     * @return 结果
     */
    int updateSdTrafficIncidentInfo(SdTrafficIncidentInfo sdTrafficIncidentInfo);

    /**
     * 批量删除交通运行管控-事件信息管理
     *
     * @param ids 需要删除的交通运行管控-事件信息管理主键集合
     * @return 结果
     */
    int deleteSdTrafficIncidentInfoByIds(Long[] ids);

    /**
     * 删除交通运行管控-事件信息管理信息
     *
     * @param id 交通运行管控-事件信息管理主键
     * @return 结果
     */
    int deleteSdTrafficIncidentInfoById(Long id);

    /**
     * 获取已发布的交通事件信息
     *
     * @return
     */
    List<Map> getPublishIncidentInfo();
}
