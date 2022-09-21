package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdMaintenanceManagement;
import com.tunnel.business.domain.dataInfo.SdMaintenanceManagementFile;
import com.tunnel.business.mapper.dataInfo.SdMaintenanceManagementFileMapper;
import com.tunnel.business.mapper.dataInfo.SdMaintenanceManagementMapper;
import com.tunnel.business.service.dataInfo.ISdMaintenanceManagementService;
import com.tunnel.business.utils.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * 养护管理Service业务层处理
 *
 * @author ruoyi
 * @date 2022-02-14
 */
@Service
public class SdMaintenanceManagementServiceImpl implements ISdMaintenanceManagementService {

    private static final Logger logger = LoggerFactory.getLogger(SdMaintenanceManagementServiceImpl.class);

    @Autowired
    private SdMaintenanceManagementMapper sdMaintenanceManagementMapper;

    @Autowired
    private SdMaintenanceManagementFileMapper sdMaintenanceManagementFileMapper;

    public String ioToBase64(String url) throws IOException {
        String strBase64 = null;
        if (url == null || "".equals(url)) {
            //log.info("url转base64参数为空" + url);
            strBase64 = "data:image/jpg;base64,";
        } else {
            String fileName = url; // 源文件
            try {
                InputStream in = new FileInputStream(fileName);
                // in.available()返回文件的字节长度
                byte[] bytes = new byte[in.available()];
                // 将文件中的内容读入到数组中
                in.read(bytes);
                strBase64 = "data:image/jpg;base64," + Base64.getEncoder().encodeToString(bytes); // 将字节流数组转换为字符串
                in.close();
            } catch (FileNotFoundException fe) {
                fe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return strBase64;
    }

    /**
     * 查询养护管理
     *
     * @param id 养护管理主键
     * @return 养护管理
     */
    @Override
    public SdMaintenanceManagement selectSdMaintenanceManagementById(Long id) throws IOException {
        SdMaintenanceManagement sdMaintenanceManagement = sdMaintenanceManagementMapper.selectSdMaintenanceManagementById(id);
        if (sdMaintenanceManagement.getpFileId() != null && !"".equals(sdMaintenanceManagement.getpFileId())) {
            SdMaintenanceManagementFile managementFile = new SdMaintenanceManagementFile();
            managementFile.setPlanfileid(sdMaintenanceManagement.getpFileId());
            List<SdMaintenanceManagementFile> fileList = sdMaintenanceManagementFileMapper.selectSdMaintenanceManagementFileList(managementFile);
            if (fileList.size() > 0) {
                for (int i = 0; i < fileList.size(); i++) {
                    SdMaintenanceManagementFile sdMaintenanceManagementFile = fileList.get(i);
                    String base64 = ioToBase64(sdMaintenanceManagementFile.getUrl());
                    sdMaintenanceManagementFile.setUrl(base64);
                }
            }
            sdMaintenanceManagement.setFileLists(fileList);
        }
        return sdMaintenanceManagement;
    }

    /**
     * 查询养护管理列表
     *
     * @param sdMaintenanceManagement 养护管理
     * @return 养护管理
     */
    @Override
    public List<SdMaintenanceManagement> selectSdMaintenanceManagementList(SdMaintenanceManagement sdMaintenanceManagement) {
        sdMaintenanceManagement.setCuringProgress(null);
        Long deptId = SecurityUtils.getDeptId();
        sdMaintenanceManagement.getParams().put("deptId", deptId);
        return sdMaintenanceManagementMapper.selectSdMaintenanceManagementList(sdMaintenanceManagement);
    }

    /**
     * 新增养护管理
     *
     * @param sdMaintenanceManagement 养护管理
     * @return 结果
     */
    @Override
    public int insertSdMaintenanceManagement(MultipartFile[] file, SdMaintenanceManagement sdMaintenanceManagement) {
        sdMaintenanceManagement.setCreateTime(DateUtils.getNowDate());
//        return sdMaintenanceManagementMapper.insertSdMaintenanceManagement(sdMaintenanceManagement);
        int result = -1;
        try {
            sdMaintenanceManagement.setCreateTime(DateUtils.getNowDate());//创建时间
            sdMaintenanceManagement.setCreateBy(SecurityUtils.getUsername());//设置当前创建人
            if (file != null && file.length > 0) {
                List<SdMaintenanceManagementFile> list = new ArrayList<SdMaintenanceManagementFile>();
                String guid = UUIDUtil.getRandom32BeginTimePK();//生成guid
                sdMaintenanceManagement.setpFileId(guid);//文件关联ID
                for (int i = 0; i < file.length; i++) {
                    // 从缓存中获取文件存储路径
                    String fileServerPath = RuoYiConfig.getUploadPath();
                    // 原图文件名
                    String filename = file[i].getOriginalFilename();
                    // 原图扩展名
                    String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
                    // 新的全名
                    String fileName = extendName;
                    // 加路径全名
                    File dir = new File(fileServerPath + "/" + fileName);
                    File filepath = new File(fileServerPath);
                    SdMaintenanceManagementFile planFile = new SdMaintenanceManagementFile();
                    planFile.setPlanfileid(guid);
                    planFile.setUrl(fileServerPath + "/" + fileName);
                    planFile.setFilename(fileName);
                    planFile.setCreateBy(SecurityUtils.getUsername());
                    planFile.setCreateTime(DateUtils.getNowDate());
                    list.add(planFile);
                    for (int j = 0; j < list.size(); j++) {
                        SdMaintenanceManagementFile sdMaintenanceManagementFile = list.get(j);
                        System.err.println(sdMaintenanceManagementFile);
                    }
                    if (!filepath.exists()) {
                        filepath.mkdirs();
                    } else {
                    }
                    file[i].transferTo(dir);
                }
                result = sdMaintenanceManagementMapper.insertSdMaintenanceManagement(sdMaintenanceManagement);
                if (result > -1) {
                    result = sdMaintenanceManagementFileMapper.brachInsertSdMaintenanceManagementFile(list);
                }
            } else {
                sdMaintenanceManagement.setpFileId(null);//文件关联ID
                result = sdMaintenanceManagementMapper.insertSdMaintenanceManagement(sdMaintenanceManagement);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }

    /**
     * 修改养护管理
     *
     * @param sdMaintenanceManagement 养护管理
     * @return 结果
     */
    @Override
    public int updateSdMaintenanceManagement(MultipartFile[] file, SdMaintenanceManagement sdMaintenanceManagement, Long[] ids) {
        sdMaintenanceManagement.setUpdateTime(DateUtils.getNowDate());
//        return sdMaintenanceManagementMapper.updateSdMaintenanceManagement(sdMaintenanceManagement);
        int result = 0;
        List<SdMaintenanceManagementFile> list = new ArrayList<SdMaintenanceManagementFile>();
        try {
            sdMaintenanceManagement.setUpdateTime(DateUtils.getNowDate());//创建时间
            sdMaintenanceManagement.setUpdateBy(SecurityUtils.getUsername());//设置当前创建人
            String guid = sdMaintenanceManagement.getpFileId();//关联ID--guid
            sdMaintenanceManagement.setpFileId(guid);//文件关联ID
            if (file != null && file.length > 0) {
                for (int i = 0; i < file.length; i++) {
                    // 从缓存中获取文件存储路径
                    String fileServerPath = RuoYiConfig.getUploadPath();//Global.getUploadPath()
                    // 原图文件名
                    String filename = file[i].getOriginalFilename();
                    // 原图扩展名
                    String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
                    // 新的全名
                    String fileName = extendName;
                    // 加路径全名
                    File dir = new File(fileServerPath + "/" + fileName);
                    File filepath = new File(fileServerPath);
                    SdMaintenanceManagementFile planFile = new SdMaintenanceManagementFile();
                    planFile.setPlanfileid(guid);
                    planFile.setUrl(fileServerPath + "/" + fileName);
                    planFile.setFilename(fileName);
                    planFile.setCreateBy(SecurityUtils.getUsername());
                    planFile.setCreateTime(DateUtils.getNowDate());
                    list.add(planFile);

                    if (!filepath.exists()) {
                        filepath.mkdirs();
                    } else {
                    }
                    file[i].transferTo(dir);
                }
                result = sdMaintenanceManagementFileMapper.brachInsertSdMaintenanceManagementFile(list);
            } else {
                logger.info("当前文件信息为空或文件没有发生改动");
            }
            if (ids.length > 0) {
                result = sdMaintenanceManagementFileMapper.deleteSdMaintenanceManagementFileByIds(ids);//ids 为要删除的file id数组
            }
            if (result >= 0) {
                result = sdMaintenanceManagementMapper.updateSdMaintenanceManagement(sdMaintenanceManagement);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }

    /**
     * 批量删除养护管理
     *
     * @param ids 需要删除的养护管理主键
     * @return 结果
     */
    @Override
    public int deleteSdMaintenanceManagementByIds(Long[] ids) {
        for (Long l : ids) {
            SdMaintenanceManagement management = sdMaintenanceManagementMapper.selectSdMaintenanceManagementById(l);
            if (management.getpFileId() != null && !"".equals(management.getpFileId())) {
                String fileId = management.getpFileId();
                sdMaintenanceManagementFileMapper.deleteFileByFileIds(fileId);
            }
        }
        return sdMaintenanceManagementMapper.deleteSdMaintenanceManagementByIds(ids);
    }

    /**
     * 删除养护管理信息
     *
     * @param id 养护管理主键
     * @return 结果
     */
    @Override
    public int deleteSdMaintenanceManagementById(Long id) {
        return sdMaintenanceManagementMapper.deleteSdMaintenanceManagementById(id);
    }

    @Override
    public int add(SdMaintenanceManagement sdMaintenanceManagement) {
        sdMaintenanceManagement.setCreateTime(DateUtils.getNowDate());//创建时间
        sdMaintenanceManagement.setCreateBy(SecurityUtils.getUsername());//设置当前创建人
        if (null != sdMaintenanceManagement.getpFileId() && !"".equals(sdMaintenanceManagement.getpFileId())
                && sdMaintenanceManagement.getpFileId().contains(",")) {
            String fileIds = sdMaintenanceManagement.getpFileId();
            if (fileIds.endsWith(",")) {
                fileIds = fileIds.substring(0, fileIds.length() - 1);
            }
            String[] pFileIds = fileIds.split(",");
            String fileId = pFileIds[0];
            for (String s : pFileIds) {
                if (!s.equals(fileId)) {
                    sdMaintenanceManagementFileMapper.updateFileId(fileId, s);
                }
            }
            sdMaintenanceManagement.setpFileId(fileId);
        }
        return sdMaintenanceManagementMapper.insertSdMaintenanceManagement(sdMaintenanceManagement);
    }

    @Override
    public int edit(SdMaintenanceManagement sdMaintenanceManagement) {
        sdMaintenanceManagement.setUpdateTime(DateUtils.getNowDate());
        sdMaintenanceManagement.setUpdateBy(SecurityUtils.getUsername());
        //如果关联ID不同，就重新定义关联ID
        SdMaintenanceManagement old = sdMaintenanceManagementMapper.selectSdMaintenanceManagementById(sdMaintenanceManagement.getId());
        String oldFileId = old.getpFileId();
        String newFileId = sdMaintenanceManagement.getpFileId();
        if (!oldFileId.equals(newFileId) && newFileId != null && !"".equals(newFileId)) {
            if (newFileId.endsWith(",")) {
                newFileId = newFileId.substring(0, newFileId.length() - 1);
            }
            SdMaintenanceManagementFile sdMaintenanceManagementFile = new SdMaintenanceManagementFile();
            sdMaintenanceManagementFile.setPlanfileid(oldFileId);
            List<SdMaintenanceManagementFile> sdMaintenanceManagementFiles = sdMaintenanceManagementFileMapper.selectSdMaintenanceManagementFileList(sdMaintenanceManagementFile);
            String[] pFileIds = newFileId.split(",");
            String fileId = pFileIds[0];
            for (String s : pFileIds) {
                if (!s.equals(fileId)) {
                    sdMaintenanceManagementFileMapper.updateFileId(fileId, s);
                }
            }
            if (sdMaintenanceManagementFiles.size() > 0) {
                for (int i = 0; i < sdMaintenanceManagementFiles.size(); i++) {
                    SdMaintenanceManagementFile maintenanceManagementFile = sdMaintenanceManagementFiles.get(i);
                    sdMaintenanceManagementFileMapper.updateFileId(fileId, maintenanceManagementFile.getPlanfileid());
                }
            }
            sdMaintenanceManagement.setpFileId(fileId);
        }
        return sdMaintenanceManagementMapper.updateSdMaintenanceManagement(sdMaintenanceManagement);
    }

    @Override
    public String uploadPicture(MultipartFile[] file) throws IOException {
        if (file != null && file.length > 0) {
            List<SdMaintenanceManagementFile> list = new ArrayList<SdMaintenanceManagementFile>();
            String guid = UUIDUtil.getRandom32BeginTimePK();//生成文件关联ID
            for (int i = 0; i < file.length; i++) {
                // 从缓存中获取文件存储路径
                String fileServerPath = RuoYiConfig.getUploadPath();
                // 原图文件名
                String filename = file[i].getOriginalFilename();
                // 原图扩展名
                String extendName = filename.substring(filename.lastIndexOf("\\") + 1);
                // 新的全名
                String fileName = extendName;
                // 加路径全名
                File dir = new File(fileServerPath + "/" + fileName);
                File filepath = new File(fileServerPath);
                SdMaintenanceManagementFile planFile = new SdMaintenanceManagementFile();
                planFile.setPlanfileid(guid);
                planFile.setUrl(fileServerPath + "/" + fileName);
                planFile.setFilename(fileName);
                planFile.setCreateBy(SecurityUtils.getUsername());
                planFile.setCreateTime(DateUtils.getNowDate());
                list.add(planFile);
                for (int j = 0; j < list.size(); j++) {
                    SdMaintenanceManagementFile sdMaintenanceManagementFile = list.get(j);
                    System.err.println(sdMaintenanceManagementFile);
                }
                if (!filepath.exists()) {
                    filepath.mkdirs();
                } else {
                }
                file[i].transferTo(dir);
                if (list.size() > -1) {
                    sdMaintenanceManagementFileMapper.brachInsertSdMaintenanceManagementFile(list);
                    return guid;
                } else {
                    throw new RuntimeException("图片上传失败！请重试！");
                }
            }
        }
        return "图片文件信息为空";
    }
}
