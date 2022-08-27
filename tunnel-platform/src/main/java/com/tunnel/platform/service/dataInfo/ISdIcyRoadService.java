package com.tunnel.platform.service.dataInfo;

import com.tunnel.platform.domain.dataInfo.SdIcyRoad;

import java.util.List;

/**
 * 道路结冰记录Service接口
 *
 * @author liubaokui
 * @date 2021-03-26
 */
public interface ISdIcyRoadService
{
    /**
     * 查询道路结冰记录
     *
     * @param id 道路结冰记录ID
     * @return 道路结冰记录
     */
    public SdIcyRoad selectSdIcyRoadById(Long id);

    /**
     * 查询道路结冰记录列表
     *
     * @param sdIcyRoad 道路结冰记录
     * @return 道路结冰记录集合
     */
    public List<SdIcyRoad> selectSdIcyRoadList(SdIcyRoad sdIcyRoad);

    /**
     * 新增道路结冰记录
     *
     * @param sdIcyRoad 道路结冰记录
     * @return 结果
     */
    public int insertSdIcyRoad(SdIcyRoad sdIcyRoad);

    /**
     * 修改道路结冰记录
     *
     * @param sdIcyRoad 道路结冰记录
     * @return 结果
     */
    public int updateSdIcyRoad(SdIcyRoad sdIcyRoad);

    /**
     * 批量删除道路结冰记录
     *
     * @param ids 需要删除的道路结冰记录ID
     * @return 结果
     */
    public int deleteSdIcyRoadByIds(Long[] ids);

    /**
     * 删除道路结冰记录信息
     *
     * @param id 道路结冰记录ID
     * @return 结果
     */
    public int deleteSdIcyRoadById(Long id);

    /**
     * 道路结冰协议解析
     * @param content
     * @param deviceId
     * @param sdIcyRoadMap
     * @return
     */
	public void ParsingProtocol(String content, String deviceId);

	/**
     * 道路结冰协议失败
     * @param content
     * @param deviceId
     * @param sdIcyRoadMap
     * @return
     */
	public void ErrorParsingProtocol(SdIcyRoad sdIcyRoad, String deviceId);


    /**
     * 查询某隧道最新的数据记录
     */
    List<SdIcyRoad> selectLatestIcyRoadList(String tunnelId);
}
