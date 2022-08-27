package com.tunnel.platform.service.emeDiagram;

import com.tunnel.platform.domain.emeDiagram.SdOperationalDiagram;

import java.util.List;

/**
 * 作战示意图Service接口
 * 
 * @author ruoyi
 * @date 2021-11-22
 */
public interface ISdOperationalDiagramService 
{
    /**
     * 查询作战示意图
     * 
     * @param id 作战示意图ID
     * @return 作战示意图
     */
    public SdOperationalDiagram selectSdOperationalDiagramById(Long id);

    /**
     * 查询作战示意图列表
     * 
     * @param sdOperationalDiagram 作战示意图
     * @return 作战示意图集合
     */
    public List<SdOperationalDiagram> selectSdOperationalDiagramList(SdOperationalDiagram sdOperationalDiagram);

    /**
     * 新增作战示意图
     * 
     * @param sdOperationalDiagram 作战示意图
     * @return 结果
     */
    public int insertSdOperationalDiagram(SdOperationalDiagram sdOperationalDiagram);

    /**
     * 修改作战示意图
     * 
     * @param sdOperationalDiagram 作战示意图
     * @return 结果
     */
    public int updateSdOperationalDiagram(SdOperationalDiagram sdOperationalDiagram);

    /**
     * 批量删除作战示意图
     * 
     * @param ids 需要删除的作战示意图ID
     * @return 结果
     */
    public int deleteSdOperationalDiagramByIds(Long[] ids);

    /**
     * 删除作战示意图信息
     * 
     * @param id 作战示意图ID
     * @return 结果
     */
    public int deleteSdOperationalDiagramById(Long id);
}
