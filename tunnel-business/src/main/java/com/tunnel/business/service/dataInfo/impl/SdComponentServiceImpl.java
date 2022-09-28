package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdComponent;
import com.tunnel.business.domain.dataInfo.SdEquipmentFile;
import com.tunnel.business.mapper.dataInfo.SdComponentMapper;
import com.tunnel.business.mapper.dataInfo.SdEquipmentFileMapper;
import com.tunnel.business.service.dataInfo.ISdComponentService;
import com.tunnel.business.utils.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备档案管理Service业务层处理
 *
 * @author yanghousheng
 * @date 2020-11-18
 */
@Service
public class SdComponentServiceImpl implements ISdComponentService {
    @Autowired
    private SdComponentMapper sdComponentMapper;
    @Autowired
    private SdEquipmentFileMapper sdEquipmentFileMapper;

    /**
     * 查询设备档案管理
     *
     * @param id 设备档案管理ID
     * @return 设备档案管理
     */
    @Override
    public SdComponent selectSdComponentById(Long id) {
        SdComponent plan = sdComponentMapper.selectSdComponentById(id);
        if (plan.getFileId() != null) {
            SdEquipmentFile sdEquipmentFile = new SdEquipmentFile();
            sdEquipmentFile.setFileId(plan.getFileId());
            plan.setpFileList(sdEquipmentFileMapper.selectSdEquipmentFileList(sdEquipmentFile));
        }
        return plan;
    }

    /**
     * 查询设备档案管理列表
     *
     * @param sdComponent 设备档案管理
     * @return 设备档案管理
     */
    @Override
    public List<SdComponent> selectSdComponentList(SdComponent sdComponent) {
        Long deptId = SecurityUtils.getDeptId();
        sdComponent.getParams().put("deptId", deptId);
        return sdComponentMapper.selectSdComponentList(sdComponent);
    }

    /**
     * 新增设备档案管理
     *
     * @param sdComponent 设备档案管理
     * @return 结果
     */
    @Override
    public int insertSdComponent(MultipartFile[] file, SdComponent sdComponent) {
        //创建一个list集合来存储添加得所有设备档案文件
        List<SdEquipmentFile> list = new ArrayList<SdEquipmentFile>();
        try {
            sdComponent.setCreateTime(DateUtils.getNowDate());
            sdComponent.setCreateBy(SecurityUtils.getUsername());//设置当前创建人
            String guid = UUIDUtil.getRandom32BeginTimePK();//生成guid
            sdComponent.setFileId(guid);//文件关联ID
            for (int i = 0; i < file.length; i++) {
                //从缓存中获取文件的存储路径
                String fileServerPath = RuoYiConfig.getUploadPath();
                //原文件名
                String filename = file[i].getOriginalFilename();
                //原文件扩展名
                String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
                // 新的全名
                String newFileName = extendName;
                // 加路径全名
                File dir = new File(fileServerPath + "/" + newFileName);
                File filepath = new File(fileServerPath);
                SdEquipmentFile sdEquipmentFile = new SdEquipmentFile();
                sdEquipmentFile.setFileId(guid);
                sdEquipmentFile.setFileName(newFileName);//文件名称
                sdEquipmentFile.setUrl(fileServerPath + "/" + newFileName);//文件路径
                sdEquipmentFile.setCreateBy(SecurityUtils.getUsername());
                sdEquipmentFile.setCreateTime(DateUtils.getNowDate());
                list.add(sdEquipmentFile);
                if (!filepath.exists()) {
                    filepath.mkdirs();
                } else {
                }
                file[i].transferTo(dir);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        int result = sdComponentMapper.insertSdComponent(sdComponent);
        if (result > -1) {
            result = sdEquipmentFileMapper.brachInsertSdEquipmentFile(list);
        }
        return result;
    }

    /**
     * 修改设备档案管理
     *
     * @param sdComponent 设备档案管理
     * @return 结果
     */
    @Override
    public int updateSdComponent(MultipartFile[] file, SdComponent sdComponent, Long[] ids) {
        int result = 0;
        //创建一个list集合来存储添加得所有设备档案文件
        List<SdEquipmentFile> list = new ArrayList<SdEquipmentFile>();
        try {
            sdComponent.setUpdateTime(DateUtils.getNowDate());//创建时间
            sdComponent.setUpdateBy(SecurityUtils.getUsername());//设置当前创建人
            String guid = sdComponent.getFileId();//关联ID--guid
            sdComponent.setFileId(guid);//文件关联ID
            if (file != null && file.length > 0) {
                for (int i = 0; i < file.length; i++) {
                    //从缓存中获取文件的存储路径
                    String fileServerPath = RuoYiConfig.getUploadPath();//Global.getUploadPath()
                    //原文件名
                    String filename = file[i].getOriginalFilename();
                    //原文件扩展名
                    String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
                    // 新的全名
                    String newFileName = extendName;
                    // 加路径全名
                    File dir = new File(fileServerPath + "/" + newFileName);
                    File filepath = new File(fileServerPath);
                    SdEquipmentFile sdEquipmentFile = new SdEquipmentFile();
                    sdEquipmentFile.setFileId(guid);
                    sdEquipmentFile.setFileName(newFileName);//文件名称
                    sdEquipmentFile.setUrl(fileServerPath + "/" + newFileName);//文件路径
                    sdEquipmentFile.setFileName(newFileName);
                    sdEquipmentFile.setCreateBy(SecurityUtils.getUsername());
                    sdEquipmentFile.setCreateTime(DateUtils.getNowDate());
                    list.add(sdEquipmentFile);

                    if (!filepath.exists()) {
                        filepath.mkdirs();
                    } else {
                    }
                    file[i].transferTo(dir);
                }
                result = sdEquipmentFileMapper.brachInsertSdEquipmentFile(list);
            }
            if (ids.length > 0) {
                result = sdEquipmentFileMapper.deleteSdEquipmentFileByIds(ids);
            }
        /*if(result>=0){
        	result = sdEquipmentFileMapper.brachInsertSdEquipmentFile(list);
        }*/
            if (result >= 0) {
                result = sdComponentMapper.updateSdComponent(sdComponent);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return result;

    }

    /**
     * 批量删除设备档案管理
     *
     * @param rlIds 需要删除的设备档案管理ID
     * @return 结果
     */
    @Override
    public int deleteSdComponentByIds(String[] rlIds) {
        int result = sdComponentMapper.deleteSdComponentByrlIds(rlIds);
        if (result >= 0) {
            result = sdEquipmentFileMapper.deleteSdEquipmentFileByrlIds(rlIds);
        }
        return result;
    }

    /**
     * 删除设备档案管理信息
     *
     * @param id 设备档案管理ID
     * @return 结果
     */
    @Override
    public int deleteSdComponentById(Long[] id) {
        return sdComponentMapper.deleteSdComponentByIds(id);
    }
}
