package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdCrashRecovery;
import com.tunnel.business.domain.event.SdStrategy;
import com.tunnel.business.mapper.dataInfo.SdCrashRecoveryMapper;
import com.tunnel.business.mapper.event.SdStrategyMapper;
import com.tunnel.business.service.dataInfo.ISdCrashRecoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 应急恢复Service业务层处理
 *
 * @author why
 * @date 2022-02-22
 */
@Service
public class SdCrashRecoveryServiceImpl implements ISdCrashRecoveryService {
    @Autowired
    private SdCrashRecoveryMapper sdCrashRecoveryMapper;
    @Autowired
    private SdStrategyMapper sdStrategyMapper;

    /**
     * 查询应急恢复
     *
     * @param id 应急恢复主键
     * @return 应急恢复
     */
    @Override
    public SdCrashRecovery selectSdCrashRecoveryById(Long id) {
        return sdCrashRecoveryMapper.selectSdCrashRecoveryById(id);
    }

    /**
     * 查询应急恢复列表
     *
     * @param sdCrashRecovery 应急恢复
     * @return 应急恢复
     */
    @Override
    public List<SdCrashRecovery> selectSdCrashRecoveryList(SdCrashRecovery sdCrashRecovery) {
        Long deptId = SecurityUtils.getDeptId();
        sdCrashRecovery.getParams().put("deptId", deptId);
        List<SdCrashRecovery> sdCrashRecoveries = sdCrashRecoveryMapper.selectSdCrashRecoveryList(sdCrashRecovery);
        for (SdCrashRecovery sdCrashRecovery1 : sdCrashRecoveries) {
            String afterContent = sdCrashRecovery1.getAfterContent();
            StringBuilder strategys = new StringBuilder();
            if (null != afterContent && !"-1".equals(afterContent)) {
                String[] strategyIdAyy = afterContent.split("；");
                for (String s : strategyIdAyy) {
                    SdStrategy sds = sdStrategyMapper.selectSdStrategyById(Long.parseLong(s));
                    strategys.append(sds.getStrategyName()).append(";");
                }
            }
            sdCrashRecovery1.setStrategyContent(strategys.toString());
        }
        return sdCrashRecoveries;
    }

    /**
     * 新增应急恢复
     *
     * @param sdCrashRecovery 应急恢复
     * @return 结果
     */
    @Override
    public int insertSdCrashRecovery(MultipartFile[] file, SdCrashRecovery sdCrashRecovery) throws IOException {
        if (file != null && file.length > 0) {
            for (MultipartFile multipartFile : file) {
                // 从缓存中获取文件存储路径
                String fileServerPath = RuoYiConfig.getUploadPath();
                // 原图文件名
                String filename = multipartFile.getOriginalFilename();
                // 原图扩展名
                String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
                // 新的全名
                String fileName = extendName;
                // 加路径全名
                File dir = new File(fileServerPath + "/" + fileName);
                System.err.println("file:" + fileServerPath + "/" + fileName);
                File filepath = new File(fileServerPath + "/");
                sdCrashRecovery.setBeforeImage(fileServerPath + "/" + fileName);
                if (!filepath.exists()) {
                    filepath.mkdirs();
                }
                multipartFile.transferTo(dir);
            }
        }
        sdCrashRecovery.setCreateTime(DateUtils.getNowDate());
        sdCrashRecovery.setCreateBy(SecurityUtils.getUsername());
        return sdCrashRecoveryMapper.insertSdCrashRecovery(sdCrashRecovery);
    }

    /**
     * 修改应急恢复
     *
     * @param sdCrashRecovery 应急恢复
     * @return 结果
     */
    @Override
    public int updateSdCrashRecovery(MultipartFile[] file, SdCrashRecovery sdCrashRecovery) throws IOException {
        if (file != null && file.length > 0) {
            for (MultipartFile multipartFile : file) {
                // 从缓存中获取文件存储路径
                String fileServerPath = RuoYiConfig.getUploadPath();
                // 原图文件名
                String filename = multipartFile.getOriginalFilename();
                // 原图扩展名
                String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
                // 新的全名
                String fileName = extendName;
                // 加路径全名
                File dir = new File(fileServerPath + "/" + fileName);
                System.err.println("file:" + fileServerPath + "/" + fileName);
                File filepath = new File(fileServerPath + "/");
                sdCrashRecovery.setAfterImage(fileServerPath + "/" + fileName);
                sdCrashRecovery.setAfterTime(DateUtils.getNowDate());
                sdCrashRecovery.setAfterContent(sdCrashRecovery.getAfterContent());
                if (!filepath.exists()) {
                    filepath.mkdirs();
                }
                multipartFile.transferTo(dir);
            }
        }
        sdCrashRecovery.setUpdateTime(DateUtils.getNowDate());
        sdCrashRecovery.setUpdateBy(SecurityUtils.getUsername());
        return sdCrashRecoveryMapper.updateSdCrashRecovery(sdCrashRecovery);
    }

    /**
     * 批量删除应急恢复
     *
     * @param ids 需要删除的应急恢复主键
     * @return 结果
     */
    @Override
    public int deleteSdCrashRecoveryByIds(Long[] ids) {
        return sdCrashRecoveryMapper.deleteSdCrashRecoveryByIds(ids);
    }

    /**
     * 删除应急恢复信息
     *
     * @param id 应急恢复主键
     * @return 结果
     */
    @Override
    public int deleteSdCrashRecoveryById(Long id) {
        return sdCrashRecoveryMapper.deleteSdCrashRecoveryById(id);
    }

    @Override
    public int controlRecovery(SdCrashRecovery sdCrashRecovery) {
        return sdCrashRecoveryMapper.controlRecovery(sdCrashRecovery);
    }
}
