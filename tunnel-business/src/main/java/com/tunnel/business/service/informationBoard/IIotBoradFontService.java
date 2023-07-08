package com.tunnel.business.service.informationBoard;


import com.ruoyi.common.core.domain.entity.SysDictData;
import com.tunnel.business.domain.informationBoard.IotBoradFont;

import java.util.List;
import java.util.Map;

/**
 * 情报板字体适应模板Service接口
 *
 * @author wangyaozong
 * @date 2020-06-08
 */
public interface IIotBoradFontService {
    /**
     * 查询情报板字体适应模板
     *
     * @param id 情报板字体适应模板ID
     * @return 情报板字体适应模板
     */
    IotBoradFont selectIotBoradFontById(Long id);

    /**
     * 查询情报板字体适应模板列表
     *
     * @param iotBoradFont 情报板字体适应模板
     * @return 情报板字体适应模板集合
     */
    List<IotBoradFont> selectIotBoradFontList(IotBoradFont iotBoradFont);

    /**
     * 新增情报板字体适应模板
     *
     * @param iotBoradFont 情报板字体适应模板
     * @return 结果
     */
    int insertIotBoradFont(IotBoradFont iotBoradFont);

    /**
     * 修改情报板字体适应模板
     *
     * @param iotBoradFont 情报板字体适应模板
     * @return 结果
     */
    int updateIotBoradFont(IotBoradFont iotBoradFont);

    /**
     * 批量删除情报板字体适应模板
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteIotBoradFontByIds(String ids);

    /**
     * 删除情报板字体适应模板信息
     *
     * @param id 情报板字体适应模板ID
     * @return 结果
     */
    int deleteIotBoradFontById(Long id);

    Map<String, Object> getFontSizeByDevicePixel(String devicePixel);
}
