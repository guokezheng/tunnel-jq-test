package com.tunnel.platform.mapper.informationBoard;

import com.tunnel.platform.domain.informationBoard.IotBoradFont;

import java.util.List;

/**
 * 情报板字体适应模板Mapper接口
 * 
 * @author wangyaozong
 * @date 2020-06-08
 */
public interface IotBoradFontMapper 
{
    /**
     * 查询情报板字体适应模板
     * 
     * @param id 情报板字体适应模板ID
     * @return 情报板字体适应模板
     */
    public IotBoradFont selectIotBoradFontById(Long id);

    /**
     * 查询情报板字体适应模板列表
     * 
     * @param iotBoradFont 情报板字体适应模板
     * @return 情报板字体适应模板集合
     */
    public List<IotBoradFont> selectIotBoradFontList(IotBoradFont iotBoradFont);

    /**
     * 新增情报板字体适应模板
     * 
     * @param iotBoradFont 情报板字体适应模板
     * @return 结果
     */
    public int insertIotBoradFont(IotBoradFont iotBoradFont);

    /**
     * 修改情报板字体适应模板
     * 
     * @param iotBoradFont 情报板字体适应模板
     * @return 结果
     */
    public int updateIotBoradFont(IotBoradFont iotBoradFont);

    /**
     * 删除情报板字体适应模板
     * 
     * @param id 情报板字体适应模板ID
     * @return 结果
     */
    public int deleteIotBoradFontById(Long id);

    /**
     * 批量删除情报板字体适应模板
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteIotBoradFontByIds(String[] ids);
}
