package com.tunnel.business.mapper.trafficOperationControl.eventManage;


import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficIncidentInfo;

import java.util.List;
import java.util.Map;

/**
 * 交通运行管控-事件信息管理Mapper接口
 *
 * @author ruoyi
 * @date 2022-02-14
 */
public interface SdTrafficIncidentInfoMapper
{
    /**
     * 查询交通运行管控-事件信息管理
     *
     * @param id 交通运行管控-事件信息管理主键
     * @return 交通运行管控-事件信息管理
     */
    public SdTrafficIncidentInfo selectSdTrafficIncidentInfoById(Long id);

    /**
     * 查询交通运行管控-事件信息管理列表
     *
     * @param sdTrafficIncidentInfo 交通运行管控-事件信息管理
     * @return 交通运行管控-事件信息管理集合
     */
    public List<SdTrafficIncidentInfo> selectSdTrafficIncidentInfoList(SdTrafficIncidentInfo sdTrafficIncidentInfo);

    /**
     * 新增交通运行管控-事件信息管理
     *
     * @param sdTrafficIncidentInfo 交通运行管控-事件信息管理
     * @return 结果
     */
    public int insertSdTrafficIncidentInfo(SdTrafficIncidentInfo sdTrafficIncidentInfo);

    /**
     * 修改交通运行管控-事件信息管理
     *
     * @param sdTrafficIncidentInfo 交通运行管控-事件信息管理
     * @return 结果
     */
    public int updateSdTrafficIncidentInfo(SdTrafficIncidentInfo sdTrafficIncidentInfo);

    /**
     * 删除交通运行管控-事件信息管理
     *
     * @param id 交通运行管控-事件信息管理主键
     * @return 结果
     */
    public int deleteSdTrafficIncidentInfoById(Long id);

    /**
     * 批量删除交通运行管控-事件信息管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdTrafficIncidentInfoByIds(Long[] ids);


    /**
     * 获取已发布的交通事件信息
     * @param status 发布状态
     * @return
     */
    List<Map> getPublishIncidentInfo(String status);

}
