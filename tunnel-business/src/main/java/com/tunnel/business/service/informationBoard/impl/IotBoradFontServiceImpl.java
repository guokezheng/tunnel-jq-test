package com.tunnel.business.service.informationBoard.impl;

import com.ruoyi.common.core.text.Convert;
import com.tunnel.business.domain.informationBoard.IotBoradFont;
import com.tunnel.business.mapper.informationBoard.IotBoradFontMapper;
import com.tunnel.business.service.informationBoard.IIotBoradFontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 情报板字体适应模板Service业务层处理
 *
 * @author wangyaozong
 * @date 2020-06-08
 */
@Service
public class IotBoradFontServiceImpl implements IIotBoradFontService {
    @Autowired
    private IotBoradFontMapper iotBoradFontMapper;

    /**
     * 查询情报板字体适应模板
     *
     * @param id 情报板字体适应模板ID
     * @return 情报板字体适应模板
     */
    @Override
    public IotBoradFont selectIotBoradFontById(Long id) {
        return iotBoradFontMapper.selectIotBoradFontById(id);
    }

    /**
     * 查询情报板字体适应模板列表
     *
     * @param iotBoradFont 情报板字体适应模板
     * @return 情报板字体适应模板
     */
    @Override
    public List<IotBoradFont> selectIotBoradFontList(IotBoradFont iotBoradFont) {
        return iotBoradFontMapper.selectIotBoradFontList(iotBoradFont);
    }

    /**
     * 新增情报板字体适应模板
     *
     * @param iotBoradFont 情报板字体适应模板
     * @return 结果
     */
    @Override
    public int insertIotBoradFont(IotBoradFont iotBoradFont) {
        return iotBoradFontMapper.insertIotBoradFont(iotBoradFont);
    }

    /**
     * 修改情报板字体适应模板
     *
     * @param iotBoradFont 情报板字体适应模板
     * @return 结果
     */
    @Override
    public int updateIotBoradFont(IotBoradFont iotBoradFont) {
        return iotBoradFontMapper.updateIotBoradFont(iotBoradFont);
    }

    /**
     * 删除情报板字体适应模板对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteIotBoradFontByIds(String ids) {
        return iotBoradFontMapper.deleteIotBoradFontByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除情报板字体适应模板信息
     *
     * @param id 情报板字体适应模板ID
     * @return 结果
     */
    @Override
    public int deleteIotBoradFontById(Long id) {
        return iotBoradFontMapper.deleteIotBoradFontById(id);
    }
}
