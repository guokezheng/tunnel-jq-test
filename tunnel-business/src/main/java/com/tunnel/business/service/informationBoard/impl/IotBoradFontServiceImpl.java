package com.tunnel.business.service.informationBoard.impl;

import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.tunnel.business.datacenter.domain.enumeration.InformationBoardFontEnum;
import com.tunnel.business.domain.informationBoard.IotBoradFont;
import com.tunnel.business.mapper.informationBoard.IotBoradFontMapper;
import com.tunnel.business.service.informationBoard.IIotBoradFontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private SysDictDataMapper sysDictDataMapper;

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

    @Override
    public Map<String, Object> getFontSizeByDevicePixel(String devicePixel) {
        //返回体
        Map<String, Object> map = new HashMap<>();
        List<SysDictData> fontSize = sysDictDataMapper.selectDictDataByType("iot_device_font_size");
        if (!fontSize.isEmpty() && devicePixel != null && !devicePixel.equals("")) {
            List<SysDictData> fontSizes = new ArrayList<>();
            String[] split = devicePixel.split("\\*");
            Long paramOne = Long.valueOf(split[0]);
            Long paramTwo = Long.valueOf(split[1]);
            Long limitSize = 0L;
            if (paramOne.longValue() > paramTwo.longValue()) {
                limitSize = paramTwo;
            } else if (paramOne.longValue() < paramTwo.longValue()) {
                limitSize = paramOne;
            } else if (paramOne.longValue() == paramTwo.longValue()) {
                limitSize = paramOne;
            }
            for (int i = 0; i < fontSize.size(); i++) {
                SysDictData sysDictData = fontSize.get(i);
                String dictLabel = sysDictData.getDictLabel().toLowerCase().replaceAll("px", "");
                Long size = Long.valueOf(dictLabel);
                if (limitSize.longValue() > size.longValue()) {
                    fontSizes.add(sysDictData);
                }
            }
            //字体大小集合
            map.put("fontSizeList",fontSizes);
            //默认字体大小
            map.put("defaultFont", InformationBoardFontEnum.getValue(devicePixel));
            return map;
        }
        return null;
    }
}
