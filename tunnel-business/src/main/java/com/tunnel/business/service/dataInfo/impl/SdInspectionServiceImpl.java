package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdInspection;
import com.tunnel.business.domain.dataInfo.SdInspectionFile;
import com.tunnel.business.mapper.dataInfo.SdInspectionFileMapper;
import com.tunnel.business.mapper.dataInfo.SdInspectionMapper;
import com.tunnel.business.service.dataInfo.ISdInspectionService;
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
 * 巡视记录Service业务层处理
 *
 * @author liubaokui
 * @date 2021-05-12
 */
@Service
public class SdInspectionServiceImpl implements ISdInspectionService {

    private static final Logger logger = LoggerFactory.getLogger(SdInspectionServiceImpl.class);

    @Autowired
    private SdInspectionMapper sdInspectionMapper;
    @Autowired
    private SdInspectionFileMapper sdInspectionFileMapper;

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
     * 查询巡视记录
     *
     * @param inspectionId 巡视记录ID
     * @return 巡视记录
     */
    @Override
    public SdInspection selectSdInspectionById(Long inspectionId) throws IOException {
        SdInspection sdInspection = sdInspectionMapper.selectSdInspectionById(inspectionId);
        if (null != sdInspection.getPlanFileId() && !"".equals(sdInspection.getPlanFileId())) {
            SdInspectionFile sdInspectionFile = new SdInspectionFile();
            sdInspectionFile.setPlanFileId(sdInspection.getPlanFileId());
            List<SdInspectionFile> fileList = sdInspectionFileMapper.selectSdInspectionFileList(sdInspectionFile);
            if (fileList.size() > 0) {
                for (int i = 0; i < fileList.size(); i++) {
                    SdInspectionFile sdMaintenanceManagementFile = fileList.get(i);
                    String base64 = ioToBase64(sdMaintenanceManagementFile.getUrl());
                    sdMaintenanceManagementFile.setUrl(base64);
                }
            }
            sdInspection.setFileLists(fileList);
        }
        return sdInspection;
    }

    /**
     * 查询巡视记录列表
     *
     * @param sdInspection 巡视记录
     * @return 巡视记录
     */
    @Override
    public List<SdInspection> selectSdInspectionList(SdInspection sdInspection) {
        Long deptId = SecurityUtils.getDeptId();
        sdInspection.getParams().put("deptId", deptId);
        return sdInspectionMapper.selectSdInspectionList(sdInspection);
    }

    /**
     * 新增巡视记录
     *
     * @param sdInspection 巡视记录
     * @return 结果
     */
    @Override
    public int insertSdInspection(SdInspection sdInspection) {
        sdInspection.setCreateTime(DateUtils.getNowDate());
        sdInspection.setCreateName(SecurityUtils.getUsername());
        if (null != sdInspection.getPlanFileId() && !"".equals(sdInspection.getPlanFileId())
                && sdInspection.getPlanFileId().contains(",")) {
            String fileIds = sdInspection.getPlanFileId();
            if (fileIds.endsWith(",")) {
                fileIds = fileIds.substring(0, fileIds.length() - 1);
            }
            String[] pFileIds = fileIds.split(",");
            String fileId = pFileIds[0];
            for (String s : pFileIds) {
                if (!s.equals(fileId)) {
                    sdInspectionFileMapper.updateFileId(fileId, s);
                }
            }
            sdInspection.setPlanFileId(fileId);
        }
        return sdInspectionMapper.insertSdInspection(sdInspection);
    }

    /**
     * 修改巡视记录
     *
     * @param sdInspection 巡视记录
     * @return 结果
     */
    @Override
    public int updateSdInspection(SdInspection sdInspection) {
        sdInspection.setUpdateTime(DateUtils.getNowDate());
        sdInspection.setUpdateName(SecurityUtils.getUsername());
        //如果关联ID不同，就重新定义关联ID
        SdInspection old = sdInspectionMapper.selectSdInspectionById(sdInspection.getInspectionId());
        String oldFileId = old.getPlanFileId();
        String newFileId = sdInspection.getPlanFileId();
        if (!oldFileId.equals(newFileId) && newFileId != null && !"".equals(newFileId)) {
            if (newFileId.endsWith(",")) {
                newFileId = newFileId.substring(0, newFileId.length() - 1);
            }
            SdInspectionFile sdInspectionFile = new SdInspectionFile();
            sdInspectionFile.setPlanFileId(oldFileId);
            List<SdInspectionFile> sdInspectionFiles = sdInspectionFileMapper.selectSdInspectionFileList(sdInspectionFile);
            String[] pFileIds = newFileId.split(",");
            String fileId = pFileIds[0];
            for (String s : pFileIds) {
                if (!s.equals(fileId)) {
                    sdInspectionFileMapper.updateFileId(fileId, s);
                }
            }
            if (sdInspectionFiles.size() > 0) {
                for (int i = 0; i < sdInspectionFiles.size(); i++) {
                    SdInspectionFile inspectionFile = sdInspectionFiles.get(i);
                    sdInspectionFileMapper.updateFileId(fileId, inspectionFile.getPlanFileId());
                }
            }
            sdInspection.setPlanFileId(fileId);
        }
        return sdInspectionMapper.updateSdInspection(sdInspection);
    }

