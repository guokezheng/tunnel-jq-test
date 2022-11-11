package com.tunnel.business.service.electromechanicalPatrol.impl;


import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdEquipmentStateIconFile;
import com.tunnel.business.domain.electromechanicalPatrol.SdFaultList;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficImage;
import com.tunnel.business.mapper.electromechanicalPatrol.SdFaultListMapper;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficImageMapper;
import com.tunnel.business.service.electromechanicalPatrol.ISdFaultListService;
import com.tunnel.business.utils.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 故障清单Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-02
 */
@Service
public class SdFaultListServiceImpl implements ISdFaultListService
{
    @Autowired
    private SdFaultListMapper sdFaultListMapper;

    @Autowired
    private SdTrafficImageMapper sdTrafficImageMapper;

    /**
     * 查询故障清单
     * 
     * @param id 故障清单主键
     * @return 故障清单
     */
    @Override
    public SdFaultList selectSdFaultListById(String id)
    {
        return sdFaultListMapper.selectSdFaultListById(id);
    }

    /**
     * 查询故障清单列表
     * 
     * @param sdFaultList 故障清单
     * @return 故障清单
     */
    @Override
    public List<SdFaultList> selectSdFaultListList(SdFaultList sdFaultList)
    {
        return sdFaultListMapper.selectSdFaultListList(sdFaultList);
    }

    /**
     * 新增故障清单
     * 
     * @param sdFaultList 故障清单
     * @return 结果
     */
    @Override
    public int insertSdFaultList(MultipartFile[] file, SdFaultList sdFaultList)
    {
        int result = -1;
        List<SdTrafficImage> list = new ArrayList<SdTrafficImage>();
        try {
            sdFaultList.setCreateTime(DateUtils.getNowDate());// 创建时间
            sdFaultList.setCreateBy(SecurityUtils.getUsername());// 设置当前创建人
            if (file.length > 0) {
                String guid = UUIDUtil.getRandom32BeginTimePK();// 生成guid
                sdFaultList.setImgFileId(guid);// 文件关联ID
                for (int i = 0; i < file.length; i++) {
                    // 图片Base64
                    String imageBaseStr = null;
                    try {
                        String contentType = file[i].getContentType();
                        if (!contentType.contains("image")) {
                            throw new RuntimeException("文件类型不正确!");
                        }
                        byte[] imageBytes = file[i].getBytes();
                        BASE64Encoder base64Encoder = new BASE64Encoder();
                        imageBaseStr = "data:" + contentType + ";base64," + base64Encoder.encode(imageBytes);
                        imageBaseStr = imageBaseStr.replaceAll("[\\s*\t\n\r]", "");
                    } catch (IOException e) {
                        throw new RuntimeException("图片转换base64异常");
                    }
                    // 从缓存中获取文件存储路径
                    String fileServerPath = RuoYiConfig.getUploadPath();
                    // 原图文件名
                    String filename = file[i].getOriginalFilename();
                    // 原图扩展名
                    String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
                    // 新的全名
                    String fileName = extendName;
                    // 加路径全名
                    File dir = new File(fileServerPath + "/faultIcon/" + fileName);
                    File filepath = new File(fileServerPath + "/faultIcon");

                    SdTrafficImage iconFile = new SdTrafficImage();
                    iconFile.setBusinessId(guid);
                    iconFile.setImgUrl(imageBaseStr);
                    iconFile.setImgName(fileName);
                    iconFile.setCreateBy(SecurityUtils.getUsername());
                    iconFile.setCreateTime(DateUtils.getNowDate());
                    list.add(iconFile);

                    if (!filepath.exists()) {
                        filepath.mkdirs();
                    } else {
                    }
                    file[i].transferTo(dir);
                }
                result = sdTrafficImageMapper.brachInsertFaultIconFile(list);
                if (result > -1) {
                    result = sdFaultListMapper.insertSdFaultList(sdFaultList);
                }

            } else {
                sdFaultList.setImgFileId(null);// 图标文件ID
                result = sdFaultListMapper.insertSdFaultList(sdFaultList);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }

    /**
     * 修改故障清单
     * 
     * @param sdFaultList 故障清单
     * @return 结果
     */
    @Override
    public int updateSdFaultList(SdFaultList sdFaultList)
    {
        return sdFaultListMapper.updateSdFaultList(sdFaultList);
    }

    /**
     * 批量删除故障清单
     * 
     * @param ids 需要删除的故障清单主键
     * @return 结果
     */
    @Override
    public int deleteSdFaultListByIds(String[] ids)
    {
        return sdFaultListMapper.deleteSdFaultListByIds(ids);
    }

    /**
     * 删除故障清单信息
     * 
     * @param id 故障清单主键
     * @return 结果
     */
    @Override
    public int deleteSdFaultListById(String id)
    {
        return sdFaultListMapper.deleteSdFaultListById(id);
    }
}
