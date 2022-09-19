package com.tunnel.business.service.dataInfo;

import com.tunnel.business.domain.dataInfo.SdInspection;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 巡视记录Service接口
 *
 * @author liubaokui
 * @date 2021-05-12
 */
public interface ISdInspectionService {
    /**
     * 查询巡视记录
     *
     * @param inspectionId 巡视记录ID
     * @return 巡视记录
     */
    SdInspection selectSdInspectionById(Long inspectionId) throws IOException;

    /**
     * 查询巡视记录列表
     *
     * @param sdInspection 巡视记录
     * @return 巡视记录集合
     */
    List<SdInspection> selectSdInspectionList(SdInspection sdInspection);

    /**
     * 新增巡视记录
     *
     * @param sdInspection 巡视记录
     * @return 结果
     */
    int insertSdInspection(SdInspection sdInspection);

    /**
     * 修改巡视记录
     *
     * @param sdInspection 巡视记录
     * @return 结果
     */
    int updateSdInspection(SdInspection sdInspection);

    int updateInspection(MultipartFile[] file, SdInspection sdInspection, Long[] ids);

    /**
     * 批量删除巡视记录
     *
     * @param inspectionIds 需要删除的巡视记录ID
     * @return 结果
     */
    int deleteSdInspectionByIds(Long[] inspectionIds);

    /**
     * 删除巡视记录信息
     *
     * @param inspectionId 巡视记录ID
     * @return 结果
     */
    int deleteSdInspectionById(Long inspectionId);

    int addInspection(MultipartFile[] file, SdInspection sdInspection);

    String uploadPicture(MultipartFile[] file) throws IOException;
}