    @Override
    public int updateInspection(MultipartFile[] file, SdInspection sdInspection, Long[] ids) {
        int result = 0;
        List<SdInspectionFile> list = new ArrayList<>();
        try {
            sdInspection.setUpdateTime(DateUtils.getNowDate());
            sdInspection.setUpdateBy(SecurityUtils.getUsername());
            String guid = sdInspection.getPlanFileId();
            sdInspection.setPlanFileId(guid);
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
                    SdInspectionFile planFile = new SdInspectionFile();
                    planFile.setPlanFileId(guid);
                    planFile.setUrl(fileServerPath + "/" + fileName);
                    planFile.setFileName(fileName);
                    planFile.setCreateBy(SecurityUtils.getUsername());
                    planFile.setCreateTime(DateUtils.getNowDate());
                    list.add(planFile);
                    if (!filepath.exists()) {
                        filepath.mkdirs();
                    }
                    file[i].transferTo(dir);
                }
                result = sdInspectionFileMapper.brachInsertSdInspectionFile(list);
            } else {
                logger.info("当前文件信息为空或文件没有发生改动");
            }
            if (ids.length > 0) {
                result = sdInspectionFileMapper.deleteSdInspectionFileByIds(ids);//ids 为要删除的 file id数组
            }
            if (result >= 0) {
                result = sdInspectionMapper.updateSdInspection(sdInspection);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }

    /**
     * 批量删除巡视记录
     *
     * @param inspectionIds 需要删除的巡视记录ID
     * @return 结果
     */
    @Override
    public int deleteSdInspectionByIds(Long[] inspectionIds) {
        for (Long l : inspectionIds) {
            SdInspection management = sdInspectionMapper.selectSdInspectionById(l);
            if (management.getPlanFileId() != null && !"".equals(management.getPlanFileId())) {
                String fileId = management.getPlanFileId();
                sdInspectionFileMapper.deleteFileByFileIds(fileId);
            }
        }
        return sdInspectionMapper.deleteSdInspectionByIds(inspectionIds);
    }

    /**
     * 删除巡视记录信息
     *
     * @param inspectionId 巡视记录ID
     * @return 结果
     */
    @Override
    public int deleteSdInspectionById(Long inspectionId) {
        return sdInspectionMapper.deleteSdInspectionById(inspectionId);
    }

    @Override
    public int addInspection(MultipartFile[] file, SdInspection sdInspection) {
        int result = -1;
        try {
            //创建时间
            sdInspection.setCreateTime(DateUtils.getNowDate());
            //设置当前创建人
            sdInspection.setCreateBy(SecurityUtils.getUsername());
            //生成guid
            if (file.length > 0 && file != null) {
                List<SdInspectionFile> list = new ArrayList<>();
                String guid = UUIDUtil.getRandom32BeginTimePK();
                sdInspection.setPlanFileId(guid);
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
                    SdInspectionFile planFile = new SdInspectionFile();
                    planFile.setPlanFileId(guid);
                    planFile.setUrl(fileServerPath + "/" + fileName);
                    planFile.setFileName(fileName);
                    planFile.setCreateBy(SecurityUtils.getUsername());
                    planFile.setCreateTime(DateUtils.getNowDate());
                    list.add(planFile);
                    if (!filepath.exists()) {
                        filepath.mkdirs();
                    }
                    file[i].transferTo(dir);
                }
                result = sdInspectionMapper.insertSdInspection(sdInspection);
                if (result > -1) {
                    result = sdInspectionFileMapper.brachInsertSdInspectionFile(list);
                }
            } else {
                //文件关联ID
                sdInspection.setPlanFileId(null);
                result = sdInspectionMapper.insertSdInspection(sdInspection);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }

    @Override
    public String uploadPicture(MultipartFile[] file) throws IOException {
        if (file != null && file.length > 0) {
            List<SdInspectionFile> list = new ArrayList<SdInspectionFile>();
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
                SdInspectionFile planFile = new SdInspectionFile();
                planFile.setPlanFileId(guid);
                planFile.setUrl(fileServerPath + "/" + fileName);
                planFile.setFileName(fileName);
                planFile.setCreateBy(SecurityUtils.getUsername());
                planFile.setCreateTime(DateUtils.getNowDate());
                list.add(planFile);
                for (int j = 0; j < list.size(); j++) {
                    SdInspectionFile sdInspectionFile = list.get(j);
                    System.err.println(sdInspectionFile);
                }
                if (!filepath.exists()) {
                    filepath.mkdirs();
                } else {
                }
                file[i].transferTo(dir);
                if (list.size() > -1) {
                    sdInspectionFileMapper.brachInsertSdInspectionFile(list);
                    return guid;
                } else {
                    throw new RuntimeException("图片上传失败！请重试！");
                }
            }
        }
        return "图片文件信息为空";
    }
}
